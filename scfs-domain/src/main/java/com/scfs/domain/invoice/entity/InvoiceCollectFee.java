package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	收票费用信息
 *  File: InvoiceCollectFee.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectFee extends BaseEntity {
	/** 收票费用id **/
	private Integer id;
	/** 收票id **/
	private Integer collectId;
	/** 费用id **/
	private Integer feeId;
	/** 费用含税金额 **/
	private BigDecimal inRateAmount;
	/** 费用未税金额 **/
	private BigDecimal exRateAmount;
	/** 税率 **/
	private BigDecimal taxRate;
	/** 税额 **/
	private BigDecimal rateAmount;
	/** 备注 **/
	private String remark;

	/** 费用编号 **/
	private String feeNo;
	/** 费用类型 **/
	private Integer feeType;
	/** 应付费用科目 **/
	private Integer payFeeSpec;
	/** 费用日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date payDate;
	/** 费用收票税率 **/
	private BigDecimal acceptInvoiceTaxRate;
	/** 费用应付金额 **/
	private BigDecimal payAmount;
	/** 可用金额 **/
	private BigDecimal blance;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCollectId() {
		return collectId;
	}

	public void setCollectId(Integer collectId) {
		this.collectId = collectId;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public BigDecimal getInRateAmount() {
		return inRateAmount;
	}

	public void setInRateAmount(BigDecimal inRateAmount) {
		this.inRateAmount = inRateAmount;
	}

	public BigDecimal getExRateAmount() {
		return exRateAmount;
	}

	public void setExRateAmount(BigDecimal exRateAmount) {
		this.exRateAmount = exRateAmount;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getRateAmount() {
		return rateAmount;
	}

	public void setRateAmount(BigDecimal rateAmount) {
		this.rateAmount = rateAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getPayFeeSpec() {
		return payFeeSpec;
	}

	public void setPayFeeSpec(Integer payFeeSpec) {
		this.payFeeSpec = payFeeSpec;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getAcceptInvoiceTaxRate() {
		return acceptInvoiceTaxRate;
	}

	public void setAcceptInvoiceTaxRate(BigDecimal acceptInvoiceTaxRate) {
		this.acceptInvoiceTaxRate = acceptInvoiceTaxRate;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
	}

}
