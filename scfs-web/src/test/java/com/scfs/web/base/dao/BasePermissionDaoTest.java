package com.scfs.web.base.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.dao.base.entity.BasePermissionDao;
import com.scfs.domain.base.entity.BasePermission;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016/9/23.
 */
public class BasePermissionDaoTest extends BaseJUnitTest {

    @Autowired
    private BasePermissionDao basePermissionDao;

    @Test
    public void testInsert(){
//        {编辑=/permission/edit, 提交=, 锁定=/permission/lock, 删除=/permission/delete, 详情=/permission/detail, 解锁=/permission/unlock}

        BasePermission basePermission = new BasePermission();
//            basePermission.setName("权限编辑");
//            basePermission.setUrl("/permission/edit");
//            basePermission.setState(1);
//            basePermission.setType(1);
//            basePermission.setCreateAt(new Date());
//            basePermissionDao.insert(basePermission);
//            basePermission.setName("权限提交");
//            basePermission.setUrl("/permission/submit");
//            basePermission.setState(1);
//            basePermission.setType(1);
//            basePermission.setCreateAt(new Date());
//            basePermissionDao.insert(basePermission);
//
//        //菜单权限
//        basePermission.setName("基本信息");
//        basePermission.setUrl("/base/index");
//        basePermission.setState(1);
//        basePermission.setType(2);
//        basePermission.setMenuLevel(1);
//        basePermission.setOrd(100001);
//        basePermission.setCreateAt(new Date());
//        basePermissionDao.insert(basePermission);

        //菜单权限
        basePermission.setName("主题信息");
        basePermission.setUrl("/base/index/two");
        basePermission.setState(1);
        basePermission.setType(2);
        basePermission.setMenuLevel(2);
        basePermission.setParentId(749);
        basePermission.setOrd(200001);
        basePermission.setCreateAt(new Date());
        basePermissionDao.insert(basePermission);
    }

    @Test
    public void testQuery(){
    }


}
