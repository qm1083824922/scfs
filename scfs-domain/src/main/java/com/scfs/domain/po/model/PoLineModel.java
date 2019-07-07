package com.scfs.domain.po.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/17.
 */
public class PoLineModel {
	// 行ID
	private Integer id;
	/**
	 * 采购单ID
	 */
	private Integer poId;

	private Integer projectId;

	private String projectName;

	private Integer supplierId;

	private String supplierName;

	private Integer warehouseId;

	private String warehouseName;

	private Integer customerId;

	private String customerName;

	// 商品编号
	private Integer goodsId;
	/***
	 * 商品条码
	 */
	private String goodsBarCode;
	/***
	 * 商品编码
	 */
	private String goodsNo;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/** 商品型号 */
	private String goodsType;
	private String specification;
	/** 商品数量 */
	// @JsonSerialize(using = NumSerializer.class)
	private BigDecimal goodsNum;
	/** 商品单价 */
	private BigDecimal goodsPrice;
	/** 商品金额goodsNum*goodsPrice */
	private BigDecimal goodsAmount;
	/** 入库数量 */
	private BigDecimal storageNum;
	/** 商品折扣金额 **/
	private BigDecimal discountAmount;
	/** 商品折扣单价 **/
	private BigDecimal discountPrice;
	/** 商品折扣总金额goodsNum*discountAmount **/
	private BigDecimal priceCountAmout;
	/** 商品折扣总金额总和 **/
	private BigDecimal totalPriceCountAmout;
	/** 币种 */
	private Integer currencyId;
	/** 币种名称 */
	private String currencyName;
	/** 到货数量 */
	private BigDecimal arrivalNum;
	/** 到货金额 */
	private BigDecimal arrivalAmount;
	/** 收票数量 */
	private BigDecimal invoiceTotalNum;
	/** 收票金额 */
	private BigDecimal invoiceTotalAmount;
	/** 批次号 */
	private String batchNum;
	/** 库存状态 */
	private String goodsStatus;
	/** 库存状态名称 */
	private String goodsStatusName;
	/** 原采购单价 */
	private BigDecimal originGoodsPrice;
	/** 订单总数量 */
	private BigDecimal orderTotalNum;
	/** 订单总金额 */
	private BigDecimal orderTotalAmount;
	private BigDecimal totalDiscountAmount;

	/** 采购编号 */
	private String orderNo;
	/** 采购订单附属编号 */
	private String appendNo;
	/** 订单日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date orderTime;
	/** 单位 */
	private String unit;
	/** 税率 **/
	private BigDecimal taxRate;
	/** 收票可用数量 **/
	private BigDecimal blanceNum;
	/** 收票可用金额 **/
	private BigDecimal blanceAmount;
	/** 已付款金额 **/
	private BigDecimal paidAmount;
	private BigDecimal requiredSendPrice;
	/** 规格条码 **/
	private String barSpecificationName;
	/** 备注 **/
	private String mark;

	public String getMark() {
		return mark;
	}

	/** 已付款金额 **/
	private BigDecimal sendAmount;
	private BigDecimal sendNum;
	private BigDecimal returnAmount;
	private BigDecimal returnNum;
	/** 铺货采购单ID */
	private Integer distributeId;
	private BigDecimal distributeNum;
	private BigDecimal pledgeProportion;
	private BigDecimal remainSendNum;
	/** 毛重 **/
	private BigDecimal grossWeight;
	/** 毛重 **/
	private BigDecimal netWeight;
	/** 体积 **/
	private BigDecimal volume;
	private BigDecimal payPrice;
	/** 抵扣金额 **/
	private BigDecimal deductionMoney;
	/** 付款单汇率 **/
	private BigDecimal payRate;
	/** 订单单价 **/
	private BigDecimal poPrice;
	/** 成本单价 **/
	private BigDecimal costPrice;
	/** 资金占用天数 **/
	private Integer occupyDay;
	/** 资金占用服务费 **/
	private BigDecimal occupyServiceAmount;
	/** 日服务费率 **/
	private BigDecimal fundMonthRate;
	/** 铺货明细ID **/
	private Integer distributeLineId;
	/** 退款金额 **/
	private BigDecimal refundAmount;

