package com.scfs.rpc.pms.dto;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Administrator on 2016年12月19日.
 */
public class PmsHttpResDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 71523833208593767L;

	/**
	 * 返回标记:Y/N
	 */
	private String flag;
	/**
	 * 返回结果
	 */
	private String msg;

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
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
