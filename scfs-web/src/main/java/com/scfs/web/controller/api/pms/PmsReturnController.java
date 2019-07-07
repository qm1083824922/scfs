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
import com.scfs.domain.api.pms.dto.res.PmsReturnHttpResDto;
import com.scfs.service.api.pms.PmsReturnService;

/**
 * Created by Administrator on 2017年6月22日.
 */
@RestController
public class PmsReturnController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayConfirmController.class);

	@Autowired
	private PmsReturnService pmsReturnService;

	@RequestMapping(value = RpcUrlConsts.API_PMS_RETURN, method = RequestMethod.POST)
	public PmsReturnHttpResDto doPmsReturn(PmsHttpReqDto req) {
		PmsReturnHttpResDto pmsReturnHttpResDto = new PmsReturnHttpResDto();
		LOGGER.info("[pms]pms退货单申请接口请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		pmsReturnHttpResDto.setFlag(BaseConsts.FLAG_YES);
		try {
			pmsReturnHttpResDto = pmsReturnService.doPmsReturn(req);
		} catch (Exception e) {
			LOGGER.error("[pms]pms退货单申请接口接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			pmsReturnHttpResDto.setMsg("[pms]pms退货单申请接口接口请求失败");
		}
		return pmsReturnHttpResDto;
	}
}
