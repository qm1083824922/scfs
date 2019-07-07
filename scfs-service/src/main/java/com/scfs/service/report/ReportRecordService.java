package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.report.FundTotalReportDao;
import com.scfs.dao.report.ProfitReportMonthDao;
import com.scfs.dao.report.ReportRecordDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.report.entity.FundTotalReport;
import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.entity.OrderProfitReport;
import com.scfs.domain.report.entity.PerformanceReport;
import com.scfs.domain.report.entity.ProfitReport;
import com.scfs.domain.report.entity.ReportRecord;
import com.scfs.domain.report.req.FundReportSearchReqDto;
import com.scfs.domain.report.req.OrderProfitReportReqDto;
import com.scfs.domain.report.req.PerformanceReportReqDto;
import com.scfs.domain.report.req.ProfitReportReqDto;
import com.scfs.domain.report.req.ProfitReportReqMonthDto;
import com.scfs.domain.report.req.ReportRecordReqDto;
import com.scfs.domain.report.resp.FundReportResDto;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2017年4月13日.
 */
@Service
public class ReportRecordService {
	private final static Logger LOGGER = LoggerFactory.getLogger(ReportRecordService.class);

	@Autowired
	private ReportRecordDao reportRecordDao;
	@Autowired
	private PerformanceReportService performanceReportService;
	@Autowired
	private FundReportService fundReportService;
	@Autowired
	private ProfitReportService profitReportService;
	@Autowired
	private OrderProfitReportService orderProfitReportService;
	@Autowired
	private FundTotalReportDao fundTotalReportDao;
	@Autowired
	private ProfitReportMonthDao profitReportMonthDao;
	@Autowired
	private CapitalTurnoverService capitalTurnoverService;
	@Autowired
	private CacheService cacheService;

	/**
	 * 处理报表
	 * 
	 * @param reportType
	 *            报表类型
	 * @param isSchedule
	 *            是否调度任务
	 * @param isCurrentMonth
	 *            是否当月
	 */
	public void dealReport(Integer reportType, boolean isSchedule, boolean isCurrentMonth) {
		ReportRecord reportRecord = getReport(reportType, isSchedule, isCurrentMonth);
		dealReportRecord(reportRecord, false, isSchedule, isCurrentMonth);
	}

	public ReportRecord getReport(Integer reportType, boolean isSchedule, boolean isCurrentMonth) {
		Date currDate = new Date();
		String issue = null;
		if (reportType.equals(BaseConsts.TWO) && isSchedule) { // 2-利润报表且调度任务
			issue = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		} else if (reportType.equals(BaseConsts.THREE) && isSchedule && isCurrentMonth) { // 3-资金报表且调度任务且当月
			issue = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		} else if (reportType.equals(BaseConsts.FOUR) && isSchedule) { // 4-利润月报表且调度任务
			issue = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		} else {
			issue = DateFormatUtils.format(DateFormatUtils.YYYYMM, DateFormatUtils.getPreMonthDate(currDate));
		}
		ReportRecordReqDto reportRecordReqDto = new ReportRecordReqDto();
		reportRecordReqDto.setIssue(issue);
		reportRecordReqDto.setReportType(reportType);
		int count = reportRecordDao.queryReportRecordCount(reportRecordReqDto);
		ReportRecord reportRecord = new ReportRecord();
		if (count <= 0) {
			reportRecord.setReportType(reportType);
			reportRecord.setIssue(issue);
			reportRecord.setIsSuccess(BaseConsts.ZERO);
			reportRecord.setCreatorId(ServiceSupport.getUser().getId());
			reportRecord.setCreator(ServiceSupport.getUser().getChineseName());
			reportRecordDao.insert(reportRecord);
		} else {
			reportRecord = reportRecordDao.queryReportRecordByIssue(reportRecordReqDto.getReportType(),
					reportRecordReqDto.getIssue());
		}
		return reportRecord;
	}

