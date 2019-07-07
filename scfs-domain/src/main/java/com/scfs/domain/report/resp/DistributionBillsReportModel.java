package com.scfs.domain.report.resp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: DistributionBillsReportModel.java
 *  Description: 铺货对账单(打印)
 *  TODO
 *  Date,					Who,				
 *  2017年09月13日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class DistributionBillsReportModel extends BaseEntity {
	/** 项目 **/
	private String projectName;
	/** 经营单位 **/
	private String businessUnitName;
	private String supplierChineseName;
	/** 供应商 **/
	private String supplierName;
	/** 币种 **/
	private String currencyName;
	/** 结算开始日期 **/
	@JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
	private Date oldPayCreateTime;
	/** 结算结束周期 **/
	@JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
	private Date payCreateTime;

	List<DistributionBillsReportResDto> resDtoList = new ArrayList<DistributionBillsReportResDto>();

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public String getSupplierChineseName() {
		return supplierChineseName;
	}

	public void setSupplierChineseName(String supplierChineseName) {
		this.supplierChineseName = supplierChineseName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public Date getOldPayCreateTime() {
		return oldPayCreateTime;
	}

	public void setOldPayCreateTime(Date oldPayCreateTime) {
		this.oldPayCreateTime = oldPayCreateTime;
	}

	public Date getPayCreateTime() {
		return payCreateTime;
	}

	public void setPayCreateTime(Date payCreateTime) {
		this.payCreateTime = payCreateTime;
	}

	public List<DistributionBillsReportResDto> getResDtoList() {
		return resDtoList;
	}

	public void setResDtoList(List<DistributionBillsReportResDto> resDtoList) {
		this.resDtoList = resDtoList;
	}

}
