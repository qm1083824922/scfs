package com.scfs.dao.project;

import org.apache.ibatis.annotations.Param;

import com.scfs.domain.project.entity.ProjectNoSeq;

public interface ProjectNoSeqDao {

	int insert(ProjectNoSeq projectNoSeq);

	ProjectNoSeq queryEntityById(Integer id);

	int updateById(ProjectNoSeq projectNoSeq);

	ProjectNoSeq getSeqByType(@Param("projectNoType") String projectNoType);

	int updateSeqValByType(ProjectNoSeq projectNoSeq);
}