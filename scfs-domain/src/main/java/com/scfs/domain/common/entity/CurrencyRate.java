package com.scfs.domain.common.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CurrencyRate {
    /**
     * 自增id
     */ 
    private Integer id;

    /**
     * 名称
     */ 
    private String name;

    /**
     * 币种
     */ 
    private String currency;

    /**
     * CNY汇率
     */ 
    private BigDecimal cnyRate;

    /**
     * HKD汇率
     */ 
    private BigDecimal hkdRate;

    /**
     * 创建人
     */ 
    private String createUser;

    /**
     * 创建时间
     */ 
    private Date createTime;

    /**
     * 更新人
     */ 
    private String updateUser;

    /**
     * 更新时间
     */ 
    private Date updateTime;

    /**
     * 1开启  2封存
     */ 
    private Integer status;

    /**
     * 月份
     */ 
    private String theMonthCd;

    /**
     * USD汇率
     */ 
    private BigDecimal usdRate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public BigDecimal getCnyRate() {
        return cnyRate;
    }

    public void setCnyRate(BigDecimal cnyRate) {
        this.cnyRate = cnyRate;
    }

    public BigDecimal getHkdRate() {
        return hkdRate;
    }

    public void setHkdRate(BigDecimal hkdRate) {
        this.hkdRate = hkdRate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser == null ? null : updateUser.trim();
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTheMonthCd() {
        return theMonthCd;
    }

    public void setTheMonthCd(String theMonthCd) {
        this.theMonthCd = theMonthCd == null ? null : theMonthCd.trim();
    }

    public BigDecimal getUsdRate() {
        return usdRate;
    }

    public void setUsdRate(BigDecimal usdRate) {
        this.usdRate = usdRate;
    }
}