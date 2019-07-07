package com.scfs.web.redis;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.service.support.CacheService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016/10/21.
 */
public class RedisClientTest extends BaseJUnitTest{
    @Autowired
    private CacheService cacheService;

    @Test
    public void testInitData(){
        LOGGER.info(JSONObject.toJSONString(cacheService.queryMenuPermissions())+"-----------");
    }

    @Test
    public void  testClient(){
        LOGGER.info("+++++++++++++" +cacheService.incrLoginErrorCount("tang"));
    }

    @Test
    public void testCacheName(){
        LOGGER.info(JSONObject.toJSONString( cacheService.queryMenuPermissions())+"-----------");
    }

    @Test
    public void testProject(){
        LOGGER.info(JSONObject.toJSONString( cacheService.getProjectById(1))+"-----------");
    }

    @Test
    public void testGoods(){
        LOGGER.info(JSONObject.toJSONString( cacheService.getGoodsById(1))+"-----------");
    }

    @Test
    public void testUser(){
        LOGGER.info(JSONObject.toJSONString( cacheService.getUserByid(1))+"-----------");
    }
}
