package com.scfs.domain.audit.dto.req;

import java.io.Serializable;

/**
 * Created by Administrator on 2016年11月5日.
 */
public class BillOutStoreAuditReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9086595364914863982L;

	// 审核ID
	private Integer auditId;
	// 出库单ID
	private Integer billOutStoreId;
	// 转交、加签人id
	private Integer pauditorId;
	// 转交、加签人id
	private String suggestion;

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public Integer getPauditorId() {
		return pauditorId;
	}

	public void setPauditorId(Integer pauditorId) {
		this.pauditorId = pauditorId;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

}
