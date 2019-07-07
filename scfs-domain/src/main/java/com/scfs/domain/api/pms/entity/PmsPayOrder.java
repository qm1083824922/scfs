package com.scfs.domain.api.pms.entity;

import java.util.List;

/**
 * <pre>
 * 
 *  File: PmsPayOrder.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月15日			Administrator
 *
 * </pre>
 */
public class PmsPayOrder {
	private PmsPayOrderTitle orderTitle;

	private List<PmsPayOrderDtl> orderDtls;

	public PmsPayOrderTitle getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(PmsPayOrderTitle orderTitle) {
		this.orderTitle = orderTitle;
	}

	public List<PmsPayOrderDtl> getOrderDtls() {
		return orderDtls;
	}

	public void setOrderDtls(List<PmsPayOrderDtl> orderDtls) {
		this.orderDtls = orderDtls;
	}
}
