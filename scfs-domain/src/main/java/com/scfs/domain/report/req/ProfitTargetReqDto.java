package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: ProfitTargetReqDto.java
 *  Description: 业务指标目标值
 *  TODO
 *  Date,					Who,				
 *  2017年7月14日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ProfitTargetReqDto extends BaseReqDto {
	/** 部门 **/
	private Integer departmentId;
	/** 业务员 **/
	private String userName;
	/** 开始统计时间 **/
	private String startStatisticsDate;
	/** 结束统计时间 **/
	private String endStatisticsDate;
	/** 状态 **/
	private Integer state;
	/** 用户列表 **/
	private List<Integer> userIds;

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getStartStatisticsDate() {
		return startStatisticsDate;
	}

	public void setStartStatisticsDate(String startStatisticsDate) {
		this.startStatisticsDate = startStatisticsDate;
	}

	public String getEndStatisticsDate() {
		return endStatisticsDate;
	}

	public void setEndStatisticsDate(String endStatisticsDate) {
		this.endStatisticsDate = endStatisticsDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

}
