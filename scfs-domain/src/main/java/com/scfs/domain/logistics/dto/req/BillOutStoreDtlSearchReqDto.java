package com.scfs.domain.logistics.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年10月20日.
 */
public class BillOutStoreDtlSearchReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7295384670363966378L;

	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 查询可拣货明细的标识，1-表示发货数量大于已拣货数量的记录
	 */
	private Integer availableFlag;

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public Integer getAvailableFlag() {
		return availableFlag;
	}

	public void setAvailableFlag(Integer availableFlag) {
		this.availableFlag = availableFlag;
	}

}
