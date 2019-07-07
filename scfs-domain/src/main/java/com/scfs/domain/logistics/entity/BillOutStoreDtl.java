package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.scfs.common.utils.DecimalUtil;

public class BillOutStoreDtl implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4432396826323839167L;

	private Integer id;
	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 出库单ID
	 */
	private Integer billDeliveryDtlId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 发货数量
	 */
	private BigDecimal sendNum;
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
	 * 批次
	 */
	private String batchNo;
	/**
	 * 库存状态 1-常规 2-残次品
	 */
	private Integer goodsStatus;
	/**
	 * 成本金额
	 */
	private BigDecimal costAmount;
	/**
	 * 订单金额
	 */
	private BigDecimal poAmount;
	/**
	 * 指定库存标志
	 */
	private Integer assignStlFlag;
	/**
	 * 已报关数量
	 */
	private BigDecimal customsDeclareNum;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建人ID
	 */
	private Integer poReturnId;
	/**
	 * 创建人ID
	 */
	private Integer poReturnDtlId;
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
	 * 资金归还金额
	 */
	private BigDecimal fundBackDtlAmount;

	/*************** 扩展字段 *******************/
	/**
	 * 拣货明细
	 */
	private List<BillOutStorePickDtl> billOutStorePickDtlList;
	/**
	 * 库存
	 */
	private List<Stl> stlList;
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
	 * 未拣货数量
	 */
	@SuppressWarnings("unused")
	private BigDecimal unPickupNum;
	/**
	 * 拣货金额
	 */
	private BigDecimal pickupAmount;
	/**
	 * 发货金额
	 */
	@SuppressWarnings("unused")
	private BigDecimal sendAmount;

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

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public BigDecimal getSendNum() {
		return sendNum;
	}

	public void setSendNum(BigDecimal sendNum) {
		this.sendNum = sendNum;
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

	public BigDecimal getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(BigDecimal costAmount) {
		this.costAmount = costAmount;
	}

	public BigDecimal getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(BigDecimal poAmount) {
		this.poAmount = poAmount;
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

	public BigDecimal getUnPickupNum() {
		return DecimalUtil.subtract(null == this.sendNum ? BigDecimal.ZERO : this.sendNum,
				null == this.pickupNum ? BigDecimal.ZERO : this.pickupNum);
	}

	public void setUnPickupNum(BigDecimal unPickupNum) {
		this.unPickupNum = unPickupNum;
	}

	public BigDecimal getPickupAmount() {
		return pickupAmount;
	}

	public void setPickupAmount(BigDecimal pickupAmount) {
		this.pickupAmount = pickupAmount;
	}

	public BigDecimal getSendAmount() {
		return DecimalUtil.multiply(null == this.sendNum ? BigDecimal.ZERO : this.sendNum,
				null == this.sendPrice ? BigDecimal.ZERO : this.sendPrice);
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	public List<BillOutStorePickDtl> getBillOutStorePickDtlList() {
		return billOutStorePickDtlList;
	}

	public void setBillOutStorePickDtlList(List<BillOutStorePickDtl> billOutStorePickDtlList) {
		this.billOutStorePickDtlList = billOutStorePickDtlList;
	}

	public List<Stl> getStlList() {
		return stlList;
	}

	public void setStlList(List<Stl> stlList) {
		this.stlList = stlList;
	}

	public Integer getAssignStlFlag() {
		return assignStlFlag;
	}

	public void setAssignStlFlag(Integer assignStlFlag) {
		this.assignStlFlag = assignStlFlag;
	}

	public Integer getBillDeliveryDtlId() {
		return billDeliveryDtlId;
	}

	public void setBillDeliveryDtlId(Integer billDeliveryDtlId) {
		this.billDeliveryDtlId = billDeliveryDtlId;
	}

	public BigDecimal getCustomsDeclareNum() {
		return customsDeclareNum;
	}

	public void setCustomsDeclareNum(BigDecimal customsDeclareNum) {
		this.customsDeclareNum = customsDeclareNum;
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

	public Integer getPoReturnId() {
		return poReturnId;
	}

	public void setPoReturnId(Integer poReturnId) {
		this.poReturnId = poReturnId;
	}

	public Integer getPoReturnDtlId() {
		return poReturnDtlId;
	}

	public void setPoReturnDtlId(Integer poReturnDtlId) {
		this.poReturnDtlId = poReturnDtlId;
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

	public BigDecimal getFundBackDtlAmount() {
		return fundBackDtlAmount;
	}

	public void setFundBackDtlAmount(BigDecimal fundBackDtlAmount) {
		this.fundBackDtlAmount = fundBackDtlAmount;
	}

}