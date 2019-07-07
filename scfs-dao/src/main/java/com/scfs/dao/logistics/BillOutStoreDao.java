package com.scfs.dao.logistics;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreSum;
import com.scfs.domain.sale.dto.req.BillOutStoreDetailSearchReqDto;
import com.scfs.domain.sale.dto.resp.BillOutStoreDetailResDto;

public interface BillOutStoreDao {
	int deleteById(Integer id);

	int insert(BillOutStore billOutStore);

	BillOutStore queryAndLockEntityById(Integer id);

	BillOutStore queryEntityById(Integer id);

	int updateById(BillOutStore billOutStore);

	List<BillOutStore> queryResultsByCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto, RowBounds rowBounds);

	List<BillOutStore> queryResultsByCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	int queryCountByCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	BillOutStore queryById(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	int queryDtlsCount(BillOutStore billOutStore);

	int queryPickDtlsCount(BillOutStore billOutStore);

	BillOutStore queryDtlsTotalInfo(BillOutStore billOutStore);

	BillOutStore queryPickDtlsTotalInfo(BillOutStore billOutStore);

	List<BillOutStore> queryByBillDeliveryId(BillOutStore billOutStore);

	int updateBillOutStoreInfo(BillOutStore billOutStore);

	List<BillOutStoreSum> querySumBillOutStore(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	List<BillOutStore> queryResultsByConNoUser(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	List<BillOutStore> queryAndLockResultsByCon(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	List<BillOutStore> querySaleFinishedResults(BillOutStoreSearchReqDto billOutStoreSearchReqDto);

	List<BillOutStoreDetailResDto> queryBillOutStoreDetailResultsByCon(
			BillOutStoreDetailSearchReqDto billOutStoreDetailSearchReqDto, RowBounds rowBounds);

	int updateReturnNum(Integer id);

	List<BillOutStore> queryByPoReturnId(BillOutStore billOutStore);

	/**
	 * 通过附属编号获取信息
	 * 
	 * @param billOutStoreSearchReqDto
	 * @return
	 */
	List<BillOutStore> queryResultsByAffiliateNo(BillOutStore billOutStore);

	/**
	 * 供应商退款类型的水单 查询出库单的数据
	 * 
	 * @param billOutStoreSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BillOutStoreDetailResDto> queryOutStoreResultsByCon(BillOutStoreDetailSearchReqDto dto, RowBounds rowBounds);

	/**
	 * 查询需刷新融资池的出库单
	 * 
	 * @return
	 */
	List<BillOutStore> queryRefreshProjectPoolResults();

	BigDecimal queryPayAmountByPayRate(String billNo);

	/**
	 * 查询需刷新资金池的出库单
	 * 
	 * @return
	 */
	List<BillOutStore> queryRefreshReceiptPoolResults();
}