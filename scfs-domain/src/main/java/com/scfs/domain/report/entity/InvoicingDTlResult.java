package com.scfs.domain.report.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InvoicingDTlResult {

	private String billNo;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date billDate;
	private Integer billType;
	private String billTypeName;
	private BigDecimal number;
	private BigDecimal amount;
	private BigDecimal numberSum;
	private BigDecimal amountSum;

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public BigDecimal getNumber() {
		return number;
	}

	public void setNumber(BigDecimal number) {
		this.number = number;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getNumberSum() {
		return numberSum;
	}

	public void setNumberSum(BigDecimal numberSum) {
		this.numberSum = numberSum;
	}

	public BigDecimal getAmountSum() {
		return amountSum;
	}

	public void setAmountSum(BigDecimal amountSum) {
		this.amountSum = amountSum;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
}
