package com.scfs.domain.report.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: FundCostBillDtlSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年4月7日				Administrator
 *
 * </pre>
 */
public class FundCostBillSearchReqDto extends BaseReqDto {
	private static final long serialVersionUID = 9010601382718288758L;

	private String departmentId;
	private Integer projectId;
	private Integer accountId;
	private Integer currencyType;
	private List<String> departmentList;
	private Integer searchType; // 1:查询付款金额来源
								// 2：查询收款金额来源(应收保理业务付款单预计内部打款日期inner_pay_date当天产生一条虚拟的收款)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date billDate;
	/** 业务员ID */
	private Integer bizManagerId;
	/** 经营单位 */
	private Integer businessUnitId;
	private Integer capitalAccountType;
	/** 统计维度 */
	private Integer statisticsDimension;

	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public List<String> getDepartmentList() {
		return departmentList;
	}

	public void setDepartmentList(List<String> departmentList) {
		this.departmentList = departmentList;
	}

	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getCapitalAccountType() {
		return capitalAccountType;
	}

	public void setCapitalAccountType(Integer capitalAccountType) {
		this.capitalAccountType = capitalAccountType;
	}

	public Integer getStatisticsDimension() {
		return statisticsDimension;
	}

	public void setStatisticsDimension(Integer statisticsDimension) {
		this.statisticsDimension = statisticsDimension;
	}
}
