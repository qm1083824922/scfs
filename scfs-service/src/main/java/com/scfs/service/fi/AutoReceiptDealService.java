package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.fi.AdvanceReceiptRelDao;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.RecReceiptRelDao;
import com.scfs.dao.fi.ReceiveDao;
import com.scfs.dao.fi.VlReceiptRelDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.project.ProjectPoolFundDao;
import com.scfs.domain.fee.dto.req.QueryFeeReqDto;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelReqDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelSearchReqDto;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.req.VlReceiptRelSearchReqDto;
import com.scfs.domain.fi.entity.AdvanceReceiptRel;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.fi.entity.VlReceiptRel;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.entity.PayFeeRelationModel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.project.dto.req.ProjectPoolDtlSearchReqDto;
import com.scfs.domain.project.entity.ProjectPoolFund;
import com.scfs.service.bookkeeping.FeeKeepingService;
import com.scfs.service.bookkeeping.ReceiptBookkeepingService;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.pay.PayFeeRelationService;
import com.scfs.service.pay.PayService;
import com.scfs.service.project.ProjectPoolService;

/**
 * <pre>
 * 
 *  File: AutoReceiptDealService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月1日				Administrator
 *
 * </pre>
 */

@Service
public class AutoReceiptDealService {
	@Autowired
	BankReceiptDao bankReceiptDao;
	@Autowired
	ReceiveDao receiveDao;
	@Autowired
	RecReceiptRelService recReceiptRelService;
	@Autowired
	BankReceiptService bankReceiptService;
	@Autowired
	BillOutStoreService billOutStoreService;
	@Autowired
	BillOutStoreDao billOutStoreDao;
	@Autowired
	VlReceiptRelDao vlReceiptRelDao;
	@Autowired
	ProjectPoolService projectPoolService;
	@Autowired
	AdvanceReceiptRelDao advanceReceiptRelDao;
	@Autowired
	ProjectPoolFundDao projectPoolFundDao;
	@Autowired
	PayOrderDao payOrderDao;
	@Autowired
	PayFeeRelationService payFeeRelationService;
	@Autowired
	ReceiptBookkeepingService receiptBookkeepingService;
	@Autowired
	FeeServiceImpl feeServiceImpl;
	@Autowired
	FeeDao feeDao;
	@Autowired
	FeeKeepingService feeKeepingService;
	@Autowired
	RecLineService recLineService;
	@Autowired
	ReceiveService receiveService;
	@Autowired
	RecReceiptRelDao recReceiptRelDao;
	@Autowired
	PayService payService;

	/**
	 * 水单自动核销应收 TODO.
	 *
	 * @param bankReceiptSearchReqDto
	 */
	public void autoReceiptDeal(BankReceiptSearchReqDto bankReceiptSearchReqDto) {
		bankReceiptSearchReqDto.setState(BaseConsts.TWO); // 待核销
		List<BankReceipt> bankReceipts = bankReceiptDao.queryResultsByCon(bankReceiptSearchReqDto);
		if (!CollectionUtils.isEmpty(bankReceipts)) {
			for (BankReceipt bankReceipt : bankReceipts) {
				autoVerificateReceipt(bankReceipt, false);
			}
		}
	}

