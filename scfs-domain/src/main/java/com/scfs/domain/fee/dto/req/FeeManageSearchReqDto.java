package com.scfs.domain.fee.dto.req;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 	管理费用基本信息
 *  File: FeeManageSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年04月12日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FeeManageSearchReqDto extends BaseReqDto {
	/** 管理费用id **/
	private Integer id;
	/** 费用科目类型 **/
	private Integer feeType;
	/** 部门id **/
	private List<Integer> departmentId = new ArrayList<Integer>();
	/** 项目 **/
	private Integer projectId;
	/** 开始日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/** 结束日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/** 状态 0 未提交 **/
	private Integer state;
	/** 创建人id **/
	private Integer creatorId;

	/** 单号 **/
	private String feeManageNo;

	/***
	 * 查询分摊，1表示是，2表示否 key:IS_NEED
	 */
	private Integer needShare;

	public String getFeeManageNo() {
		return feeManageNo;
	}

	public void setFeeManageNo(String feeManageNo) {
		this.feeManageNo = feeManageNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

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

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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

	public Integer getNeedShare() {
		return needShare;
	}

	public void setNeedShare(Integer needShare) {
		this.needShare = needShare;
	}

}
