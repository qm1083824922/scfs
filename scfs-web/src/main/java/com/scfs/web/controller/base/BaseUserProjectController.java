package com.scfs.web.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.resp.BaseProjectResDto;
import com.scfs.domain.base.dto.resp.BaseUserProjectResDto;
import com.scfs.domain.project.dto.req.UserProjectReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.user.BaseUserProjectService;
import com.scfs.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BaseUserProjectController extends BaseController {

	@Autowired
	private BaseUserProjectService baseUserProjectService;
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseUserProjectController.class);

	/**
	 * 查询用户项目列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUSERPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public Object queryUserProject(UserProjectReqDto baseReqDto) {
		PageResult<BaseUserProjectResDto> result = new PageResult<BaseUserProjectResDto>();
		try {
			result = baseUserProjectService.queryBaseUserProjectAssignedToUser(baseReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 查询用户项目列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询用户未分配项目列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUSERPROJECTNOTASSIGNED, method = RequestMethod.POST)
	@ResponseBody
	public Object queryUserProjectNotAssigned(UserProjectReqDto baseReqDto) {
		PageResult<BaseProjectResDto> result = new PageResult<BaseProjectResDto>();
		try {
			result = baseUserProjectService.queryProjectNotAssignedToUser(baseReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询用户未分配项目列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量删除用户项目
	 * 
	 * @param ids
	 * @param baseReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DELETEALLUSERPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public Object deleteAllUserProject(BaseReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseUserProjectService.deleteAllBaseUserProject(baseReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 批量删除用户项目：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 分配用户项目
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADDUSERPROJECT, method = RequestMethod.POST)
	@ResponseBody
	public Object addUserProject(BaseReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			return baseUserProjectService.addBaseUserProject(baseReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 分配用户项目异常[{}]", JSONObject.toJSONString(baseReqDto), e);

			result.setMsg("分配用户项目异常，请稍后重试");

		}
		return result;
	}

	/**
	 * 项目分配用户
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.PROJECTUSERDIVIDE, method = RequestMethod.POST)
	@ResponseBody
	public Object addUserToProject(BaseReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			return baseUserProjectService.addUserToProject(baseReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 分配用户项目异常[{}]", JSONObject.toJSONString(baseReqDto), e);

			result.setMsg("分配用户项目异常，请稍后重试");

		}
		return result;
	}

	/**
	 * 批量删除用户项目
	 * 
	 * @param ids
	 * @param baseReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.PROJECTUSERDELETE, method = RequestMethod.POST)
	@ResponseBody
	public Object deleteAllUserToProject(BaseReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseUserProjectService.deleteAllUserToProject(baseReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 批量删除用户项目：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

}
