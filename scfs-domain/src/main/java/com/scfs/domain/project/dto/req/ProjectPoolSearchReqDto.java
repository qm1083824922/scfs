package com.scfs.domain.project.dto.req;

import com.scfs.domain.BaseReqDto;

@SuppressWarnings("serial")
public class ProjectPoolSearchReqDto extends BaseReqDto {

	/** ID */
	private Integer id;

	/** 项目ID */
	private Integer projectId;

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

}
