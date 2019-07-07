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
import com.scfs.domain.invoice.dto.req.InvoiceOverseasPoReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasPoResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceOverseasPoService;

/**
 * <pre>
 * 	
 *  File: InvoiceOverseasPoController.java
 *  Description:境外开票销售单业务
 *  TODO
 *  Date,					Who,				
 *  2017年03月30日				Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceOverseasPoController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceOverseasPoController.class);
	@Autowired
	private InvoiceOverseasPoService invoiceOverseasPoService;

	/**
	 * 获取采购单相关信息
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_INVOICE_PO_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceOverseasPoResDto> queryPoLinesByCon(InvoiceOverseasPoReqDto poReqDto) {
		PageResult<InvoiceOverseasPoResDto> pageResult = new PageResult<InvoiceOverseasPoResDto>();
		try {
			pageResult = invoiceOverseasPoService.querySaleNotSelectByCon(poReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加采购信息失败[{}]", JSONObject.toJSON(poReqDto), e);
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
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_PO_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createInvoiceOverseasPo(@RequestBody InvoiceOverseasPoReqDto poReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasPoService.createInvoiceOverseasPo(poReqDto);
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
	 * @param invoiceOverseasPo
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_INVOICE_PO_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceOverseasPoResDto> editInvoiceOverseasPoById(InvoiceOverseasPoReqDto poReqDto) {
		Result<InvoiceOverseasPoResDto> result = new Result<InvoiceOverseasPoResDto>();
		try {
			result = invoiceOverseasPoService.editInvoiceCollectPoById(poReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑采购单信息失败[{}]", JSONObject.toJSON(poReqDto), e);
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
	@RequestMapping(value = BusUrlConsts.UPDATE_INVOICE_PO_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoiceOverseasPoById(@RequestBody InvoiceOverseasPoReqDto poReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasPoService.updateInvoiceOverseasPo(poReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_INVOICE_PO_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceOverseasPoByIds(InvoiceOverseasPoReqDto poReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasPoService.deleteInvoiceOverseasPo(poReqDto);
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
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_PO_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceOverseasPoResDto> queryInvoiceOverseasPo(InvoiceOverseasPoReqDto poReqDto) {
		PageResult<InvoiceOverseasPoResDto> pageResult = new PageResult<InvoiceOverseasPoResDto>();
		try {
			pageResult = invoiceOverseasPoService.queryInvoiceCollectPoResultsByCon(poReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询采购信息失败[{}]", JSONObject.toJSON(poReqDto), e);
			pageResult.setMsg("查询采购信息异常，请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ALL_INVOICE_PO_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceOverseasPoResDto> queryAllInvoiceOverseasPo(InvoiceOverseasPoReqDto poReqDto) {
		PageResult<InvoiceOverseasPoResDto> pageResult = new PageResult<InvoiceOverseasPoResDto>();
		try {
			pageResult = invoiceOverseasPoService.queryAllInvoiceCollectPoResults(poReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询采购信息失败[{}]", JSONObject.toJSON(poReqDto), e);
			pageResult.setMsg("查询采购信息异常，请稍后重试");
		}
		return pageResult;
	}

}
