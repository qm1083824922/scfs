package com.scfs.dao.logistics;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.dto.req.StlSummarySearchReqDto;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.logistics.entity.StlSum;

public interface StlDao {
	int deleteById(Integer id);

	int insert(Stl stl);

	Stl queryAndLockEntityById(Integer id);

	Stl queryEntityById(Integer id);

	int updateById(Stl stl);

	List<Stl> queryStlResultsByCon(StlSearchReqDto stlSearchReqDto, RowBounds rowBounds);

	List<Stl> queryStlResultsByCon(StlSearchReqDto stlSearchReqDto);

	int queryStlCountByCon(StlSearchReqDto stlSearchReqDto);

	List<Stl> queryStlSummaryResultsByCon(StlSummarySearchReqDto stlSummarySearchReqDto, RowBounds rowBounds);

	List<Stl> queryStlSummaryResultsByCon(StlSummarySearchReqDto stlSummarySearchReqDto);

	int queryStlSummaryCountByCon(StlSummarySearchReqDto stlSummarySearchReqDto);

	List<Stl> queryStl4FIFO(StlSearchReqDto stlSearchReqDto);

	int queryStlCount4LastDay(StlSearchReqDto stlSearchReqDto);

	Stl queryStlByBillInStoreTallyDtlId(Integer billInStoreTallyDtlId);

	/**
	 * 库存数量合计
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	StlSum querySumResultsByCon(StlSearchReqDto stlSearchReqDto);

	/**
	 * 查询显示合计
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	List<StlSum> querySumStl(StlSearchReqDto stlSearchReqDto);

	/**
	 * 汇总查询显示合计
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	List<StlSum> querySumStlSummary(StlSummarySearchReqDto stlSummarySearchReqDto);

	List<Stl> queryResultsByPoLineId(Integer poLineId);

	List<Stl> queryResultsByPoId(Integer pod);

	List<Stl> queryResultsByPayId(Integer payId);

	/**
	 * 通过入库编号修改库存
	 * 
	 * @param stl
	 * @return
	 */
	int updateByBillInStoreId(Stl stl);

	/**
	 * 原首页 获取账期临期3天，7天库存
	 * 
	 * @return
	 */
	List<Stl> queryResultsByAdvent(Integer day);

	/**
	 * now首页相关 在仓库存
	 * 
	 * @param stlSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<Stl> queryResultsGroupByGoodsNumber(StlSearchReqDto stlSearchReqDto, RowBounds rowBounds);

	List<Stl> queryResultsGroupByGoodsNumber(StlSearchReqDto stlSearchReqDto);

	// 在仓库存总金额,总数量
	Stl querySumNowStlAmount(StlSearchReqDto stlSearchReqDto);

	/**
	 * 获取平均库龄
	 * 
	 * @return
	 */
	BigDecimal queryAvgOldLibrary(StlSearchReqDto stlSearchReqDto);

	/**
	 * 获取平均库龄数据
	 * 
	 * @param stlSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<Stl> queryResultsGroupByAvgAge(StlSearchReqDto stlSearchReqDto, RowBounds rowBounds);

	/**
	 * 超期库存
	 * 
	 * @param stlSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	List<Stl> queryResultsGroupByOver(StlSearchReqDto stlSearchReqDto, RowBounds rowBounds);

	List<Stl> queryResultsGroupByOver(StlSearchReqDto stlSearchReqDto);

	/**
	 * 超期库存CNY总金额
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	BigDecimal querySumAmount(StlSearchReqDto stlSearchReqDto);
}