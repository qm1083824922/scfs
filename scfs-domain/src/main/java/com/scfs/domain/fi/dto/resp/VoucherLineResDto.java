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
 *  File: VoucherLineResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator			
 *
 * </pre>
 */
public class VoucherLineResDto
{
    private Integer projectId;
    private String projectName;
    private Integer custId;
    private String custName;
    private Integer busiUnit;
    private String busiUnitName;
    private Integer currencyType;
    private String currencyTypeName;
    private BigDecimal amount; //对账金额(项目和客户维度总金额)
    private BigDecimal amountChecked; //已对账金额 
    private BigDecimal amountUnChecked; //未对账金额
    private String billNo;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date billDate;
    private String billTypeName;
    private String createDateRange;
    
    /**操作集合*/
    private List<CodeValue> opertaList;
    
    public static class Operate{
        public static Map<String,String> operMap = new HashMap<String,String>();
        static {
            operMap.put(OperateConsts.CHECK_BILL, BusUrlConsts.ADD_REC_DETAIL);  //对账,新增应收明细
            operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_CHECK_BILL_INFO);  //对账时，查询出待对账明细
        }
    }
    
    public Integer getProjectId()
    {
        return projectId;
    }
    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }
    public Integer getCustId()
    {
        return custId;
    }
    public void setCustId(Integer custId)
    {
        this.custId = custId;
    }
    public Integer getBusiUnit()
    {
        return busiUnit;
    }
    public void setBusiUnit(Integer busiUnit)
    {
        this.busiUnit = busiUnit;
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
    public Integer getCurrencyType()
    {
        return currencyType;
    }
    public void setCurrencyType(Integer currencyType)
    {
        this.currencyType = currencyType;
    }
    public String getCurrencyTypeName()
    {
        return currencyTypeName;
    }
    public void setCurrencyTypeName(String currencyTypeName)
    {
        this.currencyTypeName = currencyTypeName;
    }
    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    public BigDecimal getAmountChecked()
    {
        return amountChecked;
    }
    public void setAmountChecked(BigDecimal amountChecked)
    {
        this.amountChecked = amountChecked;
    }
    public BigDecimal getAmountUnChecked()
    {
        return amountUnChecked;
    }
    public void setAmountUnChecked(BigDecimal amountUnChecked)
    {
        this.amountUnChecked = amountUnChecked;
    }
    public String getBillNo()
    {
        return billNo;
    }
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }
    public Date getBillDate()
    {
        return billDate;
    }
    public void setBillDate(Date billDate)
    {
        this.billDate = billDate;
    }
    public String getBillTypeName()
    {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName)
    {
        this.billTypeName = billTypeName;
    }
    public String getCreateDateRange()
    {
        return createDateRange;
    }
    public void setCreateDateRange(String createDateRange)
    {
        this.createDateRange = createDateRange;
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

