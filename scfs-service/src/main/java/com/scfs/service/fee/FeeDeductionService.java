package com.scfs.service.fee;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.pay.PayDeductionFeeRelationDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.pay.entity.PayDeductionFeeRelation;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;

/**
 * <pre>
 * 	
 *  File: FeeRecDeductionService.java
 *  Description:抵扣费用 
 *  TODO
 *  Date,					Who,				
 *  2017年07月27日				Administrator
 *
 * </pre>
 */
@Service
public class FeeDeductionService {

	@Autowired
	private PayDeductionFeeRelationDao payDeductionFeeRelationDao;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private CacheService cacheService;

	/**
	 * 根据费用单ID查询
	 * 
	 * @param id
	 * @return
	 */
	public PageResult<PoTitleRespDto> queryPoTitleResultByCon(Integer id) {
		PageResult<PoTitleRespDto> result = new PageResult<PoTitleRespDto>();
		if (id == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用查询数据为空,请重新操作");
		}
		List<PayDeductionFeeRelation> payDeductionFeeRelations = payDeductionFeeRelationDao.queryResultByFeeId(id);
		List<PoTitleRespDto> poRespDto = new ArrayList<PoTitleRespDto>();
		if (!CollectionUtils.isEmpty(payDeductionFeeRelations)) {
			for (PayDeductionFeeRelation payDeductionFeeRelation : payDeductionFeeRelations) {
				List<PurchaseOrderTitle> purchaseOrderTitles = purchaseOrderTitleDao
						.queryPoRelationTitleResult(payDeductionFeeRelation.getPayId());
				poRespDto = this.convertToResult(purchaseOrderTitles, payDeductionFeeRelation.getPayAmount());
			}
		}
		result.setItems(poRespDto);
		return result;
	}

	/**
	 * 封装返回的数据
	 * 
	 * @param PoTitles
	 * @return
	 */
	private List<PoTitleRespDto> convertToResult(List<PurchaseOrderTitle> PoTitles, BigDecimal deductionAmount) {
		List<PoTitleRespDto> poTitleList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(PoTitles)) {
			return poTitleList;
		}
		for (PurchaseOrderTitle purchaseOrderTitle : PoTitles) {
			PoTitleRespDto poRespDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle, deductionAmount);
			poTitleList.add(poRespDto);
		}
		return poTitleList;
	}

	private PoTitleRespDto purchaseOrderTitleConvertToRes(PurchaseOrderTitle purchaseOrderTitle,
			BigDecimal deductionAmount) {
		if (purchaseOrderTitle == null) {
			return null;
		}
		PoTitleRespDto poRespDto = new PoTitleRespDto();
		poRespDto.setOrderNo(purchaseOrderTitle.getOrderNo());
		poRespDto.setId(purchaseOrderTitle.getId());
		poRespDto.setBusinessUnitId(purchaseOrderTitle.getBusinessUnitId());
		poRespDto.setProjectId(purchaseOrderTitle.getProjectId());
		poRespDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		poRespDto.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		poRespDto.setAppendNo(purchaseOrderTitle.getAppendNo());
		// 项目
		poRespDto.setProjectId(purchaseOrderTitle.getProjectId());
		poRespDto.setProjectName(cacheService.showProjectNameById(purchaseOrderTitle.getProjectId()));
		// 经营单位
		poRespDto.setBusinessUnitName(cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getBusinessUnitId(),
				CacheKeyConsts.BUSI_UNIT));
		poRespDto.setBusinessUnitId(purchaseOrderTitle.getBusinessUnitId());
		// 供应商
		poRespDto.setSupplierName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getSupplierId(), CacheKeyConsts.SUPPLIER));
		poRespDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		// 仓库
		poRespDto.setWarehouseName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		poRespDto.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		// 仓库地址
		poRespDto.setSupplierAddressId(purchaseOrderTitle.getSupplierAddressId());
		poRespDto.setSupplierAddressName(
				cacheService.getAddressById(purchaseOrderTitle.getSupplierAddressId()).getAddressDetail());
		// 客户
		poRespDto.setCustomerName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getCustomerId(), CacheKeyConsts.CUSTOMER));
		poRespDto.setCustomerId(purchaseOrderTitle.getCustomerId());
		poRespDto.setOrderTime(purchaseOrderTitle.getOrderTime());
		poRespDto.setOrderTotalNum(purchaseOrderTitle.getOrderTotalNum());
		poRespDto.setOrderTotalAmount(purchaseOrderTitle.getOrderTotalAmount());
		poRespDto.setCreateAt(purchaseOrderTitle.getCreateAt());
		poRespDto.setCreateUser(purchaseOrderTitle.getCreator());
		poRespDto.setDeductionAmount(deductionAmount);
		return poRespDto;
	}
}
