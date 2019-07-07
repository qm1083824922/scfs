package com.scfs.web.base.dao;

import com.scfs.dao.base.entity.BasePermissionGroupDao;
import com.scfs.domain.base.entity.BasePermissionGroup;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/23.
 */
public class BasePermissionGroupDaoTest extends BaseJUnitTest{
    @Autowired
    private BasePermissionGroupDao basePermissionsDao;

    @Test
    public void testInsert(){
        for(int i = 0;i<5;i++){
            BasePermissionGroup basePermissions = new BasePermissionGroup();
            basePermissions.setName("权限组"+i);
            basePermissions.setCreateAt(new Date());
            basePermissionsDao.insert(basePermissions);
        }
    }
}
