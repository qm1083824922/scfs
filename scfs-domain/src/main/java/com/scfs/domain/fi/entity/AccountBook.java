package com.scfs.domain.fi.entity;

import com.scfs.domain.base.entity.BaseEntity;

import java.util.Date;

/**
 * <pre>
 * 
 *  File: AccountBook.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountBook extends BaseEntity
{
    /**财务编号**/
    private String fiNo;
    /**帐套名称**/
    private String accountBookName;
    /**帐套编号**/
    private String accountBookNo;
    /**经营单位 关联tb_base_subject{id}**/
    private Integer busiUnit;
    /**状态**/
    private Integer state;
    /**锁定人id**/
    private Integer lockedById;
    /**锁定人**/
    private String lockedBy;
    /**锁定时间**/
    private Date lockAt;
    /**是否国内**/
    private Integer isHome;
    /**审核人id**/
    private Integer auditorId;
    /**审核人**/
    private String auditor;
    /**本位币**/
    private Integer standardCoin;
    
    /**扩展字段**/
    private String busiUnitName;
    private String homeName;
    private String standardCoinName;

    public String getNameNo(){
        return this.accountBookNo + "-" + this.accountBookName;
    }
    public String getFiNo()
    {
        return fiNo;
    }
    public void setFiNo(String fiNo)
    {
        this.fiNo = fiNo;
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
    public Integer getLockedById()
    {
        return lockedById;
    }
    public void setLockedById(Integer lockedById)
    {
        this.lockedById = lockedById;
    }
    public String getLockedBy()
    {
        return lockedBy;
    }
    public void setLockedBy(String lockedBy)
    {
        this.lockedBy = lockedBy;
    }
    public Date getLockAt()
    {
        return lockAt;
    }
    public void setLockAt(Date lockAt)
    {
        this.lockAt = lockAt;
    }
    public Integer getIsHome()
    {
        return isHome;
    }
    public void setIsHome(Integer isHome)
    {
        this.isHome = isHome;
    }
    public Integer getAuditorId()
    {
        return auditorId;
    }
    public void setAuditorId(Integer auditorId)
    {
        this.auditorId = auditorId;
    }
    public String getAuditor()
    {
        return auditor;
    }
    public void setAuditor(String auditor)
    {
        this.auditor = auditor;
    }
    public Integer getStandardCoin()
    {
        return standardCoin;
    }
    public void setStandardCoin(Integer standardCoin)
    {
        this.standardCoin = standardCoin;
    }
    public String getBusiUnitName()
    {
        return busiUnitName;
    }
    public void setBusiUnitName(String busiUnitName)
    {
        this.busiUnitName = busiUnitName;
    }
    public String getHomeName()
    {
        return homeName;
    }
    public void setHomeName(String homeName)
    {
        this.homeName = homeName;
    }
    public String getStandardCoinName()
    {
        return standardCoinName;
    }
    public void setStandardCoinName(String standardCoinName)
    {
        this.standardCoinName = standardCoinName;
    }
    
}

