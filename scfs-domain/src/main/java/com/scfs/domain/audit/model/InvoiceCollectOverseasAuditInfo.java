package com.scfs.domain.audit.model;

import java.util.List;

import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasFeeResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasPoFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasPoResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectOverseasResDto;

public class InvoiceCollectOverseasAuditInfo {

	/** Invoice 收票基本信息 **/
	private InvoiceCollectOverseasResDto overseasResDto;

	/** Invoice收票费用信息 **/
	List<InvoiceCollectOverseasFeeResDto> overseasFeeResDto;

	/** Invoice收票采购信息 **/
	List<InvoiceCollectOverseasPoResDto> overseasPoResDto;

	/** 收票附件信息 **/
	List<InvoiceCollectOverseasPoFileResDto> invoiceCollectFileList;

	public InvoiceCollectOverseasResDto getOverseasResDto() {
		return overseasResDto;
	}

	public void setOverseasResDto(InvoiceCollectOverseasResDto overseasResDto) {
		this.overseasResDto = overseasResDto;
	}

	public List<InvoiceCollectOverseasFeeResDto> getOverseasFeeResDto() {
		return overseasFeeResDto;
	}

	public void setOverseasFeeResDto(List<InvoiceCollectOverseasFeeResDto> overseasFeeResDto) {
		this.overseasFeeResDto = overseasFeeResDto;
	}

	public List<InvoiceCollectOverseasPoResDto> getOverseasPoResDto() {
		return overseasPoResDto;
	}

	public void setOverseasPoResDto(List<InvoiceCollectOverseasPoResDto> overseasPoResDto) {
		this.overseasPoResDto = overseasPoResDto;
	}

	public List<InvoiceCollectOverseasPoFileResDto> getInvoiceCollectFileList() {
		return invoiceCollectFileList;
	}

	public void setInvoiceCollectFileList(List<InvoiceCollectOverseasPoFileResDto> invoiceCollectFileList) {
		this.invoiceCollectFileList = invoiceCollectFileList;
	}

}
