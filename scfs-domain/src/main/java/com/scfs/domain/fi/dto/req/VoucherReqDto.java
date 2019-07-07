package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: VoucherReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class VoucherReqDto extends BaseReqDto
{
    private Integer voucherId;

    public Integer getVoucherId()
    {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId)
    {
        this.voucherId = voucherId;
    }
}

