package com.scfs.domain.audit.model;

import java.util.List;

import com.scfs.domain.project.dto.resp.ProjectPoolAdjustFileDto;
import com.scfs.domain.project.dto.resp.ProjectPoolAdjustResDto;
import com.scfs.domain.project.dto.resp.ProjectPoolResDto;

/**
 * <pre>
 * 
 *  File: ProjectPoolAdjustAuditInfo.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月23日				Administrator
 *
 * </pre>
 */
public class ProjectPoolAdjustAuditInfo {

	private ProjectPoolAdjustResDto projectPoolAdjustResDto;
	private List<ProjectPoolAdjustFileDto> projectPoolAdjustFileDtos;
	private List<ProjectPoolResDto> projectPoolResDtos;

	public ProjectPoolAdjustResDto getProjectPoolAdjustResDto() {
		return projectPoolAdjustResDto;
	}

	public void setProjectPoolAdjustResDto(ProjectPoolAdjustResDto projectPoolAdjustResDto) {
		this.projectPoolAdjustResDto = projectPoolAdjustResDto;
	}

	public List<ProjectPoolAdjustFileDto> getProjectPoolAdjustFileDtos() {
		return projectPoolAdjustFileDtos;
	}

	public void setProjectPoolAdjustFileDtos(List<ProjectPoolAdjustFileDto> projectPoolAdjustFileDtos) {
		this.projectPoolAdjustFileDtos = projectPoolAdjustFileDtos;
	}

	public List<ProjectPoolResDto> getProjectPoolResDtos() {
		return projectPoolResDtos;
	}

	public void setProjectPoolResDtos(List<ProjectPoolResDto> projectPoolResDtos) {
		this.projectPoolResDtos = projectPoolResDtos;
	}
}
