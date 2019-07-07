package com.scfs.dao.base.entity;

import com.scfs.domain.base.entity.BaseGoods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BaseGoodsDao {

	/**
	 * 查询商品列表
	 * 
	 * @return
	 */
	List<BaseGoods> getGoodsList(BaseGoods baseGoods, RowBounds rowBounds);

	/**
	 * 查询商品列表
	 * 
	 * @return
	 */
	List<BaseGoods> getGoodsList(BaseGoods baseGoods);

	/**
	 * 查询所有商品
	 * 
	 * @return
	 */
	List<BaseGoods> queryAllGoodsList(@Param("updateAt") String updateAt);

	/**
	 * 查询最后更新时间
	 * 
	 * @return
	 */
	Date queryLastUpdateAt();

	/**
	 * 根据ID获取商品信息
	 * 
	 * @param id
	 * @return
	 */
	BaseGoods queryBaseGoodsById(int id);

	/**
	 * 新增商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	int insert(BaseGoods baseGoods);

	/**
	 * 更新商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	int update(BaseGoods baseGoods);

	/**
	 * 提交操作
	 * 
	 * @param baseGoods
	 * @return
	 */
	int submit(BaseGoods baseGoods);

	/**
	 * 锁定操作
	 * 
	 * @param baseGoods
	 * @return
	 */
	int lock(BaseGoods baseGoods);

	/**
	 * 解锁操作
	 * 
	 * @param baseGoods
	 * @return
	 */
	int unlock(BaseGoods baseGoods);

	/**
	 * 删除操作
	 * 
	 * @param baseGoods
	 * @return
	 */
	int delete(BaseGoods baseGoods);

	int getExistCountByCon(BaseGoods baseGoods);

	/**
	 * 根据商品编号查询详情
	 * 
	 * @param number
	 * @return
	 */
	BaseGoods queryGoodsByNumber(String number);

}
