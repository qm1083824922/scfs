package com.scfs.domain.project.dto.resp;

import java.math.BigDecimal;
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
 *  File: ProjectPoolAdjustResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年03月22日			Administrator
 *
 * </pre>
 */

public class ProjectPoolAdjustResDto {
	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 调整编码
	 */
	private String adjustNo;

	/**
	 * 项目id
	 */
	private Integer projectId;
	private String projectName;

	/**
	 * 项目当前额度
	 */
	private BigDecimal projectAmount;

	/**
	 * 当前可用额度
	 */
	private BigDecimal remainFundAmount;

	/**
	 * 币种
	 */
	private Integer currencyType;
	private String currencyTypeName;

	/**
	 * 临时调整额度
	 */
	private BigDecimal adjustAmount;

	/**
	 * 创建人id
	 */
	private Integer createId;

	/**
	 * 创建人
	 */
	private String creator;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateAt;

	/**
	 * 有效期开始时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date startValidDate;

	/**
	 * 有效期结束时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date endValidDate;
	/** 有效期 **/
	private String validDateString;
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 项目当前资金占用
	 */
	private BigDecimal usedFundAmount;

	/** 状态 **/
	private Integer state;
	private String stateName;
	private String remainRate;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAILPROJECTPOOLADJUST);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDITPROJECTPOOLADJUST);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMITPROJECTPOOLADJUST);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETEPROJECTPOOLADJUST);
			operMap.put(OperateConsts.LOCK, BusUrlConsts.LOCKPROJECTPOOLADJUST);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAdjustNo() {
		return adjustNo;
	}

	public void setAdjustNo(String adjustNo) {
		this.adjustNo = adjustNo;
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

	public BigDecimal getProjectAmount() {
		return projectAmount;
	}

	public void setProjectAmount(BigDecimal projectAmount) {
		this.projectAmount = projectAmount;
	}

	public BigDecimal getRemainFundAmount() {
		return remainFundAmount;
	}

	public void setRemainFundAmount(BigDecimal remainFundAmount) {
		this.remainFundAmount = remainFundAmount;
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

	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public Integer getCreateId() {
		return createId;
	}

	public void setCreateId(Integer createId) {
		this.createId = createId;
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

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Date getStartValidDate() {
		return startValidDate;
	}

	public void setStartValidDate(Date startValidDate) {
		this.startValidDate = startValidDate;
	}

	public Date getEndValidDate() {
		return endValidDate;
	}

	public void setEndValidDate(Date endValidDate) {
		this.endValidDate = endValidDate;
	}

	public String getValidDateString() {
		return validDateString;
	}

	public void setValidDateString(String validDateString) {
		this.validDateString = validDateString;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getUsedFundAmount() {
		return usedFundAmount;
	}

	public void setUsedFundAmount(BigDecimal usedFundAmount) {
		this.usedFundAmount = usedFundAmount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getRemainRate() {
		return remainRate;
	}

	public void setRemainRate(String remainRate) {
		this.remainRate = remainRate;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}
}
