package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

public class VlReceiptRel {
    /**
     * 主键id
     */ 
    private Integer id;

    /**
     * 分录id
     */ 
    private Integer voucherLineId;

    /**
     * 水单id
     */ 
    private Integer receiptId;

    /**
     * 核销金额
     */ 
    private BigDecimal writeOffAmount;

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
    
    /***扩展字段****/
    private Integer billType;
    private Integer outStoreId;
    private Integer feeId;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVoucherLineId() {
        return voucherLineId;
    }

    public void setVoucherLineId(Integer voucherLineId) {
        this.voucherLineId = voucherLineId;
    }

    public Integer getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId) {
        this.receiptId = receiptId;
    }

    public BigDecimal getWriteOffAmount() {
        return writeOffAmount;
    }

    public void setWriteOffAmount(BigDecimal writeOffAmount) {
        this.writeOffAmount = writeOffAmount;
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

    public Integer getBillType()
    {
        return billType;
    }

    public void setBillType(Integer billType)
    {
        this.billType = billType;
    }

    public Integer getOutStoreId()
    {
        return outStoreId;
    }

    public void setOutStoreId(Integer outStoreId)
    {
        this.outStoreId = outStoreId;
    }

    public Integer getFeeId()
    {
        return feeId;
    }

    public void setFeeId(Integer feeId)
    {
        this.feeId = feeId;
    }
}