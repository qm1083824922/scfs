package com.scfs.web.controller.api.pms;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.RpcUrlConsts;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsStoreInHttpResDto;
import com.scfs.service.api.pms.PmsSyncBillInStoreService;

@RestController
public class PmsSynBillInStoreController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSynBillInStoreController.class);

	@Autowired
	PmsSyncBillInStoreService pmsSyncBillInStoreService;

	@RequestMapping(value = RpcUrlConsts.API_PMS_STORE_IN, method = RequestMethod.POST)
	public List<PmsStoreInHttpResDto> doPmsSyncPay(PmsHttpReqDto req) {
		List<PmsStoreInHttpResDto> pmsHttpResDto = new ArrayList<PmsStoreInHttpResDto>();
		LOGGER.info("[pms]pms同步入库接口请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		try {
			pmsHttpResDto = pmsSyncBillInStoreService.doPmsSyncBillInStore(req);
		} catch (Exception e) {
			LOGGER.error("[pms]pms同步入库接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
		}
		return pmsHttpResDto;
	}
}
