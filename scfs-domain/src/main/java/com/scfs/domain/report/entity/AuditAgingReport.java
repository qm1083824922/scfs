package com.scfs.domain.report.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Description: 审核时效
 * @author Administrator
 * @date:2017年10月19日下午2:46:39
 * 
 */
public class AuditAgingReport {
	/**
	 * 单据编号
	 */
	private String billNo;
	/**
	 * 付款编号
	 */
	private String payNo;
	/**
	 * 单据类型
	 */
	private Integer billType;
	/**
	 * 单据类型名称
	 */
	private String billTypeName;
	/**
	 * 单据抵达时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date arrivedDate;
	/**
	 * 审核通过时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date auditPassDate;
	/**
	 * 审核人ID
	 */
	private Integer auditorId;
	/**
	 * 审核人
	 */
	private String auditor;
	/**
	 * 审核时长
	 */
	private String auditDuration;
	/**
	 * 审核时效
	 */
	private long auditAgeDuration;

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public Date getArrivedDate() {
		return arrivedDate;
	}

	public void setArrivedDate(Date arrivedDate) {
		this.arrivedDate = arrivedDate;
	}

	public Date getAuditPassDate() {
		return auditPassDate;
	}

	public void setAuditPassDate(Date auditPassDate) {
		this.auditPassDate = auditPassDate;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public String getAuditDuration() {
		return auditDuration;
	}

	public void setAuditDuration(String auditDuration) {
		this.auditDuration = auditDuration;
	}

	public long getAuditAgeDuration() {
		return auditAgeDuration;
	}

	public void setAuditAgeDuration(long auditAgeDuration) {
		this.auditAgeDuration = auditAgeDuration;
	}

}
