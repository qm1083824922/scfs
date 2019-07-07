package com.scfs.service.pay;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.pay.entity.MergePayOrder;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.rpc.cms.entity.CmsPayOrder;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * 上传付款单和合并付款单至第三方财务系统
 *
 */
@Service
public class UploadPayService {
	private final static Logger LOGGER = LoggerFactory.getLogger(UploadPayService.class);
	@Autowired
	private CacheService cacheService;
	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private MergePayOrderService mergePayOrderService;

	public boolean uploadPayOrder(PayOrder payOrder) {
		return uploadPayOrder(Lists.newArrayList(payOrder), false, null);
	}

	public boolean uploadPayOrder(MergePayOrder mergePayOrder) {
		List<PayOrder> payOrderList = mergePayOrderService.queryPayOrderByMergePayId(mergePayOrder.getId());
		return uploadPayOrder(Lists.newArrayList(payOrderList), true, mergePayOrder);
	}

	public boolean uploadPayOrder(List<PayOrder> payOrderList, boolean isMergePay, MergePayOrder mergePayOrder) {
		boolean isSuccess = false;
		List<CmsPayOrder> CmsPayOrderList = Lists.newArrayList();
		for (PayOrder payOrder : payOrderList) {
			CmsPayOrder cmsPayOrder = createCmsPayOrder(payOrder, isMergePay, mergePayOrder);
			CmsPayOrderList.add(cmsPayOrder);
		}
		String data = JSON.toJSONString(CmsPayOrderList, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
		try {
			save(payOrderList, isMergePay, mergePayOrder, data);
		} catch (Exception e) {
			LOGGER.error("新增接口调用日志失败：", e);
			return isSuccess;
		}
		return true;
	}

	/**
	 * 创建cms付款单信息
	 * 
	 * @param payOrder
	 *            付款单
	 * @param isMergePay
	 *            是否合并
	 * @param mergePayOrder
	 *            合并付款单
	 * @return
	 */
	private CmsPayOrder createCmsPayOrder(PayOrder payOrder, boolean isMergePay, MergePayOrder mergePayOrder) {
		CmsPayOrder cmsPayOrder = new CmsPayOrder();
		cmsPayOrder.setDocument_no(payOrder.getPayNo());
		cmsPayOrder.setPrint_row(payOrder.getMergePayNo());
		cmsPayOrder.setSupplier_code(payOrder.getAttachedNumbe());
		BaseSubject payerSubject = cacheService.getSubjectById(payOrder.getPayer(), CacheKeyConsts.BUSI_UNIT);
		cmsPayOrder.setInvoice_title(payerSubject.getChineseName());
		cmsPayOrder.setProject_name(cacheService.showProjectNameById(payOrder.getProjectId()));
		cmsPayOrder.setPayment_type(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_TYPE, payOrder.getPayType() + ""));
		cmsPayOrder.setCurrency(BaseConsts.CURRENCY_UNIT_MAP.get(payOrder.getCurrnecyType()));
		cmsPayOrder
				.setPay_type(ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_WAY, payOrder.getPayWay() + ""));
		cmsPayOrder.setAmount(payOrder.getPayAmount());
		BaseAccount baseAccount = cacheService.getAccountById(payOrder.getPayAccountId());
		if (baseAccount != null) {
			cmsPayOrder.setP_account_name(baseAccount.getAccountor());
			cmsPayOrder.setP_account_bank(baseAccount.getBankName());
			cmsPayOrder.setP_account(baseAccount.getAccountNo());
			cmsPayOrder.setBank_city(baseAccount.getBankAddress());
		}
		if (isMergePay == true && null != mergePayOrder) {
			cmsPayOrder.setLatest_payment_date(
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, mergePayOrder.getRequestPayTime()));
		} else {
			cmsPayOrder.setLatest_payment_date(
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, payOrder.getRequestPayTime()));
		}
		cmsPayOrder.setApply_user(payOrder.getCreator());
		cmsPayOrder.setCreate_time(DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, payOrder.getCreateAt()));
		return cmsPayOrder;
	}

	private InvokeLog save(List<PayOrder> payOrderList, boolean isMergePay, MergePayOrder mergePayOrder, String data)
			throws Exception {
		InvokeLog invokeLog = new InvokeLog();
		if (StringUtils.isNotBlank(data)) {
			invokeLog.setContent(data);
		}
		invokeLog.setInvokeType(InvokeTypeEnum.CMS_PAY_ORDER.getType());
		invokeLog.setInvokeMode(BaseConsts.TWO); // 2-异步
		invokeLog.setModuleType(BaseConsts.NINE); // 9-资金
		if (isMergePay == true) {
			invokeLog.setBillNo(mergePayOrder.getMergePayNo()); // 单据号
			invokeLog.setBillType(BaseConsts.SIX); // 6-合并付款单
		} else {
			invokeLog.setBillNo(payOrderList.get(0).getPayNo());// 单据号
			invokeLog.setBillType(BaseConsts.FIVE); // 5-付款单
		}
		invokeLog.setProvider(BaseConsts.FOUR); // 4-cms
		invokeLog.setConsumer(BaseConsts.ONE); // 1-scfs
		invokeLog.setIsSuccess(BaseConsts.ZERO); // 0-未调用
		invokeLog.setTryAgainFlag(BaseConsts.ONE); // 1-可重新调用
		invokeLogService.add(invokeLog);
		return invokeLog;
	}

}
