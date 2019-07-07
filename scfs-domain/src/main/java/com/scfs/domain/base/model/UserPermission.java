package com.scfs.domain.base.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */
public class UserPermission implements Comparable<UserPermission>, Serializable {

	private static final long serialVersionUID = 4875309904190696980L;
	// 用户ID
	private int userId;
	// 权限ID
	private int permId;
	/**
	 * 权限名称
	 */
	private String name;
	/**
	 * 权限类型
	 */
	private int type;
	/**
	 * 父级权限
	 */
	private int parentId;
	/**
	 * 权限URL
	 */
	private String url;
	/**
	 * 权限状态
	 */
	private int state;
	/**
	 * 菜单级别
	 */
	private int menuLevel;
	// 排序
	private int ord;
	// 二级菜单列表权限
	private List<UserPermission> twoLevelPermissions;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getPermId() {
		return permId;
	}

	public void setPermId(int permId) {
		this.permId = permId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	public List<UserPermission> getTwoLevelPermissions() {
		return twoLevelPermissions;
	}

	public void setTwoLevelPermissions(List<UserPermission> twoLevelPermissions) {
		this.twoLevelPermissions = twoLevelPermissions;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	@Override
	public int compareTo(UserPermission userPermission) {
		if (this.ord < userPermission.getOrd()) {
			return -1;
		}
		if (this.ord > userPermission.getOrd()) {
			return 1;
		}
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		UserPermission that = (UserPermission) o;
		if (permId == that.permId) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = userId;
		result = 31 * result + permId;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + type;
		result = 31 * result + parentId;
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + state;
		result = 31 * result + menuLevel;
		result = 31 * result + ord;
		result = 31 * result + (twoLevelPermissions != null ? twoLevelPermissions.hashCode() : 0);
		return result;
	}
}
