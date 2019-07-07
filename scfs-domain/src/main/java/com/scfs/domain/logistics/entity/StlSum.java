package com.scfs.domain.logistics.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Administrator on 2016年11月14日.
 */
public class StlSum implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1118538320835208636L;
	/**
	 * 库存数量
	 */
	private BigDecimal storeNum;
	/**
	 * 出库锁定数量
	 */
	private BigDecimal lockNum;
	/**
	 * 销售锁定数量
	 */
	private BigDecimal saleLockNum;
	/**
	 * 可用数量
	 */
	private BigDecimal availableNum;

	/**
	 * 总成本单价
	 */
	private BigDecimal totalCostPrice;
	/**
	 * 总订单单价
	 */
	private BigDecimal totalPoPrice;
	/**
	 * 总理货数量
	 */
	private BigDecimal totalTallyNum;
	/**
	 * 总库存数量
	 */
	private BigDecimal totalStoreNum;
	/**
	 * 总出库锁定数量
	 */
	private BigDecimal totalLockNum;
	/**
	 * 总销售锁定数量
	 */
	private BigDecimal totalSaleLockNum;
	/**
	 * 总可用数量
	 */
	private BigDecimal totalAvailableNum;
	/**
	 * 总库存金额
	 */
	private BigDecimal totalStoreAmount;
	/**
	 * 总出库锁定金额
	 */
	private BigDecimal totalLockAmount;
	/**
	 * 总销售锁定金额
	 */
	private BigDecimal totalSaleLockAmount;
	/**
	 * 总可用金额
	 */
	private BigDecimal totalAvailableAmount;
	/**
	 * 币种
	 */
	private Integer currencyType;

	public BigDecimal getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(BigDecimal storeNum) {
		this.storeNum = storeNum;
	}

	public BigDecimal getLockNum() {
		return lockNum;
	}

	public void setLockNum(BigDecimal lockNum) {
		this.lockNum = lockNum;
	}

	public BigDecimal getTotalCostPrice() {
		return totalCostPrice;
	}

	public void setTotalCostPrice(BigDecimal totalCostPrice) {
		this.totalCostPrice = totalCostPrice;
	}

	public BigDecimal getTotalPoPrice() {
		return totalPoPrice;
	}

	public void setTotalPoPrice(BigDecimal totalPoPrice) {
		this.totalPoPrice = totalPoPrice;
	}

	public BigDecimal getTotalTallyNum() {
		return totalTallyNum;
	}

	public void setTotalTallyNum(BigDecimal totalTallyNum) {
		this.totalTallyNum = totalTallyNum;
	}

	public BigDecimal getTotalStoreNum() {
		return totalStoreNum;
	}

	public void setTotalStoreNum(BigDecimal totalStoreNum) {
		this.totalStoreNum = totalStoreNum;
	}

	public BigDecimal getTotalLockNum() {
		return totalLockNum;
	}

	public void setTotalLockNum(BigDecimal totalLockNum) {
		this.totalLockNum = totalLockNum;
	}

	public BigDecimal getTotalAvailableNum() {
		return totalAvailableNum;
	}

	public void setTotalAvailableNum(BigDecimal totalAvailableNum) {
		this.totalAvailableNum = totalAvailableNum;
	}

	public BigDecimal getTotalStoreAmount() {
		return totalStoreAmount;
	}

	public void setTotalStoreAmount(BigDecimal totalStoreAmount) {
		this.totalStoreAmount = totalStoreAmount;
	}

	public BigDecimal getTotalLockAmount() {
		return totalLockAmount;
	}

	public void setTotalLockAmount(BigDecimal totalLockAmount) {
		this.totalLockAmount = totalLockAmount;
	}

	public BigDecimal getTotalAvailableAmount() {
		return totalAvailableAmount;
	}

	public void setTotalAvailableAmount(BigDecimal totalAvailableAmount) {
		this.totalAvailableAmount = totalAvailableAmount;
	}

	public Integer getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(Integer currencyType) {
		this.currencyType = currencyType;
	}

	public BigDecimal getAvailableNum() {
		return availableNum;
	}

	public void setAvailableNum(BigDecimal availableNum) {
		this.availableNum = availableNum;
	}

	public BigDecimal getSaleLockNum() {
		return saleLockNum;
	}

	public void setSaleLockNum(BigDecimal saleLockNum) {
		this.saleLockNum = saleLockNum;
	}

	public BigDecimal getTotalSaleLockNum() {
		return totalSaleLockNum;
	}

	public void setTotalSaleLockNum(BigDecimal totalSaleLockNum) {
		this.totalSaleLockNum = totalSaleLockNum;
	}

	public BigDecimal getTotalSaleLockAmount() {
		return totalSaleLockAmount;
	}

	public void setTotalSaleLockAmount(BigDecimal totalSaleLockAmount) {
		this.totalSaleLockAmount = totalSaleLockAmount;
	}

}
