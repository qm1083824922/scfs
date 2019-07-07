package com.scfs.domain.logistics.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.MoneySerializer;
import com.scfs.domain.NumSerializer;
import com.scfs.domain.PriceSerializer;

/**
 * Created by Administrator on 2016年10月20日.
 */
public class BillOutStoreDtlResDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5295481238679818669L;

	private Integer id;
	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 发货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal sendNum;
	/**
	 * 发货单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal sendPrice;
	/**
	 * 拣货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
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
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal costAmount;
	/**
	 * 订单金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal poAmount;
	/**
	 * 指定库存标志
	 */
	private Integer assignStlFlag;
	/**
	 * 已报关数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal customsDeclareNum;
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
	 * 拣货明细
	 */
	private List<BillOutStorePickDtlResDto> billOutStorePickDtlResDtoList;
	/************************************************/

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
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal unPickupNum;
	/**
	 * 拣货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal pickupAmount;
	/**
	 * 发货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal sendAmount;
	/**
	 * 库存状态 1-常规 2-残次品
	 */
	private String goodsStatusName;
	/**
	 * 指定库存标志名称
	 */
	private String assignStlFlagName;

	/**
	 * 成本金额(字符串)
	 */
	private String costAmountStr;
	/**
	 * 订单金额(字符串)
	 */
	private String poAmountStr;
	/**
	 * 发货单价(字符串)
	 */
	private String sendPriceStr;
	/**
	 * 发货金额(字符串)
	 */
	private String sendAmountStr;

	/**
	 * 销售单单号
	 */
	private String billNo;

	/**
	 * 出库日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date deliverTime;
	/**
	 * 总共的送货数量
	 */
	private BigDecimal countSendNum;

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
		this.batchNo = batchNo;
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

	public List<BillOutStorePickDtlResDto> getBillOutStorePickDtlResDtoList() {
		return billOutStorePickDtlResDtoList;
	}

	public void setBillOutStorePickDtlResDtoList(List<BillOutStorePickDtlResDto> billOutStorePickDtlResDtoList) {
		this.billOutStorePickDtlResDtoList = billOutStorePickDtlResDtoList;
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
		return unPickupNum;
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
		return sendAmount;
	}

	public void setSendAmount(BigDecimal sendAmount) {
		this.sendAmount = sendAmount;
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

	public Integer getAssignStlFlag() {
		return assignStlFlag;
	}

	public void setAssignStlFlag(Integer assignStlFlag) {
		this.assignStlFlag = assignStlFlag;
	}

	public String getAssignStlFlagName() {
		return assignStlFlagName;
	}

	public void setAssignStlFlagName(String assignStlFlagName) {
		this.assignStlFlagName = assignStlFlagName;
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

	public BigDecimal getCustomsDeclareNum() {
		return customsDeclareNum;
	}

	public void setCustomsDeclareNum(BigDecimal customsDeclareNum) {
		this.customsDeclareNum = customsDeclareNum;
	}

	public String getCostAmountStr() {
		return costAmountStr;
	}

	public void setCostAmountStr(String costAmountStr) {
		this.costAmountStr = costAmountStr;
	}

	public String getPoAmountStr() {
		return poAmountStr;
	}

	public void setPoAmountStr(String poAmountStr) {
		this.poAmountStr = poAmountStr;
	}

	public String getSendPriceStr() {
		return sendPriceStr;
	}

	public void setSendPriceStr(String sendPriceStr) {
		this.sendPriceStr = sendPriceStr;
	}

	public String getSendAmountStr() {
		return sendAmountStr;
	}

	public void setSendAmountStr(String sendAmountStr) {
		this.sendAmountStr = sendAmountStr;
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

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getDeliverTime() {
		return deliverTime;
	}

	public void setDeliverTime(Date deliverTime) {
		this.deliverTime = deliverTime;
	}

	public BigDecimal getCountSendNum() {
		return countSendNum;
	}

	public void setCountSendNum(BigDecimal countSendNum) {
		this.countSendNum = countSendNum;
	}

}
