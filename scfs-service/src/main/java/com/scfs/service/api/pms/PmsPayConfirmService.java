package com.scfs.service.api.pms;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.api.pms.PmsPayPoRelDao;
import com.scfs.dao.api.pms.PmsPledgePayConfirmDao;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsPayConfirmHttpResDto;
import com.scfs.domain.api.pms.entity.PmsPayConfirm;
import com.scfs.domain.api.pms.entity.PmsPayPoRel;
import com.scfs.domain.api.pms.entity.PmsPledgePayConfirm;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.VerificationAdvance;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.logistics.VerificationAdvanceService;
import com.scfs.service.pay.PayService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * 融通质押业务请款单确认 Created by Administrator on 2017年5月2日.
 */
@Service
public class PmsPayConfirmService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayConfirmService.class);

	@Autowired
	private InvokeConfig invokeConfig;
	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BillDeliveryService billDeliveryService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private BankReceiptDao bankReceiptDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private ReceiveService receiveService; // 应收
	@Autowired
	private PayService payService;
	@Autowired
	private BaseAccountDao baseAccountDao;
	@Autowired
	private VerificationAdvanceService verificationAdvanceService;
	@Autowired
	private PmsPledgePayConfirmDao pmsPledgePayConfirmDao;
	@Autowired
	private PmsPayPoRelDao pmsPayPoRelDao;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;

	public PmsPayConfirmHttpResDto doPmsPayConfirm(PmsHttpReqDto req) {
		PmsPayConfirmHttpResDto res = new PmsPayConfirmHttpResDto();
		res.setFlag(BaseConsts.FLAG_YES);
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);
		invokeLog.setCreateAt(new Date());
		invokeLog.setConsumer(BaseConsts.THREE);
		invokeLog.setProvider(BaseConsts.ONE);
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PAY_PURCHASE_CONFIRM.getType()); // pms同步请款接口
		invokeLog.setInvokeMode(BaseConsts.TWO);
		invokeLogService.add(invokeLog);

		PmsPayConfirm pmsPayConfirm = null;
		try {
			pmsPayConfirm = JSON.parseObject(data, PmsPayConfirm.class);
		} catch (Exception e) {
			LOGGER.error("[pms]pms请款单付款确认接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			res.setMsg("[pms]pms请款单付款确认接口请求失败");
			invokeLog.setReturnMsg(JSON.toJSONString(res));
			invokeLogService.invokeException(invokeLog, e);
		}
		if (null != pmsPayConfirm) {
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					res.setMsg("请求非法: 签名校验出错");
					res.setPay_sn(pmsPayConfirm.getPay_sn());
					invokeLog.setReturnMsg(JSON.toJSONString(res));
					invokeLogService.invokeError(invokeLog);
					return res;
				}
			}
			try {
				checkData(pmsPayConfirm); // 接口前置检查
				dealPmsPayConfirm(pmsPayConfirm);

				res.setPay_sn(pmsPayConfirm.getPay_sn());
				invokeLog.setBillNo(pmsPayConfirm.getPay_sn());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeSuccess(invokeLog);
			} catch (BaseException e) {
				LOGGER.error("[pms]pms请款单付款确认接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
				res.setMsg(commonService.getMsg(e.getMsg()));
				res.setPay_sn(pmsPayConfirm.getPay_sn());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
			} catch (Exception e) {
				LOGGER.error("[pms]pms请款单付款确认接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
				res.setMsg(commonService.getMsg(e.getMessage()));
				res.setPay_sn(pmsPayConfirm.getPay_sn());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
			}
		}
		return res;
	}

	private void checkData(PmsPayConfirm pmsPayConfirm) {
		if (pmsPayConfirm == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数格式有误");
		}
		if (StringUtils.isEmpty(pmsPayConfirm.getPay_sn())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 请款单号不能为空");
		}
		if (StringUtils.isEmpty(pmsPayConfirm.getPurchase_sn())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 采购单号不能为空");
		}
		if (null == pmsPayConfirm.getVerify_time()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 确认日期不能为空");
		}
		if (null == pmsPayConfirm.getPay_price()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 请款金额不能为空");
		}
		if (StringUtils.isEmpty(pmsPayConfirm.getCurrency_type())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 请款币种不能为空");
		}
		if (null == pmsPayConfirm.getCurrency_money()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 实际请款金额不能为空");
		}
		if (StringUtils.isEmpty(pmsPayConfirm.getReal_currency_type())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 实际请款币种不能为空");
		}

		String paySn = pmsPayConfirm.getPay_sn();
		List<BillDelivery> billDeliveryList = billDeliveryService.queryFinishedBillDeliveryByAffiliateNo(paySn);
		if (!CollectionUtils.isEmpty(billDeliveryList)) {
			if (billDeliveryList.size() > 1) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "处理异常: 该请款单对应多个SCFS销售单");
			}
			BillDelivery billDelivery = billDeliveryList.get(0);
			if (!BaseConsts.CURRENCY_UNIT_MAP.get(billDelivery.getCurrencyType())
					.equals(pmsPayConfirm.getCurrency_type())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "处理异常: 请款币种与原请款单(待付款)币种不一致");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "处理异常: 该请款单没有对应的SCFS销售单");
		}
	}

	private void dealPmsPayConfirm(PmsPayConfirm pmsPayConfirm) {
		PmsPledgePayConfirm pmsPledgePayConfirm = new PmsPledgePayConfirm();
		pmsPledgePayConfirm.setPaySn(pmsPayConfirm.getPay_sn());
		pmsPledgePayConfirm.setPurchaseSn(pmsPayConfirm.getPurchase_sn());
		pmsPledgePayConfirm.setCurrencyType(pmsPayConfirm.getCurrency_type());
		pmsPledgePayConfirm.setRealCurrencyType(pmsPayConfirm.getReal_currency_type());
		pmsPledgePayConfirm.setPayPrice(pmsPayConfirm.getPay_price());
		pmsPledgePayConfirm.setCurrencyMoney(pmsPayConfirm.getCurrency_money());
		pmsPledgePayConfirm.setVerifyTime(pmsPayConfirm.getVerify_time());
		pmsPledgePayConfirmDao.insert(pmsPledgePayConfirm);
		// 回写请款数量
		List<PmsPayPoRel> PmsPayPoRelList = pmsPayPoRelDao
				.queryPmsPayPoRelListByPmsPaySn(pmsPledgePayConfirm.getPaySn());
		if (CollectionUtils.isNotEmpty(PmsPayPoRelList)) {
			for (PmsPayPoRel pmsPayPoRel : PmsPayPoRelList) {
				Integer poLineId = pmsPayPoRel.getPoLineId();
				PurchaseOrderLine purchaseOrderLine = purchaseOrderLineDao.queryPurchaseOrderLineById(poLineId);
				PurchaseOrderLine updatePurchaseOrderLine = new PurchaseOrderLine();
				updatePurchaseOrderLine.setId(purchaseOrderLine.getId());
				updatePurchaseOrderLine.setDistributeNum(DecimalUtil.add(
						null == purchaseOrderLine.getDistributeNum() ? BigDecimal.ZERO
								: purchaseOrderLine.getDistributeNum(),
						null == pmsPayPoRel.getPayQuantity() ? BigDecimal.ZERO : pmsPayPoRel.getPayQuantity()));
				purchaseOrderLineDao.updatePurchaseOrderLineById(updatePurchaseOrderLine);
			}
		}

		/**
		 * List<BillDelivery> billDeliveryList =
		 * billDeliveryService.queryFinishedBillDeliveryByAffiliateNo(pmsPayConfirm.getPay_sn());
		 * BillDelivery billDelivery = billDeliveryList.get(0); //生成水单
		 * BankReceipt bankReceipt = createBankReceipt(pmsPayConfirm,
		 * billDelivery);
		 * 
		 * //插入水单和销售单的关系 addVerificationAdvance(billDelivery, bankReceipt);
		 * //核销水单 BillOutStore billOutStore =
		 * billOutStoreService.queryValidBillOutStoreByBillDeliveryId(billDelivery.getId());
		 * verificationReceipt(billOutStore);
		 **/
		// 尾款生成付款单
		// createPayOrderByPo(pmsPayConfirm);
	}

	private void addVerificationAdvance(BillDelivery billDelivery, BankReceipt bankReceipt) {
		VerificationAdvance verificationAdvance = new VerificationAdvance();
		verificationAdvance.setId(null);
		verificationAdvance.setBillDeliveryId(billDelivery.getId());
		verificationAdvance.setReceiptId(bankReceipt.getId()); // 水单ID
		verificationAdvance.setAmount(billDelivery.getRequiredSendAmount());
		verificationAdvanceService.addVerificationAdvance(verificationAdvance);
		verificationAdvanceService.dealReceiptAdvance(billDelivery, verificationAdvance.getId());
	}

	private void verificationReceipt(BillOutStore billOutStore) {
		ReceiveSearchReqDto receiveSearchReqDto = new ReceiveSearchReqDto();
		receiveSearchReqDto.setOutStoreId(billOutStore.getId());
		receiveSearchReqDto.setBillType(BaseConsts.THREE);
		List<ReceiveResDto> receiveResDtoList = receiveService.queryListResultByCon(receiveSearchReqDto);
		if (CollectionUtils.isNotEmpty(receiveResDtoList)) {
			Set<Integer> recIdSet = new HashSet<Integer>();
			for (ReceiveResDto receiveResDto : receiveResDtoList) {
				recIdSet.add(receiveResDto.getId());
			}
			for (Integer recId : recIdSet) {
				verificationAdvanceService.verificationReceipt(recId, billOutStore.getBillNo(),
						billOutStore.getBillDeliveryId(), BaseConsts.THREE);
			}
		}
	}

	private BankReceipt createBankReceipt(PmsPayConfirm pmsPayConfirm, BillDelivery billDelivery) {
		BankReceipt bankReceipt = new BankReceipt();
		bankReceipt.setReceiptNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_RECEIPT_NO, SeqConsts.S_RECEIPT_NO,
				BaseConsts.INT_13));
		bankReceipt.setProjectId(billDelivery.getProjectId());
		bankReceipt.setCustId(billDelivery.getCustomerId());
		BaseProject baseProject = cacheService.getProjectById(billDelivery.getProjectId());
		Integer businessUnitId = null;
		if (null != baseProject) {
			businessUnitId = baseProject.getBusinessUnitId();
			bankReceipt.setBusiUnit(businessUnitId);
		}
		String currencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
				pmsPayConfirm.getCurrency_type());
		String actualCurrencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
				pmsPayConfirm.getReal_currency_type());

		bankReceipt.setCurrencyType(Integer.parseInt(currencyType));
		bankReceipt.setReceiptType(BaseConsts.ONE);
		bankReceipt.setReceiptDate(pmsPayConfirm.getVerify_time());
		bankReceipt.setState(BaseConsts.ONE);
		bankReceipt.setReceiptAmount(pmsPayConfirm.getPay_price());
		if (null != businessUnitId) {
			List<CodeValue> codeValueList = commonService.getAllOwnCv(CacheKeyConsts.SUBJECT_ACCOUNT,
					String.valueOf(businessUnitId));
			if (CollectionUtils.isNotEmpty(codeValueList)) {
				bankReceipt.setRecAccountNo(Integer.parseInt(codeValueList.get(0).getCode()));
			}
		}
		bankReceipt.setBankReceiptNo(pmsPayConfirm.getPay_sn());
		bankReceipt.setWriteOffAmount(BigDecimal.ZERO);
		bankReceipt.setPreRecAmount(BigDecimal.ZERO);
		bankReceipt.setPaidAmount(BigDecimal.ZERO);
		bankReceipt.setReceiptWay(BaseConsts.ONE);
		bankReceipt.setDiffAmount(BigDecimal.ZERO);
		bankReceipt.setSummary("请款金额：" + pmsPayConfirm.getPay_price() + "；请款币种：" + pmsPayConfirm.getCurrency_type()
				+ "；实付金额：" + pmsPayConfirm.getCurrency_money() + "；实付币种：" + pmsPayConfirm.getReal_currency_type());
		bankReceipt.setCreatorId(ServiceSupport.getUser().getId());
		bankReceipt.setCreator(ServiceSupport.getUser().getChineseName());
		bankReceipt.setCreateAt(new Date());
		bankReceipt.setIsDelete(BaseConsts.ZERO);
		bankReceipt.setActualReceiptAmount(pmsPayConfirm.getCurrency_money());
		bankReceipt.setActualWriteOffAmount(BigDecimal.ZERO);
		bankReceipt.setActualPreRecAmount(BigDecimal.ZERO);
		bankReceipt.setActualPaidAmount(BigDecimal.ZERO);
		bankReceipt.setActualDiffAmount(BigDecimal.ZERO);
		bankReceipt.setActualCurrencyType(Integer.parseInt(actualCurrencyType));
		if (null == pmsPayConfirm.getPay_price() || DecimalUtil.eq(pmsPayConfirm.getPay_price(), BigDecimal.ZERO)) {
			bankReceipt.setActualCurrencyRate(BigDecimal.ZERO);
		} else {
			bankReceipt.setActualCurrencyRate(
					DecimalUtil.divide(pmsPayConfirm.getCurrency_money(), pmsPayConfirm.getPay_price()));
		}
		bankReceiptDao.insert(bankReceipt);
		return bankReceipt;
	}

	private void createPayOrderByPo(PmsPayConfirm pmsPayConfirm) {
		List<PurchaseOrderTitle> poList = purchaseOrderService.queryFinishedPoByAppendNo(pmsPayConfirm.getPay_sn());
		if (!CollectionUtils.isEmpty(poList)) {
			if (poList.size() > 1) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "处理异常: 该请款单对应多个SCFS采购单");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "处理异常: 该请款单没有对应SCFS采购单");
		}

		PurchaseOrderTitle po = poList.get(0);
		Integer payId = purchaseOrderService.addPayOrderByPo(po, 2);// 生成付款单

		QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
		queryAccountReqDto.setId(po.getSupplierId());
		List<BaseAccount> baseAccounts = baseAccountDao.queryAccountBySubjectId(queryAccountReqDto);
		if (CollectionUtils.isEmpty(baseAccounts)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商[" + po.getSupplierId() + "]没有可用的账户");
		}
		PayOrder poUpd = new PayOrder();
		poUpd.setId(payId);
		poUpd.setPayWay(BaseConsts.ONE); // 付款方式为转账
		poUpd.setPayAccountId(baseAccounts.get(0).getId());
		poUpd.setAttachedNumbe(pmsPayConfirm.getPay_sn());
		poUpd.setRequestPayTime(new Date());
		poUpd.setInnerPayDate(pmsPayConfirm.getVerify_time());
		poUpd.setRemark(
				"PMS请款单确认日期：" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, pmsPayConfirm.getVerify_time()));
		payService.updatePayOrderById(poUpd);
	}

}
