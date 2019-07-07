package com.scfs.dao.base.entity;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseUserSubject;

@Repository
public interface BaseUserSubjectDao {
	/**
	 * 分配信息
	 * 
	 * @param baseUserSubject
	 * @return
	 */
	int insertUserSubject(BaseUserSubject baseUserSubject);

	/**
	 * 更新信息
	 * 
	 * @param baseUserSubject
	 * @return
	 */
	int updateUserSubject(BaseUserSubject baseUserSubject);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	BaseUserSubject queryUserSubjectById(int id);

	/**
	 * 获取所有信息
	 * 
	 * @param baseUserSubject
	 * @return
	 */
	List<BaseUserSubject> queryUserSubjectByCon(BaseUserSubjectReqDto baseUserSubject, RowBounds rowBounds);

	/**
	 * 获取所有信息
	 * 
	 * @param baseUserSubject
	 * @return
	 */
	List<BaseUserSubject> queryUserSubjectByCon(BaseUserSubjectReqDto baseUserSubject);

	/**
	 * 根据用户ID和主体ID删除数据
	 * 
	 * @param baseUserSubject
	 * @return
	 */
	int deleteUserSubject(BaseUserSubject baseUserSubject);
}
