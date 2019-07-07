package com.scfs.domain.api.pms.dto.res;

import java.io.Serializable;

public class PmsStoreInHttpResDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6936549672502767871L;
	/** 采购单号 */
	private String purchase_sn;
	/** 送货单号 */
	private String purchase_delivery_sn;
	/** 商品sku */
	private String sku;
	private String flag; // 0: 成功 -1: 失败
	private String msg;

	public String getPurchase_sn() {
		return purchase_sn;
	}

	public void setPurchase_sn(String purchase_sn) {
		this.purchase_sn = purchase_sn;
	}

	public String getPurchase_delivery_sn() {
		return purchase_delivery_sn;
	}

	public void setPurchase_delivery_sn(String purchase_delivery_sn) {
		this.purchase_delivery_sn = purchase_delivery_sn;
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

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
