package com.scfs.domain.base.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 汇率
 * 
 * @author 
 *
 */
public class BaseExchangeRate extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 银行名称 */
	private String bank;

	/** 币种 */
	private String currency;

	/** 外币种 */
	private String foreignCurrency;

	/** 现钞卖出价 */
	private BigDecimal cashSellingPrice;

	/** 现钞买入价 */
	private BigDecimal cashBuyingPrice;

	/** 电汇卖出价 */
	private BigDecimal draftSellingPrice;

	/** 电汇买入价 */
	private BigDecimal draftBuyingPrice;

	/** 发布时间 */
	private Date publishAt;

	/** 发布时间 */
	private Date backupAt;

	/** 备份开始时间 */
	private Date backupFrom;
	/** 备份结束时间 */
	private Date backupTo;

	/** 发布开始时间 */
	private Date publishBegin;

	/** 发布结束时间 */
	private Date publishEnd;

	/** 备份人 */
	private String backupPerson;

	/** 异常标记 */
	private Integer isError;

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(String foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public BigDecimal getCashSellingPrice() {
		return cashSellingPrice;
	}

	public void setCashSellingPrice(BigDecimal cashSellingPrice) {
		this.cashSellingPrice = cashSellingPrice;
	}

	public BigDecimal getCashBuyingPrice() {
		return cashBuyingPrice;
	}

	public void setCashBuyingPrice(BigDecimal cashBuyingPrice) {
		this.cashBuyingPrice = cashBuyingPrice;
	}

	public BigDecimal getDraftSellingPrice() {
		return draftSellingPrice;
	}

	public void setDraftSellingPrice(BigDecimal draftSellingPrice) {
		this.draftSellingPrice = draftSellingPrice;
	}

	public BigDecimal getDraftBuyingPrice() {
		return draftBuyingPrice;
	}

	public void setDraftBuyingPrice(BigDecimal draftBuyingPrice) {
		this.draftBuyingPrice = draftBuyingPrice;
	}

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}

	public Date getBackupAt() {
		return backupAt;
	}

	public void setBackupAt(Date backupAt) {
		this.backupAt = backupAt;
	}

	public Date getBackupFrom() {
		return backupFrom;
	}

	public void setBackupFrom(Date backupFrom) {
		this.backupFrom = backupFrom;
	}

	public Date getBackupTo() {
		return backupTo;
	}

	public void setBackupTo(Date backupTo) {
		this.backupTo = backupTo;
	}

	public Date getPublishBegin() {
		return publishBegin;
	}

	public void setPublishBegin(Date publishBegin) {
		this.publishBegin = publishBegin;
	}

	public Date getPublishEnd() {
		return publishEnd;
	}

	public void setPublishEnd(Date publishEnd) {
		this.publishEnd = publishEnd;
	}

	public String getBackupPerson() {
		return backupPerson;
	}

	public void setBackupPerson(String backupPerson) {
		this.backupPerson = backupPerson;
	}

	public Integer getIsError() {
		return isError;
	}

	public void setIsError(Integer isError) {
		this.isError = isError;
	}
}
