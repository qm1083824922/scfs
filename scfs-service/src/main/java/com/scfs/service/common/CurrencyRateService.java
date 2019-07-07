package com.scfs.service.common;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.common.CurrencyRateDao;
import com.scfs.domain.common.entity.CurrencyRate;

/**
 * Created by Administrator on 2017年5月27日.
 */
@Service
public class CurrencyRateService {
	@Autowired
	private CurrencyRateDao currencyRateDao;

	/**
	public CurrencyRate queryEntityById(Integer id) {
		return currencyRateDao.queryEntityById(id);
	}

	public int queryCountByTheMonthCd(String theMonthCd) {
		return currencyRateDao.queryCountByTheMonthCd(theMonthCd);
	}

	public CurrencyRate queryByTheMonthCd(String currency, String theMonthCd) {
		return currencyRateDao.queryByTheMonthCd(currency, theMonthCd);
	}

	public BigDecimal queryRateByTheMonthCd(Integer originCurrencyType, Integer targetCurrencyType, String theMonthCd) {
		if ((originCurrencyType != BaseConsts.ONE && originCurrencyType != BaseConsts.TWO
				&& originCurrencyType != BaseConsts.THREE)
				|| (targetCurrencyType != BaseConsts.ONE && targetCurrencyType != BaseConsts.TWO
						&& targetCurrencyType != BaseConsts.THREE)) {
			CurrencyRate currencyRate1 = currencyRateDao
					.queryByTheMonthCd(BaseConsts.CURRENCY_UNIT_MAP.get(originCurrencyType), theMonthCd);
			BigDecimal cnyRate1 = currencyRate1.getCnyRate();
			CurrencyRate currencyRate2 = currencyRateDao
					.queryByTheMonthCd(BaseConsts.CURRENCY_UNIT_MAP.get(targetCurrencyType), theMonthCd);
			BigDecimal cnyRate2 = currencyRate2.getCnyRate();
			return DecimalUtil.divide(cnyRate1, cnyRate2);
		} else {
			return currencyRateDao.queryRateByTheMonthCd(BaseConsts.CURRENCY_UNIT_MAP.get(originCurrencyType),
					targetCurrencyType, theMonthCd);
		}
	}
	**/
}
