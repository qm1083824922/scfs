package com.scfs.web.controller.export;

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
import com.scfs.domain.export.dto.req.CustomsApplySearchReqDto;
import com.scfs.domain.export.dto.resp.CustomsApplyResDto;
import com.scfs.domain.export.entity.CustomsApply;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.export.CustomsApplyService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年12月6日.
 */
@Controller
public class CustomsApplyController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(CustomsApplyController.class);

	@Autowired
	private CustomsApplyService customsApplyService;

	@RequestMapping(value = BusUrlConsts.QUERY_CUSTOMS_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CustomsApplyResDto> queryCustomsApplyResultsByCon(
			CustomsApplySearchReqDto customsApplySearchReqDto) {
		PageResult<CustomsApplyResDto> result = new PageResult<CustomsApplyResDto>();
		try {
			result = customsApplyService.queryCustomsApplyResultsByCon(customsApplySearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询报关申请列表异常[{}]", JSONObject.toJSON(customsApplySearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询报关申请列表异常[{}]", JSONObject.toJSON(customsApplySearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_CUSTOMS_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public Result<CustomsApplyResDto> detailCustomsApplyById(CustomsApply customsApply) {
		Result<CustomsApplyResDto> result = new Result<CustomsApplyResDto>();
		try {
			result = customsApplyService.detailCustomsApplyById(customsApply);
		} catch (BaseException e) {
			LOGGER.error("查询报关申请详情异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询报关申请详情异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_CUSTOMS_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public Result<CustomsApplyResDto> editCustomsApplyById(CustomsApply customsApply) {
		Result<CustomsApplyResDto> result = new Result<CustomsApplyResDto>();
		try {
			result = customsApplyService.detailCustomsApplyById(customsApply);
		} catch (BaseException e) {
			LOGGER.error("查询报关申请详情异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询报关申请详情异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.ADD_CUSTOMS_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public Result<CustomsApply> addCustomsApply(CustomsApply customsApply) {
		Result<CustomsApply> result = new Result<CustomsApply>();
		try {
			CustomsApply customsApplyRes = customsApplyService.addCustomsApply(customsApply);
			result.setItems(customsApplyRes);
		} catch (BaseException e) {
			LOGGER.error("新增报关申请异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增报关申请异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_CUSTOMS_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateCustomsApplyById(CustomsApply customsApply) {
		BaseResult result = new BaseResult();
		try {
			customsApplyService.updateCustomsApplyById(customsApply);
		} catch (BaseException e) {
			LOGGER.error("更新报关申请异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新报关申请异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_CUSTOMS_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteCustomsApplyById(CustomsApply customsApply) {
		BaseResult result = new BaseResult();
		try {
			customsApplyService.deleteCustomsApplyById(customsApply);
		} catch (BaseException e) {
			LOGGER.error("删除报关申请异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除报关申请异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_CUSTOMS_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitCustomsApplyById(CustomsApply customsApply) {
		BaseResult result = new BaseResult();
		try {
			customsApplyService.submitCustomsApplyById(customsApply);
		} catch (BaseException e) {
			LOGGER.error("提交报关申请异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交报关申请异常[{}]", JSONObject.toJSON(customsApply), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

}
