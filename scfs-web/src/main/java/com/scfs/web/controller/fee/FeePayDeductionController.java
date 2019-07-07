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
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.FeeDeductionService;
import com.scfs.service.fee.impl.FeeServiceImpl;

/**
 * <pre>
 * 	
 *  File: FeePayDeductionController.java
 *  Description:应付抵扣费用
 *  TODO
 *  Date,					Who,				
 *  2017年07月27日				Administrator
 *
 * </pre>
 */
@Controller
public class FeePayDeductionController {

	private final static Logger LOGGER = LoggerFactory.getLogger(FeePayDeductionController.class);

	@Autowired
	private FeeServiceImpl feeService;
	@Autowired
	private FeeDeductionService feeDeductionService;

	/**
	 * 查询应付抵扣费用的列表数据
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeePayDeductionResultByCon(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		try {
			queryFeeReqDto.setFeeType(BaseConsts.FIVE);// 应付抵扣费用
			pageResult = feeService.queryFeeByCond(queryFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询应付抵扣费用数据异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付抵扣费用数据异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg("查询应付抵扣费用数据失败，请重试");
		}
		return pageResult;
	}

	/**
	 * 应付抵扣费用的增加
	 * 
	 * @param fee
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createPayDeductionFee(Fee fee) {
		BaseResult bResult = new BaseResult();
		try {
			// 新增应收抵扣费用
			fee.setFeeType(BaseConsts.FIVE);
			bResult = feeService.addFee(fee);
		} catch (BaseException e) {
			LOGGER.error("插入应付抵扣费用信息异常[{}]", JSONObject.toJSON(fee), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入应付抵扣费用信息异常[{}]", JSONObject.toJSON(fee), e);
			bResult.setMsg("插入失败，请重试");
		}
		return bResult;
	}

	/**
	 * 应付抵扣费用的删除
	 * 
	 * @param fee
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayDeductionFeeById(int id) {
		BaseResult bResult = new BaseResult();
		Fee deleteFeeReqDto = new Fee();
		deleteFeeReqDto.setId(id);
		try {
			bResult = feeService.deleteFeeById(deleteFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("删除应付抵扣费用信息异常[{}]", JSONObject.toJSON(deleteFeeReqDto), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除应付抵扣费用信息异常[{}]", JSONObject.toJSON(deleteFeeReqDto), e);
			bResult.setMsg("删除失败，请重试");
		}
		return bResult;
	}

	/**
	 * 更新应付抵扣费用信息
	 * 
	 * @param recFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayDeductionFeeById(Fee recFeeReqDto) {
		BaseResult bResult = new BaseResult();
		try {
			bResult = feeService.updateFeeById(recFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("更新应付抵扣费用异常[{}]", JSONObject.toJSON(recFeeReqDto), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新应付抵扣费用异常[{}]", JSONObject.toJSON(recFeeReqDto), e);
			bResult.setMsg("更新应付抵扣费用失败，请重试");
		}
		return bResult;
	}

	/**
	 * 浏览应付抵扣费用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryResDto> detailPayDeductionFeeById(Integer id) {
		Result<FeeQueryResDto> result = new Result<FeeQueryResDto>();
		try {
			result = feeService.detailEntityById(id);
		} catch (BaseException e) {
			LOGGER.error("浏览应付抵扣费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览应付抵扣费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("浏览应付抵扣费用失败，请重试");
		}
		return result;
	}

	/**
	 * 浏览应付抵扣费用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryModel> editRecDeductionFeeById(Integer id) {
		Result<FeeQueryModel> result = new Result<FeeQueryModel>();
		try {
			result = feeService.queryEntityById(id);
		} catch (BaseException e) {
			LOGGER.error("编辑应付抵扣费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑应付抵扣费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("编辑应付抵扣失败，请重试");
		}
		return result;
	}

	/**
	 * 应付抵扣费用的提交
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPayDebuctionFeeById(int id) {
		BaseResult bResult = new BaseResult();
		Fee submitFeeReqDto = new Fee();
		submitFeeReqDto.setId(id);
		try {
			bResult = feeService.submitFeeById(submitFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("提交应付抵扣费用异常[{}]", JSONObject.toJSON(submitFeeReqDto), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交应付抵扣费用异常[{}]", JSONObject.toJSON(submitFeeReqDto), e);
			bResult.setMsg("提交应付抵扣费用失败，请重试");
		}
		return bResult;
	}

	/**
	 * 应收抵扣费用导出excel
	 *
	 * @param model
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXCEL_PAY_DEDUCTION_FEE, method = RequestMethod.GET)
	public String exportRecDeductionFeeExcel(ModelMap model, QueryFeeReqDto req) {
		req.setFeeType(BaseConsts.FIVE);
		List<FeeQueryResDto> result = feeService.queryListByCon(req);
		model.addAttribute("feeList", result);
		return "export/fee/payDeductionFee_list";
	}

	/**
	 * 应收抵扣费用后台导出excel
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXCEL_PAY_DEDUCTION_FEE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportRecDeductionFeeCount(QueryFeeReqDto req) {
		BaseResult result = new BaseResult();
		req.setFeeType(BaseConsts.FIVE);
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
	 * 下载应收抵扣excel模板
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_PAY_DEDUCTION_FEE, method = RequestMethod.GET)
	public String downloadFeeRecDeductionTemplate() {
		return "template/fee/feePayDeduction_template";
	}

	/**
	 * 导入应收抵扣excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importFeeRecDeductionExcel(MultipartFile file) {
		BaseResult result = new BaseResult();
		try {
			feeService.importPayDeductionFeeExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/**
	 * 查询采购信息列表
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_TITLE_PAY_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoTitleRespDto> queryPoTitleResultByCon(Integer id) {
		PageResult<PoTitleRespDto> bResult = new PageResult<PoTitleRespDto>();
		try {
			bResult = feeDeductionService.queryPoTitleResultByCon(id);
		} catch (BaseException e) {
			LOGGER.error("查询采购单信息列表异常[{}]", JSONObject.toJSON(id), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询采购单信息列表异常[{}]", JSONObject.toJSON(id), e);
			bResult.setMsg("查询采购单信息列表失败，请重试");
		}
		return bResult;

	}

}
