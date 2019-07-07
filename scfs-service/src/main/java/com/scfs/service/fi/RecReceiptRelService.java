package com.scfs.service.fi;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.fi.RecReceiptRelDao;
import com.scfs.dao.fi.ReceiptOutStoreRelDao;
import com.scfs.dao.fi.ReceiveDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelReqDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelSearchReqDto;
import com.scfs.domain.fi.dto.req.ReceiptOutRelReqDto;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.req.VlReceiptRelSearchReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.dto.resp.RecReceiptRelResDto;
import com.scfs.domain.fi.dto.resp.ReceiptOutRelResDto;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.fi.entity.ReceiptOutStoreRel;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.fi.entity.VlReceiptRel;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillOutStoreDetailSearchReqDto;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  应收业务相关
 *  File: RecReceiptRelService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月1日			Administrator
 *
 * </pre>
 */
@Service
public class RecReceiptRelService {
	@Autowired
	RecReceiptRelDao recReceiptRelDao;

	@Autowired
	CacheService cacheService;

	@Autowired
	ReceiveService receiveService; // 应收

	@Autowired
	BankReceiptService bankReceiptService; // 水单

	@Autowired
	RecLineDao recLineDao;

	@Autowired
	VoucherLineDao voucherLineDao;

	@Autowired
	VlReceiptRelService vlReceiptRelService;

	@Autowired
	BillOutStoreService outStoreService;

	@Autowired
	ReceiptOutStoreRelDao receiptOutStoreRelDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private ReceiveDao receiveDao;

