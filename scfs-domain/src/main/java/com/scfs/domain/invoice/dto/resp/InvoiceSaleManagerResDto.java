package com.scfs.domain.invoice.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

public class InvoiceSaleManagerResDto {
	/**
	 * 自增ID
	 */
	private Integer id;

	/**
	 * 发票申请ID
	 */
	private Integer invoiceApplyId;

	/**
	 * 单据ID, 销售单/退货单ID
	 */
	private Integer billId;

	/**
	 * 单据编号, 销售单/退货单编号
	 */
	private String billNo;

	/**
	 * 单据明细ID, 销售单/退货单明细ID
	 */
	private Integer billDtlId;

	/**
	 * 商品ID
	 */
	private Integer goodsId;
	/**
	 * 商品编号
	 */
	private String goodsNumber;
	/**
	 * 商品描述
	 */
	@JsonFormat(pattern = "yyyy-MM-dd  HH:mm:ss", timezone = "GMT+8")
	private Date billDate;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date billDateValue;
	/**
	 * 开票数量
	 */
	private BigDecimal provideInvoiceNum;
	private String provideInvoiceNumValue;
	private BigDecimal provideMaxNum;
	private BigDecimal recNum;
	private BigDecimal invoiceNum;
	/**
	 * 开票金额
	 */
	private BigDecimal provideInvoiceAmount;
	private BigDecimal provideMaxAmount;
	private BigDecimal recAmount;
	private BigDecimal invoiceAmount;
	private BigDecimal useAmount;
	private BigDecimal useNum;

	/**
	 * 开票单价
	 */
	private BigDecimal provideInvoicePrice;

	/**
	 * 收票数量
	 */
	private BigDecimal acceptInvoiceNum;

	/**
	 * 收票金额
	 */
	private BigDecimal acceptInvoiceAmount;

	/**
	 * 税额
	 */
	private BigDecimal rateAmount;

	/**
	 * 含税金额
	 */
	private BigDecimal inRateAmount;

	/**
	 * 未税金额
	 */
	private BigDecimal exRateAmount;

	/**
	 * 折扣税额
	 */
	private BigDecimal discountRateAmount;

	/**
	 * 折扣含税金额
	 */
	private BigDecimal discountInRateAmount;

	/**
	 * 折扣未税金额
	 */
	private BigDecimal discountExRateAmount;

	/**
	 * 折扣率
	 */
	private BigDecimal discount;

	/**
	 * 备注
	 */
	private String remark;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDITSALE);
		}
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public BigDecimal getRecNum() {
		return recNum;
	}

	public void setRecNum(BigDecimal recNum) {
		this.recNum = recNum;
	}

	public BigDecimal getInvoiceNum() {
		return invoiceNum;
	}

	public void setInvoiceNum(BigDecimal invoiceNum) {
		this.invoiceNum = invoiceNum;
	}

	public BigDecimal getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(BigDecimal recAmount) {
		this.recAmount = recAmount;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(BigDecimal useAmount) {
		this.useAmount = useAmount;
	}

	public BigDecimal getUseNum() {
		return useNum;
	}

	public void setUseNum(BigDecimal useNum) {
		this.useNum = useNum;
	}

	public BigDecimal getProvideMaxNum() {
		return provideMaxNum;
	}

	public void setProvideMaxNum(BigDecimal provideMaxNum) {
		this.provideMaxNum = provideMaxNum;
	}

	public Date getBillDateValue() {
		return billDateValue;
	}

	public void setBillDateValue(Date billDateValue) {
		this.billDateValue = billDateValue;
	}

	public BigDecimal getProvideMaxAmount() {
		return provideMaxAmount;
	}

	public void setProvideMaxAmount(BigDecimal provideMaxAmount) {
		this.provideMaxAmount = provideMaxAmount;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	private String goodsName;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInvoiceApplyId() {
		return invoiceApplyId;
	}

	public void setInvoiceApplyId(Integer invoiceApplyId) {
		this.invoiceApplyId = invoiceApplyId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo == null ? null : billNo.trim();
	}

	public Integer getBillDtlId() {
		return billDtlId;
	}

	public void setBillDtlId(Integer billDtlId) {
		this.billDtlId = billDtlId;
	}

	public String getProvideInvoiceNumValue() {
		return provideInvoiceNumValue;
	}

	public void setProvideInvoiceNumValue(String provideInvoiceNumValue) {
		this.provideInvoiceNumValue = provideInvoiceNumValue;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
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

	public BigDecimal getRateAmount() {
		return rateAmount;
	}

	public void setRateAmount(BigDecimal rateAmount) {
		this.rateAmount = rateAmount;
	}

	public BigDecimal getInRateAmount() {
		return inRateAmount;
	}

	public void setInRateAmount(BigDecimal inRateAmount) {
		this.inRateAmount = inRateAmount;
	}

	public BigDecimal getExRateAmount() {
		return exRateAmount;
	}

	public void setExRateAmount(BigDecimal exRateAmount) {
		this.exRateAmount = exRateAmount;
	}

	public BigDecimal getDiscountRateAmount() {
		return discountRateAmount;
	}

	public void setDiscountRateAmount(BigDecimal discountRateAmount) {
		this.discountRateAmount = discountRateAmount;
	}

	public BigDecimal getDiscountInRateAmount() {
		return discountInRateAmount;
	}

	public void setDiscountInRateAmount(BigDecimal discountInRateAmount) {
		this.discountInRateAmount = discountInRateAmount;
	}

	public BigDecimal getDiscountExRateAmount() {
		return discountExRateAmount;
	}

	public void setDiscountExRateAmount(BigDecimal discountExRateAmount) {
		this.discountExRateAmount = discountExRateAmount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public BigDecimal getProvideInvoicePrice() {
		return provideInvoicePrice;
	}

	public void setProvideInvoicePrice(BigDecimal provideInvoicePrice) {
		this.provideInvoicePrice = provideInvoicePrice;
	}

}