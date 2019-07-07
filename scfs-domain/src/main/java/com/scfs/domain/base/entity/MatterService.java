package com.scfs.domain.base.entity;

/**
 * <pre>
 *  服务要求
 *  File: MatterService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年08月02日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class MatterService extends BaseEntity {
	/** 事项id **/
	private Integer matterId;
	/** 服务类型 **/
	private Integer serviceType;
	/** 服务类型备注 **/
	private String serviceTypeRemark;
	/** 业务模型说明(项目流程说明) **/
	private String serviceExplain;
	/** 合作账期类型 **/
	private Integer accountPeriod;
	/** 合作账期类型备注 **/
	private String accountPeriodRemark;
	/** 配套资金申请金额 **/
	private String matchedAmount;
	/** 币种 **/
	private Integer currnecyType;
	/** 币种备注 **/
	private String currnecyTypeRemark;
	/** 收取待采定金 **/
	private Integer depositPaid;
	/** 待采金额 **/
	private String depositAmount;
	/** 服务费率类型 **/
	private Integer serviceRateType;
	/** 服务费率 **/
	private String serviceRate;
	/** 应收费用类型 **/
	private Integer recFeeType;
	/** 应收费用备注 **/
	private String recFeeRemark;
	/** 应付费用类型 **/
	private Integer payFeeType;
	/** 应付费用备注 **/
	private String payFeeRemark;
	/** 服务费结算时间 **/
	private Integer serviceSettlementTime;
	/** 服务费结算时间备注 **/
	private String serviceSettlementRemark;
	/** 付款方式 **/
	private Integer payWay;
	/** 放贷方式 **/
	private Integer lendWay;
	/** 放贷方式备注 **/
	private String lendWayRemark;
	/** 预计年度垫资规模 **/
	private String scale;
	/** 预计资金周转次数（/年） **/
	private String turnoverTimes;
	/** 预计资金回报率（/年） **/
	private String returnRate;
	/** 成本支出项 **/
	private Integer costExpendType;
	/** 预计年度业务毛利 **/
	private String bizBlance;
	/** 预计年度业务净利 **/
	private String blance;
	/** 签约主体 **/
	private String signSubject;
	/** 项目导入业务人员 **/
	private String projectBizManager;

	public Integer getMatterId() {
		return matterId;
	}

	public void setMatterId(Integer matterId) {
		this.matterId = matterId;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceTypeRemark() {
		return serviceTypeRemark;
	}

	public void setServiceTypeRemark(String serviceTypeRemark) {
		this.serviceTypeRemark = serviceTypeRemark;
	}

	public String getServiceExplain() {
		return serviceExplain;
	}

	public void setServiceExplain(String serviceExplain) {
		this.serviceExplain = serviceExplain;
	}

	public Integer getAccountPeriod() {
		return accountPeriod;
	}

	public void setAccountPeriod(Integer accountPeriod) {
		this.accountPeriod = accountPeriod;
	}

	public String getAccountPeriodRemark() {
		return accountPeriodRemark;
	}

	public void setAccountPeriodRemark(String accountPeriodRemark) {
		this.accountPeriodRemark = accountPeriodRemark;
	}

	public String getMatchedAmount() {
		return matchedAmount;
	}

	public void setMatchedAmount(String matchedAmount) {
		this.matchedAmount = matchedAmount;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public String getCurrnecyTypeRemark() {
		return currnecyTypeRemark;
	}

	public void setCurrnecyTypeRemark(String currnecyTypeRemark) {
		this.currnecyTypeRemark = currnecyTypeRemark;
	}

	public Integer getDepositPaid() {
		return depositPaid;
	}

	public void setDepositPaid(Integer depositPaid) {
		this.depositPaid = depositPaid;
	}

	public String getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}

	public Integer getServiceRateType() {
		return serviceRateType;
	}

	public void setServiceRateType(Integer serviceRateType) {
		this.serviceRateType = serviceRateType;
	}

	public String getServiceRate() {
		return serviceRate;
	}

	public void setServiceRate(String serviceRate) {
		this.serviceRate = serviceRate;
	}

	public Integer getRecFeeType() {
		return recFeeType;
	}

	public void setRecFeeType(Integer recFeeType) {
		this.recFeeType = recFeeType;
	}

	public String getRecFeeRemark() {
		return recFeeRemark;
	}

	public void setRecFeeRemark(String recFeeRemark) {
		this.recFeeRemark = recFeeRemark;
	}

	public Integer getPayFeeType() {
		return payFeeType;
	}

	public void setPayFeeType(Integer payFeeType) {
		this.payFeeType = payFeeType;
	}

	public String getPayFeeRemark() {
		return payFeeRemark;
	}

	public void setPayFeeRemark(String payFeeRemark) {
		this.payFeeRemark = payFeeRemark;
	}

	public Integer getServiceSettlementTime() {
		return serviceSettlementTime;
	}

	public void setServiceSettlementTime(Integer serviceSettlementTime) {
		this.serviceSettlementTime = serviceSettlementTime;
	}

	public String getServiceSettlementRemark() {
		return serviceSettlementRemark;
	}

	public void setServiceSettlementRemark(String serviceSettlementRemark) {
		this.serviceSettlementRemark = serviceSettlementRemark;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public Integer getLendWay() {
		return lendWay;
	}

	public void setLendWay(Integer lendWay) {
		this.lendWay = lendWay;
	}

	public String getLendWayRemark() {
		return lendWayRemark;
	}

	public void setLendWayRemark(String lendWayRemark) {
		this.lendWayRemark = lendWayRemark;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getTurnoverTimes() {
		return turnoverTimes;
	}

	public void setTurnoverTimes(String turnoverTimes) {
		this.turnoverTimes = turnoverTimes;
	}

	public String getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(String returnRate) {
		this.returnRate = returnRate;
	}

	public Integer getCostExpendType() {
		return costExpendType;
	}

	public void setCostExpendType(Integer costExpendType) {
		this.costExpendType = costExpendType;
	}

	public String getBizBlance() {
		return bizBlance;
	}

	public void setBizBlance(String bizBlance) {
		this.bizBlance = bizBlance;
	}

	public String getBlance() {
		return blance;
	}

	public void setBlance(String blance) {
		this.blance = blance;
	}

	public String getSignSubject() {
		return signSubject;
	}

	public void setSignSubject(String signSubject) {
		this.signSubject = signSubject;
	}

	public String getProjectBizManager() {
		return projectBizManager;
	}

	public void setProjectBizManager(String projectBizManager) {
		this.projectBizManager = projectBizManager;
	}

}
