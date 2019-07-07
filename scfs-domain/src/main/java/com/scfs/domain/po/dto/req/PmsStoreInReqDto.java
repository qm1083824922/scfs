package com.scfs.domain.po.dto.req;

import java.io.Serializable;

public class PmsStoreInReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2664130824932934043L;
	/** 采购单号 */
	private String purchase_sn;
	/** 供应商编号 */
	private String provider_sn;
	/** 币种 */
	private String currency_type;
	/** 商品sku */
	private String sku;
	/** 1-待处理 2-处理失败 3-处理成功 */
	private Integer dealFlag;

	public String getPurchase_sn() {
		return purchase_sn;
	}

	public void setPurchase_sn(String purchase_sn) {
		this.purchase_sn = purchase_sn;
	}

	public String getProvider_sn() {
		return provider_sn;
	}

	public void setProvider_sn(String provider_sn) {
		this.provider_sn = provider_sn;
	}

	public String getCurrency_type() {
		return currency_type;
	}

	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Integer dealFlag) {
		this.dealFlag = dealFlag;
	}

}
