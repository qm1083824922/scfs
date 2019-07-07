package com.scfs.dao.base.entity;

import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.subject.dto.req.AddSubjectDto;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BaseSubjectDao {
	/**
	 * 新增主体信息
	 */
	int insertBaseSubject(AddSubjectDto addSubjectDto);

	/**
	 * 根据主键查询主体信息
	 */
	BaseSubject loadAndLockEntityById(@Param(value = "id") int id);

	/**
	 * 根据主键查询主体信息
	 */
	BaseSubject queryEntityById(@Param(value = "id") int id);

	/**
	 * 查询主体信息
	 */
	List<BaseSubject> querySubjectByCond(QuerySubjectReqDto querySubjectReqDto, RowBounds rowBounds);

	/**
	 * 查询主体信息(不分页)
	 */
	List<BaseSubject> querySubjectByCond(QuerySubjectReqDto querySubjectReqDto);

	/**
	 * 修改主体信息
	 */
	int updateBaseSubject(BaseSubject baseSubject);

	List<BaseSubject> querySubjectByType(QuerySubjectReqDto querySubjectReqDto);

	List<BaseSubject> querySubjectByAbb(QuerySubjectReqDto querySubjectReqDto);

	/**
	 * 修改主体状态
	 */
	int updateSubjectState(@Param(value = "id") int id, @Param(value = "state") int state);

	/**
	 * 物理删除
	 */
	int deleteLogicSubject(BaseSubject baseSubject);

	/**
	 * 真实删除
	 */
	int deleteSubject(@Param(value = "id") int id);

	/**
	 * 提交
	 */
	int submitSubject(BaseSubject baseSubject);

	/**
	 * 锁定
	 */
	int lockSubject(BaseSubject baseSubject);

	/**
	 * 解锁
	 */
	int unLockSubject(BaseSubject baseSubject);

	/**
	 * 根据主体类型获取所有主体信息，数据初始化使用
	 * 
	 * @param updateAt
	 * @return
	 */
	List<BaseSubject> queryAllSubject(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	BaseSubject queryEntityByBusiUnitCode(String businessUnitCode);

	/**
	 * 未分配给用户的主体信息
	 * 
	 * @param querySubjectReqDto
	 * @param rowBounds
	 * @return
	 */
	List<BaseSubject> queryUnDividSubjectByUserId(BaseUserSubjectReqDto querySubjectReqDto, RowBounds rowBounds);

	/**
	 * 根据主体编号查询主体
	 * 
	 * @param subjectNo
	 * @return
	 */
	BaseSubject querySubjectBySubjectNo(@Param("subjectNo") String subjectNo);

}
