package com.scfs.web.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.service.logistics.BillInStoreTallyDtlService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月20日.
 */
public class BillInStoreTallyDtlServiceImplTest extends BaseJUnitTest{
	@Autowired
	private BillInStoreTallyDtlService billInStoreTallyDtlService;
	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	
	@Test
	public void testQuery() {
		BillInStoreDtlSearchReqDto billInStoreDtlReqDto = new BillInStoreDtlSearchReqDto();
		billInStoreDtlReqDto.setId(7);
		try {
			
	        //LOGGER.info(JSONObject.toJSON(rs)+"");
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	@Test
	public void testAdd() {
		try {
			BillInStoreDtlSearchReqDto billInStoreDtlReqDto = new BillInStoreDtlSearchReqDto();
			billInStoreDtlReqDto.setId(3);
			BillInStoreDtl billInStoreDtl2 = billInStoreDtlDao.queryById(billInStoreDtlReqDto);
			
			List<BillInStoreTallyDtl> billInStoreTallyDtls = new ArrayList<BillInStoreTallyDtl>();
			BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
			billInStoreTallyDtl.setTallyNum(new BigDecimal("1"));
			billInStoreTallyDtl.setBatchNo("T01");
			billInStoreTallyDtl.setGoodsStatus(1);
			billInStoreTallyDtls.add(billInStoreTallyDtl);
			billInStoreDtl2.setBillInStoreTallyDtlList(billInStoreTallyDtls);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(billInStoreDtl2,SerializerFeature.WriteMapNullValue)+"");

			billInStoreTallyDtlService.addBillInStoreTallyDtls(billInStoreDtl2);
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	@Test
	public void testDel() {
		try {
			BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
			billInStoreTallyDtl.setId(6);
			billInStoreTallyDtlService.deleteById(billInStoreTallyDtl);
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	@Test
	public void testAutoTally() {
		try {
			BillInStore billInStore = new BillInStore();
			billInStore.setId(4);
			billInStoreTallyDtlService.autoTally(billInStore);
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}	
}

