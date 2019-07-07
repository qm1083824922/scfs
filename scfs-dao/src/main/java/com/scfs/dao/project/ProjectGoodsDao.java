package com.scfs.dao.project;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.project.dto.req.ProjectGoodsSearchReqDto;
import com.scfs.domain.project.entity.ProjectGoods;

public interface ProjectGoodsDao {

	/**
	 * 查询项目商品列表
	 * 
	 * @param projectId
	 * @param rowBounds
	 * @return
	 */
	List<ProjectGoods> queryProjectGoodsResultsByProjectId(Integer projectId, RowBounds rowBounds);

	List<ProjectGoods> queryProjectGoodsResultsByProjectId(Integer projectId);

	/** 根据项目和商品编号查询商品信息 */
	ProjectGoods queryByProjectIdAndGoodsNo(ProjectGoodsSearchReqDto projectGoodsSearchReqDto);

	/**
	 * 根据主键查询信息
	 */
	ProjectGoods loadAndLockEntityById(@Param(value = "id") int id);

	/**
	 * 查询所有项目商品，用于缓存
	 * 
	 * @return
	 */
	List<ProjectGoods> queryAllProGoods(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 更新项目商品
	 * 
	 * @param projectGoods
	 * @return
	 */
	int updateById(ProjectGoods projectGoods);

	/**
	 * 插入项目商品
	 * 
	 * @param projectGoods
	 * @return
	 */
	int insert(ProjectGoods projectGoods);

	/**
	 * 查询未分配给用户的商品
	 * 
	 * @param projectId
	 * @param rowBounds
	 * @return
	 */
	List<BaseGoods> queryGoodsToProjectByCon(ProjectGoodsSearchReqDto projectGoodsSearchReqDto);

	List<BaseGoods> queryGoodsToProjectByCon(ProjectGoodsSearchReqDto projectGoodsSearchReqDto, RowBounds rowBounds);

	ProjectGoods queryEntityByProjectAndGoods(@Param(value = "projectId") Integer projectId,
			@Param(value = "goodsId") Integer goodsId);

	/** 根据项目和商品编号查询商品信息 状态为 2 3 */
	List<ProjectGoods> queryResultGoodsBySku(ProjectGoodsSearchReqDto projectGoodsSearchReqDto);

}
