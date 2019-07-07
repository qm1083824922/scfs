package com.scfs.web.controller.pay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;

import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.pay.dto.req.MergePayOrderRelSearchReqDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderRelResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.pay.MergePayOrderRelService;

/**
 * <pre>
 * 
 *  File: MergePayOrderRelController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */

@Controller
public class MergePayOrderRelController {
	private final static Logger LOGGER = LoggerFactory.getLogger(MergePayOrderRelController.class);

	@Autowired
	MergePayOrderRelService mergePayOrderRelService;

	/**
	 * 浏览付款信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_MERGE_PAY_ORDER_REL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<MergePayOrderRelResDto> detailMergeRelByCon(
			MergePayOrderRelSearchReqDto mergePayOrderRelSearchReqDto) {
		PageResult<MergePayOrderRelResDto> pageResult = new PageResult<MergePayOrderRelResDto>();
		try {
			pageResult = mergePayOrderRelService.queryResultsByMergeId(mergePayOrderRelSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览付款失败[{}]", JSONObject.toJSON(mergePayOrderRelSearchReqDto), e);
			pageResult.setMsg("浏览付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 编辑付款信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_MERGE_PAY_ORDER_REL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<MergePayOrderRelResDto> editMergeRelByCon(
			MergePayOrderRelSearchReqDto mergePayOrderRelSearchReqDto) {
		PageResult<MergePayOrderRelResDto> pageResult = new PageResult<MergePayOrderRelResDto>();
		try {
			pageResult = mergePayOrderRelService.queryResultsByMergeId(mergePayOrderRelSearchReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览付款失败[{}]", JSONObject.toJSON(mergePayOrderRelSearchReqDto), e);
			pageResult.setMsg("浏览付款异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 删除付款信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_MERGE_PAY_ORDER_REL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteMergeRelByCon(MergePayOrderRelSearchReqDto mergePayOrderRelSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			mergePayOrderRelService.deleteMergeRelById(mergePayOrderRelSearchReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览付款失败[{}]", JSONObject.toJSON(mergePayOrderRelSearchReqDto), e);
			result.setMsg("浏览付款异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加付款信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_MERGE_PAY_ORDER_REL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addMergeRel(MergePayOrderRelSearchReqDto req) {
		BaseResult result = new BaseResult();
		try {
			mergePayOrderRelService.createMergePayOrderRel(req);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览付款失败[{}]", JSONObject.toJSON(req), e);
			result.setMsg("浏览付款异常，请稍后重试");
		}
		return result;
	}

}
