package com.scfs.web.controller.report;

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
import com.scfs.domain.report.entity.ProfitTarget;
import com.scfs.domain.report.req.ProfitTargetReqDto;
import com.scfs.domain.report.resp.ProfitTargetResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.report.ProfitTargetService;

/**
 * <pre>
 *
 *  File: ProfitTargetController.java
 *  Description: 业务指标目标值
 *  TODO
 *  Date,                   Who,
 *  2017年07月17日         Administrator
 *
 * </pre>
 */
@Controller
public class ProfitTargetController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProfitTargetController.class);
	@Autowired
	private ProfitTargetService profitTargetService;

	/**
	 * 获取信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROFIT_TARGET, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProfitTargetResDto> queryProfitTargeResult(ProfitTargetReqDto reqDto) {
		PageResult<ProfitTargetResDto> result = new PageResult<ProfitTargetResDto>();
		try {
			result = profitTargetService.queryProfitTargeResult(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取目标值信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取目标值信息失败[{}]", null, e);
			result.setMsg("获取目标值信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PROFIT_TARGET, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createProfitTarge(ProfitTarget profitTarget) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = profitTargetService.createProfitTarge(profitTarget);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(profitTarget), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_PROFIT_TARGET, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProfitTargetResDto> detailPayOrderById(ProfitTarget profitTarget) {
		Result<ProfitTargetResDto> result = new Result<ProfitTargetResDto>();
		try {
			result = profitTargetService.detailProfitTargeById(profitTarget);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览信息失败[{}]", JSONObject.toJSON(profitTarget), e);
			result.setMsg("浏览信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PROFIT_TARGET, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProfitTargetResDto> detailProfitTargeById(ProfitTarget profitTarget) {
		Result<ProfitTargetResDto> result = new Result<ProfitTargetResDto>();
		try {
			result = profitTargetService.detailProfitTargeById(profitTarget);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑信息失败[{}]", JSONObject.toJSON(profitTarget), e);
			result.setMsg("编辑信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 修改信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PROFIT_TARGET, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateProfitTarge(ProfitTarget profitTarget) {
		BaseResult result = new BaseResult();
		try {
			result = profitTargetService.updateProfitTarge(profitTarget);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新信息失败[{}]", JSONObject.toJSON(profitTarget), e);
			result.setMsg("更新信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除信息
	 * 
	 * @param profitTarget
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PROFIT_TARGET, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteProfitTarge(ProfitTarget profitTarget) {
		BaseResult result = new BaseResult();
		try {
			result = profitTargetService.deleteProfitTarge(profitTarget);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息失败[{}]", JSONObject.toJSON(profitTarget), e);
			result.setMsg("删除信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param profitTarget
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_PROFIT_TARGET, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitProfitTarge(ProfitTarget profitTarget) {
		BaseResult result = new BaseResult();
		try {
			result = profitTargetService.submitProfitTarge(profitTarget);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(profitTarget), e);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}
}
