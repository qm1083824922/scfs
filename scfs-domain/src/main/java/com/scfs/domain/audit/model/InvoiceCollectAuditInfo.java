package com.scfs.domain.audit.model;

import java.util.List;

import com.scfs.domain.invoice.dto.resp.InvoiceCollectFeeResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectPoResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectResDto;

/**
 * <pre>
 * 
 *  File: InvoiceCollectAuditInfo.java
 *  Description:财务审核信息
 *  TODO
 *  Date,					Who,				
 *  2016年12月01日			Administrator
 *
 * </pre>
 */
public class InvoiceCollectAuditInfo {
	/** 收票基本信息 **/
	private InvoiceCollectResDto invoiceCollect;

	/** 收票费用信息 **/
	private List<InvoiceCollectFeeResDto> invoiceCollectFeeList;

	/** 收票采购信息 **/
	private List<InvoiceCollectPoResDto> invoiceCollectPoList;

	/** 收票附件信息 **/
	List<InvoiceCollectFileResDto> invoiceCollectFileList;

	public InvoiceCollectResDto getInvoiceCollect() {
		return invoiceCollect;
	}

	public void setInvoiceCollect(InvoiceCollectResDto invoiceCollect) {
		this.invoiceCollect = invoiceCollect;
	}

	public List<InvoiceCollectFeeResDto> getInvoiceCollectFeeList() {
		return invoiceCollectFeeList;
	}

	public void setInvoiceCollectFeeList(List<InvoiceCollectFeeResDto> invoiceCollectFeeList) {
		this.invoiceCollectFeeList = invoiceCollectFeeList;
	}

	public List<InvoiceCollectPoResDto> getInvoiceCollectPoList() {
		return invoiceCollectPoList;
	}

	public void setInvoiceCollectPoList(List<InvoiceCollectPoResDto> invoiceCollectPoList) {
		this.invoiceCollectPoList = invoiceCollectPoList;
	}

	public List<InvoiceCollectFileResDto> getInvoiceCollectFileList() {
		return invoiceCollectFileList;
	}

	public void setInvoiceCollectFileList(List<InvoiceCollectFileResDto> invoiceCollectFileList) {
		this.invoiceCollectFileList = invoiceCollectFileList;
	}

}
