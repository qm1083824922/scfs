package com.scfs.domain.common.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class InvokeLog implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4215040225757746499L;

	/**
     * 自增ID
     */ 
    private Integer id;

    /**
     * 接口类型(例如8001-入库单接口，第一位为模块类型，后三位自定义)
     */ 
    private Integer invokeType;
    
    /**
     * 调用模式 1-实时 2-异步
     */
    private Integer invokeMode;

    /**
     * 模块类型 7-销售 8-物流
     */ 
    private Integer moduleType;

    /**
     * 单据类型 1-采购订单 2-销售单 3-出库单 4-入库单 5-付款单
     */ 
    private Integer billType;

    /**
     * 单据ID
     */ 
    private Integer billId;

    /**
     * 单据编号
     */ 
    private String billNo;

    /**
     * 单据日期
     */ 
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date billDate;

    /**
     * 调用返回结果 0-失败 1- 成功
     */ 
    private Integer isSuccess;
    
    /**
     * 提供方
     */ 
    private Integer provider;
    
    /**
     * 调用方
     */ 
    private Integer consumer;

    /**
     * 调用返回消息 
     */ 
    private String returnMsg;

    /**
     * 调用重试次数
     */ 
    private Integer tryNum;

    /**
     * 调用异常信息
     */ 
    private String exceptionMsg;
    
    /**
     * 是否重新调用
     */
    private Integer tryAgainFlag;
    
    /**
     * 提供者为scfs时使用，0-未处理 1-处理失败 2-处理成功
     */ 
    private Integer dealFlag;
    
    /**
     * 处理重试次数
     */ 
    private Integer dealNum;
    
    /**
     * 处理失败原因
     */ 
    private String dealMsg;
    
    /**
     * 是否重新处理
     */
    private Integer dealAgainFlag;

    /**
     * 备注
     */ 
    private String remark;

    /**
     * 创建时间
     */ 
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAt;

    /**
     * 更新时间
     */ 
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateAt;

    /**
     * 传输内容
     */ 
    private String content;

    
    public InvokeLog() {
    	this.isSuccess = 1;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvokeType() {
		return invokeType;
	}

	public void setInvokeType(Integer invokeType) {
		this.invokeType = invokeType;
	}
	
	public Integer getInvokeMode() {
		return invokeMode;
	}

	public void setInvokeMode(Integer invokeMode) {
		this.invokeMode = invokeMode;
	}

	public Integer getModuleType() {
        return moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo == null ? null : billNo.trim();
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public Integer getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg == null ? null : exceptionMsg.trim();
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getProvider() {
		return provider;
	}

	public void setProvider(Integer provider) {
		this.provider = provider;
	}

	public Integer getConsumer() {
		return consumer;
	}

	public void setConsumer(Integer consumer) {
		this.consumer = consumer;
	}

	public Integer getDealFlag() {
		return dealFlag;
	}

	public void setDealFlag(Integer dealFlag) {
		this.dealFlag = dealFlag;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public Integer getTryNum() {
		return tryNum;
	}

	public void setTryNum(Integer tryNum) {
		this.tryNum = tryNum;
	}

	public Integer getDealNum() {
		return dealNum;
	}

	public void setDealNum(Integer dealNum) {
		this.dealNum = dealNum;
	}

	public String getDealMsg() {
		return dealMsg;
	}

	public void setDealMsg(String dealMsg) {
		this.dealMsg = dealMsg;
	}

	public Integer getTryAgainFlag() {
		return tryAgainFlag;
	}

	public void setTryAgainFlag(Integer tryAgainFlag) {
		this.tryAgainFlag = tryAgainFlag;
	}

	public Integer getDealAgainFlag() {
		return dealAgainFlag;
	}

	public void setDealAgainFlag(Integer dealAgainFlag) {
		this.dealAgainFlag = dealAgainFlag;
	}


}