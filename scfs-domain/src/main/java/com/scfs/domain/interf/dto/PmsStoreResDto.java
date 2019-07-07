package com.scfs.domain.interf.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 *
 */
public class PmsStoreResDto {
	/** 主键id */
	private Integer id;
	/** 订单头id */
	private Integer pmsSeriesId;
	/** 采购单号 */
	private String purchase_sn;
	/** 供应商编号 */
	private String provider_sn;
	/** 币种 */
	private String currency_type;
	/** 币种名称 */
	private String currencyName;
	/** 商品sku */
	private String sku;
	/** 入库数量 */
	private BigDecimal stockin_num;
	/**
	 * 销售数量
	 */
	private BigDecimal wms_out_stockin;
	/** 入库价格 */
	private BigDecimal purchase_price;
	/** 入库日期 */
	private Date stockin_time;
	/**
	 * 销售日期
	 */
	private Date sales_date;
	/** 送货单号 */
	private String purchase_delivery_sn;
	/** 创建时间 */
	private Date createAt;
	/** 修改时间 */
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
	/**
	 * 销售id
	 * 
	 */
	private Integer sku_id;
	private Integer type;
	/** 情况单号 **/
	private String pay_sn;
	/** 抵扣金额 **/
	private BigDecimal deduction_money;
	/** 请款单创建日期 **/
	private Date pay_create_time;
	/** 请款数量 **/
	private BigDecimal pay_quantity;
	/** 请款价格 **/
	private BigDecimal deal_price;
	/** 处理结果 **/
	private String dealFlagName;
	/** 状态名称 **/
	private String statusName;

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getDealFlagName() {
		return dealFlagName;
	}

	public void setDealFlagName(String dealFlagName) {
		this.dealFlagName = dealFlagName;
	}

	public Date getPay_create_time() {
		return pay_create_time;
	}

	public void setPay_create_time(Date pay_create_time) {
		this.pay_create_time = pay_create_time;
	}

	public String getPay_sn() {
		return pay_sn;
	}

	public BigDecimal getDeduction_money() {
		return deduction_money;
	}

	public void setDeduction_money(BigDecimal deduction_money) {
		this.deduction_money = deduction_money;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BigDecimal getWms_out_stockin() {
		return wms_out_stockin;
	}

	public void setWms_out_stockin(BigDecimal wms_out_stockin) {
		this.wms_out_stockin = wms_out_stockin;
	}

	public Date getSales_date() {
		return sales_date;
	}

	public void setSales_date(Date sales_date) {
		this.sales_date = sales_date;
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

	public Integer getSku_id() {
		return sku_id;
	}

	public void setSku_id(Integer sku_id) {
		this.sku_id = sku_id;
	}

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

	public BigDecimal getStockin_num() {
		return stockin_num;
	}

	public void setStockin_num(BigDecimal stockin_num) {
		this.stockin_num = stockin_num;
	}

	public BigDecimal getPurchase_price() {
		return purchase_price;
	}

	public void setPurchase_price(BigDecimal purchase_price) {
		this.purchase_price = purchase_price;
	}

	public Date getStockin_time() {
		return stockin_time;
	}

	public void setStockin_time(Date stockin_time) {
		this.stockin_time = stockin_time;
	}

	public String getPurchase_delivery_sn() {
		return purchase_delivery_sn;
	}

	public void setPurchase_delivery_sn(String purchase_delivery_sn) {
		this.purchase_delivery_sn = purchase_delivery_sn;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
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

}
