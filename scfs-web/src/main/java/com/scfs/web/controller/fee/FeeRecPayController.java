package com.scfs.web.controller.fee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.FeeRecPayReqDto;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fee.FeeRecPayService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: FeeRecPayController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年11月29日			Administrator
 *
 * </pre>
 */
@Controller
public class FeeRecPayController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FeeRecPayController.class);
	@Autowired
	FeeRecPayService feeRecPayService;

	/**
	 * 获取未关联数据
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_FEE_SPEC_PAY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeePayByNotRecCond(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		queryFeeReqDto.setFeeType(BaseConsts.ONE);
		try {
			pageResult = feeRecPayService.queryFeePayByNotRecCond(queryFeeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付费用异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg("查询应付费用失败，请重试");
		}
		return pageResult;
	}

	/**
	 * 获取关联数据
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_FEE_SPEC_PAY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeePayByRecCond(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		queryFeeReqDto.setFeeType(BaseConsts.ONE);
		try {
			pageResult = feeRecPayService.queryFeePayByRecCond(queryFeeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付费用异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg("查询应付费用失败，请重试");
		}
		return pageResult;
	}

	/**
	 * 添加关联数据
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_FEE_SPEC_PAY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult saveFeeRecPay(FeeRecPayReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			result = feeRecPayService.saveFeeRecPay(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("添加失败，请重试");
		}
		return result;
	}

	/**
	 * 删除批量关联数据
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_FEE_SPEC_PAY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult update(FeeRecPayReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			result = feeRecPayService.update(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("删除失败，请重试");
		}
		return result;
	}
}
