package com.scfs.web.controller.api.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.RpcUrlConsts;
import com.scfs.domain.api.cms.dto.req.CmsHttpReqDto;
import com.scfs.domain.api.cms.dto.res.CmsPayConfirmHttpResDto;
import com.scfs.service.api.cms.CmsPayConfirmService;

@RestController
public class CmsPayConfirmController {
	private final static Logger LOGGER = LoggerFactory.getLogger(CmsPayConfirmController.class);

	@Autowired
	private CmsPayConfirmService cmsPayConfirmService;

	@RequestMapping(value = RpcUrlConsts.API_CMS_PAY_ORDER_CONFIRM, method = RequestMethod.POST)
	public CmsPayConfirmHttpResDto doCmsPayConfirm(CmsHttpReqDto req) {
		CmsPayConfirmHttpResDto cmsPayConfirmHttpResDto = new CmsPayConfirmHttpResDto();
		LOGGER.info("[cms]cms付款确认接口请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		cmsPayConfirmHttpResDto.setFlag(BaseConsts.FLAG_YES);
		try {
			cmsPayConfirmHttpResDto = cmsPayConfirmService.doCmsPayConfirm(req);
		} catch (Exception e) {
			LOGGER.error("[cms]cms付款确认接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			cmsPayConfirmHttpResDto.setMsg("[cms]cms付款确认接口请求失败");
		}
		return cmsPayConfirmHttpResDto;
	}
}
