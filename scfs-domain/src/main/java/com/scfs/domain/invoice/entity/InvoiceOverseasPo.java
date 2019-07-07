package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	境外发票销售基本信息
 *  File: InvoiceOverseasPo.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceOverseasPo extends BaseEntity {
	/** 境外销售id **/
	private Integer id;
	/** 境外收票id **/
	private Integer overseasId;
	/** 销售单id **/
	private Integer billDeliveryId;
	/** 开票数量 **/
	private BigDecimal invoiceNum;
	/** 开票金额 **/
	private BigDecimal invoiceAmount;

	/** 销售单编号 **/
	private String billNo;
	/** 日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date requiredSendDate;
	/** 商品编号 **/
	private String goodsNumber;
	/** 商品描述 **/
	private String goodsName;
	/** 数量 **/
	private BigDecimal requiredSendNum;
	/** 单价 **/
	private BigDecimal requiredSendPrice;
	/** 开票数量 **/
	private BigDecimal provideInvoiceNum;
	/** 开票金额 **/
	private BigDecimal provideInvoiceAmount;

	/** 单价 **/
	private String unit;
	/** 注册地 **/
	private String regPlace;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOverseasId() {
		return overseasId;
	}

	public void setOverseasId(Integer overseasId) {
		this.overseasId = overseasId;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public BigDecimal getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(BigDecimal invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getRequiredSendDate() {
		return requiredSendDate;
	}

	public void setRequiredSendDate(Date requiredSendDate) {
		this.requiredSendDate = requiredSendDate;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public BigDecimal getRequiredSendNum() {
		return requiredSendNum;
	}

	public void setRequiredSendNum(BigDecimal requiredSendNum) {
		this.requiredSendNum = requiredSendNum;
	}

	public BigDecimal getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(BigDecimal requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
	}

	public BigDecimal getProvideInvoiceNum() {
		return provideInvoiceNum;
	}

	public void setProvideInvoiceNum(BigDecimal provideInvoiceNum) {
		this.provideInvoiceNum = provideInvoiceNum;
	}

	public BigDecimal getProvideInvoiceAmount() {
		return provideInvoiceAmount;
	}

	public void setProvideInvoiceAmount(BigDecimal provideInvoiceAmount) {
		this.provideInvoiceAmount = provideInvoiceAmount;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRegPlace() {
		return regPlace;
	}

	public void setRegPlace(String regPlace) {
		this.regPlace = regPlace;
	}

}
