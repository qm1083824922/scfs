package com.scfs.domain.common.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年11月29日.
 */
public class InvokeLogSearchReqDto extends BaseReqDto {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3994888663613436715L;

	/**
     * 接口类型(例如8001-入库单接口，第一位为模块类型，后三位自定义)
     */ 
    private Integer invokeType;

    /**
     * 模块类型 7-销售 8-物流
     */ 
    private Integer moduleType;

    /**
     * 单据类型 1-采购订单 2-销售单 3-出库单 4-入库单
     */ 
    private Integer billType;

    /**
     * 单据编号
     */ 
    private String billNo;

    /**
     * 开始单据日期
     */ 
    private String startBillDate;
    
    /**
     * 结束单据日期
     */ 
    private String endBillDate;
    
    /**
     * 开始创建时间
     */ 
    private String startCreateAt;
    
    /**
     * 结束创建时间
     */ 
    private String endCreateAt;

    /**
     * 返回结果 0-失败 1- 成功
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
     * 提供者为scfs时使用，0-未处理 1-处理失败 2-处理成功
     */ 
    private Integer dealFlag;
    
	public Integer getInvokeType() {
		return invokeType;
	}

	public void setInvokeType(Integer invokeType) {
		this.invokeType = invokeType;
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

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Integer getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Integer isSuccess) {
		this.isSuccess = isSuccess;
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

	public String getStartBillDate() {
		return startBillDate;
	}

	public void setStartBillDate(String startBillDate) {
		this.startBillDate = startBillDate;
	}

	public String getEndBillDate() {
		return endBillDate;
	}

	public void setEndBillDate(String endBillDate) {
		this.endBillDate = endBillDate;
	}

	public String getStartCreateAt() {
		return startCreateAt;
	}

	public void setStartCreateAt(String startCreateAt) {
		this.startCreateAt = startCreateAt;
	}

	public String getEndCreateAt() {
		return endCreateAt;
	}

	public void setEndCreateAt(String endCreateAt) {
		this.endCreateAt = endCreateAt;
	}


}

