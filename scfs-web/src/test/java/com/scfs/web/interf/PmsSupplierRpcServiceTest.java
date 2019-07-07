package com.scfs.web.interf;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.service.interf.PmsSupplierRpcService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年12月20日.
 */
public class PmsSupplierRpcServiceTest extends BaseJUnitTest{
	@Autowired
	private PmsSupplierRpcService pmsSupplierRpcService;
	
	@Test
	public void testValidate() throws Exception {
		try {
	        String pmsSupplierNo = "GKD0009";
	        pmsSupplierRpcService.validateSupplier(pmsSupplierNo);
	        LOGGER.info(JSONObject.toJSONString("",SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e.getMessage(), e);
		}
	}
	
	@Test
	public void testUpload() throws Exception {
		try {
	        PMSSupplierBind pmsSupplierBind = new PMSSupplierBind();
	        pmsSupplierBind.setSupplierId(4);
	        pmsSupplierBind.setSupplierNo("C00001");
	        pmsSupplierBind.setPmsSupplierNo("GKD0006");
	        pmsSupplierBind.setProjectId(1);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(pmsSupplierBind,SerializerFeature.WriteMapNullValue)+"");

	        pmsSupplierRpcService.openSupplierInfo(pmsSupplierBind);
	        LOGGER.info(JSONObject.toJSONString("",SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e);
		}
	}
}

