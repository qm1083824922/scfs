package com.scfs.domain.api.pms.dto.res;

/**
 * Created by Administrator on 2017年6月22日.
 */
public class PmsReturnHttpResDto extends BasePmsHttpResDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String refund_order_sn;

	public String getRefund_order_sn() {
		return refund_order_sn;
	}

	public void setRefund_order_sn(String refund_order_sn) {
		this.refund_order_sn = refund_order_sn;
	}

}
