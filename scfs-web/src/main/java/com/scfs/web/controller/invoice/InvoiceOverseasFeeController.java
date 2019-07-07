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
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasFeeReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFeeResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceOverseasFeeService;

/**
 * <pre>
 * 	
 *  File: InvoiceOverseasFeeController.java
 *  Description:境外开票费用业务
 *  TODO
 *  Date,					Who,				
 *  2017年03月30日				Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceOverseasFeeController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceOverseasFeeController.class);
	@Autowired
	private InvoiceOverseasFeeService invoiceOverseasFeeService;

	/**
	 * 获取费用相关信息
	 * 
	 * @param queryRecPayFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_INVOICE_FEE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeeByCond(InvoiceOverseasFeeReqDto overseasFeeResDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		try {
			pageResult = invoiceOverseasFeeService.queryFeeByCond(overseasFeeResDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加境外开票费用失败[{}]", JSONObject.toJSON(overseasFeeResDto), e);
			pageResult.setMsg("添加境外开票费用异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 添加境外开票费用
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_FEE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createInvoiceOverseasFee(@RequestBody InvoiceOverseasFeeReqDto feeReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasFeeService.createInvoiceOverseasFee(feeReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加境外开票费用失败[{}]", JSONObject.toJSON(feeReqDto), e);
			result.setMsg("添加境外开票费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑数据
	 * 
	 * @param invoiceOverseasFee
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_INVOICE_FEE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceOverseasFeeResDto> editInvoiceOverseasFeeById(InvoiceOverseasFeeReqDto feeReqDto) {
		Result<InvoiceOverseasFeeResDto> result = new Result<InvoiceOverseasFeeResDto>();
		try {
			result = invoiceOverseasFeeService.editInvoiceCollectFeeById(feeReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑境外开票费用失败[{}]", JSONObject.toJSON(feeReqDto), e);
			result.setMsg("编辑境外开票费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 修改境外开票费用
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_INVOICE_FEE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoiceOverseasFee(@RequestBody InvoiceOverseasFeeReqDto feeReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasFeeService.updateInvoiceOverseasFee(feeReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改境外开票费用失败[{}]", JSONObject.toJSON(feeReqDto), e);
			result.setMsg("修改境外开票费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除境外开票费用信息
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_INVOICE_FEE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceOverseasFee(InvoiceOverseasFeeReqDto feeReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasFeeService.deleteInvoiceOverseasFee(feeReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除境外开票费用失败[{}]", JSONObject.toJSON(feeReqDto), e);
			result.setMsg("删除境外开票费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取境外开票费用信息
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_FEE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceOverseasFeeResDto> queryInvoiceOverseasFee(InvoiceOverseasFeeReqDto feeReqDto) {
		PageResult<InvoiceOverseasFeeResDto> pageResult = new PageResult<InvoiceOverseasFeeResDto>();
		try {
			pageResult = invoiceOverseasFeeService.queryInvoiceCollectFeeResultsByCon(feeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询境外开票费用信息失败[{}]", JSONObject.toJSON(feeReqDto), e);
			pageResult.setMsg("查询境外开票费用信息异常，请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_ALL_INVOICE_FEE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceOverseasFeeResDto> queryAllInvoiceOverseasFee(InvoiceOverseasFeeReqDto feeReqDto) {
		PageResult<InvoiceOverseasFeeResDto> pageResult = new PageResult<InvoiceOverseasFeeResDto>();
		try {
			pageResult = invoiceOverseasFeeService.queryAllInvoiceCollectFeeResults(feeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询境外开票费用信息失败[{}]", JSONObject.toJSON(feeReqDto), e);
			pageResult.setMsg("查询境外开票费用信息异常，请稍后重试");
		}
		return pageResult;
	}
}
