package com.scfs.rpc.pms.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016年12月19日.
 */
public class Supplier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2159139723914908079L;
	/**
	 * 供应商编码
	 */
	private String provider_sn;

	public String getProvider_sn() {
		return provider_sn;
	}

	public void setProvider_sn(String provider_sn) {
		this.provider_sn = provider_sn;
	}

}
