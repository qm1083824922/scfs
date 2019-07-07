package com.scfs.rpc.cms.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CmsHttpResDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 558583917533614937L;

	/**
	 * 返回标记:Y/N
	 */
	private String ack;
	/**
	 * 返回结果
	 */
	private String msg;

	public String getAck() {
		return ack;
	}

	public void setAck(String ack) {
		this.ack = ack;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
