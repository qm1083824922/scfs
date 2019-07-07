package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.fi.RecReceiptRelDao;
import com.scfs.dao.fi.ReceiveDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.resp.RecSumModel;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.RecDetail;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: ReceiveService.java
 *  Description:            应收管理服务
 *  TODO
 *  Date,					Who,				
 *  2016年10月29日			Administrator
 *
 * </pre>
 */

@Service
public class ReceiveService {
	@Autowired
	ReceiveDao receiveDao;

	@Autowired
	RecLineDao recLineDao;

	@Autowired
	VoucherLineDao voucherLineDao;

	@Autowired
	CacheService cacheService;
	@Autowired
	private BankReceiptService bankReceiptService; // 水单
	@Autowired
	private RecReceiptRelDao recReceiptRelDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private BillDeliveryDao billDeliveryDao;

	/**
	 * 创建应收信息和应收明细 TODO.
	 *
	 * @param recDetail
	 * @throws Exception
	 */
	public Integer createRecDetail(RecDetail recDetail) {
		Receive receive = recDetail.getReceive();
		List<RecLine> recLines = recDetail.getRecLines();
		if (recLines.isEmpty()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "对账数据为空");
		}
		BigDecimal recSum = BigDecimal.ZERO; // 应收总金额
		Integer currencyType = receive.getCurrencyType();
		for (int index = 0; index < recLines.size(); index++) {
			RecLine recLine = recLines.get(index);
			VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId());
			BigDecimal amount_check = recLine.getAmountCheck();
			if (amount_check == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "对账金额不能为空");
			}
			if (!currencyType.equals(voucherLine.getCurrencyType())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不同需分开对账");
			}
			if (DecimalUtil.gt(DecimalUtil.ZERO, amount_check)
					&& DecimalUtil.lt(DecimalUtil.ZERO, voucherLine.getAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录金额为正数，对账金额必须为正数");
			}
			if (DecimalUtil.lt(DecimalUtil.ZERO, amount_check)
					&& DecimalUtil.gt(DecimalUtil.ZERO, voucherLine.getAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录金额为负数，对账金额必须为负数");
			}
			recSum = DecimalUtil.add(recSum, amount_check);
			BigDecimal writing_off_amount = DecimalUtil.subtract(voucherLine.getAmount(),
					voucherLine.getAmountChecked());
			// 校验 对账金额不能大于分录中的待对账金额
			if (DecimalUtil.gt(amount_check.abs(), writing_off_amount.abs())) {
				throw new BaseException(ExcMsgEnum.CHECK_AMOUNT_EXCCED, DecimalUtil.format(amount_check),
						DecimalUtil.format(writing_off_amount));
			}
			recLine.setCurrencyType(voucherLine.getCurrencyType());
		}
		BaseUser user = ServiceSupport.getUser();
		receive.setAmountReceivable(recSum);
		receive.setCurrencyType(currencyType);
		receive.setExpireDate(StringUtils.isEmpty(receive.getExpireDate()) ? new Date() : receive.getExpireDate());
		receive.setCreator(user.getChineseName());
		receive.setCreatorId(user.getId());
		// 1.新增应收记录
		receiveDao.insert(receive);
		int recId = receive.getId(); // 应收id

		for (RecLine recLine : recLines) {
			VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId());
			recLine.setRecId(recId);
			recLine.setCreator(user.getChineseName());
			recLine.setCreatorId(user.getId());
			recLine.setBillDate(voucherLine.getBillDate());
			recLine.setBillType(voucherLine.getBillType());
			recLine.setBillNo(voucherLine.getBillNo());
			recLine.setFeeId(voucherLine.getFeeId());
			recLine.setOutStoreId(voucherLine.getOutStoreId());
			// 2.新增应收明细
			recLineDao.insert(recLine);
			voucherLine.setAmountChecked(DecimalUtil.add(voucherLine.getAmountChecked(), recLine.getAmountCheck()));
			// 3.修改分录中的已对账金额
			voucherLineDao.updateById(voucherLine);
		}
		return receive.getId();
	}

	/**
	 * 合并应收 TODO.
	 *
	 * @param recDetail
	 */
	public void mergeRec(RecDetail recDetail) {
		int recId = recDetail.getReceive().getId();
		List<RecLine> recLines = recDetail.getRecLines();
		if (CollectionUtils.isEmpty(recLines)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "对账数据为空");
		}
		Receive receive = receiveDao.queryEntityById(recId);
		if (receive == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收不存在");
		}
		BigDecimal addAmount = BigDecimal.ZERO;
		Integer currencyType = receive.getCurrencyType();
		BaseUser user = ServiceSupport.getUser();
		for (int index = 0; index < recLines.size(); index++) {
			RecLine recLine = recLines.get(index);

			VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId());
			if (!currencyType.equals(voucherLine.getCurrencyType())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不同需分开对账");
			}
			ReceiveSearchReqDto reqDto = new ReceiveSearchReqDto();
			reqDto.setId(receive.getId());
			reqDto.setBillType(voucherLine.getBillType());
			List<Receive> receives = receiveDao.queryResultsByCon(reqDto);
			if (CollectionUtils.isEmpty(receives)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收单据类型与对账单据类型不一致，无法合并");
			}
			BigDecimal amount_check = recLine.getAmountCheck();
			if (DecimalUtil.gt(DecimalUtil.ZERO, amount_check)
					&& DecimalUtil.lt(DecimalUtil.ZERO, voucherLine.getAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录金额为正数，对账金额必须为正数");
			}
			if (DecimalUtil.lt(DecimalUtil.ZERO, amount_check)
					&& DecimalUtil.gt(DecimalUtil.ZERO, voucherLine.getAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分录金额为负数，对账金额必须为负数");
			}
			addAmount = DecimalUtil.add(addAmount, amount_check);
			recLine.setRecId(recId);
			recLine.setCurrencyType(voucherLine.getCurrencyType());
			recLine.setCreator(user.getChineseName());
			recLine.setCreatorId(user.getId());
			recLine.setBillDate(voucherLine.getBillDate());
			recLine.setBillType(voucherLine.getBillType());
			recLine.setBillNo(voucherLine.getBillNo());
			recLine.setFeeId(voucherLine.getFeeId());
			recLine.setOutStoreId(voucherLine.getOutStoreId());
			// 1.新增应收明细
			recLineDao.insert(recLine);

			BigDecimal writing_off_amount = DecimalUtil.subtract(voucherLine.getAmount(),
					voucherLine.getAmountChecked());
			// 校验 对账金额不能超过分录中的待对账金额
			if (DecimalUtil.gt(amount_check.abs(), writing_off_amount.abs())) {
				throw new BaseException(ExcMsgEnum.CHECK_AMOUNT_EXCCED, DecimalUtil.formatScale2(amount_check),
						DecimalUtil.formatScale2(writing_off_amount));
			}
			// 2.修改分录已对账金额
			voucherLine.setAmountChecked(DecimalUtil.add(voucherLine.getAmountChecked(), amount_check));
			voucherLineDao.updateById(voucherLine);
		}

		// 合并金额
		receive.setAmountReceivable(DecimalUtil.add(addAmount, receive.getAmountReceivable()));
		// 3.修改应收金额
		updateReceiveById(receive);
	}

	public List<ReceiveResDto> queryListResultByCon(ReceiveSearchReqDto req) {
		List<Receive> receives = receiveDao.queryResultsByCon(req);
		List<ReceiveResDto> receiveResDtos = new ArrayList<ReceiveResDto>();
		for (Receive receive : receives) {
			ReceiveResDto receiveResDto = convertToReceiveRes(receive);
			receiveResDto.setOpertaList(getOperList(receiveResDto));
			receiveResDtos.add(receiveResDto);
		}
		return receiveResDtos;
	}

	public PageResult<ReceiveResDto> queryResultsByCon(ReceiveSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		PageResult<ReceiveResDto> pageResult = new PageResult<ReceiveResDto>();
		int offSet = PageUtil.getOffSet(req.getPage(), req.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, req.getPer_page());
		List<Receive> receives = receiveDao.queryResultsByCon(req, rowBounds);
		List<ReceiveResDto> receiveResDtos = new ArrayList<ReceiveResDto>();
		for (Receive receive : receives) {
			ReceiveResDto receiveResDto = convertToReceiveRes(receive);
			receiveResDto.setOpertaList(getOperList(receiveResDto));
			receiveResDtos.add(receiveResDto);
		}
		if (req.getNeedSum() != null && req.getNeedSum().equals(BaseConsts.ONE)) {
			List<RecSumModel> list = receiveDao.querySumResultsByCon(req);
			BigDecimal amountReceivableSum = BigDecimal.ZERO;
			BigDecimal amountReceivedSum = BigDecimal.ZERO;
			BigDecimal amountUnReceivedSum = BigDecimal.ZERO;
			for (RecSumModel item : list) {
				amountReceivableSum = DecimalUtil.add(amountReceivableSum, ServiceSupport
						.amountNewToRMB(item.getAmountReceivableSum(), item.getCurrencyType(), new Date()));
				amountReceivedSum = DecimalUtil.add(amountReceivedSum,
						ServiceSupport.amountNewToRMB(item.getAmountReceivedSum(), item.getCurrencyType(), new Date()));
			}
			amountUnReceivedSum = DecimalUtil.subtract(amountReceivableSum, amountReceivedSum);
			String totalStr = "应收金额: " + DecimalUtil.toAmountString(amountReceivableSum) + " CNY   已收金额: "
					+ DecimalUtil.toAmountString(amountReceivedSum) + " CNY   未收金额: "
					+ DecimalUtil.toAmountString(amountUnReceivedSum) + " CNY";
			pageResult.setTotalStr(totalStr);

		}
		pageResult.setItems(receiveResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), req.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(req.getPage());
		pageResult.setPer_page(req.getPer_page());
		return pageResult;
	}

	/**
	 * 获取超期数据
	 * 
	 * @param req
	 * @param rowBounds
	 * @return
	 */
	public List<ReceiveResDto> queryOverDayResultsByCon(ReceiveSearchReqDto req, RowBounds rowBounds) {
		req.setUserId(ServiceSupport.getUser().getId());
		List<Receive> receives = receiveDao.queryResultsByOverDay(req, rowBounds);
		List<ReceiveResDto> receiveResDtos = new ArrayList<ReceiveResDto>();
		for (Receive receive : receives) {
			ReceiveResDto receiveResDto = convertToReceiveRes(receive);
			receiveResDto.setOpertaList(getOperList(receiveResDto));
			receiveResDtos.add(receiveResDto);
		}
		return receiveResDtos;
	}

	public List<ReceiveResDto> queryOverDayResultsByCon(ReceiveSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		List<Receive> receives = receiveDao.queryResultsByOverDay(req);
		List<ReceiveResDto> receiveResDtos = new ArrayList<ReceiveResDto>();
		if (CollectionUtils.isNotEmpty(receives)) {
			for (Receive receive : receives) {
				ReceiveResDto receiveResDto = convertToReceiveRes(receive);
				receiveResDtos.add(receiveResDto);
			}
		}
		return receiveResDtos;
	}

	/**
	 * 获取总数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public BigDecimal queryResultsSum(ReceiveSearchReqDto reqDto) {
		BigDecimal blance = receiveDao.queryResultsSum(reqDto);
		if (blance == null) {
			blance = BigDecimal.ZERO;
		}
		return blance;
	};

	private ReceiveResDto convertToReceiveRes(Receive receive) {
		ReceiveResDto resDto = new ReceiveResDto();
		BeanUtils.copyProperties(receive, resDto);
		resDto.setCreateAt(receive.getCreateAt());
		resDto.setBusiUnitName(cacheService.getSubjectNcByIdAndKey(receive.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(receive.getCustId(), CacheKeyConsts.CUSTOMER));
		resDto.setProjectName(cacheService.getProjectNameById(receive.getProjectId()));
		resDto.setAmountUnReceived(DecimalUtil.subtract(receive.getAmountReceivable(), receive.getAmountReceived()));
		resDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, receive.getCurrencyType() + ""));
		resDto.setAmountReceivable(receive.getAmountReceivable());
		resDto.setAmountReceived(receive.getAmountReceived());
		resDto.setBlance(DecimalUtil.subtract(receive.getAmountReceivable(), receive.getAmountReceived()));
		resDto.setCnyRate(receive.getCnyRate());
		return resDto;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	public List<CodeValue> getOperList(ReceiveResDto receiveResDto) {
		List<String> operNameList = new ArrayList<String>();
		operNameList.add(OperateConsts.DETAIL);
		// 未核销才能编辑和删除
		if (DecimalUtil.eq(DecimalUtil.ZERO, receiveResDto.getAmountReceived())) {
			operNameList.add(OperateConsts.EDIT);
			operNameList.add(OperateConsts.DELETE);
		}
		// 将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList, ReceiveResDto.Operate.operMap);
		return oprResult;
	}

	public void updateReceiveById(Receive receive) {
		receiveDao.updateById(receive);
	}

	public Receive queryEntityById(Integer id) {
		Receive receive = new Receive();
		receive = receiveDao.queryEntityById(id);
		return receive;
	}

	public void deleteReceiveById(Integer id) {
		Receive receive = queryEntityById(id);
		if (!DecimalUtil.eq(DecimalUtil.ZERO, receive.getAmountReceived())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "已核销的应收不能删除");
		}
		RecLineSearchReqDto req = new RecLineSearchReqDto();
		req.setRecId(id);
		List<RecLine> recLines = recLineDao.queryListByRecId(req);
		for (RecLine recLine : recLines) {
			VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId());
			voucherLine
					.setAmountChecked(DecimalUtil.subtract(voucherLine.getAmountChecked(), recLine.getAmountCheck()));
			// 1.修改分录已对账金额
			voucherLineDao.updateById(voucherLine);
			// 2.删除应收明细
			recLineDao.deleteById(recLine.getId());
		}
		// 删除应收
		receiveDao.deleteById(id);
	}

	public List<ReceiveResDto> queryResultsExcelByCon(ReceiveSearchReqDto req) {
		req.setUserId(ServiceSupport.getUser().getId());
		List<Receive> receives = receiveDao.queryResultsByCon(req);
		List<ReceiveResDto> receiveResDtos = new ArrayList<ReceiveResDto>();
		for (Receive receive : receives) {
			ReceiveResDto receiveResDto = convertToReceiveRes(receive);
			receiveResDto.setOpertaList(getOperList(receiveResDto));
			receiveResDtos.add(receiveResDto);
		}
		return receiveResDtos;
	}

	public void verifyRecAmount(List<ReceiveResDto> reclist, BankReceipt bankRec) {
		// 按时间排序核销金额
		BigDecimal balance = bankRec.getReceiptAmount();
		BigDecimal bankRecAmount = bankRec.getWriteOffAmount() == null ? BigDecimal.ZERO : bankRec.getWriteOffAmount();
		// 判断金额是否核销完成标志，完成结束循环
		boolean isFinished = false;
		if (reclist.size() > 0) {
			for (ReceiveResDto rec : reclist) {
				RecReceiptRel recReceiptRel = new RecReceiptRel();
				recReceiptRel.setReceiptId(bankRec.getId());
				recReceiptRel.setRecId(rec.getId());
				recReceiptRel.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
						: ServiceSupport.getUser().getChineseName());
				recReceiptRel.setCreatorId(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_ID
						: ServiceSupport.getUser().getId());
				recReceiptRel.setCreateAt(new Date());
				recReceiptRel.setCurrencyType(bankRec.getCurrencyType());
				// 如果大于0，表示还有剩余金额需要核销
				if (balance.compareTo(rec.getBlance()) > 0) {
					recReceiptRel.setWriteOffAmount(rec.getBlance());
					// 剩余核销的金额
					balance = DecimalUtil.subtract(balance, rec.getBlance());
					bankRecAmount = DecimalUtil.add(bankRecAmount, rec.getBlance());
				} else {
					recReceiptRel.setWriteOffAmount(balance);
					bankRecAmount = DecimalUtil.add(bankRecAmount, balance);
					isFinished = true;
				}
				recReceiptRelDao.insert(recReceiptRel);// 添加水单应收关系
				Receive upReceive = new Receive();// 修改应收金额
				upReceive.setId(rec.getId());
				upReceive.setAmountReceived(DecimalUtil.add(
						recReceiptRel.getAmountReceived() == null ? BigDecimal.ZERO : recReceiptRel.getAmountReceived(),
						recReceiptRel.getWriteOffAmount()));
				updateReceiveById(upReceive);
				// 更新出库单回款金额
				RecLineSearchReqDto recLineSearchReqDto = new RecLineSearchReqDto();
				recLineSearchReqDto.setRecId(rec.getId());
				recLineSearchReqDto.setBillType(BaseConsts.THREE); // 3-出库单
				RecLine recLine = recLineDao.queryRecLineByCon(recLineSearchReqDto);
				if (null != recLine && null != recLine.getOutStoreId()) {
					BillOutStore billOutStore = billOutStoreDao.queryEntityById(recLine.getOutStoreId());
					BillOutStore bill = new BillOutStore();
					bill.setId(recLine.getOutStoreId());
					bill.setReceivedAmount(DecimalUtil.add(billOutStore.getReceivedAmount() == null ? BigDecimal.ZERO
							: billOutStore.getReceivedAmount(), recReceiptRel.getWriteOffAmount()));
					billOutStoreDao.updateById(bill);

					BillDelivery billDelivery = billDeliveryDao
							.queryBillDeliveryByBillOutStoreId(recLine.getOutStoreId());
					if (null != billDelivery) {
						BillDelivery billDeliveryUpdate = new BillDelivery();
						billDeliveryUpdate.setId(billDelivery.getId());
						billDeliveryUpdate.setWholeReturnTime(bankRec.getReceiptDate());
						billDeliveryDao.updateById(billDeliveryUpdate);
					}
				}
				if (isFinished)
					break;
			}
			// 更新水单核销金额
			BankReceipt upBankReceipt = bankReceiptService.queryEntityById(bankRec.getId());// 修改水单核销金额
			upBankReceipt.setWriteOffAmount(
					DecimalUtil.add(bankRec.getWriteOffAmount() == null ? BigDecimal.ZERO : bankRec.getWriteOffAmount(),
							bankRecAmount));
			if (bankRec.getReceiptAmount().compareTo(upBankReceipt.getWriteOffAmount()) == 0) {
				upBankReceipt.setState(BaseConsts.THREE);
			}
			upBankReceipt.setActualWriteOffAmount(
					DecimalUtil.multiply(upBankReceipt.getWriteOffAmount(), upBankReceipt.getActualCurrencyRate()));
			upBankReceipt.setActualPreRecAmount(
					DecimalUtil.multiply(upBankReceipt.getPreRecAmount(), upBankReceipt.getActualCurrencyRate()));
			upBankReceipt.setActualPaidAmount(
					DecimalUtil.multiply(upBankReceipt.getPaidAmount(), upBankReceipt.getActualCurrencyRate()));
			upBankReceipt.setActualDiffAmount(
					DecimalUtil.multiply(upBankReceipt.getDiffAmount(), upBankReceipt.getActualCurrencyRate()));

			bankReceiptService.updateBankReceiptById(upBankReceipt);
		}
	}

}
