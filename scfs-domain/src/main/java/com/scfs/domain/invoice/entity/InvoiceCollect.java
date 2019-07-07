package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	收票基本信息
 *  File: InvoiceCollect.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollect extends BaseEntity {
	/** 收票id **/
	private Integer id;
	/** 申请编号 **/
	private String applyNo;
	/** 经营单位 **/
	private Integer businessUnit;
	/** 项目id **/
	private Integer projectId;
	/** 供应商id **/
	private Integer supplierId;
	private Integer printNum;

	/** 票据类型 1-增值税专用发票 2-增值税普通发票 **/
	private Integer invoiceType;
	/** 单据类别 1-货物 2-费用 **/
	private Integer billType;
	/** 发票金额 **/
	private BigDecimal invoiceAmount;
	/** 发票号 **/
	private String invoiceNo;
	/** 发票日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date invoiceDate;
	/** 认证日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date approveDate;
	/** 已认证金额 **/
	private BigDecimal approveAmount;
	/** 未认证金额 **/
	private BigDecimal unApproveAmount;
	/** 收票税率 **/
	private BigDecimal invoiceTaxRate;
	/** 票据备注 **/
	private String invoiceRemark;
	/** 状态 1 待提交 2 待财务审核 3 待认证 4 已完成 **/
	private Integer state;
	/** 单据备注 **/
	private String remark;
	/** 当前认证日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date currApproveDate;
	/** 当前认证金额 **/
	private BigDecimal currApproveAmount;
	/** 当前认证备注 **/
	private String currApproveRemark;

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

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public Date getApproveDate() {
		return approveDate;
	}

	public void setApproveDate(Date approveDate) {
		this.approveDate = approveDate;
	}

	public BigDecimal getInvoiceTaxRate() {
		return invoiceTaxRate;
	}

	public void setInvoiceTaxRate(BigDecimal invoiceTaxRate) {
		this.invoiceTaxRate = invoiceTaxRate;
	}

	public String getInvoiceRemark() {
		return invoiceRemark;
	}

	public void setInvoiceRemark(String invoiceRemark) {
		this.invoiceRemark = invoiceRemark;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public BigDecimal getApproveAmount() {
		return approveAmount;
	}

	public void setApproveAmount(BigDecimal approveAmount) {
		this.approveAmount = approveAmount;
	}

	public BigDecimal getUnApproveAmount() {
		return unApproveAmount;
	}

	public void setUnApproveAmount(BigDecimal unApproveAmount) {
		this.unApproveAmount = unApproveAmount;
	}

	public BigDecimal getCurrApproveAmount() {
		return currApproveAmount;
	}

	public void setCurrApproveAmount(BigDecimal currApproveAmount) {
		this.currApproveAmount = currApproveAmount;
	}

	public Date getCurrApproveDate() {
		return currApproveDate;
	}

	public void setCurrApproveDate(Date currApproveDate) {
		this.currApproveDate = currApproveDate;
	}

	public String getCurrApproveRemark() {
		return currApproveRemark;
	}

	public void setCurrApproveRemark(String currApproveRemark) {
		this.currApproveRemark = currApproveRemark;
	}

}
