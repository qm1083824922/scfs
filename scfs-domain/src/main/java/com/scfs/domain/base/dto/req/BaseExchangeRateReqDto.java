package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

public class BaseExchangeRateReqDto extends BaseReqDto {

	private static final long serialVersionUID = 7598816283809096171L;

	/**
	 * 银行名称
	 */
	private String bank;

	/**
	 * 币种
	 */
	private String currency;

	/**
	 * 外币种
	 */
	private String foreignCurrency;

	/**
	 * 现钞卖出价
	 */
	private BigDecimal cashSellingPrice;

	/**
	 * 现钞买入价
	 */
	private BigDecimal cashBuyingPrice;

	/**
	 * 电汇卖出价
	 */
	private BigDecimal draftSellingPrice;

	/**
	 * 电汇买入价
	 */
	private BigDecimal draftBuyingPrice;

	/**
	 * 发布时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishAt;

	/**
	 * 发布结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishBegin;
	/**
	 * 发布开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date publishEnd;
	/**
	 * 备份开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date backupFrom;
	/**
	 * 备份结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date backupTo;

	private String createAt;

	public String getCreateAt() {
		return createAt;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

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

	public Date getPublishAt() {
		return publishAt;
	}

	public void setPublishAt(Date publishAt) {
		this.publishAt = publishAt;
	}
}
