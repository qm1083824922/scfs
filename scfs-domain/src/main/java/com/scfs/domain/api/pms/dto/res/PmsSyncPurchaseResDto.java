package com.scfs.domain.api.pms.dto.res;

import java.io.Serializable;

public class PmsSyncPurchaseResDto implements Serializable {

	private static final long serialVersionUID = -1704473356861089279L;

	private String flag; // Y: 成功 -N: 失败
	private String msg;
	/** 退货ID **/
	private Integer refund_order_id;
	private String refund_order_sn;

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

	public Integer getRefund_order_id() {
		return refund_order_id;
	}

	public void setRefund_order_id(Integer refund_order_id) {
		this.refund_order_id = refund_order_id;
	}

	public String getRefund_order_sn() {
		return refund_order_sn;
	}

	public void setRefund_order_sn(String refund_order_sn) {
		this.refund_order_sn = refund_order_sn;
	}
}