	/**
	 * 获取应收数据
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	public PageResult<ReceiveResDto> queryResultsByCon(RecReceiptRelReqDto recReceiptRelReqDto) {
		PageResult<ReceiveResDto> result = new PageResult<ReceiveResDto>();
		int receiptId = recReceiptRelReqDto.getReceiptId();
		BankReceiptResDto bankReceipt = bankReceiptService.detailBankReceiptById(receiptId).getItems();
		ReceiveSearchReqDto req = new ReceiveSearchReqDto();
		if (bankReceipt.getProjectId() != null) {
			req.setProjectId(bankReceipt.getProjectId());
			req.setCustId(bankReceipt.getCustId());
			req.setBusiUnit(bankReceipt.getBusiUnit());
			// req.setCurrencyType(bankReceipt.getCurrencyType());
			req.setOrderType(BaseConsts.TWO);
			req.setSearchType(BaseConsts.FOUR);
			req.setPage(recReceiptRelReqDto.getPage());
			req.setPer_page(recReceiptRelReqDto.getPer_page());
			result = receiveService.queryResultsByCon(req);
			result.setTotalAmount(bankReceipt.getReceiptBlance());
		}
		return result;
	}

	/**
	 * 应收水单关系添加
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	public BaseResult createRecReceiptRel(RecReceiptRelReqDto recReceiptRelSearchReqDto) {
		BaseResult baseResult = new BaseResult();
		Date date = new Date();
		int receiptId = recReceiptRelSearchReqDto.getReceiptId();// 水单id
		Map<Integer, BigDecimal> tempMap = new HashMap<Integer, BigDecimal>();
		List<RecReceiptRel> relList = recReceiptRelSearchReqDto.getRelList();
		for (RecReceiptRel rel : relList) {
			RecReceiptRel recReceiptRel = new RecReceiptRel();
			int recId = rel.getRecId();
			BigDecimal writeOffAmount = rel.getWriteOffAmount();
			Receive receive = receiveService.queryEntityById(recId);
			if ((receive.getAmountReceivable().doubleValue()) * (writeOffAmount.doubleValue()) < 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "核销金额与应收金额必须同为正或者同为负");
			}
			if (!recReceiptRelSearchReqDto.getWtCheckWriteAmount().equals(BaseConsts.ONE)) {
				if (DecimalUtil.gt(DecimalUtil.add(receive.getAmountReceived(), writeOffAmount).abs(),
						receive.getAmountReceivable().abs())) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "核销金额不能大于应收金额，请核查");
				}
			}
			if (tempMap.containsKey(recId)) {
				tempMap.put(recId, DecimalUtil.add(tempMap.get(recId), writeOffAmount));
			} else {
				tempMap.put(recId, writeOffAmount);
			}
			// 水单供应商退款类型 做核完的时候不走 WtCheckWriteAmount 是否校验的标识
			if (!recReceiptRelSearchReqDto.getWtCheckWriteAmount().equals(BaseConsts.ONE)) {
				BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(receiptId).getItems();
				int count = recReceiptRelDao.queryCountByBankReceiptId(receiptId);
				if (count > 0 && null != bankReceiptResDto.getCurrencyType()
						&& !receive.getCurrencyType().equals(bankReceiptResDto.getCurrencyType())) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收币种必须与水单核销币种一致");
				}

				recReceiptRel.setReceiptId(receiptId);
				recReceiptRel.setRecId(recId);
				recReceiptRel.setWriteOffAmount(writeOffAmount);
				recReceiptRel.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
						: ServiceSupport.getUser().getChineseName());
				recReceiptRel.setCreatorId(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_ID
						: ServiceSupport.getUser().getId());
				recReceiptRel.setCreateAt(date);
				recReceiptRel.setCurrencyType(receive.getCurrencyType());
				recReceiptRelDao.insert(recReceiptRel);

				Receive upReceive = new Receive();// 修改应收金额
				upReceive.setId(recId);
				upReceive.setAmountReceived(DecimalUtil.add(receive.getAmountReceived(), writeOffAmount));
				receiveService.updateReceiveById(upReceive);

				BankReceipt upBankReceipt = new BankReceipt();// 修改水单核销金额
				upBankReceipt.setId(receiptId);
				upBankReceipt.setWriteOffAmount(DecimalUtil.add(bankReceiptResDto.getWriteOffAmount(), writeOffAmount));
				upBankReceipt.setActualWriteOffAmount(DecimalUtil.multiply(upBankReceipt.getWriteOffAmount(),
						bankReceiptResDto.getActualCurrencyRate()));
				upBankReceipt.setCurrencyType(receive.getCurrencyType());
				bankReceiptService.updateBankReceiptById(upBankReceipt);
			}
		}

		// 更新实际付款的金额
		BankReceipt bankReceipt = bankReceiptService.queryEntityById(receiptId); // 修改水单核销金额
		bankReceipt.setActualWriteOffAmount(
				DecimalUtil.multiply(bankReceipt.getWriteOffAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceipt.setActualPreRecAmount(
				DecimalUtil.multiply(bankReceipt.getPreRecAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceipt.setActualPaidAmount(
				DecimalUtil.multiply(bankReceipt.getPaidAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceipt.setActualDiffAmount(
				DecimalUtil.multiply(bankReceipt.getDiffAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceiptService.updateBankReceiptById(bankReceipt);

		List<RecLine> updRecLines = new ArrayList<RecLine>();
		List<VlReceiptRel> updVlReceiptRels = new ArrayList<VlReceiptRel>();
		for (Integer key : tempMap.keySet()) {
			BigDecimal amount = tempMap.get(key);
			RecLineSearchReqDto req = new RecLineSearchReqDto();
			req.setRecId(key);
			req.setSearchType(BaseConsts.FOUR);
			List<RecLine> recLines = recLineDao.queryListByRecId(req); // 查询待核销的应收明细
																		// 按凭证日期升序排序
			Iterator<RecLine> recLineIterator = recLines.iterator();
			while (recLineIterator.hasNext()) { // 将核销金额分摊到具体的分录上
				RecLine recLine = recLineIterator.next();
				// 加悲观锁
				recLine = recLineDao.queryEntityById(recLine.getId());
				if (recLine.getAmountCheck().doubleValue() * amount.doubleValue() < 0) {
					continue;
				}
				VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId()); // 分录
				VlReceiptRelSearchReqDto searchReqDto = new VlReceiptRelSearchReqDto();
				searchReqDto.setReceiptId(receiptId);
				searchReqDto.setVoucherLineId(voucherLine.getId());
				VlReceiptRel vlReceiptRel = vlReceiptRelService.queryEntityByReceiptAndLineId(searchReqDto);

				RecLine updRecLine = new RecLine();
				BigDecimal amountUnChecked = DecimalUtil.subtract(recLine.getAmountCheck(),
						recLine.getWriteOffAmount()); // 待核销金额

				BigDecimal tempAmount = BigDecimal.ZERO;
				if (DecimalUtil.eq(DecimalUtil.ZERO, amount)) {
					break;
				}
				if (DecimalUtil.gt(amountUnChecked.abs(), amount.abs())) {
					updRecLine.setId(recLine.getId());
					updRecLine.setWriteOffAmount(DecimalUtil.add(recLine.getWriteOffAmount(), amount));
					tempAmount = amount;
					amount = DecimalUtil.ZERO;

				} else {
					updRecLine.setId(recLine.getId());
					updRecLine.setWriteOffAmount(recLine.getAmountCheck());
					amount = DecimalUtil.subtract(amount, amountUnChecked);
					tempAmount = amountUnChecked;
				}
				if (vlReceiptRel != null) {
					VlReceiptRel updVlReceiptRel = new VlReceiptRel();
					updVlReceiptRel.setId(vlReceiptRel.getId());
					updVlReceiptRel.setWriteOffAmount(DecimalUtil.add(vlReceiptRel.getWriteOffAmount(), tempAmount));
					updVlReceiptRels.add(updVlReceiptRel);
				} else {
					VlReceiptRel vlReceiptRel2 = new VlReceiptRel();
					vlReceiptRel2.setVoucherLineId(voucherLine.getId());
					vlReceiptRel2.setReceiptId(receiptId);
					vlReceiptRel2.setWriteOffAmount(tempAmount);
					vlReceiptRelService.insert(vlReceiptRel2);
				}
				updRecLines.add(updRecLine);
			}
		}

		for (VlReceiptRel item : updVlReceiptRels) {
			vlReceiptRelService.updateById(item);
		}
		for (RecLine item : updRecLines) {
			recLineDao.updateById(item);
		}

		return baseResult;
	}

	/**
	 * 获取详情
	 * 
	 * @param recReceiptRelReqDto
	 * @return
	 */
	public Result<RecReceiptRelResDto> editRecReceiptRelById(RecReceiptRelReqDto recReceiptRelReqDto) {
		Result<RecReceiptRelResDto> result = new Result<RecReceiptRelResDto>();
		RecReceiptRelResDto reReceiptDto = convertToRecReceiptRelResDto(
				recReceiptRelDao.queryDetailById(recReceiptRelReqDto.getId()));
		result.setItems(reReceiptDto);
		return result;
	}

