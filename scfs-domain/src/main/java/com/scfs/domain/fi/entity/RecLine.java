package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

public class RecLine {
    private Integer id;

    /**
     * 应收id【关联应收表id】
     */ 
    private Integer recId;

    /**
     * 分录id,关联tb_voucher_line[id]
     * 新建时必填
     * 应收合并时必填
     */ 
    private Integer voucherLineId;
    
    /**
     * 对账金额
     * 新建时必填
     * 应收合并时必填
     */
    private BigDecimal amountCheck;  
    
    /**
     * 已核销金额
     */
    private BigDecimal writeOffAmount;

    private String creator;

    private Integer creatorId;

    private Date createAt;

    private Date updateAt;
    
    private Date billDate;
    private Integer billType;
    private String billNo;
    private Integer feeId;
    private Integer outStoreId;

    /**
     * 币种
     * 新建时必填
     * 应收合并时必填
     */
    private Integer currencyType;
    
    /**--------------扩展字段------------------**
    
    /**
     * 关联分录表 单据日期
     */
    private Integer projectId;
    private Integer custId;
    private Integer busiUnit;
    private Date checkDate;  
    /**
     * 关联凭证表 凭证日期
     */
    private Date voucherDate;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public Integer getVoucherLineId() {
        return voucherLineId;
    }

    public void setVoucherLineId(Integer voucherLineId) {
        this.voucherLineId = voucherLineId;
    }

    public BigDecimal getAmountCheck() {
        return amountCheck;
    }

    public void setAmountCheck(BigDecimal amountCheck) {
        this.amountCheck = amountCheck;
    }

    public BigDecimal getWriteOffAmount()
    {
        return writeOffAmount;
    }

    public void setWriteOffAmount(BigDecimal writeOffAmount)
    {
        this.writeOffAmount = writeOffAmount;
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

    public Integer getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(Integer currencyType) {
        this.currencyType = currencyType;
    }

    public Date getBillDate()
    {
        return billDate;
    }

    public void setBillDate(Date billDate)
    {
        this.billDate = billDate;
    }
    
    public Integer getBillType()
    {
        return billType;
    }

    public void setBillType(Integer billType)
    {
        this.billType = billType;
    }

    public String getBillNo()
    {
        return billNo;
    }

    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }

    public Integer getFeeId()
    {
        return feeId;
    }

    public void setFeeId(Integer feeId)
    {
        this.feeId = feeId;
    }

    public Integer getOutStoreId()
    {
        return outStoreId;
    }

    public void setOutStoreId(Integer outStoreId)
    {
        this.outStoreId = outStoreId;
    }

    public Integer getProjectId()
    {
        return projectId;
    }

    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }

    public Integer getCustId()
    {
        return custId;
    }

    public void setCustId(Integer custId)
    {
        this.custId = custId;
    }

    public Integer getBusiUnit()
    {
        return busiUnit;
    }

    public void setBusiUnit(Integer busiUnit)
    {
        this.busiUnit = busiUnit;
    }

    public Date getCheckDate()
    {
        return checkDate;
    }

    public void setCheckDate(Date checkDate)
    {
        this.checkDate = checkDate;
    }

    public Date getVoucherDate()
    {
        return voucherDate;
    }

    public void setVoucherDate(Date voucherDate)
    {
        this.voucherDate = voucherDate;
    }
}