package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.ReceiveLineReportDao;
import com.scfs.dao.report.ReceiveReportDao;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.fi.dto.req.VoucherLineSearchReqDto;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.report.entity.ReceiveLineReport;
import com.scfs.domain.report.entity.ReceiveReport;
import com.scfs.domain.report.req.ReceiveReportSearchReq;
import com.scfs.domain.report.resp.ReceiveLineReportResDto;
import com.scfs.domain.report.resp.ReceiveReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: ReceiveReportService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月13日				Administrator
 *
 * </pre>
 */
@Service
public class ReceiveReportService {
	@Autowired
	private ReceiveReportDao receiveReportDao;
	@Autowired
	private ReceiveLineReportDao receiveLineReportDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private VoucherLineDao voucherLineDao;
	@Autowired
	private ReportProjectService reportProjectService;

	public PageResult<ReceiveReportResDto> queryResultByCon(ReceiveReportSearchReq receiveReportSearchReq) {
		receiveReportSearchReq.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.TWO));

		PageResult<ReceiveReportResDto> pageResult = new PageResult<ReceiveReportResDto>();
		if (!StringUtils.isEmpty(receiveReportSearchReq.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(receiveReportSearchReq.getDepartmentId().split(","));
			receiveReportSearchReq.setDepartmentList(departmentList);
		}
		receiveReportSearchReq.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(receiveReportSearchReq.getPage(), receiveReportSearchReq.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, receiveReportSearchReq.getPer_page());
		List<ReceiveReportResDto> receiveReportResDtos = convertToResult(
				receiveReportDao.queryResultsByCon(receiveReportSearchReq, rowBounds), receiveReportSearchReq);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), receiveReportSearchReq.getPer_page());
		pageResult.setItems(receiveReportResDtos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(receiveReportSearchReq.getPage());
		pageResult.setPer_page(receiveReportSearchReq.getPer_page());
		if (receiveReportSearchReq.getNeedSum() != null && receiveReportSearchReq.getNeedSum().equals(BaseConsts.ONE)) {
			List<ReceiveReport> receiveReports = receiveReportDao.querySumByCon(receiveReportSearchReq);
			BigDecimal balance = BigDecimal.ZERO;
			BigDecimal expireRecAmount = BigDecimal.ZERO;
			BigDecimal expireAmount1 = BigDecimal.ZERO;
			BigDecimal expireAmount2 = BigDecimal.ZERO;
			BigDecimal expireAmount3 = BigDecimal.ZERO;
			BigDecimal adventAmount1 = BigDecimal.ZERO;
			BigDecimal adventAmount2 = BigDecimal.ZERO;
			BigDecimal adventAmount3 = BigDecimal.ZERO;
			for (ReceiveReport receiveReport : receiveReports) {
				balance = DecimalUtil.add(balance, ServiceSupport.amountNewToRMB(receiveReport.getBalance(),
						receiveReport.getCurrencyType(), new Date()));
				expireRecAmount = DecimalUtil.add(expireRecAmount, ServiceSupport.amountNewToRMB(
						receiveReport.getExpireRecAmount(), receiveReport.getCurrencyType(), new Date()));
				expireAmount1 = DecimalUtil.add(expireAmount1, ServiceSupport
						.amountNewToRMB(receiveReport.getExpireAmount1(), receiveReport.getCurrencyType(), new Date()));
				expireAmount2 = DecimalUtil.add(expireAmount2, ServiceSupport
						.amountNewToRMB(receiveReport.getExpireAmount2(), receiveReport.getCurrencyType(), new Date()));
				expireAmount3 = DecimalUtil.add(expireAmount3, ServiceSupport
						.amountNewToRMB(receiveReport.getExpireAmount3(), receiveReport.getCurrencyType(), new Date()));
				adventAmount1 = DecimalUtil.add(adventAmount1, ServiceSupport
						.amountNewToRMB(receiveReport.getAdventAmount1(), receiveReport.getCurrencyType(), new Date()));
				adventAmount2 = DecimalUtil.add(adventAmount2, ServiceSupport
						.amountNewToRMB(receiveReport.getAdventAmount2(), receiveReport.getCurrencyType(), new Date()));
				adventAmount3 = DecimalUtil.add(adventAmount3, ServiceSupport
						.amountNewToRMB(receiveReport.getAdventAmount3(), receiveReport.getCurrencyType(), new Date()));
			}
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("应收金额: ");
			sBuilder.append(DecimalUtil.formatScale2(balance));
			sBuilder.append(" CNY  超期应收金额:  ");
			sBuilder.append(DecimalUtil.formatScale2(expireRecAmount));
			sBuilder.append(" CNY  超期1-7天金额:  ");
			sBuilder.append(DecimalUtil.formatScale2(expireAmount1));
			sBuilder.append(" CNY  超期8-15天金额:  ");
			sBuilder.append(DecimalUtil.formatScale2(expireAmount2));
			sBuilder.append(" CNY  超期15天以上金额:  ");
			sBuilder.append(DecimalUtil.formatScale2(expireAmount3));
			sBuilder.append(" CNY  临期0-7天金额:  ");
			sBuilder.append(DecimalUtil.formatScale2(adventAmount1));
			sBuilder.append(" CNY  临期8到15天金额:  ");
			sBuilder.append(DecimalUtil.formatScale2(adventAmount2));
			sBuilder.append(" CNY  临期15天以上金额:  ");
			sBuilder.append(DecimalUtil.formatScale2(adventAmount3));
			pageResult.setTotalStr(sBuilder.toString());
		}
		return pageResult;
	}

	public PageResult<ReceiveLineReportResDto> queryResultDetailByCon(ReceiveReportSearchReq receiveReportSearchReq) {
		receiveReportSearchReq.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.TWO));

		PageResult<ReceiveLineReportResDto> pageResult = new PageResult<ReceiveLineReportResDto>();
		if (!StringUtils.isEmpty(receiveReportSearchReq.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(receiveReportSearchReq.getDepartmentId().split(","));
			receiveReportSearchReq.setDepartmentList(departmentList);
		}
		receiveReportSearchReq.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(receiveReportSearchReq.getPage(), receiveReportSearchReq.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, receiveReportSearchReq.getPer_page());
		List<ReceiveLineReportResDto> receiveReportResDtos = convertToLineResult(
				receiveLineReportDao.queryResultDetialByCon(receiveReportSearchReq, rowBounds));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), receiveReportSearchReq.getPer_page());
		String totalStr = "";
		List<ReceiveLineReport> billTypeSums = receiveLineReportDao.querySumByCon(receiveReportSearchReq);
		String currencySymbol = "";
		if (receiveReportSearchReq.getCurrencyType() == BaseConsts.ONE) {
			currencySymbol = "CNY";
		} else if (receiveReportSearchReq.getCurrencyType() == BaseConsts.TWO) {
			currencySymbol = "USD";
		} else if (receiveReportSearchReq.getCurrencyType() == BaseConsts.THREE) {
			currencySymbol = "HKD";
		}
		if (!CollectionUtils.isEmpty(billTypeSums)) {
			for (ReceiveLineReport receiveLineReport : billTypeSums) {
				if (!StringUtils.isEmpty(receiveLineReport.getBillType())) {
					totalStr += ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE,
							receiveLineReport.getBillType() + "") + "总金额: "
							+ DecimalUtil.formatScale2(receiveLineReport.getBalance()) + currencySymbol + "   ";
				}
			}
		}
		pageResult.setItems(receiveReportResDtos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(receiveReportSearchReq.getPage());
		pageResult.setPer_page(receiveReportSearchReq.getPer_page());
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	public List<ReceiveReportResDto> queryListByCon(ReceiveReportSearchReq receiveReportSearchReq) {
		receiveReportSearchReq.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.TWO));
		if (!StringUtils.isEmpty(receiveReportSearchReq.getDepartmentId())) {
			List<String> departmentList = Arrays.asList(receiveReportSearchReq.getDepartmentId().split(","));
			receiveReportSearchReq.setDepartmentList(departmentList);
		}
		receiveReportSearchReq.setUserId(ServiceSupport.getUser().getId());
		return convertToResult(receiveReportDao.queryResultsByCon(receiveReportSearchReq), receiveReportSearchReq);
	}

	private List<ReceiveReportResDto> convertToResult(List<ReceiveReport> receiveReports,
			ReceiveReportSearchReq receiveReportSearchReq) {
		List<ReceiveReportResDto> receiveReportResDtos = new ArrayList<ReceiveReportResDto>();
		if (CollectionUtils.isEmpty(receiveReports)) {
			return receiveReportResDtos;
		}
		for (ReceiveReport receiveReport : receiveReports) {
			ReceiveReportResDto receiveReportResDto = new ReceiveReportResDto();
			BeanUtils.copyProperties(receiveReport, receiveReportResDto);
			if (!StringUtils.isEmpty(receiveReport.getCustId())) {
				receiveReportResDto.setCustName(
						cacheService.getSubjectNcByIdAndKey(receiveReport.getCustId(), CacheKeyConsts.CUSTOMER));
			}
			VoucherLineSearchReqDto voucherLineSearchReq = new VoucherLineSearchReqDto();
			voucherLineSearchReq.setUserId(receiveReportSearchReq.getUserId());
			voucherLineSearchReq.setStatisticsDimension(receiveReportSearchReq.getStatisticsDimension());
			voucherLineSearchReq.setDebitOrCredit(BaseConsts.ONE);
			voucherLineSearchReq.setCustId(receiveReport.getCustId());
			voucherLineSearchReq.setProjectId(receiveReport.getProjectId());
			voucherLineSearchReq.setCurrencyType(receiveReport.getCurrencyType());
			List<VoucherLine> voucherLines = voucherLineDao.queryGroupResultsByCon(voucherLineSearchReq);
			if (!CollectionUtils.isEmpty(voucherLines) && voucherLines.size() == 1) {
				VoucherLine voucherLine = voucherLines.get(0);
				receiveReportResDto.setUnCheckAmount(
						DecimalUtil.formatScale2(voucherLine.getAmount().subtract(voucherLine.getAmountChecked())));
			} else {
				receiveReportResDto.setUnCheckAmount(BigDecimal.ZERO);
			}
			if (DecimalUtil.eq(receiveReportResDto.getBalance(), DecimalUtil.ZERO)
					&& DecimalUtil.eq(receiveReportResDto.getUnCheckAmount(), DecimalUtil.ZERO)) {
				continue;
			}
			if (!StringUtils.isEmpty(receiveReport.getProjectId())) {
				receiveReportResDto.setProjectName(cacheService.getProjectNameById(receiveReport.getProjectId()));
				BaseProject baseProject = cacheService.getProjectById(receiveReport.getProjectId());
				BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseProject.getDepartmentId());
				receiveReportResDto
						.setBizManagerName(cacheService.getUserChineseNameByid(baseProject.getBizSpecialId()));
				receiveReportResDto.setDepartmentName(baseDepartment.getName());
				receiveReportResDto.setProjectName(baseProject.getProjectNo() + "-" + baseProject.getProjectName());
			}
			receiveReportResDto.setCurrencyTypeName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, receiveReport.getCurrencyType() + ""));
			receiveReportResDto.setBillTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, receiveReport.getBillType() + ""));
			receiveReportResDtos.add(receiveReportResDto);
		}
		return receiveReportResDtos;
	}

	private List<ReceiveLineReportResDto> convertToLineResult(List<ReceiveLineReport> receiveLineReports) {
		List<ReceiveLineReportResDto> receiveLineReportResDtos = new ArrayList<ReceiveLineReportResDto>();
		if (CollectionUtils.isEmpty(receiveLineReports)) {
			return receiveLineReportResDtos;
		}
		for (ReceiveLineReport receiveLineReport : receiveLineReports) {
			ReceiveLineReportResDto receiveLineReportResDto = new ReceiveLineReportResDto();
			BeanUtils.copyProperties(receiveLineReport, receiveLineReportResDto);
			if (!StringUtils.isEmpty(receiveLineReport.getCustId())) {
				receiveLineReportResDto.setCustName(
						cacheService.getSubjectNcByIdAndKey(receiveLineReport.getCustId(), CacheKeyConsts.CUSTOMER));
			}
			if (!StringUtils.isEmpty(receiveLineReport.getProjectId())) {
				receiveLineReportResDto
						.setProjectName(cacheService.getProjectNameById(receiveLineReport.getProjectId()));
				BaseProject baseProject = cacheService.getProjectById(receiveLineReport.getProjectId());
				BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseProject.getDepartmentId());
				receiveLineReportResDto
						.setBizManagerName(cacheService.getUserChineseNameByid(baseProject.getBizSpecialId()));
				receiveLineReportResDto.setDepartmentName(baseDepartment.getName());
				receiveLineReportResDto.setProjectName(baseProject.getProjectNo() + "-" + baseProject.getProjectName());
			}
			receiveLineReportResDto.setCurrencyTypeName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, receiveLineReport.getCurrencyType() + ""));
			receiveLineReportResDto.setBillTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, receiveLineReport.getBillType() + ""));
			receiveLineReportResDtos.add(receiveLineReportResDto);
		}
		return receiveLineReportResDtos;
	}
}
