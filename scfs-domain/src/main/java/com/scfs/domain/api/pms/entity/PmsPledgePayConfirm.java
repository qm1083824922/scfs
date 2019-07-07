package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PmsPledgePayConfirm {
	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 采购单号
	 */
	private String purchaseSn;

	/**
	 * 请款单号
	 */
	private String paySn;

	/**
	 * 请款币种
	 */
	private String currencyType;

	/**
	 * 实付币种
	 */
	private String realCurrencyType;

	/**
	 * 请款金额
	 */
	private BigDecimal payPrice;

	/**
	 * 实付金额
	 */
	private BigDecimal currencyMoney;

	/**
	 * 确认时间
	 */
	private Date verifyTime;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 修改时间
	 */
	private Date updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPurchaseSn() {
		return purchaseSn;
	}

	public void setPurchaseSn(String purchaseSn) {
		this.purchaseSn = purchaseSn == null ? null : purchaseSn.trim();
	}

	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn == null ? null : paySn.trim();
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType == null ? null : currencyType.trim();
	}

	public String getRealCurrencyType() {
		return realCurrencyType;
	}

	public void setRealCurrencyType(String realCurrencyType) {
		this.realCurrencyType = realCurrencyType == null ? null : realCurrencyType.trim();
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public BigDecimal getCurrencyMoney() {
		return currencyMoney;
	}

	public void setCurrencyMoney(BigDecimal currencyMoney) {
		this.currencyMoney = currencyMoney;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}