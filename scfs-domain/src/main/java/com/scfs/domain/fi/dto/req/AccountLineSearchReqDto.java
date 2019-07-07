package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: AccountLineSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月18日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountLineSearchReqDto extends BaseReqDto
{
    private String accountLineNo;
    private String accountLineName;
    private Integer accountLineType;
    private Integer debitOrCredit;
    private Integer accountLineLevel;
    private Integer state;
    private Integer isLast;
    
    private Integer accountBookId;  //过滤掉accountBookId已经分配的科目
    public String getAccountLineNo()
    {
        return accountLineNo;
    }
    public void setAccountLineNo(String accountLineNo)
    {
        this.accountLineNo = accountLineNo;
    }
    public String getAccountLineName()
    {
        return accountLineName;
    }
    public void setAccountLineName(String accountLineName)
    {
        this.accountLineName = accountLineName;
    }
    public Integer getAccountLineType()
    {
        return accountLineType;
    }
    public void setAccountLineType(Integer accountLineType)
    {
        this.accountLineType = accountLineType;
    }
    public Integer getDebitOrCredit()
    {
        return debitOrCredit;
    }
    public void setDebitOrCredit(Integer debitOrCredit)
    {
        this.debitOrCredit = debitOrCredit;
    }
    public Integer getAccountLineLevel()
    {
        return accountLineLevel;
    }
    public void setAccountLineLevel(Integer accountLineLevel)
    {
        this.accountLineLevel = accountLineLevel;
    }
    public Integer getState()
    {
        return state;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }
    public Integer getAccountBookId()
    {
        return accountBookId;
    }
    public void setAccountBookId(Integer accountBookId)
    {
        this.accountBookId = accountBookId;
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

