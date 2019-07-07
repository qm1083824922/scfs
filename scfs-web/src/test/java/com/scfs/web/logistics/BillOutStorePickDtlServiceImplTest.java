package com.scfs.web.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月31日.
 */
public class BillOutStorePickDtlServiceImplTest extends BaseJUnitTest{
	@Autowired
	private BillOutStorePickDtlService billOutStorePickDtlService;
	
	@Test
	public void testAdd() {
		try {
			BillOutStoreDtl billOutStoreDtl = new BillOutStoreDtl();
			billOutStoreDtl.setId(7);
			
			List<Stl> stlList = new ArrayList<Stl>();
			Stl stl = new Stl();
			stl.setId(2);
			stl.setBatchNo("1111");
			stl.setGoodsStatus(1);
			stl.setPickupNum(new BigDecimal("1"));
			stlList.add(stl);
			billOutStoreDtl.setStlList(stlList);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(billOutStoreDtl,SerializerFeature.WriteMapNullValue)+"");

			billOutStorePickDtlService.addBillOutStorePickDtls(billOutStoreDtl);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
	
	@Test
	public void testDelete() {
		try {
			
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
	
	@Test
	public void testAutoPick() {
		try {
			
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
}

