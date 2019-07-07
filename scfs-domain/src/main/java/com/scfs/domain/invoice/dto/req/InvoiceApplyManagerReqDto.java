package com.scfs.domain.invoice.dto.req;

import com.scfs.domain.BaseReqDto;

public class InvoiceApplyManagerReqDto extends BaseReqDto {

	private static final long serialVersionUID = 1160345133780718527L;

	/** 自增ID */
	private Integer id;
	/** 申请编号 */
	private String applyNo;
	/** 申请类型 1-开票 2-收票 */
	private Integer applyType;
	/** 项目ID */
	private Integer projectId;
	/** 客户ID */
	private Integer customerId;
	/** 发票编号 */
	private String invoiceNo;
	/** 发票号 */
	private String invoiceCode;
	/** 经营单位ID */
	private Integer businessUnitId;
	/** 发票类型 1-增值税专用发票 2-增值税普通发票 */
	private Integer invoiceType;
	/** 开票信息 关联tb_base_invoice */
	private Integer baseInvoiceId;
	/** 状态 1-待模拟 2-待提交 3-待财务审核 4-待确认 5-已完成 */
	private Integer status;
	/** 单据类别 1-货物 2-费用 */
	private Integer billType;

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

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
		this.applyNo = applyNo == null ? null : applyNo.trim();
	}

	public Integer getApplyType() {
		return applyType;
	}

	public void setApplyType(Integer applyType) {
		this.applyType = applyType;
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

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Integer getBaseInvoiceId() {
		return baseInvoiceId;
	}

	public void setBaseInvoiceId(Integer baseInvoiceId) {
		this.baseInvoiceId = baseInvoiceId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

}