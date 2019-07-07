package com.scfs.domain.export.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CustomsApplyLine implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8867291251975593783L;

	/**
     * 自增ID
     */ 
    private Integer id;

    /**
     * 报关申请ID
     */ 
    private Integer customsApplyId;

    /**
     * 出库单ID
     */ 
    private Integer billId;

    /**
     * 出库单编号
     */ 
    private String billNo;

    /**
     * 出库单附属编号
     */ 
    private String billAffiliateNo;

    /**
     * 出库单明细ID
     */ 
    private Integer billDtlId;

    /**
     * 商品ID
     */ 
    private Integer goodsId;

    /**
     * 报关数量
     */ 
    private BigDecimal customsNum;

    /**
     * 报关含税单价
     */ 
    private BigDecimal customsPrice;

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
    /**
     * 商品税率
     */
    private BigDecimal taxRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomsApplyId() {
        return customsApplyId;
    }

    public void setCustomsApplyId(Integer customsApplyId) {
        this.customsApplyId = customsApplyId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    public String getBillAffiliateNo() {
        return billAffiliateNo;
    }

    public void setBillAffiliateNo(String billAffiliateNo) {
        this.billAffiliateNo = billAffiliateNo == null ? null : billAffiliateNo.trim();
    }

    public Integer getBillDtlId() {
        return billDtlId;
    }

    public void setBillDtlId(Integer billDtlId) {
        this.billDtlId = billDtlId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getCustomsNum() {
        return customsNum;
    } 
    public void setCustomsNum(BigDecimal customsNum) {
        this.customsNum = customsNum;
    }

    public BigDecimal getCustomsPrice() {
        return customsPrice;
    } 
    
    public void setCustomsPrice(BigDecimal customsPrice) {
        this.customsPrice = customsPrice;
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

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
    
    
}