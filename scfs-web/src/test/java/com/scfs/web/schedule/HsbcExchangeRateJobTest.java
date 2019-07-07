package com.scfs.web.schedule;

import com.scfs.service.schedule.HsbcExchangeRateJob;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/12/8.
 */
public class HsbcExchangeRateJobTest extends BaseJUnitTest {

    @Autowired
    private HsbcExchangeRateJob hsbcExchangeRateJob;

    @Test
    public void testExcute(){
        hsbcExchangeRateJob.execute();
    }
}
