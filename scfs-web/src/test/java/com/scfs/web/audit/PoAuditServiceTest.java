package com.scfs.web.audit;

import com.alibaba.fastjson.JSONObject;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.service.audit.PoAuditService;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/10/31.
 */
public class PoAuditServiceTest  extends BaseJUnitTest{
    @Autowired
    private PoAuditService poAuditService;

    @Test
    public void testQuery(){
        LOGGER.info(JSONObject.toJSONString(poAuditService.queryPoAuditInfoResultByPoId(8)));
    }

    @Test
    public void testInsert(){
        Audit audit = new Audit();
        audit.setId(2);
//        poAuditService.createFinanceAudit(audit);
        PurchaseOrderTitle po = new PurchaseOrderTitle();
        po.setProjectId(1);
        po.setSupplierId(1);
        po.setCustomerId(1);
        po.setId(1);
        po.setOrderTotalAmount(new BigDecimal("111111"));
        poAuditService.startAudit(po);
    }
}
