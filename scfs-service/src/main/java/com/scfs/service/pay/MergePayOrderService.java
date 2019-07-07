package com.scfs.service.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.pay.MergePayOrderDao;
import com.scfs.dao.pay.MergePayOrderRelDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.pay.dto.req.MergePayOrderRelSearchReqDto;
import com.scfs.domain.pay.dto.req.MergePayOrderSearchReqDto;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderRelResDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderResDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.entity.MergePayOrder;
import com.scfs.domain.pay.entity.MergePayOrderDetail;
import com.scfs.domain.pay.entity.MergePayOrderRel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.result.PageResult;
import com.scfs.service.audit.MergePayAuditService;
import com.scfs.service.audit.PayAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: MergePayOrderService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */
@Service
public class MergePayOrderService {
	@Autowired
	MergePayOrderDao mergePayOrderDao;
	@Autowired
	CacheService cacheService;
	@Autowired
	SequenceService sequenceService;
	@Autowired
	MergePayOrderRelDao mergePayOrderRelDao;
	@Autowired
	PayOrderDao payOrderDao;
	@Autowired
	PayService payService;
	@Autowired
	MergePayAuditService mergePayAuditService;
	@Autowired
	MergePayOrderRelService mergePayOrderRelService;
	@Autowired
	AuditFlowService auditFlowService;
	@Autowired
	private PayAuditService payAuditService;

