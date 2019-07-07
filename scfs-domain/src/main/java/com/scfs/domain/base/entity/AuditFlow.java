package com.scfs.domain.base.entity;

import java.util.Date;

public class AuditFlow extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6607750664345621025L;

	/**
	 * 主键id
	 */
	private Integer id;

	/**
	 * 审核流编号
	 */
	private String auditFlowNo;

	/**
	 * 审核流类型
	 */
	private Integer auditFlowType;

	/**
	 * 审核流名称
	 */
	private String auditFlowName;

	/**
	 * 待法务审核
	 */
	private String lawAudit;

	/**
	 * 待商务审核
	 */
	private String bizAudit;

	/**
	 * 待事业部审核
	 */
	private String careerAudit;

	/**
	 * 待采购审核
	 */
	private String purchaseAudit;

	/**
	 * 待供应链小组审核
	 */
	private String supplyChainGroupAudit;

	/**
	 * 待供应链服务部审核
	 */
	private String supplyChainServiceAudit;

	/**
	 * 待商品风控审核
	 */
	private String goodsRiskAudit;

	/**
	 * 待业务审核
	 */
	private String busiAudit;

	/**
	 * 待财务专员审核
	 */
	private String financeAudit;

	/**
	 * 待财务主管审核
	 */
	private String finance2Audit;

	/**
	 * 待风控专员审核
	 */
	private String riskSpecialAudit;

	/**
	 * 待风控主管审核
	 */
	private String riskAudit;

	/**
	 * 待部门主管审核
	 */
	private String deptManageAudit;

	/**
	 * 待总经理审核
	 */
	private String bossAudit;

	/**
	 * 风控是否首单
	 */
	private Integer isFirstRisk;

	/**
	 * 法务是否首单
	 */
	private Integer isFirstLaw;

	/**
	 * 删除标记 0 : 有效 1 : 删除
	 */
	private Integer isDelete;

	/**
	 * 创建时间
	 */
	private Date createAt;

	/**
	 * 更新时间
	 */
	private Date updateAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAuditFlowNo() {
		return auditFlowNo;
	}

	public void setAuditFlowNo(String auditFlowNo) {
		this.auditFlowNo = auditFlowNo == null ? null : auditFlowNo.trim();
	}

	public Integer getAuditFlowType() {
		return auditFlowType;
	}

	public void setAuditFlowType(Integer auditFlowType) {
		this.auditFlowType = auditFlowType;
	}

	public String getAuditFlowName() {
		return auditFlowName;
	}

	public void setAuditFlowName(String auditFlowName) {
		this.auditFlowName = auditFlowName == null ? null : auditFlowName.trim();
	}

	public String getLawAudit() {
		return lawAudit;
	}

	public void setLawAudit(String lawAudit) {
		this.lawAudit = lawAudit == null ? null : lawAudit.trim();
	}

	public String getBizAudit() {
		return bizAudit;
	}

	public void setBizAudit(String bizAudit) {
		this.bizAudit = bizAudit == null ? null : bizAudit.trim();
	}

	public String getCareerAudit() {
		return careerAudit;
	}

	public void setCareerAudit(String careerAudit) {
		this.careerAudit = careerAudit == null ? null : careerAudit.trim();
	}

	public String getPurchaseAudit() {
		return purchaseAudit;
	}

	public void setPurchaseAudit(String purchaseAudit) {
		this.purchaseAudit = purchaseAudit == null ? null : purchaseAudit.trim();
	}

	public String getSupplyChainGroupAudit() {
		return supplyChainGroupAudit;
	}

	public void setSupplyChainGroupAudit(String supplyChainGroupAudit) {
		this.supplyChainGroupAudit = supplyChainGroupAudit == null ? null : supplyChainGroupAudit.trim();
	}

	public String getSupplyChainServiceAudit() {
		return supplyChainServiceAudit;
	}

	public void setSupplyChainServiceAudit(String supplyChainServiceAudit) {
		this.supplyChainServiceAudit = supplyChainServiceAudit == null ? null : supplyChainServiceAudit.trim();
	}

	public String getBusiAudit() {
		return busiAudit;
	}

	public void setBusiAudit(String busiAudit) {
		this.busiAudit = busiAudit == null ? null : busiAudit.trim();
	}

	public String getFinanceAudit() {
		return financeAudit;
	}

	public void setFinanceAudit(String financeAudit) {
		this.financeAudit = financeAudit == null ? null : financeAudit.trim();
	}

	public String getFinance2Audit() {
		return finance2Audit;
	}

	public void setFinance2Audit(String finance2Audit) {
		this.finance2Audit = finance2Audit == null ? null : finance2Audit.trim();
	}

	public String getRiskSpecialAudit() {
		return riskSpecialAudit;
	}

	public void setRiskSpecialAudit(String riskSpecialAudit) {
		this.riskSpecialAudit = riskSpecialAudit == null ? null : riskSpecialAudit.trim();
	}

	public String getRiskAudit() {
		return riskAudit;
	}

	public void setRiskAudit(String riskAudit) {
		this.riskAudit = riskAudit == null ? null : riskAudit.trim();
	}

	public String getDeptManageAudit() {
		return deptManageAudit;
	}

	public void setDeptManageAudit(String deptManageAudit) {
		this.deptManageAudit = deptManageAudit == null ? null : deptManageAudit.trim();
	}

	public String getBossAudit() {
		return bossAudit;
	}

	public void setBossAudit(String bossAudit) {
		this.bossAudit = bossAudit == null ? null : bossAudit.trim();
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
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

	public Integer getIsFirstRisk() {
		return isFirstRisk;
	}

	public void setIsFirstRisk(Integer isFirstRisk) {
		this.isFirstRisk = isFirstRisk;
	}

	public Integer getIsFirstLaw() {
		return isFirstLaw;
	}

	public void setIsFirstLaw(Integer isFirstLaw) {
		this.isFirstLaw = isFirstLaw;
	}

	public String getGoodsRiskAudit() {
		return goodsRiskAudit;
	}

	public void setGoodsRiskAudit(String goodsRiskAudit) {
		this.goodsRiskAudit = goodsRiskAudit;
	}

}