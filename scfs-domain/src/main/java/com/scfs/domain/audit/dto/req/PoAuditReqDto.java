package com.scfs.domain.audit.dto.req;

/**
 * Created by Administrator on 2016/10/31.
 */
public class PoAuditReqDto {

	// 审核ID
	private Integer auditId;
	// 采购单ID
	private Integer poId;
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

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public Integer getPauditorId() {
		return pauditorId;
	}

	public void setPauditorId(Integer pauditorId) {
		this.pauditorId = pauditorId;
	}

}
