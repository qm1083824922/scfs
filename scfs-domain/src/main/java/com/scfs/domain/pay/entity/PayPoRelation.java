package com.scfs.domain.pay.entity;

import com.scfs.domain.base.entity.BaseEntity;

import java.math.BigDecimal;

/**
 * <pre>
 *  付款订单关系
 *  File: PayPoRelation.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月08日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayPoRelation extends BaseEntity {
	/** 付款订单关系id **/
	private Integer id;
	/** 付款id **/
	private Integer payId;
	/** 订单id **/
	private Integer poId;
	/** 订单明细id **/
	private Integer poLineId;
	/** 本次付款金额 **/
	private BigDecimal payAmount;
	/** 折扣金额 **/
	private BigDecimal discountAmount;
	/** 折扣前金额 **/
	private BigDecimal inDiscountAmount;
	/** 抵扣金额 **/
	private BigDecimal ductionMoney;
	private BigDecimal prePayAmount;
	/** 核销状态 0-未核销 1-已核销 **/
	private Integer writeOffFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPayId() {
		return payId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getPoLineId() {
		return poLineId;
	}

	public void setPoLineId(Integer poLineId) {
		this.poLineId = poLineId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getInDiscountAmount() {
		return inDiscountAmount;
	}

	public void setInDiscountAmount(BigDecimal inDiscountAmount) {
		this.inDiscountAmount = inDiscountAmount;
	}

	public BigDecimal getDuctionMoney() {
		return ductionMoney;
	}

	public void setDuctionMoney(BigDecimal ductionMoney) {
		this.ductionMoney = ductionMoney;
	}

	public BigDecimal getPrePayAmount() {
		return prePayAmount;
	}

	public void setPrePayAmount(BigDecimal prePayAmount) {
		this.prePayAmount = prePayAmount;
	}

	public Integer getWriteOffFlag() {
		return writeOffFlag;
	}

	public void setWriteOffFlag(Integer writeOffFlag) {
		this.writeOffFlag = writeOffFlag;
	}

}