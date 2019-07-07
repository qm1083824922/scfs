package com.scfs.domain.audit.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Audit {

	private Integer id;
	/** 订单ID */
	private Integer poId;
	/** 单据类型，1表示采购单，2表示費用, 后续业务一次往后添加 */
	private Integer poType;
	/** 单据编号 */
	private String poNo;
	/** 单据日期 */
	private Date poDate;
	/** 审核类型：1正常审核，2转交，3加签 */
	private Integer auditType;
	/** 经营单位ID */
	private Integer businessUnitId;
	/** 项目ID */
	private Integer projectId;
	/** 供应商ID */
	private Integer supplierId;
	/** 客户ID */
	private Integer customerId;
	/** 币种 **/
	private Integer currencyId;
	/** 金额 */
	private BigDecimal amount;
	/** 当前审核人 */
	private String auditor;
	/** 当前审核人ID */
	private Integer auditorId;

	/** 转交或加签给当前审核人的人 */
	private String pauditor;
	/** 转交或加签给当前审核人ID的ID */
	private Integer pauditorId;

	/** 转交或加签给当前审核人ID的原始审核数据的ID */
	private Integer pauditId;

	/** 审核人（审核通过或不通过） */
	private String auditorPass;
	/** 审核人（审核通过或不通过）ID */
	private Integer auditorPassId;
	/** 审核时间（审核通过或不通过） */
	private Date auditorPassAt;
	/** 状态 */
	private Integer state;
	/** 申请人 */
	private String proposer;
	/** 申请人ID */
	private Integer proposerId;
	/** 申请时间 */
	private Date proposerAt;
	/** 创建人 */
	private String creator;
	/** 创建人ID */
	private Integer creatorId;
	/** 创建时间 */
	private Date createAt;
	/** 审核状态,1表示通过，2表示不通过 */
	private Integer auditState;
	/** 建议 */
	private String suggestion;
	/** 逻辑删除 */
	private Integer isDelete;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Date getPoDate() {
		return poDate;
	}

	public void setPoDate(Date poDate) {
		this.poDate = poDate;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
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

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorPass() {
		return auditorPass;
	}

	public void setAuditorPass(String auditorPass) {
		this.auditorPass = auditorPass;
	}

	public Integer getAuditorPassId() {
		return auditorPassId;
	}

	public void setAuditorPassId(Integer auditorPassId) {
		this.auditorPassId = auditorPassId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getProposer() {
		return proposer;
	}

	public void setProposer(String proposer) {
		this.proposer = proposer;
	}

	public Integer getProposerId() {
		return proposerId;
	}

	public void setProposerId(Integer proposerId) {
		this.proposerId = proposerId;
	}

	public Date getProposerAt() {
		return proposerAt;
	}

	public void setProposerAt(Date proposerAt) {
		this.proposerAt = proposerAt;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getAuditType() {
		return auditType;
	}

	public void setAuditType(Integer auditType) {
		this.auditType = auditType;
	}

	public String getPauditor() {
		return pauditor;
	}

	public void setPauditor(String pauditor) {
		this.pauditor = pauditor;
	}

	public Integer getPauditorId() {
		return pauditorId;
	}

	public void setPauditorId(Integer pauditorId) {
		this.pauditorId = pauditorId;
	}

	public Date getAuditorPassAt() {
		return auditorPassAt;
	}

	public void setAuditorPassAt(Date auditorPassAt) {
		this.auditorPassAt = auditorPassAt;
	}

	public Integer getPauditId() {
		return pauditId;
	}

	public void setPauditId(Integer pauditId) {
		this.pauditId = pauditId;
	}

	public Integer getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(Integer currencyId) {
		this.currencyId = currencyId;
	}

}