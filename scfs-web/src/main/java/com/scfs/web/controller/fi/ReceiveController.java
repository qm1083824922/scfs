package com.scfs.web.controller.fi;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.fi.entity.RecDetail;
import com.scfs.domain.report.entity.SaleDtlResult;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.RecLineService;
import com.scfs.service.fi.ReceiveService;

/**
 * <pre>
 * 
 *  File: AccountCheckController.java
 *  Description:
 *  TODO                    
 *  Date,					Who,				
 *  2016年10月29日			Administrator
 *
 * </pre>
 */
@Controller
public class ReceiveController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ReceiveController.class);

	@Autowired
	ReceiveService receiveService;

	@Autowired
	RecLineService recLineService;

	/**
	 * 
	 * TODO. 对账操作，生成应收明细表记录，以及应收表记录
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_REC_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult createRecDetail(@RequestBody RecDetail recDetail) {
		BaseResult baseResult = new BaseResult();
		try {
			receiveService.createRecDetail(recDetail);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("对账失败[{}]: {}", JSONObject.toJSON(recDetail), e);
			baseResult.setMsg("对账失败,请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 
	 * TODO. 合并操作
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.MERGE_REC_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult mergeRecDetail(@RequestBody RecDetail recDetail) {
		BaseResult baseResult = new BaseResult();
		try {
			receiveService.mergeRec(recDetail);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("合并应收失败[{}]: {}", JSONObject.toJSON(recDetail), e);
			baseResult.setMsg("合并应收失败,请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 
	 * TODO. 查询操作
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_RECEIVE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiveResDto> queryReceiveResultsByCon(ReceiveSearchReqDto req) {
		PageResult<ReceiveResDto> pageResult = new PageResult<ReceiveResDto>();
		try {
			pageResult = receiveService.queryResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("应收查询失败[{}]: {}", JSONObject.toJSON(req), e);
			pageResult.setMsg("应收查询失败,请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 
	 * TODO. 查询未核完应收
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_UNFINISHED_RECEIVE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiveResDto> queryUnFinishedReceiveByCon(ReceiveSearchReqDto req) {
		PageResult<ReceiveResDto> pageResult = new PageResult<ReceiveResDto>();
		try {
			req.setSearchType(BaseConsts.FOUR);
			pageResult = receiveService.queryResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("应收未核完查询失败[{}]: {}", JSONObject.toJSON(req), e);
			pageResult.setMsg("应收未核完查询失败,请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 
	 * TODO. 查询应收未收
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_UN_RECEIVE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiveResDto> queryUnReceiveByCon(ReceiveSearchReqDto req) {
		PageResult<ReceiveResDto> pageResult = new PageResult<ReceiveResDto>();
		try {
			req.setSearchType(BaseConsts.ONE);
			pageResult = receiveService.queryResultsByCon(req);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("应收未收查询失败[{}]: {}", JSONObject.toJSON(req), e);
			pageResult.setMsg("应收未收查询失败,请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 删除应收
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_RECEIVE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteReceiveById(Integer id) {
		BaseResult baseResult = new BaseResult();
		try {
			receiveService.deleteReceiveById(id);
		} catch (BaseException e) {
			LOGGER.error("删除应收失败[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除应收失败[{}]: {}", JSONObject.toJSON(id), e);
			baseResult.setMsg("删除应收失败,请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.RECEIVE_EXPORT, method = RequestMethod.GET)
	public String exportSaleReport(ModelMap model, ReceiveSearchReqDto req) {
		List<ReceiveResDto> result = receiveService.queryResultsExcelByCon(req);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("recManagerList", result);
		} else {
			model.addAttribute("recManagerList", new ArrayList<SaleDtlResult>());
		}
		return "export/fi/recManager_list";
	}

}
