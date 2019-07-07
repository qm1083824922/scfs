package com.scfs.domain.fee.dto.resp;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <pre>
 * 	管理费用分摊
 *  File: FeeShareResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月17日				Administrator
 *
 * </pre>
 */
public class FeeShareResDto {
	/** 主键id **/
	private Integer id;

	/** 费用管理id **/
	private Integer manageId;

	/** 项目id **/
	private Integer shareProjectId;
	private String shareProjectName;

	/** 用户id **/
	private Integer shareUserId;
	private String shareUserName;

	/** 客户id **/
	private Integer shareCustId;
	private String shareCustName;

	/** 金额 **/
	private BigDecimal amount;

	   /** 分摊日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date shareDate;
    
	
    public Date getShareDate()
    {
        return shareDate;
    }

    
    public void setShareDate(Date shareDate)
    {
        this.shareDate = shareDate;
    }

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getManageId() {
		return manageId;
	}

	public void setManageId(Integer manageId) {
		this.manageId = manageId;
	}

	public Integer getShareProjectId() {
		return shareProjectId;
	}

	public void setShareProjectId(Integer shareProjectId) {
		this.shareProjectId = shareProjectId;
	}

	public String getShareProjectName() {
		return shareProjectName;
	}

	public void setShareProjectName(String shareProjectName) {
		this.shareProjectName = shareProjectName;
	}

	public Integer getShareUserId() {
		return shareUserId;
	}

	public void setShareUserId(Integer shareUserId) {
		this.shareUserId = shareUserId;
	}

	public String getShareUserName() {
		return shareUserName;
	}

	public void setShareUserName(String shareUserName) {
		this.shareUserName = shareUserName;
	}

	public Integer getShareCustId() {
		return shareCustId;
	}

	public void setShareCustId(Integer shareCustId) {
		this.shareCustId = shareCustId;
	}

	public String getShareCustName() {
		return shareCustName;
	}

	public void setShareCustName(String shareCustName) {
		this.shareCustName = shareCustName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

}
