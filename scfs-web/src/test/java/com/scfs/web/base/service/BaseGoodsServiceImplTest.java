package com.scfs.web.base.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.domain.base.dto.req.BaseGoodsReqDto;
import com.scfs.service.base.goods.BaseGoodsService;
import com.scfs.web.base.BaseJUnitTest;

public class BaseGoodsServiceImplTest extends BaseJUnitTest {

	@Autowired
	private BaseGoodsService baseGoodsService;
	
	@Test
    public void testQuery(){
		BaseGoodsReqDto baseGoodsReqDto = new BaseGoodsReqDto();
		LOGGER.info(JSONObject.toJSON( baseGoodsService.getBaseGoodsList(baseGoodsReqDto) ) + "");
	}

}
