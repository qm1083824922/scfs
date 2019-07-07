package com.scfs.web.common;

import com.scfs.dao.common.AsyncExcelDao;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by Administrator on 2017/2/15.
 */
public class AsyncExcelDaoTest  extends BaseJUnitTest{
    @Autowired
    private AsyncExcelDao asyncExcelDao;

    @Test
    public void insert(){
        AsyncExcel asyncExcelWithBLOBs = new AsyncExcel();
        asyncExcelWithBLOBs.setClassName("aa");
        asyncExcelWithBLOBs.setMethodName("bb");
        asyncExcelWithBLOBs.setPoType(2);
        asyncExcelWithBLOBs.setTemplatePath("/as/sdd/dkk/ff");
        asyncExcelWithBLOBs.setExcelPath("/excel/");
        asyncExcelWithBLOBs.setYn(0);
        asyncExcelWithBLOBs.setResult(0);
        asyncExcelDao.insert(asyncExcelWithBLOBs);
    }

    public static void main(String [] args) throws Exception {
        ScriptEngineManager factory = new ScriptEngineManager();//step 1
        ScriptEngine engine = factory.getEngineByName("JavaScript");//Step 2
        engine.eval("print('Hello, Scripting')");//Step 3
    }
}
