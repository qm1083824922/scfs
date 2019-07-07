package com.scfs.domain.project.entity;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: ProjectRisk.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月22日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ProjectRisk extends BaseEntity {
	/** 主键id **/
	private Integer id;
	/** 项目id **/
	private Integer projectId;
	/** 类型 **/
	private String risktype;
	/** 事件标题 **/
	private String title;
	/** 事件内容 **/
	private String remarks;
	/** 事件状态 1待提交 2已完成 **/
	private Integer status;

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

	public String getRisktype() {
		return risktype;
	}

	public void setRisktype(String risktype) {
		this.risktype = risktype;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
