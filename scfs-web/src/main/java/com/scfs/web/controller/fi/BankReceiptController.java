package com.scfs.web.controller.fi;

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
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.resp.BankReceipFileResDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.dto.resp.VoucherDetailResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.support.FileUploadService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: BankReceiptController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月29日			Administrator
 *
 * </pre>
 */
@Controller
public class BankReceiptController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(BankReceiptController.class);
	@Autowired
	BankReceiptService bankReceiptService;
	@Autowired
	VoucherService voucherService;
	@Autowired
	private FileUploadService fileUploadService;

	/**
	 * 创建水单
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createBankReceipt(BankReceipt bankReceipt) {
		Result<Integer> baseResult = new Result<Integer>();
		try {
			int id = bankReceiptService.createBankReceipt(bankReceipt);
			baseResult.setItems(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			baseResult.setMsg("新增水单异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 浏览水单
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public Result<BankReceiptResDto> detailBankReceiptById(BankReceipt bankReceipt) {
		Result<BankReceiptResDto> result = new Result<BankReceiptResDto>();
		try {
			result = bankReceiptService.detailBankReceiptById(bankReceipt.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			result.setMsg("浏览水单异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑水单
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public Result<BankReceiptResDto> editBankReceiptById(BankReceipt bankReceipt) {
		Result<BankReceiptResDto> result = new Result<BankReceiptResDto>();
		try {
			result = bankReceiptService.editBankReceiptById(bankReceipt.getId());
		} catch (Exception e) {
			LOGGER.error("编辑水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			result.setMsg("编辑水单异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新水单
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateBankReceiptById(BankReceipt bankReceipt) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = bankReceiptService.updateById(bankReceipt, false);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			baseResult.setMsg("更新水单异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 核销水单
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.CHECK_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateBankReceiptByCheck(BankReceipt bankReceipt) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = bankReceiptService.updateById(bankReceipt, true);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			baseResult.setMsg("更新水单异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 提交水单
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitBankReceiptById(BankReceipt bankReceipt) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = bankReceiptService.submitBankReceiptById(bankReceipt);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			baseResult.setMsg("提交水单异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 核完水单
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.OVER_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitBankReceiptByState(BankReceipt bankReceipt) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = bankReceiptService.submitBankReceiptByState(bankReceipt);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("核完水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			baseResult.setMsg("核完水单异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 删除水单
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteBankReceiptById(BankReceipt bankReceipt) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = bankReceiptService.deleteBankReceiptById(bankReceipt);
		} catch (Exception e) {
			LOGGER.error("删除水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			baseResult.setMsg("删除水单异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 驳回水单
	 * 
	 * @param bankReceipt
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.REJECT_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult rejectBankReceiptById(BankReceipt bankReceipt) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = bankReceiptService.rejectBankReceiptById(bankReceipt);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("驳回水单失败[{}]", JSONObject.toJSON(bankReceipt), e);
			baseResult.setMsg("驳回水单异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 水单列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BankReceiptResDto> queryBankReceiptResultsByCon(BankReceiptSearchReqDto req) {
		PageResult<BankReceiptResDto> pageResult = new PageResult<BankReceiptResDto>();
		try {
			pageResult = bankReceiptService.queryBankReceiptResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询水单失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询水单异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 水单导出excel
	 * 
	 * @param model
	 * @param bankReceiptReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_BANK_RECEIPT, method = RequestMethod.GET)
	public String exportBankReceiptExcel(ModelMap model, BankReceiptSearchReqDto bankReceiptReqDto) {
		List<BankReceiptResDto> receiptList = bankReceiptService.queryBankReceiptResultsByExcel(bankReceiptReqDto);
		if (!CollectionUtils.isEmpty(receiptList) && receiptList.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("receiptList", receiptList);
		} else {
			model.addAttribute("receiptList", new ArrayList<PayOrderResDto>());
		}
		return "export/pay/receipt_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_BANK_RECEIPT_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportBankReceiptByCount(BankReceiptSearchReqDto bankReceiptReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = bankReceiptService.isOverasyncMaxLine(bankReceiptReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", bankReceiptReqDto, e);
		}
		return result;
	}

	/**
	 * 水单凭证信息
	 * 
	 * @param voucher
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_BANK_RECEIPT_VOUCHER, method = RequestMethod.POST)
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
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_BANK_RECEIPT, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_BANK_RECEIPT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BankReceipFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<BankReceipFileResDto> pageResult = new PageResult<BankReceipFileResDto>();
		try {
			pageResult = bankReceiptService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_BANK_RECEIPT, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_BANK_RECEIPT, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_BANK_RECEIPT, method = RequestMethod.GET)
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
