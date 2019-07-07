package com.scfs.domain.report.entity;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: StockReport.java
 *  Description: 库存报表
 *  TODO
 *  Date,					Who,				
 *  2017年2月16日				Administrator
 *
 * </pre>
 */
public class StockReport {
	private Integer id;
	/** 项目id **/
	private Integer projectId;
	/** 经营单位id **/
	private Integer businessUnitId;
	/** 商品id **/
	private Integer goodsId;
	/** 供应商id **/
	private Integer supplierId;
	/** 客户id **/
	private Integer customerId;
	/** 部门id **/
	private Integer departmentId;
	/** 业务员 **/
	private Integer bizManagerId;
	/** 仓库ID **/
	private Integer warehouseId;
	/** 币种 **/
	private Integer currencyType;
	/** 总库存 **/
	private BigDecimal sumStore;
	/** 锁定库存 **/
	private BigDecimal sumLock;
	/** 未锁定库存 **/
	private BigDecimal sumNotLock;
	/** 总数量 **/
	private BigDecimal sumNum;
	/** 锁定数量 **/
	private BigDecimal sumLockNum;
	/** 未锁定数量 **/
	private BigDecimal sumNotLockNum;
	/** 临期1-7天 **/
	private BigDecimal adventAmount;
	/** 超期库存 **/
	private BigDecimal expireAmount;
	/** 超期1-7天 **/
	private BigDecimal expireAmount1;
	/** 超期8-15天 **/
	private BigDecimal expireAmount2;
	/** 超期超过16天及以上 **/
	private BigDecimal expireAmount3;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getSumStore() {
		return sumStore;
	}

	public void setSumStore(BigDecimal sumStore) {
		this.sumStore = sumStore;
	}

	public BigDecimal getSumLock() {
		return sumLock;
	}

	public void setSumLock(BigDecimal sumLock) {
		this.sumLock = sumLock;
	}

	public BigDecimal getSumNotLock() {
		return sumNotLock;
	}

	public void setSumNotLock(BigDecimal sumNotLock) {
		this.sumNotLock = sumNotLock;
	}

	public BigDecimal getSumNum() {
		return sumNum;
	}

	public void setSumNum(BigDecimal sumNum) {
		this.sumNum = sumNum;
	}

	public BigDecimal getSumLockNum() {
		return sumLockNum;
	}

	public void setSumLockNum(BigDecimal sumLockNum) {
		this.sumLockNum = sumLockNum;
	}

	public BigDecimal getSumNotLockNum() {
		return sumNotLockNum;
	}

	public void setSumNotLockNum(BigDecimal sumNotLockNum) {
		this.sumNotLockNum = sumNotLockNum;
	}

	public BigDecimal getAdventAmount() {
		return adventAmount;
	}

	public void setAdventAmount(BigDecimal adventAmount) {
		this.adventAmount = adventAmount;
	}

	public BigDecimal getExpireAmount() {
		return expireAmount;
	}

	public void setExpireAmount(BigDecimal expireAmount) {
		this.expireAmount = expireAmount;
	}

	public BigDecimal getExpireAmount1() {
		return expireAmount1;
	}

	public void setExpireAmount1(BigDecimal expireAmount1) {
		this.expireAmount1 = expireAmount1;
	}

	public BigDecimal getExpireAmount2() {
		return expireAmount2;
	}

	public void setExpireAmount2(BigDecimal expireAmount2) {
		this.expireAmount2 = expireAmount2;
	}

	public BigDecimal getExpireAmount3() {
		return expireAmount3;
	}

	public void setExpireAmount3(BigDecimal expireAmount3) {
		this.expireAmount3 = expireAmount3;
	}

}
