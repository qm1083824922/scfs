package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InvoiceSaleManager {
	/** 自增ID */
	private Integer id;
	/** 发票申请ID */
	private Integer invoiceApplyId;
	/** 单据ID, 销售单/退货单ID */
	private Integer billId;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date billDate;
	/** 税率 */
	private BigDecimal taxRate;
	private String number;
	private String name;
	/** 单据编号, 销售单/退货单编号 */
	private String billNo;
	/** 单据明细ID, 销售单/退货单明细ID */
	private Integer billDtlId;
	/** 商品ID */
	private Integer goodsId;
	/** 开票数量 */
	private BigDecimal provideInvoiceNum;
	/** 开票单价 */
	private BigDecimal provideInvoicePrice;
	/** 开票金额 */
	private BigDecimal provideInvoiceAmount;
	/** 收票数量 */
	private BigDecimal acceptInvoiceNum;
	/** 收票金额 */
	private BigDecimal acceptInvoiceAmount;
	/** 税额 */
	private BigDecimal rateAmount;
	/** 含税金额 */
	private BigDecimal inRateAmount;
	/** 未税金额 */
	private BigDecimal exRateAmount;
	/** 折扣税额 */
	private BigDecimal discountRateAmount;
	/** 折扣含税金额 */
	private BigDecimal discountInRateAmount;
	/** 折扣未税金额 */
	private BigDecimal discountExRateAmount;
	/** 折扣率 */
	private BigDecimal discount;
	/** 备注 */
	private String remark;
	/** 创建人ID */
	private Integer creatorId;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	private Date createAt;
	/** 更新时间 */
	private Date updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getBillDate() {
		return billDate;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public Integer getInvoiceApplyId() {
		return invoiceApplyId;
	}

	public void setInvoiceApplyId(Integer invoiceApplyId) {
		this.invoiceApplyId = invoiceApplyId;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		this.creator = creator == null ? null : creator.trim();
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

	public BigDecimal getProvideInvoicePrice() {
		return provideInvoicePrice;
	}

	public void setProvideInvoicePrice(BigDecimal provideInvoicePrice) {
		this.provideInvoicePrice = provideInvoicePrice;
	}

}