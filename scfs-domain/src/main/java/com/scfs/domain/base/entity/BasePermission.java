package com.scfs.domain.base.entity;

/**
 * Created by Administrator on 2016/9/23.
 */
public class BasePermission extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	/** 菜单顺序 */
	private Integer ord;

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

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

}
