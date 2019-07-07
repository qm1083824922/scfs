package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016/9/26.
 */
public class BasePermissionReqDto extends BaseReqDto {

	private static final long serialVersionUID = -3574286394169656122L;
	/** 权限ID */
	private Integer id;
	/** 权限组ID */
	private Integer permissionGroupId;
	/** 权限名称 */
	private String name;
	/** 权限类型 */
	private Integer type;
	/** 父级权限 */
	private Integer parentId;
	/** 权限URL */
	private String url;
	/** 权限状态 */
	private Integer state;
	/** 菜单级别 */
	private Integer menuLevel;
	/** 角色id */
	private Integer roleId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPermissionGroupId() {
		return permissionGroupId;
	}

	public void setPermissionGroupId(Integer permissionGroupId) {
		this.permissionGroupId = permissionGroupId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

}
