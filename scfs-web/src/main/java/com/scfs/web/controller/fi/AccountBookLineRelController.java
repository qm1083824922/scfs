package com.scfs.web.controller.fi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.AcctBookLineRelSearchReqDto;
import com.scfs.domain.fi.dto.resp.AcctBookLineRelResDto;
import com.scfs.domain.fi.entity.AccountBookLineRel;
import com.scfs.domain.result.PageResult;
import com.scfs.common.exception.BaseException;
import com.scfs.service.fi.AccountBookLineRelService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: AccountBookLineRelControoler.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月20日			Administrator
 *
 * </pre>
 */
@RestController
public class AccountBookLineRelController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AccountBookLineRelController.class);

	@Autowired
	AccountBookLineRelService relService;

	@RequestMapping(value = BusUrlConsts.ADD_ACCOUNT_BOOK_LINE_REL, method = RequestMethod.POST)
	public BaseResult addRel(AccountBookLineRel rel) {
		BaseResult bResult = new BaseResult();
		try {
			relService.createRel(rel);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增帐套科目关系失败[{}]: {}", JSONObject.toJSON(rel), e);
			bResult.setMsg("新增帐套科目关系失败,请稍后重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.ADD_ACCOUNT_BOOK_LINE_REL_BATCH, method = RequestMethod.POST)
	public BaseResult batchAddRel(@RequestBody BaseReqDto req) {
		BaseResult bResult = new BaseResult();
		if (req.getId() == null || req.getIds() == null) {
			bResult.setMsg("参数有误，请核查");
			return bResult;
		}
		try {
			relService.batchCreateRel(req);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量新增帐套科目关系失败[{}]: {}", JSONObject.toJSON(req), e);
			bResult.setMsg("批量新增帐套科目关系失败,请稍后重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.INVALID_ACCOUNT_BOOK_LINE_REL, method = RequestMethod.POST)
	public BaseResult deleteRel(@RequestParam(value = "id") int id) {
		BaseResult baseResult = new BaseResult();
		try {
			relService.deleteRel(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("作废帐套科目关系失败[{}]: {}", JSONObject.toJSON(id), e);
			baseResult.setMsg("作废帐套科目关系失败,请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.INVALID_ACCOUNT_BOOK_LINE_REL_BATCH, method = RequestMethod.POST)
	public BaseResult batchDeleteRel(@RequestBody BaseReqDto req) {
		BaseResult bResult = new BaseResult();
		if (req.getIds() == null) {
			bResult.setMsg("参数有误，请核查");
			return bResult;
		}
		try {
			relService.batchDeleteRel(req);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量作废帐套科目关系失败[{}]: {}", JSONObject.toJSON(req), e);
			bResult.setMsg("批量作废帐套科目关系失败,请稍后重试");
		}

		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ACCOUNT_BOOK_LINE_REL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AcctBookLineRelResDto> queryRelByBookId(AcctBookLineRelSearchReqDto reqDto) {
		PageResult<AcctBookLineRelResDto> pageResult = new PageResult<AcctBookLineRelResDto>();
		try {
			pageResult = relService.queryRelByBookId(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询帐套科目关系失败[{}]: {}", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询帐套科目关系失败,请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ALL_ACCOUNT_BOOK_LINE_REL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AccountBookLineRel> queryAllRelByBookId(AcctBookLineRelSearchReqDto reqDto) {
		PageResult<AccountBookLineRel> pageResult = new PageResult<AccountBookLineRel>();
		try {
			pageResult = relService.queryAllUseLastRelByBookId(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询帐套科目关系失败[{}]: {}", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询帐套科目关系失败,请稍后重试");
		}
		return pageResult;
	}
}
