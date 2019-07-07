package com.scfs.domain.pay.dto.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: MergePayOrderSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */
public class MergePayOrderSearchReqDto extends BaseReqDto {
	private static final long serialVersionUID = -6888722249867326009L;
	/** 经营单位 **/
	private Integer busiUnit;
	/** 项目 **/
	private Integer projectId;
	/** 收款单位 **/
	private Integer payee;
	/** 付款编号 **/
	private String mergePayNo;
	/** 付款类型 **/
	private Integer payType;
	/** 付款方式 **/
	private Integer payWay;
	/** 要求付款开始日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startRequestTime;
	/** 要求付款结束日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endRequestTime;
	/** 状态 **/
	private Integer state;
	/** 付款编号list **/
	private List<String> mergePayNoList;
	private String unionPrintIdentifier;
	/**
	 * 业务类型
	 */
	private Integer bizType;

	/** 根据供应商获取 **/
	private List<Integer> subjectList;

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getPayee() {
		return payee;
	}

	public void setPayee(Integer payee) {
		this.payee = payee;
	}

	public String getMergePayNo() {
		return mergePayNo;
	}

	public void setMergePayNo(String mergePayNo) {
		this.mergePayNo = mergePayNo;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayWay() {
		return payWay;
	}

	public void setPayWay(Integer payWay) {
		this.payWay = payWay;
	}

	public Date getStartRequestTime() {
		return startRequestTime;
	}

	public void setStartRequestTime(Date startRequestTime) {
		this.startRequestTime = startRequestTime;
	}

	public Date getEndRequestTime() {
		return endRequestTime;
	}

	public void setEndRequestTime(Date endRequestTime) {
		this.endRequestTime = endRequestTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public List<String> getMergePayNoList() {
		return mergePayNoList;
	}

	public void setMergePayNoList(List<String> mergePayNoList) {
		this.mergePayNoList = mergePayNoList;
	}

	public String getUnionPrintIdentifier() {
		return unionPrintIdentifier;
	}

	public void setUnionPrintIdentifier(String unionPrintIdentifier) {
		this.unionPrintIdentifier = unionPrintIdentifier;
	}

	public List<Integer> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<Integer> subjectList) {
		this.subjectList = subjectList;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

}
