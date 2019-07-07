package com.scfs.dao.logistics;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.logistics.dto.req.StlHistorySearchReqDto;
import com.scfs.domain.logistics.dto.req.StlHistorySummarySearchReqDto;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.logistics.entity.StlHistory;
import com.scfs.domain.logistics.entity.StlSum;

public interface StlHistoryDao {
	int deleteById(Integer id);

	int insert(StlHistory stlHistory);

	Stl queryEntityById(Integer id);

	int updateById(StlHistory stlHistory);

	List<StlHistory> queryStlHistoryResultsByCon(StlHistorySearchReqDto stlHistorySearchReqDto, RowBounds rowBounds);

	List<StlHistory> queryStlHistoryResultsByCon(StlHistorySearchReqDto stlHistorySearchReqDto);

	int queryStlHistoryCountByCon(StlHistorySearchReqDto stlHistorySearchReqDto);

	List<StlHistory> queryStlHistorySummaryResultsByCon(StlHistorySummarySearchReqDto stlHistorySummarySearchReqDto,
			RowBounds rowBounds);

	List<StlHistory> queryStlHistorySummaryResultsByCon(StlHistorySummarySearchReqDto stlHistorySummarySearchReqDto);

	int queryStlHistorySummaryCountByCon(StlHistorySummarySearchReqDto stlHistorySummarySearchReqDto);

	int copyInsert(@Param("offSet") int offSet, @Param("pageSize") int pageSize,
			@Param("stlSearchReqDto") StlSearchReqDto stlSearchReqDto);

	int queryStlHistoryCountByCopyDate(@Param("copyDate") Date copyDate);

	List<StlSum> querySumStlHistory(StlHistorySearchReqDto stlHistorySearchReqDto);

	List<StlSum> querySumStlHistorySummary(StlHistorySummarySearchReqDto stlHistorySummarySearchReqDto);

}