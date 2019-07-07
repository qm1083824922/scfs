package com.scfs.web.controller.invoice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.BaseResult;
import com.scfs.domain.invoice.dto.req.InvoiceReqList;
import com.scfs.domain.invoice.dto.req.InvoiceSaleManagerReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceSaleManagerResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceSaleService;
import com.scfs.web.controller.BaseController;

@Controller
public class InvoiceSaleController extends BaseController {

	@Autowired
	private InvoiceSaleService invoiceSaleService;

	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceSaleController.class);

	/**
	 * 查询销售信息(已选择)
	 * 
	 * @param queryInvoiceFeeDto
	 * @return
	 */

	@RequestMapping(value = BusUrlConsts.QUERYSALESELECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceSaleManagerResDto> queryInvoiceFeeSelectByCon(
			InvoiceSaleManagerReqDto invoiceSaleManagerReqDto) {
		PageResult<InvoiceSaleManagerResDto> pageResult = new PageResult<InvoiceSaleManagerResDto>();
		try {
			pageResult = invoiceSaleService.queryInvoiceResultsByCon(invoiceSaleManagerReqDto);

		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(invoiceSaleManagerReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 编辑
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDITSALE, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceSaleManagerResDto> editGoodsById(InvoiceSaleManagerReqDto invoiceSaleManagerReqDto) {
		Result<InvoiceSaleManagerResDto> result = new Result<InvoiceSaleManagerResDto>();
		try {
			result = invoiceSaleService.queryInvoiceResultsById(invoiceSaleManagerReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询销售信息(未选择)
	 * 
	 * @param queryInvoiceFeeDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYSALENOTSELECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceSaleManagerResDto> queryInvoiceFeeByCon(InvoiceSaleManagerReqDto queryInvoiceSaleDto) {
		PageResult<InvoiceSaleManagerResDto> pageResult = new PageResult<InvoiceSaleManagerResDto>();
		try {
			pageResult = invoiceSaleService.querySaleNotSelectByCon(queryInvoiceSaleDto);

		} catch (BaseException e) {

			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(queryInvoiceSaleDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 
	 * 批量删除费用信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEBATCHSALE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoice(BaseReqDto baseReqDto) {
		BaseResult result = new Result<InvoiceSaleManagerResDto>();
		if (!CollectionUtils.isEmpty(baseReqDto.getIds())) {
			try {
				invoiceSaleService.deleteInvoiceByIds(baseReqDto.getIds());
			} catch (BaseException e) {

				result.setMsg(e.getMsg());
			} catch (Exception e) {
				LOGGER.error("批量删除销售信息异常[{}]", JSONObject.toJSON(baseReqDto.getIds()), e);
				result.setMsg("系统异常，请稍后重试");
			}
		} else {
			result.setMsg("请选择需操作的记录！");
		}
		return result;
	}

	/**
	 * 
	 * 删除单条费用信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETESINGLESALE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteSingleFee(Integer id) {
		BaseResult result = new Result<InvoiceSaleManagerResDto>();
		try {
			invoiceSaleService.deleteInvoiceById(id);
		} catch (BaseException e) {

			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除费用信息异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("系统异常，请稍后重试");
		}

		return result;
	}

	/**
	 * 
	 * 批量修改费用信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATEINVOICESALE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoice(InvoiceSaleManagerResDto invoiceManager) {
		BaseResult result = new Result<InvoiceSaleManagerResDto>();
		if (invoiceManager != null) {
			try {
				result = invoiceSaleService.updateSaleByCon(invoiceManager);
			} catch (BaseException e) {
				result.setMsg(e.getMsg());
			} catch (Exception e) {
				LOGGER.error("更新销售信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
				result.setMsg("系统异常，请稍后重试");
			}
		} else {
			result.setMsg("请选择需操作的记录！");
		}
		return result;
	}

	/**
	 * 批量插入费用信息
	 */
	@RequestMapping(value = BusUrlConsts.INSERTINVOICESALE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult insertInvoice(@RequestBody InvoiceReqList invoiceManager) {
		BaseResult result = new Result<InvoiceSaleManagerResDto>();
		try {
			result = invoiceSaleService.addBactchInvoice(invoiceManager);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量插入费用信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
			result.setMsg("您所插入的金额超出，请稍后重试");
		}
		return result;
	}
}