	/**
	 * 新建付款信息
	 *
	 * @param payOrder
	 */
	public Integer createMergePayOrder(MergePayOrderDetail mergePayOrderDetail) {
		if (mergePayOrderDetail == null) {
			return null;
		}
		MergePayOrder mergePayOrder = mergePayOrderDetail.getMergePayOrder();
		if (mergePayOrder.getState() == null) {
			mergePayOrder.setState(BaseConsts.ZERO);
		}

		Date date = new Date();
		mergePayOrder.setCreateAt(date);
		mergePayOrder.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		mergePayOrder.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		mergePayOrder.setMergePayNo(sequenceService.getNumDateByBusName(BaseConsts.MERGE_PAY_ORDER_NO,
				SeqConsts.S_PAYORDER_NO, BaseConsts.INT_13));
		int id = mergePayOrderDao.insert(mergePayOrder);

		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(mergePayOrder));
		}
		MergePayOrderRelSearchReqDto req = new MergePayOrderRelSearchReqDto();
		req.setMergePayId(mergePayOrder.getId());
		req.setIds(mergePayOrderDetail.getIds());
		mergePayOrderRelService.createMergePayOrderRel(req);
		return mergePayOrder.getId();
	}

	/**
	 * 更新付款信息
	 *
	 * @param payOrder
	 * @return
	 */
	public void updatePayOrderById(MergePayOrder mergePayOrder) {
		MergePayOrder entity = mergePayOrderDao.queryEntityById(mergePayOrder.getId());
		if (entity == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, MergePayOrderDao.class, mergePayOrder.getId());
		}
		mergePayOrderDao.updateById(mergePayOrder);
	}

	/**
	 * 编辑付款信息
	 *
	 * @param payOrder
	 * @return
	 */
	public MergePayOrderResDto editPayOrderById(Integer id) {
		MergePayOrder mergePayOrder = mergePayOrderDao.queryEntityById(id);
		MergePayOrderResDto data = convertToPayOrderResDto(mergePayOrder);
		return data;
	}

	/**
	 * 浏览付款信息
	 *
	 * @param payOrder
	 * @return
	 */
	public MergePayOrderResDto detailPayOrderById(Integer id) {
		MergePayOrder mergePayOrder = mergePayOrderDao.queryEntityById(id);
		MergePayOrderResDto data = convertToPayOrderResDto(mergePayOrder);
		return data;
	}

	public void deleteById(Integer id) {
		MergePayOrder entity = mergePayOrderDao.queryEntityById(id);
		if (entity == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, MergePayOrderDao.class, id);
		}
		if (BaseConsts.ZERO != entity.getState()) {
			throw new BaseException(ExcMsgEnum.DELETE_ERROR);
		}
		List<MergePayOrderRel> mergePayOrderRels = mergePayOrderRelDao.queryResultsByMergeId(id);
		if (!CollectionUtils.isEmpty(mergePayOrderRels)) {
			MergePayOrderRelSearchReqDto req = new MergePayOrderRelSearchReqDto();
			req.setMergePayId(id);
			List<Integer> ids = new ArrayList<Integer>();
			for (MergePayOrderRel mergePayOrderRel : mergePayOrderRels) {
				ids.add(mergePayOrderRel.getId());
			}
			req.setIds(ids);
			mergePayOrderRelService.deleteMergeRelById(req);
		}
		mergePayOrderDao.deleteById(id);
	}

	public void batchMergePaySubmitById(MergePayOrderSearchReqDto reqDto) {
		List<Integer> ids = reqDto.getIds();
		if (CollectionUtils.isNotEmpty(ids)) {
			for (Integer id : ids) {
				submitById(id);
			}
		}
	}

	public void submitById(Integer id) {
		MergePayOrder entity = mergePayOrderDao.queryEntityById(id);
		if (entity == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, MergePayOrderDao.class, id);
		}
		if (BaseConsts.ZERO != entity.getState() && BaseConsts.THREE != entity.getState()) {
			throw new BaseException(ExcMsgEnum.SUBMIT_ERROR);
		}
		if (DecimalUtil.eq(DecimalUtil.ZERO, entity.getPayAmount())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款金额为0不能提交");
		}
		Integer state = entity.getState();
		MergePayOrder mergePayOrderUpd = new MergePayOrder();
		mergePayOrderUpd.setId(id);

		List<MergePayOrderRel> mergePayOrderRels = mergePayOrderRelDao.queryResultsByMergeId(id);
		BaseProject baseProject = cacheService.getProjectById(entity.getProjectId());

		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("mergePayOrderId", id);
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_14, baseProject.getId(), paramMap);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		if (startAuditNode.getAuditNodeState().equals(BaseConsts.TEN)) { // 10-商务审核
			startAuditNode = auditFlowService.getNextAuditNode(BaseConsts.TEN, BaseConsts.INT_14, baseProject.getId(), paramMap);
		}
		mergePayOrderUpd.setState(startAuditNode.getAuditNodeState());

		Integer payWayType = BaseConsts.ZERO;
		if (!CollectionUtils.isEmpty(mergePayOrderRels)) {
			for (MergePayOrderRel mergePayOrderRel : mergePayOrderRels) {
				PayOrder payOrder = payOrderDao.queryEntityById(mergePayOrderRel.getPayId());
				payService.checkSubmit(payOrder); // 付款单提交校验
				PayOrder payOrderUpd = new PayOrder();
				payOrderUpd.setId(payOrder.getId());
				if (payOrder.getPayWayType().equals(BaseConsts.ONE) && baseProject.getBizType().equals(BaseConsts.SIX)
						&& state != BaseConsts.THREE) { // 预付款付款单,融通铺货项目,并且不是供应商确认提交
					payOrderUpd.setState(BaseConsts.THREE);
				} else {
					payOrderUpd.setState(mergePayOrderUpd.getState());
				}
				payOrderDao.updateById(payOrderUpd);
				payWayType = payOrder.getPayWayType();
			}
		}
		if (payWayType.equals(BaseConsts.ONE) && baseProject.getBizType().equals(BaseConsts.SIX)
				&& state != BaseConsts.THREE) { // 预付款付款单,融通铺货项目,并且不是供应商确认提交
			mergePayOrderUpd.setState(BaseConsts.THREE);
			mergePayOrderDao.updateById(mergePayOrderUpd);
			entity = mergePayOrderDao.queryEntityById(id);
			payAuditService.sendWechatMsgByProjectMerge(entity);
			mergePayAuditService.startAudit(entity, startAuditNode, BaseConsts.INT_99);// 提交审核
		} else {
			mergePayOrderDao.updateById(mergePayOrderUpd);
			entity = mergePayOrderDao.queryEntityById(id);
			mergePayAuditService.startAudit(entity, startAuditNode, state);// 提交审核
		}
	}

	public void rejectPayOrderById(ProjectItemReqDto poAuditReqDto) {
		mergePayAuditService.unPassAudit(poAuditReqDto);
	}

	public void updateMergePayOrderState(Integer id, Integer state) {
		MergePayOrder entity = mergePayOrderDao.queryEntityById(id);
		if (entity == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, MergePayOrderDao.class, id);
		}

		MergePayOrder mergePayOrderUpd = new MergePayOrder();
		mergePayOrderUpd.setId(id);
		mergePayOrderUpd.setState(state);
		mergePayOrderDao.updateById(mergePayOrderUpd);

		List<MergePayOrderRel> mergePayOrderRels = mergePayOrderRelDao.queryResultsByMergeId(id);
		if (!CollectionUtils.isEmpty(mergePayOrderRels)) {
			for (MergePayOrderRel mergePayOrderRel : mergePayOrderRels) {
				PayOrder payOrder = payOrderDao.queryEntityById(mergePayOrderRel.getPayId());
				if (BaseConsts.SIX == state) {
					if (BaseConsts.TWO == payOrder.getPayWay()) {
						state = BaseConsts.FIVE;
					} else {
						state = BaseConsts.FOUR;
					}
				}
				PayOrder payOrderUpd = new PayOrder();
				payOrderUpd.setId(payOrder.getId());
				payOrderUpd.setState(state);
				payOrderDao.updateById(payOrderUpd);
			}
		}
	}

	/**
	 * 根据合并付款id查询可合并的付款列表
	 * 
	 * @param payOrderSearchReqDto
	 * @return
	 */
	public PageResult<PayOrderResDto> dividPayOrderByMergeId(MergePayOrderSearchReqDto mergePayOrderSearchReqDto) {
		MergePayOrder mergePayOrder = mergePayOrderDao.queryEntityById(mergePayOrderSearchReqDto.getId());
		PayOrderSearchReqDto payOrderSearchReqDto = new PayOrderSearchReqDto();
		payOrderSearchReqDto.setProjectId(mergePayOrder.getProjectId());
		payOrderSearchReqDto.setBusiUnit(mergePayOrder.getBusiUnit());
		payOrderSearchReqDto.setPayee(mergePayOrder.getPayee());
		payOrderSearchReqDto.setPayType(mergePayOrder.getPayType());
		payOrderSearchReqDto.setPayWay(mergePayOrder.getPayWay());
		payOrderSearchReqDto.setPayer(mergePayOrder.getPayer());
		payOrderSearchReqDto.setPayAccountId(mergePayOrder.getPayAccountId());
		payOrderSearchReqDto.setCurrencyType(mergePayOrder.getCurrencyType());
		payOrderSearchReqDto.setCanMerge(BaseConsts.ONE);
		payOrderSearchReqDto.setState(BaseConsts.ZERO);
		payOrderSearchReqDto.setPage(mergePayOrderSearchReqDto.getPage());
		payOrderSearchReqDto.setPer_page(mergePayOrderSearchReqDto.getPer_page());
		PageResult<PayOrderResDto> pageResult = payService.queryPayOrderResultsByCon(payOrderSearchReqDto);

		return pageResult;
	}

	public PageResult<MergePayOrderResDto> queryResultsByCon(MergePayOrderSearchReqDto req) {
		PageResult<MergePayOrderResDto> pageResult = new PageResult<MergePayOrderResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		if (!StringUtils.isEmpty(req.getMergePayNo())) {
			req.setMergePayNoList(Arrays.asList((req.getMergePayNo().split(","))));
		}
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.OVER_PAY_ORDER)
				|| !ServiceSupport.isAllowPerm(BusUrlConsts.OVER_PAY_ORDER_OPEN)) {// 判断用户是否拥有操作付款确认及承兑开立权限
			req.setUserId(ServiceSupport.getUser().getId());
			List<Integer> subjectList = new ArrayList<Integer>();
			subjectList.add(BaseConsts.ONE);
			subjectList.add(BaseConsts.FIVE);
			subjectList.add(BaseConsts.NINE);
			subjectList.add(BaseConsts.INT_13);
			req.setSubjectList(subjectList);
		}
		List<MergePayOrder> mergePayOrders = mergePayOrderDao.queryResultsByCon(req, rowBounds);
		if (req.getNeedSum() != null && req.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<MergePayOrder> sumResDto = mergePayOrderDao.sumMergePayOrder(req);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal payAmountSum = BigDecimal.ZERO;
				for (MergePayOrder mergePayOrder : sumResDto) {
					if (mergePayOrder != null) {
						payAmountSum = DecimalUtil.add(payAmountSum,
								ServiceSupport.amountNewToRMB(
										mergePayOrder.getPayAmount() == null ? DecimalUtil.ZERO
												: mergePayOrder.getPayAmount(),
										mergePayOrder.getCurrencyType(), new Date()));
					}
				}
				String totalStr = "付款金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum))
						+ " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(convertToPayOrderResDtos(mergePayOrders));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	public List<MergePayOrderResDto> convertToPayOrderResDtos(List<MergePayOrder> mergePayOrders) {
		List<MergePayOrderResDto> mergePayOrderResDtos = new ArrayList<MergePayOrderResDto>();
		for (MergePayOrder mergePayOrder : mergePayOrders) {
			MergePayOrderResDto mergePayOrderResDto = convertToPayOrderResDto(mergePayOrder);
			List<CodeValue> operList = getOperList(mergePayOrder);
			mergePayOrderResDto.setOpertaList(operList);
			mergePayOrderResDtos.add(mergePayOrderResDto);
		}
		return mergePayOrderResDtos;
	}

	/**
	 * 获取操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(MergePayOrder mergePayOrder) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(mergePayOrder);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PayOrderResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(MergePayOrder mergePayOrder) {
		List<String> opertaList = Lists.newArrayList();
		if (mergePayOrder.getState() == null) {
			return opertaList;
		}
		switch (mergePayOrder.getState()) {
		// 状态 0 待提交 25待财务专员审核 30待财务主管审核 40待风控审核 80待部门主管审核 90待总经理审核 4待确认 5待开立
		// 6已完成 改状态常量与【tb_pay_order【state】】一致
		case BaseConsts.ZERO:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.SUBMIT);
			break;
		case BaseConsts.SIX:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		default:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}

		return opertaList;
	}

	public MergePayOrderResDto convertToPayOrderResDto(MergePayOrder payOrder) {
		MergePayOrderResDto payOrderResDto = new MergePayOrderResDto();
		BeanUtils.copyProperties(payOrder, payOrderResDto);
		if (null != payOrder) {
			// 经营单位
			payOrderResDto.setBusiUnit(
					cacheService.showSubjectNameByIdAndKey(payOrder.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
			// 项目
			payOrderResDto.setProjectName(cacheService.showProjectNameById(payOrder.getProjectId()));
			payOrderResDto.setSystemTime(new Date());
			BaseProject baseProject = cacheService.getProjectById(payOrder.getProjectId());
			if (null != baseProject) {
				payOrderResDto.setBizType(baseProject.getBizType());
				payOrderResDto.setBizTypeName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE, baseProject.getBizType() + ""));
				BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
				if (null != busiUnit) {
					payOrderResDto.setBusinessUnitNameValue(busiUnit.getChineseName());
					payOrderResDto.setBusinessUnitAddress(busiUnit.getOfficeAddress());
				}
			}
			// 付款人
			payOrderResDto.setPayerName(
					cacheService.showSubjectNameByIdAndKey(payOrder.getPayer(), CacheKeyConsts.BUSI_UNIT));
			// 付款类型
			payOrderResDto.setPayTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_TYPE, payOrder.getPayType() + ""));
			// 付款方式
			payOrderResDto.setPayWayName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_WAY, payOrder.getPayWay() + ""));
			payOrderResDto.setPayAmount(payOrder.getPayAmount());

			// 收款单位
			payOrderResDto
					.setPayeeName(cacheService.getSubjectNcByIdAndKey(payOrder.getPayee(), CacheKeyConsts.CUSTOMER));
			payOrderResDto.setPayee(payOrder.getPayee());
			// 收款账户id
			payOrderResDto.setPayAccountId(payOrder.getPayAccountId());
			payOrderResDto.setRequestPayTime(payOrder.getRequestPayTime());
			payOrderResDto.setState(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_STATE, payOrder.getState() + ""));
			payOrderResDto.setStateInt(payOrder.getState());
			if (payOrder.getPayAccountId() != null) {
				BaseAccount baseAccount = cacheService.getAccountById(payOrder.getPayAccountId());
				if (baseAccount != null) {
					payOrderResDto.setBankName(baseAccount.getBankName());
					payOrderResDto.setSubjectName(baseAccount.getAccountor());
					payOrderResDto.setBankAddress(baseAccount.getBankAddress());
					payOrderResDto.setAccountNo(baseAccount.getAccountNo());
					payOrderResDto.setBankCode(baseAccount.getBankCode());
					payOrderResDto.setPhoneNumber(baseAccount.getPhoneNumber());
					payOrderResDto.setDefaultCurrency(ServiceSupport
							.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, payOrder.getCurrencyType() + ""));
					payOrderResDto.setIban(baseAccount.getIban());
				}
			}

			payOrderResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					payOrder.getCurrencyType() + ""));
			payOrderResDto.setPrintNum(payOrder.getPrintNum());
			payOrderResDto.setUnionPrintIdentifier(payOrder.getUnionPrintIdentifier());
			MergePayOrder sumOrder = mergePayOrderDao.querySumById(payOrder.getId());
			BigDecimal advanceAmount = sumOrder == null ? DecimalUtil.ZERO : sumOrder.getAdvanceAmount();
			payOrderResDto.setAdvanceAmount(advanceAmount);
			if (payOrderResDto.getPayAmount() != null
					&& !DecimalUtil.eq(DecimalUtil.ZERO, payOrderResDto.getPayAmount())) {
				BigDecimal payAdvanceAmount = DecimalUtil.subtract(payOrder.getPayAmount(), advanceAmount);
				MergePayOrderRelSearchReqDto req = new MergePayOrderRelSearchReqDto();
				req.setMergePayId(payOrder.getId());
				List<MergePayOrderRelResDto> mergePayOrderRelList = mergePayOrderRelService.queryListByMergeId(req);
				if (!CollectionUtils.isEmpty(mergePayOrderRelList)) {
					for (MergePayOrderRelResDto mergePayOrder : mergePayOrderRelList) {
						PayOrder payInfo = payOrderDao.queryEntityById(mergePayOrder.getPayId());
						if (payInfo.getPayWayType().equals(BaseConsts.TWO)) {// 付尾款时占用为0
							payAdvanceAmount = DecimalUtil.subtract(payAdvanceAmount, payInfo.getPayAmount());
						}
					}
				}
				payOrderResDto.setPayAdvanceAmount(payAdvanceAmount);
				payOrderResDto.setPayAdvanceAmountRate(payOrderResDto.getPayAdvanceAmount()
						.divide(payOrderResDto.getPayAmount(), 4, BigDecimal.ROUND_HALF_UP));
				payOrderResDto.setPayAdvanceAmountRateName(
						payOrderResDto.getPayAdvanceAmountRate().multiply(new BigDecimal("100")).setScale(2) + "%");

				payOrderResDto.setAdvanceAmountRate(
						advanceAmount.divide(payOrderResDto.getPayAmount(), 4, BigDecimal.ROUND_HALF_UP));
				payOrderResDto.setAdvanceAmountRateName(
						payOrderResDto.getAdvanceAmountRate().multiply(new BigDecimal("100")).setScale(2) + "%");
			}
			BigDecimal sumSendPrice = mergePayOrderDao.querySaleAmountById(payOrder.getId());
			payOrderResDto.setSumSendPrice(sumSendPrice);
			BigDecimal sumProfit = BigDecimal.ZERO;
			BigDecimal discountRate = BigDecimal.ZERO;
			if (sumSendPrice != null && DecimalUtil.gt(sumSendPrice, BigDecimal.ZERO)) {// 计算总利润率
				BigDecimal count = DecimalUtil.subtract(sumSendPrice, payOrder.getPayAmount());
				sumProfit = DecimalUtil.divide(count, sumSendPrice);

			}
			if (sumOrder != null && DecimalUtil.gt(sumOrder.getInDiscountAmount(), DecimalUtil.ZERO)) {
				discountRate = DecimalUtil.divide(sumOrder.getDiscountAmount(), sumOrder.getInDiscountAmount());
			}
			payOrderResDto.setSumProfit(DecimalUtil.toPercentString(sumProfit));
			payOrderResDto.setDiscountRateStr(DecimalUtil.toPercentString(discountRate));
		}
		return payOrderResDto;
	}

	public void updatePrintNum(Integer id) {
		MergePayOrder entity = mergePayOrderDao.queryEntityById(id);
		if (entity == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, MergePayOrderDao.class, id);
		}
		MergePayOrder mergePayOrderUpd = new MergePayOrder();
		mergePayOrderUpd.setId(id);
		mergePayOrderUpd.setPrintNum(entity.getPrintNum() + 1);
		mergePayOrderDao.updateById(mergePayOrderUpd);
		List<MergePayOrderRel> mergePayOrderRels = mergePayOrderRelDao.queryResultsByMergeId(id);
		if (!CollectionUtils.isEmpty(mergePayOrderRels)) {
			for (MergePayOrderRel mergePayOrderRel : mergePayOrderRels) {
				PayOrder payEntity = payOrderDao.queryEntityById(mergePayOrderRel.getPayId());
				PayOrder payOrderUpd = new PayOrder();
				payOrderUpd.setId(payEntity.getId());
				payOrderUpd.setPrintNum(payEntity.getPrintNum() + 1);
				payOrderDao.updateById(payOrderUpd);
			}
		}
	}

	public String prePrint(Integer id) {
		String unionPrintIdentifier = sequenceService.getNumIncByBusName("", SeqConsts.S_UNION_PRINT_IDENTIFIER,
				BaseConsts.SIX); // 生成统一打印标示符
		MergePayOrder mergePayOrderUpd = new MergePayOrder();
		mergePayOrderUpd.setId(id);
		mergePayOrderUpd.setUnionPrintIdentifier(unionPrintIdentifier);
		mergePayOrderDao.updateById(mergePayOrderUpd);

		List<MergePayOrderRel> mergePayOrderRels = mergePayOrderRelDao.queryResultsByMergeId(id);
		if (!CollectionUtils.isEmpty(mergePayOrderRels)) {
			for (MergePayOrderRel mergePayOrderRel : mergePayOrderRels) {
				PayOrder payOrderUpd = new PayOrder();
				payOrderUpd.setId(mergePayOrderRel.getPayId());
				payOrderUpd.setUnionPrintIdentifier(unionPrintIdentifier);
				payOrderDao.updateById(payOrderUpd);
			}
		}
		return unionPrintIdentifier;
	}

	/**
	 * 合并付款 明细业务删除
	 * 
	 * @param mergePayNo
	 */
	public void deleteMergePayOderById(PayOrder payResult) {
		// 根据合并付款单的id和付款单的ID进行业务删除
		MergePayOrder mergePayOrder = mergePayOrderDao.queryMergePayOrderByMerge(payResult.getMergePayNo());
		MergePayOrderRel mergePayOrderRel = new MergePayOrderRel();
		mergePayOrderRel.setMergePayId(mergePayOrder.getId());
		mergePayOrderRel.setPayId(payResult.getId());
		// 合并付款删除操作
		mergePayOrderRelDao.deleteMergePayOrdrById(mergePayOrderRel);
	}

	/**
	 * 支持单笔 驳回合并付款单改变合并金额
	 * 
	 * @param payResult
	 */
	public void updateMergeOrderAmount(PayOrder payResult) {
		// 根据合并付款单的单号进行查询和修改
		MergePayOrder mergePayOrder = mergePayOrderDao.queryMergePayOrderByMerge(payResult.getMergePayNo());
		BigDecimal payAmount = mergePayOrder.getPayAmount().subtract(payResult.getPayAmount());
		int changeType = payAmount.compareTo(BigDecimal.ZERO);
		if (changeType == BaseConsts.ZERO) {// 金额等于0的时候，要逻辑删除合并付款单的数据
			mergePayOrderDao.deleteById(mergePayOrder.getId());
		} else {
			mergePayOrder.setPayAmount(payAmount);
			mergePayOrderDao.updateById(mergePayOrder);
		}
	}

	public MergePayOrder queryEntityById(Integer id) {
		return mergePayOrderDao.queryEntityById(id);
	}

	public List<PayOrder> queryPayOrderByMergePayId(Integer id) {
		return payOrderDao.queryPayOrderByMergePayId(id);
	}
}
