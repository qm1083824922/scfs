package com.scfs.web.controller.fi;

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
import com.scfs.domain.fi.dto.req.AccountStatementSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountStatementTitleResDto;
import com.scfs.domain.fi.entity.AccountStatementTitle;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.AccountStatementTitleService;

/**
 * <pre>
 * 
 *  File: AccountStatementTitleController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月22日			Administrator
 *
 * </pre>
 */

@RestController
public class AccountStatementTitleController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AccountStatementTitleController.class);

	@Autowired
	AccountStatementTitleService accountStatementTitleService;

	@RequestMapping(value = BusUrlConsts.ADD_ACCOUNT_STATEMENT_TITLE, method = RequestMethod.POST)
	public Result<Integer> createAccountStatementTitle(AccountStatementTitle accountStatementTitle) {
		Result<Integer> result = new Result<Integer>();
		try {
			int id = accountStatementTitleService.createAccountStatementTitle(accountStatementTitle);
			if (id <= 0) {
				result.setMsg("新增队长范异常，请稍后重试");
			}
			result.setItems(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增对账单失败[{}]", JSONObject.toJSON(accountStatementTitle), e);
			result.setMsg("新增对账单异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_ACCOUNT_STATEMENT_TITLE, method = RequestMethod.POST)
	public BaseResult updateById(AccountStatementTitle accountStatementTitle) {
		BaseResult baseResult = new BaseResult();
		try {
			accountStatementTitleService.updateById(accountStatementTitle);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改对账单失败[{}]", JSONObject.toJSON(accountStatementTitle), e);
			baseResult.setMsg("修改对账单异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ACCOUNT_STATEMENT_TITLE, method = RequestMethod.POST)
	public PageResult<AccountStatementTitleResDto> queryResultsByCon(AccountStatementSearchReqDto req) {
		PageResult<AccountStatementTitleResDto> pageResult = new PageResult<AccountStatementTitleResDto>();
		try {
			pageResult = accountStatementTitleService.queryResultsByCon(req);
		} catch (Exception e) {
			LOGGER.error("查询对账单失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询对账单异常，请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_ACCOUNT_STATEMENT_TITLE, method = RequestMethod.POST)
	public Result<AccountStatementTitleResDto> detailVoucherResultsById(int id) {
		Result<AccountStatementTitleResDto> result = new Result<AccountStatementTitleResDto>();
		try {
			AccountStatementTitleResDto res = accountStatementTitleService.detailById(id);
			result.setItems(res);
		} catch (Exception e) {
			LOGGER.error("浏览对账单失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("浏览对账单异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_ACCOUNT_STATEMENT_TITLE, method = RequestMethod.POST)
	public Result<AccountStatementTitleResDto> editVoucherResultsById(int id) {
		Result<AccountStatementTitleResDto> result = new Result<AccountStatementTitleResDto>();
		try {
			AccountStatementTitleResDto res = accountStatementTitleService.detailById(id);
			result.setItems(res);
		} catch (Exception e) {
			LOGGER.error("编辑对账单失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("编辑对账单异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_ACCOUNT_STATEMENT_TITLE, method = RequestMethod.POST)
	public BaseResult deleteVoucherById(Integer id) {
		BaseResult baseResult = new BaseResult();
		try {
			accountStatementTitleService.deleteById(id);
		} catch (Exception e) {
			LOGGER.error("删除对账单失败[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("删除对账单常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_ACCOUNT_STATEMENT_TITLE, method = RequestMethod.POST)
	public BaseResult submitVoucherById(int id) {
		BaseResult baseResult = new BaseResult();
		try {
			accountStatementTitleService.submitById(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交对账单失败[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("提交对账单异常，请稍后重试");
		}
		return baseResult;
	}

}
