package com.scfs.domain.fi.dto.req;
/**
 * <pre>
 * 
 *  File: VlReceiptRelSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月28日			Administrator			
 *
 * </pre>
 */
public class VlReceiptRelSearchReqDto
{
    
    private Integer receiptId;
    
    private Integer voucherLineId;

    public Integer getReceiptId()
    {
        return receiptId;
    }

    public void setReceiptId(Integer receiptId)
    {
        this.receiptId = receiptId;
    }

    public Integer getVoucherLineId()
    {
        return voucherLineId;
    }

    public void setVoucherLineId(Integer voucherLineId)
    {
        this.voucherLineId = voucherLineId;
    }
    
    

}

