package com.scfs.web.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseRoleReqDto;
import com.scfs.domain.base.dto.resp.BaseRoleResDto;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.shiro.BaseRoleService;
import com.scfs.service.support.ServiceSupport;
import com.scfs.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/27.
 */
@Controller
public class BaseRoleController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BaseRoleController.class);

	@Autowired
	private BaseRoleService baseRoleService;

	@RequestMapping(value = BaseUrlConsts.QUERYROLE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<BaseRoleResDto> queryRoleList(BaseRoleReqDto baseRoleReqDto) {
		try {
			return baseRoleService.queryBaseRoleList(baseRoleReqDto);
		} catch (Exception e) {
			LOGGER.error("查询角色信息异常[{}]", JSONObject.toJSON(baseRoleReqDto), e);
			PageResult<BaseRoleResDto> result = new PageResult<BaseRoleResDto>();
			result.setMsg("查询异常，请稍后重试");
			return result;
		}
	}

	@RequestMapping(value = BaseUrlConsts.ADDROLE, method = RequestMethod.POST)
	@ResponseBody
	Result<Integer> addBaseRole(BaseRole baseRole) {
		Result<Integer> br = new Result<Integer>();
		try {
			Integer result = baseRoleService.addBaseRole(baseRole);
			br.setItems(result);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增角色异常，【{}】", JSONObject.toJSON(baseRole), e);
		}
		return br;
	}

	@RequestMapping(value = BaseUrlConsts.UPDATEROLE, method = RequestMethod.POST)
	@ResponseBody
	BaseResult updateBaseRole(BaseRole baseRole) {
		BaseResult result = new BaseResult();
		try {
			baseRoleService.updateBaseRole(baseRole);
		} catch (Exception e) {
			result.setMsg("更新角色异常，请稍后重试");
			LOGGER.error("更新角色异常，【{}】", JSONObject.toJSON(baseRole), e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.LOCKROLE, method = RequestMethod.POST)
	@ResponseBody
	BaseResult lockBaseRole(BaseRole baseRole) {
		BaseResult result = new BaseResult();
		try {
			baseRole.setState(BaseConsts.THREE);
			baseRole.setDeleteAt(new Date());
			baseRole.setLocker(ServiceSupport.getUser().getChineseName());
			baseRole.setLockAt(new Date());
			baseRoleService.updateBaseRole(baseRole);
		} catch (Exception e) {
			result.setMsg("锁定角色异常，请稍后重试");
			LOGGER.error("锁定角色异常，【{}】", JSONObject.toJSON(baseRole), e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.UNLOCKROLE, method = RequestMethod.POST)
	@ResponseBody
	BaseResult unlockBaseRole(BaseRole baseRole) {
		BaseResult result = new BaseResult();
		try {
			baseRole.setState(BaseConsts.TWO);
			baseRoleService.updateBaseRole(baseRole);
		} catch (Exception e) {
			result.setMsg("解锁角色异常，请稍后重试");
			LOGGER.error("解锁角色异常，【{}】", JSONObject.toJSON(baseRole), e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DETAILROLE, method = RequestMethod.POST)
	@ResponseBody
	BaseResult detailBaseRole(int id) {
		try {
			return baseRoleService.queryBaseRoleById(id);
		} catch (Exception e) {
			LOGGER.error("浏览角色信息异常[{}]", id, e);
			Result<BaseRoleResDto> result = new Result<BaseRoleResDto>();
			result.setMsg("浏览角色信息异常，请稍后重试");
			return result;
		}
	}

	/**
	 * 分配权限组
	 * 
	 * @param baseRoleReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DIVIDPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	BaseResult dividePermissionGroup(BaseRoleReqDto baseRoleReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseRoleService.dividePermissionGroup(baseRoleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配权限组信息异常[{}]", JSONObject.toJSON(baseRoleReqDto), e);
			result.setMsg("分配权限组异常，请稍后重试！");
			return result;
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.INVALIDPERMISSIONGROUP, method = RequestMethod.POST)
	@ResponseBody
	BaseResult invalidPermisssionGroup(BaseRoleReqDto baseRoleReqDto) {
		BaseResult result = new BaseResult();
		try {
			// 作废权限组与角色关系
			return baseRoleService.invalidPermissionGroupRelation(baseRoleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("作废权限组与角色信息异常[{}]", JSONObject.toJSON(baseRoleReqDto), e);

			result.setMsg("作废权限组与角色异常，请稍后重试！");

		}
		return result;
	}

}
