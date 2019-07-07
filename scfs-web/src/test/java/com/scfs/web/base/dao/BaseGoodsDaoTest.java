package com.scfs.web.base.dao;

import java.util.Date;

import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.web.base.BaseJUnitTest;

public class BaseGoodsDaoTest extends BaseJUnitTest {
	
	@Autowired
    private BaseGoodsDao baseGoodsDao;
	
	@Test
    public void testInsert(){
		BaseGoods baseGoods = new BaseGoods();
		baseGoods.setNumber("xx-001");
		baseGoods.setName("小米5");
		baseGoods.setType("手机");
		baseGoods.setBarCode("693718643530");
		baseGoods.setSpecification("3Gx64G");
		baseGoods.setTaxClassification("营业税");
		baseGoods.setUnit("台");
		baseGoods.setStatus(1);
		baseGoods.setCreator("张三");
		baseGoods.setCreateAt(new Date());
		baseGoodsDao.insert(baseGoods);
	}
	
	@Test
    public void testQuery(){
		BaseGoods baseGoods = new BaseGoods();
		baseGoods.setNumber("xx-001");
		logger.info(JSONObject.toJSON( baseGoodsDao.getGoodsList(baseGoods, new RowBounds(0,15)) ));
	}

}
