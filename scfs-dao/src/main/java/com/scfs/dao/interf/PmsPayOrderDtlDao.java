package com.scfs.dao.interf;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.api.pms.entity.PmsPayOrderDtl;

@Repository
public interface PmsPayOrderDtlDao {

	int insert(PmsPayOrderDtl record);

	PmsPayOrderDtl queryEntityById(Integer id);

	int updateById(PmsPayOrderDtl record);

	List<PmsPayOrderDtl> queryResultsByTitleId(Integer titleId, RowBounds rowBounds);

	List<PmsPayOrderDtl> queryResultsByTitleId(Integer titleId);

}