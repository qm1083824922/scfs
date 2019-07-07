package com.scfs.web.controller.fi;

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
import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.dto.resp.AdvanceResDto;
import com.scfs.domain.fi.entity.Advance;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.AdvanceManagerService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 *  转预收Controller
 *  File: AdvanceManagerController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月07日			Administrator
 *
 * </pre>
 */
@Controller
public class AdvanceManagerController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AdvanceManagerController.class);

	@Autowired
	AdvanceManagerService advanceManagerService;

	/**
	 * 预收核销
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_ADVANCE_MANAGER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createVerify(BankReceipt bankReceipt) {
		BaseResult baseResult = new BaseResult();
		try {
			advanceManagerService.createVerify(bankReceipt);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("预收核销失败[{}]", JSONObject.toJSON(bankReceipt), e);
			baseResult.setMsg("预收核销异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 核销
	 * 
	 * @param advance
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_ADVANCE_MANAGER, method = RequestMethod.POST)
	@ResponseBody
	public Result<AdvanceResDto> editAdvanceById(Advance advance) {
		Result<AdvanceResDto> baseResult = new Result<AdvanceResDto>();
		try {
			baseResult = advanceManagerService.editAdvanceById(advance);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("核销失败[{}]", JSONObject.toJSON(advance), e);
			baseResult.setMsg("核销异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 获取预收管理列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_ADVANCE_MANAGER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AdvanceResDto> queryAdvanceResultsByCon(AdvanceSearchReqDto advanceSearchReqDto) {
		PageResult<AdvanceResDto> pageResult = new PageResult<AdvanceResDto>();
		try {
			pageResult = advanceManagerService.queryAdvanceResultsByCon(advanceSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取列表信息失败[{}]", JSONObject.toJSON(advanceSearchReqDto), e);
			pageResult.setMsg("获取列表信息异常，请稍后重试");
		}
		return pageResult;
	}
}
