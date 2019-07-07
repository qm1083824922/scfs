package com.scfs.domain.pay.entity;

import com.scfs.domain.base.entity.BaseEntity;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: PayFeeRelation.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月08日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayFeeRelation extends BaseEntity {
	/** 付款ID(tb_pay_order[id]) **/
	private Integer payId;
	/** 费用ID **/
	private Integer feeId;
	/** 付款金额 **/
	private BigDecimal payAmount;
	/** 核销状态 0-未核销 1-已核销 **/
	private Integer writeOffFlag;

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getWriteOffFlag() {
		return writeOffFlag;
	}

	public void setWriteOffFlag(Integer writeOffFlag) {
		this.writeOffFlag = writeOffFlag;
	}

}