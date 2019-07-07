package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: VoucherModelResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月25日			Administrator			
 *
 * </pre>
 */
public class VoucherModelResDto 
{
    /**主键ID*/
    private Integer id;
    /**帐套id 关联tb_fi_account_book{id} **/
    private Integer accountBookId;
    
    /**经营单位id 关联tb_base_subject{id} **/
    private Integer busiUnit;
    
    /**凭证字 **/
    private Integer voucherWord;
    
    /**凭证摘要 **/
    private String voucherSummary;
    
    /**凭证编号 **/
    private String voucherNo;
    
    /**贷方金额 **/
    private BigDecimal debitAmount;
    
    /**借方金额 **/
    private BigDecimal creditAmount;
    
    /**费用单据id **/
    private Integer feeId;
    
    /**出库单据id **/
    private Integer outStoreId;
    
    /**入库单据id **/
    private Integer inStoreId;
    
    /**付款单据id **/
    private Integer payId;
    
    /**收票单据id **/
    private Integer acceptInvoiceId;
    
    /**开票单据id **/
    private Integer provideInvoiceId;
    
    /**水单单据id **/
    private Integer receiptId;
    
    /**状态 **/
    private Integer state;
    
    /**附单据数 **/
    private Integer attachmentNumber;
    
    /**凭证日期，可能不是凭证创建日期 **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date voucherDate;
    private Integer billType;
    private String billNo;
    private Date billDate;
    /**创建人id*/
    private Integer creatorId;
    /**创建人*/
    private String creator;
    /**创建时间*/
    private Date createAt;
    /**删除人*/
    private String deleter;
    /**删除人id*/
    private Integer deleterId;
    /**删除时间*/
    private Date deleteAt;
    /**逻辑删除*/
    private Integer isDelete;
    /**更新时间*/
    private Date updateAt;
    
    private String accountBookName;
    private String busiUnitName;
    private String busiUnitAddress;
    private String voucherWordName;
    private String billTypeName;
    private String stateName;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date systemTime;
    
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public Integer getAccountBookId()
    {
        return accountBookId;
    }
    public void setAccountBookId(Integer accountBookId)
    {
        this.accountBookId = accountBookId;
    }
    public Integer getBusiUnit()
    {
        return busiUnit;
    }
    public void setBusiUnit(Integer busiUnit)
    {
        this.busiUnit = busiUnit;
    }
    public Integer getVoucherWord()
    {
        return voucherWord;
    }
    public void setVoucherWord(Integer voucherWord)
    {
        this.voucherWord = voucherWord;
    }
    public String getVoucherSummary()
    {
        return voucherSummary;
    }
    public void setVoucherSummary(String voucherSummary)
    {
        this.voucherSummary = voucherSummary;
    }
    public String getVoucherNo()
    {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo)
    {
        this.voucherNo = voucherNo;
    }
    public BigDecimal getDebitAmount()
    {
        return debitAmount;
    }
    public void setDebitAmount(BigDecimal debitAmount)
    {
        this.debitAmount = debitAmount;
    }
    public BigDecimal getCreditAmount()
    {
        return creditAmount;
    }
    public void setCreditAmount(BigDecimal creditAmount)
    {
        this.creditAmount = creditAmount;
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
    public Integer getInStoreId()
    {
        return inStoreId;
    }
    public void setInStoreId(Integer inStoreId)
    {
        this.inStoreId = inStoreId;
    }
    public Integer getPayId()
    {
        return payId;
    }
    public void setPayId(Integer payId)
    {
        this.payId = payId;
    }
    public Integer getAcceptInvoiceId()
    {
        return acceptInvoiceId;
    }
    public void setAcceptInvoiceId(Integer acceptInvoiceId)
    {
        this.acceptInvoiceId = acceptInvoiceId;
    }
    public Integer getProvideInvoiceId()
    {
        return provideInvoiceId;
    }
    public void setProvideInvoiceId(Integer provideInvoiceId)
    {
        this.provideInvoiceId = provideInvoiceId;
    }
    public Integer getReceiptId()
    {
        return receiptId;
    }
    public void setReceiptId(Integer receiptId)
    {
        this.receiptId = receiptId;
    }
    public Integer getState()
    {
        return state;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }
    public Integer getAttachmentNumber()
    {
        return attachmentNumber;
    }
    public void setAttachmentNumber(Integer attachmentNumber)
    {
        this.attachmentNumber = attachmentNumber;
    }
    public Date getVoucherDate()
    {
        return voucherDate;
    }
    public void setVoucherDate(Date voucherDate)
    {
        this.voucherDate = voucherDate;
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
    public Date getBillDate()
    {
        return billDate;
    }
    public void setBillDate(Date billDate)
    {
        this.billDate = billDate;
    }
    public Integer getCreatorId()
    {
        return creatorId;
    }
    public void setCreatorId(Integer creatorId)
    {
        this.creatorId = creatorId;
    }
    public String getCreator()
    {
        return creator;
    }
    public void setCreator(String creator)
    {
        this.creator = creator;
    }
    public Date getCreateAt()
    {
        return createAt;
    }
    public void setCreateAt(Date createAt)
    {
        this.createAt = createAt;
    }
    public String getDeleter()
    {
        return deleter;
    }
    public void setDeleter(String deleter)
    {
        this.deleter = deleter;
    }
    public Integer getDeleterId()
    {
        return deleterId;
    }
    public void setDeleterId(Integer deleterId)
    {
        this.deleterId = deleterId;
    }
    public Date getDeleteAt()
    {
        return deleteAt;
    }
    public void setDeleteAt(Date deleteAt)
    {
        this.deleteAt = deleteAt;
    }
    public Integer getIsDelete()
    {
        return isDelete;
    }
    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }
    public Date getUpdateAt()
    {
        return updateAt;
    }
    public void setUpdateAt(Date updateAt)
    {
        this.updateAt = updateAt;
    }
    public String getAccountBookName()
    {
        return accountBookName;
    }
    public void setAccountBookName(String accountBookName)
    {
        this.accountBookName = accountBookName;
    }
    public String getBusiUnitName()
    {
        return busiUnitName;
    }
    public void setBusiUnitName(String busiUnitName)
    {
        this.busiUnitName = busiUnitName;
    }
    public String getBusiUnitAddress()
    {
        return busiUnitAddress;
    }
    public void setBusiUnitAddress(String busiUnitAddress)
    {
        this.busiUnitAddress = busiUnitAddress;
    }
    public String getVoucherWordName()
    {
        return voucherWordName;
    }
    public void setVoucherWordName(String voucherWordName)
    {
        this.voucherWordName = voucherWordName;
    }
    public String getBillTypeName()
    {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName)
    {
        this.billTypeName = billTypeName;
    }
    public String getStateName()
    {
        return stateName;
    }
    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }
    public Date getSystemTime()
    {
        return systemTime;
    }
    public void setSystemTime(Date systemTime)
    {
        this.systemTime = systemTime;
    }
}

