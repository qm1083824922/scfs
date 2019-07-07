package com.scfs.domain.fi.dto.req;

import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: VoucherLineSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月25日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class VoucherLineSearchReqDto extends BaseReqDto
{
    //private String[] noStrings; 
    private Integer projectId;
    private Integer custId;
    private Integer busiUnit;
    private Integer currencyType;
    private String billNo;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startBillDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endBillDate;
    private Integer debitOrCredit;
    private Integer voucherId;
    private String accountLineNo;
    private BigDecimal taxRate;
    
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;
	private Integer accountBookId;
    @DateTimeFormat(pattern="yyyy-MM-dd")
	private Date voucherDate;
	
    /**
     * 1.查询未对账分录 2.查询已对账分录 3.查询已对完分录 4.待对账分录(包含未对账和已对账)
     */
    private Integer searchType;
    private Integer statisticsDimension; //1:项目+币种 2:客户+币种 3.项目+客户+币种
    private Integer isPage; //是否分页
    
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
    public Integer getCurrencyType()
    {
        return currencyType;
    }
    public void setCurrencyType(Integer currencyType)
    {
        this.currencyType = currencyType;
    }
    public String getBillNo()
    {
        return billNo;
    }
    public void setBillNo(String billNo)
    {
        this.billNo = billNo;
    }
    public Date getStartBillDate()
    {
        return startBillDate;
    }
    public void setStartBillDate(Date startBillDate)
    {
        this.startBillDate = startBillDate;
    }
    public Date getEndBillDate()
    {
        return endBillDate;
    }
    public void setEndBillDate(Date endBillDate)
    {
        this.endBillDate = endBillDate;
    }
    public Integer getSearchType()
    {
        return searchType;
    }
    public void setSearchType(Integer searchType)
    {
        this.searchType = searchType;
    }
    public Integer getDebitOrCredit()
    {
        return debitOrCredit;
    }
    public void setDebitOrCredit(Integer debitOrCredit)
    {
        this.debitOrCredit = debitOrCredit;
    }
    public Integer getVoucherId()
    {
        return voucherId;
    }
    public void setVoucherId(Integer voucherId)
    {
        this.voucherId = voucherId;
    }
    public Integer getStatisticsDimension()
    {
        return statisticsDimension;
    }
    public void setStatisticsDimension(Integer statisticsDimension)
    {
        this.statisticsDimension = statisticsDimension;
    }
    public Integer getIsPage()
    {
        return isPage;
    }
    public void setIsPage(Integer isPage)
    {
        this.isPage = isPage;
    }
	public String getAccountLineNo() {
		return accountLineNo;
	}
	public void setAccountLineNo(String accountLineNo) {
		this.accountLineNo = accountLineNo;
	}
	public BigDecimal getTaxRate() {
		return taxRate;
	}
	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public Integer getAccountBookId() {
		return accountBookId;
	}
	public void setAccountBookId(Integer accountBookId) {
		this.accountBookId = accountBookId;
	}
	public Date getVoucherDate() {
		return voucherDate;
	}
	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

    
}