	public boolean autoVerificateReceipt(BankReceipt bankReceipt, boolean autoRefresh) {
		boolean isSuccess = true;
		BigDecimal amountSum = DecimalUtil.add(
				null == bankReceipt.getReceiptAmount() ? BigDecimal.ZERO : bankReceipt.getReceiptAmount(),
				null == bankReceipt.getDiffAmount() ? BigDecimal.ZERO : bankReceipt.getDiffAmount());
		BigDecimal preSum = DecimalUtil.add(
				null == bankReceipt.getWriteOffAmount() ? BigDecimal.ZERO : bankReceipt.getWriteOffAmount(),
				null == bankReceipt.getPreRecAmount() ? BigDecimal.ZERO : bankReceipt.getPreRecAmount());
		BigDecimal receiptBalance = DecimalUtil.formatScale2(DecimalUtil.subtract(amountSum, preSum));// 获取可核销金额

		ReceiveSearchReqDto receiveSearchReqDto = new ReceiveSearchReqDto();
		receiveSearchReqDto.setProjectId(bankReceipt.getProjectId());
		receiveSearchReqDto.setCustId(bankReceipt.getCustId());
		receiveSearchReqDto.setBusiUnit(bankReceipt.getBusiUnit());
		receiveSearchReqDto.setCurrencyType(bankReceipt.getCurrencyType());
		receiveSearchReqDto.setOrderType(BaseConsts.TWO);
		receiveSearchReqDto.setSearchType(BaseConsts.FOUR);
		if (autoRefresh == true) {
			receiveSearchReqDto.setOutStoreId(bankReceipt.getOutStoreId());
			receiveSearchReqDto.setFeeId(bankReceipt.getFeeId());
		}
		List<Receive> receives = receiveDao.queryResultsByConNoUser(receiveSearchReqDto);

		if (!CollectionUtils.isEmpty(receives)) {
			RecReceiptRelReqDto recReceiptRelReqDto = new RecReceiptRelReqDto();
			List<RecReceiptRel> receiptRels = new ArrayList<RecReceiptRel>();
			for (Receive receive : receives) {
				if (DecimalUtil.le(receiptBalance, DecimalUtil.ZERO)) {
					break;
				}
				RecReceiptRel receiptRel = new RecReceiptRel();
				BigDecimal recBalance = DecimalUtil
						.formatScale2(DecimalUtil.subtract(receive.getAmountReceivable(), receive.getAmountReceived()));
				BigDecimal writeOffAmount = null;
				if (DecimalUtil.gt(receiptBalance, recBalance)) {
					writeOffAmount = recBalance;
					receiptBalance = DecimalUtil.formatScale2(DecimalUtil.subtract(receiptBalance, recBalance));
				} else {
					writeOffAmount = receiptBalance;
					receiptBalance = DecimalUtil.ZERO;
				}
				receiptRel.setRecId(receive.getId());
				receiptRel.setWriteOffAmount(writeOffAmount);
				receiptRels.add(receiptRel);
			}

			recReceiptRelReqDto.setRelList(receiptRels);
			recReceiptRelReqDto.setReceiptId(bankReceipt.getId());

			recReceiptRelService.createRecReceiptRel(recReceiptRelReqDto);

			BankReceipt newBankReceipt = bankReceiptDao.queryEntityById(bankReceipt.getId());
			BigDecimal moeny = DecimalUtil.add(newBankReceipt.getReceiptAmount(), newBankReceipt.getDiffAmount());// 获取水单金额
			// 获取预收总额
			BigDecimal advanceMoney = newBankReceipt.getPreRecAmount();
			// 获取应收总额
			BigDecimal accountMoney = newBankReceipt.getWriteOffAmount();
			// 判断金额是否相等
			if (DecimalUtil.eq(moeny, DecimalUtil.add(accountMoney, advanceMoney))) {
				bankReceiptService.submitBankReceiptByState(bankReceipt);
			}
		} else {
			isSuccess = false;
		}
		return isSuccess;
	}

	public void autoSend(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		billOutStoreSearchReqDto.setStatus(BaseConsts.FOUR);
		List<BillOutStore> billOutStores = billOutStoreDao.queryResultsByConNoUser(billOutStoreSearchReqDto);
		if (!CollectionUtils.isEmpty(billOutStores)) {
			for (BillOutStore billOutStore : billOutStores) {
				if (billOutStore.getIsDelete() == BaseConsts.ZERO) {
					billOutStoreService.sendBillOutStore(billOutStore);
				}
			}
		}
	}

