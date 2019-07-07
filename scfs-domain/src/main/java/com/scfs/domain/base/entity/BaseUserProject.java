package com.scfs.domain.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * 用户项目关联对象
 * 
 * @author 
 *
 */
public class BaseUserProject extends BaseEntity {

	private static final long serialVersionUID = -8808146911618864555L;

	/** 关联用户 */
	private Integer userId;
	private Integer businessUnitId;
	/** 分配人 */
	private String assigner;

	private Integer assignerId;

	/** 分配时间 */
	private Date assignAt;

	private Integer projectId;
	/** 状态 */
	private Integer state;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	private String projectName;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAssignAt() {
		return assignAt;
	}

	public void setAssignAt(Date assignAt) {
		this.assignAt = assignAt;
	}

	public Integer getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(Integer assignerId) {
		this.assignerId = assignerId;
	}
}
