package com.scfs.domain.project.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.scfs.domain.base.entity.BaseEntity;

public class ProjectItem extends BaseEntity {

	private static final long serialVersionUID = 2988438982675793890L;
	/** 订单总金额 */
	private BigDecimal proTotalAmount;
	/** 自增ID */
	private Integer id;
	/** 编号 */
	private String itemNo;
	/** 项目ID */
	private Integer projectId;
	/** 银行名称 */
	private Integer accountMethod;
	private String accountMethodValue;
	private Integer bankId;
	private String bankName;
	/** 项目ID */
	private String projectName;
	/** 经营单位 */
	private Integer businessUnitId;
	private Integer customerId;
	/** 经营单位 */
	private String businessUnitName;
	/** 业务类型 */
	private String businessType;
	private String primaryCustomerName;
	/** 最低消费天数 */
	private Integer minSaleDay;
	/** 开始日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;
	/** 结束日期 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;
	/** 额度总额 */
	private BigDecimal totalAmount;
	/** 额度总额 */
	private String totalAmountValue;
	/** 额度总额单位 */
	private Integer amountCurrency;
	/** 额度总额单位 */
	private String amountCurrencyValue;
	/** 是否资金占用结算 */
	private Integer isFundAccount;
	private Integer dayRules;
	/** 是否资金占用结算 */
	private String isFundAccountName;
	/** 资金使用帐期 */
	private Integer fundAccountPeriod;
	/** 资金使用帐期 */
	private String fundAccountPeriodValue;
	/** 资金月服务费率 */
	private BigDecimal fundMonthRate;
	/** 日违约金费率 */
	private BigDecimal dayPenalRate;
	/** 违约宽限期 */
	private Integer penalGracePeriod;
	/** 是否操作费结算 */
	private Integer isOperateAccount;
	/** 是否操作费结算 */
	private String isOperateAccountValue;
	/** 操作费分类 */
	private Integer operateFeeType;
	/** 操作服务费率 */
	private BigDecimal operateFeeRate;
	/** 客户对帐日期 */
	private Integer clientCheckDay;
	private Integer clientCheckWeek;
	private String clientCheckWeekName;
	/** 供应商对帐日期 */
	private Integer supplierCheckDay;
	private Integer supplierCheckWeek;
	private String supplierCheckWeekName;
	/** 付款币种 */
	private Integer payCurrency;
	/** 付款币种 */
	private String payCurrencyValue;
	/** 收款币种 */
	private Integer receiveCurrency;
	private String receiveCurrencyValue;
	/** 付款比例 */
	private BigDecimal payRate;
	/** 是否代理出口 */
	private Integer isAgencyExport;
	/** 是否代理出口 */
	private String isAgencyExportValue;
	/** 代理出口费率 */
	private BigDecimal agencyExportRate;
	/** 结算汇率类型 */
	private Integer accountRateType;
	/** 结算汇率类型 */
	private String accountRateTypeValue;
	/** 结算汇率 */
	private BigDecimal accountRate;
	/** PAYPAL帐户 */
	private String paypalAccount;
	/** PAYPAL帐户结算方式 */
	private Integer paypalAccountType;
	/** PAYPAL帐户结算方式：天数 */
	private Integer paypalDays;
	/** PAYPAL帐户结算方式：固定日类型 */
	private Integer paypalFixdayType;
	/** PAYPAL帐户结算方式：固定日 */
	private Integer paypalFixday;
	/** PAYPAL帐户结算方式：金额 */
	private BigDecimal paypalAmount;
	/** PAYPAL帐户结算方式：金额单位 */
	private Integer paypalAmountCurrency;
	/** PAYPAL帐户结算方式：金额单位 */
	private String paypalAmountCurrencyValue;
	private String customerName;
	private String supplierName;
	/** 我司联系地址 */
	private String address;
	/** 我司联系人 */
	private String contacts;
	/** 我司联系电话 */
	private String phone;
	/** 我司联系传真 */
	private String fax;
	/** 我司联系邮箱 */
	private String email;
	/** 客户联系地址 */
	private String clientAddress;
	/** 客户联系人 */
	private String clientContacts;
	/** 客户联系电话 */
	private String clientPhone;
	/** 客户联系传真 */
	private String clientFax;
	/** 客户联系邮箱 */
	private String clientEmail;
	/** 供应商联系地址 */
	private String supplierAddress;
	/** 供应商联系人 */
	private String supplierContacts;
	/** 供应商联系电话 */
	private String supplierPhone;
	/** 供应商联系传真 */
	private String supplierFax;
	/** 供应商联系邮箱 */
	private String supplierEmail;
	/** 备注 */
	private String remark;
	/** 创建人 */
	private String creator;
	/** 作废人 */
	private String deleter;
	/** 创建时间 */
	private Date createAt;
	/** 更新时间 */
	private Date updateAt;
	/** 作废时间 */
	private Date deleteAt;
	/** 状态 */
	private Integer status;
	/** 删除标记 */
	private Integer isDelete;
	private String businesstypeValue;
	/** 操作服务费率 */
	private BigDecimal operaterate;
	/** 项目对账日期 */
	private Integer projectcheckdate;
	private Integer projectCheckWeek;
	private String projectCheckWeekName;
	/** 项目对账方式 */
	private Integer projectchecktype;
	private String projectchecktypeValue;
	/** 客户对账方式 */
	private Integer clientchecktype;
	private String clientchecktypeValue;
	/** 供应商对账方式 */
	private Integer supplierchecktype;
	private String supplierchecktypeValue;
	/** 代理进口费率 */
	private BigDecimal agencyimportrate;
	/** 是否代理进口 */
	private Integer isagencyimport;
	/** 是否代理进口 */
	private String isagencyimportValue;
	/** 文件上传标志 */
	private Integer file;
	/** 是否固定点数 */
	private Integer fixedpoints;
	/** * 是否固定点数 */
	private String fixedpointsValue;
	/** 价差固定点数 */
	private BigDecimal spreadfixedpoints;
	/** 签收标准 */
	private Integer signStandard;
	/** 身份证号码 */
	private String certificateId;
	/** 身份证姓名 */
	private String certificateName;
	/** 公章名 */
	private String officialSeal;
	/** 签收标准 */
	private String signStandardValue;
	private BigDecimal singleAmount;
	private String singleAmountValue;
	private Integer libraryAge;
	private String dayRulesValue;
	private Integer settleType;
	private String settleTypeValue;
	/** 结算计算方式 1-日 2-分段 **/
	private Integer paypalCalcType;
	private String paypalCalcTypeName;
	/** 付款审核方式 **/
	private String payAuditType;
	private String payAuditTypeName;
	/** 结算计费方式 **/
	private List<ProjectItemSegment> projectItemSegmentList;
	/** 付款周期 **/
	private Integer payCycle;

