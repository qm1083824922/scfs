package com.scfs.web.interf;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.service.interf.PmsPayRpcService;
import com.scfs.service.interf.PmsSyncReturnPurchsePassService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年12月20日.
 */
public class PmsPayRpcServiceTest extends BaseJUnitTest {
	@Autowired
	private PmsPayRpcService pmsPayRpcService;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PmsSyncReturnPurchsePassService pmsSyncReturnPurchsePassService; 
	
	@Test
	public void testValidate() throws Exception {
		try {
	        PayOrder payOrder = new PayOrder();
	        payOrder = payOrderDao.queryEntityById(184);
	        pmsPayRpcService.passPayOrder(payOrder , null);
	        LOGGER.info(JSONObject.toJSONString("",SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e);
		}
	}
	@Test
	public void testPurchase()  throws Exception{
		try {
			Audit audit=new Audit();
			audit.setSuggestion("测试");
			PurchaseOrderTitle purchaseOrderTitle= new PurchaseOrderTitle(); 
			purchaseOrderTitle.setId(11);
			purchaseOrderTitle.setOrderNo("ddd");
			pmsSyncReturnPurchsePassService.unPassPurchase(audit, purchaseOrderTitle);
	        LOGGER.info(JSONObject.toJSONString("",SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e);
		}
	}
}

