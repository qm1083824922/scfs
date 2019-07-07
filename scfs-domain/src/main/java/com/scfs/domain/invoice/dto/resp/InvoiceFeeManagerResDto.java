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

public class InvoiceFeeManagerResDto {
	/**
	 * 自增ID
	 */
	private Integer id;

	/**
	 * 发票申请ID
	 */
	private Integer invoiceApplyId;

	/**
	 * 费用ID,关联fee表
	 */
	private Integer feeId;

	/**
	 * 费用编号
	 */
	private String feeNo;

	/**
	 * 收票金额
	 */
	private BigDecimal acceptInvoiceAmount;

	/**
	 * 税额
	 */
	private BigDecimal rateAmount;
	private BigDecimal useAmount;

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
	private BigDecimal recAmount;
	private BigDecimal invoiceAmount;
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
	private Date createAt;

	/**
	 * 更新时间
	 */
	private Date updateAt;
	/**
	 * 费用类型 1.应收费用 2.应付费用 3.应收应付费用
	 */
	private Integer feeType;
	private String feeTypeName;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDITSALE);
		}
	}

	/**
	 * 费用日期 开票-应收日期 收票-应付日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date feeDate;
	@JsonFormat(pattern = "yyyy-MM-dd ", timezone = "GMT+8")
	private Date feeDateValue;
	/**
	 * 开票金额
	 */
	private BigDecimal provideInvoiceAmount;
	private BigDecimal provideMaxAmount;

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public BigDecimal getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(BigDecimal recAmount) {
		this.recAmount = recAmount;
	}

	public BigDecimal getUseAmount() {
		return useAmount;
	}

	public void setUseAmount(BigDecimal useAmount) {
		this.useAmount = useAmount;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getProvideMaxAmount() {
		return provideMaxAmount;
	}

	public void setProvideMaxAmount(BigDecimal provideMaxAmount) {
		this.provideMaxAmount = provideMaxAmount;
	}

	public String getFeeTypeName() {
		return feeTypeName;
	}

	public void setFeeTypeName(String feeTypeName) {
		this.feeTypeName = feeTypeName;
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

	public Integer getFeeId() {
		return feeId;
	}

	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}

	public String getFeeNo() {
		return feeNo;
	}

	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo == null ? null : feeNo.trim();
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public Date getFeeDate() {
		return feeDate;
	}

	public void setFeeDate(Date feeDate) {
		this.feeDate = feeDate;
	}

	public BigDecimal getProvideInvoiceAmount() {
		return provideInvoiceAmount;
	}

	public void setProvideInvoiceAmount(BigDecimal provideInvoiceAmount) {
		this.provideInvoiceAmount = provideInvoiceAmount;
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

	public Date getFeeDateValue() {
		return feeDateValue;
	}

	public void setFeeDateValue(Date feeDateValue) {
		this.feeDateValue = feeDateValue;
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
}