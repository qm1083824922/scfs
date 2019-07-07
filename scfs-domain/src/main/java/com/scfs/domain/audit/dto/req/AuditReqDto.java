package com.scfs.domain.audit.dto.req;

import java.util.List;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.CodeValue;

public class AuditReqDto extends BaseReqDto {

	private static final long serialVersionUID = 1L;

	/** 状态 */
	private Integer state;
	/** 项目ID */
	private Integer projectId;
	private String projectName;

	/** 供应商ID */
	private Integer supplierId;
	/** 客户ID */
	private Integer customerId;
	/** 订单类型 */
	private Integer poId;
	/** 订单类型 */
	private Integer poType;
	/** 单据编号 */
	private String poNo;
	/** 当前审核人ID */
	private Integer auditorId;
	/** 申请人ID */
	private Integer proposerId;
	/** 币种 **/
	private Integer currencyId;
	/** 用户下项目列表 **/
	List<CodeValue> codeList;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getPoType() {
		return poType;
	}

	public void setPoType(Integer poType) {
		this.poType = poType;
	}

	public String getPoNo() {
		return poNo;
	}

	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public Integer getProposerId() {
		return proposerId;
	}

	public void setProposerId(Integer proposerId) {
		this.proposerId = proposerId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

	public List<CodeValue> getCodeList() {
		return codeList;
	}

	public void setCodeList(List<CodeValue> codeList) {
		this.codeList = codeList;
	}

}
