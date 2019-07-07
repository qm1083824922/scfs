package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016/10/27.
 */
public class BaseUserRoleReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private Integer userId;

	/**
	 * 角色id
	 */
	private Integer roleId;
	/**
	 * 角色名称
	 */
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

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

}
