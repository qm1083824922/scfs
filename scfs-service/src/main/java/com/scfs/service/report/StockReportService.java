package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.report.StockReportDao;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.report.entity.StockReport;
import com.scfs.domain.report.req.StockReportReqDto;
import com.scfs.domain.report.resp.StockReportResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: StockReportService.java
 *  Description: 库存业务
 *  TODO
 *  Date,					Who,				
 *  2017年2月17日				Administrator
 *
 * </pre>
 */
@Service
public class StockReportService {
	@Autowired
	private StockReportDao stockReportDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ReportProjectService reportProjectService;

	/**
	 * 库存报表
	 * 
	 * @param receiveReportSearchReq
	 * @return
	 */
	public PageResult<StockReportResDto> queryResultByCon(StockReportReqDto stockReportReqDto) {
		stockReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.FOUR));

		PageResult<StockReportResDto> pageResult = new PageResult<StockReportResDto>();
		stockReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(stockReportReqDto.getPage(), stockReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, stockReportReqDto.getPer_page());
		List<StockReportResDto> stockReportResDtos = convertToResult(
				stockReportDao.queryResultsByCon(stockReportReqDto, rowBounds));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), stockReportReqDto.getPer_page());
		pageResult.setItems(stockReportResDtos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(stockReportReqDto.getPage());
		pageResult.setPer_page(stockReportReqDto.getPer_page());

		if (stockReportReqDto.getNeedSum() != null && stockReportReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			if (CollectionUtils.isNotEmpty(stockReportResDtos)) {
				BigDecimal sumStore = BigDecimal.ZERO;
				BigDecimal sumLock = BigDecimal.ZERO;
				BigDecimal sumNotLock = BigDecimal.ZERO;
				BigDecimal sumNum = BigDecimal.ZERO;
				BigDecimal sumLockNum = BigDecimal.ZERO;
				BigDecimal sumNotLockNum = BigDecimal.ZERO;
				BigDecimal expireAmount = BigDecimal.ZERO;
				BigDecimal adventAmount = BigDecimal.ZERO;
				BigDecimal expireAmount1 = BigDecimal.ZERO;
				BigDecimal expireAmount2 = BigDecimal.ZERO;
				BigDecimal expireAmount3 = BigDecimal.ZERO;
				List<StockReportResDto> countReportResDtos = convertToResult(
						stockReportDao.queryResultsByCon(stockReportReqDto));
				for (StockReportResDto stockResDto : countReportResDtos) {
					if (stockResDto != null) {
						sumStore = DecimalUtil.add(sumStore, ServiceSupport.amountNewToRMB(stockResDto.getSumStore(),
								stockResDto.getCurrencyType(), new Date()));
						sumLock = DecimalUtil.add(sumLock, ServiceSupport.amountNewToRMB(stockResDto.getSumLock(),
								stockResDto.getCurrencyType(), new Date()));
						sumNotLock = DecimalUtil.add(sumNotLock, ServiceSupport.amountNewToRMB(
								stockResDto.getSumNotLock(), stockResDto.getCurrencyType(), new Date()));
						sumNum = DecimalUtil.add(sumNum, stockResDto.getSumNum());
						sumLockNum = DecimalUtil.add(sumLockNum, stockResDto.getSumLock());
						sumNotLockNum = DecimalUtil.add(sumNotLockNum, stockResDto.getSumNotLockNum());
						expireAmount = DecimalUtil.add(expireAmount, ServiceSupport.amountNewToRMB(
								stockResDto.getExpireAmount(), stockResDto.getCurrencyType(), new Date()));
						adventAmount = DecimalUtil.add(adventAmount, ServiceSupport.amountNewToRMB(
								stockResDto.getAdventAmount(), stockResDto.getCurrencyType(), new Date()));
						expireAmount1 = DecimalUtil.add(expireAmount1, ServiceSupport.amountNewToRMB(
								stockResDto.getExpireAmount1(), stockResDto.getCurrencyType(), new Date()));
						expireAmount2 = DecimalUtil.add(expireAmount2, ServiceSupport.amountNewToRMB(
								stockResDto.getExpireAmount2(), stockResDto.getCurrencyType(), new Date()));
						expireAmount3 = DecimalUtil.add(expireAmount3, ServiceSupport.amountNewToRMB(
								stockResDto.getExpireAmount3(), stockResDto.getCurrencyType(), new Date()));
					}
				}
				String totalStr = "总库存  : " + DecimalUtil.formatScale2(sumStore) + " CNY &nbsp;&nbsp;&nbsp;  锁定库存: "
						+ DecimalUtil.formatScale2(sumLock) + " CNY &nbsp;&nbsp;&nbsp;  未锁定库存: "
						+ DecimalUtil.formatScale2(sumNotLock) + " CNY &nbsp;&nbsp;&nbsp;  总数量: "
						+ DecimalUtil.formatScale2(sumNum) + " &nbsp;&nbsp;&nbsp;  锁定数量: "
						+ DecimalUtil.formatScale2(sumLockNum) + " &nbsp;&nbsp;&nbsp;  未锁定数量: "
						+ DecimalUtil.formatScale2(sumNotLockNum) + " &nbsp;&nbsp;&nbsp;  超期库存: "
						+ DecimalUtil.formatScale2(expireAmount) + " CNY &nbsp;&nbsp;&nbsp;  临期0-7天金额: "
						+ DecimalUtil.formatScale2(adventAmount) + " CNY &nbsp;&nbsp;&nbsp;  超期1-7天金额: "
						+ DecimalUtil.formatScale2(expireAmount1) + " CNY &nbsp;&nbsp;&nbsp;  超期8-15天金额: "
						+ DecimalUtil.formatScale2(expireAmount2) + " CNY &nbsp;&nbsp;&nbsp;  超期16天以上: "
						+ DecimalUtil.formatScale2(expireAmount3) + " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		return pageResult;
	}

	/**
	 * 库存商品
	 * 
	 * @param stockReportReqDto
	 * @return
	 */
	public PageResult<StockReportResDto> queryResultDetialByCon(StockReportReqDto stockReportReqDto) {
		stockReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.FOUR));

		PageResult<StockReportResDto> pageResult = new PageResult<StockReportResDto>();
		stockReportReqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(stockReportReqDto.getPage(), stockReportReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, stockReportReqDto.getPer_page());
		List<StockReportResDto> stockReportResDtos = convertToResult(
				stockReportDao.queryResultDetialByCon(stockReportReqDto, rowBounds));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), stockReportReqDto.getPer_page());
		pageResult.setItems(stockReportResDtos);
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(stockReportReqDto.getPage());
		pageResult.setPer_page(stockReportReqDto.getPer_page());
		String totalStr = "";
		String currencySymbol = "";
		if (stockReportReqDto.getCurrencyType() == BaseConsts.ONE) {
			currencySymbol = "CNY";
		} else if (stockReportReqDto.getCurrencyType() == BaseConsts.TWO) {
			currencySymbol = "USD";
		} else if (stockReportReqDto.getCurrencyType() == BaseConsts.THREE) {
			currencySymbol = "HKD";
		}
		totalStr += "总库存金额: " + DecimalUtil.toPriceString(stockReportReqDto.getSumStore()) + currencySymbol + "   ";
		pageResult.setTotalStr(totalStr);
		return pageResult;
	}

	/**
	 * 获取库存导出数据
	 * 
	 * @param stockReportReqDto
	 * @return
	 */
	public List<StockReportResDto> queryResultByConExcel(StockReportReqDto stockReportReqDto) {
		stockReportReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.FOUR));
		stockReportReqDto.setUserId(ServiceSupport.getUser().getId());
		return convertToResult(stockReportDao.queryResultsByCon(stockReportReqDto));
	}

	public List<StockReportResDto> convertToResult(List<StockReport> result) {
		List<StockReportResDto> StockReportResDtos = new ArrayList<StockReportResDto>();
		if (ListUtil.isEmpty(result)) {
			return StockReportResDtos;
		}
		for (StockReport stockReport : result) {
			StockReportResDto stockReportResDto = convertToStockReportResDto(stockReport);
			StockReportResDtos.add(stockReportResDto);
		}
		return StockReportResDtos;
	}

	public StockReportResDto convertToStockReportResDto(StockReport model) {
		StockReportResDto result = new StockReportResDto();
		result.setId(model.getId());
		result.setSupplierId(model.getSupplierId());
		if (!StringUtils.isEmpty(model.getProjectId())) {
			result.setProjectId(model.getProjectId());
			result.setProjectName(cacheService.getProjectNameById(model.getProjectId()));
		}
		if (!StringUtils.isEmpty(model.getBusinessUnitId())) {
			result.setBusinessUnitId(model.getBusinessUnitId());
			result.setBusinessUnitName(
					cacheService.showSubjectNameByIdAndKey(model.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		}
		if (!StringUtils.isEmpty(model.getCustomerId())) {
			result.setCustomerId(model.getCustomerId());
			result.setCustomerName(cacheService.getSubjectNcByIdAndKey(model.getCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		result.setGoodsId(model.getGoodsId());
		if (!StringUtils.isEmpty(model.getGoodsId())) {
			BaseGoods goods = cacheService.getGoodsById(model.getGoodsId());
			result.setGoodsName(goods.getName());
			result.setGoodsNo(goods.getNumber());
		}
		BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(model.getDepartmentId());
		result.setDepartmentId(model.getDepartmentId());
		if (baseDepartment != null) {
			result.setDepartmentName(baseDepartment.getName());
		}
		result.setCurrencyType(model.getCurrencyType());
		result.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, model.getCurrencyType() + ""));
		result.setBizManagerId(model.getBizManagerId());
		result.setBizManagerName(cacheService.getUserChineseNameByid(model.getBizManagerId()));
		result.setWarehouseId(model.getWarehouseId());
		result.setWarehouseName(cacheService.getSubjectNcByIdAndKey(model.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		result.setSumStore(model.getSumStore());
		result.setSumLock(model.getSumLock());
		result.setSumNotLock(model.getSumNotLock());
		result.setSumNum(model.getSumNum());
		result.setSumLockNum(model.getSumLockNum());
		result.setSumNotLockNum(model.getSumNotLockNum());
		result.setAdventAmount(model.getAdventAmount());
		result.setExpireAmount(model.getExpireAmount());
		result.setExpireAmount1(model.getExpireAmount1());
		result.setExpireAmount2(model.getExpireAmount2());
		result.setExpireAmount3(model.getExpireAmount3());
		return result;
	}
}
