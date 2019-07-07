package com.scfs.rpc.pms.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * PmsSyncReturnPurchaseResDto 退货订单确认回传返回数据
 * 
 * @author  2017年06月23号
 *
 */
public class PmsSyncReturnPurchaseResDto extends PmsHttpResDto {

	private static final long serialVersionUID = 7244131347592277926L;

	/**
	 * 退货单号
	 */
	private String refund_order_sn;

	public String getRefund_order_sn() {
		return refund_order_sn;
	}

	public void setRefund_order_sn(String refund_order_sn) {
		this.refund_order_sn = refund_order_sn;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
