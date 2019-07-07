package com.scfs.domain.audit.dto.resp;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AuditFlowsResDto {

	/** ID */
	private Integer id;
	/** ID */
	private Integer projectId;

	/** ID */
	private Integer poType;

	/** 节点 */
	private Integer state;
	/** 节点 */
	private String stateName;

	/** 节点 */
	private Integer auditState;
	/** 节点 */
	private String auditStateName;

	/** 处理人 */
	private String dealName;

	/** 开始时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/** 处理时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date dealTime;

	/** 建议 */
	private String suggestion;

	/** 节点 */
	private Integer auditType;

	/** 节点 */
	private Integer pauditId;

	private String backcolor;

	private String fontcolor;

	/**
	 * 经营单位
	 */
	private Integer busiUnit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getDealName() {
		return dealName;
	}

	public void setDealName(String dealName) {
		this.dealName = dealName;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAuditType() {
		return auditType;
	}

	public void setAuditType(Integer auditType) {
		this.auditType = auditType;
	}

	public Integer getPauditId() {
		return pauditId;
	}

	public void setPauditId(Integer pauditId) {
		this.pauditId = pauditId;
	}

	public Integer getPoType() {
		return poType;
	}

	public void setPoType(Integer poType) {
		this.poType = poType;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public String getAuditStateName() {
		return auditStateName;
	}

	public void setAuditStateName(String auditStateName) {
		this.auditStateName = auditStateName;
	}

	public String getBackcolor() {
		return backcolor;
	}

	public void setBackcolor(String backcolor) {
		this.backcolor = backcolor;
	}

	public String getFontcolor() {
		return fontcolor;
	}

	public void setFontcolor(String fontcolor) {
		this.fontcolor = fontcolor;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

}
