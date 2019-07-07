package com.scfs.rpc.pms.entity;

import java.io.Serializable;

/**
 * 
 * @author  2017年06月23日
 *
 */
public class PmsSyncReturnPurchseReqDto implements Serializable {

	private static final long serialVersionUID = -7867287078413035783L;

	/**
	 * 退货单号
	 */
	private String refund_order_sn;

	/**
	 * 处理状态
	 */
	private Integer audit_state;

	/**
	 * 审核意见
	 */
	private String audit_suggestion;

	public String getRefund_order_sn() {
		return refund_order_sn;
	}

	public void setRefund_order_sn(String refund_order_sn) {
		this.refund_order_sn = refund_order_sn;
	}

	public Integer getAudit_state() {
		return audit_state;
	}

	public void setAudit_state(Integer audit_state) {
		this.audit_state = audit_state;
	}

	public String getAudit_suggestion() {
		return audit_suggestion;
	}

	public void setAudit_suggestion(String audit_suggestion) {
		this.audit_suggestion = audit_suggestion;
	}

}
