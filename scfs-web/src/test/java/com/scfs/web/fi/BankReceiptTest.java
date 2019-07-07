package com.scfs.web.fi;

import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.web.base.BaseJUnitTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class BankReceiptTest extends BaseJUnitTest {
	@Autowired
	BankReceiptService bankReceiptService;

	@Test
	public void queryAccountBook() {
		BankReceiptSearchReqDto bankReceiptReqDto = new BankReceiptSearchReqDto();
		bankReceiptReqDto.setCustId(1);
		PageResult<BankReceiptResDto> pageResult = bankReceiptService.queryBankReceiptResultsByCon(bankReceiptReqDto);
		System.out.println(pageResult.getTotal());
	}

	@Test
	public void createBankReceipt() {
		/*BankReceipt bankReceipt = new BankReceipt();
		bankReceipt.setCustId(1);
		bankReceipt.setBusiUnit(2);
		bankReceipt.setProjectId(3);
		bankReceipt.setBankReceiptNo("123");
		bankReceipt.setRecAccountNo("789455");
		bankReceipt.setReceiptAmount(new BigDecimal("63"));
		bankReceipt.setReceiptNo("11111");
		bankReceipt.setReceiptDate("2016-11-05");
		bankReceipt.setCurrencyType(1);
		bankReceipt.setCreator("张三");
		bankReceipt.setCreatorId(2);
		bankReceiptService.createBankReceipt(bankReceipt);*/

	}

	@Test
	public void editBankReceiptById() {
		BankReceipt bankReceipt = new BankReceipt();

		bankReceipt.setDiffAmount(new BigDecimal("0.22"));
		bankReceipt.setReceiptNo("123123");

		/*bankReceipt.setBankReceiptNo("123");
		bankReceipt.setReceiptDate("2016-11-05");
		bankReceipt.setRecAccountNo("789455");
		bankReceipt.setReceiptAmount(new BigDecimal("63"));
		bankReceipt.setCurrencyType(1);
		bankReceipt.setPreRecAmount(new BigDecimal("33"));
		bankReceiptService.updateBankReceiptById(bankReceipt);*/

	}

	@Test
	public void submitBankReceiptById() {
		BankReceipt bankReceipt = new BankReceipt();
		bankReceipt.setId(1);
		bankReceiptService.submitBankReceiptById(bankReceipt);

	}

	@Test
	public void submitBankReceiptByState() {
		BankReceipt bankReceipt = new BankReceipt();
		bankReceipt.setId(1);
		;
		bankReceiptService.submitBankReceiptByState(bankReceipt);

	}
}
