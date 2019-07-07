package com.scfs.web.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseUserReqDto;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.dto.resp.BaseUserResDto;
import com.scfs.domain.base.dto.resp.BaseUserSubjectResDto;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.user.BaseUserService;
import com.scfs.service.base.user.BaseUserSubjectService;
import com.scfs.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <pre>
 *  用户基础信息关系Controller
 *  File: BaseUserSubjectController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月01日				Administrator
 *
 * </pre>
 */
@Controller
public class BaseUserSubjectController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BaseUserSubjectController.class);

	@Autowired
	private BaseUserSubjectService baseUserSubjectService;
	@Autowired
	private BaseUserService baseUserService;

	/**************************** 用户-仓库 *****************************/
	/**
	 * 查询用户仓库列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERY_USER_SUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public Object queryUserSubject(BaseUserSubjectReqDto baseReqDto) {
		PageResult<BaseUserSubjectResDto> result = new PageResult<BaseUserSubjectResDto>();
		try {
			baseReqDto.setSubjectType(BaseConsts.TWO);
			result = baseUserSubjectService.queryUserSubjectByCon(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error(" 查询用户仓库列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 查询用户仓库列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询用户未分配仓库列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERY_USER_SUBJECT_NOTASSIGNED, method = RequestMethod.POST)
	@ResponseBody
	public Object queryUserSubjectNotAssigned(BaseUserSubjectReqDto baseReqDto) {
		PageResult<BaseSubject> result = new PageResult<BaseSubject>();
		try {
			baseReqDto.setSubjectType(BaseConsts.TWO);
			result = baseUserSubjectService.querySubjectByCond(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询用户未分配仓库列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询用户未分配仓库列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量删除用户仓库
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DELETE_ALL_USER_SUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteAllUserSubject(BaseUserSubjectReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseUserSubjectService.deleteAllBaseUserSubject(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error(" 批量删除用户仓库：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 批量删除用户仓库：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 分配用户仓库
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADD_USER_DIVID, method = RequestMethod.POST)
	@ResponseBody
	public Object addUserSubject(BaseUserSubject baseUserSubject) {
		BaseResult result = new BaseResult();
		try {
			baseUserSubject.setSubjectType(BaseConsts.TWO);
			return baseUserSubjectService.addBaseUserSubject(baseUserSubject);
		} catch (BaseException e) {
			LOGGER.error("分配用户仓库异常[{}]", JSONObject.toJSONString(baseUserSubject), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配用户仓库异常[{}]", JSONObject.toJSONString(baseUserSubject), e);
			result.setMsg("分配用户仓库异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量分配用户仓库
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADD_USER_DIVID_ALL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addUserSubjectList(@RequestBody BaseUserSubjectReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseReqDto.setSubjectType(BaseConsts.TWO);
			return baseUserSubjectService.addBaseUserSubjectList(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error("批量分配用户仓库异常[{}]", JSONObject.toJSONString(baseReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量分配用户仓库异常[{}]", JSONObject.toJSONString(baseReqDto), e);
			result.setMsg("批量分配用户仓库异常，请稍后重试");
		}
		return result;
	}

	/**************************** 用户-供应商 *****************************/
	/**
	 * 查询用户供应商列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERY_USER_SUPPLIER, method = RequestMethod.POST)
	@ResponseBody
	public Object queryUserSupplier(BaseUserSubjectReqDto baseReqDto) {
		PageResult<BaseUserSubjectResDto> result = new PageResult<BaseUserSubjectResDto>();
		try {
			baseReqDto.setSubjectType(BaseConsts.FOUR);
			result = baseUserSubjectService.queryUserSubjectByCon(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error(" 查询用户供应商列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 查询用户供应商列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询用户未分配供应商列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERY_USER_SUPPLIER_NOTASSIGNED, method = RequestMethod.POST)
	@ResponseBody
	public Object queryUserSupplierNotAssigned(BaseUserSubjectReqDto baseReqDto) {
		PageResult<BaseSubject> result = new PageResult<BaseSubject>();
		try {
			baseReqDto.setSubjectType(BaseConsts.FOUR);
			result = baseUserSubjectService.querySubjectByCond(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询用户未分配供应商列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询用户未分配供应商列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量删除用户供应商
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DELETE_ALL_USER_SUPPLIER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteAllUserSupplier(BaseUserSubjectReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseReqDto.setSubjectType(BaseConsts.FOUR);
			baseUserSubjectService.deleteAllBaseUserSubject(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error(" 批量删除用户供应商：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 批量删除用户供应商：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量分配用户供应商
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADD_USER_SUPPLIER_DIVID_ALL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addUserSupplierList(@RequestBody BaseUserSubjectReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseReqDto.setSubjectType(BaseConsts.FOUR);
			return baseUserSubjectService.addBaseUserSubjectList(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error("批量分配用户供应商异常[{}]", JSONObject.toJSONString(baseReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量分配用户供应商异常[{}]", JSONObject.toJSONString(baseReqDto), e);
			result.setMsg("批量分配用户供应商异常，请稍后重试");
		}
		return result;
	}

	/**************************** 主体-用户 *****************************/
	/**
	 * 查询主体用户列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERY_SUBJECT_USER, method = RequestMethod.POST)
	@ResponseBody
	public Object querySubjectUser(BaseUserReqDto baseReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		try {
			result = baseUserService.queryBaseUserListBySubjectId(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error(" 查询主体用户列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error(" 查询主体用户列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询主体未分配用户列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERY_SUBJECT_USER_NOTASSIGNED, method = RequestMethod.POST)
	@ResponseBody
	public Object querySubjectUserNotAssigned(BaseUserReqDto baseReqDto) {
		PageResult<BaseUserResDto> result = new PageResult<BaseUserResDto>();
		try {
			result = baseUserService.queryUndivideUserBySubjectId(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询主体未分配用户列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询主体未分配用户列表信息异常[{}]", JSONObject.toJSON(result), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量删除主体用户
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DELETE_ALL_SUBJECT_USER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteAllSubjectUser(BaseUserSubjectReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			baseUserSubjectService.deleteAllBaseUserSubjectBySubjectId(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error("批量删除主体用户：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除主体用户：[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量分配主体用户
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADD_SUBJECT_DIVID_ALL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addSubjectUserList(@RequestBody BaseUserSubjectReqDto baseReqDto) {
		BaseResult result = new BaseResult();
		try {
			return baseUserSubjectService.addBaseUserSubjectListBySubjectId(baseReqDto);
		} catch (BaseException e) {
			LOGGER.error("批量分配主体用户异常[{}]", JSONObject.toJSONString(baseReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量分配主体用户异常[{}]", JSONObject.toJSONString(baseReqDto), e);
			result.setMsg("批量分配仓库用户异常，请稍后重试");
		}
		return result;
	}
}
