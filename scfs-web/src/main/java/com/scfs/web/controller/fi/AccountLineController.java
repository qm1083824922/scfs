package com.scfs.web.controller.fi;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.AccountLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountLineResDto;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.AccountLineService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: AccountLineController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月20日			Administrator
 *
 * </pre>
 */
@Controller
public class AccountLineController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(AccountLineController.class);

	@Autowired
	AccountLineService accountLineService;

	@RequestMapping(value = BusUrlConsts.ADD_ACCOUNT_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addLine(AccountLine req) {
		BaseResult bResult = new BaseResult();
		try {
			bResult = accountLineService.createAccountLine(req);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增财务科目失败[{}]", JSONObject.toJSON(req), e);
			bResult.setMsg("新增财务科目异常，请稍后重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_ACCOUNT_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteLine(AccountLine req) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = accountLineService.deleteAccountLineById(req);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除财务科目失败[{}]", JSONObject.toJSON(req), e);
			baseResult.setMsg("删除财务科目异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_ACCOUNT_LINE, method = RequestMethod.POST)
	@ResponseBody
	public Result<AccountLineResDto> detailAccountLine(int id) {
		Result<AccountLineResDto> result = new Result<AccountLineResDto>();
		try {
			result = accountLineService.detailAccountLineById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览财务科目失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("浏览财务科目异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_ACCOUNT_LINE, method = RequestMethod.POST)
	@ResponseBody
	public Result<AccountLineResDto> editAccountLine(int id) {
		Result<AccountLineResDto> result = new Result<AccountLineResDto>();
		try {
			result = accountLineService.editAccountLineById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑财务科目失败[{}]", JSONObject.toJSON(id), e);
			result.setMsg("编辑财务科目异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ACCOUNT_LINE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AccountLineResDto> queryAccountLineByCond(AccountLineSearchReqDto req) {
		PageResult<AccountLineResDto> pageResult = new PageResult<AccountLineResDto>();
		try {
			pageResult = accountLineService.queryAccountLineByCond(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询财务科目失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询财务科目异常，请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_ACCOUNT_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateAccountLine(AccountLine req) {
		BaseResult bResult = new BaseResult();
		try {
			bResult = accountLineService.updateAccountLineById(req);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新财务科目失败[{}]", JSONObject.toJSON(req), e);
			bResult.setMsg("更新财务科目异常，请稍后重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_ACCOUNT_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitAccountLine(int id) {
		BaseResult bResult = new BaseResult();
		try {
			bResult = accountLineService.submitAccountLineById(id);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交财务科目失败[{}]", JSONObject.toJSON(id), e);
			bResult.setMsg("提交财务科目异常，请稍后重试");
		}
		return bResult;
	}

	/**
	 * 导出excel
	 *
	 * @param model
	 * @param AccountBookSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_ACCOUNTLINE, method = RequestMethod.GET)
	public String exportPoExcel(ModelMap model, AccountLineSearchReqDto req) {
		List<AccountLineResDto> result = accountLineService.queryListByCond(req);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("accountLineList", result);
		} else {
			model.addAttribute("accountLineList", new ArrayList<AccountLineResDto>());
		}
		return "export/fi/accountLine_list";
	}

}
