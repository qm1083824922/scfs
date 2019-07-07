package com.scfs.domain.sale.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: SalesDailyWechar.java
 *  Description:销售日报微信推送
 *  TODO
 *  Date,					Who,				
 *  2017年10月26日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class SalesDailyWechar extends BaseEntity {
	/** 部门负责人用户id **/
	private Integer departmentUserId;
	/** 昨日数据日期 **/
	private String dailyDate;
	/** 日销售额 **/
	private BigDecimal dailySales = BigDecimal.ZERO;
	/** 月销售额 **/
	private BigDecimal monthSales = BigDecimal.ZERO;
	/** 日付款额 **/
	private BigDecimal dailyPay = BigDecimal.ZERO;
	/** 月付款额 **/
	private BigDecimal monthPay = BigDecimal.ZERO;
	/** 日回款额 **/
	private BigDecimal dailyPayment = BigDecimal.ZERO;
	/** 月回款额 **/
	private BigDecimal monthPayment = BigDecimal.ZERO;
	/** 库存数量 **/
	private BigDecimal stlNum = BigDecimal.ZERO;
	/** 库存金额 **/
	private BigDecimal stlAmount = BigDecimal.ZERO;
	/** 币种 **/
	private Integer currnecyType;
	/** 发送状态0 未发送，1 发送成功 **/
	private Integer sendState;
	/** 发送时间 **/
	private Date sendDate;

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

	public BigDecimal getDailySales() {
		return dailySales;
	}

	public void setDailySales(BigDecimal dailySales) {
		this.dailySales = dailySales;
	}

	public BigDecimal getMonthSales() {
		return monthSales;
	}

	public void setMonthSales(BigDecimal monthSales) {
		this.monthSales = monthSales;
	}

	public BigDecimal getDailyPay() {
		return dailyPay;
	}

	public void setDailyPay(BigDecimal dailyPay) {
		this.dailyPay = dailyPay;
	}

	public BigDecimal getMonthPay() {
		return monthPay;
	}

	public void setMonthPay(BigDecimal monthPay) {
		this.monthPay = monthPay;
	}

	public BigDecimal getDailyPayment() {
		return dailyPayment;
	}

	public void setDailyPayment(BigDecimal dailyPayment) {
		this.dailyPayment = dailyPayment;
	}

	public BigDecimal getMonthPayment() {
		return monthPayment;
	}

	public void setMonthPayment(BigDecimal monthPayment) {
		this.monthPayment = monthPayment;
	}

	public BigDecimal getStlNum() {
		return stlNum;
	}

	public void setStlNum(BigDecimal stlNum) {
		this.stlNum = stlNum;
	}

	public BigDecimal getStlAmount() {
		return stlAmount;
	}

	public void setStlAmount(BigDecimal stlAmount) {
		this.stlAmount = stlAmount;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public Integer getSendState() {
		return sendState;
	}

	public void setSendState(Integer sendState) {
		this.sendState = sendState;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

}
