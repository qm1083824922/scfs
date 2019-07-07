package com.scfs.domain.logistics.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年11月14日.
 */
public class BillOutStorePickDtlSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 749918388588608975L;
	private Integer billOutStoreId;
	private Integer billOutStoreDtlId;

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public Integer getBillOutStoreDtlId() {
		return billOutStoreDtlId;
	}

	public void setBillOutStoreDtlId(Integer billOutStoreDtlId) {
		this.billOutStoreDtlId = billOutStoreDtlId;
	}

}
