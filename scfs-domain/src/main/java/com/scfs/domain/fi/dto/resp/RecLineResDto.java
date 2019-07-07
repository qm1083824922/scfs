package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: RecLineResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月1日				Administrator			
 *
 * </pre>
 */
public class RecLineResDto 
{
    private Integer id;
    private String projectName;     
    private String custName;       
    private String busiUnitName;    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date billDate;
    private String currencyTypeName;
    private BigDecimal amountCheck;
    private BigDecimal writeOffAmount;
    private String billNo;
    
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    public String getProjectName()
    {
        return projectName;
    }
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }
    public String getCustName()
    {
        return custName;
    }
    public void setCustName(String custName)
    {
        this.custName = custName;
    }
    public String getBusiUnitName()
    {
        return busiUnitName;
    }
    public void setBusiUnitName(String busiUnitName)
    {
        this.busiUnitName = busiUnitName;
    }
    public Date getBillDate()
    {
        return billDate;
    }
    public void setBillDate(Date billDate)
    {
        this.billDate = billDate;
    }
    public BigDecimal getAmountCheck()
    {
        return amountCheck;
    }
    public void setAmountCheck(BigDecimal amountCheck)
    {
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
    public String getCurrencyTypeName()
    {
        return currencyTypeName;
    }
    public void setCurrencyTypeName(String currencyTypeName)
    {
        this.currencyTypeName = currencyTypeName;
    }
    public String getBillNo()
    {
        return billNo;
    }
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }
}

