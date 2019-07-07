package com.scfs.web.project;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.project.dto.req.ProjectPoolDtlSearchReqDto;
import com.scfs.domain.project.dto.req.ProjectPoolSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectPoolResDto;
import com.scfs.domain.project.entity.ProjectPool;
import com.scfs.domain.project.entity.ProjectPoolDtl;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.web.base.BaseJUnitTest; 

public class ProjectPoolTest extends BaseJUnitTest{
	
	@Autowired
	private ProjectPoolService projectPoolService;
	
	@Test
	public void testQuery() {
		try {
			ProjectPoolSearchReqDto queryProjectPoolReqDto = new ProjectPoolSearchReqDto();
			queryProjectPoolReqDto.setId(1);
			PageResult<ProjectPoolResDto> rs = projectPoolService.queryProjectPoolResultsByCon(queryProjectPoolReqDto);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testQueryDtl() {
		try {
			ProjectPoolDtlSearchReqDto queryProjectPoolReqDto = new ProjectPoolDtlSearchReqDto();
			queryProjectPoolReqDto.setType(1);
			//PageResult<ProjectPoolDtlResDto> rs = projectPoolService.queryProjectPoolDtlResultsByCon(queryProjectPoolReqDto);
//	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testAddDtl() {
		try {
			ProjectPoolDtl queryProjectPoolReqDto = new ProjectPoolDtl();
			queryProjectPoolReqDto.setType(2);
			queryProjectPoolReqDto.setProjectId(4);
			queryProjectPoolReqDto.setBillNo("sds");
			queryProjectPoolReqDto.setBillSource(1);
			queryProjectPoolReqDto.setCustomerId(123);
//			Integer rs = projectPoolService.insertProjectPoolDtl(queryProjectPoolReqDto);
//	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("SDSDSDS", e);
		}
	}
	
	@Test
	public void testQueryById() {
		Result<ProjectPoolResDto> result = new Result<ProjectPoolResDto>();
		ProjectPool projectPool =new ProjectPool();
		projectPool.setId(1);
		try {
			ProjectPoolResDto vo = projectPoolService.detailProjectItemById(projectPool.getId());
			result.setItems(vo);
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(projectPool), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
	}
}
