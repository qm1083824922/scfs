package com.scfs.web.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.dto.resp.VoucherDetailResDto;
import com.scfs.domain.fi.dto.resp.VoucherResDto;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherDetail;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fi.VoucherService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * <pre>
 * 
 *  File: VoucherTest.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月25日			Administrator
 *
 * </pre>
 */
public class VoucherTest extends BaseJUnitTest {
	@Autowired
	VoucherService voucherService;

	@Test
	public void insert() {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = new Voucher();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		voucher.setAccountBookId(1);
		voucher.setBusiUnit(1);
		voucher.setVoucherWord(1);
		voucher.setVoucherDate(new Date());
		for (int i = 0; i < 4; i++) {
			VoucherLine line = new VoucherLine();
			line.setAccountId(1);
			line.setAccountLineId(1);
			line.setAmount(new BigDecimal(1));
			line.setCustId(1);
			line.setCurrencyType(2);
			line.setDebitOrCredit(1);
			line.setExchangeRate(new BigDecimal(1));
			line.setProjectId(1);
			line.setSupplierId(1);
			line.setUserId(1);
			line.setTaxRate(new BigDecimal("1"));
			line.setVoucherLineSummary("华为 3000台");
			voucherLines.add(line);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		try {
			voucherService.createVoucherDetail(voucherDetail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void update() {
		VoucherDetail voucherDetail = new VoucherDetail();
		Voucher voucher = new Voucher();
		List<VoucherLine> voucherLines = new ArrayList<VoucherLine>();
		voucher.setId(7);
		voucher.setAccountBookId(1);
		voucher.setBusiUnit(1);
		voucher.setVoucherNo("-");
		voucher.setVoucherWord(1);

		for (int i = 0; i < 2; i++) {
			VoucherLine line = new VoucherLine();
			line.setId(1);
			line.setAmount(new BigDecimal(39.9999));
			line.setVoucherLineSummary("小米 4000台");
			voucherLines.add(line);
		}
		voucherDetail.setVoucher(voucher);
		voucherDetail.setVoucherLines(voucherLines);
		voucherService.updateVoucherDetail(voucherDetail);
	}

	// @Test
	public void detail() {
		Result<VoucherDetailResDto> result = new Result<VoucherDetailResDto>();
		VoucherDetailResDto resDto = voucherService.detailVoucherDetailById(7);
		result.setItems(resDto);
		System.out.println("success");
	}

	// @Test
	public void queryVoucherPage() {
		VoucherSearchReqDto reqDto = new VoucherSearchReqDto();
		reqDto.setAccountBookId(1);
		reqDto.setBusiUnit(1);
		reqDto.setProjectId(1);
		PageResult<VoucherResDto> pageResult = voucherService.queryVoucherResultsByCon(reqDto);
		System.out.println(pageResult);
	}

	// @Test
	public void submit() {
		voucherService.submitVoucherById(7);
	}

	// @Test
	public void add() {
		BigDecimal aBigDecimal = BigDecimal.ZERO;
		BigDecimal bDecimal = new BigDecimal(2.9);
		aBigDecimal = aBigDecimal.add(bDecimal);
		System.out.println(aBigDecimal);
	}

}
