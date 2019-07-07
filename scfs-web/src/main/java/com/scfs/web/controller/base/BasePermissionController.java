package com.scfs.web.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BasePermissionReqDto;
import com.scfs.domain.base.dto.resp.BasePermissionResDto;
import com.scfs.domain.base.entity.BasePermission;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.shiro.BasePermissionService;
import com.scfs.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/26. 权限controller
 */
@Controller
public class BasePermissionController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BasePermissionController.class);

	@Autowired
	private BasePermissionService basePermissionService;

	@RequestMapping(value = BaseUrlConsts.QUERYPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	PageResult<BasePermissionResDto> queryPermisssionList(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> pageResult = new PageResult<BasePermissionResDto>();
		try {
			pageResult = basePermissionService.queryPermissions(basePermissionReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询权限信息异常[{}]", JSONObject.toJSON(basePermissionReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BaseUrlConsts.ADDPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	Result<Integer> addPermisssion(BasePermission basePermission) {
		Result<Integer> br = new Result<Integer>();
		try {
			Integer result = basePermissionService.addPermission(basePermission);
			br.setItems(result);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入权限信息异常[{}]", JSONObject.toJSON(basePermission), e);
			br.setMsg("插入权限信息异常，请稍后重试！");
		}
		return br;
	}

	@RequestMapping(value = BaseUrlConsts.UPDATEPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	BaseResult updatePermisssion(BasePermission basePermission) {
		BaseResult result = new BaseResult();
		try {
			// 如果是URL权限1
			if (basePermission.getType() != null && basePermission.getType() == BaseConsts.ONE) {
				basePermission.setMenuLevel(0);
				basePermission.setParentId(0);
			}
			if (basePermission.getMenuLevel() != null && basePermission.getMenuLevel() == BaseConsts.ONE) {
				basePermission.setParentId(0);
			}
			basePermissionService.updatePermission(basePermission);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新权限信息异常[{}]", JSONObject.toJSON(basePermission), e);
			result.setMsg("更新权限异常，请稍后重试！");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DELETEPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	BaseResult deletePermisssion(BasePermission basePermission) {
		BaseResult result = new BaseResult();
		try {
			basePermission.setIsDelete(BaseConsts.ONE);// 删除
			basePermissionService.updatePermission(basePermission);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除权限信息异常[{}]", JSONObject.toJSON(basePermission), e);
			result.setMsg("删除权限异常，请稍后重试！");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.EDITPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	Result<BasePermissionResDto> editPermisssion(@RequestParam(value = "id") int id) {
		Result<BasePermissionResDto> result = new Result<BasePermissionResDto>();
		try {
			return basePermissionService.queryPermissionById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑权限信息异常[{}]", id, e);
			result.setMsg("编辑权限异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DETAILPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	BaseResult detailPermisssion(@RequestParam(value = "id") int id) {
		Result<BasePermissionResDto> result = new Result<BasePermissionResDto>();
		try {
			return basePermissionService.queryPermissionById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览权限信息异常[{}]", id, e);
			result.setMsg("浏览权限异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.SUBMITPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	BaseResult submitPermisssion(BasePermission basePermission) {
		BaseResult result = new BaseResult();
		try {
			basePermission.setState(BaseConsts.TWO);// 提交之后,就是已完成状态
			basePermissionService.updatePermission(basePermission);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交权限信息异常[{}]", JSONObject.toJSON(basePermission), e);
			result.setMsg("提交权限异常，请稍后重试！");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.LOCKPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	BaseResult lockPermisssion(BasePermission basePermission) {
		BaseResult result = new BaseResult();
		try {
			basePermission.setState(BaseConsts.THREE);// 锁定状态
			basePermissionService.updatePermission(basePermission);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("锁定权限信息异常[{}]", JSONObject.toJSON(basePermission), e);
			result.setMsg("锁定权限异常，请稍后重试！");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.UNLOCKPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	BaseResult unLockPermisssion(BasePermission basePermission) {
		BaseResult result = new BaseResult();
		try {
			basePermission.setState(BaseConsts.TWO);// 解锁之后,就是已完成状态
			basePermissionService.updatePermission(basePermission);
		} catch (Exception e) {
			LOGGER.error("解锁权限信息异常[{}]", JSONObject.toJSON(basePermission), e);
			result.setMsg("解锁权限异常，请稍后重试！");
		}
		return result;
	}

	/**
	 * 根据权限组ID查询已经分配的权限
	 * 
	 * @param basePermissionReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYDIVIDPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	PageResult<BasePermissionResDto> queryDividPermisssionList(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> result = new PageResult<BasePermissionResDto>();
		try {
			return basePermissionService.queryDividPermissionListByGroupId(basePermissionReqDto);
		} catch (Exception e) {
			LOGGER.error("根据权限组ID查询已经分配的权限信息异常[{}]", JSONObject.toJSON(basePermissionReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 根据权限组ID查询未分配的权限
	 * 
	 * @param basePermissionReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYUNDIVIDPERMISSION, method = RequestMethod.POST)
	@ResponseBody
	PageResult<BasePermissionResDto> queryUnDividPermisssionList(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> result = new PageResult<BasePermissionResDto>();
		try {
			return basePermissionService.queryUnDividPermissionListByGroupId(basePermissionReqDto);
		} catch (Exception e) {
			LOGGER.error("根据权限组ID查询未分配的权限信息异常[{}]", JSONObject.toJSON(basePermissionReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询所有一级菜单
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYFIRSTMENU, method = RequestMethod.POST)
	@ResponseBody
	List<BasePermission> queryFirstMenus() {
		List<BasePermission> result = new ArrayList<BasePermission>();
		try {
			result = basePermissionService.queryFirstPermission();
		} catch (Exception e) {
			LOGGER.error("查询菜单权限信息异常[{}]", JSONObject.toJSON(result), e);
		}
		return result;
	}

	/**
	 * 通过角色获取权限
	 * 
	 * @param basePermissionReqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYPERMISSIONROLE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<BasePermissionResDto> queryPermisssionListByRole(BasePermissionReqDto basePermissionReqDto) {
		PageResult<BasePermissionResDto> pageResult = new PageResult<BasePermissionResDto>();
		try {
			pageResult = basePermissionService.queryPermissionsByRoleId(basePermissionReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询权限信息异常[{}]", JSONObject.toJSON(basePermissionReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 下载excel模板
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.PERMISSION_TEMPLATE_DOWNLOAD, method = RequestMethod.GET)
	public String downloadGoodsTemplate() {
		return "template/baseinfo/authority/permission_template";
	}

	/**
	 * 权限确认导入excel
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.IMPORT_PERMISSION, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importGoodsExcel(MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			basePermissionService.importExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}
}
