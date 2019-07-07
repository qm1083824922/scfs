package com.scfs.domain.pay.dto.resq;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: PayPoRelationResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月09日				Administrator
 *
 * </pre>
 */
public class PayPoRelationResDto {
	/** 付款订单关系id **/
	private Integer id;
	/** 付款id **/
	private Integer payId;
	/** 订单id **/
	private Integer poId;
	/** 本次付款金额 **/
	private BigDecimal payAmount;

	/** 订单编号 */
	private String orderNo;
	/** 附属编号 */
	private String appendNo;
	/** 订单日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTime;
	/** 预计到货日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
	/** 可付款金额 (总额金额-已付+付款金额) **/
	private BigDecimal paymentAmount;
	private Integer currencyId;
	private BigDecimal goodsNum;
	private BigDecimal goodsPrice;
	private BigDecimal goodsAmount;
	private String currencyName;

	/** 商品编码 **/
	private String goodsNo;

	/** 商品名称 **/
	private String goodsName;

	/** 质押比例 **/
	private BigDecimal pledge;

	/** 销售价 **/
	private BigDecimal requiredSendPrice;
	/** 利润率 **/
	private BigDecimal profitMargin;
	private String payNo;
	private BigDecimal discountAmount;
	private BigDecimal inDiscountAmount; // 折扣前金额
	private BigDecimal discountRate;
	private String discountRateStr;
	/** 核销状态 0-未核销 1-已核销 **/
	private Integer writeOffFlag;
	private String writeOffFlagName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

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

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
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

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(BigDecimal requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
	}

	public BigDecimal getProfitMargin() {
		return profitMargin;
	}

	public void setProfitMargin(BigDecimal profitMargin) {
		this.profitMargin = profitMargin;
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

	public BigDecimal getInDiscountAmount() {
		return inDiscountAmount;
	}

	public void setInDiscountAmount(BigDecimal inDiscountAmount) {
		this.inDiscountAmount = inDiscountAmount;
	}

	public BigDecimal getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}

	public String getDiscountRateStr() {
		return discountRateStr;
	}

	public void setDiscountRateStr(String discountRateStr) {
		this.discountRateStr = discountRateStr;
	}

	public BigDecimal getPledge() {
		return pledge;
	}

	public void setPledge(BigDecimal pledge) {
		this.pledge = pledge;
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
