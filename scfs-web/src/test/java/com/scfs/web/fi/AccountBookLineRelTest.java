package com.scfs.web.fi;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.fi.dto.req.AcctBookLineRelSearchReqDto;
import com.scfs.domain.fi.dto.resp.AcctBookLineRelResDto;
import com.scfs.domain.fi.entity.AccountBookLineRel;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.AccountBookLineRelService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * <pre>
 * 
 *  File: AccountBookLineRelTest.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2016年10月20日             Administrator
 *
 * </pre>
 */
public class AccountBookLineRelTest extends BaseJUnitTest {
	@Autowired
	AccountBookLineRelService accountBookLineRelService;

	@Test
	public void addRel() {
		AccountBookLineRel rel = new AccountBookLineRel();
		rel.setAccountBookId(1);
		rel.setAccountLineId(1);
		rel.setCreator("zhangdan");
		rel.setCreatorId(1);
		accountBookLineRelService.createRel(rel);

	}

	@Test
	public void queryRel() {
		AcctBookLineRelSearchReqDto reqDto = new AcctBookLineRelSearchReqDto();
		reqDto.setAccountBookId(1);
		PageResult<AcctBookLineRelResDto> resDtos = accountBookLineRelService.queryRelByBookId(reqDto);
		if (resDtos != null) {
			System.out.println("success");
		}
	}

	// @Test
	public void deleteRel() {
		accountBookLineRelService.deleteRel(1);
	}

}
