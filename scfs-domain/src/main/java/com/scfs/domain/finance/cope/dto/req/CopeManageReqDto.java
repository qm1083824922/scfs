package com.scfs.domain.finance.cope.dto.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: CopeManageReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年10月31日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CopeManageReqDto extends BaseReqDto {
	private Integer copeId;
	/** 项目id **/
	private Integer projectId;
	/** 客户id **/
	private Integer customerId;
	/** 经营单位 **/
	private Integer busiUnitId;
	/** 币种 **/
	private Integer currnecyType;
	/** 应付开始时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startCopeTime;
	/** 应付结束时间 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endCopeTime;
	/** 单据编号 **/
	private String billNumber;

	public Integer getCopeId() {
		return copeId;
	}

	public void setCopeId(Integer copeId) {
		this.copeId = copeId;
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

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public Date getStartCopeTime() {
		return startCopeTime;
	}

	public void setStartCopeTime(Date startCopeTime) {
		this.startCopeTime = startCopeTime;
	}

	public Date getEndCopeTime() {
		return endCopeTime;
	}

	public void setEndCopeTime(Date endCopeTime) {
		this.endCopeTime = endCopeTime;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

}
