package com.scfs.web.sale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.sale.dto.req.BillDeliveryDtlSearchReqDto;
import com.scfs.domain.sale.dto.req.BillDeliveryReqDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryDtlResDto;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.service.sale.BillDeliveryDtlService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月28日.
 */
public class BillDeliveryDtlTest extends BaseJUnitTest{
	@Autowired
	private BillDeliveryDtlService billDeliveryDtlService;
	
	@Test
	public void testQuery() {
		try {
			BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto = new BillDeliveryDtlSearchReqDto();
			billDeliveryDtlSearchReqDto.setBillDeliveryId(1);
			PageResult<BillDeliveryDtlResDto> rs = billDeliveryDtlService.queryBillDeliveryDtlsByBillDeliveryId(billDeliveryDtlSearchReqDto);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testAdd() {
		try {
			BillDeliveryReqDto billDeliveryReqDto = new BillDeliveryReqDto();
			billDeliveryReqDto.setId(4);
			List<BillDeliveryDtl> billDeliveryDtlList = new ArrayList<BillDeliveryDtl>(); 
			BillDeliveryDtl billDeliveryDtl = new BillDeliveryDtl();
			billDeliveryDtl.setRequiredSendPrice(new BigDecimal("50"));
			billDeliveryDtl.setRequiredSendNum(new BigDecimal("1"));
			billDeliveryDtl.setGoodsId(1);		
			billDeliveryDtlList.add(billDeliveryDtl);
			billDeliveryReqDto.setBillDeliveryDtlList(billDeliveryDtlList);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(billDeliveryReqDto,SerializerFeature.WriteMapNullValue)+"");
			billDeliveryDtlService.addBillDeliveryDtls(billDeliveryReqDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAddByStl() {
		try {
			BillDeliveryReqDto billDeliveryReqDto = new BillDeliveryReqDto();
			billDeliveryReqDto.setId(1);
			List<Stl> stlList = new ArrayList<Stl>(); 
			Stl stl = new Stl();
			stl.setId(1);
			stl.setRequiredSendPrice(new BigDecimal("200"));
			stl.setRequiredSendNum(new BigDecimal("1"));
			stl.setGoodsId(1);
			stlList.add(stl);
			billDeliveryReqDto.setStlList(stlList);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(billDeliveryReqDto,SerializerFeature.WriteMapNullValue)+"");
			billDeliveryDtlService.addBillDeliveryDtlsByStl(billDeliveryReqDto);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDelete() {
		try {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(4);
			//billDeliveryDtlService.deleteBillDeliveryDtlsByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

