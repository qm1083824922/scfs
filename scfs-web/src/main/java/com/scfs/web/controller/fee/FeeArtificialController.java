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
import com.scfs.domain.fee.dto.req.FeeManageSearchReqDto;
import com.scfs.domain.fee.dto.resp.FeeManageResDto;
import com.scfs.domain.fee.entity.FeeManage;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.FeeManageService;

/**
 * <pre>
 * 	
 *  File: FeeArtificialController.java
 *  Description:人工费用业务
 *  TODO
 *  Date,					Who,				
 *  2017年04月26日				Administrator
 *
 * </pre>
 */
@Controller
public class FeeArtificialController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FeeArtificialController.class);

	@Autowired
	private FeeManageService feeManageService;

	/**
	 * 新建人工费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createFeeArtificial(FeeManage feeManage) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = feeManageService.createFeeManage(feeManage);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建人工费用信息异常[{}]", JSONObject.toJSON(feeManage), e);
			br.setSuccess(false);
			br.setMsg("新建人工费用失败，请重试");
		}
		return br;
	}

	/***
	 * 修改人工费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateFeeArtificial(FeeManage feeManage) {
		BaseResult result = new BaseResult();
		try {
			result = feeManageService.updateFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改人工费用开票失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("修改人工费用开票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 编辑人工费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeManageResDto> editFeeArtificial(FeeManage feeManage) {
		Result<FeeManageResDto> result = new Result<FeeManageResDto>();
		try {
			result = feeManageService.editFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑境外开票失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("编辑境外开票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 浏览人工费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeManageResDto> detailFeeArtificial(FeeManage feeManage) {
		Result<FeeManageResDto> result = new Result<FeeManageResDto>();
		try {
			result = feeManageService.editFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览人工费用失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("浏览人工费用异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 删除人工费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteFeeArtificial(FeeManage feeManage) {
		BaseResult result = new BaseResult();
		try {
			result = feeManageService.deleteFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除人工费用失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("删除人工费用异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 提交人工费用信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitFeeArtificial(FeeManage feeManage) {
		BaseResult result = new BaseResult();
		try {
			result = feeManageService.sumitFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交人工费用失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("提交人工费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量提交人工费用信息
	 * 
	 * @param feeManageSearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_FEE_ARTIFICIAL_ALL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitAllFeeArtificial(FeeManageSearchReqDto feeManageSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			result = feeManageService.sumitFeeManageByIds(feeManageSearchReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量提交人工费用失败[{}]", JSONObject.toJSON(feeManageSearchReqDto), e);
			result.setMsg("批量提交人工费用异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 人工费用列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeManageResDto> queryFeeArtificialResultsByCon(FeeManageSearchReqDto searchreqDto) {
		PageResult<FeeManageResDto> pageResult = new PageResult<FeeManageResDto>();
		try {
			pageResult = feeManageService.queryFeeArtificialResultsByCon(searchreqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询人工费用失败[{}]", JSONObject.toJSON(searchreqDto), e);
			pageResult.setMsg("查询人工费用异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 导出人工费用信息excel
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FEE_ARTIFICIAL, method = RequestMethod.GET)
	public String exportFeeManageExcel(ModelMap model, FeeManageSearchReqDto searchreqDto) {
		searchreqDto.setFeeType(BaseConsts.FIVE);
		List<FeeManageResDto> feeManageList = feeManageService.queryFeeManageResultsByEx(searchreqDto).getItems();
		model.addAttribute("feeManageList", feeManageList);
		return "export/fee/feeManage_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_FEE_ARTIFICIAL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportFeeManageByCount(FeeManageSearchReqDto searchreqDto) {
		BaseResult result = new BaseResult();
		try {
			searchreqDto.setFeeType(BaseConsts.FIVE);
			boolean isOver = feeManageService.isFeeManageMaxLine(searchreqDto);
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
	 * 下载人工费用导入excel模板
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FEE_FILE_ARTIFICIAL, method = RequestMethod.GET)
	public String downloadFeeManageTemplate() {
		return "template/fee/feeManage_template";
	}

	/**
	 * 导入人工费用excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importFeeManageExcel(MultipartFile file) {
		BaseResult result = new BaseResult();
		try {
			feeManageService.importFeeManageExcel(file, BaseConsts.FIVE);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

	/**
	 * 人工费批量手动分摊
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_SHARE_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createFeeArtificialShare(FeeManage feeManage) {
		Result<Integer> br = new Result<Integer>();
		try {
			feeManageService.dealPerShareReport(feeManage);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("人工费批量手动分摊信息异常[{}]", JSONObject.toJSON(feeManage), e);
			br.setSuccess(false);
			br.setMsg("人工费批量手动分摊失败，请重试");
		}
		return br;
	}

	/**
	 * 管理费批量手动分摊
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_MANAMGE_FEE_ARTIFICIAL, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createFeeManageShare(FeeManage feeManage) {
		Result<Integer> br = new Result<Integer>();
		try {
			feeManageService.dealPerShareReport(feeManage);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("管理费用批量手动分摊信息异常[{}]", JSONObject.toJSON(feeManage), e);
			br.setSuccess(false);
			br.setMsg("管理费用批量手动分摊失败，请重试");
		}
		return br;
	}

}
