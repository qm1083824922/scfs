package com.scfs.domain.fee.entity;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  应收应付费用
 *  File: FeeShare.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2017年11月29日             Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FeeRecPay extends BaseEntity {
	/***
	 * 应收id
	 */
	private Integer recFeeId;
	/***
	 * 应付id
	 */
	private Integer payFeeId;

	public Integer getRecFeeId() {
		return recFeeId;
	}

	public void setRecFeeId(Integer recFeeId) {
		this.recFeeId = recFeeId;
	}

	public Integer getPayFeeId() {
		return payFeeId;
	}

	public void setPayFeeId(Integer payFeeId) {
		this.payFeeId = payFeeId;
	}

}
