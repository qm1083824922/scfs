package com.scfs.domain.fi.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: AccountStatementSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月22日			Administrator			
 *
 * </pre>
 */
public class AccountStatementSearchReqDto extends BaseReqDto
{
    private static final long serialVersionUID = -2595764696186552031L;
    
    private Integer projectId;
    private Integer busiUnit;
    private Integer custId;
    private String  billNo;
    private Integer state;
    public Integer getProjectId()
    {
        return projectId;
    }
    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }
    public Integer getBusiUnit()
    {
        return busiUnit;
    }
    public void setBusiUnit(Integer busiUnit)
    {
        this.busiUnit = busiUnit;
    }
    public Integer getCustId()
    {
        return custId;
    }
    public void setCustId(Integer custId)
    {
        this.custId = custId;
    }
    public String getBillNo()
    {
        return billNo;
    }
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
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

