package com.scfs.dao.interf;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;

@Repository
public interface PmsPayOrderTitleDao {

	int insert(PmsPayOrderTitle record);

	PmsPayOrderTitle queryEntityById(Integer id);

	int updateById(PmsPayOrderTitle record);

	List<PmsPayOrderTitle> queryResultsByCon(PmsPoTitleSearchReqDto req, RowBounds rowBounds);

	List<PmsPayOrderTitle> queryResultsByCon(PmsPoTitleSearchReqDto req);

	List<PmsPayOrderTitle> queryResultsByPayNo(PmsPoTitleSearchReqDto req);

}