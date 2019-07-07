package com.scfs.dao.project;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.project.dto.req.ProjectPoolDtlSearchReqDto;
import com.scfs.domain.project.entity.ProjectPoolFund;

public interface ProjectPoolFundDao {

	int deleteById(Integer id);

	int insert(ProjectPoolFund record);

	List<ProjectPoolFund> selectByCon(ProjectPoolDtlSearchReqDto projectPoolFund, RowBounds rowBounds);

	List<ProjectPoolFund> selectByCon(ProjectPoolDtlSearchReqDto projectPoolFund);

	int updateById(ProjectPoolFund record);

	int deleteExculdeOfSunyou();
}