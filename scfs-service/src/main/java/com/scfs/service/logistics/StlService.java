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

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseUserSubjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.dto.req.StlSummarySearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlResDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.logistics.entity.StlSum;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月17日.
 */
@Service
public class StlService {
	@Autowired
	private StlDao stlDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private BillDeliveryDao billDeliveryDao;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private BaseUserSubjectDao baseUserSubjectDao;

	/**
	 * 根据条件查询库存列表
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public PageResult<StlResDto> queryStlResultsByCon(StlSearchReqDto stlSearchReqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		int offSet = PageUtil.getOffSet(stlSearchReqDto.getPage(), stlSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, stlSearchReqDto.getPer_page());

		stlSearchReqDto.setStoreFlag(BaseConsts.ONE);
		stlSearchReqDto.setUserId(ServiceSupport.getUser().getId());

		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		List<Stl> stlList = null;
		if (!CollectionUtils.isEmpty(userSubject)) {
			stlSearchReqDto.setUserSubject(userSubject);
			stlList = stlDao.queryStlResultsByCon(stlSearchReqDto, rowBounds);
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL);
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);
		result.setItems(stlResDtoList);
		String totalStr = querySumStl(stlSearchReqDto, isAllowPerm);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), stlSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(stlSearchReqDto.getPage());
		result.setPer_page(stlSearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 根据条件查询库存列表(不分页)
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public List<StlResDto> queryAllStlResultsByCon(StlSearchReqDto stlSearchReqDto) {
		stlSearchReqDto.setStoreFlag(BaseConsts.ONE);
		if (null == stlSearchReqDto.getUserId()) {
			stlSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		List<Stl> stlList = null;
		if (!CollectionUtils.isEmpty(userSubject)) {
			stlSearchReqDto.setUserSubject(userSubject);
			stlList = stlDao.queryStlResultsByCon(stlSearchReqDto);
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL, stlSearchReqDto.getUserId());
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);

		return stlResDtoList;
	}

	/**
	 * 根据条件查询库存汇总列表
	 * 
	 * @param stlHistorySummarySearchReqDto
	 * @return
	 */
	public PageResult<StlResDto> queryStlSummaryResultsByCon(StlSummarySearchReqDto stlSummarySearchReqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		int offSet = PageUtil.getOffSet(stlSummarySearchReqDto.getPage(), stlSummarySearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, stlSummarySearchReqDto.getPer_page());

		List<String> groupByCondition = stlSummarySearchReqDto.getGroupByCondition();
		stlSummarySearchReqDto.setGroupByCondition(groupByCondition);
		stlSummarySearchReqDto.setStoreFlag(BaseConsts.ONE);
		stlSummarySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<Stl> stlList = stlDao.queryStlSummaryResultsByCon(stlSummarySearchReqDto, rowBounds);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL);
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);
		result.setItems(stlResDtoList);
		String totalStr = querySumStlSummary(stlSummarySearchReqDto, isAllowPerm);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), stlSummarySearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(stlSummarySearchReqDto.getPage());
		result.setPer_page(stlSummarySearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 根据条件查询库存汇总列表(不分页)
	 * 
	 * @param stlHistorySummarySearchReqDto
	 * @return
	 */
	public List<StlResDto> queryAllStlSummaryResultsByCon(StlSummarySearchReqDto stlSummarySearchReqDto) {
		List<String> groupByCondition = stlSummarySearchReqDto.getGroupByCondition();
		stlSummarySearchReqDto.setGroupByCondition(groupByCondition);
		stlSummarySearchReqDto.setStoreFlag(BaseConsts.ONE);
		if (null == stlSummarySearchReqDto.getUserId()) {
			stlSummarySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<Stl> stlList = stlDao.queryStlSummaryResultsByCon(stlSummarySearchReqDto);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL,
				stlSummarySearchReqDto.getUserId());
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);

		return stlResDtoList;
	}

	/**
	 * 获取在仓库存分页数据
	 * 
	 * @param stlSearchReqDto
	 * @param rowBounds
	 * @return
	 */
	public List<StlResDto> queryInSenateStl(StlSearchReqDto stlSearchReqDto, RowBounds rowBounds) {
		List<Stl> stlList = stlDao.queryResultsGroupByGoodsNumber(stlSearchReqDto, rowBounds);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL, stlSearchReqDto.getUserId());
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);
		return stlResDtoList;
	}

	/**
	 * 获取在仓库所有数据
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public List<StlResDto> queryAllInSenateStl(StlSearchReqDto stlSearchReqDto) {
		List<Stl> stlList = stlDao.queryResultsGroupByGoodsNumber(stlSearchReqDto);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL, stlSearchReqDto.getUserId());
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);
		return stlResDtoList;
	}

	/**
	 * 获取临期相关信息
	 * 
	 * @param day
	 * @return
	 */
	public List<StlResDto> queryAllStlByAdvent(Integer day) {
		List<Stl> stlList = stlDao.queryResultsByAdvent(day);
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, false, null);
		return stlResDtoList;
	}

	public List<StlResDto> queryAvgStlAge(StlSearchReqDto stlSearchReqDto, RowBounds rowBounds) {
		List<Stl> stlList = stlDao.queryResultsGroupByAvgAge(stlSearchReqDto, rowBounds);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL, stlSearchReqDto.getUserId());
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);
		return stlResDtoList;
	}

	/**
	 * 获取超期相关信息
	 * 
	 * @param day
	 * @return
	 */
	public List<StlResDto> queryAllStlByOverAge(StlSearchReqDto stlSearchReqDto) {
		List<Stl> stlList = stlDao.queryResultsGroupByOver(stlSearchReqDto);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL, stlSearchReqDto.getUserId());
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);
		return stlResDtoList;
	}

	public List<StlResDto> queryStlByOverAge(StlSearchReqDto stlSearchReqDto, RowBounds rowBounds) {
		List<Stl> stlList = stlDao.queryResultsGroupByOver(stlSearchReqDto, rowBounds);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL, stlSearchReqDto.getUserId());
		List<StlResDto> stlResDtoList = convertToResDto(stlList, null, isAllowPerm, null);
		return stlResDtoList;
	}

	/**
	 * 超期动销滞销风险总库存金额
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public BigDecimal querySumAmount(StlSearchReqDto stlSearchReqDto) {
		BigDecimal stlAmount = stlDao.querySumAmount(stlSearchReqDto);
		if (stlAmount == null) {
			stlAmount = BigDecimal.ZERO;
		}
		return stlAmount;
	}

	/**
	 * 根据条件查询可用库存列表
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public PageResult<StlResDto> queryAvailableStlResultsByCon(StlSearchReqDto stlSearchReqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		if (null != stlSearchReqDto.getQuerySource()) {
			BillDelivery billDeliveryRes = null;
			Integer querySource = stlSearchReqDto.getQuerySource();
			if (querySource.equals(BaseConsts.ONE) && null != stlSearchReqDto.getBillDeliveryId()) { // 1-销售选择库存
				BillDelivery billDelivery = new BillDelivery();
				billDelivery.setId(stlSearchReqDto.getBillDeliveryId());
				billDeliveryRes = billDeliveryDao.queryEntityById(billDelivery);

				if (null != billDeliveryRes) {
					stlSearchReqDto.setProjectId(billDeliveryRes.getProjectId());
					stlSearchReqDto.setCustomerId(billDeliveryRes.getCustomerId());
					stlSearchReqDto.setWarehouseId(billDeliveryRes.getWarehouseId());
					stlSearchReqDto.setCurrencyType(billDeliveryRes.getCurrencyType());
					if (null != stlSearchReqDto.getIsSameCustomer()
							&& stlSearchReqDto.getIsSameCustomer().equals(BaseConsts.ONE)) { // 是否同客户
					} else {
						stlSearchReqDto.setIsCustomerNullFlag(BaseConsts.ONE);
					}
					stlSearchReqDto.setIsExistPay(BaseConsts.ONE);
				} else {
					return result;
				}
			}
			if (querySource.equals(BaseConsts.TWO) && null != stlSearchReqDto.getBillOutStoreId()) { // 2-出库选择库存
				BillOutStore billOutStore = billOutStoreDao.queryEntityById(stlSearchReqDto.getBillOutStoreId());

				if (null != billOutStore) {
					stlSearchReqDto.setProjectId(billOutStore.getProjectId());
					stlSearchReqDto.setWarehouseId(billOutStore.getWarehouseId());
					stlSearchReqDto.setCurrencyType(billOutStore.getCurrencyType());
					if (!billOutStore.getBillType().equals(BaseConsts.TWO)) { // 非出库类型
																				// 2-调拨(调拨客户为空)
						stlSearchReqDto.setCustomerId(billOutStore.getCustomerId());
						stlSearchReqDto.setIsCustomerNullFlag(BaseConsts.ONE);
					}
				} else {
					return result;
				}
			}
			if (querySource.equals(BaseConsts.THREE) && null != stlSearchReqDto.getBillOutStoreId()) { // 3-出库单拣货
				BillOutStore billOutStore = billOutStoreDao.queryEntityById(stlSearchReqDto.getBillOutStoreId());
				BillOutStoreDtl billOutStoreDtl = billOutStoreDtlDao
						.queryEntityById(stlSearchReqDto.getBillOutStoreDtlId());

				if (null != billOutStore && null != billOutStoreDtl) {
					if (billOutStoreDtl.getAssignStlFlag().equals(BaseConsts.ONE)) { // 指定库存
						stlSearchReqDto.setRefId(billOutStoreDtl.getId());
						stlSearchReqDto.setId(billOutStoreDtl.getStlId());
					} else {
						stlSearchReqDto.setProjectId(billOutStore.getProjectId());
						stlSearchReqDto.setWarehouseId(billOutStore.getWarehouseId());
						stlSearchReqDto.setCurrencyType(billOutStore.getCurrencyType());
						stlSearchReqDto.setRefId(billOutStoreDtl.getId());
						stlSearchReqDto.setGoodsId(billOutStoreDtl.getGoodsId());
						stlSearchReqDto.setGoodsStatus(billOutStoreDtl.getGoodsStatus());
						stlSearchReqDto.setBatchNo(billOutStoreDtl.getBatchNo());
						if (!billOutStore.getBillType().equals(BaseConsts.TWO)) { // 非出库类型
																					// 2-调拨(调拨客户为空)
							stlSearchReqDto.setCustomerId(billOutStore.getCustomerId());
							stlSearchReqDto.setIsCustomerNullFlag(BaseConsts.ONE);
						}
					}
				} else {
					return result;
				}
			}

			if (querySource.equals(BaseConsts.FIVE) && null != stlSearchReqDto.getId()) { // 5-采购单退货
				PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(stlSearchReqDto.getId());
				if (null != purchaseOrderTitle) {
					stlSearchReqDto.setId(null);
					stlSearchReqDto.setProjectId(purchaseOrderTitle.getProjectId());
					stlSearchReqDto.setCustomerId(purchaseOrderTitle.getCustomerId());
					stlSearchReqDto.setWarehouseId(purchaseOrderTitle.getWarehouseId());
					stlSearchReqDto.setCurrencyId(purchaseOrderTitle.getCurrencyId());
					stlSearchReqDto.setSupplierId(purchaseOrderTitle.getSupplierId());
					stlSearchReqDto.setUserId(ServiceSupport.getUser().getId());
				} else {
					return result;
				}
			}

			stlSearchReqDto.setAvailableFlag(BaseConsts.ONE); // 查询可用库存
			BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
			baseReq.setUserId(ServiceSupport.getUser().getId());
			List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
			List<Stl> stlList = null;
			if (!CollectionUtils.isEmpty(userSubject)) {
				stlSearchReqDto.setUserSubject(userSubject);
				stlList = stlDao.queryStlResultsByCon(stlSearchReqDto);
			}
			if (null != stlSearchReqDto.getQuerySource() && stlSearchReqDto.getQuerySource().equals(BaseConsts.THREE)
					&& null != stlSearchReqDto.getRefId()) {
				BillOutStoreDtl billOutStoreDtl = billOutStoreDtlDao.queryEntityById(stlSearchReqDto.getRefId());
				if (null != billOutStoreDtl) {
					stlSearchReqDto.setUnPickNum(DecimalUtil.subtract(
							null == billOutStoreDtl.getSendNum() ? BigDecimal.ZERO : billOutStoreDtl.getSendNum(),
							null == billOutStoreDtl.getPickupNum() ? BigDecimal.ZERO : billOutStoreDtl.getPickupNum()));
				}
			}
			boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_STL);
			List<StlResDto> stlResDtoList = convertToResDto(stlList, stlSearchReqDto, isAllowPerm, billDeliveryRes);
			result.setItems(stlResDtoList);
		}
		return result;
	}

	/**
	 * 根据条件和商品ID查询库存合计
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public Result<StlSum> queryAvailableStlByGoodsId(StlSearchReqDto stlSearchReqDto) {
		Result<StlSum> result = new Result<StlSum>();
		if (null != stlSearchReqDto.getQuerySource()) {
			Integer querySource = stlSearchReqDto.getQuerySource();
			if (querySource.equals(BaseConsts.FOUR) && null != stlSearchReqDto.getBillOutStoreId()) { // 4-出库单添加商品
				BillOutStore billOutStore = billOutStoreDao.queryEntityById(stlSearchReqDto.getBillOutStoreId());

				if (null != billOutStore) {
					stlSearchReqDto.setProjectId(billOutStore.getProjectId());
					stlSearchReqDto.setWarehouseId(billOutStore.getWarehouseId());
					stlSearchReqDto.setCurrencyType(billOutStore.getCurrencyType());
					if (!billOutStore.getBillType().equals(BaseConsts.TWO)) { // 非出库类型
																				// 2-调拨(调拨客户为空)
						stlSearchReqDto.setCustomerId(billOutStore.getCustomerId());
						stlSearchReqDto.setIsCustomerNullFlag(BaseConsts.ONE);
					}
				} else {
					return result;
				}
			}
			if (querySource.equals(BaseConsts.FIVE) && null != stlSearchReqDto.getBillDeliveryId()) { // 5-销售单添加商品
				BillDelivery billDelivery = new BillDelivery();
				billDelivery.setId(stlSearchReqDto.getBillDeliveryId());
				BillDelivery billDeliveryRes = billDeliveryDao.queryEntityById(billDelivery);

				if (null != billDeliveryRes) {
					stlSearchReqDto.setProjectId(billDeliveryRes.getProjectId());
					stlSearchReqDto.setCustomerId(billDeliveryRes.getCustomerId());
					stlSearchReqDto.setWarehouseId(billDeliveryRes.getWarehouseId());
					stlSearchReqDto.setCurrencyType(billDeliveryRes.getCurrencyType());
					if (null != stlSearchReqDto.getIsSameCustomer()
							&& stlSearchReqDto.getIsSameCustomer().equals(BaseConsts.ONE)) { // 是否同客户
					} else {
						stlSearchReqDto.setIsCustomerNullFlag(BaseConsts.ONE);
					}
				} else {
					return result;
				}
			}
			if (querySource.equals(BaseConsts.SIX) && null != stlSearchReqDto.getBillDeliveryId()) { // 6-销售单明细修改
				BillDelivery billDelivery = new BillDelivery();
				billDelivery.setId(stlSearchReqDto.getBillDeliveryId());
				BillDelivery billDeliveryRes = billDeliveryDao.queryEntityById(billDelivery);

				if (null != billDeliveryRes) {
					stlSearchReqDto.setProjectId(billDeliveryRes.getProjectId());
					stlSearchReqDto.setCustomerId(billDeliveryRes.getCustomerId());
					stlSearchReqDto.setWarehouseId(billDeliveryRes.getWarehouseId());
					stlSearchReqDto.setCurrencyType(billDeliveryRes.getCurrencyType());
				} else {
					return result;
				}
			}
			stlSearchReqDto.setAvailableFlag(BaseConsts.ONE); // 查询可用库存
			StlSum stlSum = stlDao.querySumResultsByCon(stlSearchReqDto);
			result.setItems(stlSum);
		}
		return result;
	}

	/**
	 * 查询合计
	 * 
	 * @param stlSearchReqDto
	 * @param isAllowPerm
	 * @return
	 */
	private String querySumStl(StlSearchReqDto stlSearchReqDto, boolean isAllowPerm) {
		String totalStr = "";
		if (stlSearchReqDto.getNeedSum() != null && stlSearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<StlSum> stlSumList = stlDao.querySumStl(stlSearchReqDto);
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
	private String querySumStlSummary(StlSummarySearchReqDto stlSummarySearchReqDto, boolean isAllowPerm) {
		String totalStr = "";
		if (stlSummarySearchReqDto.getNeedSum() != null && stlSummarySearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<StlSum> stlSumList = stlDao.querySumStlSummary(stlSummarySearchReqDto);
			totalStr = getTotalStr(stlSumList, isAllowPerm, true);
		}
		return totalStr;
	}

	private String getTotalStr(List<StlSum> stlSumList, boolean isAllowPerm, boolean isSummary) {
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

	/**
	 * 编辑浏览
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public Result<StlResDto> editStlById(StlSearchReqDto stlSearchReqDto) {
		Result<StlResDto> result = new Result<StlResDto>();
		Stl stl = stlDao.queryEntityById(stlSearchReqDto.getId());
		StlResDto stlResDto = convertToResDto(stl, stlSearchReqDto, false, null);
		result.setItems(stlResDto);
		return result;
	}

	/**
	 * 拆分浏览
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public Result<StlResDto> splitStlById(StlSearchReqDto stlSearchReqDto) {
		Result<StlResDto> result = new Result<StlResDto>();
		Stl stl = stlDao.queryEntityById(stlSearchReqDto.getId());
		StlResDto stlResDto = convertToResDto(stl, stlSearchReqDto, false, null);
		result.setItems(stlResDto);
		return result;
	}

	/**
	 * 修改库存状态和批次
	 * 
	 * @param stl
	 */
	public void updateGoodsStatusAndBatchNo(Stl stl) {
		Stl stl2 = new Stl();
		stl2.setId(stl.getId());
		stl2.setGoodsStatus(stl.getGoodsStatus());
		stl2.setBatchNo(null == stl.getBatchNo() ? "" : stl.getBatchNo());
		stlDao.updateById(stl);
	}

	/**
	 * 拆分库存
	 * 
	 * @param stlSearchReqDto
	 */
	public void splitUpdateStlById(StlSearchReqDto stlSearchReqDto) {
		Integer id = stlSearchReqDto.getId();
		List<Stl> stlList = stlSearchReqDto.getStlList();
		if (!CollectionUtils.isEmpty(stlList)) {
			Stl stl = stlDao.queryAndLockEntityById(id);
			BigDecimal availableNum = (null == stl.getAvailableNum() ? BigDecimal.ZERO : stl.getAvailableNum());
			BigDecimal remainNum = availableNum;
			BigDecimal totalStoreNum = BigDecimal.ZERO;
			for (Stl s : stlList) {
				totalStoreNum = DecimalUtil.add(totalStoreNum, s.getStoreNum());
			}
			if (availableNum.compareTo(totalStoreNum) < 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "可用库存数量小于可拆分库存数量，请重新操作");
			}
			if (availableNum.compareTo(totalStoreNum) == 0 && stlList.size() == 1) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "当前库存不满足拆分库存要求，请重新操作");
			}

			if (availableNum.compareTo(totalStoreNum) == 0) {
				stlList.remove(0); // 当可用库存数量等于可拆分库存数量，且拥有多条拆分库存时，移除第一条拆分库存
			}
			BigDecimal totalSplitStoreNum = BigDecimal.ZERO;
			for (Stl s : stlList) {
				BigDecimal splitStoreNum = BigDecimal.ZERO;
				BigDecimal storeNum = s.getStoreNum();
				if (DecimalUtil.ge(storeNum, remainNum)) {
					splitStoreNum = remainNum;
				} else {
					splitStoreNum = storeNum;
				}
				// 插入新库存行
				Stl newStl = new Stl();
				BeanUtils.copyProperties(stl, newStl);
				newStl.setId(null);
				newStl.setStoreNum(splitStoreNum);
				newStl.setLockNum(BigDecimal.ZERO);
				newStl.setSaleLockNum(BigDecimal.ZERO);
				newStl.setTallyNum(splitStoreNum);
				newStl.setInStoreNum(splitStoreNum);
				stlDao.insert(newStl);

				totalSplitStoreNum = DecimalUtil.add(totalSplitStoreNum, splitStoreNum);
				if (DecimalUtil.eq(remainNum, BigDecimal.ZERO)) {
					break;
				}
			}
			// 更新原有库存行
			stl.setStoreNum(DecimalUtil.subtract(stl.getStoreNum(), totalSplitStoreNum));
			stl.setTallyNum(DecimalUtil.subtract(stl.getTallyNum(), totalSplitStoreNum));
			stl.setInStoreNum(DecimalUtil.subtract(stl.getInStoreNum(), totalSplitStoreNum));
			stlDao.updateById(stl);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "拆分库存明细不能为空");
		}
	}

	private List<StlResDto> convertToResDto(List<Stl> stlList, StlSearchReqDto stlSearchReqDto, boolean isAllowPerm,
			BillDelivery billDeliveryRes) {
		List<StlResDto> stlResDtoList = new ArrayList<StlResDto>(5);
		if (CollectionUtils.isEmpty(stlList)) {
			return stlResDtoList;
		}
		for (Stl stl : stlList) {
			StlResDto stlResDto = convertToResDto(stl, stlSearchReqDto, isAllowPerm, billDeliveryRes);
			stlResDto.setOpertaList(getOperList());

			stlResDtoList.add(stlResDto);
		}
		return stlResDtoList;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList, StlResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.EDIT);
		opertaList.add(OperateConsts.SPLIT);
		return opertaList;
	}

	private StlResDto convertToResDto(Stl stl, StlSearchReqDto stlSearchReqDto, boolean isAllowPerm,
			BillDelivery billDeliveryRes) {
		StlResDto stlResDto = new StlResDto();
		BeanUtils.copyProperties(stl, stlResDto);

		if (null != stl.getGoodsStatus()) {
			stlResDto.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
					Integer.toString(stl.getGoodsStatus())));
		}
		if (stl.getCurrencyType() != null) {
			stlResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					Integer.toString(stl.getCurrencyType())));
		}
		if (null != stl.getGoodsId()) {
			BaseGoods baseGoods = cacheService.getGoodsById(stl.getGoodsId());
			if (null != baseGoods) {
				stlResDto.setGoodsName(baseGoods.getName());
				stlResDto.setGoodsNumber(baseGoods.getNumber());
				stlResDto.setGoodsType(baseGoods.getType());
				stlResDto.setGoodsBarCode(baseGoods.getBarCode());
				stlResDto.setSpecification(baseGoods.getSpecification());
			}
		}
		stlResDto.setProjectName(cacheService.showProjectNameById(stl.getProjectId()));
		stlResDto.setSupplierName(cacheService.showSubjectNameByIdAndKey(stl.getSupplierId(), CacheKeyConsts.SUPPLIER));
		stlResDto.setWarehouseName(
				cacheService.showSubjectNameByIdAndKey(stl.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		stlResDto.setCustomerName(cacheService.showSubjectNameByIdAndKey(stl.getCustomerId(), CacheKeyConsts.CUSTOMER));

		if (null != stlSearchReqDto) {
			BigDecimal unPickNum = null == stlSearchReqDto.getUnPickNum() ? BigDecimal.ZERO
					: stlSearchReqDto.getUnPickNum();
			BigDecimal availableNum = null == stlResDto.getAvailableNum() ? BigDecimal.ZERO
					: stlResDto.getAvailableNum();
			BigDecimal pickupNum = BigDecimal.ZERO;
			if (DecimalUtil.gt(unPickNum, availableNum)) {
				pickupNum = availableNum;
			} else {
				pickupNum = unPickNum;
			}
			stlResDto.setPickupNum(pickupNum);
			if (null != stlSearchReqDto.getIsExistPay() && null != billDeliveryRes) {
				stlResDto.setDefaultAvailableNum(stlResDto.getAvailableNum());
				stlResDto
						.setDefaultPrice(projectItemService.getSalePrice(stl.getId(), billDeliveryRes.getReturnTime()));
			}
		}

		BigDecimal storeAmount = (null == stlResDto.getStoreAmount() ? BigDecimal.ZERO : stlResDto.getStoreAmount());
		BigDecimal lockAmount = (null == stlResDto.getLockAmount() ? BigDecimal.ZERO : stlResDto.getLockAmount());
		BigDecimal saleLockAmount = (null == stlResDto.getSaleLockAmount() ? BigDecimal.ZERO
				: stlResDto.getSaleLockAmount());
		BigDecimal availableAmount = (null == stlResDto.getAvailableAmount() ? BigDecimal.ZERO
				: stlResDto.getAvailableAmount());
		BigDecimal costPrice = (null == stlResDto.getCostPrice() ? BigDecimal.ZERO : stlResDto.getCostPrice());
		BigDecimal poPrice = (null == stlResDto.getPoPrice() ? BigDecimal.ZERO : stlResDto.getPoPrice());
		BigDecimal availableModel = (null == stlResDto.getAvailableModel() ? BigDecimal.ZERO
				: stlResDto.getAvailableModel());
		stlResDto.setStoreAmountStr(DecimalUtil.toAmountString(storeAmount));
		stlResDto.setLockAmountStr(DecimalUtil.toAmountString(lockAmount));
		stlResDto.setSaleLockAmountStr(DecimalUtil.toAmountString(saleLockAmount));
		stlResDto.setAvailableAmountStr(DecimalUtil.toAmountString(availableAmount));
		stlResDto.setCostPriceStr(DecimalUtil.toPriceString(costPrice));
		stlResDto.setPoPriceStr(DecimalUtil.toPriceString(poPrice));
		stlResDto.setAvailableModelStr(DecimalUtil.toPriceString(availableModel));
		if (isAllowPerm) { // 不显示金额权限
			stlResDto.setCurrencyTypeName(BaseConsts.NO_PERMISSION_HIT);
			stlResDto.setStoreAmountStr(BaseConsts.NO_PERMISSION_HIT);
			stlResDto.setLockAmountStr(BaseConsts.NO_PERMISSION_HIT);
			stlResDto.setSaleLockAmountStr(BaseConsts.NO_PERMISSION_HIT);
			stlResDto.setAvailableAmountStr(BaseConsts.NO_PERMISSION_HIT);
			stlResDto.setCostPriceStr(BaseConsts.NO_PERMISSION_HIT);
			stlResDto.setPoPriceStr(BaseConsts.NO_PERMISSION_HIT);
		}
		return stlResDto;
	}

	/**
	 * 根据ID查询库存
	 * 
	 * @param id
	 * @return
	 */
	public Stl queryEntityById(Integer id) {
		return stlDao.queryEntityById(id);
	}

	/**
	 * 根据ID查询库存(加锁)
	 * 
	 * @param id
	 * @return
	 */
	public Stl queryAndLockEntityById(Integer id) {
		return stlDao.queryAndLockEntityById(id);
	}

	/**
	 * 查询库存(先进先出)
	 * 
	 * @param stlSearchReqDto
	 * @return
	 */
	public List<Stl> queryStl4FIFO(StlSearchReqDto stlSearchReqDto) {
		return stlDao.queryStl4FIFO(stlSearchReqDto);
	}

	/**
	 * 锁定库存的出库数量
	 * 
	 * @param stlId
	 *            库存ID
	 * @param lockNum
	 *            锁定数
	 */
	public void lockStl(Integer stlId, BigDecimal lockNum) {
		Stl stl = stlDao.queryAndLockEntityById(stlId);
		lockStl(stl, lockNum);
	}

	/**
	 * 锁定库存的出库数量
	 * 
	 * @param stl
	 *            库存行
	 * @param lockNum
	 *            锁定数
	 */
	public void lockStl(Stl stl, BigDecimal lockNum) {
		if (null != stl) {
			BigDecimal currLockNum = (null == stl.getLockNum() ? BigDecimal.ZERO : stl.getLockNum());
			lockNum = (null == lockNum ? BigDecimal.ZERO : lockNum);
			BigDecimal newLockNum = DecimalUtil.add(currLockNum, lockNum);
			if (DecimalUtil.gt(newLockNum, stl.getStoreNum())) {
				throw new BaseException(ExcMsgEnum.STL_LOCK_ERROR);
			}
			stl.setLockNum(newLockNum);
			int result = stlDao.updateById(stl);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.STL_LOCK_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.STL_LOCK_ERROR);
		}
	}

	/**
	 * 释放锁定库存的出库数量
	 * 
	 * @param stlId
	 *            库存ID
	 * @param lockNum
	 *            释放数
	 */
	public void releaseStl(Integer stlId, BigDecimal lockNum) {
		Stl stl = stlDao.queryAndLockEntityById(stlId);
		releaseStl(stl, lockNum);
	}

	/**
	 * 释放锁定库存的出库数量
	 * 
	 * @param stl
	 *            库存行
	 * @param lockNum
	 *            释放数
	 */
	public void releaseStl(Stl stl, BigDecimal lockNum) {
		if (null != stl) {
			BigDecimal currLockNum = (null == stl.getLockNum() ? BigDecimal.ZERO : stl.getLockNum());
			lockNum = (null == lockNum ? BigDecimal.ZERO : lockNum);
			BigDecimal newLockNum = DecimalUtil.subtract(currLockNum, lockNum);
			if (DecimalUtil.lt(newLockNum, BigDecimal.ZERO)) {
				throw new BaseException(ExcMsgEnum.STL_STOCK_RELEASE_ERROR);
			}
			stl.setLockNum(newLockNum);
			int result = stlDao.updateById(stl);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.STL_STOCK_RELEASE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.STL_STOCK_RELEASE_ERROR);
		}
	}

	/**
	 * 锁定库存的销售数量
	 * 
	 * @param stlId
	 *            库存ID
	 * @param saleLockNum
	 *            锁定数
	 */
	public void lockStlSaleNum(Integer stlId, BigDecimal saleLockNum) {
		Stl stl = stlDao.queryAndLockEntityById(stlId);
		lockStlSaleNum(stl, saleLockNum);
	}

	/**
	 * 锁定库存的销售数量
	 * 
	 * @param stl
	 *            库存行
	 * @param saleLockNum
	 *            锁定数
	 */
	public void lockStlSaleNum(Stl stl, BigDecimal saleLockNum) {
		if (null != stl) {
			BigDecimal currSaleLockNum = (null == stl.getSaleLockNum() ? BigDecimal.ZERO : stl.getSaleLockNum());
			saleLockNum = (null == saleLockNum ? BigDecimal.ZERO : saleLockNum);
			BigDecimal newSaleLockNum = DecimalUtil.add(currSaleLockNum, saleLockNum);
			if (DecimalUtil.gt(newSaleLockNum, stl.getStoreNum())) {
				throw new BaseException(ExcMsgEnum.STL_LOCK_ERROR);
			}
			stl.setSaleLockNum(newSaleLockNum);
			int result = stlDao.updateById(stl);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.STL_SALE_LOCK_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.STL_SALE_LOCK_ERROR);
		}
	}

	/**
	 * 释放库存的销售数量
	 * 
	 * @param stlId
	 *            库存行ID
	 * @param saleLockNum
	 *            释放数
	 */
	public void releaseStlSaleNum(Integer stlId, BigDecimal saleLockNum) {
		Stl stl = stlDao.queryAndLockEntityById(stlId);
		releaseStlSaleNum(stl, saleLockNum);
	}

	/**
	 * 释放库存的销售数量
	 * 
	 * @param stl
	 *            库存行
	 * @param saleLockNum
	 *            释放数
	 */
	public void releaseStlSaleNum(Stl stl, BigDecimal saleLockNum) {
		if (null != stl) {
			BigDecimal currSaleLockNum = (null == stl.getSaleLockNum() ? BigDecimal.ZERO : stl.getSaleLockNum());
			saleLockNum = (null == saleLockNum ? BigDecimal.ZERO : saleLockNum);
			BigDecimal newSaleLockNum = DecimalUtil.subtract(currSaleLockNum, saleLockNum);
			if (DecimalUtil.lt(newSaleLockNum, BigDecimal.ZERO)) {
				throw new BaseException(ExcMsgEnum.STL_SALE_RELEASE_ERROR);
			}
			stl.setSaleLockNum(newSaleLockNum);
			int result = stlDao.updateById(stl);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.STL_SALE_RELEASE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.STL_SALE_RELEASE_ERROR);
		}
	}

	/**
	 * 释放库存的销售数量，锁定库存的出库数量
	 * 
	 * @param stlId
	 *            库存ID
	 * @param saleLockNum
	 *            释放销售数量
	 * @param lockNum
	 *            锁定出库数量
	 */
	public void releaseAndLockStl(Integer stlId, BigDecimal saleLockNum, BigDecimal lockNum) {
		Stl stl = stlDao.queryAndLockEntityById(stlId);
		releaseAndLockStl(stl, saleLockNum, lockNum);
	}

	/**
	 * 释放库存的销售数量，同时锁定库存的出库数量
	 * 
	 * @param stl
	 *            库存行
	 * @param saleLockNum
	 *            释放销售数量
	 * @param lockNum
	 *            锁定出库数量
	 */
	public void releaseAndLockStl(Stl stl, BigDecimal saleLockNum, BigDecimal lockNum) {
		if (null != stl) {
			BigDecimal currSaleLockNum = (null == stl.getSaleLockNum() ? BigDecimal.ZERO : stl.getSaleLockNum());
			saleLockNum = (null == saleLockNum ? BigDecimal.ZERO : saleLockNum);
			BigDecimal newSaleLockNum = DecimalUtil.subtract(currSaleLockNum, saleLockNum);
			if (DecimalUtil.lt(newSaleLockNum, BigDecimal.ZERO)) {
				throw new BaseException(ExcMsgEnum.STL_SALE_RELEASE_ERROR);
			}
			stl.setSaleLockNum(newSaleLockNum);

			BigDecimal currLockNum = (null == stl.getLockNum() ? BigDecimal.ZERO : stl.getLockNum());
			lockNum = (null == lockNum ? BigDecimal.ZERO : lockNum);
			BigDecimal newLockNum = DecimalUtil.add(currLockNum, lockNum);
			if (DecimalUtil.gt(newLockNum, stl.getStoreNum())) {
				throw new BaseException(ExcMsgEnum.STL_LOCK_ERROR);
			}
			stl.setLockNum(newLockNum);

			int result = stlDao.updateById(stl);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.STL_SALE_RELEASE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.STL_SALE_RELEASE_ERROR);
		}
	}

	/**
	 * 释放库存的出库数量，同时扣减库存数量
	 * 
	 * @param stlId
	 *            库存ID
	 * @param lockNum
	 *            锁定数量
	 */
	public void releaseAndSubtractStl(Integer stlId, BigDecimal lockNum, BillOutStore billOutStore,
			Integer operateFlag) {
		Stl stl = stlDao.queryAndLockEntityById(stlId);
		releaseAndSubtractStl(stl, lockNum, billOutStore, operateFlag);
	}

	/**
	 * 释放库存的出库数量，同时扣减库存数量
	 * 
	 * @param stl
	 *            库存行
	 * @param lockNum
	 *            锁定数量
	 * @param operateFlag
	 *            操作 1-删除 2-提交 3-驳回 4-送货
	 */
	public void releaseAndSubtractStl(Stl stl, BigDecimal lockNum, BillOutStore billOutStore, Integer operateFlag) {
		BigDecimal currLockNum = (null == stl.getLockNum() ? BigDecimal.ZERO : stl.getLockNum());
		BigDecimal currStoreNum = (null == stl.getStoreNum() ? BigDecimal.ZERO : stl.getStoreNum());
		lockNum = (null == lockNum ? BigDecimal.ZERO : lockNum);
		BigDecimal newLockNum = DecimalUtil.subtract(currLockNum, lockNum);
		if (DecimalUtil.lt(newLockNum, BigDecimal.ZERO)) {
			throw new BaseException(ExcMsgEnum.STL_STOCK_RELEASE_ERROR);
		}
		stl.setLockNum(newLockNum);

		// 驳回出库单且单据为销售出库单或者采购退货单，不仅释放锁定库存的出库数量，同时锁定库存的销售数据
		if (operateFlag == BaseConsts.THREE && (billOutStore.getBillType().equals(BaseConsts.ONE)
				|| billOutStore.getBillType().equals(BaseConsts.FIVE)
				|| billOutStore.getBillType().equals(BaseConsts.SIX))) {
			BigDecimal currSaleLockNum = (null == stl.getSaleLockNum() ? BigDecimal.ZERO : stl.getSaleLockNum());
			BigDecimal newSaleLockNum = DecimalUtil.add(currSaleLockNum, lockNum);
			stl.setSaleLockNum(newSaleLockNum);
		}
		// 送货出库单，不仅释放锁定的出库数量，同时减少库存总数量
		if (operateFlag == BaseConsts.FOUR) {
			BigDecimal newStoreNum = DecimalUtil.subtract(currStoreNum, lockNum);
			if (DecimalUtil.lt(newStoreNum, BigDecimal.ZERO)) {
				throw new BaseException(ExcMsgEnum.STL_STOCK_SUBTRACT_ERROR);
			}
			stl.setStoreNum(newStoreNum);
		}
		stlDao.updateById(stl);
	}

	public boolean isOverStlMaxLine(StlSearchReqDto stlSearchReqDto) {
		stlSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = stlDao.queryStlCountByCon(stlSearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("库存导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncStlExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/stl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_16);
			asyncExcelService.addAsyncExcel(stlSearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncStlExport(StlSearchReqDto stlSearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<StlResDto> stlResDtoList = queryAllStlResultsByCon(stlSearchReqDto);
		model.put("stlList", stlResDtoList);
		return model;
	}

	public boolean isOverStlSummaryMaxLine(StlSummarySearchReqDto stlSummarySearchReqDto) {
		stlSummarySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = stlDao.queryStlSummaryCountByCon(stlSummarySearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("库存汇总导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncStlSummaryExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/stl_summary_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_17);
			asyncExcelService.addAsyncExcel(stlSummarySearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncStlSummaryExport(StlSummarySearchReqDto stlSummarySearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<StlResDto> stlResDtoList = queryAllStlSummaryResultsByCon(stlSummarySearchReqDto);
		model.put("stlSummaryList", stlResDtoList);
		return model;
	}
}
