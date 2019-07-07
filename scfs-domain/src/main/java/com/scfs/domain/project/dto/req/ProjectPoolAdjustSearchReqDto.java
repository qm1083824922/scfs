package com.scfs.domain.project.dto.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: ProjectPoolAdjustSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月22日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ProjectPoolAdjustSearchReqDto extends BaseReqDto {
	/** 项目 **/
	private Integer projectId;

	/** 状态 **/
	private Integer state;

	/** 申请人 **/
	private Integer creatorId;

	/** 开始申请日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startApplyDate;

	/** 结束申请日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endApplyDate;

	private Integer searchType; // 1.查询出未完成和已完成 2....

	private String creator;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getStartApplyDate() {
		return startApplyDate;
	}

	public void setStartApplyDate(Date startApplyDate) {
		this.startApplyDate = startApplyDate;
	}

	public Date getEndApplyDate() {
		return endApplyDate;
	}

	public void setEndApplyDate(Date endApplyDate) {
		this.endApplyDate = endApplyDate;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
}
