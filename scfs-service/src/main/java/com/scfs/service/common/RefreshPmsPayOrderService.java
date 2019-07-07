package com.scfs.service.common;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.fi.RecReceiptRelDao;
import com.scfs.dao.interf.PmsPayOrderTitleDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.common.dto.resp.RefreshFundUsedResDto;
import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.RecReceiptRel;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.service.interf.PmsPayOrderTitleService;

/**
 * Created by Administrator on 2017年5月27日.
 */
@Service
public class RefreshPmsPayOrderService {
	@Autowired
	private PmsPayOrderTitleDao pmsPayOrderTitleDao;
	@Autowired
	private PmsPayOrderTitleService pmsPayOrderTitleService;
	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	RecReceiptRelDao recReceiptRelDao;
	@Autowired
	RecLineDao recLineDao;
	@Autowired
	BillOutStoreDao billOutStoreDao;

	public void refreshPmsPayOrder(String billNo) {
		PmsPoTitleSearchReqDto req = new PmsPoTitleSearchReqDto();
		if (StringUtils.isNotBlank(billNo)) {
			req.setPayNo(billNo);
			List<PmsPayOrderTitle> pmsPayOrderTitles = pmsPayOrderTitleDao.queryResultsByPayNo(req);
			PayOrder payOrder = payOrderDao.queryEntityByAttachedNumbe(billNo);
			if (!CollectionUtils.isEmpty(pmsPayOrderTitles) && null != payOrder) { // pms请款单
				pmsPayOrderTitleService.confirmPmsPayOrder(pmsPayOrderTitles.get(0).getId(), payOrder.getId());
			}
		}
	}

	public void refreshFundUsed(String isVirtualReceipt) {
		if (StringUtils.isBlank(isVirtualReceipt) || !isVirtualReceipt.equals(String.valueOf(BaseConsts.ONE))) {
			isVirtualReceipt = null;
		}
		Integer isVirtual = isVirtualReceipt == null ? null : Integer.parseInt(isVirtualReceipt);
		List<RefreshFundUsedResDto> refreshFundUsedResDtoList = recReceiptRelDao.getRefreshFundUsedList(isVirtual);
		if (!CollectionUtils.isEmpty(refreshFundUsedResDtoList)) {
			for (RefreshFundUsedResDto refreshFundUsedResDto : refreshFundUsedResDtoList) {
				if (refreshFundUsedResDto.getBillSource().equals(BaseConsts.ONE)) { // 1、费用

				} else if (refreshFundUsedResDto.getBillSource().equals(BaseConsts.TWO)) { // 2、出库单
					RecLineSearchReqDto recLineSearchReqDto = new RecLineSearchReqDto();
					recLineSearchReqDto.setOutStoreId(refreshFundUsedResDto.getId());
					recLineSearchReqDto.setBillType(BaseConsts.THREE); // 3-出库单
					RecLine recLine = recLineDao.queryRecLineByCon(recLineSearchReqDto);
					if (null != recLine) {
						BillOutStore billOutStore = billOutStoreDao
								.queryAndLockEntityById(refreshFundUsedResDto.getId());
						BigDecimal payAmount = billOutStore.getPayAmount();
						BigDecimal receivedAmount = billOutStore.getReceivedAmount();
						Integer recId = recLine.getRecId();
						List<RecReceiptRel> recReceiptRelList = recReceiptRelDao.getFifoRecReceiptRelByRecId(recId,
								isVirtual);
						if (!CollectionUtils.isEmpty(recReceiptRelList)) {
							BigDecimal matchAmount = BigDecimal.ZERO;
							if (DecimalUtil.ge(payAmount, receivedAmount)) {
								matchAmount = receivedAmount;
							} else {
								matchAmount = payAmount;
							}
							for (RecReceiptRel recReceiptRel : recReceiptRelList) {
								if (DecimalUtil.le(matchAmount, BigDecimal.ZERO)) {
									break;
								}
								BigDecimal writeOffAmount = recReceiptRel.getWriteOffAmount();
								if (DecimalUtil.ge(matchAmount, writeOffAmount)) {
									RecReceiptRel upRecReceiptRel = new RecReceiptRel();
									upRecReceiptRel.setId(recReceiptRel.getId());
									upRecReceiptRel.setFundUsed(writeOffAmount);
									recReceiptRelDao.updateById(upRecReceiptRel);
									matchAmount = DecimalUtil.subtract(matchAmount, writeOffAmount);
								} else {
									RecReceiptRel upRecReceiptRel = new RecReceiptRel();
									upRecReceiptRel.setId(recReceiptRel.getId());
									upRecReceiptRel.setFundUsed(matchAmount);
									recReceiptRelDao.updateById(upRecReceiptRel);
									break;
								}
							}
						}
					}
				}
			}
		}
	}
}
