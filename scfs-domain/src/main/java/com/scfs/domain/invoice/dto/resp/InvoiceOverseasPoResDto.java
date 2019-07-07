package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	境外开票销售信息
 *  File: InvoiceOverseasPoResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceOverseasPoResDto extends BaseEntity {
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
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date requiredSendDate;
	/** 商品编号 **/
	private String goodsNumber;
	/** 商品描述 **/
	private String goodsName;
	/** 可开票数量 **/
	private BigDecimal provideMaxNum;
	/** 可开票金额 **/
	private BigDecimal provideMaxAmount;

	/** 单价 **/
	private BigDecimal costPrice;
	/** 单位 **/
	private String unit;

	/** 销售单统计 **/
	private BigDecimal sumNum;
	private BigDecimal sumAmount;
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

	public BigDecimal getProvideMaxNum() {
		return provideMaxNum;
	}

	public void setProvideMaxNum(BigDecimal provideMaxNum) {
		this.provideMaxNum = provideMaxNum;
	}

	public BigDecimal getProvideMaxAmount() {
		return provideMaxAmount;
	}

	public void setProvideMaxAmount(BigDecimal provideMaxAmount) {
		this.provideMaxAmount = provideMaxAmount;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getSumNum() {
		return sumNum;
	}

	public void setSumNum(BigDecimal sumNum) {
		this.sumNum = sumNum;
	}

	public BigDecimal getSumAmount() {
		return sumAmount;
	}

	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	public String getRegPlace() {
		return regPlace;
	}

	public void setRegPlace(String regPlace) {
		this.regPlace = regPlace;
	}

}
