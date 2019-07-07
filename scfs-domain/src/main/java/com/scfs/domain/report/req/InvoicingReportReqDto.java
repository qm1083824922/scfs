package com.scfs.domain.report.req;

import com.scfs.domain.BaseReqDto;

public class InvoicingReportReqDto extends BaseReqDto {

	private static final long serialVersionUID = -6957637942279479629L;
	/** 经营单位 */
	private Integer busiUnit;
	/** 项目ID */
	private Integer projectId;
	/** 商品ID */
	private Integer goodsId;
	/** 部门名称 */
	private Integer departmentId;
	/** 业务员 */
	private Integer bussinessUserId;
	/** 币种 */
	private Integer currencyType;
	/** 仓库 */
	private Integer wareHouseId;
	/** 统计维度 */
	private Integer statisticsDimensionType;
	/** 日期开始时间 */
	private String startBusinessDate;
	/** 日期结束时间 */
	private String endBusinessDate;
	/** 客户ID */
	private Integer customerId;

	/** 第几页 */
	private int first = 0;

	/** 每页显示数目 */
	private int last = 15;

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getBussinessUserId() {
		return bussinessUserId;
	}

	public void setBussinessUserId(Integer bussinessUserId) {
		this.bussinessUserId = bussinessUserId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Integer wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public Integer getStatisticsDimensionType() {
		return statisticsDimensionType;
	}

	public void setStatisticsDimensionType(Integer statisticsDimensionType) {
		this.statisticsDimensionType = statisticsDimensionType;
	}

	public String getStartBusinessDate() {
		return startBusinessDate;
	}

	public void setStartBusinessDate(String startBusinessDate) {
		this.startBusinessDate = startBusinessDate;
	}

	public String getEndBusinessDate() {
		return endBusinessDate;
	}

	public void setEndBusinessDate(String endBusinessDate) {
		this.endBusinessDate = endBusinessDate;
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getLast() {
		return last;
	}

	public void setLast(int last) {
		this.last = last;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
