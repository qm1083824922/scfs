package com.scfs.domain.base.entity;

/**
 * Created by Administrator on 2016/9/23. 权限组
 */
public class BasePermissionGroup extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 权限组名称 */
	private String name;
	/** 状态 */
	private Integer state;

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
}
