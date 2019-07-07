package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ProfitReport {
	private Integer id;

	/**
	 * 报表记录表ID
	 */
	private Integer reportRecordId;
	/**
	 * 期号
	 */
	private String issue;
	/**
	 * 关联单据ID
	 */
	private Integer billId;

	/**
	 * 项目ID
	 */
	private Integer projectId;

	/**
	 * 项目名称
	 */
	private String projectName;

	/**
	 * 部门ID
	 */
	private Integer departmentId;

	/**
	 * 部门名称
	 */
	private String departmentName;

	/**
	 * 客户ID
	 */
	private Integer customerId;

	/**
	 * 客户名称
	 */
	private String customerName;

	/**
	 * 业务员ID
	 */
	private Integer bizManagerId;

	/**
	 * 业务员名称
	 */
	private String bizManagerName;

	/**
	 * 仓库ID
	 */
	private Integer warehouseId;

	/**
	 * 仓库名称
	 */
	private String warehouseName;

	/**
	 * 经营单位ID
	 */
	private Integer businessUnitId;

	/**
	 * 经营单位名称
	 */
	private String businessUnitName;

	/**
	 * 业务类型 1-服务 2-贸易
	 */
	private Integer bizType;

	/**
	 * 业务类型名称
	 */
	private String bizTypeName;

	/**
	 * 单据类型 1-应收费用 2-应付费用 3-应收应付费用 4-销售单 5-资金成本
	 */
	private Integer billType;

	/**
	 * 单据类型名称
	 */
	private String billTypeName;

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
	private BigDecimal num;

	/**
	 * 下单日期
	 */
	private Date placeDate;

	/**
	 * 统计日期(实际业务日期)
	 */
	private Date statisticsDate;

	/**
	 * 销售总价
	 */
	private BigDecimal saleAmount;

	/**
	 * 服务收入
	 */
	private BigDecimal serviceAmount;

	/**
	 * 综合税金
	 */
	private BigDecimal compositeTaxAmount;

	/**
	 * 采购成本
	 */
	private BigDecimal purchaseCost;

	/**
	 * 资金成本
	 */
	private BigDecimal fundCost;

	/**
	 * 仓储物流费
	 */
	private BigDecimal warehouseAmount;

	/**
	 * 市场费用
	 */
	private BigDecimal marketAmount;

	/**
	 * 财务费用
	 */
	private BigDecimal financeAmount;

	/**
	 * 管理费用
	 */
	private BigDecimal manageAmount;

	/**
	 * 人工费用
	 */
	private BigDecimal manualAmount;

	/**
	 * 利润
	 */
	private BigDecimal profitAmount;

	/**
	 * 利润率
	 */
	private BigDecimal profitRate;

	/**
	 * 税率
	 */
	private BigDecimal taxRate;

	/**
	 * 币种 1.人民币 2.美元 3.港币 4.欧元
	 */
	private Integer currencyType;

	/**
	 * 币种名称
	 */
	private String currencyName;

	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;

	/**
	 * 创建人ID
	 */
	private Integer creatorId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 修改时间
	 */
	private Date updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReportRecordId() {
		return reportRecordId;
	}

	public void setReportRecordId(Integer reportRecordId) {
		this.reportRecordId = reportRecordId;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
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
		this.projectName = projectName == null ? null : projectName.trim();
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
		this.departmentName = departmentName == null ? null : departmentName.trim();
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
		this.customerName = customerName == null ? null : customerName.trim();
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
		this.bizManagerName = bizManagerName == null ? null : bizManagerName.trim();
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
		this.warehouseName = warehouseName == null ? null : warehouseName.trim();
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
		this.businessUnitName = businessUnitName == null ? null : businessUnitName.trim();
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
		this.bizTypeName = bizTypeName == null ? null : bizTypeName.trim();
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
		this.billTypeName = billTypeName == null ? null : billTypeName.trim();
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo == null ? null : billNo.trim();
	}

	public String getProductNo() {
		return productNo;
	}

	public void setProductNo(String productNo) {
		this.productNo = productNo == null ? null : productNo.trim();
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

	public BigDecimal getFundCost() {
		return fundCost;
	}

	public void setFundCost(BigDecimal fundCost) {
		this.fundCost = fundCost;
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

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName == null ? null : currencyName.trim();
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}