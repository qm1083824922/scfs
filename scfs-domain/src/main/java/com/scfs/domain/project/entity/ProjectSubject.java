package com.scfs.domain.project.entity;

import com.scfs.domain.base.entity.BaseEntity;

public class ProjectSubject extends BaseEntity {

	private static final long serialVersionUID = -8246649983525590753L;

	/** 关联项目 */
	private Integer projectId;

	/** 关联主体 */
	private Integer subjectId;

	/** 主体类型 */
	private Integer subjectType;
	private Integer industrial;
	/** 状态 */
	private Integer status;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public Integer getIndustrial() {
		return industrial;
	}

	public void setIndustrial(Integer industrial) {
		this.industrial = industrial;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
