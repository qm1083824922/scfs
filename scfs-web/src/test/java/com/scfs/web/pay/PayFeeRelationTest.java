package com.scfs.web.pay;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.dto.resp.FeeQueryResDto;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayFeeRelationResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.pay.PayFeeRelationService;
import com.scfs.web.base.BaseJUnitTest;

public class PayFeeRelationTest extends BaseJUnitTest {

	@Autowired
	private PayFeeRelationService payFeeRelationService;

	@Test
	public void query() {
		PayFeeRelationReqDto payFeeRel = new PayFeeRelationReqDto();
		payFeeRel.setPayId(4);
		PageResult<PayFeeRelationResDto> pageResult = payFeeRelationService.queryPayFeeRelationResultsByCon(payFeeRel);
		System.out.println(pageResult.getTotal());
	}

	@Test
	public void queryFee() {
		QueryFeeReqDto queryRecPayFeeReqDto = new QueryFeeReqDto();
		PageResult<FeeQueryResDto> pageResult = payFeeRelationService.queryFeeByCond(queryRecPayFeeReqDto);
		System.out.println(pageResult.getTotal());
	}
}
