package com.scfs.dao.api.pms;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import com.scfs.domain.api.pms.entity.PmsSeries;
import com.scfs.domain.api.pms.model.PmsSeriesModel;
import com.scfs.domain.interf.dto.PmsDistributionSearchReqDto;

public interface PmsSeriesDao {
	int deleteById(Integer id);

	int insert(PmsSeries record);

	PmsSeries queryEntityById(Integer id);

	int updateById(PmsSeries record);

	/**
	 * 查询当前PMS铺货整体接口数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	List<PmsSeriesModel> queryResultByCon(PmsDistributionSearchReqDto reqDto, RowBounds rowBounds);

	List<PmsSeriesModel> querySuccessResultByCon(PmsDistributionSearchReqDto reqDto, RowBounds rowBounds);

}