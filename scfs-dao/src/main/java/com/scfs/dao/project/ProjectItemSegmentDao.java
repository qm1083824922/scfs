package com.scfs.dao.project;

import java.util.List;

import com.scfs.domain.project.dto.req.ProjectItemSegmentReqDto;
import com.scfs.domain.project.entity.ProjectItemSegment;

public interface ProjectItemSegmentDao {
	List<ProjectItemSegment> queryNeedToDeleteResults(ProjectItemSegmentReqDto projectItemSegmentReqDto);

	int insert(ProjectItemSegment projectItemSegment);

	ProjectItemSegment queryEntityById(Integer id);

	int updateById(ProjectItemSegment projectItemSegment);

	List<ProjectItemSegment> queryResultsByCon(ProjectItemSegmentReqDto projectItemSegmentReqDto);

	ProjectItemSegment querySegmentBySegmentDay(ProjectItemSegmentReqDto projectItemSegmentReqDto);

	ProjectItemSegment queryMaxSegmentBySegmentDay(ProjectItemSegmentReqDto projectItemSegmentReqDto);

}