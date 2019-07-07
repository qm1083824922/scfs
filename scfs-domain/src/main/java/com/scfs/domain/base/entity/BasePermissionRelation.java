package com.scfs.domain.base.entity;

/**
 * Created by Administrator on 2016/9/23. 权限组与权限关联
 */
public class BasePermissionRelation extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 权限组ID */
	private Integer permissionGroupId;
	/** 权限ID */
	private Integer permissionId;

	public Integer getPermissionGroupId() {
		return permissionGroupId;
	}

	public void setPermissionGroupId(Integer permissionGroupId) {
		this.permissionGroupId = permissionGroupId;
	}

	public Integer getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}
}
