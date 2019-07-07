package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <pre>
 * 
 *  File: PayPoRelationModel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月09日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayPoRelationModel extends PayPoRelation {
	/** 订单编号 */
	private String orderNo;
	/** 附属编号 */
	private String appendNo;
	/** 订单日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orderTime;
	/** 预计到货日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date perdictTime;
	/** 订单总数量 */
	private BigDecimal orderTotalNum;
	/** 订单总金额 */
	private BigDecimal orderTotalAmount;
	/** 订单明细收票金额 */
	private BigDecimal invoiceAmount;
	/** 订单明细到货金额 */
	private BigDecimal arrivalAmount;
	/** 订单明细已付款金额 */
	private BigDecimal paidAmount;
	private Integer currencyId;
	private BigDecimal goodsNum;
	private BigDecimal goodsPrice;
	private BigDecimal goodsAmount;
	/** 商品id **/
	private Integer goodsId;
	/** 销售价 **/
	private BigDecimal requiredSendPrice;
	private String payNo;
	private BigDecimal discountAmount;
	/** 核销状态 0-未核销 1-已核销 **/
	private Integer writeOffFlag;
	private String writeOffFlagName;

	public BigDecimal getDuctionMoney() {
		return ductionMoney;
	}

	public void setDuctionMoney(BigDecimal ductionMoney) {
		this.ductionMoney = ductionMoney;
	}

	/** 抵扣金额 **/
	private BigDecimal ductionMoney;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getPerdictTime() {
		return perdictTime;
	}

	public void setPerdictTime(Date perdictTime) {
		this.perdictTime = perdictTime;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getArrivalAmount() {
		return arrivalAmount;
	}

	public void setArrivalAmount(BigDecimal arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public BigDecimal getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(BigDecimal goodsNum) {
		this.goodsNum = goodsNum;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public BigDecimal getOrderTotalNum() {
		return orderTotalNum;
	}

	public void setOrderTotalNum(BigDecimal orderTotalNum) {
		this.orderTotalNum = orderTotalNum;
	}

	public BigDecimal getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public BigDecimal getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(BigDecimal requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Integer getWriteOffFlag() {
		return writeOffFlag;
	}

	public void setWriteOffFlag(Integer writeOffFlag) {
		this.writeOffFlag = writeOffFlag;
	}

	public String getWriteOffFlagName() {
		return writeOffFlagName;
	}

	public void setWriteOffFlagName(String writeOffFlagName) {
		this.writeOffFlagName = writeOffFlagName;
	}

}
