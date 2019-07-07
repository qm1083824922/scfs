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
import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasFeeReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasFeeResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.invoice.InvoiceCollectOverseasFeeService;

/**
 * <pre>
 *  境外收票关联采购单
 *  File: InvoiceCollectOverseasFeeController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月24日			Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceCollectOverseasFeeController {

	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceCollectOverseasFeeController.class);

	@Autowired
	private InvoiceCollectOverseasFeeService collectOverseasFeeService;

	/**
	 * 查询Incoice收票关联费用单的数据列表信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_COLLECT_OVER_FEE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectOverseasFeeResDto> queryInvoiceCollectFeeResult(
			InvoiceCollectOverseasFeeReqDto reqDto) {
		PageResult<InvoiceCollectOverseasFeeResDto> pageResult = new PageResult<InvoiceCollectOverseasFeeResDto>();
		try {
			pageResult = collectOverseasFeeService.queryInvoiceCollectFeeResult(reqDto);
		} catch (BaseException e) {
			LOGGER.error("查询境外收票费用明细列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg(e.getMsg());
			return pageResult;
		} catch (Exception e) {
			LOGGER.error("查询境外收票费用明细列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询境外收票费用列表信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 查询Incoice收票关联费用单明细数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_INVOICE_COLLECT_OVER_FEE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectOverseasFeeResDto> queryFeeResultByCon(QueryFeeReqDto reqDto) {
		PageResult<InvoiceCollectOverseasFeeResDto> result = new PageResult<InvoiceCollectOverseasFeeResDto>();
		try {
			result = collectOverseasFeeService.queryFeeResultByCon(reqDto);
		} catch (BaseException e) {
			LOGGER.error("查询境外收票采购明细信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询境外收票采购明细信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("查询境外收票采购明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加费用单
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_COLLECT_OVER_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createInvoiceCollectOverseasFee(@RequestBody InvoiceCollectOverseasFeeReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			collectOverseasFeeService.createInvoiceFeeByCon(reqDto);
		} catch (BaseException e) {
			LOGGER.error("添加境外收票费用明细信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加境外收票费用明细信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("添加境外收票费用明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除Incoice收票关联费用单明细数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_INVOICE_COLLECT_OVER_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceCollectFeeByIds(InvoiceCollectOverseasFeeReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			collectOverseasFeeService.deleteInvoiceCollectFeeByIds(reqDto);
		} catch (BaseException e) {
			LOGGER.error("删除境外收票费用明细列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除境外收票费用明细列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("删除境外收票费用列表信息异常，请稍后重试");
		}
		return result;
	}

}
