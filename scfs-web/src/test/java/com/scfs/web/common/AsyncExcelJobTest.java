package com.scfs.web.common;

import com.scfs.service.schedule.AsyncExcelJob;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/2/15.
 */
public class AsyncExcelJobTest extends BaseJUnitTest {
    @Autowired
    private AsyncExcelJob asyncExcelJob;

    @Test
    public void testExecute(){
        asyncExcelJob.execute();
    }
}
