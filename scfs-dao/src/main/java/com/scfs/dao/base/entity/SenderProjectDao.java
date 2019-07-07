package com.scfs.dao.base.entity;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.base.dto.req.SenderProjectReqDto;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.SenderProject;

@Repository
public interface SenderProjectDao {
	/**
	 * 获取分组数据
	 * 
	 * @param senderManageReqDto
	 * @param rowBounds
	 * @return
	 */
	List<SenderProject> queryResultsByCon(SenderProjectReqDto senderProjectReqDto, RowBounds rowBounds);

	/**
	 * 获取所有数据
	 * 
	 * @param senderManageReqDto
	 * @return
	 */
	List<SenderProject> queryResultsByCon(SenderProjectReqDto senderManageReqDto);

	/**
	 * 获取没有关联项目
	 * 
	 * @param senderProjectReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BaseProject> queryBaseProject(SenderProjectReqDto senderProjectReqDto, RowBounds rowBounds);

	/**
	 * 通过id删除
	 * 
	 * @param id
	 * @return
	 */
	int deleteById(Integer id);

	/**
	 * 添加数据
	 * 
	 * @param senderManage
	 * @return
	 */
	int insert(SenderProject senderProject);

}
