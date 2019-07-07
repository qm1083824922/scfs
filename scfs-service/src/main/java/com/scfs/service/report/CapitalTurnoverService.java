package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.CapitalTurnoverDao;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.report.entity.CapitalTurnover;
import com.scfs.domain.report.req.CapitalTurnoverReqDto;
import com.scfs.domain.report.resp.CapitalTurnoverResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *
 *  File: CapitalTurnoverService.java
 *  Description: 资金周转率
 *  TODO
 *  Date,                   Who,
 *  2017年07月06日         Administrator
 *
 * </pre>
 */
@Service
public class CapitalTurnoverService {
	@Autowired
	private CapitalTurnoverDao capitalTurnoverDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ReportProjectService reportProjectService;

	/**
	 * 获取信息
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<CapitalTurnoverResDto> queryCapitalTurnoverResultsByCon(CapitalTurnoverReqDto reqDto) {
		reqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.NINE));
		PageResult<CapitalTurnoverResDto> pageResult = new PageResult<CapitalTurnoverResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		reqDto.setUserId(ServiceSupport.getUser().getId());
		List<CapitalTurnoverResDto> capitalTurnoverResDto = convertToCapitalTurnoverResDtos(
				capitalTurnoverDao.queryResultsByCon(reqDto, rowBounds));

		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<CapitalTurnover> sumResDto = capitalTurnoverDao.queryResultsByCon(reqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal saleAmount = BigDecimal.ZERO;
				BigDecimal beginAmount = BigDecimal.ZERO;
				BigDecimal endAmount = BigDecimal.ZERO;
				for (CapitalTurnover capitalTurnover : sumResDto) {
					saleAmount = DecimalUtil.add(saleAmount, capitalTurnover.getSaleAmount());
					beginAmount = DecimalUtil.add(beginAmount, capitalTurnover.getBeginAmount());
					endAmount = DecimalUtil.add(endAmount, capitalTurnover.getEndAmount());
				}
				String totalStr = "销售金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(saleAmount))
						+ " CNY &nbsp;&nbsp;&nbsp;  期初金额  : "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(beginAmount))
						+ " CNY &nbsp;&nbsp;&nbsp;  期末金额  : "
						+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(endAmount)) + " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(capitalTurnoverResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 获取所有数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public List<CapitalTurnoverResDto> queryAllResultsByCon(CapitalTurnoverReqDto reqDto) {
		reqDto.setUserId(ServiceSupport.getUser().getId());
		List<CapitalTurnover> capitalList = capitalTurnoverDao.queryResultsByCon(reqDto);
		List<CapitalTurnoverResDto> resDtoList = convertToCapitalTurnoverResDtos(capitalList);
		return resDtoList;
	}

	/**
	 * 获取资金周转率
	 * 
	 * @param reqDto
	 * @return
	 */
	public BigDecimal queryRurnoverRate(CapitalTurnoverReqDto reqDto) {
		BigDecimal rurnoverRate = capitalTurnoverDao.queryRurnoverRate(reqDto);
		if (rurnoverRate == null) {
			rurnoverRate = BigDecimal.ZERO;
		}
		return rurnoverRate;
	}

	public List<CapitalTurnoverResDto> convertToCapitalTurnoverResDtos(List<CapitalTurnover> result) {
		List<CapitalTurnoverResDto> capitalTurnoverResDto = new ArrayList<CapitalTurnoverResDto>();
		if (ListUtil.isEmpty(result)) {
			return capitalTurnoverResDto;
		}
		for (CapitalTurnover capitalTurnover : result) {
			CapitalTurnoverResDto collectResDto = convertToCapitalTurnoverResDto(capitalTurnover);
			capitalTurnoverResDto.add(collectResDto);
		}
		return capitalTurnoverResDto;
	}

	public CapitalTurnoverResDto convertToCapitalTurnoverResDto(CapitalTurnover model) {
		CapitalTurnoverResDto result = new CapitalTurnoverResDto();
		result.setId(model.getId());
		result.setDepartmentId(model.getDepartmentId());
		BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(model.getDepartmentId());
		if (null != baseDepartment) {
			result.setDepartmentName(baseDepartment.getName());
		}
		result.setProjectId(model.getProjectId());
		result.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
		result.setSaleAmount(model.getSaleAmount());
		result.setBeginAmount(model.getBeginAmount());
		result.setEndAmount(model.getEndAmount());
		result.setTurnoverRate(model.getTurnoverRate());
		result.setTurnoverRateStr(DecimalUtil.toPercentString(model.getTurnoverRate()));
		result.setCurrencyType(model.getCurrencyType());
		result.setCurrencyName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrencyType() + ""));
		result.setIssue(model.getIssue());
		result.setCreator(model.getCreator());
		result.setCreatorId(model.getCreatorId());
		result.setCreateAt(model.getCreateAt());
		result.setUpdateAt(model.getUpdateAt());
		return result;
	}

	/**
	 * 定时业务处理
	 */
	public void dealReport(String beforeDate) {
		CapitalTurnoverReqDto reqDto = new CapitalTurnoverReqDto();
		reqDto.setStartTime(beforeDate);
		reqDto.setEndTime(beforeDate);
		List<CapitalTurnover> sumResDto = capitalTurnoverDao.queryResultsByCon(reqDto);
		if (CollectionUtils.isNotEmpty(sumResDto)) {
			for (CapitalTurnover capitalTurnover : sumResDto) {
				capitalTurnoverDao.deleteById(capitalTurnover.getId());
			}
		}
		List<CapitalTurnover> capitalList = capitalTurnoverDao.queryAllResultsByCon(reqDto);
		if (CollectionUtils.isNotEmpty(capitalList)) {
			for (CapitalTurnover result : capitalList) {
				BigDecimal sumAmount = DecimalUtil.divide(
						DecimalUtil.add(result.getBeginAmount(), result.getEndAmount()),
						new BigDecimal(BaseConsts.TWO));
				BigDecimal turnoverRate = BigDecimal.ZERO;
				if (DecimalUtil.lt(sumAmount, BigDecimal.ZERO)) {
					sumAmount = BigDecimal.ZERO;
				}
				if (!DecimalUtil.eq(sumAmount, BigDecimal.ZERO)) {
					turnoverRate = DecimalUtil.divide(result.getSaleAmount(), sumAmount);// 资金周转率
					// =
					// 销售总额/（（期初金额+期末金额）/2）
				}
				result.setTurnoverRate(turnoverRate);
				result.setCreator(ServiceSupport.getUser().getChineseName());
				result.setCreatorId(ServiceSupport.getUser().getId());
				result.setCreateAt(new Date());
				capitalTurnoverDao.insert(result);
			}
		}
	}
}
