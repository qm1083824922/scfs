package com.scfs.web.schedule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.service.schedule.ProfitReportJob;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator
 * Date: 2017年10月12日
 */
public class ProfitReportJobTest extends BaseJUnitTest{
	@Autowired
	private ProfitReportJob profitReportJob;
	
	@Test
	public void testExecute() {
		profitReportJob.execute();
	}

}

