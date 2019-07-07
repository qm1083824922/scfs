package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.api.pms.PmsPayPoRelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.DistributionBillsReportDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.report.entity.DistributionBillsReport;
import com.scfs.domain.report.req.DistributionBillsReportReqDto;
import com.scfs.domain.report.resp.DistributionBillsReportModel;
import com.scfs.domain.report.resp.DistributionBillsReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  铺货对账单信息
 *  File: DistributionBillsReportService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2017年09月12日         Administrator
 *
 * </pre>
 */
@Service
public class DistributionBillsReportService {
	@Autowired
	private DistributionBillsReportDao distributionBillsReportDao;
	@Autowired
	private PmsPayPoRelDao pmsPayPoRelDao;
	@Autowired
	private CacheService cacheService;

	/**
	 * 获取列表信息
	 * 
	 * @param customerMaintainReqDto
	 * @return
	 */
	public PageResult<DistributionBillsReportResDto> queryDistributionBillsReportResultsByCon(
			DistributionBillsReportReqDto reqDto) {
		PageResult<DistributionBillsReportResDto> result = new PageResult<DistributionBillsReportResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.PRINT_DISTRIBUTION_POWER)) {// 判断用户是否拥有权限
			reqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<DistributionBillsReport> distributionBillsReportList = distributionBillsReportDao.queryResultsByCon(reqDto,
				rowBounds);
		List<DistributionBillsReportResDto> sumResDto = convertToResult(distributionBillsReportList, null);
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			BigDecimal requiredSendNum = BigDecimal.ZERO;
			BigDecimal requiredSendAmount = BigDecimal.ZERO;
			BigDecimal discountAmount = BigDecimal.ZERO;
			BigDecimal payAmount = BigDecimal.ZERO;
			BigDecimal retainageAmount = BigDecimal.ZERO;
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				for (DistributionBillsReportResDto resDto : sumResDto) {
					if (resDto != null) {
						requiredSendNum = DecimalUtil.add(requiredSendNum, resDto.getRequiredSendNum());
						requiredSendAmount = DecimalUtil.add(requiredSendAmount, resDto.getRequiredSendAmount());
						discountAmount = DecimalUtil.add(discountAmount, resDto.getDiscountAmount());
						payAmount = DecimalUtil.add(payAmount, resDto.getPayAmount());
						retainageAmount = DecimalUtil.add(retainageAmount, resDto.getRetainageAmount());
					}
				}
			}
			String totalStr = " 销售数量  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(requiredSendNum))
					+ " &nbsp;&nbsp;销售金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(requiredSendAmount))
					+ " &nbsp;&nbsp;服务费  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(discountAmount))
					+ " &nbsp;&nbsp;已付金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmount))
					+ " &nbsp;&nbsp;应付尾款  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(retainageAmount))
					+ " &nbsp;";
			result.setTotalStr(totalStr);

		}
		result.setItems(sumResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 铺货预算对账单:服务费=预付金额*天数*条款的费率
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<DistributionBillsReportResDto> queryDistributionBudgetBillsReportResultsByCon(
			DistributionBillsReportReqDto reqDto) {
		PageResult<DistributionBillsReportResDto> result = new PageResult<DistributionBillsReportResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.PRINT_DISTRIBUTION_POWER)) {// 判断用户是否拥有权限
			reqDto.setUserId(ServiceSupport.getUser().getId());
		}
		if (reqDto.getEndPayCreateDate() == null) {
			reqDto.setEndPayCreateDate(new Date());
		}
		if (reqDto.getStartPayCreateDate() == null) {
			reqDto.setStartPayCreateDate(DateFormatUtils.beforeDay(reqDto.getEndPayCreateDate(), BaseConsts.SEVEN));
		}
		List<DistributionBillsReport> distributionBillsReportList = distributionBillsReportDao
				.queryBudgetResultsByCon(reqDto, rowBounds);
		List<DistributionBillsReportResDto> distributionResList = convertToResult(distributionBillsReportList,
				reqDto.getPayCreateDate());// 结算日期动态变更
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			BigDecimal requiredSendNum = BigDecimal.ZERO;
			BigDecimal requiredSendAmount = BigDecimal.ZERO;
			BigDecimal discountAmount = BigDecimal.ZERO;
			BigDecimal retainageAmount = BigDecimal.ZERO;
			if (CollectionUtils.isNotEmpty(distributionResList)) {
				for (DistributionBillsReportResDto resDto : distributionResList) {
					if (resDto != null) {
						requiredSendNum = DecimalUtil.add(requiredSendNum, resDto.getRequiredSendNum());
						requiredSendAmount = DecimalUtil.add(requiredSendAmount, resDto.getRequiredSendAmount());
						discountAmount = DecimalUtil.add(discountAmount, resDto.getDiscountAmount());
						retainageAmount = DecimalUtil.add(retainageAmount, resDto.getRetainageAmount());
					}
				}
			}
			String totalStr = " 销售数量  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(requiredSendNum))
					+ " &nbsp;&nbsp;销售金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(requiredSendAmount))
					+ " &nbsp;&nbsp;服务费  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(discountAmount))
					+ " &nbsp;&nbsp;尾款  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(retainageAmount))
					+ " &nbsp;";
			result.setTotalStr(totalStr);
		}
		result.setItems(distributionResList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(reqDto.getPage());
		result.setPer_page(reqDto.getPer_page());
		return result;
	}

	/**
	 * 铺货对账单打印及合并打印数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public Result<DistributionBillsReportModel> queryDistributionBillsPrintResultsByCon(
			DistributionBillsReportReqDto reqDto) {
		Result<DistributionBillsReportModel> result = new Result<DistributionBillsReportModel>();
		DistributionBillsReportModel model = new DistributionBillsReportModel();
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.PRINT_DISTRIBUTION_POWER)) {// 判断用户是否拥有权限
			reqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<DistributionBillsReport> distributionBillsReportList = distributionBillsReportDao
				.queryResultsByCon(reqDto);
		List<DistributionBillsReportResDto> resList = convertToResult(distributionBillsReportList, null);
		if (CollectionUtils.isNotEmpty(resList)) {
			model.setPayCreateTime(reqDto.getEndPayCreateDate());
			model.setOldPayCreateTime(reqDto.getStartPayCreateDate());
			model.setProjectName(resList.get(BaseConsts.ZERO).getProjectName());
			model.setBusinessUnitName(resList.get(BaseConsts.ZERO).getBusinessUnitName());
			model.setSupplierName(resList.get(BaseConsts.ZERO).getSupplierName());
			model.setSupplierChineseName(resList.get(BaseConsts.ZERO).getSupplierChineseName());
			model.setCurrencyName(resList.get(BaseConsts.ZERO).getCurrencyName());
			model.setResDtoList(resList);
		}
		result.setItems(model);
		return result;
	}

	/**
	 * 铺货预算对账单打印及合并打印数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public Result<DistributionBillsReportModel> queryDistributionBudgetBillsPrintResultsByCon(
			DistributionBillsReportReqDto reqDto) {
		Result<DistributionBillsReportModel> result = new Result<DistributionBillsReportModel>();
		DistributionBillsReportModel model = new DistributionBillsReportModel();
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.PRINT_DISTRIBUTION_POWER)) {// 判断用户是否拥有权限
			reqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<DistributionBillsReport> distributionBillsReportList = distributionBillsReportDao
				.queryBudgetResultsByCon(reqDto);
		List<DistributionBillsReportResDto> resList = convertToResult(distributionBillsReportList,
				reqDto.getPayCreateDate());
		if (CollectionUtils.isNotEmpty(resList)) {
			model.setPayCreateTime(reqDto.getEndPayCreateDate());
			model.setOldPayCreateTime(reqDto.getStartPayCreateDate());
			model.setProjectName(resList.get(BaseConsts.ZERO).getProjectName());
			model.setBusinessUnitName(resList.get(BaseConsts.ZERO).getBusinessUnitName());
			model.setSupplierName(resList.get(BaseConsts.ZERO).getSupplierName());
			model.setSupplierChineseName(resList.get(BaseConsts.ZERO).getSupplierChineseName());
			model.setCurrencyName(resList.get(BaseConsts.ZERO).getCurrencyName());
			model.setResDtoList(resList);
		}
		result.setItems(model);
		return result;
	}

	/**
	 * 浏览信息
	 * 
	 * @param supplierAuth
	 * @return
	 */
	public Result<DistributionBillsReportResDto> queryDistributionBillsReportServiceById(
			DistributionBillsReport distributionBills) {
		Result<DistributionBillsReportResDto> result = new Result<DistributionBillsReportResDto>();
		DistributionBillsReport distribution = distributionBillsReportDao.queryEntityById(distributionBills.getId());
		result.setItems(convertToResDto(distribution));
		return result;
	}

	private List<DistributionBillsReportResDto> convertToResult(
			List<DistributionBillsReport> distributionBillsReportList, Date isBudget) {
		List<DistributionBillsReportResDto> distributionResDtoList = new ArrayList<DistributionBillsReportResDto>();
		if (CollectionUtils.isEmpty(distributionBillsReportList)) {
			return distributionResDtoList;
		}
		for (DistributionBillsReport distributionBillsReport : distributionBillsReportList) {
			DistributionBillsReportResDto restDto = convertToResDto(distributionBillsReport);
			if (isBudget != null) {// 铺货预算对账单, 服务费=预付金额*天数*条款的费率
				restDto.setPayCreateTime(isBudget);
				BigDecimal discountAmount = BigDecimal.ZERO;
				ProjectItem projectItem = cacheService.getProjectItemByPid(restDto.getProjectId());
				if (projectItem != null) {
					Integer occupyDay = restDto.getOccupyDay()
							+ (projectItem.getPayCycle() == null ? BaseConsts.ZERO : projectItem.getPayCycle());// 结算日期=结算日期+条款付款周期
					discountAmount = DecimalUtil.multiply(restDto.getBudgetAmount(), new BigDecimal(occupyDay));
					discountAmount = DecimalUtil.multiply(discountAmount,
							projectItem.getFundMonthRate() == null ? BigDecimal.ZERO : projectItem.getFundMonthRate());
					restDto.setOccupyDay(occupyDay);
					restDto.setRetainageAmount(DecimalUtil
							.formatScale2(DecimalUtil.subtract(restDto.getRetainageAmount(), discountAmount)));// 尾款=尾款-服务费
				}
				restDto.setRequiredSendAmount(DecimalUtil.formatScale2(restDto.getRequiredSendAmount()));
				restDto.setBudgetAmount(DecimalUtil.formatScale2(restDto.getBudgetAmount()));
				restDto.setDiscountAmount(DecimalUtil.formatScale2(discountAmount));
			}
			restDto.setOpertaList(getOperList(distributionBillsReport));
			distributionResDtoList.add(restDto);
		}
		return distributionResDtoList;
	}

	/**
	 * @param model
	 * @return
	 */
	public DistributionBillsReportResDto convertToResDto(DistributionBillsReport model) {
		DistributionBillsReportResDto result = new DistributionBillsReportResDto();
		if (model != null) {
			result.setId(model.getId());
			result.setPayId(model.getPayId());
			result.setPoId(model.getPoId());
			result.setLineId(model.getLineId());
			result.setGoodsId(model.getGoodsId());
			result.setDeliveryId(model.getDeliveryId());
			result.setDeliveryDtlId(model.getDeliveryDtlId());
			result.setProjectId(model.getProjectId());
			result.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
			result.setBusinessUnitId(model.getBusinessUnitId());
			result.setBusinessUnitName(
					cacheService.showSubjectNameByIdAndKey(model.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
			result.setSupplierId(model.getSupplierId());
			result.setSupplierName(
					cacheService.showSubjectNameByIdAndKey(model.getSupplierId(), CacheKeyConsts.SUPPLIER));
			BaseSubject baseSubject = cacheService.getSubjectById(model.getSupplierId(), CacheKeyConsts.SUPPLIER);
			if (baseSubject != null) {
				result.setSupplierChineseName(baseSubject.getChineseName());
			}
			result.setOrderNo(model.getOrderNo());
			result.setAppendNo(model.getAppendNo());
			String consignmentOrderNo = pmsPayPoRelDao.queryOrderNoByLineId(model.getLineId());
			result.setConsignmentOrderNo(consignmentOrderNo);
			;
			result.setNumber(model.getNumber());
			result.setGoodsPrice(model.getGoodsPrice());
			result.setCurrencyId(model.getCurrencyId());
			result.setCurrencyName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrencyId() + ""));
			result.setBatchNum(model.getBatchNum());
			result.setRequiredSendNum(model.getRequiredSendNum());
			result.setRequiredSendAmount(model.getRequiredSendAmount());
			result.setPledge(model.getPledge());
			result.setPayTime(model.getPayTime());
			result.setBudgetAmount(model.getBudgetAmount());
			result.setPayCreateTime(model.getPayCreateTime());
			result.setOccupyDay(model.getOccupyDay());
			result.setDiscountAmount(model.getDiscountAmount());
			result.setPayAmount(model.getPayAmount());
			result.setRetainageAmount(model.getRetainageAmount());
		}
		return result;
	}

	private List<CodeValue> getOperList(DistributionBillsReport distributionBillsReport) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(distributionBillsReport);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				DistributionBillsReportResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(DistributionBillsReport distributionBillsReport) {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.PRINT);
		return opertaList;
	}
}
