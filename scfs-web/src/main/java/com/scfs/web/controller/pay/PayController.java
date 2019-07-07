package com.scfs.web.controller.pay;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.domain.BaseResult;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fi.dto.resp.VoucherDetailResDto;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.pay.dto.req.PayOrderBatchConfirmReq;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.dto.resq.PayOrderBatchConfirmResp;
import com.scfs.domain.pay.dto.resq.PayOrderFileResDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.report.entity.SaleDtlResult;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.common.exception.BaseException;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.pay.PayService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * <pre>
 *  付款
 *  File: PayController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月08日			Administrator
 *
 * </pre>
 */
@Controller
public class PayController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PayController.class);
	@Autowired
	private PayService payService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	VoucherService voucherService;

	/**
	 * 新建
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayOrder> createPayOrder(PayOrder payOrder) {
		Result<PayOrder> br = new Result<PayOrder>();
		try {
			PayOrder order = payService.createPayOrder(payOrder);
			br.setItems(order);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(payOrder), e);
			br.setSuccess(false);
			br.setMsg("添加失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览付款信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayOrderResDto> detailPayOrderById(PayOrder payOrder) {
		Result<PayOrderResDto> result = new Result<PayOrderResDto>();
		try {
			result = payService.detailPayOrderById(payOrder);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览付款失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setMsg("浏览付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑付款信息
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayOrderResDto> editPayOrderById(PayOrder payOrder) {
		Result<PayOrderResDto> result = new Result<PayOrderResDto>();
		try {
			result = payService.editPayOrderById(payOrder);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑付款失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setMsg("编辑付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新付款信息
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayOrderById(PayOrder payOrder) {
		BaseResult result = new BaseResult();
		try {
			result = payService.updatePayOrderById(payOrder);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新付款失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setMsg("更新付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除付款
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayOrderById(PayOrder payOrder) {
		BaseResult result = new BaseResult();
		try {
			result = payService.deletePayOrderById(payOrder);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除付款失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setMsg("删除付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPayOrderById(PayOrder payOrder, HttpServletRequest request) {
		BaseResult result = new BaseResult();
		try {
			boolean type = false;
			String wechat = request.getParameter("s");
			if (!StringUtils.isEmpty(wechat) && wechat.equals("1")) {
				type = true;
			}
			payService.submitPayOrderById(payOrder, type);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setSuccess(false);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 付款列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayOrderResDto> queryPayOrderResultsByCon(PayOrderSearchReqDto req, HttpServletRequest request) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		try {
			// 获取当前访问的微信渠道标识
			req.setWechatSource(request.getParameter("s"));
			pageResult = payService.queryPayOrderResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 待确认列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_ORDER_WRITE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayOrderResDto> queryPayOrderResults(PayOrderSearchReqDto req) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		try {
			pageResult = payService.queryPayOrderResults(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 待确认列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_PAY_ORDER_ENSURE, method = RequestMethod.GET)
	public String exportPayOrderResults(ModelMap model, PayOrderSearchReqDto req) {
		List<PayOrderResDto> result = payService.queryExportPayOrderResults(req);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("payEnsureList", result);
		} else {
			model.addAttribute("payEnsureList", new ArrayList<SaleDtlResult>());
		}
		return "export/pay/pay_ensure_list";
	}

	/**
	 * 已完成
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.OVER_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPayOver(PayOrder payOrder) {
		BaseResult result = new BaseResult();
		try {
			result = payService.submitPayOver(payOrder);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 开立已完成
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.OVER_PAY_ORDER_OPEN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPayOpen(PayOrder payOrder) {
		BaseResult result = new BaseResult();
		try {
			result = payService.submitPayOver(payOrder);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量确认
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.BATCH_OVER_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult batchConfirmPayOver(@RequestBody PayOrderBatchConfirmReq payOrderBatchConfirmReq) {
		BaseResult result = new BaseResult();
		try {
			payService.batchConfirmPayOver(payOrderBatchConfirmReq);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量确认失败[{}]", JSONObject.toJSON(payOrderBatchConfirmReq), e);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量开立
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.BATCH_OVER_PAY_ORDER_OPEN, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult batchOpenPayOver(@RequestBody PayOrderBatchConfirmReq payOrderBatchConfirmReq) {
		BaseResult result = new BaseResult();
		try {
			payService.batchConfirmPayOver(payOrderBatchConfirmReq);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量确认失败[{}]", JSONObject.toJSON(payOrderBatchConfirmReq), e);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 待开立列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_ORDER_OPEN, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayOrderResDto> queryPayOrderOpen(PayOrderSearchReqDto req) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		try {
			pageResult = payService.queryPayOrderOpen(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 计算实际付款金额
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_DEFAULT_REAL_PAY_AMOUNT, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayOrderResDto> queryDefaultRealPayAmount(
			@RequestBody PayOrderBatchConfirmReq payOrderBatchConfirmReq) {
		Result<PayOrderResDto> result = new Result<PayOrderResDto>();
		try {
			PayOrderResDto payOrderResDto = payService.queryDefaultRealPayAmount(payOrderBatchConfirmReq);
			result.setItems(payOrderResDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("计算默认付款金额失败[{}]", JSONObject.toJSON(payOrderBatchConfirmReq), e);
			result.setMsg("计算默认付款金额异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询批量确认付款单信息
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_BATCH_CONFIRM_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayOrderBatchConfirmResp> queryBatchConfirmPayOrder(
			@RequestBody PayOrderBatchConfirmReq payOrderBatchConfirmReq) {
		Result<PayOrderBatchConfirmResp> result = new Result<PayOrderBatchConfirmResp>();
		try {
			PayOrderBatchConfirmResp payOrderBatchConfirmResp = payService
					.queryBatchConfirmPayOrder(payOrderBatchConfirmReq);
			result.setItems(payOrderBatchConfirmResp);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("计算默认付款金额失败[{}]", JSONObject.toJSON(payOrderBatchConfirmReq), e);
			result.setMsg("计算默认付款金额异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询批量打印付款单信息
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_BATCH_PRINT_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayOrderBatchConfirmResp> queryBatchPrintPayOrder(
			@RequestBody PayOrderBatchConfirmReq payOrderBatchConfirmReq) {
		Result<PayOrderBatchConfirmResp> result = new Result<PayOrderBatchConfirmResp>();
		try {
			PayOrderBatchConfirmResp payOrderBatchConfirmResp = payService
					.queryBatchConfirmPayOrder(payOrderBatchConfirmReq);
			result.setItems(payOrderBatchConfirmResp);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询批量打印付款单失败[{}]", JSONObject.toJSON(payOrderBatchConfirmReq), e);
			result.setMsg("查询批量打印付款单异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量打印付款单预处理信息
	 */
	@RequestMapping(value = BusUrlConsts.PRE_BATCH_PRINT_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> preBatchPrint(@RequestBody PayOrderSearchReqDto payOrderSearchReqDto) {
		Result<String> result = new Result<String>();
		try {
			String unionPrintIdentifier = payService.preBatchPrint(payOrderSearchReqDto);
			result.setItems(unionPrintIdentifier);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量打印预处理失败[{}]", JSONObject.toJSON(payOrderSearchReqDto), e);
			result.setMsg("批量打印预处理失败异常，请稍后重试");
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
	@RequestMapping(value = BusUrlConsts.UPLOAD_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			fileUploadService.uploadFileList(fileList, fileAttach);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("上传文件异常[{}]", JSONObject.toJSON(fileAttach), e);
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayOrderFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<PayOrderFileResDto> pageResult = new PageResult<PayOrderFileResDto>();
		try {
			pageResult = payService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_PAY_ORDER, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_PAY_ORDER, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_PAY_ORDER, method = RequestMethod.GET)
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
	 * 导出付款信息excel
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_PAY_ORDER, method = RequestMethod.GET)
	public String exportPayOrderExcel(ModelMap model, PayOrderSearchReqDto searchreqDto) {
		List<PayOrderResDto> payList = payService.queryPayOrderResultsExcel(searchreqDto);
		if (!CollectionUtils.isEmpty(payList) && payList.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("payList", payList);
		} else {
			model.addAttribute("payList", new ArrayList<PayOrderResDto>());
		}
		return "export/pay/pay_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_PAY_ORDER_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPayOrderByCount(PayOrderSearchReqDto searchreqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = payService.isOverasyncMaxLine(searchreqDto);
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
	 * 付款凭证信息
	 * 
	 * @param voucher
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_PAY_ORDER_VOUCHER, method = RequestMethod.POST)
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
	 * 导入excel
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_PAY_ENSURE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importGoodsExcel(MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			payService.importExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/**
	 * 资金 付款确认 驳回功能
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.REJECT_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult rejectPayOrderById(PayOrder payOrder) {
		BaseResult baseResult = new BaseResult();
		try {
			if (null != payOrder && payOrder.getId() != null) {
				payService.rejectPayOrderById(payOrder);// 业务逻辑操作
			} else {
				baseResult.setMsg("驳回付款异常，请稍后重试！");
			}
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			baseResult.setMsg("驳回付款异常，请稍后重试！");
			LOGGER.error("驳回付款异常，请稍后重试！", e);
		}
		return baseResult;
	}

	/**
	 * 批量提交
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.BATCH_SUBMIT_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitBatchPayOrderById(PayOrder payOrder, HttpServletRequest request) {
		BaseResult result = new BaseResult();
		try {
			boolean type = false;
			String wechat = request.getParameter("s");
			if (!StringUtils.isEmpty(wechat) && wechat.equals("1")) {
				type = true;
			}
			payService.submitBatchPayOrderById(payOrder, type);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量提交失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setSuccess(false);
			result.setMsg("批量提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 刷新数据
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.REFRESH_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult refreshPayOrderById(PayOrderSearchReqDto req) {
		BaseResult result = new BaseResult();
		try {
			payService.refreshFundUsed(req);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("刷新失败[{}]", JSONObject.toJSON(req), e);
			result.setSuccess(false);
			result.setMsg("刷新异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.CONFIG_SUBMIT_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult configSubmitPayOrderById(PayOrder payOrder, HttpServletRequest request) {
		BaseResult result = new BaseResult();
		try {
			boolean type = false;
			String wechat = request.getParameter("s");
			if (!StringUtils.isEmpty(wechat) && wechat.equals("1")) {
				type = true;
			}
			if (!ServiceSupport.isAllowPerm(BusUrlConsts.CONFIG_SUBMIT_PAY_ORDER)) {// 判断用户是否拥有权限
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "用户无权限!");
			}
			payService.submitPayOrderById(payOrder, type);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setSuccess(false);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 供应商确认驳回
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.REJECT_UPDATE_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateRejectById(ProjectItemReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			if (!ServiceSupport.isAllowPerm(BusUrlConsts.REJECT_UPDATE_PAY_ORDER)) {// 判断用户是否拥有权限
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "用户无权限!");
			}
			result = payService.updateReject(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("驳回失败[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("驳回异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 已完成列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_OVER_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayOrderResDto> queryOverPayOrderResults(PayOrderSearchReqDto req) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		try {
			pageResult = payService.queryOverPayOrderResults(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(req), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 更新付款水单日期
	 * 
	 * @param payOrder
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_MEMO_PAY_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateMemoPayOrderById(PayOrder payOrder) {
		BaseResult result = new BaseResult();
		try {
			result = payService.updateMemoPayOrderById(payOrder);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新付款失败[{}]", JSONObject.toJSON(payOrder), e);
			result.setMsg("更新付款异常，请稍后重试");
		}
		return result;
	}
}
