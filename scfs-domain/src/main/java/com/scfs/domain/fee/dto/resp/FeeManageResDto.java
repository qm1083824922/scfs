package com.scfs.domain.fee.dto.resp;

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
 * 	管理费用基本信息
 *  File: FeeManageResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年04月12日				Administrator
 *
 * </pre>
 */
public class FeeManageResDto {
	/** 管理费用id **/
	private Integer id;
	/** 管理费用编号 **/
	private String feeManageNo;
	/** 部门id **/
	private Integer departmentId;
	private String departmentName;
	/** 项目id **/
	private Integer projectId;
	private String projectName;
	/** 用户id **/
	private Integer userId;
	private String userName;
	/** 客户id **/
	private Integer custId;
	private String custName;
	/** 费用科目id **/
	private Integer feeSpecId;
	private String feeSpecName;
	/** 应付方式 **/
	private Integer recType;
	private String recTypeName;
	/** 日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date date;
	/** 币种 **/
	private Integer currnecyType;
	private String currnecyTypeName;
	/** 管理费用金额 **/
	private BigDecimal amount;
	/** 分摊金额 **/
	private BigDecimal shareAmount;
	/** 可分摊金额 **/
	private BigDecimal blanceAmount;
	/** 状态 0 未提交 **/
	private Integer state;
	private String stateName;

	/** 备注 **/
	private String remark;

	/** 创建人 */
	private String creator;
	/** 创建日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date createAt;
	
	/** 分摊日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date shareDate;
	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_FEE_MANAGE);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_FEE_MANAGE);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_FEE_MANAGE);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_FEE_MANAGE);
			operMap.put(OperateConsts.SHARE, BusUrlConsts.QUERY_FEE_SHARE);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFeeManageNo() {
		return feeManageNo;
	}

	public void setFeeManageNo(String feeManageNo) {
		this.feeManageNo = feeManageNo;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Integer getFeeSpecId() {
		return feeSpecId;
	}

	public void setFeeSpecId(Integer feeSpecId) {
		this.feeSpecId = feeSpecId;
	}

	public String getFeeSpecName() {
		return feeSpecName;
	}

	public void setFeeSpecName(String feeSpecName) {
		this.feeSpecName = feeSpecName;
	}

	public Integer getRecType() {
		return recType;
	}

	public void setRecType(Integer recType) {
		this.recType = recType;
	}

	public String getRecTypeName() {
		return recTypeName;
	}

	public void setRecTypeName(String recTypeName) {
		this.recTypeName = recTypeName;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(BigDecimal shareAmount) {
		this.shareAmount = shareAmount;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
	public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }
	
	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

	public BigDecimal getBlanceAmount() {
		return blanceAmount;
	}

	public void setBlanceAmount(BigDecimal blanceAmount) {
		this.blanceAmount = blanceAmount;
	}

}
