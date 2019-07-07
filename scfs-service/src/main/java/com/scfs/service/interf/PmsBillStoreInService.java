package com.scfs.service.interf;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.api.pms.PmsStoreInDao;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.base.entity.BaseAddressDao;
import com.scfs.dao.base.entity.DistributionGoodsDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.pay.PayPoRelationDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.dao.project.ProjectGoodsDao;
import com.scfs.dao.project.ProjectSubjectDao;
import com.scfs.domain.api.pms.entity.PmsStoreIn;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.pay.entity.PayPoRelation;
import com.scfs.domain.po.dto.req.PmsStoreInReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.project.dto.req.ProjectGoodsSearchReqDto;
import com.scfs.domain.project.dto.req.ProjectSubjectSearchReqDto;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.service.api.pms.PmsSyncBillInStoreService;
import com.scfs.service.audit.AuditService;
import com.scfs.service.base.subject.BaseSubjectService;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.pay.PayService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class PmsBillStoreInService {

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private ProjectGoodsDao projectGoodsDao;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private PayService payService;
	@Autowired
	private DistributionGoodsDao distributionGoodsDao;
	@Autowired
	private ProjectSubjectDao projectSubjectDao;
	@Autowired
	private PayPoRelationDao payPoRelationDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private BaseAccountDao baseAccountDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private BaseAddressDao baseAddressDao;
	@Autowired
	private AuditService auditService;
	@Autowired
	private PmsSyncBillInStoreService pmsSyncBillInStoreService;
	@Autowired
	private BaseSubjectService baseSubjectService;
	@Autowired
	private PMSSupplierBindService pmsSupplierBindService;
	@Autowired
	private PmsStoreInDao pmsStoreInDao;

	public void dealPmsStoreIn(Integer id, boolean isSchedule) {
		// TODO Auto-generated method stub
		PmsStoreIn pmsStoreIn = pmsStoreInDao.queryEntityById(id);
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		// 订单编号
		String orderNo = sequenceService.getNumDateByBusName(BaseConsts.PO_NO_PRE, SeqConsts.PO_NO, BaseConsts.INT_13);
		// 通过value查询code
		String code = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
				pmsStoreIn.getCurrency_type());
		// 根据结算对象查经营单位
		QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
		querySubjectReqDto.setPmsSupplierCode(pmsStoreIn.getAccount_sn());
		querySubjectReqDto.setSubjectType(BaseConsts.ONE);
		List<BaseSubject> custs = baseSubjectService.querySubTypeAndPmsSupplier(querySubjectReqDto);
		if (CollectionUtils.isEmpty(custs)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS入库结算对象供应商编码【" + pmsStoreIn.getAccount_sn() + "】不存在");
		}
		if (custs.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS入库结算对象供应商编码【" + pmsStoreIn.getAccount_sn() + "】不唯一");
		}
		// 通过pms供应商查询对应的项目与供应商
		PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
		pMSSupplierBindReqDto.setPmsSupplierNo(pmsStoreIn.getProvider_sn());
		pMSSupplierBindReqDto.setBusinessUnit(custs.get(BaseConsts.ZERO).getId());
		PMSSupplierBind pmsSupplierBind = pmsSupplierBindService.queryPmsBySuppNoAndBui(pMSSupplierBindReqDto);
		if (pmsSupplierBind == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS入库消息发送,PMS供应商编号【" + pmsStoreIn.getProvider_sn()
					+ "】,结算单编码【" + pmsStoreIn.getAccount_sn() + "】查询PMS供应商绑定数据为空");
		}
		// 根据采购单号、供应商、币种查询对应的明细信息
		PmsStoreInReqDto pmsStoreInReqDto = new PmsStoreInReqDto();
		pmsStoreInReqDto.setCurrency_type(pmsStoreIn.getCurrency_type());
		pmsStoreInReqDto.setProvider_sn(pmsStoreIn.getProvider_sn());
		pmsStoreInReqDto.setPurchase_sn(pmsStoreIn.getPurchase_sn());
		if (isSchedule) {
			pmsStoreInReqDto.setDealFlag(BaseConsts.ONE);
		} else {
			pmsStoreInReqDto.setDealFlag(BaseConsts.TWO);
		}
		List<PmsStoreIn> pmsStoreList = pmsStoreInDao.queryPmsStoreInByCon(pmsStoreInReqDto);
		for (PmsStoreIn pmsStoreInDtl : pmsStoreList) {
			ProjectGoodsSearchReqDto projectGoodsSearchReqDto = new ProjectGoodsSearchReqDto();
			projectGoodsSearchReqDto.setProjectId(pmsSupplierBind.getProjectId());
			projectGoodsSearchReqDto.setNumber(pmsStoreInDtl.getSku());
			ProjectGoods projectGoods = projectGoodsDao.queryByProjectIdAndGoodsNo(projectGoodsSearchReqDto);
			if (projectGoods == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该项目下无该商品");
			}
		}

		// 通过项目查询经营单位
		BaseProject baseProject = cacheService.getProjectById(pmsSupplierBind.getProjectId());
		// 通过项目查询类型为监管的仓库
		purchaseOrderTitle.setWarehouseId(BaseConsts.INT_79);
		List<BaseAddress> baseAddresses = baseAddressDao.queryResultsyBySubjectId(purchaseOrderTitle.getWarehouseId());
		if (!CollectionUtils.isEmpty(baseAddresses)) {
			purchaseOrderTitle.setWareAddrId(baseAddresses.get(0).getId());
		}
		// 通过项目查询客户
		ProjectSubjectSearchReqDto projectReqDto = new ProjectSubjectSearchReqDto();
		projectReqDto.setProjectId(pmsSupplierBind.getProjectId());
		projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_CUSTOMER);
		List<ProjectSubject> projectSubject = projectSubjectDao.queryResultsByCon(projectReqDto);
		purchaseOrderTitle.setOrderNo(orderNo);
		purchaseOrderTitle.setOrderType(BaseConsts.TWO);
		purchaseOrderTitle.setAppendNo(pmsStoreIn.getPurchase_sn());
		purchaseOrderTitle.setCurrencyId(Integer.parseInt(code));
		purchaseOrderTitle.setCreateAt(new Date());
		purchaseOrderTitle.setCreator(ServiceSupport.getUser().getChineseName());
		purchaseOrderTitle.setCreatorId(ServiceSupport.getUser().getId());
		purchaseOrderTitle.setIsDelete(BaseConsts.ZERO);
		purchaseOrderTitle.setProjectId(pmsSupplierBind.getProjectId());
		purchaseOrderTitle.setSupplierId(pmsSupplierBind.getSupplierId());
		purchaseOrderTitle.setBusinessUnitId(baseProject.getBusinessUnitId());
		purchaseOrderTitle.setState(BaseConsts.FIVE);
		purchaseOrderTitle.setCustomerId(projectSubject.get(0).getSubjectId());
		purchaseOrderTitle.setOrderTime(pmsStoreIn.getStockin_time());
		purchaseOrderTitle.setPerdictTime(pmsStoreIn.getStockin_time());
		purchaseOrderTitle.setInvoiceTotalNum(BigDecimal.ZERO);
		purchaseOrderTitle.setInvoiceTotalAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setPerRecAmount(BigDecimal.ZERO);
		// 创建一条铺货订单信息
		purchaseOrderTitleDao.insert(purchaseOrderTitle);
		// 根据铺货订单创建一条付款单
		Integer payOrderId = createPayOrder(purchaseOrderTitle);

		dealStoreInDtl(pmsStoreList, purchaseOrderTitle, payOrderId);
	}

	private void dealStoreInDtl(List<PmsStoreIn> pmsStoreList, PurchaseOrderTitle purchaseOrderTitle,
			Integer payOrderId) {
		// TODO Auto-generated method stub
		BigDecimal sumNum = BigDecimal.ZERO;
		BigDecimal sumAmount = BigDecimal.ZERO;
		BigDecimal payAmount = BigDecimal.ZERO;
		for (PmsStoreIn pmsStoreIn : pmsStoreList) {
			try {
				PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
				purchaseOrderLine.setPoId(purchaseOrderTitle.getId());
				ProjectGoodsSearchReqDto projectGoodsSearchReqDto = new ProjectGoodsSearchReqDto();
				projectGoodsSearchReqDto.setProjectId(purchaseOrderTitle.getProjectId());
				projectGoodsSearchReqDto.setNumber(pmsStoreIn.getSku());
				ProjectGoods projectGoods = projectGoodsDao.queryByProjectIdAndGoodsNo(projectGoodsSearchReqDto);
				purchaseOrderLine.setGoodsId(projectGoods.getGoodsId());
				purchaseOrderLine.setGoodsNo(pmsStoreIn.getSku());
				purchaseOrderLine.setPledgeProportion(
						distributionGoodsDao.queryDistributionGoodsById(projectGoods.getGoodsId()).getPledge());
				purchaseOrderLine.setGoodsNum(pmsStoreIn.getStockin_num());
				purchaseOrderLine.setGoodsPrice(pmsStoreIn.getPurchase_price());
				purchaseOrderLine.setAmount(DecimalUtil.multiply(
						pmsStoreIn.getStockin_num() == null ? BigDecimal.ZERO : pmsStoreIn.getStockin_num(),
						pmsStoreIn.getPurchase_price()));
				purchaseOrderLine.setPurchaseDeliverySn(pmsStoreIn.getPurchase_delivery_sn());
				purchaseOrderLine.setInvoiceNum(pmsStoreIn.getStockin_num());
				purchaseOrderLine.setStorageNum(pmsStoreIn.getStockin_num());
				purchaseOrderLine.setStockinTime(pmsStoreIn.getStockin_time());
				purchaseOrderLine.setCreateAt(new Date());
				purchaseOrderLine.setCreator(ServiceSupport.getUser().getChineseName());
				purchaseOrderLine.setCreatorId(ServiceSupport.getUser().getId());
				purchaseOrderLine.setIsDelete(BaseConsts.ZERO);
				purchaseOrderLine.setDiscountPrice(pmsStoreIn.getPurchase_price());
				purchaseOrderLine.setDiscountAmount(DecimalUtil.ZERO);
				purchaseOrderLine.setCostPrice(pmsStoreIn.getPurchase_price());
				purchaseOrderLine.setPoPrice(pmsStoreIn.getPurchase_price());
				purchaseOrderLine.setOriginGoodsPrice(BigDecimal.ZERO);
				purchaseOrderLine.setRemainSendNum(pmsStoreIn.getStockin_num());
				purchaseOrderLine.setDistributeNum(BigDecimal.ZERO);
				purchaseOrderLineDao.insert(purchaseOrderLine);
				// 添加付款单与采购单关系
				PayPoRelation payPoRel = new PayPoRelation();
				payPoRel.setPayId(payOrderId);
				payPoRel.setPoId(purchaseOrderTitle.getId());
				payPoRel.setPoLineId(purchaseOrderLine.getId());
				payPoRel.setPayAmount(DecimalUtil.multiply(
						pmsStoreIn.getStockin_num() == null ? BigDecimal.ZERO : pmsStoreIn.getStockin_num(),
						purchaseOrderLine.getGoodsPrice()));
				payPoRel.setPayAmount(
						DecimalUtil.multiply(payPoRel.getPayAmount(), purchaseOrderLine.getPledgeProportion()));
				payPoRel.setDiscountAmount(BigDecimal.ZERO);
				payPoRel.setInDiscountAmount(DecimalUtil.multiply(
						pmsStoreIn.getStockin_num() == null ? BigDecimal.ZERO : pmsStoreIn.getStockin_num(),
						pmsStoreIn.getPurchase_price()));
				payPoRel.setCreateAt(new Date());
				payPoRel.setCreator(
						ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
				payPoRel.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
				payPoRelationDao.insert(payPoRel);
				sumNum = DecimalUtil.add(sumNum,
						pmsStoreIn.getStockin_num() == null ? BigDecimal.ZERO : pmsStoreIn.getStockin_num());
				sumAmount = DecimalUtil.add(sumAmount,
						purchaseOrderLine.getAmount() == null ? BigDecimal.ZERO : purchaseOrderLine.getAmount());
				payAmount = DecimalUtil.add(payAmount,
						payPoRel.getPayAmount() == null ? BigDecimal.ZERO : payPoRel.getPayAmount());
				// 更新处理状态
				dealSuccess(pmsStoreIn.getId(), purchaseOrderTitle.getId());
			} catch (BaseException ex) {
				// 更新处理状态
				dealFail(pmsStoreIn.getId(), ex.getMsg());
				continue;
			} catch (Exception e) {
				// 更新处理状态
				dealFail(pmsStoreIn.getId(), e.getMessage());
				continue;
			}
		}
		// 更新铺货订单总数量和总金额
		purchaseOrderTitle.setOrderTotalNum(sumNum);
		purchaseOrderTitle.setOrderTotalAmount(sumAmount);
		purchaseOrderTitle.setTotalDiscountAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setArrivalNum(sumNum);
		purchaseOrderTitle.setArrivalAmount(sumAmount);
		purchaseOrderTitle.setInvoiceTotalNum(sumNum);
		purchaseOrderTitle.setIsRequestPay(BaseConsts.TWO);
		purchaseOrderTitleDao.updatePurchaseOrderTitleById(purchaseOrderTitle);
		// 更新付款单总金额
		PayOrder payResult = payOrderDao.queryEntityById(payOrderId);
		payResult.setPayAmount(payAmount);
		payResult.setPoBlance(payAmount);
		payOrderDao.updateById(payResult);
	}

	public void dealFail(Integer id, String errorMsg) {
		PmsStoreIn pmsStoreIn = pmsStoreInDao.queryEntityById(id);
		pmsStoreIn.setDealFlag(BaseConsts.TWO);
		pmsStoreIn.setDealMsg(commonService.getMsg("处理失败:" + errorMsg));
		pmsStoreInDao.updateById(pmsStoreIn);
		pmsSyncBillInStoreService.sendSystemAlarm(id, BaseConsts.ZERO, pmsStoreIn.getDealMsg());
	}

	public void dealSuccess(Integer id, Integer poId) {
		PmsStoreIn pmsStoreIn = pmsStoreInDao.queryEntityById(id);
		pmsStoreIn.setDealFlag(BaseConsts.THREE);
		pmsStoreIn.setPoId(poId);
		pmsStoreIn.setDealMsg("处理成功");
		pmsStoreInDao.updateById(pmsStoreIn);
		pmsSyncBillInStoreService.sendSystemAlarm(id, BaseConsts.ONE, StringUtils.EMPTY);
	}

	private int createPayOrder(PurchaseOrderTitle po) {
		Integer payId = purchaseOrderService.addPayOrderByPo(po, 1);// 生成付款单
		QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
		queryAccountReqDto.setId(po.getSupplierId());
		List<BaseAccount> baseAccounts = baseAccountDao.queryAccountBySubjectId(queryAccountReqDto);
		if (CollectionUtils.isEmpty(baseAccounts)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商[" + po.getSupplierId() + "]没有可用的账户");
		}
		Date currDate = new Date();
		PayOrder poUpd = new PayOrder();
		poUpd.setId(payId);
		poUpd.setPayWay(BaseConsts.ONE); // 付款方式为转账
		poUpd.setPayAccountId(baseAccounts.get(0).getId());
		poUpd.setAttachedNumbe(po.getOrderNo());
		poUpd.setRequestPayTime(currDate);
		poUpd.setRemark(po.getRemark());
		payService.updatePayOrderById(poUpd);
		// sendWechatMsgByProject(payId);
		return payId;
	}

	public void sendWechatMsgByProject(Integer payId) {
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		auditService.sendWechatMsgByProject(payOrder);
	}

}
