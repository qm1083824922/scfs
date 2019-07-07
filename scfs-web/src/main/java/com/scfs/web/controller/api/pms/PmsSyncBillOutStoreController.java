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
import com.scfs.domain.api.pms.dto.res.PmsHttpBillStoreOutResDto;
import com.scfs.service.api.pms.PmsSyncBillOutStoreService;

/**
 * <pre>
 * 
 *  File: PmsSynPayController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月03日			Administrator
 *
 * </pre>
 */
@RestController
public class PmsSyncBillOutStoreController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncBillOutStoreController.class);

	@Autowired
	PmsSyncBillOutStoreService pmsSyncBillOutStoreService;

	/**
	 * PMS同步出库单明细(铺货)
	 * 
	 * @return
	 */
	@RequestMapping(value = RpcUrlConsts.API_PMS_STORE_OUT, method = RequestMethod.POST)
	public List<PmsHttpBillStoreOutResDto> doPmsStoreOut(PmsHttpReqDto req) {
		List<PmsHttpBillStoreOutResDto> pmsHttpResDto = new ArrayList<PmsHttpBillStoreOutResDto>();
		LOGGER.info("[pms]pms同步出库单明细(铺货)请求参数：sign=" + req.getKey() + "; data=" + req.getData());
		try {
			pmsHttpResDto = pmsSyncBillOutStoreService.doPmsStoreOut(req);
		} catch (Exception e) {
			LOGGER.error("[pms]pms同步出库单明细(铺货)接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
		}
		return pmsHttpResDto;
	}
}
