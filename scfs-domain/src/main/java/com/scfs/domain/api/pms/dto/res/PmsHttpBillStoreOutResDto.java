package com.scfs.domain.api.pms.dto.res;

import java.io.Serializable;

/**
 * <pre>
 * 
 *  File: PmsHttpResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月06日			Administrator
 *
 * </pre>
 */
public class PmsHttpBillStoreOutResDto implements Serializable {
	private static final long serialVersionUID = -4360980201204208987L;
	private String flag; // 0: 成功 -1: 失败
	private String msg;
	private Integer sku_id;

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

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
}
