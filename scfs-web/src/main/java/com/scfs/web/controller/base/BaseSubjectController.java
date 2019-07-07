package com.scfs.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.subject.dto.req.AddSubjectDto;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.base.subject.dto.resp.QuerySubjectResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.subject.BaseSubjectService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: BaseSubjectController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月26日				Administrator
 *
 * </pre>
 */
@Controller
public class BaseSubjectController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseSubjectController.class);

	@Autowired
	private BaseSubjectService baseSubjectService;

	/** 新增经营单位 **/
	@RequestMapping(value = BaseUrlConsts.ADDBUSIUNIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addBusiUnit(AddSubjectDto addSubjectDto) {
		Result<Integer> br = new Result<Integer>();
		try {
			// addSubjectDto.setSubjectType(BaseConsts.SUBJECT_TYPE_BUSI_UNIT);
			Integer id = baseSubjectService.addBaseSubject(addSubjectDto);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(addSubjectDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/** 新增供应商 **/
	@RequestMapping(value = BaseUrlConsts.ADDSUPPLIER, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addSupplier(AddSubjectDto addSubjectDto) {
		Result<Integer> br = new Result<Integer>();
		try {
			// addSubjectDto.setSubjectType(BaseConsts.SUBJECT_TYPE_SUPPLIER);
			Integer id = baseSubjectService.addBaseSubject(addSubjectDto);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(addSubjectDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/** 新增仓库 **/
	@RequestMapping(value = BaseUrlConsts.ADDWAREHOUSE, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addWarehouse(AddSubjectDto addSubjectDto) {
		Result<Integer> br = new Result<Integer>();
		try {
			addSubjectDto.setSubjectType(BaseConsts.SUBJECT_TYPE_WAREHOUSE);
			Integer id = baseSubjectService.addBaseSubject(addSubjectDto);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(addSubjectDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/** 新增客户 **/
	@RequestMapping(value = BaseUrlConsts.ADDCUST, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addCust(AddSubjectDto addSubjectDto) {
		Result<Integer> br = new Result<Integer>();
		try {
			// addSubjectDto.setSubjectType(BaseConsts.SUBJECT_TYPE_CUSTOMER);
			Integer id = baseSubjectService.addBaseSubject(addSubjectDto);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(addSubjectDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/** 新增复杂客户 **/
	@RequestMapping(value = BaseUrlConsts.ADDMUTISUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addMutiSubject(AddSubjectDto addSubjectDto) {
		Result<Integer> br = new Result<Integer>();
		try {
			Integer id = baseSubjectService.addBaseSubject(addSubjectDto);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(addSubjectDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/** 查询信息 **/
	@RequestMapping(value = BaseUrlConsts.QUERYSUBJECTBYCOND, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<QuerySubjectResDto> querySubjectByCond(QuerySubjectReqDto querySubjectDto) {
		PageResult<QuerySubjectResDto> pageResult = new PageResult<QuerySubjectResDto>();
		try {
			pageResult = baseSubjectService.querySubjectByCond(querySubjectDto);
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(querySubjectDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/** 更新 **/
	@RequestMapping(value = BaseUrlConsts.UPDATESUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateSubjectById(BaseSubject baseSubject) {
		BaseResult br = new BaseResult();
		try {
			baseSubjectService.updateBaseSubject(baseSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新信息异常[{}]", JSONObject.toJSON(baseSubject), e);
			br.setSuccess(false);
			br.setMsg("更新失败，请重试");
		}
		return br;
	}

	/** 删除主体 **/
	@RequestMapping(value = BaseUrlConsts.DELETESUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteSubjectById(BaseSubject baseSubject) {
		BaseResult br = new BaseResult();
		try {
			baseSubjectService.deleteSubject(baseSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(baseSubject), e);
			br.setSuccess(false);
			br.setMsg("插入失败，请重试");
		}
		return br;
	}

	/** 浏览主体详细信息 **/
	@RequestMapping(value = BaseUrlConsts.DETAILSUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<QuerySubjectResDto> querySubjectDetailById(BaseSubject baseSubject) {
		Result<QuerySubjectResDto> rs = new Result<QuerySubjectResDto>();
		try {
			rs = baseSubjectService.querySubjectById(baseSubject.getId());
		} catch (BaseException e) {
			rs.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(baseSubject), e);
			rs.setMsg("查询异常，请稍后重试");
			rs.setSuccess(false);
		}

		return rs;
	}

	/** 编辑主体详细信息 **/
	@RequestMapping(value = BaseUrlConsts.EDITSUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<QuerySubjectResDto> querySubjectEditById(BaseSubject baseSubject) {
		Result<QuerySubjectResDto> rs = new Result<QuerySubjectResDto>();
		try {
			rs = baseSubjectService.querySubjectById(baseSubject.getId());
		} catch (BaseException e) {
			rs.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(baseSubject), e);
			rs.setMsg("查询异常，请稍后重试");
			rs.setSuccess(false);
		}

		return rs;
	}

	/** 提交主体 **/
	@RequestMapping(value = BaseUrlConsts.SUBMITSUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitSubjectById(BaseSubject baseSubject) {
		BaseResult br = new BaseResult();
		try {
			baseSubjectService.submitSubject(baseSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交信息异常[{}]", JSONObject.toJSON(baseSubject), e);
			br.setSuccess(false);
			br.setMsg("提交失败，请重试");
		}
		return br;
	}

	/** 锁定主体 **/
	@RequestMapping(value = BaseUrlConsts.LOCKSUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult lockSubjectById(BaseSubject baseSubject) {
		BaseResult br = new BaseResult();
		try {
			baseSubjectService.lockSubject(baseSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("锁定信息异常[{}]", JSONObject.toJSON(baseSubject), e);
			br.setSuccess(false);
			br.setMsg("锁定失败，请重试");
		}
		return br;
	}

	/** 解锁主体 **/
	@RequestMapping(value = BaseUrlConsts.UNLOCKSUBJECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unlockBusiUnit(BaseSubject baseSubject) {
		BaseResult br = new BaseResult();
		try {
			baseSubjectService.unlockSubject(baseSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("解锁信息异常[{}]", JSONObject.toJSON(baseSubject), e);
			br.setSuccess(false);
			br.setMsg("解锁失败，请重试");
		}
		return br;
	}

}
