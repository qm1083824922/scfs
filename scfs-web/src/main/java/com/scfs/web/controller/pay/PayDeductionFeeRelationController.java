package com.scfs.web.controller.pay;

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
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.pay.dto.req.PayDeductionFeeRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayDeductionFeeRelationResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.pay.PayDeductionFeeRelationService;

@Controller
public class PayDeductionFeeRelationController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PayDeductionFeeRelationController.class);

	@Autowired
	private PayDeductionFeeRelationService payDeductionFeeRelationService;

	/**
	 * 查询付款抵扣费用信息
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_DEDUCTION_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayDeductionFeeRelationResDto> queryPayDeductionFeeRelationResultsByCon(
			PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		PageResult<PayDeductionFeeRelationResDto> pageResult = new PageResult<PayDeductionFeeRelationResDto>();
		try {
			pageResult = payDeductionFeeRelationService
					.queryPayDeductionFeeRelationResultsByCon(payDeductionFeeRelationReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询付款抵扣费用信息失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款抵扣费用信息失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			pageResult.setMsg("查询付款抵扣费用信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 获取完成抵扣费用信息
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PAY_DEDUCTION_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeeByCond(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		try {
			pageResult = payDeductionFeeRelationService.queryFeeByCond(queryFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询完成抵扣费用信息失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询完成抵扣费用信息失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg("查询完成抵扣费用信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 付款抵扣费用添加
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PAY_DEDUCTION_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createPayDeductionFeeRelation(
			@RequestBody PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payDeductionFeeRelationService.createPayDeductionFeeRelation(payDeductionFeeRelationReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款抵扣费用添加失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款抵扣费用添加失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			baseResult.setMsg("付款抵扣费用添加异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 编辑付款抵扣费用信息
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PAY_DEDUCTION_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayDeductionFeeRelationResDto> editPayDeductionFeeRelationById(
			PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		Result<PayDeductionFeeRelationResDto> result = new Result<PayDeductionFeeRelationResDto>();
		try {
			result = payDeductionFeeRelationService.editPayDeductionFeeRelationById(payDeductionFeeRelationReqDto);
		} catch (BaseException e) {
			LOGGER.error("编辑付款抵扣费用信息失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑付款抵扣费用信息失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			result.setMsg("编辑付款抵扣费用信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 付款抵扣费用修改
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PAY_DEDUCTION_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayDeductionFeeRelation(
			@RequestBody PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payDeductionFeeRelationService.updatePayDeductionFeeRelation(payDeductionFeeRelationReqDto);
		} catch (BaseException e) {
			LOGGER.error("付款抵扣费用修改失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款抵扣费用修改失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			baseResult.setMsg("付款抵扣费用修改异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 删除付款抵扣费用
	 * 
	 * @param payDeductionFeeRelationReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_PAY_DEDUCTION_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayDeductionFeeRelation(PayDeductionFeeRelationReqDto payDeductionFeeRelationReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payDeductionFeeRelationService.deletePayDeductionFeeRelation(payDeductionFeeRelationReqDto);
		} catch (BaseException e) {
			LOGGER.error("删除付款抵扣费用失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除付款抵扣费用失败[{}]", JSONObject.toJSON(payDeductionFeeRelationReqDto), e);
			baseResult.setMsg("删除付款抵扣费用异常，请稍后重试");
		}
		return baseResult;
	}
}
