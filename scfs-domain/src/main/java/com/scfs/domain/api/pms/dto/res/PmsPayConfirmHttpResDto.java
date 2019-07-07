package com.scfs.domain.api.pms.dto.res;

/**
 * Created by Administrator on 2017年5月6日.
 */
public class PmsPayConfirmHttpResDto extends BasePmsHttpResDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1074577542768489365L;

	private String pay_sn;

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

}
