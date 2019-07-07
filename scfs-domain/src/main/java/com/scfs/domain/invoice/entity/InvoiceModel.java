package com.scfs.domain.invoice.entity;

import java.util.List;

public class InvoiceModel {
	/** 销售信息id */
	private Integer id;
	/** 单据明细 id */
	private Integer billDtlid;
	/** 货物id-货物数量 */
	private List<Integer> goodInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillDtlid() {
		return billDtlid;
	}

	public void setBillDtlid(Integer billDtlid) {
		this.billDtlid = billDtlid;
	}

	public List<Integer> getGoodInfo() {
		return goodInfo;
	}

	public void setGoodInfo(List<Integer> goodInfo) {
		this.goodInfo = goodInfo;
	}
}
