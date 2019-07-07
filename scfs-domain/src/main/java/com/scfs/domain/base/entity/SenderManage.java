package com.scfs.domain.base.entity;

/**
 * <pre>
 *  消息推送信息
 *  File: senderManage.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月22日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class SenderManage extends BaseEntity {

	/** 用户信息 **/
	public Integer userId;

	/** 发送业务类型： 0 账期提醒 **/
	public Integer bizSendType;

	/** 状态 **/
	public Integer status;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getBizSendType() {
		return bizSendType;
	}

	public void setBizSendType(Integer bizSendType) {
		this.bizSendType = bizSendType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
