package com.scfs.web.controller.logistics;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.logistics.dto.req.StlHistorySearchReqDto;
import com.scfs.domain.logistics.dto.req.StlHistorySummarySearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlHistoryResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.logistics.StlHistoryService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年10月21日.
 */
@Controller
public class StlHistoryController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(StlHistoryController.class);

	@Autowired
	private StlHistoryService stlHistoryService;

	@RequestMapping(value = BusUrlConsts.QUERY_STL_HISTORY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlHistoryResDto> queryStlList(StlHistorySearchReqDto stlHistorySearchReqDto) {
		PageResult<StlHistoryResDto> result = new PageResult<StlHistoryResDto>();
		try {
			result = stlHistoryService.queryStlHistoryResultsByCon(stlHistorySearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询历史库存列表异常[{}]", JSONObject.toJSON(stlHistorySearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询历史库存列表异常[{}]", JSONObject.toJSON(stlHistorySearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_STL_HISTORY_SUMMARY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlHistoryResDto> queryStlSummaryList(StlHistorySummarySearchReqDto stlHistorySummaryReqDto) {
		PageResult<StlHistoryResDto> result = new PageResult<StlHistoryResDto>();
		try {
			result = stlHistoryService.queryStlHistorySummaryResultsByCon(stlHistorySummaryReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询历史库存汇总列表异常[{}]", JSONObject.toJSON(stlHistorySummaryReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询历史库存汇总列表异常[{}]", JSONObject.toJSON(stlHistorySummaryReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_STL_HISTORY_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportStlHistoryCount(StlHistorySearchReqDto stlHistorySearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = stlHistoryService.isOverStlHistoryMaxLine(stlHistorySearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(stlHistorySearchReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_STL_HISTORY, method = RequestMethod.GET)
	public String exportStlHistory(ModelMap model, StlHistorySearchReqDto stlHistorySearchReqDto) {
		List<StlHistoryResDto> result = stlHistoryService.queryAllStlHistoryResultsByCon(stlHistorySearchReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("stlHistoryList", result);
		} else {
			model.addAttribute("stlHistoryList", new ArrayList<StlHistoryResDto>());
		}
		return "export/logistics/stl_history_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_STL_HISTORY_SUMMARY_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportStlHistorySummaryCount(StlHistorySummarySearchReqDto stlHistorySummaryReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = stlHistoryService.isOverStlHistorySummaryMaxLine(stlHistorySummaryReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(stlHistorySummaryReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_STL_HISTORY_SUMMARY, method = RequestMethod.GET)
	public String exportStlHistorySummary(ModelMap model, StlHistorySummarySearchReqDto stlHistorySummaryReqDto) {
		List<StlHistoryResDto> result = stlHistoryService
				.queryAllStlHistorySummaryResultsByCon(stlHistorySummaryReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("stlHistorySummaryList", result);
		} else {
			model.addAttribute("stlHistorySummaryList", new ArrayList<StlHistoryResDto>());
		}
		return "export/logistics/stl_history_summary_list";
	}
}
