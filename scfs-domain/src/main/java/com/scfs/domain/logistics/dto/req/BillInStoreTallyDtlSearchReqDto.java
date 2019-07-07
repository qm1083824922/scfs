package com.scfs.domain.logistics.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年10月18日.
 */
public class BillInStoreTallyDtlSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7700431024910621175L;

	private Integer billInStoreId;

	private Integer billInStoreDtlId;

	public Integer getBillInStoreId() {
		return billInStoreId;
	}

	public void setBillInStoreId(Integer billInStoreId) {
		this.billInStoreId = billInStoreId;
	}

	public Integer getBillInStoreDtlId() {
		return billInStoreDtlId;
	}

	public void setBillInStoreDtlId(Integer billInStoreDtlId) {
		this.billInStoreDtlId = billInStoreDtlId;
	}

}
