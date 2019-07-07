package com.scfs.domain.po.dto.req;

import java.io.Serializable;

public class AutoProcessPoDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3750865614669934904L;
	/**
	 * 入库单号
	 */
	private String billInStoreNo;
	public String getBillInStoreNo() {
		return billInStoreNo;
	}
	public void setBillInStoreNo(String billInStoreNo) {
		this.billInStoreNo = billInStoreNo;
	}
}
