package com.scfs.dao.api.pms;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.interf.dto.PmsDistributionSearchReqDto;
import com.scfs.domain.pay.entity.PmsStoreOut;

@Repository
public interface PmsStoreOutDao {
	int deleteByPrimaryKey(Integer id);

	int updateByPrimaryKey(PmsStoreOut record);

	PmsStoreOut selectByPrimaryKey(Integer id);

	List<PmsStoreOut> queryPmsStoreOut();

	int batchInsert(List<PmsStoreOut> item);

	PmsStoreOut queryEntityById(Integer id);

	int createPmsStoreOut(PmsStoreOut storeOut);

	/**
	 * 获取PMS明细列表
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<PmsStoreOut> queryStoreBySeries(PmsDistributionSearchReqDto reqDto, RowBounds rowBounds);

	/**
	 * 获取PMS接口信息
	 * 
	 * @param reqDto
	 * @return
	 */
	List<PmsStoreOut> queryStoreBySeries(PmsDistributionSearchReqDto reqDto);

	/**
	 * 修改PMS同步出库的数据
	 * 
	 * @param out
	 * @return
	 */
	int updateById(PmsStoreOut out);

	/** 根据PMS流水号查询当前入库单的失败数据 **/
	List<PmsStoreOut> queryStoreOutFailure(PmsDistributionSearchReqDto reqDto);

}