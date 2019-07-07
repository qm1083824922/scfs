package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	
 *  File: InvoiceCollectOverseasFeeResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月15日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectOverseasFeeResDto extends BaseEntity {
	/**
	 * 境外收票id
	 */
	private Integer collectOverseasId;

	/**
	 * 费用ID
	 */
	private Integer feeId;

	/**
	 * 收票金额
	 */
	private BigDecimal invoiceAmount;

	/** 币种 **/
	private Integer currencyType;
	/** 币种名称 **/
	private String currencyTypeName;

	/** 费用编号 **/
	private String feeNo;
	/** 费用类型 **/
	private Integer feeType;
	/** 费用类型 **/
	private String feeTypeName;
	/** 费用日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payDate;

	public Integer getCollectOverseasId() {
		return collectOverseasId;
	}

	public void setCollectOverseasId(Integer collectOverseasId) {
		this.collectOverseasId = collectOverseasId;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
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

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
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
}
