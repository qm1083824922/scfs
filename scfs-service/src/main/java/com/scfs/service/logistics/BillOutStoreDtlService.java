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
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.export.CustomsApplyDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.BillOutStorePickDtlDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.export.entity.CustomsApply;
import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlCustomsResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlExtResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStorePickDtlResDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStoreDtlExt;
import com.scfs.domain.logistics.entity.BillOutStoreDtlSum;
import com.scfs.domain.logistics.entity.BillOutStoreDtlTaxGroupSum;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月17日.
 */
@Service
public class BillOutStoreDtlService {
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private BillOutStorePickDtlDao billOutStorePickDtlDao;
	@Autowired
	private CustomsApplyDao customsApplyDao;
	@Autowired
	private StlDao stlDao;
	@Autowired
	private StlService stlService;
	@Autowired
	private BillOutStorePickDtlService billOutStorePickDtlService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	/**
	 * 根据出库单ID查询出库单明细
	 * 
	 * @param billOutStoreDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillOutStoreDtlResDto> queryBillOutStoreDtlsByBillOutStoreId(
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto, boolean includePick) {
		PageResult<BillOutStoreDtlResDto> result = new PageResult<BillOutStoreDtlResDto>();

		int offSet = PageUtil.getOffSet(billOutStoreDtlSearchReqDto.getPage(),
				billOutStoreDtlSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, billOutStoreDtlSearchReqDto.getPer_page());
		List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreDtlDao.queryResultsByCon(billOutStoreDtlSearchReqDto,
				rowBounds);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_OUT_STORE);
		List<BillOutStoreDtlResDto> billOutStoreDtlResDtoList = convertToResDto(billOutStoreDtlList, includePick,
				isAllowPerm);
		result.setItems(billOutStoreDtlResDtoList);
		String totalStr = querySumBillOutStoreDtl(billOutStoreDtlSearchReqDto, isAllowPerm);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), billOutStoreDtlSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(billOutStoreDtlSearchReqDto.getPage());
		result.setPer_page(billOutStoreDtlSearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 查询合计
	 * 
	 * @param billOutStoreSearchReqDto
	 * @param isAllowPerm
	 * @return
	 */
	private String querySumBillOutStoreDtl(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto,
			boolean isAllowPerm) {
		String totalStr = "";
		BillOutStoreDtlSum billOutStoreDtlSum = billOutStoreDtlDao.querySumBillOutStoreDtl(billOutStoreDtlSearchReqDto);
		if (null != billOutStoreDtlSum) {
			BigDecimal totalSendNum = BigDecimal.ZERO;
			BigDecimal totalPickupNum = BigDecimal.ZERO;
			BigDecimal totalSendAmount = BigDecimal.ZERO;
			BigDecimal totalPickupAmount = BigDecimal.ZERO;
			BigDecimal totalPoAmount = BigDecimal.ZERO;
			BigDecimal totalCostAmount = BigDecimal.ZERO;
			String totalSendAmountStr = "";
			String totalPickupAmountStr = "";
			String totalPoAmountStr = "";
			String totalCostAmountStr = "";

			totalSendNum = null == billOutStoreDtlSum.getSendNum() ? BigDecimal.ZERO : billOutStoreDtlSum.getSendNum();
			totalPickupNum = null == billOutStoreDtlSum.getPickupNum() ? BigDecimal.ZERO
					: billOutStoreDtlSum.getPickupNum();
			totalSendAmount = null == billOutStoreDtlSum.getSendAmount() ? BigDecimal.ZERO
					: billOutStoreDtlSum.getSendAmount();
			totalPickupAmount = null == billOutStoreDtlSum.getPickupAmount() ? BigDecimal.ZERO
					: billOutStoreDtlSum.getPickupAmount();
			totalPoAmount = null == billOutStoreDtlSum.getPoAmount() ? BigDecimal.ZERO
					: billOutStoreDtlSum.getPoAmount();
			totalCostAmount = null == billOutStoreDtlSum.getCostAmount() ? BigDecimal.ZERO
					: billOutStoreDtlSum.getCostAmount();

			if (isAllowPerm == true) {
				totalSendAmountStr = BaseConsts.NO_PERMISSION_HIT;
				totalPickupAmountStr = BaseConsts.NO_PERMISSION_HIT;
				totalPoAmountStr = BaseConsts.NO_PERMISSION_HIT;
				totalCostAmountStr = BaseConsts.NO_PERMISSION_HIT;
			} else {
				totalSendAmountStr = DecimalUtil.toAmountString(totalSendAmount);
				totalPickupAmountStr = DecimalUtil.toAmountString(totalPickupAmount);
				totalPoAmountStr = DecimalUtil.toAmountString(totalPoAmount);
				totalCostAmountStr = DecimalUtil.toAmountString(totalCostAmount);
			}
			totalStr = "发货数量：" + DecimalUtil.toQuantityString(totalSendNum) + "；拣货数量："
					+ DecimalUtil.toQuantityString(totalPickupNum) + "；发货金额：" + totalSendAmountStr
					+ BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(billOutStoreDtlSum.getCurrencyType()) + "；拣货金额："
					+ totalPickupAmountStr + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(billOutStoreDtlSum.getCurrencyType()) + "；订单金额："
					+ totalPoAmountStr + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(billOutStoreDtlSum.getCurrencyType()) + "；成本金额："
					+ totalCostAmountStr + BaseConsts.STRING_BLANK_SPACE
					+ BaseConsts.CURRENCY_UNIT_MAP.get(billOutStoreDtlSum.getCurrencyType());
		}
		return totalStr;
	}

