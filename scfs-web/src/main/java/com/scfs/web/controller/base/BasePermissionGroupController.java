package com.scfs.web.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BasePermissionGroupReqDto;
import com.scfs.domain.base.dto.req.BasePermissionReqDto;
import com.scfs.domain.base.dto.resp.BasePermissionGroupResDto;
import com.scfs.domain.base.dto.resp.BaseRoleResDto;
import com.scfs.domain.base.dto.resp.BaseUserResDto;
import com.scfs.domain.base.entity.BasePermissionGroup;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.shiro.BasePermissionGroupService;
import com.scfs.service.base.shiro.BaseRoleService;
import com.scfs.service.base.user.BaseUserService;
import com.scfs.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/9/26. 权限组controller
 */
@Controller
public class BasePermissionGroupController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BasePermissionGroupController.class);

	@Autowired
	private BasePermissionGroupService basePermissionGroupService;
	@Autowired
	private BaseRoleService baseRoleService;

	@Autowired
	private BaseUserService baseUserService;

	@RequestMapping(value = BaseUrlConsts.QUERYPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BasePermissionGroupResDto> queryPermissionGroups(
			BasePermissionGroupReqDto basePermissionGroupReqDto) {
		PageResult<BasePermissionGroupResDto> result = new PageResult<BasePermissionGroupResDto>();
		try {
			return basePermissionGroupService.queryPermissionGroups(basePermissionGroupReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询权限组信息异常[{}]", JSONObject.toJSON(basePermissionGroupReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.QUERYPERMISSIONGROUPROLE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseRoleResDto> queryPermissionGroupsRole(BasePermissionGroupReqDto basePermissionGroupReqDto) {
		PageResult<BaseRoleResDto> result = new PageResult<BaseRoleResDto>();
		try {
			result = baseRoleService.queryRoleByPermissionGroup(basePermissionGroupReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询角色信息异常[{}]", JSONObject.toJSON(basePermissionGroupReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.QUERYPERMISSIONGROUPUSER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseUserResDto> queryPermissionGroupsUsers(BasePermissionGroupReqDto basePermissionGroupReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		try {
			result = baseUserService.queryUserByPermissionGroup(basePermissionGroupReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询用户信息异常[{}]", JSONObject.toJSON(basePermissionGroupReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.ADDPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addPermisssionGroup(BasePermissionGroup basePermissionGroup) {
		Result<Integer> br = new Result<Integer>();
		try {
			basePermissionGroup.setState(BaseConsts.TWO);// 新建，就是已完成状态
			Integer result = basePermissionGroupService.addPermissionGroup(basePermissionGroup);
			br.setItems(result);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增权限组信息异常[{}]", JSONObject.toJSON(basePermissionGroup), e);
			br.setMsg("新增权限组异常，请稍后重试！");
		}
		return br;
	}

	@RequestMapping(value = BaseUrlConsts.DETAILPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	Result<BasePermissionGroupResDto> detailPermisssionGroup(@RequestParam(value = "id") Integer id) {
		Result<BasePermissionGroupResDto> result = new Result<BasePermissionGroupResDto>();
		try {
			return basePermissionGroupService.queryPermissionGroupById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询权限组信息异常[{}]", id, e);
			result.setMsg("查询权限异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.LOCKPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	BaseResult lockPermisssionGroup(BasePermissionGroup basePermissionGroup) {
		BaseResult result = new BaseResult();
		try {
			basePermissionGroup.setState(BaseConsts.TWO);// 锁定状态
			basePermissionGroupService.updatePermissionGroup(basePermissionGroup);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("锁定权限组异常，请稍后重试！");
			LOGGER.error("锁定权限组信息异常[{}]", JSONObject.toJSON(basePermissionGroup), e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.UNLOCKPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	BaseResult unLockPermisssionGroup(BasePermissionGroup basePermissionGroup) {
		BaseResult result = new BaseResult();
		try {
			basePermissionGroup.setState(BaseConsts.ONE);// 解锁之后,就是已完成状态
			basePermissionGroupService.updatePermissionGroup(basePermissionGroup);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("解锁权限组异常，请稍后重试！");
			LOGGER.error("解锁权限组信息异常[{}]", JSONObject.toJSON(basePermissionGroup), e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.UPDATEPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	BaseResult updatePermisssionGroup(BasePermissionGroup basePermissionGroup) {
		BaseResult result = new BaseResult();
		try {
			basePermissionGroupService.updatePermissionGroup(basePermissionGroup);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新权限组信息异常[{}]", JSONObject.toJSON(basePermissionGroup), e);
			result.setMsg("更新权限组异常，请稍后重试！");
		}
		return result;
	}

	/**
	 * 分配权限
	 * 
	 * @param basePermissionGroupReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DIVIDPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	BaseResult dividePermission(BasePermissionGroupReqDto basePermissionGroupReqDto) {
		BaseResult result = new BaseResult();
		try {
			return basePermissionGroupService.dividePermission(basePermissionGroupReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配权限组信息异常[{}]", JSONObject.toJSON(basePermissionGroupReqDto), e);

			result.setMsg("分配权限组异常，请稍后重试！");

		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.INVALIDEPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	BaseResult invalidPermisssion(BasePermissionGroupReqDto basePermissionGroupReqDto) {
		BaseResult result = new BaseResult();
		try {
			// 作废权限组与权限关系
			return basePermissionGroupService.invalidPermissionRelation(basePermissionGroupReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("作废权限组与权限信息异常[{}]", JSONObject.toJSON(basePermissionGroupReqDto), e);

			result.setMsg("作废权限组与权限异常，请稍后重试！");

		}
		return result;
	}

	/**
	 * 根据角色ID查询已经分配的权限组
	 * 
	 * @param basePermissionGroupReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYDIVIDPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	PageResult<BasePermissionGroupResDto> queryDividPermisssionGroupList(
			BasePermissionGroupReqDto basePermissionGroupReqDto) {
		try {
			return basePermissionGroupService.queryDividPermissionGroupList(basePermissionGroupReqDto);
		} catch (Exception e) {
			LOGGER.error("根据角色ID查询权限组信息异常[{}]", JSONObject.toJSON(basePermissionGroupReqDto), e);
			PageResult<BasePermissionGroupResDto> result = new PageResult<BasePermissionGroupResDto>();
			result.setMsg("查询异常，请稍后重试");
			return result;
		}
	}

	/**
	 * 根据角色ID查询未分配的权限组
	 * 
	 * @param basePermissionGroupReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUNDIVIDPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	PageResult<BasePermissionGroupResDto> queryUnDividPermisssionGroupList(
			BasePermissionGroupReqDto basePermissionGroupReqDto) {
		try {
			return basePermissionGroupService.queryUnDividPermissionGroupList(basePermissionGroupReqDto);
		} catch (Exception e) {
			LOGGER.error("根据角色ID查询权限组信息异常[{}]", JSONObject.toJSON(basePermissionGroupReqDto), e);
			PageResult<BasePermissionGroupResDto> result = new PageResult<BasePermissionGroupResDto>();
			result.setMsg("查询异常，请稍后重试");
			return result;
		}
	}

	@RequestMapping(value = BaseUrlConsts.QUERYPERMISSIONGROUPBYPER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BasePermissionGroupResDto> queryPermissionGroupByPer(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionGroupResDto> result = new PageResult<BasePermissionGroupResDto>();
		try {
			result = basePermissionGroupService.queryPermissionGroupByPer(basePermissionReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询角色信息异常[{}]", JSONObject.toJSON(basePermissionReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}
}
