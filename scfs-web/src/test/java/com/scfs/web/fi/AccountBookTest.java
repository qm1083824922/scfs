package com.scfs.web.fi;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.AccountBookSearchReqDto;
import com.scfs.domain.fi.dto.resp.AccountBookResDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.AccountBookService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * <pre>
 * 
 *  File: AccountBookTest.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2016年10月19日         Administrator
 *
 * </pre>
 */
public class AccountBookTest extends BaseJUnitTest {

	@Autowired
	AccountBookService accountBookService;

	@Test
	public void addAccountBook() {
		AccountBook accountBook = new AccountBook();
		accountBook.setAccountBookName("环球4");
		accountBook.setBusiUnit(277);
		accountBook.setFiNo("fi_004");
		BaseResult baseResult = accountBookService.createAccountBook(accountBook);
		if (baseResult.isSuccess()) {
			System.out.println("success");
		}
	}

	// @Test
	public void queryAccountBook() {
		AccountBookSearchReqDto req = new AccountBookSearchReqDto();
		req.setAccountBookName("环球");
		req.setBusiUnit(1);
		PageResult<AccountBookResDto> result = accountBookService.queryAccountBookByCond(req);
		if (result != null) {
			System.out.println("success");
		}
	}

	// @Test
	public void updateAccountBook() {
		AccountBook accountBook = new AccountBook();
		accountBook.setAccountBookName("环球2");
		accountBook.setFiNo("fi_002");
		accountBook.setId(1);
		BaseResult baseResult = accountBookService.updateAccountBookById(accountBook);
		if (baseResult.isSuccess()) {
			System.out.println("success");
		}
	}

}
