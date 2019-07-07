package com.scfs.service.po;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.po.PurchasePackPrintDao;
import com.scfs.domain.po.dto.resp.PurchasePackPrintResDto;
import com.scfs.domain.po.entity.PurchasePackPrint;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: PurchasePackPrint.java
 *  Description:采购单装箱打印信息
 *  TODO
 *  Date,					Who,				
 *  2017年12月19日				Administrator
 *
 * </pre>
 */
@Service
public class PurchasePackPrintService {
	@Autowired
	private PurchasePackPrintDao purchasePackPrintDao;

	/**
	 * 获取采购单相关信息
	 * 
	 * @param ids
	 * @return
	 */
	public PageResult<PurchasePackPrintResDto> queryPoLinesByPoTitleByIds(List<Integer> ids) {
		PageResult<PurchasePackPrintResDto> result = new PageResult<PurchasePackPrintResDto>();
		List<PurchasePackPrintResDto> poLineList = purchasePackPrintDao.queryPoLineListByPoIds(ids);
		if (CollectionUtils.isNotEmpty(poLineList)) {
			for (PurchasePackPrintResDto resDto : poLineList) {
				if (resDto.getId() != null) {
					PurchasePackPrint packPrint = purchasePackPrintDao.queryEntityById(resDto.getId());
					resDto.setPackages(packPrint.getPackages());
					resDto.setNetWeight(packPrint.getNetWeight());
					resDto.setGrossWeight(packPrint.getGrossWeight());
					resDto.setVolume(packPrint.getVolume());
				}
			}
		}
		result.setItems(poLineList);
		return result;
	}

	/**
	 * 获取销售单相关信息
	 * 
	 * @param ids
	 * @return
	 */
	public PageResult<PurchasePackPrintResDto> querySaleLinesByIds(List<Integer> ids) {
		PageResult<PurchasePackPrintResDto> result = new PageResult<PurchasePackPrintResDto>();
		List<PurchasePackPrintResDto> poLineList = purchasePackPrintDao.queryPoLineListBySaleIds(ids);
		if (CollectionUtils.isNotEmpty(poLineList)) {
			for (PurchasePackPrintResDto resDto : poLineList) {
				if (resDto.getId() != null) {
					PurchasePackPrint packPrint = purchasePackPrintDao.queryEntityById(resDto.getId());
					resDto.setPackages(packPrint.getPackages());
					resDto.setNetWeight(packPrint.getNetWeight());
					resDto.setGrossWeight(packPrint.getGrossWeight());
					resDto.setVolume(packPrint.getVolume());
				}
			}
		}
		result.setItems(poLineList);
		return result;
	}

	/**
	 * 编辑数据
	 * 
	 * @param packPrint
	 */
	public void createPackPrint(List<PurchasePackPrint> packPrint) {
		for (PurchasePackPrint purchasePackPrint : packPrint) {
			if (purchasePackPrint.getId() != null) {
				updatePurchasePackPrint(purchasePackPrint);
			} else {
				createPurchasePackPrint(purchasePackPrint);
			}
		}
	}

	public Integer createPurchasePackPrint(PurchasePackPrint purchasePackPrint) {
		purchasePackPrint.setCreateAt(new Date());
		purchasePackPrint.setIsDelete(BaseConsts.ZERO);
		purchasePackPrint
				.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		purchasePackPrint.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		int count = purchasePackPrintDao.insert(purchasePackPrint);
		if (count < BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.PO_ADD_EXCEPTION);
		}
		return purchasePackPrint.getId();
	}

	public void updatePurchasePackPrint(PurchasePackPrint purchasePackPrint) {
		purchasePackPrintDao.update(purchasePackPrint);
	}

}
