package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <pre>
 * 
 *  File: PayFeeRelationModel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月10日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayDeductionFeeRelationModel extends PayFeeRelation {
	/** 费用编号 **/
	private String feeNo;
	/** 费用类型 **/
	private Integer feeType;
	/** 费用日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payDate;
	/** 费用金额 **/
	private BigDecimal expPayAmount;
	/** 收票金额 **/
	private BigDecimal acceptInvoiceAmount;
	/** 已付款金额 **/
	private BigDecimal oldPayAmount;
	/** 币种 **/
	private Integer currencyType;
	/** 付款单号 **/
	private String payNo;
	/** 1.资金占用 2.代付 **/
	private Integer payFeeType;

	public String getFeeNo() {
		return feeNo;
	}

	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getExpPayAmount() {
		return expPayAmount;
	}

	public void setExpPayAmount(BigDecimal expPayAmount) {
		this.expPayAmount = expPayAmount;
	}

	public BigDecimal getAcceptInvoiceAmount() {
		return acceptInvoiceAmount;
	}

	public void setAcceptInvoiceAmount(BigDecimal acceptInvoiceAmount) {
		this.acceptInvoiceAmount = acceptInvoiceAmount;
	}

	public BigDecimal getOldPayAmount() {
		return oldPayAmount;
	}

	public void setOldPayAmount(BigDecimal oldPayAmount) {
		this.oldPayAmount = oldPayAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Integer getPayFeeType() {
		return payFeeType;
	}

	public void setPayFeeType(Integer payFeeType) {
		this.payFeeType = payFeeType;
	}

}
