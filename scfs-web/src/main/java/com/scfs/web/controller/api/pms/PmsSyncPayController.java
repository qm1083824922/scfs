package com.scfs.web.controller.api.pms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.RpcUrlConsts;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsPayOrderConfirmHttpResDto;
import com.scfs.domain.api.pms.dto.res.PmsHttpResDto;
import com.scfs.domain.api.pms.dto.res.PmsWaitPayHttpResDto;
import com.scfs.service.api.pms.PmsConfirmService;
import com.scfs.service.api.pms.PmsSyncPayService;
import com.scfs.service.api.pms.PmsSyncWaitPayService;

/**
 * <pre>
 * 
 *  File: PmsSynPayController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月15日			Administrator
 *
 * </pre>
 */
@RestController
public class PmsSyncPayController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncPayController.class);

	@Autowired
	PmsSyncPayService pmsSyncPayService;
	@Autowired
	PmsConfirmService pmsConfirmService;
	@Autowired
	PmsSyncWaitPayService pmsSyncWaitPayService;

	/**
	 * 同步PMS请款单(应收保理)
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = RpcUrlConsts.API_PMS_SYNC_PAY_ORDER, method = RequestMethod.POST)
	public PmsHttpResDto doPmsSyncPay(PmsHttpReqDto req) {
		PmsHttpResDto pmsHttpResDto = new PmsHttpResDto();
		LOGGER.info("[pms]pms同步请款接口请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		pmsHttpResDto.setFlag(BaseConsts.FLAG_YES);
		try {
			pmsHttpResDto = pmsSyncPayService.doPmsSyncPay(req);
		} catch (Exception e) {
			LOGGER.error("[pms]pms同步请款接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			pmsHttpResDto.setMsg("[pms]pms同步请款接口请求失败");
		}
		return pmsHttpResDto;
	}

	/**
	 * PMS付款单确认(应收保理)
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = RpcUrlConsts.API_PMS_PAY_ORDER_CONFIRM, method = RequestMethod.POST)
	public PmsPayOrderConfirmHttpResDto doPmsPayOrderConfirm(PmsHttpReqDto req) {
		PmsPayOrderConfirmHttpResDto pmsPayOrderConfirmHttpResDto = new PmsPayOrderConfirmHttpResDto();
		LOGGER.info("[pms]pms付款单确认(应收保理)接口请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		pmsPayOrderConfirmHttpResDto.setFlag(BaseConsts.FLAG_YES);
		try {
			pmsPayOrderConfirmHttpResDto = pmsConfirmService.doPmsPayOrderConfirm(req);
		} catch (Exception e) {
			LOGGER.error("[pms]pms同步请款接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			pmsPayOrderConfirmHttpResDto.setMsg("[pms]pms付款单确认(应收保理)接口请求失败");
		}
		return pmsPayOrderConfirmHttpResDto;
	}

	/**
	 * pms同步请款待付款接口(融通铺货)
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = RpcUrlConsts.API_PMS_SYNC_WAIT_PAY_ORDER, method = RequestMethod.POST)
	public PmsWaitPayHttpResDto doPmsSyncWaitPay(PmsHttpReqDto req) {
		PmsWaitPayHttpResDto pmsHttpResDto = new PmsWaitPayHttpResDto();
		LOGGER.info("[pms]pms同步请款待付款接口请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		pmsHttpResDto.setFlag(BaseConsts.FLAG_YES);
		try {
			pmsHttpResDto = pmsSyncWaitPayService.doPmsSyncPay(req);
		} catch (Exception e) {
			LOGGER.error("[pms]pms同步请款待付款接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			pmsHttpResDto.setFlag(BaseConsts.FLAG_NO);
			pmsHttpResDto.setMsg("[pms]pms同步请款接口请求失败");
		}
		return pmsHttpResDto;
	}

}
