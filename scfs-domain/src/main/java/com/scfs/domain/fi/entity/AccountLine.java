package com.scfs.domain.fi.entity;

import com.scfs.common.consts.BaseConsts;
import com.scfs.domain.base.entity.BaseEntity;

import java.util.Date;

/**
 * <pre>
 * 
 *  File: AccountLine.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class AccountLine extends BaseEntity
{
    /**科目编号**/
    private String accountLineNo;
    /**科目名称**/
    private String accountLineName;
    /**科目类型**/
    private Integer accountLineType;
    /**科目级别**/
    private Integer accountLineLevel;
    /**借贷**/
    private Integer debitOrCredit;
    /**是否需要项目辅助 0为不需要 1为需要**/
    private int needProject;
    /**是否需要供应商辅助**/
    private int needSupplier;
    /**是否需要客户辅助**/
    private int needCust;
    /**是否需要账户辅助**/
    private int needAccount;
    /**是否需要税率辅助**/
    private int needTaxRate;
    /**是否需要用户辅助**/
    private int needUser;
    /**是否需要内部经营单位辅助**/
    private int needInnerBusiUnit;
    /**状态**/
    private Integer state;
    /**锁定人id**/
    private Integer lockedById;
    /**锁定人**/
    private String lockedBy;
    /**锁定时间**/
    private Date lockAt;
    /**是否末级科目**/
    private Integer isLast;
    /**科目状态 0-老科目 1-新科目**/
    private Integer accoutLineState;
	public String getNameNo(){
        return this.accountLineNo + "-" + this.accountLineName;
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
    public Integer getAccountLineLevel()
    {
        return accountLineLevel;
    }
    public void setAccountLineLevel(Integer accountLineLevel)
    {
        this.accountLineLevel = accountLineLevel;
    }
    public Integer getDebitOrCredit()
    {
        return debitOrCredit;
    }
    public void setDebitOrCredit(Integer debitOrCredit)
    {
        this.debitOrCredit = debitOrCredit;
    }
    public int getNeedProject()
    {
        return needProject;
    }
    public void setNeedProject(int needProject)
    {
        this.needProject = needProject;
    }
    public int getNeedSupplier()
    {
        return needSupplier;
    }
    public void setNeedSupplier(int needSupplier)
    {
        this.needSupplier = needSupplier;
    }
    public int getNeedCust()
    {
        return needCust;
    }
    public void setNeedCust(int needCust)
    {
        this.needCust = needCust;
    }
    public int getNeedAccount()
    {
        return needAccount;
    }
    public void setNeedAccount(int needAccount)
    {
        this.needAccount = needAccount;
    }
    public int getNeedTaxRate()
    {
        return needTaxRate;
    }
    public void setNeedTaxRate(int needTaxRate)
    {
        this.needTaxRate = needTaxRate;
    }
    public int getNeedUser()
    {
        return needUser;
    }
    public void setNeedUser(int needUser)
    {
        this.needUser = needUser;
    }
    public int getNeedInnerBusiUnit()
    {
        return needInnerBusiUnit;
    }
    public void setNeedInnerBusiUnit(int needInnerBusiUnit)
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
    public Integer getIsLast()
    {
        return isLast;
    }

    public void setIsLast(Integer isLast)
    {
        this.isLast = isLast;
    }

	public Integer getAccoutLineState() {
		return accoutLineState;
	}

	public void setAccoutLineState(Integer accoutLineState) {
		this.accoutLineState = accoutLineState;
	}

    public static String tranToNeedDec(Integer needProject , Integer needSupplier , Integer needCust, Integer needAccount , 
            Integer needTaxRate,  Integer needUser , Integer innerBusiUnit) {
        String resultStr = "";
        if (needProject.equals(BaseConsts.ONE)) {
            resultStr += "项目," ;
        }
        if (needSupplier.equals(BaseConsts.ONE)) {
            resultStr += "供应商," ;
        }
        if (needCust.equals(BaseConsts.ONE)) {
            resultStr += "客户," ;
        }
        if (needAccount.equals(BaseConsts.ONE)) {
            resultStr += "账号," ;
        }
        if (needTaxRate.equals(BaseConsts.ONE)) {
            resultStr += "税率," ;
        }
        if (needUser.equals(BaseConsts.ONE)) {
            resultStr += "人员," ;
        }
        if (innerBusiUnit.equals(BaseConsts.ONE)) {
            resultStr += "内部经营单位," ;
        }
        if(resultStr.endsWith(",")) {
            resultStr = resultStr.substring(0, resultStr.length()-1);
        }
        return resultStr;
    }
}

