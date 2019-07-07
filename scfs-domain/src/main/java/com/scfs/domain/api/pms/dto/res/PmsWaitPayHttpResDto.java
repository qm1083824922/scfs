package com.scfs.domain.api.pms.dto.res;

/**
 * @author Administrator
 *
 */
public class PmsWaitPayHttpResDto {
	private String pay_sn;

	private String flag; // 0: 成功 -1: 失败
	private String msg;

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
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
