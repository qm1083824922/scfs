
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
import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceApplyFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceApplyManagerResDto;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;
import com.scfs.domain.invoice.entity.InvoiceInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.invoice.InvoiceApplyService;
import com.scfs.service.invoice.InvoiceInfoService;
import com.scfs.service.support.FileUploadService;
import com.scfs.web.controller.BaseController;

@Controller
public class InvoiceApplyController extends BaseController {

	@Autowired
	private InvoiceApplyService invoiceApplyService;

	@Autowired
	private InvoiceInfoService invoiceInfoService;

	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceApplyController.class);

	@Autowired
	private FileUploadService fileUploadService;

	@Autowired
	VoucherService voucherService;

	/**
	 * 分页查询发票信息
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYINVOICEITEM, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceApplyManagerResDto> queryProjectResultsByCon(
			InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		PageResult<InvoiceApplyManagerResDto> pageResult = new PageResult<InvoiceApplyManagerResDto>();
		try {
			pageResult = invoiceApplyService.queryInvoiceResultsByCon(queryInvoiceReqDto);

		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(queryInvoiceReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 新增发票信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADDINVOICESERVICE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createInvoice(InvoiceApplyManager invoiceManager) {
		Result<Integer> br = new Result<Integer>();
		try {
			Integer id = invoiceApplyService.insertInvoice(invoiceManager);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
			br.setSuccess(false);
			br.setMsg("插入失败，请重试");
		}
		return br;
	}

	/**
	 * 编辑单条发票信息
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDITINVOICEITEM, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceApplyManager> editProjectItemById(InvoiceApplyManager invoiceManager) {
		Result<InvoiceApplyManager> result = new Result<InvoiceApplyManager>();
		try {
			InvoiceApplyManager vo = invoiceApplyService.editInvoiceById(invoiceManager);
			result.setItems(vo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 浏览单条发票信息
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAILINVOICEITEM, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceApplyManager> detailProjectItemById(InvoiceApplyManager invoiceManager) {
		Result<InvoiceApplyManager> result = new Result<InvoiceApplyManager>();
		try {
			InvoiceApplyManager vo = invoiceApplyService.detailInvoiceById(invoiceManager);
			result.setItems(vo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新发票信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATEINVOICESERVICE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoice(InvoiceApplyManager invoiceManager) {
		BaseResult br = new BaseResult();
		try {
			invoiceApplyService.updateInvoiceById(invoiceManager);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
			br.setSuccess(false);
			br.setMsg("更新失败，请重试");
		}
		return br;
	}

	/**
	 * 
	 * 删除单条信息
	 * 
	 * @param invoiceManager
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEINVOICESERVICE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoice(InvoiceApplyManager invoiceManager) {
		BaseResult br = new BaseResult();
		try {
			invoiceApplyService.deleteInvoice(invoiceManager);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(invoiceManager), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 开票确认查询
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYINVOICEENSUREITEM, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceApplyManagerResDto> queryInvoiceEnsureByCon(InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		PageResult<InvoiceApplyManagerResDto> pageResult = new PageResult<InvoiceApplyManagerResDto>();
		try {
			pageResult = invoiceInfoService.queryInvoiceByCon(queryInvoiceReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(queryInvoiceReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_ENSURE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportInvoiceEnsureByCount(InvoiceApplyManagerReqDto queryInvoiceReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = invoiceInfoService.isOverasyncInvoiceMaxLine(queryInvoiceReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", queryInvoiceReqDto, e);
		}
		return result;
	}

	/**
	 * 开票确认
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ENSUREINVOICEENSUREITEM, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceApplyManagerResDto> ensureInvoiceEnsure(InvoiceInfo invoiceInfo) {
		Result<InvoiceApplyManagerResDto> pageResult = new Result<InvoiceApplyManagerResDto>();
		try {
			invoiceInfoService.ensureInvoiceEnsure(invoiceInfo, 0);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("开票确认异常[{}]", JSONObject.toJSON(invoiceInfo), e);
			pageResult.setMsg("开票确认异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 下载excel模板
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.INVOICE_TEMPLATE_DOWNLOAD, method = RequestMethod.GET)
	public String downloadGoodsTemplate() {
		return "template/invoice/invoice_ensure_template";
	}

	/**
	 * 开票确认导入excel
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_INVOICE_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importGoodsExcel(MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			invoiceInfoService.importExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/***
	 * 提交开票信息
	 * 
	 * @param invoiceApplyManager
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMITINVOICESERVICE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitInvoiceInfo(InvoiceApplyManager invoiceApplyManager) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceApplyService.submitInvoiceInfo(invoiceApplyManager.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交发票信息失败[{}]", JSONObject.toJSON(invoiceApplyManager), e);
			result.setMsg("提交发票信息异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_EXCEL, method = RequestMethod.GET)
	public String exportInvoiceEnsure(ModelMap model, InvoiceApplyManagerReqDto invoiceApplyManagerReqDto) {
		List<InvoiceApplyManagerResDto> pageResult = invoiceInfoService
				.queryInvoiceByconAndPage(invoiceApplyManagerReqDto);

		if (!CollectionUtils.isEmpty(pageResult) && pageResult.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("invoiceEnsureDtlList", pageResult);
		} else {
			model.addAttribute("invoiceEnsureDtlList", new ArrayList<InvoiceApplyManagerResDto>());
		}
		return "export/invoice/makeinvoice/makeinvoice_ensure_dtl_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_APPLY_EXCEL, method = RequestMethod.GET)
	public String exportInvoiceApply(ModelMap model, InvoiceApplyManagerReqDto invoiceApplyManagerReqDto) {
		List<InvoiceApplyManagerResDto> pageResult = invoiceApplyService
				.queryInvoicePrintByCon(invoiceApplyManagerReqDto);

		if (!CollectionUtils.isEmpty(pageResult) && pageResult.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("invoiceApplyList", pageResult);
		} else {
			model.addAttribute("invoiceApplyList", new ArrayList<InvoiceApplyManagerResDto>());
		}
		return "export/invoice/makeinvoice/makeinvoice_apply_dtl_list";
	}

	/**
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_INVOICE_APPLY, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_INVOICE_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceApplyFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<InvoiceApplyFileResDto> pageResult = new PageResult<InvoiceApplyFileResDto>();
		try {
			pageResult = invoiceApplyService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_INVOICE_APPLY, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_INVOICE_APPLY, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_INVOICE_APPLY, method = RequestMethod.GET)
	public void downLoadFileById(Integer fileId, HttpServletResponse response) {
		try {
			fileUploadService.viewFileById(fileId, response);
		} catch (BaseException e) {
			e.getMsg();
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileId), e);
		}
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_APPLY_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportInvoiceByCount(InvoiceApplyManagerReqDto projectReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = invoiceApplyService.isOverasyncMaxLine(projectReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", projectReqDto, e);
		}
		return result;
	}

	/**
	 * 开票凭证信息
	 * 
	 * @param voucher
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_INVOICE_INFO_VOUCHER, method = RequestMethod.POST)
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
}
