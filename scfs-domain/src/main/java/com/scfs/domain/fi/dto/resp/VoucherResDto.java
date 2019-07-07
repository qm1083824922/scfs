package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: QueryVoucherResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月22日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class VoucherResDto extends BaseEntity
{
    private String accountBookName;
    private String busiUnitName;
    private String voucherWord;
    private String voucherSummary;
    private String voucherNo;
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String billTypeName;
    private String billNo;
    private String stateName;
    private Integer attachmentNumber;
    private Integer voucherLineNumber; //分录数量
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date voucherDate;
    
    /**操作集合*/
    private List<CodeValue> opertaList;
    
    public static class Operate{
        public static Map<String,String> operMap = new HashMap<String,String>();
        static {
            operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_VOUCHER);
            operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_VOUCHER_DETAIL);
            operMap.put(OperateConsts.EDIT,   BusUrlConsts.EDIT_VOUCHER_DETAIL);
            operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_VOUCHER);
            operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_VOUCHER);
        }
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
    public String getBillTypeName()
    {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName)
    {
        this.billTypeName = billTypeName;
    }
    public Integer getAttachmentNumber()
    {
        return attachmentNumber;
    }
    public void setAttachmentNumber(Integer attachmentNumber)
    {
        this.attachmentNumber = attachmentNumber;
    }
    public String getBillNo()
    {
        return billNo;
    }
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }
    public List<CodeValue> getOpertaList()
    {
        return opertaList;
    }
    public void setOpertaList(List<CodeValue> opertaList)
    {
        this.opertaList = opertaList;
    }
    public String getVoucherWord()
    {
        return voucherWord;
    }
    public void setVoucherWord(String voucherWord)
    {
        this.voucherWord = voucherWord;
    }
    public String getStateName()
    {
        return stateName;
    }
    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }
    public Integer getVoucherLineNumber()
    {
        return voucherLineNumber;
    }
    public void setVoucherLineNumber(Integer voucherLineNumber)
    {
        this.voucherLineNumber = voucherLineNumber;
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

