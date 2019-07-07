package com.scfs.domain.logistics.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class BillOutStorePickDtlExt extends BillOutStorePickDtl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7524468436379152193L;

	/***************** 出库单信息 *******************/
	/**
	 * 出库编号
	 */
	private String tBillNo;
	/**
	 * 出库类型 1-销售出库 2-调拨出库
	 */
	private Integer tBillType;
	/**
	 * 出库附属编号
	 */
	private String tAffiliateNo;
	/**
	 * 项目ID
	 */
	private Integer tProjectId;
	/**
	 * 仓库ID
	 */
	private Integer tWarehouseId;
	/**
	 * 客户ID
	 */
	private Integer tCustomerId;
	/**
	 * 接收仓库ID
	 */
	private Integer tReceiveWarehouseId;
	/**
	 * 状态 0-待提交 1-已发货
	 */
	private Integer tStatus;
	/**
	 * 要求发货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tRequiredSendDate;
	/**
	 * 发货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tSendDate;
	/**
	 * 销售单ID
	 */
	private Integer tBillDeliveryId;
	/**
	 * 发货数量
	 */
	private BigDecimal tSendNum;
	/**
	 * 发货金额
	 */
	private BigDecimal tSendAmount;
	/**
	 * 拣货数量
	 */
	private BigDecimal tPickupNum;
	/**
	 * 拣货金额
	 */
	private BigDecimal tPickupAmount;
	/**
	 * 成本金额
	 */
	private BigDecimal tCostAmount;
	/**
	 * 订单金额
	 */
	private BigDecimal tPoAmount;
	/**
	 * 出库客户地址ID
	 */
	private Integer tCustomerAddressId;
	/**
	 * 运输方式 1-自提
	 */
	private Integer tTransferMode;
	/**
	 * 出库人ID
	 */
	private Integer tDeliverId;
	/**
	 * 出库人
	 */
	private String tDeliverer;
	/**
	 * 出库时间
	 */
	private Date tDeliverTime;
	/**
	 * 币种 1-人民币 2-美元 3-港元
	 */
	private Integer tCurrencyType;
	/**
	 * 汇率
	 */
	private BigDecimal tExchangeRate;
	/**
	 * 原因
	 */
	private Integer tReasonType;
	/**
	 * 调用WMS接口 0-未调用 1-已调用
	 */
	private Integer tWmsStatus;
	/**
	 * 备注
	 */
	private String tRemark;
	/**
	 * 打印次数
	 */
	private Integer tPrintNum;

	public String gettBillNo() {
		return tBillNo;
	}

	public void settBillNo(String tBillNo) {
		this.tBillNo = tBillNo;
	}

	public Integer gettBillType() {
		return tBillType;
	}

	public void settBillType(Integer tBillType) {
		this.tBillType = tBillType;
	}

	public String gettAffiliateNo() {
		return tAffiliateNo;
	}

	public void settAffiliateNo(String tAffiliateNo) {
		this.tAffiliateNo = tAffiliateNo;
	}

	public Integer gettProjectId() {
		return tProjectId;
	}

	public void settProjectId(Integer tProjectId) {
		this.tProjectId = tProjectId;
	}

	public Integer gettWarehouseId() {
		return tWarehouseId;
	}

	public void settWarehouseId(Integer tWarehouseId) {
		this.tWarehouseId = tWarehouseId;
	}

	public Integer gettCustomerId() {
		return tCustomerId;
	}

	public void settCustomerId(Integer tCustomerId) {
		this.tCustomerId = tCustomerId;
	}

	public Integer gettReceiveWarehouseId() {
		return tReceiveWarehouseId;
	}

	public void settReceiveWarehouseId(Integer tReceiveWarehouseId) {
		this.tReceiveWarehouseId = tReceiveWarehouseId;
	}

	public Integer gettStatus() {
		return tStatus;
	}

	public void settStatus(Integer tStatus) {
		this.tStatus = tStatus;
	}

	public Date gettRequiredSendDate() {
		return tRequiredSendDate;
	}

	public void settRequiredSendDate(Date tRequiredSendDate) {
		this.tRequiredSendDate = tRequiredSendDate;
	}

	public Date gettSendDate() {
		return tSendDate;
	}

	public void settSendDate(Date tSendDate) {
		this.tSendDate = tSendDate;
	}

	public Integer gettBillDeliveryId() {
		return tBillDeliveryId;
	}

	public void settBillDeliveryId(Integer tBillDeliveryId) {
		this.tBillDeliveryId = tBillDeliveryId;
	}

	public BigDecimal gettSendNum() {
		return tSendNum;
	}

	public void settSendNum(BigDecimal tSendNum) {
		this.tSendNum = tSendNum;
	}

	public BigDecimal gettSendAmount() {
		return tSendAmount;
	}

	public void settSendAmount(BigDecimal tSendAmount) {
		this.tSendAmount = tSendAmount;
	}

	public BigDecimal gettPickupNum() {
		return tPickupNum;
	}

	public void settPickupNum(BigDecimal tPickupNum) {
		this.tPickupNum = tPickupNum;
	}

	public BigDecimal gettPickupAmount() {
		return tPickupAmount;
	}

	public void settPickupAmount(BigDecimal tPickupAmount) {
		this.tPickupAmount = tPickupAmount;
	}

	public BigDecimal gettCostAmount() {
		return tCostAmount;
	}

	public void settCostAmount(BigDecimal tCostAmount) {
		this.tCostAmount = tCostAmount;
	}

	public BigDecimal gettPoAmount() {
		return tPoAmount;
	}

	public void settPoAmount(BigDecimal tPoAmount) {
		this.tPoAmount = tPoAmount;
	}

	public Integer gettCustomerAddressId() {
		return tCustomerAddressId;
	}

	public void settCustomerAddressId(Integer tCustomerAddressId) {
		this.tCustomerAddressId = tCustomerAddressId;
	}

	public Integer gettTransferMode() {
		return tTransferMode;
	}

	public void settTransferMode(Integer tTransferMode) {
		this.tTransferMode = tTransferMode;
	}

	public Integer gettDeliverId() {
		return tDeliverId;
	}

	public void settDeliverId(Integer tDeliverId) {
		this.tDeliverId = tDeliverId;
	}

	public String gettDeliverer() {
		return tDeliverer;
	}

	public void settDeliverer(String tDeliverer) {
		this.tDeliverer = tDeliverer;
	}

	public Date gettDeliverTime() {
		return tDeliverTime;
	}

	public void settDeliverTime(Date tDeliverTime) {
		this.tDeliverTime = tDeliverTime;
	}

	public Integer gettCurrencyType() {
		return tCurrencyType;
	}

	public void settCurrencyType(Integer tCurrencyType) {
		this.tCurrencyType = tCurrencyType;
	}

	public BigDecimal gettExchangeRate() {
		return tExchangeRate;
	}

	public void settExchangeRate(BigDecimal tExchangeRate) {
		this.tExchangeRate = tExchangeRate;
	}

	public Integer gettReasonType() {
		return tReasonType;
	}

	public void settReasonType(Integer tReasonType) {
		this.tReasonType = tReasonType;
	}

	public Integer gettWmsStatus() {
		return tWmsStatus;
	}

	public void settWmsStatus(Integer tWmsStatus) {
		this.tWmsStatus = tWmsStatus;
	}

	public String gettRemark() {
		return tRemark;
	}

	public void settRemark(String tRemark) {
		this.tRemark = tRemark;
	}

	public Integer gettPrintNum() {
		return tPrintNum;
	}

	public void settPrintNum(Integer tPrintNum) {
		this.tPrintNum = tPrintNum;
	}

}