	/** 明细金额的和 **/
	private BigDecimal countGoodsAmount;
	/** 经营单位id **/
	private Integer businessUnitId;
	/** 创建时间 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	/** 折扣后金额 amount- discountAmount **/
	private BigDecimal afterDiscountAmount;
	/** 付款单实际支付币种 **/
	private Integer realCurrencyType;
	/** 付款单实际支付币种名称 **/
	private String realCurrencyTypeName;
	/** 付款单实际支付币种 **/
	private Integer payRealCurrency;
	/**
	 * 明细折扣金额之和
	 */
	private BigDecimal sumDiscountAmount;
	/**
	 * 乘以汇率以后的折扣金额之和
	 */
	private BigDecimal sumRateDisAmount;
	/**
	 * 乘以汇率以后的折扣金额
	 */
	private BigDecimal rateDisAmount;

	/** 抵扣单价 **/
	private BigDecimal deductionPrice;
	/** 抵扣后金额 **/
	private BigDecimal afterDeductionMoney;

	public Integer getPayRealCurrency() {
		return payRealCurrency;
	}

	public void setPayRealCurrency(Integer payRealCurrency) {
		this.payRealCurrency = payRealCurrency;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
	}

	public BigDecimal getDeductionMoney() {
		return deductionMoney;
	}

	public void setDeductionMoney(BigDecimal deductionMoney) {
		this.deductionMoney = deductionMoney;
	}

	private Date payTime;

	public void setMark(String mark) {
		this.mark = mark;
	}

	public BigDecimal getGrossWeight() {
		return grossWeight;
	}

	public void setGrossWeight(BigDecimal grossWeight) {
		this.grossWeight = grossWeight;
	}

	public BigDecimal getNetWeight() {
		return netWeight;
	}

