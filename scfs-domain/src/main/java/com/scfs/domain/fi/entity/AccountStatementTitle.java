package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class AccountStatementTitle {
    /**
     * 主键id
     */ 
    private Integer id;

    /**
     * 项目id
     */ 
    private Integer projectId;

    /**
     * 单据编号
     */ 
    private String billNo;

    /**
     * 单据附属编号
     */ 
    private String billAttachNo;

    /**
     * 客户id
     */ 
    private Integer custId;

    /**
     * 结算开始日期
     */ 
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date astStartDate;

    /**
     * 结算结束日期
     */ 
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date astEndDate;

    /**
     * 币种 (项目上的币种)
     */ 
    private Integer currencyType;

    /**
     * 项目总额度
     */ 
    private BigDecimal projectAmount;

    /**
     * 总占用额度
     */ 
    private BigDecimal totalAmount;

    /**
     * 可用额度
     */ 
    private BigDecimal inUseAmount;

    /**
     * 在库监管总额
     */ 
    private BigDecimal inStoreAmount;

    /**
     * 在途总额
     */ 
    private BigDecimal onWayAmount;

    /**
     * 借货总额
     */ 
    private BigDecimal lendAmount;

    /**
     * 状态 1:待提交 2:待财务审核 3:已完成
     */ 
    private Integer state;

    /**
     * 备注
     */ 
    private String note;

    /**
     * 创建人
     */ 
    private Integer creatorId;

    /**
     * 创建人id
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

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    public String getBillAttachNo() {
        return billAttachNo;
    }

    public void setBillAttachNo(String billAttachNo) {
        this.billAttachNo = billAttachNo == null ? null : billAttachNo.trim();
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Date getAstStartDate() {
        return astStartDate;
    }

    public void setAstStartDate(Date astStartDate) {
        this.astStartDate = astStartDate;
    }

    public Date getAstEndDate() {
        return astEndDate;
    }

    public void setAstEndDate(Date astEndDate) {
        this.astEndDate = astEndDate;
    }

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public BigDecimal getProjectAmount() {
        return projectAmount;
    }

    public void setProjectAmount(BigDecimal projectAmount) {
        this.projectAmount = projectAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getInUseAmount() {
        return inUseAmount;
    }

    public void setInUseAmount(BigDecimal inUseAmount) {
        this.inUseAmount = inUseAmount;
    }

    public BigDecimal getInStoreAmount() {
        return inStoreAmount;
    }

    public void setInStoreAmount(BigDecimal inStoreAmount) {
        this.inStoreAmount = inStoreAmount;
    }

    public BigDecimal getOnWayAmount() {
        return onWayAmount;
    }

    public void setOnWayAmount(BigDecimal onWayAmount) {
        this.onWayAmount = onWayAmount;
    }

    public BigDecimal getLendAmount() {
        return lendAmount;
    }

    public void setLendAmount(BigDecimal lendAmount) {
        this.lendAmount = lendAmount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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