package com.scfs.domain.audit.model;

import java.io.Serializable;
import java.util.List;

import com.scfs.domain.project.entity.ProjectItem;

/**
 * Created by Administrator on 2016年11月2日.
 */
public class ProejctItemAuditInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1523503650992671749L;
	/** 审核单头信息 */
	private ProjectItem projectItem;
	/** 审核单行列表信息 */
	private List<ProjectItem> projectItemList;

	public ProjectItem getProjectItem() {
		return projectItem;
	}

	public void setProjectItem(ProjectItem projectItem) {
		this.projectItem = projectItem;
	}

	public List<ProjectItem> getProjectItemList() {
		return projectItemList;
	}

	public void setProjectItemList(List<ProjectItem> projectItemList) {
		this.projectItemList = projectItemList;
	}

}
