package com.scfs.domain.fi.dto.req;

import java.util.ArrayList;
import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.fi.entity.CopeReceiptRel;

/**
 * <pre>
 * 
 *  File: CopeReceiptRelReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 * 2017年10月31日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class CopeReceiptRelReqDto extends BaseReqDto {

	private List<CopeReceiptRel> rels = new ArrayList<CopeReceiptRel>();
	/** 水单id **/
	private Integer receiptId;
	/** 应付明细id **/
	private Integer copeDtlID;
	/**
	 * 项目id
	 */
	private Integer projectId;
	/**
	 * 客户id
	 */
	private Integer customerId;

	/**
	 * 经营单位
	 */
	private Integer busiUnitId;
	/**
	 * 币种
	 */
	private Integer currnecyType;

	/**
	 * 应付管理id
	 */
	private Integer copeId;

	public List<CopeReceiptRel> getRels() {
		return rels;
	}

	public void setRels(List<CopeReceiptRel> rels) {
		this.rels = rels;
	}

	public Integer getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}

	public Integer getCopeDtlID() {
		return copeDtlID;
	}

	public void setCopeDtlID(Integer copeDtlID) {
		this.copeDtlID = copeDtlID;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getBusiUnitId() {
		return busiUnitId;
	}

	public void setBusiUnitId(Integer busiUnitId) {
		this.busiUnitId = busiUnitId;
	}

	public Integer getCurrnecyType() {
		return currnecyType;
	}

	public void setCurrnecyType(Integer currnecyType) {
		this.currnecyType = currnecyType;
	}

	public Integer getCopeId() {
		return copeId;
	}

	public void setCopeId(Integer copeId) {
		this.copeId = copeId;
	}

}
