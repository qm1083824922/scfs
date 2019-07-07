package com.scfs.web.sale;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillDeliverySearchReqDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月27日.
 */
public class BillDeliveryTest extends BaseJUnitTest{
	@Autowired
	private BillDeliveryService billDeliveryService;
	
	@Test
	public void testQuery() {
		BillDeliverySearchReqDto billDeliverySearchReqDto = new BillDeliverySearchReqDto();
        LOGGER.info("[参数]" + JSONObject.toJSONString(billDeliverySearchReqDto,SerializerFeature.WriteMapNullValue)+"");

		billDeliverySearchReqDto.setBillNo("11");
		try {
			PageResult<BillDeliveryResDto> result = billDeliveryService.queryBillDeliveryResultsByCon(billDeliverySearchReqDto);
	        LOGGER.info(JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue)+"");
	        
	        
	        BillDelivery billDelivery = new BillDelivery();
	        billDelivery.setId(1);
	        Result<BillDeliveryResDto> rs = billDeliveryService.detailBillDeliveryById(billDelivery);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
	
	@Test
	public void testAdd() {
		BillDelivery billDelivery = new BillDelivery();

        billDelivery.setWarehouseId(33);
        billDelivery.setProjectId(3);
        billDelivery.setCustomerId(52);
        billDelivery.setCustomerAddressId(1);
        billDelivery.setBillType(1);
        billDelivery.setAffiliateNo("121212");
        billDelivery.setTransferMode(1);
        billDelivery.setRequiredSendDate(new Date());
        LOGGER.info("[参数]" + JSONObject.toJSONString(billDelivery,SerializerFeature.WriteMapNullValue)+"");
		try {
			billDeliveryService.addBillDelivery(billDelivery);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
	
	@Test
	public void testSubmit() {
		try {
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(4);
			billDeliveryService.submitBillDeliveryById(billDelivery);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
}

