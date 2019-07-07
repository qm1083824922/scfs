package com.scfs.web.controller.interf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.interf.dto.PmsPayOrderTitleResDto;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.interf.PmsPayOrderTitleService;

/**
 * <pre>
 * 
 *  File: PmsPayOrderTitleController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月19日			Administrator
 *
 * </pre>
 */

@RestController
public class PmsPayOrderTitleController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayOrderTitleController.class);

	@Autowired
	PmsPayOrderTitleService pmsPayOrderTitleService;

	@RequestMapping(value = BusUrlConsts.QUERY_PMS_PAY_ORDER_TITLE, method = RequestMethod.POST)
	public PageResult<PmsPayOrderTitleResDto> queryResultsByCon(PmsPoTitleSearchReqDto req) {
		PageResult<PmsPayOrderTitleResDto> pageResult = new PageResult<PmsPayOrderTitleResDto>();
		try {
			pageResult = pmsPayOrderTitleService.queryResultByCond(req);
		} catch (Exception e) {
			LOGGER.error("查询pms请款单失败[{}]: {}", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询pms请款单失败,请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.DEAL_PMS_PAY_ORDER, method = RequestMethod.POST)
	public BaseResult dealPmsPayOrder(Integer id) {
		BaseResult baseResult = new BaseResult();
		try {
			pmsPayOrderTitleService.dealPmsPayOrder(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
			pmsPayOrderTitleService.dealFail(id, e.getMsg());
		} catch (Exception e) {
			LOGGER.error("处理pms请款单失败[{}]: {}", JSONObject.toJSON(id), e);
			baseResult.setMsg("系统异常，请稍后再试");
			pmsPayOrderTitleService.dealFail(id, "系统异常，请稍后再试");
		}
		return baseResult;
	}
}
