package com.scfs.web.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.dto.req.PoOrderReqDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreResDto;
import com.scfs.domain.logistics.dto.resp.PoOrderDtlResDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.logistics.BillInStoreService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月17日.
 */
public class BillInStoreServiceImplTest extends BaseJUnitTest{
	@Autowired
	private BillInStoreService billInStoreService;
	
	@Test
	public void testAdd() throws Exception {
		try {
			BillInStore billInStore = new BillInStore();
			billInStore.setProjectId(3);
			billInStore.setWarehouseId(33);
			billInStore.setSupplierId(35);
			billInStore.setCustomerId(52);
			billInStore.setBillType(1);
			billInStore.setReceiveNum(new BigDecimal("2"));
			billInStore.setReceiveAmount(new BigDecimal("100"));
			billInStore.setReceiveDate(new Date());
			
			billInStoreService.addBillInStore(billInStore);
		} catch (Exception e) {
			logger.error("", e);
		}


	}
	
	@Test
	public void testQueryList() throws Exception {
		BillInStoreSearchReqDto billInStoreReqDto = new BillInStoreSearchReqDto();
        LOGGER.info("[参数]" + JSONObject.toJSONString(billInStoreReqDto,SerializerFeature.WriteMapNullValue)+"");
		PageResult<BillInStoreResDto> list = billInStoreService.queryBillInStoreList(billInStoreReqDto);
        LOGGER.info(JSONObject.toJSONString(list,SerializerFeature.WriteMapNullValue)+"");
	}
	
	@Test
	public void testQueryById() throws Exception {
		BillInStoreSearchReqDto billInStoreReqDto = new BillInStoreSearchReqDto();
		billInStoreReqDto.setId(1);
		Result<BillInStoreResDto> result = billInStoreService.queryBillInStoreById(billInStoreReqDto);
        LOGGER.info(JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue)+"");
	}
	
	@Test
	public void testSubmit() throws Exception {
		try {
			BillInStore billInStore = new BillInStore();
			billInStore.setId(13);

			billInStoreService.submitBillInStore(billInStore , new Date());
		} catch (Exception e) {
	        LOGGER.error("", e);
		}

	}
	
	@Test
	public void testDelete() throws Exception {
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		billInStoreService.deleteBillInStoreByIds(ids);
	}
	
	@Test
	public void testUpdate() throws Exception {
		BillInStore billInStore = new BillInStore();
		billInStore.setId(1);
		billInStore.setReceiveNum(new BigDecimal("20"));
		billInStore.setReceiveAmount(new BigDecimal("2000"));
		billInStore.setReceiveDate(new Date());
		billInStoreService.updateBillInStore(billInStore);
	}
	
	@Test
	public void testQueryOrder() {
		BillInStoreDtlSearchReqDto billInStoreDtlReqDto = new BillInStoreDtlSearchReqDto();
		billInStoreDtlReqDto.setId(3);
		try {
			PoOrderReqDto poOrderReqDto = new PoOrderReqDto();
			PageResult<PoOrderDtlResDto> result = billInStoreService.queryPoOrderDtlList(poOrderReqDto);   
	        LOGGER.info(JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
	        LOGGER.error("", e);

		}
	}
}

