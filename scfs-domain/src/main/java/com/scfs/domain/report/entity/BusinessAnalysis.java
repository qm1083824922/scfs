package com.scfs.domain.report.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2017年1月4日.
 */
public class BusinessAnalysis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6043895181398653811L;

	/**
	 * 项目ID
	 */
	private Integer projectId;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 年度业绩
	 */
	private BigDecimal annualPerformance;
	/**
	 * 上期业绩
	 */
	private BigDecimal priorPeriodPerformance;
	/**
	 * 本期业绩
	 */
	private BigDecimal currPeriodPerformance;
	/**
	 * 上期回款
	 */
	private BigDecimal priorPeriodRefundAmount;
	/**
	 * 本期回款
	 */
	private BigDecimal currPeriodRefundAmount;
	/**
	 * 经营利润
	 */
	private BigDecimal busiProfit;
	/**
	 * 经营利润占比
	 */
	private BigDecimal busiProfitRatio;
	/**
	 * 应收费用
	 */
	private BigDecimal recAmount;
	/**
	 * 静态资金占用
	 */
	private BigDecimal staticFundAmount;
	/**
	 * 存货周转天数
	 */
	private BigDecimal assetTurnoverDays;
	/**
	 * 资金周转天数
	 */
	private BigDecimal fundTurnoverDays;
	/**
	 * 物流费用
	 */
	private BigDecimal logisticsAmount;
	/**
	 * 物流费用占比
	 */
	private BigDecimal logisticsAmountRatio;
	/**
	 * 市场费用
	 */
	private BigDecimal marketAmount;
	/**
	 * 市场费用占比
	 */
	private BigDecimal marketAmountRatio;
	/**
	 * 财务费用
	 */
	private BigDecimal financeAmount;
	/**
	 * 财务费用占比
	 */
	private BigDecimal financeAmountRatio;
	/**
	 * 管理费用
	 */
	private BigDecimal manageAmount;
	/**
	 * 管理费用占比
	 */
	private BigDecimal manageAmountRatio;
	/**
	 * 资金成本
	 */
	private BigDecimal fundCostAmount;
	/**
	 * 资金成本占比
	 */
	private BigDecimal fundCostAmountRatio;
	/**
	 * 净利润
	 */
	private BigDecimal netProfit;
	/**
	 * 净利润占比
	 */
	private BigDecimal netProfitRatio;
	/**
	 * 税负成本
	 */
	private BigDecimal raxNegCostAmount;
	/**
	 * 税负成本占比
	 */
	private BigDecimal raxNegCostAmountRatio;
	/**
	 * 绩效利润
	 */
	private BigDecimal performanceProfit;
	/**
	 * 应收账款
	 */
	private BigDecimal recAccountAmount;
	/**
	 * 应收账款占比
	 */
	private BigDecimal recAccountAmountRatio;
	/**
	 * 超期应收
	 */
	private BigDecimal overDueRecAmount;
	/**
	 * 库存金额
	 */
	private BigDecimal storeAmount;
	/**
	 * 加权平均库龄
	 */
	private BigDecimal weightAvgAge;
	/**
	 * 在途库存金额
	 */
	private BigDecimal onwayAmount;
	/**
	 * 总额度
	 */
	private BigDecimal totalAmount;
	/**
	 * 可用额度
	 */
	private BigDecimal availableAmount;
	/**
	 * 可用额度占比
	 */
	private BigDecimal availableAmountRatio;
	/**
	 * 账期天数
	 */
	private Integer accountPeriodDays;
	/**
	 * 资金池余额
	 */
	private BigDecimal unUsedfundPoolAmount;
	/**
	 * 开票金额
	 */
	private BigDecimal invoiceAmount;
	/**
	 * 未开票金额
	 */
	private BigDecimal unInvoiceAmount;
	/**
	 * 收票金额
	 */
	private BigDecimal collectInvoiceAmount;
	/**
	 * 未收票金额
	 */
	private BigDecimal unCollectInvoiceAmount;
	/**
	 * 风险评级
	 */
	private Integer riskRating;
	/**
	 * 项目运作评分
	 */
	private Integer projectScore;
	/**
	 * 风险评级(字符串)
	 */
	private String riskRatingName;
	/**
	 * 项目运作评分(字符串)
	 */
	private String projectScoreName;
	/**
	 * 币种
	 */
	private Integer currencyType;
	/**
	 * 币种名称
	 */
	private String currencyTypeName;

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

	public BigDecimal getAnnualPerformance() {
		return annualPerformance;
	}

	public void setAnnualPerformance(BigDecimal annualPerformance) {
		this.annualPerformance = annualPerformance;
	}

	public BigDecimal getPriorPeriodPerformance() {
		return priorPeriodPerformance;
	}

	public void setPriorPeriodPerformance(BigDecimal priorPeriodPerformance) {
		this.priorPeriodPerformance = priorPeriodPerformance;
	}

	public BigDecimal getCurrPeriodPerformance() {
		return currPeriodPerformance;
	}

	public void setCurrPeriodPerformance(BigDecimal currPeriodPerformance) {
		this.currPeriodPerformance = currPeriodPerformance;
	}

	public BigDecimal getPriorPeriodRefundAmount() {
		return priorPeriodRefundAmount;
	}

	public void setPriorPeriodRefundAmount(BigDecimal priorPeriodRefundAmount) {
		this.priorPeriodRefundAmount = priorPeriodRefundAmount;
	}

	public BigDecimal getCurrPeriodRefundAmount() {
		return currPeriodRefundAmount;
	}

	public void setCurrPeriodRefundAmount(BigDecimal currPeriodRefundAmount) {
		this.currPeriodRefundAmount = currPeriodRefundAmount;
	}

	public BigDecimal getBusiProfit() {
		return busiProfit;
	}

	public void setBusiProfit(BigDecimal busiProfit) {
		this.busiProfit = busiProfit;
	}

	public BigDecimal getBusiProfitRatio() {
		return busiProfitRatio;
	}

	public void setBusiProfitRatio(BigDecimal busiProfitRatio) {
		this.busiProfitRatio = busiProfitRatio;
	}

	public BigDecimal getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(BigDecimal recAmount) {
		this.recAmount = recAmount;
	}

	public BigDecimal getStaticFundAmount() {
		return staticFundAmount;
	}

	public void setStaticFundAmount(BigDecimal staticFundAmount) {
		this.staticFundAmount = staticFundAmount;
	}

	public BigDecimal getAssetTurnoverDays() {
		return assetTurnoverDays;
	}

	public void setAssetTurnoverDays(BigDecimal assetTurnoverDays) {
		this.assetTurnoverDays = assetTurnoverDays;
	}

	public BigDecimal getFundTurnoverDays() {
		return fundTurnoverDays;
	}

	public void setFundTurnoverDays(BigDecimal fundTurnoverDays) {
		this.fundTurnoverDays = fundTurnoverDays;
	}

	public BigDecimal getLogisticsAmount() {
		return logisticsAmount;
	}

	public void setLogisticsAmount(BigDecimal logisticsAmount) {
		this.logisticsAmount = logisticsAmount;
	}

	public BigDecimal getLogisticsAmountRatio() {
		return logisticsAmountRatio;
	}

	public void setLogisticsAmountRatio(BigDecimal logisticsAmountRatio) {
		this.logisticsAmountRatio = logisticsAmountRatio;
	}

	public BigDecimal getMarketAmount() {
		return marketAmount;
	}

	public void setMarketAmount(BigDecimal marketAmount) {
		this.marketAmount = marketAmount;
	}

	public BigDecimal getMarketAmountRatio() {
		return marketAmountRatio;
	}

	public void setMarketAmountRatio(BigDecimal marketAmountRatio) {
		this.marketAmountRatio = marketAmountRatio;
	}

	public BigDecimal getFinanceAmount() {
		return financeAmount;
	}

	public void setFinanceAmount(BigDecimal financeAmount) {
		this.financeAmount = financeAmount;
	}

	public BigDecimal getFinanceAmountRatio() {
		return financeAmountRatio;
	}

	public void setFinanceAmountRatio(BigDecimal financeAmountRatio) {
		this.financeAmountRatio = financeAmountRatio;
	}

	public BigDecimal getManageAmount() {
		return manageAmount;
	}

	public void setManageAmount(BigDecimal manageAmount) {
		this.manageAmount = manageAmount;
	}

	public BigDecimal getManageAmountRatio() {
		return manageAmountRatio;
	}

	public void setManageAmountRatio(BigDecimal manageAmountRatio) {
		this.manageAmountRatio = manageAmountRatio;
	}

	public BigDecimal getFundCostAmount() {
		return fundCostAmount;
	}

	public void setFundCostAmount(BigDecimal fundCostAmount) {
		this.fundCostAmount = fundCostAmount;
	}

	public BigDecimal getFundCostAmountRatio() {
		return fundCostAmountRatio;
	}

	public void setFundCostAmountRatio(BigDecimal fundCostAmountRatio) {
		this.fundCostAmountRatio = fundCostAmountRatio;
	}

	public BigDecimal getNetProfit() {
		return netProfit;
	}

	public void setNetProfit(BigDecimal netProfit) {
		this.netProfit = netProfit;
	}

	public BigDecimal getNetProfitRatio() {
		return netProfitRatio;
	}

	public void setNetProfitRatio(BigDecimal netProfitRatio) {
		this.netProfitRatio = netProfitRatio;
	}

	public BigDecimal getRaxNegCostAmount() {
		return raxNegCostAmount;
	}

	public void setRaxNegCostAmount(BigDecimal raxNegCostAmount) {
		this.raxNegCostAmount = raxNegCostAmount;
	}

	public BigDecimal getRaxNegCostAmountRatio() {
		return raxNegCostAmountRatio;
	}

	public void setRaxNegCostAmountRatio(BigDecimal raxNegCostAmountRatio) {
		this.raxNegCostAmountRatio = raxNegCostAmountRatio;
	}

	public BigDecimal getPerformanceProfit() {
		return performanceProfit;
	}

	public void setPerformanceProfit(BigDecimal performanceProfit) {
		this.performanceProfit = performanceProfit;
	}

	public BigDecimal getRecAccountAmount() {
		return recAccountAmount;
	}

	public void setRecAccountAmount(BigDecimal recAccountAmount) {
		this.recAccountAmount = recAccountAmount;
	}

	public BigDecimal getRecAccountAmountRatio() {
		return recAccountAmountRatio;
	}

	public void setRecAccountAmountRatio(BigDecimal recAccountAmountRatio) {
		this.recAccountAmountRatio = recAccountAmountRatio;
	}

	public BigDecimal getOverDueRecAmount() {
		return overDueRecAmount;
	}

	public void setOverDueRecAmount(BigDecimal overDueRecAmount) {
		this.overDueRecAmount = overDueRecAmount;
	}

	public BigDecimal getStoreAmount() {
		return storeAmount;
	}

	public void setStoreAmount(BigDecimal storeAmount) {
		this.storeAmount = storeAmount;
	}

	public BigDecimal getWeightAvgAge() {
		return weightAvgAge;
	}

	public void setWeightAvgAge(BigDecimal weightAvgAge) {
		this.weightAvgAge = weightAvgAge;
	}

	public BigDecimal getOnwayAmount() {
		return onwayAmount;
	}

	public void setOnwayAmount(BigDecimal onwayAmount) {
		this.onwayAmount = onwayAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(BigDecimal availableAmount) {
		this.availableAmount = availableAmount;
	}

	public BigDecimal getAvailableAmountRatio() {
		return availableAmountRatio;
	}

	public void setAvailableAmountRatio(BigDecimal availableAmountRatio) {
		this.availableAmountRatio = availableAmountRatio;
	}

	public Integer getAccountPeriodDays() {
		return accountPeriodDays;
	}

	public void setAccountPeriodDays(Integer accountPeriodDays) {
		this.accountPeriodDays = accountPeriodDays;
	}

	public BigDecimal getUnUsedfundPoolAmount() {
		return unUsedfundPoolAmount;
	}

	public void setUnUsedfundPoolAmount(BigDecimal unUsedfundPoolAmount) {
		this.unUsedfundPoolAmount = unUsedfundPoolAmount;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public BigDecimal getUnInvoiceAmount() {
		return unInvoiceAmount;
	}

	public void setUnInvoiceAmount(BigDecimal unInvoiceAmount) {
		this.unInvoiceAmount = unInvoiceAmount;
	}

	public BigDecimal getCollectInvoiceAmount() {
		return collectInvoiceAmount;
	}

	public void setCollectInvoiceAmount(BigDecimal collectInvoiceAmount) {
		this.collectInvoiceAmount = collectInvoiceAmount;
	}

	public BigDecimal getUnCollectInvoiceAmount() {
		return unCollectInvoiceAmount;
	}

	public void setUnCollectInvoiceAmount(BigDecimal unCollectInvoiceAmount) {
		this.unCollectInvoiceAmount = unCollectInvoiceAmount;
	}

	public Integer getRiskRating() {
		return riskRating;
	}

	public void setRiskRating(Integer riskRating) {
		this.riskRating = riskRating;
	}

	public Integer getProjectScore() {
		return projectScore;
	}

	public void setProjectScore(Integer projectScore) {
		this.projectScore = projectScore;
	}

	public String getRiskRatingName() {
		return riskRatingName;
	}

	public void setRiskRatingName(String riskRatingName) {
		this.riskRatingName = riskRatingName;
	}

	public String getProjectScoreName() {
		return projectScoreName;
	}

	public void setProjectScoreName(String projectScoreName) {
		this.projectScoreName = projectScoreName;
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
}
