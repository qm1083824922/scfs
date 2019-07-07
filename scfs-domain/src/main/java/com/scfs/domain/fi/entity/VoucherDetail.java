package com.scfs.domain.fi.entity;

import java.util.List;

/**
 * <pre>
 * 
 *  File: VoucherDetail.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月24日			Administrator			
 *
 * </pre>
 */
public class VoucherDetail
{
    Voucher voucher;
    List<VoucherLine> voucherLines;
    public Voucher getVoucher()
    {
        return voucher;
    }
    public void setVoucher(Voucher voucher)
    {
        this.voucher = voucher;
    }
    public List<VoucherLine> getVoucherLines()
    {
        return voucherLines;
    }
    public void setVoucherLines(List<VoucherLine> voucherLines)
    {
        this.voucherLines = voucherLines;
    }
    
    
}

