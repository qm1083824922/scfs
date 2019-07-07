package com.scfs.dao.base.entity;

import com.scfs.domain.base.dto.req.DistributionGoodsReqDto;
import com.scfs.domain.base.entity.DistributionGoods;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface DistributionGoodsDao {

	/**
	 * 查询商品列表
	 * 
	 * @return
	 */
	List<DistributionGoods> queryDistributionGoodsList(DistributionGoodsReqDto distributionGoods, RowBounds rowBounds);

	/**
	 * 查询商品列表
	 * 
	 * @return
	 */
	List<DistributionGoods> queryDistributionGoodsList(DistributionGoodsReqDto distributionGoods);

	/**
	 * 查询所有商品
	 * 
	 * @return
	 */
	List<DistributionGoods> queryAllDistributionGoodsList(DistributionGoodsReqDto distributionGoods);

	/**
	 * 查询融通质押项目下铺货信息
	 * 
	 * @param distributionGoods
	 * @param rowBounds
	 * @return
	 */
	List<DistributionGoods> queryDistributionGoodsListByProject(DistributionGoodsReqDto distributionGoods,
			RowBounds rowBounds);

	/**
	 * 查询所有商品
	 * 
	 * @param updateAt
	 * @return
	 */
	List<DistributionGoods> queryAllDistributeGoodsList(@Param("updateAt") String updateAt);

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
	DistributionGoods queryDistributionGoodsById(int id);

	/**
	 * 新增商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	int insert(DistributionGoods distributionGoods);

	/**
	 * 更新商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	int update(DistributionGoods distributionGoods);

	/**
	 * 提交操作
	 * 
	 * @param baseGoods
	 * @return
	 */
	int submit(DistributionGoods distributionGoods);

	/**
	 * 锁定操作
	 * 
	 * @param baseGoods
	 * @return
	 */
	int lock(DistributionGoods distributionGoods);

	/**
	 * 解锁操作
	 * 
	 * @param baseGoods
	 * @return
	 */
	int unlock(DistributionGoods distributionGoods);

	/**
	 * 删除操作
	 * 
	 * @param baseGoods
	 * @return
	 */
	int delete(DistributionGoods distributionGoods);

	int getExistCountByCon(DistributionGoods distributionGoods);

	/** 根据编号（sku）和币种进行查询 **/
	DistributionGoods queryDistributionGoodByNumber(DistributionGoodsReqDto distributionGoods);

	/**
	 * 根据ID获取商品信息
	 * 
	 * @param id
	 * @return
	 */
	DistributionGoods queryDistributionGoodsByNumber(String number);

	/**
	 * 根据Sku查询商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	List<DistributionGoods> queryResultGoodsBySku(String number);

}
