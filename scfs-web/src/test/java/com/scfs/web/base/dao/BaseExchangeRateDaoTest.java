package com.scfs.web.base.dao;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.scfs.domain.base.dto.req.BaseExchangeRateReqDto;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.dao.base.entity.BaseExchangeRateDao;
import com.scfs.domain.base.entity.BaseExchangeRate;
import com.scfs.web.base.BaseJUnitTest;

public class BaseExchangeRateDaoTest extends BaseJUnitTest {
	
	@Autowired
    private BaseExchangeRateDao baseExchangeRateDao;
	
	@Test
    public void testInsert(){
		BaseExchangeRate baseExchangeRate = new BaseExchangeRate();
		baseExchangeRate.setBank("汇丰银行");
		baseExchangeRate.setCurrency("港币");
		baseExchangeRate.setForeignCurrency("美元");

		try {
			DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			baseExchangeRate.setPublishAt(df.parse("2016年9月29日 10:58"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		baseExchangeRate.setCreator("系统");
		baseExchangeRate.setCreateAt(new Date());
		baseExchangeRateDao.insert(baseExchangeRate);
	}
	
	@Test
    public void testUpdate(){
		BaseExchangeRate baseExchangeRate = new BaseExchangeRate();
		baseExchangeRate.setId(139);
		baseExchangeRateDao.updateById(baseExchangeRate);
	}
	
	@Test
    public void testQuery(){
		BaseExchangeRateReqDto baseExchangeRate = new BaseExchangeRateReqDto();
		baseExchangeRate.setBank("汇丰银行");
		baseExchangeRate.setCurrency("港币");
		baseExchangeRate.setForeignCurrency("美元");
		logger.info(JSONObject.toJSON( baseExchangeRateDao.getHisExchangeRateList(baseExchangeRate, new RowBounds(0,15)) ));
	}

}
