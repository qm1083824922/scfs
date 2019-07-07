package com.scfs.domain.base.entity;

/**
 * Created by Administrator on 2016/9/23. 用户角色和权限组关联
 */
public class BaseRolePermissionGroup extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 角色ID */
	private Integer roleId;
	/** 权限组ID */
	private Integer permissionGroupId;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPermissionGroupId() {
		return permissionGroupId;
	}

	public void setPermissionGroupId(Integer permissionGroupId) {
		this.permissionGroupId = permissionGroupId;
	}
}
