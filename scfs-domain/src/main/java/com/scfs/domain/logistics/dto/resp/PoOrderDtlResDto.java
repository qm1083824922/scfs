package com.scfs.domain.logistics.dto.resp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.domain.NumSerializer;

/**
 * Created by Administrator on 2016年10月19日.
 */
public class PoOrderDtlResDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2416321492968487803L;

	private Integer id;
	/**
	 * 订单ID
	 */
	private Integer poId;
	/**
	 * 订单明细ID
	 */
	private Integer poDtlId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单附属编号
	 */
	private String appendNo;
	/**
	 * 订单日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date orderTime;
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
	 * 订单明细数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal poNum;
	/**
	 * 订单明细入库数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal storageNum;
	/**
	 * 收货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal receiveNum;
	/**
	 * 理货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal tallyNum;
	/**
	 * 收货单价
	 */
	private BigDecimal receivePrice;
	/**
	 * 订单单价
	 */
	private BigDecimal poPrice;
	/**
	 * 成本单价
	 */
	private BigDecimal costPrice;
	/**
	 * 批次
	 */
	private String batchNum;
	/**
	 * 币种
	 */
	private Integer currencyId;
	/**
	 * 币种名称
	 */
	private String currencyName;
	/**
	 * 订单明细未入库数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal unStorageNum;
	/**
	 * 商品折扣金额
	 */
	private BigDecimal discountAmount;
	/**
	 * 商品折扣单价
	 */
	private BigDecimal discountPrice;

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
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

	public Integer getPoDtlId() {
		return poDtlId;
	}

	public void setPoDtlId(Integer poDtlId) {
		this.poDtlId = poDtlId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
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

	public BigDecimal getPoNum() {
		return poNum;
	}

	public void setPoNum(BigDecimal poNum) {
		this.poNum = poNum;
	}

	public BigDecimal getStorageNum() {
		return storageNum;
	}

	public void setStorageNum(BigDecimal storageNum) {
		this.storageNum = storageNum;
	}

	public BigDecimal getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(BigDecimal receiveNum) {
		this.receiveNum = receiveNum;
	}

	public BigDecimal getTallyNum() {
		return tallyNum;
	}

	public void setTallyNum(BigDecimal tallyNum) {
		this.tallyNum = tallyNum;
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

	public BigDecimal getUnStorageNum() {
		return DecimalUtil.subtract(null == this.poNum ? BigDecimal.ZERO : this.poNum,
				null == this.storageNum ? BigDecimal.ZERO : this.storageNum);
	}

	public void setUnStorageNum(BigDecimal unStorageNum) {
		this.unStorageNum = unStorageNum;
	}

	public String getGoodsBarCode() {
		return goodsBarCode;
	}

	public void setGoodsBarCode(String goodsBarCode) {
		this.goodsBarCode = goodsBarCode;
	}

	public BigDecimal getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
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
}
