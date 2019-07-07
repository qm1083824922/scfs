package com.scfs.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseDepartmentReqDto;
import com.scfs.domain.base.dto.resp.BaseDepartmentResDto;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.department.BaseDepartMentService;

@Controller
public class BaseDepartmentController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BaseDepartmentController.class);

	@Autowired
	private BaseDepartMentService baseDepartMentService;

	@RequestMapping(value = BaseUrlConsts.QUERYDEPARTMENT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseDepartmentResDto> queryAccountBySubjectId(BaseDepartmentReqDto baseDepartmentReqDto) {
		PageResult<BaseDepartmentResDto> pageResult = new PageResult<BaseDepartmentResDto>();
		try {
			pageResult = baseDepartMentService.queryBaseUserList(baseDepartmentReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(baseDepartmentReqDto), e);
			pageResult.setSuccess(false);
			pageResult.setMsg("查询失败，请重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BaseUrlConsts.ADDDEPARTMENT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult add(BaseDepartment baseDepartment) {

		BaseResult br = new BaseResult();
		try {
			baseDepartMentService.add(baseDepartment);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(baseDepartment), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	@RequestMapping(value = BaseUrlConsts.EDITDEPARTMENT, method = RequestMethod.POST)
	@ResponseBody
	public Result<BaseDepartment> edit(BaseDepartmentReqDto baseDepartmentReqDto) {
		Result<BaseDepartment> result = new Result<BaseDepartment>();
		try {
			BaseDepartment baseDepartment = baseDepartMentService.edit(baseDepartmentReqDto.getId());
			result.setItems(baseDepartment);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑信息异常[{}]", JSONObject.toJSON(baseDepartmentReqDto), e);
			result.setSuccess(false);
			result.setMsg("编辑失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DETAILDEPARTMENT, method = RequestMethod.POST)
	@ResponseBody
	public Result<BaseDepartmentResDto> detail(BaseDepartmentReqDto baseDepartmentReqDto) {
		Result<BaseDepartmentResDto> result = new Result<BaseDepartmentResDto>();
		try {
			BaseDepartmentResDto baseDepartment = baseDepartMentService.detail(baseDepartmentReqDto.getId());
			result.setItems(baseDepartment);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览信息异常[{}]", JSONObject.toJSON(baseDepartmentReqDto), e);
			result.setSuccess(false);
			result.setMsg("浏览失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DELETEDEPARTMENT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult delete(BaseDepartmentReqDto baseDepartmentReqDto) {
		BaseResult br = new BaseResult();
		try {
			baseDepartMentService.delete(baseDepartmentReqDto.getId());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(baseDepartmentReqDto), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	@RequestMapping(value = BaseUrlConsts.UPDATEDEPARTMENT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult update(BaseDepartment baseDepartment) {
		BaseResult br = new BaseResult();
		try {
			baseDepartMentService.update(baseDepartment);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新部门信息异常[{}]", JSONObject.toJSON(baseDepartment), e);
			br.setSuccess(false);
			br.setMsg("更新部门失败，请重试");
		}
		return br;
	}

}
