package com.scfs.domain.logistics.dto.req;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年10月18日.
 */
public class PoOrderDtlReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5183934020537315586L;

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
	@DateTimeFormat(pattern = "yyyy-MM-dd")
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
	 * 订单明细数量
	 */
	private BigDecimal poNum;
	/**
	 * 订单明细入库数量
	 */
	private BigDecimal storageNum;
	/**
	 * 收货数量
	 */
	private BigDecimal receiveNum;
	/**
	 * 理货数量
	 */
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
	 * 批次
	 */
	private String batchNum;
	/**
	 * 币种
	 */
	private Integer currencyId;

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

	public BigDecimal getReceiveNum() {
		return receiveNum;
	}

	public void setReceiveNum(BigDecimal receiveNum) {
		this.receiveNum = receiveNum;
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

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

}
