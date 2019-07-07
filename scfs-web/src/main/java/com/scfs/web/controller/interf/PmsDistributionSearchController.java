package com.scfs.web.controller.interf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.interf.dto.PmsDistributionSearchReqDto;
import com.scfs.domain.interf.dto.PmsDistributionSearchResDto;
import com.scfs.domain.interf.dto.PmsStoreResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.interf.PmsDistributionSearchService;

/**
 * <pre>
 * 
 *  File: PmsDistributionSearchController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月06日			Administrator
 *
 * </pre>
 */

@RestController
public class PmsDistributionSearchController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsDistributionSearchController.class);

	@Autowired
	PmsDistributionSearchService distributionSearchService;

	/**
	 * 查询当前PMS铺货业务的所有接口数据
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PMS_DISTRIBUTION_SEARCH, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PmsDistributionSearchResDto> queryDistributionResultByCond(PmsDistributionSearchReqDto reqDto) {
		PageResult<PmsDistributionSearchResDto> pageResult = new PageResult<PmsDistributionSearchResDto>();
		try {
			pageResult = distributionSearchService.queryDistributionResultByCon(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询PMS铺货接口查询失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg("查询PMS铺货接口查询失败[{}]，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 浏览当前PMS铺货接口的明细信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_PMS_DISTRIBUTION_SEARCH, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PmsStoreResDto> detailDistributionResultByCon(PmsDistributionSearchReqDto reqDto) {
		PageResult<PmsStoreResDto> pageResult = new PageResult<PmsStoreResDto>();
		try {
			pageResult = distributionSearchService.detailDistributionResultByCon(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询PMS铺货明细查询失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg("查询PMS铺货明细查询失败[{}]，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 重新调用PMS接口
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.RESET_PMS_DISTRIBUTION_SEARCH, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult reinvokeDistributionByCon(PmsDistributionSearchReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		try {
			distributionSearchService.reinvokeDistributionByCon(reqDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("PMS铺货接口调用失败", JSONObject.toJSON(baseResult), e);
			baseResult.setMsg("PMS铺货接口调用失败[{" + e.getMessage() + "}]，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 查询当前PMS铺货接口调用失败的原因
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_FAILURE_PMS_DISTRIBUTION_SEARCH, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> queryDistributionFailureDetail(PmsDistributionSearchReqDto reqDto) {
		Result<String> result = new Result<String>();
		try {
			String msg = distributionSearchService.queryDistributionFailureDetail(reqDto);
			result.setItems(msg);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("添加信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setSuccess(false);
			result.setMsg("添加失败，请重试");
		}
		return result;
	}

	/**
	 * 查询PMS铺货成功的接口数据
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PMS_DISTRIBUTION_SUCCESS_SEARCH, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PmsDistributionSearchResDto> queryDistributionSuccessResultByCon(
			PmsDistributionSearchReqDto reqDto) {
		PageResult<PmsDistributionSearchResDto> pageResult = new PageResult<PmsDistributionSearchResDto>();
		try {
			pageResult = distributionSearchService.queryDistributionSuccessResultByCon(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询PMS铺货接口查询失败[{}]", JSONObject.toJSON(pageResult), e);
			pageResult.setMsg("查询PMS铺货接口查询失败[{}]，请稍后重试");
		}
		return pageResult;
	}
}
