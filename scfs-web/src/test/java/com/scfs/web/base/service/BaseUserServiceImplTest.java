package com.scfs.web.base.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.domain.base.dto.req.BaseUserReqDto;
import com.scfs.service.base.user.BaseUserService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年9月28日.
 */
public class BaseUserServiceImplTest extends BaseJUnitTest{
    @Autowired
    private BaseUserService baseUserService;

    @Test
    public void testQuery(){
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setChineseName("李四");
        LOGGER.info(JSONObject.toJSON( baseUserService.queryBaseUserList(baseUserReqDto))+"");
    }
    
    @Test
    public void testQueryById(){
        BaseUserReqDto baseUserReqDto = new BaseUserReqDto();
        baseUserReqDto.setId(3);
        Object obj = baseUserService.queryBaseUser(baseUserReqDto);
        LOGGER.info(JSONObject.toJSON(obj)+"");
    }
}

