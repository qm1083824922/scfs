package com.scfs.domain.audit.dto.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * <pre>
 * 
 *  File: FeeAuditReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月5日				Administrator
 *
 * </pre>
 */
public class FeeAuditReqDto {
	// 审核ID
	private Integer auditId;
	// 费用单ID
	private Integer feeId;
	// 转交、加签人id
	private Integer pauditorId;
	// 审核意见
	private String suggestion;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date bookDate;

	public Integer getAuditId() {
		return auditId;
	}

	public void setAuditId(Integer auditId) {
		this.auditId = auditId;
	}

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
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

	public Date getBookDate() {
		return bookDate;
	}

	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}

}
