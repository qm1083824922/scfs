package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
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
 *  File: AccountStatementTitleResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月22日			Administrator			
 *
 * </pre>
 */
public class AccountStatementTitleResDto 
{
    private Integer id;
    private Integer projectId;
    private String billNo;
    private String billAttachNo;
    private Integer custId;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date astStartDate;
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date astEndDate;
    private Integer currencyType;
    private BigDecimal projectAmount;
    private BigDecimal totalAmount;
    private BigDecimal inUseAmount;
    private BigDecimal inStoreAmount;
    private BigDecimal onWayAmount;
    private BigDecimal lendAmount;
    private Integer state;
    private String note;
    private String creator;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS",timezone="GMT+8")
    private Date createAt;
    
    private String projectName;
    private String custName;
    private String busiUnitName;
    private String currencyTypeName;
    private String stateName;
    
    
    /**操作集合*/
    private List<CodeValue> opertaList;
    
    public static class Operate{
        public static Map<String,String> operMap = new HashMap<String,String>();
        static {
            
            operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_ACCOUNT_STATEMENT_TITLE);
            operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_ACCOUNT_STATEMENT_TITLE);
            operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_ACCOUNT_STATEMENT_TITLE);
            operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_ACCOUNT_STATEMENT_TITLE);
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
    public Integer getProjectId()
    {
        return projectId;
    }
    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }
    public String getBillNo()
    {
        return billNo;
    }
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }
    public String getBillAttachNo()
    {
        return billAttachNo;
    }
    public void setBillAttachNo(String billAttachNo)
    {
        this.billAttachNo = billAttachNo;
    }
    public Integer getCustId()
    {
        return custId;
    }
    public void setCustId(Integer custId)
    {
        this.custId = custId;
    }
    public Date getAstStartDate()
    {
        return astStartDate;
    }
    public void setAstStartDate(Date astStartDate)
    {
        this.astStartDate = astStartDate;
    }
    public Date getAstEndDate()
    {
        return astEndDate;
    }
    public void setAstEndDate(Date astEndDate)
    {
        this.astEndDate = astEndDate;
    }
    public Integer getCurrencyType()
    {
        return currencyType;
    }
    public void setCurrencyType(Integer currencyType)
    {
        this.currencyType = currencyType;
    }
    public BigDecimal getProjectAmount()
    {
        return projectAmount;
    }
    public void setProjectAmount(BigDecimal projectAmount)
    {
        this.projectAmount = projectAmount;
    }
    public BigDecimal getTotalAmount()
    {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount)
    {
        this.totalAmount = totalAmount;
    }
    public BigDecimal getInUseAmount()
    {
        return inUseAmount;
    }
    public void setInUseAmount(BigDecimal inUseAmount)
    {
        this.inUseAmount = inUseAmount;
    }
    public BigDecimal getInStoreAmount()
    {
        return inStoreAmount;
    }
    public void setInStoreAmount(BigDecimal inStoreAmount)
    {
        this.inStoreAmount = inStoreAmount;
    }
    public BigDecimal getOnWayAmount()
    {
        return onWayAmount;
    }
    public void setOnWayAmount(BigDecimal onWayAmount)
    {
        this.onWayAmount = onWayAmount;
    }
    public BigDecimal getLendAmount()
    {
        return lendAmount;
    }
    public void setLendAmount(BigDecimal lendAmount)
    {
        this.lendAmount = lendAmount;
    }
    public Integer getState()
    {
        return state;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }
    public String getNote()
    {
        return note;
    }
    public void setNote(String note)
    {
        this.note = note;
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
    public String getProjectName()
    {
        return projectName;
    }
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }
    public String getCustName()
    {
        return custName;
    }
    public void setCustName(String custName)
    {
        this.custName = custName;
    }
    public String getBusiUnitName()
    {
        return busiUnitName;
    }
    public void setBusiUnitName(String busiUnitName)
    {
        this.busiUnitName = busiUnitName;
    }
    public String getCurrencyTypeName()
    {
        return currencyTypeName;
    }
    public void setCurrencyTypeName(String currencyTypeName)
    {
        this.currencyTypeName = currencyTypeName;
    }
    public String getStateName()
    {
        return stateName;
    }
    public void setStateName(String stateName)
    {
        this.stateName = stateName;
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

