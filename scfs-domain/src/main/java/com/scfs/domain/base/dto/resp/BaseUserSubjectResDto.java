package com.scfs.domain.base.dto.resp;

import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: BaseUserSubjectResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月31日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class BaseUserSubjectResDto extends BaseEntity {
	/** 关联用户 */
	private Integer userId;
	/** 仓管操作;0 否 1 是 **/
	private Integer operater;
	private String operaterStr;
	/** 基础信息 **/
	private Integer subjectId;
	private String subjectName;
	/** 分配人 */
	private Integer assignerId;
	private String assigner;
	/** 分配时间 */
	private Date assignAt;
	/** 状态 */
	private Integer state;
	private String stateName;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOperater() {
		return operater;
	}

	public void setOperater(Integer operater) {
		this.operater = operater;
	}

	public String getOperaterStr() {
		return operaterStr;
	}

	public void setOperaterStr(String operaterStr) {
		this.operaterStr = operaterStr;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Integer getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(Integer assignerId) {
		this.assignerId = assignerId;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public Date getAssignAt() {
		return assignAt;
	}

	public void setAssignAt(Date assignAt) {
		this.assignAt = assignAt;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

}
