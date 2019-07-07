package com.scfs.domain.report.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: DistributionBillsReportReqDto.java
 *  Description: 铺货对账单
 *  TODO
 *  Date,					Who,				
 *  2017年09月12日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class DistributionBillsReportReqDto extends BaseReqDto {
	/** 项目 **/
	private Integer projectId;
	/** 供应商 **/
	private Integer supplierId;
	/** 经营单位 **/
	private Integer businessUnitId;
	/** 订单编号 **/
	private String orderNo;
	/** 订单附属编号 **/
	private String appendNo;
	/** 开始订单时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startOrderDate;
	/** 结束订单时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endOrderDate;
	/** 开始结算时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startPayCreateDate;
	/** 结束结算时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endPayCreateDate;
	/** 结算日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date payCreateDate;

	private List<String> orderNos;

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

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
	}

	public Date getStartOrderDate() {
		return startOrderDate;
	}

	public void setStartOrderDate(Date startOrderDate) {
		this.startOrderDate = startOrderDate;
	}

	public Date getEndOrderDate() {
		return endOrderDate;
	}

	public void setEndOrderDate(Date endOrderDate) {
		this.endOrderDate = endOrderDate;
	}

	public Date getStartPayCreateDate() {
		return startPayCreateDate;
	}

	public void setStartPayCreateDate(Date startPayCreateDate) {
		this.startPayCreateDate = startPayCreateDate;
	}

	public Date getEndPayCreateDate() {
		return endPayCreateDate;
	}

	public void setEndPayCreateDate(Date endPayCreateDate) {
		this.endPayCreateDate = endPayCreateDate;
	}

	public Date getPayCreateDate() {
		return payCreateDate;
	}

	public void setPayCreateDate(Date payCreateDate) {
		this.payCreateDate = payCreateDate;
	}

	public List<String> getOrderNos() {
		return orderNos;
	}

	public void setOrderNos(List<String> orderNos) {
		this.orderNos = orderNos;
	}

}
