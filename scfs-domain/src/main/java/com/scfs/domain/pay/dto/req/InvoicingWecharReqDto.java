package com.scfs.domain.pay.dto.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 *  PMS出库明细
 *  File: PmsStoreOutReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年09月20日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class InvoicingWecharReqDto extends BaseReqDto {
	/** 供应商id **/
	private Integer supplierId;
	/** 类型 **/
	private Integer type;
	/** 商品sku **/
	private String sku;
	/** 开始日期 **/
	private String startDate;
	/** 结束日期 **/
	private String endDate;
	private String sendTime;

	private List<Integer> supplierIds;

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public List<Integer> getSupplierIds() {
		return supplierIds;
	}

	public void setSupplierIds(List<Integer> supplierIds) {
		this.supplierIds = supplierIds;
	}

}
