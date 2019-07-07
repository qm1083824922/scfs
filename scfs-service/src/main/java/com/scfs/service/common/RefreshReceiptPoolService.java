package com.scfs.service.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.ReceiptPoolAssestDao;
import com.scfs.dao.fi.ReceiptPoolDao;
import com.scfs.dao.fi.ReceiptPoolDtlDao;
import com.scfs.dao.fi.ReceiptPoolFundDtlDao;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.service.fi.ReceiptFundPoolService;
import com.scfs.service.fi.ReceiptPoolAssestService;
import com.scfs.service.fi.ReceiptPoolService;

/**
 * Created by Administrator on 2018年08月29日.
 */
@Service
public class RefreshReceiptPoolService {

	@Autowired
	private BankReceiptDao bankReceiptDao;
	@Autowired
	private ReceiptPoolService receiptPoolService;
	@Autowired
	private ReceiptFundPoolService receiptFundPoolService;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private ReceiptPoolAssestService poolAssestService;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private ReceiptPoolDao receiptPoolDao;
	@Autowired
	private ReceiptPoolFundDtlDao receiptPoolFundDtlDao;
	@Autowired
	private ReceiptPoolAssestDao receiptPoolAssestDao;
	@Autowired
	private ReceiptPoolDtlDao receiptPoolDtlDao;

	/**
	 * 刷新资金池的数据
	 */
	public void refreshReceiptPool() {
		this.deleteReceiptPool();
		/** 刷新水单数据 **/
		this.refreshBankReceipt();
		// 刷新出库单数据
		this.refreshBillOut();
		// 刷新入库单数据
		this.refreshBillIn();
		// 刷新付款单
		this.refreshPayOrder();
	}

	/**
	 * 清除资金池数据
	 */
	public void deleteReceiptPool() {
		receiptPoolAssestDao.deleteAllPoolAssest();
		receiptPoolFundDtlDao.deleteAllPoolFund();
		receiptPoolDtlDao.deleteAllPoolDtl();
		receiptPoolDao.deleteAllReceiptPool();
	}

	/**
	 * 刷新水单的资金池数据
	 */
	private void refreshBankReceipt() {
		List<BankReceipt> bankReceipts = bankReceiptDao.queryRefreshReceiptPoolResults();
		if (!CollectionUtils.isEmpty(bankReceipts)) {
			FundPoolReqDto fundPoolReqDto = new FundPoolReqDto();
			for (BankReceipt bankReceipt : bankReceipts) {
				if (bankReceipt.getReceiptType().equals(BaseConsts.FIVE)) {
					fundPoolReqDto.setId(bankReceipt.getId());
					receiptPoolService.createReceiptPoolByCon(fundPoolReqDto);
				} else {
					fundPoolReqDto.setId(bankReceipt.getId());
					receiptFundPoolService.createPoolFundByCon(fundPoolReqDto);
				}
			}
		}
	}

	/**
	 * 刷新出库单销售类型的数据
	 */
	private void refreshBillOut() {
		List<BillOutStore> billOutStores = billOutStoreDao.queryRefreshReceiptPoolResults();
		if (!CollectionUtils.isEmpty(billOutStores)) {
			for (BillOutStore billOutStore : billOutStores) {
				FundPoolReqDto poolReqDto = new FundPoolReqDto();
				poolReqDto.setId(billOutStore.getId());
				poolAssestService.createPoolAssestOut(poolReqDto);
			}
		}
	}

	/**
	 * 刷新入库单销售类型的数据
	 */
	private void refreshBillIn() {
		List<BillInStore> billInStore = billInStoreDao.queryRefreshReceiptPoolResults();
		if (!CollectionUtils.isEmpty(billInStore)) {
			for (BillInStore billInStore2 : billInStore) {
				FundPoolReqDto poolReqDto = new FundPoolReqDto();
				poolReqDto.setId(billInStore2.getId());
				poolAssestService.createPoolAssestIn(poolReqDto);
			}
		}
	}

	/**
	 * 刷新付款单
	 */
	private void refreshPayOrder() {
		List<PayOrder> orders = payOrderDao.queryRefreshReceiptFund();
		if (!CollectionUtils.isEmpty(orders)) {
			for (PayOrder payOrder : orders) {
				FundPoolReqDto fundPoolReqDto = new FundPoolReqDto();
				fundPoolReqDto.setId(payOrder.getId());
				receiptFundPoolService.createFundPoolPayByCon(fundPoolReqDto);
			}
		}
	}
}
