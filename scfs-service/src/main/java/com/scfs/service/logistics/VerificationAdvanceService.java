package com.scfs.service.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.testng.collections.Lists;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.logistics.VerificationAdvanceDao;
import com.scfs.domain.fi.dto.req.AdvanceSearchReqDto;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.req.RecReceiptRelReqDto;
import com.scfs.domain.fi.entity.AdvanceReceiptRel;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.logistics.dto.req.VeriAdvanceSearchReqDto;
import com.scfs.domain.logistics.entity.VerificationAdvance;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.fi.AdvanceManagerService;
import com.scfs.service.fi.AdvanceService;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.fi.RecReceiptRelService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: VerificationAdvanceService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年12月27日			Administrator
 *
 * </pre>
 */

@Service
public class VerificationAdvanceService {
	@Autowired
	VerificationAdvanceDao verificationAdvanceDao;

	@Autowired
	AdvanceManagerService advanceManagerService;

	@Autowired
	AdvanceService advanceService;

	@Autowired
	BankReceiptService bankReceiptService;

	@Autowired
	BankReceiptDao bankReceiptDao;

	@Autowired
	RecLineDao recLineDao;

	@Autowired
	ReceiveService receiveService;

	@Autowired
	RecReceiptRelService recReceiptRelService;

	@Autowired
	BillOutStoreService billOutStoreService;

