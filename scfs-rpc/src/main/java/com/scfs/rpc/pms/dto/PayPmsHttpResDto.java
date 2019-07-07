package com.scfs.rpc.pms.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Created by Administrator on 2016年12月19日.
 */
public class PayPmsHttpResDto extends PmsHttpResDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 665658438528175649L;
	/**
	 * 请款单号
	 */
	private String pay_sn;

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
