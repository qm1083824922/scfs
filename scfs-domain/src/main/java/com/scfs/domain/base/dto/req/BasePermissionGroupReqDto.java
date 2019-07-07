package com.scfs.domain.base.dto.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016/9/26.
 */
public class BasePermissionGroupReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 角色ID */
	private Integer roleId;
	private List<Integer> ids;
	/** 权限组名称 */
	private String name;
	/** 状态 */
	private Integer state;
	/** 权限组ID */
	private Integer permissionGroupId;
	/** 权限ID集合 */
	private List<Integer> permissionIds;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public List<Integer> getIds() {
		return ids;
	}

	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getPermissionGroupId() {
		return permissionGroupId;
	}

	public void setPermissionGroupId(Integer permissionGroupId) {
		this.permissionGroupId = permissionGroupId;
	}

	public List<Integer> getPermissionIds() {
		return permissionIds;
	}

	public void setPermissionIds(List<Integer> permissionIds) {
		this.permissionIds = permissionIds;
	}
}
