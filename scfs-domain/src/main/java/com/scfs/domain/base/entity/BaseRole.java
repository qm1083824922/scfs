package com.scfs.domain.base.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by Administrator on 2016/9/23.
 */
public class BaseRole extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 角色名称 */
	private String name;
	/** 状态 */
	private Integer state;

	/** 锁定人 */
	private String locker;
	/** 锁定时间 */
	private Date lockAt;

	public String getLocker() {
		return locker;
	}

	public void setLocker(String locker) {
		this.locker = locker;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getLockAt() {
		return lockAt;
	}

	public void setLockAt(Date lockAt) {
		this.lockAt = lockAt;
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
