package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: Advance.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class Advance extends BaseEntity {
	/** 预收id **/
	private Integer id;
	/** 客户 关联tb_base_subject[id] **/
	private Integer custId;
	/** 经营单位 关联tb_base_subject[id] **/
	private Integer busiUnit;
	/** 项目 关联tb_base_project[id] **/
	private Integer projectId;
	/** 预收余额 **/
	private BigDecimal preRecAmount;
	/** 预收总额 **/
	private BigDecimal preRecSum;
	/** 已核销总额 **/
	private BigDecimal writeOffSum;
	/** 待核销金额 **/
	private BigDecimal writingOffAmount;
	/** 币种 **/
	private Integer currencyType;
	/** 创建时间 **/
	private Date createAt;
	/** 创建人 **/
	private String creator;
	/** 创建人id **/
	private Integer creatorId;
	/** 修改时间 **/
	private Date updateAt;
	/** 预收类型 **/
	private Integer advanceType;
	/** 已付金额 **/
	private BigDecimal paidAmount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public BigDecimal getPreRecAmount() {
		return preRecAmount;
	}

	public void setPreRecAmount(BigDecimal preRecAmount) {
		this.preRecAmount = preRecAmount;
	}

	public BigDecimal getPreRecSum() {
		return preRecSum;
	}

	public void setPreRecSum(BigDecimal preRecSum) {
		this.preRecSum = preRecSum;
	}

	public BigDecimal getWriteOffSum() {
		return writeOffSum;
	}

	public void setWriteOffSum(BigDecimal writeOffSum) {
		this.writeOffSum = writeOffSum;
	}

	public BigDecimal getWritingOffAmount() {
		return writingOffAmount;
	}

	public void setWritingOffAmount(BigDecimal writingOffAmount) {
		this.writingOffAmount = writingOffAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Integer getAdvanceType() {
		return advanceType;
	}

	public void setAdvanceType(Integer advanceType) {
		this.advanceType = advanceType;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

}
