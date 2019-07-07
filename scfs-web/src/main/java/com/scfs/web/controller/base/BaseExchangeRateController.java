package com.scfs.web.controller.base;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseExchangeRateReqDto;
import com.scfs.domain.base.dto.resp.BaseExchangeRateResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BaseExchangeRateController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(BaseExchangeRateController.class);

	@Autowired
	private BaseExchangeRateService baseExchangeRateService;

	/**
	 * 查询汇总汇率列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYEXCHANGERATE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseExchangeRateResDto> queryExchangeRate(BaseExchangeRateReqDto baseExchangeRateReqDto) {
		try {
			return baseExchangeRateService.getBaseExchangeRateList(baseExchangeRateReqDto);
		} catch (Exception e) {
			LOGGER.error("查询汇率信息异常[{}]", JSONObject.toJSON(baseExchangeRateReqDto), e);
			PageResult<BaseExchangeRateResDto> result = new PageResult<BaseExchangeRateResDto>();
			result.setMsg("查询异常，请稍后重试");
			return result;
		}
	}

	/**
	 * 查询历史汇率列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYEXCHANGERATEHIS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseExchangeRateResDto> queryExchangeRateHis(BaseExchangeRateReqDto baseExchangeRateReqDto) {
		try {
			return baseExchangeRateService.getBaseExchangeRateHisList(baseExchangeRateReqDto);
		} catch (Exception e) {
			LOGGER.error("查询汇率信息异常[{}]", JSONObject.toJSON(baseExchangeRateReqDto), e);
			PageResult<BaseExchangeRateResDto> result = new PageResult<BaseExchangeRateResDto>();
			result.setMsg("查询异常，请稍后重试");
			return result;
		}
	}

	/**
	 * 添加汇率
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADDEXCHANGERATE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addExchangeRate(BaseExchangeRateReqDto baseExchangeRateReqDto) {
		try {
			return baseExchangeRateService.addBaseExchangeRate(baseExchangeRateReqDto);
		} catch (Exception e) {
			LOGGER.error("插入汇率信息异常[{}]", JSONObject.toJSON(baseExchangeRateReqDto), e);
			BaseResult result = new BaseResult();
			result.setMsg("插入汇率异常，请稍后重试");
			return result;
		}
	}

	/**
	 * 更新汇率
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.UPDATEEXCHANGERATE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateExchangeRate(BaseExchangeRateReqDto baseExchangeRateReqDto) {
		try {
			return baseExchangeRateService.updateBaseExchangeRate(baseExchangeRateReqDto);
		} catch (Exception e) {
			LOGGER.error("更新汇率信息异常[{}]", JSONObject.toJSON(baseExchangeRateReqDto), e);
			BaseResult result = new BaseResult();
			result.setMsg("更新汇率异常，请稍后重试");
			return result;
		}
	}

	/**
	 * 编辑汇率
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.EDITEXCHANGERATE, method = RequestMethod.POST)
	@ResponseBody
	public Result<BaseExchangeRateResDto> editExchangeRate(int id) {
		try {
			return baseExchangeRateService.queryBaseExchangeRateById(id);
		} catch (Exception e) {
			LOGGER.error("查询汇率信息异常[{}]", id, e);
			Result<BaseExchangeRateResDto> result = new Result<BaseExchangeRateResDto>();
			result.setMsg("查询汇率异常，请稍后重试");
			return result;
		}
	}

}
