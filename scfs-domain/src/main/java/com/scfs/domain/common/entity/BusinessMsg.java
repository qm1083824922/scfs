package com.scfs.domain.common.entity;

import java.util.Date;

public class BusinessMsg {
    /**
     * 自增ID
     */ 
    private Integer id;

    /**
     * 1-付款单
     */ 
    private Integer billType;

    /**
     * 单据号
     */ 
    private String billNo;
    
    /**
     * 接口类型(与tb_invoke_log的接口类型一致) 7001-CMS上传付款单接口
     */
    private Integer invokeType;

    /**
     * 0-成功 1-失败
     */ 
    private Integer flag;

    /**
     * 返回消息
     */ 
    private String msg;
    
    /**
     * 调用次数
     */
    private Integer tryNum;

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

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
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

	public Integer getTryNum() {
		return tryNum;
	}

	public void setTryNum(Integer tryNum) {
		this.tryNum = tryNum;
	}

	public Integer getInvokeType() {
		return invokeType;
	}

	public void setInvokeType(Integer invokeType) {
		this.invokeType = invokeType;
	}
    
    
}