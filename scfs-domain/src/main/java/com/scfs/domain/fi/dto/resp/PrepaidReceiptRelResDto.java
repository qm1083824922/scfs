package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PrepaidReceiptRelResDto {
	/**
	 * 预付和水单关联表ID
	 */
	private Integer id;
	/**
	 * 水单ID
	 */
	private Integer receiptId;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 客户ID
	 */
	private Integer customerId;

	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 经营单位
	 */
	private Integer busiUnitId;
	/**
	 * 经营单位名称
	 */
	private String busiUnitName;
	/**
	 * 币种
	 */
	private Integer currnecyType;
	/**
	 * 币种名称
	 */
	private String currnecyName;
	/**
	 * 预付类型
	 */
	private Integer prepaidType;
	/**
	 * 预付类型名称
	 */
	private String prepaidName;

	/**
	 * 可转预付金额
	 */
	private BigDecimal prepaidAmount;
	/**
	 * 实际转预付金额
	 */
	private BigDecimal actualPrepaidAmount;
	/**
	 * 创建人
	 */
	private String creator;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date createAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public String getCurrnecyName() {
		return currnecyName;
	}

	public void setCurrnecyName(String currnecyName) {
		this.currnecyName = currnecyName;
	}

	public Integer getPrepaidType() {
		return prepaidType;
	}

	public void setPrepaidType(Integer prepaidType) {
		this.prepaidType = prepaidType;
	}

	public String getPrepaidName() {
		return prepaidName;
	}

	public void setPrepaidName(String prepaidName) {
		this.prepaidName = prepaidName;
	}

	public BigDecimal getPrepaidAmount() {
		return prepaidAmount;
	}

	public void setPrepaidAmount(BigDecimal prepaidAmount) {
		this.prepaidAmount = prepaidAmount;
	}

	public BigDecimal getActualPrepaidAmount() {
		return actualPrepaidAmount;
	}

	public void setActualPrepaidAmount(BigDecimal actualPrepaidAmount) {
		this.actualPrepaidAmount = actualPrepaidAmount;
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
}
