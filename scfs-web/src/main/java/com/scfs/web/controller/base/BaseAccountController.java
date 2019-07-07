package com.scfs.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.subject.dto.req.AddAccountDto;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.base.subject.dto.resp.QueryAccountResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.subject.BaseAccountService;
import com.scfs.common.exception.BaseException;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: BaseAccountController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Controller
public class BaseAccountController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseAccountController.class);

	@Autowired
	private BaseAccountService baseAccountService;

	@RequestMapping(value = BaseUrlConsts.ADDSUBJECTACCOUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addBaseAccount(AddAccountDto addAccountDto) {

		BaseResult br = new BaseResult();
		try {
			baseAccountService.addBaseAccount(addAccountDto);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(addAccountDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	@RequestMapping(value = BaseUrlConsts.QUERYACCOUNTBYSUBJECTID, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<QueryAccountResDto> queryAccountBySubjectId(QueryAccountReqDto queryAccountReqDto) {
		return baseAccountService.queryAccountBySubjectId(queryAccountReqDto);
	}

	/**
	 * 通过id获取收款账户信息
	 * 
	 * @param queryAccountReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.EDITACCOUNTBYID, method = RequestMethod.POST)
	@ResponseBody
	public Result<QueryAccountResDto> editAccountById(int id) {
		Result<QueryAccountResDto> result = baseAccountService.editAccountById(id);
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.BATCHINVALIDSUBJECTACCOUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult invalidSubjectAccount(QueryAccountReqDto queryAccountReqDto) {
		BaseResult br = new BaseResult();
		try {
			baseAccountService.invalidBaseAccountByIds(queryAccountReqDto.getIds());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("作废信息异常[{}]", JSONObject.toJSON(queryAccountReqDto), e);
			br.setSuccess(false);
			br.setMsg("作废失败，请重试");
		}
		return br;
	}

}
