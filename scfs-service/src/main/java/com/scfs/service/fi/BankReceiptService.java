package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.fee.FeeRecPayDao;
import com.scfs.dao.fi.AdvanceReceiptRelDao;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.CopeReceiptRelDao;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.fi.RecReceiptRelDao;
import com.scfs.dao.fi.VlReceiptRelDao;
import com.scfs.dao.finance.CopeManageDtlDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.VerificationAdvanceDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.pay.PayRefundRelationDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeRecPay;
import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelSearchReqDto;
import com.scfs.domain.fi.dto.resp.AdvanceResDto;
import com.scfs.domain.fi.dto.resp.BankReceipFileResDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.entity.AdvanceReceiptRel;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.finance.cope.entity.CopeManageDtl;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.project.entity.ProjectPoolFund;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.bookkeeping.ReceiptBookkeepingService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.pay.AccountPoolFundService;
import com.scfs.service.pay.PayService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: BankReceiptService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月29日			Administrator
 *
 * </pre>
 */
@Service
public class BankReceiptService {

	@Autowired
	BankReceiptDao bankReceiptDao;

	@Autowired
	AdvanceService advanceService; // 预收

	@Autowired
	RecReceiptRelService recReceiptRelService; // 应收

	@Autowired
	SequenceService sequenceService;

	@Autowired
	CacheService cacheService;

	@Autowired
	ReceiptBookkeepingService receiptBookkeepingService;

	@Autowired
	VerificationAdvanceDao verificationAdvanceDao;

	@Autowired
	AdvanceManagerService advanceManagerService;

	@Autowired
	AdvanceReceiptRelDao advanceReceiptRelDao;

	@Autowired
	RecReceiptRelDao recReceiptRelDao;

	@Autowired
	ProjectPoolService projectPoolService;

	@Autowired
	VlReceiptRelDao vlReceiptRelDao;

	@Autowired
	BillOutStoreDao billOutStoreDao;

	@Autowired
	AsyncExcelService asyncExcelService;

	@Autowired
	FileUploadService fileUploadService;

	@Autowired
	FeeDao feeDao;

	@Autowired
	PayRefundRelationDao refundRelationDao;

	@Autowired
	RecLineDao recLineDao;

	@Autowired
	BillDeliveryDao billDeliveryDao;
	@Autowired
	private ReceiptPoolService receiptPoolService;
	@Autowired
	private ReceiptFundPoolService receiptFundPoolService;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private AccountPoolFundService accountPoolFundService;// 资金池明细
	@Autowired
	PayService payService;
	@Autowired
	private CopeReceiptRelService copeReceiptRelService;
	@Autowired
	private FeeRecPayDao feeRecPayDao;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private CopeManageDtlDao copeManageDtlDao;
	@Autowired
	private CopeReceiptRelDao copeReceiptRelDao;

	/**
	 * 添加水单数据,状态为待提交
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public int createBankReceipt(BankReceipt bankReceipt) {
		Date date = new Date();
		bankReceipt.setCreateAt(date);
		bankReceipt.setCreator(ServiceSupport.getUser().getChineseName());
		bankReceipt.setCreatorId(ServiceSupport.getUser().getId());
		bankReceipt.setState(BaseConsts.ONE);
		// bankReceipt.setReceiptType(BaseConsts.ONE);// 回款水单单
		bankReceipt.setReceiptNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_RECEIPT_NO, SeqConsts.S_RECEIPT_NO,
				BaseConsts.INT_13));
		// TODO
		// 目前币种一致，以后若币种不一致，水单录入的币种和金额需放入actualCurrencyType和actualReceiptAmount，应收记录的币种和金额放入currencyType和receiptAmount
		bankReceipt.setActualReceiptAmount(bankReceipt.getReceiptAmount());
		bankReceipt.setActualWriteOffAmount(BigDecimal.ZERO);
		bankReceipt.setActualPreRecAmount(BigDecimal.ZERO);
		bankReceipt.setActualPaidAmount(BigDecimal.ZERO);
		bankReceipt.setActualDiffAmount(BigDecimal.ZERO);
		bankReceipt.setActualCurrencyType(bankReceipt.getCurrencyType());
		if (null == bankReceipt.getReceiptAmount() || DecimalUtil.eq(bankReceipt.getReceiptAmount(), BigDecimal.ZERO)) {
			bankReceipt.setActualCurrencyRate(BigDecimal.ZERO);
		} else {
			bankReceipt.setActualCurrencyRate(
					DecimalUtil.divide(bankReceipt.getActualReceiptAmount(), bankReceipt.getReceiptAmount()));
		}
		int id = bankReceiptDao.insert(bankReceipt);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(bankReceipt));
		}
		return id;
	}

	/**
	 * 添加水单数据，状态为待核销
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public Integer createPreBankReceipt(BankReceipt bankReceipt) {
		Date date = new Date();
		bankReceipt.setCreateAt(date);

		BaseProject baseProject = cacheService.getProjectById(bankReceipt.getProjectId());// 获取商务信息
		bankReceipt.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
				: cacheService.getUserByid(baseProject.getBusinessManagerId()).getChineseName());
		bankReceipt.setCreatorId(baseProject.getBusinessManagerId());

		bankReceipt.setState(BaseConsts.TWO);
		bankReceipt.setReceiptNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_RECEIPT_NO, SeqConsts.S_RECEIPT_NO,
				BaseConsts.INT_13));
		// TODO
		// 目前币种一致，以后若币种不一致，水单录入的币种和金额放入actualCurrencyType和actualReceiptAmount，应收记录的币种和金额放入currencyType和receiptAmount
		bankReceipt.setActualReceiptAmount(bankReceipt.getReceiptAmount());
		bankReceipt.setActualWriteOffAmount(BigDecimal.ZERO);
		bankReceipt.setActualPreRecAmount(BigDecimal.ZERO);
		bankReceipt.setActualPaidAmount(BigDecimal.ZERO);
		bankReceipt.setActualDiffAmount(BigDecimal.ZERO);
		bankReceipt.setActualCurrencyType(bankReceipt.getCurrencyType());
		if (null == bankReceipt.getReceiptAmount() || DecimalUtil.eq(bankReceipt.getReceiptAmount(), BigDecimal.ZERO)) {
			bankReceipt.setActualCurrencyRate(BigDecimal.ZERO);
		} else {
			bankReceipt.setActualCurrencyRate(
					DecimalUtil.divide(bankReceipt.getActualReceiptAmount(), bankReceipt.getReceiptAmount()));
		}
		int num = bankReceiptDao.insert(bankReceipt);
		if (num != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.RECEIPT_RECEIPT_UPDATE, bankReceipt.getId());
		}
		return bankReceipt.getId();
	}

	/**
	 * 更新数据，仅供controller调用
	 * 
	 * @param bankReceipt
	 * @param isCheck
	 *            是否核销
	 * @return
	 */
	public BaseResult updateById(BankReceipt bankReceipt, boolean isCheck) {
		BaseResult baseResult = new BaseResult();
		// TODO
		// 目前币种一致，以后若币种不一致，水单录入的币种和金额需放入actualCurrencyType和actualReceiptAmount，应收记录的币种和金额放入currencyType和receiptAmount
		bankReceipt.setActualReceiptAmount(bankReceipt.getReceiptAmount());
		bankReceipt.setActualCurrencyType(bankReceipt.getCurrencyType());
		bankReceipt.setActualDiffAmount(bankReceipt.getDiffAmount());
		if (isCheck == false) {
			if (null == bankReceipt.getReceiptAmount()
					|| DecimalUtil.eq(bankReceipt.getReceiptAmount(), BigDecimal.ZERO)) {
				bankReceipt.setActualCurrencyRate(BigDecimal.ZERO);
			} else {
				bankReceipt.setActualCurrencyRate(
						DecimalUtil.divide(bankReceipt.getActualReceiptAmount(), bankReceipt.getReceiptAmount()));
			}
		}
		int result = bankReceiptDao.updateById(bankReceipt);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新水单失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 更新数据
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public BaseResult updateBankReceiptById(BankReceipt bankReceipt) {
		BaseResult baseResult = new BaseResult();
		int result = bankReceiptDao.updateById(bankReceipt);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新水单失败，请重试");
		}
		return baseResult;
	}

