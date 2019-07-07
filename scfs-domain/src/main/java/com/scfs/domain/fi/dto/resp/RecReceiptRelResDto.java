package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 
 *  File: RecReceiptRelResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月1日				Administrator
 *
 * </pre>
 */
public class RecReceiptRelResDto {
	private Integer id;
	/** 水单id **/
	private Integer receiptId;
	/** 应收id **/
	private Integer recId;

	/** 项目名称 **/
	private String projectName;
	/** 经营单位 **/
	private String busiUnit;
	/** 客户名称 **/
	private String custName;
	private String currencyTypeName;
	private BigDecimal writeOffAmount;
	/** 应收金额 **/
	private BigDecimal amountReceivable;
	/** 已收金额 ， 水单核销总额 **/
	private BigDecimal amountReceived;
	/** 应收余额 **/
	private BigDecimal blance;
	
	/** 系统生成的水单编号 **/
    private String receiptNo;
    /** 水单备注 **/
    private String receiptNote;
    /** 水单日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receiptDate;
    /** 银行水单号 **/
    private String bankReceiptNo;
    /** 收款账号 **/
    private String recAccountNo;
    /** 水单金额 **/
    private BigDecimal receiptAmount;
    /** 水单类型 1 回款 2 预收定金 3 预收货款 4融资  5内部 **/
    private Integer receiptType;
    private String receiptTypeName;
    /** 币种 **/
    private Integer receiptCurrencyType;
    /** 摘要 **/
    private String summary;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expireDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(String busiUnit) {
		this.busiUnit = busiUnit;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getWriteOffAmount() {
		return writeOffAmount;
	}

	public void setWriteOffAmount(BigDecimal writeOffAmount) {
		this.writeOffAmount = writeOffAmount;
	}

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
	}

	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}

	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}

	public BigDecimal getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}

	public Integer getRecId() {
		return recId;
	}

	public void setRecId(Integer recId) {
		this.recId = recId;
	}

    public String getReceiptNo()
    {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo)
    {
        this.receiptNo = receiptNo;
    }

    public String getReceiptNote()
    {
        return receiptNote;
    }

    public void setReceiptNote(String receiptNote)
    {
        this.receiptNote = receiptNote;
    }

    public Date getReceiptDate()
    {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate)
    {
        this.receiptDate = receiptDate;
    }

    public String getBankReceiptNo()
    {
        return bankReceiptNo;
    }

    public void setBankReceiptNo(String bankReceiptNo)
    {
        this.bankReceiptNo = bankReceiptNo;
    }

    public BigDecimal getReceiptAmount()
    {
        return receiptAmount;
    }

    public void setReceiptAmount(BigDecimal receiptAmount)
    {
        this.receiptAmount = receiptAmount;
    }

    public Integer getReceiptType()
    {
        return receiptType;
    }

    public void setReceiptType(Integer receiptType)
    {
        this.receiptType = receiptType;
    }

    public Integer getReceiptCurrencyType()
    {
        return receiptCurrencyType;
    }

    public void setReceiptCurrencyType(Integer receiptCurrencyType)
    {
        this.receiptCurrencyType = receiptCurrencyType;
    }

    public String getSummary()
    {
        return summary;
    }

    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    public String getRecAccountNo()
    {
        return recAccountNo;
    }

    public void setRecAccountNo(String recAccountNo)
    {
        this.recAccountNo = recAccountNo;
    }

    public String getReceiptTypeName()
    {
        return receiptTypeName;
    }

    public void setReceiptTypeName(String receiptTypeName)
    {
        this.receiptTypeName = receiptTypeName;
    }

    public Date getExpireDate()
    {
        return expireDate;
    }

    public void setExpireDate(Date expireDate)
    {
        this.expireDate = expireDate;
    }

}
