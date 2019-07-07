package com.scfs.domain.fi.dto.resp;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017年5月31日.
 */
public class StandardCoinResDto {
	
	private BigDecimal standardDebitAmount;
	
	private BigDecimal standardCreditAmount;
	
	private Integer standardCoin;
	
	private String standardCoinName;
	
	private BigDecimal standardRate;

	public BigDecimal getStandardDebitAmount() {
		return standardDebitAmount;
	}

	public void setStandardDebitAmount(BigDecimal standardDebitAmount) {
		this.standardDebitAmount = standardDebitAmount;
	}

	public BigDecimal getStandardCreditAmount() {
		return standardCreditAmount;
	}

	public void setStandardCreditAmount(BigDecimal standardCreditAmount) {
		this.standardCreditAmount = standardCreditAmount;
	}

	public Integer getStandardCoin() {
		return standardCoin;
	}

	public void setStandardCoin(Integer standardCoin) {
		this.standardCoin = standardCoin;
	}

	public String getStandardCoinName() {
		return standardCoinName;
	}

	public void setStandardCoinName(String standardCoinName) {
		this.standardCoinName = standardCoinName;
	}

	public BigDecimal getStandardRate() {
		return standardRate;
	}

	public void setStandardRate(BigDecimal standardRate) {
		this.standardRate = standardRate;
	}
	
	

}

