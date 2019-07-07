package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 *  消息推送信息
 *  File: senderManageReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月22日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class SenderManageReqDto extends BaseReqDto {
	/** 部门id **/
	public Integer departmentId;

	/** 项目id **/
	public Integer projectId;

	/** 发送业务类型： 0 账期提醒 **/
	public Integer bizSendType;

	/** 用户信息 **/
	private String userName;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBizSendType() {
		return bizSendType;
	}

	public void setBizSendType(Integer bizSendType) {
		this.bizSendType = bizSendType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
