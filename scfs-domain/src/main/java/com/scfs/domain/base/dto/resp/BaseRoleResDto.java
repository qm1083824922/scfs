package com.scfs.domain.base.dto.resp;

import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/27.
 */
public class BaseRoleResDto {
	/** 角色ID */
	private int id;
	/** 角色名称 */
	private String name;
	/** 状态 */
	private Integer state;

	public Integer getState() {
		return state;
	}

	/** 状态 */
	private String stateName;

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private String createDate;
	/** 作废人 */
	private String deleter;
	/** 作废时间 */
	private String deleteDate;
	/** 锁定人 */
	private String locker;
	/** 锁定时间 */
	private String lockAt;

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	public String getLockAt() {
		return lockAt;
	}

	public void setLockAt(String lockAt) {
		this.lockAt = lockAt;
	}

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BaseUrlConsts.EDITROLE);
			operMap.put(OperateConsts.DETAIL, BaseUrlConsts.DETAILROLE);
			operMap.put(OperateConsts.LOCK, BaseUrlConsts.LOCKROLE);
			operMap.put(OperateConsts.UNLOCK, BaseUrlConsts.UNLOCKROLE);
			operMap.put(OperateConsts.DIVIDE, BaseUrlConsts.ADDUSERROLE);
		}

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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getDeleter() {
		return deleter;
	}

	public void setDeleter(String deleter) {
		this.deleter = deleter;
	}

	public String getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(String deleteDate) {
		this.deleteDate = deleteDate;
	}
}
