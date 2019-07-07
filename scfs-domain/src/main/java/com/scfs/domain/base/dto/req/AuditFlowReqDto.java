package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年7月22日.
 */
public class AuditFlowReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8400735337447522047L;

	private String auditFlowNo;

	private Integer auditFlowType;

	private String auditFlowName;

	public String getAuditFlowNo() {
		return auditFlowNo;
	}

	public void setAuditFlowNo(String auditFlowNo) {
		this.auditFlowNo = auditFlowNo;
	}

	public Integer getAuditFlowType() {
		return auditFlowType;
	}

	public void setAuditFlowType(Integer auditFlowType) {
		this.auditFlowType = auditFlowType;
	}

	public String getAuditFlowName() {
		return auditFlowName;
	}

	public void setAuditFlowName(String auditFlowName) {
		this.auditFlowName = auditFlowName;
	}

}
