package com.scfs.web.logistics;


import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.domain.logistics.dto.req.StlHistorySearchReqDto;
import com.scfs.domain.logistics.dto.req.StlHistorySummarySearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlHistoryResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.logistics.StlHistoryService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * Created by Administrator on 2016年10月19日.
 */
public class StlHistoryServiceImplTest extends BaseJUnitTest{
	@Autowired
	private StlHistoryService stlHistoryService;
	
	@Test
	public void test() throws Exception {
		try {
			StlHistorySearchReqDto stlHistorySearchReqDto = new StlHistorySearchReqDto();
			stlHistorySearchReqDto.setSupplierId(111);
			PageResult<StlHistoryResDto> result = stlHistoryService.queryStlHistoryResultsByCon(stlHistorySearchReqDto);
	        LOGGER.info(JSONObject.toJSON(result)+"");
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e);
		}
	}
	
	@Test
	public void testSummary() throws Exception {        
		try {
			StlHistorySummarySearchReqDto stlHistorySummarySearchReqDto = new StlHistorySummarySearchReqDto();
			stlHistorySummarySearchReqDto.setProjectFlag(1);
			stlHistorySummarySearchReqDto.setSupplierFlag(1);
	        PageResult<StlHistoryResDto> result2 = stlHistoryService.queryStlHistorySummaryResultsByCon(stlHistorySummarySearchReqDto);
	        LOGGER.info(JSONObject.toJSON(result2)+"");
		} catch (Exception e) {
			LOGGER.error("数据库异常:", e);
		}
	}
}

