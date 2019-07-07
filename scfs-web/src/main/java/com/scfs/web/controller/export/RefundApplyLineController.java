package com.scfs.web.controller.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.export.dto.req.CustomsApplySearchReqDto;
import com.scfs.domain.export.dto.req.RefundApplyLineReqDto;
import com.scfs.domain.export.dto.resp.CustomsApplyResDto;
import com.scfs.domain.export.dto.resp.RefundApplyLineResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.export.RefundApplyLineService;

/**
 * <pre>
 *  出口退税明细
 *  File: RefundApplyLineController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月07日				Administrator
 *
 * </pre>
 */
@Controller
public class RefundApplyLineController {
	private final static Logger LOGGER = LoggerFactory.getLogger(RefundApplyLineController.class);
	@Autowired
	RefundApplyLineService refundApplyLineService;

	/**
	 * 获取报关信息
	 * 
	 * @param customsApply
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_REFUND_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CustomsApplyResDto> queryCustomsApplyResultsByCon(CustomsApplySearchReqDto customsApply) {
		PageResult<CustomsApplyResDto> pageResult = new PageResult<CustomsApplyResDto>();
		try {
			pageResult = refundApplyLineService.queryCustomsApplyResultsByCon(customsApply);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询报关信息失败[{}]", JSONObject.toJSON(customsApply), e);
			pageResult.setMsg("查询报关信息异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 添加退税明细
	 * 
	 * @param refundApplyLineReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_REFUND_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createRefundApplyLine(@RequestBody RefundApplyLineReqDto refundApplyLineReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = refundApplyLineService.createRefundApplyLine(refundApplyLineReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加退税明细异常[{}]", JSONObject.toJSON(refundApplyLineReqDto), e);
			baseResult.setMsg("添加退税明细失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 删除数据
	 * 
	 * @param refundApplyLineReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_ALL_REFUND_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteRefundApplyLine(RefundApplyLineReqDto refundApplyLineReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			baseResult = refundApplyLineService.deleteRefundApplyLineById(refundApplyLineReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除退税明细异常[{}]", JSONObject.toJSON(refundApplyLineReqDto), e);
			baseResult.setMsg("删除退税明细失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 获取列表分页数据
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_REFUND_APPLY_LINE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<RefundApplyLineResDto> queryRefundApplyLineResultsByCon(RefundApplyLineReqDto reqDto) {
		PageResult<RefundApplyLineResDto> pageResult = new PageResult<RefundApplyLineResDto>();
		try {
			pageResult = refundApplyLineService.queryRefundApplyLineResultsByCon(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询退税明细信息失败[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询退税明细信息异常，请稍后重试");
		}
		return pageResult;
	}
}
