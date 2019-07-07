package com.scfs.domain.common.dto.resp;

public class RefreshFundUsedResDto {
	/**
	 * 单据ID
	 */
	private Integer id;
	/**
	 * 单据号
	 */
	private String billNo;
	/**
	 * 单据来源
	 */
	private Integer billSource;
	
	private Integer receiptType;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Integer getBillSource() {
		return billSource;
	}
	public void setBillSource(Integer billSource) {
		this.billSource = billSource;
	}
	public Integer getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}

	
}
