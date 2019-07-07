package com.scfs.domain.logistics.dto.req;

/**
 * <pre>
 * 
 *  File: VeriAdvanceSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年1月3日				Administrator
 *
 * </pre>
 */
public class VeriAdvanceSearchReqDto {
	private Integer billDeliveryId;
	private Integer receiptId;

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

}
