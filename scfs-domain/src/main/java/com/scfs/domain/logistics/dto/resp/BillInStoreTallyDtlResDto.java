package com.scfs.domain.logistics.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.NumSerializer;
import com.scfs.domain.PriceSerializer;

/**
 * Created by Administrator on 2016年10月18日.
 */
public class BillInStoreTallyDtlResDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 156311942856876520L;
	private Integer id;
	/**
	 * 入库单ID
	 */
	private Integer billInStoreId;
	/**
	 * 入库单明细ID
	 */
	private Integer billInStoreDtlId;
	/**
	 * PO订单明细ID
	 */
	private Integer poDtlId;
	/**
	 * PO订单ID
	 */
	private Integer poId;
	/**
	 * 出库单明细ID
	 */
	private Integer billOutStoreDtlId;
	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 理货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal tallyNum;
	/**
	 * 批次
	 */
	private String batchNo;
	/**
	 * 库存状态 1-常规 2-残次品
	 */
	private Integer goodsStatus;
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
	 * 到货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date receiveDate;
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
	 * 关联销售退货单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 关联销售退货单明细ID
	 */
	private Integer billDeliveryDtlId;

	/**********************************************/
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单附属编号
	 */
	private String appendNo;
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
	 * 收货单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal receivePrice;
	/**
	 * 订单单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal poPrice;
	/**
	 * 库存状态 1-常规 2-残次品
	 */
	private String goodsStatusName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillInStoreId() {
		return billInStoreId;
	}

	public void setBillInStoreId(Integer billInStoreId) {
		this.billInStoreId = billInStoreId;
	}

	public Integer getBillInStoreDtlId() {
		return billInStoreDtlId;
	}

	public void setBillInStoreDtlId(Integer billInStoreDtlId) {
		this.billInStoreDtlId = billInStoreDtlId;
	}

	public Integer getPoDtlId() {
		return poDtlId;
	}

	public void setPoDtlId(Integer poDtlId) {
		this.poDtlId = poDtlId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public BigDecimal getTallyNum() {
		return tallyNum;
	}

	public void setTallyNum(BigDecimal tallyNum) {
		this.tallyNum = tallyNum;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAppendNo() {
		return appendNo;
	}

	public void setAppendNo(String appendNo) {
		this.appendNo = appendNo;
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

	public BigDecimal getReceivePrice() {
		return receivePrice;
	}

	public void setReceivePrice(BigDecimal receivePrice) {
		this.receivePrice = receivePrice;
	}

	public BigDecimal getPoPrice() {
		return poPrice;
	}

	public void setPoPrice(BigDecimal poPrice) {
		this.poPrice = poPrice;
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

	public Integer getBillOutStoreDtlId() {
		return billOutStoreDtlId;
	}

	public void setBillOutStoreDtlId(Integer billOutStoreDtlId) {
		this.billOutStoreDtlId = billOutStoreDtlId;
	}

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
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

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
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

	public Date getOriginAcceptTime() {
		return originAcceptTime;
	}

	public void setOriginAcceptTime(Date originAcceptTime) {
		this.originAcceptTime = originAcceptTime;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getBillDeliveryDtlId() {
		return billDeliveryDtlId;
	}

	public void setBillDeliveryDtlId(Integer billDeliveryDtlId) {
		this.billDeliveryDtlId = billDeliveryDtlId;
	}

}
