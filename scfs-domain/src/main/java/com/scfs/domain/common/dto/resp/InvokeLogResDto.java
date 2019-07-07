package com.scfs.domain.common.dto.resp;

import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.common.entity.InvokeLog;
import com.google.common.collect.Maps;

/**
 * Created by Administrator on 2016年11月29日.
 */
public class InvokeLogResDto extends InvokeLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5331895600424174319L;
	/**
	 * 接口名称
	 */
	private String invokeTypeName;
	/**
	 * 模块名称
	 */
	private String moduleTypeName;
	/**
	 * 单据类型
	 */
	private String billTypeName;
	/**
	 * 是否成功
	 */
	private String isSuccessName;
    /**
     * 提供方
     */ 
    private String providerName;
    /**
     * 调用方
     */ 
    private String consumerName;
    /**
     * 处理结果
     */ 
    private String dealFlagName;

    /**操作集合*/
    private List<CodeValue> opertaList;
    
    public static class Operate{
        public static Map<String,String> operMap = Maps.newHashMap();
        static {
        	operMap.put(OperateConsts.REINVOKE, BaseUrlConsts.REINVOKEINVOKELOG);
        	operMap.put(OperateConsts.REDEAL, BaseUrlConsts.REDEALINVOKELOG);
        }
    }

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public String getInvokeTypeName() {
		return invokeTypeName;
	}

	public void setInvokeTypeName(String invokeTypeName) {
		this.invokeTypeName = invokeTypeName;
	}

	public String getModuleTypeName() {
		return moduleTypeName;
	}

	public void setModuleTypeName(String moduleTypeName) {
		this.moduleTypeName = moduleTypeName;
	}

	public String getBillTypeName() {
		return billTypeName;
	}

	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}

	public String getIsSuccessName() {
		return isSuccessName;
	}

	public void setIsSuccessName(String isSuccessName) {
		this.isSuccessName = isSuccessName;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getConsumerName() {
		return consumerName;
	}

	public void setConsumerName(String consumerName) {
		this.consumerName = consumerName;
	}

	public String getDealFlagName() {
		return dealFlagName;
	}

	public void setDealFlagName(String dealFlagName) {
		this.dealFlagName = dealFlagName;
	}
    
    
}

