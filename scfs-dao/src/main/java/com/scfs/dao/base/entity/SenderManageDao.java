package com.scfs.dao.base.entity;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.base.dto.req.SenderManageReqDto;
import com.scfs.domain.base.entity.SenderManage;

@Repository
public interface SenderManageDao {
	/**
	 * 获取分组数据
	 * 
	 * @param senderManageReqDto
	 * @param rowBounds
	 * @return
	 */
	List<SenderManage> queryResultsByCon(SenderManageReqDto senderManageReqDto, RowBounds rowBounds);

	/**
	 * 获取所有数据
	 * 
	 * @param senderManageReqDto
	 * @return
	 */
	List<SenderManage> queryResultsByCon(SenderManageReqDto senderManageReqDto);

	/**
	 * 获取详情
	 * 
	 * @param id
	 * @return
	 */
	SenderManage queryEntityById(Integer id);

	/**
	 * 添加数据
	 * 
	 * @param senderManage
	 * @return
	 */
	int insert(SenderManage senderManage);

	/**
	 * 修改数据
	 * 
	 * @param senderManage
	 * @return
	 */
	int update(SenderManage senderManage);

	/**
	 * 查询所有商品
	 * 
	 * @param updateAt
	 * @return
	 */
	List<SenderManage> querySenderManageList(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 通过关联获取信息
	 * 
	 * @param senderManageReqDto
	 * @return
	 */
	List<SenderManage> querySenderByProject(SenderManageReqDto senderManageReqDto);
}
