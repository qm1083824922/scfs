package com.scfs.domain.sale.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BillDeliveryDtlExt extends BillDeliveryDtl {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3950415108504041927L;
	/******************* 销售单信息 **********************/

	/**
	 * 销售单编号
	 */
	private String tBillNo;

	/**
	 * 销售类型 1-销售销售 2-借货销售
	 */
	private Integer tBillType;

	/**
	 * 销售附属编号
	 */
	private String tAffiliateNo;

	/**
	 * 项目ID,关联tb_base_subject[id]
	 */
	private Integer tProjectId;

	/**
	 * 仓库ID,关联tb_base_subject[id]
	 */
	private Integer tWarehouseId;

	/**
	 * 客户ID,关联tb_base_subject[id]
	 */
	private Integer tCustomerId;

	/**
	 * 状态 1-待提交 2-已提交
	 */
	private Integer tStatus;

	/**
	 * 销售单日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tDeliveryDate;

	/**
	 * 应发货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tRequiredSendDate;

	/**
	 * 应发货数量
	 */
	private BigDecimal tRequiredSendNum;

	/**
	 * 应发货金额
	 */
	private BigDecimal tRequiredSendAmount;

	/**
	 * 成本金额
	 */
	private BigDecimal tCostAmount;

	/**
	 * 订单金额
	 */
	private BigDecimal tPoAmount;

	/**
	 * 客户地址ID
	 */
	private Integer tCustomerAddressId;

	/**
	 * 运输方式 1-自提
	 */
	private Integer tTransferMode;

	/**
	 * 币种 1-人民币 2-美元
	 */
	private Integer tCurrencyType;

	/**
	 * 汇率
	 */
	private BigDecimal tExchangeRate;
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
	/**
	 * 回款日期
	 */
	private Date tReturnTime;

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

	public Integer gettStatus() {
		return tStatus;
	}

	public void settStatus(Integer tStatus) {
		this.tStatus = tStatus;
	}

	public Date gettDeliveryDate() {
		return tDeliveryDate;
	}

	public void settDeliveryDate(Date tDeliveryDate) {
		this.tDeliveryDate = tDeliveryDate;
	}

	public Date gettRequiredSendDate() {
		return tRequiredSendDate;
	}

	public void settRequiredSendDate(Date tRequiredSendDate) {
		this.tRequiredSendDate = tRequiredSendDate;
	}

	public BigDecimal gettRequiredSendNum() {
		return tRequiredSendNum;
	}

	public void settRequiredSendNum(BigDecimal tRequiredSendNum) {
		this.tRequiredSendNum = tRequiredSendNum;
	}

	public BigDecimal gettRequiredSendAmount() {
		return tRequiredSendAmount;
	}

	public void settRequiredSendAmount(BigDecimal tRequiredSendAmount) {
		this.tRequiredSendAmount = tRequiredSendAmount;
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

	public Date gettReturnTime() {
		return tReturnTime;
	}

	public void settReturnTime(Date tReturnTime) {
		this.tReturnTime = tReturnTime;
	}

}