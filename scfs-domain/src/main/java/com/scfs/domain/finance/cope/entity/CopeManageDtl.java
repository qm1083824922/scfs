package com.scfs.domain.finance.cope.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  应付管理明细
 *  File: CopeDtl.java
 *  Description:
 *  TODO
 *  Date;					Who;				
 *  2017年10月31日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CopeManageDtl extends BaseEntity {
	/** 应付管理id **/
	private Integer copeId;
	/** 单据id **/
	private Integer billId;
	/** 凭证明细id **/
	private Integer voucherLineId;
	/** 项目id **/
	private Integer projectId;
	/** 客户id **/
	private Integer customerId;
	/** 经营单位 **/
	private Integer busiUnitId;
	/** 类型：1 应付费用 **/
	private Integer copeDtlType;
	/** 币种 **/
	private Integer currnecyType;
	/** 单据编号 **/
	private String billNumber;
	/** 单据日期 **/
	private Date billDate;
	/** 单据金额 **/
	private BigDecimal billAmount;
	/** 应付金额(核销金额) **/
	private BigDecimal copeAmount;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	/** 未付金额 **/
	private BigDecimal unpaidAmount;

	public Integer getCopeId() {
		return copeId;
	}

	public void setCopeId(Integer copeId) {
		this.copeId = copeId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getVoucherLineId() {
		return voucherLineId;
	}

	public void setVoucherLineId(Integer voucherLineId) {
		this.voucherLineId = voucherLineId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public Integer getCopeDtlType() {
		return copeDtlType;
	}

	public void setCopeDtlType(Integer copeDtlType) {
		this.copeDtlType = copeDtlType;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public BigDecimal getCopeAmount() {
		return copeAmount;
	}

	public void setCopeAmount(BigDecimal copeAmount) {
		this.copeAmount = copeAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

}
