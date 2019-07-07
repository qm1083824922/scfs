package com.scfs.domain.po.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PurchaseReturnDtl implements Serializable {

	private static final long serialVersionUID = -3969408639672822018L;
	/** 采购单编号 */
	private Integer poId;
	/** 入库明细单id */
	private Integer id;
	/** 入库编号 */
	private String billNo;
	/** 入库类型, 1-采购入库 2-调拨入库 */
	private Integer billType;
	/** 入库附属编号 */
	private String affiliateNo;
	/** 项目ID */
	private Integer projectId;
	/** 项目ID */
	private String projectName;
	/** 供应商ID */
	private Integer supplierId;
	private String supplierName;

	/** 仓库ID */
	private Integer warehouseId;
	private String warehouseName;

	/** 客户ID */
	private Integer customerId;
	private String customerName;

	/** 到货日期 */
	private Date receiveDate;
	/** 收货单价 */
	private BigDecimal receivePrice;
	/** 收货数量 */
	private BigDecimal tallyNum;
	/** 退货数量 */
	private BigDecimal returnNum;
	/** 成本单价 */
	private BigDecimal costPrice;
	/** 采购单价 */
	private BigDecimal poPrice;
	/** 商品状态 */
	private Integer goodsStatus;
	/** 库存id */
	private Integer stlId;
	/** 退货单价 */
	private BigDecimal returnPrice;
	/** 入库人ID */
	private Integer acceptorId;
	/** 入库人 */
	private String acceptor;
	/** 入库时间 */
	private Date acceptTime;
	/** 入库单明细id */
	private Integer billInStoreDtlId;
	private Integer billInStoreTallyDtlId;
	/** 关联出库单ID */
	private Integer billOutStoreId;
	/** 币种 1-人民币 2-美元 */
	private Integer currencyType;
	private String currencyTypeName;
	/** 汇率 */
	private BigDecimal exchangeRate;
	/** 调用WMS接口 0-未调用 1-已调用 */
	private Integer wmsStatus;
	/** 备注 */
	private String remark;
	/** 打印次数 */
	private Integer printNum;
	/** 创建人ID */
	private Integer creatorId;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private Date createAt;
	/** 更新时间 */
	private Date updateAt;
	/** 付款金额 */
	private BigDecimal payAmount;
	/** 订单编号 */
	private String orderNo;
	/** 订单附属编号 */
	private String appendNo;
	/** 商品ID */
	private Integer goodsId;
	/** 商品名称 */
	private String goodsName;
	/** 商品编号 */
	private String goodsNumber;
	/** 商品型号 */
	private String goodsType;
	/** 商品单位 */
	private String goodsUnit;
	/** 商品条码 */
	private String goodsBarCode;
	/** 商品规格 */
	private String specification;
	/** 收货金额 */
	private BigDecimal receiveAmount;
	/** 批次号 */
	private String batchNo;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date orderTime;
	/** 付款时间 **/
	private Date payTime;
	/** 付款单价 **/
	private BigDecimal payPrice;
	/** 付款汇率 **/
	private BigDecimal payRate;
	/** 付款实际支付币种 **/
	private Integer payRealCurrency;
	/**
	 * 库存状态
	 */
	private String goodsStatusName;

	public BigDecimal getTallyNum() {
		return tallyNum;
	}

	public void setTallyNum(BigDecimal tallyNum) {
		this.tallyNum = tallyNum;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
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

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public BigDecimal getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(BigDecimal returnNum) {
		this.returnNum = returnNum;
	}

	public Integer getAcceptorId() {
		return acceptorId;
	}

	public void setAcceptorId(Integer acceptorId) {
		this.acceptorId = acceptorId;
	}

	public String getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	public Integer getBillInStoreDtlId() {
		return billInStoreDtlId;
	}

	public void setBillInStoreDtlId(Integer billInStoreDtlId) {
		this.billInStoreDtlId = billInStoreDtlId;
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

	public Integer getWmsStatus() {
		return wmsStatus;
	}

	public void setWmsStatus(Integer wmsStatus) {
		this.wmsStatus = wmsStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
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

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
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

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public BigDecimal getReturnPrice() {
		return returnPrice;
	}

	public void setReturnPrice(BigDecimal returnPrice) {
		this.returnPrice = returnPrice;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
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

	public Integer getStlId() {
		return stlId;
	}

	public void setStlId(Integer stlId) {
		this.stlId = stlId;
	}

	public Integer getBillInStoreTallyDtlId() {
		return billInStoreTallyDtlId;
	}

	public void setBillInStoreTallyDtlId(Integer billInStoreTallyDtlId) {
		this.billInStoreTallyDtlId = billInStoreTallyDtlId;
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

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public BigDecimal getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(BigDecimal payPrice) {
		this.payPrice = payPrice;
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
