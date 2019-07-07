package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: ReceiveResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月31日			Administrator
 *
 * </pre>
 */
public class ReceiveResDto  {
    private Integer id;
	private Integer projectId;
	private String projectName;
	private Integer custId;
	private String custName;
	private Integer busiUnit;
	private String busiUnitName;
	private BigDecimal amountUnReceived;
	private Integer currencyType;
	private String currencyTypeName;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date checkDate; // 对账日期
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expireDate; // 应收到期日期

	private BigDecimal amountReceivable;// 应收金额
	private BigDecimal amountReceived;// 已收金额 ， 水单核销总额
	private BigDecimal blance;// 余额
	private String creator;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createAt;

	/**
	 * 超期天数
	 */
	private BigDecimal overDay;
	/** 部门 **/
	private Integer departmentId;
	private String departmentName;
	/** CNY汇率 **/ 
    private BigDecimal cnyRate;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.QUERY_REC_LINE); // 浏览应收明细
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_REC_LINE); // 编辑应收明细
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_RECEIVE); // 删除应收明细
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
	}

	public BigDecimal getAmountUnReceived() {
		return amountUnReceived;
	}

	public void setAmountUnReceived(BigDecimal amountUnReceived) {
		this.amountUnReceived = amountUnReceived;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public BigDecimal getAmountReceivable() {
		return amountReceivable;
	}

	public void setAmountReceivable(BigDecimal amountReceivable) {
		this.amountReceivable = amountReceivable;
	}

	public BigDecimal getAmountReceived() {
		return amountReceived;
	}

	public void setAmountReceived(BigDecimal amountReceived) {
		this.amountReceived = amountReceived;
	}

	public BigDecimal getBlance() {
		return blance;
	}

	public void setBlance(BigDecimal blance) {
		this.blance = blance;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public BigDecimal getOverDay() {
		return overDay;
	}

	public void setOverDay(BigDecimal overDay) {
		this.overDay = overDay;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public BigDecimal getCnyRate() {
		return cnyRate;
	}

	public void setCnyRate(BigDecimal cnyRate) {
		this.cnyRate = cnyRate;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}
	
}
