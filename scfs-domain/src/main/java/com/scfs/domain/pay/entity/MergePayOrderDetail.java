package com.scfs.domain.pay.entity;

import java.util.List;

/**
 * <pre>
 * 
 *  File: MergePayOrderDetail.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月3日				Administrator
 *
 * </pre>
 */
public class MergePayOrderDetail {
	private MergePayOrder mergePayOrder;

	private List<Integer> ids;

	public MergePayOrder getMergePayOrder() {
		return mergePayOrder;
	}

	public void setMergePayOrder(MergePayOrder mergePayOrder) {
		this.mergePayOrder = mergePayOrder;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
}
