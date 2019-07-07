package com.scfs.dao.sale;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.sale.dto.req.BillDeliverySearchReqDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.domain.sale.entity.BillDeliverySum;

public interface BillDeliveryDao {
	int insert(BillDelivery billDelivery);

	BillDelivery queryAndLockEntityById(Integer id);

	int updateById(BillDelivery BillDelivery);

	List<BillDelivery> queryResultsByCon(BillDeliverySearchReqDto billDeliverySearchReqDto, RowBounds rowBounds);

	List<BillDelivery> queryResultsByCon(BillDeliverySearchReqDto billDeliverySearchReqDto);

	int queryCountByCon(BillDeliverySearchReqDto billDeliverySearchReqDto);

	BillDelivery queryEntityById(BillDelivery billDelivery);

	List<BillDeliverySum> querySumBillDelivery(BillDeliverySearchReqDto billDeliverySearchReqDto);

	int queryChangePriceCountById(Integer id);

	int queryReturnChangePriceCountById(Integer id);

	BillDelivery queryEntityByBillNo(@Param("billNo") String billNo);

	List<BillDelivery> queryFinishedBillDeliveryByAffiliateNo(String affiliateNo);

	BillDelivery queryBillDeliveryById(Integer id);

	BillDelivery queryBillDeliveryByBillOutStoreId(Integer billOutStoreId);

	/**
	 * 获取销售单信息
	 * 
	 * @param billDelivery
	 * @return
	 */
	BillDelivery queryEntityByParam(BillDelivery billDelivery);

	int deleteById(BillDelivery billDelivery);
}