package com.scfs.domain.base.dto.resp;

import java.util.List;
import java.util.Map;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseEntity;
import com.google.common.collect.Maps;

/**
 * <pre>
 *  客户跟进信息表
 *  File: CustomerFollowResDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月27日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CustomerFollowResDto extends BaseEntity {
	/** 客户id **/
	private Integer customerId;
	/** 所处阶段 1 意向阶段 2 合作阶段 3 已取消 **/
	private Integer stage;
	private String stageName;
	/** 客户简称 **/
	private String abbreviation;
	/** 中文全称 **/
	private String chineseName;
	/** 英文名称 **/
	private String englishName;
	/** 跟进人 **/
	private Integer fllow;
	private String fllowName;
	/** 跟进内容 **/
	private String content;

	/** 操作集合 */
	private List<CodeValue> opertaList;

	public static class Operate {
		public static Map<String, String> operMap = Maps.newHashMap();
		static {
			operMap.put(OperateConsts.EDIT, BusUrlConsts.EDIT_CUSTOMER_FOLLOW);
			operMap.put(OperateConsts.DELETE, BusUrlConsts.DELETE_CUSTOMER_FOLLOW);
		}
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getStage() {
		return stage;
	}

	public void setStage(Integer stage) {
		this.stage = stage;
	}

	public String getStageName() {
		return stageName;
	}

	public void setStageName(String stageName) {
		this.stageName = stageName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public Integer getFllow() {
		return fllow;
	}

	public void setFllow(Integer fllow) {
		this.fllow = fllow;
	}

	public String getFllowName() {
		return fllowName;
	}

	public void setFllowName(String fllowName) {
		this.fllowName = fllowName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<CodeValue> getOpertaList() {
		return opertaList;
	}

	public void setOpertaList(List<CodeValue> opertaList) {
		this.opertaList = opertaList;
	}

}
