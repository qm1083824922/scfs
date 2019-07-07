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
import com.scfs.domain.base.dto.req.SenderProjectReqDto;
import com.scfs.domain.base.dto.resp.BaseProjectResDto;
import com.scfs.domain.base.dto.resp.SenderProjectResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.send.SenderProjectService;

/**
 * <pre>
 * 	
 *  File: SenderProjectController.java
 *  Description:推送人项目
 *  TODO
 *  Date,					Who,				
 *  2017年07月19日				Administrator
 *
 * </pre>
 */
@Controller
public class SenderProjectController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SenderProjectController.class);
	@Autowired
	private SenderProjectService senderProjectService;

	/**
	 * 查询信息
	 * 
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_SEND_PROJECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<SenderProjectResDto> querySenderProjectResultsByCon(SenderProjectReqDto searchreqDto) {
		PageResult<SenderProjectResDto> pageResult = new PageResult<SenderProjectResDto>();
		try {
			pageResult = senderProjectService.querySenderProjectResultsByCon(searchreqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询推送项目失败[{}]", JSONObject.toJSON(searchreqDto), e);
			pageResult.setMsg("查询推送项目异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 获取项目信息
	 * 
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_SEND_BASEPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseProjectResDto> queryBaseProjectResultsByCon(SenderProjectReqDto searchreqDto) {
		PageResult<BaseProjectResDto> pageResult = new PageResult<BaseProjectResDto>();
		try {
			pageResult = senderProjectService.queryBaseProject(searchreqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询信息失败[{}]", JSONObject.toJSON(searchreqDto), e);
			pageResult.setMsg("查询信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 新建信息
	 * 
	 * @param senderProjectReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_SEND_PROJECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createSenderProject(SenderProjectReqDto reqDto) {
		Result<Integer> br = new Result<Integer>();
		try {
			senderProjectService.createSenderProject(reqDto);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建推送项目信息异常[{}]", JSONObject.toJSON(reqDto), e);
			br.setSuccess(false);
			br.setMsg("新建推送项目失败，请重试");
		}
		return br;
	}

	/**
	 * 删除信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_SEND_PROJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteSenderProject(SenderProjectReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			result = senderProjectService.deleteSenderProject(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除推送项目信息失败[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("删除推送项目信息异常，请稍后重试");
		}
		return result;
	}
}
