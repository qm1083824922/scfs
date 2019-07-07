package com.scfs.service.api.pms;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.api.pms.PmsPayDao;
import com.scfs.dao.api.pms.PmsPayDtlDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsWaitPayHttpResDto;
import com.scfs.domain.api.pms.entity.PmsPay;
import com.scfs.domain.api.pms.entity.PmsPayDtl;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.api.pms.entity.PmsPayWait;
import com.scfs.domain.api.pms.entity.PmsSeries;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.base.subject.BaseSubjectService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.interf.PMSSupplierBindService;
import com.scfs.service.project.ProjectItemService;

/**
 * <pre>
 *  pms同步请款待付款接口 
 *  File: PmsSyncWaitPayService.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2017年05月05日                                    Administrator
 *
 * </pre>
 */
@Service
public class PmsSyncWaitPayService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncWaitPayService.class);

	@Autowired
	private InvokeConfig invokeConfig;

	@Autowired
	private InvokeLogService invokeLogService;

	@Autowired
	private PmsPayDao pmsPayDao;

	@Autowired
	private PmsPayDtlDao pmsPayDtlDao;

	@Autowired
	private PmsSeriesService pmsSeriesService;

	@Autowired
	ProjectItemService projectItemService;

	@Autowired
	PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private BaseSubjectService baseSubjectService;
	@Autowired
	private PMSSupplierBindService pmsSupplierBindService;

	@IgnoreTransactionalMark
	public PmsWaitPayHttpResDto doPmsSyncPay(PmsHttpReqDto req) {

		// pms日志
		PmsWaitPayHttpResDto res = new PmsWaitPayHttpResDto();
		res.setFlag(BaseConsts.FLAG_YES);
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);
		invokeLog.setCreateAt(new Date());
		invokeLog.setConsumer(BaseConsts.THREE);
		invokeLog.setProvider(BaseConsts.ONE);
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PAY_PURCHASE.getType()); // pms同步请款待付款接口
		invokeLog.setInvokeMode(BaseConsts.TWO);
		invokeLogService.add(invokeLog);

		// 接口调用状态描述
		PmsSeries pmsSeries = pmsSeriesService.createPmsSeries(BaseConsts.THREE);
		try {
			// pms调用scfs接口
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					res.setFlag(BaseConsts.FLAG_NO);
					res.setMsg("请求非法: 签名校验出错.");
					invokeLog.setReturnMsg(JSON.toJSONString(res));
					invokeLogService.invokeError(invokeLog);
					return res;
				}
			}

			PmsPayWait pmsPayWait = JSON.parseObject(data, PmsPayWait.class);
			if (pmsPayWait == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数格式有误.");
			}
			if (pmsPayWait.getPms_pay() == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误:请求请款单不能为空.");
			}
			res.setPay_sn(pmsPayWait.getPms_pay().getPay_sn());

			try {
				res = checkData(pmsPayWait, res); // 接口前置检查
			} catch (BaseException e) {
				res.setFlag(BaseConsts.FLAG_NO);
				res.setMsg(e.getMsg());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
				return res;
			}
			invokeLog.setReturnMsg(JSON.toJSONString(res));
			invokeLogService.invokeSuccess(invokeLog);
			if (res.getFlag().equals(BaseConsts.FLAG_YES)) {
				createPmsPay(pmsPayWait, pmsSeries, res);
				res.setMsg("处理成功!");
			}
		} catch (Exception e) {
			LOGGER.error("[pms]pms同步请款待付款接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			res.setFlag(BaseConsts.FLAG_NO);
			res.setMsg("[pms]pms同步请款待付款接口请求失败");
			invokeLog.setReturnMsg(JSON.toJSONString(res));
			invokeLogService.invokeException(invokeLog, e);
		}
		return res;
	}

	private void createPmsPay(PmsPayWait pmsPayWait, PmsSeries pmsSeries, PmsWaitPayHttpResDto returnDto) {
		StringBuilder return_msg = new StringBuilder();
		PmsPay pmsPayInfo = pmsPayWait.getPms_pay();
		pmsPayInfo.setCreateAt(new Date());
		pmsPayInfo.setPmsSeriesId(pmsSeries.getId());
		pmsPayInfo.setFlag(BaseConsts.ZERO); // 0-接收成功
		pmsPayInfo.setDealFlag(BaseConsts.ONE); // 1-待处理
		Integer status = pmsPayDao.insert(pmsPayInfo);
		if (status <= BaseConsts.ZERO) {
			return_msg.append("插入pms请款待付款头信息异常.");
		}
		if (pmsPayWait.getPms_pay_dtl() != null) {
			Integer id = pmsPayInfo.getId();
			for (PmsPayDtl pmsPayDtl : pmsPayWait.getPms_pay_dtl()) {
				pmsPayDtl.setPmsPayId(id);
				pmsPayDtl.setCreateAt(new Date());
				Integer result = pmsPayDtlDao.insert(pmsPayDtl);
				if (result < BaseConsts.ZERO) {
					return_msg.append("批量插入pms请款待付款明细信息异常.");
				}
			}
		}
		if (return_msg.length() > BaseConsts.ZERO) {
			returnDto.setFlag(BaseConsts.FLAG_NO);
			returnDto.setMsg(return_msg.toString());
		}
	}

	private PmsWaitPayHttpResDto checkData(PmsPayWait pmsPayWait, PmsWaitPayHttpResDto result) {
		StringBuilder return_msg = new StringBuilder();

		PMSSupplierBind pmsSupplierBind = null;
		PmsPay pmsPay = pmsPayWait.getPms_pay();
		if (StringUtils.isEmpty(pmsPay.getPay_sn())) {
			result.setPay_sn(pmsPay.getPay_sn());
			return_msg.append("请求参数有误: 请款单号不能为空.");
		}

		if (StringUtils.isEmpty(pmsPay.getStatus())) {
			result.setPay_sn(pmsPay.getPay_sn());
			return_msg.append("请求参数有误:状态不能为空.");
		}
		if (pmsPay.getStatus() != BaseConsts.ZERO && pmsPay.getStatus() != BaseConsts.ONE) {
			result.setPay_sn(pmsPay.getPay_sn());
			return_msg.append("请求参数有误:请求状态不正确空.");
		}

		if (StringUtils.isEmpty(pmsPay.getPay_create_time())) {
			result.setPay_sn(pmsPay.getPay_sn());
			return_msg.append("请求参数有误: 日期不能为空.");
		}
		if (StringUtils.isEmpty(pmsPay.getCurrency_type())) {
			result.setPay_sn(pmsPay.getPay_sn());

			return_msg.append("请求参数有误: 币种不能为空.");
		} else {
			Integer currencyType = PmsPayOrderTitle.convertCurrencyType(pmsPay.getCurrency_type());
			if (currencyType == null) {
				result.setPay_sn(pmsPay.getPay_sn());
				return_msg.append("请求参数有误: SCFS系统暂不支持币种" + pmsPay.getCurrency_type() + "，可以选择的币种有人民币，美元，港元.");
			}
		}
		if (StringUtils.isEmpty(pmsPay.getProvider_sn())) {
			result.setPay_sn(pmsPay.getPay_sn());
			return_msg.append("请求参数有误: 供应商不能为空.");
		}
		if (StringUtils.isEmpty(pmsPay.getDeduction_money())
				&& DecimalUtil.ge(BigDecimal.ZERO, pmsPay.getDeduction_money())) {
			result.setPay_sn(pmsPay.getPay_sn());
			return_msg.append("请求参数有误: 抵扣金额不能为空并且不能小于0.");
		}
		if (StringUtils.isEmpty(pmsPay.getUnique_number())) {
			result.setPay_sn(pmsPay.getPay_sn());
			return_msg.append("请求参数有误: 流水号不能为空.");
		}
		List<PmsPayDtl> pmsPayDtlList = pmsPayWait.getPms_pay_dtl();
		if (pmsPay.getStatus().equals(BaseConsts.ZERO)) {
			if (CollectionUtils.isEmpty(pmsPayDtlList)) {
				result.setPay_sn(pmsPay.getPay_sn());
				return_msg.append("请求参数有误: 待付款明细不能为空.");
			}
		}
		if (return_msg.length() < BaseConsts.ONE) {
			if (pmsPayDtlList != null) {
				for (PmsPayDtl pmsPayDtl : pmsPayDtlList) {
					QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
					querySubjectReqDto.setPmsSupplierCode(pmsPayDtl.getAccount_sn());
					querySubjectReqDto.setSubjectType(BaseConsts.ONE);
					List<BaseSubject> custs = baseSubjectService.querySubTypeAndPmsSupplier(querySubjectReqDto);
					if (CollectionUtils.isEmpty(custs) || custs.size() > 1) {
						return_msg.append("PMS结算对象供应商编码【" + pmsPayDtl.getAccount_sn() + "】不存在或者不唯一");
					} else {
						// 通过pms供应商查询对应的项目与供应商
						PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
						pMSSupplierBindReqDto.setPmsSupplierNo(pmsPay.getProvider_sn());
						pMSSupplierBindReqDto.setBusinessUnit(custs.get(BaseConsts.ZERO).getId());
						pmsSupplierBind = pmsSupplierBindService.queryPmsBySuppNoAndBui(pMSSupplierBindReqDto);
					}
					if (pmsSupplierBind == null) {
						result.setPay_sn(pmsPay.getPay_sn());
						return_msg.append("请求参数有误: 该供应商无质押项目.");
					}
					if (StringUtils.isEmpty(pmsPayDtl.getSku())) {
						return_msg.append("请求参数有误: sku不能为空.");
					}
					if (StringUtils.isEmpty(pmsPayDtl.getPay_quantity())
							&& DecimalUtil.ge(BigDecimal.ZERO, pmsPayDtl.getPay_quantity())) {
						return_msg.append("请求参数有误: 数量不能为空并且不能小于0.");
					}
					if (StringUtils.isEmpty(pmsPayDtl.getDeal_price())) {
						return_msg.append("请求参数有误: 价格不能为空.");
					}
					if (StringUtils.isEmpty(pmsPayDtl.getPurchase_sn())) {
						return_msg.append("请求参数有误: 采购单号不能为空.");
					}
				}
			}
		}

		if (return_msg.length() > 0) {
			result.setFlag(BaseConsts.FLAG_NO);
			result.setPay_sn(pmsPay.getPay_sn());
			result.setMsg(return_msg.toString());
		}
		return result;
	}
}
