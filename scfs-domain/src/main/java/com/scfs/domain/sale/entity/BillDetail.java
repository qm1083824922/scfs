package com.scfs.domain.sale.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BillDetail implements Serializable {

	/**
	* 
	*/
	private static final long serialVersionUID = 6422490471865877797L;
	/**
	 * 销售单id
	 */
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 销售单编号
	 */
	private String billNo;
	/**
	 * 创建时间
	 */
	private Date deliveryDate;

	/**
	 * 商品编号
	 */
	private String number;
	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 开票数量
	 */
	private BigDecimal provideInvoiceNum;

	/**
	 * 开票金额
	 */
	private BigDecimal provideInvoiceAmount;

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getProvideInvoiceNum() {
		return provideInvoiceNum;
	}

	public void setProvideInvoiceNum(BigDecimal provideInvoiceNum) {
		this.provideInvoiceNum = provideInvoiceNum;
	}

	public BigDecimal getProvideInvoiceAmount() {
		return provideInvoiceAmount;
	}

	public void setProvideInvoiceAmount(BigDecimal provideInvoiceAmount) {
		this.provideInvoiceAmount = provideInvoiceAmount;
	}

}
