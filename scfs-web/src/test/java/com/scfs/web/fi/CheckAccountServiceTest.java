package com.scfs.web.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.scfs.common.exception.BaseException;
import com.scfs.domain.fi.dto.req.VoucherLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.VoucherLineResDto;
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
 *  File: CheckAccountServiceTest.java
 *  Description:            对账管理功能service测试
 *  TODO
 *  Date,					Who,				
 *  2016年11月2日				Administrator
 *
 * </pre>
 */
public class CheckAccountServiceTest extends BaseJUnitTest {
	@Autowired
	VoucherLineService voucherLineService;

	@Autowired
	ReceiveService receiveService;

	@Autowired
	RecLineService recLineService;

	/**
	 * 执行对账操作，插入应收明细表及应收表 TODO.
	 *
	 */
	// @Test
	public void exeCheckAccount() {
		RecDetail recDetail = new RecDetail();
		Receive receive = new Receive();
		receive.setCustId(1);
		receive.setProjectId(1);
		receive.setBusiUnit(1);
		receive.setCheckDate(new Date());
		receive.setCheckNote("收钱啦");
		receive.setCurrencyType(1);
		List<RecLine> recLines = new ArrayList<RecLine>();
		for (int i = 0; i < 2; i++) {
			RecLine recLine = new RecLine();
			recLine.setAmountCheck(new BigDecimal(42));
			recLine.setVoucherLineId(i + 1);
			recLine.setCurrencyType(1);
			recLines.add(recLine);
		}
		recDetail.setReceive(receive);
		recDetail.setRecLines(recLines);
		try {
			receiveService.createRecDetail(recDetail);
			System.out.println("执行对账成功");
		} catch (BaseException e) {
			System.out.println(e.getMsg());
		}
	}

	/**
	 * 合并对账，将对账金额明细合并到未核销的应收里 TODO.
	 *
	 */
	@Test
	public void mergeRec() {
		RecDetail recDetail = new RecDetail();
		Receive receive = new Receive();
		// 合并的应收id
		receive.setId(8);
		List<RecLine> recLines = new ArrayList<RecLine>();
		for (int i = 0; i < 2; i++) {
			RecLine recLine = new RecLine();
			recLine.setAmountCheck(new BigDecimal(20.888));
			recLine.setVoucherLineId(i + 1);
			recLine.setCurrencyType(1);
			recLine.setCreator("zangdan");
			recLine.setCreatorId(1);
			recLines.add(recLine);
		}
		recDetail.setReceive(receive);
		recDetail.setRecLines(recLines);
		try {
			receiveService.mergeRec(recDetail);
			System.out.println("合并应收成功");
		} catch (BaseException e) {
			System.out.println(e.getMsg());
		}
	}

	/**
	 * 对账页面，根据项目，客户，经营单位，单据编号，单据日期查询待对账分录 TODO.
	 *
	 */
	// @Test
	public void queryVoucherLineByCon() {
		VoucherLineSearchReqDto req = new VoucherLineSearchReqDto();
		req.setCustId(1);
		req.setProjectId(1);
		// req.setBillNo("");
		req.setBusiUnit(1);
	}

	// @Test
	public void queryGroupResultsByCon() {
		VoucherLineSearchReqDto req = new VoucherLineSearchReqDto();
		req.setCustId(1);
		req.setProjectId(1);
		// req.setBillNo("");
		req.setBusiUnit(1);
		// req.setStartBillDate("2016-01-01");
		// req.setEndBillDate("2016-12-12");
		PageResult<VoucherLineResDto> pageResult = voucherLineService.queryGroupResultsByCon(req);
		if (pageResult != null) {
			System.out.println("按照项目，客户汇总对账结果查询成功");
		}
	}
}
