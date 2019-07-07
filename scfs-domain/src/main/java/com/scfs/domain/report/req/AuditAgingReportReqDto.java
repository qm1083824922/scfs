package com.scfs.domain.report.req;

import com.scfs.domain.BaseReqDto;

/**
 * @Description: TODO (这里用一句话描述这个方法的作用)
 * @author Administrator
 * @date:2017年10月19日下午2:37:55
 * 
 */
public class AuditAgingReportReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4813712486507866906L;

	private String billNo;

	private Integer billType;

	private String startArrivedDate;

	private String endArrivedDate;

	private Integer auditNodeState;

	private Integer auditorId;

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getStartArrivedDate() {
		return startArrivedDate;
	}

	public void setStartArrivedDate(String startArrivedDate) {
		this.startArrivedDate = startArrivedDate;
	}

	public String getEndArrivedDate() {
		return endArrivedDate;
	}

	public void setEndArrivedDate(String endArrivedDate) {
		this.endArrivedDate = endArrivedDate;
	}

	public Integer getAuditNodeState() {
		return auditNodeState;
	}

	public void setAuditNodeState(Integer auditNodeState) {
		this.auditNodeState = auditNodeState;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

}
