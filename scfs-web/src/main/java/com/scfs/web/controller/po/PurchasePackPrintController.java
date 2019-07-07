package com.scfs.web.controller.po;

import java.util.List;

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
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PurchasePackPrintResDto;
import com.scfs.domain.po.entity.PurchasePackPrint;
import com.scfs.domain.result.PageResult;
import com.scfs.service.po.PurchasePackPrintService;

/**
 * <pre>
 * 
 *  File: PurchasePackPrint.java
 *  Description:采购单装箱打印信息
 *  TODO
 *  Date,					Who,				
 *  2017年12月19日				Administrator
 *
 * </pre>
 */
@Controller
public class PurchasePackPrintController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PurchasePackPrintController.class);
	@Autowired
	private PurchasePackPrintService purchasePackPrintService;

	/**
	 * 添加采购单装箱打印信息
	 * 
	 * @param packPrint
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PO_PACK_PRINT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createPackPrint(@RequestBody List<PurchasePackPrint> packPrintList) {
		BaseResult baseResult = new BaseResult();
		try {
			purchasePackPrintService.createPackPrint(packPrintList);
		} catch (BaseException e) {
			LOGGER.error("采购单装箱打印信息添加失败[{}]", JSONObject.toJSON(packPrintList), e);
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("采购单装箱打印信息添加失败[{}]", JSONObject.toJSON(packPrintList), e);
			baseResult.setMsg("采购单装箱打印信息添加异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 获取合并打印明细信息
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MERGE_PO_PACK_LINE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<PurchasePackPrintResDto> queryPoLinesByPoTitleByIds(PoTitleReqDto poTitleReqDto) {
		PageResult<PurchasePackPrintResDto> result = new PageResult<PurchasePackPrintResDto>();
		try {
			return purchasePackPrintService.queryPoLinesByPoTitleByIds(poTitleReqDto.getIds());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询采购单装箱列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采购单装箱列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 销售单装箱打印信息
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MERGE_SALE_PACK_LINE, method = RequestMethod.POST)
	@ResponseBody
	PageResult<PurchasePackPrintResDto> querySaleLinesByIds(PoTitleReqDto poTitleReqDto) {
		PageResult<PurchasePackPrintResDto> result = new PageResult<PurchasePackPrintResDto>();
		try {
			return purchasePackPrintService.querySaleLinesByIds(poTitleReqDto.getIds());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询销售单装箱列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询采销售装箱列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}
}