	public void setNetWeight(BigDecimal netWeight) {
		this.netWeight = netWeight;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public BigDecimal getOrderTotalNum() {
		return orderTotalNum;
	}

	public void setOrderTotalNum(BigDecimal orderTotalNum) {
		this.orderTotalNum = orderTotalNum;
	}

	public BigDecimal getOrderTotalAmount() {
		return orderTotalAmount;
	}

	public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
		this.orderTotalAmount = orderTotalAmount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public BigDecimal getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(BigDecimal goodsNum) {
		this.goodsNum = goodsNum;
	}

	public BigDecimal getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(BigDecimal goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public BigDecimal getArrivalAmount() {
		return arrivalAmount;
	}

	public void setArrivalAmount(BigDecimal arrivalAmount) {
		this.arrivalAmount = arrivalAmount;
	}

	public BigDecimal getInvoiceTotalNum() {
		return invoiceTotalNum;
	}

	public void setInvoiceTotalNum(BigDecimal invoiceTotalNum) {
		this.invoiceTotalNum = invoiceTotalNum;
	}

	public BigDecimal getInvoiceTotalAmount() {
		return invoiceTotalAmount;
	}

	public void setInvoiceTotalAmount(BigDecimal invoiceTotalAmount) {
		this.invoiceTotalAmount = invoiceTotalAmount;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
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

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getBlanceNum() {
		return blanceNum;
	}

	public void setBlanceNum(BigDecimal blanceNum) {
		this.blanceNum = blanceNum;
	}

	public BigDecimal getBlanceAmount() {
		return blanceAmount;
	}

	public void setBlanceAmount(BigDecimal blanceAmount) {
		this.blanceAmount = blanceAmount;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getArrivalNum() {
		return arrivalNum;
	}

	public void setArrivalNum(BigDecimal arrivalNum) {
		this.arrivalNum = arrivalNum;
	}

	public BigDecimal getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(BigDecimal requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getTotalDiscountAmount() {
		return totalDiscountAmount;
	}

	public void setTotalDiscountAmount(BigDecimal totalDiscountAmount) {
		this.totalDiscountAmount = totalDiscountAmount;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public BigDecimal getStorageNum() {
		return storageNum;
	}

	public void setStorageNum(BigDecimal storageNum) {
		this.storageNum = storageNum;
	}

	public BigDecimal getOriginGoodsPrice() {
		return originGoodsPrice;
	}

	public void setOriginGoodsPrice(BigDecimal originGoodsPrice) {
		this.originGoodsPrice = originGoodsPrice;
	}

	public String getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

	public BigDecimal getPriceCountAmout() {
		return priceCountAmout;
	}

	public void setPriceCountAmout(BigDecimal priceCountAmout) {
		this.priceCountAmout = priceCountAmout;
	}

	public String getBarSpecificationName() {
		return barSpecificationName;
	}

	public void setBarSpecificationName(String barSpecificationName) {
		this.barSpecificationName = barSpecificationName;
	}

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	public BigDecimal getSendNum() {
		return sendNum;
	}

	public void setSendNum(BigDecimal sendNum) {
		this.sendNum = sendNum;
	}

	public BigDecimal getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(BigDecimal returnAmount) {
		this.returnAmount = returnAmount;
	}

	public BigDecimal getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(BigDecimal returnNum) {
		this.returnNum = returnNum;
	}

	public BigDecimal getDistributeNum() {
		return distributeNum;
	}

	public void setDistributeNum(BigDecimal distributeNum) {
		this.distributeNum = distributeNum;
	}

	public BigDecimal getPledgeProportion() {
		return pledgeProportion;
	}

	public void setPledgeProportion(BigDecimal pledgeProportion) {
		this.pledgeProportion = pledgeProportion;
	}

	public BigDecimal getRemainSendNum() {
		return remainSendNum;
	}

	public void setRemainSendNum(BigDecimal remainSendNum) {
		this.remainSendNum = remainSendNum;
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

	public Integer getDistributeId() {
		return distributeId;
	}

	public void setDistributeId(Integer distributeId) {
		this.distributeId = distributeId;
	}

	public BigDecimal getPoPrice() {
		return poPrice;
	}

	public void setPoPrice(BigDecimal poPrice) {
		this.poPrice = poPrice;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public Integer getOccupyDay() {
		return occupyDay;
	}

	public void setOccupyDay(Integer occupyDay) {
		this.occupyDay = occupyDay;
	}

	public BigDecimal getOccupyServiceAmount() {
		return occupyServiceAmount;
	}

	public void setOccupyServiceAmount(BigDecimal occupyServiceAmount) {
		this.occupyServiceAmount = occupyServiceAmount;
	}

	public BigDecimal getFundMonthRate() {
		return fundMonthRate;
	}

	public void setFundMonthRate(BigDecimal fundMonthRate) {
		this.fundMonthRate = fundMonthRate;
	}

	public Integer getDistributeLineId() {
		return distributeLineId;
	}

	public void setDistributeLineId(Integer distributeLineId) {
		this.distributeLineId = distributeLineId;
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

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public BigDecimal getCountGoodsAmount() {
		return countGoodsAmount;
	}

	public void setCountGoodsAmount(BigDecimal countGoodsAmount) {
		this.countGoodsAmount = countGoodsAmount;
	}

	public BigDecimal getTotalPriceCountAmout() {
		return totalPriceCountAmout;
	}

	public void setTotalPriceCountAmout(BigDecimal totalPriceCountAmout) {
		this.totalPriceCountAmout = totalPriceCountAmout;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	public BigDecimal getAfterDiscountAmount() {
		return afterDiscountAmount;
	}

	public void setAfterDiscountAmount(BigDecimal afterDiscountAmount) {
		this.afterDiscountAmount = afterDiscountAmount;
	}

	public Integer getRealCurrencyType() {
		return realCurrencyType;
	}

	public void setRealCurrencyType(Integer realCurrencyType) {
		this.realCurrencyType = realCurrencyType;
	}

	public String getRealCurrencyTypeName() {
		return realCurrencyTypeName;
	}

	public void setRealCurrencyTypeName(String realCurrencyTypeName) {
		this.realCurrencyTypeName = realCurrencyTypeName;
	}

	public BigDecimal getSumDiscountAmount() {
		return sumDiscountAmount;
	}

	public void setSumDiscountAmount(BigDecimal sumDiscountAmount) {
		this.sumDiscountAmount = sumDiscountAmount;
	}

	public BigDecimal getSumRateDisAmount() {
		return sumRateDisAmount;
	}

	public void setSumRateDisAmount(BigDecimal sumRateDisAmount) {
		this.sumRateDisAmount = sumRateDisAmount;
	}

	public BigDecimal getRateDisAmount() {
		return rateDisAmount;
	}

	public void setRateDisAmount(BigDecimal rateDisAmount) {
		this.rateDisAmount = rateDisAmount;
	}

	public BigDecimal getDeductionPrice() {
		return deductionPrice;
	}

	public void setDeductionPrice(BigDecimal deductionPrice) {
		this.deductionPrice = deductionPrice;
	}

	public BigDecimal getAfterDeductionMoney() {
		return afterDeductionMoney;
	}

	public void setAfterDeductionMoney(BigDecimal afterDeductionMoney) {
		this.afterDeductionMoney = afterDeductionMoney;
	}
}
