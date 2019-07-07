package com.scfs.domain.report.resp;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.fi.dto.resp.FundPoolResDto;

/**
 * <pre>
 * 
 *  File: EntryInfoResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年06月27日				Administrator
 *
 * </pre>
 */
public class EntryInfoResDto {
	/** 销售总额 **/
	private BigDecimal saleAmount;
	private String saleAmountStr;
	/** PO单 **/
	private BigDecimal poAmount;
	private String poAmountStr;
	/** 在仓库存 **/
	private BigDecimal stlAmount;
	private String stlAmountStr;
	/** 平均库龄 **/
	private BigDecimal avgStlAge;
	private String avgStlAgeStr;
	/** 超期库存 **/
	private BigDecimal overStlAmount;
	private String overStlAmountStr;
	/** 超期应收 **/
	private BigDecimal overReceiveAmount;
	private String overReceiveStr;
	/** 动销滞销风险 **/
	private BigDecimal riskAmount;
	private String riskAmountStr;
	/** 资金周转率 **/
	private BigDecimal turnoverRate;

	private BigDecimal sumFundAmount = BigDecimal.ZERO; // 资金总额
	private BigDecimal blanceFundAmount = BigDecimal.ZERO;// 资金余额
	private BigDecimal useFundAmount = BigDecimal.ZERO;// 已使用资金
	private BigDecimal usedAssetAmount = BigDecimal.ZERO;// 已使用资产
	private BigDecimal blanceAssetAmount = BigDecimal.ZERO;// 资产余额
	private BigDecimal advancePayAmount = BigDecimal.ZERO; // 预付总额
	private BigDecimal paymentAmount = BigDecimal.ZERO; // 在途货款总额
	private BigDecimal recAmount = BigDecimal.ZERO; // 应收总额

	private String currencyTypeName;// 币种
	private BigDecimal usedFundAmountRate = BigDecimal.ZERO;// 已使用资金额度比例
	private BigDecimal blanceFundAmountRate = BigDecimal.ZERO;// 资金余额比例
	private BigDecimal usedAssetAmountRate = BigDecimal.ZERO;// 已使用资产比例
	private BigDecimal blanceAssetAmountRate = BigDecimal.ZERO;// 资产余额比例
	private BigDecimal advancePayAmountRate = BigDecimal.ZERO;// 预付比例
	private BigDecimal paymentAmountRate = BigDecimal.ZERO;// 在途货款比例
	private BigDecimal recAmountRate = BigDecimal.ZERO;// 应收比例
	private BigDecimal stlAmountRate = BigDecimal.ZERO;// 库存比例
	private List<FundPoolResDto> busList = new ArrayList<FundPoolResDto>();// 获取所有经营单位比例

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public String getSaleAmountStr() {
		return saleAmountStr;
	}

	public void setSaleAmountStr(String saleAmountStr) {
		this.saleAmountStr = saleAmountStr;
	}

	public BigDecimal getPoAmount() {
		return poAmount;
	}

	public void setPoAmount(BigDecimal poAmount) {
		this.poAmount = poAmount;
	}

	public String getPoAmountStr() {
		return poAmountStr;
	}

	public void setPoAmountStr(String poAmountStr) {
		this.poAmountStr = poAmountStr;
	}

	public BigDecimal getStlAmount() {
		return stlAmount;
	}

	public void setStlAmount(BigDecimal stlAmount) {
		this.stlAmount = stlAmount;
	}

	public String getStlAmountStr() {
		return stlAmountStr;
	}

	public void setStlAmountStr(String stlAmountStr) {
		this.stlAmountStr = stlAmountStr;
	}

	public BigDecimal getAvgStlAge() {
		return avgStlAge;
	}

	public void setAvgStlAge(BigDecimal avgStlAge) {
		this.avgStlAge = avgStlAge;
	}

	public String getAvgStlAgeStr() {
		return avgStlAgeStr;
	}

	public void setAvgStlAgeStr(String avgStlAgeStr) {
		this.avgStlAgeStr = avgStlAgeStr;
	}

	public BigDecimal getOverStlAmount() {
		return overStlAmount;
	}

	public void setOverStlAmount(BigDecimal overStlAmount) {
		this.overStlAmount = overStlAmount;
	}

	public String getOverStlAmountStr() {
		return overStlAmountStr;
	}

	public void setOverStlAmountStr(String overStlAmountStr) {
		this.overStlAmountStr = overStlAmountStr;
	}

	public BigDecimal getOverReceiveAmount() {
		return overReceiveAmount;
	}

