package com.scfs.web.controller.report;

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
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.report.entity.SaleDtlResult;
import com.scfs.domain.report.entity.SaleReport;
import com.scfs.domain.report.req.SaleReportReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.report.SaleReportService;
import com.scfs.web.controller.BaseController;

/**
 * Created by Administrator on 2017年2月13日.
 */
@Controller
public class SaleReportController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(SaleReportController.class);
	@Autowired
	private SaleReportService saleReportService;

	@RequestMapping(value = BusUrlConsts.QUERY_SALE_REPORT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<SaleReport> queryResultsByCon(SaleReportReqDto saleReportReqDto) {
		PageResult<SaleReport> result = new PageResult<SaleReport>();
		try {
			result = saleReportService.queryResultsByCon(saleReportReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询销售报表异常[{}]", JSONObject.toJSON(saleReportReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售报表异常[{}]", JSONObject.toJSON(saleReportReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_SALE_REPORT_SALE_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<SaleDtlResult> querySaleDtlResults(SaleReportReqDto saleReportReqDto) {
		PageResult<SaleDtlResult> result = new PageResult<SaleDtlResult>();
		try {
			result = saleReportService.querySaleDtlResults(saleReportReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询销售报表销售明细异常[{}]", JSONObject.toJSON(saleReportReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售报表销售明细异常[{}]", JSONObject.toJSON(saleReportReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.QUERY_SALE_REPORT_FEE_PROFIT_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeQueryResDto> queryFeeProfitDtlResults(SaleReportReqDto saleReportReqDto) {
		PageResult<FeeQueryResDto> result = new PageResult<FeeQueryResDto>();
		try {
			saleReportReqDto.setNeedSum(BaseConsts.ONE);
			result = saleReportService.queryFeeDtlResults(saleReportReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询销售报表费用利润明细异常[{}]", JSONObject.toJSON(saleReportReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询销售报表费用利润明细异常[{}]", JSONObject.toJSON(saleReportReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_SALE_REPORT_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportSaleReportCount(SaleReportReqDto saleReportReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = saleReportService.isOverSaleReportMaxLine(saleReportReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(saleReportReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_SALE_REPORT, method = RequestMethod.GET)
	public String exportSaleReport(ModelMap model, SaleReportReqDto saleReportReqDto) {
		List<SaleReport> result = saleReportService.queryAllResultsByCon(saleReportReqDto);

		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("saleReportList", result);
		} else {
			model.addAttribute("saleReportList", new ArrayList<SaleDtlResult>());
		}
		return "export/report/sale/sale_report_list";
	}
}
