package com.scfs.domain.project.dto.resp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

/**
 * <pre>
 * 
 *  File: FeeResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月13日			Administrator
 *
 * </pre>
 */
public class ProjectItemResDto {
	/** 条款id **/
	private Integer id;
	/** 条款编号 **/
	private String itemNo;
	/** 经营单位 **/
	private String businessUnitName;
	/** 项目 **/
	private String projectName;
	/** 业务类别 **/
	private String bizType;
	/** 有效期 **/
	private String dateStr;
	/** 结算类型 **/
	private String isFundAccount;
	/** 额度总额 **/
	private String amount;
	/** 状态 **/
	private String status;
	/** 操作集合 */
	private List<CodeValue> opertaList;
	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAILPROJECTITEM);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDITPROJECTITEM);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMITPROJECTITEM);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETEPROJECTITEM);
			operMap.put(OperateConsts.LOCK, BusUrlConsts.LOCKPROJECTITEM);
			operMap.put(OperateConsts.COPY, BusUrlConsts.COPYPROJECTITEM);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getProjectName() {
		return projectName;
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

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public String getIsFundAccount() {
		return isFundAccount;
	}

	public void setIsFundAccount(String isFundAccount) {
		this.isFundAccount = isFundAccount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
