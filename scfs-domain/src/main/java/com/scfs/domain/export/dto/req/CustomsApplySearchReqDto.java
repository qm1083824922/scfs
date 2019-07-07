package com.scfs.domain.export.dto.req;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2016年12月6日.
 */
public class CustomsApplySearchReqDto extends BaseReqDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -125847221956352846L;
	/**
	 * 报关申请编号
	 */
	private String applyNo;

	/**
	 * 报关申请附属编号
	 */
	private String affiliateNo;

	/**
	 * 项目ID
	 */
	private Integer projectId;

	/**
	 * 报关代理公司ID
	 */
	private Integer proxyCompanyId;

	/**
	 * 客户ID
	 */
	private Integer customerId;

	/**
	 * 开始报关日期
	 */
	private String startCustomsDate;

	/**
	 * 结束报关日期
	 */
	private String endCustomsDate;

	/**
	 * 状态 1-待提交 2-已完成
	 */
	private Integer status;

	/**
	 * 是否退税 0-未退税 1-已退税
	 */
	private Integer isReturnTax;

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public String getAffiliateNo() {
		return affiliateNo;
	}

	public void setAffiliateNo(String affiliateNo) {
		this.affiliateNo = affiliateNo;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getProxyCompanyId() {
		return proxyCompanyId;
	}

	public void setProxyCompanyId(Integer proxyCompanyId) {
		this.proxyCompanyId = proxyCompanyId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getStartCustomsDate() {
		return startCustomsDate;
	}

	public void setStartCustomsDate(String startCustomsDate) {
		this.startCustomsDate = startCustomsDate;
	}

	public String getEndCustomsDate() {
		return endCustomsDate;
	}

	public void setEndCustomsDate(String endCustomsDate) {
		this.endCustomsDate = endCustomsDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsReturnTax() {
		return isReturnTax;
	}

	public void setIsReturnTax(Integer isReturnTax) {
		this.isReturnTax = isReturnTax;
	}

}
