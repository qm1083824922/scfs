package com.scfs.web.controller.invoice;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasSearchReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasResDto;
import com.scfs.domain.invoice.entity.InvoiceOverseas;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceOverseasService;
import com.scfs.service.support.FileUploadService;

/**
 * <pre>
 * 	
 *  File: InvoiceOverseasController.java
 *  Description:境外开票业务
 *  TODO
 *  Date,					Who,				
 *  2017年03月30日				Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceOverseasController {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceOverseasController.class);

	@Autowired
	private InvoiceOverseasService invoiceOverseasService;
	@Autowired
	private FileUploadService fileUploadService;

	/**
	 * 新建境外开票信息
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createInvoiceOverseas(InvoiceOverseas invoiceOverseas) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = invoiceOverseasService.createInvoiceOverseas(invoiceOverseas);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建开票信息异常[{}]", JSONObject.toJSON(invoiceOverseas), e);
			br.setSuccess(false);
			br.setMsg("新建开票失败，请重试");
		}
		return br;
	}

	/***
	 * 修改境外开票信息
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_INVOICE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoiceOverseas(InvoiceOverseas invoiceOverseas) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasService.updateInvoiceOverseasById(invoiceOverseas);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改境外开票失败[{}]", JSONObject.toJSON(invoiceOverseas), e);
			result.setMsg("修改境外开票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 编辑境外开票信息
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_INVOICE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceOverseasResDto> editInvoiceOverseas(InvoiceOverseas invoiceOverseas) {
		Result<InvoiceOverseasResDto> result = new Result<InvoiceOverseasResDto>();
		try {
			result = invoiceOverseasService.editInvoiceCollectById(invoiceOverseas);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑境外开票失败[{}]", JSONObject.toJSON(invoiceOverseas), e);
			result.setMsg("编辑境外开票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 浏览境外开票信息
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_INVOICE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceOverseasResDto> detailInvoiceOverseas(InvoiceOverseas invoiceOverseas) {
		Result<InvoiceOverseasResDto> result = new Result<InvoiceOverseasResDto>();
		try {
			result = invoiceOverseasService.editInvoiceCollectById(invoiceOverseas);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览境外开票失败[{}]", JSONObject.toJSON(invoiceOverseas), e);
			result.setMsg("浏览境外开票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 删除开票信息
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_INVOICE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceOverseas(InvoiceOverseas invoiceOverseas) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasService.deleteInvoiceOverseasById(invoiceOverseas);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除境外开票失败[{}]", JSONObject.toJSON(invoiceOverseas), e);
			result.setMsg("删除境外开票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 提交境外开票信息
	 * 
	 * @param invoiceOverseas
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_INVOICE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitInvoiceOverseas(InvoiceOverseas invoiceOverseas) {
		BaseResult result = new BaseResult();
		try {
			result = invoiceOverseasService.sumitInvoiceOverseasById(invoiceOverseas);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交境外开票失败[{}]", JSONObject.toJSON(invoiceOverseas), e);
			result.setMsg("提交境外开票异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 开票境外列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceOverseasResDto> queryInvoiceOverseasResultsByCon(
			InvoiceOverseasSearchReqDto searchreqDto) {
		PageResult<InvoiceOverseasResDto> pageResult = new PageResult<InvoiceOverseasResDto>();
		try {
			pageResult = invoiceOverseasService.queryInvoiceOverseasResultsByCon(searchreqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询境外开票失败[{}]", JSONObject.toJSON(searchreqDto), e);
			pageResult.setMsg("查询境外开票异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 导出开票信息excel
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_OVERSEAS, method = RequestMethod.GET)
	public String exportOverseasExcel(ModelMap model, InvoiceOverseasSearchReqDto searchreqDto) {
		List<InvoiceOverseasResDto> overseasList = invoiceOverseasService.queryInvoiceOverseasResultsByEx(searchreqDto)
				.getItems();
		model.addAttribute("overseasList", overseasList);
		return "export/invoice/overseas/overseas_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_INVOICE_OVERSEAS_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportOverseasByCount(InvoiceOverseasSearchReqDto searchreqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = invoiceOverseasService.isOverasyncMaxLine(searchreqDto);
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
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_INVOICE_OVERSEAS, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_INVOICE_OVERSEAS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceOverseasFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<InvoiceOverseasFileResDto> pageResult = new PageResult<InvoiceOverseasFileResDto>();
		try {
			pageResult = invoiceOverseasService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_INVOICE_OVERSEAS, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_INVOICE_OVERSEAS, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_INVOICE_OVERSEAS, method = RequestMethod.GET)
	public void downLoadFileById(Integer fileId, HttpServletResponse response) {
		try {
			fileUploadService.viewFileById(fileId, response);
		} catch (BaseException e) {
			e.getMsg();
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileId), e);
		}
	}

}
