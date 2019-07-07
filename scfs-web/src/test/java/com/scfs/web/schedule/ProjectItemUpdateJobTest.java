package com.scfs.web.schedule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.service.schedule.ProjectItemUpdateJob;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年12月22日.
 */
public class ProjectItemUpdateJobTest extends BaseJUnitTest{
    @Autowired
    private ProjectItemUpdateJob projectItemUpdateJob;

    @Test
    public void testExcute(){
    	try {
			projectItemUpdateJob.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

