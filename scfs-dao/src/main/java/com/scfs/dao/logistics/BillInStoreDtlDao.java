package com.scfs.dao.logistics;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.dto.req.PoOrderReqDto;
import com.scfs.domain.logistics.dto.resp.PoOrderDtlResDto;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreDtlExt;
import com.scfs.domain.logistics.entity.BillInStoreDtlSum;
import com.scfs.domain.logistics.entity.BillInStoreDtlTaxGroupSum;

public interface BillInStoreDtlDao {
	int deleteById(Integer id);

	int insert(BillInStoreDtl billInStoreDtl);

	BillInStoreDtl queryEntityById(Integer id);

	List<BillInStoreDtl> selectList(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto);

	int updateById(BillInStoreDtl billInStoreDtl);

	List<BillInStoreDtl> queryResultsByCon(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto, RowBounds rowBounds);

	List<BillInStoreDtl> queryResultsByCon(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto);

	List<BillInStoreDtlExt> queryResultsByBillInStoreCon(BillInStoreSearchReqDto billInStoreSearchReqDto);

	int queryCountByBillInStoreCon(BillInStoreSearchReqDto billInStoreSearchReqDto);

	BillInStoreDtl queryById(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto);

	BillInStoreDtlSum querySumByBillInStoreId(BillInStoreDtl billInStoreDtl);

	int updateAcceptTime(BillInStoreDtl billInStoreDtl);

	List<BillInStoreDtl> queryByIds(@Param("ids") List<Integer> ids);

	List<PoOrderDtlResDto> queryPoOrderDtlResults(PoOrderReqDto poOrderReqDto, RowBounds rowBounds);

	List<PoOrderDtlResDto> queryPoOrderDtlResults(PoOrderReqDto poOrderReqDto);

	BigDecimal queryStorageNumByPoDtlId(BillInStoreDtl billInStoreDtl);

	List<BillInStoreDtlTaxGroupSum> queryTaxGroupSumByBillInStoreId(Integer id);

	BigDecimal querySumAmountByBillInStoreId(Integer id);

	BigDecimal querySumCostAmountByBillInStoreId(Integer id);

	List<BillInStoreDtl> queryResultsByPoLineId(Integer poLineId);

	List<BillInStoreDtl> queryBillInStoreTallyExportResults(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto);

	BillInStoreDtlSum querySumBillInStoreDtl(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto);

	/**
	 * 根据入库单id查询数据
	 * 
	 * @param billDeliveryDtlId
	 * @return
	 */
	List<BillInStoreDtl> queryResultInStoreID(Integer inStoreID);
}