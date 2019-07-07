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
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.AccountBookSearchReqDto;
import com.scfs.domain.fi.dto.req.AccountLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountBookResDto;
import com.scfs.domain.fi.dto.resp.AccountLineResDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.common.exception.BaseException;
import com.scfs.service.fi.AccountBookService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: AccountBookController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月20日			Administrator
 *
 * </pre>
 */
@Controller
public class AccountBookController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AccountBookController.class);

	@Autowired
	AccountBookService accountBookService;

	@RequestMapping(value = BusUrlConsts.ADD_ACCOUNT_BOOK, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addAccountBook(AccountBook req) {
		Result<Integer> result = new Result<Integer>();
		try {
			result = accountBookService.createAccountBook(req);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增帐套失败[{}]: {}", JSONObject.toJSON(req), e);
			result.setMsg("新增帐套失败,请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_ACCOUNT_BOOK, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteAccountBook(int id) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = accountBookService.deleteAccountBookById(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除帐套失败[{}]: {}", JSONObject.toJSON(id), e);
			baseResult.setMsg("删除帐套失败,请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_ACCOUNT_BOOK, method = RequestMethod.POST)
	@ResponseBody
	public Result<AccountBook> detailAccountBookById(int id) {
		Result<AccountBook> result = new Result<AccountBook>();
		try {
			result = accountBookService.detailAccountBookById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览帐套失败[{}]: {}", JSONObject.toJSON(id), e);
			result.setMsg("浏览帐套失败,请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_ACCOUNT_BOOK, method = RequestMethod.POST)
	@ResponseBody
	public Result<AccountBook> editAccountBookById(int id) {
		Result<AccountBook> result = new Result<AccountBook>();
		try {
			result = accountBookService.editAccountBookById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑帐套失败[{}]: {}", JSONObject.toJSON(id), e);
			result.setMsg("编辑帐套失败,请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ACCOUNT_BOOK, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AccountBookResDto> queryAccountBookByCond(AccountBookSearchReqDto req) {
		PageResult<AccountBookResDto> pageResult = new PageResult<AccountBookResDto>();
		try {
			pageResult = accountBookService.queryAccountBookByCond(req);
		} catch (BaseException e) {
			LOGGER.error("查询帐套失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询帐套失败[{}]: {}", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询帐套失败,请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_ACCOUNT_BOOK, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateAccountBook(AccountBook req) {
		BaseResult bResult = new BaseResult();
		try {
			bResult = accountBookService.updateAccountBookById(req);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新帐套失败[{}]: {}", JSONObject.toJSON(req), e);
			bResult.setMsg("更新帐套失败,请稍后重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_ACCOUNT_BOOK, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitAccountBook(int id) {
		BaseResult bResult = new BaseResult();
		try {
			bResult = accountBookService.submitAccountBookById(id);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交帐套失败[{}]: {}", JSONObject.toJSON(id), e);
			bResult.setMsg("提交帐套失败,请稍后重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.DIVID_ACCOUNT_BOOK, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AccountLineResDto> dividAccountBook(AccountLineSearchReqDto reqDto) {
		PageResult<AccountLineResDto> pageResult = new PageResult<AccountLineResDto>();
		try {
			pageResult = accountBookService.divideAccountBook(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("帐套分配失败[{}]: {}", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("帐套分配失败,请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 导出excel
	 *
	 * @param model
	 * @param AccountBookSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_ACCOUNTBOOK, method = RequestMethod.GET)
	public String exportAbExcel(ModelMap model, AccountBookSearchReqDto req) {
		List<AccountBookResDto> result = accountBookService.queryListByCon(req);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("accountBookList", result);
		} else {
			model.addAttribute("accountBookList", new ArrayList<AccountBookResDto>());
		}
		return "export/fi/accountBook_list";
	}

}
