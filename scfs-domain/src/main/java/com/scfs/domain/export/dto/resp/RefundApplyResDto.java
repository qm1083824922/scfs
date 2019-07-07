package com.scfs.domain.export.dto.resp;

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
 *  File: RefundApplyResDto.java
 *  Description:出口退税申请
 *  TODO
 *  Date,					Who,				
 *  2016年12月06日				Administrator
 *
 * </pre>
 */
public class RefundApplyResDto {
	/** 出口退税申请id **/
	private Integer id;

	/** 退税申请编号 **/
	private String refundApplyNo;

	/** 退税附属编号 **/
	private String refundAttachNo;

	/** 项目id **/
	private Integer projectId;
	private String projectName;

	/** 客户id **/
	private Integer custId;
	private String cusName;

	/** 退税数量 **/
	private BigDecimal refundApplyNum;
	/** 退税金额 **/
	private BigDecimal refundApplyAmount;
	/** 可退税额 **/
	private BigDecimal refundApplyTax;
	/** 退税申请日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date refundApplyDate;
	/** 核销金额 **/
	private BigDecimal verifyAmount;
	/** 核销日期 **/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date verifyDate;
	/** 核销 **/
	private String verify;
	/** 状态 1:待提交 2.待财务审核 3,已完成 **/
	private Integer state;
	private String stateName;
	/** 备注 **/
	private String remark;

	private String businessUnitNameValue;
	/** 经营单位地址 */
	private String businessUnitAddress;
	/** 系统时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date systemTime;

	/** 创建人 */
	private String creator;
	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createAt;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_REFUND_APPLY);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_REFUND_APPLY);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_REFUND_APPLY);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_REFUND_APPLY);
			operMap.put(OperateConsts.PRINT, BusUrlConsts.PRINT_REFUND_APPLY);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRefundApplyNo() {
		return refundApplyNo;
	}

	public void setRefundApplyNo(String refundApplyNo) {
		this.refundApplyNo = refundApplyNo;
	}

	public String getRefundAttachNo() {
		return refundAttachNo;
	}

	public void setRefundAttachNo(String refundAttachNo) {
		this.refundAttachNo = refundAttachNo;
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

	public String getCusName() {
		return cusName;
	}

	public void setCusName(String cusName) {
		this.cusName = cusName;
	}

	public BigDecimal getRefundApplyNum() {
		return refundApplyNum;
	}

	public void setRefundApplyNum(BigDecimal refundApplyNum) {
		this.refundApplyNum = refundApplyNum;
	}

	public BigDecimal getRefundApplyAmount() {
		return refundApplyAmount;
	}

	public void setRefundApplyAmount(BigDecimal refundApplyAmount) {
		this.refundApplyAmount = refundApplyAmount;
	}

	public BigDecimal getRefundApplyTax() {
		return refundApplyTax;
	}

	public void setRefundApplyTax(BigDecimal refundApplyTax) {
		this.refundApplyTax = refundApplyTax;
	}

	public Date getRefundApplyDate() {
		return refundApplyDate;
	}

	public void setRefundApplyDate(Date refundApplyDate) {
		this.refundApplyDate = refundApplyDate;
	}

	public BigDecimal getVerifyAmount() {
		return verifyAmount;
	}

	public void setVerifyAmount(BigDecimal verifyAmount) {
		this.verifyAmount = verifyAmount;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public String getVerify() {
		return verify;
	}

	public void setVerify(String verify) {
		this.verify = verify;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
