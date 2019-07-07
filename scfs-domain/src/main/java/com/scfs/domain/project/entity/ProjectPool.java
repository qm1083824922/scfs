package com.scfs.domain.project.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ProjectPool implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6859748124091938887L;

	/**
	 * ID
	 */
	private Integer id;

	/**
	 * 项目ID
	 */
	private Integer projectId;

	/**
	 * 项目额度
	 */
	private BigDecimal projectAmount;

	/**
	 * 项目额度(CNY)
	 */
	private BigDecimal projectAmountCny;

	/**
	 * 币种
	 */
	private Integer currencyType;

	/**
	 * 已使用资金额度
	 */
	private BigDecimal usedFundAmount;

	/**
	 * 已使用资金额度(CNY)
	 */
	private BigDecimal usedFundAmountCny;

	/**
	 * 资金余额
	 */
	private BigDecimal remainFundAmount;

	/**
	 * 资金余额(CNY)
	 */
	private BigDecimal remainFundAmountCny;

	/**
	 * 已使用资产额度
	 */
	private BigDecimal usedAssetAmount;

	/**
	 * 已使用资产额度(CNY)
	 */
	private BigDecimal usedAssetAmountCny;

	/**
	 * 资产余额
	 */
	private BigDecimal remainAssetAmount;

	/**
	 * 资产余额(CNY)
	 */
	private BigDecimal remainAssetAmountCny;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public BigDecimal getProjectAmount() {
		return projectAmount;
	}

	public void setProjectAmount(BigDecimal projectAmount) {
		this.projectAmount = projectAmount;
	}

	public BigDecimal getProjectAmountCny() {
		return projectAmountCny;
	}

	public void setProjectAmountCny(BigDecimal projectAmountCny) {
		this.projectAmountCny = projectAmountCny;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getUsedFundAmount() {
		return usedFundAmount;
	}

	public void setUsedFundAmount(BigDecimal usedFundAmount) {
		this.usedFundAmount = usedFundAmount;
	}

	public BigDecimal getUsedFundAmountCny() {
		return usedFundAmountCny;
	}

	public void setUsedFundAmountCny(BigDecimal usedFundAmountCny) {
		this.usedFundAmountCny = usedFundAmountCny;
	}

	public BigDecimal getRemainFundAmount() {
		return remainFundAmount;
	}

	public void setRemainFundAmount(BigDecimal remainFundAmount) {
		this.remainFundAmount = remainFundAmount;
	}

	public BigDecimal getRemainFundAmountCny() {
		return remainFundAmountCny;
	}

	public void setRemainFundAmountCny(BigDecimal remainFundAmountCny) {
		this.remainFundAmountCny = remainFundAmountCny;
	}

	public BigDecimal getUsedAssetAmount() {
		return usedAssetAmount;
	}

	public void setUsedAssetAmount(BigDecimal usedAssetAmount) {
		this.usedAssetAmount = usedAssetAmount;
	}

	public BigDecimal getUsedAssetAmountCny() {
		return usedAssetAmountCny;
	}

	public void setUsedAssetAmountCny(BigDecimal usedAssetAmountCny) {
		this.usedAssetAmountCny = usedAssetAmountCny;
	}

	public BigDecimal getRemainAssetAmount() {
		return remainAssetAmount;
	}

	public void setRemainAssetAmount(BigDecimal remainAssetAmount) {
		this.remainAssetAmount = remainAssetAmount;
	}

	public BigDecimal getRemainAssetAmountCny() {
		return remainAssetAmountCny;
	}

	public void setRemainAssetAmountCny(BigDecimal remainAssetAmountCny) {
		this.remainAssetAmountCny = remainAssetAmountCny;
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