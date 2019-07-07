package com.scfs.domain.fi.entity;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: AccountBookLineRel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountBookLineRel extends BaseEntity
{
    private Integer accountBookId;
    private Integer accountLineId;
    private Integer state;
    private Integer busiUnit;
    private String accountBookName;
    private String accountLineName;
    private String accountLineNo;
    private Integer accountLineType;
    private String accountLineTypeName;
    private Integer debitOrCredit;
    private String debitOrCreditName;
    private Integer accountLineLevel;
    private String accountLineLevelName;
    private Integer needProject;
    private Integer needSupplier;
    private Integer needCust;
    private Integer needAccount;
    private Integer needTaxRate;
    private Integer needUser;
    private Integer needInnerBusiUnit;
    
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
    public Integer getBusiUnit()
    {
        return busiUnit;
    }
    public void setBusiUnit(Integer busiUnit)
    {
        this.busiUnit = busiUnit;
    }
    public String getAccountBookName()
    {
        return accountBookName;
    }
    public void setAccountBookName(String accountBookName)
    {
        this.accountBookName = accountBookName;
    }
    public String getAccountLineName()
    {
        return accountLineName;
    }
    public void setAccountLineName(String accountLineName)
    {
        this.accountLineName = accountLineName;
    }
    public String getAccountLineNo()
    {
        return accountLineNo;
    }
    public void setAccountLineNo(String accountLineNo)
    {
        this.accountLineNo = accountLineNo;
    }
    public Integer getAccountLineType()
    {
        return accountLineType;
    }
    public void setAccountLineType(Integer accountLineType)
    {
        this.accountLineType = accountLineType;
    }
    public String getAccountLineTypeName()
    {
        return accountLineTypeName;
    }
    public void setAccountLineTypeName(String accountLineTypeName)
    {
        this.accountLineTypeName = accountLineTypeName;
    }
    public Integer getDebitOrCredit()
    {
        return debitOrCredit;
    }
    public void setDebitOrCredit(Integer debitOrCredit)
    {
        this.debitOrCredit = debitOrCredit;
    }
    public String getDebitOrCreditName()
    {
        return debitOrCreditName;
    }
    public void setDebitOrCreditName(String debitOrCreditName)
    {
        this.debitOrCreditName = debitOrCreditName;
    }
    public Integer getAccountLineLevel()
    {
        return accountLineLevel;
    }
    public void setAccountLineLevel(Integer accountLineLevel)
    {
        this.accountLineLevel = accountLineLevel;
    }
    public String getAccountLineLevelName()
    {
        return accountLineLevelName;
    }
    public void setAccountLineLevelName(String accountLineLevelName)
    {
        this.accountLineLevelName = accountLineLevelName;
    }
    public Integer getNeedProject()
    {
        return needProject;
    }
    public void setNeedProject(Integer needProject)
    {
        this.needProject = needProject;
    }
    public Integer getNeedSupplier()
    {
        return needSupplier;
    }
    public void setNeedSupplier(Integer needSupplier)
    {
        this.needSupplier = needSupplier;
    }
    public Integer getNeedCust()
    {
        return needCust;
    }
    public void setNeedCust(Integer needCust)
    {
        this.needCust = needCust;
    }
    public Integer getNeedAccount()
    {
        return needAccount;
    }
    public void setNeedAccount(Integer needAccount)
    {
        this.needAccount = needAccount;
    }
    public Integer getNeedTaxRate()
    {
        return needTaxRate;
    }
    public void setNeedTaxRate(Integer needTaxRate)
    {
        this.needTaxRate = needTaxRate;
    }
    public Integer getNeedUser()
    {
        return needUser;
    }
    public void setNeedUser(Integer needUser)
    {
        this.needUser = needUser;
    }
    public Integer getNeedInnerBusiUnit()
    {
        return needInnerBusiUnit;
    }
    public void setNeedInnerBusiUnit(Integer needInnerBusiUnit)
    {
        this.needInnerBusiUnit = needInnerBusiUnit;
    }
}

