package com.scfs.domain.logistics.dto.req;

import java.util.List;

import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.Stl;

/**
 * Created by Administrator on 2016年11月4日.
 */
public class BillOutStoreReqDto extends BillOutStore {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1981813146554318418L;
	/**
	 * 出库单明细
	 */
	private List<BillOutStoreDtl> billOutStoreDtlList;
	/**
	 * 库存明细
	 */
	private List<Stl> stlList;

	public List<BillOutStoreDtl> getBillOutStoreDtlList() {
		return billOutStoreDtlList;
	}

	public void setBillOutStoreDtlList(List<BillOutStoreDtl> billOutStoreDtlList) {
		this.billOutStoreDtlList = billOutStoreDtlList;
	}

	public List<Stl> getStlList() {
		return stlList;
	}

	public void setStlList(List<Stl> stlList) {
		this.stlList = stlList;
	}

}
