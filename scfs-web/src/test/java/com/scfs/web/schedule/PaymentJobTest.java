package com.scfs.web.schedule;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.scfs.service.schedule.PmsBillInStoreJob; 
import com.scfs.web.base.BaseJUnitTest;

public class PaymentJobTest  extends BaseJUnitTest{
	 	
		@Autowired
	    private PmsBillInStoreJob pmsBillInStoreJob;

	    @Test
	    public void testExcute(){
	    	pmsBillInStoreJob.execute();
	    }
	    
}
