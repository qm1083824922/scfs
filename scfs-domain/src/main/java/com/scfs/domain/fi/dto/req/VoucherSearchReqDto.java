package com.scfs.domain.fi.dto.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: QueryVoucherReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月24日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class VoucherSearchReqDto extends BaseReqDto
{
    private String voucherNo;
    private Integer projectId;
    private Integer voucherWord;
    private Integer accountBookId;
    private Integer busiUnit;
    private Integer state;
    private Integer billType;
    private String  billNo;
    private String voucherSummary;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startVoucherDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endVoucherDate;
    public String getVoucherNo()
    {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo)
    {
        this.voucherNo = voucherNo;
    }
    public Integer getProjectId()
    {
        return projectId;
    }
    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }
    public Integer getVoucherWord()
    {
        return voucherWord;
    }
    public void setVoucherWord(Integer voucherWord)
    {
        this.voucherWord = voucherWord;
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
    public Integer getState()
    {
        return state;
    }
    public void setState(Integer state)
    {
        this.state = state;
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
    public String getVoucherSummary()
    {
        return voucherSummary;
    }
    public void setVoucherSummary(String voucherSummary)
    {
        this.voucherSummary = voucherSummary;
    }
    public Date getStartVoucherDate()
    {
        return startVoucherDate;
    }
    public void setStartVoucherDate(Date startVoucherDate)
    {
        this.startVoucherDate = startVoucherDate;
    }
    public Date getEndVoucherDate()
    {
        return endVoucherDate;
    }
    public void setEndVoucherDate(Date endVoucherDate)
    {
        this.endVoucherDate = endVoucherDate;
    }
}

