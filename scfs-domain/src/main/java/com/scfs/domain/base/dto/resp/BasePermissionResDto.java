package com.scfs.domain.base.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/26.
 */
public class BasePermissionResDto {
	/** 权限ID */
	private int id;
	/** 权限名称 */
	private String name;
	/** 权限类型 */
	private Integer type;
	/** 权限类型 */
	private String typeName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	/** 父级权限 */
	private Integer parentId;
	/** 父级名称 */
	private String parentName;
	/*** 排序 */
	private Integer ord;

	/** 权限URL */
	private String url;
	/** 权限状态 code name */
	private String state;

	/** 菜单级别 */
	private Integer menuLevel;
	private String menuLevelName;
	/** 是否可用 */
	private Integer isDelete;

	/** 操作集合 */
	private List<CodeValue> opertaList;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BaseUrlConsts.EDITPERMISSION);
			operMap.put(OperateConsts.DELETE, BaseUrlConsts.DELETEPERMISSION);
			operMap.put(OperateConsts.DETAIL, BaseUrlConsts.DETAILPERMISSION);
			operMap.put(OperateConsts.SUBMIT, BaseUrlConsts.SUBMITPERMISSION);
			operMap.put(OperateConsts.LOCK, BaseUrlConsts.LOCKPERMISSION);
			operMap.put(OperateConsts.UNLOCK, BaseUrlConsts.UNLOCKPERMISSION);
		}

	}

	public Integer getOrd() {
		return ord;
	}

	public void setOrd(Integer ord) {
		this.ord = ord;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	public String getMenuLevelName() {
		return menuLevelName;
	}

	public void setMenuLevelName(String menuLevelName) {
		this.menuLevelName = menuLevelName;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}
}
