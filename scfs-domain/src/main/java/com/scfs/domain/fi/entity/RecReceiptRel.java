package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: RecReceiptRel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月1日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class RecReceiptRel extends BaseEntity {
	/** 水单与应收关系表id **/
	private Integer id;
	/** 应收id **/
	private Integer recId;
	/** 水单id **/
	private Integer receiptId;
	/** 核销金额 **/
	private BigDecimal writeOffAmount;
	/** 创建人 **/
	private String creator;
	/** 创建人id **/
	private Integer creatorId;
	/** 创建时间 **/
	private Date createAt;
	/** 币种 **/
	private Integer currencyType;
	/** 资金占用 **/
	private BigDecimal fundUsed;

	/** 客户 关联tb_base_subject[id] **/
	private Integer custId;
	/** 经营单位 关联tb_base_subject[id] **/
	private Integer busiUnit;
	/** 项目 关联tb_base_project[id] **/
	private Integer projectId;

	/** 应收金额 **/
	private BigDecimal amountReceivable;
	/** 已收金额 ， 水单核销总额 **/
	private BigDecimal amountReceived;

	/** ----------------扩展水单字段--------------------------- **/
	/** 系统生成的水单编号 **/
	private String receiptNo;
	/** 水单备注 **/
	private String receiptNote;
	/** 水单日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date receiptDate;
	/** 银行水单号 **/
	private String bankReceiptNo;
	/** 收款账号 **/
	private Integer recAccountNo;
	/** 水单金额 **/
	private BigDecimal receiptAmount;
	/** 水单类型 1 回款 2 预收定金 3 预收货款 4融资 5内部 **/
	private Integer receiptType;
	/** 币种 **/
	private Integer receiptCurrencyType;
	/** 摘要 **/
	private String summary;
	private Date expireDate;
	/** 对账日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkDate;

	public Integer getId() {
		return id;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRecId() {
		return recId;
	}

	public void setRecId(Integer recId) {
		this.recId = recId;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public BigDecimal getWriteOffAmount() {
		return writeOffAmount;
	}

	public void setWriteOffAmount(BigDecimal writeOffAmount) {
		this.writeOffAmount = writeOffAmount;
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

	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}

	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}

	public BigDecimal getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getReceiptNote() {
		return receiptNote;
	}

	public void setReceiptNote(String receiptNote) {
		this.receiptNote = receiptNote;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public String getBankReceiptNo() {
		return bankReceiptNo;
	}

	public void setBankReceiptNo(String bankReceiptNo) {
		this.bankReceiptNo = bankReceiptNo;
	}

	public Integer getRecAccountNo() {
		return recAccountNo;
	}

	public void setRecAccountNo(Integer recAccountNo) {
		this.recAccountNo = recAccountNo;
	}

	public BigDecimal getReceiptAmount() {
		return receiptAmount;
	}

	public void setReceiptAmount(BigDecimal receiptAmount) {
		this.receiptAmount = receiptAmount;
	}

	public Integer getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getReceiptCurrencyType() {
		return receiptCurrencyType;
	}

	public void setReceiptCurrencyType(Integer receiptCurrencyType) {
		this.receiptCurrencyType = receiptCurrencyType;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public BigDecimal getFundUsed() {
		return fundUsed;
	}

	public void setFundUsed(BigDecimal fundUsed) {
		this.fundUsed = fundUsed;
	}

}
