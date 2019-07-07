package com.scfs.domain.fi.dto.resp;

import java.util.List;
/**
 * <pre>
 * 
 *  File: QueryVoucherDetailsResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月24日			Administrator			
 *
 * </pre>
 */
public class VoucherDetailResDto
{
    VoucherModelResDto voucher;
    List<VoucherLineModelResDto> voucherLines;
    public VoucherModelResDto getVoucher()
    {
        return voucher;
    }
    public void setVoucher(VoucherModelResDto voucher)
    {
        this.voucher = voucher;
    }
    public List<VoucherLineModelResDto> getVoucherLines()
    {
        return voucherLines;
    }
    public void setVoucherLines(List<VoucherLineModelResDto> voucherLines)
    {
        this.voucherLines = voucherLines;
    }
    
}