	/**
	 * 应收水单关系金额修改
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	public BaseResult updateRecReceiptRelById(RecReceiptRelReqDto recReceiptRelResDto) {
		BaseResult baseResult = new BaseResult();
		Map<Integer, BigDecimal> tempMap = new HashMap<Integer, BigDecimal>();
		List<RecReceiptRel> relList = recReceiptRelResDto.getRelList();
		int receiptId = 0;
		for (RecReceiptRel rel : relList) {
			BigDecimal writeOffAmount = rel.getWriteOffAmount();
			// 判断输入金额是否小于费余额
			RecReceiptRel recReceiptRel = recReceiptRelDao.queryEntityById(rel.getId());// 已录入金额
			Receive receive = receiveService.queryEntityById(recReceiptRel.getRecId());// 应收
			if ((receive.getAmountReceivable().doubleValue()) * (writeOffAmount.doubleValue()) < 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "核销金额与应收金额必须同为正或者同为负");
			}
			BigDecimal diffPayAmount = DecimalUtil.subtract(receive.getAmountReceived(),
					recReceiptRel.getWriteOffAmount());// 已付款金额减去上次录入
			BigDecimal amountReceivable = receive.getAmountReceivable();// 应收金额
			BigDecimal nowAmout = DecimalUtil.add(writeOffAmount, diffPayAmount);
			if (DecimalUtil.lt(amountReceivable.abs(), nowAmout.abs())) {
				baseResult.setMsg("应收余额不足！");
				return baseResult;
			}
		}
		for (RecReceiptRel rel : relList) {
			int id = rel.getId();

			BigDecimal writeOffAmount = rel.getWriteOffAmount();
			// 计算修改差额并修改
			RecReceiptRel oldEntity = recReceiptRelDao.queryEntityById(id);
			BigDecimal diffPayAmount = DecimalUtil.subtract(writeOffAmount, oldEntity.getWriteOffAmount());// 本次输入与上次金额差额
			// 修改本次金额
			RecReceiptRel upRecReceiptRel = new RecReceiptRel();
			upRecReceiptRel.setId(id);
			upRecReceiptRel.setWriteOffAmount(writeOffAmount);
			recReceiptRelDao.updateById(upRecReceiptRel);
			// 修改费用已收应收金额
			int recId = oldEntity.getRecId();
			Receive receive = receiveService.queryEntityById(recId);
			if (tempMap.containsKey(recId)) {
				tempMap.put(recId, DecimalUtil.add(tempMap.get(recId), diffPayAmount));
			} else {
				tempMap.put(recId, diffPayAmount);
			}
			Receive upReceive = new Receive();
			upReceive.setId(recId);
			upReceive.setAmountReceived(DecimalUtil.add(receive.getAmountReceived(), diffPayAmount));
			receiveService.updateReceiveById(upReceive);
			// 修改水单核销总额
			receiptId = oldEntity.getReceiptId();
			BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(receiptId).getItems();
			BankReceipt upBankReceipt = new BankReceipt();
			upBankReceipt.setId(receiptId);
			upBankReceipt.setWriteOffAmount(DecimalUtil.add(bankReceiptResDto.getWriteOffAmount(), diffPayAmount));
			upBankReceipt.setActualWriteOffAmount(
					DecimalUtil.multiply(upBankReceipt.getWriteOffAmount(), bankReceiptResDto.getActualCurrencyRate()));
			bankReceiptService.updateBankReceiptById(upBankReceipt);
		}

		List<RecLine> updRecLines = new ArrayList<RecLine>();
		List<VlReceiptRel> updVlReceiptRels = new ArrayList<VlReceiptRel>();
		for (Integer key : tempMap.keySet()) {
			BigDecimal diffAmount = tempMap.get(key); // 差额
			RecLineSearchReqDto req = new RecLineSearchReqDto();
			req.setRecId(key);
			List<RecLine> recLines = recLineDao.queryListByRecId(req);
			// Collections.sort(recLines, new RecLineComparator()); //按凭证日期升序排序
			Iterator<RecLine> recLineIterator = recLines.iterator();
			while (recLineIterator.hasNext()) {
				BigDecimal tempAmount = DecimalUtil.ZERO;
				RecLine recLine = recLineIterator.next();
				recLine = recLineDao.queryEntityById(recLine.getId());
				BigDecimal amountUnChecked = DecimalUtil.subtract(recLine.getAmountCheck(),
						recLine.getWriteOffAmount());

				VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId()); // 分录
				VlReceiptRelSearchReqDto searchReqDto = new VlReceiptRelSearchReqDto();
				searchReqDto.setReceiptId(receiptId);
				searchReqDto.setVoucherLineId(voucherLine.getId());
				VlReceiptRel vlReceiptRel = vlReceiptRelService.queryEntityByReceiptAndLineId(searchReqDto);
				RecLine updRecLine = new RecLine();

				if (DecimalUtil.eq(DecimalUtil.ZERO, diffAmount)) {
					break;
				}
				boolean isAdd = true;
				if ((recLine.getAmountCheck().doubleValue() > 0 && diffAmount.doubleValue() < 0)
						|| (recLine.getAmountCheck().doubleValue() < 0 && diffAmount.doubleValue() > 0)) {
					isAdd = false;
				}
				if (isAdd) {
					if (DecimalUtil.gt(amountUnChecked.abs(), diffAmount.abs())) {
						updRecLine.setId(recLine.getId());
						updRecLine.setWriteOffAmount(DecimalUtil.add(recLine.getWriteOffAmount(), diffAmount));
						tempAmount = diffAmount;
						diffAmount = DecimalUtil.ZERO;

					} else {
						updRecLine.setId(recLine.getId());
						updRecLine.setWriteOffAmount(recLine.getAmountCheck());
						diffAmount = DecimalUtil.subtract(diffAmount, amountUnChecked);
						tempAmount = amountUnChecked;
					}
					if (vlReceiptRel != null) {
						VlReceiptRel updVlReceiptRel = new VlReceiptRel();
						updVlReceiptRel.setId(vlReceiptRel.getId());
						updVlReceiptRel
								.setWriteOffAmount(DecimalUtil.add(vlReceiptRel.getWriteOffAmount(), tempAmount));
						updVlReceiptRels.add(updVlReceiptRel);
					} else {
						VlReceiptRel vlReceiptRel2 = new VlReceiptRel();
						vlReceiptRel2.setVoucherLineId(voucherLine.getId());
						vlReceiptRel2.setReceiptId(receiptId);
						vlReceiptRel2.setWriteOffAmount(tempAmount);
						vlReceiptRelService.insert(vlReceiptRel2);
					}
					updRecLines.add(updRecLine);
				} else {
					VlReceiptRel updVlReceiptRel = new VlReceiptRel();
					if (vlReceiptRel != null) {
						BigDecimal writeOffAmount = vlReceiptRel.getWriteOffAmount(); // 已核销金额
						if (DecimalUtil.gt(writeOffAmount.abs(), diffAmount.abs())) {
							updVlReceiptRel.setId(vlReceiptRel.getId());
							updVlReceiptRel.setWriteOffAmount(DecimalUtil.add(writeOffAmount, diffAmount));
							updVlReceiptRels.add(updVlReceiptRel); // 更新水单与分录关系金额

							updRecLine.setId(recLine.getId());
							updRecLine.setWriteOffAmount(DecimalUtil.add(recLine.getWriteOffAmount(), diffAmount));
							updRecLines.add(updRecLine); // 更新应收明细已核销金额
							diffAmount = DecimalUtil.ZERO;
						} else {
							updVlReceiptRel.setId(vlReceiptRel.getId());
							updVlReceiptRel.setWriteOffAmount(BigDecimal.ZERO);
							updVlReceiptRels.add(updVlReceiptRel);

							updRecLine.setId(recLine.getId());
							updRecLine.setWriteOffAmount(
									DecimalUtil.subtract(recLine.getWriteOffAmount(), writeOffAmount));
							updRecLines.add(updRecLine);
							diffAmount = DecimalUtil.add(writeOffAmount, diffAmount);
						}
					}
				}
			}
		}
		for (VlReceiptRel item : updVlReceiptRels) {
			vlReceiptRelService.updateById(item);
		}
		for (RecLine item : updRecLines) {
			recLineDao.updateById(item);
		}
		return baseResult;
	}

	/**
	 * 解除关系
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	public BaseResult deleteRecReceiptRelById(RecReceiptRelSearchReqDto recReceiptRelSearchReqDto) {
		Map<Integer, BigDecimal> tempMap = new HashMap<Integer, BigDecimal>();
		int receiptId = 0;
		for (Integer id : recReceiptRelSearchReqDto.getIds()) {
			RecReceiptRel recReceiptRel = recReceiptRelDao.queryEntityById(id);
			receiptId = recReceiptRel.getReceiptId();
			int recId = recReceiptRel.getRecId();
			BigDecimal writeOffAmount = recReceiptRel.getWriteOffAmount();

			if (tempMap.containsKey(recId)) {
				tempMap.put(recId, DecimalUtil.add(tempMap.get(recId), writeOffAmount));
			} else {
				tempMap.put(recId, writeOffAmount);
			}

			// 修改应收金额
			Receive receive = receiveService.queryEntityById(recId);
			Receive upReceive = new Receive();
			upReceive.setId(recId);
			upReceive.setAmountReceived(DecimalUtil.subtract(receive.getAmountReceived(), writeOffAmount));
			receiveService.updateReceiveById(upReceive);

			// 修改水单核销金额
			BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(receiptId).getItems();
			BankReceipt upBankReceipt = new BankReceipt();
			upBankReceipt.setId(receiptId);
			upBankReceipt
					.setWriteOffAmount(DecimalUtil.subtract(bankReceiptResDto.getWriteOffAmount(), writeOffAmount));
			upBankReceipt.setActualWriteOffAmount(
					DecimalUtil.multiply(upBankReceipt.getWriteOffAmount(), bankReceiptResDto.getActualCurrencyRate()));
			bankReceiptService.updateBankReceiptById(upBankReceipt);

			// 删除
			recReceiptRelDao.deleteById(id);
		}
		List<RecLine> updRecLines = new ArrayList<RecLine>();
		List<VlReceiptRel> updVlReceiptRels = new ArrayList<VlReceiptRel>();
		for (Integer key : tempMap.keySet()) {
			BigDecimal amount = tempMap.get(key);
			RecLineSearchReqDto req = new RecLineSearchReqDto();
			req.setRecId(key);
			req.setSearchType(BaseConsts.FIVE);
			List<RecLine> recLines = recLineDao.queryListByRecId(req); // 查询已核销的应收明细
			// Collections.sort(recLines, new RecLineComparator()); //按凭证日期升序排序
			Iterator<RecLine> recLineIterator = recLines.iterator();
			while (recLineIterator.hasNext()) {
				RecLine recLine = recLineIterator.next();
				recLine = recLineDao.queryEntityById(recLine.getId());
				VoucherLine voucherLine = voucherLineDao.queryEntityById(recLine.getVoucherLineId());
				RecLine updRecLine = new RecLine();
				if (DecimalUtil.eq(DecimalUtil.ZERO, amount)) {
					break;
				}
				VlReceiptRelSearchReqDto searchReqDto = new VlReceiptRelSearchReqDto();
				searchReqDto.setReceiptId(receiptId);
				searchReqDto.setVoucherLineId(voucherLine.getId());
				VlReceiptRel vlReceiptRel = vlReceiptRelService.queryEntityByReceiptAndLineId(searchReqDto);
				VlReceiptRel updVlReceiptRel = new VlReceiptRel();
				if (vlReceiptRel != null) {
					BigDecimal writeOffAmount = vlReceiptRel.getWriteOffAmount(); // 已核销金额
					if (DecimalUtil.gt(writeOffAmount.abs(), amount.abs())) {
						updVlReceiptRel.setId(vlReceiptRel.getId());
						updVlReceiptRel.setWriteOffAmount(DecimalUtil.subtract(writeOffAmount, amount));
						updVlReceiptRels.add(updVlReceiptRel); // 更新水单与分录关系金额

						updRecLine.setId(recLine.getId());
						updRecLine.setWriteOffAmount(DecimalUtil.subtract(recLine.getWriteOffAmount(), amount));
						updRecLines.add(updRecLine); // 更新应收明细已核销金额
						amount = DecimalUtil.ZERO;
					} else {
						updVlReceiptRel.setId(vlReceiptRel.getId());
						updVlReceiptRel.setWriteOffAmount(BigDecimal.ZERO);
						updVlReceiptRels.add(updVlReceiptRel);

						updRecLine.setId(recLine.getId());
						updRecLine.setWriteOffAmount(DecimalUtil.subtract(recLine.getWriteOffAmount(), writeOffAmount));
						updRecLines.add(updRecLine);
						amount = DecimalUtil.subtract(amount, writeOffAmount);
					}
				}
			}
		}
		for (VlReceiptRel item : updVlReceiptRels) {
			vlReceiptRelService.updateById(item);
		}
		for (RecLine item : updRecLines) {
			recLineDao.updateById(item);
		}
		return new BaseResult();
	}

	/**
	 * 获取所有应收相关数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public List<RecReceiptRel> queryListByCon(RecReceiptRelSearchReqDto reqDto) {
		return recReceiptRelDao.queryResultsByCon(reqDto);
	}

	/**
	 * 查询分页数据
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	public PageResult<RecReceiptRelResDto> queryRecReceiptRelResultsByCon(
			RecReceiptRelSearchReqDto recReceiptRelSearchReqDto) {

		PageResult<RecReceiptRelResDto> pageResult = new PageResult<RecReceiptRelResDto>();
		Integer receiptId = recReceiptRelSearchReqDto.getReceiptId();// 获取水单id
		if (!StringUtils.isEmpty(receiptId)) {
			BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptId).getItems();
			BigDecimal blance = bankReceipt.getReceiptBlance();
			String totalStr = "水单金额  : " + DecimalUtil.toAmountString(bankReceipt.getReceiptAmount())
					+ "&nbsp;&nbsp;&nbsp;尾差: " + DecimalUtil.toAmountString(bankReceipt.getDiffAmount())
					+ "&nbsp;&nbsp;&nbsp;核销金额: " + DecimalUtil.toAmountString(bankReceipt.getWriteOffAmount())
					+ "&nbsp;&nbsp;&nbsp;预收金额: " + DecimalUtil.toAmountString(bankReceipt.getPreRecAmount())
					+ "&nbsp;&nbsp;&nbsp;余额: " + DecimalUtil.toAmountString(blance);
			pageResult.setTotalStr(totalStr);
			pageResult.setTotalAmount(blance);
		}

		int offSet = PageUtil.getOffSet(recReceiptRelSearchReqDto.getPage(), recReceiptRelSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, recReceiptRelSearchReqDto.getPer_page());
		List<RecReceiptRelResDto> recReceiptRelResDto = convertToRecReceiptRelDtos(
				recReceiptRelDao.queryResultsByCon(recReceiptRelSearchReqDto, rowBounds));
		pageResult.setItems(recReceiptRelResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), recReceiptRelSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(recReceiptRelSearchReqDto.getPage());
		pageResult.setPer_page(recReceiptRelSearchReqDto.getPer_page());
		return pageResult;
	}

	public List<RecReceiptRelResDto> convertToRecReceiptRelDtos(List<RecReceiptRel> result) {
		List<RecReceiptRelResDto> recReceiptRelResDtos = new ArrayList<RecReceiptRelResDto>();
		if (ListUtil.isEmpty(result)) {
			return recReceiptRelResDtos;
		}
		for (RecReceiptRel recReceiptRel : result) {
			RecReceiptRelResDto recReceiptRelResDto = convertToRecReceiptRelResDto(recReceiptRel);
			recReceiptRelResDtos.add(recReceiptRelResDto);
		}
		return recReceiptRelResDtos;
	}

	public RecReceiptRelResDto convertToRecReceiptRelResDto(RecReceiptRel recReceiptRel) {
		RecReceiptRelResDto resDto = new RecReceiptRelResDto();
		BeanUtils.copyProperties(recReceiptRel, resDto);
		resDto.setProjectName(cacheService.getProjectNameById(recReceiptRel.getProjectId()));
		resDto.setBusiUnit(cacheService.getSubjectNcByIdAndKey(recReceiptRel.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(recReceiptRel.getCustId(), CacheKeyConsts.CUSTOMER));
		resDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				recReceiptRel.getCurrencyType() + ""));
		resDto.setBlance(DecimalUtil.subtract(recReceiptRel.getAmountReceivable(), recReceiptRel.getAmountReceived()));
		resDto.setReceiptTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.RECEIPT_TYPE, recReceiptRel.getReceiptType() + ""));
		resDto.setRecAccountNo(cacheService.getAccountNoById(recReceiptRel.getRecAccountNo()));
		return resDto;
	}

	public List<RecReceiptRel> getRecReceiptRelByReceiptId(Integer receiptId) {
		return recReceiptRelDao.getRecReceiptRelByReceiptId(receiptId);
	}

	/**
	 * 查询分页数据
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	public PageResult<ReceiptOutRelResDto> queryReceiptOutRelByCon(ReceiptOutRelReqDto relReqDto) {
		PageResult<ReceiptOutRelResDto> pageResult = new PageResult<ReceiptOutRelResDto>();
		Integer receiptId = relReqDto.getReceiptId();// 获取水单id
		if (!StringUtils.isEmpty(receiptId)) {// 水单id不为空
			// 获取水单的详情信息
			BankReceiptResDto bankReceipt = bankReceiptService.editBankReceiptById(receiptId).getItems();
			BigDecimal blance = bankReceipt.getReceiptBlance();
			String totalStr = "水单金额  : " + DecimalUtil.toAmountString(bankReceipt.getReceiptAmount())
					+ "&nbsp;&nbsp;&nbsp;尾差: " + DecimalUtil.toAmountString(bankReceipt.getDiffAmount())
					+ "&nbsp;&nbsp;&nbsp;核销金额: " + DecimalUtil.toAmountString(bankReceipt.getWriteOffAmount())
					+ "&nbsp;&nbsp;&nbsp;预收金额: " + DecimalUtil.toAmountString(bankReceipt.getPreRecAmount())
					+ "&nbsp;&nbsp;&nbsp;余额: " + DecimalUtil.toAmountString(blance);
			pageResult.setTotalStr(totalStr);
			pageResult.setTotalAmount(blance);
		}
		// 带分页的查询当前水单和出库单的关系数据
		int offSet = PageUtil.getOffSet(relReqDto.getPage(), relReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, relReqDto.getPer_page());
		List<ReceiptOutRelResDto> receiptOutRelResDtos = convertToRecOutRelDtos(
				receiptOutStoreRelDao.queryResultsByCon(relReqDto, rowBounds));
		// 查询应收和水单的关系数据
		RecReceiptRelSearchReqDto recReceiptRelSearchReqDto = new RecReceiptRelSearchReqDto();
		recReceiptRelSearchReqDto.setReceiptId(receiptId);
		RowBounds rowBound = new RowBounds(offSet, recReceiptRelSearchReqDto.getPer_page());
		List<RecReceiptRel> receiptRels = recReceiptRelDao.queryResultsByCon(recReceiptRelSearchReqDto, rowBound);
		if (!CollectionUtils.isEmpty(receiptRels)) {
			for (RecReceiptRel recReceiptRel : receiptRels) {
				ReceiptOutRelResDto receiptOutRelResDto = convertToRecOutResDto(recReceiptRel);
				receiptOutRelResDtos.add(receiptOutRelResDto);
			}
		}
		pageResult.setItems(receiptOutRelResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), relReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(relReqDto.getPage());
		pageResult.setPer_page(relReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 封装当前水单出库的关联数据
	 * 
	 * @param receiptOutStoreRels
	 * @return
	 */
	public List<ReceiptOutRelResDto> convertToRecOutRelDtos(List<ReceiptOutStoreRel> receiptOutStoreRels) {
		List<ReceiptOutRelResDto> resDtos = new ArrayList<ReceiptOutRelResDto>();
		if (ListUtil.isEmpty(receiptOutStoreRels)) {
			return resDtos;
		}
		for (ReceiptOutStoreRel outStoreRel : receiptOutStoreRels) {
			ReceiptOutRelResDto recReceiptRelResDto = convertToRecOutRelResDto(outStoreRel);
			resDtos.add(recReceiptRelResDto);
		}
		return resDtos;
	}

