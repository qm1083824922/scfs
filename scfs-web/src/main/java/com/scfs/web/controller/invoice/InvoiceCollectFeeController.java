package com.scfs.web.controller.invoice;

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
import com.scfs.domain.invoice.dto.req.InvoiceCollectFeeReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectFeeResDto;
import com.scfs.domain.invoice.entity.InvoiceCollectFee;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceCollectFeeService;

/**
 * <pre>
 *  收票费用
 *  File: InvoiceCollectFeeController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日			Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceCollectFeeController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceCollectFeeController.class);

	@Autowired
	InvoiceCollectFeeService invoiceCollectFeeService;

	/**
	 * 获取费用相关信息
	 * 
	 * @param queryRecPayFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_INVOICE_FEE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeeByCond(QueryFeeReqDto queryRecPayFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		try {
			pageResult = invoiceCollectFeeService.queryFeeByCond(queryRecPayFeeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加收票费用失败[{}]", JSONObject.toJSON(queryRecPayFeeReqDto), e);
			pageResult.setMsg("添加收票费用异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 添加收票费用
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_FEE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createInvoiceCollectFee(@RequestBody InvoiceCollectFeeReqDto feeReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectFeeService.createInvoiceCollectFee(feeReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加收票费用失败[{}]", JSONObject.toJSON(feeReqDto), e);
			result.setMsg("添加收票费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑数据
	 * 
	 * @param invoiceCollectFee
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_INVOICE_FEE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceCollectFeeResDto> editInvoiceCollectFeeById(InvoiceCollectFee invoiceCollectFee) {
		Result<InvoiceCollectFeeResDto> result = new Result<InvoiceCollectFeeResDto>();
		try {
			result = invoiceCollectFeeService.editInvoiceCollectFeeById(invoiceCollectFee);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑收票费用失败[{}]", JSONObject.toJSON(invoiceCollectFee), e);
			result.setMsg("编辑收票费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 修改收票费用
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_INVOICE_FEE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoiceCollectFee(@RequestBody InvoiceCollectFeeReqDto feeReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectFeeService.updateInvoiceCollectFeeById(feeReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改收票费用失败[{}]", JSONObject.toJSON(feeReqDto), e);
			result.setMsg("修改收票费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除收票费用信息
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_INVOICE_FEE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceCollectFee(InvoiceCollectFeeReqDto feeReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectFeeService.deleteInvoiceCollectFeeById(feeReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除收票费用失败[{}]", JSONObject.toJSON(feeReqDto), e);
			result.setMsg("删除收票费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取收票费用信息
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_FEE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectFeeResDto> queryInvoiceCollectFee(InvoiceCollectFeeReqDto feeReqDto) {
		PageResult<InvoiceCollectFeeResDto> pageResult = new PageResult<InvoiceCollectFeeResDto>();
		try {
			pageResult = invoiceCollectFeeService.queryInvoiceCollectFeeResultsByCon(feeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询收票费用信息失败[{}]", JSONObject.toJSON(feeReqDto), e);
			pageResult.setMsg("查询收票费用信息异常，请稍后重试");
		}
		return pageResult;
	}
}
