package com.scfs.domain.export.dto.resp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Maps;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.export.entity.CustomsApply;

/**
 * Created by Administrator on 2016年12月6日.
 */
public class CustomsApplyResDto extends CustomsApply{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2452465298549689822L;
	
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 代理公司名称
	 */
	private String proxyCompanyName;
	/**
	 * 客户名称
	 */
	private String customerName;
	/**
	 * 客户地址
	 */
	private String customerAddressName;
	/**
	 * 状态名称
	 */
	private String statusName;
	/**
	 * 是否退税
	 */
	private String isReturnTaxName;
    /**
     * 报关数量
     */ 
    private String taxRateValue;
    /**
     * 报关数量
     */ 
    private String taxNumValue;

    /**
     * 报关含税金额
     */ 
    private String customsAmountValue;
    
    /**
     * 报关税额
     */
    private String customsTaxAmountValue;
    private String businessUnitNameValue;
    private String businessUnitAddress;
    /**操作集合*/
    private List<CodeValue> opertaList;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date systemTime;
    public static class Operate{
        public static Map<String,String> operMap = Maps.newHashMap();
        static {
        	operMap.put(OperateConsts.DETAIL,BusUrlConsts.DETAIL_CUSTOMS_APPLY);
        	operMap.put(OperateConsts.EDIT,BusUrlConsts.EDIT_CUSTOMS_APPLY);
        	operMap.put(OperateConsts.SUBMIT,BusUrlConsts.SUBMIT_CUSTOMS_APPLY);
        	operMap.put(OperateConsts.DELETE,BusUrlConsts.DELETE_CUSTOMS_APPLY);
        	operMap.put(OperateConsts.PRINT,BusUrlConsts.PRINT_CUSTOMS_APPLY);
        }
    }
	
	public String getBusinessUnitNameValue() {
		return businessUnitNameValue;
	}
	public void setBusinessUnitNameValue(String businessUnitNameValue) {
		this.businessUnitNameValue = businessUnitNameValue;
	}
	public String getBusinessUnitAddress() {
		return businessUnitAddress;
	}
	public void setBusinessUnitAddress(String businessUnitAddress) {
		this.businessUnitAddress = businessUnitAddress;
	}
	public Date getSystemTime() {
		return systemTime;
	}
	public void setSystemTime(Date systemTime) {
		this.systemTime = systemTime;
	}
	public String getTaxNumValue() {
		return taxNumValue;
	}
	public void setTaxNumValue(String taxNumValue) {
		this.taxNumValue = taxNumValue;
	}
	public String getCustomsAmountValue() {
		return customsAmountValue;
	}
	public void setCustomsAmountValue(String customsAmountValue) {
		this.customsAmountValue = customsAmountValue;
	}
	public String getCustomsTaxAmountValue() {
		return customsTaxAmountValue;
	}
	public void setCustomsTaxAmountValue(String customsTaxAmountValue) {
		this.customsTaxAmountValue = customsTaxAmountValue;
	}
	public String getTaxRateValue() {
		return taxRateValue;
	}
	public void setTaxRateValue(String taxRateValue) {
		this.taxRateValue = taxRateValue;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProxyCompanyName() {
		return proxyCompanyName;
	}
	public void setProxyCompanyName(String proxyCompanyName) {
		this.proxyCompanyName = proxyCompanyName;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerAddressName() {
		return customerAddressName;
	}
	public void setCustomerAddressName(String customerAddressName) {
		this.customerAddressName = customerAddressName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getIsReturnTaxName() {
		return isReturnTaxName;
	}
	public void setIsReturnTaxName(String isReturnTaxName) {
		this.isReturnTaxName = isReturnTaxName;
	}
	public List<CodeValue> getOpertaList() {
		return opertaList;
	}
	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}
	
	
}

