package com.scfs.web.invoice;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.invoice.InvoiceInfoDao;
import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceApplyManagerResDto;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;
import com.scfs.domain.invoice.entity.InvoiceInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.service.invoice.InvoiceApplyService;
import com.scfs.web.base.BaseJUnitTest;

public class InvoiceTest extends BaseJUnitTest{
	
	@Autowired
	private InvoiceApplyService invoiceApplyService ;
	
	@Autowired
	private InvoiceInfoDao invoiceInfoDao;
	@Test
	public void testQuery() {
		try {
			InvoiceApplyManagerReqDto queryProjectPoolReqDto = new InvoiceApplyManagerReqDto();
			queryProjectPoolReqDto.setProjectId(2);
			PageResult<InvoiceApplyManagerResDto> rs = invoiceApplyService.queryInvoiceResultsByCon(queryProjectPoolReqDto);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testAddDtl() {
		try {
			InvoiceApplyManager queryProjectPoolReqDto = new InvoiceApplyManager();
			queryProjectPoolReqDto.setApplyType(1);
			queryProjectPoolReqDto.setBillType(1);
			queryProjectPoolReqDto.setBusinessUnitId(1);
			queryProjectPoolReqDto.setInvoiceType(1);
			queryProjectPoolReqDto.setStatus(3);
			queryProjectPoolReqDto.setProjectId(3); 
			Integer rs = invoiceApplyService.insertInvoice(queryProjectPoolReqDto);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("SDSDSDS", e);
		}
	}
	
	@Test
	public void testQueryById() {
		try {
			InvoiceApplyManager  queryProjectPoolReqDto = new InvoiceApplyManager();
			queryProjectPoolReqDto.setId(1);
			InvoiceApplyManager rs = invoiceApplyService.detailInvoiceById(queryProjectPoolReqDto);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testupdateById() {
		try {
			InvoiceApplyManager  queryProjectPoolReqDto = new InvoiceApplyManager();
			queryProjectPoolReqDto.setId(1);
			queryProjectPoolReqDto.setStatus(2);
			Integer rs = invoiceApplyService.updateInvoiceById(queryProjectPoolReqDto);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testDeleteById() {
		try {
			InvoiceApplyManager  queryProjectPoolReqDto = new InvoiceApplyManager();
			queryProjectPoolReqDto.setId(2); 
			Integer rs = invoiceApplyService.deleteInvoice(queryProjectPoolReqDto);
	        LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testAdd() {
		try {
			for(int num = 5; num < 200;num++){
				InvoiceInfo invoiceInfo = new InvoiceInfo(); 
				invoiceInfo.setCustomerId(5);
				invoiceInfo.setInvoiceApplyId(139);
				invoiceInfo.setInvoiceApplyNo("VT16122600002");
				invoiceInfo.setBaseInvoiceId(3); 
				invoiceInfo.setInvoiceNo("VT16122600002" + BaseConsts.CONJUNCTION_FLAG + num);
				invoiceInfo.setInvoiceRemark("");
				invoiceInfo.setCreateAt(new Date()); 
				invoiceInfo.setStatus(BaseConsts.ONE);
				Integer info = invoiceInfoDao.insert(invoiceInfo);
				 LOGGER.info(JSONObject.toJSONString(info,SerializerFeature.WriteMapNullValue)+"");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
