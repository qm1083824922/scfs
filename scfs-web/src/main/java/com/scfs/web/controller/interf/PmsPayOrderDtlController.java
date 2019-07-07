package com.scfs.web.controller.interf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.domain.api.pms.entity.PmsPayOrderDtl;
import com.scfs.domain.interf.dto.PmsPoDtlSearchReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.interf.PmsPayOrderDtlService;

/**
 * <pre>
 * 
 *  File: PmsPayOrderDtlController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月19日			Administrator
 *
 * </pre>
 */

@RestController
public class PmsPayOrderDtlController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayOrderDtlController.class);

	@Autowired
	PmsPayOrderDtlService pmsPayOrderDtlService;

	@RequestMapping(value = BusUrlConsts.DETAIL_PMS_PAY_ORDER_DTL, method = RequestMethod.POST)
	public PageResult<PmsPayOrderDtl> queryResultsByTitleId(PmsPoDtlSearchReqDto req) {
		PageResult<PmsPayOrderDtl> pageResult = new PageResult<PmsPayOrderDtl>();
		try {
			pageResult = pmsPayOrderDtlService.queryResultsByTitleId(req);
		} catch (Exception e) {
			LOGGER.error("查询pms请款单明细失败[{}]: {}", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询pms请款单明细失败,请稍后重试");
		}
		return pageResult;
	}

}
