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
import com.scfs.domain.api.pms.dto.res.PmsPayConfirmHttpResDto;
import com.scfs.service.api.pms.PmsPayConfirmService;

/**
 * Created by Administrator on 2017年5月2日.
 */
@RestController
public class PmsPayConfirmController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayConfirmController.class);

	@Autowired
	private PmsPayConfirmService pmsPayConfirmService;

	@RequestMapping(value = RpcUrlConsts.API_PMS_PAY_PURCHASE_CONFIRM, method = RequestMethod.POST)
	public PmsPayConfirmHttpResDto doPmsPayConfirm(PmsHttpReqDto req) {
		PmsPayConfirmHttpResDto pmsPayConfirmHttpResDto = new PmsPayConfirmHttpResDto();
		LOGGER.info("[pms]pms请款单付款确认接口请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		pmsPayConfirmHttpResDto.setFlag(BaseConsts.FLAG_YES);
		try {
			pmsPayConfirmHttpResDto = pmsPayConfirmService.doPmsPayConfirm(req);
		} catch (Exception e) {
			LOGGER.error("[pms]pms请款单付款确认接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			pmsPayConfirmHttpResDto.setMsg("[pms]pms请款单付款确认接口请求失败");
		}
		return pmsPayConfirmHttpResDto;
	}
}