	/**
	 * 回滚已完成状态的费用 TODO.
	 *
	 * @param queryFeeReqDto
	 */
	public void rollbackFee(QueryFeeReqDto queryFeeReqDto) {
		List<Fee> fees = feeDao.queryFeeByCond(queryFeeReqDto);
		if (CollectionUtils.isEmpty(fees)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用单不存在");
		} else if (fees.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用单不唯一");
		}
		Fee fee = fees.get(0);
		// 1.更新费用单状态为待提交
		Fee feeUpd = new Fee();
		feeUpd.setId(fee.getId());
		feeUpd.setState(BaseConsts.ONE);
		feeDao.updateById(feeUpd);
		// 2.删除应收
		if (fee.getFeeType().equals(BaseConsts.ONE) || fee.getFeeType().equals(BaseConsts.THREE)) {
			List<Receive> receives = receiveDao.queryListByBillNo(BaseConsts.ONE, fee.getFeeNo());
			for (Receive receive : receives) {
				RecReceiptRelSearchReqDto reqDto = new RecReceiptRelSearchReqDto();
				reqDto.setRecId(receive.getId());
				List<RecReceiptRel> receiptRels = recReceiptRelService.queryListByCon(reqDto);
				if (!CollectionUtils.isEmpty(receiptRels)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用单生成的应收【" + receive.getId() + "】已被核销，暂不支持撤销");
				}
				receiveService.deleteReceiveById(receive.getId());
			}
		}
		// 3.删除费用单凭证
		feeKeepingService.rollbackFeeBookkeeping(fee.getId());

	}

