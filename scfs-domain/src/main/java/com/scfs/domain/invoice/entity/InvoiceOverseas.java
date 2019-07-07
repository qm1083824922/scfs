package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	境外发票基本信息
 *  File: InvoiceOverseas.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceOverseas extends BaseEntity {
	/** 境外开票id **/
	private Integer id;
	/** 申请编号自动生成 **/
	private String applyNo;
	/** 经营单位 **/
	private Integer businessUnit;
	/** 项目id **/
	private Integer projectId;
	/** 客户id **/
	private Integer customerId;
	/** 币种 **/
	private Integer currnecyType;
	/** 收款账户id **/
	private Integer accountId;
	/** 打印次数 **/
	private Integer printNum;
	/** 申请金额 **/
	private BigDecimal invoiceAmount;
	/** 结算开始日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date balanceStartDate;
	/** 结算结束日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date balanceEndDate;
	/** 单据类型 **/
	private Integer billType;
	/** 开票明细 **/
	private Integer feeType;
	/** 同品合并 0 否 1 是 **/
	private Integer isMerge;
	/** 票据备注 **/
	private String invoiceRemark;
	/** 单据备注 **/
	private String remark;
	/** 状态 1 待提交 20 待财务专员审核 30 待财务主管审核 2 已完成 **/
	private Integer state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public Integer getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(Integer businessUnit) {
		this.businessUnit = businessUnit;
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

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public Date getBalanceStartDate() {
		return balanceStartDate;
	}

	public void setBalanceStartDate(Date balanceStartDate) {
		this.balanceStartDate = balanceStartDate;
	}

	public Date getBalanceEndDate() {
		return balanceEndDate;
	}

	public void setBalanceEndDate(Date balanceEndDate) {
		this.balanceEndDate = balanceEndDate;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public Integer getIsMerge() {
		return isMerge;
	}

	public void setIsMerge(Integer isMerge) {
		this.isMerge = isMerge;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
