package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 *  消息项目
 *  File: SenderProjectReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月19日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class SenderProjectReqDto extends BaseReqDto {
	/** 发送id **/
	public Integer senderId;

	/** 项目id **/
	public Integer projectId;

	public String projectName;
	public String projectNo;

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

	public String getProjectNo() {
		return projectNo;
	}

	public void setProjectNo(String projectNo) {
		this.projectNo = projectNo;
	}

}
