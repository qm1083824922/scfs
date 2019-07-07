package com.scfs.web.schedule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.service.schedule.InvokeLogJob;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2017年1月9日.
 */
public class InvokeLogJobTest extends BaseJUnitTest{
    @Autowired
    private InvokeLogJob invokeLogJob;

    @Test
    public void testExcute(){
    	invokeLogJob.execute();
    }
}

