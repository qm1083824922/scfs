package com.scfs.domain.invoice.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年3月8日.
 */
public class InvoiceCollectApproveSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1222684040484339498L;

	private Integer invoiceCollectId;

	public Integer getInvoiceCollectId() {
		return invoiceCollectId;
	}

	public void setInvoiceCollectId(Integer invoiceCollectId) {
		this.invoiceCollectId = invoiceCollectId;
	}

}
