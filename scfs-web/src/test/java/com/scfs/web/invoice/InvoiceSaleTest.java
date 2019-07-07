package com.scfs.web.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.scfs.domain.invoice.dto.req.InvoiceReqList;
import com.scfs.domain.invoice.dto.req.InvoiceSaleManagerReqDto;
import com.scfs.domain.invoice.entity.InvoiceSaleManager;
import com.scfs.service.invoice.InvoiceSaleService;
import com.scfs.web.base.BaseJUnitTest;

public class InvoiceSaleTest extends BaseJUnitTest{
	
	@Autowired
	private InvoiceSaleService invoiceSaleService ;
	

	/**
	 * 
	 */
	@Test
	public void testNotSelectQuery() {
		try {
			InvoiceSaleManagerReqDto queryProjectPoolReqDto = new InvoiceSaleManagerReqDto();  
			queryProjectPoolReqDto.setBillNo("TH16102900003");
//			PageResult<BillDetail> rs = invoiceSaleService.querySaleNotSelectByCon(queryProjectPoolReqDto); 
//			 LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	@Test
	public void testSelectQuery() {
		try { 
			//PageResult<InvoiceSaleManagerResDto> rs = invoiceSaleService.queryInvoiceResultsByCon(2);
			 //LOGGER.info(JSONObject.toJSONString(rs,SerializerFeature.WriteMapNullValue)+"");
			 
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void testAddDtl() {
		try {
			InvoiceReqList queryProjectPoolReqDto = new InvoiceReqList(); 
			InvoiceSaleManager apply = new InvoiceSaleManager();
			List<InvoiceSaleManager> invoiceApply = new ArrayList<InvoiceSaleManager>();
			apply.setBillNo("123123123");;
			apply.setProvideInvoiceAmount(new BigDecimal(20)); 
			apply.setGoodsId(1); 
			invoiceApply.add(apply);
			queryProjectPoolReqDto.setInvoiceSaleManagerReqDto(invoiceApply);;
			invoiceSaleService.addBactchInvoice(queryProjectPoolReqDto);;
	        LOGGER.info(JSONObject.toJSONString(apply,SerializerFeature.WriteMapNullValue)+"");
		} catch (Exception e) {
			LOGGER.error("SDSDSDS", e);
		}
	}
	
	
	@Test
	public void testDelete() {
		try {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(1);
			invoiceSaleService.deleteInvoiceByIds(ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUpdate() {
		try {
			InvoiceReqList queryProjectPoolReqDto = new InvoiceReqList(); 
			InvoiceSaleManager apply = new InvoiceSaleManager();
			List<InvoiceSaleManager> invoiceApply = new ArrayList<InvoiceSaleManager>();
			apply.setId(1);
			apply.setBillNo("123123123");;
			apply.setProvideInvoiceAmount(new BigDecimal(20)); 
			apply.setGoodsId(1); 
			invoiceApply.add(apply);
			queryProjectPoolReqDto.setInvoiceSaleManagerReqDto(invoiceApply);; 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
