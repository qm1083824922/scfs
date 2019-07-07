package com.scfs.web.logistics;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.service.schedule.CopyStlJob;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月26日.
 */
public class CopyStlJobTest extends BaseJUnitTest{
	@Autowired
	private CopyStlJob copyStlJob;
	
	@Test
	public void test() {
		try {
			copyStlJob.execute();
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e);
		}
	}

}

