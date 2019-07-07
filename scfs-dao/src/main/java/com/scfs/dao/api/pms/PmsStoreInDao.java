package com.scfs.dao.api.pms;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.api.pms.entity.PmsStoreIn;
import com.scfs.domain.interf.dto.PmsDistributionSearchReqDto;
import com.scfs.domain.po.dto.req.PmsStoreInReqDto;

public interface PmsStoreInDao {
	int deleteById(Integer id);

	int insert(PmsStoreIn record);

	int batchInsert(List<PmsStoreIn> item);

	PmsStoreIn queryEntityById(Integer id);

	int updateById(PmsStoreIn record);

	List<PmsStoreIn> queryPmsStoreIn();

	List<PmsStoreIn> queryPmsStoreInByCon(PmsStoreInReqDto pmsStoreInReqDto);

	List<PmsStoreIn> queryPmsStoreInBySeries(PmsDistributionSearchReqDto reqDto, RowBounds rowBounds);

	List<PmsStoreIn> queryPmsStoreInBySeries(PmsDistributionSearchReqDto reqDto);

	/** 根据PMS铺货流水号查询PMS入库调用失败的数据 **/
	List<PmsStoreIn> queryPmsStoreInFailure(PmsDistributionSearchReqDto reqDto);

}