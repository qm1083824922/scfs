package com.scfs.web.base.service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.domain.base.dto.req.BasePermissionReqDto;
import com.scfs.domain.base.entity.BasePermission;
import com.scfs.service.base.shiro.BasePermissionService;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/9/26.
 */
public class BasePermissionServiceImplTest extends BaseJUnitTest {
    @Autowired
    private BasePermissionService basePermissionService;

    @Test
    public void testQuery(){
        BasePermissionReqDto basePermissionReqDto  = new BasePermissionReqDto();
//        basePermissionReqDto.setMenuLevel(1);
        basePermissionReqDto.setPer_page(1);
        LOGGER.info(JSONObject.toJSON( basePermissionService.queryPermissions(basePermissionReqDto))+"");


        LOGGER.info(JSONObject.toJSON( basePermissionService.queryAllPermission(basePermissionReqDto))+"========");

    }

    @Test
    public void testQueryDividPermissionListByGroupId(){
        BasePermissionReqDto basePermissionReqDto = new BasePermissionReqDto();
        basePermissionReqDto.setPermissionGroupId(14324);
//        LOGGER.info(JSONObject.toJSONString(basePermissionService.queryDividPermissionListByGroupId(basePermissionReqDto)));
        LOGGER.info(JSONObject.toJSONString(basePermissionService.queryUnDividPermissionListByGroupId(basePermissionReqDto)));
    }

    @Test
    public void testInsert(){
        basePermissionService.addPermission(new BasePermission());
    }
}
