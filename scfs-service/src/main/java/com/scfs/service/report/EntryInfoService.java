package com.scfs.service.report;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fi.ReceiptPoolDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.StlDao;
import com.scfs.domain.base.entity.BaseDepartment;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.resp.FundPoolResDto;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.fi.entity.ReceiptPool;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlResDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.report.req.CapitalTurnoverReqDto;
import com.scfs.domain.report.resp.EntryInfoResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.CommonParamValidate;
import com.scfs.service.common.ReportProjectService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.logistics.StlService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *
 *  File: EntryInfoService.java
 *  Description: 首页相关信息统计
 *  TODO
 *  Date,                   Who,
 *  2017年06月27日         Administrator
 *
 * </pre>
 */
@Service
public class EntryInfoService {
	@Autowired
	private PurchaseOrderService purchaseOrderService;// 采购订单表
	@Autowired
	private StlDao stlDao;// 库存相关
	@Autowired
	private StlService stlService;
	@Autowired
	private ReceiveService receiveService;// 应收管理相关
	@Autowired
	private CapitalTurnoverService capitalTurnoverService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ReportProjectService reportProjectService;
	@Autowired
	private ReceiptPoolDao receiptPoolDao;
	@Autowired
	private CommonParamValidate commonParamValidate;

	public Result<EntryInfoResDto> queryEntriInfoDetail() {
		Result<EntryInfoResDto> result = new Result<EntryInfoResDto>();
		BigDecimal poAmount = BigDecimal.ZERO;
		BigDecimal stlAmount = BigDecimal.ZERO;
		BigDecimal avgStlAge = BigDecimal.ZERO;
		BigDecimal overStlAmount = BigDecimal.ZERO;
		BigDecimal overReceiveAmount = BigDecimal.ZERO;
		BigDecimal riskAmount = BigDecimal.ZERO;
		BigDecimal turnoverRate = BigDecimal.ZERO;
		String beforeDate = DateFormatUtils.format(DateFormatUtils.YYYYMM, DateFormatUtils.getPreMonthDate(new Date()));// 获取上个月
		Integer userId = ServiceSupport.getUser().getId();

		// 获取资金周转率
		CapitalTurnoverReqDto capitalReqDto = new CapitalTurnoverReqDto();
		capitalReqDto.setIssue(beforeDate);
		capitalReqDto.setUserId(userId);
		capitalReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.NINE));
		turnoverRate = capitalTurnoverService.queryRurnoverRate(capitalReqDto);

		// 待到货PO单金额
		PoTitleReqDto poReqDto = new PoTitleReqDto();
		poReqDto.setOrderType(BaseConsts.ZERO);
		poReqDto.setUserId(userId);
		poReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.TEN));
		poAmount = purchaseOrderService.querySumPoBlance(poReqDto);

		// 在仓库存
		StlSearchReqDto stlSearchReqDto = new StlSearchReqDto();
		stlSearchReqDto.setUserId(userId);
		stlSearchReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_11));
		List<StlResDto> stlResDto = stlService.queryAllInSenateStl(stlSearchReqDto);
		stlAmount = getStlAmount(stlResDto);

		// 平均库龄
		StlSearchReqDto searchReqDto = new StlSearchReqDto();
		searchReqDto.setUserId(userId);
		searchReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_12));
		avgStlAge = stlDao.queryAvgOldLibrary(searchReqDto);

		// 超期库存金额
		StlSearchReqDto overStlReqDto = new StlSearchReqDto();
		overStlReqDto.setDay(BaseConsts.ZERO);
		overStlReqDto.setUserId(userId);
		overStlReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_13));
		overStlAmount = stlService.querySumAmount(overStlReqDto);

		// 超期应收
		ReceiveSearchReqDto receiveReqDto = new ReceiveSearchReqDto();
		receiveReqDto.setUserId(userId);
		receiveReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_14));
		overReceiveAmount = receiveService.queryResultsSum(receiveReqDto);

		// 动销滞销风险
		StlSearchReqDto riskReqDto = new StlSearchReqDto();
		riskReqDto.setUserId(ServiceSupport.getUser().getId());
		riskReqDto.setDay(BaseConsts.SEVEN);
		riskReqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_15));
		riskAmount = stlService.querySumAmount(riskReqDto);

		EntryInfoResDto entryInfo = new EntryInfoResDto();
		entryInfo.setPoAmountStr(
				DecimalUtil.toAmountString(DecimalUtil.divide(poAmount, new BigDecimal(BaseConsts.INT_10000))));
		entryInfo.setStlAmountStr(
				DecimalUtil.toAmountString(DecimalUtil.divide(stlAmount, new BigDecimal(BaseConsts.INT_10000))));
		entryInfo.setAvgStlAgeStr(DecimalUtil.toAmountString(avgStlAge));
		entryInfo.setOverStlAmountStr(
				DecimalUtil.toAmountString(DecimalUtil.divide(overStlAmount, new BigDecimal(BaseConsts.INT_10000))));
		entryInfo.setOverReceiveStr(DecimalUtil
				.toAmountString(DecimalUtil.divide(overReceiveAmount, new BigDecimal(BaseConsts.INT_10000))));
		entryInfo.setRiskAmountStr(
				DecimalUtil.toAmountString(DecimalUtil.divide(riskAmount, new BigDecimal(BaseConsts.INT_10000))));
		entryInfo.setTurnoverRate(DecimalUtil.toPercent(turnoverRate));
		result.setItems(entryInfo);
		return result;
	}

	/**
	 * 获取待到货po单信息
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<PoTitleRespDto> queryPoWaitOrderResult(PoTitleReqDto reqDto) {
		reqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.TEN));
		reqDto.setOrderType(BaseConsts.ZERO);
		reqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<PoTitleRespDto> pageResult = new PageResult<PoTitleRespDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<PoTitleRespDto> poRespDto = purchaseOrderService.queryPuOrderTitleListGroupByNo(reqDto, rowBounds);
		if (CollectionUtils.isNotEmpty(poRespDto)) {
			for (PoTitleRespDto poTitleRespDto : poRespDto) {
				BaseProject baseProject = cacheService.getProjectById(poTitleRespDto.getProjectId());
				poTitleRespDto.setDepartmentId(baseProject.getDepartmentId());
				BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseProject.getDepartmentId());
				if (null != baseDepartment) {
					poTitleRespDto.setDepartmentName(baseDepartment.getName());
				}
			}
		}
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			BigDecimal poAmount = BigDecimal.ZERO;
			poAmount = purchaseOrderService.querySumPoBlance(reqDto);
			String totalStr = "未到货金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(poAmount)) + " CNY ";
			pageResult.setTotalStr(totalStr);
		}
		pageResult.setItems(poRespDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 在仓库存
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<StlResDto> queryInSenateStlResult(StlSearchReqDto reqDto) {
		reqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_11));
		reqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<StlResDto> pageResult = new PageResult<StlResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<StlResDto> stlResDto = stlService.queryInSenateStl(reqDto, rowBounds);
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<StlResDto> sumResDto = stlService.queryAllInSenateStl(reqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal stlAmount = BigDecimal.ZERO;
				BigDecimal num = BigDecimal.ZERO;
				for (StlResDto resDto : sumResDto) {
					BigDecimal storeAmount = DecimalUtil.multiply(resDto.getAvailableModel(), resDto.getCnyRate());// 转换RNY
					stlAmount = DecimalUtil.add(stlAmount, storeAmount);
					num = DecimalUtil.add(num, resDto.getStoreNum());
				}
				String totalStr = "库存数量  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(num))
						+ "&nbsp;&nbsp 库存金额   :   " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(stlAmount))
						+ " CNY ";
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(stlResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 平均库龄信息
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<StlResDto> queryAvgStlAgeResult(StlSearchReqDto reqDto) {
		reqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_12));
		reqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<StlResDto> pageResult = new PageResult<StlResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<StlResDto> stlResDto = stlService.queryAvgStlAge(reqDto, rowBounds);
		pageResult.setItems(stlResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 获取超期库存
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<StlResDto> queryStlByOverAge(StlSearchReqDto reqDto) {
		reqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_13));
		reqDto.setDay(BaseConsts.ZERO);
		reqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<StlResDto> pageResult = new PageResult<StlResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<StlResDto> stlResDto = stlService.queryStlByOverAge(reqDto, rowBounds);
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			String totalStr = "库存金额   :   "
					+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(stlService.querySumAmount(reqDto))) + " CNY ";
			pageResult.setTotalStr(totalStr);
		}
		pageResult.setItems(stlResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 超期应收
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<ReceiveResDto> queryOverDayReceive(ReceiveSearchReqDto reqDto) {
		reqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_14));
		reqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<ReceiveResDto> pageResult = new PageResult<ReceiveResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<ReceiveResDto> receiveResDto = receiveService.queryOverDayResultsByCon(reqDto, rowBounds);
		if (CollectionUtils.isNotEmpty(receiveResDto)) {
			for (ReceiveResDto resDto : receiveResDto) {
				BaseProject baseProject = cacheService.getProjectById(resDto.getProjectId());
				resDto.setDepartmentId(baseProject.getDepartmentId());
				BaseDepartment baseDepartment = cacheService.getBaseDepartmentById(baseProject.getDepartmentId());
				if (null != baseDepartment) {
					resDto.setDepartmentName(baseDepartment.getName());
				}
			}
		}
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			String totalStr = "超期应收金额   :   "
					+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(receiveService.queryResultsSum(reqDto)))
					+ " CNY ";
			pageResult.setTotalStr(totalStr);
		}
		pageResult.setItems(receiveResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 动销滞销风险
	 * 
	 * @param reqDto
	 * @return
	 */
	public PageResult<StlResDto> queryRiskStlResult(StlSearchReqDto reqDto) {
		reqDto.setExcludeProjectIdList(reportProjectService.queryReportProject(BaseConsts.INT_15));
		reqDto.setDay(BaseConsts.SEVEN);
		reqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<StlResDto> pageResult = new PageResult<StlResDto>();
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<StlResDto> stlResDto = stlService.queryStlByOverAge(reqDto, rowBounds);
		if (CollectionUtils.isNotEmpty(stlResDto)) {
			for (StlResDto stlRes : stlResDto) {
				if (DecimalUtil.le(stlRes.getNearAge(), BigDecimal.ZERO)) {
					stlRes.setOverAge(null);
				} else {
					stlRes.setOverAge(stlRes.getNearAge());
					stlRes.setNearAge(null);
				}
			}
		}
		if (reqDto.getNeedSum() != null && reqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			String totalStr = "库存金额   :   "
					+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(stlService.querySumAmount(reqDto))) + " CNY ";
			pageResult.setTotalStr(totalStr);
		}
		pageResult.setItems(stlResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 获取列表总金额
	 * 
	 * @param sumResDto
	 * @return
	 */
	private BigDecimal getStlAmount(List<StlResDto> sumResDto) {
		BigDecimal stlAmount = BigDecimal.ZERO;
		if (CollectionUtils.isNotEmpty(sumResDto)) {
			for (StlResDto resDto : sumResDto) {
				BigDecimal storeAmount = DecimalUtil.multiply(resDto.getAvailableModel(), resDto.getCnyRate());// 转换RNY
				stlAmount = DecimalUtil.add(stlAmount, storeAmount);
			}
		}
		return stlAmount;
	}

	/**
	 * 获取资金值资产值
	 * 
	 * @return
	 */
	public Result<EntryInfoResDto> queryFundPool(String businName) {
		Result<EntryInfoResDto> result = new Result<EntryInfoResDto>();
		String businId = commonParamValidate.getAllCdByKeyValidate(CacheKeyConsts.BUSI_UNIT, businName);
		if (StringUtils.isNotEmpty(businId)) {
			EntryInfoResDto resDto = new EntryInfoResDto();
			BigDecimal usedFundAmountRate = BigDecimal.ZERO;// 已使用资金额度比例
			BigDecimal blanceFundAmountRate = BigDecimal.ZERO;// 资金余额比例
			BigDecimal usedAssetAmountRate = BigDecimal.ZERO;// 已使用资产比例
			BigDecimal blanceAssetAmountRate = BigDecimal.ZERO;// 资产余额比例
			BigDecimal advancePayAmountRate = BigDecimal.ZERO;// 预付比例
			BigDecimal paymentAmountRate = BigDecimal.ZERO;// 在途货款比例
			BigDecimal recAmountRate = BigDecimal.ZERO;// 应收比例
			BigDecimal stlAmountRate = BigDecimal.ZERO;// 库存比例

			BigDecimal blanceFundAmount = BigDecimal.ZERO;// 资金余额
			BigDecimal useFundAmount = BigDecimal.ZERO;// 已使用资金
			BigDecimal usedAssetAmount = BigDecimal.ZERO;// 已使用资产
			BigDecimal blanceAssetAmount = BigDecimal.ZERO;// 资产余额
			BigDecimal sumFundAmount = BigDecimal.ZERO; // 资金总额
			BigDecimal advancePayAmount = BigDecimal.ZERO; // 预付总额
			BigDecimal paymentAmount = BigDecimal.ZERO; // 在途货款总额
			BigDecimal recAmount = BigDecimal.ZERO; // 应收总额
			BigDecimal stlAmount = BigDecimal.ZERO; // 库存总额

			FundPoolReqDto poolReqDto = new FundPoolReqDto();
			poolReqDto.setUserId(ServiceSupport.getUser().getId());
			poolReqDto.setBusinessUnitId(Integer.parseInt(businId));
			ReceiptPool receiptPool = receiptPoolDao.sumReceiptPool(poolReqDto);
			if (receiptPool != null) {
				if (receiptPool.getCountFundAmount() != null) {
					sumFundAmount = receiptPool.getCountFundAmount();
				}
				if (receiptPool.getRemainFundAmount() != null) {
					blanceFundAmount = receiptPool.getRemainFundAmount();
				}
				if (receiptPool.getRemainAssetAmount() != null) {
					usedAssetAmount = receiptPool.getRemainAssetAmount();
				}
				if (receiptPool.getAdvancePayAmount() != null) {
					advancePayAmount = receiptPool.getAdvancePayAmount();
				}
				if (receiptPool.getPaymentAmount() != null) {
					paymentAmount = receiptPool.getPaymentAmount();
				}
				if (receiptPool.getRecAmount() != null) {
					recAmount = receiptPool.getRecAmount();
				}
				if (receiptPool.getStlAmount() != null) {
					stlAmount = receiptPool.getStlAmount();
				}
				resDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						receiptPool.getCurrencyType() + ""));
			}
			if (DecimalUtil.gt(sumFundAmount, BigDecimal.ZERO)) {
				blanceFundAmountRate = DecimalUtil.toPercent(DecimalUtil.divide(blanceFundAmount, sumFundAmount));
				if (DecimalUtil.gt(blanceFundAmountRate, new BigDecimal(BaseConsts.INT_100))) {
					blanceFundAmountRate = new BigDecimal(BaseConsts.INT_100);
				} else if (DecimalUtil.lt(blanceFundAmount, BigDecimal.ZERO)) {
					blanceFundAmountRate = BigDecimal.ZERO;
				}
				usedFundAmountRate = DecimalUtil.subtract(new BigDecimal(BaseConsts.INT_100), blanceFundAmountRate);
			}
			if (DecimalUtil.gt(sumFundAmount, BigDecimal.ZERO)) {
				usedAssetAmountRate = DecimalUtil.toPercent(DecimalUtil.divide(usedAssetAmount, sumFundAmount));
				if (DecimalUtil.gt(usedAssetAmountRate, new BigDecimal(BaseConsts.INT_100))) {
					usedAssetAmountRate = new BigDecimal(BaseConsts.INT_100);
				} else if (DecimalUtil.lt(usedAssetAmountRate, BigDecimal.ZERO)) {
					usedAssetAmountRate = BigDecimal.ZERO;
				}
				blanceAssetAmountRate = DecimalUtil.subtract(new BigDecimal(BaseConsts.INT_100), usedAssetAmountRate);
			}
			if (DecimalUtil.gt(usedAssetAmount, BigDecimal.ZERO)) {
				advancePayAmountRate = DecimalUtil.toPercent(DecimalUtil.divide(advancePayAmount, usedAssetAmount));
				if (DecimalUtil.gt(advancePayAmountRate, new BigDecimal(BaseConsts.INT_100))) {
					advancePayAmountRate = new BigDecimal(BaseConsts.INT_100);
				} else if (DecimalUtil.lt(advancePayAmountRate, BigDecimal.ZERO)) {
					advancePayAmountRate = BigDecimal.ZERO;
				}
				paymentAmountRate = DecimalUtil.toPercent(DecimalUtil.divide(paymentAmount, usedAssetAmount));
				if (DecimalUtil.gt(paymentAmountRate, new BigDecimal(BaseConsts.INT_100))) {
					paymentAmountRate = new BigDecimal(BaseConsts.INT_100);
				} else if (DecimalUtil.lt(paymentAmountRate, BigDecimal.ZERO)) {
					paymentAmountRate = BigDecimal.ZERO;
				}
				recAmountRate = DecimalUtil.toPercent(DecimalUtil.divide(recAmount, usedAssetAmount));
				if (DecimalUtil.gt(recAmountRate, new BigDecimal(BaseConsts.INT_100))) {
					recAmountRate = new BigDecimal(BaseConsts.INT_100);
				} else if (DecimalUtil.lt(recAmountRate, BigDecimal.ZERO)) {
					recAmountRate = BigDecimal.ZERO;
				}
				stlAmountRate = DecimalUtil.toPercent(DecimalUtil.divide(stlAmount, usedAssetAmount));
				if (DecimalUtil.gt(stlAmountRate, new BigDecimal(BaseConsts.INT_100))) {
					stlAmountRate = new BigDecimal(BaseConsts.INT_100);
				} else if (DecimalUtil.lt(stlAmountRate, BigDecimal.ZERO)) {
					stlAmountRate = BigDecimal.ZERO;
				}
			}
			useFundAmount = DecimalUtil.subtract(sumFundAmount, blanceFundAmount);
			blanceAssetAmount = DecimalUtil.subtract(sumFundAmount, usedAssetAmount);
			resDto.setSumFundAmount(sumFundAmount);
			resDto.setBlanceFundAmount(blanceFundAmount);
			resDto.setUseFundAmount(useFundAmount);
			resDto.setUsedAssetAmount(usedAssetAmount);
			resDto.setBlanceAssetAmount(blanceAssetAmount);
			resDto.setAdvancePayAmount(advancePayAmount);
			resDto.setPaymentAmount(paymentAmount);
			resDto.setRecAmount(recAmount);
			resDto.setStlAmount(stlAmount);

			resDto.setUsedFundAmountRate(usedFundAmountRate);
			resDto.setBlanceFundAmountRate(blanceFundAmountRate);
			resDto.setUsedFundAmountRate(usedFundAmountRate);
			resDto.setBlanceAssetAmountRate(blanceAssetAmountRate);
			resDto.setAdvancePayAmountRate(advancePayAmountRate);
			resDto.setPaymentAmountRate(paymentAmountRate);
			resDto.setRecAmountRate(recAmountRate);
			resDto.setStlAmountRate(stlAmountRate);
			result.setItems(resDto);
		}
		return result;
	}

	/**
	 * 获取所有比例
	 * 
	 * @return
	 */
	public Result<EntryInfoResDto> queryFundPoolList() {
		Result<EntryInfoResDto> result = new Result<EntryInfoResDto>();
		EntryInfoResDto resDto = new EntryInfoResDto();
		FundPoolReqDto poolReqDto = new FundPoolReqDto();
		poolReqDto.setUserId(ServiceSupport.getUser().getId());
		BigDecimal sumFundAmount = receiptPoolDao.sumCountFundAmount(poolReqDto);
		if (sumFundAmount != null) {
			if (DecimalUtil.gt(sumFundAmount, BigDecimal.ZERO)) {
				List<ReceiptPool> amountList = receiptPoolDao.queryReceiptPoolCount(poolReqDto);
				if (CollectionUtils.isNotEmpty(amountList)) {
					List<FundPoolResDto> rateList = new ArrayList<FundPoolResDto>();
					for (ReceiptPool pool : amountList) {
						FundPoolResDto poolResDto = new FundPoolResDto();
						BigDecimal rate = BigDecimal.ZERO;
						if (DecimalUtil.gt(pool.getCountFundAmount(), BigDecimal.ZERO)) {
							rate = DecimalUtil.toPercent(DecimalUtil.divide(pool.getCountFundAmount(), sumFundAmount));
						}
						poolResDto.setCountRecriptAmount(rate);
						poolResDto.setBusinessUnitName(cacheService.getSubjectNcByIdAndKey(pool.getBusinessUnitId(),
								CacheKeyConsts.BUSI_UNIT));// 经营单位名称
						rateList.add(poolResDto);
					}
					resDto.setBusList(rateList);
				}
			}
		}
		result.setItems(resDto);
		return result;
	}
}
