package com.scfs.domain.base.dto.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 *  客户维护信息表
 *  File: CustomerMaintainResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月26日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CustomerMaintainReqDto extends BaseReqDto {
	private Integer userId;
	/** 客户编号 **/
	private String customerNo;
	/** 客户名称 **/
	private String chineseName;
	/** 来源渠道 **/
	private Integer sourceChannel;
	/** 维护人 **/
	private Integer guardian;
	/** 跟进人 **/
	private Integer fllow;
	/** 创建日期开始日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startCreateTime;
	/** 创建日期结束日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endCreateTime;
	/** 所处阶段 1 意向阶段 2 合作阶段 3 已取消 **/
	private Integer stage;
	/** 客户类型 1 客户 2 供应商 3 经营单位 **/
	private Integer customerType;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public Integer getSourceChannel() {
		return sourceChannel;
	}

	public void setSourceChannel(Integer sourceChannel) {
		this.sourceChannel = sourceChannel;
	}

	public Integer getGuardian() {
		return guardian;
	}

	public void setGuardian(Integer guardian) {
		this.guardian = guardian;
	}

	public Integer getFllow() {
		return fllow;
	}

	public void setFllow(Integer fllow) {
		this.fllow = fllow;
	}

	public Date getStartCreateTime() {
		return startCreateTime;
	}

	public void setStartCreateTime(Date startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

}
