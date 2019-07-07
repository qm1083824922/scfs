package com.scfs.domain.api.pms.dto.res;

import java.io.Serializable;

/**
 * <pre>
 * 
 *  File: PmsSyncPurchaseSendResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月23日			Administrator
 *
 * </pre>
 */
public class PmsSyncPurchaseSendResDto implements Serializable {

	private static final long serialVersionUID = -7225776477281286194L;

	private String flag; // Y: 成功 -N: 失败
	private String msg;
	/** 退货ID **/
	private Integer refund_order_id;

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
}
