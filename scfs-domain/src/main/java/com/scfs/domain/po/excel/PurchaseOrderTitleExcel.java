package com.scfs.domain.po.excel;

import java.util.Date;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * 
 *  File: PurchaseOrderTitleExcel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月29日			Administrator
 *
 * </pre>
 */
public class PurchaseOrderTitleExcel {
	/** 附属编号 */
	private String appendNo;
	/** 项目编号 */
	private Integer projectId;
	/** 供应商编号 */
	private Integer supplierId;
	/** 仓库编号 */
	private Integer warehouseId;
	/** 客户编号 */
	private Integer customerId;
	private Integer currencyId;
	/** 订单日期 */
	private Date orderTime;
	/** 预计到货日期 */
	private Date perdictTime;
	/** 备注 */
	private String remark;

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getPerdictTime() {
		return perdictTime;
	}

	public void setPerdictTime(Date perdictTime) {
		this.perdictTime = perdictTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public int hashCode() {

		int result = 17;
		return result * 31 + appendNo.hashCode() + projectId.hashCode() + supplierId.hashCode() + warehouseId.hashCode()
				+ (StringUtils.isEmpty(customerId) ? 0 : customerId.hashCode()) + currencyId.hashCode()
				+ orderTime.hashCode() + perdictTime.hashCode() + (StringUtils.isEmpty(remark) ? 0 : remark.hashCode());
	}

	@Override
	public String toString() {
		return "appendNo=" + appendNo + "&projectId=" + projectId + "&supplierId=" + supplierId + "&warehouseId"
				+ warehouseId + "&customerId=" + customerId + "&currencyId=" + currencyId + "&orderTime=" + orderTime
				+ "&perdictTime=" + perdictTime + "&remark=" + remark;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != getClass())
			return false;
		PurchaseOrderTitleExcel potExcel = (PurchaseOrderTitleExcel) obj;
		boolean result = (appendNo.equals(potExcel.appendNo) && projectId.equals(potExcel.projectId)
				&& supplierId.equals(potExcel.supplierId) && warehouseId.equals(potExcel.warehouseId)
				&& customerId.equals(potExcel.customerId) && currencyId.equals(potExcel.currencyId)
				&& orderTime.equals(potExcel.orderTime) && perdictTime.equals(potExcel.perdictTime)
				&& remark.equals(potExcel.remark));
		return result;
	}

}
