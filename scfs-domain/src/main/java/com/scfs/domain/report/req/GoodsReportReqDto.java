package com.scfs.domain.report.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: GoodsReportReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
  *  2017年08月31日				Administrator
 *
 * </pre>
 */
public class GoodsReportReqDto extends BaseReqDto {

	private static final long serialVersionUID = -7280695955225755977L;

	/** 项目ID **/
	private Integer projectId;
	/** 客户ID **/
	private Integer customerId;
	private String startCheckDate;
	private String endCheckDate;
	/** 供应商ID **/
	private Integer supplierId;
	/** 商品编号 **/
	private String goodsCode;
	/**
	 * 查询的方式的条件
	 */
	private Integer statisticsDimension;

	private List<String> goodsCodeList;
	private List<Integer> supplierIdList;
	private List<Integer> customerIdList;
	private List<Integer> projectIdList;
	/** 采购单号 **/
	private String orderNo;
	/** 是否超期 **/
	private Integer needOver;

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

	public String getStartCheckDate() {
		return startCheckDate;
	}

	public void setStartCheckDate(String startCheckDate) {
		this.startCheckDate = startCheckDate;
	}

	public String getEndCheckDate() {
		return endCheckDate;
	}

	public void setEndCheckDate(String endCheckDate) {
		this.endCheckDate = endCheckDate;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public Integer getStatisticsDimension() {
		return statisticsDimension;
	}

	public void setStatisticsDimension(Integer statisticsDimension) {
		this.statisticsDimension = statisticsDimension;
	}

	public List<String> getGoodsCodeList() {
		return goodsCodeList;
	}

	public void setGoodsCodeList(List<String> goodsCodeList) {
		this.goodsCodeList = goodsCodeList;
	}

	public List<Integer> getSupplierIdList() {
		return supplierIdList;
	}

	public void setSupplierIdList(List<Integer> supplierIdList) {
		this.supplierIdList = supplierIdList;
	}

	public List<Integer> getCustomerIdList() {
		return customerIdList;
	}

	public void setCustomerIdList(List<Integer> customerIdList) {
		this.customerIdList = customerIdList;
	}

	public List<Integer> getProjectIdList() {
		return projectIdList;
	}

	public void setProjectIdList(List<Integer> projectIdList) {
		this.projectIdList = projectIdList;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getNeedOver() {
		return needOver;
	}

	public void setNeedOver(Integer needOver) {
		this.needOver = needOver;
	}

}
