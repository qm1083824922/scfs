package com.scfs.domain.common.entity;

import java.util.Date;

public class SysParam {
    /**
     * 主键uuid
     */ 
    private Integer id;

    /**
     * 参数名称
     */ 
    private String paramName;

    /**
     * 参数key
     */ 
    private String paramKey;

    /**
     * 参数value
     */ 
    private String paramValue;

    /**
     * 参数状态 0-启用 1-禁用 默认0
     */ 
    private String status;

    /**
     * 备注
     */ 
    private String remark;

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

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName == null ? null : paramName.trim();
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey == null ? null : paramKey.trim();
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue == null ? null : paramValue.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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