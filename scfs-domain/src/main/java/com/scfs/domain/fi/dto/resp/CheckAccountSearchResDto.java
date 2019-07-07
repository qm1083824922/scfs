package com.scfs.domain.fi.dto.resp;

import java.util.List;

import com.scfs.domain.fi.entity.Receive;

/**
 * <pre>
 * 
 *  File: CheckAccountSearchResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator			
 *
 * </pre>
 */
public class CheckAccountSearchResDto
{
    private List<VoucherLineModelResDto> voucherLines ;
    private List<Receive> receives;
    public List<VoucherLineModelResDto> getVoucherLines()
    {
        return voucherLines;
    }
    public void setVoucherLines(List<VoucherLineModelResDto> voucherLines)
    {
        this.voucherLines = voucherLines;
    }
    public List<Receive> getReceives()
    {
        return receives;
    }
    public void setReceives(List<Receive> receives)
    {
        this.receives = receives;
    }
}

