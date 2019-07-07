package com.scfs.domain.logistics.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年10月18日.
 */
public class BillInStoreDtlSearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7006388597847287987L;

	/**
	 * 入库单ID
	 */
	private Integer billInStoreId;
	/**
	 * 查询可理货明细的标识，1-表示收货数量大于已理货数量的记录
	 */
	private Integer availableFlag;

	public Integer getBillInStoreId() {
		return billInStoreId;
	}

	public void setBillInStoreId(Integer billInStoreId) {
		this.billInStoreId = billInStoreId;
	}

	public Integer getAvailableFlag() {
		return availableFlag;
	}

	public void setAvailableFlag(Integer availableFlag) {
		this.availableFlag = availableFlag;
	}

}
