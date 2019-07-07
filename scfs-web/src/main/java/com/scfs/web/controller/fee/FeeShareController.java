package com.scfs.web.controller.fee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.FeeShareReqDto;
import com.scfs.domain.fee.dto.resp.FeeShareResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fee.FeeShareService;

/**
 * <pre>
 * 	管理费用分摊Controller
 *  File: FeeShareController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月17日				Administrator
 *
 * </pre>
 */
@Controller
public class FeeShareController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FeeShareController.class);

	@Autowired
	private FeeShareService feeShareService;

	/**
	 * 新建分摊金额
	 * 
	 * @param feeShareReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_FEE_SHARE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createFeeShare(@RequestBody FeeShareReqDto feeShareReqDto) {
		BaseResult result = new BaseResult();
		try {
			feeShareService.createFeeShare(feeShareReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("新增分摊金额异常,入参：[{}],{},{}", JSONObject.toJSON(feeShareReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("新增分摊金额异常：[{}]", JSONObject.toJSON(feeShareReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除
	 * 
	 * @param feeShareReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_FEE_SHARE, method = RequestMethod.POST)
	@ResponseBody
	BaseResult deleteFeeShare(FeeShareReqDto feeShareReqDto) {
		BaseResult result = new BaseResult();
		try {
			feeShareService.deleteFeeShare(feeShareReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("删除分摊金额列表异常,入参：[{}],{},{}", JSONObject.toJSON(feeShareReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("删除分摊金额列表异常：[{}]", JSONObject.toJSON(feeShareReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取分摊信息
	 * 
	 * @param feeShareReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_FEE_SHARE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeShareResDto> queryFeeShareResultsByCon(FeeShareReqDto feeShareReqDto) {
		PageResult<FeeShareResDto> pageResult = new PageResult<FeeShareResDto>();
		try {
			pageResult = feeShareService.queryManageShareResultsByCon(feeShareReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询分摊信息失败[{}]", JSONObject.toJSON(feeShareReqDto), e);
			pageResult.setMsg("查询分摊信息异常，请稍后重试");
		}
		return pageResult;
	}

}
