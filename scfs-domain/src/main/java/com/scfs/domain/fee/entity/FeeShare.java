package com.scfs.domain.fee.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 *  管理费用分摊
 *  File: FeeShare.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2017年05月17日             Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class FeeShare extends BaseEntity {
    /** 主键id **/
    private Integer id;
    /** 费用管理id **/
    private Integer manageId;
    /** 项目id **/
    private Integer shareProjectId;
    /** 用户id **/
    private Integer shareUserId;
    /** 客户id **/
    private Integer shareCustId;
    /** 金额 **/
    private String amount;
    /** 分摊日期 **/
    private Date shareDate;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
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

    public Integer getShareUserId() {
        return shareUserId;
    }

    public void setShareUserId(Integer shareUserId) {
        this.shareUserId = shareUserId;
    }

    public Integer getShareCustId() {
        return shareCustId;
    }

    public void setShareCustId(Integer shareCustId) {
        this.shareCustId = shareCustId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

}
