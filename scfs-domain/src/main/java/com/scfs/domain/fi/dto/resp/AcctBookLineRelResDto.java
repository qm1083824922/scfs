package com.scfs.domain.fi.dto.resp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

/**
 * <pre>
 * 
 *  File: QueryAcctBookLineRelResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月18日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AcctBookLineRelResDto implements Serializable
{
    private Integer id;
    private Integer accountBookId;
    private String accountBookName;
    private Integer accountLineId;
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
    
    private Integer state;
    private String stateName;
    
    private String needDec;
    private Integer creatorId;
    private String creator;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createAt;
    private Integer deleterId;
    private String deleter;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deleteAt;
    
    /**操作集合*/
    private List<CodeValue> opertaList;
    
    public static class Operate{
        public static Map<String,String> operMap = Maps.newHashMap();
        static {
            operMap.put(OperateConsts.INVALID, BusUrlConsts.INVALID_ACCOUNT_BOOK_LINE_REL);
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

    public Integer getAccountBookId()
    {
        return accountBookId;
    }

    public void setAccountBookId(Integer accountBookId)
    {
        this.accountBookId = accountBookId;
    }

    public String getAccountBookName()
    {
        return accountBookName;
    }

    public void setAccountBookName(String accountBookName)
    {
        this.accountBookName = accountBookName;
    }

    public Integer getAccountLineId()
    {
        return accountLineId;
    }

    public void setAccountLineId(Integer accountLineId)
    {
        this.accountLineId = accountLineId;
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

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public String getStateName()
    {
        return stateName;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }

    public String getNeedDec()
    {
        return needDec;
    }

    public void setNeedDec(String needDec)
    {
        this.needDec = needDec;
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

    public String getDeleter()
    {
        return deleter;
    }

    public void setDeleter(String deleter)
    {
        this.deleter = deleter;
    }

    public Date getDeleteAt()
    {
        return deleteAt;
    }

    public void setDeleteAt(Date deleteAt)
    {
        this.deleteAt = deleteAt;
    }

    public Integer getCreatorId()
    {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId)
    {
        this.creatorId = creatorId;
    }

    public Integer getDeleterId()
    {
        return deleterId;
    }

    public void setDeleterId(Integer deleterId)
    {
        this.deleterId = deleterId;
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

