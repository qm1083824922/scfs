package com.scfs.web.controller.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.export.dto.req.RefundApplySearchReqDto;
import com.scfs.domain.export.dto.resp.RefundApplyResDto;
import com.scfs.domain.export.entity.RefundApply;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.export.RefundApplyService;

/**
 * <pre>
 *  出口退税基本信息
 *  File: RefundApplyController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月06日				Administrator
 *
 * </pre>
 */
@Controller
public class RefundApplyController {
	private final static Logger LOGGER = LoggerFactory.getLogger(RefundApplyController.class);
	@Autowired
	private RefundApplyService refundApplyService;

	/**
	 * 新建
	 * 
	 * @param refundApply
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_REFUND_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createRefundApply(RefundApply refundApply) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = refundApplyService.createRefundApply(refundApply);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建信息异常[{}]", JSONObject.toJSON(refundApply), e);
			br.setSuccess(false);
			br.setMsg("新建失败，请重试");
		}
		return br;
	}

	/**
	 * 修改数据
	 * 
	 * @param refundApply
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_REFUND_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateRefundApplyById(RefundApply refundApply) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = refundApplyService.updateRefundApplyById(refundApply);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改信息异常[{}]", JSONObject.toJSON(refundApply), e);
			baseResult.setMsg("修改失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 编辑数据
	 * 
	 * @param refundApply
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_REFUND_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public Result<RefundApplyResDto> editRefundApplyResDto(RefundApply refundApply) {
		Result<RefundApplyResDto> result = new Result<RefundApplyResDto>();
		try {
			result = refundApplyService.editRefundApplyById(refundApply);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑基本信息失败[{}]", JSONObject.toJSON(refundApply), e);
			result.setMsg("编辑基本信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 浏览数据
	 * 
	 * @param refundApply
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_REFUND_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public Result<RefundApplyResDto> detailRefundApplyResDto(RefundApply refundApply) {
		Result<RefundApplyResDto> result = new Result<RefundApplyResDto>();
		try {
			result = refundApplyService.editRefundApplyById(refundApply);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览基本信息失败[{}]", JSONObject.toJSON(refundApply), e);
			result.setMsg("浏览基本信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param refundApply
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_REFUND_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sumitRefundApplyById(RefundApply refundApply) {
		BaseResult result = new BaseResult();
		try {
			result = refundApplyService.sumitRefundApplyById(refundApply);
		} catch (Exception e) {
			LOGGER.error("提交失败[{}]", JSONObject.toJSON(refundApply), e);
			result.setMsg("提交异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除数据
	 * 
	 * @param refundApply
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_REFUND_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteRefundApplyById(RefundApply refundApply) {
		BaseResult result = new BaseResult();
		try {
			result = refundApplyService.deleteRefundApplyById(refundApply);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除失败[{}]", JSONObject.toJSON(refundApply), e);
			result.setMsg("删除异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 列表信息
	 * 
	 * @param applySearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_REFUND_APPLY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<RefundApplyResDto> queryRefundApplyResultsByCon(RefundApplySearchReqDto applySearchReqDto) {
		PageResult<RefundApplyResDto> pageResult = new PageResult<RefundApplyResDto>();
		try {
			pageResult = refundApplyService.queryRefundApplyResultsByCon(applySearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询付款失败[{}]", JSONObject.toJSON(applySearchReqDto), e);
			pageResult.setMsg("查询付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 导出退税信息excel
	 * 
	 * @param model
	 * @param applySearchReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_REFUND_APPLY, method = RequestMethod.GET)
	public String exportCollectApproveExcel(ModelMap model, RefundApplySearchReqDto applySearchReqDto) {
		model.addAttribute("refundList", refundApplyService.queryRefundApplyResultsByExe(applySearchReqDto));
		return "export/apply/refund/refund_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_REFUND_APPLY_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportRefundApplyByCount(RefundApplySearchReqDto applySearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = refundApplyService.isOverasyncMaxLine(applySearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", applySearchReqDto, e);
		}
		return result;
	}
}
