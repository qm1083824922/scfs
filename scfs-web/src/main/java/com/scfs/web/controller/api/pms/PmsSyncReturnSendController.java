package com.scfs.web.controller.api.pms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.RpcUrlConsts;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsPurchaseResDto;
import com.scfs.service.api.pms.PmsSyncReturnPurchseSendService;

@RestController
public class PmsSyncReturnSendController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncReturnSendController.class);

	@Autowired
	PmsSyncReturnPurchseSendService pmsSyncReturnPurchseSendService;

	/**
	 * 调用pms退货订单完成发送接口
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = RpcUrlConsts.API_PMS_PURCHASE_SEND, method = RequestMethod.POST)
	public PmsPurchaseResDto doPmsPurchaseSend(PmsHttpReqDto req) {
		PmsPurchaseResDto dto = new PmsPurchaseResDto();
		LOGGER.info("[pms]pms退货订单完成发送接口请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		try {
			dto = pmsSyncReturnPurchseSendService.doPmsPurchase(req);
		} catch (Exception e) {
			LOGGER.error("[pms]pms退货订单完成发送接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
		}
		return dto;
	}
}
