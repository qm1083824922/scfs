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
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.api.pms.PmsFactorPayConfirmDao;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsPayOrderConfirmHttpResDto;
import com.scfs.domain.api.pms.entity.PmsFactorPayConfirm;
import com.scfs.domain.api.pms.entity.PmsPayOrderConfirm;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.VerificationAdvance;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.logistics.VerificationAdvanceService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * 应收保理业务请款单确认 Created by Administrator on 2017年5月22日.
 */
@Service
public class PmsConfirmService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsConfirmService.class);

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
	private ReceiveService receiveService; // 应收
	@Autowired
	private VerificationAdvanceService verificationAdvanceService;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PmsFactorPayConfirmDao pmsFactorPayConfirmDao;

	public PmsPayOrderConfirmHttpResDto doPmsPayOrderConfirm(PmsHttpReqDto req) {
		PmsPayOrderConfirmHttpResDto res = new PmsPayOrderConfirmHttpResDto();
		res.setFlag(BaseConsts.FLAG_YES);
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);
		invokeLog.setCreateAt(new Date());
		invokeLog.setConsumer(BaseConsts.THREE);
		invokeLog.setProvider(BaseConsts.ONE);
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PAY_ORDER_CONFIRM.getType());
		invokeLog.setInvokeMode(BaseConsts.TWO);
		invokeLogService.add(invokeLog);

		PmsPayOrderConfirm pmsPayOrderConfirm = null;
		try {
			pmsPayOrderConfirm = JSON.parseObject(data, PmsPayOrderConfirm.class);
		} catch (Exception e) {
			LOGGER.error("[pms]pms付款确认(应收保理)接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			res.setMsg("[pms]pms付款确认(应收保理)接口请求失败");
			invokeLog.setReturnMsg(JSON.toJSONString(res));
			invokeLogService.invokeException(invokeLog, e);
		}
		if (null != pmsPayOrderConfirm) {
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					res.setMsg("请求非法: 签名校验出错");
					res.setPay_no(pmsPayOrderConfirm.getNew_pay_no());
					invokeLog.setReturnMsg(JSON.toJSONString(res));
					invokeLogService.invokeError(invokeLog);
					return res;
				}
			}
			try {
				checkData(pmsPayOrderConfirm); // 接口前置检查
				dealPmsPayOrderConfirm(pmsPayOrderConfirm);

				res.setPay_no(pmsPayOrderConfirm.getNew_pay_no());
				invokeLog.setBillNo(pmsPayOrderConfirm.getPay_no());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeSuccess(invokeLog);
			} catch (BaseException e) {
				LOGGER.error("[pms]pms付款确认(应收保理)接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
				res.setMsg(commonService.getMsg(e.getMsg()));
				res.setPay_no(pmsPayOrderConfirm.getNew_pay_no());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
			} catch (Exception e) {
				LOGGER.error("[pms]pms付款确认(应收保理)接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
				res.setMsg(commonService.getMsg(e.getMessage()));
				res.setPay_no(pmsPayOrderConfirm.getNew_pay_no());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
			}
		}
		return res;
	}

	private void checkData(PmsPayOrderConfirm pmsPayOrderConfirm) {
		if (pmsPayOrderConfirm == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数格式有误");
		}
		if (StringUtils.isEmpty(pmsPayOrderConfirm.getPay_no())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款编号不能为空");
		}
		if (StringUtils.isEmpty(pmsPayOrderConfirm.getProvider_sn())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 供应商编号不能为空");
		}
		if (null == pmsPayOrderConfirm.getConfirm_date()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 确认日期不能为空");
		}
		if (null == pmsPayOrderConfirm.getPay_amount()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款金额不能为空");
		}
		if (StringUtils.isEmpty(pmsPayOrderConfirm.getCurrency_type())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款币种不能为空");
		}
		if (null == pmsPayOrderConfirm.getReal_pay_amount()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 实际付款金额不能为空");
		}
		if (StringUtils.isEmpty(pmsPayOrderConfirm.getReal_currency_type())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 实际付款币种不能为空");
		}

		String payNo = pmsPayOrderConfirm.getPay_no();
		PayOrder payOrder = payOrderDao.queryEntityByAttachedNumbe(payNo);
		if (null == payOrder) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "处理异常: 该付款单没有对应的SCFS付款单");
		}
	}

	private void dealPmsPayOrderConfirm(PmsPayOrderConfirm pmsPayOrderConfirm) {
		PmsFactorPayConfirm pmsFactorPayConfirm = new PmsFactorPayConfirm();
		pmsFactorPayConfirm.setProviderSn(pmsPayOrderConfirm.getProvider_sn());
		pmsFactorPayConfirm.setBankSeriesNo(pmsPayOrderConfirm.getBank_series_no());
		pmsFactorPayConfirm.setConfirmDate(pmsPayOrderConfirm.getConfirm_date());
		pmsFactorPayConfirm.setCorporationCode(pmsPayOrderConfirm.getCorporation_code());
		pmsFactorPayConfirm.setCorporationName(pmsPayOrderConfirm.getCorporation_name());
		pmsFactorPayConfirm.setNewPayNo(pmsPayOrderConfirm.getNew_pay_no());
		pmsFactorPayConfirm.setPayNo(pmsPayOrderConfirm.getPay_no());
		pmsFactorPayConfirm.setCurrencyType(pmsPayOrderConfirm.getCurrency_type());
		pmsFactorPayConfirm.setPayAmount(pmsPayOrderConfirm.getPay_amount());
		pmsFactorPayConfirm.setRealCurrencyType(pmsPayOrderConfirm.getReal_currency_type());
		pmsFactorPayConfirm.setRealPayAmount(pmsPayOrderConfirm.getReal_pay_amount());
		pmsFactorPayConfirmDao.insert(pmsFactorPayConfirm);
		/**
		 * List<BillDelivery> billDeliveryList =
		 * billDeliveryService.queryFinishedBillDeliveryByAffiliateNo(pmsPayOrderConfirm.getPay_no());
		 * BillDelivery billDelivery = billDeliveryList.get(0);
		 * 
		 * //生成水单 BankReceipt bankReceipt =
		 * createBankReceipt(pmsPayOrderConfirm, billDelivery);
		 * 
		 * //插入水单和销售单的关系 addVerificationAdvance(billDelivery, bankReceipt);
		 * //核销水单 BillOutStore billOutStore =
		 * billOutStoreService.queryValidBillOutStoreByBillDeliveryId(billDelivery.getId());
		 * verificationReceipt(billOutStore);
		 **/
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
						billOutStore.getBillDeliveryId(), BaseConsts.TWO);
			}
		}
	}

	private BankReceipt createBankReceipt(PmsPayOrderConfirm pmsPayOrderConfirm, BillDelivery billDelivery) {
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
				pmsPayOrderConfirm.getCurrency_type());
		String actualCurrencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
				pmsPayOrderConfirm.getReal_currency_type());
		bankReceipt.setCurrencyType(Integer.parseInt(currencyType));
		bankReceipt.setReceiptType(BaseConsts.ONE);
		bankReceipt.setReceiptDate(pmsPayOrderConfirm.getConfirm_date());
		bankReceipt.setState(BaseConsts.ONE);
		bankReceipt.setReceiptAmount(pmsPayOrderConfirm.getPay_amount());
		if (null != businessUnitId) {
			List<CodeValue> codeValueList = commonService.getAllOwnCv(CacheKeyConsts.SUBJECT_ACCOUNT,
					String.valueOf(businessUnitId));
			if (CollectionUtils.isNotEmpty(codeValueList)) {
				bankReceipt.setRecAccountNo(Integer.parseInt(codeValueList.get(0).getCode()));
			}
		}
		bankReceipt.setBankReceiptNo(pmsPayOrderConfirm.getPay_no());
		bankReceipt.setWriteOffAmount(BigDecimal.ZERO);
		bankReceipt.setPreRecAmount(BigDecimal.ZERO);
		bankReceipt.setPaidAmount(BigDecimal.ZERO);
		bankReceipt.setReceiptWay(BaseConsts.ONE);
		bankReceipt.setSummary("付款金额：" + pmsPayOrderConfirm.getPay_amount() + "；请款币种："
				+ pmsPayOrderConfirm.getCurrency_type() + "；实际付款金额：" + pmsPayOrderConfirm.getReal_pay_amount()
				+ "；实际付款币种：" + pmsPayOrderConfirm.getReal_currency_type() + "；银行流水号："
				+ (StringUtils.isBlank(pmsPayOrderConfirm.getBank_series_no()) ? ""
						: pmsPayOrderConfirm.getBank_series_no()));
		bankReceipt.setCreatorId(ServiceSupport.getUser().getId());
		bankReceipt.setCreator(ServiceSupport.getUser().getChineseName());
		bankReceipt.setCreateAt(new Date());
		bankReceipt.setIsDelete(BaseConsts.ZERO);
		bankReceipt.setActualReceiptAmount(pmsPayOrderConfirm.getReal_pay_amount());
		bankReceipt.setActualWriteOffAmount(BigDecimal.ZERO);
		bankReceipt.setActualPreRecAmount(BigDecimal.ZERO);
		bankReceipt.setActualPaidAmount(BigDecimal.ZERO);
		bankReceipt.setActualDiffAmount(BigDecimal.ZERO);
		bankReceipt.setActualCurrencyType(Integer.parseInt(actualCurrencyType));
		if (null == pmsPayOrderConfirm.getPay_amount()
				|| DecimalUtil.eq(pmsPayOrderConfirm.getPay_amount(), BigDecimal.ZERO)) {
			bankReceipt.setActualCurrencyRate(BigDecimal.ZERO);
		} else {
			bankReceipt.setActualCurrencyRate(
					DecimalUtil.divide(pmsPayOrderConfirm.getReal_pay_amount(), pmsPayOrderConfirm.getPay_amount()));
		}
		bankReceiptDao.insert(bankReceipt);
		return bankReceipt;
	}

}
