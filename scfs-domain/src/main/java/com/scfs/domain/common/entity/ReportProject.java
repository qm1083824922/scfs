package com.scfs.domain.common.entity;

import java.util.Date;

public class ReportProject {
    /**
     * 自增ID
     */ 
    private Integer id;

    /**
     * 报表类型 1-销售报表 2-应收报表 3-进销存报表 4-库存报表 5-利润报表 6-资金统计报表 7-月结利润报表
     */ 
    private Integer reportType;

    /**
     * 需过滤项目ID
     */ 
    private Integer projectId;

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

    public Integer getReportType() {
        return reportType;
    }

    public void setReportType(Integer reportType) {
        this.reportType = reportType;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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