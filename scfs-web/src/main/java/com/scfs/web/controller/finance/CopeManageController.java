package com.scfs.web.controller.finance;

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
import com.scfs.domain.fi.dto.req.CopeReceiptRelReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.finance.cope.dto.req.CopeManageReqDto;
import com.scfs.domain.finance.cope.dto.resq.CopeManageDtlResDto;
import com.scfs.domain.finance.cope.dto.resq.CopeManageResDto;
import com.scfs.domain.finance.cope.entity.CopeManage;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.finance.CopeManageDtlService;
import com.scfs.service.finance.CopeManageMemoService;
import com.scfs.service.finance.CopeManageService;

/**
 * <pre>
 *  应付管理
 *  File: CopeManageController.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年10月31日         Administrator
 *
 * </pre>
 */
@Controller
public class CopeManageController {
	private final static Logger LOGGER = LoggerFactory.getLogger(CopeManageController.class);
	@Autowired
	private CopeManageService copeManageService;
	@Autowired
	private CopeManageDtlService copeManageDtlService;
	@Autowired
	private CopeManageMemoService copeManageMemoService;

	/***
	 * 获取列表数据
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_COPE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CopeManageResDto> queryCopeManageResults(CopeManageReqDto reqDto) {
		PageResult<CopeManageResDto> pageResult = new PageResult<CopeManageResDto>();
		try {
			pageResult = copeManageService.queryCopeManageResults(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付管理信息失败[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询应付管理信息异常，请稍后重试");
		}
		return pageResult;
	}

	/***
	 * 获取详情
	 * 
	 * @param copeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_COPE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<CopeManageResDto> detailCopeManage(CopeManage copeManage) {
		Result<CopeManageResDto> result = new Result<CopeManageResDto>();
		try {
			result = copeManageService.detailCopeManage(copeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览应付管理信息失败[{}]", JSONObject.toJSON(copeManage), e);
			result.setMsg("浏览应付管理信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取明细信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_COPE_MANAGE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CopeManageDtlResDto> queryCopeManageDtlResults(CopeManageReqDto reqDto) {
		PageResult<CopeManageDtlResDto> pageResult = new PageResult<CopeManageDtlResDto>();
		try {
			pageResult = copeManageDtlService.queryCopeManageDtlResults(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付管理明细信息失败[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询应付管理信息明细异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 获取水单明细相关
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_COPE_MANAGE_MEMO, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BankReceiptResDto> queryCopeReceiptRelByCon(CopeReceiptRelReqDto reqDto) {
		PageResult<BankReceiptResDto> pageResult = new PageResult<BankReceiptResDto>();
		try {
			pageResult = copeManageMemoService.queryCopeReceiptRelByCon(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付管理付款水单信息失败[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询应付管理信息付款水单信息异常，请稍后重试");
		}
		return pageResult;
	}
}
