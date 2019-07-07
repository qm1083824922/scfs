package com.scfs.domain.api.pms.dto.res;

/**
 * Created by Administrator on 2017年5月22日.
 */
public class PmsPayOrderConfirmHttpResDto extends BasePmsHttpResDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2757577202824224927L;
	private String pay_no;

	public String getPay_no() {
		return pay_no;
	}

	public void setPay_no(String pay_no) {
		this.pay_no = pay_no;
	}

}
