package com.scfs.domain.fee.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: FeeQueryModel.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月13日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FeeQueryModel extends BaseEntity
{
    /**  费用编号     **/
    private String feeNo;
    /**  币种     **/
    private Integer currencyType;
    /**  费用类型     **/
    private Integer feeType;
    /**  项目     **/
    private Integer projectId;
    /**  状态     **/
    private Integer state;
    /**  经营单位**/
    private Integer busiUnit;
    
    /**  应收客户     **/
    private Integer custPayer;
    /**  应收费用科目     **/
    private Integer recFeeSpec;
    /**  应收辅助科目     **/
    private Integer recAssistFeeSpec;
    /**  应收方式     **/
    private Integer recType;
    /**  应收日期     **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date recDate;
    /**  已收金额     **/
    private BigDecimal receivedAmount;
    /**  应收金额     **/
    private BigDecimal recAmount;
    /**  开票金额     **/
    private BigDecimal provideInvoiceAmount;
    /**  开票方式     **/
    private Integer provideInvoiceType;
    /**  开票税率     **/
    private BigDecimal provideInvoiceTaxRate;
    /**  应收备注    **/
    private String recNote;
    
    /**  应付客户     **/
    private Integer custReceiver;
    /**  应付费用科目     **/
    private Integer payFeeSpec;
    /**  应付辅助科目     **/
    private Integer payAssistFeeSpec;
    /**  支付方式     **/
    private Integer payType;
    /**  状态     **/
    private Integer payFeeType;
    /**  应付日期     **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date payDate;
    /** 到期日     **/
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date expireDate;
    /**  已付金额    **/
    private BigDecimal paidAmount;
    /**  应付金额    **/
    private BigDecimal payAmount;
    /**  收票金额     **/
    private BigDecimal acceptInvoiceAmount;
    /**  收票方式     **/
    private Integer acceptInvoiceType;
    /**  收票税率     **/
    private BigDecimal acceptInvoiceTaxRate;
    /**  应付备注     **/
    private String payNote;
    
    
    /**项目名称**/
    private String projectName;
    /**应收费用科目名称**/
    private String recFeeSpecName;
    /**应收客户名称**/
    private String custPayerName;
    /**应付客户名称**/
    private String custReceiverName;
    /**应付费用科目名称**/
    private String payFeeSpecName;
    /**经营单位名称**/
    private String busiUnitName;
    /**抵扣活动名称**/
    private String deductionTypeName;
    /**抵扣活动类型*/
    private Integer deductionType;
	public Integer getDeductionType() {
		return deductionType;
	}
	public void setDeductionType(Integer deductionType) {
		this.deductionType = deductionType;
	}
	public String getFeeNo()
    {
        return feeNo;
    }
    public void setFeeNo(String feeNo)
    {
        this.feeNo = feeNo;
    }
    public Integer getCurrencyType()
    {
        return currencyType;
    }
    public void setCurrencyType(Integer currencyType)
    {
        this.currencyType = currencyType;
    }
    public Integer getFeeType()
    {
        return feeType;
    }
    public void setFeeType(Integer feeType)
    {
        this.feeType = feeType;
    }
    public Integer getProjectId()
    {
        return projectId;
    }
    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
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
    public Integer getCustPayer()
    {
        return custPayer;
    }
    public void setCustPayer(Integer custPayer)
    {
        this.custPayer = custPayer;
    }
    public Integer getRecFeeSpec()
    {
        return recFeeSpec;
    }
    public void setRecFeeSpec(Integer recFeeSpec)
    {
        this.recFeeSpec = recFeeSpec;
    }
    public Integer getRecAssistFeeSpec()
    {
        return recAssistFeeSpec;
    }
    public void setRecAssistFeeSpec(Integer recAssistFeeSpec)
    {
        this.recAssistFeeSpec = recAssistFeeSpec;
    }
    public Integer getRecType()
    {
        return recType;
    }
    public void setRecType(Integer recType)
    {
        this.recType = recType;
    }
    public Date getRecDate()
    {
        return recDate;
    }
    public void setRecDate(Date recDate)
    {
        this.recDate = recDate;
    }
    public BigDecimal getReceivedAmount()
    {
        return receivedAmount;
    }
    public void setReceivedAmount(BigDecimal receivedAmount)
    {
        this.receivedAmount = receivedAmount;
    }
    public BigDecimal getRecAmount()
    {
        return recAmount;
    }
    public void setRecAmount(BigDecimal recAmount)
    {
        this.recAmount = recAmount;
    }
    public BigDecimal getProvideInvoiceAmount()
    {
        return provideInvoiceAmount;
    }
    public void setProvideInvoiceAmount(BigDecimal provideInvoiceAmount)
    {
        this.provideInvoiceAmount = provideInvoiceAmount;
    }
    public Integer getProvideInvoiceType()
    {
        return provideInvoiceType;
    }
    public void setProvideInvoiceType(Integer provideInvoiceType)
    {
        this.provideInvoiceType = provideInvoiceType;
    }
    public BigDecimal getProvideInvoiceTaxRate()
    {
        return provideInvoiceTaxRate;
    }
    public void setProvideInvoiceTaxRate(BigDecimal provideInvoiceTaxRate)
    {
        this.provideInvoiceTaxRate = provideInvoiceTaxRate;
    }
    public String getRecNote()
    {
        return recNote;
    }
    public void setRecNote(String recNote)
    {
        this.recNote = recNote;
    }
    public Integer getCustReceiver()
    {
        return custReceiver;
    }
    public void setCustReceiver(Integer custReceiver)
    {
        this.custReceiver = custReceiver;
    }
    public Integer getPayFeeSpec()
    {
        return payFeeSpec;
    }
    public void setPayFeeSpec(Integer payFeeSpec)
    {
        this.payFeeSpec = payFeeSpec;
    }
    public Integer getPayAssistFeeSpec()
    {
        return payAssistFeeSpec;
    }
    public void setPayAssistFeeSpec(Integer payAssistFeeSpec)
    {
        this.payAssistFeeSpec = payAssistFeeSpec;
    }
    public Integer getPayType()
    {
        return payType;
    }
    public void setPayType(Integer payType)
    {
        this.payType = payType;
    }
    public Date getPayDate()
    {
        return payDate;
    }
    public void setPayDate(Date payDate)
    {
        this.payDate = payDate;
    }
    public BigDecimal getPaidAmount()
    {
        return paidAmount;
    }
    public void setPaidAmount(BigDecimal paidAmount)
    {
        this.paidAmount = paidAmount;
    }
    public BigDecimal getPayAmount()
    {
        return payAmount;
    }
    public void setPayAmount(BigDecimal payAmount)
    {
        this.payAmount = payAmount;
    }
    public BigDecimal getAcceptInvoiceAmount()
    {
        return acceptInvoiceAmount;
    }
    public void setAcceptInvoiceAmount(BigDecimal acceptInvoiceAmount)
    {
        this.acceptInvoiceAmount = acceptInvoiceAmount;
    }
    public Integer getAcceptInvoiceType()
    {
        return acceptInvoiceType;
    }
    public void setAcceptInvoiceType(Integer acceptInvoiceType)
    {
        this.acceptInvoiceType = acceptInvoiceType;
    }
    public BigDecimal getAcceptInvoiceTaxRate()
    {
        return acceptInvoiceTaxRate;
    }
    public void setAcceptInvoiceTaxRate(BigDecimal acceptInvoiceTaxRate)
    {
        this.acceptInvoiceTaxRate = acceptInvoiceTaxRate;
    }
    public String getPayNote()
    {
        return payNote;
    }
    public void setPayNote(String payNote)
    {
        this.payNote = payNote;
    }
    public String getProjectName()
    {
        return projectName;
    }
    public void setProjectName(String projectName)
    {
        this.projectName = projectName;
    }
    public String getRecFeeSpecName()
    {
        return recFeeSpecName;
    }
    public void setRecFeeSpecName(String recFeeSpecName)
    {
        this.recFeeSpecName = recFeeSpecName;
    }
    public String getCustPayerName()
    {
        return custPayerName;
    }
    public void setCustPayerName(String custPayerName)
    {
        this.custPayerName = custPayerName;
    }
    public String getCustReceiverName()
    {
        return custReceiverName;
    }
    public void setCustReceiverName(String custReceiverName)
    {
        this.custReceiverName = custReceiverName;
    }
    public String getPayFeeSpecName()
    {
        return payFeeSpecName;
    }
    public void setPayFeeSpecName(String payFeeSpecName)
    {
        this.payFeeSpecName = payFeeSpecName;
    }
    public String getBusiUnitName()
    {
        return busiUnitName;
    }
    public void setBusiUnitName(String busiUnitName)
    {
        this.busiUnitName = busiUnitName;
    } 
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Integer getPayFeeType() {
		return payFeeType;
	}
	public void setPayFeeType(Integer payFeeType) {
		this.payFeeType = payFeeType;
	}
    
    public String getDeductionTypeName() {
		return deductionTypeName;
	}
	public void setDeductionTypeName(String deductionTypeName) {
		this.deductionTypeName = deductionTypeName;
	}
}

