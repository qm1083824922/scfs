package com.scfs.domain.report.resp;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;

/**
 * <pre>
 * 
 *  File: ProfitTargetResDto.java
 *  Description: 业务指标目标值
 *  TODO
 *  Date,					Who,				
 *  2017年7月14日				Administrator
 *
 * </pre>
 */
public class ProfitTargetResDto {
	private Integer id;
	/** 月份 **/
	private String issue;
	/** 业务员id **/
	private Integer userId;
	private String userIdName;
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
	/** 业务审核员 **/
	private Integer busiId;
	private String busiIdName;
	/** 部门主管审核人 **/
	private Integer deptManageId;
	private String deptManageIdName;
	/** 状态 0 待提交 1 待业务主管审核 2 待部门主管审核 3 已完成 **/
	private Integer state;
	private String stateName;

	/** 利润完成值 **/
	private BigDecimal profitAmount = BigDecimal.ZERO;
	/** 业务利润完成值 **/
	private BigDecimal bizManagerAmount = BigDecimal.ZERO;
	/** 销售毛利润完成值 **/
	private BigDecimal saleBlanceAmount = BigDecimal.ZERO;
	/** 经营收入完成值 **/
	private BigDecimal saleAmount = BigDecimal.ZERO;
	/** 管理费用完成值 **/
	private BigDecimal manageAmount = BigDecimal.ZERO;
	/** 经营费用完成值 **/
	private BigDecimal warehouseAmount = BigDecimal.ZERO;
	/** 资金成本完成值 **/
	private BigDecimal fundCost = BigDecimal.ZERO;

	/** 利润完成比例 **/
	private String profitAmountRateStr;
	/** 业务利润完成比例 **/
	private String bizManagerAmountRateStr;
	/** 销售毛利润完成比例 **/
	private String saleBlanceAmountRateStr;
	/** 经营收入完成比例 **/
	private String saleAmountRateStr;
	/** 管理费用完成比例 **/
	private String manageAmountRateStr;
	/** 经营费用完成比例 **/
	private String warehouseAmountRateStr;
	/** 资金成本完成比例 **/
	private String fundCostRateStr;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = new HashMap<String, String>();
		static {
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_PROFIT_TARGET);
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.DETAIL_PROFIT_TARGET);
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_PROFIT_TARGET);
			operMap.put(OperateConsts.SUBMIT, BusUrlConsts.SUBMIT_PROFIT_TARGET);
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserIdName() {
		return userIdName;
	}

	public void setUserIdName(String userIdName) {
		this.userIdName = userIdName;
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

	public Integer getBusiId() {
		return busiId;
	}

	public void setBusiId(Integer busiId) {
		this.busiId = busiId;
	}

	public String getBusiIdName() {
		return busiIdName;
	}

	public void setBusiIdName(String busiIdName) {
		this.busiIdName = busiIdName;
	}

	public Integer getDeptManageId() {
		return deptManageId;
	}

	public void setDeptManageId(Integer deptManageId) {
		this.deptManageId = deptManageId;
	}

	public String getDeptManageIdName() {
		return deptManageIdName;
	}

	public void setDeptManageIdName(String deptManageIdName) {
		this.deptManageIdName = deptManageIdName;
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

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
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

	public BigDecimal getSaleBlanceAmount() {
		return saleBlanceAmount;
	}

	public void setSaleBlanceAmount(BigDecimal saleBlanceAmount) {
		this.saleBlanceAmount = saleBlanceAmount;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getManageAmount() {
		return manageAmount;
	}

	public void setManageAmount(BigDecimal manageAmount) {
		this.manageAmount = manageAmount;
	}

	public BigDecimal getWarehouseAmount() {
		return warehouseAmount;
	}

	public void setWarehouseAmount(BigDecimal warehouseAmount) {
		this.warehouseAmount = warehouseAmount;
	}

	public BigDecimal getFundCost() {
		return fundCost;
	}

	public void setFundCost(BigDecimal fundCost) {
		this.fundCost = fundCost;
	}

	public String getProfitAmountRateStr() {
		return profitAmountRateStr;
	}

	public void setProfitAmountRateStr(String profitAmountRateStr) {
		this.profitAmountRateStr = profitAmountRateStr;
	}

	public String getBizManagerAmountRateStr() {
		return bizManagerAmountRateStr;
	}

	public void setBizManagerAmountRateStr(String bizManagerAmountRateStr) {
		this.bizManagerAmountRateStr = bizManagerAmountRateStr;
	}

	public String getSaleBlanceAmountRateStr() {
		return saleBlanceAmountRateStr;
	}

	public void setSaleBlanceAmountRateStr(String saleBlanceAmountRateStr) {
		this.saleBlanceAmountRateStr = saleBlanceAmountRateStr;
	}

	public String getSaleAmountRateStr() {
		return saleAmountRateStr;
	}

	public void setSaleAmountRateStr(String saleAmountRateStr) {
		this.saleAmountRateStr = saleAmountRateStr;
	}

	public String getManageAmountRateStr() {
		return manageAmountRateStr;
	}

	public void setManageAmountRateStr(String manageAmountRateStr) {
		this.manageAmountRateStr = manageAmountRateStr;
	}

	public String getWarehouseAmountRateStr() {
		return warehouseAmountRateStr;
	}

	public void setWarehouseAmountRateStr(String warehouseAmountRateStr) {
		this.warehouseAmountRateStr = warehouseAmountRateStr;
	}

	public String getFundCostRateStr() {
		return fundCostRateStr;
	}

	public void setFundCostRateStr(String fundCostRateStr) {
		this.fundCostRateStr = fundCostRateStr;
	}

}
