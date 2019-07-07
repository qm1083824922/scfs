package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fi.dto.req.VoucherLineSearchReqDto;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.fi.entity.VoucherLine;
import com.scfs.domain.fi.entity.VoucherLineModel;

@Repository
public interface VoucherLineDao {

	int insert(VoucherLine voucherLine);

	int updateById(VoucherLine voucherLine);

	List<VoucherLine> queryResultsByVoucherId(@Param(value = "voucherId") Integer voucherId);

	List<VoucherLine> queryGroupResultsByCon(VoucherLineSearchReqDto req, RowBounds rowBounds);

	List<VoucherLine> queryGroupResultsByCon(VoucherLineSearchReqDto req);

	List<VoucherLine> queryTotalGroupResultsByCon(VoucherLineSearchReqDto req);

	List<VoucherLine> queryLineResultsByCon(VoucherLineSearchReqDto req, RowBounds rowBounds);

	List<VoucherLine> queryLineResultsByCon(VoucherLineSearchReqDto req);

	int queryLineCountByVoucherId(@Param(value = "voucherId") int voucherId);

	VoucherLine queryEntityById(@Param(value = "id") int id);

	List<VoucherLine> queryEntityByIds(VoucherLineSearchReqDto req);

	int deleteById(@Param(value = "id") int id);

	List<VoucherLineModel> queryLineResultsByVoucherCon(VoucherSearchReqDto voucherSearchReqDto);

	int queryLineCountByVoucherCon(VoucherSearchReqDto voucherSearchReqDto);

	VoucherLine queryVoucherLineByBillOutStore(VoucherLineSearchReqDto req);

	Voucher querySumByVoucherId(Integer voucherId);

	/**
	 * 查询费用管理凭证明细
	 * 
	 * @param feeId
	 * @return
	 */
	List<VoucherLine> queryLineResultsByFeeId(Integer feeId);
}
