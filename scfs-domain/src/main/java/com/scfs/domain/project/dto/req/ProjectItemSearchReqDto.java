package com.scfs.domain.project.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: QueryProjectItemReq.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月18日				cuichao
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ProjectItemSearchReqDto extends BaseReqDto {
	/** 条款编号 **/
	private String itemNo;
	/** 项目 **/
	private Integer projectId;
	/** 经营单位 **/
	private Integer businessUnitId;
	/** 状态 **/
	private Integer status;
	/** 是否后台调度任务 **/
	private Integer scheduleFlag;

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getScheduleFlag() {
		return scheduleFlag;
	}

	public void setScheduleFlag(Integer scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

}
