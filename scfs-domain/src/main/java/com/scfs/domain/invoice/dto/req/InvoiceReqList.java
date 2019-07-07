package com.scfs.domain.invoice.dto.req;

import java.util.List;

import com.scfs.domain.invoice.entity.InvoiceFeeManager;
import com.scfs.domain.invoice.entity.InvoiceSaleManager;

public class InvoiceReqList {

	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/** 费用信息 */
	private List<InvoiceFeeManager> invoiceFeeManagerReqDto;
	/** 销售信息 */
	private List<InvoiceSaleManager> invoiceSaleManagerReqDto;

	public List<InvoiceFeeManager> getInvoiceFeeManagerReqDto() {
		return invoiceFeeManagerReqDto;
	}

	public void setInvoiceFeeManagerReqDto(List<InvoiceFeeManager> invoiceFeeManagerReqDto) {
		this.invoiceFeeManagerReqDto = invoiceFeeManagerReqDto;
	}

	public List<InvoiceSaleManager> getInvoiceSaleManagerReqDto() {
		return invoiceSaleManagerReqDto;
	}

	public void setInvoiceSaleManagerReqDto(List<InvoiceSaleManager> invoiceSaleManagerReqDto) {
		this.invoiceSaleManagerReqDto = invoiceSaleManagerReqDto;
	}

}
