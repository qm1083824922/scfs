package com.scfs.domain.fi.dto.resp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: QueryAccountBookResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator			
 *
 * </pre>
 */
public class AccountBookResDto
{
    private Integer id;
    private String accountBookName;
    private String accountBookNo;
    private String busiUnitName;
    private String stateName;
    private String fiNo;
    private String homeName;
    private String auditor;
    private String standardCoinName;
    private String creator;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAt;
    /**操作集合*/
    private List<CodeValue> opertaList;
    
    public static class Operate{
        public static Map<String,String> operMap = new HashMap<String,String>();
        static {
            
            operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_ACCOUNT_BOOK);
            operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_ACCOUNT_BOOK);
            operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_ACCOUNT_BOOK);
            operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_ACCOUNT_BOOK);
        }
    }
    
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
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
    public String getBusiUnitName()
    {
        return busiUnitName;
    }
    public void setBusiUnitName(String busiUnitName)
    {
        this.busiUnitName = busiUnitName;
    }
    public String getStateName()
    {
        return stateName;
    }
    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }
    public String getFiNo()
    {
        return fiNo;
    }
    public void setFiNo(String fiNo)
    {
        this.fiNo = fiNo;
    }
    public String getHomeName()
    {
        return homeName;
    }
    public void setHomeName(String homeName)
    {
        this.homeName = homeName;
    }
    public String getAuditor()
    {
        return auditor;
    }
    public void setAuditor(String auditor)
    {
        this.auditor = auditor;
    }
    public String getStandardCoinName()
    {
        return standardCoinName;
    }
    public void setStandardCoinName(String standardCoinName)
    {
        this.standardCoinName = standardCoinName;
    }
    public String getCreator()
    {
        return creator;
    }
    public void setCreator(String creator)
    {
        this.creator = creator;
    }
    public Date getCreateAt()
    {
        return createAt;
    }
    public void setCreateAt(Date createAt)
    {
        this.createAt = createAt;
    }
    public List<CodeValue> getOpertaList()
    {
        return opertaList;
    }
    public void setOpertaList(List<CodeValue> opertaList)
    {
        this.opertaList = opertaList;
    }
}

