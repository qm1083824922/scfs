package com.scfs.dao.project;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.project.dto.req.ProjectPoolDtlSearchReqDto;
import com.scfs.domain.project.entity.ProjectPoolDtl;

public interface ProjectPoolDtlDao {

	/**
	 * 根据条件分页查询出多条结果
	 * 
	 * @return
	 */
	List<ProjectPoolDtl> queryProjectPoolDtlResultsByCon(ProjectPoolDtl projectReqDto, RowBounds rowBounds);

	/**
	 * 新建资金/资产信息
	 * 
	 * @return
	 */
	int insert(ProjectPoolDtl projectPoolDtl);

	/**
	 * 查询所有项目id相同的资金/资产信息
	 * 
	 * @return
	 */
	BigDecimal queryProjectPoolDtlResultsByPro(ProjectPoolDtlSearchReqDto projectReqDto);

}