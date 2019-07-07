package com.scfs.dao.fee;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fee.dto.req.FeeShareReqDto;
import com.scfs.domain.fee.entity.FeeShare;

@Repository
public interface FeeShareDao {

	int insert(FeeShare feeShare);

	FeeShare queryEntityById(Integer id);

	int updateById(FeeShare feeShare);

	List<FeeShare> queryResultsByCon(FeeShareReqDto feeShare, RowBounds rowBounds);

	// 报表人工费用（通过条件查询分摊信息）
	List<FeeShare> queryResultsByManageId(FeeShareReqDto feeShare);

}
