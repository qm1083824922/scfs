package com.scfs.domain.pay.dto.req;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 * 
 *  File: PayOrderSearchReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月08日			Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class PayOrderSearchReqDto extends BaseReqDto {
	/** 经营单位 **/
	private Integer busiUnit;
	/** 项目 **/
	private Integer projectId;
	/** 收款单位 **/
	private Integer payee;
	/** 付款编号 **/
	private String payNo;
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
	/** 确认开始日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date starConfirmorAt;
	/** 确认结束日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endConfirmorAt;
	/** 状态 **/
	private Integer state;
	/** 附属编号 **/
	private String attachedNumbe;
	/** 付款编号list **/
	private List<String> payNoList;
	private String unionOverIdentifier;
	private String unionPrintIdentifier;
	private Integer canMerge; // 1:可以合并
	private Integer payer;
	private Integer payAccountId;
	private Integer currencyType;
	/** 微信区别标识字段 **/
	private String wechatSource;
	/** 付款支付类型 0-全部 1-预付 2-尾款 **/
	private Integer payWayType;
	private Integer payWayTypeEq;

	/** 付款账户 **/
	private Integer paymentAccount;
	/** 实际付款币种 **/
	private Integer realCurrencyType;
	private List<Integer> payTypeList;

	/** 要求付款开始日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startCreateTime;
	/** 要求付款结束日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endCreateTime;
	/** 根据供应商获取 **/
	private List<Integer> subjectList;
	/** 是否合并付款 **/
	private Integer isMergePay;
	/** 业务类型 */
	private Integer bizType;
	/** 是否无订单 0-否 1-是 */
	private Integer noneOrderFlag;
	/** 是否核销 0-未核销 1-已核销 */
	private Integer writeOffFlag;

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

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
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

	public Date getStarConfirmorAt() {
		return starConfirmorAt;
	}

	public void setStarConfirmorAt(Date starConfirmorAt) {
		this.starConfirmorAt = starConfirmorAt;
	}

	public Date getEndConfirmorAt() {
		return endConfirmorAt;
	}

	public void setEndConfirmorAt(Date endConfirmorAt) {
		this.endConfirmorAt = endConfirmorAt;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getAttachedNumbe() {
		return attachedNumbe;
	}

	public void setAttachedNumbe(String attachedNumbe) {
		this.attachedNumbe = attachedNumbe;
	}

	public List<String> getPayNoList() {
		return payNoList;
	}

	public void setPayNoList(List<String> payNoList) {
		this.payNoList = payNoList;
	}

	public String getUnionOverIdentifier() {
		return unionOverIdentifier;
	}

	public void setUnionOverIdentifier(String unionOverIdentifier) {
		this.unionOverIdentifier = unionOverIdentifier;
	}

	public String getUnionPrintIdentifier() {
		return unionPrintIdentifier;
	}

	public void setUnionPrintIdentifier(String unionPrintIdentifier) {
		this.unionPrintIdentifier = unionPrintIdentifier;
	}

	public Integer getCanMerge() {
		return canMerge;
	}

	public void setCanMerge(Integer canMerge) {
		this.canMerge = canMerge;
	}

	public Integer getPayer() {
		return payer;
	}

	public void setPayer(Integer payer) {
		this.payer = payer;
	}

	public Integer getPayAccountId() {
		return payAccountId;
	}

	public void setPayAccountId(Integer payAccountId) {
		this.payAccountId = payAccountId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getWechatSource() {
		return wechatSource;
	}

	public void setWechatSource(String wechatSource) {
		this.wechatSource = wechatSource;
	}

	public Integer getPayWayType() {
		return payWayType;
	}

	public void setPayWayType(Integer payWayType) {
		this.payWayType = payWayType;
	}

	public Integer getPayWayTypeEq() {
		return payWayTypeEq;
	}

	public void setPayWayTypeEq(Integer payWayTypeEq) {
		this.payWayTypeEq = payWayTypeEq;
	}

	public Integer getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(Integer paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public Integer getRealCurrencyType() {
		return realCurrencyType;
	}

	public void setRealCurrencyType(Integer realCurrencyType) {
		this.realCurrencyType = realCurrencyType;
	}

	public List<Integer> getPayTypeList() {
		return payTypeList;
	}

	public void setPayTypeList(List<Integer> payTypeList) {
		this.payTypeList = payTypeList;
	}

	public Date getStartCreateTime() {
		return startCreateTime;
	}

	public void setStartCreateTime(Date startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public List<Integer> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<Integer> subjectList) {
		this.subjectList = subjectList;
	}

	public Integer getIsMergePay() {
		return isMergePay;
	}

	public void setIsMergePay(Integer isMergePay) {
		this.isMergePay = isMergePay;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getNoneOrderFlag() {
		return noneOrderFlag;
	}

	public void setNoneOrderFlag(Integer noneOrderFlag) {
		this.noneOrderFlag = noneOrderFlag;
	}

	public Integer getWriteOffFlag() {
		return writeOffFlag;
	}

	public void setWriteOffFlag(Integer writeOffFlag) {
		this.writeOffFlag = writeOffFlag;
	}

}
