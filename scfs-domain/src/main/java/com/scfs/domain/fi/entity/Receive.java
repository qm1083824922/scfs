package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Receive {

	/**
	 * 合并应收时必填
	 */
	private Integer id;

	/**
	 * 客户 新建时必填
	 */
	private Integer custId;

	/**
	 * 经营单位 新建时必填
	 */
	private Integer busiUnit;

	/**
	 * 项目,新建时必填
	 */
	private Integer projectId;

	/**
	 * 1:人民币 2:美元 新建时必填
	 */
	private Integer currencyType;

	/**
	 * 应收金额
	 */
	private BigDecimal amountReceivable;

	/**
	 * 已收金额 ， 水单核销总额
	 */
	private BigDecimal amountReceived;

	/**
	 * 对账日期 新建时必填
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date checkDate;

	/**
	 * 还款期限日
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date expireDate;

	private Integer state;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建人id
	 */
	private Integer creatorId;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	/**
	 * 对账备注
	 */
	private String checkNote;

	/**
	 * 未收金额
	 */
	private BigDecimal blance;

	/**
	 * 超期天数
	 */
	private BigDecimal overDay;
	/** CNY汇率 **/
	private BigDecimal cnyRate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
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

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}

	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}

	public BigDecimal getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getCheckNote() {
		return checkNote;
	}

	public void setCheckNote(String checkNote) {
		this.checkNote = checkNote == null ? null : checkNote.trim();
	}

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
	}

	public BigDecimal getOverDay() {
		return overDay;
	}

	public void setOverDay(BigDecimal overDay) {
		this.overDay = overDay;
	}

	public BigDecimal getCnyRate() {
		return cnyRate;
	}

	public void setCnyRate(BigDecimal cnyRate) {
		this.cnyRate = cnyRate;
	}

}