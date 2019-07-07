package com.scfs.web.base.service;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.collections.Lists;

import com.alibaba.fastjson.JSONObject;
import com.scfs.domain.base.dto.req.BaseRoleReqDto;
import com.scfs.service.base.shiro.BaseRoleService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016/9/30.
 */
public class BaseRoleServiceImplTest extends BaseJUnitTest {

    @Autowired
    private BaseRoleService baseRoleService;

    @Test
    public void testQuery(){
        LOGGER.info(JSONObject.toJSONString(baseRoleService.queryBaseRoleById(137)));
    }

    @Test
    public void testInvild(){
        BaseRoleReqDto baseRoleReqDto = new BaseRoleReqDto();
        baseRoleReqDto.setRoleId(136);
        List<Integer> ids =Lists.newArrayList();
        ids.add(14324);
        ids.add(123123);
        baseRoleReqDto.setIds(ids);
        baseRoleService.invalidPermissionGroupRelation( baseRoleReqDto);
    }
}
