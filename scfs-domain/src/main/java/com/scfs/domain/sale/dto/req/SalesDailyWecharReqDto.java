package com.scfs.domain.sale.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: SalesDailyWecharReqDto.java
 *  Description:销售日报微信推送
 *  TODO
 *  Date,					Who,				
 *  2017年10月26日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class SalesDailyWecharReqDto extends BaseReqDto {
	/** 部门负责人用户id **/
	private Integer departmentUserId;
	/** 昨日数据日期 **/
	private String dailyDate;
	/** 发送状态 **/
	private Integer sendState;
	/** 开始日期 **/
	private String startDate;
	/** 结束日期 **/
	private String endDate;
	/** 币种 **/
	private Integer currnecyType;

	public Integer getDepartmentUserId() {
		return departmentUserId;
	}

	public void setDepartmentUserId(Integer departmentUserId) {
		this.departmentUserId = departmentUserId;
	}

	public String getDailyDate() {
		return dailyDate;
	}

	public void setDailyDate(String dailyDate) {
		this.dailyDate = dailyDate;
	}

	public Integer getSendState() {
		return sendState;
	}

	public void setSendState(Integer sendState) {
		this.sendState = sendState;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

}
