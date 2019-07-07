package com.scfs.domain.project.dto.resp;

import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.google.common.collect.Maps;

public class ProjectPoolResDto {

	private Integer id;

	/** 项目ID */
	private Integer projectId;
	/** 项目 */
	private String projectname;
	/** 项目额度 */
	private String projectAmount;
	/** 项目额度(CNY) */
	private String projectAmountCny;

	/** 币种 */
	private Integer currencyType;
	/** 币种 */
	private String currencyTypeName;

	/** 已使用资金额度 */
	private String usedFundAmount;

	/** 已使用资金额度(CNY) */
	private String usedFundAmountCny;

	/** 资金余额 */
	private String remainFundAmount;

	/** 资金余额(CNY) */
	private String remainFundAmountCny;

	/** 已使用资产额度 */
	private String usedAssetAmount;

	/** 已使用资产额度(CNY) */
	private String usedAssetAmountCny;

	/** 资产余额 */
	private String remainAssetAmount;

	/** 资产余额(CNY) */
	private String remainAssetAmountCny;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.DETAIL, BusUrlConsts.QUERYPROJECTPOOLBYID);
		}
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

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

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getProjectAmount() {
		return projectAmount;
	}

	public void setProjectAmount(String projectAmount) {
		this.projectAmount = projectAmount;
	}

	public String getProjectAmountCny() {
		return projectAmountCny;
	}

	public void setProjectAmountCny(String projectAmountCny) {
		this.projectAmountCny = projectAmountCny;
	}

	public String getUsedFundAmount() {
		return usedFundAmount;
	}

	public void setUsedFundAmount(String usedFundAmount) {
		this.usedFundAmount = usedFundAmount;
	}

	public String getUsedFundAmountCny() {
		return usedFundAmountCny;
	}

	public void setUsedFundAmountCny(String usedFundAmountCny) {
		this.usedFundAmountCny = usedFundAmountCny;
	}

	public String getRemainFundAmount() {
		return remainFundAmount;
	}

	public void setRemainFundAmount(String remainFundAmount) {
		this.remainFundAmount = remainFundAmount;
	}

	public String getRemainFundAmountCny() {
		return remainFundAmountCny;
	}

	public void setRemainFundAmountCny(String remainFundAmountCny) {
		this.remainFundAmountCny = remainFundAmountCny;
	}

	public String getUsedAssetAmount() {
		return usedAssetAmount;
	}

	public void setUsedAssetAmount(String usedAssetAmount) {
		this.usedAssetAmount = usedAssetAmount;
	}

	public String getUsedAssetAmountCny() {
		return usedAssetAmountCny;
	}

	public void setUsedAssetAmountCny(String usedAssetAmountCny) {
		this.usedAssetAmountCny = usedAssetAmountCny;
	}

	public String getRemainAssetAmount() {
		return remainAssetAmount;
	}

	public void setRemainAssetAmount(String remainAssetAmount) {
		this.remainAssetAmount = remainAssetAmount;
	}

	public String getRemainAssetAmountCny() {
		return remainAssetAmountCny;
	}

	public void setRemainAssetAmountCny(String remainAssetAmountCny) {
		this.remainAssetAmountCny = remainAssetAmountCny;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}
}
