package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: AdvanceResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator
 *
 * </pre>
 */
public class AdvanceResDto {
	private Integer id;
	/** 项目名称 **/
	private String projectName;
	/** 经营单位 **/
	private String busiUnit;
	/** 客户名称 **/
	private String custName;
	/** 币种 **/
	private String currencyTypeName;
	/** 预收余额 **/
	private BigDecimal preRecAmount;
	/** 预收总额 **/
	private BigDecimal preRecSum;
	/** 已核销总额 **/
	private BigDecimal writeOffSum;
	/** 待核销金额 **/
	private BigDecimal writingOffAmount;

	/** 预收类型 **/
	private Integer advanceType;
	private String advanceTypeName;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	/** 余额 **/
	private BigDecimal blance;

	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;
	private Integer deletePrivFlag;//前台是否有删除预收关系得权限

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(String busiUnit) {
		this.busiUnit = busiUnit;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getPreRecAmount() {
		return preRecAmount;
	}

	public void setPreRecAmount(BigDecimal preRecAmount) {
		this.preRecAmount = preRecAmount;
	}

	public BigDecimal getPreRecSum() {
		return preRecSum;
	}

	public void setPreRecSum(BigDecimal preRecSum) {
		this.preRecSum = preRecSum;
	}

	public BigDecimal getWriteOffSum() {
		return writeOffSum;
	}

	public void setWriteOffSum(BigDecimal writeOffSum) {
		this.writeOffSum = writeOffSum;
	}

	public BigDecimal getWritingOffAmount() {
		return writingOffAmount;
	}

	public void setWritingOffAmount(BigDecimal writingOffAmount) {
		this.writingOffAmount = writingOffAmount;
	}

	public Integer getAdvanceType() {
		return advanceType;
	}

	public void setAdvanceType(Integer advanceType) {
		this.advanceType = advanceType;
	}

	public String getAdvanceTypeName() {
		return advanceTypeName;
	}

	public void setAdvanceTypeName(String advanceTypeName) {
		this.advanceTypeName = advanceTypeName;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
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

    public Integer getDeletePrivFlag()
    {
        return deletePrivFlag;
    }

    public void setDeletePrivFlag(Integer deletePrivFlag)
    {
        this.deletePrivFlag = deletePrivFlag;
    }

}
