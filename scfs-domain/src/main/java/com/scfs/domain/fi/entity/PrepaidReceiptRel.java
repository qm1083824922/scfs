package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

public class PrepaidReceiptRel {
    /**
     * 预付水单关系id
     */ 
    private Integer id;

    /**
     * 水单ID
     */ 
    private Integer receiptId;

    /**
     * 项目id
     */ 
    private Integer projectId;

    /**
     * 客户id
     */ 
    private Integer customerId;

    /**
     * 经营单位
     */ 
    private Integer busiUnitId;

    /**
     * 币种
     */ 
    private Integer currnecyType;

    /**
     * 可转转预付金额
     */ 
    private BigDecimal prepaidAmount;

    /**
     * 实际转预付金额
     */ 
    private BigDecimal actualPrepaidAmount;

    /**
     * 预付类型
     */ 
    private Integer prepaidType;

    /**
     * 是否删除
     */ 
    private Integer isDelete;

    /**
     * 创建人
     */ 
    private String creator;

    /**
     * 创建人id
     */ 
    private Integer creatorId;

    /**
     * 创建时间
     */ 
    private Date createAt;

    /**
     * 修改时间
     */ 
    private Date updateAt;

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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getBusiUnitId() {
        return busiUnitId;
    }

    public void setBusiUnitId(Integer busiUnitId) {
        this.busiUnitId = busiUnitId;
    }

    public Integer getCurrnecyType() {
        return currnecyType;
    }

    public void setCurrnecyType(Integer currnecyType) {
        this.currnecyType = currnecyType;
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

    public Integer getPrepaidType() {
        return prepaidType;
    }

    public void setPrepaidType(Integer prepaidType) {
        this.prepaidType = prepaidType;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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