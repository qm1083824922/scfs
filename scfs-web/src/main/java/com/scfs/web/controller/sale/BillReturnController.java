package com.scfs.web.controller.sale;

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
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillDeliverySearchReqDto;
import com.scfs.domain.sale.dto.req.BillOutStoreDetailSearchReqDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryDtlExtResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryFileResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;
import com.scfs.domain.sale.dto.resp.BillOutStoreDetailResDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.sale.BillDeliveryDtlService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.FileUploadService;

/**
 * Created by Administrator on 2017年3月22日.
 */
@Controller
public class BillReturnController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillReturnController.class);
	@Autowired
	private BillDeliveryService billDeliveryService;
	@Autowired
	private BillDeliveryDtlService billDeliveryDtlService;
	@Autowired
	private FileUploadService fileUploadService;

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillDeliveryResDto> queryBillDeliveryResultsByCon(
			BillDeliverySearchReqDto billDeliverySearchReqDto) {
		PageResult<BillDeliveryResDto> result = new PageResult<BillDeliveryResDto>();
		try {
			billDeliverySearchReqDto.setBillType(BaseConsts.TWO); // 2-退货
			result = billDeliveryService.queryBillDeliveryResultsByCon(billDeliverySearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询销售退货单列表异常[{}]", JSONObject.toJSON(billDeliverySearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售退货单列表异常[{}]", JSONObject.toJSON(billDeliverySearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.ADD_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillDelivery> addBillDelivery(BillDelivery billDelivery) {
		Result<BillDelivery> result = new Result<BillDelivery>();
		try {
			billDelivery.setBillType(BaseConsts.TWO); // 2-退货
			BillDelivery billDeliveryRes = billDeliveryService.addBillDelivery(billDelivery);
			result.setItems(billDeliveryRes);
		} catch (BaseException e) {
			LOGGER.error("新增销售退货单异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增销售退货单异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateBillDeliveryById(BillDelivery billDelivery) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryService.updateBillDeliveryById(billDelivery);
		} catch (BaseException e) {
			LOGGER.error("更新销售退货单异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新销售退货单异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillDeliveryResDto> detailBillDeliveryById(BillDelivery billDelivery) {
		Result<BillDeliveryResDto> result = new Result<BillDeliveryResDto>();
		try {
			result = billDeliveryService.detailBillDeliveryById(billDelivery);
		} catch (BaseException e) {
			LOGGER.error("查询销售退货单详情异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售退货单详情异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillDeliveryResDto> editBillDeliveryById(BillDelivery billDelivery) {
		Result<BillDeliveryResDto> result = new Result<BillDeliveryResDto>();
		try {
			result = billDeliveryService.detailBillDeliveryById(billDelivery);
		} catch (BaseException e) {
			LOGGER.error("查询销售退货单详情异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售退货单详情异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteBillDeliveryByIds(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryService.deleteBillDeliveryByIds(billDeliverySearchReqDto.getIds());
		} catch (BaseException e) {
			LOGGER.error("批量删除销售退货单异常[{}]", JSONObject.toJSON(billDeliverySearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除销售退货单异常[{}]", JSONObject.toJSON(billDeliverySearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitBillDeliveryById(BillDelivery billDelivery) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryService.submitBillDeliveryById(billDelivery);
		} catch (BaseException e) {
			LOGGER.error("提交销售退货单异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交销售退货单异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.REJECT_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult rejectBillDelivery(BillDelivery billDelivery) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryService.rejectbillDeliveryById(billDelivery);
		} catch (BaseException e) {
			LOGGER.error("驳回销售退货单异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("驳回销售退货单异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_RETURN_DTL_BY_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStoreDetailResDto> queryBillOutStoreDetailResultsByCon(
			BillOutStoreDetailSearchReqDto billOutStoreDetailSearchReqDto) {
		PageResult<BillOutStoreDetailResDto> result = new PageResult<BillOutStoreDetailResDto>();
		try {
			result = billDeliveryService.queryBillOutStoreDetailResultsByCon(billOutStoreDetailSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询可退货明细列表异常[{}]", JSONObject.toJSON(billOutStoreDetailSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询可退货明细列表异常[{}]", JSONObject.toJSON(billOutStoreDetailSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_RETURN_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBillDeliveryCount(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliverySearchReqDto.setBillType(BaseConsts.TWO); // 2-退货
			boolean isOver = billDeliveryService.isOverBillReturnMaxLine(billDeliverySearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(billDeliverySearchReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_RETURN, method = RequestMethod.GET)
	public String exportBillDelivery(ModelMap model, BillDeliverySearchReqDto billDeliverySearchReqDto) {
		billDeliverySearchReqDto.setBillType(BaseConsts.TWO); // 2-退货
		List<BillDeliveryResDto> result = billDeliveryService
				.queryAllBillDeliveryResultsByCon(billDeliverySearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billDeliveryList", result);
		} else {
			model.addAttribute("billDeliveryList", new ArrayList<BillDeliveryResDto>());
		}
		return "export/logistics/bill_return_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_RETURN_DTL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBillDeliveryDtlCount(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billDeliverySearchReqDto.setBillType(BaseConsts.TWO); // 2-退货
			boolean isOver = billDeliveryDtlService.isOverBillReturnDtlMaxLine(billDeliverySearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(billDeliverySearchReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_RETURN_DTL, method = RequestMethod.GET)
	public String exportBillDeliveryDtl(ModelMap model, BillDeliverySearchReqDto billDeliverySearchReqDto) {
		billDeliverySearchReqDto.setBillType(BaseConsts.TWO); // 2-退货
		List<BillDeliveryDtlExtResDto> result = billDeliveryDtlService
				.queryAllBillDeliveryDtlExtResultsByBillDeliveryCon(billDeliverySearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billDeliveryDtlList", result);
		} else {
			model.addAttribute("billDeliveryDtlList", new ArrayList<BillDeliveryDtlExtResDto>());
		}
		return "export/logistics/bill_return_dtl_list";
	}

	/**
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			fileAttach.setBusType(BaseConsts.INT_24);
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillDeliveryFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<BillDeliveryFileResDto> pageResult = new PageResult<BillDeliveryFileResDto>();
		try {
			fileAttReqDto.setBusType(BaseConsts.INT_24);
			pageResult = billDeliveryService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_BILL_RETURN, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_BILL_RETURN, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_BILL_RETURN, method = RequestMethod.GET)
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
	 * 下载销售退货单excel模板(TODO 待定)
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_BILL_RETURN_TEMPLATE, method = RequestMethod.GET)
	public String downloadBillDeliveryTemplate() {
		return "template/sale/billDelivery/billReturn_template";
	}

	/**
	 * 导入销售退货单excel(TODO 待定)
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_BILL_RETURN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importBillDeliveryExcel(MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			billDeliveryService.importBillDeliveryExcel(file, BaseConsts.TWO);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/**
	 * 销售退换单打印查询
	 * 
	 * @param billDelivery
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_RETURN_PRINT, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillDeliveryResDto> printDetailBillReturn(BillDelivery billDelivery) {
		Result<BillDeliveryResDto> result = new Result<BillDeliveryResDto>();
		try {
			result = billDeliveryService.printDetailBillReturn(billDelivery);
		} catch (BaseException e) {
			LOGGER.error("查询销售退货单详情异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售退货单详情异常[{}]", JSONObject.toJSON(billDelivery), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}
}
