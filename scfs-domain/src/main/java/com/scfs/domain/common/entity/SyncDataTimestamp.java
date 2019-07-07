package com.scfs.domain.common.entity;

import java.io.Serializable;
import java.util.Date;

public class SyncDataTimestamp implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1622948905023040914L;

	/**
     * 自增ID
     */ 
    private Integer id;

    /**
     * 同步最大更新时间
     */ 
    private Date maxUpdateAt;

    /**
     * 业务类型 1-同步顺友BL数据
     */ 
    private Integer businessType;

    /**
     * 创建时间
     */ 
    private Date createAt;

    /**
     * 更新时间
     */ 
    private Date updateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getMaxUpdateAt() {
        return maxUpdateAt;
    }

    public void setMaxUpdateAt(Date maxUpdateAt) {
        this.maxUpdateAt = maxUpdateAt;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}