package com.scfs.web.controller.send;

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
import com.scfs.domain.base.dto.req.SenderManageReqDto;
import com.scfs.domain.base.dto.resp.SenderManageResDto;
import com.scfs.domain.base.entity.SenderManage;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.send.SenderManageService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 	
 *  File: SenderManageController.java
 *  Description:推送人信息管理
 *  TODO
 *  Date,					Who,				
 *  2017年06月23日				Administrator
 *
 * </pre>
 */
@Controller
public class SenderManageController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SenderManageController.class);
	@Autowired
	private SenderManageService senderManageService;

	/**
	 * 新建推送人信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_SEND_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createSenderManage(SenderManage senderManage) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = senderManageService.addSenderManage(senderManage);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建推送人信息异常[{}]", JSONObject.toJSON(senderManage), e);
			br.setSuccess(false);
			br.setMsg("新建推送人失败，请重试");
		}
		return br;
	}

	/**
	 * 修改信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_SEND_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateSenderManage(SenderManage senderManage) {
		BaseResult result = new BaseResult();
		try {
			result = senderManageService.updateSenderManage(senderManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改推送人信息失败[{}]", JSONObject.toJSON(senderManage), e);
			result.setMsg("修改推送人信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑推送人信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_SEND_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<SenderManageResDto> editSenderManage(SenderManage senderManage) {
		Result<SenderManageResDto> result = new Result<SenderManageResDto>();
		try {
			result = senderManageService.querySenderManageById(senderManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑推送人信息失败[{}]", JSONObject.toJSON(senderManage), e);
			result.setMsg("编辑境推送人信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 浏览推送人信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_SEND_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<SenderManageResDto> detailSenderManage(SenderManage senderManage) {
		Result<SenderManageResDto> result = new Result<SenderManageResDto>();
		try {
			result = senderManageService.querySenderManageById(senderManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览推送人信息失败[{}]", JSONObject.toJSON(senderManage), e);
			result.setMsg("浏览推送人信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_SEND_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteSenderManage(SenderManage senderManage) {
		BaseResult result = new BaseResult();
		try {
			result = senderManageService.deleteSenderManage(senderManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除推送人信息失败[{}]", JSONObject.toJSON(senderManage), e);
			result.setMsg("删除推送人信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询信息
	 * 
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_SEND_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<SenderManageResDto> querySenderManageResultsByCon(SenderManageReqDto searchreqDto) {
		PageResult<SenderManageResDto> pageResult = new PageResult<SenderManageResDto>();
		try {
			pageResult = senderManageService.querySenderManageResultsByCon(searchreqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询推送人信息失败[{}]", JSONObject.toJSON(searchreqDto), e);
			pageResult.setMsg("查询推送人信息异常，请稍后重试");
		}
		return pageResult;
	}
}
