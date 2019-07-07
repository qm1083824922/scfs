package com.scfs.dao.logistics;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlCustomsResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlResDto;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStoreDtlExt;
import com.scfs.domain.logistics.entity.BillOutStoreDtlSum;
import com.scfs.domain.logistics.entity.BillOutStoreDtlTaxGroupSum;

public interface BillOutStoreDtlDao {
	int deleteById(Integer id);

	int insert(BillOutStoreDtl billOutStoreDtl);

	BillOutStoreDtl queryEntityById(Integer id);

	List<BillOutStoreDtl> selectList(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto);

	int updateById(BillOutStoreDtl billOutStoreDtl);

	List<BillOutStoreDtl> queryResultsByCon(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto,
			RowBounds rowBounds);

	List<BillOutStoreDtl> queryResultsByCon(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto);

	List<BillOutStoreDtlExt> queryResultsByBillOutStoreCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	int queryCountByBillOutStoreCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	BillOutStoreDtl queryById(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto);

	List<BillOutStoreDtl> queryPickList(BillOutStoreDtl billOutStoreDtl, RowBounds rowBounds);

	BillOutStoreDtlSum querySumByBillOutStoreId(BillOutStoreDtl billOutStoreDtl);

	List<BillOutStoreDtlTaxGroupSum> queryTaxGroupSumByBillOutStoreId(Integer id);

	BigDecimal querySumAmountByBillOutStoreId(Integer id);

	List<BillOutStoreDtlCustomsResDto> queryAvailableResultByCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto,
			RowBounds rowBounds);

	BillOutStoreDtlSum querySumBillOutStoreDtl(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto);

	List<BillOutStoreDtl> queryResultsByBillDeliveryDtlId(Integer billDeliveryDtlId);

	/**
	 * 根据出库单id查询数据
	 * 
	 * @param billDeliveryDtlId
	 * @return
	 */
	List<BillOutStoreDtl> queryResultOutStoreID(Integer outID);

	/**
	 * 查询资金归还金额小于付款金额的出库明细
	 * 
	 * @param billOutStoreId
	 * @return
	 */
	List<BillOutStoreDtl> queryUnFundbackResultsByBillOutStoreId(Integer billOutStoreId);

	/**
	 * 根据出库单ID查询出库打印的数据
	 * 
	 * @param billOutStoreId
	 * @return
	 */
	List<BillOutStoreDtlResDto> queryBillOutPrintByBillOutId(Integer billOutStoreId);

}