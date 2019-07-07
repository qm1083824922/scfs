package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: AdvanceReceiptRel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月2日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AdvanceReceiptRel extends BaseEntity {
	/** 预收水单关系id **/
	private Integer id;
	/** 水单id **/
	private Integer receiptId;
	/** 预收id **/
	private Integer preRecId;
	/** 转预收金额 **/
	private BigDecimal exchangeAmount;
	/** 创建人id **/
	private Integer creatorId;
	/** 创建人 **/
	private String creator;
	/** 创建时间 **/
	private Date createAt;
	/** 币种 **/
	private Integer currencyType;
	/** 项目 关联tb_base_project[id] **/
	private Integer projectId;
	/** 预收备注 **/
	private String preRecNote;

	/** 客户 关联tb_base_subject[id] **/
	private Integer custId;
	/** 经营单位 关联tb_base_subject[id] **/
	private Integer busiUnit;
	/** 预收类型 **/
	private Integer advanceType;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	private Integer deletePrivFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getPreRecId() {
		return preRecId;
	}

	public void setPreRecId(Integer preRecId) {
		this.preRecId = preRecId;
	}

	public BigDecimal getExchangeAmount() {
		return exchangeAmount;
	}

	public void setExchangeAmount(BigDecimal exchangeAmount) {
		this.exchangeAmount = exchangeAmount;
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
		this.creator = creator;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getPreRecNote() {
		return preRecNote;
	}

	public void setPreRecNote(String preRecNote) {
		this.preRecNote = preRecNote;
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

    public Integer getDeletePrivFlag()
    {
        return deletePrivFlag;
    }

    public void setDeletePrivFlag(Integer deletePrivFlag)
    {
        this.deletePrivFlag = deletePrivFlag;
    }

}
