package com.scfs.domain.finance.cope.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  应付管理
 *  File: CopeManageResDto.java
 *  Description:
 *  TODO
 *  Date;					Who;				
 *  2017年10月31日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CopeManage extends BaseEntity {
	/** 项目id **/
	private Integer projectId;
	/** 客户id **/
	private Integer customerId;
	/** 经营单位 **/
	private Integer busiUnitId;
	/** 币种 **/
	private Integer currnecyType;
	/** 应付金额 **/
	private BigDecimal copeAmount;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	/** 未付金额 **/
	private BigDecimal unpaidAmount;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public BigDecimal getCopeAmount() {
		return copeAmount;
	}

	public void setCopeAmount(BigDecimal copeAmount) {
		this.copeAmount = copeAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

}
