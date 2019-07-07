package com.scfs.domain.report.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: ProfitTarget.java
 *  Description: 业务指标目标值
 *  TODO
 *  Date,					Who,				
 *  2017年7月14日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ProfitTarget extends BaseEntity {
	/** 月份 **/
	private String issue;
	/** 业务员id **/
	private Integer userId;
	/** 利润目标值 **/
	private BigDecimal targetProfitAmount;
	/** 业务利润目标值 **/
	private BigDecimal targetBizManager;
	/** 销售毛利润目标值 **/
	private BigDecimal targetSaleBlance;
	/** 经营收入目标值 **/
	private BigDecimal targetSaleAmount;
	/** 管理费用目标值 **/
	private BigDecimal targetManageAmount;
	/** 经营费用目标值 **/
	private BigDecimal targetWarehouseAmount;
	/** 资金成本目标值 **/
	private BigDecimal targetFundVost;
	/** 业务审核员 **/
	private Integer busiId;
	/** 部门主管审核人 **/
	private Integer deptManageId;
	/** 状态 0 待提交 1 待业务主管审核 2 待部门主管审核 3 已完成 **/
	private Integer state;

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getTargetProfitAmount() {
		return targetProfitAmount;
	}

	public void setTargetProfitAmount(BigDecimal targetProfitAmount) {
		this.targetProfitAmount = targetProfitAmount;
	}

	public BigDecimal getTargetBizManager() {
		return targetBizManager;
	}

	public void setTargetBizManager(BigDecimal targetBizManager) {
		this.targetBizManager = targetBizManager;
	}

	public BigDecimal getTargetSaleBlance() {
		return targetSaleBlance;
	}

	public void setTargetSaleBlance(BigDecimal targetSaleBlance) {
		this.targetSaleBlance = targetSaleBlance;
	}

	public BigDecimal getTargetSaleAmount() {
		return targetSaleAmount;
	}

	public void setTargetSaleAmount(BigDecimal targetSaleAmount) {
		this.targetSaleAmount = targetSaleAmount;
	}

	public BigDecimal getTargetManageAmount() {
		return targetManageAmount;
	}

	public void setTargetManageAmount(BigDecimal targetManageAmount) {
		this.targetManageAmount = targetManageAmount;
	}

	public BigDecimal getTargetWarehouseAmount() {
		return targetWarehouseAmount;
	}

	public void setTargetWarehouseAmount(BigDecimal targetWarehouseAmount) {
		this.targetWarehouseAmount = targetWarehouseAmount;
	}

	public BigDecimal getTargetFundVost() {
		return targetFundVost;
	}

	public void setTargetFundVost(BigDecimal targetFundVost) {
		this.targetFundVost = targetFundVost;
	}

	public Integer getBusiId() {
		return busiId;
	}

	public void setBusiId(Integer busiId) {
		this.busiId = busiId;
	}

	public Integer getDeptManageId() {
		return deptManageId;
	}

	public void setDeptManageId(Integer deptManageId) {
		this.deptManageId = deptManageId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
