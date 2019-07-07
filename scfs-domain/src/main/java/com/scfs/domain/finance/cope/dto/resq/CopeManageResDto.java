package com.scfs.domain.finance.cope.dto.resq;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;
import com.google.common.collect.Maps;

/**
 * <pre>
 *  应付管理
 *  File: CopeManageResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年10月31日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CopeManageResDto extends BaseEntity {
	/** 项目id **/
	private Integer projectId;
	private String projectName;
	/** 客户id **/
	private Integer customerId;
	private String customerName;
	/** 经营单位 **/
	private Integer busiUnitId;
	private String busiUnitName;
	/** 币种 **/
	private Integer currnecyType;
	private String currnecyTypeName;
	/** 应付金额 **/
	private BigDecimal copeAmount;
	/** 已付金额 **/
	private BigDecimal paidAmount;
	/** 未付金额 **/
	private BigDecimal unpaidAmount;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_COPE_MANAGE);
		}
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public String getCurrnecyTypeName() {
		return currnecyTypeName;
	}

	public void setCurrnecyTypeName(String currnecyTypeName) {
		this.currnecyTypeName = currnecyTypeName;
	}

	public BigDecimal getCopeAmount() {
		return copeAmount;
	}

	public void setCopeAmount(BigDecimal copeAmount) {
		this.copeAmount = copeAmount;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getUnpaidAmount() {
		return unpaidAmount;
	}

	public void setUnpaidAmount(BigDecimal unpaidAmount) {
		this.unpaidAmount = unpaidAmount;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
