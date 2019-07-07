package com.scfs.web.logistics;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.dto.req.StlSummarySearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.logistics.StlService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月19日.
 */
public class StlServiceImplTest extends BaseJUnitTest{
	@Autowired
	private StlService stlService;
	
	@Test
	public void test() throws Exception {
		try {
			StlSearchReqDto stlReqDto = new StlSearchReqDto();
			stlReqDto.setSupplierId(111);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(stlReqDto,SerializerFeature.WriteMapNullValue)+"");

			PageResult<StlResDto> result = stlService.queryStlResultsByCon(stlReqDto);
	        LOGGER.info(JSONObject.toJSONString(result,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e);
		}
	}
	
	@Test
	public void testSummary() throws Exception {        
		try {
			StlSummarySearchReqDto stlSummaryReqDto = new StlSummarySearchReqDto();
	        stlSummaryReqDto.setProjectFlag(1);
	        stlSummaryReqDto.setSupplierFlag(1);
	        LOGGER.info("[参数]" + JSONObject.toJSONString(stlSummaryReqDto,SerializerFeature.WriteMapNullValue)+"");

	        PageResult<StlResDto> rs = stlService.queryStlSummaryResultsByCon(stlSummaryReqDto);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e);
		}
	}
}

