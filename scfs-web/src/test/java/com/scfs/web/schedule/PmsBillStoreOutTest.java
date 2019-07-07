package com.scfs.web.schedule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.service.schedule.PmsPayOrderDealJob;
import com.scfs.web.base.BaseJUnitTest;

public class PmsBillStoreOutTest extends BaseJUnitTest {

	@Autowired
	PmsPayOrderDealJob payOrderDealJob;

	@Test
	public void updateStoreOut() {
		try {
			payOrderDealJob.execute();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
