package com.scfs.domain.sale.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.domain.MoneySerializer;
import com.scfs.domain.NumSerializer;
import com.scfs.domain.PriceSerializer;

/**
 * Created by Administrator on 2016年10月27日.
 */
public class BillDeliveryDtlResDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2236703002729896005L;
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
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal requiredSendNum;

	/**
	 * 应发货单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
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
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal costPrice;

	/**
	 * 订单单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal poPrice;

	/**
	 * 开票数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal provideInvoiceNum;

	/**
	 * 开票金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal provideInvoiceAmount;

	/**
	 * 收票数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal acceptInvoiceNum;

	/**
	 * 收票金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

	/*************************************************/
	/**
	 * 发货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date requiredSendDate;
	/**
	 * 销售日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deliveryDate;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 销售编号
	 */
	private String billNo;
	/**
	 * 商品编号
	 */
	private String goodsNo;

	/**************************************************/
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
	 * 库存状态 1-常规 2-残次品
	 */
	private String goodsStatusName;
	/**
	 * 应发货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal requiredSendAmount;

	/**
	 * 指定库存标志名称
	 */
	private String assignStlFlagName;
	/**
	 * 可用库存数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal availableNum;
	/**
	 * 可退货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal availableReturnNum;
	/**
	 * 应发货单价(字符串)
	 */
	private String requiredSendPriceStr;
	private String requiredSendPriceDouble;
	/**
	 * 应发货金额(字符串)
	 */
	private String requiredSendAmountStr;
	/**
	 * 库存单价(字符串)
	 */
	private String costPriceStr;
	/**
	 * 原发货单价(退货)(字符串)
	 */
	private String originSendPriceStr;
	/**
	 * 资金占用金额-付款金额
	 */
	private BigDecimal payAmount;
	/**
	 * 资金占用金额-付款金额(字符串)
	 */
	private String payAmountStr;
	/**
	 * 资金占用天数
	 */
	private Integer payDays;
	/**
	 * 服务费率
	 */
	private BigDecimal fundMonthRate;
	/**
	 * 违约金费率
	 */
	private BigDecimal dayPenalRate;
	/**
	 * 服务金额
	 */
	private BigDecimal serviceAmount;
	/**
	 * 服务金额(字符串)
	 */
	private String serviceAmountStr;
	/**
	 * 其他费用金额
	 */
	private BigDecimal otherAmount;
	/**
	 * 其他费用金额(字符串)
	 */
	private String otherAmountStr;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 回款日期
	 */
	private Date returnTime;
	/** 付款单汇率 **/
	private BigDecimal payRate;
	/** 根据汇率转换后单价 **/
	private BigDecimal payCurtPrice;
	/** 根据汇率转换后金额 **/
	private BigDecimal payCurtAmount;
	/** 根据汇率转换后总金额 **/
	private BigDecimal countPayCurtAmount;
	/** 付款单实际支付币种 **/
	private Integer payRealCurrency;

	public Integer getPayRealCurrency() {
		return payRealCurrency;
	}

	public void setPayRealCurrency(Integer payRealCurrency) {
		this.payRealCurrency = payRealCurrency;
	}

	public BigDecimal getCountPayCurtAmount() {
		return countPayCurtAmount;
	}

	public void setCountPayCurtAmount(BigDecimal countPayCurtAmount) {
		this.countPayCurtAmount = countPayCurtAmount;
	}

	public BigDecimal getPayCurtPrice() {
		return payCurtPrice;
	}

	public void setPayCurtPrice(BigDecimal payCurtPrice) {
		this.payCurtPrice = payCurtPrice;
	}

	public BigDecimal getPayCurtAmount() {
		return payCurtAmount;
	}

	public void setPayCurtAmount(BigDecimal payCurtAmount) {
		this.payCurtAmount = payCurtAmount;
	}

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
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

	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

	public BigDecimal getRequiredSendAmount() {
		return DecimalUtil.multiply(null == this.getRequiredSendNum() ? BigDecimal.ZERO : this.getRequiredSendNum(),
				null == this.getRequiredSendPrice() ? BigDecimal.ZERO : this.getRequiredSendPrice());
	}

	public void setRequiredSendAmount(BigDecimal requiredSendAmount) {
		this.requiredSendAmount = requiredSendAmount;
	}

	public String getAssignStlFlagName() {
		return assignStlFlagName;
	}

	public void setAssignStlFlagName(String assignStlFlagName) {
		this.assignStlFlagName = assignStlFlagName;
	}

	public BigDecimal getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(BigDecimal availableNum) {
		this.availableNum = availableNum;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}

	public String getRequiredSendPriceStr() {
		return requiredSendPriceStr;
	}

	public void setRequiredSendPriceStr(String requiredSendPriceStr) {
		this.requiredSendPriceStr = requiredSendPriceStr;
	}

	public String getRequiredSendAmountStr() {
		return requiredSendAmountStr;
	}

	public void setRequiredSendAmountStr(String requiredSendAmountStr) {
		this.requiredSendAmountStr = requiredSendAmountStr;
	}

	public String getCostPriceStr() {
		return costPriceStr;
	}

	public void setCostPriceStr(String costPriceStr) {
		this.costPriceStr = costPriceStr;
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
		this.batchNo = batchNo;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public BigDecimal getPoPrice() {
		return poPrice;
	}

	public void setPoPrice(BigDecimal poPrice) {
		this.poPrice = poPrice;
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

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayAmountStr() {
		return payAmountStr;
	}

	public void setPayAmountStr(String payAmountStr) {
		this.payAmountStr = payAmountStr;
	}

	public Integer getPayDays() {
		return payDays;
	}

	public void setPayDays(Integer payDays) {
		this.payDays = payDays;
	}

	public BigDecimal getFundMonthRate() {
		return fundMonthRate;
	}

	public void setFundMonthRate(BigDecimal fundMonthRate) {
		this.fundMonthRate = fundMonthRate;
	}

	public BigDecimal getDayPenalRate() {
		return dayPenalRate;
	}

	public void setDayPenalRate(BigDecimal dayPenalRate) {
		this.dayPenalRate = dayPenalRate;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public String getServiceAmountStr() {
		return serviceAmountStr;
	}

	public void setServiceAmountStr(String serviceAmountStr) {
		this.serviceAmountStr = serviceAmountStr;
	}

	public BigDecimal getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(BigDecimal otherAmount) {
		this.otherAmount = otherAmount;
	}

	public String getOtherAmountStr() {
		return otherAmountStr;
	}

	public void setOtherAmountStr(String otherAmountStr) {
		this.otherAmountStr = otherAmountStr;
	}

	public BigDecimal getProfitPrice() {
		return profitPrice;
	}

	public void setProfitPrice(BigDecimal profitPrice) {
		this.profitPrice = profitPrice;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getRequiredSendPriceDouble() {
		return requiredSendPriceDouble;
	}

	public void setRequiredSendPriceDouble(String requiredSendPriceDouble) {
		this.requiredSendPriceDouble = requiredSendPriceDouble;
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

	public String getOriginSendPriceStr() {
		return originSendPriceStr;
	}

	public void setOriginSendPriceStr(String originSendPriceStr) {
		this.originSendPriceStr = originSendPriceStr;
	}

	public BigDecimal getAvailableReturnNum() {
		return availableReturnNum;
	}

	public void setAvailableReturnNum(BigDecimal availableReturnNum) {
		this.availableReturnNum = availableReturnNum;
	}

}
