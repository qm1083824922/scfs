package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	费用信息
 *  File: InvoiceCollectFeeResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectFeeResDto extends BaseEntity {
	/** 收票费用id **/
	private Integer id;
	/** 收票id **/
	private Integer collectId;
	/** 费用id **/
	private Integer feeId;
	/** 费用编号 **/
	private String feeNo;
	/** 费用类型 **/
	private Integer feeType;
	private String feeTypeName;
	/** 应付费用科目 **/
	private Integer payFeeSpec;
	private String payFeeSpecName;
	/** 费用日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payDate;
	/** 费用收票税率 **/
	private BigDecimal taxRate;
	private BigDecimal acceptInvoiceTaxRate;
	/** 费用应付金额 **/
	private BigDecimal payAmount;
	/** 费用含税金额 **/
	private BigDecimal inRateAmount;
	/** 费用未税金额 **/
	private BigDecimal exRateAmount;
	/** 税额 **/
	private BigDecimal rateAmount;
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

	public Integer getPayFeeSpec() {
		return payFeeSpec;
	}

	public void setPayFeeSpec(Integer payFeeSpec) {
		this.payFeeSpec = payFeeSpec;
	}

	public String getPayFeeSpecName() {
		return payFeeSpecName;
	}

	public void setPayFeeSpecName(String payFeeSpecName) {
		this.payFeeSpecName = payFeeSpecName;
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
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

	public BigDecimal getRateAmount() {
		return rateAmount;
	}

	public void setRateAmount(BigDecimal rateAmount) {
		this.rateAmount = rateAmount;
	}

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
	}

}
