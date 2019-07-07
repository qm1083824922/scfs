package com.scfs.web.controller.invoice;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasResDto;
import com.scfs.domain.invoice.entity.InvoiceCollectOverseas;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceCollectOverseasService;

/**
 * <pre>
 *  境外收票
 *  File: InvoiceCollectOverseasController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月24日			Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceCollectOverseasController {

	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceCollectOverseasController.class);

	@Autowired
	private InvoiceCollectOverseasService invoiceCollectOverseasService;

	/**
	 * 查询Invoice境外收票列表信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectOverseasResDto> queryInvoiceOverseasResultByCon(
			InvoiceCollectOverseasReqDto reqDto) {
		PageResult<InvoiceCollectOverseasResDto> pageResult = new PageResult<InvoiceCollectOverseasResDto>();
		try {
			pageResult = invoiceCollectOverseasService.queryInvoiceOverseasResultByCon(reqDto);
		} catch (BaseException e) {
			LOGGER.error("查询境外收票列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg(e.getMsg());
			return pageResult;
		} catch (Exception e) {
			LOGGER.error("查询境外收票列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询境外收票列表信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 创建境外开票数据
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createInvoiceCollectOverseas(InvoiceCollectOverseasReqDto reqDto) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = invoiceCollectOverseasService.createInvoiceCollectOver(reqDto);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
			LOGGER.error("添加Invoice收票信息异常[{}]", JSONObject.toJSON(reqDto), e);
		} catch (Exception e) {
			LOGGER.error("添加Invoice收票信息异常[{}]", JSONObject.toJSON(reqDto), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;

	}

	/**
	 * 浏览境外开票数据详情
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceCollectOverseasResDto> queryInvoiceCollectOverById(InvoiceCollectOverseasReqDto reqDto) {
		Result<InvoiceCollectOverseasResDto> result = new Result<InvoiceCollectOverseasResDto>();
		try {
			result = invoiceCollectOverseasService.queryInvoiceCollectOverById(reqDto.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("浏览Invoice收票信息异常[{}]", JSONObject.toJSON(reqDto), e);
		} catch (Exception e) {
			LOGGER.error("浏览Invoice收票信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setSuccess(false);
			result.setMsg("浏览异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑当前境外开票
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceCollectOverseasResDto> editInvoiceCollectById(InvoiceCollectOverseasReqDto reqDto) {
		Result<InvoiceCollectOverseasResDto> result = new Result<InvoiceCollectOverseasResDto>();
		try {
			result = invoiceCollectOverseasService.queryInvoiceCollectOverById(reqDto.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("编辑Invoice收票信息异常[{}]", JSONObject.toJSON(reqDto), e);
		} catch (Exception e) {
			LOGGER.error("编辑境外开票详情信息异常[{}]", reqDto, e);
			result.setSuccess(false);
			result.setMsg("编辑异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新Invoice收票
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoiceCollectById(InvoiceCollectOverseasReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasService.updateInvoiceCollectOverById(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("更新Invoice收票信息异常[{}]", JSONObject.toJSON(reqDto), e);
		} catch (Exception e) {
			LOGGER.error("更新境外开票详情信息异常[{}]", reqDto, e);
			result.setSuccess(false);
			result.setMsg("更新异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 刪除当前境外发票
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceCollect(InvoiceCollectOverseasReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasService.deleteInvoiceCollectOver(reqDto.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("删除Invoice收票信息异常[{}]", JSONObject.toJSON(reqDto), e);
		} catch (Exception e) {
			LOGGER.error("刪除境外开票详情信息异常[{}]", reqDto, e);
			result.setSuccess(false);
			result.setMsg("删除异常，请稍后重试");
		}
		return result;
	}

	/**
	 * invoice收票的提交
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submit(int id) {
		BaseResult bResult = new BaseResult();
		InvoiceCollectOverseas overseas = new InvoiceCollectOverseas();
		overseas.setId(id);
		try {
			bResult = invoiceCollectOverseasService.submit(overseas);
		} catch (BaseException e) {
			LOGGER.error("提交invoice收票异常[{}]", JSONObject.toJSON(overseas), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交invoice收票异常[{}]", JSONObject.toJSON(overseas), e);
			bResult.setMsg("提交invoice收票失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.APPROVE_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult approveInvoiceCollect(InvoiceCollectOverseasReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasService.approveInvoiceCollect(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("invoice收票认证信息异常[{}]", JSONObject.toJSON(reqDto), e);
		} catch (Exception e) {
			LOGGER.error("invoice收票认证信息异常[{}]", reqDto, e);
			result.setSuccess(false);
			result.setMsg("删除异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 导出Invoice收票认证excel
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_COLLECT_OVER, method = RequestMethod.GET)
	public String exportInvoiceCollectApproveExcel(ModelMap model, InvoiceCollectOverseasReqDto searchreqDto) {
		List<InvoiceCollectOverseasResDto> invoiceList = invoiceCollectOverseasService
				.queryInvoiceCollectResultsByEx(searchreqDto).getItems();
		model.addAttribute("invoiceCollectList", invoiceList);
		return "export/invoice/overseas/invoicecollectoverseas_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_COLLECT_OVER_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportInvoiceCollectByCount(InvoiceCollectOverseasReqDto searchreqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = invoiceCollectOverseasService.isOverasyncMaxLine(searchreqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", searchreqDto, e);
		}
		return result;
	}

	/**
	 * 收票确认导入excel
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_INVOICE_COLLECT_OVER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importGoodsExcel(MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasService.importExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}
}
