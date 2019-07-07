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
import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayPoRelationResDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.pay.PayPoRelationService;

/**
 * <pre>
 *  付款订单
 *  File: PayPoRelationController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月09日			Administrator
 *
 * </pre>
 */
@Controller
public class PayPoRelationController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PayPoRelationController.class);

	@Autowired
	private PayPoRelationService payPoRelationService;

	/**
	 * 付款订单信息列表
	 * 
	 * @param payPoRelationSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_PORELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayPoRelationResDto> queryPoTitlesResultsByCon(PayPoRelationReqDto payPoRelationSearchReqDto) {
		PageResult<PayPoRelationResDto> pageResult = new PageResult<PayPoRelationResDto>();
		try {
			pageResult = payPoRelationService.queryPayPoRelationResultsByCon(payPoRelationSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款订单信息失败[{}]", JSONObject.toJSON(payPoRelationSearchReqDto), e);
			pageResult.setMsg("查询付款订单信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 获取完成订单
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PAY_PORELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> pageResult = new PageResult<PoLineModel>();
		try {
			pageResult = payPoRelationService.queryPoTitlesResultsByCon(poTitleReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询完成订单信息失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg("查询完成订单信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 付款订单添加
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PAY_PORELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createPayPoRelation(@RequestBody PayPoRelationReqDto payPoRelation) {
		BaseResult baseResult = new BaseResult();
		try {
			payPoRelationService.createPayPoRelation(payPoRelation);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款订单添加失败[{}]", JSONObject.toJSON(payPoRelation), e);
			baseResult.setSuccess(false);
			baseResult.setMsg("付款订单单添加异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 编辑付款订单信息
	 * 
	 * @param payPoRelation
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PAY_PORELA, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayPoRelationResDto> editPayOrderById(PayPoRelationReqDto payPoRelation) {
		Result<PayPoRelationResDto> result = new Result<PayPoRelationResDto>();
		try {
			result = payPoRelationService.editPayPoRelationById(payPoRelation);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑付款订单信息失败[{}]", JSONObject.toJSON(payPoRelation), e);
			result.setMsg("编辑付款订单信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 付款订单修改
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PAY_PORELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayPoRelation(@RequestBody PayPoRelationReqDto payPoRelation) {
		BaseResult baseResult = new BaseResult();
		try {
			payPoRelationService.updatePayPoRelation(payPoRelation);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款订单修改失败[{}]", JSONObject.toJSON(payPoRelation), e);
			baseResult.setMsg("付款订单修改异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 删除付款订单
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_PAY_PORELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePoRelation(PayPoRelationReqDto payPoRelation) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payPoRelationService.deletePayPoRelation(payPoRelation);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除付款订单失败[{}]", JSONObject.toJSON(payPoRelation), e);
			baseResult.setMsg("删除付款订单异常，请稍后重试");
		}
		return baseResult;
	}

}
