package com.scfs.web.base.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.domain.base.dto.req.BaseExchangeRateReqDto;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.web.base.BaseJUnitTest;

import java.util.Date;

public class BaseExchangeRateServiceImplTest extends BaseJUnitTest {
	
	@Autowired
	private BaseExchangeRateService baseExchangeRateService;
	
	@Test
    public void testQuery(){
//		BaseExchangeRateReqDto baseExchangeRateReqDto = new BaseExchangeRateReqDto();
//		LOGGER.info(JSONObject.toJSON( baseExchangeRateService.getBaseExchangeRateList(baseExchangeRateReqDto) ) + "");
		LOGGER.info(JSONObject.toJSON( baseExchangeRateService.convertCurrency("1","1","2",new Date()) ) + "");
	}

	@Test
	public void testQueryHistory(){
		LOGGER.info(JSONObject.toJSON( baseExchangeRateService.getBaseExchangeRateHisList(new BaseExchangeRateReqDto()) ) + "");
	}

}
