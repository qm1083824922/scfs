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
import org.testng.collections.Lists;

import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.dao.logistics.BillInStoreTallyDtlDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.dto.req.PoOrderDtlReqDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreDtlExtResDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreDtlResDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreTallyDtlExportResDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreTallyDtlResDto;
import com.scfs.domain.logistics.dto.resp.PoOrderDtlResDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreDtlExt;
import com.scfs.domain.logistics.entity.BillInStoreDtlSum;
import com.scfs.domain.logistics.entity.BillInStoreDtlTaxGroupSum;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月17日.
 */
@Service
public class BillInStoreDtlService {

	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private BillInStoreTallyDtlDao billInStoreTallyDtlDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BillInStoreTallyDtlService billInStoreTallyDtlService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	/**
	 * 新增入库单明细
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	public void addBillInStoreDtls(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		BillInStore billInStore = billInStoreDao.queryAndLockEntityById(billInStoreSearchReqDto.getId());
		if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
			Integer currencyType = billInStore.getCurrencyType();
			Integer oldCurrencyType = currencyType;

			List<PoOrderDtlReqDto> poOrderDtlReqDtoList = billInStoreSearchReqDto.getPoOrderDtlReqDtoList();
			if (!CollectionUtils.isEmpty(poOrderDtlReqDtoList)) {
				for (PoOrderDtlReqDto poOrderDtlReqDto : poOrderDtlReqDtoList) {
					PurchaseOrderLine purchaseOrderLine = purchaseOrderLineDao
							.queryPurchaseOrderLineById(poOrderDtlReqDto.getPoDtlId());
					PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao
							.queryEntityById(purchaseOrderLine.getPoId());

					BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
					billInStoreDtl.setBillInStoreId(billInStoreSearchReqDto.getId());
					billInStoreDtl.setPoId(purchaseOrderLine.getPoId());
					billInStoreDtl.setPoDtlId(purchaseOrderLine.getId());
					billInStoreDtl.setGoodsId(purchaseOrderLine.getGoodsId());
					/**
					 * billInStoreDtl.setPoPrice(null ==
					 * purchaseOrderLine.getGoodsPrice() ? BigDecimal.ZERO :
					 * purchaseOrderLine.getGoodsPrice());
					 * billInStoreDtl.setCostPrice(null ==
					 * purchaseOrderLine.getCostPrice() ? BigDecimal.ZERO :
					 * purchaseOrderLine.getCostPrice());
					 **/
					billInStoreDtl.setPoPrice(null == purchaseOrderLine.getDiscountPrice() ? BigDecimal.ZERO
							: purchaseOrderLine.getDiscountPrice());
					billInStoreDtl.setCostPrice(null == purchaseOrderLine.getDiscountPrice() ? BigDecimal.ZERO
							: purchaseOrderLine.getDiscountPrice());
					billInStoreDtl.setReceivePrice(null == purchaseOrderLine.getDiscountPrice() ? BigDecimal.ZERO
							: purchaseOrderLine.getDiscountPrice());
					billInStoreDtl.setBatchNo(purchaseOrderLine.getBatchNum());
					billInStoreDtl.setReceiveNum(null == poOrderDtlReqDto.getReceiveNum() ? BigDecimal.ZERO
							: poOrderDtlReqDto.getReceiveNum());
					billInStoreDtl.setTallyNum(BigDecimal.ZERO);
					billInStoreDtl.setCreator(ServiceSupport.getUser().getChineseName());
					billInStoreDtl.setCreatorId(ServiceSupport.getUser().getId());
					billInStoreDtl.setPayPrice(purchaseOrderLine.getPayPrice());
					billInStoreDtl.setPayTime(purchaseOrderLine.getPayTime());
					billInStoreDtl.setPayRate(
							purchaseOrderLine.getPayRate() == null ? BigDecimal.ZERO : purchaseOrderLine.getPayRate());
					billInStoreDtl.setPayRealCurrency(purchaseOrderLine.getPayRealCurrency());

					Integer currCurrencyId = poOrderDtlReqDto.getCurrencyId();
					if (null == currencyType && null != currCurrencyId) {
						currencyType = currCurrencyId;
					} else if (null != currencyType && null != currCurrencyId) {
						if (!currencyType.equals(currCurrencyId)) {
							if (null == oldCurrencyType) {
								throw new BaseException(ExcMsgEnum.BILL_IN_STORE_CURRENCY_DIFF_ERROR);
							} else {
								throw new BaseException(ExcMsgEnum.BILL_IN_STORE_CURRENCY_TYPE_DIFF_ERROR);
							}
						}
					} else {
						throw new BaseException(ExcMsgEnum.BILL_IN_STORE_CURRENCY_TYPE_NOT_EXIST);
					}

