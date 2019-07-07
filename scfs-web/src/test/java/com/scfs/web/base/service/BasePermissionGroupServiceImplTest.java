package com.scfs.web.base.service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.domain.base.dto.req.BasePermissionGroupReqDto;
import com.scfs.domain.base.dto.resp.BasePermissionGroupResDto;
import com.scfs.domain.base.entity.BasePermissionGroup;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.shiro.BasePermissionGroupService;
import com.scfs.web.base.BaseJUnitTest;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class BasePermissionGroupServiceImplTest extends BaseJUnitTest {
    @Autowired
    private BasePermissionGroupService basePermissionGroupService;

    @Test
    public void testInsert(){
        BasePermissionGroup basePermissionGroup = new BasePermissionGroup();
        basePermissionGroup.setName("abc");
        basePermissionGroup.setCreator("张三");
        basePermissionGroupService.addPermissionGroup(basePermissionGroup);
    }

    @Test
    public void testUpdate(){
        BasePermissionGroup basePermissionGroup = new BasePermissionGroup();
        basePermissionGroup.setId(14323);
        basePermissionGroup.setName("哈哈");
        basePermissionGroup.setCreator("王武");
        basePermissionGroupService.updatePermissionGroup(basePermissionGroup);
    }

    @Test
    public void testUpdateRelation(){
        BasePermissionGroupReqDto basePermissionGroupReqDto = new BasePermissionGroupReqDto();
        List<Integer> permissionIds = Lists.newArrayList();
        permissionIds.add(747);
        permissionIds.add(780);
        basePermissionGroupReqDto.setPermissionGroupId(14324);
        basePermissionGroupReqDto.setPermissionIds(permissionIds);
        basePermissionGroupService.invalidPermissionRelation(basePermissionGroupReqDto);
    }

    @Test
    public void testQuery(){
        BasePermissionGroupReqDto basePermissionGroupReqDto = new BasePermissionGroupReqDto();
        PageResult<BasePermissionGroupResDto> result = basePermissionGroupService.queryPermissionGroups(basePermissionGroupReqDto);
        logger.info("======"+JSONObject.toJSON(result));
    }

    @Test
    public void testQueryDividPermissionGroupList(){
        BasePermissionGroupReqDto basePermissionGroupReqDto = new BasePermissionGroupReqDto();
        basePermissionGroupReqDto.setRoleId(136);
//        PageResult<BasePermissionGroupResDto> result =  basePermissionGroupService.queryDividPermissionGroupList(basePermissionGroupReqDto);
        PageResult<BasePermissionGroupResDto> result =  basePermissionGroupService.queryUnDividPermissionGroupList(basePermissionGroupReqDto);
        logger.info("======"+JSONObject.toJSON(result));

    }
}
