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
 *  File: QueryAccountLineResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月18日			Administrator			
 *
 * </pre>
 */
public class AccountLineResDto
{
    private Integer id;
    private String accountLineNo;
    private String accountLineName;
    private Integer accountLineType;
    private String accountLineTypeName;
    private Integer debitOrCredit;
    private String debitOrCreditName;
    private Integer accountLineLevel;
    private String accountLineLevelName;
    private Integer state;
    private Integer isLast;
    private String isLastLabel;
    private String stateName;
    private Integer needProject;
    private Integer needSupplier;
    private Integer needCust;
    private Integer needAccount;
    private Integer needTaxRate;
    private Integer needUser;
    private Integer needInnerBusiUnit;
    
    private String  needProjectStr;
    private String needSupplierStr;
    private String needCustStr;
    private String needAccountStr;
    private String needTaxRateStr;
    private String needUserStr;
    private String needInnerBusiUnitStr;
    private String needDec;
    private Integer creatorId;
    private String creator;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAt;
    
    /**操作集合*/
    private List<CodeValue> opertaList;
    
    public static class Operate{
        public static Map<String,String> operMap = new HashMap<String,String>();
        static {
            operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_ACCOUNT_LINE);
            operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_ACCOUNT_LINE);
            operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_ACCOUNT_LINE);
            operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_ACCOUNT_LINE);
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
    public String getIsLastLabel()
    {
        return isLastLabel;
    }
    public void setIsLastLabel(String isLastLabel)
    {
        this.isLastLabel = isLastLabel;
    }
    public String getStateName()
    {
        return stateName;
    }
    public void setStateName(String stateName)
    {
        this.stateName = stateName;
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
    public String getNeedProjectStr()
    {
        return needProjectStr;
    }
    public void setNeedProjectStr(String needProjectStr)
    {
        this.needProjectStr = needProjectStr;
    }
    public String getNeedSupplierStr()
    {
        return needSupplierStr;
    }
    public void setNeedSupplierStr(String needSupplierStr)
    {
        this.needSupplierStr = needSupplierStr;
    }
    public String getNeedCustStr()
    {
        return needCustStr;
    }
    public void setNeedCustStr(String needCustStr)
    {
        this.needCustStr = needCustStr;
    }
    public String getNeedAccountStr()
    {
        return needAccountStr;
    }
    public void setNeedAccountStr(String needAccountStr)
    {
        this.needAccountStr = needAccountStr;
    }
    public String getNeedTaxRateStr()
    {
        return needTaxRateStr;
    }
    public void setNeedTaxRateStr(String needTaxRateStr)
    {
        this.needTaxRateStr = needTaxRateStr;
    }
    public String getNeedUserStr()
    {
        return needUserStr;
    }
    public void setNeedUserStr(String needUserStr)
    {
        this.needUserStr = needUserStr;
    }
    public String getNeedInnerBusiUnitStr()
    {
        return needInnerBusiUnitStr;
    }
    public void setNeedInnerBusiUnitStr(String needInnerBusiUnitStr)
    {
        this.needInnerBusiUnitStr = needInnerBusiUnitStr;
    }
    public String getNeedDec()
    {
        return needDec;
    }
    public void setNeedDec(String needDec)
    {
        this.needDec = needDec;
    }
    public Integer getCreatorId()
    {
        return creatorId;
    }
    public void setCreatorId(Integer creatorId)
    {
        this.creatorId = creatorId;
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

