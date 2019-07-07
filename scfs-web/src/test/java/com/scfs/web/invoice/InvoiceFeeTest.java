package com.scfs.web.invoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.invoice.dto.req.InvoiceApplyManagerReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceReqList;
import com.scfs.domain.invoice.entity.InvoiceFeeManager;
import com.scfs.service.invoice.InvoiceFeeService;
import com.scfs.web.base.BaseJUnitTest; 

public class InvoiceFeeTest  extends BaseJUnitTest{
	
	@Autowired
	private InvoiceFeeService invoiceFeeService ;
	
	/**
	 * 
	 */
	@Test
	public void testQuery() {
		try { 
		//	PageResult<InvoiceFeeManagerResDto> rs = invoiceFeeService.queryInvoiceResultsByCon(1);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	@Test
	public void testAddDtl() {
		try {
			InvoiceReqList queryProjectPoolReqDto = new InvoiceReqList(); 
			InvoiceFeeManager apply = new InvoiceFeeManager();
			List<InvoiceFeeManager> invoiceApply = new ArrayList<InvoiceFeeManager>();
			apply.setFeeId(1);
			apply.setProvideInvoiceAmount(new BigDecimal(20));
			apply.setFeeNo("23213");
			apply.setInvoiceApplyId(1);
			invoiceApply.add(apply);
			queryProjectPoolReqDto.setInvoiceFeeManagerReqDto(invoiceApply);  
		} catch (Exception e) {
			LOGGER.error("SDSDSDS", e);
		}
	}
	
	@Test
	public void testDelete() {
		try {
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(2);
			invoiceFeeService.deleteInvoiceByIds(ids);;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testQueryNotSelect() {
		try {
			InvoiceApplyManagerReqDto queryProjectPoolReqDto = new InvoiceApplyManagerReqDto();
			queryProjectPoolReqDto.setProjectId(1);
			queryProjectPoolReqDto.setCustomerId(52);  
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
}
