package com.scfs.domain.sale.dto.req;

import java.util.Date;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年10月27日.
 */
public class BillDeliveryDtlSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -325016503836878810L;

	/**
	 * 销售单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 回款时间
	 */
	private Date returnTime;

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}

}
