package com.scfs.service.report;

import java.math.BigDecimal;
import java.text.ParseException;
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
import com.google.common.collect.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.FundDtlReportDao;
import com.scfs.dao.report.FundReportDao;
import com.scfs.dao.report.PerformanceReportDao;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.report.entity.FundDtlReport;
import com.scfs.domain.report.entity.PerformanceReport;
import com.scfs.domain.report.entity.PerformanceReportSum;
import com.scfs.domain.report.req.FundReportSearchReqDto;
import com.scfs.domain.report.req.PerformanceReportReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2017年4月12日.
 */
@Service
public class PerformanceReportService {
	@Autowired
	private PerformanceReportDao performanceReportDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private FundReportDao fundReportDao;
	@Autowired
	private FundDtlReportDao fundDtlReportDao;
	@Autowired
	private ReportRecordService reportRecordService;
	@Autowired
	private ReportProjectService reportProjectService;

	/**
	 * 查询绩效报表
	 * 
	 * @param performanceReportReqDto
	 * @return
	 */
	public PageResult<PerformanceReport> queryResultsByCon(PerformanceReportReqDto performanceReportReqDto) {
		performanceReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SEVEN));
		resetPerformanceReportReqDto(performanceReportReqDto, false);

		PageResult<PerformanceReport> result = new PageResult<PerformanceReport>();
		int offSet = PageUtil.getOffSet(performanceReportReqDto.getPage(), performanceReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, performanceReportReqDto.getPer_page());

		performanceReportReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PerformanceReport> performanceReportList = performanceReportDao
				.queryPerformanceReportResultsByCon(performanceReportReqDto, rowBounds);
		List<PerformanceReport> performanceReportResList = convertToResDto(performanceReportList,
				performanceReportReqDto);
		result.setItems(performanceReportResList);

		String totalStr = querySumPerformanceReport(performanceReportReqDto);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), performanceReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(performanceReportReqDto.getPage());
		result.setPer_page(performanceReportReqDto.getPer_page());
		return result;
	}

	private void resetPerformanceReportReqDto(PerformanceReportReqDto performanceReportReqDto, boolean isSchedule) {
		if (!reportRecordService.matchIssue(performanceReportReqDto.getStartIssue())
				|| !reportRecordService.matchIssue(performanceReportReqDto.getEndIssue())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请输入正确的起止期号");
		}
		if (performanceReportReqDto.getEndIssue().compareTo(performanceReportReqDto.getStartIssue()) < 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "起始期号必须小于等于截止期号");
		}

		if (performanceReportReqDto.getPeriodType().equals(BaseConsts.ONE)) { // 1-当期
			performanceReportReqDto.setStartIssue(performanceReportReqDto.getStartIssue());
			performanceReportReqDto.setEndIssue(performanceReportReqDto.getEndIssue());
			performanceReportReqDto.setPastStartIssue(null);
			performanceReportReqDto.setPastEndIssue(null);
			// performanceReportReqDto.setExcludeFundCost(BaseConsts.ONE);
		} else if (performanceReportReqDto.getPeriodType().equals(BaseConsts.TWO)) { // 2-往期
			performanceReportReqDto.setPastStartIssue(performanceReportReqDto.getStartIssue());
			performanceReportReqDto.setPastEndIssue(performanceReportReqDto.getEndIssue());
			performanceReportReqDto.setStartIssue(null);
			performanceReportReqDto.setEndIssue(null);
		}

		/**
		 * String startIssue = performanceReportReqDto.getStartIssue(); String
		 * endIssue = performanceReportReqDto.getEndIssue(); Date currDate = new
		 * Date(); String currIssue =
		 * DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);//当前月 String
		 * preIssue = DateFormatUtils.format(DateFormatUtils.YYYYMM,
		 * DateFormatUtils.getPreMonthDate(currDate));//上个月 if
		 * (endIssue.compareTo(preIssue) > 0) { int count =
		 * currencyRateDao.queryCountByTheMonthCd(currIssue); if (count <= 0) {
		 * throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "未同步【" + currIssue
		 * + "】的最新汇率至SCFS"); } performanceReportReqDto.setStartIssue(currIssue);
		 * performanceReportReqDto.setEndIssue(endIssue); if
		 * (startIssue.compareTo(currIssue) < 0) {
		 * performanceReportReqDto.setPastStartIssue(startIssue);
		 * performanceReportReqDto.setPastEndIssue(preIssue); } } else { if
		 * (isSchedule == true) {
		 * performanceReportReqDto.setStartIssue(startIssue);
		 * performanceReportReqDto.setEndIssue(endIssue);
		 * performanceReportReqDto.setPastStartIssue(null);
		 * performanceReportReqDto.setPastEndIssue(null); } else {
		 * performanceReportReqDto.setStartIssue(null);
		 * performanceReportReqDto.setEndIssue(null);
		 * performanceReportReqDto.setPastStartIssue(startIssue);
		 * performanceReportReqDto.setPastEndIssue(endIssue); } }
		 **/
	}

	/**
	 * 查询绩效报表合计
	 * 
	 * @param performanceReportReqDto
	 * @return
	 */
	private String querySumPerformanceReport(PerformanceReportReqDto performanceReportReqDto) {
		String totalStr = "";
		if (performanceReportReqDto.getNeedSum() != null && performanceReportReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			BigDecimal totalNum = BigDecimal.ZERO;
			BigDecimal totalSaleAmount = BigDecimal.ZERO;
			BigDecimal totalServiceAmount = BigDecimal.ZERO;
			BigDecimal totalCompositeTaxAmount = BigDecimal.ZERO;
			BigDecimal totalPurchaseCost = BigDecimal.ZERO;
			BigDecimal totalFundCost = BigDecimal.ZERO;
			BigDecimal totalWarehouseAmount = BigDecimal.ZERO;
			BigDecimal totalMarketAmount = BigDecimal.ZERO;
			BigDecimal totalFinanceAmount = BigDecimal.ZERO;
			BigDecimal totalManageAmount = BigDecimal.ZERO;
			BigDecimal totalManualAmount = BigDecimal.ZERO;
			BigDecimal totalProfitAmount = BigDecimal.ZERO;
			if (performanceReportReqDto.getPeriodType().equals(BaseConsts.ONE)) { // 1-当期
				performanceReportReqDto.setExcludeFundCost(BaseConsts.ONE);
				List<PerformanceReportSum> performanceReportSumList = performanceReportDao
						.querySumPerformanceReportByCon(performanceReportReqDto);
				if (!CollectionUtils.isEmpty(performanceReportSumList)) {
					for (PerformanceReportSum performanceReportSum : performanceReportSumList) {
						totalNum = DecimalUtil.add(totalNum, null == performanceReportSum.getNum() ? BigDecimal.ZERO
								: performanceReportSum.getNum());
						totalSaleAmount = DecimalUtil.add(totalSaleAmount, null == performanceReportSum.getSaleAmount()
								? BigDecimal.ZERO : performanceReportSum.getSaleAmount());
						totalServiceAmount = DecimalUtil.add(totalServiceAmount,
								null == performanceReportSum.getServiceAmount() ? BigDecimal.ZERO
										: performanceReportSum.getServiceAmount());
						totalCompositeTaxAmount = DecimalUtil.add(totalCompositeTaxAmount,
								null == performanceReportSum.getCompositeTaxAmount() ? BigDecimal.ZERO
										: performanceReportSum.getCompositeTaxAmount());
						totalPurchaseCost = DecimalUtil.add(totalPurchaseCost,
								null == performanceReportSum.getPurchaseCost() ? BigDecimal.ZERO
										: performanceReportSum.getPurchaseCost());
						totalFundCost = DecimalUtil.add(totalFundCost, null == performanceReportSum.getFundCost()
								? BigDecimal.ZERO : performanceReportSum.getFundCost());
						totalWarehouseAmount = DecimalUtil.add(totalWarehouseAmount,
								null == performanceReportSum.getWarehouseAmount() ? BigDecimal.ZERO
										: performanceReportSum.getWarehouseAmount());
						totalMarketAmount = DecimalUtil.add(totalMarketAmount,
								null == performanceReportSum.getMarketAmount() ? BigDecimal.ZERO
										: performanceReportSum.getMarketAmount());
						totalFinanceAmount = DecimalUtil.add(totalFinanceAmount,
								null == performanceReportSum.getFinanceAmount() ? BigDecimal.ZERO
										: performanceReportSum.getFinanceAmount());
						totalManageAmount = DecimalUtil.add(totalManageAmount,
								null == performanceReportSum.getManageAmount() ? BigDecimal.ZERO
										: performanceReportSum.getManageAmount());
						totalManualAmount = DecimalUtil.add(totalManualAmount,
								null == performanceReportSum.getManualAmount() ? BigDecimal.ZERO
										: performanceReportSum.getManualAmount());
						totalProfitAmount = DecimalUtil.add(totalProfitAmount,
								null == performanceReportSum.getProfitAmount() ? BigDecimal.ZERO
										: performanceReportSum.getProfitAmount());
					}
				}

				/**
				 * //查询资金成本 performanceReportReqDto.setExcludeFundCost(null);
				 * performanceReportReqDto.setBillType(BaseConsts.FIVE);
				 * List<PerformanceReport> performanceReportList =
				 * performanceReportDao.queryPerformanceReportResultsByCon(performanceReportReqDto);
				 * List<PerformanceReport> performanceReportResList =
				 * convertToResDto(performanceReportList,
				 * performanceReportReqDto); if
				 * (!CollectionUtils.isEmpty(performanceReportResList)) { for
				 * (PerformanceReport performanceReport :
				 * performanceReportResList) { totalNum =
				 * DecimalUtil.add(totalNum, null == performanceReport.getNum()
				 * ? BigDecimal.ZERO : performanceReport.getNum());
				 * totalSaleAmount = DecimalUtil.add(totalSaleAmount, null ==
				 * performanceReport.getSaleAmount() ? BigDecimal.ZERO :
				 * performanceReport.getSaleAmount()); totalServiceAmount =
				 * DecimalUtil.add(totalServiceAmount, null ==
				 * performanceReport.getServiceAmount() ? BigDecimal.ZERO :
				 * performanceReport.getServiceAmount());
				 * totalCompositeTaxAmount =
				 * DecimalUtil.add(totalCompositeTaxAmount, null ==
				 * performanceReport.getCompositeTaxAmount() ? BigDecimal.ZERO :
				 * performanceReport.getCompositeTaxAmount()); totalPurchaseCost
				 * = DecimalUtil.add(totalPurchaseCost, null ==
				 * performanceReport.getPurchaseCost() ? BigDecimal.ZERO :
				 * performanceReport.getPurchaseCost()); totalFundCost =
				 * DecimalUtil.add(totalFundCost, null ==
				 * performanceReport.getFundCost() ? BigDecimal.ZERO :
				 * performanceReport.getFundCost()); totalWarehouseAmount =
				 * DecimalUtil.add(totalWarehouseAmount, null ==
				 * performanceReport.getWarehouseAmount() ? BigDecimal.ZERO :
				 * performanceReport.getWarehouseAmount()); totalMarketAmount =
				 * DecimalUtil.add(totalMarketAmount, null ==
				 * performanceReport.getMarketAmount() ? BigDecimal.ZERO :
				 * performanceReport.getMarketAmount()); totalFinanceAmount =
				 * DecimalUtil.add(totalFinanceAmount, null ==
				 * performanceReport.getFinanceAmount() ? BigDecimal.ZERO :
				 * performanceReport.getFinanceAmount()); totalManageAmount =
				 * DecimalUtil.add(totalManageAmount, null ==
				 * performanceReport.getManageAmount() ? BigDecimal.ZERO :
				 * performanceReport.getManageAmount()); totalManualAmount =
				 * DecimalUtil.add(totalManualAmount, null ==
				 * performanceReport.getManualAmount() ? BigDecimal.ZERO :
				 * performanceReport.getManualAmount()); totalProfitAmount =
				 * DecimalUtil.add(totalProfitAmount, null ==
				 * performanceReport.getProfitAmount() ? BigDecimal.ZERO :
				 * performanceReport.getProfitAmount()); } }
				 **/
			} else if (performanceReportReqDto.getPeriodType().equals(BaseConsts.TWO)) { // 2-往期
				// 本地存储数据
				if (null != performanceReportReqDto.getPastStartIssue()
						&& null != performanceReportReqDto.getPastEndIssue()) {
					List<PerformanceReportSum> performanceReportSumList2 = performanceReportDao
							.querySumPerformanceReportByCon4Store(performanceReportReqDto);
					if (!CollectionUtils.isEmpty(performanceReportSumList2)) {
						for (PerformanceReportSum performanceReportSum : performanceReportSumList2) {
							totalNum = DecimalUtil.add(totalNum, null == performanceReportSum.getNum() ? BigDecimal.ZERO
									: performanceReportSum.getNum());
							totalSaleAmount = DecimalUtil.add(totalSaleAmount,
									null == performanceReportSum.getSaleAmount() ? BigDecimal.ZERO
											: performanceReportSum.getSaleAmount());
							totalServiceAmount = DecimalUtil.add(totalServiceAmount,
									null == performanceReportSum.getServiceAmount() ? BigDecimal.ZERO
											: performanceReportSum.getServiceAmount());
							totalCompositeTaxAmount = DecimalUtil.add(totalCompositeTaxAmount,
									null == performanceReportSum.getCompositeTaxAmount() ? BigDecimal.ZERO
											: performanceReportSum.getCompositeTaxAmount());
							totalPurchaseCost = DecimalUtil.add(totalPurchaseCost,
									null == performanceReportSum.getPurchaseCost() ? BigDecimal.ZERO
											: performanceReportSum.getPurchaseCost());
							totalFundCost = DecimalUtil.add(totalFundCost, null == performanceReportSum.getFundCost()
									? BigDecimal.ZERO : performanceReportSum.getFundCost());
							totalWarehouseAmount = DecimalUtil.add(totalWarehouseAmount,
									null == performanceReportSum.getWarehouseAmount() ? BigDecimal.ZERO
											: performanceReportSum.getWarehouseAmount());
							totalMarketAmount = DecimalUtil.add(totalMarketAmount,
									null == performanceReportSum.getMarketAmount() ? BigDecimal.ZERO
											: performanceReportSum.getMarketAmount());
							totalFinanceAmount = DecimalUtil.add(totalFinanceAmount,
									null == performanceReportSum.getFinanceAmount() ? BigDecimal.ZERO
											: performanceReportSum.getFinanceAmount());
							totalManageAmount = DecimalUtil.add(totalManageAmount,
									null == performanceReportSum.getManageAmount() ? BigDecimal.ZERO
											: performanceReportSum.getManageAmount());
							totalManualAmount = DecimalUtil.add(totalManualAmount,
									null == performanceReportSum.getManualAmount() ? BigDecimal.ZERO
											: performanceReportSum.getManualAmount());
							totalProfitAmount = DecimalUtil.add(totalProfitAmount,
									null == performanceReportSum.getProfitAmount() ? BigDecimal.ZERO
											: performanceReportSum.getProfitAmount());
						}
					}
				}
			}

			totalStr = "数量：" + DecimalUtil.toAmountString(totalNum) + "；销售总价："
					+ DecimalUtil.toAmountString(totalSaleAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；服务收入："
					+ DecimalUtil.toAmountString(totalServiceAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；综合税金："
					+ DecimalUtil.toAmountString(totalCompositeTaxAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；采购成本："
					+ DecimalUtil.toAmountString(totalPurchaseCost) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；资金成本："
					+ DecimalUtil.toAmountString(totalFundCost) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；仓储物流费："
					+ DecimalUtil.toAmountString(totalWarehouseAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；市场费用："
					+ DecimalUtil.toAmountString(totalMarketAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；财务费用："
					+ DecimalUtil.toAmountString(totalFinanceAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；管理费用："
					+ DecimalUtil.toAmountString(totalManageAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；人工费用："
					+ DecimalUtil.toAmountString(totalManualAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；利润："
					+ DecimalUtil.toAmountString(totalProfitAmount) + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
		}
		return totalStr;
	}

	/**
	 * 查询绩效报表(全部数据)
	 * 
	 * @param performanceReportReqDto
	 * @return
	 */
	public List<PerformanceReport> queryAllResultsByCon(PerformanceReportReqDto performanceReportReqDto) {
		performanceReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SEVEN));
		if (null == performanceReportReqDto.getUserId()) {
			performanceReportReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		resetPerformanceReportReqDto(performanceReportReqDto, false);
		List<PerformanceReport> performanceReportList = performanceReportDao
				.queryPerformanceReportResultsByCon(performanceReportReqDto);
		List<PerformanceReport> performanceReportResList = convertToResDto(performanceReportList,
				performanceReportReqDto);
		return performanceReportResList;
	}

	public List<PerformanceReport> queryAllResultsByCon4Admin(PerformanceReportReqDto performanceReportReqDto) {
		performanceReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SEVEN));
		resetPerformanceReportReqDto(performanceReportReqDto, true);
		List<PerformanceReport> performanceReportList = performanceReportDao
				.queryPerformanceReportResultsByCon(performanceReportReqDto);
		List<PerformanceReport> performanceReportResList = convertToResDto(performanceReportList,
				performanceReportReqDto);
		return performanceReportResList;
	}

	private List<PerformanceReport> convertToResDto(List<PerformanceReport> performanceReportList,
			PerformanceReportReqDto performanceReportReqDto) {
		List<PerformanceReport> performanceReportResList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(performanceReportList)) {
			return performanceReportResList;
		}
		for (PerformanceReport performanceReport : performanceReportList) {
			PerformanceReport performanceReportRes = convertToResDto(performanceReport, performanceReportReqDto);
			performanceReportResList.add(performanceReportRes);
		}
		return performanceReportResList;
	}

	private PerformanceReport convertToResDto(PerformanceReport performanceReport,
			PerformanceReportReqDto performanceReportReqDto) {
		PerformanceReport performanceReportRes = new PerformanceReport();
		BeanUtils.copyProperties(performanceReport, performanceReportRes);
		if (null != performanceReportRes.getProjectId()) {
			performanceReportRes.setProjectName(cacheService.showProjectNameById(performanceReportRes.getProjectId()));
			performanceReportRes.setBusinessUnitName(cacheService.showSubjectNameByIdAndKey(
					cacheService.getProjectById(performanceReportRes.getProjectId()).getBusinessUnitId(),
					CacheKeyConsts.BUSI_UNIT));

		}
		if (null != performanceReportRes.getCustomerId()) {
			performanceReportRes.setCustomerName(cacheService
					.showSubjectNameByIdAndKey(performanceReportRes.getCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		if (null != performanceReportRes.getWarehouseId()) {
			performanceReportRes.setWarehouseName(cacheService
					.showSubjectNameByIdAndKey(performanceReportRes.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		}

		if (null != performanceReportRes.getDepartmentId()) {
			BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(performanceReportRes.getDepartmentId());
			if (null != baseDepartment) {
				performanceReportRes.setDepartmentName(baseDepartment.getName());
			}
		}
		if (null != performanceReportRes.getBizManagerId()) {
			performanceReportRes
					.setBizManagerName(cacheService.getUserChineseNameByid(performanceReportRes.getBizManagerId()));
		}
		if (null != performanceReportRes.getBizType()) {
			performanceReportRes.setBizTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE,
					Integer.toString(performanceReportRes.getBizType())));
		}
		if (null != performanceReportRes.getBillType()) {
			performanceReportRes.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PERFORMANCE_BILL_TYPE,
					Integer.toString(performanceReportRes.getBillType())));
		}
		if (null != performanceReportRes.getBusinessUnitId()) {
			performanceReportRes.setBusinessUnitName(cacheService
					.showSubjectNameByIdAndKey(performanceReportRes.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		}

		if (performanceReportRes.getDataSource().equals(BaseConsts.ONE)) { // 1-实时查询
			if (performanceReportRes.getBillType().equals(BaseConsts.ONE)
					|| performanceReportRes.getBillType().equals(BaseConsts.TWO)
					|| performanceReportRes.getBillType().equals(BaseConsts.THREE)
					|| performanceReportRes.getBillType().equals(BaseConsts.SIX)) {
				performanceReportRes.setProductNo(
						cacheService.getFeeSpecNoNameById(Integer.parseInt(performanceReportRes.getProductNo())));
			}

			if (performanceReportRes.getBillType().equals(BaseConsts.ONE)) { // 1-应收费用
				performanceReportRes.setProfitAmount(getProfitAmount(performanceReportRes));
				if (null != performanceReportRes.getRecAmount()
						&& !DecimalUtil.eq(performanceReportRes.getRecAmount(), BigDecimal.ZERO)) {
					performanceReportRes.setProfitRate(DecimalUtil.divide(performanceReportRes.getProfitAmount(),
							DecimalUtil.subtract(
									DecimalUtil.add(performanceReportRes.getSaleAmount(),
											performanceReportRes.getServiceAmount()),
									performanceReportRes.getCompositeTaxAmount())));
				}
			}
			if (performanceReportRes.getBillType().equals(BaseConsts.TWO)
					|| performanceReportRes.getBillType().equals(BaseConsts.SIX)) { // 2-应付费用,
																					// 6-管理费用
				performanceReportRes.setProfitAmount(getProfitAmount(performanceReportRes));
				if (null != performanceReportRes.getPayAmount()) {
					if (DecimalUtil.eq(performanceReportRes.getProfitAmount(), BigDecimal.ZERO)) {
						performanceReportRes.setProfitRate(BigDecimal.ZERO);
					} else if (DecimalUtil.gt(performanceReportRes.getProfitAmount(), BigDecimal.ZERO)) {
						performanceReportRes.setProfitRate(new BigDecimal("1"));
					} else if (DecimalUtil.lt(performanceReportRes.getProfitAmount(), BigDecimal.ZERO)) {
						performanceReportRes.setProfitRate(new BigDecimal("-1"));
					}
				}
			}
			if (performanceReportRes.getBillType().equals(BaseConsts.THREE)) { // 3-应收应付费用
				performanceReportRes.setProfitAmount(getProfitAmount(performanceReportRes));
				if (DecimalUtil.eq(performanceReportRes.getProfitAmount(), BigDecimal.ZERO)) {
					performanceReportRes.setProfitRate(BigDecimal.ZERO);
				} else {
					BigDecimal incomeAmount = DecimalUtil.subtract(performanceReportRes.getSaleAmount(), DecimalUtil
							.add(performanceReportRes.getCompositeTaxAmount(), performanceReportRes.getPurchaseCost()));
					performanceReportRes
							.setProfitRate(DecimalUtil.divide(performanceReportRes.getProfitAmount(), incomeAmount));
				}
			}
			if (performanceReportRes.getBillType().equals(BaseConsts.FOUR)) { // 4-销售单
				performanceReportRes.setServiceAmount(BigDecimal.ZERO);
				BaseGoods baseGoods = cacheService.getGoodsById(Integer.parseInt(performanceReportRes.getProductNo()));
				if (null != baseGoods) {
					performanceReportRes.setProductNo(baseGoods.getNumber());
				} else {
					performanceReportRes.setProductNo(null);
				}
				performanceReportRes.setProfitAmount(getProfitAmount(performanceReportRes));
				if (null != performanceReportRes.getSaleAmount()
						&& !DecimalUtil.eq(performanceReportRes.getSaleAmount(), BigDecimal.ZERO)) {
					performanceReportRes.setProfitRate(DecimalUtil.divide(performanceReportRes.getProfitAmount(),
							DecimalUtil.subtract(
									DecimalUtil.add(performanceReportRes.getSaleAmount(),
											performanceReportRes.getServiceAmount()),
									performanceReportRes.getCompositeTaxAmount())));
				}
			}
			/**
			 * if (performanceReportRes.getBillType().equals(BaseConsts.FIVE)) {
			 * // 5-资金成本 BigDecimal fundCost = getFundCost(performanceReportRes,
			 * performanceReportReqDto); //资金成本
			 * performanceReportRes.setFundCost(fundCost);
			 * performanceReportRes.setProfitAmount(getProfitAmount(performanceReportRes));
			 * 
			 * if
			 * (performanceReportRes.getProfitAmount().compareTo(BigDecimal.ZERO)
			 * == 0) { performanceReportRes.setProfitRate(BigDecimal.ZERO); }
			 * else if
			 * (performanceReportRes.getProfitAmount().compareTo(BigDecimal.ZERO)
			 * < 0) { performanceReportRes.setProfitRate(new BigDecimal("-1"));
			 * } else if
			 * (performanceReportRes.getProfitAmount().compareTo(BigDecimal.ZERO)
			 * > 0) { performanceReportRes.setProfitRate(new BigDecimal("1")); }
			 * }
			 **/
		}

		performanceReportRes.setSaleAmount(DecimalUtil.formatScale2(performanceReportRes.getSaleAmount()));
		performanceReportRes.setServiceAmount(DecimalUtil.formatScale2(performanceReportRes.getServiceAmount()));
		performanceReportRes
				.setCompositeTaxAmount(DecimalUtil.formatScale2(performanceReportRes.getCompositeTaxAmount()));
		performanceReportRes.setPurchaseCost(DecimalUtil.formatScale2(performanceReportRes.getPurchaseCost()));
		performanceReportRes.setFundCost(DecimalUtil.formatScale2(performanceReportRes.getFundCost()));
		performanceReportRes.setWarehouseAmount(DecimalUtil.formatScale2(performanceReportRes.getWarehouseAmount()));
		performanceReportRes.setManageAmount(DecimalUtil.formatScale2(performanceReportRes.getManageAmount()));
		performanceReportRes.setMarketAmount(DecimalUtil.formatScale2(performanceReportRes.getMarketAmount()));
		performanceReportRes.setFinanceAmount(DecimalUtil.formatScale2(performanceReportRes.getFinanceAmount()));
		performanceReportRes.setManualAmount(DecimalUtil.formatScale2(performanceReportRes.getManualAmount()));
		performanceReportRes.setProfitAmount(DecimalUtil.formatScale2(performanceReportRes.getProfitAmount()));
		performanceReportRes.setProfitRate(performanceReportRes.getProfitRate().setScale(4, BigDecimal.ROUND_HALF_UP));
		performanceReportRes.setProfitRateStr(DecimalUtil.toPercentString(performanceReportRes.getProfitRate()));

		if (null != performanceReportRes.getCurrencyType()) { // 币种转化成人民币
			performanceReportRes.setCurrencyType(BaseConsts.ONE);
			performanceReportRes.setCurrencyTypeName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, String.valueOf(BaseConsts.ONE))); // 1-人民币
		}
		return performanceReportRes;
	}

	public BigDecimal getProfitAmount(PerformanceReport performanceReportRes) {
		BigDecimal profitAmount = DecimalUtil.subtract(
				DecimalUtil.add(performanceReportRes.getSaleAmount(),
						performanceReportRes.getServiceAmount()),
				DecimalUtil.add(
						DecimalUtil.add(
								DecimalUtil.add(
										DecimalUtil.add(
												DecimalUtil.add(
														DecimalUtil.add(DecimalUtil.add(
																performanceReportRes.getCompositeTaxAmount(),
																performanceReportRes.getPurchaseCost())),
														performanceReportRes.getFundCost()),
												performanceReportRes.getWarehouseAmount()),
										performanceReportRes.getManageAmount()),
								performanceReportRes.getMarketAmount()),
						performanceReportRes.getFinanceAmount()),
				performanceReportRes.getManualAmount());
		return profitAmount;
	}

	public BigDecimal getFundCost(PerformanceReport performanceReportRes,
			PerformanceReportReqDto performanceReportReqDto) {
		Date startDate = getStartDate(performanceReportReqDto.getStartIssue());
		Date endDate = getEndDate(performanceReportReqDto.getEndIssue());
		FundReportSearchReqDto reqDto = new FundReportSearchReqDto();
		reqDto.setUserId(performanceReportReqDto.getUserId());
		reqDto.setProjectId(performanceReportRes.getProjectId());
		reqDto.setCurrencyType(performanceReportRes.getCurrencyType());
		reqDto.setStartDate(startDate);

		BigDecimal startBalance = DecimalUtil.formatScale2(fundReportDao.queryBeginBalanceByCon(reqDto));
		performanceReportRes.setBeginBalance(startBalance); // 查询期初余额
		performanceReportRes.setBalance(DecimalUtil
				.formatScale2(performanceReportRes.getBeginBalance().add(performanceReportRes.getCurBalance())));
		// 计算资金成本

		FundReportSearchReqDto reqDto2 = new FundReportSearchReqDto();
		reqDto2.setUserId(performanceReportReqDto.getUserId());
		reqDto2.setProjectId(performanceReportRes.getProjectId());
		reqDto2.setCurrencyType(performanceReportRes.getCurrencyType());
		reqDto2.setStartDate(startDate);
		reqDto2.setEndDate(endDate);

		List<FundDtlReport> fundDtlReports = fundDtlReportDao.queryFundDtlsGroupByDate(reqDto2);

		BigDecimal fundCost = BigDecimal.ZERO;
		try {
			for (int i = 0; i < fundDtlReports.size(); i++) {
				FundDtlReport fundDtlReport = fundDtlReports.get(i);
				if (DateFormatUtils.diffDate(fundDtlReport.getBillDate(), startDate) == 0) {
					startBalance = startBalance.add(fundDtlReport.getCurBalance());
				} else {
					i--;
				}
				if (DecimalUtil.gt(startBalance, DecimalUtil.ZERO)) { // 余额为负数不计资金成本
					fundCost = fundCost.add(DecimalUtil.formatScale2(startBalance.multiply(BaseConsts.getFeeRate())));
				}
				startDate = DateFormatUtils.beforeDay(startDate, -1);
			}
			long diffDays = DateFormatUtils.diffDate(endDate, startDate);
			if (DecimalUtil.gt(startBalance, DecimalUtil.ZERO)) {
				for (int i = 0; i <= diffDays; i++) {
					fundCost = fundCost.add(DecimalUtil.formatScale2(startBalance.multiply(BaseConsts.getFeeRate())));
				}
			}
		} catch (ParseException e) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "日期解析错误");
		}

		return DecimalUtil.multiply(fundCost, performanceReportRes.getExchangeRate());
	}

	private Date getStartDate(String issue) {
		int year = Integer.valueOf(StringUtils.substring(issue, 0, 4));
		int month = Integer.valueOf(StringUtils.substring(issue, 4, 6));
		return DateFormatUtils.getFirstDateOfMonth(year, month);
	}

	private Date getEndDate(String issue) {
		int year = Integer.valueOf(StringUtils.substring(issue, 0, 4));
		int month = Integer.valueOf(StringUtils.substring(issue, 4, 6));
		return DateFormatUtils.getEndDateOfMonth(year, month);
	}

	public int queryPerformanceReportCount(PerformanceReportReqDto performanceReportReqDto) {
		return performanceReportDao.queryPerformanceReportCount(performanceReportReqDto);
	}

	public int deletePerformanceReport(PerformanceReportReqDto performanceReportReqDto) {
		return performanceReportDao.deletePerformanceReport(performanceReportReqDto);
	}

	public void insert(PerformanceReport performanceReport) {
		performanceReportDao.insert(performanceReport);
	}

	public boolean isOverPerformanceReportMaxLine(PerformanceReportReqDto performanceReportReqDto) {
		performanceReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SEVEN));
		performanceReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = performanceReportDao.queryPerformanceReportCountByCon(performanceReportReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("绩效报表导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncPerformanceReportExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/report/performance/performance_report_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_29);
			asyncExcelService.addAsyncExcel(performanceReportReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncPerformanceReportExport(PerformanceReportReqDto performanceReportReqDto) {
		performanceReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SEVEN));
		Map<String, Object> model = Maps.newHashMap();
		List<PerformanceReport> performanceReportList = queryAllResultsByCon(performanceReportReqDto);
		model.put("performanceReportList", performanceReportList);
		return model;
	}
}