	/**
	 * 处理记录表
	 * 
	 * @param reportRecord
	 *            记录表
	 * @param forceExec
	 *            是否强制执行
	 * @param isSchedule
	 *            是否任务
	 * @param isCurrentMonth
	 *            是否当月
	 */
	public void dealReportRecord(ReportRecord reportRecord, boolean forceExec, boolean isSchedule,
			boolean isCurrentMonth) {
		if (reportRecord.getReportType().equals(BaseConsts.ONE)
				&& (reportRecord.getIsSuccess().equals(BaseConsts.ZERO) || forceExec == true)) { // 1-绩效报表
			invokePerformanceReport(reportRecord, isSchedule);
		} else if (reportRecord.getReportType().equals(BaseConsts.TWO)) { // 2-利润报表
			invokeProfitReport(reportRecord, isSchedule);
		} else if (reportRecord.getReportType().equals(BaseConsts.THREE) || forceExec == true) { // 2-资金报表
			invokeFundReport(reportRecord, isSchedule, isCurrentMonth);
		} else if (reportRecord.getReportType().equals(BaseConsts.FOUR)) { // 4-利润月报表
			invokeProfitReportMounth(reportRecord, isSchedule);
		} else if (reportRecord.getReportType().equals(BaseConsts.FIVE)) { // 5-资金周转率
			invokeCapitalTurnover(reportRecord, isSchedule);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "当前期号报表已生成");
		}
	}

	public void invokePerformanceReport(ReportRecord reportRecord, boolean isSchedule) {
		Integer reportRecordId = reportRecord.getId();
		String issue = reportRecord.getIssue();
		PerformanceReportReqDto performanceReportReqDto = new PerformanceReportReqDto();
		performanceReportReqDto.setReportRecordId(reportRecordId);
		performanceReportReqDto.setIssue(issue);
		int number = performanceReportService.queryPerformanceReportCount(performanceReportReqDto);
		if (number > 0) {
			performanceReportService.deletePerformanceReport(performanceReportReqDto);
		}
		try {
			performanceReportReqDto.setPeriodType(BaseConsts.ONE);
			performanceReportReqDto.setStartIssue(issue);
			performanceReportReqDto.setEndIssue(issue);
			List<PerformanceReport> performanceReportList = performanceReportService
					.queryAllResultsByCon4Admin(performanceReportReqDto);
			if (!CollectionUtils.isEmpty(performanceReportList)) {
				for (PerformanceReport performanceReport : performanceReportList) {
					performanceReport.setId(null);
					if (performanceReport.getProjectId() != null) {
						performanceReport.setBusinessUnitId(
								cacheService.getProjectById(performanceReport.getProjectId()).getBusinessUnitId());
					}
					performanceReport.setReportRecordId(reportRecord.getId());
					performanceReport.setIssue(reportRecord.getIssue());
					performanceReport.setCreatorId(ServiceSupport.getUser().getId());
					performanceReport.setCreator(ServiceSupport.getUser().getChineseName());
					performanceReportService.insert(performanceReport);
				}
			}
			reportRecord.setIsSuccess(BaseConsts.ONE);
			reportRecord.setMsg(StringUtils.EMPTY);
			reportRecordDao.updateById(reportRecord);
		} catch (Exception e) {
			LOGGER.error("生成绩效报表报错：", e);
			reportRecord.setIsSuccess(BaseConsts.ZERO);
			reportRecord.setMsg(getExceptionMsg(e));
			reportRecordDao.updateById(reportRecord);
			int count = performanceReportService.queryPerformanceReportCount(performanceReportReqDto);
			if (count > 0) {
				performanceReportService.deletePerformanceReport(performanceReportReqDto);
			}
		}
	}

	public void invokeProfitReport(ReportRecord reportRecord, boolean isSchedule) {
		Date currDate = new Date();
		String issue = reportRecord.getIssue();
		Integer reportRecordId = reportRecord.getId();

		OrderProfitReportReqDto orderProfitReportReqDto = new OrderProfitReportReqDto();
		String startStatisticsDate = null;
		String endStatisticsDate = null;
		String startCreateAt = null;
		String endCreateAt = null;
		String currIssue = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate); // 当前时间期号
		Integer searchProfitReport = 1; // 查询资金成本
		if (isSchedule == true) { // 调度任务，统计时间取前一天
			String preDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, DateFormatUtils.beforeDay(currDate, 1));
			startCreateAt = preDate;
			endCreateAt = preDate;
			String preIssue = DateFormatUtils.format(DateFormatUtils.YYYYMM, DateFormatUtils.beforeDay(currDate, 1));
			if (currIssue.equals(preIssue)) {
				searchProfitReport = 0;
			}
		} else { // 非调度任务，统计时间取指定期号的起止时间
			int year = Integer.valueOf(StringUtils.substring(issue, 0, 4));
			int month = Integer.valueOf(StringUtils.substring(issue, 4, 6));
			startStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
					DateFormatUtils.getFirstDateOfMonth(year, month));

			if (currIssue.equals(issue)) { // 如果是当月
				searchProfitReport = 0;
				// 结束时间取当前日期的前一天
				endStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
						DateFormatUtils.beforeDay(DateFormatUtils.getEndDateOfMonth(year, month), 1));
			} else {
				endStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
						DateFormatUtils.getEndDateOfMonth(year, month));
			}
		}

		ProfitReportReqDto profitReportReqDto = new ProfitReportReqDto();
		if (isSchedule == false) {
			profitReportReqDto.setStartStatisticsDate(startStatisticsDate);
			profitReportReqDto.setEndStatisticsDate(endStatisticsDate);
			profitReportReqDto.setReportRecordId(reportRecordId);
			// 删除指定统计日期的所有记录
			int number = profitReportService.queryProfitReportCount(profitReportReqDto);
			if (number > 0) {
				profitReportService.deleteProfitReport(profitReportReqDto);
			}
		}

		try {
			orderProfitReportReqDto.setSearchProfitReport(searchProfitReport); // 设置是否查询资金成本
			if (isSchedule == true) {
				orderProfitReportReqDto.setStartStatisticsDate(startCreateAt);
				orderProfitReportReqDto.setEndStatisticsDate(endCreateAt);
				orderProfitReportReqDto.setStartSystemDeliverTime(startCreateAt); // 开始系统出库时间
				orderProfitReportReqDto.setEndSystemDeliverTime(endCreateAt); // 结束系统出库时间
				orderProfitReportReqDto.setStartFeeManageCreateAt(startCreateAt); // 开始管理费用创建时间
				orderProfitReportReqDto.setEndFeeManageCreateAt(endCreateAt); // 结束管理费用创建时间
				orderProfitReportReqDto.setIsSchedule(BaseConsts.ONE);
			} else {
				orderProfitReportReqDto.setStartStatisticsDate(startStatisticsDate);
				orderProfitReportReqDto.setEndStatisticsDate(endStatisticsDate);
				orderProfitReportReqDto.setIsSchedule(BaseConsts.ZERO);
			}
			List<OrderProfitReport> orderProfitReportList = orderProfitReportService
					.queryAllResultsByCon4Admin(orderProfitReportReqDto);

			String preIssue = DateFormatUtils.format(DateFormatUtils.YYYYMM, DateFormatUtils.beforeDay(currDate, 1));
			if (!CollectionUtils.isEmpty(orderProfitReportList)) {
				for (OrderProfitReport orderProfitReport : orderProfitReportList) {
					ProfitReport profitReport = new ProfitReport();
					BeanUtils.copyProperties(orderProfitReport, profitReport);
					profitReport.setId(null);
					profitReport.setReportRecordId(reportRecordId);
					/**
					 * if (isSchedule == true) { // 解决跨月期号为当月的问题
					 * profitReport.setIssue(preIssue); } else {
					 * profitReport.setIssue(issue); }
					 **/
					if (profitReport.getStatisticsDate() != null) {
						profitReport.setIssue(
								DateFormatUtils.format(DateFormatUtils.YYYYMM, profitReport.getStatisticsDate()));
					} else {
						if (isSchedule == true) { // 解决跨月期号为当月的问题
							profitReport.setIssue(preIssue);
						} else {
							profitReport.setIssue(issue);
						}
					}
					profitReport = getProfitReportByOrderProfitReport(profitReport, orderProfitReport);
					profitReportService.insert(profitReport);
				}
			}
			reportRecord.setIsSuccess(BaseConsts.ONE);
			reportRecord.setMsg(StringUtils.EMPTY);
			reportRecordDao.updateById(reportRecord);
		} catch (Exception e) {
			LOGGER.error("生成利润报表报错：", e);
			reportRecord.setIsSuccess(BaseConsts.ZERO);
			reportRecord.setMsg(getExceptionMsg(e));
			reportRecordDao.updateById(reportRecord);
			int count = profitReportService.queryProfitReportCount(profitReportReqDto);
			if (count > 0) {
				profitReportService.deleteProfitReport(profitReportReqDto);
			}
		}
	}

	public PerformanceReport getPerformanceReportByFundReport(PerformanceReport performanceReport,
			FundReportResDto fundReportResDto, Map<Integer, BigDecimal> currencyRateMap, Date endDate) {
		BigDecimal exchangeRate = currencyRateMap.get(fundReportResDto.getCurrencyType());
		performanceReport.setProjectId(fundReportResDto.getProjectId());
		performanceReport.setDepartmentId(fundReportResDto.getDepartmentId());
		// performanceReport.setCustomerId(fundReportResDto.getCustomerId());
		performanceReport.setCustomerId(null); // 资金成本不显示客户
		performanceReport.setBizManagerId(fundReportResDto.getBizManagerId());
		performanceReport.setWarehouseId(null);
		performanceReport.setBusinessUnitId(fundReportResDto.getBusinessUnitId());
		performanceReport.setBizType(fundReportResDto.getBizType());
		performanceReport.setBillType(BaseConsts.FIVE);
		performanceReport.setOrderNo(null);
		performanceReport.setBillNo(null);
		performanceReport.setProductNo(BaseConsts.FUND_PRODUCT_NO);
		performanceReport.setNum(BigDecimal.ZERO);
		performanceReport.setPlaceDate(endDate);
		performanceReport.setStatisticsDate(endDate);
		performanceReport.setSaleAmount(BigDecimal.ZERO);
		performanceReport.setServiceAmount(BigDecimal.ZERO);
		performanceReport.setCompositeTaxAmount(BigDecimal.ZERO);
		performanceReport.setPurchaseCost(BigDecimal.ZERO);
		performanceReport.setFundCost(DecimalUtil.multiply(fundReportResDto.getFundCost(), exchangeRate));
		performanceReport.setWarehouseAmount(BigDecimal.ZERO);
		performanceReport.setManageAmount(BigDecimal.ZERO);
		performanceReport.setMarketAmount(BigDecimal.ZERO);
		performanceReport.setFinanceAmount(BigDecimal.ZERO);
		performanceReport.setManualAmount(BigDecimal.ZERO);
		performanceReport.setProfitAmount(getProfitAmount(performanceReport));
		BigDecimal incomeAmount = DecimalUtil.subtract(
				DecimalUtil.add(performanceReport.getSaleAmount(), performanceReport.getServiceAmount()),
				performanceReport.getCompositeTaxAmount());
		if (performanceReport.getBillType().equals(BaseConsts.FIVE)) {
			if (performanceReport.getProfitAmount().compareTo(BigDecimal.ZERO) == 0) {
				performanceReport.setProfitRate(BigDecimal.ZERO);
			} else if (performanceReport.getProfitAmount().compareTo(BigDecimal.ZERO) < 0) {
				performanceReport.setProfitRate(new BigDecimal("-1"));
			} else if (performanceReport.getProfitAmount().compareTo(BigDecimal.ZERO) > 0) {
				performanceReport.setProfitRate(new BigDecimal("1"));
			}
		} else {
			if (incomeAmount.compareTo(BigDecimal.ZERO) != 0) {
				performanceReport.setProfitRate(DecimalUtil.divide(performanceReport.getProfitAmount(), incomeAmount));
			}
		}
		performanceReport.setProfitRateStr(DecimalUtil.toPercentString(performanceReport.getProfitRate()));
		performanceReport.setCurrencyType(BaseConsts.ONE);
		performanceReport.setExchangeRate(exchangeRate);

		performanceReport.setCreatorId(ServiceSupport.getUser().getId());
		performanceReport.setCreator(ServiceSupport.getUser().getChineseName());
		return performanceReport;
	}

	public BigDecimal getProfitAmount(PerformanceReport performanceReport) {
		BigDecimal profitAmount = DecimalUtil
				.subtract(
						DecimalUtil.add(performanceReport.getSaleAmount(),
								performanceReport.getServiceAmount()),
						DecimalUtil.add(
								DecimalUtil.add(
										DecimalUtil.add(
												DecimalUtil.add(
														DecimalUtil.add(
																DecimalUtil.add(DecimalUtil.add(
																		performanceReport.getCompositeTaxAmount(),
																		performanceReport.getPurchaseCost())),
																performanceReport.getFundCost()),
														performanceReport.getWarehouseAmount()),
												performanceReport.getManageAmount()),
										performanceReport.getMarketAmount()),
								performanceReport.getFinanceAmount()),
						performanceReport.getManualAmount());
		return profitAmount;
	}

	public ProfitReport getProfitReportByOrderProfitReport(ProfitReport profitReport,
			OrderProfitReport orderProfitReport) {
		profitReport.setBillId(orderProfitReport.getId());
		profitReport.setCurrencyName(orderProfitReport.getCurrencyTypeName());
		profitReport.setCreatorId(ServiceSupport.getUser().getId());
		profitReport.setCreator(ServiceSupport.getUser().getChineseName());
		return profitReport;
	}

	private String getExceptionMsg(Exception e) {
		String exceptionMsg = e.getMessage();
		if (StringUtils.isNotBlank(exceptionMsg)) {
			exceptionMsg = exceptionMsg.substring(0, exceptionMsg.length() > 500 ? 500 : exceptionMsg.length());
		} else {
			exceptionMsg = "";
		}
		return exceptionMsg;
	}

	@IgnoreTransactionalMark
	public void dealPerformanceReport(boolean isSchedule) {
		dealReport(BaseConsts.ONE, isSchedule, false);
	}

	@IgnoreTransactionalMark
	public void dealProfitReport(boolean isSchedule) {
		dealReport(BaseConsts.TWO, isSchedule, false);
	}

	@IgnoreTransactionalMark
	public void dealFundReport(boolean isSchedule) {
		dealReport(BaseConsts.THREE, isSchedule, false);
	}

	@IgnoreTransactionalMark
	public void dealProfitReportMounth(boolean isSchedule) {
		dealReport(BaseConsts.FOUR, isSchedule, false);
	}

	@IgnoreTransactionalMark
	public void dealCapitalTurnover(boolean isSchedule) {
		dealReport(BaseConsts.FIVE, isSchedule, false);
	}

	@IgnoreTransactionalMark
	public void dealCurrentMonthFundReport(boolean isSchedule) {
		dealReport(BaseConsts.THREE, isSchedule, true);
	}

	@IgnoreTransactionalMark
	public void reExecReport(ReportRecordReqDto reportRecordReqDto) throws Exception {
		if (reportRecordReqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "参数不能为空");
		}
		if (reportRecordReqDto.getReportType() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请选择报表类型");
		}
		if (StringUtils.isBlank(reportRecordReqDto.getStartIssue())
				|| StringUtils.isBlank(reportRecordReqDto.getEndIssue())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "起止期号不能为空");
		}
		if (!matchIssue(reportRecordReqDto.getStartIssue())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请输入正确的起始期号");
		}
		if (!matchIssue(reportRecordReqDto.getEndIssue())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请输入正确的截止期号");
		}
		if (reportRecordReqDto.getEndIssue().compareTo(reportRecordReqDto.getStartIssue()) < 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "起始期号必须小于等于截止期号");
		}

		List<ReportRecord> reportRecordList = reportRecordDao.queryResultsByCon(reportRecordReqDto);
		if (!CollectionUtils.isEmpty(reportRecordList)) {
			for (ReportRecord reportRecord : reportRecordList) {
				dealReportRecord(reportRecord, reportRecordReqDto.isForceExec(), false,
						reportRecordReqDto.isCurrentMonth());
			}
		}
	}

	/**
	 * 期号格式校验
	 * 
	 * @param issue
	 * @return
	 */
	public boolean matchIssue(String issue) {
		Pattern p = Pattern.compile("^\\d{4}(0[123456789]|1[012])$");
		Matcher m = p.matcher(issue);
		boolean b = m.matches();
		if (b) {
			return true;
		} else {
			return false;
		}
	}

	// 资金报表定时统计
	public void invokeFundReport(ReportRecord reportRecord, boolean isSchedule, boolean isCurrentMonth) {
		Date currDate = new Date();
		FundReportSearchReqDto fundReportSearchReqDto = new FundReportSearchReqDto();
		FundReportSearchReqDto reqDto = new FundReportSearchReqDto();
		fundReportSearchReqDto.setReportRecordId(reportRecord.getId());
		reqDto.setReportRecordId(reportRecord.getId());
		fundReportSearchReqDto.setIssue(reportRecord.getIssue());
		reqDto.setIssue(reportRecord.getIssue());
		// 删除指定统计日期的所有记录
		int number = fundReportService.queryFundReportCount(fundReportSearchReqDto);
		if (number > BaseConsts.ZERO) {
			fundReportService.deleteFundReport(fundReportSearchReqDto);
		}

		try {
			List<FundTotalReport> result = new ArrayList<FundTotalReport>();
			List<FundTotalReport> result1 = new ArrayList<FundTotalReport>();
			int year = Integer.valueOf(StringUtils.substring(reportRecord.getIssue(), 0, 4));
			int month = Integer.valueOf(StringUtils.substring(reportRecord.getIssue(), 4, 6));
			Date startDate = DateFormatUtils.getFirstDateOfMonth(year, month);
			Date endDate = DateFormatUtils.getEndDateOfMonth(year, month);

			// 统计期号是当月，且需要统计当月数据，则起止时间为1号至当前系统时间
			String currIssue = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
			if (currIssue.equals(reportRecord.getIssue()) && isCurrentMonth) {
				startDate = DateFormatUtils.getFirstDateOfMonth(year, month);
				endDate = currDate;
			}
			reqDto.setStartDate(startDate);
			reqDto.setEndDate(endDate);
			fundReportSearchReqDto.setStartDate(startDate);
			fundReportSearchReqDto.setEndDate(endDate);
			fundReportSearchReqDto.setStatisticsDimension(BaseConsts.THREE);
			List<FundReportResDto> list = fundReportService.refreshFundJob(fundReportSearchReqDto);
			if (!CollectionUtils.isEmpty(list)) {
				for (FundReportResDto fund : list) {
					FundTotalReport fundTotalReport = new FundTotalReport();
					BeanUtils.copyProperties(fund, fundTotalReport);
					fundTotalReport.setReportRecordId(reportRecord.getId());
					Integer busiUnit = fund.getBusinessUnitId();
					if (busiUnit == null) {
						busiUnit = BaseConsts.ZERO;
					}
					fundTotalReport.setBusiUnit(busiUnit);
					fundTotalReport.setIssue(reportRecord.getIssue());
					fundTotalReport.setCreateAt(new Date());
					fundTotalReport.setCreator(ServiceSupport.getUser().getChineseName());
					fundTotalReport.setCreatorId(ServiceSupport.getUser().getId());
					fundTotalReport.setCurrencyName(ServiceSupport
							.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, fund.getCurrencyType() + ""));
					result.add(fundTotalReport);
				}
				fundTotalReportDao.batchInsert(result);
			}
			reqDto.setCapitalAccountType(BaseConsts.TWO);
			reqDto.setStatisticsDimension(BaseConsts.TWO);
			List<FundReportResDto> list2 = fundReportService.refreshFundJob(reqDto);
			if (!CollectionUtils.isEmpty(list2)) {
				for (FundReportResDto fund : list2) {
					FundTotalReport fundTotalReport = new FundTotalReport();
					BeanUtils.copyProperties(fund, fundTotalReport);
					fundTotalReport.setReportRecordId(reportRecord.getId());
					fundTotalReport.setIssue(reportRecord.getIssue());
					Integer busiUnit = fund.getBusinessUnitId();
					if (busiUnit == null) {
						busiUnit = BaseConsts.ZERO;
					}
					fundTotalReport.setBusiUnit(busiUnit);
					fundTotalReport.setProjectId(null);
					fundTotalReport.setProjectName(null);
					fundTotalReport.setCreateAt(new Date());
					fundTotalReport.setCreator(ServiceSupport.getUser().getChineseName());
					fundTotalReport.setCreatorId(ServiceSupport.getUser().getId());
					fundTotalReport.setCurrencyName(ServiceSupport
							.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, fund.getCurrencyType() + ""));
					result1.add(fundTotalReport);
				}
				fundTotalReportDao.batchInsert(result1);
			}
			reportRecord.setIsSuccess(BaseConsts.ONE);
			reportRecord.setMsg(StringUtils.EMPTY);
			reportRecordDao.updateById(reportRecord);
		} catch (Exception e) {
			LOGGER.error("生成资金统计报表报错：", e);
			reportRecord.setIsSuccess(BaseConsts.ZERO);
			reportRecord.setMsg(getExceptionMsg(e));
			reportRecordDao.updateById(reportRecord);
			int count = fundReportService.queryFundReportCount(fundReportSearchReqDto);
			if (count > 0) {
				fundReportService.deleteFundReport(fundReportSearchReqDto);
			}
		}
	}

	/**
	 * 月结报表相关
	 * 
	 * @param reportRecord
	 * @param isSchedule
	 */
	public void invokeProfitReportMounth(ReportRecord reportRecord, boolean isSchedule) {
		Date currDate = new Date();
		String issue = reportRecord.getIssue();

		String startStatisticsDate = null;
		String endStatisticsDate = null;
		if (isSchedule == true) { // 调度任务，统计当月的
			String preDate = "";
			Integer day = DateFormatUtils.getDateDay(currDate);
			if (day == BaseConsts.ONE) {// 当月1号时处理上月数据
				preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, DateFormatUtils.getPreMonthDate(currDate));
			} else {
				preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
			}
			startStatisticsDate = preDate;
			endStatisticsDate = preDate;
			issue = preDate;
		} else { // 非调度任务，统计时间取指定期号的起止时间
			startStatisticsDate = issue;
			endStatisticsDate = issue;
		}
		ProfitReportReqMonthDto profitReportReqMounthDto = new ProfitReportReqMonthDto();
		profitReportReqMounthDto.setStartStatisticsDate(startStatisticsDate);
		profitReportReqMounthDto.setEndStatisticsDate(endStatisticsDate);
		// 删除指定统计日期的所有记录
		int number = profitReportMonthDao.queryProfitReportMounthCount(profitReportReqMounthDto);
		if (number > 0) {
			profitReportMonthDao.deleteReportMounth(profitReportReqMounthDto);
		}
		try {
			OrderProfitReportReqDto profitReportDto = new OrderProfitReportReqDto();
			profitReportDto.setStartStatisticsDate(startStatisticsDate);
			profitReportDto.setEndStatisticsDate(endStatisticsDate);
			List<MounthProfitReport> mounthLists = orderProfitReportService.queryMounthResults(profitReportDto);
			if (!CollectionUtils.isEmpty(mounthLists)) {
				for (MounthProfitReport mounth : mounthLists) {
					MounthProfitReport mounthReportRecord = new MounthProfitReport();
					BeanUtils.copyProperties(mounth, mounthReportRecord);
					mounthReportRecord.setId(null);
					mounthReportRecord.setIssue(issue);
					mounthReportRecord.setCreatorId(ServiceSupport.getUser().getId());
					profitReportMonthDao.insert(mounthReportRecord);
				}
			}
			reportRecord.setIsSuccess(BaseConsts.ONE);
			reportRecord.setMsg(StringUtils.EMPTY);
			reportRecordDao.updateById(reportRecord);
		} catch (Exception e) {
			LOGGER.error("生成月利润报表报错：", e);
			reportRecord.setIsSuccess(BaseConsts.ZERO);
			reportRecord.setMsg(getExceptionMsg(e));
			reportRecordDao.updateById(reportRecord);
			int count = profitReportMonthDao.queryProfitReportMounthCount(profitReportReqMounthDto);
			if (count > 0) {
				profitReportMonthDao.deleteReportMounth(profitReportReqMounthDto);
			}
		}
	}

	/**
	 * 资金周转相关
	 * 
	 * @param reportRecord
	 * @param isSchedule
	 */
	public void invokeCapitalTurnover(ReportRecord reportRecord, boolean isSchedule) {
		String issue = "";
		if (isSchedule) {
			issue = DateFormatUtils.format(DateFormatUtils.YYYYMM, DateFormatUtils.getPreMonthDate(new Date()));// 获取上个月
		} else {
			issue = reportRecord.getIssue();
		}
		try {
			capitalTurnoverService.dealReport(issue);
			reportRecord.setIsSuccess(BaseConsts.ONE);
			reportRecord.setMsg(StringUtils.EMPTY);
			reportRecordDao.updateById(reportRecord);
		} catch (Exception e) {
			LOGGER.error("生成资金周转报表报错：", e);
			reportRecord.setIsSuccess(BaseConsts.ZERO);
			reportRecord.setMsg(getExceptionMsg(e));
			reportRecordDao.updateById(reportRecord);
		}
	}
}
