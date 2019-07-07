package com.scfs.dao.project;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.project.dto.req.ProjectPoolDtlSearchReqDto;
import com.scfs.domain.project.entity.ProjectPoolAsset;

public interface ProjectPoolAssetDao {
	int deleteById(Integer id);

	int insert(ProjectPoolAsset record);

	List<ProjectPoolAsset> selectByCon(ProjectPoolDtlSearchReqDto projectPoolDtlSearchReqDto, RowBounds rowBounds);

	List<ProjectPoolAsset> selectByCon(ProjectPoolDtlSearchReqDto projectPoolDtlSearchReqDto);

	int updateById(ProjectPoolAsset record);

	List<ProjectPoolAsset> queryResultsByCon(ProjectPoolDtlSearchReqDto projectPoolDtlSearchReqDto);

	List<ProjectPoolAsset> queryArrivePaymentPeriodResults(ProjectPoolDtlSearchReqDto projectPoolDtlSearchReqDto);

	ProjectPoolAsset queryResultByBillNo(String billNo);

	int deleteExculdeOfSunyou();
}