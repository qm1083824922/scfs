package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.subject.dto.req.AddAddressDto;
import com.scfs.domain.base.subject.dto.req.QueryAddressReqDto;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * <pre>
 * 
 *  File: BaseAddressDao.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年9月27日				Administrator
 *
 * </pre>
 */
@Repository
public interface BaseAddressDao {
	int insertBaseAddress(AddAddressDto addAddressDto);

	/**
	 * 根据主键查询
	 */
	BaseAddress loadAndLockEntityById(@Param(value = "id") int id);

	/**
	 * 根据主键ID获取地址信息
	 * 
	 * @param id
	 * @return
	 */
	BaseAddress queryEntityById(@Param(value = "id") int id);

	/**
	 * 查询地址信息
	 */
	List<BaseAddress> queryAddressBySubjectId(QueryAddressReqDto queryAddressReqDto, RowBounds rowBounds);

	/**
	 * 查询地址信息
	 */
	List<BaseAddress> queryResultsyBySubjectId(@Param("subjectId") Integer subjectId);

	/**
	 * 查询所有主体正在使用的地址
	 * 
	 * @return
	 */
	List<BaseAddress> queryAllSubjectAddress(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 修改地址信息
	 */
	int updateBaseAddressById(BaseAddress baseAddress);

	/**
	 * 作废信息
	 */
	int invalidAddressById(BaseAddress baseAddress);

}
