package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: RecSumModel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月13日				Administrator			
 *
 * </pre>
 */
public class RecSumModel
{
    private BigDecimal amountReceivableSum ; 
    private BigDecimal amountReceivedSum;
    private Integer currencyType;
    
    public BigDecimal getAmountReceivableSum()
    {
        return amountReceivableSum;
    }
    public void setAmountReceivableSum(BigDecimal amountReceivableSum)
    {
        this.amountReceivableSum = amountReceivableSum;
    }
    public BigDecimal getAmountReceivedSum()
    {
        return amountReceivedSum;
    }
    public void setAmountReceivedSum(BigDecimal amountReceivedSum)
    {
        this.amountReceivedSum = amountReceivedSum;
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

