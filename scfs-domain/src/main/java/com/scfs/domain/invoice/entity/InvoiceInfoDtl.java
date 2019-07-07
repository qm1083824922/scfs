package com.scfs.domain.invoice.entity;

import java.util.List;

public class InvoiceInfoDtl {

	private Integer id;
	private InvoiceInfo invoiceInfoList;
	private List<InvoiceDtlInfo> invoiceDtlInfoList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public InvoiceInfo getInvoiceInfoList() {
		return invoiceInfoList;
	}

	public void setInvoiceInfoList(InvoiceInfo invoiceInfoList) {
		this.invoiceInfoList = invoiceInfoList;
	}

	public List<InvoiceDtlInfo> getInvoiceDtlInfoList() {
		return invoiceDtlInfoList;
	}

	public void setInvoiceDtlInfoList(List<InvoiceDtlInfo> invoiceDtlInfoList) {
		this.invoiceDtlInfoList = invoiceDtlInfoList;
	}
}