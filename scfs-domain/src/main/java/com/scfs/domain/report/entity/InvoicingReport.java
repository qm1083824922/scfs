package com.scfs.domain.report.entity;

import java.math.BigDecimal;

public class InvoicingReport {
	/** 经营单位 */
	private String busiUnitName;
	private Integer busiUnit;
	/** 项目ID */
	private String projectName;
	private Integer projectId;
	/** 部门名称 */
	private String departName;
	private Integer departmentId;
	/** 业务员 */
	private String bussinessUserName;
	private Integer userId;

	/** 仓库 */
	private String wareHouseName;
	private Integer wareHouseId;
	/** 币种 */
	private String currencyTypeName;
	private Integer currencyType;
	/** 客户 */
	private String customerName;
	private Integer customerId;
	/** 商品编号 */
	private String number;
	private Integer goodsId;
	/** 商品描述 */
	private String name;
	private BigDecimal price;
	/** 期初数量 */
	private BigDecimal startNumber;
	/** 期末数量 */
	private BigDecimal endNumber;
	/** 本期入库数量 */
	private BigDecimal currInNumber;
	/** 本期出库数量 */
	private BigDecimal currOutNumber;
	/** 期初金额 */
	private BigDecimal startAmount;
	/** 期初未税金额 */
	private BigDecimal startExRateAmount;
	/** 期末金额 */
	private BigDecimal endAmount;
	/** 期末未税金额 */
	private BigDecimal endExRateAmount;
	/** 本期入库金额 */
	private BigDecimal currInAmount;
	/** 本期入库未税金额 */
	private BigDecimal currInExRateAmount;
	/** 本期出库金额 */
	private BigDecimal currOutAmount;
	/** 本期出库未税金额 */
	private BigDecimal currOutExRateAmount;
	/** 日期开始时间 */
	private String startBusinessDate;
	/** 日期结束时间 */
	private String endBusinessDate;

	public String getBusiUnitName() {
		return busiUnitName;
	}

	public void setBusiUnitName(String busiUnitName) {
		this.busiUnitName = busiUnitName;
	}

	public Integer getBusiUnit() {
		return busiUnit;
	}

	public void setBusiUnit(Integer busiUnit) {
		this.busiUnit = busiUnit;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public String getDepartName() {
		return departName;
	}

	public void setDepartName(String departName) {
		this.departName = departName;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getBussinessUserName() {
		return bussinessUserName;
	}

	public void setBussinessUserName(String bussinessUserName) {
		this.bussinessUserName = bussinessUserName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCurrencyTypeName() {
		return currencyTypeName;
	}

	public void setCurrencyTypeName(String currencyTypeName) {
		this.currencyTypeName = currencyTypeName;
	}

	public String getWareHouseName() {
		return wareHouseName;
	}

	public void setWareHouseName(String wareHouseName) {
		this.wareHouseName = wareHouseName;
	}

	public Integer getWareHouseId() {
		return wareHouseId;
	}

	public void setWareHouseId(Integer wareHouseId) {
		this.wareHouseId = wareHouseId;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(BigDecimal startNumber) {
		this.startNumber = startNumber;
	}

	public BigDecimal getEndNumber() {
		return endNumber;
	}

	public void setEndNumber(BigDecimal endNumber) {
		this.endNumber = endNumber;
	}

	public BigDecimal getCurrInNumber() {
		return currInNumber;
	}

	public void setCurrInNumber(BigDecimal currInNumber) {
		this.currInNumber = currInNumber;
	}

	public BigDecimal getCurrOutNumber() {
		return currOutNumber;
	}

	public void setCurrOutNumber(BigDecimal currOutNumber) {
		this.currOutNumber = currOutNumber;
	}

	public BigDecimal getStartAmount() {
		return startAmount;
	}

	public void setStartAmount(BigDecimal startAmount) {
		this.startAmount = startAmount;
	}

	public BigDecimal getStartExRateAmount() {
		return startExRateAmount;
	}

	public void setStartExRateAmount(BigDecimal startExRateAmount) {
		this.startExRateAmount = startExRateAmount;
	}

	public BigDecimal getEndAmount() {
		return endAmount;
	}

	public void setEndAmount(BigDecimal endAmount) {
		this.endAmount = endAmount;
	}

	public BigDecimal getEndExRateAmount() {
		return endExRateAmount;
	}

	public void setEndExRateAmount(BigDecimal endExRateAmount) {
		this.endExRateAmount = endExRateAmount;
	}

	public BigDecimal getCurrInAmount() {
		return currInAmount;
	}

	public void setCurrInAmount(BigDecimal currInAmount) {
		this.currInAmount = currInAmount;
	}

	public BigDecimal getCurrInExRateAmount() {
		return currInExRateAmount;
	}

	public void setCurrInExRateAmount(BigDecimal currInExRateAmount) {
		this.currInExRateAmount = currInExRateAmount;
	}

	public BigDecimal getCurrOutAmount() {
		return currOutAmount;
	}

	public void setCurrOutAmount(BigDecimal currOutAmount) {
		this.currOutAmount = currOutAmount;
	}

	public BigDecimal getCurrOutExRateAmount() {
		return currOutExRateAmount;
	}

	public void setCurrOutExRateAmount(BigDecimal currOutExRateAmount) {
		this.currOutExRateAmount = currOutExRateAmount;
	}

	public String getStartBusinessDate() {
		return startBusinessDate;
	}

	public void setStartBusinessDate(String startBusinessDate) {
		this.startBusinessDate = startBusinessDate;
	}

	public String getEndBusinessDate() {
		return endBusinessDate;
	}

	public void setEndBusinessDate(String endBusinessDate) {
		this.endBusinessDate = endBusinessDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

}
