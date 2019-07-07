package com.scfs.domain.project.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ProjectPoolFundInOut {
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 资金-入ID
	 */
	private Integer fundInId;

	/**
	 * 资金-出ID
	 */
	private Integer fundOutId;

	/**
	 * 冗余金额
	 */
	private BigDecimal fundRedant;

	/**
	 * 记账日期
	 */
	private Date businessDate;

	/**
	 * 创建时间
	 */
	private Date createAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFundInId() {
		return fundInId;
	}

	public void setFundInId(Integer fundInId) {
		this.fundInId = fundInId;
	}

	public Integer getFundOutId() {
		return fundOutId;
	}

	public void setFundOutId(Integer fundOutId) {
		this.fundOutId = fundOutId;
	}

	public BigDecimal getFundRedant() {
		return fundRedant;
	}

	public void setFundRedant(BigDecimal fundRedant) {
		this.fundRedant = fundRedant;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}