package com.scfs.domain.report.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  资金周转率信息
 *  File: CapitalTurnover.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月06日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CapitalTurnover extends BaseEntity {
	/** 部门 **/
	private Integer departmentId;
	/** 项目 **/
	private Integer projectId;
	/** 销售金额 **/
	private BigDecimal saleAmount;
	/** 期初金额 **/
	private BigDecimal beginAmount;
	/** 期末金额 **/
	private BigDecimal endAmount;
	/** 资金周转率 **/
	private BigDecimal turnoverRate;
	/** 币种 **/
	private Integer currencyType;
	/** 月份 **/
	private String issue;

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

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getBeginAmount() {
		return beginAmount;
	}

	public void setBeginAmount(BigDecimal beginAmount) {
		this.beginAmount = beginAmount;
	}

	public BigDecimal getEndAmount() {
		return endAmount;
	}

	public void setEndAmount(BigDecimal endAmount) {
		this.endAmount = endAmount;
	}

	public BigDecimal getTurnoverRate() {
		return turnoverRate;
	}

	public void setTurnoverRate(BigDecimal turnoverRate) {
		this.turnoverRate = turnoverRate;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

}
