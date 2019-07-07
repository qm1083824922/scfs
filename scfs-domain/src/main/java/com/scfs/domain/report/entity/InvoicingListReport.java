package com.scfs.domain.report.entity;

import java.util.List;

public class InvoicingListReport {

	private InvoicingReport invoicingReport;
	private List<InvoicingDTlResult> invoicingList;

	public InvoicingReport getInvoicingReport() {
		return invoicingReport;
	}

	public void setInvoicingReport(InvoicingReport invoicingReport) {
		this.invoicingReport = invoicingReport;
	}

	public List<InvoicingDTlResult> getInvoicingList() {
		return invoicingList;
	}

	public void setInvoicingList(List<InvoicingDTlResult> invoicingList) {
		this.invoicingList = invoicingList;
	}

}
