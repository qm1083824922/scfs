package com.scfs.domain.po.excel;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.po.model.PoLineModel;

public class PurchaseOrderDtlExtResDto extends PoLineModel {

	/** 系统编号 */
	private String sysNo;
	/** 附属编号 */
	private String appendNo;
	/** 订单编号 */
	private String orderNo;
	/** 经营单位 */
	private Integer businessUnitId;
	private String businessUnitName;
	/** 项目 */
	private Integer projectId;
	private String projectName;
	private String businessUnitNameValue;
	/** 经营单位地址 */
	private String businessUnitAddress;
	/** 供应商 */
	private Integer supplierId;
	private String supplierName;
	/** 仓库 */
	private Integer warehouseId;
	private String warehouseName;

	/** 仓库地址 */
	private Integer wareAddrId;
	private String wareAddrName;
	/** 客户银行流水 */
	private String cBankWater;
	/** 客户 */
	private Integer customerId;
	private String customerName;
	/** 订单状态 */
	private Integer stateId;
	private String stateName;
	/** 预计到货日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date perdictTime;
	/** 订单日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTime;
	/** 要求付款时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date requestPayTime;

	public String getSysNo() {
		return sysNo;
	}

	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBusinessUnitNameValue() {
		return businessUnitNameValue;
	}

	public void setBusinessUnitNameValue(String businessUnitNameValue) {
		this.businessUnitNameValue = businessUnitNameValue;
	}

	public String getBusinessUnitAddress() {
		return businessUnitAddress;
	}

	public void setBusinessUnitAddress(String businessUnitAddress) {
		this.businessUnitAddress = businessUnitAddress;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Integer getWareAddrId() {
		return wareAddrId;
	}

	public void setWareAddrId(Integer wareAddrId) {
		this.wareAddrId = wareAddrId;
	}

	public String getWareAddrName() {
		return wareAddrName;
	}

	public void setWareAddrName(String wareAddrName) {
		this.wareAddrName = wareAddrName;
	}

	public String getcBankWater() {
		return cBankWater;
	}

	public void setcBankWater(String cBankWater) {
		this.cBankWater = cBankWater;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getStateId() {
		return stateId;
	}

	public void setStateId(Integer stateId) {
		this.stateId = stateId;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public Date getPerdictTime() {
		return perdictTime;
	}

	public void setPerdictTime(Date perdictTime) {
		this.perdictTime = perdictTime;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Date getRequestPayTime() {
		return requestPayTime;
	}

	public void setRequestPayTime(Date requestPayTime) {
		this.requestPayTime = requestPayTime;
	}
}
