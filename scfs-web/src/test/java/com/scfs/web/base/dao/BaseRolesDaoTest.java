package com.scfs.web.base.dao;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.base.entity.BaseRoleDao;
import com.scfs.domain.base.dto.req.BaseRoleReqDto;
import com.scfs.domain.base.entity.BaseRole;
import com.scfs.web.base.BaseJUnitTest;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/9/23.
 */
public class BaseRolesDaoTest extends BaseJUnitTest {
    @Autowired
    private BaseRoleDao baseRolesDao;

    @Test
    public void testInsert() {
        BaseRole baseRoles = new BaseRole();
        baseRoles.setName("管理员");
        baseRoles.setState(BaseConsts.TWO);
        baseRolesDao.insert(baseRoles);
    }

    @Test
    public void testQuery() {
        logger.info(JSONObject.toJSON(baseRolesDao.queryBaseRoleListByUserName("张三")));
    }

    @Test
    public void testQueryPage() {
        BaseRoleReqDto baseRole = new BaseRoleReqDto();
//        baseRole.setName("管理员");
        RowBounds rowBounds = new RowBounds(0, 12);
        logger.info(JSONObject.toJSON(baseRolesDao.queryBaseRoleList(baseRole, rowBounds)));
    }

    @Test
    public void testUpdate() {
        BaseRole baseRole = new BaseRole();
        baseRole.setId(137);
        baseRole.setName("普通角色");
        baseRole.setState(2);
        baseRolesDao.update(baseRole);
    }

    @Test
    public void testQueryDivid() {
        baseRolesDao.queryDividRoleByUserId(1, new RowBounds(1, 20));
    }
}
