package com.scfs.domain.base.entity;

import java.util.Date;

/**
 * <pre>
 *  用户基础信息关系
 *  File: BaseUserSubject.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月31日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class BaseUserSubject extends BaseEntity {
	/** 关联用户 */
	private Integer userId;
	/** 主体ID **/
	private Integer subjectId;
	/** 主体类型 **/
	private Integer subjectType;
	/** 仓管操作;0 否 1 是 **/
	private Integer operater;
	/** 分配人 */
	private Integer assignerId;
	/** 分配时间 */
	private Date assignAt;
	/** 状态 */
	private Integer state;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(Integer subjectType) {
		this.subjectType = subjectType;
	}

	public Integer getOperater() {
		return operater;
	}

	public void setOperater(Integer operater) {
		this.operater = operater;
	}

	public Integer getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(Integer assignerId) {
		this.assignerId = assignerId;
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

}
