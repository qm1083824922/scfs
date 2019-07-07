package com.scfs.web.controller.report;

import java.text.ParseException;
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
import com.scfs.domain.report.entity.GoodsInRepot;
import com.scfs.domain.report.entity.GoodsOutReport;
import com.scfs.domain.report.entity.GoodsPlReport;
import com.scfs.domain.report.entity.GoodsRtReport;
import com.scfs.domain.report.entity.GoodsStlReport;
import com.scfs.domain.report.req.GoodsReportReqDto;
import com.scfs.domain.report.resp.GoodsReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.report.GoodsReportService;

/**
 * <pre>
 * 
 *  File: GoodsReportContorller.java
 *  Description:          铺货进销存报表
 *  TODO
 *  Date,					Who,				
 *  2017年08月31日				Administrator
 *
 * </pre>
 */
@Controller
public class GoodsReportContorller {

	private final static Logger LOGGER = LoggerFactory.getLogger(GoodsReportContorller.class);
	@Autowired
	private GoodsReportService goodsReportService;

	/**
	 * 查询铺货进销存数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_GOODS_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<GoodsReportResDto> queryGoodsReportByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsReportResDto> pageResult = new PageResult<GoodsReportResDto>();
		try {
			pageResult = goodsReportService.queryResultByCon(goodsReportReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			pageResult.setMsg("铺货进销存报表异常,请稍后再试");
			LOGGER.error("铺货进销存报表查询异常[{}]: {}", JSONObject.toJSON(goodsReportReqDto), e);
		}
		return pageResult;
	}

	/**
	 * 查询商品详情
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_GOODS_DETAIL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<GoodsReportResDto> queryGoodsDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsReportResDto> result = new PageResult<GoodsReportResDto>();
		try {
			return goodsReportService.queryGoodsDetail(goodsReportReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询商品详情信息异常,请稍后再试");
			LOGGER.error("铺查询商品详情信息异常[{}]: {}", JSONObject.toJSON(goodsReportReqDto), e);
		}
		return result;
	}

	/**
	 * 查询入库明细
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_GOODS_PMSIN_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<GoodsInRepot> queryPmsInDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsInRepot> result = new PageResult<GoodsInRepot>();
		try {
			result = goodsReportService.queryPmsInDetailByCon(goodsReportReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询入库明细信息异常,请稍后再试");
			LOGGER.error("查询入库明细信息异常[{}]: {}", JSONObject.toJSON(goodsReportReqDto), e);
		}
		return result;
	}

	/**
	 * 查询出库明细
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_GOODS_PMSOUT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<GoodsOutReport> queryPmsOutDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsOutReport> result = new PageResult<GoodsOutReport>();
		try {
			result = goodsReportService.queryPmsOutDetail(goodsReportReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询出库明细信息异常,请稍后再试");
			LOGGER.error("查询出库明细信息异常[{}]: {}", JSONObject.toJSON(goodsReportReqDto), e);
		}
		return result;
	}

	/**
	 * 查询请款明细数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_GOODS_PMSPL_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<GoodsPlReport> queryPmsPlDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsPlReport> result = new PageResult<GoodsPlReport>();
		try {
			result = goodsReportService.queryPmsPlDetailByCon(goodsReportReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询请款明细信息异常,请稍后再试");
			LOGGER.error("查询请款明细信息异常[{}]: {}", JSONObject.toJSON(goodsReportReqDto), e);
		}
		return result;
	}

	/**
	 * 查询退货数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_GOODS_PMSRT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<GoodsRtReport> queryPmsRTDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsRtReport> result = new PageResult<GoodsRtReport>();
		try {
			result = goodsReportService.queryPmsRTDetailByCon(goodsReportReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询退货明细信息异常,请稍后再试");
			LOGGER.error("查询退货明细信息异常[{}]: {}", JSONObject.toJSON(goodsReportReqDto), e);
		}
		return result;
	}

	/**
	 * 查询库存数据
	 * 
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_GOODS_STL_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<GoodsStlReport> queryStlDetailByCon(GoodsReportReqDto goodsReportReqDto) {
		PageResult<GoodsStlReport> result = new PageResult<GoodsStlReport>();
		try {
			result = goodsReportService.queryStlDetailByCon(goodsReportReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询库存明细信息异常,请稍后再试");
			LOGGER.error("查询库存明细信息异常[{}]: {}", JSONObject.toJSON(goodsReportReqDto), e);
		}
		return result;
	}

	/**
	 * 导出铺货报表数据
	 * 
	 * @param model
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_GOODS_REPORT, method = RequestMethod.GET)
	public String exportGoodsReport(ModelMap model, GoodsReportReqDto goodsReportReqDto) {
		List<GoodsReportResDto> result = goodsReportService.queryAllGoodsReportExport(goodsReportReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("goodsReportList", result);
		} else {
			model.addAttribute("goodsReportList", new ArrayList<GoodsReportResDto>());
		}
		return "export/report/goods/goods_report_list";
	}

	/**
	 * 导出入库明细
	 * 
	 * @param model
	 * @param goodsReportReqDto
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_GOODS_PMSIN, method = RequestMethod.GET)
	public String exportGoodsPmsInReport(ModelMap model, GoodsReportReqDto goodsReportReqDto) throws ParseException {
		List<GoodsInRepot> result = goodsReportService.queryAllGoodsPmsInReportExport(goodsReportReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("goodsPmsInReportList", result);
		} else {
			model.addAttribute("goodsPmsInReportList", new ArrayList<GoodsReportResDto>());
		}
		return "export/report/goods/goods_pmsin_report_list";
	}

	/**
	 * 导出出库明细
	 * 
	 * @param model
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_GOODS_PMSOUT, method = RequestMethod.GET)
	public String exportGoodsPmsOutReport(ModelMap model, GoodsReportReqDto goodsReportReqDto) {
		List<GoodsOutReport> result = goodsReportService.queryAllGoodsPmsOutReportExport(goodsReportReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("goodsPmsOutReportList", result);
		} else {
			model.addAttribute("goodsPmsOutReportList", new ArrayList<GoodsReportResDto>());
		}
		return "export/report/goods/goods_pmsout_report_list";
	}

	/**
	 * 导出请款明细
	 * 
	 * @param model
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_GOODS_PLEASE, method = RequestMethod.GET)
	public String exportGoodsPleaseReport(ModelMap model, GoodsReportReqDto goodsReportReqDto) {
		List<GoodsPlReport> result = goodsReportService.queryAllGoodsPleaseReportExport(goodsReportReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("goodsReportPleaseList", result);
		} else {
			model.addAttribute("goodsReportPleaseList", new ArrayList<GoodsReportResDto>());
		}
		return "export/report/goods/goods_please_report_list";
	}

	/**
	 * 导出退货明细
	 * 
	 * @param model
	 * @param goodsReportReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_GOODS_RETURN, method = RequestMethod.GET)
	public String exportGoodsReturnReport(ModelMap model, GoodsReportReqDto goodsReportReqDto) {
		List<GoodsRtReport> result = goodsReportService.queryAllGoodsReturnReportExport(goodsReportReqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("goodsReturnReportList", result);
		} else {
			model.addAttribute("goodsReturnReportList", new ArrayList<GoodsReportResDto>());
		}
		return "export/report/goods/goods_return_report_list";
	}
}