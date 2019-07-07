package com.scfs.domain.fi.dto.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;
/**
 * <pre>
 * 
 *  File: ReceiveSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator			
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class ReceiveSearchReqDto extends BaseReqDto
{
    private Integer projectId;
    private Integer custId;
    private Integer busiUnit;
    private Integer billType;
    private String billNo;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startBillDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endBillDate;
    private Integer currencyType;
    /**
     * 1.未收 2.未核完 3.已核完 4.待核销(包含1,2)
     */
    private Integer searchType;
    private Integer orderType; //1:按id排序 2:按应收到期日排序
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startCheckDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endCheckDate;
    
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date startExpireDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date endExpireDate;
    private Integer feeId;
	private Integer outStoreId;
	/** 部门 **/
	private List<Integer> departmentId;
	
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
    public Integer getBillType()
    {
        return billType;
    }
    public void setBillType(Integer billType)
    {
        this.billType = billType;
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
    public Integer getCurrencyType()
    {
        return currencyType;
    }
    public void setCurrencyType(Integer currencyType)
    {
        this.currencyType = currencyType;
    }
    public Integer getSearchType()
    {
        return searchType;
    }
    public void setSearchType(Integer searchType)
    {
        this.searchType = searchType;
    }
    public Integer getOrderType()
    {
        return orderType;
    }
    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }
    public Date getStartCheckDate()
    {
        return startCheckDate;
    }
    public void setStartCheckDate(Date startCheckDate)
    {
        this.startCheckDate = startCheckDate;
    }
    public Date getEndCheckDate()
    {
        return endCheckDate;
    }
    public void setEndCheckDate(Date endCheckDate)
    {
        this.endCheckDate = endCheckDate;
    }
    public Date getStartExpireDate()
    {
        return startExpireDate;
    }
    public void setStartExpireDate(Date startExpireDate)
    {
        this.startExpireDate = startExpireDate;
    }
    public Date getEndExpireDate()
    {
        return endExpireDate;
    }
    public void setEndExpireDate(Date endExpireDate)
    {
        this.endExpireDate = endExpireDate;
    }
	public Integer getFeeId() {
		return feeId;
	}
	public void setFeeId(Integer feeId) {
		this.feeId = feeId;
	}
	public Integer getOutStoreId() {
		return outStoreId;
	}
	public void setOutStoreId(Integer outStoreId) {
		this.outStoreId = outStoreId;
	}
	public List<Integer> getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(List<Integer> departmentId) {
		this.departmentId = departmentId;
	}
	
}

