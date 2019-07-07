package com.scfs.dao.sale;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.invoice.dto.req.InvoiceSaleManagerReqDto;
import com.scfs.domain.sale.dto.req.BillDeliverySearchReqDto;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.domain.sale.entity.BillDeliveryDtlExt;
import com.scfs.domain.sale.entity.BillDeliveryDtlSum;

public interface BillDeliveryDtlDao {
	int deleteById(Integer id);

	int insert(BillDeliveryDtl billDeliveryDtl);

	BillDeliveryDtl queryAndLockEntityById(Integer id);

	int updateById(BillDeliveryDtl billDeliveryDtl);

	List<BillDeliveryDtl> queryResultsByBillDeliveryId(Integer billDeliveryId, RowBounds rowBounds);

	List<BillDeliveryDtl> queryResultsByBillDeliveryId(Integer billDeliveryId);

	List<BillDeliveryDtlExt> queryResultsByBillDeliveryCon(BillDeliverySearchReqDto billDeliverySearchReqDto);

	int queryCountByBillDeliveryCon(BillDeliverySearchReqDto billDeliverySearchReqDto);

	BillDeliveryDtlSum querySumByBillDeliveryId(BillDeliveryDtl billDeliveryDtl);

	int queryCountByBillDeliveryId(BillDeliveryDtl billDeliveryDtl);

	BillDeliveryDtl queryAmountByInvAppId(Integer billDeliveryId);

	List<BillDeliveryDtl> queryResultsByIds(@Param("ids") List<Integer> ids);

	BillDeliveryDtl queryEntityById(Integer id);

	List<BillDeliveryDtl> queryNotSelectByCon(InvoiceSaleManagerReqDto InvoiceSaleManagerReqDto);

	List<BillDeliveryDtl> queryNotSelectByCon(InvoiceSaleManagerReqDto InvoiceSaleManagerReqDto, RowBounds rowBounds);

	List<BillDeliveryDtl> queryResultsByStlId(Integer stlId);

}