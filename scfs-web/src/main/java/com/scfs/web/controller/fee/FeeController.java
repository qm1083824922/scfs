package com.scfs.web.controller.fee;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeQueryModel;
import com.scfs.domain.fi.dto.resp.VoucherDetailResDto;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.fi.VoucherService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: FeeControoler.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月13日			Administrator
 *
 * </pre>
 */
@Controller
public class FeeController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FeeController.class);
	@Autowired
	FeeServiceImpl feeService;
	@Autowired
	VoucherService voucherService;

	@RequestMapping(value = BusUrlConsts.ADDRECFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addRecFee(Fee fee) {
		BaseResult bResult = new BaseResult();
		fee.setFeeType(BaseConsts.ONE);
		try {
			bResult = feeService.addFee(fee);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(fee), e);
			bResult.setMsg("插入失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.ADDPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addPayFee(Fee fee) {
		BaseResult bResult = new BaseResult();
		fee.setFeeType(BaseConsts.TWO);
		try {
			bResult = feeService.addFee(fee);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(fee), e);
			bResult.setMsg("插入失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.ADDRECPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addRecPayFee(Fee fee) {
		BaseResult bResult = new BaseResult();
		fee.setFeeType(BaseConsts.THREE);
		try {
			bResult = feeService.addFee(fee);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(fee), e);
			bResult.setMsg("插入失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.UPDATERECFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult upadteRecFee(Fee recFeeReqDto) {
		BaseResult bResult = new BaseResult();
		recFeeReqDto.setFeeType(BaseConsts.ONE);
		try {
			bResult = feeService.updateFeeById(recFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新应收费用异常[{}]", JSONObject.toJSON(recFeeReqDto), e);
			bResult.setMsg("更新应收费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.UPDATEPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayFee(Fee payFeeReqDto) {
		BaseResult bResult = new BaseResult();
		payFeeReqDto.setFeeType(BaseConsts.TWO);
		try {
			bResult = feeService.updateFeeById(payFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新应付费用异常[{}]", JSONObject.toJSON(payFeeReqDto), e);
			bResult.setMsg("更新应付费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.UPDATERECPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateRecPayFee(Fee recPayFeeReqDto) {
		BaseResult bResult = new BaseResult();
		recPayFeeReqDto.setFeeType(BaseConsts.THREE);
		try {
			bResult = feeService.updateFeeById(recPayFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新应收应付费用异常[{}]", JSONObject.toJSON(recPayFeeReqDto), e);
			bResult.setMsg("更新应收应付费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.DELETERECFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteRecFee(int id) {
		BaseResult bResult = new BaseResult();
		Fee deleteFeeReqDto = new Fee();
		deleteFeeReqDto.setId(id);
		try {
			bResult = feeService.deleteFeeById(deleteFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除应收费用异常[{}]", JSONObject.toJSON(deleteFeeReqDto), e);
			bResult.setMsg("删除应收费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.DELETEPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayFee(int id) {
		BaseResult bResult = new BaseResult();
		Fee deleteFeeReqDto = new Fee();
		deleteFeeReqDto.setId(id);
		try {
			bResult = feeService.deleteFeeById(deleteFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除应付费用异常[{}]", JSONObject.toJSON(deleteFeeReqDto), e);
			bResult.setMsg("删除应付费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.DELETERECPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteRecPayFee(int id) {
		BaseResult bResult = new BaseResult();
		Fee deleteFeeReqDto = new Fee();
		deleteFeeReqDto.setId(id);
		try {
			bResult = feeService.deleteFeeById(deleteFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除应收应付费用异常[{}]", JSONObject.toJSON(deleteFeeReqDto), e);
			bResult.setMsg("删除应收应付费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.SUBMITRECFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitRecFee(int id) {
		BaseResult bResult = new BaseResult();
		Fee submitFeeReqDto = new Fee();
		submitFeeReqDto.setId(id);
		try {
			bResult = feeService.submitFeeById(submitFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交应收费用异常[{}]", JSONObject.toJSON(submitFeeReqDto), e);
			bResult.setMsg("提交应收费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.SUBMITPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPayFee(int id) {
		BaseResult bResult = new BaseResult();
		Fee submitFeeReqDto = new Fee();
		submitFeeReqDto.setId(id);
		try {
			bResult = feeService.submitFeeById(submitFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交应收费用异常[{}]", JSONObject.toJSON(submitFeeReqDto), e);
			bResult.setMsg("提交应收费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.SUBMITRECPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitRecPayFee(int id) {
		BaseResult bResult = new BaseResult();
		Fee submitFeeReqDto = new Fee();
		submitFeeReqDto.setId(id);
		try {
			bResult = feeService.submitFeeById(submitFeeReqDto);
		} catch (BaseException e) {
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交应收费用异常[{}]", JSONObject.toJSON(submitFeeReqDto), e);
			bResult.setMsg("提交应收应付费用失败，请重试");
		}
		return bResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERYRECFEE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryRecFee(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		queryFeeReqDto.setFeeType(BaseConsts.ONE);
		try {
			pageResult = feeService.queryFeeByCond(queryFeeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应收费用异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg("查询应收费用失败，请重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERYPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryPayFee(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		queryFeeReqDto.setFeeType(BaseConsts.TWO);
		try {
			pageResult = feeService.queryFeeByCond(queryFeeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付费用异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg("查询应付费用失败，请重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.QUERYRECPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryRecPayFee(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		queryFeeReqDto.setFeeType(BaseConsts.THREE);
		try {
			pageResult = feeService.queryFeeByCond(queryFeeReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询费用异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg("查询费用失败，请重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.DETAILRECPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryResDto> detailRecPayFee(Integer id) {
		Result<FeeQueryResDto> result = new Result<FeeQueryResDto>();
		try {
			result = feeService.detailEntityById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("浏览费用失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAILPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryResDto> detailPayFee(Integer id) {
		Result<FeeQueryResDto> result = new Result<FeeQueryResDto>();
		try {
			result = feeService.detailEntityById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("浏览费用失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAILRECFEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryResDto> detailRecFee(Integer id) {
		Result<FeeQueryResDto> result = new Result<FeeQueryResDto>();
		try {
			result = feeService.detailEntityById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("浏览应收应付费用失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDITRECPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryModel> editRecPayFee(Integer id) {
		Result<FeeQueryModel> result = new Result<FeeQueryModel>();
		try {
			result = feeService.queryEntityById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("编辑应收应付费用失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDITPAYFEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryModel> editRecFee(Integer id) {
		Result<FeeQueryModel> result = new Result<FeeQueryModel>();
		try {
			result = feeService.queryEntityById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("编辑应收应付费用失败，请重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDITRECFEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryModel> editPayFee(Integer id) {
		Result<FeeQueryModel> result = new Result<FeeQueryModel>();
		try {
			result = feeService.queryEntityById(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("编辑应收应付费用失败，请重试");
		}
		return result;
	}

	/**
	 * 应收费用导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORTRECFEE, method = RequestMethod.GET)
	public String exportRecFeeExcel(ModelMap model, QueryFeeReqDto req) {
		req.setFeeType(BaseConsts.ONE);
		List<FeeQueryResDto> result = feeService.queryListByCon(req);
		model.addAttribute("feeList", result);
		return "export/fee/recFee_list";
	}

	/**
	 * 应付费用导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORTPAYFEE, method = RequestMethod.GET)
	public String exportPayFeeExcel(ModelMap model, QueryFeeReqDto req) {
		req.setFeeType(BaseConsts.TWO);
		List<FeeQueryResDto> result = feeService.queryListByCon(req);
		model.addAttribute("feeList", result);
		return "export/fee/payFee_list";
	}

	/**
	 * 应收应付费用导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORTRECPAYFEE, method = RequestMethod.GET)
	public String exportRecPayFeeExcel(ModelMap model, QueryFeeReqDto req) {
		req.setFeeType(BaseConsts.THREE);
		List<FeeQueryResDto> result = feeService.queryListByCon(req);
		model.addAttribute("feeList", result);
		return "export/fee/recPayFee_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORTPAYFEECOUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPayFeeCount(QueryFeeReqDto req) {
		BaseResult result = new BaseResult();
		req.setFeeType(BaseConsts.ONE);
		try {
			boolean isOver = feeService.isExcelExportOverMaxLine(req);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(req), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORTRECFEECOUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportRecFeeCount(QueryFeeReqDto req) {
		BaseResult result = new BaseResult();
		req.setFeeType(BaseConsts.TWO);
		try {
			boolean isOver = feeService.isExcelExportOverMaxLine(req);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(req), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORTRECPAYFEECOUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportRecPayFeeCount(QueryFeeReqDto req) {
		BaseResult result = new BaseResult();
		req.setFeeType(BaseConsts.THREE);
		try {
			boolean isOver = feeService.isExcelExportOverMaxLine(req);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(req), e);
		}
		return result;
	}

	/**
	 * 费用凭证信息浏览
	 * 
	 * @param voucher
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_FEE_VOUCHER, method = RequestMethod.POST)
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
	 * 下载应收费用excel模板
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FEE_RECEIVE, method = RequestMethod.GET)
	public String downloadFeeReceiveTemplate() {
		return "template/fee/feeReceive_template";
	}

	/**
	 * 导入应收excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_FEE_RECEIVE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importFeeExcel(MultipartFile file) {
		BaseResult result = new BaseResult();
		try {
			feeService.importRecFeeExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/**
	 * 下载应付费用excel模板
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_PAY_FEE_RECEIVE, method = RequestMethod.GET)
	public String downloadFeePayReceiveTemplate() {
		return "template/fee/feePay_template";
	}

	/**
	 * 导入应付excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_PAY_FEE_RECEIVE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importFeePayExcel(MultipartFile file) {
		BaseResult result = new BaseResult();
		try {
			feeService.importPayFeeExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/**
	 * 下载应收应付费用excel模板
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_REC_PAY_FEE_RECEIVE, method = RequestMethod.GET)
	public String downloadFeeRecPayReceiveTemplate() {
		return "template/fee/feePayReceive_template";
	}

	/**
	 * 导入应收应付excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_REC_PAY_FEE_RECEIVE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importFeeRecPayExcel(MultipartFile file) {
		BaseResult result = new BaseResult();
		try {
			feeService.importRecPayFeeExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}
}
