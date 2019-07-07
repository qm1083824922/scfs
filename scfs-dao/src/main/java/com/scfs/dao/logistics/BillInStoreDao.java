package com.scfs.dao.logistics;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreSum;

public interface BillInStoreDao {
	int deleteById(Integer id);

	int insert(BillInStore billInStore);

	BillInStore queryAndLockEntityById(Integer id);

	int updateById(BillInStore billInStore);

	List<BillInStore> queryResultsByCon(BillInStoreSearchReqDto billInStoreSearchReqDto, RowBounds rowBounds);

	List<BillInStore> queryResultsByCon(BillInStoreSearchReqDto billInStoreSearchReqDto);

	int queryCountByCon(BillInStoreSearchReqDto billInStoreSearchReqDto);

	BillInStore queryEntityById(BillInStoreSearchReqDto billInStoreSearchReqDto);

	int queryDtlsCount(BillInStore billInStore);

	int queryTallyDtlsCount(BillInStore billInStore);

	BillInStore queryDtlsTotalInfo(BillInStore billInStore);

	BillInStore queryTallyDtlsTotalInfo(BillInStore billInStore);

	List<BillInStore> selectList(BillInStoreSearchReqDto billInStoreSearchReqDto);

	int updateBillInStoreInfo(BillInStore billInStore);

	List<BillInStoreSum> querySumBillInStore(BillInStoreSearchReqDto billInStoreSearchReqDto);

	List<BillInStore> queryByBillDeliveryId(BillInStore billInStore);

	/**
	 * 通过附属编号获取信息
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	List<BillInStore> queryEntityByParam(BillInStoreSearchReqDto billInStoreSearchReqDto);

	List<BillInStore> queryFinishedBillInStoreByAffiliateNo(String affiliateNo);

	/**
	 * 查询需刷新融资池的入库单
	 * 
	 * @return
	 */
	List<BillInStore> queryRefreshProjectPoolResults();

	BigDecimal queryPayAmountByPayRate(String billNo);

	/**
	 * 查询需刷新资金池的入库单
	 * 
	 * @return
	 */
	List<BillInStore> queryRefreshReceiptPoolResults();
}