	public void setOverReceiveAmount(BigDecimal overReceiveAmount) {
		this.overReceiveAmount = overReceiveAmount;
	}

	public String getOverReceiveStr() {
		return overReceiveStr;
	}

	public void setOverReceiveStr(String overReceiveStr) {
		this.overReceiveStr = overReceiveStr;
	}

	public BigDecimal getRiskAmount() {
		return riskAmount;
	}

	public void setRiskAmount(BigDecimal riskAmount) {
		this.riskAmount = riskAmount;
	}

	public String getRiskAmountStr() {
		return riskAmountStr;
	}

	public void setRiskAmountStr(String riskAmountStr) {
		this.riskAmountStr = riskAmountStr;
	}

	public BigDecimal getTurnoverRate() {
		return turnoverRate;
	}

	public void setTurnoverRate(BigDecimal turnoverRate) {
		this.turnoverRate = turnoverRate;
	}

	public BigDecimal getSumFundAmount() {
		return sumFundAmount;
	}

	public void setSumFundAmount(BigDecimal sumFundAmount) {
		this.sumFundAmount = sumFundAmount;
	}

	public BigDecimal getBlanceFundAmount() {
		return blanceFundAmount;
	}

	public void setBlanceFundAmount(BigDecimal blanceFundAmount) {
		this.blanceFundAmount = blanceFundAmount;
	}

	public BigDecimal getUseFundAmount() {
		return useFundAmount;
	}

	public void setUseFundAmount(BigDecimal useFundAmount) {
		this.useFundAmount = useFundAmount;
	}

	public BigDecimal getUsedAssetAmount() {
		return usedAssetAmount;
	}

	public void setUsedAssetAmount(BigDecimal usedAssetAmount) {
		this.usedAssetAmount = usedAssetAmount;
	}

	public BigDecimal getBlanceAssetAmount() {
		return blanceAssetAmount;
	}

	public void setBlanceAssetAmount(BigDecimal blanceAssetAmount) {
		this.blanceAssetAmount = blanceAssetAmount;
	}

	public BigDecimal getAdvancePayAmount() {
		return advancePayAmount;
	}

	public void setAdvancePayAmount(BigDecimal advancePayAmount) {
		this.advancePayAmount = advancePayAmount;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public BigDecimal getRecAmount() {
		return recAmount;
	}

	public void setRecAmount(BigDecimal recAmount) {
		this.recAmount = recAmount;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public BigDecimal getUsedFundAmountRate() {
		return usedFundAmountRate;
	}

	public void setUsedFundAmountRate(BigDecimal usedFundAmountRate) {
		this.usedFundAmountRate = usedFundAmountRate;
	}

	public BigDecimal getBlanceFundAmountRate() {
		return blanceFundAmountRate;
	}

	public void setBlanceFundAmountRate(BigDecimal blanceFundAmountRate) {
		this.blanceFundAmountRate = blanceFundAmountRate;
	}

	public BigDecimal getUsedAssetAmountRate() {
		return usedAssetAmountRate;
	}

	public void setUsedAssetAmountRate(BigDecimal usedAssetAmountRate) {
		this.usedAssetAmountRate = usedAssetAmountRate;
	}

	public BigDecimal getBlanceAssetAmountRate() {
		return blanceAssetAmountRate;
	}

	public void setBlanceAssetAmountRate(BigDecimal blanceAssetAmountRate) {
		this.blanceAssetAmountRate = blanceAssetAmountRate;
	}

	public BigDecimal getAdvancePayAmountRate() {
		return advancePayAmountRate;
	}

	public void setAdvancePayAmountRate(BigDecimal advancePayAmountRate) {
		this.advancePayAmountRate = advancePayAmountRate;
	}

	public BigDecimal getPaymentAmountRate() {
		return paymentAmountRate;
	}

	public void setPaymentAmountRate(BigDecimal paymentAmountRate) {
		this.paymentAmountRate = paymentAmountRate;
	}

	public BigDecimal getRecAmountRate() {
		return recAmountRate;
	}

	public void setRecAmountRate(BigDecimal recAmountRate) {
		this.recAmountRate = recAmountRate;
	}

	public BigDecimal getStlAmountRate() {
		return stlAmountRate;
	}

	public void setStlAmountRate(BigDecimal stlAmountRate) {
		this.stlAmountRate = stlAmountRate;
	}

	public List<FundPoolResDto> getBusList() {
		return busList;
	}

	public void setBusList(List<FundPoolResDto> busList) {
		this.busList = busList;
	}

}
