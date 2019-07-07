package com.scfs.web.fi;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.AccountLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountLineResDto;
import com.scfs.domain.fi.entity.AccountLine;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.AccountLineService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * <pre>
 * 
 *  File: AccountLineTest.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2016年10月20日                             Administrator
 *
 * </pre>
 */
public class AccountLineTest extends BaseJUnitTest {
	@Autowired
	AccountLineService accountLineService;

	@Test
	public void addAccountLine() {
		AccountLine accountLine = new AccountLine();
		accountLine.setAccountLineLevel(3);
		accountLine.setAccountLineName("开票");
		accountLine.setAccountLineNo("KP_5008");
		accountLine.setAccountLineType(2);
		accountLine.setDebitOrCredit(1);
		accountLine.setNeedAccount(1);
		accountLine.setNeedCust(1);
		accountLine.setNeedProject(1);
		accountLine.setNeedSupplier(1);
		accountLine.setNeedTaxRate(1);
		accountLine.setNeedUser(1);
		BaseResult baseResult = accountLineService.createAccountLine(accountLine);
		if (baseResult.isSuccess()) {
			System.out.println("success");
		}
	}

	// @Test
	public void updateAccountLine() {
		AccountLine accountLine = new AccountLine();
		accountLine.setId(1);
		accountLine.setAccountLineNo("CK_1002");
		accountLine.setDebitOrCredit(2);
		BaseResult baseResult = accountLineService.updateAccountLineById(accountLine);
		if (baseResult.isSuccess()) {
			System.out.println("success");
		}
	}

	// @Test
	public void queryAccountLine() {
		AccountLineSearchReqDto dto = new AccountLineSearchReqDto();
		dto.setState(0);
		PageResult<AccountLineResDto> resDto = accountLineService.queryAccountLineByCond(dto);
		if (resDto != null) {
			System.out.println("success");
		}
	}
}
