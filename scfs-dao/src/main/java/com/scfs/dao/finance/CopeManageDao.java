package com.scfs.dao.finance;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.finance.cope.dto.req.CopeManageReqDto;
import com.scfs.domain.finance.cope.entity.CopeManage;

@Repository
public interface CopeManageDao {

	int insert(CopeManage copeManage);

	int updateById(CopeManage copeManage);

	CopeManage queryEntityById(Integer id);

	List<CopeManage> queryResultsByCon(CopeManageReqDto reqDto, RowBounds rowBounds);

	List<CopeManage> queryResultsByCon(CopeManageReqDto reqDto);

}
