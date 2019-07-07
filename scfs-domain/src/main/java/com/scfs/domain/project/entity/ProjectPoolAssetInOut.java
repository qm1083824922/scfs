package com.scfs.domain.project.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016年12月21日.
 */
public class ProjectPoolAssetInOut {
	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 资产-入ID
	 */
	private Integer assetInId;

	/**
	 * 资产-出ID
	 */
	private Integer assetOutId;

	/**
	 * 冗余金额
	 */
	private BigDecimal assetRedant;

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

	public Integer getAssertInId() {
		return assetInId;
	}

	public void setAssertInId(Integer assetInId) {
		this.assetInId = assetInId;
	}

	public Integer getAssertOutId() {
		return assetOutId;
	}

	public void setAssertOutId(Integer assetOutId) {
		this.assetOutId = assetOutId;
	}

	public BigDecimal getAssertRedant() {
		return assetRedant;
	}

	public void setAssertRedant(BigDecimal assetRedant) {
		this.assetRedant = assetRedant;
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
