package com.scfs.domain.api.cms.dto.req;

import java.io.Serializable;

public class CmsHttpReqDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1737780112864772450L;
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
