package com.scfs.web.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlResDto;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.logistics.BillOutStoreDtlService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月31日.
 */
public class BillOutStoreDtlServiceImplTest extends BaseJUnitTest{
	@Autowired 
	private BillOutStoreDtlService billOutStoreDtlService;
	
	@Test
	public void testQuery() {
		try {
			BillOutStoreDtlSearchReqDto billOutStoreDtlReqDto = new BillOutStoreDtlSearchReqDto();
			billOutStoreDtlReqDto.setId(1);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(billOutStoreDtlReqDto,SerializerFeature.WriteMapNullValue)+"");
	        Result<BillOutStoreDtlResDto> result = billOutStoreDtlService.queryBillOutStoreDtlById(billOutStoreDtlReqDto);
	        LOGGER.info("[结果]" + JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue)+"");
			
			billOutStoreDtlReqDto = new BillOutStoreDtlSearchReqDto();
			billOutStoreDtlReqDto.setBillOutStoreId(1);
	        LOGGER.info("[参数2]" + JSONObject.toJSONString(billOutStoreDtlReqDto,SerializerFeature.WriteMapNullValue)+"");
			PageResult<BillOutStoreDtlResDto> pageResult = billOutStoreDtlService.queryBillOutStoreDtlsByBillOutStoreId(billOutStoreDtlReqDto, false);
	        LOGGER.info("[结果2]" + JSONObject.toJSONString(pageResult,SerializerFeature.WriteMapNullValue)+"");

		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}

	
	@Test
	public void testAdd() {
		try {
			BillOutStoreReqDto billOutStoreReqDto = new BillOutStoreReqDto();
			billOutStoreReqDto.setId(9);
			
			List<BillOutStoreDtl> billOutStoreDtls = new ArrayList<BillOutStoreDtl>();
			BillOutStoreDtl billOutStoreDtl = new BillOutStoreDtl();
			billOutStoreDtl.setGoodsId(6);
			billOutStoreDtl.setSendNum(new BigDecimal(2));
			billOutStoreDtl.setSendPrice(new BigDecimal("100"));
			billOutStoreDtl.setBatchNo("111");
			billOutStoreDtl.setGoodsStatus(1);
			
			BillOutStoreDtl billOutStoreDtl2 = new BillOutStoreDtl();
			billOutStoreDtl2.setGoodsId(6);
			billOutStoreDtl2.setSendNum(new BigDecimal(1));
			billOutStoreDtl2.setSendPrice(new BigDecimal("100"));
			billOutStoreDtl2.setBatchNo("222");
			billOutStoreDtl2.setGoodsStatus(1);
			
			billOutStoreDtls.add(billOutStoreDtl);
			billOutStoreDtls.add(billOutStoreDtl2);
			billOutStoreReqDto.setBillOutStoreDtlList(billOutStoreDtls);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(billOutStoreReqDto,SerializerFeature.WriteMapNullValue)+"");
	        billOutStoreDtlService.addBillOutStoreDtls(billOutStoreReqDto);
		} catch (Exception e) {
			LOGGER.error("", e);
		}
	}
	
}

