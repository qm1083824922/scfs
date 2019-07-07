package com.scfs.domain.logistics.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.NumSerializer;
import com.scfs.domain.PriceSerializer;

/**
 * Created by Administrator on 2016年10月20日.
 */
public class BillOutStorePickDtlResDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3333084082103110658L;
	private Integer id;
	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 出库单明细ID
	 */
	private Integer billOutStoreDtlId;
	/**
	 * PO订单ID
	 */
	private Integer poId;
	/**
	 * PO订单明细ID
	 */
	private Integer poDtlId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 拣货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal pickupNum;
	/**
	 * 发货单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal sendPrice;
	/**
	 * 库存ID
	 */
	private Integer stlId;
	/**
	 * 批次
	 */
	private String batchNo;
	/**
	 * 库存状态 1-常规 2-残次品
	 */
	private Integer goodsStatus;
	/**
	 * 成本单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal costPrice;
	/**
	 * 订单单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal poPrice;
	/**
	 * 入库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date acceptTime;
	/**
	 * 原入库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date originAcceptTime;
	/**
	 * 币种 1-人民币 2-美元 3-港元
	 */
	private Integer currencyType;
	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 供应商ID
	 */
	private Integer supplierId;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人ID
	 */
	private Integer creatorId;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateAt;
	/**
	 * 付款单价
	 */
	private BigDecimal payPrice;
	/**
	 * 付款时间
	 */
	private Date payTime;
	/**
	 * 销售退货数量
	 */
	private BigDecimal returnNum;

	/******************************************************/
	/**
	 * 库存状态 1-常规 2-残次品
	 */
	private String goodsStatusName;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 商品编号
	 */
	private String goodsNumber;
	/**
	 * 商品型号
	 */
	private String goodsType;
	/**
	 * 商品条码
	 */
	private String goodsBarCode;
	/**
	 * 商品单位
	 */
	private String goodsUnit;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public Integer getBillOutStoreDtlId() {
		return billOutStoreDtlId;
	}

	public void setBillOutStoreDtlId(Integer billOutStoreDtlId) {
		this.billOutStoreDtlId = billOutStoreDtlId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public BigDecimal getPickupNum() {
		return pickupNum;
	}

	public void setPickupNum(BigDecimal pickupNum) {
		this.pickupNum = pickupNum;
	}

	public Integer getStlId() {
		return stlId;
	}

	public void setStlId(Integer stlId) {
		this.stlId = stlId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getPoPrice() {
		return poPrice;
	}

	public void setPoPrice(BigDecimal poPrice) {
		this.poPrice = poPrice;
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
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

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

	public BigDecimal getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(BigDecimal sendPrice) {
		this.sendPrice = sendPrice;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getPoDtlId() {
		return poDtlId;
	}

	public void setPoDtlId(Integer poDtlId) {
		this.poDtlId = poDtlId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public Date getOriginAcceptTime() {
		return originAcceptTime;
	}

	public void setOriginAcceptTime(Date originAcceptTime) {
		this.originAcceptTime = originAcceptTime;
	}

	public BigDecimal getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(BigDecimal returnNum) {
		this.returnNum = returnNum;
	}

}
