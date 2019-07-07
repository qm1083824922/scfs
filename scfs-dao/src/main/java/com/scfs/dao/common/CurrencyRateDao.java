package com.scfs.dao.common;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.scfs.domain.common.entity.CurrencyRate;

public interface CurrencyRateDao {
	CurrencyRate queryEntityById(Integer id);

	int queryCountByTheMonthCd(String theMonthCd);

	CurrencyRate queryByTheMonthCd(@Param("currency") String currency, @Param("theMonthCd") String theMonthCd);

	BigDecimal queryRateByTheMonthCd(@Param("originCurrency") String originCurrency,
			@Param("targetCurrencyType") Integer targetCurrencyType, @Param("theMonthCd") String theMonthCd);
}