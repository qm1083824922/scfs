package com.scfs.web.controller.fi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.RecLineResDto;
import com.scfs.domain.fi.entity.RecDetail;
import com.scfs.domain.result.PageResult;
import com.scfs.common.exception.BaseException;
import com.scfs.service.fi.RecLineService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: RecLineController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月4日				Administrator
 *
 * </pre>
 */
@RestController
public class RecLineController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ReceiveController.class);

	@Autowired
	ReceiveService receiveService;

	@Autowired
	RecLineService recLineService;

	@RequestMapping(value = BusUrlConsts.QUERY_REC_LINE, method = RequestMethod.POST)
	public PageResult<RecLineResDto> queryLineResultsByRecId(RecLineSearchReqDto req) {
		PageResult<RecLineResDto> pageResult = new PageResult<RecLineResDto>();
		try {
			pageResult = recLineService.queryResultsByRecId(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("应收明细查询失败[{}]: {}", JSONObject.toJSON(req), e);
			pageResult.setMsg("应收明细查询失败,请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_REC_LINE, method = RequestMethod.POST)
	public PageResult<RecLineResDto> editLineResultsByRecId(RecLineSearchReqDto req) {
		PageResult<RecLineResDto> pageResult = new PageResult<RecLineResDto>();
		try {
			pageResult = recLineService.queryResultsByRecId(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("应收明细编辑失败[{}]: {}", JSONObject.toJSON(req), e);
			pageResult.setMsg("应收明细编辑失败,请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_REC_LINE_BATCH, method = RequestMethod.POST)
	public BaseResult batchDeleteById(@RequestBody BaseReqDto req) {
		BaseResult baseResult = new BaseResult();
		try {
			recLineService.batchDeleteRecLineById(req);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量删除应收明细失败[{}]: {}", JSONObject.toJSON(req), e);
			baseResult.setMsg("批量删除应收明细失败,请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_REC_LINE_BATCH, method = RequestMethod.POST)
	public BaseResult batchUpdateById(@RequestBody RecDetail recDetail) {
		BaseResult baseResult = new BaseResult();
		try {
			recLineService.batchUpdateRecLineById(recDetail);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量修改应收明细失败[{}]: {}", JSONObject.toJSON(recDetail), e);
			baseResult.setMsg("批量修改应收明细失败,请稍后重试");
		}
		return baseResult;
	}
}
