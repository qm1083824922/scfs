package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;
/**
 * <pre>
 * 
 *  File: ReceiptOutStoreRel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月03日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ReceiptOutStoreRel  extends BaseEntity{
    /**
     * 水单和出库单的关系ID
     */ 
    private Integer id;

    /**
     * 出库单的ID
     */ 
    private Integer billOutId;

    /**
     * 水单ID
     */ 
    private Integer bankReceiptId;

    /**
     * 经营单位
     */ 
    private Integer busiUnit;

    /**
     * 项目ID
     */ 
    private Integer projectId;

    /**
     * 客户id
     */ 
    private Integer customerId;

    /**
     * 币种 1.人民币 2.美元 3.港币
     */ 
    private Integer currencyType;

    /**
     * 更新时间
     */ 
    private Date updateAt;

    /**
     * 删除标记 0 : 有效 1 : 删除
     */ 
    private Integer isDelete;

    /**
     * 回款金额
     */ 
    private BigDecimal receivedAmount;

    /**
     * 核销金额{出库单发货金额}
     */ 
    private BigDecimal writeOffAmount;

    /**
     * 创建时间
     */ 
    private Date createAt;
/**单据类型**/
    private Integer billType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBillOutId() {
        return billOutId;
    }

    public void setBillOutId(Integer billOutId) {
        this.billOutId = billOutId;
    }

    public Integer getBankReceiptId() {
        return bankReceiptId;
    }

    public void setBankReceiptId(Integer bankReceiptId) {
        this.bankReceiptId = bankReceiptId;
    }

    public Integer getBusiUnit() {
        return busiUnit;
    }

    public void setBusiUnit(Integer busiUnit) {
        this.busiUnit = busiUnit;
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

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public BigDecimal getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(BigDecimal receivedAmount) {
        this.receivedAmount = receivedAmount;
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
    
   public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}
}