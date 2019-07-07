package com.scfs.domain.audit.model;

import java.io.Serializable;

public class InvoiceAuditInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2519089524622132273L;

	// 审核ID
	private Integer auditId;
	// 条款id
	private Integer InvoiceApplyId;
	// 转交、加签人id
	private Integer pauditorId;
	// 转交、加签人id
	private String suggestion;

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public Integer getInvoiceApplyId() {
		return InvoiceApplyId;
	}

	public void setInvoiceApplyId(Integer invoiceApplyId) {
		InvoiceApplyId = invoiceApplyId;
	}

	public Integer getPauditorId() {
		return pauditorId;
	}

	public void setPauditorId(Integer pauditorId) {
		this.pauditorId = pauditorId;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

}