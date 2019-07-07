package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: QueryAcctBookLineRelReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月20日				Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AcctBookLineRelSearchReqDto extends BaseReqDto
{
    
    private Integer accountBookId;
    
    private Integer accountLineId;
    
    private Integer state;
    
    private Integer isLast;

    public Integer getAccountBookId()
    {
        return accountBookId;
    }

    public void setAccountBookId(Integer accountBookId)
    {
        this.accountBookId = accountBookId;
    }

    public Integer getAccountLineId()
    {
        return accountLineId;
    }

    public void setAccountLineId(Integer accountLineId)
    {
        this.accountLineId = accountLineId;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getIsLast()
    {
        return isLast;
    }

    public void setIsLast(Integer isLast)
    {
        this.isLast = isLast;
    }
    
}