					billInStoreDtl.setCurrencyType(currencyType);
					billInStoreDtl.setReceiveDate(billInStore.getReceiveDate());
					billInStoreDtl.setCustomerId(purchaseOrderTitle.getCustomerId());
					billInStoreDtl.setSupplierId(purchaseOrderTitle.getSupplierId());
					// 新增入库单明细
					billInStoreDtlDao.insert(billInStoreDtl);
					// 更新订单入库数量
					updatePoWarehouseNum(billInStoreDtl, billInStoreDtl, purchaseOrderLine, BaseConsts.ONE);
				}
				// 更新入库单头的收货数量、收货金额、理货数量、理货金额
				billInStore.setCurrencyType(currencyType);
				updateBillInStoreInfo(billInStore);
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 根据采购订单明细新增入库单
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	public void addBillInStoreDtlsByPoLine(BillInStore billInStore, PurchaseOrderTitle purchaseOrderTitle,
			List<PoOrderDtlResDto> poOrderDtlResDtoList) {
		for (PoOrderDtlResDto poOrderDtlResDto : poOrderDtlResDtoList) {
			PurchaseOrderLine purchaseOrderLine = purchaseOrderLineDao
					.queryPurchaseOrderLineById(poOrderDtlResDto.getPoDtlId());
			BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
			billInStoreDtl.setBillInStoreId(billInStore.getId());
			billInStoreDtl.setPoId(purchaseOrderLine.getPoId());
			billInStoreDtl.setPoDtlId(purchaseOrderLine.getId());
			billInStoreDtl.setGoodsId(purchaseOrderLine.getGoodsId());
			/**
			 * billInStoreDtl.setPoPrice(null ==
			 * purchaseOrderLine.getGoodsPrice() ? BigDecimal.ZERO :
			 * purchaseOrderLine.getGoodsPrice());
			 * billInStoreDtl.setCostPrice(null ==
			 * purchaseOrderLine.getCostPrice() ? BigDecimal.ZERO :
			 * purchaseOrderLine.getCostPrice());
			 **/
			billInStoreDtl.setPoPrice(null == purchaseOrderLine.getDiscountPrice() ? BigDecimal.ZERO
					: purchaseOrderLine.getDiscountPrice());
			billInStoreDtl.setCostPrice(null == purchaseOrderLine.getDiscountPrice() ? BigDecimal.ZERO
					: purchaseOrderLine.getDiscountPrice());
			billInStoreDtl.setReceivePrice(null == purchaseOrderLine.getDiscountPrice() ? BigDecimal.ZERO
					: purchaseOrderLine.getDiscountPrice());
			billInStoreDtl.setBatchNo(purchaseOrderLine.getBatchNum());
			billInStoreDtl.setReceiveNum(
					null == poOrderDtlResDto.getUnStorageNum() ? BigDecimal.ZERO : poOrderDtlResDto.getUnStorageNum());
			billInStoreDtl.setTallyNum(BigDecimal.ZERO);
			billInStoreDtl.setCreator(ServiceSupport.getUser().getChineseName());
			billInStoreDtl.setCreatorId(ServiceSupport.getUser().getId());
			billInStoreDtl.setPayPrice(purchaseOrderLine.getPayPrice());
			billInStoreDtl.setPayTime(purchaseOrderLine.getPayTime());
			billInStoreDtl.setPayRate(
					purchaseOrderLine.getPayRate() == null ? BigDecimal.ZERO : purchaseOrderLine.getPayRate());
			billInStoreDtl.setPayRealCurrency(purchaseOrderLine.getPayRealCurrency());
			billInStoreDtl.setCurrencyType(billInStore.getCurrencyType());
			billInStoreDtl.setReceiveDate(billInStore.getReceiveDate());
			billInStoreDtl.setCustomerId(purchaseOrderTitle.getCustomerId());
			billInStoreDtl.setSupplierId(purchaseOrderTitle.getSupplierId());
			// 新增入库单明细
			billInStoreDtlDao.insert(billInStoreDtl);
			// 更新订单入库数量
			updatePoWarehouseNum(billInStoreDtl, billInStoreDtl, purchaseOrderLine, BaseConsts.ONE);
		}
		// 更新入库单头的收货数量、收货金额、理货数量、理货金额
		updateBillInStoreInfo(billInStore);
	}

	/**
	 * 更新入库单明细
	 * 
	 * @param billInStore
	 * @return
	 */
	public void updateBillInStoreDtls(BillInStore billInStore) {
		List<BillInStoreDtl> billInStoreDtlList = billInStore.getBillInStoreDtlList();
		billInStore = billInStoreDao.queryAndLockEntityById(billInStore.getId());
		if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
			for (BillInStoreDtl billInStoreDtl : billInStoreDtlList) {
				Integer id = billInStoreDtl.getId();
				if (null != id) {
					BillInStoreDtl currBillInStoreDtl = billInStoreDtlDao.queryEntityById(id);
					PurchaseOrderLine purchaseOrderLine = purchaseOrderLineDao
							.queryPurchaseOrderLineById(currBillInStoreDtl.getPoDtlId());
					// 检查收货数量是否超过可入库数量
					validateReceiveNum(currBillInStoreDtl.getReceiveNum(), billInStoreDtl.getReceiveNum(),
							purchaseOrderLine);
					// 更新入库单明细
					billInStoreDtlDao.updateById(billInStoreDtl);
					// 删除理货明细
					billInStoreTallyDtlDao.deleteByBillInStoreDtlId(id);
					billInStoreTallyDtlService.updateBillInStoreDtlTallyInfo(id);
					// 更新订单入库数量
					updatePoWarehouseNum(currBillInStoreDtl, billInStoreDtl, null, BaseConsts.TWO);
				}
			}
			// 更新入库单头的收货数量、收货金额、理货数量、理货金额
			billInStore = billInStoreDao.queryAndLockEntityById(billInStore.getId());
			updateBillInStoreInfo(billInStore);
		} else {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_UPDATE_STATUS_ERROR);
		}
	}

	/**
	 * 根据入库单ID查询入库单明细
	 * 
	 * @param billInStoreDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillInStoreDtlResDto> queryBillInStoreDtlsByBillInStoreId(
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto, boolean inculdeTally) {
		PageResult<BillInStoreDtlResDto> result = new PageResult<BillInStoreDtlResDto>();

		int offSet = PageUtil.getOffSet(billInStoreDtlSearchReqDto.getPage(), billInStoreDtlSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, billInStoreDtlSearchReqDto.getPer_page());
		List<BillInStoreDtl> billInStoreDtlList = billInStoreDtlDao.queryResultsByCon(billInStoreDtlSearchReqDto,
				rowBounds);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_IN_STORE);
		List<BillInStoreDtlResDto> billInStoreDtlResDtoList = convertToResDto(billInStoreDtlList, inculdeTally,
				isAllowPerm);
		String totalStr = querySumBillInStoreDtl(billInStoreDtlSearchReqDto, isAllowPerm);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		result.setItems(billInStoreDtlResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), billInStoreDtlSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(billInStoreDtlSearchReqDto.getPage());
		result.setPer_page(billInStoreDtlSearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 查询合计
	 * 
	 * @param billInStoreSearchReqDto
	 * @param isAllowPerm
	 * @return
	 */
	private String querySumBillInStoreDtl(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto, boolean isAllowPerm) {
		String totalStr = "";
		BillInStoreDtlSum billInStoreDtlSum = billInStoreDtlDao.querySumBillInStoreDtl(billInStoreDtlSearchReqDto);
		if (null != billInStoreDtlSum) {
			BigDecimal totalReceiveNum = BigDecimal.ZERO;
			BigDecimal totalTallyNum = BigDecimal.ZERO;
			BigDecimal totalReceiveAmount = BigDecimal.ZERO;
			String totalReceiveAmountStr = "";

			totalReceiveNum = null == billInStoreDtlSum.getReceiveNum() ? BigDecimal.ZERO
					: billInStoreDtlSum.getReceiveNum();
			totalTallyNum = null == billInStoreDtlSum.getTallyNum() ? BigDecimal.ZERO : billInStoreDtlSum.getTallyNum();
			totalReceiveAmount = null == billInStoreDtlSum.getReceiveAmount() ? BigDecimal.ZERO
					: billInStoreDtlSum.getReceiveAmount();

			if (isAllowPerm == true) {
				totalReceiveAmountStr = BaseConsts.NO_PERMISSION_HIT;
			} else {
				totalReceiveAmountStr = DecimalUtil.toAmountString(totalReceiveAmount);
			}
			totalStr = "收货数量：" + DecimalUtil.toQuantityString(totalReceiveNum) + "；理货数量："
					+ DecimalUtil.toQuantityString(totalTallyNum) + "；收货金额：" + totalReceiveAmountStr
					+ BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(billInStoreDtlSum.getCurrencyType());
		}
		return totalStr;
	}

	/**
	 * 根据入库单ID查询全部入库单明细(不分页)
	 * 
	 * @param billInStoreDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillInStoreDtlResDto> queryAllBillInStoreDtlsByBillInStoreId(
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		PageResult<BillInStoreDtlResDto> result = new PageResult<BillInStoreDtlResDto>();

		List<BillInStoreDtl> billInStoreDtlList = billInStoreDtlDao.queryResultsByCon(billInStoreDtlSearchReqDto);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_IN_STORE);
		List<BillInStoreDtlResDto> billInStoreDtlResDtoList = convertToResDto(billInStoreDtlList, false, isAllowPerm);
		result.setItems(billInStoreDtlResDtoList);

		return result;
	}

	/**
	 * 根据单据查询条件查询入库单明细(含入库单信息)
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	public List<BillInStoreDtlExtResDto> queryAllBillInStoreDtlExtList(
			BillInStoreSearchReqDto billInStoreSearchReqDto) {
		if (null == billInStoreSearchReqDto.getUserId()) {
			billInStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<BillInStoreDtlExt> billInStoreDtlExtList = billInStoreDtlDao
				.queryResultsByBillInStoreCon(billInStoreSearchReqDto);
		List<BillInStoreDtlExtResDto> billInStoreDtlExtResDtoList = convertToExtResDto(billInStoreDtlExtList);
		return billInStoreDtlExtResDtoList;
	}

	/**
	 * 根据可供理货的收货明细
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	public PageResult<BillInStoreTallyDtlExportResDto> queryAllBillInStoreTallyDtlExportList(
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		PageResult<BillInStoreTallyDtlExportResDto> result = new PageResult<BillInStoreTallyDtlExportResDto>();

		List<BillInStoreDtl> billInStoreDtlList = billInStoreDtlDao
				.queryBillInStoreTallyExportResults(billInStoreDtlSearchReqDto);
		List<BillInStoreTallyDtlExportResDto> billInStoreTallyDtlExportList = Lists.newArrayList();
		if (!CollectionUtils.isEmpty(billInStoreDtlList)) {
			for (BillInStoreDtl billInStoreDtl : billInStoreDtlList) {
				BillInStoreTallyDtlExportResDto billInStoreTallyDtlExport = new BillInStoreTallyDtlExportResDto();
				BeanUtils.copyProperties(billInStoreDtl, billInStoreTallyDtlExport);
				if (billInStoreTallyDtlExport.getGoodsId() != null) {
					BaseGoods baseGoods = cacheService.getGoodsById(billInStoreTallyDtlExport.getGoodsId());
					if (null != baseGoods) {
						billInStoreTallyDtlExport.setGoodsName(baseGoods.getName());
						billInStoreTallyDtlExport.setGoodsNumber(baseGoods.getNumber());
						billInStoreTallyDtlExport.setGoodsType(baseGoods.getType());
						billInStoreTallyDtlExport.setGoodsBarCode(baseGoods.getBarCode());
						billInStoreTallyDtlExport.setGoodsUnit(baseGoods.getUnit());
					}
				}
				billInStoreTallyDtlExportList.add(billInStoreTallyDtlExport);
			}
		}
		result.setItems(billInStoreTallyDtlExportList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), billInStoreDtlSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(billInStoreDtlSearchReqDto.getPage());
		result.setPer_page(billInStoreDtlSearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 删除入库单明细
	 * 
	 * @param billInStoreDtlSearchReqDto
	 * @return
	 */
	public void deleteBillInStoreDtlsByIds(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		List<Integer> ids = billInStoreDtlSearchReqDto.getIds();
		Integer billInStoreId = billInStoreDtlSearchReqDto.getBillInStoreId();
		if (!CollectionUtils.isEmpty(ids) && null != billInStoreId) {
			BillInStore billInStore = billInStoreDao.queryAndLockEntityById(billInStoreId);
			if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
				for (Integer id : ids) {
					if (null != id) {
						BillInStoreDtl currBillInStoreDtl = billInStoreDtlDao.queryEntityById(id);
						billInStoreDtlDao.deleteById(id);
						billInStoreTallyDtlDao.deleteByBillInStoreDtlId(id);
						// 更新订单入库数量
						updatePoWarehouseNum(currBillInStoreDtl, currBillInStoreDtl, null, BaseConsts.THREE);
					}
				}

				// 更新入库单头的收货数量、收货金额、理货数量、理货金额
				billInStore = billInStoreDao.queryAndLockEntityById(billInStoreId);
				int count = billInStoreDao.queryDtlsCount(billInStore);
				if (count == 0) {
					billInStore.setCurrencyType(null);
				}
				updateBillInStoreInfo(billInStore);
			} else {
				throw new BaseException(ExcMsgEnum.BILL_IN_STORE_DELETE_STATUS_ERROR);
			}
		}
	}

	/**
	 * 根据入库单明细ID查询明细详情
	 * 
	 * @param billInStoreDtlSearchReqDto
	 * @return
	 */
	public Result<BillInStoreDtlResDto> queryBillInStoreDtlById(BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto) {
		Result<BillInStoreDtlResDto> result = new Result<BillInStoreDtlResDto>();
		BillInStoreDtl billInStoreDtlRes = billInStoreDtlDao.queryById(billInStoreDtlSearchReqDto);
		BillInStoreDtlResDto billInStoreDtlResDto = convertToResDto(billInStoreDtlRes, false);
		if (null != billInStoreDtlResDto.getPoDtlId()) {
			PurchaseOrderLine purchaseOrderLine = purchaseOrderLineDao
					.queryPurchaseOrderLineById(billInStoreDtlResDto.getPoDtlId());
			if (null != purchaseOrderLine) {
				BigDecimal goodsNum = (null == purchaseOrderLine.getGoodsNum() ? BigDecimal.ZERO
						: purchaseOrderLine.getGoodsNum());
				BigDecimal storageNum = (null == purchaseOrderLine.getStorageNum() ? BigDecimal.ZERO
						: purchaseOrderLine.getStorageNum());
				billInStoreDtlResDto.setUnStorageNum(DecimalUtil.subtract(goodsNum, storageNum));
			}
		}
		result.setItems(billInStoreDtlResDto);
		return result;
	}

	/**
	 * 根据入库单明细ID集合查询明细列表
	 * 
	 * @param billInStoreDtlReqDto
	 * @return
	 */
	public PageResult<BillInStoreDtlResDto> queryBillInStoreDtlByIds(BillInStoreDtlSearchReqDto billInStoreDtlReqDto) {
		PageResult<BillInStoreDtlResDto> result = new PageResult<BillInStoreDtlResDto>();
		List<Integer> ids = billInStoreDtlReqDto.getIds();
		if (!CollectionUtils.isEmpty(ids)) {
			List<BillInStoreDtl> billInStoreDtls = billInStoreDtlDao.queryByIds(ids);
			if (!CollectionUtils.isEmpty(billInStoreDtls)) {
				List<BillInStoreDtlResDto> BillInStoreDtlResDtos = new ArrayList<BillInStoreDtlResDto>(5);
				for (BillInStoreDtl billInStoreDtl : billInStoreDtls) {
					BillInStoreDtlResDto billInStoreDtlResDto = convertToResDto(billInStoreDtl, false);
					BillInStoreDtlResDtos.add(billInStoreDtlResDto);
				}
				result.setItems(BillInStoreDtlResDtos);
			}
		}
		return result;
	}

	/**
	 * 更新订单入库数量
	 * 
	 * @param currBillInStoreDtl
	 *            当前入库明细
	 * @param newBillInStoreDtl
	 *            新入库明细
	 * @param purchaseOrderLine
	 *            采购订单明细
	 * @param operateFlag
	 *            1-新增 2-修改 3-删除
	 */
	public void updatePoWarehouseNum(BillInStoreDtl currBillInStoreDtl, BillInStoreDtl newBillInStoreDtl,
			PurchaseOrderLine purchaseOrderLine, Integer operateFlag) {
		if (null == purchaseOrderLine) {
			purchaseOrderLine = purchaseOrderLineDao.queryPurchaseOrderLineById(currBillInStoreDtl.getPoDtlId());
		}
		if (null != purchaseOrderLine) {
			// 更新订单明细入库数量
			BigDecimal storageNum = (null == purchaseOrderLine.getStorageNum() ? BigDecimal.ZERO
					: purchaseOrderLine.getStorageNum());
			BigDecimal goodsNum = (null == purchaseOrderLine.getGoodsNum() ? BigDecimal.ZERO
					: purchaseOrderLine.getGoodsNum());

			PurchaseOrderLine purchaseOrderLine2 = new PurchaseOrderLine();
			purchaseOrderLine2.setId(currBillInStoreDtl.getPoDtlId());
			if (operateFlag.equals(BaseConsts.ONE)) { // 1-新增
				purchaseOrderLine2.setStorageNum(DecimalUtil.add(storageNum, newBillInStoreDtl.getReceiveNum()));
				if (DecimalUtil.gt(purchaseOrderLine2.getStorageNum(), goodsNum)) {
					throw new BaseException(ExcMsgEnum.RECEIVE_NUM_EXCEED);
				}
			}
			if (operateFlag.equals(BaseConsts.TWO)) { // 2-修改
				purchaseOrderLine2.setStorageNum(
						DecimalUtil.add(DecimalUtil.subtract(storageNum, currBillInStoreDtl.getReceiveNum()),
								newBillInStoreDtl.getReceiveNum()));
				if (DecimalUtil.gt(purchaseOrderLine2.getStorageNum(), goodsNum)) {
					throw new BaseException(ExcMsgEnum.RECEIVE_NUM_EXCEED);
				}
			}
			if (operateFlag.equals(BaseConsts.THREE)) { // 3-删除
				purchaseOrderLine2.setStorageNum(DecimalUtil.subtract(storageNum, newBillInStoreDtl.getReceiveNum()));
				if (DecimalUtil.lt(purchaseOrderLine2.getStorageNum(), BigDecimal.ZERO)) {
					throw new BaseException(ExcMsgEnum.RECEIVE_NUM_EXCEED);
				}
			}
			purchaseOrderLineDao.updatePurchaseOrderLineById(purchaseOrderLine2);

			// 更新订单头到货数量和金额
			PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(purchaseOrderLine.getPoId());
			PurchaseOrderTitle purchaseOrderTitle2 = purchaseOrderLineDao.queryTotalByPoId(purchaseOrderLine.getPoId());
			if (null != purchaseOrderTitle2) {
				purchaseOrderTitle.setArrivalNum(null == purchaseOrderTitle2.getArrivalNum() ? BigDecimal.ZERO
						: purchaseOrderTitle2.getArrivalNum());
				purchaseOrderTitle.setArrivalAmount(null == purchaseOrderTitle2.getArrivalAmount() ? BigDecimal.ZERO
						: purchaseOrderTitle2.getArrivalAmount());
				purchaseOrderTitleDao.updatePurchaseOrderTitleById(purchaseOrderTitle);
			}
		}
	}

	/**
	 * 添加和修改收货明细时校验收货数量是否超过订单入库数量
	 */
	private void validateReceiveNum(BigDecimal oldReceiveNum, BigDecimal newReceiveNum,
			PurchaseOrderLine purchaseOrderLine) {
		BigDecimal goodsNum = (null == purchaseOrderLine.getGoodsNum() ? BigDecimal.ZERO
				: purchaseOrderLine.getGoodsNum());
		oldReceiveNum = (null == oldReceiveNum ? BigDecimal.ZERO : oldReceiveNum);
		newReceiveNum = (null == newReceiveNum ? BigDecimal.ZERO : newReceiveNum);
		BigDecimal storageNum = (null == purchaseOrderLine.getStorageNum() ? BigDecimal.ZERO
				: purchaseOrderLine.getStorageNum());
		BigDecimal currStorageNum = DecimalUtil.add(DecimalUtil.subtract(storageNum, oldReceiveNum), newReceiveNum);
		if (DecimalUtil.gt(currStorageNum, goodsNum)) {
			throw new BaseException(ExcMsgEnum.RECEIVE_NUM_EXCEED);
		}
	}

	/**
	 * 更新入库单头的收货数量、收货金额、理货数量、理货金额
	 * 
	 * @param billInStoreId
	 */
	private void updateBillInStoreInfo(BillInStore billInStore) {
		if (null != billInStore.getId()) {
			BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
			billInStoreDtl.setBillInStoreId(billInStore.getId());
			BillInStoreDtlSum billInStoreDtlSum = billInStoreDtlDao.querySumByBillInStoreId(billInStoreDtl);

			if (null != billInStoreDtlSum) {
				BillInStore billInStoreReq = new BillInStore();
				billInStoreReq.setId(billInStore.getId());
				billInStoreReq.setReceiveNum(null == billInStoreDtlSum.getReceiveNum() ? BigDecimal.ZERO
						: billInStoreDtlSum.getReceiveNum());
				billInStoreReq.setReceiveAmount(null == billInStoreDtlSum.getReceiveAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billInStoreDtlSum.getReceiveAmount()));
				billInStoreReq.setTallyNum(
						null == billInStoreDtlSum.getTallyNum() ? BigDecimal.ZERO : billInStoreDtlSum.getTallyNum());
				billInStoreReq.setTallyAmount(null == billInStoreDtlSum.getTallyAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billInStoreDtlSum.getTallyAmount()));
				billInStoreReq.setPayAmount(null == billInStoreDtlSum.getPayAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billInStoreDtlSum.getPayAmount()));
				billInStoreReq.setCurrencyType(billInStore.getCurrencyType());
				billInStoreDao.updateBillInStoreInfo(billInStoreReq);
			}
		}
	}

	private List<BillInStoreDtlResDto> convertToResDto(List<BillInStoreDtl> billInStoreDtlList, boolean inculdeTally,
			boolean isAllowPerm) {
		List<BillInStoreDtlResDto> billInStoreDtlResDtoList = new ArrayList<BillInStoreDtlResDto>(5);
		if (CollectionUtils.isEmpty(billInStoreDtlList)) {
			return billInStoreDtlResDtoList;
		}
		for (BillInStoreDtl billInStoreDtl : billInStoreDtlList) {
			BillInStoreDtlResDto billInStoreDtlResDto = convertToResDto(billInStoreDtl, isAllowPerm);

			if (inculdeTally == true) { // 返回结果集包含理货明细
				List<BillInStoreTallyDtl> tallyDtlList = billInStoreTallyDtlDao
						.queryResultsByBillInStoreDtlId(billInStoreDtl.getId());
				if (!CollectionUtils.isEmpty(tallyDtlList)) {
					List<BillInStoreTallyDtlResDto> tallyDtlResDtoList = new ArrayList<BillInStoreTallyDtlResDto>(5);
					for (BillInStoreTallyDtl billInStoreTallyDtl : tallyDtlList) {
						BillInStoreTallyDtlResDto billInStoreTallyDtlResDto = convertToTallyResDto(billInStoreTallyDtl);
						tallyDtlResDtoList.add(billInStoreTallyDtlResDto);
					}
					billInStoreDtlResDto.setBillInStoreTallyDtlResDtoList(tallyDtlResDtoList);
				}
			}
			billInStoreDtlResDtoList.add(billInStoreDtlResDto);
		}
		return billInStoreDtlResDtoList;
	}

	private BillInStoreDtlResDto convertToResDto(BillInStoreDtl billInStoreDtl, boolean isAllowPerm) {
		BillInStoreDtlResDto billInStoreDtlResDto = new BillInStoreDtlResDto();
		if (null != billInStoreDtl) {
			BeanUtils.copyProperties(billInStoreDtl, billInStoreDtlResDto);
			if (billInStoreDtlResDto.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billInStoreDtlResDto.getGoodsId());
				if (null != baseGoods) {
					billInStoreDtlResDto.setGoodsName(baseGoods.getName());
					billInStoreDtlResDto.setGoodsNumber(baseGoods.getNumber());
					billInStoreDtlResDto.setGoodsType(baseGoods.getType());
					billInStoreDtlResDto.setGoodsUnit(baseGoods.getUnit());
					billInStoreDtlResDto.setGoodsBarCode(baseGoods.getBarCode());
					billInStoreDtlResDto.setSpecification(baseGoods.getSpecification());
				}
			}

			BigDecimal receivePrice = (null == billInStoreDtlResDto.getReceivePrice() ? BigDecimal.ZERO
					: billInStoreDtlResDto.getReceivePrice());
			BigDecimal receiveAmount = (null == billInStoreDtlResDto.getReceiveAmount() ? BigDecimal.ZERO
					: billInStoreDtlResDto.getReceiveAmount());
			billInStoreDtlResDto.setReceivePriceStr(DecimalUtil.toPriceString(receivePrice));
			billInStoreDtlResDto.setReceiveAmountStr(DecimalUtil.toAmountString(receiveAmount));
			if (isAllowPerm) { // 不显示金额权限
				billInStoreDtlResDto.setReceivePriceStr(BaseConsts.NO_PERMISSION_HIT);
				billInStoreDtlResDto.setReceiveAmountStr(BaseConsts.NO_PERMISSION_HIT);
			}
		}
		return billInStoreDtlResDto;
	}

	private BillInStoreTallyDtlResDto convertToTallyResDto(BillInStoreTallyDtl billInStoreTallyDtl) {
		BillInStoreTallyDtlResDto billInStoreTallyDtlResDto = new BillInStoreTallyDtlResDto();
		if (null != billInStoreTallyDtl) {
			BeanUtils.copyProperties(billInStoreTallyDtl, billInStoreTallyDtlResDto);
			if (billInStoreTallyDtlResDto.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billInStoreTallyDtlResDto.getGoodsId());
				if (null != baseGoods) {
					billInStoreTallyDtlResDto.setGoodsName(baseGoods.getName());
					billInStoreTallyDtlResDto.setGoodsNumber(baseGoods.getNumber());
					billInStoreTallyDtlResDto.setGoodsType(baseGoods.getType());
					billInStoreTallyDtlResDto.setGoodsBarCode(baseGoods.getBarCode());
				}
			}
			if (billInStoreTallyDtl.getGoodsStatus() != null) {
				billInStoreTallyDtlResDto
						.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
								Integer.toString(billInStoreTallyDtl.getGoodsStatus())));
			}
		}
		return billInStoreTallyDtlResDto;
	}

	/**
	 * 对象转换扩展
	 * 
	 * @param billInStoreDtlExtList
	 * @return
	 */
	private List<BillInStoreDtlExtResDto> convertToExtResDto(List<BillInStoreDtlExt> billInStoreDtlExtList) {
		List<BillInStoreDtlExtResDto> billInStoreDtlExtResDtoList = new ArrayList<BillInStoreDtlExtResDto>(5);
		if (CollectionUtils.isEmpty(billInStoreDtlExtList)) {
			return billInStoreDtlExtResDtoList;
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_IN_STORE);
		for (BillInStoreDtlExt billInStoreDtlExt : billInStoreDtlExtList) {
			BillInStoreDtlExtResDto billInStoreDtlExtResDto = convertToExtResDto(billInStoreDtlExt, isAllowPerm);
			billInStoreDtlExtResDtoList.add(billInStoreDtlExtResDto);
		}
		return billInStoreDtlExtResDtoList;
	}

	private BillInStoreDtlExtResDto convertToExtResDto(BillInStoreDtlExt billInStoreDtlExt, boolean isAllowPerm) {
		BillInStoreDtlExtResDto billInStoreDtlExtResDto = new BillInStoreDtlExtResDto();
		if (null != billInStoreDtlExt) {
			BeanUtils.copyProperties(billInStoreDtlExt, billInStoreDtlExtResDto);
			if (billInStoreDtlExt.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billInStoreDtlExtResDto.getGoodsId());
				if (null != baseGoods) {
					billInStoreDtlExtResDto.setGoodsName(baseGoods.getName());
					billInStoreDtlExtResDto.setGoodsNumber(baseGoods.getNumber());
					billInStoreDtlExtResDto.setGoodsType(baseGoods.getType());
					billInStoreDtlExtResDto.setGoodsUnit(baseGoods.getUnit());
					billInStoreDtlExtResDto.setGoodsBarCode(baseGoods.getBarCode());
				}
			}

			BigDecimal receivePrice = (null == billInStoreDtlExtResDto.getReceivePrice() ? BigDecimal.ZERO
					: billInStoreDtlExtResDto.getReceivePrice());
			BigDecimal receiveAmount = (null == billInStoreDtlExtResDto.getReceiveAmount() ? BigDecimal.ZERO
					: billInStoreDtlExtResDto.getReceiveAmount());
			billInStoreDtlExtResDto.setReceivePriceStr(DecimalUtil.toPriceString(receivePrice));
			billInStoreDtlExtResDto.setReceiveAmountStr(DecimalUtil.toAmountString(receiveAmount));
			if (isAllowPerm) { // 不显示金额权限
				billInStoreDtlExtResDto.setReceivePriceStr(BaseConsts.NO_PERMISSION_HIT);
				billInStoreDtlExtResDto.setReceiveAmountStr(BaseConsts.NO_PERMISSION_HIT);
			}

			// 入库单信息
			if (billInStoreDtlExt.gettStatus() != null) {
				billInStoreDtlExtResDto.settStatusName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_IN_STORE_STATUS, Integer.toString(billInStoreDtlExt.gettStatus())));
			}
			if (billInStoreDtlExt.gettBillType() != null) {
				billInStoreDtlExtResDto.settBillTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_IN_STORE_TYPE, Integer.toString(billInStoreDtlExt.gettBillType())));
			}
			if (billInStoreDtlExt.getCurrencyType() != null) {
				billInStoreDtlExtResDto.settCurrencyTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.DEFAULT_CURRENCY_TYPE, Integer.toString(billInStoreDtlExt.getCurrencyType())));
			}

			billInStoreDtlExtResDto
					.settProjectName(cacheService.showProjectNameById(billInStoreDtlExt.gettProjectId()));
			billInStoreDtlExtResDto.settSupplierName(cacheService
					.showSubjectNameByIdAndKey(billInStoreDtlExt.gettSupplierId(), CacheKeyConsts.SUPPLIER));
			billInStoreDtlExtResDto.settWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billInStoreDtlExt.gettWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billInStoreDtlExtResDto.settCustomerName(cacheService
					.showSubjectNameByIdAndKey(billInStoreDtlExt.gettCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		return billInStoreDtlExtResDto;
	}

	public List<BillInStoreDtlTaxGroupSum> queryTaxGroupSumByBillInStoreId(Integer billInStoreId) {
		List<BillInStoreDtlTaxGroupSum> billInStoreDtlTaxGroupSums = new ArrayList<BillInStoreDtlTaxGroupSum>();
		billInStoreDtlTaxGroupSums = billInStoreDtlDao.queryTaxGroupSumByBillInStoreId(billInStoreId);
		return billInStoreDtlTaxGroupSums;
	}

	public BigDecimal querySumByBillInStoreId(Integer billInStoreId) {
		return billInStoreDtlDao.querySumAmountByBillInStoreId(billInStoreId);
	}

	public BigDecimal querySumCostAmountByBillInStoreId(Integer billInStoreId) {
		return billInStoreDtlDao.querySumCostAmountByBillInStoreId(billInStoreId);
	}

	public boolean isOverBillInStoreDtlMaxLine(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		billInStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billInStoreDtlDao.queryCountByBillInStoreCon(billInStoreSearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("入库单单据明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillInStoreDtlExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_in_store_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_11);
			asyncExcelService.addAsyncExcel(billInStoreSearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillInStoreDtlExport(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillInStoreDtlExtResDto> billInStoreDtlExtResDtoList = queryAllBillInStoreDtlExtList(
				billInStoreSearchReqDto);
		model.put("billInStoreDtlList", billInStoreDtlExtResDtoList);
		return model;
	}
}
