package com.scfs.service.interf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.common.collect.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.interf.PMSSupplierBindDao;
import com.scfs.dao.interf.PmsPayOrderDtlDao;
import com.scfs.dao.interf.PmsPayOrderTitleDao;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.pay.PayPoRelationDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.api.pms.entity.PmsPayOrder;
import com.scfs.domain.api.pms.entity.PmsPayOrderDtl;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.dto.PmsPayOrderTitleResDto;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.result.PageResult;
import com.scfs.service.audit.AuditService;
import com.scfs.service.base.goods.BaseGoodsService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.logistics.BillInStoreService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.pay.PayPoRelationService;
import com.scfs.service.pay.PayService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.project.ProjectGoodsService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: PmsPayOrderTitleService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月19日			Administrator
 *
 * </pre>
 */
@Service
public class PmsPayOrderTitleService {
	@Autowired
	PmsPayOrderTitleDao pmsPayOrderTitleDao;

	@Autowired
	PmsPayOrderDtlDao pmsPayOrderDtlDao;

	@Autowired
	PMSSupplierBindDao pmsSupplierBindDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	BaseGoodsService baseGoodsService;

	@Autowired
	BaseGoodsDao baseGoodsDao;

	@Autowired
	ProjectGoodsService projectGoodsService;

	@Autowired
	PurchaseOrderService purchaseOrderService;

	@Autowired
	PayService payService;

	@Autowired
	BillInStoreService billInStoreService;

	@Autowired
	BillInStoreDao billInStoreDao;

	@Autowired
	BillDeliveryService billDeliveryService;

	@Autowired
	BillOutStoreService billOutStoreService;

	@Autowired
	BaseAccountDao baseAccountDao;

	@Autowired
	BaseSubjectDao baseSubjectDao;

	@Autowired
	ProjectItemService projectItemService;

	@Autowired
	ReceiveService receiveService;

	@Autowired
	StlDao stlDao;

	@Autowired
	PmsPayRpcService pmsPayRpcService;

	@Autowired
	PayPoRelationDao payPoRelationDao;

	@Autowired
	PurchaseOrderTitleDao purchaseOrderTitleDao;

	@Autowired
	PurchaseOrderLineDao purchaseOrderLineDao;

	@Autowired
	PayPoRelationService payPoRelationService;

	@Autowired
	AuditService auditService;

	@Autowired
	MsgContentService msgContentService;

	public void createPmsPayOrder(PmsPayOrder pmsPayOrder) {
		PmsPayOrderTitle pmsPayOrderTitle = pmsPayOrder.getOrderTitle();
		pmsPayOrderTitle.setState(BaseConsts.ONE); // 1待处理
		pmsPayOrderTitle.setCreateAt(new Date());
		if (pmsPayOrderTitle.getDeduction_money() == null) {
			pmsPayOrderTitle.setDeduction_money(BigDecimal.ZERO);
		}
		pmsPayOrderTitleDao.insert(pmsPayOrderTitle);

		List<PmsPayOrderDtl> list = pmsPayOrder.getOrderDtls();
		for (PmsPayOrderDtl item : list) {
			item.setPmsPayOrderTitleId(pmsPayOrderTitle.getId());
			item.setCreateAt(new Date());
			pmsPayOrderDtlDao.insert(item);
		}
	}

