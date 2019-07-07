package com.scfs.dao.logistics;

import java.math.BigDecimal;
import java.util.List;

import com.scfs.domain.logistics.entity.BillOutStoreTaxGroupCostPrice;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.logistics.entity.BillOutStorePickDtlExt;
import com.scfs.domain.logistics.entity.BillOutStorePickDtlSum;

public interface BillOutStorePickDtlDao {
	int deleteById(Integer id);

	int insert(BillOutStorePickDtl billOutStorePickDtl);

	BillOutStorePickDtl queryEntityById(Integer id);

	List<BillOutStorePickDtl> queryResultsByBillOutStoreId(Integer billOutStoreId);

	List<BillOutStorePickDtl> queryResultsByBillOutStoreDtlId(Integer billOutStoreDtlId);

	int updateById(BillOutStorePickDtl billOutStorePickDtl);

	int deleteByBillOutStoreDtlId(Integer billOutStoreDtlId);

	BillOutStorePickDtlSum querySumByBillOutStoreDtlId(BillOutStorePickDtl billOutStorePickDtl);

	List<BillOutStoreTaxGroupCostPrice> queryCostPriceByBillOutStoreId(Integer billOutStoreId);

	BigDecimal queryCostPriceByOutStoreId(Integer billOutStoreId);

	List<BillOutStorePickDtlExt> queryResultsByBillOutStoreCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	int queryCountByBillOutStoreCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	/**
	 * 根据出库ID查询拣货明细
	 * 
	 * @param billOutStoreId
	 * @return
	 */
	List<BillOutStoreTaxGroupCostPrice> queryPickDtlByOutId(Integer billOutStoreId);

}