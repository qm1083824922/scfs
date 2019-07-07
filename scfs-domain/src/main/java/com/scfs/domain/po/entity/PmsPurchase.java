package com.scfs.domain.po.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class PmsPurchase implements Serializable {

	private static final long serialVersionUID = -3918025197216373783L;
	/** 退货ID **/
	private Integer refund_order_id;
	/** 退货单号 **/
	private String refund_order_sn;
	/** 采购单号 **/
	private String purchase_sn;
	/** 供应商 **/
	private String provider_sn;
	/** 商品SKU **/
	private String sku;
	/** 实际退货数量 **/
	private BigDecimal refund_quantity;
	/** 事业部 **/
	private String division_code;

	public Integer getRefund_order_id() {
		return refund_order_id;
	}

	public void setRefund_order_id(Integer refund_order_id) {
		this.refund_order_id = refund_order_id;
	}

	public String getRefund_order_sn() {
		return refund_order_sn;
	}

	public void setRefund_order_sn(String refund_order_sn) {
		this.refund_order_sn = refund_order_sn;
	}

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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public BigDecimal getRefund_quantity() {
		return refund_quantity;
	}

	public void setRefund_quantity(BigDecimal refund_quantity) {
		this.refund_quantity = refund_quantity;
	}

	public String getDivision_code() {
		return division_code;
	}

	public void setDivision_code(String division_code) {
		this.division_code = division_code;
	}

}
