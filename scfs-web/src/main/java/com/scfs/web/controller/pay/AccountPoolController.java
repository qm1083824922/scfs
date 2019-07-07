package com.scfs.web.controller.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.pay.dto.req.AccountPoolFundReqDto;
import com.scfs.domain.pay.dto.req.AccountPoolReqDto;
import com.scfs.domain.pay.dto.resq.AccountPoolFundResDto;
import com.scfs.domain.pay.dto.resq.AccountPoolResDto;
import com.scfs.domain.pay.entity.AccountPool;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.pay.AccountPoolFundService;
import com.scfs.service.pay.AccountPoolService;

/**
 * <pre>
 * 
 *  File: AccountPoolController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年09月25日				Administrator
 *
 * </pre>
 */

@Controller
public class AccountPoolController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AccountPoolController.class);
	@Autowired
	private AccountPoolService accountPoolService;
	@Autowired
	private AccountPoolFundService accountPoolFundService;

	/**
	 * 获取列表信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_ACCOUNT_POOL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AccountPoolResDto> queryAccountPoolResultsByCon(AccountPoolReqDto reqDto) {
		PageResult<AccountPoolResDto> pageResult = new PageResult<AccountPoolResDto>();
		try {
			pageResult = accountPoolService.queryAccountPoolResultsByCon(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询资金池信息失败[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询资金池信息异常，请稍后重试");
		}
		return pageResult;
	}

	/***
	 * 资金池详情
	 * 
	 * @param account
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_ACCOUNT_POOL, method = RequestMethod.POST)
	@ResponseBody
	public Result<AccountPoolResDto> detailAccountPool(AccountPool account) {
		Result<AccountPoolResDto> result = new Result<AccountPoolResDto>();
		try {
			result = accountPoolService.detailAccountPool(account);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览资金池信息失败[{}]", JSONObject.toJSON(account), e);
			result.setMsg("浏览资金池信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 资金明细
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_ACCOUNT_POOL_FUND, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AccountPoolFundResDto> queryAccountPoolResultsByCon(AccountPoolFundReqDto reqDto) {
		PageResult<AccountPoolFundResDto> pageResult = new PageResult<AccountPoolFundResDto>();
		try {
			pageResult = accountPoolFundService.queryAccountPoolResultsByCon(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询资金明细信息失败[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询资金明细信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 刷新资金池数据
	 */
	@RequestMapping(value = BusUrlConsts.REFRESH_ACCOUNT_POOL, method = RequestMethod.GET)
	@ResponseBody
	public BaseResult refreshAccountPool() {
		BaseResult result = new BaseResult();
		try {
			accountPoolService.refreshAccountPool();
		} catch (BaseException e) {
			LOGGER.error("刷新资金池数据异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("刷新资金池数据异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}
}