	/**
	 * 根据出库单ID查询出库单明细(不分页)
	 * 
	 * @param billOutStoreDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillOutStoreDtlResDto> queryAllBillOutStoreDtlsByBillOutStoreId(
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		PageResult<BillOutStoreDtlResDto> result = new PageResult<BillOutStoreDtlResDto>();

		List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreDtlDao.queryResultsByCon(billOutStoreDtlSearchReqDto);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_OUT_STORE);
		List<BillOutStoreDtlResDto> billOutStoreDtlResDtoList = convertToResDto(billOutStoreDtlList, false,
				isAllowPerm);
		result.setItems(billOutStoreDtlResDtoList);

		return result;
	}

	/**
	 * 根据单据查询条件查询出库单明细(含出库单信息)
	 * 
	 * @param billInStoreDtlSearchReqDto
	 * @return
	 */
	public List<BillOutStoreDtlExtResDto> queryAllBillOutStoreDtlExtList(
			BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		if (null == billOutStoreSearchReqDto.getUserId()) {
			billOutStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<BillOutStoreDtlExt> billOutStoreDtlExtList = billOutStoreDtlDao
				.queryResultsByBillOutStoreCon(billOutStoreSearchReqDto);
		List<BillOutStoreDtlExtResDto> billOutStoreDtlExtResDtoList = convertToExtResDto(billOutStoreDtlExtList);

		return billOutStoreDtlExtResDtoList;
	}

	/**
	 * 根据出库单明细ID查询出库单明细
	 * 
	 * @param billOutStoreDtlReqDto
	 * @return
	 */
	public Result<BillOutStoreDtlResDto> queryBillOutStoreDtlById(BillOutStoreDtlSearchReqDto billOutStoreDtlReqDto) {
		Result<BillOutStoreDtlResDto> result = new Result<BillOutStoreDtlResDto>();
		BillOutStoreDtl billOutStoreDtlRes = billOutStoreDtlDao.queryById(billOutStoreDtlReqDto);
		BillOutStoreDtlResDto billOutStoreDtlResDto = convertToResDto(billOutStoreDtlRes, false);
		result.setItems(billOutStoreDtlResDto);
		return result;
	}

	/**
	 * 人工录入新增出库单明细(调拨)
	 * 
	 * @param billDeliveryReqDto
	 * @return
	 */
	public void addBillOutStoreDtls(BillOutStoreReqDto billOutStoreReqDto) {
		BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStoreReqDto.getId());
		if (billOutStore.getStatus().equals(BaseConsts.ONE) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) {// 待发货且未删除
			List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreReqDto.getBillOutStoreDtlList();
			if (!CollectionUtils.isEmpty(billOutStoreDtlList)) {
				for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtlList) {
					billOutStoreDtl.setBillOutStoreId(billOutStoreReqDto.getId());
					billOutStoreDtl.setPickupNum(BigDecimal.ZERO);
					billOutStoreDtl.setCostAmount(BigDecimal.ZERO);
					billOutStoreDtl.setPoAmount(BigDecimal.ZERO);
					billOutStoreDtl.setAssignStlFlag(BaseConsts.ZERO); // 0-不指定库存
					billOutStoreDtl.setCreator(ServiceSupport.getUser().getChineseName());
					billOutStoreDtl.setCreatorId(ServiceSupport.getUser().getId());
					billOutStoreDtlDao.insert(billOutStoreDtl);
				}
			}
			// 更新出库单发货数量、发货金额、拣货数量、拣货金额
			updateBillOutStoreInfo(billOutStore);
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 根据库存新增出库单明细(调拨)
	 * 
	 * @param billDeliveryReqDto
	 * @return
	 */
	public void addBillOutStoreDtlsByStl(BillOutStoreReqDto billOutStoreReqDto) {
		BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStoreReqDto.getId());
		if (billOutStore.getStatus().equals(BaseConsts.ONE) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) {// 待发货且未删除
			Integer currencyType = billOutStore.getCurrencyType();
			Integer oldCurrencyType = currencyType;

			List<Stl> stlList = billOutStoreReqDto.getStlList();
			if (!CollectionUtils.isEmpty(stlList)) {
				List<BillOutStoreDtl> billOutStoreDtlList = new ArrayList<BillOutStoreDtl>(5);
				for (Stl stl : stlList) {
					BillOutStoreDtl billOutStoreDtl = new BillOutStoreDtl();
					Integer stlId = stl.getId();
					Stl stlRes = stlDao.queryEntityById(stlId);
					billOutStoreDtl.setBillOutStoreId(billOutStore.getId());
					billOutStoreDtl.setStlId(stlRes.getId());
					billOutStoreDtl.setGoodsId(stlRes.getGoodsId());
					billOutStoreDtl.setBatchNo(stlRes.getBatchNo());
					billOutStoreDtl.setGoodsStatus(stlRes.getGoodsStatus());
					billOutStoreDtl.setSendNum(stl.getSendNum());
					billOutStoreDtl.setSendPrice(stl.getSendPrice());
					billOutStoreDtl.setPickupNum(BigDecimal.ZERO);
					billOutStoreDtl.setRemark(stlRes.getRemark());
					billOutStoreDtl.setCostAmount(BigDecimal.ZERO);
					billOutStoreDtl.setPoAmount(BigDecimal.ZERO);
					billOutStoreDtl.setAssignStlFlag(BaseConsts.ONE); // 1-指定库存
					billOutStoreDtl.setCreator(ServiceSupport.getUser().getChineseName());
					billOutStoreDtl.setCreatorId(ServiceSupport.getUser().getId());
					billOutStoreDtl.setPayPrice(stlRes.getPayPrice());
					billOutStoreDtl.setPayTime(stlRes.getPayTime());
					billOutStoreDtl.setPayRate(stlRes.getPayRate() == null ? BigDecimal.ZERO : stlRes.getPayRate());
					Integer currCurrencyType = stlRes.getCurrencyType();
					if (null == currencyType && null != currCurrencyType) {
						currencyType = currCurrencyType;
					} else if (null != currencyType && null != currCurrencyType) {
						if (!currencyType.equals(currCurrencyType)) {
							if (null == oldCurrencyType) {
								throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_CURRENCY_DIFF_ERROR);
							} else {
								throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_CURRENCY_TYPE_DIFF_ERROR);
							}
						}
					} else {
						throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_CURRENCY_TYPE_NOT_EXIST);
					}
					billOutStoreDtl.setPayRealCurrency(currCurrencyType);
					billOutStoreDtlDao.insert(billOutStoreDtl);
					billOutStoreDtlList.add(billOutStoreDtl);
				}
				// 自动拣货明细
				billOutStorePickDtlService.autoPickByBillOutStoreDtls(billOutStore, billOutStoreDtlList);
				// 更新出库单发货数量、发货金额、拣货数量、拣货金额
				billOutStore.setCurrencyType(currencyType);
				updateBillOutStoreInfo(billOutStore);
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 更新出库单明细(调拨)
	 * 
	 * @param billDeliveryReqDto
	 * @return
	 */
	public void updateBillOutStoreDtls(BillOutStoreReqDto billOutStoreReqDto) {
		BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStoreReqDto.getId());
		if (billOutStore.getStatus().equals(BaseConsts.ONE) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) {// 待发货且未删除
			List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreReqDto.getBillOutStoreDtlList();
			if (!CollectionUtils.isEmpty(billOutStoreDtlList)) {
				for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtlList) {
					billOutStoreDtlDao.updateById(billOutStoreDtl);
					// 删除拣货明细
					List<BillOutStorePickDtl> pickDtlList = billOutStorePickDtlDao
							.queryResultsByBillOutStoreDtlId(billOutStoreDtl.getId());
					for (BillOutStorePickDtl billOutStorePickDtl : pickDtlList) {
						billOutStorePickDtlDao.deleteById(billOutStorePickDtl.getId());
						stlService.releaseStl(billOutStorePickDtl.getStlId(), billOutStorePickDtl.getPickupNum()); // 释放锁定库存
					}
					// 更新出库单明细的拣货数量、拣货金额、成本金额、订单金额
					billOutStorePickDtlService.updateBillOutStoreDtlPickInfo(billOutStoreDtl.getId());
				}
				// 更新出库单发货数量、发货金额、拣货数量、拣货金额
				updateBillOutStoreInfo(billOutStore);
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_UPDATE_STATUS_ERROR);
		}
	}

	/**
	 * 删除出库单明细(调拨)
	 * 
	 * @param ids
	 * @return
	 */
	public void deleteBillOutStoreDtlsByIds(BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		List<Integer> ids = billOutStoreDtlSearchReqDto.getIds();
		Integer billOutStoreId = billOutStoreDtlSearchReqDto.getBillOutStoreId();
		if (!CollectionUtils.isEmpty(ids) && null != billOutStoreId) {
			BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStoreId);
			if (billOutStore.getStatus().equals(BaseConsts.ONE) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
				for (Integer id : ids) {
					billOutStoreDtlDao.deleteById(id);
					List<BillOutStorePickDtl> pickDtlList = billOutStorePickDtlDao.queryResultsByBillOutStoreDtlId(id);
					for (BillOutStorePickDtl billOutStorePickDtl : pickDtlList) {
						billOutStorePickDtlDao.deleteById(billOutStorePickDtl.getId());
						stlService.releaseStl(billOutStorePickDtl.getStlId(), billOutStorePickDtl.getPickupNum()); // 释放锁定库存
					}
				}
				int count = billOutStoreDao.queryDtlsCount(billOutStore);
				if (count == 0) {
					billOutStore.setCurrencyType(null);
				}
				// 更新出库单发货数量、发货金额、拣货数量、拣货金额
				updateBillOutStoreInfo(billOutStore);
			} else {
				throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_DELETE_STATUS_ERROR);
			}
		}
	}

	/**
	 * 更新出库单发货数量、发货金额、拣货数量、拣货金额
	 */
	public void updateBillOutStoreInfo(BillOutStore billOutStore) {
		BillOutStoreDtl billOutStoreDtlReq = new BillOutStoreDtl();
		billOutStoreDtlReq.setBillOutStoreId(billOutStore.getId());
		BillOutStoreDtlSum billOutStoreDtlSum = billOutStoreDtlDao.querySumByBillOutStoreId(billOutStoreDtlReq);

		if (null != billOutStoreDtlSum) {
			BillOutStore billOutStoreReq = new BillOutStore();
			billOutStoreReq.setId(billOutStore.getId());
			billOutStoreReq.setStatus(billOutStore.getStatus());
			billOutStoreReq.setSendNum(
					null == billOutStoreDtlSum.getSendNum() ? BigDecimal.ZERO : billOutStoreDtlSum.getSendNum());
			billOutStoreReq.setSendAmount(null == billOutStoreDtlSum.getSendAmount() ? BigDecimal.ZERO
					: DecimalUtil.formatScale2(billOutStoreDtlSum.getSendAmount()));
			billOutStoreReq.setCostAmount(null == billOutStoreDtlSum.getCostAmount() ? BigDecimal.ZERO
					: DecimalUtil.formatScale2(billOutStoreDtlSum.getCostAmount()));
			billOutStoreReq.setPoAmount(null == billOutStoreDtlSum.getPoAmount() ? BigDecimal.ZERO
					: DecimalUtil.formatScale2(billOutStoreDtlSum.getPoAmount()));
			billOutStoreReq.setPickupNum(
					null == billOutStoreDtlSum.getPickupNum() ? BigDecimal.ZERO : billOutStoreDtlSum.getPickupNum());
			billOutStoreReq.setPickupAmount(null == billOutStoreDtlSum.getPickupAmount() ? BigDecimal.ZERO
					: DecimalUtil.formatScale2(billOutStoreDtlSum.getPickupAmount()));
			billOutStoreReq.setCurrencyType(billOutStore.getCurrencyType());
			billOutStoreDao.updateBillOutStoreInfo(billOutStoreReq);
		}
	}

	private List<BillOutStoreDtlResDto> convertToResDto(List<BillOutStoreDtl> billOutStoreDtlList, boolean includePick,
			boolean isAllowPerm) {
		List<BillOutStoreDtlResDto> billOutStoreDtlResDtoList = new ArrayList<BillOutStoreDtlResDto>(5);
		if (CollectionUtils.isEmpty(billOutStoreDtlList)) {
			return billOutStoreDtlResDtoList;
		}
		for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtlList) {
			BillOutStoreDtlResDto billOutStoreDtlResDto = convertToResDto(billOutStoreDtl, isAllowPerm);
			if (includePick == true) {
				List<BillOutStorePickDtl> pickDtlList = billOutStorePickDtlDao
						.queryResultsByBillOutStoreDtlId(billOutStoreDtl.getId());
				if (!CollectionUtils.isEmpty(pickDtlList)) {
					List<BillOutStorePickDtlResDto> pickDtlResDtoList = new ArrayList<BillOutStorePickDtlResDto>(5);
					for (BillOutStorePickDtl billOutStorePickDtl : pickDtlList) {
						BillOutStorePickDtlResDto billOutStorePickDtlResDto = convertToPickResDto(billOutStorePickDtl);
						pickDtlResDtoList.add(billOutStorePickDtlResDto);
					}
					billOutStoreDtlResDto.setBillOutStorePickDtlResDtoList(pickDtlResDtoList);
				}
			}
			billOutStoreDtlResDtoList.add(billOutStoreDtlResDto);
		}
		return billOutStoreDtlResDtoList;
	}

	private BillOutStoreDtlResDto convertToResDto(BillOutStoreDtl billOutStoreDtl, boolean isAllowPerm) {
		BillOutStoreDtlResDto billOutStoreDtlResDto = new BillOutStoreDtlResDto();
		if (null != billOutStoreDtl) {
			BeanUtils.copyProperties(billOutStoreDtl, billOutStoreDtlResDto);
			if (billOutStoreDtl.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billOutStoreDtl.getGoodsId());
				if (null != baseGoods) {
					billOutStoreDtlResDto.setGoodsName(baseGoods.getName());
					billOutStoreDtlResDto.setGoodsNumber(baseGoods.getNumber());
					billOutStoreDtlResDto.setGoodsType(baseGoods.getType());
					billOutStoreDtlResDto.setGoodsUnit(baseGoods.getUnit());
					billOutStoreDtlResDto.setGoodsBarCode(baseGoods.getBarCode());
				}
			}
			if (billOutStoreDtl.getGoodsStatus() != null) {
				billOutStoreDtlResDto.setGoodsStatusName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_IN_STORE_GOODS_STATUS, Integer.toString(billOutStoreDtl.getGoodsStatus())));
			}
			if (billOutStoreDtl.getAssignStlFlag() != null) {
				billOutStoreDtlResDto.setAssignStlFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS,
						Integer.toString(billOutStoreDtl.getAssignStlFlag())));
			}

			BigDecimal sendPrice = (null == billOutStoreDtlResDto.getSendPrice() ? BigDecimal.ZERO
					: billOutStoreDtlResDto.getSendPrice());
			BigDecimal sendAmount = (null == billOutStoreDtlResDto.getSendAmount() ? BigDecimal.ZERO
					: billOutStoreDtlResDto.getSendAmount());
			BigDecimal costAmount = (null == billOutStoreDtlResDto.getCostAmount() ? BigDecimal.ZERO
					: billOutStoreDtlResDto.getCostAmount());
			BigDecimal poAmount = (null == billOutStoreDtlResDto.getPoAmount() ? BigDecimal.ZERO
					: billOutStoreDtlResDto.getPoAmount());
			billOutStoreDtlResDto.setSendPriceStr(DecimalUtil.toPriceString(sendPrice));
			billOutStoreDtlResDto.setSendAmountStr(DecimalUtil.toAmountString(sendAmount));
			billOutStoreDtlResDto.setCostAmountStr(DecimalUtil.toAmountString(costAmount));
			billOutStoreDtlResDto.setPoAmountStr(DecimalUtil.toAmountString(poAmount));
			if (isAllowPerm) { // 不显示金额权限
				billOutStoreDtlResDto.setSendPriceStr(BaseConsts.NO_PERMISSION_HIT);
				billOutStoreDtlResDto.setSendAmountStr(BaseConsts.NO_PERMISSION_HIT);
				billOutStoreDtlResDto.setCostAmountStr(BaseConsts.NO_PERMISSION_HIT);
				billOutStoreDtlResDto.setPoAmountStr(BaseConsts.NO_PERMISSION_HIT);
			}
		}
		return billOutStoreDtlResDto;
	}

	private BillOutStorePickDtlResDto convertToPickResDto(BillOutStorePickDtl billOutStorePickDtl) {
		BillOutStorePickDtlResDto billOutStorePickDtlResDto = new BillOutStorePickDtlResDto();
		if (null != billOutStorePickDtl) {
			BeanUtils.copyProperties(billOutStorePickDtl, billOutStorePickDtlResDto);
			if (billOutStorePickDtl.getGoodsStatus() != null) {
				billOutStorePickDtlResDto
						.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
								Integer.toString(billOutStorePickDtl.getGoodsStatus())));
			}
		}
		return billOutStorePickDtlResDto;
	}

	/**
	 * 对象转换扩展
	 * 
	 * @param billInStoreDtlExtList
	 * @return
	 */
	private List<BillOutStoreDtlExtResDto> convertToExtResDto(List<BillOutStoreDtlExt> billOutStoreDtlExtList) {
		List<BillOutStoreDtlExtResDto> billOutStoreDtlExtResDtoList = new ArrayList<BillOutStoreDtlExtResDto>(5);
		if (CollectionUtils.isEmpty(billOutStoreDtlExtList)) {
			return billOutStoreDtlExtResDtoList;
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_OUT_STORE);
		for (BillOutStoreDtlExt billOutStoreDtlExt : billOutStoreDtlExtList) {
			BillOutStoreDtlExtResDto billOutStoreDtlExtResDto = convertToExtResDto(billOutStoreDtlExt, isAllowPerm);
			billOutStoreDtlExtResDtoList.add(billOutStoreDtlExtResDto);
		}
		return billOutStoreDtlExtResDtoList;
	}

	private BillOutStoreDtlExtResDto convertToExtResDto(BillOutStoreDtlExt billOutStoreDtlExt, boolean isAllowPerm) {
		BillOutStoreDtlExtResDto billOutStoreDtlExtResDto = new BillOutStoreDtlExtResDto();
		if (null != billOutStoreDtlExt) {
			BeanUtils.copyProperties(billOutStoreDtlExt, billOutStoreDtlExtResDto);
			if (billOutStoreDtlExt.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billOutStoreDtlExt.getGoodsId());
				if (null != baseGoods) {
					billOutStoreDtlExtResDto.setGoodsName(baseGoods.getName());
					billOutStoreDtlExtResDto.setGoodsNumber(baseGoods.getNumber());
					billOutStoreDtlExtResDto.setGoodsType(baseGoods.getType());
					billOutStoreDtlExtResDto.setGoodsUnit(baseGoods.getUnit());
					billOutStoreDtlExtResDto.setGoodsBarCode(baseGoods.getBarCode());
				}
			}
			if (billOutStoreDtlExt.getGoodsStatus() != null) {
				billOutStoreDtlExtResDto
						.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
								Integer.toString(billOutStoreDtlExt.getGoodsStatus())));
			}
			if (billOutStoreDtlExt.getAssignStlFlag() != null) {
				billOutStoreDtlExtResDto.setAssignStlFlagName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.PROMPT_STATUS, Integer.toString(billOutStoreDtlExt.getAssignStlFlag())));
			}

			// 出库单信息
			if (billOutStoreDtlExt.gettStatus() != null) {
				billOutStoreDtlExtResDto.settStatusName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_OUT_STORE_STATUS, Integer.toString(billOutStoreDtlExt.gettStatus())));
			}
			if (billOutStoreDtlExt.gettBillType() != null) {
				billOutStoreDtlExtResDto.settBillTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_OUT_STORE_TYPE, Integer.toString(billOutStoreDtlExt.gettBillType())));
			}
			if (billOutStoreDtlExt.gettTransferMode() != null) {
				billOutStoreDtlExtResDto.settTransferModeName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_OUT_STORE_TRANSFER_MODE,
								Integer.toString(billOutStoreDtlExt.gettTransferMode())));
			}
			if (billOutStoreDtlExt.gettReasonType() != null) {
				billOutStoreDtlExtResDto.settReasonName(ServiceSupport.getValueByBizCode(BizCodeConsts.OUT_REASON_TYPE,
						Integer.toString(billOutStoreDtlExt.gettReasonType())));
			}
			if (billOutStoreDtlExt.gettCurrencyType() != null) {
				billOutStoreDtlExtResDto.settCurrencyTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.DEFAULT_CURRENCY_TYPE, Integer.toString(billOutStoreDtlExt.gettCurrencyType())));
			}

			billOutStoreDtlExtResDto
					.settProjectName(cacheService.showProjectNameById(billOutStoreDtlExt.gettProjectId()));
			billOutStoreDtlExtResDto.settWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billOutStoreDtlExt.gettWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billOutStoreDtlExtResDto.settReceiveWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billOutStoreDtlExt.gettReceiveWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billOutStoreDtlExtResDto.settCustomerName(cacheService
					.showSubjectNameByIdAndKey(billOutStoreDtlExt.gettCustomerId(), CacheKeyConsts.CUSTOMER));

			BaseAddress baseAddress = cacheService.getAddressById(billOutStoreDtlExt.gettCustomerAddressId());
			if (null != baseAddress) {
				billOutStoreDtlExtResDto.settCustomerAddress(baseAddress.getShowValue());
			}

			BigDecimal sendPrice = (null == billOutStoreDtlExtResDto.getSendPrice() ? BigDecimal.ZERO
					: billOutStoreDtlExtResDto.getSendPrice());
			BigDecimal sendAmount = (null == billOutStoreDtlExtResDto.getSendAmount() ? BigDecimal.ZERO
					: billOutStoreDtlExtResDto.getSendAmount());
			BigDecimal costAmount = (null == billOutStoreDtlExtResDto.getCostAmount() ? BigDecimal.ZERO
					: billOutStoreDtlExtResDto.getCostAmount());
			BigDecimal poAmount = (null == billOutStoreDtlExtResDto.getPoAmount() ? BigDecimal.ZERO
					: billOutStoreDtlExtResDto.getPoAmount());
			billOutStoreDtlExtResDto.setSendPriceStr(DecimalUtil.toPriceString(sendPrice));
			billOutStoreDtlExtResDto.setSendAmountStr(DecimalUtil.toAmountString(sendAmount));
			billOutStoreDtlExtResDto.setCostAmountStr(DecimalUtil.toAmountString(costAmount));
			billOutStoreDtlExtResDto.setPoAmountStr(DecimalUtil.toAmountString(poAmount));
			if (isAllowPerm) { // 不显示金额权限
				billOutStoreDtlExtResDto.setSendPriceStr(BaseConsts.NO_PERMISSION_HIT);
				billOutStoreDtlExtResDto.setSendAmountStr(BaseConsts.NO_PERMISSION_HIT);
				billOutStoreDtlExtResDto.setCostAmountStr(BaseConsts.NO_PERMISSION_HIT);
				billOutStoreDtlExtResDto.setPoAmountStr(BaseConsts.NO_PERMISSION_HIT);
			}
		}
		return billOutStoreDtlExtResDto;
	}

	public List<BillOutStoreDtlTaxGroupSum> queryTaxGroupSumByBillOutStoreId(Integer billOutStoreId) {
		List<BillOutStoreDtlTaxGroupSum> billOutStoreDtlTaxGroupSums = new ArrayList<BillOutStoreDtlTaxGroupSum>();
		billOutStoreDtlTaxGroupSums = billOutStoreDtlDao.queryTaxGroupSumByBillOutStoreId(billOutStoreId);
		return billOutStoreDtlTaxGroupSums;
	}

	public BigDecimal querySumByBillOutStoreId(Integer billOutStoreId) {
		return billOutStoreDtlDao.querySumAmountByBillOutStoreId(billOutStoreId);
	}

	/**
	 * 查询可报关的出库明细
	 * 
	 * @param billOutStoreSearchReqDto
	 * @return
	 */
	public PageResult<BillOutStoreDtlCustomsResDto> queryAvailableResultByCon(
			BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		PageResult<BillOutStoreDtlCustomsResDto> result = new PageResult<BillOutStoreDtlCustomsResDto>();

		if (null != billOutStoreSearchReqDto.getCustomsApplyId()) {
			CustomsApply customsApply = customsApplyDao.queryEntityById(billOutStoreSearchReqDto.getCustomsApplyId());
			if (null != customsApply) {
				billOutStoreSearchReqDto.setProjectId(customsApply.getProjectId());
				billOutStoreSearchReqDto.setCustomerId(customsApply.getCustomerId());

				int offSet = PageUtil.getOffSet(billOutStoreSearchReqDto.getPage(),
						billOutStoreSearchReqDto.getPer_page());
				RowBounds rowBounds = new RowBounds(offSet, billOutStoreSearchReqDto.getPer_page());
				List<BillOutStoreDtlCustomsResDto> billOutStoreDtlCustomsResDtoList = billOutStoreDtlDao
						.queryAvailableResultByCon(billOutStoreSearchReqDto, rowBounds);
				if (!CollectionUtils.isEmpty(billOutStoreDtlCustomsResDtoList)) {
					for (BillOutStoreDtlCustomsResDto billOutStoreDtlCustomsResDto : billOutStoreDtlCustomsResDtoList) {
						if (billOutStoreDtlCustomsResDto.getGoodsId() != null) {
							BaseGoods baseGoods = cacheService.getGoodsById(billOutStoreDtlCustomsResDto.getGoodsId());
							if (null != baseGoods) {
								billOutStoreDtlCustomsResDto.setGoodsName(baseGoods.getName());
								billOutStoreDtlCustomsResDto.setGoodsNumber(baseGoods.getNumber());
								billOutStoreDtlCustomsResDto.setGoodsType(baseGoods.getType());
								billOutStoreDtlCustomsResDto.setGoodsBarCode(baseGoods.getBarCode());
								billOutStoreDtlCustomsResDto.setTaxRate(baseGoods.getTaxRate());
							}
						}
						billOutStoreDtlCustomsResDto.setWarehouseName(cacheService.showSubjectNameByIdAndKey(
								billOutStoreDtlCustomsResDto.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
					}
				}

				result.setItems(billOutStoreDtlCustomsResDtoList);
				int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(),
						billOutStoreSearchReqDto.getPer_page());
				result.setLast_page(totalPage);
				result.setTotal(CountHelper.getTotalRow());
				result.setCurrent_page(billOutStoreSearchReqDto.getPage());
				result.setPer_page(billOutStoreSearchReqDto.getPer_page());
			}
		}

		return result;
	}

	public boolean isOverBillOutStoreDtlMaxLine(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		billOutStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billOutStoreDtlDao.queryCountByBillOutStoreCon(billOutStoreSearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("出库单单据明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillOutStoreDtlExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_out_store_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_13);
			asyncExcelService.addAsyncExcel(billOutStoreSearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillOutStoreDtlExport(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillOutStoreDtlExtResDto> billOutStoreDtlExtResDtoList = queryAllBillOutStoreDtlExtList(
				billOutStoreSearchReqDto);
		model.put("billOutStoreDtlList", billOutStoreDtlExtResDtoList);
		return model;
	}

	/**
	 * 查询出库单打印的数据
	 * 
	 * @param billOutStoreDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillOutStoreDtlResDto> queryBillOutDtlPrint(
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto) {
		PageResult<BillOutStoreDtlResDto> result = new PageResult<BillOutStoreDtlResDto>();
		List<Integer> ids = billOutStoreDtlSearchReqDto.getIds();
		List<BillOutStoreDtlResDto> outStoreDtlResDtos = new ArrayList<BillOutStoreDtlResDto>();
		if (!CollectionUtils.isEmpty(ids)) {
			BigDecimal countSenNum = BigDecimal.ZERO;
			for (Integer id : ids) {
				List<BillOutStoreDtlResDto> list = billOutStoreDtlDao.queryBillOutPrintByBillOutId(id);
				for (BillOutStoreDtlResDto billOutStoreDtlResDto : list) {
					countSenNum = DecimalUtil.add(countSenNum, billOutStoreDtlResDto.getSendNum());
					billOutStoreDtlResDto.setCountSendNum(countSenNum);
					outStoreDtlResDtos.add(billOutStoreDtlResDto);
				}
			}
		}
		result.setItems(outStoreDtlResDtos);
		return result;
	}
}
