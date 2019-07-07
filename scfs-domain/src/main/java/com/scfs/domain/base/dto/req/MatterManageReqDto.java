package com.scfs.domain.base.dto.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 *  事项管理 
 *  File: MatterManageReqDto.java
 *  Description: 
 *  TODO
 *  Date,					Who,				
 *  2017年07月28日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class MatterManageReqDto extends BaseReqDto {
	/** 事项编码,自动生成 **/
	private String matterNo;
	/** 事项名称,1 项目导入表 2 项目监控 **/
	private Integer matterName;
	/** 事项类型, 1 客户事项 2 项目事项 **/
	private Integer matterType;
	/** 项目 **/
	private Integer projectId;
	/** 客户 **/
	private String customerName;
	/** 申请日期开始日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startCreateTime;
	/** 申请日期结束日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endCreateTime;
	/** 状态 0待提交 **/
	private Integer state;

	/** 客户跟进id **/
	private Integer followId;

	public String getMatterNo() {
		return matterNo;
	}

	public void setMatterNo(String matterNo) {
		this.matterNo = matterNo;
	}

	public Integer getMatterName() {
		return matterName;
	}

	public void setMatterName(Integer matterName) {
		this.matterName = matterName;
	}

	public Integer getMatterType() {
		return matterType;
	}

	public void setMatterType(Integer matterType) {
		this.matterType = matterType;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getStartCreateTime() {
		return startCreateTime;
	}

	public void setStartCreateTime(Date startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getFollowId() {
		return followId;
	}

	public void setFollowId(Integer followId) {
		this.followId = followId;
	}

}
