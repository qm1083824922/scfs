package com.scfs.domain.wechat.domain;

import java.util.Map;

/**
 * Created by Administrator on 2016/12/5.
 */
public class BaseMessage {
	// 开发者微信号
	private String ToUserName;
	// 发送方帐号（一个 OpenID）
	private String FromUserName;
	// 消息创建时间 （整型）
	private String CreateTime;
	// 消息类型（text/image/location/link/video/shortvideo）
	private String MsgType;
	// 消息 id，64 位整型
	private String MsgId;

	public BaseMessage() {

	}

	public BaseMessage(Map<String, String> map) {
		this.setFromUserName(map.get("ToUserName"));
		this.setToUserName(map.get("FromUserName"));
		this.setCreateTime(map.get("CreateTime"));
	}

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}
