package com.scfs.web.controller.invoice;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.scfs.domain.invoice.dto.req.InvoiceCollectOverseasPoReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasPoFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasPoResDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.invoice.InvoiceCollectOverseasPoService;
import com.scfs.service.support.FileUploadService;

/**
 * <pre>
 *  境外收票关联采购单
 *  File: InvoiceCollectOverseasPoController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月24日			Administrator
 *
 * </pre>
 */
@Controller
public class InvoiceCollectOverseasPoController {

	private final static Logger LOGGER = LoggerFactory.getLogger(InvoiceCollectOverseasPoController.class);

	@Autowired
	private InvoiceCollectOverseasPoService invoiceCollectOverseasPoService;
	@Autowired
	private FileUploadService fileUploadService;

	/**
	 * 查询Incoice收票关联采购单的数据列表信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_COLLECT_OVER_PO, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectOverseasPoResDto> queryInvoiceCollectPoResult(
			InvoiceCollectOverseasPoReqDto reqDto) {
		PageResult<InvoiceCollectOverseasPoResDto> pageResult = new PageResult<InvoiceCollectOverseasPoResDto>();
		try {
			pageResult = invoiceCollectOverseasPoService.queryInvoiceCollectPoResult(reqDto);
		} catch (BaseException e) {
			LOGGER.error("查询境外收票采购明细列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg(e.getMsg());
			return pageResult;
		} catch (Exception e) {
			LOGGER.error("查询境外收票采购明细列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询境外收票列表信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 查询Incoice收票关联采购单明细数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_INVOICE_COLLECT_OVER_PO, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoLinesResultByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			result = invoiceCollectOverseasPoService.queryPoLinesResultByCon(poTitleReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询境外收票采购明细信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询境外收票采购明细信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("查询境外收票采购明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加采购单
	 * 
	 * @param feeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_INVOICE_COLLECT_OVER_PO, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createInvoiceCollectOverseasPo(@RequestBody InvoiceCollectOverseasPoReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasPoService.createInvoiceCollectOverseasPo(reqDto);
		} catch (BaseException e) {
			LOGGER.error("添加境外收票采购明细信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加境外收票采购明细信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("添加境外收票采购明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除Incoice收票关联采购单明细数据
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_INVOICE_COLLECT_OVER_PO, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteInvoiceCollectPoByIds(InvoiceCollectOverseasPoReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasPoService.deleteInvoiceCollectPoByIds(reqDto);
		} catch (BaseException e) {
			LOGGER.error("删除境外收票采购明细列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除境外收票采购明细列表信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("删除境外收票列表信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑invoice收票的采购单信息
	 * 
	 * @param invoiceCollectPo
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_INVOICE_COLLECT_OVER_PO, method = RequestMethod.POST)
	@ResponseBody
	public Result<InvoiceCollectOverseasPoResDto> editInvoiceCollectOverseasPoById(
			InvoiceCollectOverseasPoReqDto reqDto) {
		Result<InvoiceCollectOverseasPoResDto> result = new Result<InvoiceCollectOverseasPoResDto>();
		try {
			result = invoiceCollectOverseasPoService.editInvoiceCollectOverseasPoById(reqDto);
		} catch (BaseException e) {
			LOGGER.error("编辑境外收票采购明细信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑境外收票采购明细信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("编辑境外收票采购明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 修改收票采购信息 该操作作废
	 * 
	 * @param poReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_INVOICE_COLLECT_OVER_PO, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateInvoiceOverseasPoById(@RequestBody InvoiceCollectOverseasPoReqDto poReqDto) {
		BaseResult result = new BaseResult();
		try {
			invoiceCollectOverseasPoService.updateInvoiceOverseasPoById(poReqDto);
		} catch (BaseException e) {
			LOGGER.error("修改境外收票采购信息异常[{}]", JSONObject.toJSON(poReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改境外收票采购信息异常[{}]", JSONObject.toJSON(poReqDto), e);
			result.setMsg("修改境外收票采购信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询附件列表
	 * 
	 * @param fileAttReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INVOICE_COLLECT_OVER_FILE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<InvoiceCollectOverseasPoFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<InvoiceCollectOverseasPoFileResDto> pageResult = new PageResult<InvoiceCollectOverseasPoFileResDto>();
		try {
			pageResult = invoiceCollectOverseasPoService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_INVOICE_COLLECT_OVER_FILE, method = RequestMethod.POST)
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
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_INVOICE_COLLECT_OVER_FILE, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			fileAttach.setBusType(BaseConsts.INT_38);
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
	 * 批量下载附件
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_INVOICE_COLLECT_OVER_FILE_LIST, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_INVOICE_COLLECT_OVER_FILE, method = RequestMethod.GET)
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
