package com.scfs.web.controller.fi;

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
import com.scfs.domain.fi.dto.req.PrepaidReceiptRelReqDto;
import com.scfs.domain.fi.dto.resp.PrepaidReceiptRelResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.PrepaidReceiptRelService;

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
public class PrepaidReceiptRelController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PrepaidReceiptRelController.class);

	@Autowired
	private PrepaidReceiptRelService prepaidReceiptRelService;

	/**
	 * 查询水单预付关系数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PREPAID_RECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PrepaidReceiptRelResDto> queryPrepaidReceiptRelByCon(PrepaidReceiptRelReqDto receiptRelReqDto) {
		PageResult<PrepaidReceiptRelResDto> pageResult = new PageResult<PrepaidReceiptRelResDto>();
		try {
			pageResult = prepaidReceiptRelService.queryPrepaidReceiptRelByCon(receiptRelReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询预付水单关联异常[{}]", JSONObject.toJSON(receiptRelReqDto), e);
			pageResult.setMsg("查询预付水单关联异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 编辑预付水单明细数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PREPAID_RECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public Result<PrepaidReceiptRelResDto> editPrepaidReceiptByCon(PrepaidReceiptRelReqDto receiptRelReqDto) {
		Result<PrepaidReceiptRelResDto> baseResult = new Result<PrepaidReceiptRelResDto>();
		try {
			baseResult = prepaidReceiptRelService.editPrepaidReceiptByCon(receiptRelReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑应付水单关系异常[{}]", JSONObject.toJSON(receiptRelReqDto), e);
			baseResult.setMsg("编辑应付水单关系异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 保存预付水单的关系数据
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PREPAID_RECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createPrepaidReceiptByCon(PrepaidReceiptRelReqDto receiptRelReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			prepaidReceiptRelService.createPrepaidReceiptByCon(receiptRelReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("保存预付水单数据异常[{}]", JSONObject.toJSON(receiptRelReqDto), e);
			baseResult.setMsg("保存预付水单数据异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 删除应付管理和水单的关系
	 * 
	 * @param receiptRelReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PREPAID_RECEIPTREL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePrepaidRecepitRel(PrepaidReceiptRelReqDto receiptRelReqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			prepaidReceiptRelService.deletePrepaidRecepitRel(receiptRelReqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除预付水单数据异常[{}]", JSONObject.toJSON(receiptRelReqDto), e);
			baseResult.setMsg("删除预付水单数据异常，请稍后重试");
		}
		return baseResult;
	}

}
