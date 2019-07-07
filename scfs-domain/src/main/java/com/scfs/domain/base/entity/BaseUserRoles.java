package com.scfs.domain.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrator on 2016/9/23. 用户角色关联
 */
@JsonIgnoreProperties(value = { "handler" })
public class BaseUserRoles extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 关联用户ID */
	private Integer userId;

	/** 关联角色ID */
	private Integer roleId;

	/** 状态 */
	private Integer status;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
