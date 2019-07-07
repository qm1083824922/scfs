package com.scfs.web.schedule;

import com.scfs.service.schedule.ChinaBankExchangeRateJob;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2016/12/8.
 */
public class ChinaBankExchangeRateJobTest  extends BaseJUnitTest{
    @Autowired
    private ChinaBankExchangeRateJob chinaBankExchangeRateJob;

    @Test
    public void testExcute(){
        chinaBankExchangeRateJob.execute();
    }
}
