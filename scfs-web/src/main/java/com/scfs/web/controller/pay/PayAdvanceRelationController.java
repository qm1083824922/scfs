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
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.pay.dto.req.PayAdvanceRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayAdvanceRelationResDto;
import com.scfs.domain.pay.entity.PayAdvanceRelation;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.pay.PayAdvanceRelationService;

/**
 * <pre>
 *  付款预收单
 *  File: PayAdvanceRelationController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月23日			Administrator
 *
 * </pre>
 */
@Controller
public class PayAdvanceRelationController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PayAdvanceRelationController.class);

	@Autowired
	PayAdvanceRelationService payAdvanRelationService;

	/**
	 * 获取付款预收单信息
	 * 
	 * @param PayAdvanceRelationReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_ADVANRELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayAdvanceRelationResDto> queryAdvanByCond(PayAdvanceRelationReqDto req) {
		PageResult<PayAdvanceRelationResDto> pageResult = new PageResult<PayAdvanceRelationResDto>();
		try {
			pageResult = payAdvanRelationService.queryPayAdvanRelationResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款预收单信息失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg("查询付款预收单信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 查询预收单信息
	 * 
	 * @param payADVANRELAtionReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PAY_ADVANRELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BankReceiptResDto> queryAdvanceResultsByCon(BankReceiptSearchReqDto req) {
		PageResult<BankReceiptResDto> pageResult = new PageResult<BankReceiptResDto>();
		try {
			pageResult = payAdvanRelationService.queryAdvanceResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询预收单信息失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询预收单信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 付款预收单添加
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PAY_ADVANRELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult craetePayAdvanceRelation(@RequestBody PayAdvanceRelationReqDto record) {
		BaseResult baseResult = new BaseResult();
		try {
			payAdvanRelationService.createPayAdvanceRelation(record);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款预收单添加失败[{}]", JSONObject.toJSON(record), e);
			baseResult.setSuccess(false);
			baseResult.setMsg("付款预收单添加异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 编辑付款预收单信息
	 * 
	 * @param payPoRelation
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PAY_ADVANRELA, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayAdvanceRelationResDto> editPayADVANRELAtionById(PayAdvanceRelation payAdvanceRelation) {
		Result<PayAdvanceRelationResDto> result = new Result<PayAdvanceRelationResDto>();
		try {
			result = payAdvanRelationService.editPayAdvanceRelationById(payAdvanceRelation);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑付款预收单信息失败[{}]", JSONObject.toJSON(payAdvanceRelation), e);
			result.setMsg("编辑付款预收单信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 付款预收单修改
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PAY_ADVANRELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayPoRelation(@RequestBody PayAdvanceRelationReqDto record) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payAdvanRelationService.updatePayAdvanceRelation(record);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("付款预收单修改失败[{}]", JSONObject.toJSON(record), e);
			baseResult.setMsg("付款预收单修改异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 删除付款预收单
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_PAY_ADVANRELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayADVANRELAtion(PayAdvanceRelationReqDto payAdvanRelation) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payAdvanRelationService.deletePayAdvanRelation(payAdvanRelation);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除付款预收单失败[{}]", JSONObject.toJSON(payAdvanRelation), e);
			baseResult.setMsg("删除付款预收单异常，请稍后重试");
		}
		return baseResult;
	}
}
