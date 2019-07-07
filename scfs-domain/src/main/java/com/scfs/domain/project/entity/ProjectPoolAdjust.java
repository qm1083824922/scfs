package com.scfs.domain.project.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class ProjectPoolAdjust {
	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 调整编码
	 */
	private String adjustNo;

	/**
	 * 项目id
	 */
	private Integer projectId;

	/**
	 * 项目当前额度
	 */
	private BigDecimal projectAmount;

	/**
	 * 当前可用额度
	 */
	private BigDecimal remainFundAmount;

	/**
	 * 币种
	 */
	private Integer currencyType;

	/**
	 * 临时调整额度
	 */
	private BigDecimal adjustAmount;

	/**
	 * 创建人id
	 */
	private Integer createId;

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
	 * 有效期开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startValidDate;

	/**
	 * 有效期结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endValidDate;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 项目当前资金占用
	 */
	private BigDecimal usedFundAmount;

	private Integer state;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdjustNo() {
		return adjustNo;
	}

	public void setAdjustNo(String adjustNo) {
		this.adjustNo = adjustNo == null ? null : adjustNo.trim();
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

	public BigDecimal getRemainFundAmount() {
		return remainFundAmount;
	}

	public void setRemainFundAmount(BigDecimal remainFundAmount) {
		this.remainFundAmount = remainFundAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
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

	public Date getStartValidDate() {
		return startValidDate;
	}

	public void setStartValidDate(Date startValidDate) {
		this.startValidDate = startValidDate;
	}

	public Date getEndValidDate() {
		return endValidDate;
	}

	public void setEndValidDate(Date endValidDate) {
		this.endValidDate = endValidDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public BigDecimal getUsedFundAmount() {
		return usedFundAmount;
	}

	public void setUsedFundAmount(BigDecimal usedFundAmount) {
		this.usedFundAmount = usedFundAmount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}