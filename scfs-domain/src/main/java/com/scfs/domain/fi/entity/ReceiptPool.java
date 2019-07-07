package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ReceiptPool {
    /**
     * 资金池的主键ID
     */ 
    private Integer id;

    /**
     * 资金额度（水单总金额）
     */ 
    private BigDecimal countFundAmount;

    /**
     * 经营单位ID
     */ 
    private Integer businessUnitId;

    /**
     * 币种
     */ 
    private Integer currencyType;

    /**
     * 已使用资金额度
     */ 
    private BigDecimal usedFundAmount;

    /**
     * 资金余额
     */ 
    private BigDecimal remainFundAmount;

    /**
     * 资产余额
     */ 
    private BigDecimal remainAssetAmount;

    /**
     * 资金额度（CNY）
     */ 
    private BigDecimal countFundAmountCny;

    /**
     * 已使用资金额度（CNY）
     */ 
    private BigDecimal usedFundAmountCny;

    /**
     * 资金余额（CNY）
     */ 
    private BigDecimal remainFundAmountCny;

    /**
     * 资产余额（CNY）
     */ 
    private BigDecimal remainAssetAmountCny;

    /**
     * 创建人ID
     */ 
    private Integer creatorId;

    /**
     * 创建人
     */ 
    private String creator;

    /**
     * 更新时间
     */ 
    private Date updateAt;

    /**
     * 创建时间
     */ 
    private Date createAt;
    
    /**
     * 预付款金额
     */
     private BigDecimal advancePayAmount;
     /**
      * 付货款金额
      */
     private BigDecimal paymentAmount;
     
     /**
      * 库存金额
      */
     private BigDecimal stlAmount;
     
     /**
      * 应收金额
      */
     private BigDecimal recAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
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

    public BigDecimal getRemainFundAmount() {
        return remainFundAmount;
    }

    public void setRemainFundAmount(BigDecimal remainFundAmount) {
        this.remainFundAmount = remainFundAmount;
    }

    public BigDecimal getRemainAssetAmount() {
        return remainAssetAmount;
    }

    public void setRemainAssetAmount(BigDecimal remainAssetAmount) {
        this.remainAssetAmount = remainAssetAmount;
    }

	public BigDecimal getCountFundAmount() {
		return countFundAmount;
	}

	public void setCountFundAmount(BigDecimal countFundAmount) {
		this.countFundAmount = countFundAmount;
	}

	public BigDecimal getCountFundAmountCny() {
		return countFundAmountCny;
	}

	public void setCountFundAmountCny(BigDecimal countFundAmountCny) {
		this.countFundAmountCny = countFundAmountCny;
	}

	public BigDecimal getUsedFundAmountCny() {
        return usedFundAmountCny;
    }

    public void setUsedFundAmountCny(BigDecimal usedFundAmountCny) {
        this.usedFundAmountCny = usedFundAmountCny;
    }

    public BigDecimal getRemainFundAmountCny() {
        return remainFundAmountCny;
    }

    public void setRemainFundAmountCny(BigDecimal remainFundAmountCny) {
        this.remainFundAmountCny = remainFundAmountCny;
    }

    public BigDecimal getRemainAssetAmountCny() {
        return remainAssetAmountCny;
    }

    public void setRemainAssetAmountCny(BigDecimal remainAssetAmountCny) {
        this.remainAssetAmountCny = remainAssetAmountCny;
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

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

	public BigDecimal getAdvancePayAmount() {
		return advancePayAmount;
	}

	public void setAdvancePayAmount(BigDecimal advancePayAmount) {
		this.advancePayAmount = advancePayAmount;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public BigDecimal getStlAmount() {
		return stlAmount;
	}

	public void setStlAmount(BigDecimal stlAmount) {
		this.stlAmount = stlAmount;
	}

	public BigDecimal getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(BigDecimal recAmount) {
		this.recAmount = recAmount;
	}
}