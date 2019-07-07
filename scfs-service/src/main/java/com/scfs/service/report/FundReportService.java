package com.scfs.service.report;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.FundDtlReportDao;
import com.scfs.dao.report.FundReportDao;
import com.scfs.dao.report.FundTotalReportDao;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.report.entity.FundDtlReport;
import com.scfs.domain.report.entity.FundReport;
import com.scfs.domain.report.entity.FundTotalReport;
import com.scfs.domain.report.req.FundCostBillSearchReqDto;
import com.scfs.domain.report.req.FundReportSearchReqDto;
import com.scfs.domain.report.resp.FundDtlResDto;
import com.scfs.domain.report.resp.FundReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: FundReportService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月28日				Administrator
 *
 * </pre>
 */
@Service
public class FundReportService {
	@Autowired
	private FundReportDao fundReportDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private FundDtlReportDao fundDtlReportDao;
	@Autowired
	private FundTotalReportDao fundTotalReportDao;
	@Autowired
	private ReportProjectService reportProjectService;

	public PageResult<FundReportResDto> queryResultsByCon(FundReportSearchReqDto fundReportSearchReqDto) {
		fundReportSearchReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SIX));
		fundReportSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(fundReportSearchReqDto.getPage(), fundReportSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, fundReportSearchReqDto.getPer_page());
		PageResult<FundReportResDto> pageResult = new PageResult<FundReportResDto>();
		if (!StringUtils.isEmpty(fundReportSearchReqDto.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(fundReportSearchReqDto.getDepartmentId().split(","));
			fundReportSearchReqDto.setDepartmentList(departmentList);
		}
		if (fundReportSearchReqDto.getPeriodType() == BaseConsts.TWO) {
			List<FundTotalReport> fundReports = new ArrayList<>();
			if (null != fundReportSearchReqDto.getNeedSum() && fundReportSearchReqDto.getNeedSum() == BaseConsts.ONE) {
				if (fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
					if (fundReportSearchReqDto.getCapitalAccountType() == null) {
						fundReports = fundTotalReportDao.queryResultsBySta(fundReportSearchReqDto);
					} else {
						fundReports = fundTotalReportDao.queryResultsByStaTwo(fundReportSearchReqDto);
					}
				} else {
					fundReports = fundTotalReportDao.queryResultsByCon(fundReportSearchReqDto);
				}
			} else {
				if (fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
					if (fundReportSearchReqDto.getCapitalAccountType() == null) {
						fundReports = fundTotalReportDao.queryResultsBySta(fundReportSearchReqDto, rowBounds);
					} else {
						fundReports = fundTotalReportDao.queryResultsByStaTwo(fundReportSearchReqDto, rowBounds);
					}
				} else {
					fundReports = fundTotalReportDao.queryResultsByCon(fundReportSearchReqDto, rowBounds);
				}
			}
			List<FundReportResDto> fundReportResDtos = new ArrayList<FundReportResDto>();
			// 期初余额
			BigDecimal initialBalance = BigDecimal.ZERO;
			// 本期付款金额
			BigDecimal currentPayment = BigDecimal.ZERO;
			// 本期收款金额
			BigDecimal currentRec = BigDecimal.ZERO;
			// 期末余额
			BigDecimal balance = BigDecimal.ZERO;
			// 本期资金成本
			BigDecimal fundCost = BigDecimal.ZERO;

			for (FundTotalReport fund : fundReports) {
				FundReportResDto fundRes = new FundReportResDto();
				BeanUtils.copyProperties(fund, fundRes);
				if (fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
					fundRes.setProjectName("");
				}
				fundRes.setBusinessUnitId(fund.getBusiUnit());
				fundRes.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						fundRes.getCurrencyType() + ""));
				fundRes.setCapitalAccountTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.CAPITAL_ACCOUNT_TYPE,
						cacheService.getAccountById(fund.getAccountId()).getCapitalAccountType() + ""));
				fundRes.setCapitalAccountType(cacheService.getAccountById(fund.getAccountId()).getCapitalAccountType());
				if (StringUtils.isNoneBlank(fundRes.getIssue())) {
					int year = Integer.valueOf(StringUtils.substring(fundRes.getIssue(), 0, 4));
					int month = Integer.valueOf(StringUtils.substring(fundRes.getIssue(), 4, 6));
					fundRes.setStartIssueDate(DateFormatUtils.getFirstDateOfMonth(year, month));
					fundRes.setEndIssueDate(DateFormatUtils.getEndDateOfMonth(year, month));
				}
				fundReportResDtos.add(fundRes);

				// 合计
				if (null != fundReportSearchReqDto.getNeedSum() && fundReportSearchReqDto.getNeedSum() == 1) {
					initialBalance = DecimalUtil.add(initialBalance,
							ServiceSupport.amountNewToRMB(
									null == fundRes.getBeginBalance() ? BigDecimal.ZERO : fundRes.getBeginBalance(),
									fundRes.getCurrencyType(), null));
					currentPayment = DecimalUtil.add(currentPayment,
							ServiceSupport.amountNewToRMB(
									null == fundRes.getPayAmount() ? BigDecimal.ZERO : fundRes.getPayAmount(),
									fundRes.getCurrencyType(), null));
					currentRec = DecimalUtil.add(currentRec,
							ServiceSupport.amountNewToRMB(
									null == fundRes.getReceiptAmount() ? BigDecimal.ZERO : fundRes.getReceiptAmount(),
									fundRes.getCurrencyType(), null));
					balance = DecimalUtil.add(balance,
							ServiceSupport.amountNewToRMB(
									null == fundRes.getBalance() ? BigDecimal.ZERO : fundRes.getBalance(),
									fundRes.getCurrencyType(), null));
					fundCost = DecimalUtil.add(fundCost,
							ServiceSupport.amountNewToRMB(
									null == fundRes.getFundCost() ? BigDecimal.ZERO : fundRes.getFundCost(),
									fundRes.getCurrencyType(), null));
				}
			}
			if (null != fundReportSearchReqDto.getNeedSum() && fundReportSearchReqDto.getNeedSum() == 1) {
				String totalStr = "期初余额：" + DecimalUtil.toAmountString(initialBalance) + "；本期付款："
						+ DecimalUtil.toAmountString(currentPayment) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；本期收款："
						+ DecimalUtil.toAmountString(currentRec) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；余额："
						+ DecimalUtil.toAmountString(balance) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；资金成本："
						+ DecimalUtil.toAmountString(fundCost) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
				pageResult.setTotalStr(totalStr);
			}
			pageResult.setItems(fundReportResDtos);
		} else {
			FundReportSearchReqDto fundReportReq = new FundReportSearchReqDto();
			BeanUtils.copyProperties(fundReportSearchReqDto, fundReportReq);
			List<FundReport> fundReports = new ArrayList<FundReport>();
			if (null != fundReportSearchReqDto.getNeedSum() && fundReportSearchReqDto.getNeedSum() == 1) {
				if (fundReportReq.getStatisticsDimension() == BaseConsts.TWO) {
					if (fundReportReq.getCapitalAccountType() == null) { // 独立的根据经营单位进行查询
						fundReports = fundReportDao.queryInitListByConByCon(fundReportReq);
					} else {
						if (fundReportReq.getCapitalAccountType() == BaseConsts.ONE) {
							fundReports = fundReportDao.queryInitListByCon(fundReportReq);
						} else if (fundReportReq.getCapitalAccountType() == BaseConsts.TWO) {
							fundReports = fundReportDao.queryInitListByConByTwo(fundReportReq);
						}
					}
				} else {
					fundReports = fundReportDao.queryInitListByCon(fundReportReq);
				}
			} else {
				if (fundReportReq.getStatisticsDimension() == BaseConsts.TWO) {
					if (fundReportReq.getCapitalAccountType() == null) { // 独立的根据经营单位进行查询
						fundReports = fundReportDao.queryInitListByConByCon(fundReportReq, rowBounds);
					} else {
						if (fundReportReq.getCapitalAccountType() == BaseConsts.ONE) {
							fundReports = fundReportDao.queryInitListByCon(fundReportReq, rowBounds);
						} else if (fundReportReq.getCapitalAccountType() == BaseConsts.TWO) {
							fundReports = fundReportDao.queryInitListByConByTwo(fundReportReq, rowBounds);
						}
					}
				} else {
					fundReports = fundReportDao.queryInitListByCon(fundReportReq, rowBounds);
				}
			}
			List<FundReportResDto> fundReportResDtos = convertToResult(fundReports, fundReportReq);
			if (null != fundReportSearchReqDto.getNeedSum() && fundReportSearchReqDto.getNeedSum() == 1) {
				String totalStr = totalFund(fundReportSearchReqDto);
				pageResult.setTotalStr(totalStr);
			}
			pageResult.setItems(fundReportResDtos);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fundReportSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fundReportSearchReqDto.getPage());
		pageResult.setPer_page(fundReportSearchReqDto.getPer_page());
		return pageResult;
	}

	public List<FundReportResDto> queryAllResultsByCon(FundReportSearchReqDto fundReportSearchReqDto) {
		fundReportSearchReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SIX));
		fundReportSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<FundReportResDto> pageResult = new ArrayList<FundReportResDto>();
		if (!StringUtils.isEmpty(fundReportSearchReqDto.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(fundReportSearchReqDto.getDepartmentId().split(","));
			fundReportSearchReqDto.setDepartmentList(departmentList);
		}
		if (fundReportSearchReqDto.getPeriodType() == BaseConsts.TWO) {
			List<FundTotalReport> fundReports = fundTotalReportDao.queryResultsByCon(fundReportSearchReqDto);
			List<FundReportResDto> fundReportResDtos = new ArrayList<FundReportResDto>();
			for (FundTotalReport fund : fundReports) {
				FundReportResDto fundRes = new FundReportResDto();
				BeanUtils.copyProperties(fund, fundRes);
				fundRes.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						fundRes.getCurrencyType() + ""));
				fundRes.setCapitalAccountTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.CAPITAL_ACCOUNT_TYPE,
						cacheService.getAccountById(fund.getAccountId()).getCapitalAccountType() + ""));
				fundRes.setCapitalAccountType(cacheService.getAccountById(fund.getAccountId()).getCapitalAccountType());
				if (StringUtils.isNoneBlank(fundRes.getIssue())) {
					int year = Integer.valueOf(StringUtils.substring(fundRes.getIssue(), 0, 4));
					int month = Integer.valueOf(StringUtils.substring(fundRes.getIssue(), 4, 6));
					fundRes.setStartIssueDate(DateFormatUtils.getFirstDateOfMonth(year, month));
					fundRes.setEndIssueDate(DateFormatUtils.getEndDateOfMonth(year, month));
				}
				fundReportResDtos.add(fundRes);
			}
			pageResult = fundReportResDtos;
		} else {
			FundReportSearchReqDto fundReportReq = new FundReportSearchReqDto();
			BeanUtils.copyProperties(fundReportSearchReqDto, fundReportReq);
			List<FundReport> fundReports = fundReportDao.queryInitListByCon(fundReportReq);
			List<FundReportResDto> fundReportResDtos = convertToResult(fundReports, fundReportReq);
			pageResult = fundReportResDtos;
		}
		return pageResult;
	}

	private String totalFund(FundReportSearchReqDto fundReportSearchReqDto) {
		// TODO Auto-generated method stub
		// 期初余额
		BigDecimal initialBalance = BigDecimal.ZERO;
		// 本期付款金额
		BigDecimal currentPayment = BigDecimal.ZERO;
		// 本期收款金额
		BigDecimal currentRec = BigDecimal.ZERO;
		// 期末余额
		BigDecimal balance = BigDecimal.ZERO;
		// 本期资金成本
		BigDecimal fundCost = BigDecimal.ZERO;
		List<FundReport> fundReports = new ArrayList<FundReport>();
		if (fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
			if (fundReportSearchReqDto.getCapitalAccountType() == null) { // 独立的根据经营单位进行查询
				fundReports = fundReportDao.queryInitListByConByCon(fundReportSearchReqDto);
			} else {
				if (fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.ONE) {
					fundReports = fundReportDao.queryInitListByCon(fundReportSearchReqDto);
				} else if (fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO) {
					fundReports = fundReportDao.queryInitListByConByTwo(fundReportSearchReqDto);
				}
			}
		} else {
			fundReports = fundReportDao.queryInitListByCon(fundReportSearchReqDto);
		}
		List<FundReportResDto> fundReportResDtos = convertToResult(fundReports, fundReportSearchReqDto);
		for (FundReportResDto fun : fundReportResDtos) {
			initialBalance = DecimalUtil.add(initialBalance,
					ServiceSupport.amountNewToRMB(
							null == fun.getBeginBalance() ? BigDecimal.ZERO : fun.getBeginBalance(),
							fun.getCurrencyType(), null));
			currentPayment = DecimalUtil.add(currentPayment, ServiceSupport.amountNewToRMB(
					null == fun.getPayAmount() ? BigDecimal.ZERO : fun.getPayAmount(), fun.getCurrencyType(), null));
			currentRec = DecimalUtil.add(currentRec,
					ServiceSupport.amountNewToRMB(
							null == fun.getReceiptAmount() ? BigDecimal.ZERO : fun.getReceiptAmount(),
							fun.getCurrencyType(), null));
			balance = DecimalUtil.add(balance, ServiceSupport.amountNewToRMB(
					null == fun.getBalance() ? BigDecimal.ZERO : fun.getBalance(), fun.getCurrencyType(), null));
			fundCost = DecimalUtil.add(fundCost, ServiceSupport.amountNewToRMB(
					null == fun.getFundCost() ? BigDecimal.ZERO : fun.getFundCost(), fun.getCurrencyType(), null));
		}
		String totalStr = "期初余额：" + DecimalUtil.toAmountString(initialBalance) + "；本期付款："
				+ DecimalUtil.toAmountString(currentPayment) + BaseConsts.STRING_BLANK_SPACE
				+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；本期收款：" + DecimalUtil.toAmountString(currentRec)
				+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；余额："
				+ DecimalUtil.toAmountString(balance) + BaseConsts.STRING_BLANK_SPACE
				+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；资金成本：" + DecimalUtil.toAmountString(fundCost)
				+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
		return totalStr;
	}

	public List<FundReportResDto> exportFundReportExcel(FundReportSearchReqDto fundReportSearchReqDto) {
		return queryListByCon(fundReportSearchReqDto, true);
	}

	public List<FundReportResDto> queryListByCon(FundReportSearchReqDto fundReportSearchReqDto, boolean userState) {
		fundReportSearchReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SIX));
		if (!StringUtils.isEmpty(fundReportSearchReqDto.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(fundReportSearchReqDto.getDepartmentId().split(","));
			fundReportSearchReqDto.setDepartmentList(departmentList);
		}
		if (userState == true) {
			fundReportSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<FundReport> fundReports = new ArrayList<FundReport>();
		if (fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO
				&& fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO) {
			fundReports = fundReportDao.queryInitListByConByTwo(fundReportSearchReqDto);
		} else {
			fundReports = fundReportDao.queryInitListByCon(fundReportSearchReqDto);
		}
		List<FundReportResDto> fundReportResDtos = convertToResult(fundReports, fundReportSearchReqDto);
		return fundReportResDtos;
	}

	private List<FundReportResDto> convertToResult(List<FundReport> fundReports,
			FundReportSearchReqDto fundReportSearchReqDto) {
		List<FundReportResDto> fundReportResDtos = new ArrayList<FundReportResDto>();
		if (CollectionUtils.isEmpty(fundReports)) {
			return fundReportResDtos;
		}
		for (FundReport item : fundReports) {
			item = queryDetailDataByCon(item, fundReportSearchReqDto);
			FundReportResDto fundReportResDto = convertToResDto(item, fundReportSearchReqDto);
			fundReportResDtos.add(fundReportResDto);
		}
		return fundReportResDtos;
	}

	/** 获取该条资金信息的本期收、付款金额 */
	private FundReport queryDetailDataByCon(FundReport fundReport, FundReportSearchReqDto fundReportSearchReqDto) {

		fundReportSearchReqDto.setBusinessUnitId(null);
		fundReportSearchReqDto.setDepartmentList(null);
		if (null != fundReport.getCapitalAccountType()) {
			fundReportSearchReqDto.setCapitalAccountType(fundReport.getCapitalAccountType());
			if (fundReport.getCapitalAccountType() == BaseConsts.TWO) {
				fundReportSearchReqDto.setProjectId(null);
			}
		}
		if (null != fundReport.getProjectId()) {
			fundReportSearchReqDto.setProjectId(fundReport.getProjectId());
		}
		if (null != fundReport.getBusiUnit() && fundReportSearchReqDto.getCapitalAccountType() == 2
				&& fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
			fundReportSearchReqDto.setBusinessUnitId(fundReport.getBusiUnit());
		}
		if (!StringUtils.isEmpty(fundReport.getDepartmentId()) && fundReportSearchReqDto.getCapitalAccountType() == 1
				&& fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
			List<String> departmentList = Arrays.asList(fundReport.getDepartmentId().split(","));
			fundReportSearchReqDto.setDepartmentList(departmentList);
		}
		fundReportSearchReqDto.setAccountId(fundReport.getAccountId());
		FundReport fundReportResult = new FundReport();
		fundReportSearchReqDto.setCurrencyType(fundReport.getCurrencyType());
		fundReportResult = fundReportDao.queryDataResultByCon(fundReportSearchReqDto);
		if (fundReportSearchReqDto.getCapitalAccountType() != null
				&& fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO
				&& fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
			fundReportResult.setCurBalance(DecimalUtil.multiply(new BigDecimal("-1"),
					fundReportResult.getCurBalance() == null ? BigDecimal.ZERO : fundReportResult.getCurBalance()));
		}
		fundReportResult.setBusiUnit(fundReport.getBusiUnit());
		fundReportResult.setAccountId(fundReport.getAccountId());
		fundReportResult.setProjectId(fundReport.getProjectId());
		fundReportResult.setCurrencyType(fundReport.getCurrencyType());
		fundReportResult.setCapitalAccountType(fundReport.getCapitalAccountType());
		return fundReportResult;
	}

	/** 获取该条资金信息的本期首付款金额 */
	private FundReportResDto convertToResDto(FundReport fundReport, FundReportSearchReqDto fundReportSearchReqDto) {
		FundReportResDto fundReportResDto = new FundReportResDto();
		BeanUtils.copyProperties(fundReport, fundReportResDto);
		if (null != fundReport.getCapitalAccountType()) {
			fundReportResDto.setCapitalAccountTypeName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.CAPITAL_ACCOUNT_TYPE, fundReport.getCapitalAccountType() + ""));
			fundReportResDto.setCapitalAccountType(fundReport.getCapitalAccountType());
		}
		if (null != fundReport.getProjectId()) {
			BaseProject baseProject = cacheService.getProjectById(fundReport.getProjectId());
			BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseProject.getDepartmentId());
			fundReportResDto.setDepartmentName(baseDepartment.getName());
			fundReportResDto.setDepartmentId(baseDepartment.getId());
			if (!fundReportSearchReqDto.getStatisticsDimension().equals(BaseConsts.TWO)) {
				fundReportResDto.setProjectName(cacheService.getProjectNameById(fundReport.getProjectId()));
			}
			fundReportResDto.setBizManagerId(baseProject.getBizSpecialId());
			fundReportResDto.setBizType(baseProject.getBizType());
			fundReportResDto.setBusinessUnitId(baseProject.getBusinessUnitId());
		}
		fundReportResDto.setBusinessUnitId(fundReport.getBusiUnit());
		fundReportResDto.setCurrencyType(fundReport.getCurrencyType());
		fundReportResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				fundReport.getCurrencyType() + ""));
		/** 查询期初余额 */
		BigDecimal startBalance = DecimalUtil.formatScale2(
				fundReportDao.queryBeginBalanceByCon(queryBeginningBalance(fundReportSearchReqDto, fundReport)));
		if (fundReportSearchReqDto.getCapitalAccountType() != null
				&& fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO
				&& fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
			startBalance = DecimalUtil.multiply(new BigDecimal("-1"), startBalance);
		}
		fundReportResDto.setBeginBalance(startBalance); // 查询期初余额
		fundReportResDto.setBalance(
				DecimalUtil.formatScale2(fundReportResDto.getBeginBalance().add(fundReport.getCurBalance())));
		/** 查询本期资金成本列表 */
		List<FundDtlReport> fundDtlReports = fundDtlReportDao
				.queryFundDtlListByDate(queryBeginningBalance(fundReportSearchReqDto, fundReport));
		try {
			BigDecimal fundCost = computeFunCost(fundDtlReports, fundReportSearchReqDto, startBalance);
			fundReportResDto.setFundCost(fundCost);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "日期解析错误");
		}
		return fundReportResDto;
	}

	/** 查询条件转换 */
	private FundReportSearchReqDto queryBeginningBalance(FundReportSearchReqDto fundReportSearchReqDto,
			FundReport fundReport) {
		FundReportSearchReqDto reqDto = new FundReportSearchReqDto();
		reqDto.setUserId(fundReportSearchReqDto.getUserId());
		reqDto.setProjectId(fundReport.getProjectId());
		reqDto.setAccountId(fundReport.getAccountId());
		reqDto.setCurrencyType(fundReport.getCurrencyType());
		reqDto.setStartDate(fundReportSearchReqDto.getStartDate());
		reqDto.setDepartmentList(fundReportSearchReqDto.getDepartmentList());
		reqDto.setDepartmentId(fundReportSearchReqDto.getDepartmentId());
		reqDto.setEndDate(fundReportSearchReqDto.getEndDate());
		reqDto.setCapitalAccountType(fundReport.getCapitalAccountType());
		reqDto.setStatisticsDimension(fundReportSearchReqDto.getStatisticsDimension());
		reqDto.setBusinessUnitId(fundReportSearchReqDto.getBusinessUnitId());
		return reqDto;
	}

	/** 计算资金成本 */
	private BigDecimal computeFunCost(List<FundDtlReport> fundDtlReports, FundReportSearchReqDto reqDto,
			BigDecimal startBalance) throws ParseException {
		Date startDate = reqDto.getStartDate();
		Date endDate = reqDto.getEndDate();
		BigDecimal copyStartBalance = new BigDecimal(startBalance.doubleValue());
		BigDecimal fundCost = new BigDecimal(0);
		for (int i = 0; i < fundDtlReports.size(); i++) {
			BigDecimal curBalance = BigDecimal.ZERO;
			FundDtlReport fundDtlReport = fundDtlReports.get(i);
			if (reqDto.getCapitalAccountType() != null && reqDto.getCapitalAccountType() == BaseConsts.TWO
					&& reqDto.getStatisticsDimension() == BaseConsts.TWO) {
				curBalance = DecimalUtil.multiply(new BigDecimal("-1"),
						fundDtlReport.getCurBalance() == null ? BigDecimal.ZERO : fundDtlReport.getCurBalance());
				// fundDtlReport.setCurBalance(DecimalUtil.multiply(new
				// BigDecimal("-1"), fundDtlReport.getCurBalance()));
			} else {
				curBalance = fundDtlReport.getCurBalance();
			}
			if (DecimalUtil.gt(copyStartBalance, DecimalUtil.ZERO)) { // 余额为负数不计资金成本
				fundCost = fundCost.add(DecimalUtil.formatScale2(copyStartBalance.multiply(BaseConsts.getFeeRate())));
			}
			if (DateFormatUtils.diffDate(fundDtlReport.getBillDate(), startDate) == 0) {
				copyStartBalance = copyStartBalance.add(curBalance);
			} else {
				i--;
			}
			startDate = DateFormatUtils.beforeDay(startDate, -1);
		}
		long diffDays = DateFormatUtils.diffDate(endDate, startDate);
		for (int i = 0; i <= diffDays; i++) {
			if (DecimalUtil.gt(copyStartBalance, DecimalUtil.ZERO)) { // 余额为负数不计资金成本
				fundCost = fundCost.add(DecimalUtil.formatScale2(copyStartBalance.multiply(BaseConsts.getFeeRate())));
			}
		}

		if (CollectionUtils.isNotEmpty(fundDtlReports)
				&& DateFormatUtils.diffDate(fundDtlReports.get(0).getBillDate(), reqDto.getStartDate()) == 0) {
			if (reqDto.getStatisticsDimension() == BaseConsts.TWO && reqDto.getCapitalAccountType() == BaseConsts.TWO) {
				// 如果第一天有付款操作，则计算在资金成本内
				BigDecimal firstDayReceipt = DecimalUtil
						.formatScale2(fundDtlReports.get(0).getPayAmount().multiply(BaseConsts.getFeeRate()));
				fundCost = fundCost.subtract(firstDayReceipt);
			} else {
				// 如果第一天有收款操作，则计算在资金成本内
				BigDecimal firstDayReceipt = DecimalUtil
						.formatScale2(fundDtlReports.get(0).getReceiptAmount().multiply(BaseConsts.getFeeRate()));
				if (DecimalUtil.ge(fundCost, firstDayReceipt)) {
					fundCost = fundCost.subtract(firstDayReceipt);
				}
			}
		}
		return fundCost;
	}

	/**
	 * 资金统计明细 TODO.
	 *
	 * @param fundReportSearchReqDto
	 * @return
	 */
	public List<FundDtlResDto> queryDtlsByCon(FundReportSearchReqDto fundReportSearchReqDto) {
		fundReportSearchReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SIX));
		if (fundReportSearchReqDto.getPeriodType() == BaseConsts.TWO) {
			try {
				// fundReportSearchReqDto.setStatisticsDimension(BaseConsts.THREE);
				fundReportSearchReqDto.setStartDate(
						DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, fundReportSearchReqDto.getStartPeriod()));
				fundReportSearchReqDto.setEndDate(
						DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, fundReportSearchReqDto.getEndPeriod()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<FundDtlResDto> fundDtlResDtos = new ArrayList<FundDtlResDto>();
		if (!StringUtils.isEmpty(fundReportSearchReqDto.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(fundReportSearchReqDto.getDepartmentId().split(","));
			fundReportSearchReqDto.setDepartmentList(departmentList);
		}
		fundReportSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		// 查询期初余额
		BigDecimal startBalance = DecimalUtil
				.formatScale2(fundReportDao.queryBeginBalanceByCon(fundReportSearchReqDto));
		if (fundReportSearchReqDto.getCapitalAccountType() != null
				&& fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO
				&& fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
			startBalance = DecimalUtil.multiply(new BigDecimal("-1"), startBalance);
		}
		FundDtlResDto startFundDtlRes = beginAndEndConvert("期初", fundReportSearchReqDto);
		startFundDtlRes.setBalance(startBalance);
		fundDtlResDtos.add(startFundDtlRes);
		// 查询资金统计详细信息
		List<FundDtlReport> fundDtlReports = fundDtlReportDao.queryFundDtlsByCon(fundReportSearchReqDto);

		BigDecimal copyStartBalance = new BigDecimal(startBalance.doubleValue()); // 这个值会被改变
		List<FundDtlResDto> fundDtlResDtos2 = convertToDtls(copyStartBalance, fundDtlReports, fundReportSearchReqDto);
		fundDtlResDtos.addAll(fundDtlResDtos2);

		FundDtlResDto endDtlResDto = new FundDtlResDto();
		if (CollectionUtils.isNotEmpty(fundDtlResDtos2)) {
			FundDtlResDto stanDto = fundDtlResDtos2.get(fundDtlResDtos2.size() - 1);
			if (null != fundReportSearchReqDto.getProjectId()) {
				endDtlResDto.setProjectName(stanDto.getProjectName());
			}
			if (null != fundReportSearchReqDto.getAccountId()) {
				endDtlResDto.setAccountNo(stanDto.getAccountNo());
				endDtlResDto.setAccountTypeName(stanDto.getAccountTypeName());
				endDtlResDto.setCapitalAccountTypeName(stanDto.getCapitalAccountTypeName());
			}
			endDtlResDto.setCurrencyType(stanDto.getCurrencyType());
			endDtlResDto.setCurrencyTypeName(stanDto.getCurrencyTypeName());
			endDtlResDto.setBalance(stanDto.getBalance());
			endDtlResDto.setBillDate(fundReportSearchReqDto.getEndDate());
			endDtlResDto.setBillNo("期末");
		} else {
			BeanUtils.copyProperties(startFundDtlRes, endDtlResDto);
			endDtlResDto.setBillDate(fundReportSearchReqDto.getEndDate());
			endDtlResDto.setBillNo("期末");
			;
		}
		fundDtlResDtos.add(endDtlResDto);
		return fundDtlResDtos;
	}

	public FundDtlResDto beginAndEndConvert(String beginOrEnd, FundReportSearchReqDto fundReportSearchReqDto) {
		FundDtlReport fundDtlReport = new FundDtlReport();
		fundDtlReport.setAccountId(fundReportSearchReqDto.getAccountId());
		fundDtlReport.setProjectId(fundReportSearchReqDto.getProjectId());
		fundDtlReport.setCurrencyType(fundReportSearchReqDto.getCurrencyType());
		fundDtlReport.setCapitalAccountType(fundReportSearchReqDto.getCapitalAccountType());
		fundDtlReport.setBillDate(fundReportSearchReqDto.getStartDate());
		fundDtlReport.setBillNo(beginOrEnd);
		if (fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
			fundDtlReport.setProjectId(null);
		}
		FundDtlResDto fundDtlRes = convertToDtl(fundDtlReport);
		return fundDtlRes;
	}

	/**
	 * 资金成本明细 TODO.
	 *
	 * @param fundReportSearchReqDto
	 * @return
	 */
	public List<FundDtlResDto> queryFundCostDtlsByCon(FundReportSearchReqDto fundReportSearchReqDto) {
		fundReportSearchReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SIX));
		if (fundReportSearchReqDto.getPeriodType() != null
				&& fundReportSearchReqDto.getPeriodType() == BaseConsts.TWO) {
			try {
				// fundReportSearchReqDto.setStatisticsDimension(BaseConsts.THREE);
				fundReportSearchReqDto.setStartDate(
						DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, fundReportSearchReqDto.getStartPeriod()));
				fundReportSearchReqDto.setEndDate(
						DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, fundReportSearchReqDto.getEndPeriod()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (!StringUtils.isEmpty(fundReportSearchReqDto.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(fundReportSearchReqDto.getDepartmentId().split(","));
			fundReportSearchReqDto.setDepartmentList(departmentList);
		}
		fundReportSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		/** 查询期初余额 */
		BigDecimal startBalance = DecimalUtil
				.formatScale2(fundReportDao.queryBeginBalanceByCon(fundReportSearchReqDto));// 期初余额
		// List<FundDtlReport> fundDtlReports =
		// fundDtlReportDao.queryFundDtlsGroupByDate(fundReportSearchReqDto);
		// //按天计算余额
		if (fundReportSearchReqDto.getCapitalAccountType() != null
				&& fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO
				&& fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
			startBalance = DecimalUtil.multiply(new BigDecimal("-1"), startBalance);
		}
		/** 查询本期资金成本列表 */
		List<FundDtlReport> fundDtlReports = fundDtlReportDao.queryFundDtlListByDate(fundReportSearchReqDto);
		return convertFundDtlList(fundDtlReports, fundReportSearchReqDto, startBalance);
	}

	/** 转换资金成本明细信息 */
	private List<FundDtlResDto> convertFundDtlList(List<FundDtlReport> fundDtlReports,
			FundReportSearchReqDto fundReportSearchReqDto, BigDecimal startBalance) {
		List<FundDtlResDto> fundDtlResDtos = new ArrayList<FundDtlResDto>();
		BigDecimal copyStartBalance = new BigDecimal(startBalance.doubleValue());
		Date startDate = fundReportSearchReqDto.getStartDate();
		Date endDate = fundReportSearchReqDto.getEndDate();
		try {
			BigDecimal curBalance = BigDecimal.ZERO;
			for (int i = 0; i < fundDtlReports.size(); i++) {
				FundDtlReport fundDtlReport = fundDtlReports.get(i);
				if (fundReportSearchReqDto.getCapitalAccountType() != null
						&& fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO
						&& fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
					curBalance = DecimalUtil.multiply(new BigDecimal("-1"), fundDtlReport.getCurBalance());
					// fundDtlReport.setCurBalance(DecimalUtil.multiply(new
					// BigDecimal("-1"), fundDtlReport.getCurBalance()));
				} else {
					curBalance = fundDtlReport.getCurBalance();
				}
				fundDtlReport.setProjectId(fundReportSearchReqDto.getProjectId());
				fundDtlReport.setCapitalAccountType(fundReportSearchReqDto.getCapitalAccountType());
				fundDtlReport.setCurrencyType(fundReportSearchReqDto.getCurrencyType());
				FundDtlResDto fundDtlResDto = convertToDtl(fundDtlReport);
				if (DateFormatUtils.diffDate(fundDtlReport.getBillDate(), startDate) == 0) {
					fundDtlResDto.setBeginBalance(copyStartBalance);
					fundDtlResDto.setBalance(curBalance.add(copyStartBalance));
					copyStartBalance = fundDtlResDto.getBalance();
				} else {
					fundDtlResDto.setBeginBalance(copyStartBalance);
					fundDtlResDto.setPayAmount(DecimalUtil.ZERO);
					fundDtlResDto.setReceiptAmount(DecimalUtil.ZERO);
					fundDtlResDto.setBalance(copyStartBalance);
					fundDtlResDto.setBillDate(startDate);
					i--;
				}
				if (DecimalUtil.gt(fundDtlResDto.getBeginBalance(), DecimalUtil.ZERO)) { // 余额为负数不计资金成本
					fundDtlResDto.setFundCost(DecimalUtil
							.formatScale2(fundDtlResDto.getBeginBalance().multiply(BaseConsts.getFeeRate())));
				} else {
					fundDtlResDto.setFundCost(DecimalUtil.ZERO);
				}
				startDate = DateFormatUtils.beforeDay(startDate, -1);
				fundDtlResDtos.add(fundDtlResDto);
			}
			long diffDays = DateFormatUtils.diffDate(endDate, startDate);
			for (int i = 0; i <= diffDays; i++) {
				FundDtlReport fundDtlReport = new FundDtlReport();
				fundDtlReport.setAccountId(fundReportSearchReqDto.getAccountId());
				fundDtlReport.setProjectId(fundReportSearchReqDto.getProjectId());
				fundDtlReport.setCurrencyType(fundReportSearchReqDto.getCurrencyType());
				fundDtlReport.setCapitalAccountType(fundReportSearchReqDto.getCapitalAccountType());
				FundDtlResDto fundDtlResDto = convertToDtl(fundDtlReport);
				fundDtlResDto.setBeginBalance(copyStartBalance);
				fundDtlResDto.setPayAmount(DecimalUtil.ZERO);
				fundDtlResDto.setReceiptAmount(DecimalUtil.ZERO);
				fundDtlResDto.setBalance(copyStartBalance);
				fundDtlResDto.setBillDate(DateFormatUtils.beforeDay(startDate, -i));
				if (DecimalUtil.gt(fundDtlResDto.getBeginBalance(), DecimalUtil.ZERO)) { // 余额为负数不计资金成本
					fundDtlResDto.setFundCost(DecimalUtil
							.formatScale2(fundDtlResDto.getBeginBalance().multiply(BaseConsts.getFeeRate())));
				} else {
					fundDtlResDto.setFundCost(DecimalUtil.ZERO);
				}
				fundDtlResDtos.add(fundDtlResDto);
			}
			// 如果数据不为空,并且属于期初时间
			if (CollectionUtils.isNotEmpty(fundDtlReports) && DateFormatUtils
					.diffDate(fundDtlReports.get(0).getBillDate(), fundReportSearchReqDto.getStartDate()) == 0) {
				if (fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO
						&& fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO) {
					// 如果第一天有付款操作，则计算在资金成本内
					BigDecimal firstDayReceipt = DecimalUtil
							.formatScale2(fundDtlReports.get(0).getPayAmount().multiply(BaseConsts.getFeeRate()));
					BigDecimal fundCost = fundDtlResDtos.get(0).getFundCost().subtract(firstDayReceipt);
					fundDtlResDtos.get(0).setFundCost(fundCost);
				} else {
					// 如果第一天有收款操作，则计算在资金成本内
					BigDecimal firstDayReceipt = DecimalUtil
							.formatScale2(fundDtlReports.get(0).getReceiptAmount().multiply(BaseConsts.getFeeRate()));
					BigDecimal fundCost = fundDtlResDtos.get(0).getFundCost().subtract(firstDayReceipt);
					fundDtlResDtos.get(0).setFundCost(fundCost);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fundDtlResDtos;
	}

	/**
	 * 查询资金成本单据明细 TODO.
	 *
	 * @param fundReportSearchReqDto
	 * @return
	 */
	public List<FundDtlResDto> queryFundCostBillDtlByCon(FundCostBillSearchReqDto fundCostBillDtlSearchReqDto) {
		fundCostBillDtlSearchReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.SIX));

		List<FundDtlResDto> fundDtlResDtos = new ArrayList<FundDtlResDto>();
		if (!StringUtils.isEmpty(fundCostBillDtlSearchReqDto.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(fundCostBillDtlSearchReqDto.getDepartmentId().split(","));
			fundCostBillDtlSearchReqDto.setDepartmentList(departmentList);
		}
		fundCostBillDtlSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		if (null != fundCostBillDtlSearchReqDto.getSearchType()) {
			List<FundDtlReport> fundDtlReports = new ArrayList<FundDtlReport>();
			;
			if (fundCostBillDtlSearchReqDto.getSearchType().equals(BaseConsts.ONE)) {
				fundDtlReports = fundDtlReportDao.queryFundCostPayByCon(fundCostBillDtlSearchReqDto);
			} else if (fundCostBillDtlSearchReqDto.getSearchType().equals(BaseConsts.TWO)) {
				fundDtlReports = fundDtlReportDao.queryFundCostReceiptByCon(fundCostBillDtlSearchReqDto);
			}
			for (FundDtlReport fundDtlReport : fundDtlReports) {
				FundDtlResDto fundDtlResDto = convertToDtl(fundDtlReport);
				fundDtlResDtos.add(fundDtlResDto);
			}
		}
		return fundDtlResDtos;
	}

	private List<FundDtlResDto> convertToDtls(BigDecimal beginBlance, List<FundDtlReport> fundDtlReports,
			FundReportSearchReqDto fundReportSearchReqDto) {
		List<FundDtlResDto> fundDtlResDtos = new ArrayList<FundDtlResDto>();
		for (FundDtlReport fundDtlReport : fundDtlReports) {
			if (fundReportSearchReqDto.getCapitalAccountType() != null
					&& fundReportSearchReqDto.getCapitalAccountType() == BaseConsts.TWO
					&& fundReportSearchReqDto.getStatisticsDimension() == BaseConsts.TWO) {
				fundDtlReport.setCurBalance(DecimalUtil.multiply(new BigDecimal("-1"), fundDtlReport.getCurBalance()));
			}
			FundDtlResDto fundDtlResDto = convertToDtl(fundDtlReport);
			fundDtlResDto.setBeginBalance(beginBlance);
			fundDtlResDto.setBalance(fundDtlReport.getCurBalance().add(beginBlance));
			beginBlance = fundDtlResDto.getBalance();
			fundDtlResDtos.add(fundDtlResDto);
		}
		return fundDtlResDtos;
	}

	private FundDtlResDto convertToDtl(FundDtlReport fundDtlReport) {
		FundDtlResDto fundDtlResDto = new FundDtlResDto();
		BeanUtils.copyProperties(fundDtlReport, fundDtlResDto);
		if (null != fundDtlReport.getCapitalAccountType()) {
			fundDtlResDto.setCapitalAccountTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.CAPITAL_ACCOUNT_TYPE,
					fundDtlReport.getCapitalAccountType() + ""));
			fundDtlResDto.setCapitalAccountType(fundDtlReport.getCapitalAccountType());
		}
		fundDtlResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				fundDtlReport.getCurrencyType() + ""));
		if (null != fundDtlReport.getProjectId()) {
			BaseProject baseProject = cacheService.getProjectById(fundDtlReport.getProjectId());
			fundDtlResDto.setProjectName(cacheService.getProjectNameById(fundDtlReport.getProjectId()));
			BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseProject.getDepartmentId());
			fundDtlResDto.setDepartmentName(baseDepartment.getName());
		}
		return fundDtlResDto;
	}

	public List<FundReportResDto> refreshFundJob(FundReportSearchReqDto fundReportSearchReqDto) {
		List<FundReportResDto> fundReportResDtos = queryListByCon(fundReportSearchReqDto, false);
		return fundReportResDtos;
	}

	public int queryFundReportCount(FundReportSearchReqDto fundReportSearchReqDto) {
		return fundReportDao.queryFundReportCount(fundReportSearchReqDto);
	}

	public int deleteFundReport(FundReportSearchReqDto fundReportSearchReqDto) {
		return fundReportDao.deleteFundReport(fundReportSearchReqDto);
	}
}
