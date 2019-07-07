package com.scfs.domain.audit.dto.req;

import java.io.Serializable;

/**
 * Created by Administrator on 2016年11月2日.
 */
public class ProjectItemReqDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2519089524622132273L;

	// 审核ID
	private Integer auditId;
	// 条款id
	private Integer ProjectItemId;
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

	public Integer getProjectItemId() {
		return ProjectItemId;
	}

	public void setProjectItemId(Integer projectItemId) {
		ProjectItemId = projectItemId;
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
