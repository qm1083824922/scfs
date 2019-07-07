package com.scfs.web.schedule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.service.schedule.AutoOffsetReceiveJob;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2017年7月3日.
 */
public class AutoOffsetReceiveJobTest extends BaseJUnitTest{
    @Autowired
    private AutoOffsetReceiveJob autoOffsetReceiveJob;

    @Test
    public void testExcute(){
    	autoOffsetReceiveJob.execute();
    }
}

