package com.scfs.web.controller.po;

import java.math.BigDecimal;
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
import com.scfs.domain.po.dto.req.PoLineReqDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoFileAttachRespDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.excel.PurchaseOrderDtlExtResDto;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.FileUploadService;

/**
 * Created by Administrator on 2016/10/14.
 */
@Controller
public class PurchaseOrderController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PurchaseOrderController.class);

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private FileUploadService fileUploadService;

	@RequestMapping(value = BusUrlConsts.QUERY_PO_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoTitleRespDto> queryPoTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoTitleRespDto> result = new PageResult<PoTitleRespDto>();
		try {
			poTitleReqDto.setOrderType(0);
			result = purchaseOrderService.queryPoTitlesResultsByCon(poTitleReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询采购单信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg(e.getMsg());
			return result;
		} catch (Exception e) {
			LOGGER.error("查询采购单信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.ADD_PO_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addPurchaseOrderTitle(@Validated PurchaseOrderTitle purchaseOrderTitle,
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
			purchaseOrderTitle.setOrderType(BaseConsts.ZERO);
			Integer id = purchaseOrderService.addPurchaseOrderTitle(purchaseOrderTitle);
			result.setItems(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("新增采购单异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);

		} catch (Exception e) {
			LOGGER.error("新增采购单异常：[{}]", JSONObject.toJSON(purchaseOrderTitle), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_PO_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePurchaseOrderTitle(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
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

	@RequestMapping(value = BusUrlConsts.SUBMIT_PO_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		BaseResult result = new BaseResult();
		try {
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

	@RequestMapping(value = BusUrlConsts.UPDATE_PO_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		BaseResult result = new BaseResult();
		try {
			if (purchaseOrderTitle.getPerRecAmount() == null) {
				purchaseOrderTitle.setPerRecAmount(BigDecimal.ZERO);
			}
			if (purchaseOrderTitle.getCustomerId() == null) {
				purchaseOrderTitle.setCustomerId(-1);
			}
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

	@RequestMapping(value = BusUrlConsts.DETAIL_PO_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> detailPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return purchaseOrderService.queryPurchaseOrderTitleById(purchaseOrderTitle.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("浏览查询采购单信息异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("浏览查询采购单信息异常[{}]", purchaseOrderTitle, e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_PO_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> editPurchaseOrderTitle(Integer id) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return purchaseOrderService.queryPurchaseOrderTitleById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("编辑采购单信息异常,入参：[{}],{},{}", id, e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("编辑采购单信息异常[{}]", id, e);
			result.setMsg("编辑采购异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量新增采购单行信息
	 *
	 * @param poLineReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PO_LINE, method = RequestMethod.POST)
	@ResponseBody
	BaseResult addPoLines(@RequestBody PoLineReqDto poLineReqDto) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.addPoLines(poLineReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("新增采购单详情异常,入参：[{}],{},{}", JSONObject.toJSON(poLineReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("新增采购单详情异常：[{}]", JSONObject.toJSON(poLineReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_PO_LINE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<PoLineModel> queryPoLinesByPoTitleId(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			return purchaseOrderService.queryPoLinesByPoTitleId(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询采购单列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_PO_LINE, method = RequestMethod.POST)
	@ResponseBody
	BaseResult deletePoLinesById(@RequestBody PoLineReqDto poLineReqDto) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.deletePoLinesById(poLineReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("删除采购单列表异常,入参：[{}],{},{}", JSONObject.toJSON(poLineReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("删除采购单列表异常：[{}]", JSONObject.toJSON(poLineReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_PO_LINE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<PoLineModel> editPoLinesByIds(PoLineReqDto poLineReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			List<PoLineModel> poLineModels = purchaseOrderService.editPoLinesByIds(poLineReqDto.getIds());
			result.setItems(poLineModels);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("编辑采购单列表异常,入参：[{}],{},{}", JSONObject.toJSON(poLineReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("编辑采购单列表异常：[{}]", JSONObject.toJSON(poLineReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_PO_LINE, method = RequestMethod.POST)
	@ResponseBody
	BaseResult updatePoLinesById(PurchaseOrderLine purchaseOrderLine) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.updatePoLinesById(purchaseOrderLine);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("更新采购单列表异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderLine), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("更新采购单列表异常：[{}]", JSONObject.toJSON(purchaseOrderLine), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.RECEIVE_PO_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult receivePurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		BaseResult result = new BaseResult();
		try {
			String billNo = purchaseOrderService.receivePurchaseOrderTitle(purchaseOrderTitle);
			result.setSuccessMsg("收货成功，入库单号【" + billNo + "】");
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("采购单收货异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("采购单收货异常：[{}]", JSONObject.toJSON(purchaseOrderTitle), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}
	
	@RequestMapping(value = BusUrlConsts.FLY_ORDER_PO, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult flyOrderPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		BaseResult result = new BaseResult();
		try {
			String billNo = purchaseOrderService.flyOrderPurchaseOrderTitle(purchaseOrderTitle);
			result.setSuccessMsg("飞单成功，入库单号【" + billNo + "】");
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("采购单飞单异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("采购单飞单异常：[{}]", JSONObject.toJSON(purchaseOrderTitle), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_PO, method = RequestMethod.POST)
	public String exportPoExcel(ModelMap model, PoTitleReqDto poTitleReqDto) {
		model.addAttribute("poList", purchaseOrderService.queryPoTitlesResultsByCon(poTitleReqDto).getItems());
		return "export/po/po_list";
	}

	/**
	 * 下载excel模板
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_PO_TEMPLATE, method = RequestMethod.GET)
	public String downloadPoLineTemplate() {
		return "template/po/po_template";
	}

	/**
	 * 下载导入订单明细excel模板
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_PO_LINE_TEMPLATE, method = RequestMethod.GET)
	public String downloadPoTemplate() {
		return "template/po/poLine_template";
	}

	/**
	 * 导入excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_PO, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importPoExcel(MultipartFile file) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.importExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/**
	 * 导入订单明细excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_PO_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importPoLineExcel(MultipartFile file, Integer poId) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderService.importPoLineExcel(file, poId);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
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
	@RequestMapping(value = BusUrlConsts.UPLOAD_PO_FILE, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			fileAttach.setBusType(BaseConsts.TWO);
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
	@RequestMapping(value = BusUrlConsts.QUERY_PO_FILE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoFileAttachRespDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<PoFileAttachRespDto> result = new PageResult<PoFileAttachRespDto>();
		try {
			fileAttReqDto.setBusType(BaseConsts.TWO);
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
	@RequestMapping(value = BusUrlConsts.DELETE_PO_FILE, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_PO_FILE_LIST, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_PO_FILE, method = RequestMethod.GET)
	public void downLoadFileById(Integer fileId, HttpServletResponse response) {
		try {
			fileUploadService.viewFileById(fileId, response);
		} catch (BaseException e) {
			e.getMsg();
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileId), e);
		}
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_ORDER_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPoOrderCount(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = purchaseOrderService.isOverPoOrderMaxLine(poTitleReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_ORDER, method = RequestMethod.GET)
	public String exportPoOrder(ModelMap model, PoTitleReqDto poTitleReqDto) {
		List<PoTitleRespDto> result = purchaseOrderService.queryAllPoTitlesResultsByCon(poTitleReqDto);
		model.addAttribute("poList", result);
		return "export/po/po_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_ORDER_DTL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPoOrderDtlCount(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = purchaseOrderService.isOverPoOrderDtlMaxLine(poTitleReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_ORDER_DTL, method = RequestMethod.GET)
	public String exportPoOrderDtl(ModelMap model, PoTitleReqDto poTitleReqDto) {
		List<PurchaseOrderDtlExtResDto> result = purchaseOrderService.queryAllDtlResultsByCon(poTitleReqDto);
		model.addAttribute("poOrderDtlList", result);
		return "export/po/po_order_dtl_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_LINE_ORDER_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPoLineDtlByIdCount(Integer id) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = purchaseOrderService.isOverasyncPoDtlByTitleIdMaxLine(id);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", id, e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PO_LINE_ORDER, method = RequestMethod.GET)
	public String exportPoLineDtlById(ModelMap model, PoTitleReqDto poTitleReqDto) {
		// 单个采购单明细导出
		List<PurchaseOrderDtlExtResDto> result = purchaseOrderService.queryPoDtlByTitleId(poTitleReqDto.getId());
		model.addAttribute("poOrderDtlList", result);
		return "export/po/po_order_dtl_list";
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_PO_PACK, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> detailPurchaseOrderTitleByCon(PurchaseOrderTitle purchaseOrderTitle) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return purchaseOrderService.queryPurchaseOrderTitleById(purchaseOrderTitle.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("浏览查询采购单信息异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("浏览查询采购单信息异常[{}]", purchaseOrderTitle, e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_PO_PACK_LINE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<PoLineModel> queryPoLinesByPoTitleById(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			return purchaseOrderService.queryPoLinesByPoTitleId(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询采购单列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 采购单打印信息的加载
	 * 
	 * @param purchaseOrderTitle
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_PO_PRINT_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> printDetailPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return purchaseOrderService.queryPurchaseOrderTitle(purchaseOrderTitle.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("浏览查询打印采购单信息异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("浏览查询打印采购单信息异常[{}]", purchaseOrderTitle, e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_PO_PRINT_LINE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoLinesByPoPrintTitleId(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			return purchaseOrderService.queryPoLinesByPoPrintTitleId(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询采购单打印列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单打印列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量打印数据 查询打印信息的头信息
	 * 
	 * @param poTitleReqDto
	 * @return
	 */

	@RequestMapping(value = BusUrlConsts.QUERY_PO_TITLE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> printPoSearchTitleListById(PoTitleReqDto poTitleReqDto) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return purchaseOrderService.queryPoSearchTitleList(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			result.setSuccess(false);
			LOGGER.error("查询采购单打印列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单打印列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setSuccess(false);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量查询采购订单列表打印数据
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PO_LINE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoLinesListByPoPrintTitleId(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			return purchaseOrderService.queryPoLinesListByPoPrintTitleId(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			result.setSuccess(false);
			LOGGER.error("查询采购单明细打印列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单明细打印列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setSuccess(false);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 对账单打印
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PO_BALANCE_OF_ACCOUNT_LIST, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> printPoSearchBalOfAccountById(PoTitleReqDto poTitleReqDto) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return purchaseOrderService.queryPoSearchBalOfAccount(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			result.setSuccess(false);
			LOGGER.error("查询对账单打印列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询对账单打印列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setSuccess(false);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量查询采购对应对账单订单列表打印数据
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PO_BALANCE_OF_ACCOUNT_LINE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoLinesBalOfAccountPrintTitleId(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			return purchaseOrderService.queryPoLinesListByPoPrintTitleId(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			result.setSuccess(false);
			LOGGER.error("查询采购单明细对账单打印列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单明细对账单打印列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setSuccess(false);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 送货单打印
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PO_DELIVER_GOODS_LIST, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> printPoSearchDelGoodsById(PoTitleReqDto poTitleReqDto) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return purchaseOrderService.queryPoSearchDelGoods(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			result.setSuccess(false);
			LOGGER.error("查询送货单打印列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询送货单打印列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setSuccess(false);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量查询采购对应对送货订单列表打印数据
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PO_DELIVER_GOODS_LINE_LIST, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoLinesDeliverGoodesPrintTitleId(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			return purchaseOrderService.queryPoLinesListByPoPrintTitleId(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			result.setSuccess(false);
			LOGGER.error("查询采购单明细送货单打印列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单明细送货单打印列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setSuccess(false);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}
}
