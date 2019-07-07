package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  psm请款待付款（驳回）
 *  File: PmsPayDao.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月05日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PmsPay extends BaseEntity {
	/** 主键id **/
	private Integer id;
	/** 流水表id **/
	private Integer pmsSeriesId;
	/** 请款单号 **/
	private String pay_sn;
	/** 供应商编号 **/
	private String provider_sn;
	/** 币种 **/
	private String currency_type;
	/** 请款单创建日期 **/
	private Date pay_create_time;
	/** 状态 0-待请款 1-驳回 **/
	private Integer status;
	/** 唯一流水号 **/
	private String unique_number;
	/** 抵扣金额 **/
	private BigDecimal deduction_money;
	/** 返回值 0-接收成功 1-接收失败 **/
	private Integer flag;
	/** 返回消息 **/
	private String msg;
	/** 处理结果 1-待处理 2-处理失败 3-处理成功 **/
	private Integer dealFlag;
	/** 处理消息 **/
	private String dealMsg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPmsSeriesId() {
		return pmsSeriesId;
	}

	public void setPmsSeriesId(Integer pmsSeriesId) {
		this.pmsSeriesId = pmsSeriesId;
	}

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	public String getProvider_sn() {
		return provider_sn;
	}

	public void setProvider_sn(String provider_sn) {
		this.provider_sn = provider_sn;
	}

	public String getCurrency_type() {
		return currency_type;
	}

	public void setCurrency_type(String currency_type) {
		this.currency_type = currency_type;
	}

	public Date getPay_create_time() {
		return pay_create_time;
	}

	public void setPay_create_time(Date pay_create_time) {
		this.pay_create_time = pay_create_time;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUnique_number() {
		return unique_number;
	}

	public void setUnique_number(String unique_number) {
		this.unique_number = unique_number;
	}

	public BigDecimal getDeduction_money() {
		return deduction_money;
	}

	public void setDeduction_money(BigDecimal deduction_money) {
		this.deduction_money = deduction_money;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Integer dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getDealMsg() {
		return dealMsg;
	}

	public void setDealMsg(String dealMsg) {
		this.dealMsg = dealMsg;
	}

}
