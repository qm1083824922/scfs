package com.scfs.domain.api.pms.model;

import java.math.BigDecimal;

public class PmsPayModel {
	/** 请款单号 **/
	private String paySn;
	/** 供应商编号 **/
	private String providerSn;
	/** 币种 **/
	private String currencyType;
	/** 抵扣金额 **/
	private BigDecimal deductionMoney;
	/** 采购单号 **/
	private String purchaseSn;
	/** 商品sku **/
	private String sku;
	/** 返回消息 **/
	private String msg;
	/** 数量 **/
	private BigDecimal payQuantity;
	/** 价格 **/
	private BigDecimal dealPrice;
	/** 流水表id **/
	private Integer pmsSeriesId;
	/** 状态 **/
	private Integer status;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Integer dealFlag) {
		this.dealFlag = dealFlag;
	}

	private Integer dealFlag;

	public String getPaySn() {
		return paySn;
	}

	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}

	public String getProviderSn() {
		return providerSn;
	}

	public void setProviderSn(String providerSn) {
		this.providerSn = providerSn;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getDeductionMoney() {
		return deductionMoney;
	}

	public void setDeductionMoney(BigDecimal deductionMoney) {
		this.deductionMoney = deductionMoney;
	}

	public String getPurchaseSn() {
		return purchaseSn;
	}

	public void setPurchaseSn(String purchaseSn) {
		this.purchaseSn = purchaseSn;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public BigDecimal getPayQuantity() {
		return payQuantity;
	}

	public void setPayQuantity(BigDecimal payQuantity) {
		this.payQuantity = payQuantity;
	}

	public BigDecimal getDealPrice() {
		return dealPrice;
	}

	public void setDealPrice(BigDecimal dealPrice) {
		this.dealPrice = dealPrice;
	}

	public Integer getPmsSeriesId() {
		return pmsSeriesId;
	}

	public void setPmsSeriesId(Integer pmsSeriesId) {
		this.pmsSeriesId = pmsSeriesId;
	}
}
