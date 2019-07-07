package com.scfs.domain.export.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.scfs.domain.MoneySerializer;
import com.scfs.domain.NumSerializer; 

public class CustomsApply implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6183799640400460737L;

	/**
     * 自增ID
     */ 
    private Integer id;

    /**
     * 报关申请编号
     */ 
    private String applyNo;

    /**
     * 报关申请附属编号
     */ 
    private String affiliateNo;

    /**
     * 项目ID
     */ 
    private Integer projectId;
    private Integer printNum;

    /**
     * 报关代理公司ID
     */ 
    private Integer proxyCompanyId;

    /**
     * 客户ID
     */ 
    private Integer customerId;

    /**
     * 收货地址ID
     */ 
    private Integer customerAddressId;

    /**
     * 报关日期
     */ 
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date customsDate;

    /**
     * 状态 1-待提交 2-已完成
     */ 
    private Integer status;
    
    /**
     * 报关数量
     */ 
    private BigDecimal customsNum;

    /**
     * 税率
     */ 
    private BigDecimal taxRate;

    /**
     * 报关含税金额
     */ 
    private BigDecimal customsAmount;
    
    /**
     * 报关税额
     */
    private BigDecimal customsTaxAmount;

    /**
     * 是否退税 0-未退税 1-正在退税 2-已退税
     */ 
    private Integer isReturnTax;

    /**
     * 申请备注
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAt;

    /**
     * 更新时间
     */ 
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateAt;

    /**
     * 作废人ID
     */ 
    private Integer deleterId;

    /**
     * 作废人
     */ 
    private String deleter;

    /**
     * 删除标记 0 : 有效 1 : 删除
     */ 
    private Integer isDelete;

    /**
     * 删除时间
     */ 
    private Date deleteAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApplyNo() {
        return applyNo;
    }

    public void setApplyNo(String applyNo) {
        this.applyNo = applyNo == null ? null : applyNo.trim();
    }

    public String getAffiliateNo() {
        return affiliateNo;
    }

    public void setAffiliateNo(String affiliateNo) {
        this.affiliateNo = affiliateNo == null ? null : affiliateNo.trim();
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getProxyCompanyId() {
        return proxyCompanyId;
    }

    public void setProxyCompanyId(Integer proxyCompanyId) {
        this.proxyCompanyId = proxyCompanyId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerAddressId() {
        return customerAddressId;
    }

    public void setCustomerAddressId(Integer customerAddressId) {
        this.customerAddressId = customerAddressId;
    }

    public Date getCustomsDate() {
        return customsDate;
    }

    public void setCustomsDate(Date customsDate) {
        this.customsDate = customsDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BigDecimal getCustomsNum() {
        return customsNum;
    }
    @JsonSerialize(using=NumSerializer.class)
    public void setCustomsNum(BigDecimal customsNum) {
        this.customsNum = customsNum;
    }

    public BigDecimal getCustomsAmount() {
        return customsAmount;
    }
    
    @JsonSerialize(using=MoneySerializer.class)
    public void setCustomsAmount(BigDecimal customsAmount) {
        this.customsAmount = customsAmount;
    }

    public Integer getIsReturnTax() {
        return isReturnTax;
    }

    public void setIsReturnTax(Integer isReturnTax) {
        this.isReturnTax = isReturnTax;
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

    public Integer getDeleterId() {
        return deleterId;
    }

    public void setDeleterId(Integer deleterId) {
        this.deleterId = deleterId;
    }

    public String getDeleter() {
        return deleter;
    }

    public void setDeleter(String deleter) {
        this.deleter = deleter == null ? null : deleter.trim();
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(Date deleteAt) {
        this.deleteAt = deleteAt;
    }

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public BigDecimal getCustomsTaxAmount() {
		return customsTaxAmount;
	}
   
	@JsonSerialize(using=MoneySerializer.class)
	public void setCustomsTaxAmount(BigDecimal customsTaxAmount) {
		this.customsTaxAmount = customsTaxAmount;
	}

	public Integer getPrintNum() {
		return printNum;
	}

	public void setPrintNum(Integer printNum) {
		this.printNum = printNum;
	}
    
    
}