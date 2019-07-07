package com.scfs.domain.audit.dto.req;

/**
 * <pre>
 * 
 *  File: VoucherAuditReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月5日				Administrator
 *
 * </pre>
 */
public class VoucherAuditReqDto {
	// 审核ID
	private Integer auditId;
	// 凭证ID
	private Integer voucherId;
	// 转交、加签人id
	private Integer pauditorId;
	// 审核意见
	private String suggestion;

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public Integer getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(Integer voucherId) {
		this.voucherId = voucherId;
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
