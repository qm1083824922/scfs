package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: QueryAccountBookReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountBookSearchReqDto extends BaseReqDto
{
    private String accountBookName;
    private String accountBookNo;
    private String fiNo;
    private Integer busiUnit;
    private Integer state;
    public String getAccountBookName()
    {
        return accountBookName;
    }
    public void setAccountBookName(String accountBookName)
    {
        this.accountBookName = accountBookName;
    }
    public String getAccountBookNo()
    {
        return accountBookNo;
    }
    public void setAccountBookNo(String accountBookNo)
    {
        this.accountBookNo = accountBookNo;
    }
    public String getFiNo()
    {
        return fiNo;
    }
    public void setFiNo(String fiNo)
    {
        this.fiNo = fiNo;
    }
    public Integer getBusiUnit()
    {
        return busiUnit;
    }
    public void setBusiUnit(Integer busiUnit)
    {
        this.busiUnit = busiUnit;
    }
    public Integer getState()
    {
        return state;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }
}

