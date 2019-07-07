package com.scfs.domain.sale.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BillDeliveryDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3214192453486173126L;

	/**
	 * 销售单明细ID
	 */
	private Integer id;

	/**
	 * 销售单ID,关联tb_bill_delivery[id]
	 */
	private Integer billDeliveryId;

	/**
	 * 商品ID,关联tb_base_goods[id]
	 */
	private Integer goodsId;
	/**
	 * 应发货数量
	 */
	private BigDecimal requiredSendNum;

	/**
	 * 应发货单价
	 */
	private BigDecimal requiredSendPrice;

	/**
	 * 库存ID,关联tb_stl[id]
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
	private BigDecimal costPrice;

	/**
	 * 订单单价
	 */
	private BigDecimal poPrice;

	/**
	 * 开票数量
	 */
	private BigDecimal provideInvoiceNum;

	/**
	 * 开票金额
	 */
	private BigDecimal provideInvoiceAmount;

	/**
	 * 收票数量
	 */
	private BigDecimal acceptInvoiceNum;

	/**
	 * 收票金额
	 */
	private BigDecimal acceptInvoiceAmount;
	/**
	 * 指定库存标志
	 */
	private Integer assignStlFlag;

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
	private Date createAt;

	/**
	 * 更新时间
	 */
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
	 * 利润(服务)单价
	 */
	private BigDecimal profitPrice;
	/**
	 * 销售指导价格
	 */
	private BigDecimal saleGuidePrice;
	/**
	 * 关联出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 关联出库单明细ID
	 */
	private Integer billOutStoreDtlId;
	/**
	 * 关联出库单拣货明细ID
	 */
	private Integer billOutStorePickDtlId;
	/**
	 * 原发货单价(退货)
	 */
	private BigDecimal originSendPrice;

	private Date requiredSendDate;
	private Date deliveryDate;
	private Integer projectId;
	private Integer customerId;
	private String billNo;
	private String goodsNo;
	private Date returnTime;
	/** 付款汇率 **/
	private BigDecimal payRate;
	/** 付款实际支付币种 **/
	private Integer payRealCurrency;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public BigDecimal getRequiredSendNum() {
		return requiredSendNum;
	}

	public void setRequiredSendNum(BigDecimal requiredSendNum) {
		this.requiredSendNum = requiredSendNum;
	}

	public BigDecimal getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(BigDecimal requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
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
		this.batchNo = batchNo == null ? null : batchNo.trim();
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
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
		this.creator = creator == null ? null : creator.trim();
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

	public BigDecimal getProvideInvoiceNum() {
		return provideInvoiceNum;
	}

	public void setProvideInvoiceNum(BigDecimal provideInvoiceNum) {
		this.provideInvoiceNum = provideInvoiceNum;
	}

	public BigDecimal getProvideInvoiceAmount() {
		return provideInvoiceAmount;
	}

	public void setProvideInvoiceAmount(BigDecimal provideInvoiceAmount) {
		this.provideInvoiceAmount = provideInvoiceAmount;
	}

	public BigDecimal getAcceptInvoiceNum() {
		return acceptInvoiceNum;
	}

	public void setAcceptInvoiceNum(BigDecimal acceptInvoiceNum) {
		this.acceptInvoiceNum = acceptInvoiceNum;
	}

	public BigDecimal getAcceptInvoiceAmount() {
		return acceptInvoiceAmount;
	}

	public void setAcceptInvoiceAmount(BigDecimal acceptInvoiceAmount) {
		this.acceptInvoiceAmount = acceptInvoiceAmount;
	}

	public Integer getAssignStlFlag() {
		return assignStlFlag;
	}

	public void setAssignStlFlag(Integer assignStlFlag) {
		this.assignStlFlag = assignStlFlag;
	}

	public Date getRequiredSendDate() {
		return requiredSendDate;
	}

	public void setRequiredSendDate(Date requiredSendDate) {
		this.requiredSendDate = requiredSendDate;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
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

	public BigDecimal getProfitPrice() {
		return profitPrice;
	}

	public void setProfitPrice(BigDecimal profitPrice) {
		this.profitPrice = profitPrice;
	}

	public BigDecimal getSaleGuidePrice() {
		return saleGuidePrice;
	}

	public void setSaleGuidePrice(BigDecimal saleGuidePrice) {
		this.saleGuidePrice = saleGuidePrice;
	}

	public Date getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
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

	public Integer getBillOutStorePickDtlId() {
		return billOutStorePickDtlId;
	}

	public void setBillOutStorePickDtlId(Integer billOutStorePickDtlId) {
		this.billOutStorePickDtlId = billOutStorePickDtlId;
	}

	public BigDecimal getOriginSendPrice() {
		return originSendPrice;
	}

	public void setOriginSendPrice(BigDecimal originSendPrice) {
		this.originSendPrice = originSendPrice;
	}

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
	}

	public Integer getPayRealCurrency() {
		return payRealCurrency;
	}

	public void setPayRealCurrency(Integer payRealCurrency) {
		this.payRealCurrency = payRealCurrency;
	}

}