	public void dealReceiptAdvance(BillDelivery billDelivery, Integer id) {
		VerificationAdvance verificationAdvance = verificationAdvanceDao.queryEntityById(id);
		if (verificationAdvance == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, VerificationAdvanceDao.class, id);
		}
		if (StringUtils.isEmpty(verificationAdvance.getReceiptId())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单id不能为空");
		}
		receiptToAdvance(billDelivery, verificationAdvance);
	}

	/**
	 * 水单转预收
	 * 
	 * @param verificationAdvance
	 */
	private void receiptToAdvance(BillDelivery billDelivery, VerificationAdvance verificationAdvance) {
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(verificationAdvance.getReceiptId());
		AdvanceReceiptRel advanceReceiptRel = convertToAdvanceRel(bankReceipt, verificationAdvance.getAmount());
		if (bankReceipt == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, BankReceiptDao.class,
					advanceReceiptRel.getReceiptId());
		}
		bankReceipt.setProjectId(billDelivery.getProjectId());
		bankReceipt.setCustId(billDelivery.getCustomerId());
		if (bankReceipt.getState() != BaseConsts.TWO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单状态为"
					+ ServiceSupport.getValueByBizCode(BizCodeConsts.RECEIPT_STATUS, bankReceipt.getState() + "")
					+ "不能转预收");
		}
		if (StringUtils.isEmpty(bankReceipt.getProjectId())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单项目为空，转预收失败");
		}
		if (StringUtils.isEmpty(bankReceipt.getCustId())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单客户为空，转预收失败");
		}
		bankReceiptDao.updateById(bankReceipt);
		Integer advanceReceiptRelId = advanceService.createAdvanceRel(advanceReceiptRel);
		// 回写水单转预收生成的水单与预收关系id
		VerificationAdvance vaUpd = new VerificationAdvance();
		vaUpd.setId(verificationAdvance.getId());
		vaUpd.setAdvanceReceiptRelId(advanceReceiptRelId);
		verificationAdvanceDao.updateById(vaUpd);

	}

	private AdvanceReceiptRel convertToAdvanceRel(BankReceipt bankReceipt, BigDecimal amount) {
		AdvanceReceiptRel advanceReceiptRel = new AdvanceReceiptRel();
		advanceReceiptRel.setReceiptId(bankReceipt.getId());
		switch (bankReceipt.getReceiptType()) {
		case BaseConsts.ONE:
		case BaseConsts.THREE:
			advanceReceiptRel.setAdvanceType(BaseConsts.TWO); // 转货款预收
			break;
		case BaseConsts.TWO:
			advanceReceiptRel.setAdvanceType(BaseConsts.ONE); // 转定金预收
			break;
		default:
			advanceReceiptRel.setAdvanceType(BaseConsts.TWO); // 转货款预收
			break;
		}
		advanceReceiptRel.setExchangeAmount(amount);
		advanceReceiptRel.setDeletePrivFlag(BaseConsts.ONE); // 前台不能删除该条记录
		return advanceReceiptRel;
	}

	public void addVerificationAdvance(VerificationAdvance verificationAdvance) {
		verificationAdvance.setCreator(ServiceSupport.getUser().getChineseName());
		verificationAdvance.setCreatorId(ServiceSupport.getUser().getId());
		verificationAdvance.setCreateAt(new Date());
		verificationAdvance.setIsDelete(BaseConsts.ZERO);

		verificationAdvanceDao.insert(verificationAdvance);
	}

	public void rollBackReceiptAdvance(Integer billDeliveryId) {
		List<VerificationAdvance> verificationAdvanceList = verificationAdvanceDao
				.queryResultByBillDeliveryId(billDeliveryId);
		if (!CollectionUtils.isEmpty(verificationAdvanceList)) {
			for (VerificationAdvance verificationAdvance : verificationAdvanceList) {
				BankReceipt receipt = new BankReceipt();
				receipt.setId(verificationAdvance.getReceiptId());
				// TODO 回退
				rollbackVeriAdvance(verificationAdvance);
				verificationAdvance.setIsDelete(BaseConsts.ONE); // 1-已删除
				verificationAdvanceDao.updateById(verificationAdvance);
			}
		}
	}

	public void rollbackVeriAdvance(VerificationAdvance verificationAdvance) {
		int receiptId = verificationAdvance.getReceiptId();
		if (StringUtils.isEmpty(receiptId)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单id为空，无法驳回");
		}
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(receiptId);
		if (bankReceipt == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, BankReceiptDao.class, receiptId);
		}
		if (bankReceipt.getState() != BaseConsts.TWO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单【" + bankReceipt.getReceiptNo() + "】状态不为待核销，无法驳回");
		}
		// 判断水单类型驳回
		if (bankReceipt.getReceiptType() == BaseConsts.ONE || bankReceipt.getReceiptType() == BaseConsts.TWO
				|| bankReceipt.getReceiptType() == BaseConsts.THREE) { // 回款类型的水单
			/**
			 * 1.删除水单转预收关联 2.更新水单
			 */
			Integer advanceReceiptRelId = verificationAdvance.getAdvanceReceiptRelId();
			if (StringUtils.isEmpty(advanceReceiptRelId)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "无法找到水单转成预收的关联id，数据有误请核查");
			}
			AdvanceSearchReqDto advanceSearchReq = new AdvanceSearchReqDto();
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(advanceReceiptRelId);
			advanceSearchReq.setIds(ids);
			advanceService.deleteAdvanceRelById(advanceSearchReq);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单类型有误");
		}
	}

	/**
	 * 出库单送货，水单核销应收 1.回款类型的水单，剩余金额转预收货款水单，该水单关联的其他销售单水单id更新为预收货款水单，核完
	 * 2.预收货款类型的水单，剩余金额转预收货款水单，该水单关联的其他销售单水单id更新为预收货款水单，核完
	 * 3.预收定金类型的水单，剩余金额转预收定金水单，该水单关联的其他销售单水单id更新为预收定金水单，核完 sourceType 1-正常出库单核销
	 * 2-应收保理业务核销 3-融通质押业务核销
	 * 
	 * @param recId,
	 *            billOutStoreNo, billDeliveryId, sourceType
	 */
	public void verificationReceipt(Integer recId, String billOutStoreNo, Integer billDeliveryId, Integer sourceType) {
		List<VerificationAdvance> verificationAdvances = verificationAdvanceDao
				.queryResultByBillDeliveryId(billDeliveryId);
		if (!CollectionUtils.isEmpty(verificationAdvances)) {
			for (VerificationAdvance item : verificationAdvances) {
				dealReceipt2(item, recId, sourceType);
			}
		}
	}

	/**
	 * 出库单送货，水单核销应收 1.回款类型的水单，剩余金额转预收货款水单，该水单关联的其他销售单水单id更新为预收货款水单，核完
	 * 2.预收货款类型的水单，剩余金额转预收货款水单，该水单关联的其他销售单水单id更新为预收货款水单，核完
	 * 3.预收定金类型的水单，剩余金额转预收定金水单，该水单关联的其他销售单水单id更新为预收定金水单，核完 sourceType
	 * 默认1-正常出库单核销 2-应收保理业务核销 3-融通质押业务核销
	 * 
	 * @param recId,
	 *            billOutStoreNo, billDeliveryId, sourceType
	 */
	public void verificationReceipt(Integer recId, String billOutStoreNo, Integer billDeliveryId) {
		List<VerificationAdvance> verificationAdvances = verificationAdvanceDao
				.queryResultByBillDeliveryId(billDeliveryId);
		if (!CollectionUtils.isEmpty(verificationAdvances)) {
			for (VerificationAdvance item : verificationAdvances) {
				dealReceipt2(item, recId);
			}
		}
	}

	private void dealReceipt2(VerificationAdvance verificationAdvance, Integer recId) {
		dealReceipt2(verificationAdvance, recId, BaseConsts.ONE);
	}

	private void dealReceipt2(VerificationAdvance verificationAdvance, Integer recId, Integer sourceType) {
		int receiptId = verificationAdvance.getReceiptId();
		BankReceipt bankReceipt = bankReceiptDao.queryEntityById(receiptId);
		if (bankReceipt == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, BankReceiptDao.class, receiptId);
		}
		VeriAdvanceSearchReqDto req = new VeriAdvanceSearchReqDto();
		req.setReceiptId(receiptId);
		List<VerificationAdvance> verificationAdvances = verificationAdvanceDao.queryResultsByCon(req);

		if (!CollectionUtils.isEmpty(verificationAdvances)) {
			BigDecimal remainAmount = DecimalUtil
					.formatScale2(bankReceipt.getReceiptAmount().add(bankReceipt.getDiffAmount())
							.subtract(bankReceipt.getPreRecAmount()).subtract(bankReceipt.getWriteOffAmount()));

			AdvanceSearchReqDto advanceSearchReq = new AdvanceSearchReqDto();
			List<Integer> ids = new ArrayList<Integer>();
			BigDecimal preAmount = DecimalUtil.ZERO;
			for (VerificationAdvance curItem : verificationAdvances) {
				if (!curItem.getId().equals(verificationAdvance.getId())) {
					preAmount = DecimalUtil.add(preAmount, curItem.getAmount());
				}
				if (null != curItem.getAdvanceReceiptRelId()) {
					ids.add(curItem.getAdvanceReceiptRelId());
				}
			}
			advanceSearchReq.setIds(ids);
			advanceService.deleteAdvanceRelById(advanceSearchReq); // 1.删除水单转成的预收

			RecReceiptRelReqDto recReceiptRelReqDto = new RecReceiptRelReqDto();
			recReceiptRelReqDto.setReceiptId(bankReceipt.getId());
			List<RecReceiptRel> receiptRels = new ArrayList<RecReceiptRel>();
			RecReceiptRel recReceiptRel = new RecReceiptRel();
			recReceiptRel.setRecId(recId);
			recReceiptRel.setWriteOffAmount(verificationAdvance.getAmount());
			receiptRels.add(recReceiptRel);
			recReceiptRelReqDto.setRelList(receiptRels);
			recReceiptRelService.createRecReceiptRel(recReceiptRelReqDto); // 2.添加水单与应收关系

			BigDecimal sumPreAmount = DecimalUtil.formatScale2(DecimalUtil.add(preAmount, remainAmount));
			if (sourceType.equals(BaseConsts.ONE)) {
				// 3.将剩余的金额转成预收
				if (!DecimalUtil.eq(sumPreAmount, DecimalUtil.ZERO)) {
					AdvanceReceiptRel advanceReceiptRel = convertToAdvanceRel(bankReceipt, sumPreAmount);
					advanceService.createAdvanceRel(advanceReceiptRel);
				}
			}

			bankReceiptService.submitBankReceiptByState(bankReceipt); // 4.自动核完

			if (sourceType.equals(BaseConsts.ONE)) {
				if (verificationAdvances.size() > 1) {
					BankReceiptSearchReqDto bankReceiptSearchReqDto = new BankReceiptSearchReqDto();
					if (bankReceipt.getReceiptType().equals(BaseConsts.ONE)) {
						bankReceiptSearchReqDto.setPid(bankReceipt.getId());
						bankReceiptSearchReqDto.setReceiptType(BaseConsts.THREE);
					} else if (bankReceipt.getReceiptType().equals(BaseConsts.TWO)) {
						bankReceiptSearchReqDto.setPid(bankReceipt.getPid());
						bankReceiptSearchReqDto.setReceiptType(BaseConsts.TWO);
					} else if (bankReceipt.getReceiptType().equals(BaseConsts.THREE)) {
						bankReceiptSearchReqDto.setPid(bankReceipt.getPid());
						bankReceiptSearchReqDto.setReceiptType(BaseConsts.THREE);
					}
					bankReceiptSearchReqDto.setState(BaseConsts.TWO);
					List<BankReceipt> bankReceipts = bankReceiptDao.queryResultsByCon(bankReceiptSearchReqDto);
					if (!CollectionUtils.isEmpty(bankReceipts) && !DecimalUtil.eq(sumPreAmount, DecimalUtil.ZERO)) {
						Integer preReceiptId = null;
						for (BankReceipt brItem : bankReceipts) {
							if (DecimalUtil.eq(sumPreAmount, brItem.getReceiptAmount())) {
								preReceiptId = brItem.getId();
								break;
							}
						}
						if (preReceiptId != null) {
							BankReceipt pBankReceipt = bankReceiptDao.queryEntityById(preReceiptId);
							for (VerificationAdvance vaItem : verificationAdvances) {
								if (!vaItem.getId().equals(verificationAdvance.getId())) {

									AdvanceReceiptRel advanceReceiptRel = convertToAdvanceRel(pBankReceipt,
											vaItem.getAmount());
									Integer advanceReceiptRelId = advanceService.createAdvanceRel(advanceReceiptRel);
									// 回写水单转预收生成的水单与预收关系id
									VerificationAdvance vaUpdate = new VerificationAdvance();
									vaUpdate.setId(vaItem.getId());
									vaUpdate.setReceiptId(preReceiptId);
									vaUpdate.setAdvanceReceiptRelId(advanceReceiptRelId);
									verificationAdvanceDao.updateById(vaUpdate);// 5.将该水单关联的tb_verification_advance表的receipt_id更新为该水单剩余金额转成的预收水单id
								}
							}
						}
					}
				}
			}
		}
	}

	public List<BankReceipt> queryBankReceiptByBillDeliveryId(Integer billDeliveryId) {
		List<BankReceipt> bankReceiptList = Lists.newArrayList();
		List<VerificationAdvance> verificationAdvanceList = verificationAdvanceDao
				.queryResultByBillDeliveryId(billDeliveryId);
		if (!CollectionUtils.isEmpty(verificationAdvanceList)) {
			for (VerificationAdvance verificationAdvance : verificationAdvanceList) {
				if (null != verificationAdvance.getReceiptId()) {
					BankReceiptSearchReqDto bankReceiptSearchReqDto = new BankReceiptSearchReqDto();
					bankReceiptSearchReqDto.setId(verificationAdvance.getReceiptId());
					BankReceipt bankReceipt = bankReceiptDao
							.queryResultsById4BillDeliveryAudit(bankReceiptSearchReqDto);
					if (null != bankReceipt) {
						BigDecimal verificationAdvanceAmount = (null == verificationAdvance.getAmount()
								? BigDecimal.ZERO : verificationAdvance.getAmount());
						BigDecimal remainAmount = (null == bankReceipt.getRemainAmount() ? BigDecimal.ZERO
								: bankReceipt.getRemainAmount());
						bankReceipt.setRemainAmount(DecimalUtil.add(verificationAdvanceAmount, remainAmount));
						bankReceipt.setVerificationAdvanceAmount(verificationAdvanceAmount);
						bankReceiptList.add(bankReceipt);
					}
				}
			}
		}
		return bankReceiptList;
	}
}