	/***
	 * 提交
	 * 
	 * @param receipt
	 * @return
	 */
	public BaseResult submitBankReceiptById(BankReceipt bankReceipt) {
		bankReceipt.setState(BaseConsts.TWO);
		bankReceiptDao.updateById(bankReceipt);
		return new BaseResult();
	}

	/***
	 * 删除
	 * 
	 * @param receipt
	 * @return
	 */
	public BaseResult deleteBankReceiptById(BankReceipt bankReceipt) {
		bankReceipt.setIsDelete(BaseConsts.ONE);
		bankReceipt.setDeleter(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
				: ServiceSupport.getUser().getChineseName());
		bankReceipt.setDeleterId(
				ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_ID : ServiceSupport.getUser().getId());
		bankReceipt.setDeleteAt(new Date());
		bankReceiptDao.updateById(bankReceipt);
		return new BaseResult();
	}

	/***
	 * 驳回
	 * 
	 * @param receipt
	 * @return
	 */
	public BaseResult rejectBankReceiptById(BankReceipt receipt) {
		int id = receipt.getId();
		deleteRel(id);
		// 驳回应收
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(id);
		if (bankReceipt.getState() != BaseConsts.TWO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单状态有误，无法驳回");
		}
		// 判断水单类型驳回
		// if (bankReceipt.getReceiptType() == BaseConsts.ONE) {
		// receipt.setProjectId(BaseConsts.ZERO);
		// receipt.setCustId(BaseConsts.ZERO);
		receipt.setWriteOffAmount(BigDecimal.ZERO);
		receipt.setPreRecAmount(BigDecimal.ZERO);
		receipt.setDiffAmount(BigDecimal.ZERO);
		receipt.setState(BaseConsts.ONE);

		bankReceiptDao.updateById(receipt);
		// }
		// else {
		// advanceService.rejectAdvanceReceipt(bankReceipt);
		// deleteBankReceiptById(receipt);
		// }
		return new BaseResult();
	}

	/**
	 * 水单id
	 * 
	 * @param payId
	 */
	public void deleteRel(int receiptId) {
		// 删除所有关联应收数据
		RecReceiptRelSearchReqDto reqDto = new RecReceiptRelSearchReqDto();
		reqDto.setReceiptId(receiptId);
		List<RecReceiptRel> recList = recReceiptRelService.queryListByCon(reqDto);
		if (recList != null && recList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (RecReceiptRel recReceiptRel : recList) {
				ids.add(recReceiptRel.getId());
			}
			RecReceiptRelSearchReqDto recReceiptDto = new RecReceiptRelSearchReqDto();
			recReceiptDto.setIds(ids);
			recReceiptRelService.deleteRecReceiptRelById(recReceiptDto);
		}

		// 删除所有管理预收数据
		AdvanceSearchReqDto advanceReqDto = new AdvanceSearchReqDto();
		advanceReqDto.setReceiptId(receiptId);
		List<AdvanceResDto> advanceRelList = advanceService.queryAdvanceRelResultsById(advanceReqDto);
		if (advanceRelList != null && advanceRelList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (AdvanceResDto advanceRel : advanceRelList) {
				ids.add(advanceRel.getId());
			}
			AdvanceSearchReqDto advanceDto = new AdvanceSearchReqDto();
			advanceDto.setIds(ids);
			advanceService.deleteAdvanceRelById(advanceDto);
		}
	}

