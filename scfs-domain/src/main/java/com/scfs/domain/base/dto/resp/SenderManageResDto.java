package com.scfs.domain.base.dto.resp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;
import com.google.common.collect.Maps;

/**
 * <pre>
 *  消息推送信息
 *  File: senderManageResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月22日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class SenderManageResDto extends BaseEntity {

	/** 发送业务类型： 0 账期提醒 **/
	public Integer bizSendType;
	public String bizSendTypeName;

	/** 用户信息 **/
	public Integer userId;
	private String userName;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date creatorAt;

	/** 作废时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deleteAt;

	/** 状态 **/
	public Integer status;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_SEND_MANAGE);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_SEND_MANAGE);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_SEND_MANAGE);
		}
	}

	public Integer getBizSendType() {
		return bizSendType;
	}

	public void setBizSendType(Integer bizSendType) {
		this.bizSendType = bizSendType;
	}

	public String getBizSendTypeName() {
		return bizSendTypeName;
	}

	public void setBizSendTypeName(String bizSendTypeName) {
		this.bizSendTypeName = bizSendTypeName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getCreatorAt() {
		return creatorAt;
	}

	public void setCreatorAt(Date creatorAt) {
		this.creatorAt = creatorAt;
	}

	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
