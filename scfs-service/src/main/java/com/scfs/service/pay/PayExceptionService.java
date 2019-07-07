package com.scfs.service.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.dao.fi.ReceiveDao;
import com.scfs.dao.fi.VoucherDao;
import com.scfs.dao.fi.VoucherLineDao;
import com.scfs.dao.interf.PmsPayOrderTitleDao;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.dao.logistics.BillInStoreTallyDtlDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.BillOutStorePickDtlDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.pay.entity.PayPoRelationModel;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.sale.dto.req.BillDeliverySearchReqDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: PayExceptionService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年2月28日				Administrator
 *
 * </pre>
 */
@Service
public class PayExceptionService {
	@Autowired
	PayOrderDao payOrderDao;
	@Autowired
	StlDao stlDao;
	@Autowired
	BillDeliveryDao billDeliveryDao;
	@Autowired
	BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	PayPoRelationService payPoRelationService;
	@Autowired
	PurchaseOrderService purchaseOrderService;
	@Autowired
	PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	BillInStoreTallyDtlDao billInStoreTallyDtlDao;
	@Autowired
	VoucherDao voucherDao;
	@Autowired
	VoucherLineDao voucherLineDao;
	@Autowired
	PmsPayOrderTitleDao pmsPayOrderTitleDao;
	@Autowired
	BillInStoreDao billInStoreDao;
	@Autowired
	BillOutStoreDao billOutStoreDao;
	@Autowired
	BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	BillOutStorePickDtlDao billOutStorePickDtlDao;
	@Autowired
	ReceiveDao receiveDao;
	@Autowired
	PurchaseOrderTitleDao purchaseOrderTitleDao;

	public void changeConfirmAt(Integer payId, Date newConfirmorDate) {
		PayOrder payOrder = payOrderDao.queryEntityById(payId);
		if (payOrder == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PayOrderDao.class, payId);
		}
		if (!payOrder.getState().equals(BaseConsts.SIX)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态有误，无法改确认时间");
		}
		PayOrder payOrderUpd = new PayOrder();
		payOrderUpd.setId(payId);
		payOrderUpd.setConfirmorAt(newConfirmorDate);
		// payOrderUpd.setpe
		payOrderDao.updateById(payOrderUpd); // 1.更新付款单付款时间
		boolean isPms = false;
		// 处理pms请款单
		PmsPoTitleSearchReqDto req = new PmsPoTitleSearchReqDto();
		if (!StringUtils.isEmpty(payOrder.getAttachedNumbe())) {
			req.setPayNo(payOrder.getAttachedNumbe());
			List<PmsPayOrderTitle> pmsPayOrderTitles = pmsPayOrderTitleDao.queryResultsByPayNo(req);
			if (!CollectionUtils.isEmpty(pmsPayOrderTitles)) { // pms请款单
				isPms = true;
			}
		}
		PayPoRelationReqDto payPoRelationReqDto = new PayPoRelationReqDto();
		payPoRelationReqDto.setPayId(payId);
		List<PayPoRelationModel> payPoRelationModels = payPoRelationService
				.queryPayPoRelationByCon(payPoRelationReqDto);
		if (CollectionUtils.isEmpty(payPoRelationModels)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单未找到采购单");
		}
		Map<String, Integer> inStoreMap = new HashMap<String, Integer>();
		for (int index = 0; index < payPoRelationModels.size(); index++) {
			PayPoRelationModel item = payPoRelationModels.get(index);
			PurchaseOrderLine poLineEntity = purchaseOrderService.queryPoLineEntityById(item.getPoLineId());
			PurchaseOrderLine poLineUpd = new PurchaseOrderLine();
			if (poLineEntity == null) {
				throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PurchaseOrderService.class, item.getPoLineId());
			}
			poLineUpd.setId(poLineEntity.getId());
			poLineUpd.setPayTime(newConfirmorDate);
			purchaseOrderLineDao.updatePurchaseOrderLineById(poLineUpd); // 2.更新采购订单行付款时间

