package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年3月18日.
 */
public class OrderProfitReportReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6242491866623223670L;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 业务员ID
	 */
	private Integer bizManagerId;
	/**
	 * 部门ID
	 */
	private List<Integer> departmentId;
	/**
	 * 开始下单时间
	 */
	private String startPlaceDate;
	/**
	 * 结束下单时间
	 */
	private String endPlaceDate;
	/**
	 * 开始统计时间
	 */
	private String startStatisticsDate;
	/**
	 * 结束统计时间
	 */
	private String endStatisticsDate;
	/**
	 * 业务类型
	 */
	private Integer bizType;
	/**
	 * 单据类型
	 */
	private Integer billType;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 币种
	 */
	private Integer businessUnitId;
	/**
	 * 查询所有管理费用
	 */
	private Integer isQueryAllFeeManage;
	/**
	 * 管理费用部门
	 */
	private List<Integer> manageFeeDepartmentId;
	/**
	 * 开始创建时间
	 */
	private String startCreateAt;
	/**
	 * 结束创建时间
	 */
	private String endCreateAt;
	/**
	 * 开始系统出库时间
	 */
	private String startSystemDeliverTime;
	/**
	 * 结束系统出库时间
	 */
	private String endSystemDeliverTime;
	/**
	 * 0-非调度 1-调度
	 */
	private Integer isSchedule = 0;
	/**
	 * 查询资金成本报表 0-不查询 1-查询
	 */
	private Integer searchProfitReport = 1;
	/**
	 * 开始管理费用创建时间
	 */
	private String startFeeManageCreateAt;
	/**
	 * 结束管理费用创建时间
	 */
	private String endFeeManageCreateAt;

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBizManagerId() {
		return bizManagerId;
	}

	public void setBizManagerId(Integer bizManagerId) {
		this.bizManagerId = bizManagerId;
	}

	public List<Integer> getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(List<Integer> departmentId) {
		this.departmentId = departmentId;
	}

	public String getStartPlaceDate() {
		return startPlaceDate;
	}

	public void setStartPlaceDate(String startPlaceDate) {
		this.startPlaceDate = startPlaceDate;
	}

	public String getEndPlaceDate() {
		return endPlaceDate;
	}

	public void setEndPlaceDate(String endPlaceDate) {
		this.endPlaceDate = endPlaceDate;
	}

	public String getStartStatisticsDate() {
		return startStatisticsDate;
	}

	public void setStartStatisticsDate(String startStatisticsDate) {
		this.startStatisticsDate = startStatisticsDate;
	}

	public String getEndStatisticsDate() {
		return endStatisticsDate;
	}

	public void setEndStatisticsDate(String endStatisticsDate) {
		this.endStatisticsDate = endStatisticsDate;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public Integer getIsQueryAllFeeManage() {
		return isQueryAllFeeManage;
	}

	public void setIsQueryAllFeeManage(Integer isQueryAllFeeManage) {
		this.isQueryAllFeeManage = isQueryAllFeeManage;
	}

	public List<Integer> getManageFeeDepartmentId() {
		return manageFeeDepartmentId;
	}

	public void setManageFeeDepartmentId(List<Integer> manageFeeDepartmentId) {
		this.manageFeeDepartmentId = manageFeeDepartmentId;
	}

	public String getStartCreateAt() {
		return startCreateAt;
	}

	public void setStartCreateAt(String startCreateAt) {
		this.startCreateAt = startCreateAt;
	}

	public String getEndCreateAt() {
		return endCreateAt;
	}

	public void setEndCreateAt(String endCreateAt) {
		this.endCreateAt = endCreateAt;
	}

	public String getStartSystemDeliverTime() {
		return startSystemDeliverTime;
	}

	public void setStartSystemDeliverTime(String startSystemDeliverTime) {
		this.startSystemDeliverTime = startSystemDeliverTime;
	}

	public String getEndSystemDeliverTime() {
		return endSystemDeliverTime;
	}

	public void setEndSystemDeliverTime(String endSystemDeliverTime) {
		this.endSystemDeliverTime = endSystemDeliverTime;
	}

	public Integer getIsSchedule() {
		return isSchedule;
	}

	public void setIsSchedule(Integer isSchedule) {
		this.isSchedule = isSchedule;
	}

	public Integer getSearchProfitReport() {
		return searchProfitReport;
	}

	public void setSearchProfitReport(Integer searchProfitReport) {
		this.searchProfitReport = searchProfitReport;
	}

	public String getStartFeeManageCreateAt() {
		return startFeeManageCreateAt;
	}

	public void setStartFeeManageCreateAt(String startFeeManageCreateAt) {
		this.startFeeManageCreateAt = startFeeManageCreateAt;
	}

	public String getEndFeeManageCreateAt() {
		return endFeeManageCreateAt;
	}

	public void setEndFeeManageCreateAt(String endFeeManageCreateAt) {
		this.endFeeManageCreateAt = endFeeManageCreateAt;
	}

}
