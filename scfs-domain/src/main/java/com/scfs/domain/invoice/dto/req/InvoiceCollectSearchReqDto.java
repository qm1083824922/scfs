package com.scfs.domain.invoice.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 	
 *  File: InvoiceCollectSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoiceCollectSearchReqDto extends BaseReqDto {
	/** 经营单位 **/
	private Integer businessUnit;
	/** 项目id **/
	private Integer projectId;
	/** 供应商id **/
	private Integer supplierId;
	/** 申请编号 **/
	private String applyNo;
	/** 发票号 **/
	private String invoiceNo;
	/** 票据类型 **/
	private Integer invoiceType;
	/** 单据类别 **/
	private Integer billType;
	/** 状态 **/
	private Integer state;

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

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