			List<BillInStoreDtl> billInStoreDtls = billInStoreDtlDao.queryResultsByPoLineId(item.getPoLineId());
			if (!CollectionUtils.isEmpty(billInStoreDtls)) {
				for (BillInStoreDtl billInStoreDtl : billInStoreDtls) {
					inStoreMap.put("billInStoreId", billInStoreDtl.getBillInStoreId());
					BillInStoreDtl billInStoreDtlUpd = new BillInStoreDtl();
					billInStoreDtlUpd.setId(billInStoreDtl.getId());
					billInStoreDtlUpd.setPayTime(newConfirmorDate);
					if (isPms) {
						billInStoreDtlUpd.setReceiveDate(newConfirmorDate);
						billInStoreDtlUpd.setOriginAcceptTime(newConfirmorDate);
						billInStoreDtlUpd.setAcceptTime(newConfirmorDate);
					}
					billInStoreDtlDao.updateById(billInStoreDtlUpd); // 3.更新入库单明细pay_time
				}

				List<BillInStoreTallyDtl> billInStoreTallyDtls = billInStoreTallyDtlDao
						.queryResultsByPoLineId(item.getPoLineId());
				if (!CollectionUtils.isEmpty(billInStoreTallyDtls)) {
					for (BillInStoreTallyDtl billInStoreTallyDtl : billInStoreTallyDtls) {
						BillInStoreTallyDtl entity = billInStoreTallyDtlDao
								.queryEntityById(billInStoreTallyDtl.getId());
						if (entity == null) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, BillInStoreTallyDtlDao.class,
									billInStoreTallyDtl.getId());
						}
						BillInStoreTallyDtl billInStoreTallyDtlUpd = new BillInStoreTallyDtl();
						billInStoreTallyDtlUpd.setId(entity.getId());
						billInStoreTallyDtlUpd.setPayTime(newConfirmorDate);
						if (isPms) {
							billInStoreTallyDtlUpd.setReceiveDate(newConfirmorDate);
							billInStoreTallyDtlUpd.setOriginAcceptTime(newConfirmorDate);
							billInStoreTallyDtlUpd.setAcceptTime(newConfirmorDate);
						}
						billInStoreTallyDtlDao.updateById(billInStoreTallyDtlUpd); // 4.更新入库单理货明细pay_time
					}
				}
				List<Stl> stls = stlDao.queryResultsByPoLineId(item.getPoLineId());
				if (!CollectionUtils.isEmpty(stls)) {
					for (Stl stl : stls) {
						Stl entity = stlDao.queryEntityById(stl.getId());
						if (entity == null) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, StlDao.class, stl.getId());
						}
						Stl stlUpd = new Stl();
						stlUpd.setId(entity.getId());
						stlUpd.setPayTime(newConfirmorDate);
						if (isPms) {
							stlUpd.setReceiveDate(newConfirmorDate);
							stlUpd.setOriginAcceptTime(newConfirmorDate);
							stlUpd.setAcceptTime(newConfirmorDate);
						}
						stlDao.updateById(stlUpd); // 5.更新库存pay_time
					}
				}
			}
		}

		if (isPms) {
			PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
			poTitleReqDto.setAppendNo(payOrder.getAttachedNumbe());
			List<PurchaseOrderTitle> purchaseOrderTitles = purchaseOrderTitleDao
					.queryPurchaseOrderTitleList(poTitleReqDto);
			assert (purchaseOrderTitles.size() == 1);
			PurchaseOrderTitle purchaseOrderTitleUpd = new PurchaseOrderTitle();
			purchaseOrderTitleUpd.setId(purchaseOrderTitles.get(0).getId());
			purchaseOrderTitleUpd.setPerdictTime(newConfirmorDate);
			purchaseOrderTitleDao.updatePurchaseOrderTitleById(purchaseOrderTitleUpd);

			Set<Map.Entry<String, Integer>> entries = inStoreMap.entrySet();
			assert (entries.size() == 1);

			for (Map.Entry<String, Integer> entry : entries) {
				BillInStore billInStoreEntity = billInStoreDao.queryAndLockEntityById(entry.getValue());
				BillInStore billInStoreUpd = new BillInStore();
				billInStoreUpd.setId(entry.getValue());
				billInStoreUpd.setReceiveDate(newConfirmorDate);
				billInStoreUpd.setAcceptTime(newConfirmorDate);
				billInStoreDao.updateById(billInStoreUpd); // 6.更新入库单receive_date,accept_date

				Voucher inStoreParam = new Voucher();
				inStoreParam.setBillNo(billInStoreEntity.getBillNo());
				inStoreParam.setBillType(BaseConsts.TWO);
				Voucher inStoreVoucher = voucherDao.queryEntityByParam(inStoreParam);
				if (inStoreVoucher != null) {
					updateVoucher(inStoreVoucher, newConfirmorDate);
				} else {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "找不到入库单凭证");
				}
			}

			BillDeliverySearchReqDto billDeliverySearchReqDto = new BillDeliverySearchReqDto();
			billDeliverySearchReqDto.setProjectId(payOrder.getProjectId());
			billDeliverySearchReqDto.setAffiliateNo(payOrder.getAttachedNumbe());
			List<BillDelivery> billDeliveries = billDeliveryDao.queryResultsByCon(billDeliverySearchReqDto);
			if (!CollectionUtils.isEmpty(billDeliveries)) {
				Integer billDeliveryId = billDeliveries.get(0).getId();
				BillDelivery billDeliveryUpd = new BillDelivery();
				billDeliveryUpd.setId(billDeliveryId);
				billDeliveryUpd.setRequiredSendDate(newConfirmorDate);
				billDeliveryUpd.setReturnTime(newConfirmorDate);
				billDeliveryDao.updateById(billDeliveryUpd);

				BillOutStore billOutStoreSearch = new BillOutStore();
				billOutStoreSearch.setBillDeliveryId(billDeliveryId);
				List<BillOutStore> billOutStores = billOutStoreDao.queryByBillDeliveryId(billOutStoreSearch);
				if (!CollectionUtils.isEmpty(billOutStores)) {
					Integer billOutStoreId = billOutStores.get(0).getId();
					BillOutStore billOutStoreEntity = billOutStoreDao.queryEntityById(billOutStoreId);

					BillOutStore billOutStoreUpd = new BillOutStore();
					billOutStoreUpd.setId(billOutStoreId);
					billOutStoreUpd.setRequiredSendDate(newConfirmorDate);
					billOutStoreUpd.setDeliverTime(newConfirmorDate);
					billOutStoreUpd.setSendDate(newConfirmorDate);
					billOutStoreDao.updateById(billOutStoreUpd);

					BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto = new BillOutStoreDtlSearchReqDto();
					billOutStoreDtlSearchReqDto.setBillOutStoreId(billOutStoreId);
					List<BillOutStoreDtl> billOutStoreDtls = billOutStoreDtlDao
							.queryResultsByCon(billOutStoreDtlSearchReqDto);
					if (!CollectionUtils.isEmpty(billOutStoreDtls)) {
						for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtls) {
							BillOutStoreDtl billOutStoreDtlUpd = new BillOutStoreDtl();
							billOutStoreDtlUpd.setId(billOutStoreDtl.getId());
							billOutStoreDtlUpd.setPayTime(newConfirmorDate);
							billOutStoreDtlDao.updateById(billOutStoreDtlUpd);
						}
					} else {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "找不到提货单");
					}

					List<BillOutStorePickDtl> billOutStorePickDtls = billOutStorePickDtlDao
							.queryResultsByBillOutStoreId(billOutStoreId);
					if (!CollectionUtils.isEmpty(billOutStorePickDtls)) {
						for (BillOutStorePickDtl billOutStorePickDtl : billOutStorePickDtls) {
							BillOutStorePickDtl billOutStorePickDtlUpd = new BillOutStorePickDtl();
							billOutStorePickDtlUpd.setId(billOutStorePickDtl.getId());
							billOutStorePickDtlUpd.setPayTime(newConfirmorDate);
							billOutStorePickDtlDao.updateById(billOutStorePickDtlUpd);
						}
					} else {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "找不到拣货单");
					}

					Voucher outStoreParam = new Voucher();
					outStoreParam.setBillNo(billOutStoreEntity.getBillNo());
					outStoreParam.setBillType(BaseConsts.THREE);
					Voucher outStoreVoucher = voucherDao.queryEntityByParam(outStoreParam);
					if (outStoreVoucher != null) {
						updateVoucher(outStoreVoucher, newConfirmorDate);
					} else {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "找不到出库单凭证");
					}
					ReceiveSearchReqDto receiveSearchReqDto = new ReceiveSearchReqDto();
					receiveSearchReqDto.setBillNo(billOutStoreEntity.getBillNo());
					receiveSearchReqDto.setProjectId(billOutStoreEntity.getProjectId());
					List<Receive> receives = receiveDao.queryListByBillNo(BaseConsts.THREE,
							billOutStoreEntity.getBillNo());
					if (CollectionUtils.isEmpty(receives)) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "找不到应收");
					}
					if (receives.size() > 1) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收数据有误，请核查");
					}
					Receive receiveUpd = new Receive();
					receiveUpd.setId(receives.get(0).getId());
					receiveUpd.setCheckDate(newConfirmorDate);
					receiveDao.updateById(receiveUpd);
				} else {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "找不到提货单");
				}
			}
		}

		Voucher param = new Voucher();
		param.setBillNo(payOrder.getPayNo());
		param.setBillType(BaseConsts.FOUR);
		Voucher payVoucher = voucherDao.queryEntityByParam(param);
		if (payVoucher != null) {
			updateVoucher(payVoucher, newConfirmorDate);
		}
	}

	private void updateVoucher(Voucher voucher, Date voucherDate) {
		Voucher voucherUpd = new Voucher();
		voucherUpd.setId(voucher.getId());
		voucherUpd.setVoucherDate(voucherDate);
		voucherUpd.setBillDate(voucherDate);
		voucherUpd
				.setVoucherSummary(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, voucher.getBillType() + "")
						+ voucher.getBillNo() + "/单据日期"
						+ (DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, voucher.getBillDate())));
		voucherDao.updateById(voucherUpd); // 6.更新付款凭证日期及单据日期
		List<VoucherLine> voucherLines = voucherLineDao.queryResultsByVoucherId(voucher.getId());
		for (VoucherLine voucherLine : voucherLines) {
			VoucherLine voucherLineUpd = new VoucherLine();
			voucherLineUpd.setId(voucherLine.getId());
			voucherLineUpd.setBillDate(voucherDate);
			voucherLineUpd.setVoucherLineSummary(voucherUpd.getVoucherSummary());
			voucherLineDao.updateById(voucherLineUpd); // 7.更新付款凭证分录单据日期
		}
	}

}
