package com.scfs.service.export;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.export.RefundApplyDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.export.dto.req.RefundApplyLineReqDto;
import com.scfs.domain.export.dto.req.RefundApplySearchReqDto;
import com.scfs.domain.export.dto.resp.RefundApplyLineResDto;
import com.scfs.domain.export.dto.resp.RefundApplyResDto;
import com.scfs.domain.export.entity.RefundApply;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.RefundApplyAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 *  
 *  File: RefundApplyService.java
 *  Description:出口退税申请
 *  TODO
 *  Date,					Who,				
 *  2016年12月06日				Administrator
 *
 * </pre>
 */
@Service
public class RefundApplyService {
	@Autowired
	private RefundApplyDao refundApplyDao;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private RefundApplyLineService refundApplyLineService;// 退税明细
	@Autowired
	private RefundApplyAuditService refundApplyAuditService;// 审核
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private AuditFlowService auditFlowService;

	/***
	 * 添加数据
	 * 
	 * @param refundApply
	 * @return
	 */
	public int createRefundApply(RefundApply refundApply) {
		Date date = new Date();
		BigDecimal zero = BigDecimal.ZERO;
		refundApply.setState(BaseConsts.ONE);
		refundApply.setRefundApplyNum(zero);
		refundApply.setRefundApplyAmount(zero);
		refundApply.setRefundApplyTax(zero);
		refundApply.setCreateAt(date);
		refundApply.setCreator(ServiceSupport.getUser().getChineseName());
		refundApply.setCreatorId(ServiceSupport.getUser().getId());
		refundApply.setIsDelete(BaseConsts.ZERO);
		refundApply.setRefundApplyNo(sequenceService.getNumDateByBusName(BaseConsts.REFUND_APPLY_NO,
				SeqConsts.S_REFUND_APPLY_NO, BaseConsts.INT_13));
		refundApply.setState(BaseConsts.ONE);
		int id = refundApplyDao.insert(refundApply);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(refundApply));
		}
		return refundApply.getId();
	}

	/**
	 * 修改数据
	 * 
	 * @param refundApply
	 * @return
	 */
	public BaseResult updateRefundApplyById(RefundApply refundApply) {
		BaseResult baseResult = new BaseResult();
		if (refundApply.getState() != null && refundApply.getState() == BaseConsts.THREE) {// 完成状态修改报关单进度
			refundApplyLineService.updateCustomsApplyByRefundId(refundApply.getId());
		}
		int result = refundApplyDao.updateById(refundApply);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新退税申请失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 提交
	 * 
	 * @param refundApply
	 * @return
	 */
	public BaseResult sumitRefundApplyById(RefundApply refundApply) {
		BaseResult baseResult = new BaseResult();
		RefundApply entity = refundApplyDao.queryEntityById(refundApply.getId());// 锁表

		List<RefundApplyLineResDto> lineList = refundApplyLineService.queryResultsByRefundId(entity.getId());
		if (lineList != null && lineList.size() > BaseConsts.ZERO) {
			AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_12, null);
			if (null == startAuditNode) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
			}
			refundApply.setState(startAuditNode.getAuditNodeState());
			refundApplyDao.updateById(refundApply);
			refundApplyAuditService.startAudit(entity, startAuditNode);
		} else {
			baseResult.setMsg("无退税明细，不能提交！");
		}
		return baseResult;
	}

	/**
	 * 删除数据
	 * 
	 * @param refundApply
	 * @return
	 */
	public BaseResult deleteRefundApplyById(RefundApply refundApply) {
		BaseResult baseResult = new BaseResult();
		RefundApply entity = refundApplyDao.queryEntityById(refundApply.getId());// 锁表

		List<RefundApplyLineResDto> lineList = refundApplyLineService.queryResultsByRefundId(entity.getId());
		if (lineList != null && lineList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			RefundApplyLineReqDto reqDto = new RefundApplyLineReqDto();
			for (RefundApplyLineResDto line : lineList) {
				ids.add(line.getId());
			}
			reqDto.setIds(ids);
			refundApplyLineService.deleteRefundApplyLineById(reqDto);
		}
		refundApply.setDeleteAt(new Date());
		refundApply.setDeleter(ServiceSupport.getUser().getChineseName());
		refundApply.setDeleterId(ServiceSupport.getUser().getId());
		refundApply.setIsDelete(BaseConsts.ONE);
		refundApplyDao.updateById(refundApply);
		return baseResult;
	}

	/**
	 * 编辑
	 * 
	 * @param refundApply
	 * @return
	 */
	public Result<RefundApplyResDto> editRefundApplyById(RefundApply refundApply) {
		Result<RefundApplyResDto> result = new Result<RefundApplyResDto>();
		RefundApplyResDto refundApplyResDto = convertToRefundApplyResDto(
				refundApplyDao.queryEntityById(refundApply.getId()));
		result.setItems(refundApplyResDto);
		return result;
	}

	/**
	 * 通过id获取详情
	 * 
	 * @param id
	 * @return
	 */
	public RefundApply queryEntityById(Integer id) {
		return refundApplyDao.queryEntityById(id);
	}

	/**
	 * 通过id获取详情
	 * 
	 * @param id
	 * @return
	 */
	public RefundApplyResDto queryEntityResDtoById(Integer id) {
		return convertToRefundApplyResDto(refundApplyDao.queryEntityById(id));
	}

	/**
	 * 获取所有导出数据
	 * 
	 * @param applySearchReqDto
	 * @return
	 */
	public List<RefundApplyResDto> queryRefundApplyResultsByExe(RefundApplySearchReqDto applySearchReqDto) {
		applySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		return convertToRefundApplyResDtos(refundApplyDao.queryResultsByCon(applySearchReqDto));
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param queryInvoiceReqDto
	 * @return
	 */
	public boolean isOverasyncMaxLine(RefundApplySearchReqDto applySearchReqDto) {
		applySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = refundApplyDao.isOverasyncMaxLine(applySearchReqDto);
		//
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("付款单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/apply/refund/refund_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_12);
			asyncExcelService.addAsyncExcel(applySearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceApplyExport(RefundApplySearchReqDto applySearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<RefundApplyResDto> refundList = queryRefundApplyResultsByExe(applySearchReqDto);
		model.put("refundList", refundList);
		return model;
	}

	/**
	 * 获取列表数据
	 * 
	 * @param applySearchReqDto
	 * @return
	 */
	public PageResult<RefundApplyResDto> queryRefundApplyResultsByCon(RefundApplySearchReqDto applySearchReqDto) {
		PageResult<RefundApplyResDto> pageResult = new PageResult<RefundApplyResDto>();
		int offSet = PageUtil.getOffSet(applySearchReqDto.getPage(), applySearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, applySearchReqDto.getPer_page());
		applySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<RefundApplyResDto> refundApplyResDto = convertToRefundApplyResDtos(
				refundApplyDao.queryResultsByCon(applySearchReqDto, rowBounds));

		if (applySearchReqDto.getNeedSum() != null && applySearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<RefundApply> sumResDto = refundApplyDao.sumRefundApply(applySearchReqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal refundApplyNumSum = BigDecimal.ZERO;
				BigDecimal refundApplyAmountSum = BigDecimal.ZERO;
				BigDecimal refundApplyTaxSum = BigDecimal.ZERO;
				for (RefundApply refundApply : sumResDto) {
					if (refundApply != null) {
						refundApplyNumSum = DecimalUtil.add(refundApplyNumSum, refundApply.getRefundApplyNum() == null
								? DecimalUtil.ZERO : refundApply.getRefundApplyNum());
						refundApplyAmountSum = DecimalUtil.add(refundApplyAmountSum,
								refundApply.getRefundApplyAmount() == null ? DecimalUtil.ZERO
										: refundApply.getRefundApplyAmount());
						refundApplyTaxSum = DecimalUtil.add(refundApplyTaxSum, refundApply.getRefundApplyTax() == null
								? DecimalUtil.ZERO : refundApply.getRefundApplyTax());
					}
				}
				String totalStr = "退税数量  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(refundApplyNumSum))
						+ "      退税含税金额: " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(refundApplyAmountSum))
						+ " CNY   可退税额: " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(refundApplyTaxSum))
						+ " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}

		pageResult.setItems(refundApplyResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), applySearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(applySearchReqDto.getPage());
		pageResult.setPer_page(applySearchReqDto.getPer_page());
		return pageResult;
	}

	public List<RefundApplyResDto> convertToRefundApplyResDtos(List<RefundApply> result) {
		List<RefundApplyResDto> refundApplyResDto = new ArrayList<RefundApplyResDto>();
		if (ListUtil.isEmpty(result)) {
			return refundApplyResDto;
		}
		for (RefundApply refundApply : result) {
			RefundApplyResDto refundesDto = convertToRefundApplyResDto(refundApply);
			List<CodeValue> operList = getOperList(refundesDto.getState());
			refundesDto.setOpertaList(operList);
			refundApplyResDto.add(refundesDto);
		}
		return refundApplyResDto;
	}

	public RefundApplyResDto convertToRefundApplyResDto(RefundApply module) {
		RefundApplyResDto resDto = new RefundApplyResDto();
		resDto.setId(module.getId());
		resDto.setRefundApplyNo(module.getRefundApplyNo());
		resDto.setRefundAttachNo(module.getRefundAttachNo());
		// 项目
		resDto.setProjectId(module.getProjectId());
		resDto.setProjectName(cacheService.getProjectNameById(module.getProjectId()));

		resDto.setSystemTime(new Date());
		BaseProject baseProject = cacheService.getProjectById(module.getProjectId());
		if (null != baseProject) {
			BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
			if (null != busiUnit) {
				resDto.setBusinessUnitNameValue(busiUnit.getChineseName());
				resDto.setBusinessUnitAddress(busiUnit.getOfficeAddress());
			}
		}

		resDto.setCustId(module.getCustId());
		resDto.setCusName(cacheService.getSubjectNcByIdAndKey(module.getCustId(), CacheKeyConsts.CUSTOMER));

		resDto.setRefundApplyNum(module.getRefundApplyNum());
		resDto.setRefundApplyAmount(module.getRefundApplyAmount());
		resDto.setRefundApplyTax(module.getRefundApplyTax());
		resDto.setRefundApplyDate(module.getRefundApplyDate());
		resDto.setVerifyAmount(module.getVerifyAmount());
		resDto.setVerifyDate(module.getVerifyDate());
		resDto.setVerify(module.getVerify());
		resDto.setState(module.getState());
		resDto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.REFUND_APPLY_STATE, module.getState() + ""));
		resDto.setRemark(module.getRemark());
		resDto.setCreator(module.getCreator());
		resDto.setCreateAt(module.getCreateAt());
		return resDto;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				RefundApplyResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		List<String> opertaList = Lists.newArrayList();
		if (state == null) {
			return opertaList;
		}
		switch (state) {
		// 状态 1:待提交 25:待财务审核 3:已完成
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DETAIL);
			break;
		default:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}

		return opertaList;
	}

	public void updatePrintNum(Integer id) {
		RefundApply refundApply = refundApplyDao.queryEntityById(id);
		RefundApply refund = new RefundApply();
		refund.setId(id);
		refund.setPrintNum(refundApply.getPrintNum() + 1);
		refundApplyDao.updatePrintNum(refund);
	}
}
