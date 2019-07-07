package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.SaleReportDao;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.fee.dto.resp.FeeSumModel;
import com.scfs.domain.report.entity.SaleDtlResult;
import com.scfs.domain.report.entity.SaleDtlSum;
import com.scfs.domain.report.entity.SaleReport;
import com.scfs.domain.report.entity.SaleReportSum;
import com.scfs.domain.report.req.SaleReportReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * Created by Administrator on 2017年2月13日.
 */
@Service
public class SaleReportService {
	@Autowired
	private SaleReportDao saleReportDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private FeeServiceImpl feeServiceImpl;
	@Autowired
	private ReportProjectService reportProjectService;

	/**
	 * 查询销售报表
	 * 
	 * @param saleReportReqDto
	 * @return
	 */
	public PageResult<SaleReport> queryResultsByCon(SaleReportReqDto saleReportReqDto) {
		saleReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.ONE));

		PageResult<SaleReport> result = new PageResult<SaleReport>();
		int offSet = PageUtil.getOffSet(saleReportReqDto.getPage(), saleReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, saleReportReqDto.getPer_page());

		saleReportReqDto.setUserId(ServiceSupport.getUser().getId());
		List<SaleReport> saleReportList = saleReportDao.querySaleReportResultsByCon(saleReportReqDto, rowBounds);
		List<SaleReport> saleReportResList = convertToResDto(saleReportList);
		result.setItems(saleReportResList);
		String totalStr = querySumSaleReport(saleReportReqDto);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), saleReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(saleReportReqDto.getPage());
		result.setPer_page(saleReportReqDto.getPer_page());
		return result;
	}

	/**
	 * 查询销售报表合计
	 * 
	 * @param saleReportReqDto
	 * @return
	 */
	private String querySumSaleReport(SaleReportReqDto saleReportReqDto) {
		String totalStr = "";
		if (saleReportReqDto.getNeedSum() != null && saleReportReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<SaleReportSum> saleReportSumList = saleReportDao.querySumSaleReportResultsByCon(saleReportReqDto);
			if (!CollectionUtils.isEmpty(saleReportSumList)) {
				BigDecimal totalOuterSaleAmount = BigDecimal.ZERO;
				BigDecimal totalInnerSaleAmount = BigDecimal.ZERO;
				BigDecimal totalSaleAmount = BigDecimal.ZERO;
				BigDecimal totalCostAmount = BigDecimal.ZERO;
				BigDecimal totalProfitAmount = BigDecimal.ZERO;
				BigDecimal totalFeeProfitAmount = BigDecimal.ZERO;
				BigDecimal totalSumProfitAmount = BigDecimal.ZERO;

				for (SaleReportSum saleReportSum : saleReportSumList) {
					totalOuterSaleAmount = DecimalUtil.add(totalOuterSaleAmount,
							ServiceSupport.amountNewToRMB(
									null == saleReportSum.getTotalOuterSaleAmount() ? BigDecimal.ZERO
											: saleReportSum.getTotalOuterSaleAmount(),
									saleReportSum.getCurrencyType(), null));
					totalInnerSaleAmount = DecimalUtil.add(totalInnerSaleAmount,
							ServiceSupport.amountNewToRMB(
									null == saleReportSum.getTotalInnerSaleAmount() ? BigDecimal.ZERO
											: saleReportSum.getTotalInnerSaleAmount(),
									saleReportSum.getCurrencyType(), null));
					totalSaleAmount = DecimalUtil.add(totalSaleAmount,
							ServiceSupport.amountNewToRMB(
									null == saleReportSum.getTotalSaleAmount() ? BigDecimal.ZERO
											: saleReportSum.getTotalSaleAmount(),
									saleReportSum.getCurrencyType(), null));
					totalCostAmount = DecimalUtil.add(totalCostAmount,
							ServiceSupport.amountNewToRMB(
									null == saleReportSum.getTotalCostAmount() ? BigDecimal.ZERO
											: saleReportSum.getTotalCostAmount(),
									saleReportSum.getCurrencyType(), null));
					totalProfitAmount = DecimalUtil.add(totalProfitAmount,
							ServiceSupport.amountNewToRMB(
									null == saleReportSum.getTotalProfitAmount() ? BigDecimal.ZERO
											: saleReportSum.getTotalProfitAmount(),
									saleReportSum.getCurrencyType(), null));
					totalFeeProfitAmount = DecimalUtil.add(totalFeeProfitAmount,
							ServiceSupport.amountNewToRMB(
									null == saleReportSum.getTotalFeeProfitAmount() ? BigDecimal.ZERO
											: saleReportSum.getTotalFeeProfitAmount(),
									saleReportSum.getCurrencyType(), null));
					totalSumProfitAmount = DecimalUtil.add(totalSumProfitAmount,
							ServiceSupport.amountNewToRMB(
									null == saleReportSum.getTotalSumProfitAmount() ? BigDecimal.ZERO
											: saleReportSum.getTotalSumProfitAmount(),
									saleReportSum.getCurrencyType(), null));
				}
				totalStr = "外部销售金额：" + DecimalUtil.toAmountString(totalOuterSaleAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；内部销售金额："
						+ DecimalUtil.toAmountString(totalInnerSaleAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；销售金额："
						+ DecimalUtil.toAmountString(totalSaleAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；销售成本："
						+ DecimalUtil.toAmountString(totalCostAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；销售利润："
						+ DecimalUtil.toAmountString(totalProfitAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；费用利润："
						+ DecimalUtil.toAmountString(totalFeeProfitAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；合计利润："
						+ DecimalUtil.toAmountString(totalSumProfitAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
			}
		}
		return totalStr;
	}

	/**
	 * 查询销售报表(全部数据)
	 * 
	 * @param saleReportReqDto
	 * @return
	 */
	public List<SaleReport> queryAllResultsByCon(SaleReportReqDto saleReportReqDto) {
		if (null == saleReportReqDto.getUserId()) {
			saleReportReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<SaleReport> saleReportList = saleReportDao.querySaleReportResultsByCon(saleReportReqDto);
		List<SaleReport> saleReportResList = convertToResDto(saleReportList);
		return saleReportResList;
	}

	private List<SaleReport> convertToResDto(List<SaleReport> saleReportList) {
		List<SaleReport> saleReportResList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(saleReportList)) {
			return saleReportResList;
		}
		for (SaleReport saleReport : saleReportList) {
			SaleReport saleReportRes = convertToResDto(saleReport);
			saleReportResList.add(saleReportRes);
		}
		return saleReportResList;
	}

	private SaleReport convertToResDto(SaleReport saleReport) {
		SaleReport saleReportRes = new SaleReport();
		BeanUtils.copyProperties(saleReport, saleReportRes);
		if (null != saleReport.getProjectId()) {
			saleReportRes.setProjectName(cacheService.showProjectNameById(saleReport.getProjectId()));
		}
		if (null != saleReport.getCustomerId()) {
			saleReportRes.setCustomerName(
					cacheService.showSubjectNameByIdAndKey(saleReport.getCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		if (null != saleReport.getDepartmentId()) {
			BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(saleReport.getDepartmentId());
			if (null != baseDepartment) {
				saleReportRes.setDepartmentName(baseDepartment.getName());
			}
		}
		if (null != saleReport.getBizManagerId()) {
			saleReportRes.setBizManagerName(cacheService.getUserChineseNameByid(saleReport.getBizManagerId()));
		}
		if (null != saleReport.getCurrencyType()) {
			saleReportRes.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					Integer.toString(saleReport.getCurrencyType())));
		}
		if (null != saleReport.getBizType()) {
			saleReportRes.setBizTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE,
					Integer.toString(saleReport.getBizType())));
		}
		return saleReportRes;
	}

	/**
	 * 销售报表的销售明细
	 * 
	 * @param saleReportReqDto
	 * @return
	 */
	public PageResult<SaleDtlResult> querySaleDtlResults(SaleReportReqDto saleReportReqDto) {
		saleReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.ONE));

		PageResult<SaleDtlResult> result = new PageResult<SaleDtlResult>();
		int offSet = PageUtil.getOffSet(saleReportReqDto.getPage(), saleReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, saleReportReqDto.getPer_page());

		saleReportReqDto.setUserId(ServiceSupport.getUser().getId());
		List<SaleDtlResult> saleDtlResultList = saleReportDao.querySaleDtlResultsByCon(saleReportReqDto, rowBounds);
		List<SaleDtlResult> saleDtlResultResList = convertToSaleDtlResultResDto(saleDtlResultList);
		result.setItems(saleDtlResultResList);
		String totalStr = querySumSaleDtl(saleReportReqDto);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), saleReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(saleReportReqDto.getPage());
		result.setPer_page(saleReportReqDto.getPer_page());
		return result;
	}

	private List<SaleDtlResult> convertToSaleDtlResultResDto(List<SaleDtlResult> saleReportList) {
		List<SaleDtlResult> saleDtlResultResList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(saleReportList)) {
			return saleDtlResultResList;
		}
		for (SaleDtlResult saleDtlResult : saleReportList) {
			SaleDtlResult saleDtlResultRes = convertToResDto(saleDtlResult);
			saleDtlResultResList.add(saleDtlResultRes);
		}
		return saleDtlResultResList;
	}

	private SaleDtlResult convertToResDto(SaleDtlResult saleDtlResult) {
		SaleDtlResult saleDtlResultRes = new SaleDtlResult();
		BeanUtils.copyProperties(saleDtlResult, saleDtlResultRes);
		if (null != saleDtlResult.getProjectId()) {
			saleDtlResultRes.setProjectName(cacheService.showProjectNameById(saleDtlResult.getProjectId()));
		}
		if (null != saleDtlResult.getCustomerId()) {
			saleDtlResultRes.setCustomerName(
					cacheService.showSubjectNameByIdAndKey(saleDtlResult.getCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		if (null != saleDtlResult.getCurrencyType()) {
			saleDtlResultRes.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					Integer.toString(saleDtlResult.getCurrencyType())));
		}
		return saleDtlResultRes;
	}

	/**
	 * 查询销售报表合计
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	private String querySumSaleDtl(SaleReportReqDto saleReportReqDto) {
		String totalStr = "";
		SaleDtlSum saleDtlSum = saleReportDao.querySumSaleDtlResultsByCon(saleReportReqDto);
		if (null != saleDtlSum) {
			totalStr = "销售金额："
					+ DecimalUtil.toAmountString(
							null == saleDtlSum.getTotalSaleAmount() ? BigDecimal.ZERO : saleDtlSum.getTotalSaleAmount())
					+ BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(saleReportReqDto.getCurrencyType()) + "；销售成本："
					+ DecimalUtil.toAmountString(
							null == saleDtlSum.getTotalCostAmount() ? BigDecimal.ZERO : saleDtlSum.getTotalCostAmount())
					+ BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(saleReportReqDto.getCurrencyType()) + "；已回款金额："
					+ DecimalUtil.toAmountString(null == saleDtlSum.getTotalReturnedAmount() ? BigDecimal.ZERO
							: saleDtlSum.getTotalReturnedAmount())
					+ BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(saleReportReqDto.getCurrencyType());
		}
		return totalStr;
	}

	public PageResult<FeeQueryResDto> queryFeeDtlResults(SaleReportReqDto saleReportReqDto) {
		saleReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.ONE));

		PageResult<FeeQueryResDto> pageResult = new PageResult<FeeQueryResDto>();
		saleReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(saleReportReqDto.getPage(), saleReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, saleReportReqDto.getPer_page());
		List<FeeQueryResDto> list = feeServiceImpl
				.convertToListDtos(saleReportDao.queryFeeDtlByCon(saleReportReqDto, rowBounds));
		if (saleReportReqDto.getNeedSum() != null && saleReportReqDto.getNeedSum().equals(BaseConsts.ONE)) { // 需要合计
			BigDecimal recAmountSum = BigDecimal.ZERO;
			BigDecimal receivedAmountSum = BigDecimal.ZERO;
			BigDecimal provideInvoiceAmountSum = BigDecimal.ZERO;
			BigDecimal paidAmountSum = BigDecimal.ZERO;
			BigDecimal payAmountSum = BigDecimal.ZERO;
			BigDecimal acceptInvoiceAmountSum = BigDecimal.ZERO;
			List<FeeSumModel> feeSumModels = saleReportDao.queryFeeDtlSumByCon(saleReportReqDto);
			for (FeeSumModel model : feeSumModels) {
				recAmountSum = DecimalUtil.add(recAmountSum,
						ServiceSupport.amountNewToRMB(model.getRecAmountSum(), model.getCurrencyType(), new Date()));
				receivedAmountSum = DecimalUtil.add(receivedAmountSum, ServiceSupport
						.amountNewToRMB(model.getReceivedAmountSum(), model.getCurrencyType(), new Date()));
				provideInvoiceAmountSum = DecimalUtil.add(provideInvoiceAmountSum, ServiceSupport
						.amountNewToRMB(model.getProvideInvoiceAmountSum(), model.getCurrencyType(), new Date()));
				paidAmountSum = DecimalUtil.add(paidAmountSum,
						ServiceSupport.amountNewToRMB(model.getPaidAmountSum(), model.getCurrencyType(), new Date()));
				payAmountSum = DecimalUtil.add(payAmountSum,
						ServiceSupport.amountNewToRMB(model.getPayAmountSum(), model.getCurrencyType(), new Date()));
				acceptInvoiceAmountSum = DecimalUtil.add(acceptInvoiceAmountSum, ServiceSupport
						.amountNewToRMB(model.getAcceptInvoiceAmountSum(), model.getCurrencyType(), new Date()));
			}

			String totalStr = "应收费用 : " + DecimalUtil.formatScale2(recAmountSum) + " CNY   已收费用: "
					+ DecimalUtil.formatScale2(receivedAmountSum) + " CNY   已开票金额: "
					+ DecimalUtil.formatScale2(provideInvoiceAmountSum) + " CNY  应付费用 : "
					+ DecimalUtil.formatScale2(payAmountSum) + " CNY   已付费用: " + DecimalUtil.formatScale2(paidAmountSum)
					+ " CNY   已收票金额: " + DecimalUtil.formatScale2(acceptInvoiceAmountSum) + " CNY";
			pageResult.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), saleReportReqDto.getPer_page());
		pageResult.setItems(list);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(saleReportReqDto.getPage());
		pageResult.setPer_page(saleReportReqDto.getPer_page());
		return pageResult;
	}

	public boolean isOverSaleReportMaxLine(SaleReportReqDto saleReportReqDto) {
		saleReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = saleReportDao.querySaleReportCountByCon(saleReportReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("销售报表导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncSaleReportExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/report/sale/sale_report_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_20);
			asyncExcelService.addAsyncExcel(saleReportReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncSaleReportExport(SaleReportReqDto saleReportReqDto) {
		saleReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.ONE));

		Map<String, Object> model = Maps.newHashMap();
		List<SaleReport> saleReportList = queryAllResultsByCon(saleReportReqDto);
		model.put("saleReportList", saleReportList);
		return model;
	}

}
