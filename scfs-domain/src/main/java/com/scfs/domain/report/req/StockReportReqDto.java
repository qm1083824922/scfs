package com.scfs.domain.report.req;

import java.math.BigDecimal;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: StockReportReqDto.java
 *  Description: 
 *  TODO
 *  Date,					Who,				
 *  2017年2月16日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class StockReportReqDto extends BaseReqDto {
	/** id **/
	private Integer id;
	/** 项目 **/
	private Integer projectId;
	/** 经营单位 **/
	private Integer businessUnitId;
	/** 客户 **/
	private Integer customerId;
	/** 仓库 **/
	private Integer warehouseId;
	/** 统计维度 **/
	private Integer statisticsDimensionType;
	/** 业务员 **/
	private Integer bizManagerId;
	/** 部门 **/
	private Integer departmentId;
	/** 币种 **/
	private Integer currencyType;
	/** 合计总库存 **/
	private BigDecimal sumStore;
	/** 商品编号 **/
	private String goodsNumber;

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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getStatisticsDimensionType() {
		return statisticsDimensionType;
	}

	public void setStatisticsDimensionType(Integer statisticsDimensionType) {
		this.statisticsDimensionType = statisticsDimensionType;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
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

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
}
