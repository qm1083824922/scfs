package com.scfs.domain.project.entity;

import java.util.List;

public class ProjectItemAuditModel {

	private ProjectItem projectItem;
	private List<ProjectItemFileAttach> projectItemFileAttachList;

	public ProjectItem getProjectItem() {
		return projectItem;
	}

	public void setProjectItem(ProjectItem projectItem) {
		this.projectItem = projectItem;
	}

	public List<ProjectItemFileAttach> getProjectItemFileAttachList() {
		return projectItemFileAttachList;
	}

	public void setProjectItemFileAttachList(List<ProjectItemFileAttach> projectItemFileAttachList) {
		this.projectItemFileAttachList = projectItemFileAttachList;
	}

}
