package com.scfs.web.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseUserRoleReqDto;
import com.scfs.domain.base.dto.resp.BaseRoleResDto;
import com.scfs.domain.base.dto.resp.BaseUserRoleResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.user.BaseUserRoleService;
import com.scfs.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BaseUserRoleController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BaseUserRoleController.class);

	@Autowired
	private BaseUserRoleService baseUserRoleService;

	/**
	 * 查询用户角色列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUSERROLE, method = RequestMethod.POST)
	@ResponseBody
	public Object queryUserRole(BaseUserRoleReqDto baseReqDto) {
		PageResult<BaseUserRoleResDto> result = new PageResult<BaseUserRoleResDto>();
		try {
			result = baseUserRoleService.queryUserRoleAssignedToUser(baseReqDto);
		} catch (Exception e) {
			LOGGER.error(" 查询用户角色列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询用户未分配角色列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUSERROLENOTASSIGNED, method = RequestMethod.POST)
	@ResponseBody
	public Object queryUserRoleNotAssigned(BaseUserRoleReqDto baseReqDto) {
		PageResult<BaseRoleResDto> result = new PageResult<BaseRoleResDto>();
		try {
			result = baseUserRoleService.queryRoleNotAssignedToUser(baseReqDto);
		} catch (Exception e) {
			LOGGER.error("查询用户未分配角色列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量删除用户角色
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DELETEALLUSERROLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteAllUserRole(BaseUserRoleReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseUserRoleService.deleteAllBaseUserRole(baseReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 批量删除用户角色：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 分配用户角色
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADDUSERROLE, method = RequestMethod.POST)
	@ResponseBody
	public Object addUserRole(BaseUserRoleReqDto baseReqDto) {
		try {
			return baseUserRoleService.addBaseUserRole(baseReqDto);
		} catch (Exception e) {
			LOGGER.error("分配用户角色异常[{}]", JSONObject.toJSONString(baseReqDto), e);
			BaseResult result = new BaseResult();
			result.setMsg("分配用户角色异常，请稍后重试");
			return result;
		}
	}

	@RequestMapping(value = BaseUrlConsts.ADDUSERROLEList, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addUserRoleList(BaseUserRoleReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			return baseUserRoleService.addUserRoleList(baseReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量新增用户角色异常[{}]", JSONObject.toJSONString(baseReqDto), e);

			result.setMsg("批量新增用户角色异常，请稍后重试");

		}
		return result;
	}
}
