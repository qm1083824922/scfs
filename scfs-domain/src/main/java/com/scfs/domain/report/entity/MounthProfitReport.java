package com.scfs.domain.report.entity;

import java.math.BigDecimal;

import com.scfs.domain.base.entity.BaseEntity;

/**
 * <pre>
 * 
 *  File: MounthProfitReport.java
 *  Description: 月结利润报表
 *  TODO
 *  Date,					Who,				
 *  2017年5月26日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class MounthProfitReport extends BaseEntity {

	private Integer id;
	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 期号
	 */
	private String issue;
	/**
	 * 项目
	 */
	private String projectName;
	/**
	 * 业务类型
	 */
	private Integer bizType;
	/**
	 * 业务类型名称
	 */
	private String bizTypeName;
	/**
	 * 销售总价
	 */
	private BigDecimal saleAmount = BigDecimal.ZERO;
	/**
	 * 销售毛利
	 */
	private BigDecimal saleBlanceAmount = BigDecimal.ZERO;
	/**
	 * 采购成本
	 */
	private BigDecimal purchaseCost = BigDecimal.ZERO;
	/**
	 * 资金成本
	 */
	private BigDecimal fundCost = BigDecimal.ZERO;
	/**
	 * 仓储物流费
	 */
	private BigDecimal warehouseAmount = BigDecimal.ZERO;
	/**
	 * 管理费用
	 */
	private BigDecimal manageAmount = BigDecimal.ZERO;
	/**
	 * 利润
	 */
	private BigDecimal profitAmount = BigDecimal.ZERO;
	/**
	 * 业务利润
	 */
	private BigDecimal bizManagerAmount = BigDecimal.ZERO;
	/**
	 * 利润率
	 */
	private BigDecimal profitRate = BigDecimal.ZERO;
	/**
	 * 销售毛利润率
	 */
	private BigDecimal saleBlanceRate = BigDecimal.ZERO;
	/**
	 * 业务利润率
	 */
	private BigDecimal bizManagerRate = BigDecimal.ZERO;
	/**
	 * 税率
	 */
	private BigDecimal taxRate;
	/**
	 * 利润率显示
	 */
	private String profitRateStr;
	/**
	 * 销售毛利润率显示
	 */
	private String saleBlanceRateStr;
	/**
	 * 业务利润率显示
	 */
	private String bizManagerRateStr;

	/** 销售金额环比 **/
	private String saleAmountMomStr;
	/** 销售成本环比 **/
	public String purchaseMomStr;
	/** 销售利润环比 **/
	public String saleBlanceMomStr;
	/** 费用利润环比 **/
	public String bizManagerMomStr;
	/** 利润环比 **/
	public String profitAmountMomStr;

	/** 利润目标值 **/
	private BigDecimal targetProfitAmount = BigDecimal.ZERO;
	/** 业务利润目标值 **/
	private BigDecimal targetBizManager = BigDecimal.ZERO;
	/** 销售毛利润目标值 **/
	private BigDecimal targetSaleBlance = BigDecimal.ZERO;
	/** 经营收入目标值 **/
	private BigDecimal targetSaleAmount = BigDecimal.ZERO;
	/** 管理费用目标值 **/
	private BigDecimal targetManageAmount = BigDecimal.ZERO;
	/** 经营费用目标值 **/
	private BigDecimal targetWarehouseAmount = BigDecimal.ZERO;
	/** 资金成本目标值 **/
	private BigDecimal targetFundVost = BigDecimal.ZERO;

	/** 利润占目标比例 **/
	private BigDecimal profitAmountPro = BigDecimal.ZERO;
	/** 业务利润占目标比例 **/
	private BigDecimal bizManagerPro = BigDecimal.ZERO;
	/** 销售毛利润占目标比例 **/
	private BigDecimal saleBlancePro = BigDecimal.ZERO;
	/** 经营收入占目标比例 **/
	private BigDecimal saleAmountPro = BigDecimal.ZERO;
	/** 管理费用占目标比例 **/
	private BigDecimal manageAmountPro = BigDecimal.ZERO;
	/** 经营费用占目标比例 **/
	private BigDecimal warehouseAmountPro = BigDecimal.ZERO;
	/** 资金成本占目标比例 **/
	private BigDecimal fundVostPro = BigDecimal.ZERO;

	/** 业务专员 **/
	private Integer bizSpecialId;
	private String bizSpecialName;

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

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public String getBizTypeName() {
		return bizTypeName;
	}

	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getSaleBlanceAmount() {
		return saleBlanceAmount;
	}

	public void setSaleBlanceAmount(BigDecimal saleBlanceAmount) {
		this.saleBlanceAmount = saleBlanceAmount;
	}

	public BigDecimal getPurchaseCost() {
		return purchaseCost;
	}

	public void setPurchaseCost(BigDecimal purchaseCost) {
		this.purchaseCost = purchaseCost;
	}

	public BigDecimal getFundCost() {
		return fundCost;
	}

	public void setFundCost(BigDecimal fundCost) {
		this.fundCost = fundCost;
	}

	public BigDecimal getWarehouseAmount() {
		return warehouseAmount;
	}

	public void setWarehouseAmount(BigDecimal warehouseAmount) {
		this.warehouseAmount = warehouseAmount;
	}

	public BigDecimal getManageAmount() {
		return manageAmount;
	}

	public void setManageAmount(BigDecimal manageAmount) {
		this.manageAmount = manageAmount;
	}

	public BigDecimal getProfitAmount() {
		return profitAmount;
	}

	public void setProfitAmount(BigDecimal profitAmount) {
		this.profitAmount = profitAmount;
	}

	public BigDecimal getBizManagerAmount() {
		return bizManagerAmount;
	}

	public void setBizManagerAmount(BigDecimal bizManagerAmount) {
		this.bizManagerAmount = bizManagerAmount;
	}

	public BigDecimal getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(BigDecimal profitRate) {
		this.profitRate = profitRate;
	}

	public BigDecimal getSaleBlanceRate() {
		return saleBlanceRate;
	}

	public void setSaleBlanceRate(BigDecimal saleBlanceRate) {
		this.saleBlanceRate = saleBlanceRate;
	}

	public BigDecimal getBizManagerRate() {
		return bizManagerRate;
	}

	public void setBizManagerRate(BigDecimal bizManagerRate) {
		this.bizManagerRate = bizManagerRate;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public String getProfitRateStr() {
		return profitRateStr;
	}

	public void setProfitRateStr(String profitRateStr) {
		this.profitRateStr = profitRateStr;
	}

	public String getSaleBlanceRateStr() {
		return saleBlanceRateStr;
	}

	public void setSaleBlanceRateStr(String saleBlanceRateStr) {
		this.saleBlanceRateStr = saleBlanceRateStr;
	}

	public String getBizManagerRateStr() {
		return bizManagerRateStr;
	}

	public void setBizManagerRateStr(String bizManagerRateStr) {
		this.bizManagerRateStr = bizManagerRateStr;
	}

	public String getSaleAmountMomStr() {
		return saleAmountMomStr;
	}

	public void setSaleAmountMomStr(String saleAmountMomStr) {
		this.saleAmountMomStr = saleAmountMomStr;
	}

	public String getPurchaseMomStr() {
		return purchaseMomStr;
	}

	public void setPurchaseMomStr(String purchaseMomStr) {
		this.purchaseMomStr = purchaseMomStr;
	}

	public String getSaleBlanceMomStr() {
		return saleBlanceMomStr;
	}

	public void setSaleBlanceMomStr(String saleBlanceMomStr) {
		this.saleBlanceMomStr = saleBlanceMomStr;
	}

	public String getBizManagerMomStr() {
		return bizManagerMomStr;
	}

	public void setBizManagerMomStr(String bizManagerMomStr) {
		this.bizManagerMomStr = bizManagerMomStr;
	}

	public String getProfitAmountMomStr() {
		return profitAmountMomStr;
	}

	public void setProfitAmountMomStr(String profitAmountMomStr) {
		this.profitAmountMomStr = profitAmountMomStr;
	}

	public BigDecimal getTargetProfitAmount() {
		return targetProfitAmount;
	}

	public void setTargetProfitAmount(BigDecimal targetProfitAmount) {
		this.targetProfitAmount = targetProfitAmount;
	}

	public BigDecimal getTargetBizManager() {
		return targetBizManager;
	}

	public void setTargetBizManager(BigDecimal targetBizManager) {
		this.targetBizManager = targetBizManager;
	}

	public BigDecimal getTargetSaleBlance() {
		return targetSaleBlance;
	}

	public void setTargetSaleBlance(BigDecimal targetSaleBlance) {
		this.targetSaleBlance = targetSaleBlance;
	}

	public BigDecimal getTargetSaleAmount() {
		return targetSaleAmount;
	}

	public void setTargetSaleAmount(BigDecimal targetSaleAmount) {
		this.targetSaleAmount = targetSaleAmount;
	}

	public BigDecimal getTargetManageAmount() {
		return targetManageAmount;
	}

	public void setTargetManageAmount(BigDecimal targetManageAmount) {
		this.targetManageAmount = targetManageAmount;
	}

	public BigDecimal getTargetWarehouseAmount() {
		return targetWarehouseAmount;
	}

	public void setTargetWarehouseAmount(BigDecimal targetWarehouseAmount) {
		this.targetWarehouseAmount = targetWarehouseAmount;
	}

	public BigDecimal getTargetFundVost() {
		return targetFundVost;
	}

	public void setTargetFundVost(BigDecimal targetFundVost) {
		this.targetFundVost = targetFundVost;
	}

	public BigDecimal getProfitAmountPro() {
		return profitAmountPro;
	}

	public void setProfitAmountPro(BigDecimal profitAmountPro) {
		this.profitAmountPro = profitAmountPro;
	}

	public BigDecimal getBizManagerPro() {
		return bizManagerPro;
	}

	public void setBizManagerPro(BigDecimal bizManagerPro) {
		this.bizManagerPro = bizManagerPro;
	}

	public BigDecimal getSaleBlancePro() {
		return saleBlancePro;
	}

	public void setSaleBlancePro(BigDecimal saleBlancePro) {
		this.saleBlancePro = saleBlancePro;
	}

	public BigDecimal getSaleAmountPro() {
		return saleAmountPro;
	}

	public void setSaleAmountPro(BigDecimal saleAmountPro) {
		this.saleAmountPro = saleAmountPro;
	}

	public BigDecimal getManageAmountPro() {
		return manageAmountPro;
	}

	public void setManageAmountPro(BigDecimal manageAmountPro) {
		this.manageAmountPro = manageAmountPro;
	}

	public BigDecimal getWarehouseAmountPro() {
		return warehouseAmountPro;
	}

	public void setWarehouseAmountPro(BigDecimal warehouseAmountPro) {
		this.warehouseAmountPro = warehouseAmountPro;
	}

	public BigDecimal getFundVostPro() {
		return fundVostPro;
	}

	public void setFundVostPro(BigDecimal fundVostPro) {
		this.fundVostPro = fundVostPro;
	}

	public Integer getBizSpecialId() {
		return bizSpecialId;
	}

	public void setBizSpecialId(Integer bizSpecialId) {
		this.bizSpecialId = bizSpecialId;
	}

	public String getBizSpecialName() {
		return bizSpecialName;
	}

	public void setBizSpecialName(String bizSpecialName) {
		this.bizSpecialName = bizSpecialName;
	}

}