	/**
	 * 水单关联出库单的数据封装
	 * 
	 * @param outStoreRel
	 * @return
	 */
	public ReceiptOutRelResDto convertToRecOutRelResDto(ReceiptOutStoreRel outStoreRel) {
		ReceiptOutRelResDto resDto = new ReceiptOutRelResDto();
		BeanUtils.copyProperties(outStoreRel, resDto);
		resDto.setOutStroeId(outStoreRel.getBillOutId());// 出库单ID
		resDto.setProjectName(cacheService.getProjectNameById(outStoreRel.getProjectId()));// 项目名称
		resDto.setBusiUnit(cacheService.getSubjectNcByIdAndKey(outStoreRel.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));// 经营单位
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(outStoreRel.getCustomerId(), CacheKeyConsts.CUSTOMER));
		resDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				outStoreRel.getCurrencyType() + ""));
		Integer billType = outStoreRel.getBillType();
		if (!StringUtils.isEmpty(billType)) {
			resDto.setBillType(outStoreRel.getBillType());// 单据类型
			if (billType.equals(BaseConsts.THREE)) {// 出库单类型
				BillOutStore billOutStore = billOutStoreDao.queryEntityById(outStoreRel.getBillOutId());
				if (billOutStore != null) {
					resDto.setBillTime(billOutStore.getSendDate());
				}
				resDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (billType) + ""));
				resDto.setWriteOffAmount(outStoreRel.getWriteOffAmount());// 核销金额
				resDto.setReceivedAmount(outStoreRel.getReceivedAmount());// 回款金额
			} else {
				PoLineModel lineModel = purchaseOrderLineDao.queryTitleAndLineByLineID(outStoreRel.getBillOutId());
				if (lineModel != null) {
					resDto.setBillTime(lineModel.getOrderTime());
				}
				resDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, (billType) + ""));
				resDto.setWriteOffAmount(DecimalUtil.multiply(new BigDecimal("-1"), outStoreRel.getWriteOffAmount()));// 核销金额
				resDto.setReceivedAmount(DecimalUtil.multiply(new BigDecimal("-1"), outStoreRel.getReceivedAmount()));// 回款金额
			}
		}
		return resDto;
	}

	/**
	 * 水单关联应收单的数据封装
	 * 
	 * @param outStoreRel
	 * @return
	 */
	public ReceiptOutRelResDto convertToRecOutResDto(RecReceiptRel receiptRel) {
		ReceiptOutRelResDto resDto = new ReceiptOutRelResDto();
		BeanUtils.copyProperties(receiptRel, resDto);
		Receive receive = receiveDao.queryEntityById(receiptRel.getRecId());
		if (receive != null) {
			resDto.setReceivedAmount(receive.getAmountReceived());// 已收金额
		}
		resDto.setOutStroeId(receiptRel.getId());// 出库单ID
		resDto.setProjectName(cacheService.getProjectNameById(receiptRel.getProjectId()));// 项目名称
		resDto.setBusiUnit(cacheService.getSubjectNcByIdAndKey(receiptRel.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));// 经营单位
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(receiptRel.getCustId(), CacheKeyConsts.CUSTOMER));
		resDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				receiptRel.getCurrencyType() + ""));
		resDto.setWriteOffAmount(receiptRel.getWriteOffAmount());// 核销金额
		resDto.setCreateAt(receiptRel.getCreateAt());// 创建日期
		resDto.setBillTime(receiptRel.getCheckDate());// 单据日前
		resDto.setBillType(BaseConsts.INT_36);// 单据类型
		resDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, (BaseConsts.INT_36) + ""));
		return resDto;
	}

	/**
	 * 获取出库数据
	 * 
	 * @param recReceiptRelSearchReqDto
	 * @return
	 */
	public PageResult<ReceiptOutRelResDto> queryRecOutResultsByCon(ReceiptOutRelReqDto relReqDto) {
		PageResult<ReceiptOutRelResDto> result = new PageResult<ReceiptOutRelResDto>();
		int receiptId = relReqDto.getReceiptId();
		BankReceiptResDto bankReceipt = bankReceiptService.detailBankReceiptById(receiptId).getItems();
		BillOutStoreDetailSearchReqDto dto = new BillOutStoreDetailSearchReqDto();
		if (bankReceipt.getProjectId() != null) {
			dto.setProjectId(bankReceipt.getProjectId());// 项目id
			dto.setCustomerId(bankReceipt.getCustId());// 客户ID
			dto.setCurrencyType(bankReceipt.getCurrencyType());// 币种
			dto.setSupplierId(bankReceipt.getCustId());
			dto.setBillType(BaseConsts.FIVE);// 退货类型
			result = outStoreService.queryOutStoreResutByCon(dto);
			List<ReceiptOutRelResDto> list = result.getItems();
			// 查询应收的数据
			RecReceiptRelReqDto recReceiptRelReqDto = new RecReceiptRelReqDto();
			recReceiptRelReqDto.setReceiptId(receiptId);
			PageResult<ReceiveResDto> pageResult = this.queryResultsByCon(recReceiptRelReqDto);
			List<ReceiveResDto> resDtos = pageResult.getItems();
			if (!CollectionUtils.isEmpty(resDtos)) {
				for (ReceiveResDto receiveResDto : resDtos) {
					ReceiptOutRelResDto resDto = new ReceiptOutRelResDto();
					resDto.setId(receiveResDto.getId());
					resDto.setCreateAt(receiveResDto.getCheckDate());// 对账日期
					resDto.setBusiUnit(receiveResDto.getBusiUnitName()); // 经营单位
					resDto.setCustName(receiveResDto.getCustName());// 客户名称
					resDto.setCurrencyTypeName(receiveResDto.getCurrencyTypeName());// 币种
					resDto.setReceivedAmount(receiveResDto.getAmountReceived());// 应收金额
					resDto.setProjectName(receiveResDto.getProjectName());
					resDto.setWriteOffAmount(receiveResDto.getBlance()); // 核销金额
					resDto.setBillType(BaseConsts.INT_36);// 应收
					resDto.setBillTypeName(
							ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, (BaseConsts.INT_36) + ""));
					list.add(resDto);
				}
			}
			result.setItems(list);
			result.setTotalAmount(bankReceipt.getReceiptBlance());
		}
		return result;
	}

	/**
	 * 新增水单和出库单的关系
	 * 
	 * @param relReqDto
	 */
	public void createRecOutRelByCon(ReceiptOutRelReqDto relReqDto) {
		// 根据水单ID查询水单数据
		if (relReqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单核销出库单数据位空");
		}
		// 获取水单数据
		BankReceiptResDto bankReceipt = bankReceiptService.detailBankReceiptById(relReqDto.getReceiptId()).getItems();
		BankReceipt upBankReceipt = new BankReceipt();// 修改水单核销金额
		BigDecimal writeAmout = BigDecimal.ZERO;
		// 新增水单和出库单的关系
		List<BillOutStore> outStores = relReqDto.getRelList();
		if (CollectionUtils.isNotEmpty(outStores)) {
			for (BillOutStore billOutStore : outStores) {
				billOutStore = billOutStoreDao.queryEntityById(billOutStore.getId());
				if (billOutStore != null) {
					BigDecimal sendAmount = billOutStore.getSendAmount();
					writeAmout = DecimalUtil.add(writeAmout, sendAmount);
					if (DecimalUtil.lt(bankReceipt.getReceiptBlance(), sendAmount)) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "核销金额大于水单余额");
					}
					ReceiptOutStoreRel receiptOutStoreRel = new ReceiptOutStoreRel();
					receiptOutStoreRel.setBillOutId(billOutStore.getId());// 出库单ID
					receiptOutStoreRel.setBankReceiptId(bankReceipt.getId());// 水单ID
					receiptOutStoreRel.setProjectId(billOutStore.getProjectId());// 项目ID
					receiptOutStoreRel
							.setBusiUnit(cacheService.getProjectById(billOutStore.getProjectId()).getBusinessUnitId());
					// 经营单位
					receiptOutStoreRel.setCustomerId(billOutStore.getCustomerId());// 客户ID
					receiptOutStoreRel.setCurrencyType(billOutStore.getCurrencyType());
					receiptOutStoreRel.setReceivedAmount(
							DecimalUtil.add(billOutStore.getReceivedAmount(), billOutStore.getSendAmount()));// 回款金额
					receiptOutStoreRel.setWriteOffAmount(billOutStore.getSendAmount());// 核销金额
																						// 发货金额
					receiptOutStoreRel.setCreateAt(new Date());
					receiptOutStoreRel.setBillType(BaseConsts.THREE);// 出库单单据类型
					receiptOutStoreRelDao.insertSelective(receiptOutStoreRel);
					if (DecimalUtil.ge(billOutStore.getReceivedAmount(), billOutStore.getSendAmount())) {// 如果回款金额大于并等于发货金额
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "出库单回款金额大于发货金额");
					}
					billOutStore.setReceivedAmount(
							DecimalUtil.add(billOutStore.getReceivedAmount(), receiptOutStoreRel.getWriteOffAmount()));
					// billOutStore.setSendAmount(DecimalUtil.subtract(billOutStore.getSendAmount(),receiptOutStoreRel.getWriteOffAmount()));
					billOutStoreDao.updateById(billOutStore);

				}
			}
		}
		// 新增业务 添加采购铺货结算单的明细关系
		List<PurchaseOrderLine> lines = relReqDto.getOrderLines();
		if (!CollectionUtils.isEmpty(lines)) {
			for (PurchaseOrderLine purchaseOrderLine : lines) {
				PoLineModel lineModel = purchaseOrderLineDao
						.queryPurchaseOrderLineAndTitleById(purchaseOrderLine.getId());
				if (lineModel != null) {
					BigDecimal writeOffAmount = DecimalUtil.subtract(lineModel.getRefundAmount(),
							lineModel.getPaidAmount());
					writeAmout = DecimalUtil.add(writeAmout,
							DecimalUtil.multiply(new BigDecimal("-1"), writeOffAmount));
					if (DecimalUtil.lt(bankReceipt.getReceiptBlance(),
							DecimalUtil.multiply(new BigDecimal("-1"), writeOffAmount))) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "核销金额大于水单余额");
					}
					ReceiptOutStoreRel receiptOutStoreRel = new ReceiptOutStoreRel();
					receiptOutStoreRel.setBillOutId(lineModel.getId());// 采购单铺货结算单明细ID
					receiptOutStoreRel.setBankReceiptId(bankReceipt.getId());// 水单ID
					receiptOutStoreRel.setProjectId(lineModel.getProjectId());// 项目ID
					receiptOutStoreRel
							.setBusiUnit(cacheService.getProjectById(lineModel.getProjectId()).getBusinessUnitId());// 经营单位
					receiptOutStoreRel.setCustomerId(lineModel.getCustomerId());// 客户ID
					receiptOutStoreRel.setCurrencyType(lineModel.getCurrencyId());
					receiptOutStoreRel.setReceivedAmount(DecimalUtil.add(lineModel.getPaidAmount(), writeOffAmount));// 退款金额
					receiptOutStoreRel.setWriteOffAmount(writeOffAmount);// 付款金额
					receiptOutStoreRel.setCreateAt(new Date());
					receiptOutStoreRel.setBillType(BaseConsts.INT_33);// 采购单铺货结算单
					receiptOutStoreRelDao.insertSelective(receiptOutStoreRel);
					// 金额回写
					purchaseOrderLine = purchaseOrderLineDao.queryPurchaseOrderLineById(purchaseOrderLine.getId());
					purchaseOrderLine
							.setPaidAmount(DecimalUtil.add(lineModel.getPaidAmount(), lineModel.getRefundAmount()));
					purchaseOrderLineDao.updatePurchaseOrderLineById(purchaseOrderLine);

				}
			}

		}
		upBankReceipt.setId(relReqDto.getReceiptId());
		upBankReceipt.setWriteOffAmount(DecimalUtil.add(bankReceipt.getWriteOffAmount(), writeAmout));
		upBankReceipt.setActualWriteOffAmount(
				DecimalUtil.multiply(upBankReceipt.getWriteOffAmount(), bankReceipt.getActualCurrencyRate()));
		bankReceiptService.updateBankReceiptById(upBankReceipt);
		// 应收关系的添加
		RecReceiptRelReqDto recReceiptRelSearchReqDto = new RecReceiptRelReqDto();
		recReceiptRelSearchReqDto.setReceiptId(bankReceipt.getId());
		recReceiptRelSearchReqDto.setRelList(relReqDto.getRels());
		this.createRecReceiptRel(recReceiptRelSearchReqDto);
	}

	/**
	 * 批量删除水单出库单关联数据
	 * 
	 * @param relReqDto
	 */
	public void deleteRecOutRelByCon(ReceiptOutRelReqDto relReqDto) {
		if (relReqDto == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "批量删除水单核销出库单数据位空");
		}
		List<Integer> list = relReqDto.getIds();
		List<Integer> list2 = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			BigDecimal writeOffAmount = BigDecimal.ZERO;
			for (Integer id : list) {
				// 删除关系 回写当前出库单或者是铺货结算单的数据
				ReceiptOutStoreRel rel = receiptOutStoreRelDao.selectByPrimaryKey(id);
				if (rel != null) {
					if (rel.getBillType().equals(BaseConsts.THREE)) {// 出库单回写
						BillOutStore billOutStore = billOutStoreDao.queryEntityById(rel.getBillOutId());
						billOutStore
								.setSendAmount(DecimalUtil.add(billOutStore.getSendAmount(), rel.getWriteOffAmount()));
						billOutStore.setReceivedAmount(
								DecimalUtil.subtract(billOutStore.getReceivedAmount(), rel.getWriteOffAmount()));
						writeOffAmount = DecimalUtil.add(writeOffAmount, rel.getWriteOffAmount());
						billOutStoreDao.updateById(billOutStore);
					} else if (rel.getBillType().equals(BaseConsts.INT_33)) {// 铺货结算单回写
						PurchaseOrderLine purchaseOrderLine = purchaseOrderLineDao
								.queryPurchaseOrderLineById(rel.getBillOutId());
						purchaseOrderLine.setPaidAmount(
								DecimalUtil.subtract(purchaseOrderLine.getPaidAmount(), rel.getWriteOffAmount()));
						writeOffAmount = DecimalUtil.add(writeOffAmount,
								DecimalUtil.multiply(new BigDecimal("-1"), rel.getWriteOffAmount()));
						purchaseOrderLineDao.updatePurchaseOrderLineById(purchaseOrderLine);
					}
					receiptOutStoreRelDao.updateRecOutByCon(id);
				} else { // 删除应收
					list2.add(id); // 封装当前ID
				}
			}
			// 修改水单核销金额
			BankReceiptResDto bankReceiptResDto = bankReceiptService.editBankReceiptById(relReqDto.getReceiptId())
					.getItems();
			BankReceipt upBankReceipt = new BankReceipt();
			upBankReceipt.setId(relReqDto.getReceiptId());
			upBankReceipt
					.setWriteOffAmount(DecimalUtil.subtract(bankReceiptResDto.getWriteOffAmount(), writeOffAmount));
			upBankReceipt.setActualWriteOffAmount(
					DecimalUtil.multiply(upBankReceipt.getWriteOffAmount(), bankReceiptResDto.getActualCurrencyRate()));
			bankReceiptService.updateBankReceiptById(upBankReceipt);
			// 根据当前返回的id来删除应收和水单的关系
			if (CollectionUtils.isNotEmpty(list2)) {
				RecReceiptRelSearchReqDto recReceiptRelSearchReqDto = new RecReceiptRelSearchReqDto();
				recReceiptRelSearchReqDto.setIds(list2);
				deleteRecReceiptRelById(recReceiptRelSearchReqDto);
			}
		}
	}

}
