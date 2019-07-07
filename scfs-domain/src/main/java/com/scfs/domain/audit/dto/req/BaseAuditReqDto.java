package com.scfs.domain.audit.dto.req;

import java.io.Serializable;

/**
 * <pre>
 * 
 *  File: BaseAdutiReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月23日				Administrator
 *
 * </pre>
 */
public class BaseAuditReqDto implements Serializable {
	private static final long serialVersionUID = -1227213975594546470L;
	// 审核ID
	private Integer auditId;
	// 条款id
	private Integer poId;
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

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
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
