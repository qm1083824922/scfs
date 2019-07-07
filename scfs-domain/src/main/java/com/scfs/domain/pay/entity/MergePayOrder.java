package com.scfs.domain.pay.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class MergePayOrder {
	/**
	 * 自增id
	 */
	private Integer id;

	/**
	 * 合并付款编号
	 */
	private String mergePayNo;

	/**
	 * 经营单位
	 */
	private Integer busiUnit;

	/**
	 * 项目id
	 */
	private Integer projectId;

	/**
	 * 付款类型
	 */
	private Integer payType;

	/**
	 * 币种
	 */
	private Integer currencyType;

	/**
	 * 付款方式
	 */
	private Integer payWay;

	/**
	 * 付款金额
	 */
	private BigDecimal payAmount;

	/**
	 * 要求付款时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date requestPayTime;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建人id
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
	 * 收款单位
	 */
	private Integer payee;

	/**
	 * 收款账号
	 */
	private Integer payAccountId;

	/**
	 * 付款单位
	 */
	private Integer payer;

	/**
	 * 0 待提交 25待财务审核 30待财务主管审核 80待部门主管审核 90 待总经理审核 6已完成
	 */
	private Integer state;

	/**
	 * 打印次数
	 */
	private Integer printNum;

	/**
	 * 统一打印标示符
	 */
	private String unionPrintIdentifier;
	/** ------------------------扩展字段-------------------------- **/
	private BigDecimal advanceAmount;
	private BigDecimal saleAmount;
	private BigDecimal discountAmount;
	private BigDecimal inDiscountAmount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMergePayNo() {
		return mergePayNo;
	}

	public void setMergePayNo(String mergePayNo) {
		this.mergePayNo = mergePayNo == null ? null : mergePayNo.trim();
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Date getRequestPayTime() {
		return requestPayTime;
	}

	public void setRequestPayTime(Date requestPayTime) {
		this.requestPayTime = requestPayTime;
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

	public Integer getPayee() {
		return payee;
	}

	public void setPayee(Integer payee) {
		this.payee = payee;
	}

	public Integer getPayAccountId() {
		return payAccountId;
	}

	public void setPayAccountId(Integer payAccountId) {
		this.payAccountId = payAccountId;
	}

	public Integer getPayer() {
		return payer;
	}

	public void setPayer(Integer payer) {
		this.payer = payer;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}

	public String getUnionPrintIdentifier() {
		return unionPrintIdentifier;
	}

	public void setUnionPrintIdentifier(String unionPrintIdentifier) {
		this.unionPrintIdentifier = unionPrintIdentifier == null ? null : unionPrintIdentifier.trim();
	}

	public BigDecimal getAdvanceAmount() {
		return advanceAmount;
	}

	public void setAdvanceAmount(BigDecimal advanceAmount) {
		this.advanceAmount = advanceAmount;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getInDiscountAmount() {
		return inDiscountAmount;
	}

	public void setInDiscountAmount(BigDecimal inDiscountAmount) {
		this.inDiscountAmount = inDiscountAmount;
	}
}