	public BigDecimal getProTotalAmount() {
		return proTotalAmount;
	}

	public void setProTotalAmount(BigDecimal proTotalAmount) {
		this.proTotalAmount = proTotalAmount;
	}

	public Integer getAccountMethod() {
		return accountMethod;
	}

	public void setAccountMethod(Integer accountMethod) {
		this.accountMethod = accountMethod;
	}

	public Integer getBankId() {
		return bankId;
	}

	public void setBankId(Integer bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public Integer getDayRules() {
		return dayRules;
	}

	public void setDayRules(Integer dayRules) {
		this.dayRules = dayRules;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBusinessUnitName() {
		return businessUnitName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public String getPaypalAmountCurrencyValue() {
		return paypalAmountCurrencyValue;
	}

	public void setPaypalAmountCurrencyValue(String paypalAmountCurrencyValue) {
		this.paypalAmountCurrencyValue = paypalAmountCurrencyValue;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getIsFundAccountName() {
		return isFundAccountName;
	}

	public void setIsFundAccountName(String isFundAccountName) {
		this.isFundAccountName = isFundAccountName;
	}

	public Integer getSignStandard() {
		return signStandard;
	}

	public void setSignStandard(Integer signStandard) {
		this.signStandard = signStandard;
	}

	public String getCertificateId() {
		return certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public String getSingleAmountValue() {
		return singleAmountValue;
	}

	public void setSingleAmountValue(String singleAmountValue) {
		this.singleAmountValue = singleAmountValue;
	}

	public String getCertificateName() {
		return certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getOfficialSeal() {
		return officialSeal;
	}

	public void setOfficialSeal(String officialSeal) {
		this.officialSeal = officialSeal;
	}

	public String getPayCurrencyValue() {
		return payCurrencyValue;
	}

	public void setPayCurrencyValue(String payCurrencyValue) {
		this.payCurrencyValue = payCurrencyValue;
	}

	public String getReceiveCurrencyValue() {
		return receiveCurrencyValue;
	}

	public Integer getMinSaleDay() {
		return minSaleDay;
	}

	public void setMinSaleDay(Integer minSaleDay) {
		this.minSaleDay = minSaleDay;
	}

	public void setReceiveCurrencyValue(String receiveCurrencyValue) {
		this.receiveCurrencyValue = receiveCurrencyValue;
	}

	public String getIsAgencyExportValue() {
		return isAgencyExportValue;
	}

	public String getTotalAmountValue() {
		return totalAmountValue;
	}

	public void setTotalAmountValue(String totalAmountValue) {
		this.totalAmountValue = totalAmountValue;
	}

	public void setIsAgencyExportValue(String isAgencyExportValue) {
		this.isAgencyExportValue = isAgencyExportValue;
	}

	public String getAccountRateTypeValue() {
		return accountRateTypeValue;
	}

	public void setAccountRateTypeValue(String accountRateTypeValue) {
		this.accountRateTypeValue = accountRateTypeValue;
	}

	public Integer getProjectchecktype() {
		return projectchecktype;
	}

	public void setProjectchecktype(Integer projectchecktype) {
		this.projectchecktype = projectchecktype;
	}

	public String getProjectchecktypeValue() {
		return projectchecktypeValue;
	}

	public void setProjectchecktypeValue(String projectchecktypeValue) {
		this.projectchecktypeValue = projectchecktypeValue;
	}

	public Integer getClientchecktype() {
		return clientchecktype;
	}

	public void setClientchecktype(Integer clientchecktype) {
		this.clientchecktype = clientchecktype;
	}

	public String getClientchecktypeValue() {
		return clientchecktypeValue;
	}

	public void setClientchecktypeValue(String clientchecktypeValue) {
		this.clientchecktypeValue = clientchecktypeValue;
	}

	public Integer getSupplierchecktype() {
		return supplierchecktype;
	}

	public void setSupplierchecktype(Integer supplierchecktype) {
		this.supplierchecktype = supplierchecktype;
	}

	public String getSupplierchecktypeValue() {
		return supplierchecktypeValue;
	}

	public void setSupplierchecktypeValue(String supplierchecktypeValue) {
		this.supplierchecktypeValue = supplierchecktypeValue;
	}

	public String getIsagencyimportValue() {
		return isagencyimportValue;
	}

	public void setIsagencyimportValue(String isagencyimportValue) {
		this.isagencyimportValue = isagencyimportValue;
	}

	public String getFixedpointsValue() {
		return fixedpointsValue;
	}

	public void setFixedpointsValue(String fixedpointsValue) {
		this.fixedpointsValue = fixedpointsValue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getItemNo() {
		return itemNo;
	}

	public void setItemNo(String itemNo) {
		this.itemNo = itemNo == null ? null : itemNo.trim();
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getBusinessUnitId() {
		return businessUnitId;
	}

	public void setBusinessUnitId(Integer businessUnitId) {
		this.businessUnitId = businessUnitId;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getAmountCurrency() {
		return amountCurrency;
	}

	public void setAmountCurrency(Integer amountCurrency) {
		this.amountCurrency = amountCurrency;
	}

	public Integer getIsFundAccount() {
		return isFundAccount;
	}

	public void setIsFundAccount(Integer isFundAccount) {
		this.isFundAccount = isFundAccount;
	}

	public Integer getFundAccountPeriod() {
		return fundAccountPeriod;
	}

	public void setFundAccountPeriod(Integer fundAccountPeriod) {
		this.fundAccountPeriod = fundAccountPeriod;
	}

	public BigDecimal getFundMonthRate() {
		return fundMonthRate;
	}

	public void setFundMonthRate(BigDecimal fundMonthRate) {
		this.fundMonthRate = fundMonthRate;
	}

	public BigDecimal getDayPenalRate() {
		return dayPenalRate;
	}

	public void setDayPenalRate(BigDecimal dayPenalRate) {
		this.dayPenalRate = dayPenalRate;
	}

	public Integer getPenalGracePeriod() {
		return penalGracePeriod;
	}

	public void setPenalGracePeriod(Integer penalGracePeriod) {
		this.penalGracePeriod = penalGracePeriod;
	}

	public Integer getIsOperateAccount() {
		return isOperateAccount;
	}

	public void setIsOperateAccount(Integer isOperateAccount) {
		this.isOperateAccount = isOperateAccount;
	}

	public Integer getOperateFeeType() {
		return operateFeeType;
	}

	public void setOperateFeeType(Integer operateFeeType) {
		this.operateFeeType = operateFeeType;
	}

	public BigDecimal getOperateFeeRate() {
		return operateFeeRate;
	}

	public void setOperateFeeRate(BigDecimal operateFeeRate) {
		this.operateFeeRate = operateFeeRate;
	}

	public Integer getPayCurrency() {
		return payCurrency;
	}

	public void setPayCurrency(Integer payCurrency) {
		this.payCurrency = payCurrency;
	}

	public Integer getReceiveCurrency() {
		return receiveCurrency;
	}

	public void setReceiveCurrency(Integer receiveCurrency) {
		this.receiveCurrency = receiveCurrency;
	}

	public BigDecimal getPayRate() {
		return payRate;
	}

	public void setPayRate(BigDecimal payRate) {
		this.payRate = payRate;
	}

	public Integer getIsAgencyExport() {
		return isAgencyExport;
	}

	public void setIsAgencyExport(Integer isAgencyExport) {
		this.isAgencyExport = isAgencyExport;
	}

	public BigDecimal getAgencyExportRate() {
		return agencyExportRate;
	}

	public void setAgencyExportRate(BigDecimal agencyExportRate) {
		this.agencyExportRate = agencyExportRate;
	}

	public Integer getAccountRateType() {
		return accountRateType;
	}

	public void setAccountRateType(Integer accountRateType) {
		this.accountRateType = accountRateType;
	}

	public BigDecimal getAccountRate() {
		return accountRate;
	}

	public void setAccountRate(BigDecimal accountRate) {
		this.accountRate = accountRate;
	}

	public String getPaypalAccount() {
		return paypalAccount;
	}

	public void setPaypalAccount(String paypalAccount) {
		this.paypalAccount = paypalAccount == null ? null : paypalAccount.trim();
	}

	public Integer getPaypalAccountType() {
		return paypalAccountType;
	}

	public void setPaypalAccountType(Integer paypalAccountType) {
		this.paypalAccountType = paypalAccountType;
	}

	public Integer getPaypalDays() {
		return paypalDays;
	}

	public void setPaypalDays(Integer paypalDays) {
		this.paypalDays = paypalDays;
	}

	public Integer getPaypalFixdayType() {
		return paypalFixdayType;
	}

	public void setPaypalFixdayType(Integer paypalFixdayType) {
		this.paypalFixdayType = paypalFixdayType;
	}

	public Integer getPaypalFixday() {
		return paypalFixday;
	}

	public void setPaypalFixday(Integer paypalFixday) {
		this.paypalFixday = paypalFixday;
	}

	public BigDecimal getPaypalAmount() {
		return paypalAmount;
	}

	public void setPaypalAmount(BigDecimal paypalAmount) {
		this.paypalAmount = paypalAmount;
	}

	public Integer getPaypalAmountCurrency() {
		return paypalAmountCurrency;
	}

	public void setPaypalAmountCurrency(Integer paypalAmountCurrency) {
		this.paypalAmountCurrency = paypalAmountCurrency;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address == null ? null : address.trim();
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts == null ? null : contacts.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone == null ? null : phone.trim();
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax == null ? null : fax.trim();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.trim();
	}

	public String getClientAddress() {
		return clientAddress;
	}

	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress == null ? null : clientAddress.trim();
	}

	public String getClientContacts() {
		return clientContacts;
	}

	public void setClientContacts(String clientContacts) {
		this.clientContacts = clientContacts == null ? null : clientContacts.trim();
	}

	public String getClientPhone() {
		return clientPhone;
	}

	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone == null ? null : clientPhone.trim();
	}

	public String getClientFax() {
		return clientFax;
	}

	public void setClientFax(String clientFax) {
		this.clientFax = clientFax == null ? null : clientFax.trim();
	}

	public String getClientEmail() {
		return clientEmail;
	}

	public void setClientEmail(String clientEmail) {
		this.clientEmail = clientEmail == null ? null : clientEmail.trim();
	}

	public String getSupplierAddress() {
		return supplierAddress;
	}

	public void setSupplierAddress(String supplierAddress) {
		this.supplierAddress = supplierAddress == null ? null : supplierAddress.trim();
	}

	public String getSupplierContacts() {
		return supplierContacts;
	}

	public void setSupplierContacts(String supplierContacts) {
		this.supplierContacts = supplierContacts == null ? null : supplierContacts.trim();
	}

	public String getSupplierPhone() {
		return supplierPhone;
	}

	public void setSupplierPhone(String supplierPhone) {
		this.supplierPhone = supplierPhone == null ? null : supplierPhone.trim();
	}

	public String getSupplierFax() {
		return supplierFax;
	}

	public void setSupplierFax(String supplierFax) {
		this.supplierFax = supplierFax == null ? null : supplierFax.trim();
	}

	public String getSupplierEmail() {
		return supplierEmail;
	}

	public void setSupplierEmail(String supplierEmail) {
		this.supplierEmail = supplierEmail == null ? null : supplierEmail.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator == null ? null : creator.trim();
	}

	public String getDeleter() {
		return deleter;
	}

	public void setDeleter(String deleter) {
		this.deleter = deleter == null ? null : deleter.trim();
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getDeleteAt() {
		return deleteAt;
	}

	public void setDeleteAt(Date deleteAt) {
		this.deleteAt = deleteAt;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public BigDecimal getOperaterate() {
		return operaterate;
	}

	public void setOperaterate(BigDecimal operaterate) {
		this.operaterate = operaterate;
	}

	public Integer getClientCheckDay() {
		return clientCheckDay;
	}

	public void setClientCheckDay(Integer clientCheckDay) {
		this.clientCheckDay = clientCheckDay;
	}

	public Integer getSupplierCheckDay() {
		return supplierCheckDay;
	}

	public void setSupplierCheckDay(Integer supplierCheckDay) {
		this.supplierCheckDay = supplierCheckDay;
	}

	public Integer getProjectcheckdate() {
		return projectcheckdate;
	}

	public void setProjectcheckdate(Integer projectcheckdate) {
		this.projectcheckdate = projectcheckdate;
	}

	public String getAmountCurrencyValue() {
		return amountCurrencyValue;
	}

	public void setAmountCurrencyValue(String amountCurrencyValue) {
		this.amountCurrencyValue = amountCurrencyValue;
	}

	public String getFundAccountPeriodValue() {
		return fundAccountPeriodValue;
	}

	public void setFundAccountPeriodValue(String fundAccountPeriodValue) {
		this.fundAccountPeriodValue = fundAccountPeriodValue;
	}

	public String getIsOperateAccountValue() {
		return isOperateAccountValue;
	}

	public void setIsOperateAccountValue(String isOperateAccountValue) {
		this.isOperateAccountValue = isOperateAccountValue;
	}

	public String getBusinesstypeValue() {
		return businesstypeValue;
	}

	public void setBusinesstypeValue(String businesstypeValue) {
		this.businesstypeValue = businesstypeValue;
	}

	public BigDecimal getAgencyimportrate() {
		return agencyimportrate;
	}

	public void setAgencyimportrate(BigDecimal agencyimportrate) {
		this.agencyimportrate = agencyimportrate;
	}

	public Integer getIsagencyimport() {
		return isagencyimport;
	}

	public void setIsagencyimport(Integer isagencyimport) {
		this.isagencyimport = isagencyimport;
	}

	public Integer getFile() {
		return file;
	}

	public void setFile(Integer file) {
		this.file = file;
	}

	public Integer getFixedpoints() {
		return fixedpoints;
	}

	public void setFixedpoints(Integer fixedpoints) {
		this.fixedpoints = fixedpoints;
	}

	public BigDecimal getSpreadfixedpoints() {
		return spreadfixedpoints;
	}

	public void setSpreadfixedpoints(BigDecimal spreadfixedpoints) {
		this.spreadfixedpoints = spreadfixedpoints;
	}

	public String getAccountMethodValue() {
		return accountMethodValue;
	}

	public void setAccountMethodValue(String accountMethodValue) {
		this.accountMethodValue = accountMethodValue;
	}

	public String getSignStandardValue() {
		return signStandardValue;
	}

	public void setSignStandardValue(String signStandardValue) {
		this.signStandardValue = signStandardValue;
	}

	public BigDecimal getSingleAmount() {
		return singleAmount;
	}

	public void setSingleAmount(BigDecimal singleAmount) {
		this.singleAmount = singleAmount;
	}

	public Integer getLibraryAge() {
		return libraryAge;
	}

	public void setLibraryAge(Integer libraryAge) {
		this.libraryAge = libraryAge;
	}

	public String getDayRulesValue() {
		return dayRulesValue;
	}

	public void setDayRulesValue(String dayRulesValue) {
		this.dayRulesValue = dayRulesValue;
	}

	public Integer getSettleType() {
		return settleType;
	}

	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}

	public String getSettleTypeValue() {
		return settleTypeValue;
	}

	public void setSettleTypeValue(String settleTypeValue) {
		this.settleTypeValue = settleTypeValue;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getPrimaryCustomerName() {
		return primaryCustomerName;
	}

	public void setPrimaryCustomerName(String primaryCustomerName) {
		this.primaryCustomerName = primaryCustomerName;
	}

	public Integer getPaypalCalcType() {
		return paypalCalcType;
	}

	public void setPaypalCalcType(Integer paypalCalcType) {
		this.paypalCalcType = paypalCalcType;
	}

	public String getPaypalCalcTypeName() {
		return paypalCalcTypeName;
	}

	public void setPaypalCalcTypeName(String paypalCalcTypeName) {
		this.paypalCalcTypeName = paypalCalcTypeName;
	}

	public List<ProjectItemSegment> getProjectItemSegmentList() {
		return projectItemSegmentList;
	}

	public void setProjectItemSegmentList(List<ProjectItemSegment> projectItemSegmentList) {
		this.projectItemSegmentList = projectItemSegmentList;
	}

	public Integer getClientCheckWeek() {
		return clientCheckWeek;
	}

	public void setClientCheckWeek(Integer clientCheckWeek) {
		this.clientCheckWeek = clientCheckWeek;
	}

	public Integer getSupplierCheckWeek() {
		return supplierCheckWeek;
	}

	public void setSupplierCheckWeek(Integer supplierCheckWeek) {
		this.supplierCheckWeek = supplierCheckWeek;
	}

	public Integer getProjectCheckWeek() {
		return projectCheckWeek;
	}

	public void setProjectCheckWeek(Integer projectCheckWeek) {
		this.projectCheckWeek = projectCheckWeek;
	}

	public String getClientCheckWeekName() {
		return clientCheckWeekName;
	}

	public void setClientCheckWeekName(String clientCheckWeekName) {
		this.clientCheckWeekName = clientCheckWeekName;
	}

	public String getSupplierCheckWeekName() {
		return supplierCheckWeekName;
	}

	public void setSupplierCheckWeekName(String supplierCheckWeekName) {
		this.supplierCheckWeekName = supplierCheckWeekName;
	}

	public String getProjectCheckWeekName() {
		return projectCheckWeekName;
	}

	public void setProjectCheckWeekName(String projectCheckWeekName) {
		this.projectCheckWeekName = projectCheckWeekName;
	}

	public String getPayAuditType() {
		return payAuditType;
	}

	public void setPayAuditType(String payAuditType) {
		this.payAuditType = payAuditType;
	}

	public String getPayAuditTypeName() {
		return payAuditTypeName;
	}

	public void setPayAuditTypeName(String payAuditTypeName) {
		this.payAuditTypeName = payAuditTypeName;
	}

	public Integer getPayCycle() {
		return payCycle;
	}

	public void setPayCycle(Integer payCycle) {
		this.payCycle = payCycle;
	}

}