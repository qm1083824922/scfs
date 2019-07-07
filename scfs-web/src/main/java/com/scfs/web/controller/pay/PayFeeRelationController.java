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
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayFeeRelationResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.pay.PayFeeRelationService;

/**
 * <pre>
 *  付款费用
 *  File: PayFeeRelationController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月10日			Administrator
 *
 * </pre>
 */
@Controller
public class PayFeeRelationController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PayFeeRelationController.class);

	@Autowired
	PayFeeRelationService payFeeRelationService;

	/**
	 * 查询付款费用信息
	 * 
	 * @param payFeeRelationReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayFeeRelationResDto> queryPayFeeRelationResultsByCon(PayFeeRelationReqDto payFeeRelationReqDto) {
		PageResult<PayFeeRelationResDto> pageResult = new PageResult<PayFeeRelationResDto>();
		try {
			pageResult = payFeeRelationService.queryPayFeeRelationResultsByCon(payFeeRelationReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询付款费用信息失败[{}]", JSONObject.toJSON(payFeeRelationReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款费用信息失败[{}]", JSONObject.toJSON(payFeeRelationReqDto), e);
			pageResult.setMsg("查询付款费用信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 获取完成费用信息
	 * 
	 * @param queryRecPayFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PAY_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeeByCond(QueryFeeReqDto queryRecPayFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		try {
			pageResult = payFeeRelationService.queryFeeByCond(queryRecPayFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询完成费用信息失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询完成费用信息失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg("查询完成费用信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 付款费用添加
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PAY_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult craetePayFeeRelation(@RequestBody PayFeeRelationReqDto payFeeRelation) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payFeeRelationService.craetePayFeeRelation(payFeeRelation);
		} catch (BaseException e) {
			LOGGER.error("付款费用添加失败[{}]", JSONObject.toJSON(payFeeRelation), e);
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款费用添加失败[{}]", JSONObject.toJSON(payFeeRelation), e);
			baseResult.setMsg("付款费用添加异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 编辑付款费用信息
	 * 
	 * @param payPoRelation
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PAY_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayFeeRelationResDto> editPayFeeRelationById(PayFeeRelationReqDto payFeeRelationReqDto) {
		Result<PayFeeRelationResDto> result = new Result<PayFeeRelationResDto>();
		try {
			result = payFeeRelationService.editPayFeeRelationById(payFeeRelationReqDto);
		} catch (BaseException e) {
			LOGGER.error("编辑付款费用信息失败[{}]", JSONObject.toJSON(payFeeRelationReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑付款费用信息失败[{}]", JSONObject.toJSON(payFeeRelationReqDto), e);
			result.setMsg("编辑付款费用信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 付款费用修改
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PAY_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayPoRelation(@RequestBody PayFeeRelationReqDto payFeeRelation) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payFeeRelationService.updatePayFeeRelation(payFeeRelation);
		} catch (BaseException e) {
			LOGGER.error("付款费用修改失败[{}]", JSONObject.toJSON(payFeeRelation), e);
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款费用修改失败[{}]", JSONObject.toJSON(payFeeRelation), e);
			baseResult.setMsg("付款费用修改异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 删除付款费用
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_PAY_FEERELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayFeeRelation(PayFeeRelationReqDto payFeeRelation) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payFeeRelationService.deletePayFeeRelation(payFeeRelation);
		} catch (BaseException e) {
			LOGGER.error("删除付款费用失败[{}]", JSONObject.toJSON(payFeeRelation), e);
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除付款费用失败[{}]", JSONObject.toJSON(payFeeRelation), e);
			baseResult.setMsg("删除付款费用异常，请稍后重试");
		}
		return baseResult;
	}
}
