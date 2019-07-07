package com.scfs.web.base.dao;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.PwdUtils;
import com.scfs.dao.base.entity.BaseUserDao;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by  on 2016/9/29.
 */
public class BaseUserDaoTest extends BaseJUnitTest {

    @Autowired
    private BaseUserDao baseUserDao;

    @Test
    public void testInsert(){
        BaseUser baseUser = new BaseUser();
        baseUser.setEmployeeNumber("246208");
        baseUser.setUserName("tester");
        baseUser.setPassword(PwdUtils.encryptPassword("123456"));
        baseUser.setChineseName("张三1");
        baseUser.setEnglishName("zhangshan");
        baseUser.setMobilePhone("13812345678");
        baseUser.setEmail("test@163.com");
        baseUser.setStatus(2);
        baseUser.setCreator("李四1");
        baseUser.setCreateAt(new Date());
        baseUserDao.insert(baseUser);
    }

    @Test
    public void testQuery(){
//        BaseUser baseUser = new BaseUser();
//        logger.info(JSONObject.toJSON( baseUserDao.queryBaseUserList(baseUser, new RowBounds(0,15)) ));
        logger.info(JSONObject.toJSON( baseUserDao.queryAllUser(null))+"=============");
        logger.info(JSONObject.toJSON( baseUserDao.queryAllUser("2016-11-25 10:57:00"))+"++++++");
        logger.info(DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS,baseUserDao.queryLastUpdateAt()));
    }

    @Test
    public void testUpdate(){
        BaseUser baseUser = new BaseUser();
        baseUser.setId(5);
        baseUser.setUserName("tester1");
        baseUserDao.update(baseUser);
    }

    @Test
    public void testSubmit(){
        BaseUser baseUser = new BaseUser();
        baseUser.setId(5);
        baseUserDao.submit(baseUser);
    }

    @Test
    public void testLock(){
        BaseUser baseUser = new BaseUser();
        baseUser.setId(5);
        baseUserDao.lock(baseUser);
    }

    @Test
    public void testUnLock(){
        BaseUser baseUser = new BaseUser();
        baseUser.setId(5);
        baseUserDao.unlock(baseUser);
    }

    @Test
    public void testDelete(){
        BaseUser baseUser = new BaseUser();
        baseUser.setId(5);
        baseUser.setDeleter("李四");
        baseUserDao.delete(baseUser);
    }

}
