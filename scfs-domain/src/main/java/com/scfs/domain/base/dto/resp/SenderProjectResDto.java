package com.scfs.domain.base.dto.resp;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  消息项目
 *  File: SenderProjectResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月19日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class SenderProjectResDto extends BaseEntity {

	/** 发送id **/
	public Integer senderId;

	/** 项目 **/
	public Integer projectId;
	private String projectName;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date creatorAt;

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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getCreatorAt() {
		return creatorAt;
	}

	public void setCreatorAt(Date creatorAt) {
		this.creatorAt = creatorAt;
	}

}
