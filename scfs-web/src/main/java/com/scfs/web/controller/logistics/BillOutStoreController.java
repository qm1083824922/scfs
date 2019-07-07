package com.scfs.web.controller.logistics;

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
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlExtResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreFileResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStorePickDtlExtResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreResDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.logistics.BillOutStoreDtlService;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.support.FileUploadService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年10月20日.
 */
@Controller
public class BillOutStoreController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillOutStoreController.class);
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private BillOutStoreDtlService billOutStoreDtlService;
	@Autowired
	private BillOutStorePickDtlService billOutStorePickDtlService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	VoucherService voucherService;

	@RequestMapping(value = BusUrlConsts.ADD_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillOutStore> addBillOutStore(BillOutStore billOutStore) {
		Result<BillOutStore> result = new Result<BillOutStore>();
		try {
			BillOutStore billOutStoreRes = billOutStoreService.addBillOutStore(billOutStore);
			result.setItems(billOutStoreRes);
		} catch (BaseException e) {
			LOGGER.error("新增出库单头信息异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增出库单头信息异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.BORROW_ADD_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillOutStore> borrowAddBillOutStore(BillOutStore billOutStore) {
		Result<BillOutStore> result = new Result<BillOutStore>();
		try {
			BillOutStore billOutStoreRes = billOutStoreService.addBillOutStore(billOutStore);
			result.setItems(billOutStoreRes);
		} catch (BaseException e) {
			LOGGER.error("新增出库单头信息异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增出库单头信息异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.RETURN_ADD_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillOutStore> returnAddBillOutStore(BillOutStore billOutStore) {
		Result<BillOutStore> result = new Result<BillOutStore>();
		try {
			BillOutStore billOutStoreRes = billOutStoreService.addBillOutStore(billOutStore);
			result.setItems(billOutStoreRes);
		} catch (BaseException e) {
			LOGGER.error("新增出库单头信息异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增出库单头信息异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateBillOutStore(BillOutStore billOutStore) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreService.updateBillOutStore(billOutStore);
		} catch (BaseException e) {
			LOGGER.error("修改出库单头信息异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改出库单头信息异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillOutStoreResDto> detailBillOutStoreById(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		Result<BillOutStoreResDto> result = new Result<BillOutStoreResDto>();
		try {
			result = billOutStoreService.queryBillOutStoreById(billOutStoreSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询出库单信息异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单信息异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillOutStoreResDto> editBillOutStoreById(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		Result<BillOutStoreResDto> result = new Result<BillOutStoreResDto>();
		try {
			result = billOutStoreService.queryBillOutStoreById(billOutStoreSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询出库单信息异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单信息异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStoreResDto> queryBillOutStore(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		PageResult<BillOutStoreResDto> result = new PageResult<BillOutStoreResDto>();
		try {
			result = billOutStoreService.queryBillOutStoreList(billOutStoreSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询出库单列表异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询出库单列表异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitBillOutStore(BillOutStore billOutStore) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreService.submitBillOutStore(billOutStore);
		} catch (BaseException e) {
			LOGGER.error("提交出库单异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交出库单异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SEND_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sendBillOutStore(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			BillOutStore billOutStore = new BillOutStore();
			billOutStore.setId(billOutStoreSearchReqDto.getId());
			billOutStoreService.sendBillOutStore(billOutStore);
		} catch (BaseException e) {
			LOGGER.error("送货异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("送货异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.BATCH_SEND_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult batchSendBillOutStore(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreService.batchSendBillOutStore(billOutStoreSearchReqDto.getIds());
		} catch (BaseException e) {
			LOGGER.error("批量送货异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量送货异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.REJECT_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult rejectBillOutStore(BillOutStore billOutStore) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreService.rejectBillOutStore(billOutStore);
		} catch (BaseException e) {
			LOGGER.error("驳回出库单异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("驳回出库单异常[{}]", JSONObject.toJSON(billOutStore), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_BILL_OUT_STORE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteBillOutStoreByIds(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			billOutStoreService.deleteBillOutStoreByIds(billOutStoreSearchReqDto.getIds());
		} catch (BaseException e) {
			LOGGER.error("批量删除出库单信息异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除出库单信息异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_OUT_STORE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBillOutStoreCount(ModelMap model, BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = billOutStoreService.isOverBillOutStoreMaxLine(billOutStoreSearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_OUT_STORE, method = RequestMethod.GET)
	public String exportBillOutStore(ModelMap model, BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		List<BillOutStoreResDto> result = billOutStoreService.queryAllBillOutStoreList(billOutStoreSearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billOutStoreList", result);
		} else {
			model.addAttribute("billOutStoreList", new ArrayList<BillOutStoreResDto>());
		}
		return "export/logistics/bill_out_store_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_OUT_STORE_DTL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBillOutStoreDtlCount(ModelMap model, BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = billOutStoreDtlService.isOverBillOutStoreDtlMaxLine(billOutStoreSearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_OUT_STORE_DTL, method = RequestMethod.GET)
	public String exportBillOutStoreDtl(ModelMap model, BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		List<BillOutStoreDtlExtResDto> result = billOutStoreDtlService
				.queryAllBillOutStoreDtlExtList(billOutStoreSearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billOutStoreDtlList", result);
		} else {
			model.addAttribute("billOutStoreDtlList", new ArrayList<BillOutStoreDtlExtResDto>());
		}
		return "export/logistics/bill_out_store_dtl_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_OUT_STORE_PICK_DTL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBillOutStorePickDtlCount(ModelMap model,
			BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = billOutStorePickDtlService.isOverBillOutStorePickDtlMaxLine(billOutStoreSearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BILL_OUT_STORE_PICK_DTL, method = RequestMethod.GET)
	public String exportBillOutStorePickDtl(ModelMap model, BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		List<BillOutStorePickDtlExtResDto> result = billOutStorePickDtlService
				.queryAllBillOutStorePickDtlExtList(billOutStoreSearchReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("billOutStorePickDtlList", result);
		} else {
			model.addAttribute("billOutStorePickDtlList", new ArrayList<BillOutStorePickDtlExtResDto>());
		}
		return "export/logistics/bill_out_store_pick_dtl_list";
	}

	/**
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_BILL_OUT_STOR, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			fileAttach.setBusType(BaseConsts.FOUR);
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_BILL_OUT_STOR, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BillOutStoreFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<BillOutStoreFileResDto> pageResult = new PageResult<BillOutStoreFileResDto>();
		try {
			fileAttReqDto.setBusType(BaseConsts.FOUR);
			pageResult = billOutStoreService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_BILL_OUT_STOR, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_BILL_OUT_STOR, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_BILL_OUT_STOR, method = RequestMethod.GET)
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
	 * 出库单凭证信息浏览
	 * 
	 * @param voucher
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_OUT_STORE_VOUCHER, method = RequestMethod.POST)
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
	 * 出库单打印头信息
	 * 
	 * @param billOutStoreSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_BILL_OUT_STORE_BATCH_PRINT, method = RequestMethod.POST)
	@ResponseBody
	public Result<BillOutStoreResDto> queryBillOutPrint(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		Result<BillOutStoreResDto> result = new Result<BillOutStoreResDto>();
		try {
			return billOutStoreService.queryBillOutPrint(billOutStoreSearchReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			result.setSuccess(false);
			LOGGER.error("查询出库单打印列表异常,入参：[{}],{},{}", JSONObject.toJSON(billOutStoreSearchReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询出库单打印列表异常：[{}]", JSONObject.toJSON(billOutStoreSearchReqDto), e);
			result.setSuccess(false);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

}
