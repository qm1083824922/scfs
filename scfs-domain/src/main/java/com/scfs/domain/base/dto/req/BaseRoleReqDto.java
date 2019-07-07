package com.scfs.domain.base.dto.req;

import com.scfs.domain.BaseReqDto;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class BaseRoleReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 角色名称 */
	private String name;
	/** 状态 */
	private Integer state;
	/** 角色ID */
	private Integer roleId;
	/** 权限组ID列表或权限组与角色关联表ID列表 */
	private List<Integer> ids;

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
}
