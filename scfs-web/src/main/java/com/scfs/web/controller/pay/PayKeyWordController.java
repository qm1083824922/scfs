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
import com.scfs.domain.pay.dto.req.PayKeyWordReqDto;
import com.scfs.domain.pay.dto.resq.PayKeyWordResDto;
import com.scfs.domain.pay.entity.PayKeyWord;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.pay.PayKeyWordService;

/**
 * <pre>
 *  关键词
 *  File: PayKeyWordController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月30日			Administrator
 *
 * </pre>
 */
@Controller
public class PayKeyWordController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PayKeyWordController.class);
	@Autowired
	private PayKeyWordService payKeyWordService;

	/**
	 * 新建
	 * 
	 * @param payKeyWord
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_PAY_KEY_WORD, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createPayKeyWord(PayKeyWord payKeyWord) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = payKeyWordService.createPayKeyWord(payKeyWord);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加关键词信息异常[{}]", JSONObject.toJSON(payKeyWord), e);
			br.setSuccess(false);
			br.setMsg("添加关键词信息失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览关键词信息
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_PAY_KEY_WORD, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayKeyWordResDto> detailPayKeyWordById(PayKeyWord payKeyWord) {
		Result<PayKeyWordResDto> result = new Result<PayKeyWordResDto>();
		try {
			result = payKeyWordService.editPayOrderById(payKeyWord);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览关键词信息失败[{}]", JSONObject.toJSON(payKeyWord), e);
			result.setMsg("浏览关键词信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑关键词信息
	 * 
	 * @param PayKeyWord
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_PAY_KEY_WORD, method = RequestMethod.POST)
	@ResponseBody
	public Result<PayKeyWordResDto> editPayKeyWordById(PayKeyWord payKeyWord) {
		Result<PayKeyWordResDto> result = new Result<PayKeyWordResDto>();
		try {
			result = payKeyWordService.editPayOrderById(payKeyWord);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑关键词失败[{}]", JSONObject.toJSON(payKeyWord), e);
			result.setMsg("编辑关键词异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 更新关键词信息
	 * 
	 * @param PayKeyWord
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PAY_KEY_WORD, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePayKeyWordById(PayKeyWord payKeyWord) {
		BaseResult result = new BaseResult();
		try {
			result = payKeyWordService.updatePayKeyWordById(payKeyWord);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新关键词信息失败[{}]", JSONObject.toJSON(payKeyWord), e);
			result.setMsg("更新关键词信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除关键词
	 * 
	 * @param PayKeyWord
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PAY_KEY_WORD, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deletePayKeyWordById(PayKeyWord payKeyWord) {
		BaseResult result = new BaseResult();
		try {
			result = payKeyWordService.deletePayKeyWordById(payKeyWord);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除关键词信息失败[{}]", JSONObject.toJSON(payKeyWord), e);
			result.setMsg("删除关键词信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 付款关键词信息
	 * 
	 * @param PayKeyWord
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PAY_KEY_WORD, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PayKeyWordResDto> queryPayOrderResultsByCon(PayKeyWordReqDto payKey) {
		PageResult<PayKeyWordResDto> pageResult = new PageResult<PayKeyWordResDto>();
		try {
			pageResult = payKeyWordService.queryPayKeyWordResultsByCon(payKey);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询关键词信息失败[{}]", JSONObject.toJSON(payKey), e);
			pageResult.setMsg("查询关键词信息异常，请稍后重试");
		}
		return pageResult;
	}
}
