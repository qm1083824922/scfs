package com.scfs.service.api.cms;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.api.cms.dto.req.CmsHttpReqDto;
import com.scfs.domain.api.cms.dto.res.CmsPayConfirmHttpResDto;
import com.scfs.domain.api.cms.entity.CmsPayConfirm;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.pay.PayService;
import com.scfs.service.support.ServiceSupport;

@Service
public class CmsPayConfirmService {
	private final static Logger LOGGER = LoggerFactory.getLogger(CmsPayConfirmService.class);
	@Autowired
	private InvokeConfig invokeConfig;
	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PayService payService;
	@Autowired
	private BaseAccountDao baseAccountDao;

	public CmsPayConfirmHttpResDto doCmsPayConfirm(CmsHttpReqDto req) {
		CmsPayConfirmHttpResDto res = new CmsPayConfirmHttpResDto();
		res.setFlag(BaseConsts.FLAG_YES);
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);
		invokeLog.setCreateAt(new Date());
		invokeLog.setConsumer(BaseConsts.FOUR);
		invokeLog.setProvider(BaseConsts.ONE);
		invokeLog.setInvokeType(InvokeTypeEnum.CMS_PAY_ORDER_CONFIRM.getType());
		invokeLog.setInvokeMode(BaseConsts.ONE);
		invokeLogService.add(invokeLog);

		CmsPayConfirm cmsPayConfirm = null;
		try {
			cmsPayConfirm = JSON.parseObject(data, CmsPayConfirm.class);
		} catch (Exception e) {
			LOGGER.error("[cms]cms付款确认接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			res.setMsg("[cms]cms付款确认接口请求失败");
			invokeLog.setReturnMsg(JSON.toJSONString(res));
			invokeLogService.invokeException(invokeLog, e);
		}
		if (null != cmsPayConfirm) {
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					res.setMsg("请求非法: 签名校验出错");
					res.setPay_no(cmsPayConfirm.getPay_no());
					invokeLog.setReturnMsg(JSON.toJSONString(res));
					invokeLogService.invokeError(invokeLog);
					return res;
				}
			}
			try {
				checkData(cmsPayConfirm); // 接口前置检查
				dealCmsPayConfirm(cmsPayConfirm);

				res.setPay_no(cmsPayConfirm.getPay_no());
				invokeLog.setBillNo(cmsPayConfirm.getPay_no());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeSuccess(invokeLog);
			} catch (BaseException e) {
				LOGGER.error("[cms]cms付款确认接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
				res.setMsg(commonService.getMsg(e.getMsg()));
				res.setPay_no(cmsPayConfirm.getPay_no());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
			} catch (Exception e) {
				LOGGER.error("[cms]cms付款确认接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
				res.setMsg(commonService.getMsg(e.getMessage()));
				res.setPay_no(cmsPayConfirm.getPay_no());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
			}
		}
		return res;
	}

	private void checkData(CmsPayConfirm cmsPayConfirm) {
		if (cmsPayConfirm == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数格式有误");
		}
		if (StringUtils.isEmpty(cmsPayConfirm.getPay_no())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款单号不能为空");
		}
		if (null == cmsPayConfirm.getPay_status()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款状态不能为空");
		}
		PayOrder payOrder = payOrderDao.queryEntityByPayNo(cmsPayConfirm.getPay_no());
		if (null == payOrder) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款单不存在");
		}
		if (payOrder.getState().equals(BaseConsts.SIX)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款单已完成，请勿重复调用");
		}
		if (cmsPayConfirm.getPay_status().equals(BaseConsts.ONE)) { // 1-付款成功
			if (null == cmsPayConfirm.getReal_pay_time()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 实际付款时间不能为空");
			}
			if (null == cmsPayConfirm.getReal_currency()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 实际付款币种不能为空");
			}
			if (null == cmsPayConfirm.getReal_pay_amount()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 实际付款金额不能为空");
			}
			if (null == cmsPayConfirm.getPay_rate()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款汇率不能为空");
			}
			if (null == cmsPayConfirm.getPayer()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款人不能为空");
			}
			if (null == cmsPayConfirm.getBank_account()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 付款账号不能为空");
			}
		} else if (cmsPayConfirm.getPay_status().equals(BaseConsts.TWO)) { // 2-付款驳回
			if (null == cmsPayConfirm.getRejecter()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 驳回人不能为空");
			}
			if (null == cmsPayConfirm.getReason()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 原因不能为空");
			}
		}
	}

	private void dealCmsPayConfirm(CmsPayConfirm cmsPayConfirm) {
		PayOrder payOrder = payOrderDao.queryEntityByPayNo(cmsPayConfirm.getPay_no());
		if (cmsPayConfirm.getPay_status().equals(BaseConsts.ONE)) { // 1-付款成功
			String currencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
					cmsPayConfirm.getReal_currency());
			BaseAccount baseAccount = baseAccountDao.queryAccountByAccountNo(cmsPayConfirm.getBank_account());
			if (null == baseAccount) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"请求参数有误: 付款账号【" + cmsPayConfirm.getBank_account() + "】在SCFS未维护");
			}
			PayOrder updatePayOrder = new PayOrder();
			updatePayOrder.setId(payOrder.getId());
			updatePayOrder.setCmsPayer(cmsPayConfirm.getPayer());
			updatePayOrder.setBankCharge(cmsPayConfirm.getBank_fee_amount());
			updatePayOrder.setConfirmorAt(cmsPayConfirm.getReal_pay_time());
			updatePayOrder.setRealCurrencyType(Integer.parseInt(currencyType));
			updatePayOrder.setRealPayAmount(cmsPayConfirm.getReal_pay_amount());
			updatePayOrder.setPayRate(cmsPayConfirm.getPay_rate());
			updatePayOrder.setPaymentAccount(baseAccount.getId());
			payOrder.setReason("");
			payOrder.setCmsRejecter("");
			payService.submitPayOver(updatePayOrder, BaseConsts.ONE); // 提交付款单
		} else if (cmsPayConfirm.getPay_status().equals(BaseConsts.TWO)) { // 2-付款驳回
			payOrder.setReason(cmsPayConfirm.getReason());
			payOrder.setCmsRejecter(cmsPayConfirm.getRejecter());
			payOrder.setCmsPayer("");
			payService.rejectPayOrderById(payOrder);
		}
	}

}
