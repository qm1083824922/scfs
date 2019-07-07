package com.scfs.domain.pay.dto.resq;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  PMS出库明细
 *  File: PmsStoreOutResDto.java
 *  Description: 
 *  TODO
 *  Date,					Who,				
 *  2017年09月20日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PmsStoreOutResDto extends BaseEntity {
	private Integer pmsSeriesId;
	/** 采购单号 **/
	private String purchaseSn;
	/** 供应商编号 **/
	private String providerSn;
	/** 币种 **/
	private String currencyType;
	/** 商品sku **/
	private String sku;
	/** 销售数量 **/
	private BigDecimal wmsOutStockin;
	/** 销售价格 **/
	private BigDecimal purchasePrice;
	/** 销售日期 **/
	private Date salesDate;
	/** 返回值 0 接受成功 1:接受失败 **/
	private Integer flag;
	/** 返回信息 **/
	private String msg;
	/** 处理消息 **/
	private String dealMsg;
	/** 处理结果 **/
	private Integer dealFlag;
	/** 销售id **/
	private Integer skuId;

	public Integer getPmsSeriesId() {
		return pmsSeriesId;
	}

	public void setPmsSeriesId(Integer pmsSeriesId) {
		this.pmsSeriesId = pmsSeriesId;
	}

	public String getPurchaseSn() {
		return purchaseSn;
	}

	public void setPurchaseSn(String purchaseSn) {
		this.purchaseSn = purchaseSn;
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public BigDecimal getWmsOutStockin() {
		return wmsOutStockin;
	}

	public void setWmsOutStockin(BigDecimal wmsOutStockin) {
		this.wmsOutStockin = wmsOutStockin;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public Date getSalesDate() {
		return salesDate;
	}

	public void setSalesDate(Date salesDate) {
		this.salesDate = salesDate;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDealMsg() {
		return dealMsg;
	}

	public void setDealMsg(String dealMsg) {
		this.dealMsg = dealMsg;
	}

	public Integer getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Integer dealFlag) {
		this.dealFlag = dealFlag;
	}

	public Integer getSkuId() {
		return skuId;
	}

	public void setSkuId(Integer skuId) {
		this.skuId = skuId;
	}

}
