package com.scfs.web.schedule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.service.schedule.CurrentMonthFundReportJob;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator
 * Date: 2017年10月12日
 */
public class CurrentMonthFundReportJobTest extends BaseJUnitTest{
    @Autowired
    private CurrentMonthFundReportJob currentMonthFundReportJob;

    @Test
    public void testExecute(){
    	currentMonthFundReportJob.execute();
    }
}

