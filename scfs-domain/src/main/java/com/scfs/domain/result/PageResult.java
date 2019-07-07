package com.scfs.domain.result;

import com.scfs.domain.BaseResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2016/9/28.
 */
public class PageResult<T> extends BaseResult {

	private List<T> items;
	/** 总条数 */
	private int total;
	/** 当前页 */
	private int current_page;
	private int per_page;
	/** 总页数 */
	private int last_page;

	private BigDecimal totalAmount;
	private BigDecimal totalNum;

	private BigDecimal feeTotalAmount;
	private BigDecimal exRateTotalAmount;
	private BigDecimal rateTotalAmount;

	private String currency = "CNY";
	private String totalStr;
	/** 合计行 */
	private T footer;

	public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getCurrent_page() {
		return current_page;
	}

	public void setCurrent_page(int current_page) {
		this.current_page = current_page;
	}

	public int getPer_page() {
		return per_page;
	}

	public void setPer_page(int per_page) {
		this.per_page = per_page;
	}

	public int getLast_page() {
		return last_page;
	}

	public void setLast_page(int last_page) {
		this.last_page = last_page;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(BigDecimal totalNum) {
		this.totalNum = totalNum;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getTotalStr() {
		return totalStr;
	}

	public void setTotalStr(String totalStr) {
		this.totalStr = totalStr;
	}

	public T getFooter() {
		return footer;
	}

	public void setFooter(T footer) {
		this.footer = footer;
	}

	public BigDecimal getFeeTotalAmount() {
		return feeTotalAmount;
	}

	public void setFeeTotalAmount(BigDecimal feeTotalAmount) {
		this.feeTotalAmount = feeTotalAmount;
	}

	public BigDecimal getExRateTotalAmount() {
		return exRateTotalAmount;
	}

	public void setExRateTotalAmount(BigDecimal exRateTotalAmount) {
		this.exRateTotalAmount = exRateTotalAmount;
	}

	public BigDecimal getRateTotalAmount() {
		return rateTotalAmount;
	}

	public void setRateTotalAmount(BigDecimal rateTotalAmount) {
		this.rateTotalAmount = rateTotalAmount;
	}

}
