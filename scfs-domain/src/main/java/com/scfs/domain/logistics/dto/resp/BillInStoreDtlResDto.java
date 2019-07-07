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
 * Created by Administrator on 2016年10月17日.
 */
public class BillInStoreDtlResDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7470404384957240586L;

	private Integer id;
	/**
	 * 入库单ID
	 */
	private Integer billInStoreId;
	/**
	 * PO订单明细ID
	 */
	private Integer poDtlId;
	/**
	 * PO订单ID
	 */
	private Integer poId;
	/**
	 * 出库单明细ID
	 */
	private Integer billOutStoreDtlId;
	/**
	 * 出库单ID
	 */
	private Integer billOutStoreId;
	/**
	 * 商品ID
	 */
	private Integer goodsId;
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
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal receivePrice;
	/**
	 * 订单单价
	 */
	@JsonSerialize(using = PriceSerializer.class)
	private BigDecimal poPrice;
	/**
	 * 关联入库单明细ID
	 */
	private Integer billInStoreDtlId;
	/**
	 * 入库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date acceptTime;
	/**
	 * 原入库时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date originAcceptTime;
	/**
	 * 批次号
	 */
	private String batchNo;
	/**
	 * 到货日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date receiveDate;
	/**
	 * 币种 1-人民币 2-美元 3-港元
	 */
	private Integer currencyType;
	/**
	 * 汇率
	 */
	private BigDecimal exchangeRate;
	/**
	 * 客户ID
	 */
	private Integer customerId;
	/**
	 * 供应商ID
	 */
	private Integer supplierId;
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
	 * 关联销售退货单ID
	 */
	private Integer billDeliveryId;
	/**
	 * 关联销售退货单明细ID
	 */
	private Integer billDeliveryDtlId;
	/**
	 * 理货明细
	 */
	private List<BillInStoreTallyDtlResDto> billInStoreTallyDtlResDtoList;
	/**************************************************************/
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单附属编号
	 */
	private String appendNo;
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
	 * 商品规格
	 */
	private String specification;
	/**
	 * 未理货数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal unTallyNum;
	/**
	 * 理货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal tallyAmount;
	/**
	 * 收货金额
	 */
	@JsonSerialize(using = MoneySerializer.class)
	private BigDecimal receiveAmount;
	/**
	 * 未入库订单数量
	 */
	@JsonSerialize(using = NumSerializer.class)
	private BigDecimal unStorageNum;
	/**
	 * 收货单价(字符串)
	 */
	private String receivePriceStr;
	/**
	 * 收货金额
	 */
	private String receiveAmountStr;
	/**
	 * 销售单号
	 */
	private String billDeliveryNo;
	/**
	 * 销售附属单号
	 */
	private String billDeliveryAffiliateNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBillInStoreId() {
		return billInStoreId;
	}

	public void setBillInStoreId(Integer billInStoreId) {
		this.billInStoreId = billInStoreId;
	}

	public Integer getPoDtlId() {
		return poDtlId;
	}

	public void setPoDtlId(Integer poDtlId) {
		this.poDtlId = poDtlId;
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

	public Integer getBillInStoreDtlId() {
		return billInStoreDtlId;
	}

	public void setBillInStoreDtlId(Integer billInStoreDtlId) {
		this.billInStoreDtlId = billInStoreDtlId;
	}

	public Date getAcceptTime() {
		return acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
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

	public List<BillInStoreTallyDtlResDto> getBillInStoreTallyDtlResDtoList() {
		return billInStoreTallyDtlResDtoList;
	}

	public void setBillInStoreTallyDtlResDtoList(List<BillInStoreTallyDtlResDto> billInStoreTallyDtlResDtoList) {
		this.billInStoreTallyDtlResDtoList = billInStoreTallyDtlResDtoList;
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

	public BigDecimal getUnTallyNum() {
		return unTallyNum;
	}

	public void setUnTallyNum(BigDecimal unTallyNum) {
		this.unTallyNum = unTallyNum;
	}

	public BigDecimal getTallyAmount() {
		return tallyAmount;
	}

	public void setTallyAmount(BigDecimal tallyAmount) {
		this.tallyAmount = tallyAmount;
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public Integer getBillOutStoreDtlId() {
		return billOutStoreDtlId;
	}

	public void setBillOutStoreDtlId(Integer billOutStoreDtlId) {
		this.billOutStoreDtlId = billOutStoreDtlId;
	}

	public Integer getBillOutStoreId() {
		return billOutStoreId;
	}

	public void setBillOutStoreId(Integer billOutStoreId) {
		this.billOutStoreId = billOutStoreId;
	}

	public BigDecimal getUnStorageNum() {
		return (null == this.unStorageNum ? BigDecimal.ZERO : this.unStorageNum)
				.add(null == this.receiveNum ? BigDecimal.ZERO : this.receiveNum);
	}

	public void setUnStorageNum(BigDecimal unStorageNum) {
		this.unStorageNum = unStorageNum;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
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

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getReceivePriceStr() {
		return receivePriceStr;
	}

	public void setReceivePriceStr(String receivePriceStr) {
		this.receivePriceStr = receivePriceStr;
	}

	public String getReceiveAmountStr() {
		return receiveAmountStr;
	}

	public void setReceiveAmountStr(String receiveAmountStr) {
		this.receiveAmountStr = receiveAmountStr;
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

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Date getOriginAcceptTime() {
		return originAcceptTime;
	}

	public void setOriginAcceptTime(Date originAcceptTime) {
		this.originAcceptTime = originAcceptTime;
	}

	public Integer getBillDeliveryId() {
		return billDeliveryId;
	}

	public void setBillDeliveryId(Integer billDeliveryId) {
		this.billDeliveryId = billDeliveryId;
	}

	public Integer getBillDeliveryDtlId() {
		return billDeliveryDtlId;
	}

	public void setBillDeliveryDtlId(Integer billDeliveryDtlId) {
		this.billDeliveryDtlId = billDeliveryDtlId;
	}

	public String getBillDeliveryNo() {
		return billDeliveryNo;
	}

	public void setBillDeliveryNo(String billDeliveryNo) {
		this.billDeliveryNo = billDeliveryNo;
	}

	public String getBillDeliveryAffiliateNo() {
		return billDeliveryAffiliateNo;
	}

	public void setBillDeliveryAffiliateNo(String billDeliveryAffiliateNo) {
		this.billDeliveryAffiliateNo = billDeliveryAffiliateNo;
	}

}
