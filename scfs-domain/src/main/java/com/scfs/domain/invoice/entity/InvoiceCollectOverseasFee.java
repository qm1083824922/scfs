package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceCollectOverseasFee {
	/**
	 * 收票费用ID自增长
	 */
	private Integer id;

	/**
	 * 境外收票id
	 */
	private Integer collectOverseasId;

	/**
	 * 费用ID
	 */
	private Integer feeId;

	/**
	 * 收票金额
	 */
	private BigDecimal invoiceAmount;

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
	 * 是否删除 0 否 1 是
	 */
	private Integer isDelete;

	/** 币种 **/
	private Integer currencyType;

	/** 费用编号 **/
	private String feeNo;
	/** 费用类型 **/
	private Integer feeType;
	/** 费用日期 **/
	private Date payDate;

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCollectOverseasId() {
		return collectOverseasId;
	}

	public void setCollectOverseasId(Integer collectOverseasId) {
		this.collectOverseasId = collectOverseasId;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public String getFeeNo() {
		return feeNo;
	}

	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public Integer geyCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}
}