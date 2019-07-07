package com.scfs.domain.fee.dto.req;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.fee.entity.FeeShare;

/**
 * <pre>
 * 	管理费用分摊
 *  File: FeeShareReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月17日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FeeShareReqDto extends BaseReqDto {
	private Integer manageId;

	List<FeeShare> feeShares;
	
	   /** 项目id **/
    private Integer projectId;
    /** 用户id **/
    private Integer userId;
    /** 客户id **/
    private Integer custId;
    /**创建时间*/
    private Date createAt;
    /**日期*/
    private Date shareDate;
    /**统计开始日期*/
    private Date startShareDate;
    /**统计结束日期*/
    private Date endShareDate;
    
    
    public Date getStartShareDate()
    {
        return startShareDate;
    }

    
    public void setStartShareDate(Date startShareDate)
    {
        this.startShareDate = startShareDate;
    }

    
    public Date getEndShareDate()
    {
        return endShareDate;
    }

    
    public void setEndShareDate(Date endShareDate)
    {
        this.endShareDate = endShareDate;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }
  
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
  
    
    public Integer getProjectId()
    {
        return projectId;
    }

    
    public void setProjectId(Integer projectId)
    {
        this.projectId = projectId;
    }

    
    public Integer getUserId()
    {
        return userId;
    }

    
    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    
    public Integer getCustId()
    {
        return custId;
    }

    
    public void setCustId(Integer custId)
    {
        this.custId = custId;
    }

    public Integer getManageId() {
		return manageId;
	}

	public void setManageId(Integer manageId) {
		this.manageId = manageId;
	}

	public List<FeeShare> getFeeShares() {
		return feeShares;
	}

	public void setFeeShares(List<FeeShare> feeShares) {
		this.feeShares = feeShares;
	}

}
