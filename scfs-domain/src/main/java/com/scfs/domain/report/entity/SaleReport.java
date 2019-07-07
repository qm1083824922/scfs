package com.scfs.domain.report.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.MoneySerializer;

/**
 * Created by Administrator on 2017年2月13日.
 */
public class SaleReport {
	/**
	 * 部门ID
	 */
	private Integer departmentId;
	/**
	 * 部门
	 */
	private String departmentName;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 项目
	 */
	private String projectName;
	/**
	 * 业务类型
	 */
	private Integer bizType;
	private String bizTypeName;
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
	 * 外部销售金额(客户不同时是经营单位时)
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal outerSaleAmount;
	/**
	 * 内部销售金额(客户同时是经营单位时)
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal innerSaleAmount;
	/**
	 * 销售金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal saleAmount;
	/**
	 * 销售成本
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal costAmount;
	/**
	 * 销售利润
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal profitAmount;
	/**
	 * 费用利润
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal feeProfitAmount;
	/**
	 * 合计利润
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal sumProfitAmount;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 币种名称
	 */
	private String currencyTypeName;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBizManagerName() {
		return bizManagerName;
	}

	public void setBizManagerName(String bizManagerName) {
		this.bizManagerName = bizManagerName;
	}

	public BigDecimal getOuterSaleAmount() {
		return outerSaleAmount;
	}

	public void setOuterSaleAmount(BigDecimal outerSaleAmount) {
		this.outerSaleAmount = outerSaleAmount;
	}

	public BigDecimal getInnerSaleAmount() {
		return innerSaleAmount;
	}

	public void setInnerSaleAmount(BigDecimal innerSaleAmount) {
		this.innerSaleAmount = innerSaleAmount;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
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

	public BigDecimal getFeeProfitAmount() {
		return feeProfitAmount;
	}

	public void setFeeProfitAmount(BigDecimal feeProfitAmount) {
		this.feeProfitAmount = feeProfitAmount;
	}

	public BigDecimal getSumProfitAmount() {
		return sumProfitAmount;
	}

	public void setSumProfitAmount(BigDecimal sumProfitAmount) {
		this.sumProfitAmount = sumProfitAmount;
	}

}
