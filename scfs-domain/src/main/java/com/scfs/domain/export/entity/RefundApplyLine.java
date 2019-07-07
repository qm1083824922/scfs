package com.scfs.domain.export.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  出口退税明细
 *  File: RefundApplyLine.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月07日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class RefundApplyLine extends BaseEntity {
	/** 退税申请id **/
	private Integer refundApplyId;
	/** 报关申请id **/
	private Integer customsApplyId;
	/** 金额 **/
	private BigDecimal applyAmount;
	/** 退税数量 **/
	private BigDecimal applyNum;
	/** 可退税额 **/
	private BigDecimal applyTax;
	/** 税率 **/
	private BigDecimal taxRate;

	/** 报关申请编号 **/
	private String applyNo;
	/** 报关申请附属编号 **/
	private String affiliateNo;

	public Integer getRefundApplyId() {
		return refundApplyId;
	}

	public void setRefundApplyId(Integer refundApplyId) {
		this.refundApplyId = refundApplyId;
	}

	public Integer getCustomsApplyId() {
		return customsApplyId;
	}

	public void setCustomsApplyId(Integer customsApplyId) {
		this.customsApplyId = customsApplyId;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public BigDecimal getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(BigDecimal applyNum) {
		this.applyNum = applyNum;
	}

	public BigDecimal getApplyTax() {
		return applyTax;
	}

	public void setApplyTax(BigDecimal applyTax) {
		this.applyTax = applyTax;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
	}

}
