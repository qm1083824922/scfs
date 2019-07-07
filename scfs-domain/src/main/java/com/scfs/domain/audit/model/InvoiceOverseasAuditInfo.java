package com.scfs.domain.audit.model;

import java.util.List;

import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFeeResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasPoResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasResDto;

/**
 * <pre>
 * 
 *  File: InvoiceOverseasAuditInfo.java
 *  Description:境外开票审核信息
 *  TODO
 *  Date,					Who,				
 *  2017年03月29日			Administrator
 *
 * </pre>
 */
public class InvoiceOverseasAuditInfo {
	/** 境外开票基本信息 **/
	private InvoiceOverseasResDto invoiceOverseas;

	/** 境外收票费用信息 **/
	private List<InvoiceOverseasFeeResDto> invoiceOverseasFeeList;

	/** 境外收票销售单信息 **/
	private List<InvoiceOverseasPoResDto> invoiceOverseasPoList;

	/** 境外收票附件信息 **/
	List<InvoiceOverseasFileResDto> invoiceOverseasFileList;

	public InvoiceOverseasResDto getInvoiceOverseas() {
		return invoiceOverseas;
	}

	public void setInvoiceOverseas(InvoiceOverseasResDto invoiceOverseas) {
		this.invoiceOverseas = invoiceOverseas;
	}

	public List<InvoiceOverseasFeeResDto> getInvoiceOverseasFeeList() {
		return invoiceOverseasFeeList;
	}

	public void setInvoiceOverseasFeeList(List<InvoiceOverseasFeeResDto> invoiceOverseasFeeList) {
		this.invoiceOverseasFeeList = invoiceOverseasFeeList;
	}

	public List<InvoiceOverseasPoResDto> getInvoiceOverseasPoList() {
		return invoiceOverseasPoList;
	}

	public void setInvoiceOverseasPoList(List<InvoiceOverseasPoResDto> invoiceOverseasPoList) {
		this.invoiceOverseasPoList = invoiceOverseasPoList;
	}

	public List<InvoiceOverseasFileResDto> getInvoiceOverseasFileList() {
		return invoiceOverseasFileList;
	}

	public void setInvoiceOverseasFileList(List<InvoiceOverseasFileResDto> invoiceOverseasFileList) {
		this.invoiceOverseasFileList = invoiceOverseasFileList;
	}

}
