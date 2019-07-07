package com.scfs.web.controller.logistics;

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
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.dto.req.StlSummarySearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlResDto;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.logistics.entity.StlSum;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.logistics.StlService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2016年10月21日.
 */
@Controller
public class StlController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(StlController.class);
	@Autowired
	private StlService stlService;

	@RequestMapping(value = BusUrlConsts.QUERY_STL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlResDto> queryStlResultsByCon(StlSearchReqDto stlSearchReqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		try {
			result = stlService.queryStlResultsByCon(stlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询库存列表异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询库存列表异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_STL_SUMMARY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlResDto> queryStlSummaryResultsByCon(StlSummarySearchReqDto stlSummarySearchReqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		try {
			result = stlService.queryStlSummaryResultsByCon(stlSummarySearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询库存汇总列表异常[{}]", JSONObject.toJSON(stlSummarySearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询库存汇总列表异常[{}]", JSONObject.toJSON(stlSummarySearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_AVAILABLE_STL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlResDto> queryAvailableStlResultsByCon(StlSearchReqDto stlSearchReqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		try {
			result = stlService.queryAvailableStlResultsByCon(stlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询可用库存列表异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询可用库存列表异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_GOODSID_AVAILABLE_STL, method = RequestMethod.POST)
	@ResponseBody
	public Result<StlSum> queryAvailableStlByGoodsId(StlSearchReqDto stlSearchReqDto) {
		Result<StlSum> result = new Result<StlSum>();
		try {
			result = stlService.queryAvailableStlByGoodsId(stlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询商品可用库存异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询商品可用库存异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_STL, method = RequestMethod.POST)
	@ResponseBody
	public Result<StlResDto> editStlById(StlSearchReqDto stlSearchReqDto) {
		Result<StlResDto> result = new Result<StlResDto>();
		try {
			result = stlService.editStlById(stlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询库存详情异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询库存详情异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_STL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateStlById(Stl stl) {
		BaseResult result = new BaseResult();
		try {
			stlService.updateGoodsStatusAndBatchNo(stl);
		} catch (BaseException e) {
			LOGGER.error("修改库存异常[{}]", JSONObject.toJSON(stl), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改库存异常[{}]", JSONObject.toJSON(stl), e);
			result.setMsg("修改异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SPLIT_STL, method = RequestMethod.POST)
	@ResponseBody
	public Result<StlResDto> splitStlById(StlSearchReqDto stlSearchReqDto) {
		Result<StlResDto> result = new Result<StlResDto>();
		try {
			result = stlService.splitStlById(stlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询库存异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询库存异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SPLIT_UPDATE_STL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult splitUpdateStlById(@RequestBody StlSearchReqDto stlSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			stlService.splitUpdateStlById(stlSearchReqDto);
		} catch (BaseException e) {
			LOGGER.error("拆分库存异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("拆分库存异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
			result.setMsg("拆分异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_STL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportStlCount(StlSearchReqDto stlSearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = stlService.isOverStlMaxLine(stlSearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(stlSearchReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_STL, method = RequestMethod.GET)
	public String exportStl(ModelMap model, StlSearchReqDto stlSearchReqDto) {
		List<StlResDto> result = stlService.queryAllStlResultsByCon(stlSearchReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("stlList", result);
		} else {
			model.addAttribute("stlList", new ArrayList<StlResDto>());
		}
		return "export/logistics/stl_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_STL_SUMMARY_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportStlSummaryCount(StlSummarySearchReqDto stlSummarySearchReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = stlService.isOverStlSummaryMaxLine(stlSummarySearchReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(stlSummarySearchReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_STL_SUMMARY, method = RequestMethod.GET)
	public String exportStlSummary(ModelMap model, StlSummarySearchReqDto stlSummarySearchReqDto) {
		List<StlResDto> result = stlService.queryAllStlSummaryResultsByCon(stlSummarySearchReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("stlSummaryList", result);
		} else {
			model.addAttribute("stlSummaryList", new ArrayList<StlResDto>());
		}
		return "export/logistics/stl_summary_list";
	}

}
