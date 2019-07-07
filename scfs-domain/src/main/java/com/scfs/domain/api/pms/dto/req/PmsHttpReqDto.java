package com.scfs.domain.api.pms.dto.req;

import java.io.Serializable;

public class PmsHttpReqDto implements Serializable {
	private static final long serialVersionUID = -5470279107254622517L;

	private String key; // 签名
	private String data; // json数据

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
