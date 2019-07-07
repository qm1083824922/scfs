package com.scfs.dao.report;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.scfs.domain.report.entity.MounthProfitReport;
import com.scfs.domain.report.req.ProfitReportReqMonthDto;

@Repository
public interface ProfitReportMonthDao {
	int deleteById(Integer id);

	int insert(MounthProfitReport reportRecord);

	MounthProfitReport queryEntityById(Integer id);

	/**
	 * 获取项目月份数据
	 * 
	 * @param profitReportReqMounthDto
	 * @return
	 */
	MounthProfitReport querySumProjectByCon(ProfitReportReqMonthDto profitReportReqMounthDto);

	int updateById(MounthProfitReport reportRecord);

	List<MounthProfitReport> queryResultsByCon(ProfitReportReqMonthDto profitReportReqMounthDto, RowBounds rowBounds);

	List<MounthProfitReport> queryResultsByCon(ProfitReportReqMonthDto profitReportReqMounthDto);

	int queryProfitReportMounthCount(ProfitReportReqMonthDto profitReportReqMounthDto);

	int deleteReportMounth(ProfitReportReqMonthDto profitReportReqMounthDto);

	/**
	 * 通过参数获取统计信息
	 * 
	 * @param profitReportReqMounthDto
	 * @return
	 */
	MounthProfitReport queryProfitReportMounthSum(ProfitReportReqMonthDto profitReportReqMounthDto);

	/**
	 * 获取类型分组信息
	 * 
	 * @param profitReportReqMounthDto
	 * @return
	 */
	List<MounthProfitReport> queryMounthProfitSum(ProfitReportReqMonthDto profitReportReqMounthDto);

	/**
	 * 获取项目月度指标相关数据
	 * 
	 * @param profitReportReqMounthDto
	 * @return
	 */
	List<MounthProfitReport> queryProjectMonth(ProfitReportReqMonthDto profitReportReqMounthDto);

	/**
	 * 通过人员查询月度项目利润
	 * 
	 * @param profitReportReqMounthDto
	 * @return
	 */
	List<MounthProfitReport> mounthProfitReportByUserId(ProfitReportReqMonthDto profitReportReqMounthDto);

	/**
	 * 通过部门查询月度项目利润
	 * 
	 * @param profitReportReqMounthDto
	 * @return
	 */
	List<MounthProfitReport> mounthProfitReportByDepartmentId(ProfitReportReqMonthDto profitReportReqMounthDto);

	/**
	 * 通过部门查询月度项目利润(人工费用)
	 * 
	 * @param profitReportReqMounthDto
	 * @return
	 */
	List<MounthProfitReport> mounthProfitReportByFeeDeptId(ProfitReportReqMonthDto profitReportReqMounthDto);
}