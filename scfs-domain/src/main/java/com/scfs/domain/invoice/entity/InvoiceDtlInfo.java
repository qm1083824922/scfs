package com.scfs.domain.invoice.entity;

import java.math.BigDecimal;
import java.util.Date;

public class InvoiceDtlInfo {
	/** 自增ID */
	private Integer id;
	/** 发票ID */
	private Integer invoiceId;
	private String unit;
	/** 名称 */
	private String name;
	/** 型号 */
	private String type;
	/** 规格 */
	private String specification;
	/** 税收分类编码 */
	private String taxCateNo;
	/** 币种 1.人民币 2.美元 */
	private Integer currencyType;
	/** 汇率 */
	private BigDecimal exchangeRate;
	/** 数量 */
	private BigDecimal num;
	/** 单价 */
	private BigDecimal price;
	/** 税率 */
	private BigDecimal rate;
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
	/** 发票编号 */
	private String invoiceNo;
	/** 发票号 */
	private String invoiceCode;
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
	/** 客户ID */
	private String customerName;
	private String version;
	private Integer rule;
	/** 发票日期 **/
	private Date invoiceDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification == null ? null : specification.trim();
	}

	public String getTaxCateNo() {
		return taxCateNo;
	}

	public void setTaxCateNo(String taxCateNo) {
		this.taxCateNo = taxCateNo;
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

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
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

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getRule() {
		return rule;
	}

	public void setRule(Integer rule) {
		this.rule = rule;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

}