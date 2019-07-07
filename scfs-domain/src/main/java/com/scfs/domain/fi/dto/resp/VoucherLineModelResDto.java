package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.fi.entity.VoucherLineModel;

/**
 * <pre>
 * 
 *  File: QueryVoucherLineResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月22日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class VoucherLineModelResDto extends VoucherLineModel
{
    private String accountLineNo;
    private String accountLineName;
    private String debitOrCreditName;
    private String currencyTypeName;
    private String projectName;
    private String supplierName;
    private String busiUnitName;
    private String custName;
    private String accountNo;
    private String userName;
    private String innerBusiUnitName;
    private BigDecimal amountUnChecked;
    
    private Integer needProject;
    private Integer needSupplier;
    private Integer needCust;
    private Integer needAccount;
    private Integer needTaxRate;
    private Integer needUser;
    private Integer needInnerBusiUnit;
    
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;
    private String billDateStr;
    private String billTypeName;
    
    private String accountBookName;	//账套
    private String standardCoinName;	//本币币种
	private String departmentName;	//部门
	private String assistDec;	//辅助项
	private Integer feeType;
	/** 创建日期 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date voucherCreateAt;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date recDate;
    
    private BigDecimal standardDebitAmount;
    private BigDecimal standardCreditAmount;
    
    public String getAccountLineName()
    {
        return accountLineName;
    }
    public void setAccountLineName(String accountLineName)
    {
        this.accountLineName = accountLineName;
    }
    public String getDebitOrCreditName()
    {
        return debitOrCreditName;
    }
    public void setDebitOrCreditName(String debitOrCreditName)
    {
        this.debitOrCreditName = debitOrCreditName;
    }
    public String getCurrencyTypeName()
    {
        return currencyTypeName;
    }
    public void setCurrencyTypeName(String currencyTypeName)
    {
        this.currencyTypeName = currencyTypeName;
    }
    public String getProjectName()
    {
        return projectName;
    }
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }
    public String getSupplierName()
    {
        return supplierName;
    }
    public void setSupplierName(String supplierName)
    {
        this.supplierName = supplierName;
    }
    public String getBusiUnitName()
    {
        return busiUnitName;
    }
    public void setBusiUnitName(String busiUnitName)
    {
        this.busiUnitName = busiUnitName;
    }
    public String getCustName()
    {
        return custName;
    }
    public void setCustName(String custName)
    {
        this.custName = custName;
    }
    public String getAccountNo()
    {
        return accountNo;
    }
    public void setAccountNo(String accountNo)
    {
        this.accountNo = accountNo;
    }
    public String getUserName()
    {
        return userName;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    public String getInnerBusiUnitName()
    {
        return innerBusiUnitName;
    }
    public void setInnerBusiUnitName(String innerBusiUnitName)
    {
        this.innerBusiUnitName = innerBusiUnitName;
    }
    public BigDecimal getAmountUnChecked()
    {
        return amountUnChecked;
    }
    public void setAmountUnChecked(BigDecimal amountUnChecked)
    {
        this.amountUnChecked = amountUnChecked;
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
    public BigDecimal getDebitAmount()
    {
        return debitAmount;
    }
    public void setDebitAmount(BigDecimal debitAmount)
    {
        this.debitAmount = debitAmount;
    }
    public BigDecimal getCreditAmount()
    {
        return creditAmount;
    }
    public void setCreditAmount(BigDecimal creditAmount)
    {
        this.creditAmount = creditAmount;
    }
    public String getBillDateStr()
    {
        return billDateStr;
    }
    public void setBillDateStr(String billDateStr)
    {
        this.billDateStr = billDateStr;
    }
    public String getBillTypeName()
    {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName)
    {
        this.billTypeName = billTypeName;
    }
	public String getAccountBookName() {
		return accountBookName;
	}
	public void setAccountBookName(String accountBookName) {
		this.accountBookName = accountBookName;
	}
	public String getStandardCoinName() {
		return standardCoinName;
	}
	public void setStandardCoinName(String standardCoinName) {
		this.standardCoinName = standardCoinName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getAccountLineNo() {
		return accountLineNo;
	}
	public void setAccountLineNo(String accountLineNo) {
		this.accountLineNo = accountLineNo;
	}
	public String getAssistDec() {
		return assistDec;
	}
	public void setAssistDec(String assistDec) {
		this.assistDec = assistDec;
	}
    public Integer getFeeType()
    {
        return feeType;
    }
    public void setFeeType(Integer feeType)
    {
        this.feeType = feeType;
    }
    public Date getVoucherCreateAt()
    {
        return voucherCreateAt;
    }
    public void setVoucherCreateAt(Date voucherCreateAt)
    {
        this.voucherCreateAt = voucherCreateAt;
    }
    public Date getRecDate()
    {
        return recDate;
    }
    public void setRecDate(Date recDate)
    {
        this.recDate = recDate;
    }
	public BigDecimal getStandardDebitAmount() {
		return standardDebitAmount;
	}
	public void setStandardDebitAmount(BigDecimal standardDebitAmount) {
		this.standardDebitAmount = standardDebitAmount;
	}
	public BigDecimal getStandardCreditAmount() {
		return standardCreditAmount;
	}
	public void setStandardCreditAmount(BigDecimal standardCreditAmount) {
		this.standardCreditAmount = standardCreditAmount;
	}
    
}

