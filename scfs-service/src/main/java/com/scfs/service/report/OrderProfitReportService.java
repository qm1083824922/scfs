package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.OrderProfitReportDao;
import com.scfs.dao.report.ProfitReportMonthDao;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.entity.OrderProfitReport;
import com.scfs.domain.report.entity.OrderProfitReportSum;
import com.scfs.domain.report.req.OrderProfitReportReqDto;
import com.scfs.domain.report.req.ProfitReportReqMonthDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * Created by Administrator on 2017年3月18日.
 */
@Service
public class OrderProfitReportService {
	@Autowired
	private OrderProfitReportDao orderProfitReportDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private ProfitReportMonthDao profitReportMonthDao;
	@Autowired
	private ReportProjectService reportProjectService;

	/**
	 * 查询订单利润报表
	 * 
	 * @param orderProfitReportReqDto
	 * @return
	 */
	public PageResult<OrderProfitReport> queryResultsByCon(OrderProfitReportReqDto orderProfitReportReqDto) {
		orderProfitReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.FIVE));

		PageResult<OrderProfitReport> result = new PageResult<OrderProfitReport>();
		int offSet = PageUtil.getOffSet(orderProfitReportReqDto.getPage(), orderProfitReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, orderProfitReportReqDto.getPer_page());

		orderProfitReportReqDto.setUserId(ServiceSupport.getUser().getId());
		Integer isQueryAllFeeManage = 0; // 0-查询部门下管理费用
		if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_MANUAL_FEE_MANAGE_POWER)) {
			isQueryAllFeeManage = 1;
		}
		if (isQueryAllFeeManage == 0) {
			List<Integer> departmentId = new ArrayList<Integer>();
			Integer dId = ServiceSupport.getUser().getDepartmentId();
			if (null != dId) {
				departmentId.add(dId);
			}
			if (!CollectionUtils.isEmpty(departmentId)) {
				orderProfitReportReqDto.setManageFeeDepartmentId(departmentId);
			}
		}
		List<OrderProfitReport> orderProfitReportList = orderProfitReportDao
				.queryOrderProfitReportResultsByCon(orderProfitReportReqDto, rowBounds);
		List<OrderProfitReport> orderProfitReportResList = convertToResDto(orderProfitReportList);
		result.setItems(orderProfitReportResList);

		String totalStr = querySumOrderProfitReport(orderProfitReportReqDto);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), orderProfitReportReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(orderProfitReportReqDto.getPage());
		result.setPer_page(orderProfitReportReqDto.getPer_page());
		return result;
	}

	/**
	 * 查询利润报表合计
	 * 
	 * @param orderProfitReportReqDto
	 * @return
	 */
	private String querySumOrderProfitReport(OrderProfitReportReqDto orderProfitReportReqDto) {
		String totalStr = "";
		if (orderProfitReportReqDto.getNeedSum() != null && orderProfitReportReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<OrderProfitReportSum> orderProfitReportSumList = orderProfitReportDao
					.querySumOrderProfitReportByCon(orderProfitReportReqDto);
			if (!CollectionUtils.isEmpty(orderProfitReportSumList)) {
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

				for (OrderProfitReportSum orderProfitReportSum : orderProfitReportSumList) {
					totalNum = DecimalUtil.add(totalNum,
							null == orderProfitReportSum.getNum() ? BigDecimal.ZERO : orderProfitReportSum.getNum());
					totalSaleAmount = DecimalUtil.add(totalSaleAmount, null == orderProfitReportSum.getSaleAmount()
							? BigDecimal.ZERO : orderProfitReportSum.getSaleAmount());
					totalServiceAmount = DecimalUtil.add(totalServiceAmount,
							null == orderProfitReportSum.getServiceAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getServiceAmount());
					totalCompositeTaxAmount = DecimalUtil.add(totalCompositeTaxAmount,
							null == orderProfitReportSum.getCompositeTaxAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getCompositeTaxAmount());
					totalPurchaseCost = DecimalUtil.add(totalPurchaseCost,
							null == orderProfitReportSum.getPurchaseCost() ? BigDecimal.ZERO
									: orderProfitReportSum.getPurchaseCost());
					totalFundCost = DecimalUtil.add(totalFundCost, null == orderProfitReportSum.getFundCost()
							? BigDecimal.ZERO : orderProfitReportSum.getFundCost());
					totalWarehouseAmount = DecimalUtil.add(totalWarehouseAmount,
							null == orderProfitReportSum.getWarehouseAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getWarehouseAmount());
					totalMarketAmount = DecimalUtil.add(totalMarketAmount,
							null == orderProfitReportSum.getMarketAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getMarketAmount());
					totalFinanceAmount = DecimalUtil.add(totalFinanceAmount,
							null == orderProfitReportSum.getFinanceAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getFinanceAmount());
					totalManageAmount = DecimalUtil.add(totalManageAmount,
							null == orderProfitReportSum.getManageAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getManageAmount());
					totalManualAmount = DecimalUtil.add(totalManualAmount,
							null == orderProfitReportSum.getManualAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getManualAmount());
					totalProfitAmount = DecimalUtil.add(totalProfitAmount,
							null == orderProfitReportSum.getProfitAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getProfitAmount());
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
		}
		return totalStr;
	}

	/**
	 * 查询订单利润报表(全部数据)
	 * 
	 * @param orderProfitReportReqDto
	 * @return
	 */
	public List<OrderProfitReport> queryAllResultsByCon(OrderProfitReportReqDto orderProfitReportReqDto) {
		orderProfitReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.FIVE));
		if (null == orderProfitReportReqDto.getUserId()) {
			orderProfitReportReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		Integer isQueryAllFeeManage = 0; // 0-查询部门下管理费用
		if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_MANUAL_FEE_MANAGE_POWER)) {
			isQueryAllFeeManage = 1;
		}
		if (isQueryAllFeeManage == 0) {
			List<Integer> departmentId = new ArrayList<Integer>();
			Integer dId = ServiceSupport.getUser().getDepartmentId();
			if (null != dId) {
				departmentId.add(dId);
			}
			if (!CollectionUtils.isEmpty(departmentId)) {
				orderProfitReportReqDto.setManageFeeDepartmentId(departmentId);
			}
		}

		List<OrderProfitReport> orderProfitReportList = orderProfitReportDao
				.queryOrderProfitReportResultsByCon(orderProfitReportReqDto);
		List<OrderProfitReport> orderProfitReportResList = convertToResDto(orderProfitReportList);
		return orderProfitReportResList;
	}

	public List<OrderProfitReport> queryAllResultsByCon4Admin(OrderProfitReportReqDto orderProfitReportReqDto) {
		orderProfitReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.FIVE));
		List<OrderProfitReport> orderProfitReportList = orderProfitReportDao
				.queryOrderProfitReportResultsByCon(orderProfitReportReqDto);
		List<OrderProfitReport> orderProfitReportResList = convertToResDto(orderProfitReportList);
		return orderProfitReportResList;
	}

	private List<OrderProfitReport> convertToResDto(List<OrderProfitReport> orderProfitReportList) {
		List<OrderProfitReport> orderProfitReportResList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(orderProfitReportList)) {
			return orderProfitReportResList;
		}
		for (OrderProfitReport orderProfitReport : orderProfitReportList) {
			OrderProfitReport orderProfitReportRes = convertToResDto(orderProfitReport);
			orderProfitReportResList.add(orderProfitReportRes);
		}
		return orderProfitReportResList;
	}

	private OrderProfitReport convertToResDto(OrderProfitReport orderProfitReport) {
		OrderProfitReport orderProfitReportRes = new OrderProfitReport();
		BeanUtils.copyProperties(orderProfitReport, orderProfitReportRes);
		if (null != orderProfitReportRes.getProjectId()) {
			Integer businessUnitId = cacheService.getProjectById(orderProfitReportRes.getProjectId())
					.getBusinessUnitId();
			orderProfitReportRes.setProjectName(cacheService.showProjectNameById(orderProfitReportRes.getProjectId()));
			orderProfitReportRes.setBusinessUnitName(
					cacheService.showSubjectNameByIdAndKey(businessUnitId, CacheKeyConsts.BUSI_UNIT));
			orderProfitReportRes.setBusinessUnitId(businessUnitId);
		}
		if (null != orderProfitReportRes.getCustomerId()) {
			orderProfitReportRes.setCustomerName(cacheService
					.showSubjectNameByIdAndKey(orderProfitReportRes.getCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		if (null != orderProfitReportRes.getWarehouseId()) {
			orderProfitReportRes.setWarehouseName(cacheService
					.showSubjectNameByIdAndKey(orderProfitReportRes.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		}

		if (null != orderProfitReportRes.getDepartmentId()) {
			BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(orderProfitReportRes.getDepartmentId());
			if (null != baseDepartment) {
				orderProfitReportRes.setDepartmentName(baseDepartment.getName());
			}
		}
		if (null != orderProfitReportRes.getBizManagerId()) {
			orderProfitReportRes
					.setBizManagerName(cacheService.getUserChineseNameByid(orderProfitReportRes.getBizManagerId()));
		}

		if (null != orderProfitReportRes.getBizType()) {
			orderProfitReportRes.setBizTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE,
					Integer.toString(orderProfitReportRes.getBizType())));
		}
		if (null != orderProfitReportRes.getBillType()) {
			orderProfitReportRes.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PERFORMANCE_BILL_TYPE,
					Integer.toString(orderProfitReportRes.getBillType())));
		}
		if (orderProfitReportRes.getBillType().equals(BaseConsts.ONE)
				|| orderProfitReportRes.getBillType().equals(BaseConsts.TWO)
				|| orderProfitReportRes.getBillType().equals(BaseConsts.THREE)
				|| orderProfitReportRes.getBillType().equals(BaseConsts.SIX)) {
			orderProfitReportRes.setProductNo(
					cacheService.getFeeSpecNoNameById(Integer.parseInt(orderProfitReportRes.getProductNo())));
		}
		if (orderProfitReportRes.getBillType().equals(BaseConsts.ONE)) { // 1-应收费用
			orderProfitReportRes.setProfitAmount(getProfitAmount(orderProfitReportRes));
			if (null != orderProfitReportRes.getRecAmount()
					&& !DecimalUtil.eq(orderProfitReportRes.getRecAmount(), BigDecimal.ZERO)) {
				orderProfitReportRes.setProfitRate(DecimalUtil.divide(orderProfitReportRes.getProfitAmount(),
						DecimalUtil.subtract(
								DecimalUtil.add(orderProfitReportRes.getSaleAmount(),
										orderProfitReportRes.getServiceAmount()),
								orderProfitReportRes.getCompositeTaxAmount())));
			}
		}
		if (orderProfitReportRes.getBillType().equals(BaseConsts.TWO)
				|| orderProfitReportRes.getBillType().equals(BaseConsts.SIX)) { // 2-应付费用,
																				// 6-管理费用
			orderProfitReportRes.setProfitAmount(getProfitAmount(orderProfitReportRes));
			if (null != orderProfitReportRes.getPayAmount()) {
				if (DecimalUtil.eq(orderProfitReportRes.getProfitAmount(), BigDecimal.ZERO)) {
					orderProfitReportRes.setProfitRate(BigDecimal.ZERO);
				} else if (DecimalUtil.gt(orderProfitReportRes.getProfitAmount(), BigDecimal.ZERO)) {
					orderProfitReportRes.setProfitRate(new BigDecimal("1"));
				} else if (DecimalUtil.lt(orderProfitReportRes.getProfitAmount(), BigDecimal.ZERO)) {
					orderProfitReportRes.setProfitRate(new BigDecimal("-1"));
				}
			}
		}
		if (orderProfitReportRes.getBillType().equals(BaseConsts.THREE)) { // 3-应收应付费用
			orderProfitReportRes.setProfitAmount(getProfitAmount(orderProfitReportRes));
			if (DecimalUtil.eq(orderProfitReportRes.getProfitAmount(), BigDecimal.ZERO)) {
				orderProfitReportRes.setProfitRate(BigDecimal.ZERO);
			} else {
				BigDecimal incomeAmount = DecimalUtil.subtract(orderProfitReportRes.getSaleAmount(), DecimalUtil
						.add(orderProfitReportRes.getCompositeTaxAmount(), orderProfitReportRes.getPurchaseCost()));
				orderProfitReportRes
						.setProfitRate(DecimalUtil.divide(orderProfitReportRes.getProfitAmount(), incomeAmount));
			}
		}
		if (orderProfitReportRes.getBillType().equals(BaseConsts.FOUR)) { // 4-销售单
			orderProfitReportRes.setServiceAmount(BigDecimal.ZERO);
			BaseGoods baseGoods = cacheService.getGoodsById(Integer.parseInt(orderProfitReportRes.getProductNo()));
			if (null != baseGoods) {
				orderProfitReportRes.setProductNo(baseGoods.getNumber());
			} else {
				orderProfitReportRes.setProductNo(null);
			}
			orderProfitReportRes.setProfitAmount(getProfitAmount(orderProfitReportRes));
			if (null != orderProfitReportRes.getSaleAmount()
					&& !DecimalUtil.eq(orderProfitReportRes.getSaleAmount(), BigDecimal.ZERO)) {
				orderProfitReportRes.setProfitRate(DecimalUtil.divide(orderProfitReportRes.getProfitAmount(),
						DecimalUtil.subtract(
								DecimalUtil.add(orderProfitReportRes.getSaleAmount(),
										orderProfitReportRes.getServiceAmount()),
								orderProfitReportRes.getCompositeTaxAmount())));
			}
		}
		if (orderProfitReportRes.getBillType().equals(BaseConsts.FIVE)) { // 5-资金成本
			orderProfitReportRes.setProfitAmount(getProfitAmount(orderProfitReportRes));
			if (orderProfitReportRes.getProfitAmount().compareTo(BigDecimal.ZERO) == 0) {
				orderProfitReportRes.setProfitRate(BigDecimal.ZERO);
			} else if (orderProfitReportRes.getProfitAmount().compareTo(BigDecimal.ZERO) < 0) {
				orderProfitReportRes.setProfitRate(new BigDecimal("-1"));
			} else if (orderProfitReportRes.getProfitAmount().compareTo(BigDecimal.ZERO) > 0) {
				orderProfitReportRes.setProfitRate(new BigDecimal("1"));
			}
		}
		orderProfitReportRes.setSaleAmount(DecimalUtil.formatScale2(orderProfitReportRes.getSaleAmount()));
		orderProfitReportRes.setServiceAmount(DecimalUtil.formatScale2(orderProfitReportRes.getServiceAmount()));
		orderProfitReportRes
				.setCompositeTaxAmount(DecimalUtil.formatScale2(orderProfitReportRes.getCompositeTaxAmount()));
		orderProfitReportRes.setPurchaseCost(DecimalUtil.formatScale2(orderProfitReportRes.getPurchaseCost()));
		orderProfitReportRes.setFundCost(DecimalUtil.formatScale2(orderProfitReportRes.getFundCost()));
		orderProfitReportRes.setWarehouseAmount(DecimalUtil.formatScale2(orderProfitReportRes.getWarehouseAmount()));
		orderProfitReportRes.setManageAmount(DecimalUtil.formatScale2(orderProfitReportRes.getManageAmount()));
		orderProfitReportRes.setMarketAmount(DecimalUtil.formatScale2(orderProfitReportRes.getMarketAmount()));
		orderProfitReportRes.setFinanceAmount(DecimalUtil.formatScale2(orderProfitReportRes.getFinanceAmount()));
		orderProfitReportRes.setManualAmount(DecimalUtil.formatScale2(orderProfitReportRes.getManualAmount()));
		orderProfitReportRes.setProfitAmount(DecimalUtil.formatScale2(orderProfitReportRes.getProfitAmount()));
		orderProfitReportRes.setProfitRate(orderProfitReportRes.getProfitRate().setScale(4, BigDecimal.ROUND_HALF_UP));
		orderProfitReportRes.setProfitRateStr(DecimalUtil.toPercentString(orderProfitReportRes.getProfitRate()));

		if (null != orderProfitReportRes.getCurrencyType()) { // 币种转化成人民币
			orderProfitReportRes.setCurrencyType(BaseConsts.ONE);
			orderProfitReportRes.setCurrencyTypeName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, String.valueOf(BaseConsts.ONE))); // 1-人民币
		}
		return orderProfitReportRes;
	}

	public BigDecimal getProfitAmount(OrderProfitReport orderProfitReportRes) {
		BigDecimal profitAmount = DecimalUtil.subtract(
				DecimalUtil.add(orderProfitReportRes.getSaleAmount(),
						orderProfitReportRes.getServiceAmount()),
				DecimalUtil.add(
						DecimalUtil.add(
								DecimalUtil.add(
										DecimalUtil.add(
												DecimalUtil.add(
														DecimalUtil.add(DecimalUtil.add(
																orderProfitReportRes.getCompositeTaxAmount(),
																orderProfitReportRes.getPurchaseCost())),
														orderProfitReportRes.getFundCost()),
												orderProfitReportRes.getWarehouseAmount()),
										orderProfitReportRes.getManageAmount()),
								orderProfitReportRes.getMarketAmount()),
						orderProfitReportRes.getFinanceAmount()),
				orderProfitReportRes.getManualAmount());
		return profitAmount;
	}

	public boolean isOverOrderProfitReportMaxLine(OrderProfitReportReqDto orderProfitReportReqDto) {
		orderProfitReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.FIVE));

		orderProfitReportReqDto.setUserId(ServiceSupport.getUser().getId());
		Integer isQueryAllFeeManage = 0; // 0-查询部门下管理费用
		if (ServiceSupport.isAllowPerm(BusUrlConsts.QUERY_MANUAL_FEE_MANAGE_POWER)) {
			isQueryAllFeeManage = 1;
		}
		if (isQueryAllFeeManage == 0) {
			List<Integer> departmentId = new ArrayList<Integer>();
			Integer dId = ServiceSupport.getUser().getDepartmentId();
			if (null != dId) {
				departmentId.add(dId);
			}
			if (!CollectionUtils.isEmpty(departmentId)) {
				orderProfitReportReqDto.setManageFeeDepartmentId(departmentId);
			}
		}

		int count = orderProfitReportDao.queryOrderProfitReportCountByCon(orderProfitReportReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("利润报表导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncOrderProfitReportExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/report/orderProfit/order_profit_report_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_22);
			asyncExcelService.addAsyncExcel(orderProfitReportReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncOrderProfitReportExport(OrderProfitReportReqDto orderProfitReportReqDto) {
		orderProfitReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.FIVE));

		Map<String, Object> model = Maps.newHashMap();
		List<OrderProfitReport> orderProfitReportList = queryAllResultsByCon(orderProfitReportReqDto);
		model.put("orderProfitReportList", orderProfitReportList);
		return model;
	}

	public List<MounthProfitReport> queryMounthResults(OrderProfitReportReqDto orderProfitReportReqDto) {
		orderProfitReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.EIGHT));

		List<MounthProfitReport> orderProfitReportList = orderProfitReportDao
				.queryProfitReportMounthResultsByCon(orderProfitReportReqDto);
		List<MounthProfitReport> orderProfitReportResList = convertMounthToResDto(orderProfitReportList);
		return orderProfitReportResList;
	}

	/**
	 * 月利润报表
	 * 
	 * @param orderProfitReportList
	 * @return
	 */
	public PageResult<MounthProfitReport> queryMounthResultsByCon(ProfitReportReqMonthDto profitReportReqMonthDto) {
		profitReportReqMonthDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.EIGHT));
		Date currDate = new Date();
		String preDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, currDate);
		if (StringUtils.isEmpty(profitReportReqMonthDto.getStartStatisticsDate())
				&& StringUtils.isEmpty(profitReportReqMonthDto.getEndStatisticsDate())) {
			profitReportReqMonthDto.setStartStatisticsDate(preDate);
			profitReportReqMonthDto.setEndStatisticsDate(preDate);
		}
		PageResult<MounthProfitReport> result = new PageResult<MounthProfitReport>();
		int offSet = PageUtil.getOffSet(profitReportReqMonthDto.getPage(), profitReportReqMonthDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, profitReportReqMonthDto.getPer_page());
		profitReportReqMonthDto.setUserId(ServiceSupport.getUser().getId());
		List<MounthProfitReport> orderProfitReportList = profitReportMonthDao.queryResultsByCon(profitReportReqMonthDto,
				rowBounds);
		List<MounthProfitReport> mounthProfitReportList = convertMounthToResDto(orderProfitReportList);
		result.setItems(mounthProfitReportList);

		String totalStr = querySumMounthProfitReport(mounthProfitReportList, profitReportReqMonthDto);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), profitReportReqMonthDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(profitReportReqMonthDto.getPage());
		result.setPer_page(profitReportReqMonthDto.getPer_page());
		return result;
	}

	/**
	 * 查询月结利润报表合计
	 * 
	 * @param orderProfitReportReqDto
	 * @return
	 */
	private String querySumMounthProfitReport(List<MounthProfitReport> modelList,
			ProfitReportReqMonthDto orderProfitReportReqDto) {
		String totalStr = "";
		if (orderProfitReportReqDto.getNeedSum() != null && orderProfitReportReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			if (!CollectionUtils.isEmpty(modelList)) {
				BigDecimal totalSaleAmount = BigDecimal.ZERO;
				BigDecimal totalPurchaseCost = BigDecimal.ZERO;
				BigDecimal totalSaleBlanceAmount = BigDecimal.ZERO;
				BigDecimal totalFundCost = BigDecimal.ZERO;
				BigDecimal totalWarehouseAmount = BigDecimal.ZERO;
				BigDecimal totalManageAmount = BigDecimal.ZERO;
				BigDecimal totalBizManagerAmount = BigDecimal.ZERO;
				BigDecimal totalProfitAmount = BigDecimal.ZERO;

				for (MounthProfitReport orderProfitReportSum : modelList) {
					totalSaleAmount = DecimalUtil.add(totalSaleAmount, null == orderProfitReportSum.getSaleAmount()
							? BigDecimal.ZERO : orderProfitReportSum.getSaleAmount());
					totalSaleBlanceAmount = DecimalUtil.add(totalSaleBlanceAmount,
							null == orderProfitReportSum.getSaleBlanceAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getSaleBlanceAmount());
					totalPurchaseCost = DecimalUtil.add(totalPurchaseCost,
							null == orderProfitReportSum.getPurchaseCost() ? BigDecimal.ZERO
									: orderProfitReportSum.getPurchaseCost());
					totalFundCost = DecimalUtil.add(totalFundCost, null == orderProfitReportSum.getFundCost()
							? BigDecimal.ZERO : orderProfitReportSum.getFundCost());
					totalWarehouseAmount = DecimalUtil.add(totalWarehouseAmount,
							null == orderProfitReportSum.getWarehouseAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getWarehouseAmount());
					totalManageAmount = DecimalUtil.add(totalManageAmount,
							null == orderProfitReportSum.getManageAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getManageAmount());
					totalBizManagerAmount = DecimalUtil.add(totalBizManagerAmount,
							null == orderProfitReportSum.getBizManagerAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getBizManagerAmount());
					totalProfitAmount = DecimalUtil.add(totalProfitAmount,
							null == orderProfitReportSum.getProfitAmount() ? BigDecimal.ZERO
									: orderProfitReportSum.getProfitAmount());
				}
				totalStr = "销售收入：" + DecimalUtil.toAmountString(totalSaleAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；销售成本："
						+ DecimalUtil.toAmountString(totalPurchaseCost) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；销售毛利："
						+ DecimalUtil.toAmountString(totalSaleBlanceAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；资金成本："
						+ DecimalUtil.toAmountString(totalFundCost) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；经营运费："
						+ DecimalUtil.toAmountString(totalWarehouseAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；管理费用："
						+ DecimalUtil.toAmountString(totalManageAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；业务利润："
						+ DecimalUtil.toAmountString(totalBizManagerAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；净利润："
						+ DecimalUtil.toAmountString(totalProfitAmount) + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
			}
		}
		return totalStr;
	}

	private List<MounthProfitReport> convertMounthToResDto(List<MounthProfitReport> orderProfitReportList) {
		List<MounthProfitReport> orderProfitReportResList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(orderProfitReportList)) {
			return orderProfitReportResList;
		}
		for (MounthProfitReport orderProfitReport : orderProfitReportList) {
			MounthProfitReport orderProfitReportRes = convertMounthToResDto(orderProfitReport);
			orderProfitReportResList.add(orderProfitReportRes);
		}
		return orderProfitReportResList;
	}

	private MounthProfitReport convertMounthToResDto(MounthProfitReport model) {
		MounthProfitReport result = new MounthProfitReport();
		result.setProjectId(model.getProjectId());
		if (null != model.getProjectId()) {
			result.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
		}
		result.setBizType(model.getBizType());
		result.setBizTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE, model.getBizType() + ""));
		result.setIssue(model.getIssue());
		result.setSaleAmount(DecimalUtil.formatScale2(model.getSaleAmount()));
		result.setPurchaseCost(DecimalUtil.formatScale2(model.getPurchaseCost()));
		result.setSaleBlanceAmount(DecimalUtil.formatScale2(model.getSaleBlanceAmount()));
		result.setFundCost(DecimalUtil.formatScale2(model.getFundCost()));
		result.setWarehouseAmount(DecimalUtil.formatScale2(model.getWarehouseAmount()));
		result.setManageAmount(DecimalUtil.formatScale2(model.getManageAmount()));
		result.setBizManagerAmount(DecimalUtil.formatScale2(model.getBizManagerAmount()));
		result.setProfitAmount(DecimalUtil.formatScale2(model.getProfitAmount()));
		if (DecimalUtil.eq(model.getSaleAmount(), BigDecimal.ZERO)) {
			// 销售利润率
			if (DecimalUtil.lt(model.getSaleBlanceAmount(), BigDecimal.ZERO)) {
				result.setSaleBlanceRate(new BigDecimal(-1));
			}
			if (DecimalUtil.gt(model.getSaleBlanceAmount(), BigDecimal.ZERO)) {
				result.setSaleBlanceRate(BigDecimal.ONE);
			}
			// 业务利润率
			if (DecimalUtil.lt(model.getBizManagerAmount(), BigDecimal.ZERO)) {
				result.setBizManagerRate(new BigDecimal(-1));
			}
			if (DecimalUtil.gt(model.getBizManagerAmount(), BigDecimal.ZERO)) {
				result.setBizManagerRate(BigDecimal.ONE);
			}
			// 净利润率
			if (DecimalUtil.lt(model.getProfitAmount(), BigDecimal.ZERO)) {
				result.setProfitRate(new BigDecimal(-1));
			}
			if (DecimalUtil.gt(model.getProfitAmount(), BigDecimal.ZERO)) {
				result.setProfitRate(BigDecimal.ONE);
			}

		} else {
			result.setSaleBlanceRate(DecimalUtil.divide(model.getSaleBlanceAmount(), model.getSaleAmount()).setScale(4,
					BigDecimal.ROUND_HALF_UP));
			result.setBizManagerRate(DecimalUtil.divide(model.getBizManagerAmount(), model.getSaleAmount()).setScale(4,
					BigDecimal.ROUND_HALF_UP));
			result.setProfitRate(DecimalUtil.divide(model.getProfitAmount(), model.getSaleAmount()).setScale(4,
					BigDecimal.ROUND_HALF_UP));
		}
		result.setSaleBlanceRateStr(DecimalUtil.toPercentString(result.getSaleBlanceAmount()));
		result.setBizManagerRateStr(DecimalUtil.toPercentString(result.getBizManagerAmount()));
		result.setProfitRateStr(DecimalUtil.toPercentString(result.getProfitRate()));
		if (model.getBizSpecialId() != null) {
			result.setBizSpecialId(model.getBizSpecialId());
			result.setBizSpecialName(cacheService.getUserChineseNameByid(model.getBizSpecialId()));
		}
		return result;
	}

	/**
	 * 获取所有
	 * 
	 * @param orderProfitReportReqDto
	 * @return
	 */
	public List<MounthProfitReport> queryAllMountResultsByCon(ProfitReportReqMonthDto profitReportReqMounthDto) {
		profitReportReqMounthDto.setUserId(ServiceSupport.getUser().getId());
		profitReportReqMounthDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.EIGHT));
		List<MounthProfitReport> orderProfitReportResList = convertMounthToResDto(
				profitReportMonthDao.queryResultsByCon(profitReportReqMounthDto));
		return orderProfitReportResList;
	}

	public boolean isOverMountProfitReportMaxLine(ProfitReportReqMonthDto profitReportReqMonthDto) {
		profitReportReqMonthDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.EIGHT));
		profitReportReqMonthDto.setUserId(ServiceSupport.getUser().getId());
		int count = profitReportMonthDao.queryProfitReportMounthCount(profitReportReqMonthDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("月结利润报表导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncMountProfitReportExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/report/orderProfit/mounth_profit_report_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_22);
			asyncExcelService.addAsyncExcel(profitReportReqMonthDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncMountProfitReportExport(ProfitReportReqMonthDto profitReportReqMounthDto) {
		profitReportReqMounthDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.EIGHT));
		Map<String, Object> model = Maps.newHashMap();
		profitReportReqMounthDto.setUserId(ServiceSupport.getUser().getId());
		List<MounthProfitReport> monthProfitReportList = queryAllMountResultsByCon(profitReportReqMounthDto);
		model.put("mounthProfitReportList", monthProfitReportList);
		return model;
	}
}
