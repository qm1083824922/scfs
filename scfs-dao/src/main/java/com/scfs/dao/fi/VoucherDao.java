package com.scfs.dao.fi;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.entity.Voucher;

@Repository
public interface VoucherDao {
	int insert(Voucher voucher);

	int updateById(Voucher voucher);

	int deleteById(@Param(value = "id") int id);

	int submitById(@Param(value = "id") int id, @Param(value = "state") int state);

	List<Voucher> queryResultsByCon(VoucherSearchReqDto req, RowBounds rowBounds);

	List<Voucher> queryResultsByCon(VoucherSearchReqDto req);

	Voucher queryEntityById(@Param(value = "id") int id);

	/**
	 * 通过条件查询凭证信息
	 * 
	 * @param req
	 * @return
	 */
	Voucher queryEntityByParam(Voucher voucher);

}
