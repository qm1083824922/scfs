package com.scfs.domain.export.dto.resp;

import java.math.BigDecimal;

/**
 * <pre>
 *  
 *  File: RefundApplyLineResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月07日				Administrator
 *
 * </pre>
 */
public class RefundApplyLineResDto {
	/** 明细id **/
	private Integer id;
	/** 退税申请id **/
	private Integer refundApplyId;
	/** 报关申请id **/
	private Integer customsApplyId;
	/** 退税数量 **/
	private BigDecimal applyNum;
	/** 含税金额 **/
	private BigDecimal applyAmount;
	/** 未税金额 **/
	private BigDecimal exRateAmount;
	/** 税率 **/
	private BigDecimal taxTate;
	/** 可退税额 **/
	private BigDecimal applyTax;
	/** 报关申请编号 **/
	private String applyNo;
	/** 报关申请附属编号 **/
	private String affiliateNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public BigDecimal getApplyNum() {
		return applyNum;
	}

	public void setApplyNum(BigDecimal applyNum) {
		this.applyNum = applyNum;
	}

	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	public BigDecimal getExRateAmount() {
		return exRateAmount;
	}

	public void setExRateAmount(BigDecimal exRateAmount) {
		this.exRateAmount = exRateAmount;
	}

	public BigDecimal getTaxTate() {
		return taxTate;
	}

	public void setTaxTate(BigDecimal taxTate) {
		this.taxTate = taxTate;
	}

	public BigDecimal getApplyTax() {
		return applyTax;
	}

	public void setApplyTax(BigDecimal applyTax) {
		this.applyTax = applyTax;
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
