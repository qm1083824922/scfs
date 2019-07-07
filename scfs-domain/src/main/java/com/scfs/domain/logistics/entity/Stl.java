package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.scfs.common.utils.DecimalUtil;

public class Stl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5865196110232555218L;

	private Integer id;
	/**
	 * 入库单理货明细表ID
	 */
	private Integer billInStoreDtlTallyId;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 供应商ID
	 */
	private Integer supplierId;
	/**
	 * 仓库ID
	 */
	private Integer warehouseId;
	/**
	 * 客户ID
	 */
	private Integer customerId;
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
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 理货数量
	 */
	private BigDecimal tallyNum;
	/**
	 * 批次
	 */
	private String batchNo;
	/**
	 * 状态 1-正常 2-残次品
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
	 * 入库数量
	 */
	private BigDecimal inStoreNum;
	/**
	 * 库存数量
	 */
	private BigDecimal storeNum;
	/**
	 * 出库锁定数量
	 */
	private BigDecimal lockNum;
	/**
	 * 销售锁定数量
	 */
	private BigDecimal saleLockNum;
	/**
	 * 关联库存ID
	 */
	private Integer stlId;
	/**
	 * 原入库时间
	 */
	private Date originAcceptTime;
	/**
	 * 入库时间
	 */
	private Date acceptTime;
	/**
	 * 收货日期
	 */
	private Date receiveDate;
	/**
	 * 币种 1-人民币 2-美元
	 */
	private Integer currencyType;
	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;
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
	 * 入库编号
	 */
	private String billInStoreNo;
	/**
	 * 入库附属编号
	 */
	private String affiliateNo;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单附属编号
	 */
	private String appendNo;
	/**
	 * 付款单价
	 */
	private BigDecimal payPrice;
	/**
	 * 付款时间
	 */
	private Date payTime;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	/************ 扩展字段 **************/
	/**
	 * 应发货数量
	 */
	private BigDecimal requiredSendNum;

	/**
	 * 应发货单价
	 */
	private BigDecimal requiredSendPrice;
	/**
	 * 应发货数量
	 */
	private BigDecimal sendNum;

	/**
	 * 应发货单价
	 */
	private BigDecimal sendPrice;
	/**
	 * 销售数量
	 */
	private BigDecimal pickupNum;
	/**
	 * 可用数量
	 */
	@SuppressWarnings("unused")
	private BigDecimal availableNum;
	/**
	 * 可用金额
	 */
	@SuppressWarnings("unused")
	private BigDecimal availableAmount;
	/**
	 * 库存金额
	 */
	@SuppressWarnings("unused")
	private BigDecimal storeAmount;
	/**
	 * 出库锁定金额
	 */
	@SuppressWarnings("unused")
	private BigDecimal lockAmount;
	/**
	 * 销售锁定金额
	 */
	@SuppressWarnings("unused")
	private BigDecimal saleLockAmount;

	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 供应商名称
	 */
	private String supplierName;
	/**
	 * 仓库名称
	 */
	private String warehouseName;
	/**
	 * 客户名称
	 */
	private String customerName;
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

	private BigDecimal availableModel;
	/**
	 * 平均库龄
	 */
	private BigDecimal avgAge;
	/**
	 * 库龄
	 */
	private BigDecimal age;
	/**
	 * 超期天数
	 */
	private BigDecimal overAge;
	/**
	 * 合作账期
	 */
	private BigDecimal periodTime;
	/**
	 * 临期
	 */
	private BigDecimal nearAge;
	/**
	 * CNY汇率
	 */
	private BigDecimal cnyRate;
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

	public Integer getBillInStoreDtlTallyId() {
		return billInStoreDtlTallyId;
	}

	public void setBillInStoreDtlTallyId(Integer billInStoreDtlTallyId) {
		this.billInStoreDtlTallyId = billInStoreDtlTallyId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
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

	public BigDecimal getInStoreNum() {
		return inStoreNum;
	}

	public void setInStoreNum(BigDecimal inStoreNum) {
		this.inStoreNum = inStoreNum;
	}

	public BigDecimal getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(BigDecimal storeNum) {
		this.storeNum = storeNum;
	}

	public BigDecimal getLockNum() {
		return lockNum;
	}

	public void setLockNum(BigDecimal lockNum) {
		this.lockNum = lockNum;
	}

	public Integer getStlId() {
		return stlId;
	}

	public void setStlId(Integer stlId) {
		this.stlId = stlId;
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

	public BigDecimal getAvailableNum() {
		return DecimalUtil.subtract(
				DecimalUtil.subtract(null == this.storeNum ? BigDecimal.ZERO : this.storeNum,
						null == this.lockNum ? BigDecimal.ZERO : this.lockNum),
				null == this.saleLockNum ? BigDecimal.ZERO : this.saleLockNum);
	}

	public void setAvailableNum(BigDecimal availableNum) {
		this.availableNum = availableNum;
	}

	public BigDecimal getAvailableAmount() {
		return DecimalUtil.multiply(
				DecimalUtil.subtract(
						DecimalUtil.subtract(null == this.storeNum ? BigDecimal.ZERO : this.storeNum,
								null == this.lockNum ? BigDecimal.ZERO : this.lockNum),
						null == this.saleLockNum ? BigDecimal.ZERO : this.saleLockNum),
				null == this.costPrice ? BigDecimal.ZERO : this.costPrice);
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getStoreAmount() {
		return DecimalUtil.multiply(null == this.storeNum ? BigDecimal.ZERO : this.storeNum,
				null == this.costPrice ? BigDecimal.ZERO : this.costPrice);
	}

	public void setStoreAmount(BigDecimal storeAmount) {
		this.storeAmount = storeAmount;
	}

	public BigDecimal getLockAmount() {
		return DecimalUtil.multiply(null == this.lockNum ? BigDecimal.ZERO : this.lockNum,
				null == this.costPrice ? BigDecimal.ZERO : this.costPrice);
	}

	public void setLockAmount(BigDecimal lockAmount) {
		this.lockAmount = lockAmount;
	}

	public Integer getBillInStoreId() {
		return billInStoreId;
	}

	public void setBillInStoreId(Integer billInStoreId) {
		this.billInStoreId = billInStoreId;
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

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
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

	public BigDecimal getPickupNum() {
		return pickupNum;
	}

	public void setPickupNum(BigDecimal pickupNum) {
		this.pickupNum = pickupNum;
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

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public String getBillInStoreNo() {
		return billInStoreNo;
	}

	public void setBillInStoreNo(String billInStoreNo) {
		this.billInStoreNo = billInStoreNo;
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

	public BigDecimal getSaleLockNum() {
		return saleLockNum;
	}

	public void setSaleLockNum(BigDecimal saleLockNum) {
		this.saleLockNum = saleLockNum;
	}

	public BigDecimal getSaleLockAmount() {
		return DecimalUtil.multiply(null == this.saleLockNum ? BigDecimal.ZERO : this.saleLockNum,
				null == this.costPrice ? BigDecimal.ZERO : this.costPrice);
	}

	public void setSaleLockAmount(BigDecimal saleLockAmount) {
		this.saleLockAmount = saleLockAmount;
	}

	public BigDecimal getAvailableModel() {
		return availableModel;
	}

	public void setAvailableModel(BigDecimal availableModel) {
		this.availableModel = availableModel;
	}

	public BigDecimal getAvgAge() {
		return avgAge;
	}

	public void setAvgAge(BigDecimal avgAge) {
		this.avgAge = avgAge;
	}

	public BigDecimal getAge() {
		return age;
	}

	public void setAge(BigDecimal age) {
		this.age = age;
	}

	public BigDecimal getOverAge() {
		return overAge;
	}

	public void setOverAge(BigDecimal overAge) {
		this.overAge = overAge;
	}

	public BigDecimal getPeriodTime() {
		return periodTime;
	}

	public void setPeriodTime(BigDecimal periodTime) {
		this.periodTime = periodTime;
	}

	public BigDecimal getNearAge() {
		return nearAge;
	}

	public void setNearAge(BigDecimal nearAge) {
		this.nearAge = nearAge;
	}

	public BigDecimal getCnyRate() {
		return cnyRate;
	}

	public void setCnyRate(BigDecimal cnyRate) {
		this.cnyRate = cnyRate;
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

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}