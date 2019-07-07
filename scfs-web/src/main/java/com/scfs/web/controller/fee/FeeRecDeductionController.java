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
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.impl.FeeServiceImpl;

/**
 * <pre>
 * 	
 *  File: DeductionFeeController.java
 *  Description:抵扣费用
 *  TODO
 *  Date,					Who,				
 *  2017年07月27日				Administrator
 *
 * </pre>
 */
@Controller
public class FeeRecDeductionController {

	private final static Logger LOGGER = LoggerFactory.getLogger(FeeRecDeductionController.class);

	@Autowired
	private FeeServiceImpl feeService;

	/**
	 * 查询应收抵扣费用的列表数据
	 * 
	 * @param queryFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_REC_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeeRecDeductionResultByCon(QueryFeeReqDto queryFeeReqDto) {
		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		try {
			queryFeeReqDto.setFeeType(BaseConsts.FOUR);// 应收抵扣费用
			pageResult = feeService.queryFeeByCond(queryFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询应收抵扣费用数据异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应收抵扣费用数据异常[{}]", JSONObject.toJSON(queryFeeReqDto), e);
			pageResult.setMsg("查询应收抵扣费用数据失败，请重试");
		}
		return pageResult;
	}

	/**
	 * 应收抵扣费用的增加
	 * 
	 * @param fee
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_REC_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createRecDeductionFee(Fee fee) {
		BaseResult bResult = new BaseResult();
		// 新增应收抵扣费用
		fee.setFeeType(BaseConsts.FOUR);
		try {
			bResult = feeService.addFee(fee);
		} catch (BaseException e) {
			LOGGER.error("插入应收抵扣费用信息异常[{}]", JSONObject.toJSON(fee), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入应收抵扣费用信息异常[{}]", JSONObject.toJSON(fee), e);
			bResult.setMsg("插入失败，请重试");
		}
		return bResult;
	}

	/**
	 * 应收抵扣费用的删除
	 * 
	 * @param fee
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_REC_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteRecDeductionFeeById(int id) {
		BaseResult bResult = new BaseResult();
		Fee deleteFeeReqDto = new Fee();
		deleteFeeReqDto.setId(id);
		try {
			bResult = feeService.deleteFeeById(deleteFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("删除应收抵扣费用信息异常[{}]", JSONObject.toJSON(deleteFeeReqDto), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除应收抵扣费用信息异常[{}]", JSONObject.toJSON(deleteFeeReqDto), e);
			bResult.setMsg("删除失败，请重试");
		}
		return bResult;
	}

	/**
	 * 更新应收抵扣费用信息
	 * 
	 * @param recFeeReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_REC_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateRecDeductionFeeById(Fee recFeeReqDto) {
		BaseResult bResult = new BaseResult();
		try {
			bResult = feeService.updateFeeById(recFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("更新应收抵扣费用异常[{}]", JSONObject.toJSON(recFeeReqDto), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新应收抵扣费用异常[{}]", JSONObject.toJSON(recFeeReqDto), e);
			bResult.setMsg("更新应收抵扣费用失败，请重试");
		}
		return bResult;
	}

	/**
	 * 浏览应收抵扣费用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_REC_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryResDto> detailRecDeductionFeeById(Integer id) {
		Result<FeeQueryResDto> result = new Result<FeeQueryResDto>();
		try {
			result = feeService.detailEntityById(id);
		} catch (BaseException e) {
			LOGGER.error("浏览应收抵扣费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览应收抵扣费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("浏览应收抵扣费用失败，请重试");
		}
		return result;
	}

	/**
	 * 编辑应收抵扣费用
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_REC_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeQueryModel> editRecDeductionFeeById(Integer id) {
		Result<FeeQueryModel> result = new Result<FeeQueryModel>();
		try {
			result = feeService.queryEntityById(id);
		} catch (BaseException e) {
			LOGGER.error("编辑应收抵扣费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑应收抵扣费用异常[{}]", JSONObject.toJSON(id), e);
			result.setMsg("编辑应收抵扣失败，请重试");
		}
		return result;
	}

	/**
	 * 应收抵扣费用的提交
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_REC_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitRecDebuctionFeeById(int id) {
		BaseResult bResult = new BaseResult();
		Fee submitFeeReqDto = new Fee();
		submitFeeReqDto.setId(id);
		try {
			bResult = feeService.submitFeeById(submitFeeReqDto);
		} catch (BaseException e) {
			LOGGER.error("提交应收抵扣费用异常[{}]", JSONObject.toJSON(submitFeeReqDto), e);
			bResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交应收抵扣费用异常[{}]", JSONObject.toJSON(submitFeeReqDto), e);
			bResult.setMsg("提交应收抵扣费用失败，请重试");
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
	@RequestMapping(value = BusUrlConsts.EXCEL_REC_DEDUCTION_FEE, method = RequestMethod.GET)
	public String exportPayDeductionFeeExcel(ModelMap model, QueryFeeReqDto req) {
		req.setFeeType(BaseConsts.FOUR);
		List<FeeQueryResDto> result = feeService.queryListByCon(req);
		model.addAttribute("feeList", result);
		return "export/fee/recDeductionFee_list";
	}

	/**
	 * 应收抵扣费用后台导出excel
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXCEL_REC_DEDUCTION_FEE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportPayDeductionFeeCount(QueryFeeReqDto req) {
		BaseResult result = new BaseResult();
		req.setFeeType(BaseConsts.FOUR);
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
	@RequestMapping(value = BusUrlConsts.EXPORT_REC_DEDUCTION_FEE, method = RequestMethod.GET)
	public String downloadFeeRecDeductionTemplate() {
		return "template/fee/feeRecDeductiom_template";
	}

	/**
	 * 导入应收抵扣excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_REC_DEDUCTION_FEE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importFeeRecDeductionExcel(MultipartFile file) {
		BaseResult result = new BaseResult();
		try {
			feeService.importRecDeductionFeeExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}
}
