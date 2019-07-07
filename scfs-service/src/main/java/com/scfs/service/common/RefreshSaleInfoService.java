package com.scfs.service.common;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.BillOutStorePickDtlDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.service.logistics.BillOutStoreDtlService;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.sale.BillDeliveryDtlService;
import com.scfs.service.sale.BillDeliveryService;

/**
 * Created by Administrator on 2017年2月28日.
 */
@Service
public class RefreshSaleInfoService {
	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private BillDeliveryService billDeliveryService;
	@Autowired
	private BillDeliveryDtlService billDeliveryDtlService;
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private BillOutStorePickDtlDao billOutStorePickDtlDao;
	@Autowired
	private BillOutStoreDtlService billOutStoreDtlService;
	@Autowired
	private BillOutStorePickDtlService billOutStorePickDtlService;

	public void refreshSalePrice(String billNos) {
		String[] billNoList = StringUtils.split(billNos, ",");
		if (null != billNoList && billNoList.length > 0) {
			for (String billNo : billNoList) {
				BillDelivery billDelivery = billDeliveryService.queryEntityByBillNo(billNo);
				Integer billDeliveryId = billDelivery.getId();
				List<BillDeliveryDtl> BillDeliveryDtlList = billDeliveryDtlDao
						.queryResultsByBillDeliveryId(billDeliveryId);
				if (!CollectionUtils.isEmpty(BillDeliveryDtlList)) {
					for (BillDeliveryDtl billDeliveryDtl : BillDeliveryDtlList) {
						BigDecimal salePrice = projectItemService.getSalePrice(billDeliveryDtl.getStlId(),
								billDelivery.getReturnTime());
						BigDecimal profitPrice = projectItemService.getProfitPriceByStl(billDeliveryDtl.getStlId(),
								billDelivery.getReturnTime());

						BillDeliveryDtl billDeliveryDtlUpdate = new BillDeliveryDtl();
						billDeliveryDtlUpdate.setId(billDeliveryDtl.getId());
						billDeliveryDtlUpdate.setRequiredSendPrice(salePrice);
						billDeliveryDtlUpdate.setProfitPrice(profitPrice);
						billDeliveryDtlUpdate.setSaleGuidePrice(salePrice);
						billDeliveryDtlDao.updateById(billDeliveryDtlUpdate);
					}
					// 更新销售明细信息
					billDeliveryDtlService.updateBillDeliveryInfo(billDeliveryId);
				}

				BillOutStore billOutStore = billOutStoreService.queryValidBillOutStoreByBillDeliveryId(billDeliveryId);
				if (null != billOutStore) {
					BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto = new BillOutStoreDtlSearchReqDto();
					billOutStoreDtlSearchReqDto.setBillOutStoreId(billOutStore.getId());
					List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreDtlDao
							.selectList(billOutStoreDtlSearchReqDto);
					if (!CollectionUtils.isEmpty(billOutStoreDtlList)) {
						for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtlList) {
							Integer billDeliveryDtlId = billOutStoreDtl.getBillDeliveryDtlId();
							BillDeliveryDtl billDeliveryDtl = billDeliveryDtlDao.queryEntityById(billDeliveryDtlId);
							BillOutStoreDtl billOutStoreDtlUpdate = new BillOutStoreDtl();
							billOutStoreDtlUpdate.setId(billOutStoreDtl.getId());
							billOutStoreDtlUpdate.setSendPrice(billDeliveryDtl.getRequiredSendPrice());
							billOutStoreDtlUpdate.setSendAmount(
									DecimalUtil.multiply(billOutStoreDtl.getSendNum(), billOutStoreDtl.getSendPrice()));
							billOutStoreDtlUpdate.setPayPrice(billDeliveryDtl.getPayPrice());
							billOutStoreDtlUpdate.setPayTime(billDeliveryDtl.getPayTime());
							billOutStoreDtlDao.updateById(billOutStoreDtlUpdate);
							List<BillOutStorePickDtl> billOutStorePickDtlList = billOutStorePickDtlDao
									.queryResultsByBillOutStoreDtlId(billOutStoreDtl.getId());
							if (!CollectionUtils.isEmpty(billOutStorePickDtlList)) {
								for (BillOutStorePickDtl billOutStorePickDtl : billOutStorePickDtlList) {
									BillOutStorePickDtl billOutStorePickDtlUpdate = new BillOutStorePickDtl();
									billOutStorePickDtlUpdate.setId(billOutStorePickDtl.getId());
									billOutStorePickDtlUpdate.setSendPrice(billDeliveryDtl.getRequiredSendPrice());
									billOutStorePickDtlUpdate.setPayPrice(billDeliveryDtl.getPayPrice());
									billOutStorePickDtlUpdate.setPayTime(billDeliveryDtl.getPayTime());
									billOutStorePickDtlDao.updateById(billOutStorePickDtlUpdate);
								}
							}
							// 更新出库单明细的拣货数量、拣货金额、成本金额、订单金额
							billOutStorePickDtlService.updateBillOutStoreDtlPickInfo(billOutStoreDtl.getId());
						}
					}
					// 更新出库单发货数量、发货金额、拣货数量、拣货金额
					billOutStoreDtlService.updateBillOutStoreInfo(billOutStore);
				}
			}
		}
	}

}
