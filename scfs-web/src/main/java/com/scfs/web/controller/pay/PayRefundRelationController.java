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
import com.scfs.domain.pay.dto.req.PayRefundRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayRefundRelationResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.pay.PayRefundRelationService;

/**
 * <pre>
 *  付款 退款信息服务
 *  File: PayRefundRelationController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年04月19日	Administrator
 *
 * </pre>
 */
@Controller
public class PayRefundRelationController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PayRefundRelationController.class);

	@Autowired
	PayRefundRelationService payRefundRelationService;

	/**
	 * 初始化加载当前付款单下面的退款单信息
	 * 
	 * @param bankReceiptSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_REFUNDRELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayRefundRelationResDto> queryRefundResultByCond(PayRefundRelationReqDto reqDto) {
		PageResult<PayRefundRelationResDto> pageResult = new PageResult<PayRefundRelationResDto>();
		try {
			pageResult = payRefundRelationService.queryRefundResultByCond(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付退单信息失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg("查询付款预收单信息异常，请稍后重试");
		}
		return pageResult;

	}

	/**
	 * 退款信息数据查询 获取水单信息的数据
	 * 
	 * @param payRefundRelationResDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PAY_REFUNDRELA, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BankReceiptResDto> queryRefundResultByCon(BankReceiptSearchReqDto req) {
		PageResult<BankReceiptResDto> pageResult = new PageResult<BankReceiptResDto>();
		try {
			pageResult = payRefundRelationService.queryRefundResultByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询退款水单信息失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询退款信息失败异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 删除退款信息的关联数据
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PAY_REFUNDRELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayRefundByCon(PayRefundRelationReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			payRefundRelationService.deleteRefundByCon(reqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除付退款信息失败[{}]", JSONObject.toJSON(reqDto), e);
			baseResult.setMsg("新增退款信息失败异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 保存水单的数据到退款信息中
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PAY_REFUNDRELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createPayfundByCon(@RequestBody PayRefundRelationReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			payRefundRelationService.createPayfundByCon(reqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增付退款信息失败[{}]", JSONObject.toJSON(reqDto), e);
			baseResult.setMsg("新增退款信息失败异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 退款信息明细编辑
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PAY_REFUNDRELA, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayRefundRelationResDto> editPayRefundByCon(PayRefundRelationReqDto reqDto) {
		Result<PayRefundRelationResDto> Result = new Result<PayRefundRelationResDto>();
		try {
			Result = payRefundRelationService.editPayRefundByCon(reqDto);
		} catch (BaseException e) {
			Result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑付退款信息失败[{}]", JSONObject.toJSON(reqDto), e);
			Result.setMsg("新增退款信息失败异常，请稍后重试");
		}
		return Result;
	}

	/**
	 * 退款信息的修改
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PAY_REFUNDRELA, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayRefundByCon(@RequestBody PayRefundRelationReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = payRefundRelationService.updatePayRefundByCon(reqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改付退款信息失败[{}]", JSONObject.toJSON(reqDto), e);
			baseResult.setMsg("新增退款信息失败异常，请稍后重试");
		}
		return baseResult;
	}
}
