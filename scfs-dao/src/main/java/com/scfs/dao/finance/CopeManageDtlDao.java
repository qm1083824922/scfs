package com.scfs.dao.finance;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.finance.cope.dto.req.CopeManageReqDto;
import com.scfs.domain.finance.cope.entity.CopeManageDtl;

@Repository
public interface CopeManageDtlDao {

	int insert(CopeManageDtl copeManageDtl);

	int updateById(CopeManageDtl copeManageDtl);

	List<CopeManageDtl> queryResultsByCon(CopeManageReqDto reqDto, RowBounds rowBounds);

	List<CopeManageDtl> queryResultsByCon(CopeManageReqDto reqDto);
	
	List<CopeManageDtl>  queryResultByProject(CopeManageReqDto reqDto,RowBounds rowBounds);
	
	CopeManageDtl queryEntityById(Integer id);
	
	CopeManageDtl  queryCopeByBillId(Integer billId);
}
