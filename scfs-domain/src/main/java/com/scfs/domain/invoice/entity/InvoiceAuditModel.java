package com.scfs.domain.invoice.entity;

import java.util.List;

import com.scfs.domain.project.entity.ProjectItemFileAttach;

public class InvoiceAuditModel {
	private InvoiceApplyManager invoiceApplyManager;
	private List<InvoiceSaleManager> invoiceSaleManagerList;
	private List<InvoiceFeeManager> invoiceFeeManagerList;
	private List<InvoiceInfoDtl> InvoiceInfoDtl;
	private List<ProjectItemFileAttach> projectItemFileAttachList;

	public List<InvoiceFeeManager> getInvoiceFeeManagerList() {
		return invoiceFeeManagerList;
	}

	public void setInvoiceFeeManagerList(List<InvoiceFeeManager> invoiceFeeManagerList) {
		this.invoiceFeeManagerList = invoiceFeeManagerList;
	}

	public InvoiceApplyManager getInvoiceApplyManager() {
		return invoiceApplyManager;
	}

	public void setInvoiceApplyManager(InvoiceApplyManager invoiceApplyManager) {
		this.invoiceApplyManager = invoiceApplyManager;
	}

	public List<InvoiceSaleManager> getInvoiceSaleManagerList() {
		return invoiceSaleManagerList;
	}

	public void setInvoiceSaleManagerList(List<InvoiceSaleManager> invoiceSaleManagerList) {
		this.invoiceSaleManagerList = invoiceSaleManagerList;
	}

	public List<InvoiceInfoDtl> getInvoiceInfoDtl() {
		return InvoiceInfoDtl;
	}

	public void setInvoiceInfoDtl(List<InvoiceInfoDtl> invoiceInfoDtl) {
		InvoiceInfoDtl = invoiceInfoDtl;
	}

	public List<ProjectItemFileAttach> getProjectItemFileAttachList() {
		return projectItemFileAttachList;
	}

	public void setProjectItemFileAttachList(List<ProjectItemFileAttach> projectItemFileAttachList) {
		this.projectItemFileAttachList = projectItemFileAttachList;
	}

}
