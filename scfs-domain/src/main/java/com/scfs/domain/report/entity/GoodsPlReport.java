package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GoodsPlReport {

	/**
	 * 付款单Id
	 */
	private Integer payId;

	/**
	 * 采购单ID
	 */
	private Integer poId;

	/**
	 *  
	 */
	private Integer poState;
	/**
	 * 付款类型
	 */
	private Integer payType;

	/**
	 * 付款状态
	 */
	private Integer payState;
	/**
	 * 代销订单号 （po 订单编号）
	 */
	private String orderNo;

	/**
	 * 附属编号
	 */
	private String appendNo;

	/***
	 * 付款单号
	 */
	private String payNo;

	/***
	 * 付款附属编号
	 */
	private String payAppendNo;

	/**
	 * 付款单币种
	 */
	private Integer currencyType;
	/**
	 * 付款单币种名称
	 */
	private String currencyTypeName;

	/**
	 * 付款数量
	 */
	private BigDecimal payNumber;

	/**
	 * 付款金额
	 */
	private BigDecimal payAmount;
	/**
	 * 实际支付币种
	 */
	private Integer realCurrencyType;

	private String realCurrencyTypeName;
	/**
	 * 实际付款金额
	 */
	private BigDecimal realPayAmount;
	/**
	 * 实际付款金额
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date paydate;

	/** 项目名称 **/
	private String projectName;
	/** 项目Id **/
	private Integer projectId;
	/** 供应商 **/
	private String supplierName;
	/** 供应商Id **/
	private Integer supplierId;
	/** 客户名称 **/
	private String customerName;
	/** 客户Id **/
	private Integer customerId;
	/** 商品编号 **/
	private String goodsCode;

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

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPayAppendNo() {
		return payAppendNo;
	}

	public void setPayAppendNo(String payAppendNo) {
		this.payAppendNo = payAppendNo;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getPayNumber() {
		return payNumber;
	}

	public void setPayNumber(BigDecimal payNumber) {
		this.payNumber = payNumber;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getRealCurrencyType() {
		return realCurrencyType;
	}

	public void setRealCurrencyType(Integer realCurrencyType) {
		this.realCurrencyType = realCurrencyType;
	}

	public String getRealCurrencyTypeName() {
		return realCurrencyTypeName;
	}

	public void setRealCurrencyTypeName(String realCurrencyTypeName) {
		this.realCurrencyTypeName = realCurrencyTypeName;
	}

	public BigDecimal getRealPayAmount() {
		return realPayAmount;
	}

	public void setRealPayAmount(BigDecimal realPayAmount) {
		this.realPayAmount = realPayAmount;
	}

	public Date getPaydate() {
		return paydate;
	}

	public void setPaydate(Date paydate) {
		this.paydate = paydate;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayState() {
		return payState;
	}

	public void setPayState(Integer payState) {
		this.payState = payState;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getPoState() {
		return poState;
	}

	public void setPoState(Integer poState) {
		this.poState = poState;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

}
