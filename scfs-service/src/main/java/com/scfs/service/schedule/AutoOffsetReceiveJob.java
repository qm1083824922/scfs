package com.scfs.service.schedule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.subject.dto.req.AddAccountDto;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.AutoReceiptDealService;
import com.scfs.service.support.ServiceSupport;

/**
 * 自动冲抵应收 Created by Administrator on 2017年7月3日.
 */
@Service
public class AutoOffsetReceiveJob {
	private final static Logger LOGGER = LoggerFactory.getLogger(AutoOffsetReceiveJob.class);
	@Autowired
	private BankReceiptDao bankReceiptDao;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private BaseAccountDao baseAccountDao;
	@Autowired
	private AutoReceiptDealService autoReceiptDealService;

	public void execute() {
		long startTime = System.currentTimeMillis();
		LOGGER.info("[自动抵冲应收任务]开始时间：" + new Date());

		Date date = new Date();
		String currDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, date);

		refreshFee(currDate);
		refreshReceive(currDate);

		long endTime = System.currentTimeMillis();
		LOGGER.info("[自动抵冲应收任务]结束时间：" + new Date() + "，共执行" + (endTime - startTime) + "ms");
	}

	public void refreshFee(String currDate) {
		// 1.费用自动抵冲
		try {
			List<BankReceipt> bankReceiptList = bankReceiptDao.queryRefreshFeeInfo(currDate);
			for (BankReceipt bankReceipt : bankReceiptList) {
				createBankReceipt(bankReceipt);
				boolean isSuccess = autoReceiptDealService.autoVerificateReceipt(bankReceipt, true);
				if (isSuccess == false) {
					bankReceipt.setIsDelete(BaseConsts.ONE);
					bankReceipt.setDeleterId(ServiceSupport.getUser().getId());
					bankReceipt.setDeleter(ServiceSupport.getUser().getChineseName());
					bankReceipt.setDeleteAt(new Date());
					bankReceiptDao.updateById(bankReceipt);
				}
			}
		} catch (BaseException e) {
			LOGGER.error("自动抵冲应收任务[费用]定时任务失败：", e);
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "系统错误，事务需要回滚");
		} catch (Exception e) {
			LOGGER.error("自动抵冲应收任务[费用]定时任务失败：", e);
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "系统错误，事务需要回滚");
		}
	}

	public void refreshReceive(String currDate) {
		// 2.付款单自动抵冲
		try {
			List<BankReceipt> bankReceiptList = bankReceiptDao.queryRefreshReceiveInfo(currDate);
			for (BankReceipt bankReceipt : bankReceiptList) {
				createBankReceipt(bankReceipt);
				boolean isSuccess = autoReceiptDealService.autoVerificateReceipt(bankReceipt, true);
				if (isSuccess == false) {
					bankReceipt.setIsDelete(BaseConsts.ONE);
					bankReceipt.setDeleterId(ServiceSupport.getUser().getId());
					bankReceipt.setDeleter(ServiceSupport.getUser().getChineseName());
					bankReceipt.setDeleteAt(new Date());
					bankReceiptDao.updateById(bankReceipt);
				}
			}
		} catch (BaseException e) {
			LOGGER.error("自动抵冲应收任务[付款单]定时任务失败：", e);
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "系统错误，事务需要回滚");
		} catch (Exception e) {
			LOGGER.error("自动抵冲应收任务[付款单]定时任务失败：", e);
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "系统错误，事务需要回滚");
		}
	}

	private BankReceipt createBankReceipt(BankReceipt bankReceipt) {
		Date datea = new Date();
		bankReceipt.setCreateAt(datea);
		bankReceipt.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
				: ServiceSupport.getUser().getChineseName());
		bankReceipt.setCreatorId(
				ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_ID : ServiceSupport.getUser().getId());
		bankReceipt.setReceiptNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_RECEIPT_NO, SeqConsts.S_RECEIPT_NO,
				BaseConsts.INT_13));
		bankReceipt.setReceiptType(BaseConsts.SIX);
		bankReceipt.setState(BaseConsts.TWO);
		bankReceipt.setReceiptWay(BaseConsts.ONE); // 付款方式：TT

		bankReceipt.setActualReceiptAmount(bankReceipt.getReceiptAmount());
		bankReceipt.setActualCurrencyType(bankReceipt.getCurrencyType());
		if (null == bankReceipt.getReceiptAmount() || DecimalUtil.eq(bankReceipt.getReceiptAmount(), BigDecimal.ZERO)) {
			bankReceipt.setActualCurrencyRate(BigDecimal.ZERO);
		} else {
			bankReceipt.setActualCurrencyRate(
					DecimalUtil.divide(bankReceipt.getActualReceiptAmount(), bankReceipt.getReceiptAmount()));
		}

		// 查询虚拟账户 ,如果不存在虚拟账户，则创建虚拟账户
		bankReceipt.setRecAccountNo(getAccountInfo(bankReceipt));
		// 创建水单头信息
		bankReceiptDao.insert(bankReceipt);
		return bankReceipt;
	}

	/**
	 * 获取虚拟账户
	 * 
	 * @param bankRec
	 * @return
	 */
	private Integer getAccountInfo(BankReceipt bankRec) {
		QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
		queryAccountReqDto.setAccountType(BaseConsts.FIVE);
		queryAccountReqDto.setSubjectId(bankRec.getBusiUnit());
		queryAccountReqDto.setCurrencyType(bankRec.getCurrencyType());
		List<BaseAccount> list = baseAccountDao.queryFicBySubjectId(queryAccountReqDto);
		if (list.size() > 0) {
			bankRec.setRecAccountNo(list.get(0).getId());
		} else {
			AddAccountDto addAccountDto = new AddAccountDto();
			addAccountDto.setAccountNo(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					bankRec.getCurrencyType() + ""));
			addAccountDto.setBankAddress("虚拟地址");
			addAccountDto.setBankCode("虚拟");
			addAccountDto.setBankName("虚拟");
			addAccountDto.setPhoneNumber("虚拟");
			addAccountDto.setDefaultCurrency(bankRec.getCurrencyType());
			addAccountDto.setBankSimple("虚拟");
			addAccountDto.setSubjectId(bankRec.getBusiUnit());
			addAccountDto.setAccountType(BaseConsts.FIVE);
			addAccountDto.setCapitalAccountType(BaseConsts.ONE);
			addAccountDto.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
					: ServiceSupport.getUser().getChineseName());
			addAccountDto.setState(BaseConsts.ONE);
			int result = baseAccountDao.insertBaseAccount(addAccountDto);
			bankRec.setRecAccountNo(addAccountDto.getId());
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(addAccountDto));
			}
		}
		return bankRec.getRecAccountNo();
	}
}
