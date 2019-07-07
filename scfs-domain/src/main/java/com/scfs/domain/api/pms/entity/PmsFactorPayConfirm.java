package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PmsFactorPayConfirm {
	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 供应商编号
	 */
	private String providerSn;

	/**
	 * 银行流水号
	 */
	private String bankSeriesNo;

	/**
	 * 确认日期
	 */
	private Date confirmDate;

	/**
	 * 付款法人代码
	 */
	private String corporationCode;

	/**
	 * 付款法人名称
	 */
	private String corporationName;

	/**
	 * 新付款编号
	 */
	private String newPayNo;

	/**
	 * 原付款编号
	 */
	private String payNo;

	/**
	 * 付款币种
	 */
	private String currencyType;

	/**
	 * 付款金额
	 */
	private BigDecimal payAmount;

	/**
	 * 实际付款币种
	 */
	private String realCurrencyType;

	/**
	 * 实际付款金额
	 */
	private BigDecimal realPayAmount;

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

	public String getProviderSn() {
		return providerSn;
	}

	public void setProviderSn(String providerSn) {
		this.providerSn = providerSn == null ? null : providerSn.trim();
	}

	public String getBankSeriesNo() {
		return bankSeriesNo;
	}

	public void setBankSeriesNo(String bankSeriesNo) {
		this.bankSeriesNo = bankSeriesNo == null ? null : bankSeriesNo.trim();
	}

	public Date getConfirmDate() {
		return confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getCorporationCode() {
		return corporationCode;
	}

	public void setCorporationCode(String corporationCode) {
		this.corporationCode = corporationCode == null ? null : corporationCode.trim();
	}

	public String getCorporationName() {
		return corporationName;
	}

	public void setCorporationName(String corporationName) {
		this.corporationName = corporationName == null ? null : corporationName.trim();
	}

	public String getNewPayNo() {
		return newPayNo;
	}

	public void setNewPayNo(String newPayNo) {
		this.newPayNo = newPayNo == null ? null : newPayNo.trim();
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo == null ? null : payNo.trim();
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType == null ? null : currencyType.trim();
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getRealCurrencyType() {
		return realCurrencyType;
	}

	public void setRealCurrencyType(String realCurrencyType) {
		this.realCurrencyType = realCurrencyType == null ? null : realCurrencyType.trim();
	}

	public BigDecimal getRealPayAmount() {
		return realPayAmount;
	}

	public void setRealPayAmount(BigDecimal realPayAmount) {
		this.realPayAmount = realPayAmount;
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