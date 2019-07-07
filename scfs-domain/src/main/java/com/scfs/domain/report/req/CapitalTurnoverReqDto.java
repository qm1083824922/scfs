package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 *  资金周转率信息
 *  File: CapitalTurnoverReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月06日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CapitalTurnoverReqDto extends BaseReqDto {
	/** 部门 **/
	private List<Integer> departmentId;
	/** 项目 **/
	private Integer projectId;
	/** 用户id **/
	private Integer userId;
	/** 开始时间 **/
	private String startTime;
	/** 结束时间 **/
	private String endTime;
	/** 当月时间 **/
	private String issue;

	public List<Integer> getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(List<Integer> departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

}
