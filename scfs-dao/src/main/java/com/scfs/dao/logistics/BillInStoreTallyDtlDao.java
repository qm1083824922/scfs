package com.scfs.dao.logistics;

import java.util.List;

import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtlExt;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtlSum;

public interface BillInStoreTallyDtlDao {
	int deleteById(Integer id);

	int insert(BillInStoreTallyDtl billInStoreTallyDtl);

	BillInStoreTallyDtl queryEntityById(Integer id);

	int updateById(BillInStoreTallyDtl billInStoreTallyDtl);

	int deleteByBillInStoreDtlId(Integer billInStoreDtlId);

	BillInStoreTallyDtlSum querySumByBillInStoreDtlId(BillInStoreTallyDtl billInStoreTallyDtl);

	int updateAcceptTime(BillInStoreTallyDtl billInStoreTallyDtl);

	List<BillInStoreTallyDtl> queryResultsByCon(BillInStoreTallyDtl billInStoreTallyDtl);

	List<BillInStoreTallyDtl> queryResultsByBillInStoreDtlId(Integer billInStoreDtlId);

	List<BillInStoreTallyDtl> queryResultsByBillInStoreId(Integer billInStoreId);

	List<BillInStoreTallyDtlExt> queryResultsByBillInStoreCon(BillInStoreSearchReqDto billInStoreSearchReqDto);

	int queryCountByBillInStoreCon(BillInStoreSearchReqDto billInStoreSearchReqDto);

	List<BillInStoreTallyDtl> queryResultsByPoLineId(Integer poLineId);

}