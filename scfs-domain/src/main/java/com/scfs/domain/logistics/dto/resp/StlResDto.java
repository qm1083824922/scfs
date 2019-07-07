package com.scfs.domain.logistics.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.MoneySerializer;
import com.scfs.domain.NumSerializer;
import com.scfs.domain.PriceSerializer;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2016年10月19日.
 */
public class StlResDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3187569714976512219L;
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
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal tallyNum;
	/**
	 * 批次
	 */
	private String batchNo;
	private String specification;

	/**
	 * 状态 1-正常 2-残次品
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
	 * 入库数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal inStoreNum;
	/**
	 * 库存数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal storeNum;
	/**
	 * 出库锁定数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal lockNum;
	/**
	 * 销售锁定数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal saleLockNum;
	/**
	 * 关联库存ID
	 */
	private Integer stlId;
	/**
	 * 原入库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date originAcceptTime;
	/**
	 * 入库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date acceptTime;
	/**
	 * 收货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date payTime;
	/**
	 * 飞单表示 0-否 1-是
	 */
	private Integer flyOrderFlag;

	/*****************************************/

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

	/**
	 * 状态 1-正常 2-残次品
	 */
	private String goodsStatusName;
	/**
	 * 可用数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal availableNum;
	/**
	 * 可用金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal availableAmount;
	/**
	 * 库存金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal storeAmount;
	/**
	 * 出库锁定金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal lockAmount;
	/**
	 * 销售锁定金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal saleLockAmount;
	/**
	 * 拣货数量
	 */
	private BigDecimal pickupNum;
	/**
	 * 币种
	 */
	private String currencyTypeName;
	/**
	 * 库存金额(字符串)
	 */
	private String storeAmountStr;
	/**
	 * 出库锁定金额(字符串)
	 */
	private String lockAmountStr;
	/**
	 * 销售锁定金额(字符串)
	 */
	private String saleLockAmountStr;
	/**
	 * 可用金额(字符串)
	 */
	private String availableAmountStr;
	/**
	 * 成本单价(字符串)
	 */
	private String costPriceStr;
	/**
	 * 订单单价(字符串)
	 */
	private String poPriceStr;
	/**
	 * 选择库存默认值数量
	 */
	private BigDecimal defaultAvailableNum;
	/**
	 * 选择库存默认值单价
	 */
	private BigDecimal defaultPrice;

	private BigDecimal availableModel;
	private String availableModelStr;

	/**
	 * CNY汇率
	 */
	private BigDecimal cnyRate;
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

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_STL);
			operMap.put(OperateConsts.SPLIT, BusUrlConsts.SPLIT_STL);
		}
	}

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

	public Integer getBillInStoreId() {
		return billInStoreId;
	}

	public void setBillInStoreId(Integer billInStoreId) {
		this.billInStoreId = billInStoreId;
	}

	public BigDecimal getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(BigDecimal availableNum) {
		this.availableNum = availableNum;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getStoreAmount() {
		return storeAmount;
	}

	public void setStoreAmount(BigDecimal storeAmount) {
		this.storeAmount = storeAmount;
	}

	public BigDecimal getLockAmount() {
		return lockAmount;
	}

	public void setLockAmount(BigDecimal lockAmount) {
		this.lockAmount = lockAmount;
	}

	public BigDecimal getPickupNum() {
		return pickupNum;
	}

	public void setPickupNum(BigDecimal pickupNum) {
		this.pickupNum = pickupNum;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getStoreAmountStr() {
		return storeAmountStr;
	}

	public void setStoreAmountStr(String storeAmountStr) {
		this.storeAmountStr = storeAmountStr;
	}

	public String getLockAmountStr() {
		return lockAmountStr;
	}

	public void setLockAmountStr(String lockAmountStr) {
		this.lockAmountStr = lockAmountStr;
	}

	public String getAvailableAmountStr() {
		return availableAmountStr;
	}

	public void setAvailableAmountStr(String availableAmountStr) {
		this.availableAmountStr = availableAmountStr;
	}

	public String getCostPriceStr() {
		return costPriceStr;
	}

	public void setCostPriceStr(String costPriceStr) {
		this.costPriceStr = costPriceStr;
	}

	public String getPoPriceStr() {
		return poPriceStr;
	}

	public void setPoPriceStr(String poPriceStr) {
		this.poPriceStr = poPriceStr;
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

	public BigDecimal getDefaultAvailableNum() {
		return defaultAvailableNum;
	}

	public void setDefaultAvailableNum(BigDecimal defaultAvailableNum) {
		this.defaultAvailableNum = defaultAvailableNum;
	}

	public BigDecimal getDefaultPrice() {
		return defaultPrice;
	}

	public void setDefaultPrice(BigDecimal defaultPrice) {
		this.defaultPrice = defaultPrice;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
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
		return saleLockAmount;
	}

	public void setSaleLockAmount(BigDecimal saleLockAmount) {
		this.saleLockAmount = saleLockAmount;
	}

	public String getSaleLockAmountStr() {
		return saleLockAmountStr;
	}

	public void setSaleLockAmountStr(String saleLockAmountStr) {
		this.saleLockAmountStr = saleLockAmountStr;
	}

	public BigDecimal getAvailableModel() {
		return availableModel;
	}

	public void setAvailableModel(BigDecimal availableModel) {
		this.availableModel = availableModel;
	}

	public String getAvailableModelStr() {
		return availableModelStr;
	}

	public void setAvailableModelStr(String availableModelStr) {
		this.availableModelStr = availableModelStr;
	}

	public BigDecimal getCnyRate() {
		return cnyRate;
	}

	public void setCnyRate(BigDecimal cnyRate) {
		this.cnyRate = cnyRate;
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

	public Integer getFlyOrderFlag() {
		return flyOrderFlag;
	}

	public void setFlyOrderFlag(Integer flyOrderFlag) {
		this.flyOrderFlag = flyOrderFlag;
	}

}
