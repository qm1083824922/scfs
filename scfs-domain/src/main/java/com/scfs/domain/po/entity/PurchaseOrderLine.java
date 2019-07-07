package com.scfs.domain.po.entity;

import com.scfs.domain.base.entity.BaseEntity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/10/14. 订单行信息
 */
public class PurchaseOrderLine extends BaseEntity {

	private static final long serialVersionUID = 2030190203649624781L;

	/** 采购单ID */
	private Integer poId;
	/** 商品ID */
	private Integer goodsId;
	/** 商品编号 */
	private String goodsNo;
	/** 商品数量 */
	private BigDecimal goodsNum;
	/** 商品单价 */
	private BigDecimal goodsPrice;
	/** 金额=数量*单价 */
	private BigDecimal amount;
	/** 入库数量 */
	private BigDecimal storageNum;
	/** 收货单价 */
	private BigDecimal originGoodsPrice;
	/** 成本单价 */
	private BigDecimal costPrice;
	/** 批次号 */
	private String batchNum;
	/** 收票数量 */
	private BigDecimal invoiceNum;
	/** 收票金额 */
	private BigDecimal invoiceAmount;
	/** 已付款金额 */
	private BigDecimal paidAmount;
	/** 付款时间 **/
	private Date payTime;
	/** 付款单价 **/
	private BigDecimal payPrice;
	/** 付款汇率 **/
	private BigDecimal payRate;
	/** 应发货单价 **/
	private BigDecimal requiredSendPrice;
	/** 折扣金额 **/
	private BigDecimal discountAmount;
	/** 折扣单价 **/
	private BigDecimal discountPrice;
	/** 入库单ID **/
	private Integer billInStoreId;
	/** 入库单明细ID **/
	private Integer billInStoreDtlId;
	/** 入库单理货明细ID **/
	private Integer billInStoreTallyDtlId;
	/** 采购单价 */
	private BigDecimal poPrice;
	/** 商品状态 */
	private Integer goodsStatus;
	/** 库存id */
	private Integer stlId;
	/** 发货数量 */
	private BigDecimal sendNum;
	/** 发货金额 */
	private BigDecimal sendAmount;
	/** 退货数量 */
	private BigDecimal returnNum;
	/** 退货金额 */
	private BigDecimal returnAmount;
	/** 质押比例 */
	private BigDecimal pledgeProportion;
	/** 送货单号 */
	private String purchaseDeliverySn;
	/** 可发货数量 */
	private BigDecimal remainSendNum;
	/** 铺货入库时间 **/
	private Date stockinTime;
	/** 铺货采购单ID */
	private Integer distributeId;
	/** 铺货已请款数量 */
	private BigDecimal distributeNum;
	/** 铺货待请款数量 */
	private BigDecimal waitDistributeNum;
	/** 抵扣后金额 **/
	private BigDecimal deductionMoney;
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
	/** 付款实际支付币种 **/
	private Integer payRealCurrency;

	public Integer getPayRealCurrency() {
		return payRealCurrency;
	}

	public void setPayRealCurrency(Integer payRealCurrency) {
		this.payRealCurrency = payRealCurrency;
	}

	public BigDecimal getDeductionMoney() {
		return deductionMoney;
	}

	public void setDeductionMoney(BigDecimal deductionMoney) {
		this.deductionMoney = deductionMoney;
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

	public BigDecimal getStorageNum() {
		return storageNum;
	}

	public void setStorageNum(BigDecimal storageNum) {
		this.storageNum = storageNum;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(BigDecimal invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
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

	public BigDecimal getRequiredSendPrice() {
		return requiredSendPrice;
	}

	public void setRequiredSendPrice(BigDecimal requiredSendPrice) {
		this.requiredSendPrice = requiredSendPrice;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(BigDecimal discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Integer getBillInStoreId() {
		return billInStoreId;
	}

	public void setBillInStoreId(Integer billInStoreId) {
		this.billInStoreId = billInStoreId;
	}

	public Integer getBillInStoreDtlId() {
		return billInStoreDtlId;
	}

	public void setBillInStoreDtlId(Integer billInStoreDtlId) {
		this.billInStoreDtlId = billInStoreDtlId;
	}

	public Integer getBillInStoreTallyDtlId() {
		return billInStoreTallyDtlId;
	}

	public void setBillInStoreTallyDtlId(Integer billInStoreTallyDtlId) {
		this.billInStoreTallyDtlId = billInStoreTallyDtlId;
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

	public BigDecimal getOriginGoodsPrice() {
		return originGoodsPrice;
	}

	public void setOriginGoodsPrice(BigDecimal originGoodsPrice) {
		this.originGoodsPrice = originGoodsPrice;
	}

	public BigDecimal getSendNum() {
		return sendNum;
	}

	public void setSendNum(BigDecimal sendNum) {
		this.sendNum = sendNum;
	}

	public BigDecimal getSendAmount() {
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
	}

	public BigDecimal getReturnNum() {
		return returnNum;
	}

	public void setReturnNum(BigDecimal returnNum) {
		this.returnNum = returnNum;
	}

	public BigDecimal getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(BigDecimal returnAmount) {
		this.returnAmount = returnAmount;
	}

	public BigDecimal getPledgeProportion() {
		return pledgeProportion;
	}

	public void setPledgeProportion(BigDecimal pledgeProportion) {
		this.pledgeProportion = pledgeProportion;
	}

	public String getPurchaseDeliverySn() {
		return purchaseDeliverySn;
	}

	public void setPurchaseDeliverySn(String purchaseDeliverySn) {
		this.purchaseDeliverySn = purchaseDeliverySn;
	}

	public BigDecimal getRemainSendNum() {
		return remainSendNum;
	}

	public void setRemainSendNum(BigDecimal remainSendNum) {
		this.remainSendNum = remainSendNum;
	}

	public Date getStockinTime() {
		return stockinTime;
	}

	public void setStockinTime(Date stockinTime) {
		this.stockinTime = stockinTime;
	}

	public Integer getDistributeId() {
		return distributeId;
	}

	public void setDistributeId(Integer distributeId) {
		this.distributeId = distributeId;
	}

	public BigDecimal getDistributeNum() {
		return distributeNum;
	}

	public void setDistributeNum(BigDecimal distributeNum) {
		this.distributeNum = distributeNum;
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

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
	}

	public BigDecimal getWaitDistributeNum() {
		return waitDistributeNum;
	}

	public void setWaitDistributeNum(BigDecimal waitDistributeNum) {
		this.waitDistributeNum = waitDistributeNum;
	}

}
