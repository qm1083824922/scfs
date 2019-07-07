package com.scfs.web.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.domain.BaseReqDto;
import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.resp.RecLineResDto;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.fi.entity.RecDetail;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.result.PageResult;
import com.scfs.service.fi.RecLineService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.fi.VoucherLineService;
import com.scfs.web.base.BaseJUnitTest;

/**
 * <pre>
 * 
 *  File: ReceiveServiceTest.java
 *  Description:            应收管理服务测试
 *  TODO
 *  Date,					Who,				
 *  2016年11月2日				Administrator
 *
 * </pre>
 */
public class ReceiveServiceTest extends BaseJUnitTest {
	@Autowired
	ReceiveService receiveService;

	@Autowired
	RecLineService recLineService;

	@Autowired
	VoucherLineService voucherLineService;

	// @Test
	public void queryReceiveResultsByCon() {
		ReceiveSearchReqDto req = new ReceiveSearchReqDto();
		req.setCustId(1);
		req.setProjectId(1);
		// req.setBillNo("");
		req.setBusiUnit(1);
		// req.setStartBillDate("2016-01-01");
		// req.setEndBillDate("2016-12-12");
		PageResult<ReceiveResDto> pageResult = receiveService.queryResultsByCon(req);
		if (pageResult != null) {
			System.out.println("应收管理分页查询成功");
		}
	}

	// @Test
	public void queryRecLineResultsByRecId() {
		RecLineSearchReqDto req = new RecLineSearchReqDto();
		req.setRecId(8);
		PageResult<RecLineResDto> pageResult = recLineService.queryResultsByRecId(req);
		if (pageResult != null) {
			System.out.println("应收明细查询成功");
		}
	}

	// @Test
	public void editRecLinesByRecId() {
		RecLineSearchReqDto req = new RecLineSearchReqDto();
		req.setRecId(1);
		PageResult<RecLineResDto> pageResult = recLineService.queryResultsByRecId(req);
		if (pageResult != null) {
			System.out.println("应收明细查询成功");
		}
	}

	/**
	 * 批量删除 TODO.
	 *
	 */
	// @Test
	public void batchDeleteRecLines() {
		BaseReqDto baseReqDto = new BaseReqDto();
		List<Integer> ids = new ArrayList<Integer>();
		ids.add(9);
		ids.add(10);
		baseReqDto.setIds(ids);
		baseReqDto.setId(8);
		try {
			recLineService.batchDeleteRecLineById(baseReqDto);
			System.out.println("批量删除应收明细成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量修改 TODO.
	 *
	 */
	// @Test
	public void updateRecLines() {
		List<RecLine> recLines = new ArrayList<RecLine>();
		RecDetail recDetail = new RecDetail();
		Receive receive = new Receive();
		receive.setId(9);
		for (int i = 10; i < 12; i++) {
			RecLine recLine = new RecLine();
			recLine.setId(i + 1);
			recLine.setAmountCheck(new BigDecimal(5));
			recLines.add(recLine);
		}
		recDetail.setReceive(receive);
		recDetail.setRecLines(recLines);
		recLineService.batchUpdateRecLineById(recDetail);
	}

	@Test
	public void allocateAmount() {

	}

	@Test
	public void updateRec() {
		Receive receive = new Receive();
		receive.setId(8);
		receive.setAmountReceived(new BigDecimal("20"));
		receiveService.updateReceiveById(receive);
	}
}
