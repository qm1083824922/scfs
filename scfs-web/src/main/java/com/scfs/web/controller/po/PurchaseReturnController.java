package com.scfs.web.controller.po;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlResDto;
import com.scfs.domain.po.dto.req.PoLineReqDto;
import com.scfs.domain.po.dto.req.PoReturnListReqDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoFileAttachRespDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.entity.PurchaseReturnDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.logistics.StlService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.FileUploadService;

@Controller
public class PurchaseReturnController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PurchaseReturnController.class);
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private StlService stlService;
	@Autowired
	private FileUploadService fileUploadService;

	/** @return采购退货单查询 @param poTitleReqDto */
	@RequestMapping(value = BusUrlConsts.QUERY_PO_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoTitleRespDto> queryPoReturnTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoTitleRespDto> result = new PageResult<PoTitleRespDto>();
		poTitleReqDto.setOrderType(1);
		try {
			result = purchaseOrderService.queryPoTitlesResultsByCon(poTitleReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询采购退货单信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg(e.getMsg());
			return result;
		} catch (Exception e) {
			LOGGER.error("查询采购退货单信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/** 添加一条采购退货单信息 */
	@RequestMapping(value = BusUrlConsts.ADD_PO_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addPurchaseReturnOrderTitle(@Validated PurchaseOrderTitle purchaseOrderTitle,
			BindingResult bindingResult) {
		Result<Integer> result = new Result<Integer>();
		try {
			if (bindingResult.hasErrors()) {
				List<ObjectError> errors = bindingResult.getAllErrors();
				List<String> msgs = Lists.newArrayList();
				for (ObjectError error : errors) {
					msgs.add(error.getDefaultMessage());
				}
				result.setMsg(JSONObject.toJSONString(msgs));
				return result;
			}
			purchaseOrderTitle.setOrderType(BaseConsts.ONE);
			Integer id = purchaseOrderService.addPurchaseOrderTitle(purchaseOrderTitle);
			result.setItems(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("新增采购退货单异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);

		} catch (Exception e) {
			LOGGER.error("新增采购退货单异常：[{}]", JSONObject.toJSON(purchaseOrderTitle), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_PO_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> editPurchaseOrderTitle(Integer id) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return purchaseOrderService.queryPurchaseOrderTitleById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("编辑采购退货单信息异常,入参：[{}],{},{}", id, e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("编辑采购退货单信息异常[{}]", id, e);
			result.setMsg("编辑采购异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询退货单对应的退货明细
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_BILL_IN_STORE_LINE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<PurchaseReturnDtl> queryBillInStoreByPoReturnTitle(PoTitleReqDto poTitleReqDto) {
		PageResult<PurchaseReturnDtl> result = new PageResult<PurchaseReturnDtl>();
		try {
			return purchaseOrderService.queryBillInStoreLineByPoTitle(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询采购单列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/** 查询退货单可以选择的入库单信息 */
	@RequestMapping(value = BusUrlConsts.QUERY_BILL_IN_STORE_LINE_UNDIVIDE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<StlResDto> queryBillInStoreUndivide(StlSearchReqDto stlSearchReqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		try {
			// stlSearchReqDto.setQuerySource(BaseConsts.FIVE);
			result = stlService.queryAvailableStlResultsByCon(stlSearchReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询采购单列表异常,入参：[{}],{},{}", JSONObject.toJSON(stlSearchReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单列表异常：[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/** 添加一条采购退货单明细信息 */
	@RequestMapping(value = BusUrlConsts.ADD_PO_RETURN_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addPoReturnDtlInfo(@RequestBody PoReturnListReqDto poReturnListReqDto) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.addPoReturnDtlInfo(poReturnListReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询采购退货单列表异常,采购单：[{}],{},{}", JSONObject.toJSON(poReturnListReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购退货单列表异常：[{}]", JSONObject.toJSON(poReturnListReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_PO_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePurchaseReturnTitle(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		poTitleReqDto.setOrderType(BaseConsts.ONE);
		try {
			purchaseOrderService.deletePurchaseOrderTitle(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("删除采购单异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("删除采购单异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_PO_LINE_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePoReturnLinesById(@RequestBody PoLineReqDto poLineReqDto) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.deletePoReturnLinesById(poLineReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("删除采购退货单单列表异常,入参：[{}],{},{}", JSONObject.toJSON(poLineReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("删除采购退货单列表异常：[{}]", JSONObject.toJSON(poLineReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_PO_LINE_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<PurchaseReturnDtl> editPurchaseReturnTitle(Integer id) {
		Result<PurchaseReturnDtl> result = new Result<PurchaseReturnDtl>();
		try {
			return purchaseOrderService.queryPurchaseReturnById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("编辑采购单信息异常,入参：[{}],{},{}", id, e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("编辑采购单信息异常[{}]", id, e);
			result.setMsg("编辑采购异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_PO_LINE_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePurchaseReturnLine(PurchaseReturnDtl purchaseReturnDtl) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.updatePurchaseReturnLine(purchaseReturnDtl);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("修改采购单异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseReturnDtl), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("修改采购单异常：[{}]", JSONObject.toJSON(purchaseReturnDtl), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_PO_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPurchaseReturnLine(PurchaseOrderTitle purchaseOrderTitle) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderTitle.setOrderType(1);
			;
			purchaseOrderService.submitPurchaseOrderTitle(purchaseOrderTitle);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("提交采购单异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("提交采购单异常：[{}]", JSONObject.toJSON(purchaseOrderTitle), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_RETURN_TITLE, method = RequestMethod.GET)
	public String exportPoReturnTitle(ModelMap model, PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.ONE);
		List<PoTitleRespDto> result = purchaseOrderService.queryAllPoReturnTitlesResultsByCon(poTitleReqDto);
		model.addAttribute("poReturnList", result);
		return "export/po/po_return_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_RETURN_LINE, method = RequestMethod.GET)
	public String exportPoReturnLine(ModelMap model, PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.ONE);
		List<PurchaseReturnDtl> result = purchaseOrderService.queryAllPoReturnLineResultsByCon(poTitleReqDto);
		model.addAttribute("poReturnLineList", result);
		return "export/po/po_return_dtl_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_RETURN_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPoReturnCount(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = purchaseOrderService.isOverPoReturnMaxLine(poTitleReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_RETURN_LINE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPoReturnLineCount(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = purchaseOrderService.isOverasyncPoReturnDtlByTitleIdMaxLine(poTitleReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_PO_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePurchaseReturnTitle(PurchaseOrderTitle purchaseOrderTitle) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.updatePurchaseOrderTitle(purchaseOrderTitle);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("修改采购单异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("修改采购单异常：[{}]", JSONObject.toJSON(purchaseOrderTitle), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 上传附件
	 *
	 * @param request
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_PO_RETURN_FILE, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			fileAttach.setBusType(BaseConsts.INT_27);
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
	@RequestMapping(value = BusUrlConsts.QUERY_PO_RETURN_FILE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoFileAttachRespDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<PoFileAttachRespDto> result = new PageResult<PoFileAttachRespDto>();
		try {
			// 业务类型
			fileAttReqDto.setBusType(BaseConsts.INT_27);
			result = purchaseOrderService.queryFileList(fileAttReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除附件
	 *
	 * @param fileId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PO_RETURN_FILE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteFileById(Integer fileId) {
		BaseResult br = new BaseResult();
		try {
			fileUploadService.deleteFileById(fileId);
		} catch (BaseException e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(fileId), e);
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(fileId), e);
			br.setSuccess(false);
		}
		return br;
	}

	/**
	 * 批量下载附件
	 *
	 * @param fileAttReqDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_PO_RETURN_FILE_LIST, method = RequestMethod.GET)
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
	 * @param fileId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_PO_RETURN_FILE, method = RequestMethod.GET)
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
