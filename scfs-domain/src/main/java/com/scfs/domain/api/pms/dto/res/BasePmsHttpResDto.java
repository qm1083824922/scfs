package com.scfs.domain.api.pms.dto.res;

import java.io.Serializable;

import com.scfs.common.consts.BaseConsts;

public class BasePmsHttpResDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3152627371582613954L;

	private String flag; // 0: 成功 -1: 失败
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
		this.flag = BaseConsts.FLAG_NO;
		this.msg = msg;
	}

}
