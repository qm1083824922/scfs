package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	境外发票费用基本信息
 *  File: InvoiceOverseasFee.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceOverseasFee extends BaseEntity {
	/** 境外费用id **/
	private Integer id;
	/** 境外收票 **/
	private Integer overseasId;
	/** 费用id **/
	private Integer feeId;
	/** 开票金额 **/
	private BigDecimal invoiceAmount;

	/** 项目 **/
	private Integer projectId;
	/** 费用编号 **/
	private String feeNo;
	/** 费用类型 **/
	private Integer feeType;
	/** 费用日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date recDate;
	/** 可开票金额 **/
	private BigDecimal blanceInvoiceAmount;
	/** 币种 **/
	private Integer currencyType;

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

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
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

	public Date getRecDate() {
		return recDate;
	}

	public void setRecDate(Date recDate) {
		this.recDate = recDate;
	}

	public BigDecimal getBlanceInvoiceAmount() {
		return blanceInvoiceAmount;
	}

	public void setBlanceInvoiceAmount(BigDecimal blanceInvoiceAmount) {
		this.blanceInvoiceAmount = blanceInvoiceAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

}
