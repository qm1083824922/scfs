package com.scfs.rpc.pms.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Administrator on 2016年12月19日.
 */
public class SupplierPmsHttpResDto extends PmsHttpResDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7083670402686756069L;

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

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
