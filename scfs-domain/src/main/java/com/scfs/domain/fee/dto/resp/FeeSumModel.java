package com.scfs.domain.fee.dto.resp;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: FeeSumModel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月13日			Administrator			
 *
 * </pre>
 */
public class FeeSumModel
{
    private Integer currencyType;
    private BigDecimal recAmountSum;
    private BigDecimal receivedAmountSum;
    private BigDecimal provideInvoiceAmountSum;
    private BigDecimal paidAmountSum;
    private BigDecimal payAmountSum;
    private BigDecimal acceptInvoiceAmountSum;
    
    public Integer getCurrencyType()
    {
        return currencyType;
    }
    public void setCurrencyType(Integer currencyType)
    {
        this.currencyType = currencyType;
    }
    public BigDecimal getRecAmountSum()
    {
        return recAmountSum;
    }
    public void setRecAmountSum(BigDecimal recAmountSum)
    {
        this.recAmountSum = recAmountSum;
    }
    public BigDecimal getReceivedAmountSum()
    {
        return receivedAmountSum;
    }
    public void setReceivedAmountSum(BigDecimal receivedAmountSum)
    {
        this.receivedAmountSum = receivedAmountSum;
    }
    public BigDecimal getProvideInvoiceAmountSum()
    {
        return provideInvoiceAmountSum;
    }
    public void setProvideInvoiceAmountSum(BigDecimal provideInvoiceAmountSum)
    {
        this.provideInvoiceAmountSum = provideInvoiceAmountSum;
    }
    public BigDecimal getPaidAmountSum()
    {
        return paidAmountSum;
    }
    public void setPaidAmountSum(BigDecimal paidAmountSum)
    {
        this.paidAmountSum = paidAmountSum;
    }
    public BigDecimal getPayAmountSum()
    {
        return payAmountSum;
    }
    public void setPayAmountSum(BigDecimal payAmountSum)
    {
        this.payAmountSum = payAmountSum;
    }
    public BigDecimal getAcceptInvoiceAmountSum()
    {
        return acceptInvoiceAmountSum;
    }
    public void setAcceptInvoiceAmountSum(BigDecimal acceptInvoiceAmountSum)
    {
        this.acceptInvoiceAmountSum = acceptInvoiceAmountSum;
    }
    
    
}