	/**
	 * 核完业务
	 * 
	 * @param receipt
	 * @return
	 */
	public BaseResult submitBankReceiptByState(BankReceipt receipt) {
		BaseResult baseResult = new BaseResult();
		int id = receipt.getId();
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(id);
		if (bankReceipt.getState() != BaseConsts.TWO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单状态有误，无法核完");
		}
		BankReceipt receiptEnty = new BankReceipt();
		receiptEnty.setId(id);
		receiptEnty.setState(BaseConsts.THREE);
		receiptEnty.setWriteOfftorId(ServiceSupport.getUser().getId());
		receiptEnty.setWriteOffAt(new Date());
		if (bankReceipt.getReceiptType() != BaseConsts.FOUR && bankReceipt.getReceiptType() != BaseConsts.FIVE) {// 判断是否是融资或者内部
																													// 和退款提交
			if (bankReceipt.getActualCurrencyType().equals(bankReceipt.getCurrencyType())) {
				BigDecimal money = DecimalUtil.add(bankReceipt.getReceiptAmount(), bankReceipt.getDiffAmount());// 获取水单金额
				// 获取预收总额
				BigDecimal advanceMoney = bankReceipt.getPreRecAmount();
				// 获取应收总额
				BigDecimal accountMoney = bankReceipt.getWriteOffAmount();
				// 判断金额是否相等
				if (DecimalUtil.ne(money, DecimalUtil.add(accountMoney, advanceMoney))) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"水单【" + bankReceipt.getBankReceiptNo() + "】的水单金额【" + money + "】不等于预收金额【"
									+ advanceMoney.doubleValue() + "】+应收金额【" + accountMoney.doubleValue() + "】");
				}
			}
			// 将预收转成水单
			AdvanceSearchReqDto advanceSearchReqDto = new AdvanceSearchReqDto();
			advanceSearchReqDto.setReceiptId(bankReceipt.getId());
			List<AdvanceReceiptRel> advanceRels = advanceReceiptRelDao.queryResultsByCon(advanceSearchReqDto);
			if (advanceRels != null && advanceRels.size() > BaseConsts.ZERO) {
				for (AdvanceReceiptRel advanceReceiptRel : advanceRels) {
					BankReceipt subBankReceipt = convertToBankReceipt(bankReceipt, advanceReceiptRel);
					if (bankReceipt.getReceiptType() == BaseConsts.SEVEN) {
						subBankReceipt.setReceiptType(BaseConsts.EIGHT);// 如果是供应商退款类型的核完
																		// 在转换为预收退款的数据
					}
					createPreBankReceipt(subBankReceipt);
				}
			}
			// 将付款水单转车预付款水单
			if (bankReceipt.getReceiptType() == BaseConsts.NINE) {
				this.convertToPrepaidReceipt(bankReceipt);
			}
			receiptEnty.setActualWriteOffAmount(
					DecimalUtil.multiply(bankReceipt.getWriteOffAmount(), bankReceipt.getActualCurrencyRate()));
			receiptEnty.setActualPreRecAmount(
					DecimalUtil.multiply(bankReceipt.getPreRecAmount(), bankReceipt.getActualCurrencyRate()));
			receiptEnty.setActualPaidAmount(
					DecimalUtil.multiply(bankReceipt.getPaidAmount(), bankReceipt.getActualCurrencyRate()));
			receiptEnty.setActualDiffAmount(
					DecimalUtil.multiply(bankReceipt.getDiffAmount(), bankReceipt.getActualCurrencyRate()));
			bankReceiptDao.updateById(receiptEnty);
			// 收款记账
			receiptBookkeepingService.receiptBookkeeping(receipt.getId());
			// 回写数据
			callbackData(bankReceipt);
			// 加入融资池
			createProjectPool(bankReceipt);
			/**
			 * if (bankReceipt.getReceiptType().equals(BaseConsts.ONE)) { //
			 * 回款类型的水单 // 生成资金明细 // (转定金、转货款和核销金额入资金池) if
			 * (!CollectionUtils.isEmpty(advanceRels)) { for (AdvanceReceiptRel
			 * advanceReceiptRel : advanceRels) { if
			 * (advanceReceiptRel.getAdvanceType().equals(BaseConsts.ONE)) {
			 * ProjectPoolFund ppf = convertToPPf(bankReceipt,
			 * advanceReceiptRel.getExchangeAmount(), BaseConsts.TWO, null,
			 * null, null); projectPoolService.addProjectPoolFund(ppf); } else
			 * if (advanceReceiptRel.getAdvanceType().equals(BaseConsts.TWO)) {
			 * ProjectPoolFund ppf = convertToPPf(bankReceipt,
			 * advanceReceiptRel.getExchangeAmount(), BaseConsts.THREE, null,
			 * null, null); projectPoolService.addProjectPoolFund(ppf); } } } if
			 * (!DecimalUtil.eq(bankReceipt.getWriteOffAmount(),
			 * DecimalUtil.ZERO)) { dealFundBack(bankReceipt); } } else if
			 * (bankReceipt.getReceiptType().equals(BaseConsts.THREE) ||
			 * bankReceipt.getReceiptType().equals(BaseConsts.TWO)) { if
			 * (!DecimalUtil.eq(bankReceipt.getWriteOffAmount(),
			 * DecimalUtil.ZERO)) { // 定金货款类型的水单，核销金额入池
			 * dealFundBack(bankReceipt); } }
			 **/
		} else {
			if (bankReceipt.getReceiptType() == BaseConsts.FIVE) {
				receiptBookkeepingService.receiptBookkeeping(receipt.getId());
			}
			receiptEnty.setActualWriteOffAmount(
					DecimalUtil.multiply(bankReceipt.getWriteOffAmount(), bankReceipt.getActualCurrencyRate()));
			receiptEnty.setActualPreRecAmount(
					DecimalUtil.multiply(bankReceipt.getPreRecAmount(), bankReceipt.getActualCurrencyRate()));
			receiptEnty.setActualPaidAmount(
					DecimalUtil.multiply(bankReceipt.getPaidAmount(), bankReceipt.getActualCurrencyRate()));
			receiptEnty.setActualDiffAmount(
					DecimalUtil.multiply(bankReceipt.getDiffAmount(), bankReceipt.getActualCurrencyRate()));
			bankReceiptDao.updateById(receiptEnty);
		}
		FundPoolReqDto fundPoolReqDto = new FundPoolReqDto();
		fundPoolReqDto.setId(bankReceipt.getId());
		// 当水单类型为内部 做资金池入池操作
		if (bankReceipt.getReceiptType() == BaseConsts.FIVE) { // 5水单类型为内部水单类型
			receiptPoolService.createReceiptPoolByCon(fundPoolReqDto);
			accountPoolFundService.dealBankReceipt(bankReceipt);// 添加资金池
		} else if (bankReceipt.getReceiptType() == BaseConsts.ONE || bankReceipt.getReceiptType() == BaseConsts.TWO
				|| bankReceipt.getReceiptType() == BaseConsts.THREE) {
			receiptFundPoolService.createPoolFundByCon(fundPoolReqDto);
			accountPoolFundService.dealBankReceipt(bankReceipt);// 添加资金池
		}
		return baseResult;
	}

	public void createProjectPool(BankReceipt bankReceipt) {
		if (bankReceipt.getReceiptType().equals(BaseConsts.ONE) || bankReceipt.getReceiptType().equals(BaseConsts.TWO)
				|| bankReceipt.getReceiptType().equals(BaseConsts.THREE)
				|| bankReceipt.getReceiptType().equals(BaseConsts.SIX)) { // 1
																			// 回款
																			// 2预收定金
																			// 3
																			// 预收货款6-虚拟水单
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
	}

	public void callbackData(BankReceipt bankReceipt) {
		/**
		 * 1、更新销售单回款日期
		 */
		List<RecReceiptRel> recReceiptRelList = recReceiptRelService.getRecReceiptRelByReceiptId(bankReceipt.getId());
		if (CollectionUtils.isNotEmpty(recReceiptRelList)) {
			for (RecReceiptRel recReceiptRel : recReceiptRelList) {
				Integer recId = recReceiptRel.getRecId();
				if (null != recId) {
					RecLineSearchReqDto recLineSearchReqDto = new RecLineSearchReqDto();
					recLineSearchReqDto.setRecId(recId);
					recLineSearchReqDto.setBillType(BaseConsts.THREE); // 3-出库单
					RecLine recLine = recLineDao.queryRecLineByCon(recLineSearchReqDto);
					if (null != recLine && null != recLine.getOutStoreId()) {
						BillDelivery billDelivery = billDeliveryDao
								.queryBillDeliveryByBillOutStoreId(recLine.getOutStoreId());
						if (null != billDelivery) {
							BillDelivery billDeliveryUpdate = new BillDelivery();
							billDeliveryUpdate.setId(billDelivery.getId());
							billDeliveryUpdate.setWholeReturnTime(bankReceipt.getReceiptDate());
							billDeliveryDao.updateById(billDeliveryUpdate);
						}
					}

					// 更新资金占用金额
					if (bankReceipt.getReceiptType().equals(BaseConsts.ONE)
							|| bankReceipt.getReceiptType().equals(BaseConsts.THREE)
							|| bankReceipt.getReceiptType().equals(BaseConsts.SIX)) {
						recLineSearchReqDto.setRecId(recId);
						recLineSearchReqDto.setBillType(null);
						List<RecLine> recLines = recLineDao.queryResultsRecLineByCon(recLineSearchReqDto);
						if (!CollectionUtils.isEmpty(recLines) && recLines.size() > 1) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "对应多条应收记录，请删除");
						}
						RecLine line = recLines.get(0);
						Integer outStoreId = line.getOutStoreId();
						Integer feeId = line.getFeeId();
						BigDecimal receivedAmount = BigDecimal.ZERO;
						BigDecimal fundUsed = BigDecimal.ZERO;
						if (null != outStoreId) {
							BillOutStore billOutStore = billOutStoreDao.queryEntityById(outStoreId);
							BigDecimal payAmount = billOutStore.getPayAmount() == null ? BigDecimal.ZERO
									: billOutStore.getPayAmount();
							receivedAmount = billOutStore.getReceivedAmount() == null ? BigDecimal.ZERO
									: billOutStore.getReceivedAmount();
							BigDecimal fundBackAmount = billOutStore.getFundBackAmount() == null ? BigDecimal.ZERO
									: billOutStore.getFundBackAmount();
							BigDecimal writeOffAmount = recReceiptRel.getWriteOffAmount() == null ? BigDecimal.ZERO
									: recReceiptRel.getWriteOffAmount();
							if (DecimalUtil.le(writeOffAmount, DecimalUtil.subtract(payAmount, fundBackAmount))) {
								fundUsed = writeOffAmount;
							} else {
								fundUsed = DecimalUtil.subtract(payAmount, fundBackAmount);
							}

							BillOutStore updateBillOutStore = new BillOutStore();
							updateBillOutStore.setId(outStoreId);
							updateBillOutStore.setFundBackAmount(DecimalUtil.add(fundBackAmount, fundUsed));

							BigDecimal currentFundUsed = BigDecimal.ZERO;
							// 查询资金归还金额小于付款金额的出库单明细
							List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreDtlDao
									.queryUnFundbackResultsByBillOutStoreId(outStoreId);
							if (CollectionUtils.isNotEmpty(billOutStoreDtlList)) {
								for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtlList) {
									BigDecimal diffAmount = DecimalUtil.subtract(
											DecimalUtil.multiply(billOutStoreDtl.getPayPrice(),
													billOutStoreDtl.getSendNum()),
											billOutStoreDtl.getFundBackDtlAmount());
									BigDecimal shareAmount = BigDecimal.ZERO;
									if (DecimalUtil.lt(diffAmount, fundUsed)) {
										shareAmount = diffAmount;
									} else {
										shareAmount = fundUsed;
									}
									BigDecimal fundBackDtlAmount = DecimalUtil
											.add(billOutStoreDtl.getFundBackDtlAmount(), shareAmount);
									BillOutStoreDtl updateBillOutStoreDtl = new BillOutStoreDtl();
									updateBillOutStoreDtl.setId(billOutStoreDtl.getId());
									updateBillOutStoreDtl.setFundBackDtlAmount(fundBackDtlAmount);
									billOutStoreDtlDao.updateById(updateBillOutStoreDtl);

									currentFundUsed = DecimalUtil.add(currentFundUsed,
											DecimalUtil.multiply(shareAmount, billOutStoreDtl.getPayRate()));
									fundUsed = DecimalUtil.subtract(fundUsed, shareAmount);
									if (DecimalUtil.le(fundUsed, BigDecimal.ZERO)) {
										break;
									}
								}
							}
							billOutStoreDao.updateById(updateBillOutStore);

							RecReceiptRel upRecReceiptRel = new RecReceiptRel();
							upRecReceiptRel.setId(recReceiptRel.getId());
							upRecReceiptRel.setFundUsed(DecimalUtil.formatScale2(currentFundUsed)); // 原币种资金占用金额*付款比例
							recReceiptRelDao.updateById(upRecReceiptRel);
						}
						if (null != feeId) {
							BigDecimal payAmount = BigDecimal.ZERO;
							Fee fee = feeDao.queryEntityById(feeId);
							receivedAmount = fee.getReceivedAmount();
							if (fee.getPayFeeType() != null) {
								if (fee.getPayFeeType() == BaseConsts.ONE) {
									payAmount = fee.getFundUsed();
								}
							} else {
								// 根据费用单查询应收应付关系表
								List<FeeRecPay> feeRecPays = feeRecPayDao.queryByRecId(feeId);
								if (CollectionUtils.isNotEmpty(feeRecPays)) {
									for (FeeRecPay feeRecPay : feeRecPays) {
										Fee fee2 = feeDao.queryEntityById(feeRecPay.getPayFeeId());
										if (null != fee2.getPayFeeType()
												&& fee2.getPayFeeType().equals(BaseConsts.ONE)) {
											BigDecimal payRate = payOrderDao.queryPayRateByFeeId(fee2.getId());
											if (payRate == null) {// 费用关系付款为空
												CopeManageDtl copeManageDtl = copeManageDtlDao
														.queryCopeByBillId(fee2.getId());
												if (copeManageDtl != null) {
													payRate = copeReceiptRelDao.queryPayRateBy(copeManageDtl.getId());
												}
											}
											if (payRate == null) {
												throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
														"水单核完,费用编号为【" + fee2.getFeeNo() + "】未找到付款汇率");
											}
											payAmount = DecimalUtil.add(payAmount, DecimalUtil.multiply(
													fee2.getFundUsed() == null ? BigDecimal.ZERO : fee2.getFundUsed(),
													payRate));
										}
									}
								}
							}
							if (DecimalUtil.ge(payAmount, receivedAmount)) {
								fundUsed = recReceiptRel.getWriteOffAmount();
							} else {
								fundUsed = DecimalUtil.subtract(recReceiptRel.getWriteOffAmount(),
										DecimalUtil.subtract(receivedAmount, payAmount));
							}
							RecReceiptRel upRecReceiptRel = new RecReceiptRel();
							upRecReceiptRel.setId(recReceiptRel.getId());
							upRecReceiptRel.setFundUsed(fundUsed);
							recReceiptRelDao.updateById(upRecReceiptRel);
						}
					}
				}
			}
		}
	}

	/**
	 * private void dealFundBack(BankReceipt bankReceipt) {
	 * VlReceiptRelSearchReqDto vlReceiptRelSearchReqDto = new
	 * VlReceiptRelSearchReqDto();
	 * vlReceiptRelSearchReqDto.setReceiptId(bankReceipt.getId());
	 * 
	 * List<VlReceiptRel> vlReceiptRels =
	 * vlReceiptRelDao.queryRecustsByCon(vlReceiptRelSearchReqDto); if
	 * (!CollectionUtils.isEmpty(vlReceiptRels)) { for (VlReceiptRel
	 * vlReceiptRel : vlReceiptRels) { if
	 * (!DecimalUtil.eq(vlReceiptRel.getWriteOffAmount(), DecimalUtil.ZERO)) {
	 * if (vlReceiptRel.getBillType().equals(BaseConsts.THREE) &&
	 * !StringUtils.isEmpty(vlReceiptRel.getOutStoreId())) { BillOutStore
	 * billOutStore = billOutStoreDao
	 * .queryAndLockEntityById(vlReceiptRel.getOutStoreId()); BigDecimal
	 * tempAmount = DecimalUtil.formatScale2(
	 * DecimalUtil.add(billOutStore.getFundBackAmount(),
	 * vlReceiptRel.getWriteOffAmount())); BigDecimal fundBackAmount = null; if
	 * (DecimalUtil.gt(tempAmount, billOutStore.getCostAmount())) {
	 * fundBackAmount =
	 * DecimalUtil.formatScale2(DecimalUtil.subtract(billOutStore.getCostAmount(
	 * ), billOutStore.getFundBackAmount())); } else { fundBackAmount =
	 * vlReceiptRel.getWriteOffAmount(); } ProjectPoolFund ppf =
	 * convertToPPf(bankReceipt, fundBackAmount, BaseConsts.ONE,
	 * BaseConsts.THREE, vlReceiptRel.getOutStoreId(),
	 * billOutStore.getBillNo()); projectPoolService.addProjectPoolFund(ppf);
	 * 
	 * BillOutStore billOutStoreUpd = new BillOutStore();
	 * billOutStoreUpd.setId(billOutStore.getId());
	 * billOutStoreUpd.setFundBackAmount(DecimalUtil
	 * .formatScale2(DecimalUtil.add(billOutStore.getFundBackAmount(),
	 * fundBackAmount))); billOutStoreDao.updateById(billOutStoreUpd);
	 * 
	 * // 资金池回冲核销金额 if (bankReceipt.getReceiptType().equals(BaseConsts.TWO) ||
	 * bankReceipt.getReceiptType().equals(BaseConsts.THREE)) { ProjectPoolFund
	 * ppf_back = convertToPPf(bankReceipt,
	 * vlReceiptRel.getWriteOffAmount().multiply(new BigDecimal("-1")),
	 * BaseConsts.ONE, BaseConsts.THREE, vlReceiptRel.getOutStoreId(),
	 * billOutStore.getBillNo());
	 * projectPoolService.addProjectPoolFund(ppf_back); } } else if
	 * (vlReceiptRel.getBillType().equals(BaseConsts.ONE) &&
	 * !StringUtils.isEmpty(vlReceiptRel.getFeeId())) { Fee feeEntity =
	 * feeDao.queryEntityById(vlReceiptRel.getFeeId()); if
	 * (feeEntity.getFeeType().equals(BaseConsts.THREE)) { ProjectPoolFund ppf =
	 * convertToPPf(bankReceipt, vlReceiptRel.getWriteOffAmount(),
	 * BaseConsts.ONE, BaseConsts.ONE, vlReceiptRel.getFeeId(),
	 * feeEntity.getFeeNo()); projectPoolService.addProjectPoolFund(ppf); } else
	 * if (feeEntity.getFeeType().equals(BaseConsts.ONE)) { if
	 * (bankReceipt.getReceiptType().equals(BaseConsts.TWO) ||
	 * bankReceipt.getReceiptType().equals(BaseConsts.THREE)) { ProjectPoolFund
	 * ppf = convertToPPf(bankReceipt,
	 * vlReceiptRel.getWriteOffAmount().multiply(new BigDecimal("-1")),
	 * BaseConsts.ONE, BaseConsts.ONE, vlReceiptRel.getFeeId(),
	 * feeEntity.getFeeNo()); projectPoolService.addProjectPoolFund(ppf); } } }
	 * else { // 待补充 } } } } }
	 **/

	/**
	 * 
	 * TODO.
	 *
	 * @param bankReceipt
	 * @param amount
	 * @param fundClass
	 *            1:应收核销 2：订金 3：货款
	 * @param billType
	 *            1：费用单 3：出库单
	 * @param billId
	 * @param assistBillNo
	 * @return
	 */
	private ProjectPoolFund convertToPPf(BankReceipt bankReceipt, BigDecimal amount, int fundClass, Integer billType,
			Integer billId, String assistBillNo) {
		ProjectPoolFund ppf = new ProjectPoolFund();
		ppf.setType(BaseConsts.TWO);
		ppf.setBillNo(bankReceipt.getReceiptNo());
		ppf.setBillSource(BaseConsts.TWO);
		ppf.setProjectId(bankReceipt.getProjectId());
		ppf.setCustomerId(bankReceipt.getCustId());
		ppf.setBusinessDate(bankReceipt.getReceiptDate());
		ppf.setBillAmount(amount);
		ppf.setBillCurrencyType(bankReceipt.getActualCurrencyType());
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

	public BankReceipt queryEntityByNo(String receiptNo) {
		BankReceipt bankReceipt = null;
		BankReceiptSearchReqDto reqDto = new BankReceiptSearchReqDto();
		reqDto.setReceiptNo(receiptNo);
		List<BankReceipt> bankReceipts = bankReceiptDao.queryResultsByCon(reqDto);
		if (CollectionUtils.isEmpty(bankReceipts)) {
			return bankReceipt;
		}
		if (bankReceipts.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单编号不唯一，请核查");
		}
		return bankReceipts.get(0);
	}

	private BankReceipt convertToBankReceipt(BankReceipt bankReceipt, AdvanceReceiptRel advanceReceiptRel) {
		BankReceipt subBankReceipt = new BankReceipt();
		BeanUtils.copyProperties(bankReceipt, subBankReceipt);
		subBankReceipt.setReceiptAmount(advanceReceiptRel.getExchangeAmount());
		subBankReceipt.setDiffAmount(DecimalUtil.ZERO);
		subBankReceipt.setWriteOffAmount(DecimalUtil.ZERO);
		subBankReceipt.setPreRecAmount(DecimalUtil.ZERO);
		subBankReceipt.setPaidAmount(DecimalUtil.ZERO);
		subBankReceipt.setPid(StringUtils.isEmpty(bankReceipt.getPid()) ? bankReceipt.getId() : bankReceipt.getPid());
		subBankReceipt.setProjectId(advanceReceiptRel.getProjectId());
		if (advanceReceiptRel.getAdvanceType().equals(BaseConsts.ONE)) {
			subBankReceipt.setReceiptType(BaseConsts.TWO); // 预收定金水单
		} else if (advanceReceiptRel.getAdvanceType().equals(BaseConsts.TWO)) {
			subBankReceipt.setReceiptType(BaseConsts.THREE); // 预收货款水单
		}
		return subBankReceipt;
	}

	/**
	 * 生成预付款类型的水单
	 * 
	 * @param bankReceipt
	 * @return
	 */
	private void convertToPrepaidReceipt(BankReceipt bankReceipt) {
		BankReceipt subBankReceipt = new BankReceipt();
		BeanUtils.copyProperties(bankReceipt, subBankReceipt);
		subBankReceipt.setReceiptAmount(bankReceipt.getPreRecAmount());
		subBankReceipt.setActualReceiptAmount(bankReceipt.getActualPreRecAmount());
		subBankReceipt.setDiffAmount(DecimalUtil.ZERO);
		subBankReceipt.setWriteOffAmount(DecimalUtil.ZERO);
		subBankReceipt.setPreRecAmount(DecimalUtil.ZERO);
		subBankReceipt.setPaidAmount(DecimalUtil.ZERO);
		subBankReceipt.setPid(StringUtils.isEmpty(bankReceipt.getPid()) ? bankReceipt.getId() : bankReceipt.getPid());
		subBankReceipt.setReceiptType(BaseConsts.TEN); // 预付款水单
		if (DecimalUtil.gt(subBankReceipt.getReceiptAmount(), BigDecimal.ZERO)) {
			this.createPreBankReceipt(subBankReceipt);
		}
		// 回写费用单的已付金额
		copeReceiptRelService.updateFeeByReceiptId(bankReceipt.getId());
	}

	/**
	 * 编辑详情
	 * 
	 * @return
	 */
	public Result<BankReceiptResDto> editBankReceiptById(Integer receiptId) {
		Result<BankReceiptResDto> result = new Result<BankReceiptResDto>();
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(receiptId);
		BankReceiptResDto data = convertToBankReceiptResDto(bankReceipt);
		result.setItems(data);
		return result;
	}

	/**
	 * 浏览详情
	 * 
	 * @param id
	 * @return
	 */
	public Result<BankReceiptResDto> detailBankReceiptById(int id) {
		Result<BankReceiptResDto> result = new Result<BankReceiptResDto>();
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(id);
		BankReceiptResDto data = convertToBankReceiptResDto(bankReceipt);
		result.setItems(data);
		return result;
	}

	public BankReceipt queryEntityById(int id) {
		return bankReceiptDao.queryEntityById(id);
	}

	/**
	 * 获取分页数据
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public PageResult<BankReceiptResDto> queryBankReceiptResultsByCon(BankReceiptSearchReqDto bankReceiptReqDto) {
		PageResult<BankReceiptResDto> pageResult = new PageResult<BankReceiptResDto>();
		int offSet = PageUtil.getOffSet(bankReceiptReqDto.getPage(), bankReceiptReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, bankReceiptReqDto.getPer_page());
		bankReceiptReqDto.setUserId(ServiceSupport.getUser().getId());
		List<BankReceiptResDto> bankReceiptResDtos = convertToRecFeeResDtos(
				bankReceiptDao.queryResultsByCon(bankReceiptReqDto, rowBounds));
		pageResult.setItems(bankReceiptResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), bankReceiptReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(bankReceiptReqDto.getPage());
		pageResult.setPer_page(bankReceiptReqDto.getPer_page());

		if (bankReceiptReqDto.getNeedSum() != null && bankReceiptReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<BankReceipt> sumResDto = bankReceiptDao.sumBankReceipt(bankReceiptReqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal receiptAmountSum = BigDecimal.ZERO;
				BigDecimal diffAmountSum = BigDecimal.ZERO;
				BigDecimal writeOffAmountSum = BigDecimal.ZERO;
				BigDecimal preRecAmountSum = BigDecimal.ZERO;
				for (BankReceipt bankReceipt : sumResDto) {
					if (bankReceipt != null) {
						receiptAmountSum = DecimalUtil.add(receiptAmountSum,
								ServiceSupport.amountNewToRMB(
										bankReceipt.getReceiptAmount() == null ? DecimalUtil.ZERO
												: bankReceipt.getReceiptAmount(),
										bankReceipt.getCurrencyType(), new Date()));
						diffAmountSum = DecimalUtil.add(diffAmountSum,
								ServiceSupport.amountNewToRMB(
										bankReceipt.getDiffAmount() == null ? DecimalUtil.ZERO
												: bankReceipt.getDiffAmount(),
										bankReceipt.getCurrencyType(), new Date()));
						writeOffAmountSum = DecimalUtil.add(writeOffAmountSum,
								ServiceSupport.amountNewToRMB(
										bankReceipt.getWriteOffAmount() == null ? DecimalUtil.ZERO
												: bankReceipt.getWriteOffAmount(),
										bankReceipt.getCurrencyType(), new Date()));
						preRecAmountSum = DecimalUtil.add(preRecAmountSum,
								ServiceSupport.amountNewToRMB(
										bankReceipt.getPreRecAmount() == null ? DecimalUtil.ZERO
												: bankReceipt.getPreRecAmount(),
										bankReceipt.getCurrencyType(), new Date()));
					}
				}
				String totalStr = "水单金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(receiptAmountSum))
						+ " CNY &nbsp;&nbsp;&nbsp;  尾差: " + DecimalUtil.formatScale2(diffAmountSum)
						+ " CNY &nbsp;&nbsp;&nbsp;  核销金额: "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(writeOffAmountSum))
						+ " CNY &nbsp;&nbsp;&nbsp;  预收金额: "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(preRecAmountSum)) + " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		return pageResult;
	}

	// 查询付款单可关联的预收定金水单,分页
	public PageResult<BankReceiptResDto> queryRootByConPayAdvanceRel(BankReceiptSearchReqDto bankReceiptReqDto) {
		PageResult<BankReceiptResDto> pageResult = new PageResult<BankReceiptResDto>();
		int offSet = PageUtil.getOffSet(bankReceiptReqDto.getPage(), bankReceiptReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, bankReceiptReqDto.getPer_page());
		bankReceiptReqDto.setUserId(ServiceSupport.getUser().getId());
		// 根据业务改动 付货款查询只查询
		List<BankReceipt> bankReceipts = new ArrayList<BankReceipt>();
		if (bankReceiptReqDto.getPayType() == BaseConsts.ONE) {
			bankReceipts = bankReceiptDao.queryRootByPayAdvanceRel(bankReceiptReqDto);
		} else {
			bankReceipts = bankReceiptDao.queryRootByConPayAdvanceRel(bankReceiptReqDto, rowBounds);
		}
		List<BankReceiptResDto> bankReceiptResDtos = convertToRecFeeResDtos(bankReceipts);
		pageResult.setItems(bankReceiptResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), bankReceiptReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(bankReceiptReqDto.getPage());
		pageResult.setPer_page(bankReceiptReqDto.getPer_page());
		return pageResult;
	}

	// 查询付款单可关联的预收定金水单，不分页
	public List<BankReceiptResDto> queryRootListByConPayAdvanceRel(BankReceiptSearchReqDto bankReceiptReqDto) {
		return convertToRecFeeResDtos(bankReceiptDao.queryRootByConPayAdvanceRel(bankReceiptReqDto));
	}

	/**
	 * 获取数据不分页
	 * 
	 * @param bankReceiptReqDto
	 * @return
	 */
	public List<BankReceiptResDto> queryBankReceiptResultsByExcel(BankReceiptSearchReqDto bankReceiptReqDto) {
		return convertToRecFeeResDtos(bankReceiptDao.queryResultsByCon(bankReceiptReqDto));
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<BankReceipFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<BankReceipFileResDto> pageResult = new PageResult<BankReceipFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.TEN);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<BankReceipFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<BankReceipFileResDto> queryFileList(Integer collectId) {
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(collectId);
		fileAttReqDto.setBusType(BaseConsts.TEN);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<BankReceipFileResDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<BankReceipFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<BankReceipFileResDto> list = new LinkedList<BankReceipFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			BankReceipFileResDto result = new BankReceipFileResDto();
			result.setId(model.getId());
			result.setBusId(model.getBusId());
			result.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, model.getBusType() + ""));
			result.setName(model.getName());
			result.setType(model.getType());
			result.setCreateAt(model.getCreateAt());
			result.setCreator(model.getCreator());
			List<CodeValue> operList = getOperFileList();
			result.setOpertaList(operList);
			list.add(result);
		}
		return list;
	}

	private List<CodeValue> getOperFileList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BankReceipFileResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DOWNLOAD);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param bankReceiptReqDto
	 * @return
	 */
	public boolean isOverasyncMaxLine(BankReceiptSearchReqDto bankReceiptReqDto) {
		int count = bankReceiptDao.isOverasyncMaxLine(bankReceiptReqDto);
		//
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("水单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/pay/receipt_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.TEN);
			asyncExcelService.addAsyncExcel(bankReceiptReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceApplyExport(BankReceiptSearchReqDto bankReceiptReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BankReceiptResDto> bankList = queryBankReceiptResultsByExcel(bankReceiptReqDto);
		model.put("receiptList", bankList);
		return model;
	}

	public List<BankReceiptResDto> convertToRecFeeResDtos(List<BankReceipt> result) {
		List<BankReceiptResDto> bankReceiptQueryResDtos = new ArrayList<BankReceiptResDto>();
		if (ListUtil.isEmpty(result)) {
			return bankReceiptQueryResDtos;
		}
		for (BankReceipt bankReceipt : result) {
			BankReceiptResDto bankReceiptResDto = convertToBankReceiptResDto(bankReceipt);
			if (bankReceiptResDto == null) {
				continue;
			}
			List<CodeValue> operList = getOperList(bankReceipt);
			bankReceiptResDto.setOpertaList(operList);
			bankReceiptQueryResDtos.add(bankReceiptResDto);
		}
		return bankReceiptQueryResDtos;
	}

	public BankReceiptResDto convertToBankReceiptResDto(BankReceipt bankReceipt) {
		return convertToBankReceiptResDto(bankReceipt, false);
	}

	public BankReceiptResDto convertToBankReceiptResDto(BankReceipt bankReceipt, boolean queryParent) {
		BankReceiptResDto bankReceiptResDto = new BankReceiptResDto();
		bankReceiptResDto.setId(bankReceipt.getId());
		bankReceiptResDto.setPid(bankReceipt.getPid());
		bankReceiptResDto.setReceiptNo(bankReceipt.getReceiptNo());
		bankReceiptResDto.setBankReceiptNo(bankReceipt.getBankReceiptNo());
		// 收款账户
		bankReceiptResDto.setRecAccountNo(bankReceipt.getRecAccountNo());
		if (bankReceipt.getRecAccountNo() != null) {
			BaseAccount baseAccount = cacheService.getAccountById(bankReceipt.getRecAccountNo());
			if (baseAccount != null) {
				bankReceiptResDto.setRecAccountNoName(baseAccount.getShowValue());
			}
		}
		bankReceiptResDto.setReceiptDate(bankReceipt.getReceiptDate());
		// 项目
		bankReceiptResDto.setProjectId(bankReceipt.getProjectId());
		bankReceiptResDto.setProjectName(cacheService.getProjectNameById(bankReceipt.getProjectId()));
		// 经营单位
		bankReceiptResDto.setBusiUnit(bankReceipt.getBusiUnit());
		bankReceiptResDto.setBusiUnitName(
				cacheService.getSubjectNcByIdAndKey(bankReceipt.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		// 客户
		bankReceiptResDto.setCustId(bankReceipt.getCustId());
		bankReceiptResDto
				.setCusName(cacheService.getSubjectNcByIdAndKey(bankReceipt.getCustId(), CacheKeyConsts.CUSTOMER));
		// 币种
		bankReceiptResDto.setCurrencyType(bankReceipt.getCurrencyType());
		bankReceiptResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				bankReceipt.getCurrencyType() + ""));
		bankReceiptResDto.setReceiptAmount(bankReceipt.getReceiptAmount());
		bankReceiptResDto.setPreRecAmount(bankReceipt.getPreRecAmount());
		bankReceiptResDto.setWriteOffAmount(bankReceipt.getWriteOffAmount());
		bankReceiptResDto.setDiffAmount(bankReceipt.getDiffAmount());
		// 状态
		bankReceiptResDto.setState(bankReceipt.getState());
		bankReceiptResDto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.RECEIPT_STATUS, bankReceipt.getState() + ""));
		// 类型
		bankReceiptResDto.setReceiptType(bankReceipt.getReceiptType());
		bankReceiptResDto.setReceiptTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.RECEIPT_TYPE, bankReceipt.getReceiptType() + ""));
		bankReceiptResDto.setSummary(bankReceipt.getSummary());
		// 收款方式
		bankReceiptResDto.setReceiptWayName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_WAY, bankReceipt.getReceiptWay() + ""));
		bankReceiptResDto.setReceiptWay(bankReceipt.getReceiptWay());
		bankReceiptResDto.setOpenType(bankReceipt.getOpenType());
		bankReceiptResDto.setOpenDate(bankReceipt.getOpenDate());
		bankReceiptResDto.setRemainAmount(bankReceipt.getRemainAmount());
		bankReceiptResDto.setCreator(bankReceipt.getCreator());
		bankReceiptResDto.setCreateAt(bankReceipt.getCreateAt());
		bankReceiptResDto.setWriteOffAt(bankReceipt.getWriteOffAt());
		BaseUser baseUser = cacheService.getUserByid(bankReceipt.getWriteOfftorId());
		if (baseUser != null) {
			bankReceiptResDto.setWriteOfftor(baseUser.getChineseName());
		}
		BigDecimal amountSum = DecimalUtil.add(bankReceipt.getReceiptAmount(), bankReceipt.getDiffAmount());
		BigDecimal preSum = DecimalUtil.add(bankReceipt.getWriteOffAmount(), bankReceipt.getPreRecAmount());
		BigDecimal blance = DecimalUtil.formatScale2(DecimalUtil.subtract(amountSum, preSum));// 获取可核销金额
		bankReceiptResDto.setReceiptBlance(blance);
		bankReceiptResDto.setPaidAmount(bankReceipt.getPaidAmount());
		// 可付金额 退款信息 水单金额— 已付金额
		bankReceiptResDto.setCanPaidAmount(bankReceipt.getReceiptAmount().subtract(bankReceipt.getPaidAmount()));
		if (bankReceiptResDto.getReceiptType().equals(BaseConsts.ONE)) {
			List<AdvanceReceiptRel> advanceReceiptRels = bankReceiptDao.querySubAmountByRoot(bankReceipt.getId());
			for (AdvanceReceiptRel advanceReceiptRel : advanceReceiptRels) {
				if (!StringUtils.isEmpty(advanceReceiptRel)) {
					if (!StringUtils.isEmpty(advanceReceiptRel.getAdvanceType())) {
						if (advanceReceiptRel.getAdvanceType().equals(BaseConsts.ONE)) {
							bankReceiptResDto.setPreDepositeAmount(advanceReceiptRel.getExchangeAmount());
						} else if (advanceReceiptRel.getAdvanceType().equals(BaseConsts.TWO)) {
							bankReceiptResDto.setPrePoAmount(advanceReceiptRel.getExchangeAmount());
						}
					}
				}
			}
		}
		// // 预收定金类型 直接赋值为当前水单的付款金额
		if (bankReceiptResDto.getReceiptType().equals(BaseConsts.TWO)) {
			bankReceiptResDto.setPreDepositeAmount(bankReceiptResDto.getReceiptAmount());
		}
		bankReceiptResDto.setPreDepositeAmount(StringUtils.isEmpty(bankReceiptResDto.getPreDepositeAmount())
				? DecimalUtil.ZERO : bankReceiptResDto.getPreDepositeAmount());
		bankReceiptResDto.setPrePoAmount(StringUtils.isEmpty(bankReceiptResDto.getPrePoAmount()) ? DecimalUtil.ZERO
				: bankReceiptResDto.getPrePoAmount());
		bankReceiptResDto.setPayableAmount(DecimalUtil.formatScale2(
				DecimalUtil.subtract(bankReceiptResDto.getPreDepositeAmount(), bankReceipt.getPaidAmount())));
		bankReceiptResDto.setVerificationAdvanceAmount(bankReceipt.getVerificationAdvanceAmount());

		// 查询父级水单
		if (queryParent == true) {
			if (bankReceiptResDto.getReceiptType().equals(BaseConsts.TWO)
					|| bankReceiptResDto.getReceiptType().equals(BaseConsts.THREE)) {
				if (null != bankReceiptResDto.getPid()) {
					BankReceipt parentBankReceipt = bankReceiptDao.queryEntityById(bankReceiptResDto.getPid());
					if (null != parentBankReceipt) {
						bankReceiptResDto.setParentReceiptId(parentBankReceipt.getId());
						bankReceiptResDto.setParentReceiptNo(parentBankReceipt.getReceiptNo());
						bankReceiptResDto.setParentReceiptType(parentBankReceipt.getReceiptType());
						bankReceiptResDto.setParentReceiptDate(parentBankReceipt.getReceiptDate());
						bankReceiptResDto.setParentReceiptAmount(parentBankReceipt.getReceiptAmount());
						bankReceiptResDto.setParentSummary(parentBankReceipt.getSummary());
					}
				} else {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"预收货款或定金水单【" + bankReceiptResDto.getReceiptNo() + "】未找到对应回款水单");
					// return null;
				}
			} else {
				bankReceiptResDto.setParentReceiptId(bankReceiptResDto.getId());
				bankReceiptResDto.setParentReceiptNo(bankReceiptResDto.getReceiptNo());
				bankReceiptResDto.setParentReceiptType(bankReceiptResDto.getReceiptType());
				bankReceiptResDto.setParentReceiptDate(bankReceiptResDto.getReceiptDate());
				bankReceiptResDto.setParentReceiptAmount(bankReceiptResDto.getReceiptAmount());
				bankReceiptResDto.setParentSummary(bankReceiptResDto.getSummary());
			}
		}
		bankReceiptResDto.setPayUnit(bankReceipt.getPayUnit());
		if (!StringUtils.isEmpty(bankReceipt.getPayUnit())) {
			bankReceiptResDto.setPayUnitName(bankReceipt.getPayUnit());
		}
		bankReceiptResDto.setActualReceiptAmount(bankReceipt.getActualReceiptAmount());
		bankReceiptResDto.setActualWriteOffAmount(bankReceipt.getActualWriteOffAmount());
		bankReceiptResDto.setActualPreRecAmount(bankReceipt.getActualPreRecAmount());
		bankReceiptResDto.setActualDiffAmount(bankReceipt.getActualDiffAmount());
		bankReceiptResDto.setActualPaidAmount(bankReceipt.getActualPaidAmount());
		bankReceiptResDto.setActualCurrencyRate(bankReceipt.getActualCurrencyRate());
		bankReceiptResDto.setActualCurrencyType(bankReceipt.getActualCurrencyType());
		if (null != bankReceipt.getActualCurrencyType()) {
			bankReceiptResDto.setActualCurrencyTypeName(ServiceSupport
					.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, bankReceipt.getActualCurrencyType() + ""));
		}
		// 预收类型的转换 变动金额的算法
		bankReceiptResDto.setDepChangePaymentAmount(DecimalUtil.formatScale2(
				DecimalUtil.subtract(bankReceiptResDto.getReceiptBlance(), bankReceiptResDto.getPaidAmount())));// 订金转退款
		return bankReceiptResDto;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(BankReceipt bankReceipt) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(bankReceipt);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BankReceiptResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(BankReceipt bankReceipt) {
		Integer state = bankReceipt.getState();
		List<String> opertaList = Lists.newArrayList();
		if (state == null) {
			return opertaList;
		}
		switch (state) {
		// 状态,1表示待提交，2表示待核销，3表示已完成
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.CHECK);
			opertaList.add(OperateConsts.OVER);
			if (bankReceipt.getReceiptType().equals(BaseConsts.ONE)
					|| bankReceipt.getReceiptType().equals(BaseConsts.FOUR)
					|| bankReceipt.getReceiptType().equals(BaseConsts.FIVE)) { // 回款类型
				AdvanceSearchReqDto advanceSearchReqDto = new AdvanceSearchReqDto();
				advanceSearchReqDto.setReceiptId(bankReceipt.getId());
				List<AdvanceReceiptRel> advanceReceiptRels = advanceReceiptRelDao
						.queryResultsByCon(advanceSearchReqDto);
				if (CollectionUtils.isEmpty(advanceReceiptRels)) {
					RecReceiptRelSearchReqDto recReceiptRelSearchReqDto = new RecReceiptRelSearchReqDto();
					recReceiptRelSearchReqDto.setReceiptId(bankReceipt.getId());
					List<RecReceiptRel> recReceiptRels = recReceiptRelDao.queryResultsByCon(recReceiptRelSearchReqDto);
					if (CollectionUtils.isEmpty(recReceiptRels)) {
						opertaList.add(OperateConsts.REJECT);
					}
				}
			}
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.THREE:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}

	/**
	 * 根据退款信息的条件进行水单信息的进行分页查询
	 * 
	 * @param bankReceiptSearchReqDto
	 * @return
	 */
	public PageResult<BankReceiptResDto> queryRootRefundResult(BankReceiptSearchReqDto bankReceiptSearchReqDto) {
		PageResult<BankReceiptResDto> pageResult = new PageResult<BankReceiptResDto>();
		int offSet = PageUtil.getOffSet(bankReceiptSearchReqDto.getPage(), bankReceiptSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, bankReceiptSearchReqDto.getPer_page());
		bankReceiptSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<BankReceiptResDto> bankReceiptResDtos = convertToRecFeeResDtos(
				bankReceiptDao.queryRefundResult(bankReceiptSearchReqDto, rowBounds));
		pageResult.setItems(bankReceiptResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), bankReceiptSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(bankReceiptSearchReqDto.getPage());
		pageResult.setPer_page(bankReceiptSearchReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 根据预付款单生成水单
	 * 
	 * @param id
	 */
	public void createBankReceiptByPay(Integer id) {
		PayOrder payOrder = payService.queryEntityById(id);
		// 校验数据
		if (payOrder == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "预付款单生成水单,单据ID为【" + id + "】未找到对应的付款单");
		}
		if (!payOrder.getState().equals(BaseConsts.SIX)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "预付款单生成水单单据编号为【" + payOrder.getPayNo() + "】其付款单状态有误");
		}
		if (!payOrder.getPayType().equals(BaseConsts.FIVE)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "预付款单生成水单单据编号为【" + payOrder.getPayNo() + "】其付款单类型有误");
		}
		BankReceipt bankReceipt = new BankReceipt();
		bankReceipt.setReceiptType(BaseConsts.NINE);// 9 付款类型
		bankReceipt.setBankReceiptNo(payOrder.getPayNo());// 银行水单号-付款单编号
		bankReceipt.setReceiptDate(payOrder.getConfirmorAt());// 水单日期-确认时间
		bankReceipt.setProjectId(payOrder.getProjectId());
		bankReceipt.setBusiUnit(payOrder.getBusiUnit());
		bankReceipt.setRecAccountNo(payOrder.getPaymentAccount());
		bankReceipt.setCustId(payOrder.getPayee());
		bankReceipt.setCurrencyType(payOrder.getRealCurrencyType());
		bankReceipt.setActualCurrencyType(payOrder.getRealCurrencyType());
		bankReceipt.setReceiptAmount(payOrder.getRealPayAmount());
		bankReceipt.setActualReceiptAmount(payOrder.getRealPayAmount());
		bankReceipt.setDiffAmount(BigDecimal.ZERO);
		bankReceipt.setState(BaseConsts.TWO);
		bankReceipt.setReceiptWay(payOrder.getPayWay());// 付款方式
		this.createPreBankReceipt(bankReceipt);
	}
}
