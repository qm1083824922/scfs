package com.scfs.service.report;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DateRange;
import com.scfs.common.utils.DateUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.report.FundReportDao;
import com.scfs.dao.report.ProfitReportMonthDao;
import com.scfs.dao.report.ProfitTargetDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.report.entity.FundReport;
import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.entity.ProfitTarget;
import com.scfs.domain.report.req.FundReportSearchReqDto;
import com.scfs.domain.report.req.ProfitReportReqMonthDto;
import com.scfs.domain.report.req.ProfitTargetReqDto;
import com.scfs.domain.report.resp.MounthProfitReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.CommonService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *
 *  File: ProfitReportMonthService.java
 *  Description: 利润月度报表相关处理
 *  TODO
 *  Date,                   Who,
 *  2017年06月06日         Administrator
 *
 * </pre>
 */
@Service
public class ProfitReportMonthService {
	@Autowired
	private ProfitReportMonthDao profitReportMonthDao;
	@Autowired
	private ProfitTargetDao profitTargetDao;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private FundReportDao fundReportDao;// 统计当月资金成本

	/**
	 * 获取当月指标数据
	 * 
	 * @param mounthDto
	 * @return
	 */
	public Result<MounthProfitReport> queryMounthIndex(ProfitReportReqMonthDto mounthDto) {
		Result<MounthProfitReport> result = new Result<MounthProfitReport>();
		Date currDate = new Date();
		String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		String startStatisticsDate = null;
		String endStatisticsDate = null;
		Integer bizType = mounthDto.getBizType();
		String startDate = mounthDto.getStartStatisticsDate();
		String endDate = mounthDto.getEndStatisticsDate();
		if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			startStatisticsDate = startDate;
			endStatisticsDate = endDate;
		} else {
			if (bizType == null || bizType.equals(BaseConsts.ONE)) {// 月
				startStatisticsDate = preDate;
				endStatisticsDate = preDate;
			} else if (bizType.equals(BaseConsts.TWO)) {// 季度
				DateRange currentQuarter = DateUtils.getThisQuarter();
				startStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currentQuarter.getStart());
				endStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currentQuarter.getEnd());
			} else if (bizType.equals(BaseConsts.THREE)) {// 年
				String year = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate).substring(BaseConsts.ZERO,
						BaseConsts.FOUR);
				startStatisticsDate = year + "01";
				endStatisticsDate = year + "12";
			}
		}
		if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_PROFIT_TARGET_POWER)) {// 判断用户是否拥有权限
			mounthDto.setUserId(ServiceSupport.getUser().getId());
		} else {// 获取当前用户为项目下业务专员
			mounthDto.setUserId(ServiceSupport.getUser().getId());
			mounthDto.setBizSpecialId(ServiceSupport.getUser().getId());
		}
		mounthDto.setStartStatisticsDate(startStatisticsDate);
		mounthDto.setEndStatisticsDate(endStatisticsDate);
		MounthProfitReport mounthReport = profitReportMonthDao.queryProfitReportMounthSum(mounthDto);
		if (mounthReport == null) {
			mounthReport = new MounthProfitReport();
		}
		if (startStatisticsDate.equals(preDate) && startStatisticsDate.equals(endStatisticsDate)) {// 当月，获取实时资金成本
			BigDecimal fundCost = BigDecimal.ZERO;
			FundReportSearchReqDto fundReportReqDto = new FundReportSearchReqDto();
			fundReportReqDto.setIssue(preDate);
			fundReportReqDto.setUserId(ServiceSupport.getUser().getId());
			List<FundReport> fundList = fundReportDao.queyrFundReportGroupBy(fundReportReqDto);
			if (!CollectionUtils.isEmpty(fundList)) {
				for (FundReport fund : fundList) {
					fundCost = DecimalUtil.add(fundCost,
							ServiceSupport.amountNewToRMB(
									fund.getFundCost() == null ? DecimalUtil.ZERO : fund.getFundCost(),
									fund.getCurrencyType(), null));
				}
			}
			mounthReport.setFundCost(fundCost);
			mounthReport
					.setProfitAmount(DecimalUtil.subtract(mounthReport.getProfitAmount(), mounthReport.getFundCost()));// 净利润=净利润-资金成本
		}
		ProfitTargetReqDto reqDto = new ProfitTargetReqDto();
		if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_PROFIT_TARGET_POWER)) {// 判断用户是否拥有权限
			List<Integer> userIds = new ArrayList<Integer>();
			Integer departmentId = ServiceSupport.getUser().getDepartmentId();
			List<BaseUser> baseUserList = cacheService.getUsersByDepartmentId(departmentId);// 获取部门下用户
			if (!CollectionUtils.isEmpty(baseUserList)) {
				for (BaseUser baseUser : baseUserList) {
					userIds.add(baseUser.getId());
				}
			}
			if (userIds.size() == BaseConsts.ZERO) {
				userIds.add(BaseConsts.ZERO);
			}
			reqDto.setUserIds(userIds);
		} else {
			reqDto.setUserId(ServiceSupport.getUser().getId());
		}
		reqDto.setStartStatisticsDate(startStatisticsDate);
		reqDto.setEndStatisticsDate(endStatisticsDate);
		ProfitTarget model = profitTargetDao.querySumByCont(reqDto);
		if (model != null) {
			mounthReport.setTargetProfitAmount(model.getTargetProfitAmount());
			mounthReport.setTargetBizManager(model.getTargetBizManager());
			mounthReport.setTargetSaleBlance(model.getTargetSaleBlance());
			mounthReport.setTargetSaleAmount(model.getTargetSaleAmount());
			mounthReport.setTargetManageAmount(model.getTargetManageAmount());
			mounthReport.setTargetWarehouseAmount(model.getTargetWarehouseAmount());
			mounthReport.setTargetFundVost(model.getTargetFundVost());
		}
		if (DecimalUtil.gt(mounthReport.getTargetProfitAmount(), BigDecimal.ZERO)) {
			BigDecimal proportion = DecimalUtil.divide(mounthReport.getProfitAmount(),
					mounthReport.getTargetProfitAmount());
			mounthReport.setProfitAmountPro(DecimalUtil.toPercent(proportion));// 利润百分比
		} else {
			mounthReport.setProfitAmountPro(new BigDecimal(BaseConsts.INT_100));
		}

		if (DecimalUtil.gt(mounthReport.getTargetBizManager(), BigDecimal.ZERO)) {
			BigDecimal proportion = DecimalUtil.divide(mounthReport.getBizManagerAmount(),
					mounthReport.getTargetBizManager());
			mounthReport.setBizManagerPro(DecimalUtil.toPercent(proportion));// 业务利润百分比
		} else {
			mounthReport.setBizManagerPro(new BigDecimal(BaseConsts.INT_100));// 业务利润百分比
		}

		if (DecimalUtil.gt(mounthReport.getTargetSaleBlance(), BigDecimal.ZERO)) {
			BigDecimal proportion = DecimalUtil.divide(mounthReport.getSaleBlanceAmount(),
					mounthReport.getTargetSaleBlance());
			mounthReport.setSaleBlancePro(DecimalUtil.toPercent(proportion));// 销售毛利润百分比
		} else {
			mounthReport.setSaleBlancePro(new BigDecimal(BaseConsts.INT_100));
		}

		if (DecimalUtil.gt(mounthReport.getTargetSaleAmount(), BigDecimal.ZERO)) {
			BigDecimal proportion = DecimalUtil.divide(mounthReport.getSaleAmount(),
					mounthReport.getTargetSaleAmount());
			mounthReport.setSaleAmountPro(DecimalUtil.toPercent(proportion));// 经营收入百分比
		} else {
			mounthReport.setSaleAmountPro(new BigDecimal(BaseConsts.INT_100));
		}

		if (DecimalUtil.gt(mounthReport.getTargetManageAmount(), BigDecimal.ZERO)) {
			BigDecimal proportion = DecimalUtil.divide(mounthReport.getManageAmount(),
					mounthReport.getTargetManageAmount());
			mounthReport.setManageAmountPro(DecimalUtil.toPercent(proportion));// 管理费用百分比
		} else {
			mounthReport.setManageAmountPro(new BigDecimal(BaseConsts.INT_100));
		}

		if (DecimalUtil.gt(mounthReport.getTargetWarehouseAmount(), BigDecimal.ZERO)) {
			BigDecimal proportion = DecimalUtil.divide(mounthReport.getWarehouseAmount(),
					mounthReport.getTargetWarehouseAmount());
			mounthReport.setWarehouseAmountPro(DecimalUtil.toPercent(proportion));// 经营费用百分比
		} else {
			mounthReport.setWarehouseAmountPro(new BigDecimal(BaseConsts.INT_100));
		}

		if (DecimalUtil.gt(mounthReport.getTargetFundVost(), BigDecimal.ZERO)) {
			BigDecimal proportion = DecimalUtil.divide(mounthReport.getFundCost(), mounthReport.getTargetFundVost());
			mounthReport.setFundVostPro(DecimalUtil.toPercent(proportion));// 资金成本百分比
		} else {
			mounthReport.setFundVostPro(new BigDecimal(BaseConsts.INT_100));
		}
		result.setItems(mounthReport);
		return result;
	}

	/**
	 * 利润额相关报表
	 * 
	 * @param mounthDto
	 * @return
	 */
	public List<MounthProfitReport> queryMounthProfit(ProfitReportReqMonthDto mounthDto) {
		Date currDate = new Date();
		String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		String startStatisticsDate = null;
		String endStatisticsDate = null;
		Integer bizType = mounthDto.getBizType();
		String startDate = mounthDto.getStartStatisticsDate();
		String endDate = mounthDto.getEndStatisticsDate();
		if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			startStatisticsDate = startDate;
			endStatisticsDate = endDate;
		} else {
			if (bizType == null || bizType.equals(BaseConsts.ONE)) {// 月
				startStatisticsDate = preDate;
				endStatisticsDate = preDate;
			} else if (bizType.equals(BaseConsts.TWO)) {// 季度
				DateRange currentQuarter = DateUtils.getThisQuarter();
				startStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currentQuarter.getStart());
				endStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currentQuarter.getEnd());
			} else if (bizType.equals(BaseConsts.THREE)) {// 年
				String year = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate).substring(BaseConsts.ZERO,
						BaseConsts.FOUR);
				startStatisticsDate = year + "01";
				endStatisticsDate = year + "12";
			}
		}
		List<CodeValue> codeList = commonService.getAllCdByKey("USER_PROJECT");
		mounthDto.setCodeList(codeList);
		mounthDto.setStartStatisticsDate(startStatisticsDate);
		mounthDto.setEndStatisticsDate(endStatisticsDate);
		List<MounthProfitReport> mounthReport = profitReportMonthDao.queryMounthProfitSum(mounthDto);
		for (MounthProfitReport result : mounthReport) {
			result.setBizTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE, result.getBizType() + ""));
			BigDecimal saleBlanceRate = BigDecimal.ZERO;
			BigDecimal saleBlanceAmount = result.getSaleBlanceAmount();
			BigDecimal saleAmount = result.getSaleAmount();
			if (!DecimalUtil.eq(saleAmount, BigDecimal.ZERO)) {// 计算销售毛利润
				saleBlanceRate = DecimalUtil.divide(saleBlanceAmount, saleAmount);
			}
			result.setSaleBlanceRate(DecimalUtil.toPercent(saleBlanceRate));
		}
		return mounthReport;
	}

	/**
	 * 获取项目近期一年所有数据
	 * 
	 * @param mounthDto
	 * @return
	 */
	public MounthProfitReportResDto queryMounthYearByCon(ProfitReportReqMonthDto mounthDto) {
		MounthProfitReportResDto result = new MounthProfitReportResDto();
		if (mounthDto.getProjectId() == null) {
			List<CodeValue> codeList = commonService.getAllCdByKey("USER_PROJECT");
			if (codeList != null && codeList.size() > BaseConsts.ZERO) {
				mounthDto.setProjectId(Integer.parseInt(codeList.get(BaseConsts.ZERO).getCode()));
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "无项目");
			}
		}
		Date currDate = new Date();
		Date beforDate = DateFormatUtils.getPreYear(currDate);
		String startDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, beforDate);
		String endDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		mounthDto.setStartStatisticsDate(startDate);
		mounthDto.setEndStatisticsDate(endDate);
		List<MounthProfitReport> mounthDatalist = profitReportMonthDao.queryResultsByCon(mounthDto);// 获取一年数据
		List<String> monthList = DateFormatUtils.getBeforeMonths(beforDate, currDate);// 获取一年月份
		if (!CollectionUtils.isEmpty(mounthDatalist)) {// 进行数据处理，按照一年月份处理
			List<MounthProfitReport> dealResult = new ArrayList<MounthProfitReport>();
			for (String month : monthList) {
				month = month.split("-")[0] + month.split("-")[1];
				boolean isEques = false;
				int index = 0;
				for (int i = 0; i < mounthDatalist.size(); i++) {
					if (month.equals(mounthDatalist.get(i).getIssue())) {
						isEques = true;
						index = i;
					}
				}
				if (isEques) {
					mounthDatalist.get(index).setProfitRate(
							DecimalUtil.multiply(mounthDatalist.get(index).getProfitRate(), new BigDecimal(100)));
					dealResult.add(mounthDatalist.get(index));
				} else {
					MounthProfitReport model = new MounthProfitReport();
					dealResult.add(model);
				}
			}
			result.setMounthData(dealResult);
		} else {
			result.setMounthData(mounthDatalist);
		}
		result.setMonthList(monthList);
		return result;
	}

	/**
	 * 获取当项目数据
	 * 
	 * @param mounthDto
	 * @return
	 * @throws ParseException
	 */
	public Result<MounthProfitReport> queryMounthByCom(ProfitReportReqMonthDto mounthDto) throws ParseException {
		if (mounthDto.getProjectId() == null) {
			List<CodeValue> codeList = commonService.getAllCdByKey("USER_PROJECT");
			if (codeList != null && codeList.size() > BaseConsts.ZERO) {
				mounthDto.setProjectId(Integer.parseInt(codeList.get(BaseConsts.ZERO).getCode()));
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "无项目");
			}
		}
		Result<MounthProfitReport> result = new Result<MounthProfitReport>();
		String startDate = mounthDto.getStartStatisticsDate();
		String endDate = mounthDto.getEndStatisticsDate();

		ProfitReportReqMonthDto beforDto = new ProfitReportReqMonthDto();
		beforDto.setProjectId(mounthDto.getProjectId());
		ProfitReportReqMonthDto nowDto = new ProfitReportReqMonthDto();
		nowDto.setProjectId(mounthDto.getProjectId());

		String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, new Date());
		if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate) && !startDate.equals(preDate)) {
			String startNowDate = startDate;
			String endNowDate = endDate;
			int month = DateFormatUtils.daysBetweenMonth(endNowDate, startNowDate) + BaseConsts.ONE;
			// 计算几个月时间差
			SimpleDateFormat sdf = new SimpleDateFormat(DateFormatUtils.YYYYMM);
			String startBeforDate = DateFormatUtils.format(DateFormatUtils.YYYYMM,
					DateFormatUtils.beforeMonth(sdf.parse(startNowDate), month));
			String endBeforDate = DateFormatUtils.format(DateFormatUtils.YYYYMM,
					DateFormatUtils.beforeMonth(sdf.parse(startNowDate), BaseConsts.ONE));
			nowDto.setStartStatisticsDate(startNowDate);
			nowDto.setEndStatisticsDate(endNowDate);
			beforDto.setStartStatisticsDate(startBeforDate);
			beforDto.setEndStatisticsDate(endBeforDate);
		} else {
			beforDto.setStartStatisticsDate(null);
			beforDto.setEndStatisticsDate(null);
			nowDto.setStartStatisticsDate(null);
			nowDto.setEndStatisticsDate(null);
			String beforeDate = DateFormatUtils.format(DateFormatUtils.YYYYMM,
					DateFormatUtils.getPreMonthDate(new Date()));
			beforDto.setIssue(beforeDate);
			nowDto.setIssue(preDate);
		}
		MounthProfitReport monthBefore = profitReportMonthDao.querySumProjectByCon(beforDto);// 之前时间数据
		MounthProfitReport monthNow = profitReportMonthDao.querySumProjectByCon(nowDto);// 当前时间数据
		// 计算环比 （当前/前一月）
		BigDecimal saleMom = BigDecimal.ZERO;
		BigDecimal purchaseMom = BigDecimal.ZERO;
		BigDecimal saleBlanceMom = BigDecimal.ZERO;
		BigDecimal bizManagerMom = BigDecimal.ZERO;
		BigDecimal profitMom = BigDecimal.ZERO;
		BigDecimal profitRate = BigDecimal.ZERO;
		if (monthNow != null) {
			if (monthNow.getSaleAmount() != null && monthNow.getProfitAmount() != null) {
				if (DecimalUtil.eq(monthNow.getSaleAmount(), BigDecimal.ZERO)) {
					if (monthNow.getProfitAmount() != null
							&& DecimalUtil.gt(monthNow.getProfitAmount(), BigDecimal.ZERO)) {
						profitRate = BigDecimal.ONE;
					}
					if (monthNow.getProfitAmount() != null
							&& DecimalUtil.lt(monthNow.getProfitAmount(), BigDecimal.ZERO)) {
						profitRate = new BigDecimal(-1);
					}
				} else {
					profitRate = DecimalUtil.divide(monthNow.getProfitAmount(), monthNow.getSaleAmount());// 重新计算利润率
				}
			}
			if (monthBefore != null) {
				if (monthNow.getSaleAmount() != null && monthBefore.getSaleAmount() != null) {
					BigDecimal blance = DecimalUtil.subtract(monthNow.getSaleAmount(), monthBefore.getSaleAmount());
					if (DecimalUtil.eq(monthBefore.getSaleAmount(), BigDecimal.ZERO)) {
						if (DecimalUtil.gt(blance, BigDecimal.ZERO)) {
							saleMom = BigDecimal.ONE;
						}
						if (DecimalUtil.lt(blance, BigDecimal.ZERO)) {
							saleMom = new BigDecimal(-1);
						}
					} else {
						saleMom = DecimalUtil.divide(blance, monthBefore.getSaleAmount());
					}
				}
				if (monthNow.getPurchaseCost() != null && monthBefore.getPurchaseCost() != null) {
					BigDecimal blance = DecimalUtil.subtract(monthNow.getPurchaseCost(), monthBefore.getPurchaseCost());
					if (DecimalUtil.eq(monthBefore.getPurchaseCost(), BigDecimal.ZERO)) {
						if (DecimalUtil.gt(blance, BigDecimal.ZERO)) {
							purchaseMom = BigDecimal.ONE;
						}
						if (DecimalUtil.lt(blance, BigDecimal.ZERO)) {
							purchaseMom = new BigDecimal(-1);
						}
					} else {
						purchaseMom = DecimalUtil.divide(blance, monthBefore.getPurchaseCost());
					}
				}
				if (monthNow.getSaleBlanceAmount() != null && monthBefore.getSaleBlanceAmount() != null) {
					BigDecimal blance = DecimalUtil.subtract(monthNow.getSaleBlanceAmount(),
							monthBefore.getSaleBlanceAmount());
					if (DecimalUtil.eq(monthBefore.getSaleBlanceAmount(), BigDecimal.ZERO)) {
						if (DecimalUtil.gt(blance, BigDecimal.ZERO)) {
							saleBlanceMom = BigDecimal.ONE;
						}
						if (DecimalUtil.lt(blance, BigDecimal.ZERO)) {
							saleBlanceMom = new BigDecimal(-1);
						}
					} else {
						saleBlanceMom = DecimalUtil.divide(blance, monthBefore.getSaleBlanceAmount());
					}
				}
				if (monthNow.getBizManagerAmount() != null && monthBefore.getBizManagerAmount() != null) {
					BigDecimal blance = DecimalUtil.subtract(monthNow.getBizManagerAmount(),
							monthBefore.getBizManagerAmount());
					if (DecimalUtil.eq(monthBefore.getBizManagerAmount(), BigDecimal.ZERO)) {
						if (DecimalUtil.gt(blance, BigDecimal.ZERO)) {
							bizManagerMom = BigDecimal.ONE;
						}
						if (DecimalUtil.lt(blance, BigDecimal.ZERO)) {
							bizManagerMom = new BigDecimal(-1);
						}
					} else {
						bizManagerMom = DecimalUtil.divide(blance, monthBefore.getBizManagerAmount());
					}
				}
				if (monthNow.getProfitAmount() != null && monthBefore.getProfitAmount() != null) {
					BigDecimal blance = DecimalUtil.subtract(monthNow.getProfitAmount(), monthBefore.getProfitAmount());
					if (DecimalUtil.eq(monthBefore.getProfitAmount(), BigDecimal.ZERO)) {
						if (DecimalUtil.gt(blance, BigDecimal.ZERO)) {
							profitMom = BigDecimal.ONE;
						}
						if (DecimalUtil.lt(blance, BigDecimal.ZERO)) {
							profitMom = new BigDecimal(-1);
						}
					} else {
						profitMom = DecimalUtil.divide(blance, monthBefore.getProfitAmount());
					}
				}
			}
		} else {
			monthNow = new MounthProfitReport();
		}
		monthNow.setProfitRate(DecimalUtil.multiply(profitRate, new BigDecimal(100)));
		monthNow.setSaleAmountMomStr(DecimalUtil.toPercentString(saleMom));
		monthNow.setPurchaseMomStr(DecimalUtil.toPercentString(purchaseMom));
		monthNow.setSaleBlanceMomStr(DecimalUtil.toPercentString(saleBlanceMom));
		monthNow.setBizManagerMomStr(DecimalUtil.toPercentString(bizManagerMom));
		monthNow.setProfitAmountMomStr(DecimalUtil.toPercentString(profitMom));
		result.setItems(monthNow);
		return result;
	}

	/**
	 * 获取相关利润占比数据
	 * 
	 * @param mounthDto
	 * @return
	 */
	public Result<MounthProfitReport> detailProfitPro(ProfitReportReqMonthDto mounthDto) {
		Result<MounthProfitReport> result = new Result<MounthProfitReport>();
		Date currDate = new Date();
		String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		String startStatisticsDate = null;
		String endStatisticsDate = null;
		Integer bizType = mounthDto.getBizType();
		String startDate = mounthDto.getStartStatisticsDate();
		String endDate = mounthDto.getEndStatisticsDate();
		if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			startStatisticsDate = startDate;
			endStatisticsDate = endDate;
		} else {
			if (bizType == null || bizType.equals(BaseConsts.ONE)) {// 月
				startStatisticsDate = preDate;
				endStatisticsDate = preDate;
			} else if (bizType.equals(BaseConsts.TWO)) {// 季度
				DateRange currentQuarter = DateUtils.getThisQuarter();
				startStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currentQuarter.getStart());
				endStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currentQuarter.getEnd());
			} else if (bizType.equals(BaseConsts.THREE)) {// 年
				String year = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate).substring(BaseConsts.ZERO,
						BaseConsts.FOUR);
				startStatisticsDate = year + "01";
				endStatisticsDate = year + "12";
			}
		}
		mounthDto.setUserId(ServiceSupport.getUser().getId());
		mounthDto.setProjectId(mounthDto.getProjectId());
		mounthDto.setStartStatisticsDate(startStatisticsDate);
		mounthDto.setEndStatisticsDate(endStatisticsDate);
		MounthProfitReport mounthReport = profitReportMonthDao.queryProfitReportMounthSum(mounthDto);
		if (mounthReport == null) {
			mounthReport = new MounthProfitReport();
		}
		BigDecimal saleBlanceAmount = mounthReport.getSaleBlanceAmount();
		if (DecimalUtil.eq(saleBlanceAmount, BigDecimal.ZERO)) {
			mounthReport.setSaleBlancePro(new BigDecimal(BaseConsts.INT_100));
		} else {
			if (startStatisticsDate.equals(preDate) && startStatisticsDate.equals(endStatisticsDate)) {// 当月，获取实时资金成本
				BigDecimal fundCost = BigDecimal.ZERO;
				FundReportSearchReqDto fundReportReqDto = new FundReportSearchReqDto();
				fundReportReqDto.setIssue(preDate);
				fundReportReqDto.setUserId(ServiceSupport.getUser().getId());
				fundReportReqDto.setProjectId(mounthDto.getProjectId());
				List<FundReport> fundList = fundReportDao.queyrFundReportGroupBy(fundReportReqDto);
				if (!CollectionUtils.isEmpty(fundList)) {
					for (FundReport fund : fundList) {
						fundCost = DecimalUtil.add(fundCost,
								ServiceSupport.amountNewToRMB(
										fund.getFundCost() == null ? DecimalUtil.ZERO : fund.getFundCost(),
										fund.getCurrencyType(), null));
					}
				}
				mounthReport.setFundCost(fundCost);
			}
			mounthReport
					.setProfitAmount(DecimalUtil.subtract(mounthReport.getProfitAmount(), mounthReport.getFundCost()));// 净利润=净利润-资金成本
			if (DecimalUtil.gt(mounthReport.getProfitAmount(), BigDecimal.ZERO)) {
				BigDecimal proportion = DecimalUtil.divide(mounthReport.getProfitAmount(), saleBlanceAmount);
				mounthReport.setProfitAmountPro(DecimalUtil.toPercent(proportion));// 利润百分比
			}
			if (DecimalUtil.gt(mounthReport.getManageAmount(), BigDecimal.ZERO)) {
				BigDecimal proportion = DecimalUtil.divide(mounthReport.getManageAmount(), saleBlanceAmount);
				mounthReport.setManageAmountPro(DecimalUtil.toPercent(proportion));// 管理费用百分比
			}
			if (DecimalUtil.gt(mounthReport.getWarehouseAmount(), BigDecimal.ZERO)) {
				BigDecimal proportion = DecimalUtil.divide(mounthReport.getWarehouseAmount(), saleBlanceAmount);
				mounthReport.setWarehouseAmountPro(DecimalUtil.toPercent(proportion));// 经营费用百分比
			}
			if (DecimalUtil.gt(mounthReport.getFundCost(), BigDecimal.ZERO)) {
				BigDecimal proportion = DecimalUtil.divide(mounthReport.getFundCost(), saleBlanceAmount);
				mounthReport.setFundVostPro(DecimalUtil.toPercent(proportion));// 资金成本百分比
			}
			/*
			 * mounthReport.setSaleBlancePro(DecimalUtil.subtract(new
			 * BigDecimal(BaseConsts.INT_100),
			 * mounthReport.getProfitAmountPro(),
			 * mounthReport.getManageAmountPro(),
			 * mounthReport.getWarehouseAmountPro(),
			 * mounthReport.getFundVostPro()));
			 */
		}
		result.setItems(mounthReport);
		return result;
	}

	/**
	 * 获取项目指标
	 * 
	 * @param mounthDto
	 * @return
	 */
	public PageResult<MounthProfitReport> queryProjectMonth(ProfitReportReqMonthDto mounthDto) {
		PageResult<MounthProfitReport> pageResult = new PageResult<MounthProfitReport>();
		String type = ServiceSupport.getCodeByBizValue(BizCodeConsts.PROJECT_BIZTYPE, mounthDto.getTypeName());
		if (type == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "业务类型:" + mounthDto.getTypeName() + " 不存在");
		}
		Date currDate = new Date();
		String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		String startStatisticsDate = null;
		String endStatisticsDate = null;
		Integer bizType = mounthDto.getBizType();
		String startDate = mounthDto.getStartStatisticsDate();
		String endDate = mounthDto.getEndStatisticsDate();
		if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
			startStatisticsDate = startDate;
			endStatisticsDate = endDate;
		} else {
			if (bizType == null || bizType.equals(BaseConsts.ONE)) {// 月
				startStatisticsDate = preDate;
				endStatisticsDate = preDate;
			} else if (bizType.equals(BaseConsts.TWO)) {// 季度
				DateRange currentQuarter = DateUtils.getThisQuarter();
				startStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currentQuarter.getStart());
				endStatisticsDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currentQuarter.getEnd());
			} else if (bizType.equals(BaseConsts.THREE)) {// 年
				String year = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate).substring(BaseConsts.ZERO,
						BaseConsts.FOUR);
				startStatisticsDate = year + "01";
				endStatisticsDate = year + "12";
			}
		}
		List<CodeValue> codeList = commonService.getAllCdByKey("USER_PROJECT");
		mounthDto.setType(Integer.parseInt(type));
		mounthDto.setCodeList(codeList);
		mounthDto.setStartStatisticsDate(startStatisticsDate);
		mounthDto.setEndStatisticsDate(endStatisticsDate);
		List<MounthProfitReport> result = profitReportMonthDao.queryProjectMonth(mounthDto);// 获取一年数据
		if (!CollectionUtils.isEmpty(result)) {
			/*
			 * for (int i = 0; i < result.size(); i++) { if
			 * (result.get(i).getProjectId().equals(BaseConsts.ONE)) {
			 * result.remove(i); } }
			 */
			for (MounthProfitReport model : result) {
				model.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
			}
		}
		pageResult.setItems(result);
		return pageResult;
	}
}
