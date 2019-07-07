package com.scfs.domain.fee.entity;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 	管理费用基本信息
 *  File: FeeManage.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年04月12日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FeeManage extends BaseEntity {
    
    private List<Integer> ids;
    /** 费用科目类型 **/
    private Integer feeType;
    /** 分摊日期 **/
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date shareDate;
    /** 管理费用id **/
	private Integer id;
	/** 管理费用编号 **/
	private String feeManageNo;
	/** 部门id **/
	private Integer departmentId;
	/** 项目id **/
	private Integer projectId;
	/** 用户id **/
	private Integer userId;
	/** 客户id **/
	private Integer custId;
	/** 费用科目id **/
	private Integer feeSpecId;
	/** 应付方式 **/
	private Integer recType;
	/** 日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	/** 币种 **/
	private Integer currnecyType;
	/** 管理费用金额 **/
	private String amount;
	/** 分摊金额 **/
	private String shareAmount;
	/** 状态 0 未提交 **/
	private Integer state;
	/** 备注 **/
	private String remark;
	public Date getShareDate()
    {
        return shareDate;
    }
    
    public void setShareDate(Date shareDate)
    {
        this.shareDate = shareDate;
    }
	public Integer getFeeType()
    {
        return feeType;
    }
    public void setFeeType(Integer feeType)
    {
        this.feeType = feeType;
    }
	public List<Integer> getIds()
    {
        return ids;
    }
    
    public void setIds(List<Integer> ids)
    {
        this.ids = ids;
    }
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFeeManageNo() {
		return feeManageNo;
	}

	public void setFeeManageNo(String feeManageNo) {
		this.feeManageNo = feeManageNo;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getFeeSpecId() {
		return feeSpecId;
	}

	public void setFeeSpecId(Integer feeSpecId) {
		this.feeSpecId = feeSpecId;
	}

	public Integer getRecType() {
		return recType;
	}

	public void setRecType(Integer recType) {
		this.recType = recType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(String shareAmount) {
		this.shareAmount = shareAmount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
