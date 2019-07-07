package com.scfs.web.controller.logistics;

import java.util.ArrayList;
import java.util.Date;
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
import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreTallyDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.PoOrderReqDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreDtlExtResDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreFileResDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreResDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreTallyDtlExportResDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreTallyDtlExtResDto;
import com.scfs.domain.logistics.dto.resp.PoOrderDtlResDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.logistics.BillInStoreDtlService;
import com.scfs.service.logistics.BillInStoreService;
import com.scfs.service.logistics.BillInStoreTallyDtlService;
import com.scfs.service.support.FileUploadService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年10月17日.
 */
@Controller
public class BillInStoreController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillInStoreController.class);

	@Autowired
	private BillInStoreService billInStoreService;
	@Autowired
	private BillInStoreDtlService billInStoreDtlService;
	@Autowired
	private BillInStoreTallyDtlService billInStoreTallyDtlService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	VoucherService voucherService;

	@RequestMapping(value = BusUrlConsts.ADD_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillInStore> addBillInStore(BillInStore billInStore) {
		Result<BillInStore> result = new Result<BillInStore>();
		try {
			BillInStore billInStoreRes = billInStoreService.addBillInStore(billInStore);
			result.setItems(billInStoreRes);
		} catch (BaseException e) {
			LOGGER.error("新增入库单头信息异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增入库单头信息异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateBillInStore(BillInStore billInStore) {
		BaseResult result = new BaseResult();
		try {
			billInStoreService.updateBillInStore(billInStore);
		} catch (BaseException e) {
			LOGGER.error("修改入库单头信息异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改入库单头信息异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillInStoreResDto> detailBillInStoreById(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		Result<BillInStoreResDto> result = new Result<BillInStoreResDto>();
		try {
			result = billInStoreService.queryBillInStoreById(billInStoreSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询入库单信息异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询入库单信息异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillInStoreResDto> editBillInStoreById(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		Result<BillInStoreResDto> result = new Result<BillInStoreResDto>();
		try {
			result = billInStoreService.queryBillInStoreById(billInStoreSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询入库单信息异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询入库单信息异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillInStoreResDto> queryBillInStore(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		PageResult<BillInStoreResDto> result = new PageResult<BillInStoreResDto>();
		try {
			result = billInStoreService.queryBillInStoreList(billInStoreSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询入库单列表异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询入库单列表异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitBillInStore(BillInStore billInStore) {
		BaseResult result = new BaseResult();
		try {
			billInStoreService.submitBillInStore(billInStore, new Date());
		} catch (BaseException e) {
			LOGGER.error("提交入库单头信息异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交入库单头信息异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteBillInStoreByIds(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billInStoreService.deleteBillInStoreByIds(billInStoreSearchReqDto.getIds());
		} catch (BaseException e) {
			LOGGER.error("批量删除入库单头信息异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除入库单头信息异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_PO_DTL_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoOrderDtlResDto> queryPoOrderDtlList(PoOrderReqDto poOrderReqDto) {
		PageResult<PoOrderDtlResDto> result = new PageResult<PoOrderDtlResDto>();
		try {
			result = billInStoreService.queryPoOrderDtlList(poOrderReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询可入库订单明细列表异常[{}]", JSONObject.toJSON(poOrderReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询可入库订单明细列表异常[{}]", JSONObject.toJSON(poOrderReqDto), e);
			result.setMsg("查询可入库订单明细异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.REJECT_BILL_IN_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult rejectBillInStore(BillInStore billInStore) {
		BaseResult result = new BaseResult();
		try {
			billInStoreService.rejectBillInStore(billInStore);
		} catch (BaseException e) {
			LOGGER.error("驳回入库单异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("驳回入库单异常[{}]", JSONObject.toJSON(billInStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_IN_STORE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBillInStoreCount(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = billInStoreService.isOverBillInStoreMaxLine(billInStoreSearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
		}
		return result;
	}

	/**
	 * 导出入库单
	 * 
	 * @param model
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_IN_STORE, method = RequestMethod.GET)
	public String exportBillInStore(ModelMap model, BillInStoreSearchReqDto billInStoreSearchReqDto) {
		List<BillInStoreResDto> result = billInStoreService.queryAllBillInStoreList(billInStoreSearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billInStoreList", result);
		} else {
			model.addAttribute("billInStoreList", new ArrayList<BillInStoreResDto>());
		}
		return "export/logistics/bill_in_store_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_IN_STORE_DTL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBillInStoreDtlCount(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = billInStoreDtlService.isOverBillInStoreDtlMaxLine(billInStoreSearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
		}
		return result;
	}

	/**
	 * 导出入库单明细
	 * 
	 * @param model
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_IN_STORE_DTL, method = RequestMethod.GET)
	public String exportBillInStoreDtl(ModelMap model, BillInStoreSearchReqDto billInStoreSearchReqDto) {
		List<BillInStoreDtlExtResDto> result = billInStoreDtlService
				.queryAllBillInStoreDtlExtList(billInStoreSearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billInStoreDtlList", result);
		} else {
			model.addAttribute("billInStoreDtlList", new ArrayList<BillInStoreDtlExtResDto>());
		}
		return "export/logistics/bill_in_store_dtl_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_IN_STORE_TALLY_DTL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBillInStoreTallyDtlCount(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = billInStoreTallyDtlService.isOverBillInStoreTallyDtlMaxLine(billInStoreSearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(billInStoreSearchReqDto), e);
		}
		return result;
	}

	/**
	 * 导出入库单理货明细
	 * 
	 * @param model
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_IN_STORE_TALLY_DTL, method = RequestMethod.GET)
	public String exportBillInStoreTallyDtl(ModelMap model, BillInStoreSearchReqDto billInStoreSearchReqDto) {
		List<BillInStoreTallyDtlExtResDto> result = billInStoreTallyDtlService
				.queryAllBillInStoreTallyDtlExtList(billInStoreSearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billInStoreTallyDtlList", result);
		} else {
			model.addAttribute("billInStoreTallyDtlList", new ArrayList<BillInStoreDtlExtResDto>());
		}
		return "export/logistics/bill_in_store_tally_dtl_list";
	}

	/**
	 * 导出收货明细，可供理货明细导入
	 * 
	 * @param model
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_IN_STORE_TALLY_DTL_EXPORT, method = RequestMethod.GET)
	public String exportBillInStoreTallyDtlExport(ModelMap model,
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		List<BillInStoreTallyDtlExportResDto> result = billInStoreDtlService
				.queryAllBillInStoreTallyDtlExportList(billInStoreDtlSearchReqDto).getItems();

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billInStoreTallyDtlExportList", result);
		} else {
			model.addAttribute("billInStoreTallyDtlExportList", new ArrayList<BillInStoreTallyDtlExportResDto>());
		}
		return "export/logistics/bill_in_store_tally_dtl_export";
	}

	/**
	 * 导入入库单理货excel
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_BILL_IN_STORE_TALLY_DTL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importBillInStoreTallyDtlExcel(BillInStoreTallyDtlSearchReqDto billInStoreTallyDtlSearchReqDto,
			MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			billInStoreTallyDtlService.importBillInStoreTallyDtlExcel(billInStoreTallyDtlSearchReqDto, file);
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
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_BILL_IN_STOR, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			fileAttach.setBusType(BaseConsts.THREE);
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_BILL_IN_STOR, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillInStoreFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<BillInStoreFileResDto> pageResult = new PageResult<BillInStoreFileResDto>();
		try {
			fileAttReqDto.setBusType(BaseConsts.THREE);
			pageResult = billInStoreService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_BILL_IN_STOR, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_BILL_IN_STOR, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_BILL_IN_STOR, method = RequestMethod.GET)
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
	 * 入库单凭证信息浏览
	 * 
	 * @param voucher
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_IN_STORE_VOUCHER, method = RequestMethod.POST)
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
