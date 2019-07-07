package com.scfs.domain.report.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.MoneySerializer;
import com.scfs.domain.NumSerializer;
import com.scfs.domain.PriceSerializer;

/**
 * Created by Administrator on 2017年3月18日.
 */
public class OrderProfitReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8941163156288073786L;
	private Integer id;
	/**
	 * 部门ID
	 */
	private Integer departmentId;
	/**
	 * 部门
	 */
	private String departmentName;
	/**
	 * 部门
	 */
	private String remark;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 项目
	 */
	private String projectName;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 客户
	 */
	private String customerName;
	/**
	 * 业务员ID
	 */
	private Integer bizManagerId;
	/**
	 * 业务员
	 */
	private String bizManagerName;
	/**
	 * 仓库ID
	 */
	private Integer warehouseId;
	/**
	 * 仓库
	 */
	private String warehouseName;
	/**
	 * 业务类型
	 */
	private Integer bizType;
	/**
	 * 业务类型名称
	 */
	private String bizTypeName;
	/**
	 * 单据类型
	 */
	private Integer billType;
	/**
	 * 单据类型名称
	 */
	private String billTypeName;
	/**
	 * 经营单位ID
	 */
	private Integer businessUnitId;
	/**
	 * 经营单位名称
	 */
	private String businessUnitName;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 单据编号
	 */
	private String billNo;
	/**
	 * 产品编号
	 */
	private String productNo;
	/**
	 * 数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal num;
	/**
	 * 下单时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date placeDate;
	/**
	 * 统计时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date statisticsDate;
	/**
	 * 销售总价
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal saleAmount = BigDecimal.ZERO;
	/**
	 * 服务收入
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal serviceAmount = BigDecimal.ZERO;
	/**
	 * 综合税金
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal compositeTaxAmount = BigDecimal.ZERO;
	/**
	 * 采购成本
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal purchaseCost = BigDecimal.ZERO;
	/**
	 * 资金成本
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal fundCost = BigDecimal.ZERO;
	/**
	 * 仓储物流费
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal warehouseAmount = BigDecimal.ZERO;
	/**
	 * 市场费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal marketAmount = BigDecimal.ZERO;
	/**
	 * 财务费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal financeAmount = BigDecimal.ZERO;
	/**
	 * 管理费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal manageAmount = BigDecimal.ZERO;
	/**
	 * 人工费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal manualAmount = BigDecimal.ZERO;
	/**
	 * 利润
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal profitAmount = BigDecimal.ZERO;
	/**
	 * 利润率
	 */
	private BigDecimal profitRate = BigDecimal.ZERO;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 币种名称
	 */
	private String currencyTypeName;
	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;
	/********************* 扩展字段 ***************************/
	/**
	 * 应收费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal recAmount;
	/**
	 * 应付费用
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal payAmount;
	/**
	 * (应收费用-应付费用)的差额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal diffAmount;
	/**
	 * 税率
	 */
	private BigDecimal taxRate;
	/**
	 * 是否国内
	 */
	private Integer isHome;
	/**
	 * 一级管理科目
	 */
	private Integer feeOneName;
	/**
	 * 利润率显示
	 */
	private String profitRateStr;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public String getBizManagerName() {
		return bizManagerName;
	}

	public void setBizManagerName(String bizManagerName) {
		this.bizManagerName = bizManagerName;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public Date getPlaceDate() {
		return placeDate;
	}

	public void setPlaceDate(Date placeDate) {
		this.placeDate = placeDate;
	}

	public Date getStatisticsDate() {
		return statisticsDate;
	}

	public void setStatisticsDate(Date statisticsDate) {
		this.statisticsDate = statisticsDate;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public BigDecimal getCompositeTaxAmount() {
		return compositeTaxAmount;
	}

	public void setCompositeTaxAmount(BigDecimal compositeTaxAmount) {
		this.compositeTaxAmount = compositeTaxAmount;
	}

	public BigDecimal getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(BigDecimal purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public BigDecimal getWarehouseAmount() {
		return warehouseAmount;
	}

	public void setWarehouseAmount(BigDecimal warehouseAmount) {
		this.warehouseAmount = warehouseAmount;
	}

	public BigDecimal getMarketAmount() {
		return marketAmount;
	}

	public void setMarketAmount(BigDecimal marketAmount) {
		this.marketAmount = marketAmount;
	}

	public BigDecimal getFinanceAmount() {
		return financeAmount;
	}

	public void setFinanceAmount(BigDecimal financeAmount) {
		this.financeAmount = financeAmount;
	}

	public BigDecimal getManageAmount() {
		return manageAmount;
	}

	public void setManageAmount(BigDecimal manageAmount) {
		this.manageAmount = manageAmount;
	}

	public BigDecimal getManualAmount() {
		return manualAmount;
	}

	public void setManualAmount(BigDecimal manualAmount) {
		this.manualAmount = manualAmount;
	}

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
	}

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(BigDecimal recAmount) {
		this.recAmount = recAmount;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getDiffAmount() {
		return diffAmount;
	}

	public void setDiffAmount(BigDecimal diffAmount) {
		this.diffAmount = diffAmount;
	}

	public BigDecimal getFundCost() {
		return fundCost;
	}

	public void setFundCost(BigDecimal fundCost) {
		this.fundCost = fundCost;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getIsHome() {
		return isHome;
	}

	public void setIsHome(Integer isHome) {
		this.isHome = isHome;
	}

	public Integer getFeeOneName() {
		return feeOneName;
	}

	public void setFeeOneName(Integer feeOneName) {
		this.feeOneName = feeOneName;
	}

	public String getProfitRateStr() {
		return profitRateStr;
	}

	public void setProfitRateStr(String profitRateStr) {
		this.profitRateStr = profitRateStr;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
