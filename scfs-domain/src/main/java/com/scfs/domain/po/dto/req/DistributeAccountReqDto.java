package com.scfs.domain.po.dto.req;

import java.util.Date;

import com.scfs.domain.BaseReqDto;

/**
 * Created by Administrator on 2017年5月13日.
 */
public class DistributeAccountReqDto extends BaseReqDto {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7284605952780342389L;

	private Date voucherDate;
	private Integer projectId;
	private Date startVoucherDate;
	private Date endVoucherDate;

	public Date getVoucherDate() {
		return voucherDate;
	}

	public void setVoucherDate(Date voucherDate) {
		this.voucherDate = voucherDate;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Date getStartVoucherDate() {
		return startVoucherDate;
	}

	public void setStartVoucherDate(Date startVoucherDate) {
		this.startVoucherDate = startVoucherDate;
	}

	public Date getEndVoucherDate() {
		return endVoucherDate;
	}

	public void setEndVoucherDate(Date endVoucherDate) {
		this.endVoucherDate = endVoucherDate;
	}

}
