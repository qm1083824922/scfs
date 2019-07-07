package com.scfs.service.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseUserSubjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.StlHistoryDao;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.logistics.dto.req.StlHistorySearchReqDto;
import com.scfs.domain.logistics.dto.req.StlHistorySummarySearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlHistoryResDto;
import com.scfs.domain.logistics.entity.StlHistory;
import com.scfs.domain.logistics.entity.StlSum;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月25日.
 */
@Service
public class StlHistoryService {
	@Autowired
	private StlHistoryDao stlHistoryDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private BaseUserSubjectDao baseUserSubjectDao;

	/**
	 * 根据条件查询历史库存列表
	 * 
	 * @param stlHistorySearchReqDto
	 * @return
	 */
	public PageResult<StlHistoryResDto> queryStlHistoryResultsByCon(StlHistorySearchReqDto stlHistorySearchReqDto) {
		PageResult<StlHistoryResDto> result = new PageResult<StlHistoryResDto>();
		int offSet = PageUtil.getOffSet(stlHistorySearchReqDto.getPage(), stlHistorySearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, stlHistorySearchReqDto.getPer_page());

		stlHistorySearchReqDto.setStoreFlag(BaseConsts.ONE);
		stlHistorySearchReqDto.setUserId(ServiceSupport.getUser().getId());

		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		List<StlHistory> stlHistoryList = null;
		if (!CollectionUtils.isEmpty(userSubject)) {
			stlHistorySearchReqDto.setUserSubject(userSubject);
			stlHistoryList = stlHistoryDao.queryStlHistoryResultsByCon(stlHistorySearchReqDto, rowBounds);
		}

		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL_HISTORY);
		List<StlHistoryResDto> stlHistoryResDtoList = convertToResDto(stlHistoryList, isAllowPerm);
		result.setItems(stlHistoryResDtoList);
		String totalStr = querySumStlHistory(stlHistorySearchReqDto, isAllowPerm);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), stlHistorySearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(stlHistorySearchReqDto.getPage());
		result.setPer_page(stlHistorySearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 根据条件查询历史库存列表(不分页)
	 * 
	 * @param stlHistorySearchReqDto
	 * @return
	 */
	public List<StlHistoryResDto> queryAllStlHistoryResultsByCon(StlHistorySearchReqDto stlHistorySearchReqDto) {
		stlHistorySearchReqDto.setStoreFlag(BaseConsts.ONE);
		if (null == stlHistorySearchReqDto.getUserId()) {
			stlHistorySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		List<StlHistory> stlHistoryList = null;
		if (!CollectionUtils.isEmpty(userSubject)) {
			stlHistorySearchReqDto.setUserSubject(userSubject);
			stlHistoryList = stlHistoryDao.queryStlHistoryResultsByCon(stlHistorySearchReqDto);
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL_HISTORY);
		List<StlHistoryResDto> stlHistoryResDtoList = convertToResDto(stlHistoryList, isAllowPerm);

		return stlHistoryResDtoList;
	}

	/**
	 * 根据条件查询历史库存汇总列表
	 * 
	 * @param stlHistorySummarySearchReqDto
	 * @return
	 */
	public PageResult<StlHistoryResDto> queryStlHistorySummaryResultsByCon(
			StlHistorySummarySearchReqDto stlHistorySummarySearchReqDto) {
		PageResult<StlHistoryResDto> result = new PageResult<StlHistoryResDto>();
		int offSet = PageUtil.getOffSet(stlHistorySummarySearchReqDto.getPage(),
				stlHistorySummarySearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, stlHistorySummarySearchReqDto.getPer_page());

		List<String> groupByCondition = stlHistorySummarySearchReqDto.getGroupByCondition();
		stlHistorySummarySearchReqDto.setGroupByCondition(groupByCondition);
		stlHistorySummarySearchReqDto.setStoreFlag(BaseConsts.ONE);
		stlHistorySummarySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<StlHistory> stlHistoryList = stlHistoryDao
				.queryStlHistorySummaryResultsByCon(stlHistorySummarySearchReqDto, rowBounds);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL_HISTORY);
		List<StlHistoryResDto> stlHistoryResDtoList = convertToResDto(stlHistoryList, isAllowPerm);
		result.setItems(stlHistoryResDtoList);
		String totalStr = querySumStlHistorySummary(stlHistorySummarySearchReqDto, isAllowPerm);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), stlHistorySummarySearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(stlHistorySummarySearchReqDto.getPage());
		result.setPer_page(stlHistorySummarySearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 根据条件查询历史库存汇总列表(不分页)
	 * 
	 * @param stlHistorySummarySearchReqDto
	 * @return
	 */
	public List<StlHistoryResDto> queryAllStlHistorySummaryResultsByCon(
			StlHistorySummarySearchReqDto stlHistorySummarySearchReqDto) {
		List<String> groupByCondition = stlHistorySummarySearchReqDto.getGroupByCondition();

		stlHistorySummarySearchReqDto.setGroupByCondition(groupByCondition);
		stlHistorySummarySearchReqDto.setStoreFlag(BaseConsts.ONE);
		if (null == stlHistorySummarySearchReqDto.getUserId()) {
			stlHistorySummarySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<StlHistory> stlHistoryList = stlHistoryDao
				.queryStlHistorySummaryResultsByCon(stlHistorySummarySearchReqDto);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL_HISTORY,
				stlHistorySummarySearchReqDto.getUserId());
		List<StlHistoryResDto> stlHistoryResDtoList = convertToResDto(stlHistoryList, isAllowPerm);

		return stlHistoryResDtoList;
	}

	/**
	 * 查询合计
	 * 
	 * @param stlSearchReqDto
	 * @param isAllowPerm
	 * @return
	 */
	private String querySumStlHistory(StlHistorySearchReqDto stlHistorySearchReqDto, boolean isAllowPerm) {
		String totalStr = "";
		if (stlHistorySearchReqDto.getNeedSum() != null && stlHistorySearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<StlSum> stlSumList = stlHistoryDao.querySumStlHistory(stlHistorySearchReqDto);
			totalStr = getTotalStr(stlSumList, isAllowPerm, false);
		}
		return totalStr;
	}

	/**
	 * 汇总查询合计
	 * 
	 * @param stlSearchReqDto
	 * @param isAllowPerm
	 * @return
	 */
	private String querySumStlHistorySummary(StlHistorySummarySearchReqDto stlHistorySummarySearchReqDto,
			boolean isAllowPerm) {
		String totalStr = "";
		if (stlHistorySummarySearchReqDto.getNeedSum() != null
				&& stlHistorySummarySearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<StlSum> stlSumList = stlHistoryDao.querySumStlHistorySummary(stlHistorySummarySearchReqDto);
			totalStr = getTotalStr(stlSumList, isAllowPerm, true);
		}
		return totalStr;
	}

	public String getTotalStr(List<StlSum> stlSumList, boolean isAllowPerm, boolean isSummary) {
		String totalStr = "";
		if (!CollectionUtils.isEmpty(stlSumList)) {
			BigDecimal totalTallyNum = BigDecimal.ZERO;
			BigDecimal totalStoreNum = BigDecimal.ZERO;
			BigDecimal totalLockNum = BigDecimal.ZERO;
			BigDecimal totalSaleLockNum = BigDecimal.ZERO;
			BigDecimal totalAvailableNum = BigDecimal.ZERO;
			BigDecimal totalCostPrice = BigDecimal.ZERO;
			BigDecimal totalPoPrice = BigDecimal.ZERO;
			BigDecimal totalStoreAmount = BigDecimal.ZERO;
			BigDecimal totalLockAmount = BigDecimal.ZERO;
			BigDecimal totalSaleLockAmount = BigDecimal.ZERO;
			BigDecimal totalAvailableAmount = BigDecimal.ZERO;
			String totalStoreAmountStr = "";
			String totalLockAmountStr = "";
			String totalSaleLockAmountStr = "";
			String totalAvailableAmountStr = "";

			for (StlSum stlSum : stlSumList) {
				totalTallyNum = DecimalUtil.add(totalTallyNum,
						null == stlSum.getTotalTallyNum() ? BigDecimal.ZERO : stlSum.getTotalTallyNum());
				totalStoreNum = DecimalUtil.add(totalStoreNum,
						null == stlSum.getTotalStoreNum() ? BigDecimal.ZERO : stlSum.getTotalStoreNum());
				totalLockNum = DecimalUtil.add(totalLockNum,
						null == stlSum.getTotalLockNum() ? BigDecimal.ZERO : stlSum.getTotalLockNum());
				totalSaleLockNum = DecimalUtil.add(totalSaleLockNum,
						null == stlSum.getTotalSaleLockNum() ? BigDecimal.ZERO : stlSum.getTotalSaleLockNum());
				totalAvailableNum = DecimalUtil.add(totalAvailableNum,
						null == stlSum.getTotalAvailableNum() ? BigDecimal.ZERO : stlSum.getTotalAvailableNum());
				BigDecimal cnyTotalCostPrice = BigDecimal.ZERO;
				BigDecimal cnyTotalPoPrice = BigDecimal.ZERO;
				BigDecimal cnyTotalStoreAmount = BigDecimal.ZERO;
				BigDecimal cnyTotalLockAmount = BigDecimal.ZERO;
				BigDecimal cnyTotalSaleLockAmount = BigDecimal.ZERO;
				BigDecimal cnyTotalAvailableAmount = BigDecimal.ZERO;
				if (null != stlSum.getCurrencyType()) {
					cnyTotalCostPrice = ServiceSupport.amountNewToRMB(
							null == stlSum.getTotalCostPrice() ? BigDecimal.ZERO : stlSum.getTotalCostPrice(),
							stlSum.getCurrencyType(), null);
					cnyTotalPoPrice = ServiceSupport.amountNewToRMB(
							null == stlSum.getTotalPoPrice() ? BigDecimal.ZERO : stlSum.getTotalPoPrice(),
							stlSum.getCurrencyType(), null);
					cnyTotalStoreAmount = ServiceSupport.amountNewToRMB(
							null == stlSum.getTotalStoreAmount() ? BigDecimal.ZERO : stlSum.getTotalStoreAmount(),
							stlSum.getCurrencyType(), null);
					cnyTotalLockAmount = ServiceSupport.amountNewToRMB(
							null == stlSum.getTotalLockAmount() ? BigDecimal.ZERO : stlSum.getTotalLockAmount(),
							stlSum.getCurrencyType(), null);
					cnyTotalSaleLockAmount = ServiceSupport.amountNewToRMB(
							null == stlSum.getTotalSaleLockAmount() ? BigDecimal.ZERO : stlSum.getTotalSaleLockAmount(),
							stlSum.getCurrencyType(), null);
					cnyTotalAvailableAmount = ServiceSupport.amountNewToRMB(null == stlSum.getTotalAvailableAmount()
							? BigDecimal.ZERO : stlSum.getTotalAvailableAmount(), stlSum.getCurrencyType(), null);
				}
				totalCostPrice = DecimalUtil.add(totalCostPrice,
						null == cnyTotalCostPrice ? BigDecimal.ZERO : cnyTotalCostPrice);
				totalPoPrice = DecimalUtil.add(totalPoPrice,
						null == cnyTotalPoPrice ? BigDecimal.ZERO : cnyTotalPoPrice);
				totalStoreAmount = DecimalUtil.add(totalStoreAmount,
						null == cnyTotalStoreAmount ? BigDecimal.ZERO : cnyTotalStoreAmount);
				totalLockAmount = DecimalUtil.add(totalLockAmount,
						null == cnyTotalLockAmount ? BigDecimal.ZERO : cnyTotalLockAmount);
				totalSaleLockAmount = DecimalUtil.add(totalSaleLockAmount,
						null == cnyTotalSaleLockAmount ? BigDecimal.ZERO : cnyTotalSaleLockAmount);
				totalAvailableAmount = DecimalUtil.add(totalAvailableAmount,
						null == cnyTotalAvailableAmount ? BigDecimal.ZERO : cnyTotalAvailableAmount);
			}
			if (isAllowPerm == true) {
				totalStoreAmountStr = BaseConsts.NO_PERMISSION_HIT;
				totalLockAmountStr = BaseConsts.NO_PERMISSION_HIT;
				totalSaleLockAmountStr = BaseConsts.NO_PERMISSION_HIT;
				totalAvailableAmountStr = BaseConsts.NO_PERMISSION_HIT;
			} else {
				totalStoreAmountStr = DecimalUtil.toAmountString(totalStoreAmount);
				totalLockAmountStr = DecimalUtil.toAmountString(totalLockAmount);
				totalSaleLockAmountStr = DecimalUtil.toAmountString(totalSaleLockAmount);
				totalAvailableAmountStr = DecimalUtil.toAmountString(totalAvailableAmount);
			}
			if (isSummary == true) {
				totalStr = "库存数量：" + DecimalUtil.toQuantityString(totalStoreNum) + "；出库锁定数量："
						+ DecimalUtil.toQuantityString(totalLockNum) + "；销售锁定数量："
						+ DecimalUtil.toQuantityString(totalSaleLockNum) + "；可用数量："
						+ DecimalUtil.toQuantityString(totalAvailableNum) + "；库存金额：" + totalStoreAmountStr
						+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；出库锁定金额："
						+ totalLockAmountStr + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；销售锁定金额：" + totalSaleLockAmountStr
						+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；可用金额："
						+ totalAvailableAmountStr + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
			} else {
				totalStr = "库存数量：" + DecimalUtil.toQuantityString(totalStoreNum) + "；出库锁定数量："
						+ DecimalUtil.toQuantityString(totalLockNum) + "；销售锁定数量："
						+ DecimalUtil.toQuantityString(totalSaleLockNum) + "；可用数量："
						+ DecimalUtil.toQuantityString(totalAvailableNum) + "；库存金额：" + totalStoreAmountStr
						+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；出库锁定金额："
						+ totalLockAmountStr + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；销售锁定金额：" + totalSaleLockAmountStr
						+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；可用金额："
						+ totalAvailableAmountStr + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
			}
		}
		return totalStr;
	}

	private List<StlHistoryResDto> convertToResDto(List<StlHistory> stlHistoryList, boolean isAllowPerm) {
		List<StlHistoryResDto> stlHistoryResDtoList = new ArrayList<StlHistoryResDto>(5);
		if (CollectionUtils.isEmpty(stlHistoryList)) {
			return stlHistoryResDtoList;
		}
		for (StlHistory stlHistory : stlHistoryList) {
			StlHistoryResDto stlHistoryResDto = convertToResDto(stlHistory, isAllowPerm);
			stlHistoryResDtoList.add(stlHistoryResDto);
		}
		return stlHistoryResDtoList;
	}

	private StlHistoryResDto convertToResDto(StlHistory stlHistory, boolean isAllowPerm) {
		StlHistoryResDto stlHistoryResDto = new StlHistoryResDto();
		BeanUtils.copyProperties(stlHistory, stlHistoryResDto);
		if (stlHistory.getGoodsStatus() != null) {
			stlHistoryResDto.setGoodsStatusName(ServiceSupport.getValueByBizCode(
					BizCodeConsts.BILL_IN_STORE_GOODS_STATUS, Integer.toString(stlHistory.getGoodsStatus())));
		}
		if (stlHistory.getCurrencyType() != null) {
			stlHistoryResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					Integer.toString(stlHistory.getCurrencyType())));
		}
		if (stlHistory.getGoodsId() != null) {
			BaseGoods baseGoods = cacheService.getGoodsById(stlHistory.getGoodsId());
			if (null != baseGoods) {
				stlHistoryResDto.setGoodsName(baseGoods.getName());
				stlHistoryResDto.setGoodsNumber(baseGoods.getNumber());
				stlHistoryResDto.setGoodsType(baseGoods.getType());
				stlHistoryResDto.setGoodsBarCode(baseGoods.getBarCode());
			}
		}
		stlHistoryResDto.setProjectName(cacheService.showProjectNameById(stlHistory.getProjectId()));
		stlHistoryResDto.setSupplierName(
				cacheService.showSubjectNameByIdAndKey(stlHistory.getSupplierId(), CacheKeyConsts.SUPPLIER));
		stlHistoryResDto.setWarehouseName(
				cacheService.showSubjectNameByIdAndKey(stlHistory.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		stlHistoryResDto.setCustomerName(
				cacheService.showSubjectNameByIdAndKey(stlHistory.getCustomerId(), CacheKeyConsts.CUSTOMER));

		BigDecimal storeAmount = (null == stlHistoryResDto.getStoreAmount() ? BigDecimal.ZERO
				: stlHistoryResDto.getStoreAmount());
		BigDecimal lockAmount = (null == stlHistoryResDto.getLockAmount() ? BigDecimal.ZERO
				: stlHistoryResDto.getLockAmount());
		BigDecimal saleLockAmount = (null == stlHistoryResDto.getSaleLockAmount() ? BigDecimal.ZERO
				: stlHistoryResDto.getSaleLockAmount());
		BigDecimal availableAmount = (null == stlHistoryResDto.getAvailableAmount() ? BigDecimal.ZERO
				: stlHistoryResDto.getAvailableAmount());
		BigDecimal costPrice = (null == stlHistoryResDto.getCostPrice() ? BigDecimal.ZERO
				: stlHistoryResDto.getCostPrice());
		BigDecimal poPrice = (null == stlHistoryResDto.getPoPrice() ? BigDecimal.ZERO : stlHistoryResDto.getPoPrice());
		stlHistoryResDto.setStoreAmountStr(DecimalUtil.toAmountString(storeAmount));
		stlHistoryResDto.setLockAmountStr(DecimalUtil.toAmountString(lockAmount));
		stlHistoryResDto.setSaleLockAmountStr(DecimalUtil.toAmountString(saleLockAmount));
		stlHistoryResDto.setAvailableAmountStr(DecimalUtil.toAmountString(availableAmount));
		stlHistoryResDto.setCostPriceStr(DecimalUtil.toPriceString(costPrice));
		stlHistoryResDto.setPoPriceStr(DecimalUtil.toPriceString(poPrice));
		if (isAllowPerm) { // 不显示金额权限
			stlHistoryResDto.setCurrencyTypeName(BaseConsts.NO_PERMISSION_HIT);
			stlHistoryResDto.setStoreAmountStr(BaseConsts.NO_PERMISSION_HIT);
			stlHistoryResDto.setLockAmountStr(BaseConsts.NO_PERMISSION_HIT);
			stlHistoryResDto.setSaleLockAmountStr(BaseConsts.NO_PERMISSION_HIT);
			stlHistoryResDto.setAvailableAmountStr(BaseConsts.NO_PERMISSION_HIT);
			stlHistoryResDto.setCostPriceStr(BaseConsts.NO_PERMISSION_HIT);
			stlHistoryResDto.setPoPriceStr(BaseConsts.NO_PERMISSION_HIT);
		}
		return stlHistoryResDto;
	}

	public boolean isOverStlHistoryMaxLine(StlHistorySearchReqDto stlHistorySearchReqDto) {
		stlHistorySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = stlHistoryDao.queryStlHistoryCountByCon(stlHistorySearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("历史库存导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncStlHistoryExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/stl_history_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_18);
			asyncExcelService.addAsyncExcel(stlHistorySearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncStlHistoryExport(StlHistorySearchReqDto stlHistorySearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<StlHistoryResDto> stlHistoryResDtoList = queryAllStlHistoryResultsByCon(stlHistorySearchReqDto);
		model.put("stlHistoryList", stlHistoryResDtoList);
		return model;
	}

	public boolean isOverStlHistorySummaryMaxLine(StlHistorySummarySearchReqDto stlHistorySummaryReqDto) {
		stlHistorySummaryReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = stlHistoryDao.queryStlHistorySummaryCountByCon(stlHistorySummaryReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("历史库存汇总导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncStlHistorySummaryExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/stl_history_summary_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_19);
			asyncExcelService.addAsyncExcel(stlHistorySummaryReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncStlHistorySummaryExport(StlHistorySummarySearchReqDto stlHistorySummaryReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<StlHistoryResDto> stlHistoryResDtoList = queryAllStlHistorySummaryResultsByCon(stlHistorySummaryReqDto);
		model.put("stlHistorySummaryList", stlHistoryResDtoList);
		return model;
	}
}
