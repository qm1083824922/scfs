package com.scfs.domain.project.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: ProjectRiskSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月08日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ProjectRiskSearchReqDto extends BaseReqDto {
	/** 项目id **/
	private Integer projectId;

	/** 事件标题 **/
	private String title;

	/** 事件状态 事件状态 1待提交 2已完成 **/
	private Integer status;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
