package com.scfs.domain.api.pms.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PmsStoreIn {
	/** 主键id */
	private Integer id;
	/** 订单头id */
	private Integer pmsSeriesId;
	/** 采购单ID **/
	private Integer poId;
	/** 采购单号 */
	private String purchase_sn;
	/** 供应商编号 */
	private String provider_sn;
	/** 币种 */
	private String currency_type;
	/** 商品sku */
	private String sku;
	/** 入库数量 */
	private BigDecimal stockin_num;
	/** 入库价格 */
	private BigDecimal purchase_price;
	/** 入库日期 */
	private Date stockin_time;
	/** 送货单号 */
	private String purchase_delivery_sn;
	/** 创建时间 */
	private Date createAt;
	/** 修改时间 */
	private Date updateAt;
	/** 返回状态 */
	private Integer flag;
	/** 返回结果 */
	private String msg;
	/** 处理结果 */
	private Integer dealFlag;
	/** 处理消息 */
	private String dealMsg;

	/** 可用库存数量 **/
	private BigDecimal remain_send_num;
	/** 供应商id **/
	private Integer supplier_id;
	/**
	 * 结算对象
	 */
	private String account_sn;

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

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public BigDecimal getRemain_send_num() {
		return remain_send_num;
	}

	public void setRemain_send_num(BigDecimal remain_send_num) {
		this.remain_send_num = remain_send_num;
	}

	public Integer getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(Integer supplier_id) {
		this.supplier_id = supplier_id;
	}

	public String getAccount_sn() {
		return account_sn;
	}

	public void setAccount_sn(String account_sn) {
		this.account_sn = account_sn;
	}

}