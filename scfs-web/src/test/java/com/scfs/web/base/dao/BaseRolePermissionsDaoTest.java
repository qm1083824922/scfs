package com.scfs.web.base.dao;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.scfs.domain.base.entity.BaseRolePermissionGroup;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016/9/23.
 */
public class BaseRolePermissionsDaoTest  extends BaseJUnitTest{

    @Test
    public void testInsert(){
        BaseRolePermissionGroup baseRolePermissions = new BaseRolePermissionGroup();
        baseRolePermissions.setRoleId(22212);
        baseRolePermissions.setPermissionGroupId(2234);

        List<BaseRolePermissionGroup> rolePermissionGroupList = Lists.newArrayList();
        rolePermissionGroupList.add(baseRolePermissions);

        BaseRolePermissionGroup baseRolePermissions2 = new BaseRolePermissionGroup();
        baseRolePermissions2.setRoleId(11412);
        baseRolePermissions2.setPermissionGroupId(23232);
        rolePermissionGroupList.add(baseRolePermissions2);

    }

}
