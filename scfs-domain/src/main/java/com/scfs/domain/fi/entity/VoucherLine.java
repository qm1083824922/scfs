package com.scfs.domain.fi.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: VoucherLine.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月17日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class VoucherLine extends BaseEntity
{
    /**凭证id 关联tb_fi_voucher{id} **/
    private Integer voucherId;
    
    /**科目id 关联tb_fi_account_line{id} **/
    private Integer accountLineId;
    
    /**经营单位id 关联tb_base_subject{id} **/
    private Integer busiUnit;
    
    /**借贷 **/
    private Integer debitOrCredit;
    
    /**币种 **/
    private Integer currencyType;
    
    /**金额 **/
    private BigDecimal amount;
    
    /**人民币金额 **/
    private BigDecimal cnyAmount;
    
    /**已对账金额**/
    private BigDecimal amountChecked;
    
    /**汇率 **/
    private BigDecimal exchangeRate;
    
    /**本币币种 **/
    private Integer standardCoin;
    
    /**本币金额 **/
    private BigDecimal standardAmount;
    
    /**原币转本币汇率 **/
    private BigDecimal standardRate;
    
    /**项目id 关联tb_base_project{id} **/
    private Integer projectId;
    
    /**供应商id 关联tb_base_subject{id} **/
    private Integer supplierId;
    
    /**客户id 关联tb_base_subject{id} **/
    private Integer custId;
    
    /**账户id 关联tb_base_account{id} **/
    private Integer accountId;
    
    /**人员id 关联tb_base_user{id} **/
    private Integer userId;
    
    /**内部经营单位id 关联tb_base_subject{id} **/
    private Integer innerBusiUnitId;
    
    /**税率  **/
    private BigDecimal taxRate;
    
    /**摘要 **/
    private String voucherLineSummary;
    
    /**费用单据id **/
    private Integer feeId;
    
    /**出库单据id **/
    private Integer outStoreId;
    
    /**入库单据id **/
    private Integer inStoreId;
    
    /**付款单据id **/
    private Integer payId;
    
    /**收票单据id **/
    private Integer acceptInvoiceId;
    
    /**开票单据id **/
    private Integer provideInvoiceId;
    
    /**水单单据id **/
    private Integer receiptId;
   
    /**收票认证id **/
    private Integer invoiceCollectApproveId;
    
    /**单据类型**/
    private Integer billType;
    
    /**单据日期**/
    private Date billDate;
    
    /**单据编号**/
    private String billNo;
    
    /**扩展字段**/
    private Date maxCreateAt;
    private Date minCreateAt;
    
    public Integer getVoucherId()
    {
        return voucherId;
    }
    public void setVoucherId(Integer voucherId)
    {
        this.voucherId = voucherId;
    }
    public Integer getAccountLineId()
    {
        return accountLineId;
    }
    public void setAccountLineId(Integer accountLineId)
    {
        this.accountLineId = accountLineId;
    }
    public Integer getBusiUnit()
    {
        return busiUnit;
    }
    public void setBusiUnit(Integer busiUnit)
    {
        this.busiUnit = busiUnit;
    }
    public Integer getDebitOrCredit()
    {
        return debitOrCredit;
    }
    public void setDebitOrCredit(Integer debitOrCredit)
    {
        this.debitOrCredit = debitOrCredit;
    }
    public Integer getCurrencyType()
    {
        return currencyType;
    }
    public void setCurrencyType(Integer currencyType)
    {
        this.currencyType = currencyType;
    }
    public BigDecimal getAmount()
    {
        return amount;
    }
    public void setAmount(BigDecimal amount)
    {
        this.amount = amount;
    }
    public BigDecimal getCnyAmount()
    {
        return cnyAmount;
    }
    public void setCnyAmount(BigDecimal cnyAmount)
    {
        this.cnyAmount = cnyAmount;
    }
    public BigDecimal getAmountChecked()
    {
        return amountChecked;
    }
    public void setAmountChecked(BigDecimal amountChecked)
    {
        this.amountChecked = amountChecked;
    }
    public BigDecimal getExchangeRate()
    {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate)
    {
        this.exchangeRate = exchangeRate;
    }
    public Integer getProjectId()
    {
        return projectId;
    }
    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }
    public Integer getSupplierId()
    {
        return supplierId;
    }
    public void setSupplierId(Integer supplierId)
    {
        this.supplierId = supplierId;
    }
    public Integer getCustId()
    {
        return custId;
    }
    public void setCustId(Integer custId)
    {
        this.custId = custId;
    }
    public Integer getAccountId()
    {
        return accountId;
    }
    public void setAccountId(Integer accountId)
    {
        this.accountId = accountId;
    }
    public BigDecimal getTaxRate()
    {
        return taxRate;
    }
    public void setTaxRate(BigDecimal taxRate)
    {
        this.taxRate = taxRate;
    }
    public Integer getUserId()
    {
        return userId;
    }
    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }
    public Integer getInnerBusiUnitId()
    {
        return innerBusiUnitId;
    }
    public void setInnerBusiUnitId(Integer innerBusiUnitId)
    {
        this.innerBusiUnitId = innerBusiUnitId;
    }
    public String getVoucherLineSummary()
    {
        return voucherLineSummary;
    }
    public void setVoucherLineSummary(String voucherLineSummary)
    {
        this.voucherLineSummary = voucherLineSummary;
    }
    public Integer getFeeId()
    {
        return feeId;
    }
    public void setFeeId(Integer feeId)
    {
        this.feeId = feeId;
    }
    public Integer getOutStoreId()
    {
        return outStoreId;
    }
    public void setOutStoreId(Integer outStoreId)
    {
        this.outStoreId = outStoreId;
    }
    public Integer getInStoreId()
    {
        return inStoreId;
    }
    public void setInStoreId(Integer inStoreId)
    {
        this.inStoreId = inStoreId;
    }
    public Integer getPayId()
    {
        return payId;
    }
    public void setPayId(Integer payId)
    {
        this.payId = payId;
    }
    public Integer getAcceptInvoiceId()
    {
        return acceptInvoiceId;
    }
    public void setAcceptInvoiceId(Integer acceptInvoiceId)
    {
        this.acceptInvoiceId = acceptInvoiceId;
    }
    public Integer getProvideInvoiceId()
    {
        return provideInvoiceId;
    }
    public void setProvideInvoiceId(Integer provideInvoiceId)
    {
        this.provideInvoiceId = provideInvoiceId;
    }
    public Integer getReceiptId()
    {
        return receiptId;
    }
    public void setReceiptId(Integer receiptId)
    {
        this.receiptId = receiptId;
    }
    public Integer getBillType()
    {
        return billType;
    }
    public void setBillType(Integer billType)
    {
        this.billType = billType;
    }
    public Date getBillDate()
    {
        return billDate;
    }
    public void setBillDate(Date billDate)
    {
        this.billDate = billDate;
    }
    public String getBillNo()
    {
        return billNo;
    }
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }
    public Date getMaxCreateAt()
    {
        return maxCreateAt;
    }
    public void setMaxCreateAt(Date maxCreateAt)
    {
        this.maxCreateAt = maxCreateAt;
    }
    public Date getMinCreateAt()
    {
        return minCreateAt;
    }
    public void setMinCreateAt(Date minCreateAt)
    {
        this.minCreateAt = minCreateAt;
    }
	public Integer getInvoiceCollectApproveId() {
		return invoiceCollectApproveId;
	}
	public void setInvoiceCollectApproveId(Integer invoiceCollectApproveId) {
		this.invoiceCollectApproveId = invoiceCollectApproveId;
	}
	public Integer getStandardCoin() {
		return standardCoin;
	}
	public void setStandardCoin(Integer standardCoin) {
		this.standardCoin = standardCoin;
	}
	public BigDecimal getStandardAmount() {
		return standardAmount;
	}
	public void setStandardAmount(BigDecimal standardAmount) {
		this.standardAmount = standardAmount;
	}
	public BigDecimal getStandardRate() {
		return standardRate;
	}
	public void setStandardRate(BigDecimal standardRate) {
		this.standardRate = standardRate;
	}
    
    
}