	public void dealPmsPayOrder(Integer pmsPayOrderTitleId) {
		PmsPayOrderTitle pmsPayOrderTitle = pmsPayOrderTitleDao.queryEntityById(pmsPayOrderTitleId);
		PMSSupplierBindReqDto reqDto = new PMSSupplierBindReqDto();
		reqDto.setSupplierNo(pmsPayOrderTitle.getSupplierNo());
		reqDto.setPmsSupplierNo(pmsPayOrderTitle.getVendorNo());
		reqDto.setStatus(BaseConsts.TWO);
		reqDto.setBizType(BaseConsts.TWO);// 应收保理
		List<PMSSupplierBind> pmsSupplierBinds = pmsSupplierBindDao.queryByProjectStatus(reqDto);
		if (CollectionUtils.isEmpty(pmsSupplierBinds)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "pms供应商绑定数据有误，请核查");
		}
		QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
		querySubjectReqDto.setBusinessUnitCode(pmsPayOrderTitle.getCorporation_code());
		querySubjectReqDto.setSubjectType(BaseConsts.ONE);
		List<BaseSubject> custs = baseSubjectDao.querySubjectByCond(querySubjectReqDto);
		if (CollectionUtils.isEmpty(custs)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"法人代码【" + pmsPayOrderTitle.getCorporation_code() + "】不存在");
		}
		if (custs.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"法人代码【" + pmsPayOrderTitle.getCorporation_code() + "】不唯一");
		}
		Integer projectId = pmsSupplierBinds.get(0).getProjectId();
		ProjectItem projectItem = projectItemService.getProjectItemByProjectId(projectId);
		if (StringUtils.isEmpty(projectItem)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请款单号[" + pmsPayOrderTitle.getPayNo() + "]下项目无条款");
		}
		if (StringUtils.isEmpty(projectItem.getSpreadfixedpoints())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "条款[" + projectItem.getItemNo() + "]固定价差点数不存在");
		}
		String supplierNo = pmsPayOrderTitle.getSupplierNo();
		BaseSubject supplier = cacheService.getSupplierByPidAndNo(projectId, supplierNo);
		if (supplier == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目" + projectId + "下供应商编号" + supplierNo + "不存在");
		}
		QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
		queryAccountReqDto.setId(supplier.getId());
		List<BaseAccount> baseAccounts = baseAccountDao.queryAccountBySubjectId(queryAccountReqDto);
		if (CollectionUtils.isEmpty(baseAccounts)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商[" + supplierNo + "]没有可用的账户");
		}
		List<PmsPayOrderDtl> pmsPayOrderDtls = pmsPayOrderDtlDao.queryResultsByTitleId(pmsPayOrderTitleId);
		if (!CollectionUtils.isEmpty(pmsPayOrderDtls)) {
			PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
			purchaseOrderTitle.setProjectId(projectId);
			purchaseOrderTitle.setSupplierId(supplier.getId());
			purchaseOrderTitle.setWarehouseId(BaseConsts.ONE); // 在途仓
			purchaseOrderTitle.setAppendNo(pmsPayOrderTitle.getPayNo());
			purchaseOrderTitle.setCurrencyId(pmsPayOrderTitle.getCurrencyType());
			purchaseOrderTitle.setOrderTime(pmsPayOrderTitle.getPayDate());
			purchaseOrderTitle.setIsRequestPay(BaseConsts.TWO); // 不需要付款
			purchaseOrderTitle.setCustomerId(custs.get(0).getId());
			purchaseOrderTitle.setOrderType(BaseConsts.ZERO); // 采购
			purchaseOrderTitle.setDuctionMoney(pmsPayOrderTitle.getDeduction_money());// 抵扣金额
			Integer poId = purchaseOrderService.addPurchaseOrderTitle(purchaseOrderTitle); // 添加采购订单头
			// 根据订单金额和抵扣金额算出抵扣率并且保留八位小数
			BigDecimal ductionRate = DecimalUtil
					.format(DecimalUtil.divide(pmsPayOrderTitle.getDeduction_money(), pmsPayOrderTitle.getPayMoney()));
			for (PmsPayOrderDtl item : pmsPayOrderDtls) {
				String goodsNo = item.getGoodsNo();
				BaseGoods baseGoodReq = new BaseGoods();
				baseGoodReq.setNumber(goodsNo);
				baseGoodReq.setGoodType(BaseConsts.ZERO);
				List<BaseGoods> baseGoods = baseGoodsDao.getGoodsList(baseGoodReq);
				Integer goodsId = null;
				if (CollectionUtils.isEmpty(baseGoods)) { // 商品不存在，生成商品并添加商品与项目之间的关系
					BaseGoods goodsAdd = new BaseGoods();
					goodsAdd.setNumber(goodsNo);
					goodsAdd.setName(item.getGoodsName());
					goodsAdd.setTaxRate(new BigDecimal(BaseConsts.DEFAULT_GOODS_TAX_RATE));
					goodsAdd.setCreator(BaseConsts.SYSTEM_ROLE_NAME);
					baseGoodsService.addBaseGoods(goodsAdd); // 添加商品
					baseGoodsService.submit(goodsAdd); // 提交商品

					ProjectGoods projectGoods = new ProjectGoods();
					projectGoods.setGoodsId(goodsAdd.getId());
					projectGoods.setProjectId(projectId);
					projectGoodsService.createProjectGoods(projectGoods); // 添加商品和项目关联关系

					goodsId = goodsAdd.getId();
				} else {
					goodsId = baseGoods.get(0).getId();

					ProjectGoods projectGoods = projectGoodsService.queryEntityByProjectAndGoods(projectId, goodsId);
					if (projectGoods == null) {
						ProjectGoods projectGoods_1 = new ProjectGoods();
						projectGoods_1.setGoodsId(goodsId);
						projectGoods_1.setProjectId(projectId);
						projectGoodsService.createProjectGoods(projectGoods_1); // 添加商品和项目关联关系
					}
				}
				PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
				purchaseOrderLine.setBatchNum(item.getPoNo());
				purchaseOrderLine.setGoodsId(goodsId);
				purchaseOrderLine.setPoId(poId);
				// purchaseOrderLine.setGoodsPrice(DecimalUtil.multiply(item.getInPrice(),
				// DecimalUtil.subtract(new BigDecimal("1"),
				// projectItem.getSpreadfixedpoints())));
				/** 算出抵扣率 总金额/抵扣金额 **/
				BigDecimal goodAmount = DecimalUtil.formatScale2(item.getInQty().multiply(item.getInPrice()));// 获取订单总金额
				// 抵扣金额
				BigDecimal deductioMoney = DecimalUtil.formatScale2(DecimalUtil.multiply(goodAmount, ductionRate));
				purchaseOrderLine.setDeductionMoney(deductioMoney);// 抵扣的抵扣金额
				// 抵扣单价
				purchaseOrderLine.setGoodsPrice(item.getInPrice());// 商品单价
				// 抵扣后的价格和扣点率的价格
				BigDecimal disCountAmount = DecimalUtil.formatScale2(DecimalUtil.subtract(goodAmount, deductioMoney))
						.multiply(projectItem.getSpreadfixedpoints());
				purchaseOrderLine.setDiscountAmount(disCountAmount);
				purchaseOrderLine.setGoodsNum(item.getInQty());
				purchaseOrderLine.setRequiredSendPrice(item.getInPrice());
				purchaseOrderService.addPoLine(poId, purchaseOrderLine);
			}
			purchaseOrderService.submitPurchaseOrderTitle(purchaseOrderTitle); // 自动提交

			PurchaseOrderTitle newPoTitle = purchaseOrderService.queryAndLockById(poId);
			Integer payId = purchaseOrderService.addPayOrderByPo(newPoTitle);// 生成付款单

			PayOrder poUpd = new PayOrder();
			poUpd.setId(payId);
			poUpd.setPayWay(BaseConsts.ONE); // 付款方式为转账
			poUpd.setPayAccountId(baseAccounts.get(0).getId());
			poUpd.setAttachedNumbe(pmsPayOrderTitle.getPayNo());
			poUpd.setRequestPayTime(new Date());
			poUpd.setInnerPayDate(pmsPayOrderTitle.getInnerPayDate());
			poUpd.setRemark(
					"PMS请款单日期" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, pmsPayOrderTitle.getPayDate()) + ","
							+ pmsPayOrderTitle.getCorporation_name());
			payService.updatePayOrderById(poUpd);
			// payService.submitPayOrderById(poUpd); //提交付款单，开始审核流程

			dealSuccess(pmsPayOrderTitleId, BaseConsts.FOUR, projectId);
		}
	}

	public void dealFail(Integer id, String errorMsg) {

		PmsPayOrderTitle entity = pmsPayOrderTitleDao.queryEntityById(id);
		PmsPayOrderTitle pmsPayOrderTitle = new PmsPayOrderTitle();
		pmsPayOrderTitle.setState(BaseConsts.THREE);
		pmsPayOrderTitle.setId(id);
		pmsPayOrderTitle.setMessage(errorMsg);
		pmsPayOrderTitleDao.updateById(pmsPayOrderTitle);

		PMSSupplierBindReqDto reqDto = new PMSSupplierBindReqDto();
		reqDto.setSupplierNo(entity.getSupplierNo());
		reqDto.setPmsSupplierNo(entity.getVendorNo());
		reqDto.setStatus(BaseConsts.TWO);
		reqDto.setBizType(BaseConsts.TWO);// 应收保理
		List<PMSSupplierBind> pmsSupplierBinds = pmsSupplierBindDao.queryByProjectStatus(reqDto);
		if (CollectionUtils.isEmpty(pmsSupplierBinds)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "pms供应商绑定数据有误，请核查");
		}
		Integer projectId = pmsSupplierBinds.get(0).getProjectId();
		// 通知商务rtx和邮件
		sendMailMsg(id, projectId);
		sendRTXMsg(id, projectId);
	}

	public void dealSuccess(Integer id, Integer state, Integer projectId) {
		pmsPayOrderTitleDao.queryEntityById(id);

		PmsPayOrderTitle pmsPayOrderTitle = new PmsPayOrderTitle();
		pmsPayOrderTitle.setId(id);
		pmsPayOrderTitle.setState(state);
		if (state.equals(BaseConsts.FOUR)) {
			pmsPayOrderTitle.setMessage("付款单待确认");
		} else if (state.equals(BaseConsts.FIVE)) {
			pmsPayOrderTitle.setMessage("处理成功，付款已确认");
		}
		pmsPayOrderTitleDao.updateById(pmsPayOrderTitle);
		// 通知商务rtx和邮件
		sendMailMsg(id, projectId);
		sendRTXMsg(id, projectId);
	}

	public void rejectPmsPayOrder(Integer pmsPayOrderTitleId, Integer payId) {
		PmsPayOrderTitle pmsPayOrderTitle = pmsPayOrderTitleDao.queryEntityById(pmsPayOrderTitleId);
		if (pmsPayOrderTitle == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PmsPayOrderTitleDao.class, pmsPayOrderTitleId);
		}
		// 1.更新请款单状态为已驳回
		PmsPayOrderTitle ppotUpd = new PmsPayOrderTitle();
		ppotUpd.setId(pmsPayOrderTitle.getId());
		ppotUpd.setState(BaseConsts.TWO);
		ppotUpd.setMessage("已驳回");
		pmsPayOrderTitleDao.updateById(ppotUpd);
		// 2.调用pms接口返回处理结果
		PayOrder payOrder = payService.queryEntityById(payId);
		pmsPayRpcService.unPassPayOrder(payOrder, pmsPayOrderTitle);
	}

	public void deletePmsPurchaseOrder(PmsPayOrderTitle pmsPayOrderTitle, PayOrder payOrder) {
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		poTitleReqDto.setAppendNo(pmsPayOrderTitle.getPayNo());
		poTitleReqDto.setProjectId(payOrder.getProjectId());
		poTitleReqDto.setSupplierId(payOrder.getPayee());
		List<PurchaseOrderTitle> purchaseOrderTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto);
		if (!CollectionUtils.isEmpty(purchaseOrderTitles)) {
			if (purchaseOrderTitles.size() > 1) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"pms请款单[" + pmsPayOrderTitle.getPayNo() + "]对应的采购订单不唯一");
			}
			Integer poId = purchaseOrderTitles.get(0).getId();
			List<PoLineModel> purchaseOrderLines = purchaseOrderLineDao.queryPoLineListByPoId(poId);
			if (!CollectionUtils.isEmpty(purchaseOrderLines)) {
				for (PoLineModel item : purchaseOrderLines) {
					purchaseOrderLineDao.deleteById(item.getId());
				}
			}
			purchaseOrderTitleDao.deleteById(poId);
		}
	}

	public void confirmPmsPayOrder(Integer pmsPayOrderTitleId, Integer payId) {
		PmsPayOrderTitle pmsPayOrderTitle = pmsPayOrderTitleDao.queryEntityById(pmsPayOrderTitleId);
		if (pmsPayOrderTitle == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PmsPayOrderTitleDao.class, pmsPayOrderTitleId);
		}
		if (!pmsPayOrderTitle.getState().equals(BaseConsts.FOUR)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款确认时，请款单状态有误，请核查");
		}
		PayOrder payOrder = payService.queryEntityById(payId);
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		poTitleReqDto.setProjectId(payOrder.getProjectId());
		poTitleReqDto.setSupplierId(payOrder.getPayee());
		poTitleReqDto.setAppendNo(pmsPayOrderTitle.getPayNo());
		List<PurchaseOrderTitle> purchaseOrderTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto);
		if (CollectionUtils.isEmpty(purchaseOrderTitles)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "附属编号[" + pmsPayOrderTitle.getPayNo() + "]找不到对应的采购订单");
		}
		if (purchaseOrderTitles.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "附属编号[" + pmsPayOrderTitle.getPayNo() + "]对应的采购订单不唯一");
		}
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitles.get(0);

		List<PoLineModel> poLineModels = purchaseOrderLineDao.queryPoLineListByPoId(purchaseOrderTitle.getId());
		if (CollectionUtils.isEmpty(poLineModels)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购订单[" + pmsPayOrderTitle.getPayNo() + "]找不到明细");
		}
		PurchaseOrderTitle potUpd = new PurchaseOrderTitle();
		potUpd.setId(purchaseOrderTitle.getId());
		potUpd.setPerdictTime(payOrder.getConfirmorAt());
		purchaseOrderTitleDao.updatePurchaseOrderTitleById(potUpd);

		//自动入库、销售、发货
		purchaseOrderService.autoProcessPo(purchaseOrderTitle, payOrder.getConfirmorAt());
		
		dealSuccess(pmsPayOrderTitleId, BaseConsts.FIVE, payOrder.getProjectId()); // 更新请款单状态
		pmsPayRpcService.passPayOrder(payOrder, pmsPayOrderTitle); // 通知pms处理请款单处理结果
	}
	
	public PageResult<PmsPayOrderTitleResDto> queryResultByCond(PmsPoTitleSearchReqDto req) {
		PageResult<PmsPayOrderTitleResDto> pageResult = new PageResult<PmsPayOrderTitleResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<PmsPayOrderTitle> pmsPayOrderTitles = pmsPayOrderTitleDao.queryResultsByCon(req, rowBounds);
		List<PmsPayOrderTitleResDto> pmsPayOrderTitleResDtos = new ArrayList<PmsPayOrderTitleResDto>();
		for (PmsPayOrderTitle item : pmsPayOrderTitles) {
			PmsPayOrderTitleResDto resDto = convertToResDto(item);
			resDto.setOpertaList(getOperList(item.getState()));
			pmsPayOrderTitleResDtos.add(resDto);
		}
		pageResult.setItems(pmsPayOrderTitleResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	private PmsPayOrderTitleResDto convertToResDto(PmsPayOrderTitle pmsPayOrderTitle) {
		PmsPayOrderTitleResDto resDto = new PmsPayOrderTitleResDto();
		BeanUtils.copyProperties(pmsPayOrderTitle, resDto);
		resDto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_PAY_ORDER_STATE, pmsPayOrderTitle.getState() + ""));
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
				PmsPayOrderTitleResDto.Operate.operMap);
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
		// 状态,1表示待处理，2表示处理成功，3表示处理失败
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.DEAL);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.THREE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.DEAL);
			break;
		case BaseConsts.FOUR:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.FIVE:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}

	private void sendMailMsg(int pmsPayOrderTitleId, int projectId) {
		PmsPayOrderTitle pmsPayOrderTitle = pmsPayOrderTitleDao.queryEntityById(pmsPayOrderTitleId);
		String stateName = ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_PAY_ORDER_STATE,
				pmsPayOrderTitle.getState() + "");

		BaseUser toUser = ServiceSupport.getOfficalUser(projectId); // 商务主管
		String auditTypeStr = "【PMS请款单处理结果】";
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("请款单编号");
		mail1.setColumnTwo(pmsPayOrderTitle.getPayNo());
		templateOnes.add(mail1);

		MailTemplateOne mail4 = new MailTemplateOne();
		mail4.setColumnOne("项目");
		mail4.setColumnTwo(cacheService.getProjectNameById(projectId));
		templateOnes.add(mail4);

		MailTemplateOne mail5 = new MailTemplateOne();
		mail5.setColumnOne("付款金额");
		mail5.setColumnTwo(
				DecimalUtil.toAmountString(pmsPayOrderTitle.getPayMoney()) + pmsPayOrderTitle.getPayCurrency());
		templateOnes.add(mail5);

		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("信息");
		mail2.setColumnTwo("单据由系统管理员于" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date())
				+ "处理,请款单状态【" + stateName + "】");
		templateOnes.add(mail2);

		String content = msgContentService.convertMailOneContent(auditTypeStr, templateOnes, null);
		auditService.sendWarnMail(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【PMS请款单】需要处理", content);
	}

	private void sendRTXMsg(int pmsPayOrderTitleId, int projectId) {
		PmsPayOrderTitle pmsPayOrderTitle = pmsPayOrderTitleDao.queryEntityById(pmsPayOrderTitleId);
		String stateName = ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_PAY_ORDER_STATE,
				pmsPayOrderTitle.getState() + "");
		BaseUser toUser = ServiceSupport.getOfficalUser(projectId); // 商务主管
		String content = "";
		content = content + "请款单编号:" + pmsPayOrderTitle.getPayNo() + "\n";
		content = content + "项目:" + cacheService.getProjectNameById(projectId) + "\n";
		content = content + "付款金额:" + DecimalUtil.toAmountString(pmsPayOrderTitle.getPayMoney())
				+ pmsPayOrderTitle.getPayCurrency() + "\n";
		content = content + "信息:" + "单据由系统管理员于"
				+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "处理,请款单状态【" + stateName
				+ "】\n";
		content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
		content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";
		auditService.sendWarnRtx(toUser.getId(), "SCFS系统提醒您,有新的【PMS请款单】需要处理", content);
	}
}
