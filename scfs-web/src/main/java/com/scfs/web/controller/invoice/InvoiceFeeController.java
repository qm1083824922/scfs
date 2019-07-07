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
import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceFeeManagerReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceReqList;
import com.scfs.domain.invoice.dto.resp.InvoiceFeeManagerResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceFeeService;
import com.scfs.web.controller.BaseController;

/**
 * 费用信息
 * 
 * @author Administrator
 *
 */
@Controller
public class InvoiceFeeController extends BaseController {

	@Autowired
	private InvoiceFeeService invoiceFeeService;

	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceFeeController.class);

	/**
	 * 查询费用信息(已选择)
	 * 
	 * @param queryInvoiceFeeDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYINOVICEFEESELECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceFeeManagerResDto> queryInvoiceFeeSelectByCon(
			InvoiceApplyManagerReqDto queryInvoiceFeeDto) {
		PageResult<InvoiceFeeManagerResDto> pageResult = new PageResult<InvoiceFeeManagerResDto>();
		try {
			pageResult = invoiceFeeService.queryInvoiceResultsByCon(queryInvoiceFeeDto);

		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(queryInvoiceFeeDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 查询费用信息(未选择)
	 * 
	 * @param queryInvoiceFeeDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYINOVICEFEENOTSELECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceFeeManagerResDto> queryInvoiceFeeByCon(InvoiceFeeManagerReqDto queryInvoiceFee) {
		PageResult<InvoiceFeeManagerResDto> pageResult = new PageResult<InvoiceFeeManagerResDto>();
		try {
			pageResult = invoiceFeeService.queryInvoiceByCon(queryInvoiceFee);

		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(queryInvoiceFee), e);
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
	@RequestMapping(value = BusUrlConsts.BATCHDELETEFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoice(BaseReqDto baseReqDto) {
		BaseResult result = new Result<InvoiceFeeManagerResDto>();
		if (!CollectionUtils.isEmpty(baseReqDto.getIds())) {
			try {
				invoiceFeeService.deleteInvoiceByIds(baseReqDto.getIds());
			} catch (BaseException e) {

				result.setMsg(e.getMsg());
			} catch (Exception e) {
				LOGGER.error("批量删除费用信息异常[{}]", JSONObject.toJSON(baseReqDto.getIds()), e);
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
	@RequestMapping(value = BusUrlConsts.DELETEINVOICEFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteSingleFee(Integer id) {
		BaseResult result = new Result<InvoiceFeeManagerResDto>();
		try {
			invoiceFeeService.deleteInvoiceById(id);
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
	 * 修改费用信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.BATCHUPDATEFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoice(InvoiceFeeManagerReqDto invoiceManager) {
		BaseResult result = new Result<InvoiceFeeManagerResDto>();
		if (invoiceManager != null) {
			try {
				result = invoiceFeeService.updateInvoice(invoiceManager);
			} catch (BaseException e) {
				result.setMsg(e.getMsg());
			} catch (Exception e) {
				LOGGER.error("批量更新费用信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
				result.setMsg("系统异常，请稍后重试");
			}
		} else {
			result.setMsg("请选择需操作的记录！");
		}
		return result;
	}

	/**
	 * 编辑
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDITFEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceFeeManagerResDto> editInvoiceFeeById(InvoiceFeeManagerReqDto invoiceFeeManagerReqDto) {
		Result<InvoiceFeeManagerResDto> result = new Result<InvoiceFeeManagerResDto>();
		try {
			result = invoiceFeeService.queryInvoiceResultsById(invoiceFeeManagerReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量插入费用信息
	 */
	@RequestMapping(value = BusUrlConsts.BATCHINSERTFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult insertInvoice(@RequestBody InvoiceReqList invoiceManager) {
		BaseResult result = new Result<InvoiceFeeManagerResDto>();
		try {
			result = invoiceFeeService.addBactchInvoice(invoiceManager);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量插入费用信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
			result.setMsg("批量插入费用信息异常，请稍后重试");
		}
		return result;
	}

}
