package com.scfs.web.controller.invoice;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fi.dto.resp.VoucherDetailResDto;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.invoice.dto.req.InvoiceCollectApproveSearchReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceCollectSearchReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectApproveResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectResDto;
import com.scfs.domain.invoice.entity.InvoiceCollect;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.invoice.InvoiceCollectApproveService;
import com.scfs.service.invoice.InvoiceCollectService;
import com.scfs.service.support.FileUploadService;

/**
 * <pre>
 *  收票
 *  File: InvoiceCollectController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日			Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceCollectController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceCollectController.class);

	@Autowired
	private InvoiceCollectService invoiceCollectService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private InvoiceCollectApproveService invoiceCollectApproveService;

	/**
	 * 新建收票信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createInvoiceCollect(InvoiceCollect invoiceCollect) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = invoiceCollectService.createInvoiceCollect(invoiceCollect);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建收票信息异常[{}]", JSONObject.toJSON(invoiceCollect), e);
			br.setSuccess(false);
			br.setMsg("新建收票失败，请重试");
		}
		return br;
	}

	/***
	 * 修改收票信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoiceCollect(InvoiceCollect invoiceCollect) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectService.updateInvoiceCollectById(invoiceCollect);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改收票失败[{}]", JSONObject.toJSON(invoiceCollect), e);
			result.setMsg("修改收票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 编辑收票信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceCollectResDto> editInvoiceCollect(InvoiceCollect invoiceCollect) {
		Result<InvoiceCollectResDto> result = new Result<InvoiceCollectResDto>();
		try {
			result = invoiceCollectService.editInvoiceCollectById(invoiceCollect);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑收票失败[{}]", JSONObject.toJSON(invoiceCollect), e);
			result.setMsg("编辑收票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 浏览收票信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceCollectResDto> detailInvoiceCollect(InvoiceCollect invoiceCollect) {
		Result<InvoiceCollectResDto> result = new Result<InvoiceCollectResDto>();
		try {
			result = invoiceCollectService.editInvoiceCollectById(invoiceCollect);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览收票失败[{}]", JSONObject.toJSON(invoiceCollect), e);
			result.setMsg("浏览收票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 删除收票信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceCollect(InvoiceCollect invoiceCollect) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectService.deleteInvoiceCollectById(invoiceCollect);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除收票失败[{}]", JSONObject.toJSON(invoiceCollect), e);
			result.setMsg("删除收票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 提交收票信息
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitInvoiceCollect(InvoiceCollect invoiceCollect) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectService.sumitInvoiceCollectById(invoiceCollect);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交收票失败[{}]", JSONObject.toJSON(invoiceCollect), e);
			result.setMsg("提交收票异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 收票列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectResDto> queryInvoiceCollectResultsByCon(InvoiceCollectSearchReqDto searchreqDto) {
		PageResult<InvoiceCollectResDto> pageResult = new PageResult<InvoiceCollectResDto>();
		try {
			pageResult = invoiceCollectService.queryInvoiceCollectResultsByCon(searchreqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询收票失败[{}]", JSONObject.toJSON(searchreqDto), e);
			pageResult.setMsg("查询收票异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 收票认证列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_COLLECT_APPROVE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectResDto> queryInvoiceCollectApproved(InvoiceCollectSearchReqDto searchreqDto) {
		PageResult<InvoiceCollectResDto> pageResult = new PageResult<InvoiceCollectResDto>();
		try {
			pageResult = invoiceCollectService.queryInvoiceCollectApproved(searchreqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询收票认证失败[{}]", JSONObject.toJSON(searchreqDto), e);
			pageResult.setMsg("查询收票认证异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 导出收票信息excel
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_COLLECT, method = RequestMethod.GET)
	public String exportCollectExcel(ModelMap model, InvoiceCollectSearchReqDto searchreqDto) {
		List<InvoiceCollectResDto> invoiceList = invoiceCollectService.queryInvoiceCollectResultsByEx(searchreqDto)
				.getItems();
		if (!CollectionUtils.isEmpty(invoiceList) && invoiceList.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("collectList", invoiceList);
		} else {
			model.addAttribute("collectList", new ArrayList<PayOrderResDto>());
		}
		return "export/invoice/collect/collect_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_COLLECT_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportCollectByCount(InvoiceCollectSearchReqDto searchreqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = invoiceCollectService.isOverasyncMaxLine(searchreqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", searchreqDto, e);
		}
		return result;
	}

	/***
	 * 收票认证
	 * 
	 * @param invoiceCollect
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_INVOICE_COLLECT_APPROVE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sumitInvoiceCollectApproved(InvoiceCollect invoiceCollect) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceCollectService.sumitInvoiceCollectApproved(invoiceCollect);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交收票失败[{}]", JSONObject.toJSON(invoiceCollect), e);
			result.setMsg("提交收票异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 导出收票认证excel
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_COLLECT_APPROVE, method = RequestMethod.GET)
	public String exportCollectApproveExcel(ModelMap model, InvoiceCollectSearchReqDto searchreqDto) {
		model.addAttribute("approveList", invoiceCollectService.queryInvoiceCollectApproved(searchreqDto).getItems());
		return "export/invoice/collect/collectApprove_list";
	}

	/**
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			fileUploadService.uploadFileList(fileList, fileAttach);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("上传文件异常[{}]", JSONObject.toJSON(request), e);
			result.setSuccess(false);
			result.setMsg("上传失败，请重试");
		}
		return result;
	}

	/**
	 * 查询附件列表
	 * 
	 * @param fileAttReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<InvoiceCollectFileResDto> pageResult = new PageResult<InvoiceCollectFileResDto>();
		try {
			pageResult = invoiceCollectService.queryFileList(fileAttReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 删除附件
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteFileById(Integer fileId) {
		BaseResult br = new BaseResult();
		try {
			fileUploadService.deleteFileById(fileId);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(fileId), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 批量下载附件
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_INVOICE_COLLECT, method = RequestMethod.GET)
	public void downLoadFileListById(FileAttachSearchReqDto fileAttReqDto, HttpServletResponse response) {
		try {
			fileUploadService.downLoadFileList(fileAttReqDto, response);
		} catch (BaseException e) {
			e.getMsg();
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
		}
	}

	/**
	 * 下载附件
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_INVOICE_COLLECT, method = RequestMethod.GET)
	public void downLoadFileById(Integer fileId, HttpServletResponse response) {
		try {
			fileUploadService.viewFileById(fileId, response);
		} catch (BaseException e) {
			e.getMsg();
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileId), e);
		}
	}

	/**
	 * 收票凭证信息
	 * 
	 * @param voucher
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_INVOICE_COLLECT_VOUCHER, method = RequestMethod.POST)
	@ResponseBody
	public Result<VoucherDetailResDto> detailPayVoucherResultsByParam(Voucher voucher) {
		Result<VoucherDetailResDto> result = new Result<VoucherDetailResDto>();
		try {
			VoucherDetailResDto res = voucherService.editVoucherDetailByParam(voucher);
			result.setItems(res);
		} catch (Exception e) {
			result.setSuccess(false);
			LOGGER.error("查询凭证失败[{}]", JSONObject.toJSON(voucher), e);
			result.setMsg("无凭证信息");
		}
		return result;
	}

	/**
	 * 下载excel模板
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.INVOICE_COLLECT_TEMPLATE_DOWNLOAD, method = RequestMethod.GET)
	public String downloadGoodsTemplate() {
		return "template/collect/collect_approve_template";
	}

	/**
	 * 收票确认导入excel
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_INVOICE_COLLECT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importGoodsExcel(MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectService.importExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/**
	 * 查询收票认证明细
	 * 
	 * @param invoiceCollectApproveSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_COLLECT_APPROVE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectApproveResDto> queryInvoiceCollectApproveResultsByCon(
			InvoiceCollectApproveSearchReqDto invoiceCollectApproveSearchReqDto) {
		PageResult<InvoiceCollectApproveResDto> pageResult = new PageResult<InvoiceCollectApproveResDto>();
		try {
			pageResult = invoiceCollectApproveService
					.queryInvoiceCollectApproveResultsByCon(invoiceCollectApproveSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询收票认证明细失败[{}]", JSONObject.toJSON(invoiceCollectApproveSearchReqDto), e);
			pageResult.setMsg("查询收票认证明细异常，请稍后重试");
		}
		return pageResult;
	}

}
