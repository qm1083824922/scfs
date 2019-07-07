package com.scfs.domain.api.pms.dto.res;

import java.math.BigDecimal;

/**
 * 进销存销售信息
 * 
 * @author Administrator
 *
 */
public class InvoicingWecharResDto {
	/** 供应商id **/
	private Integer supplierId;
	/** 商品sku **/
	private String sku;
	/** 发送时间 **/
	private String sendTime;
	/** 销售数量 **/
	private BigDecimal salseNum = BigDecimal.ZERO;
	/** 入库数量 **/
	private BigDecimal stockinNum = BigDecimal.ZERO;
	/** 可用库存数量 **/
	private BigDecimal remainSendNum = BigDecimal.ZERO;
	/** 昨日销售数量 **/
	private BigDecimal dailySalseNum = BigDecimal.ZERO;
	/** 昨日入库数量 **/
	private BigDecimal dailyStockinNum = BigDecimal.ZERO;
	/** 发送类型：0 入库 1 出口 **/
	private Integer sendType;
	/** 商品类型 **/
	private String goodsType;

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public BigDecimal getSalseNum() {
		return salseNum;
	}

	public void setSalseNum(BigDecimal salseNum) {
		this.salseNum = salseNum;
	}

	public BigDecimal getStockinNum() {
		return stockinNum;
	}

	public void setStockinNum(BigDecimal stockinNum) {
		this.stockinNum = stockinNum;
	}

	public BigDecimal getRemainSendNum() {
		return remainSendNum;
	}

	public void setRemainSendNum(BigDecimal remainSendNum) {
		this.remainSendNum = remainSendNum;
	}

	public BigDecimal getDailySalseNum() {
		return dailySalseNum;
	}

	public void setDailySalseNum(BigDecimal dailySalseNum) {
		this.dailySalseNum = dailySalseNum;
	}

	public BigDecimal getDailyStockinNum() {
		return dailyStockinNum;
	}

	public void setDailyStockinNum(BigDecimal dailyStockinNum) {
		this.dailyStockinNum = dailyStockinNum;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

}
