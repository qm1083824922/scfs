package com.scfs.web.controller.fi;

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
import com.scfs.domain.fi.dto.req.CopeReceiptRelReqDto;
import com.scfs.domain.fi.dto.resp.CopeReceiptRelResDto;
import com.scfs.domain.finance.cope.dto.resq.CopeManageDtlResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.CopeReceiptRelService;

/**
 * <pre>
 * 
 *  File: CopeReceiptRelController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年10月31日			Administrator
 *
 * </pre>
 */
@Controller
public class CopeReceiptRelController {

	private final static Logger LOGGER = LoggerFactory.getLogger(RecReceiptRelController.class);

	@Autowired
	private CopeReceiptRelService copeReceiptRelService;

	/**
	 * 查询应付水单关系表数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_COPE_RECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CopeReceiptRelResDto> queryCopeReceiptRelByCon(CopeReceiptRelReqDto receiptRelReqDto) {
		PageResult<CopeReceiptRelResDto> pageResult = new PageResult<CopeReceiptRelResDto>();
		try {
			pageResult = copeReceiptRelService.queryCopeReceiptRelByCon(receiptRelReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付水单关联异常[{}]", JSONObject.toJSON(receiptRelReqDto), e);
			pageResult.setMsg("查询应付水单关联异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 新增水单和应付的关系
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_COPE_RECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createCopeReceiptRel(@RequestBody CopeReceiptRelReqDto receiptRelReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			copeReceiptRelService.createCopeReceiptRel(receiptRelReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增应付水单关系异常[{}]", JSONObject.toJSON(receiptRelReqDto), e);
			baseResult.setMsg("查询应付水单关系异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 查询应付管理明细的数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_COPE_RECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CopeManageDtlResDto> queryCopeDtlDividByCon(CopeReceiptRelReqDto receiptRelReqDto) {
		PageResult<CopeManageDtlResDto> pageResult = new PageResult<CopeManageDtlResDto>();
		try {
			pageResult = copeReceiptRelService.queryCopeDtlDividByCon(receiptRelReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询应付水单数据异常[{}]", JSONObject.toJSON(receiptRelReqDto), e);
			pageResult.setMsg("查询应应付水单细数据异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 删除应付管理和水单的关系
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_COPE_RECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteCopeRecepitRel(CopeReceiptRelReqDto receiptRelReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			copeReceiptRelService.deleteCopeRecepitRel(receiptRelReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除应付水单数据异常[{}]", JSONObject.toJSON(receiptRelReqDto), e);
			baseResult.setMsg("查询应付水单数据异常，请稍后重试");
		}
		return baseResult;
	}

}
