package com.scfs.web.pay;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayPoRelationResDto;
import com.scfs.domain.pay.entity.PayPoRelation;
import com.scfs.domain.result.PageResult;
import com.scfs.service.pay.PayPoRelationService;
import com.scfs.web.base.BaseJUnitTest;

public class PayPoRelationTest extends BaseJUnitTest {
	@Autowired
	private PayPoRelationService payPoRelationService;

	@Test
	public void query() {
		PayPoRelationReqDto payReqDto = new PayPoRelationReqDto();
		PageResult<PayPoRelationResDto> result = payPoRelationService.queryPayPoRelationResultsByCon(payReqDto);
		System.out.println(result.getTotal());
	}

	@Test
	public void queryPoTitles() {

	}

	@Test
	public void add() {
		PayPoRelationReqDto record = new PayPoRelationReqDto();
		record.setPayId(4);
		List<PayPoRelation> relList = new ArrayList<PayPoRelation>();
		PayPoRelation record1 = new PayPoRelation();
		record1.setPoId(1);
		record1.setPayAmount(new BigDecimal(50));
		relList.add(record1);
		
		record.setRelList(relList);
		payPoRelationService.createPayPoRelation(record);
	}
}
