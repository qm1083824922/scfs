package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: CheckAmountSumModel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月13日				Administrator			
 *
 * </pre>
 */
public class CheckAmountSumModel
{
    
    private BigDecimal amountSum ; 
    private BigDecimal amountCheckedSum;
    private BigDecimal amountUnCheckedSum;
    private Integer currencyType;
    public BigDecimal getAmountSum()
    {
        return amountSum;
    }
    public void setAmountSum(BigDecimal amountSum)
    {
        this.amountSum = amountSum;
    }
    public BigDecimal getAmountCheckedSum()
    {
        return amountCheckedSum;
    }
    public void setAmountCheckedSum(BigDecimal amountCheckedSum)
    {
        this.amountCheckedSum = amountCheckedSum;
    }
    public BigDecimal getAmountUnCheckedSum()
    {
        return amountUnCheckedSum;
    }
    public void setAmountUnCheckedSum(BigDecimal amountUnCheckedSum)
    {
        this.amountUnCheckedSum = amountUnCheckedSum;
    }
    public Integer getCurrencyType()
    {
        return currencyType;
    }
    public void setCurrencyType(Integer currencyType)
    {
        this.currencyType = currencyType;
    }

}