	/**
	 * 根据水单编号回滚已核完水单 TODO.
	 *
	 * @param receiptNo
	 * 
	 */
	public void rollbackSubmitOver(BankReceiptSearchReqDto req) {
		List<BankReceipt> bankReceiptList = bankReceiptDao.queryResultsByCon(req);
		if (CollectionUtils.isEmpty(bankReceiptList)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单不存在");
		} else if (bankReceiptList.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单不唯一");
		}
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(bankReceiptList.get(0).getId());
		if (bankReceipt.getState() != BaseConsts.THREE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单状态有误，无法回滚核完操作");
		}
		AdvanceSearchReqDto advanceSearchReqDto = new AdvanceSearchReqDto();
		advanceSearchReqDto.setReceiptId(bankReceipt.getId());
		List<AdvanceReceiptRel> advanceRels = advanceReceiptRelDao.queryResultsByCon(advanceSearchReqDto);
		if (!CollectionUtils.isEmpty(advanceRels)) {
			for (AdvanceReceiptRel advanceReceiptRel : advanceRels) {
				BankReceiptSearchReqDto bankReceiptSearchReqDto = new BankReceiptSearchReqDto();
				bankReceiptSearchReqDto
						.setPid(bankReceipt.getPid() == null ? bankReceipt.getId() : bankReceipt.getPid());
				if (advanceReceiptRel.getAdvanceType().equals(BaseConsts.ONE)) {
					bankReceiptSearchReqDto.setReceiptType(BaseConsts.TWO);
				} else if (advanceReceiptRel.getAdvanceType().equals(BaseConsts.TWO)) {
					bankReceiptSearchReqDto.setReceiptType(BaseConsts.THREE);
				}
				bankReceiptSearchReqDto.setState(BaseConsts.TWO);
				bankReceiptSearchReqDto.setReceiptAmount(advanceReceiptRel.getExchangeAmount());
				List<BankReceipt> bankReceipts = bankReceiptDao.queryResultsByCon(bankReceiptSearchReqDto);
				if (CollectionUtils.isEmpty(bankReceipts)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"水单【" + bankReceipt.getReceiptNo() + "】找不到核完时生成的预收水单记录，请核查");
				}
				bankReceiptService.deleteBankReceiptById(bankReceipts.get(0));
			}
		}
		BankReceipt brUpd = new BankReceipt();
		brUpd.setId(bankReceipt.getId());
		brUpd.setState(BaseConsts.TWO);
		bankReceiptDao.updateById(brUpd);
		receiptBookkeepingService.rollbackReceiptBookkeeping(bankReceipt.getId()); // 回滚水单记账
		boolean flag = false;
		// 回滚资金明细
		if (bankReceipt.getReceiptType().equals(BaseConsts.ONE)) { // 回款类型的水单
			// 生成资金明细
			// (转定金和核销金额入资金池)
			if (!CollectionUtils.isEmpty(advanceRels)) {
				for (AdvanceReceiptRel advanceReceiptRel : advanceRels) {
					if (advanceReceiptRel.getAdvanceType().equals(BaseConsts.ONE)) {
						flag = true;
					}
				}
			}
			if (!DecimalUtil.eq(bankReceipt.getWriteOffAmount(), DecimalUtil.ZERO)) {
				rollbackFundBack(bankReceipt);
				flag = true;
			}
		} else if (bankReceipt.getReceiptType().equals(BaseConsts.THREE)) {
			if (!DecimalUtil.eq(bankReceipt.getWriteOffAmount(), DecimalUtil.ZERO)) { // 货款类型的水单，核销金额入池
				rollbackFundBack(bankReceipt);
				flag = true;
			}
		}
		if (flag) {
			ProjectPoolDtlSearchReqDto projectPoolDtlSearchReqDto = new ProjectPoolDtlSearchReqDto();
			projectPoolDtlSearchReqDto.setProjectId(bankReceipt.getProjectId());
			projectPoolDtlSearchReqDto.setBillNo(bankReceipt.getReceiptNo());
			List<ProjectPoolFund> projectPoolFunds = projectPoolFundDao.selectByCon(projectPoolDtlSearchReqDto);
			if (CollectionUtils.isEmpty(projectPoolFunds)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金明细查询失败");
			}
			for (ProjectPoolFund item : projectPoolFunds) {
				projectPoolFundDao.deleteById(item.getId());
			}
			projectPoolService.updateProjectPoolInfo(bankReceipt.getProjectId());
		}
	}

	/**
	 * 清理资金明细数据， 根据付款单和水单重新生成 TODO.
	 *
	 */
	public void autoGenaPoolFundDtl() {
		// 1:更新状态为5的出库单资金回款金额为0
		BillOutStoreSearchReqDto billOutStoreSearchReqDto = new BillOutStoreSearchReqDto();
		billOutStoreSearchReqDto.setStatus(BaseConsts.FIVE);
		List<BillOutStore> billOutStores = billOutStoreDao.queryAndLockResultsByCon(billOutStoreSearchReqDto);
		Set<Integer> billOutStoreIds = new HashSet<Integer>();
		for (BillOutStore billOutStore : billOutStores) {
			BillOutStore billOutStoreUpd = new BillOutStore();
			billOutStoreUpd.setId(billOutStore.getId());
			billOutStoreUpd.setFundBackAmount(DecimalUtil.ZERO);
			billOutStoreDao.updateById(billOutStoreUpd);
			billOutStoreIds.add(billOutStore.getId());
		}
		// 2.查询水单类型为1回款2定金3货款 且状态为3已完成的水单
		BankReceiptSearchReqDto bankReceiptSearchReqDto = new BankReceiptSearchReqDto();
		bankReceiptSearchReqDto.setState(BaseConsts.THREE);
		List<Integer> receiptTypeList = new ArrayList<Integer>();
		receiptTypeList.add(BaseConsts.ONE);
		receiptTypeList.add(BaseConsts.TWO);
		receiptTypeList.add(BaseConsts.THREE);
		bankReceiptSearchReqDto.setReceiptTypeList(receiptTypeList);
		List<BankReceipt> bankReceipts = bankReceiptDao.queryResultsByCon(bankReceiptSearchReqDto);
		for (BankReceipt bankReceipt : bankReceipts) {
			ProjectPoolDtlSearchReqDto poolDtlSearchReqDto = new ProjectPoolDtlSearchReqDto();
			poolDtlSearchReqDto.setBillNo(bankReceipt.getReceiptNo());
			poolDtlSearchReqDto.setProjectId(bankReceipt.getProjectId());
			List<ProjectPoolFund> projectPoolFunds = projectPoolFundDao.selectByCon(poolDtlSearchReqDto);
			for (ProjectPoolFund temp : projectPoolFunds) {
				projectPoolFundDao.deleteById(temp.getId());
			}
		}
		for (BankReceipt bankReceipt : bankReceipts) {
			BigDecimal moeny = DecimalUtil.add(bankReceipt.getReceiptAmount(), bankReceipt.getDiffAmount());// 获取水单金额
			// 获取预收总额
			BigDecimal advanceMoney = bankReceipt.getPreRecAmount();
			// 获取应收总额
			BigDecimal accountMoney = bankReceipt.getWriteOffAmount();
			// 判断金额是否相等
			if (DecimalUtil.eq(moeny, DecimalUtil.add(accountMoney, advanceMoney))) {
				// 删除资金明细
				if (bankReceipt.getReceiptType().equals(BaseConsts.ONE)
						|| bankReceipt.getReceiptType().equals(BaseConsts.TWO)
						|| bankReceipt.getReceiptType().equals(BaseConsts.THREE)
						|| bankReceipt.getReceiptType().equals(BaseConsts.SIX)) { // 1
																					// 回款
																					// 2
																					// 预收定金
																					// 3
																					// 预收货款
					BigDecimal amount = BigDecimal.ZERO;
					amount = recReceiptRelDao.queryFundUsedByReceiptId(bankReceipt.getId());
					ProjectPoolFund ppf = convertToPPf(bankReceipt, amount, BaseConsts.THREE, null, null, null);
					if (bankReceipt.getReceiptType().equals(BaseConsts.SIX)) { // 6-虚拟水单
						projectPoolService.addProjectPoolFund(ppf, BaseConsts.FOUR);
					} else {
						projectPoolService.addProjectPoolFund(ppf, BaseConsts.THREE);
					}
					projectPoolService.updateProjectPoolInfo(bankReceipt.getProjectId());
				}
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单id【" + bankReceipt.getId() + "】水单金额【" + moeny
						+ "】预收金额【" + advanceMoney.doubleValue() + "】应收金额【" + accountMoney.doubleValue() + "】");
			}
		}

		// 3.查询状态为6已完成的付款单
		PayOrderSearchReqDto payOrderSearchReqDto = new PayOrderSearchReqDto();
		payOrderSearchReqDto.setState(BaseConsts.SIX);
		List<PayOrder> payOrders = payOrderDao.queryResultsByCon(payOrderSearchReqDto);
		for (PayOrder payOrder : payOrders) {
			ProjectPoolDtlSearchReqDto poolDtlSearchReqDto = new ProjectPoolDtlSearchReqDto();
			poolDtlSearchReqDto.setBillNo(payOrder.getPayNo());
			poolDtlSearchReqDto.setProjectId(payOrder.getProjectId());
			List<ProjectPoolFund> projectPoolFunds = projectPoolFundDao.selectByCon(poolDtlSearchReqDto);
			for (ProjectPoolFund temp : projectPoolFunds) {
				projectPoolFundDao.deleteById(temp.getId());
			}
			if (payOrder.getPayType() == BaseConsts.ONE) {
				payService.createProjectPool(payOrder, BigDecimal.ZERO);
			} else if (payOrder.getPayType() == BaseConsts.TWO) {
				PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
				payFeeRelationReqDto.setPayId(payOrder.getId());
				List<PayFeeRelationModel> ll = payFeeRelationService.queryPayFeeRelatioByCon(payFeeRelationReqDto);
				BigDecimal mm = DecimalUtil.ZERO;
				for (int i = 0; i < ll.size(); i++) {
					PayFeeRelationModel lv = ll.get(i);
					// 只有应收应付费用入池
					if (lv.getFeeType() == BaseConsts.THREE) {
						mm = DecimalUtil
								.formatScale2(DecimalUtil.add(mm, payOrder.getPayRate().multiply(lv.getPayAmount())));
					}
				}
				if (mm.compareTo(DecimalUtil.ZERO) != 0) {
					payService.createProjectPool(payOrder, mm);
				}
			}
		}
	}

	private void rollbackFundBack(BankReceipt bankReceipt) {
		VlReceiptRelSearchReqDto vlReceiptRelSearchReqDto = new VlReceiptRelSearchReqDto();
		vlReceiptRelSearchReqDto.setReceiptId(bankReceipt.getId());

		List<VlReceiptRel> vlReceiptRels = vlReceiptRelDao.queryRecustsByCon(vlReceiptRelSearchReqDto);
		if (!CollectionUtils.isEmpty(vlReceiptRels)) {
			for (VlReceiptRel vlReceiptRel : vlReceiptRels) {
				if (!DecimalUtil.eq(vlReceiptRel.getWriteOffAmount(), DecimalUtil.ZERO)) {
					if (vlReceiptRel.getBillType().equals(BaseConsts.THREE)
							&& !StringUtils.isEmpty(vlReceiptRel.getOutStoreId())) {
						BillOutStore billOutStore = billOutStoreDao
								.queryAndLockEntityById(vlReceiptRel.getOutStoreId());
						ProjectPoolDtlSearchReqDto projectPoolDtlSearchReqDto = new ProjectPoolDtlSearchReqDto();
						projectPoolDtlSearchReqDto.setProjectId(bankReceipt.getProjectId());
						projectPoolDtlSearchReqDto.setBillNo(bankReceipt.getReceiptNo());
						projectPoolDtlSearchReqDto.setOutStoreId(vlReceiptRel.getOutStoreId());
						List<ProjectPoolFund> projectPoolFunds = projectPoolFundDao
								.selectByCon(projectPoolDtlSearchReqDto);
						if (CollectionUtils.isEmpty(projectPoolFunds)) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "资金明细查询失败");
						}
						if (projectPoolFunds.size() > 1) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
									"出库单【" + billOutStore.getBillNo() + "】资金明细不唯一");
						}
						BillOutStore billOutStoreUpd = new BillOutStore();
						billOutStoreUpd.setId(billOutStore.getId());
						billOutStoreUpd.setFundBackAmount(DecimalUtil.formatScale2(DecimalUtil
								.subtract(billOutStore.getFundBackAmount(), projectPoolFunds.get(0).getBillAmount())));
						billOutStoreDao.updateById(billOutStoreUpd);
					}
				}
			}
		}
	}

	private ProjectPoolFund convertToPPf(BankReceipt bankReceipt, BigDecimal amount, int fundClass, Integer billType,
			Integer billId, String assistBillNo) {
		ProjectPoolFund ppf = new ProjectPoolFund();
		ppf.setType(BaseConsts.TWO);
		ppf.setBillSource(BaseConsts.TWO);
		ppf.setProjectId(bankReceipt.getProjectId());
		ppf.setCustomerId(bankReceipt.getCustId());
		ppf.setBusinessDate(bankReceipt.getReceiptDate());
		ppf.setBillAmount(amount);
		ppf.setBillCurrencyType(bankReceipt.getCurrencyType());
		// ppf.setFundClass(fundClass);
		ppf.setBillType(billType);
		ppf.setAssistBillNo(assistBillNo);
		if (!StringUtils.isEmpty(billType)) {
			switch (billType) {
			case BaseConsts.THREE:
				ppf.setOutStoreId(billId);
				break;
			case BaseConsts.ONE:
				ppf.setFeeId(billId);
				break;
			default:
				break;
			}
		}
		return ppf;
	}
}
