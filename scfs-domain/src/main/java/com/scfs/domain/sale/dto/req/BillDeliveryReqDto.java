package com.scfs.domain.sale.dto.req;

import java.util.List;

import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.domain.sale.entity.BillDeliveryDtl;

/**
 * Created by Administrator on 2016年10月28日.
 */
public class BillDeliveryReqDto extends BillDelivery {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7299309096474864398L;
	/**
	 * 销售单明细
	 */
	private List<BillDeliveryDtl> billDeliveryDtlList;
	/**
	 * 库存明细
	 */
	private List<Stl> stlList;
	/**
	 * 出库明细
	 */
	private List<BillOutStoreDetailReqDto> billOutStoreDetailReqDtoList;

	public List<BillDeliveryDtl> getBillDeliveryDtlList() {
		return billDeliveryDtlList;
	}

	public void setBillDeliveryDtlList(List<BillDeliveryDtl> billDeliveryDtlList) {
		this.billDeliveryDtlList = billDeliveryDtlList;
	}

	public List<Stl> getStlList() {
		return stlList;
	}

	public void setStlList(List<Stl> stlList) {
		this.stlList = stlList;
	}

	public List<BillOutStoreDetailReqDto> getBillOutStoreDetailReqDtoList() {
		return billOutStoreDetailReqDtoList;
	}

	public void setBillOutStoreDetailReqDtoList(List<BillOutStoreDetailReqDto> billOutStoreDetailReqDtoList) {
		this.billOutStoreDetailReqDtoList = billOutStoreDetailReqDtoList;
	}

}
