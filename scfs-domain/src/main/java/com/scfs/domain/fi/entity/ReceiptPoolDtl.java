package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

public class ReceiptPoolDtl {
    /**
     * 资金池明细表ID
     */ 
    private Integer id;

    /**
     * 水单ID
     */ 
    private Integer receiptId;

    /**
     * 币种
     */ 
    private Integer currencyType;

    /**
     * 经营单位
     */ 
    private Integer businessUnitId;

    /**
     * 水单金额
     */ 
    private BigDecimal billAmount;
    
	/**尾差金额**/
    private BigDecimal diffAmount;

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
     * 跟新时间
     */ 
    private Date updateAt;

    /**
     * 项目id
     */ 
    private Integer projectId;
    /**中国银行当前汇率**/
    private BigDecimal exchangeRate;
    /**转化为人民后支付的金额**/
    private BigDecimal billAmountCny;
    /**付款单付费用类型**/
    private Integer payId;
    /**记账日期**/
    private Date businessDate;
    /**备注**/
    private String remark;
    /**单据类型**/
    private Integer  billType;
    /**单据编号**/
    private String  billNo;
    /**客户ID**/
    private Integer customerId;
    /**供应商ID**/
    private Integer supplierId;
    
	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Date getBusinessDate() {
		return businessDate;
	}

	public void setBusinessDate(Date businessDate) {
		this.businessDate = businessDate;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}


	public BigDecimal getBillAmountCny() {
		return billAmountCny;
	}

	public void setBillAmountCny(BigDecimal billAmountCny) {
		this.billAmountCny = billAmountCny;
	}

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

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public Integer getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Integer businessUnitId) {
        this.businessUnitId = businessUnitId;
    }

    public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
    public BigDecimal getDiffAmount() {
  		return diffAmount;
  	}

  	public void setDiffAmount(BigDecimal diffAmount) {
  		this.diffAmount = diffAmount;
  	}
    
}