package com.scfs.dao.fee;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fee.dto.req.FeeSpecSearchReqDto;
import com.scfs.domain.fee.entity.FeeSpec;

@Repository
public interface FeeSpecDao {
	List<FeeSpec> queryAllFeeSpec(FeeSpecSearchReqDto req);

	FeeSpec queryEntityById(@Param(value = "id") int id);

	List<FeeSpec> queryAllFeeSpec(FeeSpecSearchReqDto req, RowBounds rowBounds);

	int insert(FeeSpec feeSpec);

	int updateById(FeeSpec feeSpec);

	int deletById(@Param(value = "id") int id);
}
