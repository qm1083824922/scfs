package com.scfs.domain.logistics.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by Administrator on 2016年11月29日.
 */
public class BillInStoreDtlExt extends BillInStoreDtl {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3044617027118303368L;
	/************* 入库单信息 ****************/
	/**
	 * 入库编号
	 */
	private String tBillNo;
	/**
	 * 入库类型, 1-采购入库 2-调拨入库
	 */
	private Integer tBillType;
	/**
	 * 入库附属编号
	 */
	private String tAffiliateNo;
	/**
	 * 项目ID
	 */
	private Integer tProjectId;
	/**
	 * 供应商ID
	 */
	private Integer tSupplierId;
	/**
	 * 仓库ID
	 */
	private Integer tWarehouseId;
	/**
	 * 客户ID
	 */
	private Integer tCustomerId;
	/**
	 * 状态 1-待提交 2-已收货
	 */
	private Integer tStatus;
	/**
	 * 到货日期
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tReceiveDate;
	/**
	 * 到货数量
	 */
	private BigDecimal tReceiveNum;
	/**
	 * 收货金额
	 */
	private BigDecimal tReceiveAmount;
	/**
	 * 理货数量
	 */
	private BigDecimal tTallyNum;
	/**
	 * 理货金额
	 */
	private BigDecimal tTallyAmount;
	/**
	 * 入库人ID
	 */
	private Integer tAcceptorId;
	/**
	 * 入库人
	 */
	private String tAcceptor;
	/**
	 * 入库时间
	 */
	private Date tAcceptTime;
	/**
	 * 关联出库单ID
	 */
	private Integer tBillOutStoreId;
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

	public Integer gettSupplierId() {
		return tSupplierId;
	}

	public void settSupplierId(Integer tSupplierId) {
		this.tSupplierId = tSupplierId;
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

	public Date gettReceiveDate() {
		return tReceiveDate;
	}

	public void settReceiveDate(Date tReceiveDate) {
		this.tReceiveDate = tReceiveDate;
	}

	public BigDecimal gettReceiveNum() {
		return tReceiveNum;
	}

	public void settReceiveNum(BigDecimal tReceiveNum) {
		this.tReceiveNum = tReceiveNum;
	}

	public BigDecimal gettReceiveAmount() {
		return tReceiveAmount;
	}

	public void settReceiveAmount(BigDecimal tReceiveAmount) {
		this.tReceiveAmount = tReceiveAmount;
	}

	public BigDecimal gettTallyNum() {
		return tTallyNum;
	}

	public void settTallyNum(BigDecimal tTallyNum) {
		this.tTallyNum = tTallyNum;
	}

	public BigDecimal gettTallyAmount() {
		return tTallyAmount;
	}

	public void settTallyAmount(BigDecimal tTallyAmount) {
		this.tTallyAmount = tTallyAmount;
	}

	public Integer gettAcceptorId() {
		return tAcceptorId;
	}

	public void settAcceptorId(Integer tAcceptorId) {
		this.tAcceptorId = tAcceptorId;
	}

	public String gettAcceptor() {
		return tAcceptor;
	}

	public void settAcceptor(String tAcceptor) {
		this.tAcceptor = tAcceptor;
	}

	public Date gettAcceptTime() {
		return tAcceptTime;
	}

	public void settAcceptTime(Date tAcceptTime) {
		this.tAcceptTime = tAcceptTime;
	}

	public Integer gettBillOutStoreId() {
		return tBillOutStoreId;
	}

	public void settBillOutStoreId(Integer tBillOutStoreId) {
		this.tBillOutStoreId = tBillOutStoreId;
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

}
