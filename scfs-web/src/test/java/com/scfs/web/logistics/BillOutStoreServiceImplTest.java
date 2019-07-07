package com.scfs.web.logistics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreResDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.common.exception.BaseException;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月20日.
 */
public class BillOutStoreServiceImplTest extends BaseJUnitTest{
	@Autowired 
	private BillOutStoreService billOutStoreService;

	
	@Test
	public void testQuery() {
		BillOutStoreSearchReqDto billOutStoreReqDto = new BillOutStoreSearchReqDto();
		try {
	        LOGGER.info("[参数1]" + JSONObject.toJSONString(billOutStoreReqDto,SerializerFeature.WriteMapNullValue)+"");
			PageResult<BillOutStoreResDto> rs = billOutStoreService.queryBillOutStoreList(billOutStoreReqDto);
	        LOGGER.info("[结果]" + JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
	        
	        billOutStoreReqDto.setId(1);
	        LOGGER.info("[参数2]" + JSONObject.toJSONString(billOutStoreReqDto,SerializerFeature.WriteMapNullValue)+"");
			Result<BillOutStoreResDto> dto = billOutStoreService.queryBillOutStoreById(billOutStoreReqDto);
	        LOGGER.info("[结果]" + JSONObject.toJSONString(dto,SerializerFeature.WriteMapNullValue)+"");

	    

		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	@Test
	public void testAdd() {
		try {
			BillOutStore billOutStore = new BillOutStore();
			billOutStore.setProjectId(3);
			billOutStore.setWarehouseId(276);
			billOutStore.setCustomerId(52);
			billOutStore.setBillType(2);
			billOutStore.setReceiveWarehouseId(278);
			billOutStore.setTransferMode(1);
			billOutStore.setRequiredSendDate(new Date());
			billOutStore.setSendDate(new Date());
			billOutStore.setCustomerAddressId(2);
			billOutStore.setAffiliateNo("111111");
			
			billOutStoreService.addBillOutStore(billOutStore);
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	@Test
	public void testDelete() {
		try {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(1);
			
			billOutStoreService.deleteBillOutStoreByIds(ids);
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}

	@Test
	public void testSubmit() {
		BillOutStore billOutStore = new BillOutStore();
		billOutStore.setId(9);
		try {
			billOutStoreService.submitBillOutStore(billOutStore);
		} catch (BaseException e) {
			LOGGER.error("提交出库单异常[{}]",e.getMsg(),e);
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	@Test
	public void testAutoPick() {
		try {
			BillOutStore billOutStore = new BillOutStore();
			billOutStore.setId(1);
			
			billOutStoreService.submitBillOutStore(billOutStore);
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	@Test
	public void testSendBillOutStore() {
		try {
			BillOutStore billOutStore = new BillOutStore();
			billOutStore.setId(9);
			
			billOutStoreService.sendBillOutStore(billOutStore);
		} catch (Exception e) {
	        LOGGER.error("", e);
		}
	}
	
	
	
}

