package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  psm请款待付款（驳回）详情
 *  File: PmsPayDao.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月06日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PmsPayDtl extends BaseEntity {
	/** 主键id **/
	private Integer id;
	/** PMS同步请款单(待付款或驳回)id **/
	private Integer pmsPayId;
	/** 采购单号 **/
	private String purchase_sn;
	/** 商品sku **/
	private String sku;
	/** 数量 **/
	private BigDecimal pay_quantity;
	/** 价格 **/
	private BigDecimal deal_price;
	/**
	 * 结算对象
	 */
	private String account_sn;

	// 1-待处理 2-处理失败 3-处理成功
	private Integer dealFlag;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPmsPayId() {
		return pmsPayId;
	}

	public void setPmsPayId(Integer pmsPayId) {
		this.pmsPayId = pmsPayId;
	}

	public String getPurchase_sn() {
		return purchase_sn;
	}

	public void setPurchase_sn(String purchase_sn) {
		this.purchase_sn = purchase_sn;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public BigDecimal getPay_quantity() {
		return pay_quantity;
	}

	public void setPay_quantity(BigDecimal pay_quantity) {
		this.pay_quantity = pay_quantity;
	}

	public BigDecimal getDeal_price() {
		return deal_price;
	}

	public void setDeal_price(BigDecimal deal_price) {
		this.deal_price = deal_price;
	}

	public String getAccount_sn() {
		return account_sn;
	}

	public void setAccount_sn(String account_sn) {
		this.account_sn = account_sn;
	}

	public Integer getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Integer dealFlag) {
		this.dealFlag = dealFlag;
	}

}
