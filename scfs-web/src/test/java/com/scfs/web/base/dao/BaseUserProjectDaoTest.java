package com.scfs.web.base.dao;

import java.util.Date;

import com.scfs.dao.base.entity.BaseProjectDao;
import org.apache.ibatis.session.RowBounds;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.dao.base.entity.BaseUserProjectDao;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.BaseUserProject;
import com.scfs.domain.project.dto.req.UserProjectReqDto;
import com.scfs.web.base.BaseJUnitTest;

public class BaseUserProjectDaoTest extends BaseJUnitTest {
	
	@Autowired
    private BaseUserProjectDao baseUserProjectDao;
	@Autowired
	private BaseProjectDao baseProjectDao;
	
	@Test
    public void testInsert(){
		BaseUserProject baseUserProject = new BaseUserProject();
		BaseUser baseUser = new BaseUser();
		baseUser.setId(5);
		BaseProject baseProject = new BaseProject();
		baseProject.setId(1);
		baseUserProject.setCreator("张三1");
		baseUserProject.setCreateAt(new Date());
		baseUserProject.setAssigner("张三1");
		baseUserProject.setAssignAt(new Date());
		baseUserProjectDao.insertUserProject(baseUserProject);
	}
	
	@Test
    public void testUpdate(){
		BaseUserProject baseUserProject = new BaseUserProject();
		baseUserProject.setId(1);
		baseUserProjectDao.updateUserProject(baseUserProject);
	}
	
    @Test
    public void testQueryProjectsAssignedToUser(){
        logger.info(JSONObject.toJSON( baseProjectDao.queryUserProjectAssignedToUser(5, new RowBounds(0,15)) ));
    }
    
    @Test
    public void testQueryProjectsNotAssignedToUser(){
    	UserProjectReqDto uerProject = new UserProjectReqDto();
        logger.info(JSONObject.toJSON( baseProjectDao.queryProjectNotAssignedToUser(uerProject, new RowBounds(0,15)) ));
    }

}
