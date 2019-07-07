package com.scfs.web.base.dao;

import com.scfs.dao.base.entity.BasePermissionRelationDao;
import com.scfs.domain.base.entity.BasePermissionRelation;
import com.scfs.web.base.BaseJUnitTest;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23.
 */
public class BasePermissionRelationDaoTest extends BaseJUnitTest {

    @Autowired
    private BasePermissionRelationDao basePermissionRelationDao;

    @Test
    public void testInsert(){
        BasePermissionRelation basePermissionRelation = new BasePermissionRelation();
        basePermissionRelation.setPermissionGroupId(1);
        basePermissionRelation.setPermissionId(2);
        basePermissionRelation.setCreateAt(new Date());
        List<BasePermissionRelation> permissionRelationList = Lists.newArrayList();
        permissionRelationList.add(basePermissionRelation);

        BasePermissionRelation basePermissionRelation2 = new BasePermissionRelation();
        basePermissionRelation2.setPermissionGroupId(145454);
        basePermissionRelation2.setPermissionId(72);
        basePermissionRelation2.setCreateAt(new Date());
        permissionRelationList.add(basePermissionRelation2);

        basePermissionRelationDao.insert(basePermissionRelation);
    }

    @Test
    public void testUpdate(){
        BasePermissionRelation basePermissionRelation = new BasePermissionRelation();
        basePermissionRelation.setId(123);
        basePermissionRelation.setIsDelete(1);
        basePermissionRelation.setDeleter("lihao");
        basePermissionRelation.setDeleteAt(new Date());
        basePermissionRelationDao.invalidPermissionRelationById(basePermissionRelation);
    }
}
