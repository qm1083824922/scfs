package com.scfs.domain.base.entity;

/**
 * <pre>
 *  消息项目关系
 *  File: SenderProject.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月19日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class SenderProject extends BaseEntity {

	/** 发送id **/
	public Integer senderId;

	/** 项目id **/
	public Integer projectId;

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

}
