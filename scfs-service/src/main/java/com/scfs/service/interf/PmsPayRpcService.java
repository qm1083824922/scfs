package com.scfs.service.interf;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.rpc.pms.entity.Pay;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年12月19日.
 */
@Service
public class PmsPayRpcService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayRpcService.class);
	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private BaseSubjectDao baseSubjectDao;
	@Autowired
	private BaseAccountDao baseAccountDao;

	public boolean unPassPayOrder(PayOrder payOrder, PmsPayOrderTitle pmsPayOrderTitle) {
		return sendPayOrder(payOrder, pmsPayOrderTitle, false);
	}

	public boolean passPayOrder(PayOrder payOrder, PmsPayOrderTitle pmsPayOrderTitle) {
		return sendPayOrder(payOrder, pmsPayOrderTitle, true);
	}

	public boolean sendPayOrder(PayOrder payOrder, PmsPayOrderTitle pmsPayOrderTitle, boolean passFlag) {
		boolean isSuccess = false;
		Pay pay = createPay(payOrder, pmsPayOrderTitle, passFlag);
		String data = JSON.toJSONString(pay, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty);
		try {
			save(payOrder, data);
		} catch (Exception e) {
			LOGGER.error("新增接口调用日志失败：", e);
			return isSuccess;
		}
		return true;
	}

	private Pay createPay(PayOrder payOrder, PmsPayOrderTitle pmsPayOrderTitle, boolean passFlag) {
		Pay pay = new Pay();
		pay.setPay_no(payOrder.getPayNo());
		if (passFlag == true) {
			pay.setType(BaseConsts.TWO); // 2-付款成功
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

			Integer payer = payOrder.getPayer();
			BaseSubject busiUnit = baseSubjectDao.queryEntityById(payer);
			if (null == busiUnit) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单位[{}]不存在", payer);
			}
			pay.setCorporation_code(busiUnit.getBusinessUnitCode());
			pay.setCorporation_name(busiUnit.getChineseName());
			BaseAccount baseAccount = baseAccountDao.queryEntityById(payOrder.getPaymentAccount());
			if (null == baseAccount) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款账户[{}]不存在", payOrder.getPaymentAccount());
			}
			pay.setPay_bank(baseAccount.getBankName());
			pay.setPay_no(baseAccount.getAccountNo());
			pay.setCurrency_money(payOrder.getRealPayAmount());
			pay.setCurrency_type(BaseConsts.CURRENCY_UNIT_MAP.get(payOrder.getRealCurrencyType()));
			pay.setCurrency_rate(payOrder.getPayRate());
			if (!StringUtils.isEmpty(payOrder.getCmsPayer())) {
				pay.setCreate_user(payOrder.getCmsPayer());
			} else {
				pay.setCreate_user(payOrder.getConfirmor());
			}
			pay.setCreate_time(payOrder.getConfirmorAt());
		} else {
			pay.setType(BaseConsts.ONE); // 1-驳回
			pay.setCreate_user(ServiceSupport.getUser().getChineseName());
			pay.setCreate_time(new Date());
		}
		pay.setPay_sn(payOrder.getAttachedNumbe());
		return pay;
	}

	private InvokeLog save(PayOrder payOrder, String data) throws Exception {
		InvokeLog invokeLog = new InvokeLog();
		if (StringUtils.isNotBlank(data)) {
			invokeLog.setContent(data);
		}
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PAY_ORDER_UPLOAD.getType());
		invokeLog.setInvokeMode(BaseConsts.TWO); // 2-异步
		invokeLog.setModuleType(BaseConsts.NINE); // 9-资金
		invokeLog.setBillType(BaseConsts.FIVE); // 5-付款单
		invokeLog.setBillId(payOrder.getId());
		invokeLog.setBillNo(payOrder.getPayNo());
		invokeLog.setBillDate(payOrder.getRequestPayTime());
		invokeLog.setProvider(BaseConsts.THREE); // 2-pms
		invokeLog.setConsumer(BaseConsts.ONE); // 1-scfs
		invokeLog.setIsSuccess(BaseConsts.ZERO); // 0-未调用
		invokeLog.setTryAgainFlag(BaseConsts.ONE); // 1-可重新调用
		invokeLogService.add(invokeLog);
		return invokeLog;
	}
}
