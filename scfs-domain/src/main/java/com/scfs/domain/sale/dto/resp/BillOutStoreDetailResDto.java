package com.scfs.domain.sale.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by Administrator on 2017年3月22日.
 */
public class BillOutStoreDetailResDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2307451689305973248L;

	private Integer id;
	/**
	 * 出库编号
	 */
	private String billOutStoreNo;
	/**
	 * 出库附属编号
	 */
	private String billOutStoreAffiliateNo;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 仓库ID
	 */
	private Integer warehouseId;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 要求发货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date requiredSendDate;
	/**
	 * 出库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deliverTime;
	/**
	 * 销售单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 销售单号
	 */
	private String billDeliveryNo;
	/**
	 * 销售附属编号
	 */
	private String billDeliveryAffiliateNo;
	/**
	 * 币种 1.人民币 2.美元 3.港币
	 */
	private Integer currencyType;
	/**
	 * 发货单价
	 */
	private BigDecimal sendPrice;
	/**
	 * 拣货数量
	 */
	private BigDecimal pickupNum;
	/**
	 * 库存ID
	 */
	private Integer stlId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
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
	private BigDecimal costPrice;
	/**
	 * 订单单价
	 */
	private BigDecimal poPrice;
	/**
	 * 退货数量
	 */
	private BigDecimal returnNum;
	/*******************************************************/

	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 币种 1.人民币 2.美元 3.港币
	 */
	private String currencyTypeName;
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
	 * 商品单位
	 */
	private String goodsUnit;
	/**
	 * 商品条码
	 */
	private String goodsBarCode;
	/**
	 * 库存状态 1-常规 2-残次品
	 */
	private String goodsStatusName;
	/**
	 * 可退货数量
	 */
	private BigDecimal availableReturnNum;

	/**
	 * 创建时间
	 */
	private Date createAt;
	/***
	 * 经营单位id
	 */
	private Integer busiUnit;
	/***
	 * 回款金额
	 */
	private BigDecimal receivedAmount;
	/***
	 * 发货金额
	 */
	private BigDecimal sendAmount;
	/**
	 * 发货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date sendDate;

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public BigDecimal getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(BigDecimal receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	public String getBillOutStoreNo() {
		return billOutStoreNo;
	}

	public void setBillOutStoreNo(String billOutStoreNo) {
		this.billOutStoreNo = billOutStoreNo;
	}

	public String getBillOutStoreAffiliateNo() {
		return billOutStoreAffiliateNo;
	}

	public void setBillOutStoreAffiliateNo(String billOutStoreAffiliateNo) {
		this.billOutStoreAffiliateNo = billOutStoreAffiliateNo;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Date getRequiredSendDate() {
		return requiredSendDate;
	}

	public void setRequiredSendDate(Date requiredSendDate) {
		this.requiredSendDate = requiredSendDate;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public String getBillDeliveryNo() {
		return billDeliveryNo;
	}

	public void setBillDeliveryNo(String billDeliveryNo) {
		this.billDeliveryNo = billDeliveryNo;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getSendPrice() {
		return sendPrice;
	}

	public void setSendPrice(BigDecimal sendPrice) {
		this.sendPrice = sendPrice;
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

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
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

	public String getGoodsUnit() {
		return goodsUnit;
	}

	public void setGoodsUnit(String goodsUnit) {
		this.goodsUnit = goodsUnit;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

	public String getBillDeliveryAffiliateNo() {
		return billDeliveryAffiliateNo;
	}

	public void setBillDeliveryAffiliateNo(String billDeliveryAffiliateNo) {
		this.billDeliveryAffiliateNo = billDeliveryAffiliateNo;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getAvailableReturnNum() {
		return availableReturnNum;
	}

	public void setAvailableReturnNum(BigDecimal availableReturnNum) {
		this.availableReturnNum = availableReturnNum;
	}

	public BigDecimal getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(BigDecimal returnNum) {
		this.returnNum = returnNum;
	}

}
