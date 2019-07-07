package com.scfs.dao.common;

import java.util.List;

import com.scfs.domain.common.entity.ReportProject;

public interface ReportProjectDao {
	int deleteById(Integer id);

	int insert(ReportProject reportProject);

	ReportProject queryEntityById(Integer id);

	int updateById(ReportProject reportProject);

	int deleteByProjectId(Integer projectId);

	List<ReportProject> queryEntityByProjectId(Integer projectId);

	List<Integer> queryEntityByReportType(Integer reportType);

}