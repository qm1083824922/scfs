package com.scfs.domain.fi.entity;

import java.math.BigDecimal;

/**
 * <pre>
 * 
 *  File: ReceiptRecSum.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月25日			Administrator			
 *
 * </pre>
 */
public class ReceiptRecSum
{
    private Integer recId;
    private BigDecimal writeOffAmount;
    
    public Integer getRecId()
    {
        return recId;
    }
    public void setRecId(Integer recId)
    {
        this.recId = recId;
    }
    public BigDecimal getWriteOffAmount()
    {
        return writeOffAmount;
    }
    public void setWriteOffAmount(BigDecimal writeOffAmount)
    {
        this.writeOffAmount = writeOffAmount;
    }
}

