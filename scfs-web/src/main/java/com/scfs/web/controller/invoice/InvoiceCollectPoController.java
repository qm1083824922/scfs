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
import com.scfs.domain.invoice.dto.req.InvoiceCollectPoReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectPoResDto;
import com.scfs.domain.invoice.entity.InvoiceCollectPo;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceCollectPoService;

/**
 * <pre>
 *  采购单
 *  File: InvoiceCollectPoController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月30日			Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceCollectPoController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceCollectPoController.class);

	@Autowired
	InvoiceCollectPoService invoiceCollectPoService;

	/**
	 * 获取采购单相关信息
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_INVOICE_PO_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoLinesByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> pageResult = new PageResult<PoLineModel>();
		try {
			pageResult = invoiceCollectPoService.queryPoLinesByCon(poTitleReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加采购信息失败[{}]", JSONObject.toJSON(poTitleReqDto), e);
			pageResult.setMsg("添加采购信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 添加采购单
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_PO_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createInvoiceCollectPo(@RequestBody InvoiceCollectPoReqDto poReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectPoService.createInvoiceCollectPo(poReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加采购信息失败[{}]", JSONObject.toJSON(poReqDto), e);
			result.setMsg("添加采购信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑数据
	 * 
	 * @param invoiceCollectPo
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_INVOICE_PO_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceCollectPoResDto> editInvoiceCollectPoById(InvoiceCollectPo invoiceCollectPo) {
		Result<InvoiceCollectPoResDto> result = new Result<InvoiceCollectPoResDto>();
		try {
			result = invoiceCollectPoService.editInvoiceCollectPoById(invoiceCollectPo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑采购单信息失败[{}]", JSONObject.toJSON(invoiceCollectPo), e);
			result.setMsg("编辑采购单信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 修改采购
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_INVOICE_PO_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoiceCollectPoById(@RequestBody InvoiceCollectPoReqDto poReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectPoService.updateInvoiceCollectPoById(poReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改采购信息失败[{}]", JSONObject.toJSON(poReqDto), e);
			result.setMsg("修改采购信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除采购信息
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_INVOICE_PO_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceCollectPoByIds(InvoiceCollectPoReqDto poReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectPoService.deleteInvoiceCollectPoByIds(poReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除采购信息失败[{}]", JSONObject.toJSON(poReqDto), e);
			result.setMsg("删除采购信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取采购信息
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_PO_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectPoResDto> queryInvoiceCollectPo(InvoiceCollectPoReqDto poReqDto) {
		PageResult<InvoiceCollectPoResDto> pageResult = new PageResult<InvoiceCollectPoResDto>();
		try {
			pageResult = invoiceCollectPoService.queryInvoiceCollectPoResultsByCon(poReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询采购信息失败[{}]", JSONObject.toJSON(poReqDto), e);
			pageResult.setMsg("查询采购信息异常，请稍后重试");
		}
		return pageResult;
	}
}
