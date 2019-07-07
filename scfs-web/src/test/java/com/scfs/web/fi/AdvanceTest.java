package com.scfs.web.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.dto.resp.AdvanceResDto;
import com.scfs.domain.fi.entity.AdvanceReceiptRel;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.AdvanceService;
import com.scfs.web.base.BaseJUnitTest;

public class AdvanceTest extends BaseJUnitTest {
	@Autowired
	AdvanceService advanceService;

	@Test
	public void queryAdvance() {
		AdvanceSearchReqDto advanceSearchReqDto = new AdvanceSearchReqDto();
		advanceSearchReqDto.setReceiptId(1);
		PageResult<AdvanceResDto> pageResult = advanceService.queryAdvanceRelResultsByCon(advanceSearchReqDto);
		System.out.println(pageResult.getTotal());
		if (pageResult.getItems() != null) {
			for (AdvanceResDto advanceResDto : pageResult.getItems()) {
				System.out.println(advanceResDto.getCustName());
				System.out.println(advanceResDto.getId());
			}
		}
	}

	@Test
	public void addAdvance() {
		AdvanceReceiptRel advanceSearchReqDto = new AdvanceReceiptRel();
		advanceSearchReqDto.setCustId(1);
		advanceSearchReqDto.setBusiUnit(2);
		advanceSearchReqDto.setProjectId(3);
		advanceSearchReqDto.setReceiptId(1);
		advanceSearchReqDto.setExchangeAmount(new BigDecimal("20"));
		advanceSearchReqDto.setCreator("张三");
		advanceSearchReqDto.setCreatorId(2);
		advanceService.createAdvanceRel(advanceSearchReqDto);
	}

	@Test
	public void removAllAdvance() {
		AdvanceSearchReqDto advanceSearchReqDto = new AdvanceSearchReqDto();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(1);
		List<Integer> relId = new ArrayList<Integer>();
		relId.add(1);
		advanceSearchReqDto.setIds(ids);
		advanceService.deleteAdvanceRelById(advanceSearchReqDto);
	}
}
