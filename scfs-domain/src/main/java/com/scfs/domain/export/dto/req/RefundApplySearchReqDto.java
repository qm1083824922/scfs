package com.scfs.domain.export.dto.req;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.scfs.domain.BaseReqDto;

/**
 * <pre>
 *  
 *  File: RefundApplyReqDto.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月06日				Administrator
 *
 * </pre>
 */
@SuppressWarnings("serial")
public class RefundApplySearchReqDto extends BaseReqDto {
	/** 出口退税申请id **/
	private Integer id;
	/** 项目id **/
	private Integer projectId;
	/** 退税申请编号 **/
	private String refundApplyNo;
	/** 退税附属编号 **/
	private String refundAttachNo;
	/** 开始退税日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startApplyDate;
	/** 结束退税日期 **/
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endApplyDate;
	/** 客户id **/
	private Integer custId;
	/** 状态 1:待提交 2.待财务审核 3,已完成 **/
	private Integer state;

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

	public String getRefundApplyNo() {
		return refundApplyNo;
	}

	public void setRefundApplyNo(String refundApplyNo) {
		this.refundApplyNo = refundApplyNo;
	}

	public String getRefundAttachNo() {
		return refundAttachNo;
	}

	public void setRefundAttachNo(String refundAttachNo) {
		this.refundAttachNo = refundAttachNo;
	}

	public Date getStartApplyDate() {
		return startApplyDate;
	}

	public void setStartApplyDate(Date startApplyDate) {
		this.startApplyDate = startApplyDate;
	}

	public Date getEndApplyDate() {
		return endApplyDate;
	}

	public void setEndApplyDate(Date endApplyDate) {
		this.endApplyDate = endApplyDate;
	}

	public Integer getCustId() {
		return custId;
	}

	public void setCustId(Integer custId) {
		this.custId = custId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
