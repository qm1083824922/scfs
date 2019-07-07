package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PmsStoreOut {
	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 订单头id
	 */
	private Integer pmsSeriesId;

	/**
	 * 采购单号
	 */
	private String purchase_sn;

	/**
	 * 供应商编号
	 */
	private String provider_sn;

	/**
	 * 币种
	 */
	private String currency_type;

	/**
	 * 商品sku
	 */
	private String sku;

	/**
	 * 销售数量
	 */
	private BigDecimal wms_out_stockin;

	/**
	 * 销售价格
	 */
	private BigDecimal purchase_price;

	/**
	 * 销售日期
	 */
	private Date sales_date;

	/**
	 * 创建时间
	 */
	private Date create_at;

	/**
	 * 修改时间
	 */
	private Date updateAt;

	/**
	 * 返回值 0 接受成功 1:接受失败
	 * 
	 */
	private Integer flag;
	/**
	 * 返回信息
	 */
	private String msg;
	/** 处理消息 **/
	private String dealMsg;
	/** 处理结果 **/
	private Integer dealFlag;
	/**
	 * 销售id
	 * 
	 */
	private Integer sku_id;

	/**
	 * 结算对象
	 */
	private String account_sn;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

	public Integer getPmsSeriesId() {
		return pmsSeriesId;
	}

	public void setPmsSeriesId(Integer pmsSeriesId) {
		this.pmsSeriesId = pmsSeriesId;
	}

	public Integer getflag() {
		return flag;
	}

	public void setflag(Integer flag) {
		this.flag = flag;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPurchase_sn() {
		return purchase_sn;
	}

	public void setPurchase_sn(String purchase_sn) {
		this.purchase_sn = purchase_sn;
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

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public BigDecimal getWms_out_stockin() {
		return wms_out_stockin;
	}

	public void setWms_out_stockin(BigDecimal wms_out_stockin) {
		this.wms_out_stockin = wms_out_stockin;
	}

	public BigDecimal getPurchase_price() {
		return purchase_price;
	}

	public void setPurchase_price(BigDecimal purchase_price) {
		this.purchase_price = purchase_price;
	}

	public Date getSales_date() {
		return sales_date;
	}

	public void setSales_date(Date sales_date) {
		this.sales_date = sales_date;
	}

	public Date getCreate_at() {
		return create_at;
	}

	public void setCreate_at(Date create_at) {
		this.create_at = create_at;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getDealMsg() {
		return dealMsg;
	}

	public void setDealMsg(String dealMsg) {
		this.dealMsg = dealMsg;
	}

	public Integer getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Integer dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getAccount_sn() {
		return account_sn;
	}

	public void setAccount_sn(String account_sn) {
		this.account_sn = account_sn;
	}
}