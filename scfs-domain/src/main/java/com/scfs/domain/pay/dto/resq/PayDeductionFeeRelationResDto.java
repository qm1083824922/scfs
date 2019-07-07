package com.scfs.domain.pay.dto.resq;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: PayFeeRelationResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月10日				Administrator
 *
 * </pre>
 */
public class PayDeductionFeeRelationResDto {
	/** 付款费用关系id **/
	private Integer id;
	/** 付款ID(tb_pay_order[id]) **/
	private Integer payId;
	/** 费用ID **/
	private Integer feeId;
	/** 付款金额 **/
	private BigDecimal payAmount;
	/** 费用编号 **/
	private String feeNo;
	/** 费用类型 **/
	private Integer feeType;
	private String feeTypeName;
	/** 费用日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payDate;
	/** 费用金额 **/
	private BigDecimal expPayAmount;
	/** 收票金额 **/
	private BigDecimal acceptInvoiceAmount;
	/** 已付款金额 **/
	private BigDecimal oldPayAmount;
	/** 可付款金额 (总额金额-已付+付款金额) **/
	private BigDecimal paymentAmount;
	private String payNo;

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

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

}
