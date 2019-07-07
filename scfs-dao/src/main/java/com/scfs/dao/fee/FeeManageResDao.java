package com.scfs.dao.fee;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.fee.dto.req.FeeManageSearchReqDto;
import com.scfs.domain.fee.entity.FeeManage;

@Repository
public interface FeeManageResDao {

	int insert(FeeManage feeManage);

	FeeManage queryEntityById(Integer id);

	int updateById(FeeManage feeManage);

	int update(FeeManage feeManage);

	List<FeeManage> queryResultsByCon(FeeManageSearchReqDto feeManageSearchReqDto, RowBounds rowBounds);

	List<FeeManage> queryResultsByCon(FeeManageSearchReqDto feeManageSearchReqDto);

	int isFeeManageMaxLine(FeeManageSearchReqDto feeManageSearchReqDto);
}
