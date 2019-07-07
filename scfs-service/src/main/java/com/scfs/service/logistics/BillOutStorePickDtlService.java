package com.scfs.service.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.BillOutStorePickDtlDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStorePickDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStorePickDtlExtResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStorePickDtlResDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStoreDtlSum;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.logistics.entity.BillOutStorePickDtlExt;
import com.scfs.domain.logistics.entity.BillOutStorePickDtlSum;
import com.scfs.domain.logistics.entity.BillOutStoreTaxGroupCostPrice;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月17日.
 */
@Service
public class BillOutStorePickDtlService {
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private BillOutStorePickDtlDao billOutStorePickDtlDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private StlDao stlDao;
	@Autowired
	private StlService stlService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	/**
	 * 查询全部出库单明细
	 * 
	 * @param billOutStorePickDtlSearchReqDto
	 * @return
	 */
	public PageResult<BillOutStorePickDtlResDto> queryAllBillOutStorePickDtlsByBillOutStoreId(
			BillOutStorePickDtlSearchReqDto billOutStorePickDtlSearchReqDto) {
		PageResult<BillOutStorePickDtlResDto> result = new PageResult<BillOutStorePickDtlResDto>();

		List<BillOutStorePickDtl> billOutStorePickDtlList = billOutStorePickDtlDao
				.queryResultsByBillOutStoreId(billOutStorePickDtlSearchReqDto.getBillOutStoreId());
		List<BillOutStorePickDtlResDto> billOutStorePickDtlResDtoList = convertToResDto(billOutStorePickDtlList);
		result.setItems(billOutStorePickDtlResDtoList);
		return result;
	}

	/**
	 * 新增拣货明细
	 * 
	 * @param billOutStoreDtl
	 * @return
	 */
	public void addBillOutStorePickDtls(BillOutStoreDtl billOutStoreDtl) {
		BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStoreDtl.getBillOutStoreId());
		if (!billOutStore.getStatus().equals(BaseConsts.FIVE) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 未发货且未删除
			List<Stl> stlList = billOutStoreDtl.getStlList();
			BigDecimal pickupNum = BigDecimal.ZERO;
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto = new BillOutStoreDtlSearchReqDto();
			billOutStoreDtlSearchReqDto.setId(billOutStoreDtl.getId());
			BillOutStoreDtl billOutStoreDtlRes = billOutStoreDtlDao.queryById(billOutStoreDtlSearchReqDto);
			for (Stl stl : stlList) {
				Stl stlRes = stlDao.queryEntityById(stl.getId());
				BillOutStorePickDtl billOutStorePickDtl = new BillOutStorePickDtl();
				billOutStorePickDtl.setBillOutStoreId(billOutStoreDtlRes.getBillOutStoreId());
				billOutStorePickDtl.setBillOutStoreDtlId(billOutStoreDtlRes.getId());
				billOutStorePickDtl.setGoodsId(billOutStoreDtlRes.getGoodsId());
				billOutStorePickDtl.setSendPrice(billOutStoreDtlRes.getSendPrice());
				billOutStorePickDtl.setCreator(ServiceSupport.getUser().getChineseName());
				billOutStorePickDtl.setCreatorId(ServiceSupport.getUser().getId());
				billOutStorePickDtl.setPickupNum(stl.getPickupNum());
				pickupNum = pickupNum.add(null == billOutStorePickDtl.getPickupNum() ? BigDecimal.ZERO
						: billOutStorePickDtl.getPickupNum());
				addBillOutStorePickDtl(billOutStore, billOutStorePickDtl, stlRes, stl.getPickupNum());
			}
			BigDecimal unPickupNum = (null == billOutStoreDtlRes.getUnPickupNum() ? BigDecimal.ZERO
					: billOutStoreDtlRes.getUnPickupNum());
			if (pickupNum.compareTo(unPickupNum) > 0) {
				throw new BaseException(ExcMsgEnum.AVAILABLE_PICK_NUM_NOT_ENOUGH);
			}
			// 更新出库单明细的拣货数量、拣货金额、成本金额、订单金额
			updateBillOutStoreDtlPickInfo(billOutStoreDtlRes.getId());
			// 更新出库单头的拣货数量和拣货金额
			updateBillOutStorePickInfo(billOutStoreDtlRes.getBillOutStoreId());
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_PICK_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 删除拣货明细
	 * 
	 * @param billOutStorePickDtl
	 * @return
	 */
	public void deleteById(BillOutStorePickDtl billOutStorePickDtl) {
		Integer id = billOutStorePickDtl.getId();
		Integer billOutStoreId = billOutStorePickDtl.getBillOutStoreId();
		if (null != id && null != billOutStoreId) {
			BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStoreId);
			if (!billOutStore.getStatus().equals(BaseConsts.FIVE)
					&& billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 未发货且未删除
				BillOutStorePickDtl pickDtl = billOutStorePickDtlDao.queryEntityById(id);
				billOutStorePickDtlDao.deleteById(id);
				if (null != pickDtl) {
					stlService.releaseStl(pickDtl.getStlId(), pickDtl.getPickupNum()); // 释放锁定库存
					// 更新出库单明细的拣货数量、拣货金额、成本金额、订单金额
					updateBillOutStoreDtlPickInfo(pickDtl.getBillOutStoreDtlId());
					// 更新出库单头的拣货数量和拣货金额
					updateBillOutStorePickInfo(pickDtl.getBillOutStoreId());
				}
			} else {
				throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_PICK_DELETE_STATUS_ERROR);
			}
		}
	}

	/**
	 * 根据出库明细ID删除拣货明细
	 * 
	 * @param billOutStorePickDtlSearchReqDto
	 * @return
	 */
	public void deleteByBillOutStoreDtlId(BillOutStorePickDtlSearchReqDto billOutStorePickDtlSearchReqDto) {
		List<Integer> ids = billOutStorePickDtlSearchReqDto.getIds();
		Integer billOutStoreId = billOutStorePickDtlSearchReqDto.getBillOutStoreId();
		if (!CollectionUtils.isEmpty(ids) && null != billOutStoreId) {
			BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStoreId);
			if (!billOutStore.getStatus().equals(BaseConsts.FIVE)
					&& billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 未发货且未删除
				for (Integer id : ids) {
					BillOutStoreDtl billOutStoreDtl = billOutStoreDtlDao.queryEntityById(id);
					List<BillOutStorePickDtl> pickDtlList = billOutStorePickDtlDao.queryResultsByBillOutStoreDtlId(id);
					for (BillOutStorePickDtl billOutStorePickDtl : pickDtlList) {
						billOutStorePickDtlDao.deleteById(billOutStorePickDtl.getId());
						stlService.releaseStl(billOutStorePickDtl.getStlId(), billOutStorePickDtl.getPickupNum()); // 释放锁定库存
					}
					if (null != billOutStoreDtl) {
						// 更新出库单明细的拣货数量、拣货金额、成本金额、订单金额
						updateBillOutStoreDtlPickInfo(billOutStoreDtl.getId());
					}
				}
				// 更新出库单头的拣货数量和拣货金额
				updateBillOutStorePickInfo(billOutStoreId);
			} else {
				throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_PICK_DELETE_STATUS_ERROR);
			}
		}
	}

	/**
	 * 自动补拣
	 * 
	 * @param billOutStore
	 * @return
	 */
	public void autoPick(BillOutStore billOutStore) {
		billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStore.getId());
		if (!billOutStore.getStatus().equals(BaseConsts.FIVE) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 未发货且未删除
			BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto = new BillOutStoreDtlSearchReqDto();
			billOutStoreDtlSearchReqDto.setBillOutStoreId(billOutStore.getId());
			billOutStoreDtlSearchReqDto.setAvailableFlag(BaseConsts.ONE); // 查询可拣货明细的标识，1-表示发货数量大于已拣货数量的记录
			List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreDtlDao
					.queryResultsByCon(billOutStoreDtlSearchReqDto);
			// 根据出库单明细自动拣货
			autoPickByBillOutStoreDtls(billOutStore, billOutStoreDtlList);
			// 更新出库单头的拣货数量和拣货金额
			updateBillOutStorePickInfo(billOutStore.getId());
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_AUTO_PICK_STATUS_ERROR);
		}
	}

	/**
	 * 根据出库单明细自动拣货
	 * 
	 * @param billOutStore
	 * @param billOutStoreDtlList
	 */
	public void autoPickByBillOutStoreDtls(BillOutStore billOutStore, List<BillOutStoreDtl> billOutStoreDtlList) {
		if (!CollectionUtils.isEmpty(billOutStoreDtlList)) {
			for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtlList) {
				boolean isPickupFlag = matchStl(billOutStore, billOutStoreDtl);
				if (isPickupFlag == false) { // 自动补拣失败
					throw new BaseException(ExcMsgEnum.AUTO_PICK_NUM_NOT_ENOUGH);
				}
				// 更新出库单明细的拣货数量、拣货金额、成本金额、订单金额
				updateBillOutStoreDtlPickInfo(billOutStoreDtl.getId());
			}
		}
	}

	/**
	 * 匹配库存
	 * 
	 * @param billOutStore
	 *            出库单
	 * @param billOutStoreDtl
	 *            出库明细
	 */
	public boolean matchStl(BillOutStore billOutStore, BillOutStoreDtl billOutStoreDtl) {
		boolean isMatch = false; // 库存匹配是否成功
		BillOutStorePickDtl billOutStorePickDtl = new BillOutStorePickDtl();
		BeanUtils.copyProperties(billOutStoreDtl, billOutStorePickDtl);
		billOutStorePickDtl.setId(null);
		billOutStorePickDtl.setBillOutStoreDtlId(billOutStoreDtl.getId());

		BigDecimal sendNum = (null == billOutStoreDtl.getSendNum() ? BigDecimal.ZERO : billOutStoreDtl.getSendNum()); // 发货数量
		BigDecimal pickupNum = (null == billOutStoreDtl.getPickupNum() ? BigDecimal.ZERO
				: billOutStoreDtl.getPickupNum()); // 已拣货数量
		BigDecimal remainNum = DecimalUtil.subtract(sendNum, pickupNum); // 待拣货数量
		BigDecimal matchPickupNum = BigDecimal.ZERO; // 匹配拣货数量

		if (null != billOutStoreDtl.getStlId()) {
			Stl stl = stlDao.queryEntityById(billOutStoreDtl.getStlId());
			BigDecimal availableNum = DecimalUtil.subtract(DecimalUtil.add(stl.getAvailableNum(), sendNum), pickupNum);
			if (DecimalUtil.gt(remainNum, availableNum)) {
				isMatch = false;
				return isMatch;
			}
			BigDecimal outNum = BigDecimal.ZERO;
			outNum = remainNum;
			addBillOutStorePickDtl(billOutStore, billOutStorePickDtl, stl, outNum); // 插入拣货明细，同时锁定库存
			isMatch = true;
		} else {
			StlSearchReqDto stlSearchReqDto = new StlSearchReqDto();
			stlSearchReqDto.setProjectId(billOutStore.getProjectId());
			stlSearchReqDto.setWarehouseId(billOutStore.getWarehouseId());
			stlSearchReqDto.setCurrencyType(billOutStore.getCurrencyType());
			stlSearchReqDto.setGoodsId(billOutStoreDtl.getGoodsId());
			stlSearchReqDto.setBatchNo(billOutStoreDtl.getBatchNo());
			stlSearchReqDto.setGoodsStatus(billOutStoreDtl.getGoodsStatus());
			if (!billOutStore.getBillType().equals(BaseConsts.TWO)) { // 非出库类型
																		// 2-调拨(调拨客户为空)
				stlSearchReqDto.setCustomerId(billOutStore.getCustomerId());
				stlSearchReqDto.setIsCustomerNullFlag(BaseConsts.ONE); // 查询指定客户和客户为空的库存
			}
			List<Stl> stls = stlDao.queryStl4FIFO(stlSearchReqDto); // 按条件查询库存
			if (DecimalUtil.gt(remainNum, BigDecimal.ZERO)) {// 待拣货数量大于0
				for (Stl stl : stls) {
					BigDecimal outNum = BigDecimal.ZERO;
					BigDecimal availableNum = DecimalUtil.subtract(DecimalUtil.add(stl.getAvailableNum(), sendNum),
							pickupNum);
					if (DecimalUtil.ge(availableNum, remainNum)) { // 可用库存大于等于未拣货数量
						outNum = remainNum;
					} else { // 可用库存小于未拣货数量
						outNum = availableNum;
					}
					billOutStorePickDtl.setId(null); // ID重置
					addBillOutStorePickDtl(billOutStore, billOutStorePickDtl, stl, outNum); // 新增拣货明细，同时锁定库存
					matchPickupNum = DecimalUtil.add(matchPickupNum, outNum); // 更新匹配拣货数量

					remainNum = DecimalUtil.subtract(remainNum, outNum); // 更新待拣货数量
					if (DecimalUtil.eq(remainNum, BigDecimal.ZERO)) { // 待拣货数量为0
						break;
					}
				}
			}

			if (DecimalUtil.eq(sendNum, DecimalUtil.add(pickupNum, matchPickupNum))) {
				isMatch = true;
			}
		}
		return isMatch;
	}

	/**
	 * 新增拣货明细，同时锁定库存
	 * 
	 * @param billOutStorePickDtl
	 * @param stl
	 * @param outNum
	 */
	public void addBillOutStorePickDtl(BillOutStore billOutStore, BillOutStorePickDtl billOutStorePickDtl, Stl stl,
			BigDecimal outNum) {
		billOutStorePickDtl.setStlId(stl.getId());
		billOutStorePickDtl.setBatchNo(stl.getBatchNo());
		billOutStorePickDtl.setGoodsStatus(stl.getGoodsStatus());
		billOutStorePickDtl.setAcceptTime(stl.getAcceptTime());
		billOutStorePickDtl.setCostPrice(null == stl.getCostPrice() ? BigDecimal.ZERO : stl.getCostPrice());
		billOutStorePickDtl.setPoPrice(null == stl.getPoPrice() ? BigDecimal.ZERO : stl.getPoPrice());
		billOutStorePickDtl.setCurrencyType(stl.getCurrencyType());
		billOutStorePickDtl.setPickupNum(outNum);
		billOutStorePickDtl.setPoId(stl.getPoId());
		billOutStorePickDtl.setPoDtlId(stl.getPoDtlId());
		billOutStorePickDtl.setReceiveDate(stl.getReceiveDate());
		billOutStorePickDtl.setCustomerId(stl.getCustomerId());
		billOutStorePickDtl.setSupplierId(stl.getSupplierId());
		billOutStorePickDtl.setPayPrice(stl.getPayPrice());
		billOutStorePickDtl.setPayRate(stl.getPayRate() == null ? BigDecimal.ZERO : stl.getPayRate());
		billOutStorePickDtl.setPayTime(stl.getPayTime());
		billOutStorePickDtl.setPayRealCurrency(stl.getPayRealCurrency());
		billOutStorePickDtl.setOriginAcceptTime(stl.getOriginAcceptTime());
		billOutStorePickDtl.setReturnNum(BigDecimal.ZERO);
		billOutStorePickDtlDao.insert(billOutStorePickDtl);
		Integer billType = billOutStore.getBillType();
		if (billType.equals(BaseConsts.ONE) || billType.equals(BaseConsts.FIVE) || billType.equals(BaseConsts.SIX)) { // 1-销售出库、5-采购退货
																														// 6-内部销售
			// 释放库存的销售数量，同时锁定库存的出库数量
			stlService.releaseAndLockStl(stl.getId(), billOutStorePickDtl.getPickupNum(),
					billOutStorePickDtl.getPickupNum());
		} else {
			// 锁定库存出库数量
			stlService.lockStl(stl.getId(), billOutStorePickDtl.getPickupNum());
		}
	}

	/**
	 * 更新出库单头
	 * 
	 * @param billOutStoreId
	 */
	public void updateBillOutStorePickInfo(Integer billOutStoreId) {
		if (null != billOutStoreId) {
			BillOutStore billOutStore = new BillOutStore();
			billOutStore.setId(billOutStoreId);
			BillOutStoreDtl billOutStoreDtlReq = new BillOutStoreDtl();
			billOutStoreDtlReq.setBillOutStoreId(billOutStoreId);
			BillOutStoreDtlSum billOutStoreDtlSum = billOutStoreDtlDao.querySumByBillOutStoreId(billOutStoreDtlReq);

			if (null != billOutStoreDtlSum) {
				billOutStore.setPickupNum(null == billOutStoreDtlSum.getPickupNum() ? BigDecimal.ZERO
						: billOutStoreDtlSum.getPickupNum());
				billOutStore.setPickupAmount(null == billOutStoreDtlSum.getPickupAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billOutStoreDtlSum.getPickupAmount()));
				billOutStore.setCostAmount(null == billOutStoreDtlSum.getCostAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billOutStoreDtlSum.getCostAmount()));
				billOutStore.setPoAmount(null == billOutStoreDtlSum.getPoAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billOutStoreDtlSum.getPoAmount()));
				billOutStoreDao.updateById(billOutStore);
			}
		}
	}

	/**
	 * 更新出库单明细
	 * 
	 * @param billOutStoreDtlId
	 */
	public void updateBillOutStoreDtlPickInfo(Integer billOutStoreDtlId) {
		if (null != billOutStoreDtlId) {
			BillOutStoreDtl billOutStoreDtl = new BillOutStoreDtl();
			billOutStoreDtl.setId(billOutStoreDtlId);
			BillOutStorePickDtl billOutStorePickDtl = new BillOutStorePickDtl();
			billOutStorePickDtl.setBillOutStoreDtlId(billOutStoreDtlId);
			BillOutStorePickDtlSum billOutStorePickDtlSum = billOutStorePickDtlDao
					.querySumByBillOutStoreDtlId(billOutStorePickDtl);

			if (null != billOutStorePickDtlSum) {
				billOutStoreDtl.setPickupNum(null == billOutStorePickDtlSum.getPickupNum() ? BigDecimal.ZERO
						: billOutStorePickDtlSum.getPickupNum());
				billOutStoreDtl.setCostAmount(null == billOutStorePickDtlSum.getCostAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billOutStorePickDtlSum.getCostAmount()));
				billOutStoreDtl.setPoAmount(null == billOutStorePickDtlSum.getPoAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billOutStorePickDtlSum.getPoAmount()));
				billOutStoreDtlDao.updateById(billOutStoreDtl);
			}
		}
	}

	public List<BillOutStoreTaxGroupCostPrice> queryCostPriceByBillOutStoreId(Integer billOutStoreId) {
		return billOutStorePickDtlDao.queryCostPriceByBillOutStoreId(billOutStoreId);
	}

	public BigDecimal queryCostAmountByOutStoreId(Integer billOutStoreId) {
		return billOutStorePickDtlDao.queryCostPriceByOutStoreId(billOutStoreId);
	}

	/**
	 * 根据出库单ID查询拣货详情
	 * 
	 * @param billOutStoreId
	 * @return
	 */
	public List<BillOutStoreTaxGroupCostPrice> queryPickDtlByOutId(Integer billOutStoreId) {
		return billOutStorePickDtlDao.queryPickDtlByOutId(billOutStoreId);
	}

	/**
	 * 根据单据查询条件查询出库单拣货明细(含出库单信息)
	 * 
	 * @param billInStoreDtlSearchReqDto
	 * @return
	 */
	public List<BillOutStorePickDtlExtResDto> queryAllBillOutStorePickDtlExtList(
			BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		if (null == billOutStoreSearchReqDto.getUserId()) {
			billOutStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<BillOutStorePickDtlExt> billOutStorePickDtlExtList = billOutStorePickDtlDao
				.queryResultsByBillOutStoreCon(billOutStoreSearchReqDto);
		List<BillOutStorePickDtlExtResDto> billOutStorePickDtlExtResDtoList = convertToExtResDto(
				billOutStorePickDtlExtList);

		return billOutStorePickDtlExtResDtoList;
	}

	/**
	 * 对象转换扩展
	 * 
	 * @param billInStoreDtlExtList
	 * @return
	 */
	private List<BillOutStorePickDtlExtResDto> convertToExtResDto(
			List<BillOutStorePickDtlExt> billOutStorePickDtlExtList) {
		List<BillOutStorePickDtlExtResDto> billOutStorePickDtlExtResDtoList = new ArrayList<BillOutStorePickDtlExtResDto>(
				5);
		if (CollectionUtils.isEmpty(billOutStorePickDtlExtList)) {
			return billOutStorePickDtlExtResDtoList;
		}
		for (BillOutStorePickDtlExt billOutStorePickDtlExt : billOutStorePickDtlExtList) {
			BillOutStorePickDtlExtResDto billOutStorePickDtlExtResDto = convertToExtResDto(billOutStorePickDtlExt);
			billOutStorePickDtlExtResDtoList.add(billOutStorePickDtlExtResDto);
		}
		return billOutStorePickDtlExtResDtoList;
	}

	private BillOutStorePickDtlExtResDto convertToExtResDto(BillOutStorePickDtlExt billOutStorePickDtlExt) {
		BillOutStorePickDtlExtResDto billOutStorePickDtlExtResDto = new BillOutStorePickDtlExtResDto();
		if (null != billOutStorePickDtlExt) {
			BeanUtils.copyProperties(billOutStorePickDtlExt, billOutStorePickDtlExtResDto);
			if (billOutStorePickDtlExt.getGoodsStatus() != null) {
				billOutStorePickDtlExtResDto
						.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
								Integer.toString(billOutStorePickDtlExt.getGoodsStatus())));
			}
			if (billOutStorePickDtlExt.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billOutStorePickDtlExt.getGoodsId());
				if (null != baseGoods) {
					billOutStorePickDtlExtResDto.setGoodsName(baseGoods.getName());
					billOutStorePickDtlExtResDto.setGoodsNumber(baseGoods.getNumber());
					billOutStorePickDtlExtResDto.setGoodsType(baseGoods.getType());
					billOutStorePickDtlExtResDto.setGoodsBarCode(baseGoods.getBarCode());
				}
			}

			// 出库单信息
			if (billOutStorePickDtlExt.gettStatus() != null) {
				billOutStorePickDtlExtResDto.settStatusName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_OUT_STORE_STATUS, Integer.toString(billOutStorePickDtlExt.gettStatus())));
			}
			if (billOutStorePickDtlExt.gettBillType() != null) {
				billOutStorePickDtlExtResDto.settBillTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_OUT_STORE_TYPE, Integer.toString(billOutStorePickDtlExt.gettBillType())));
			}
			if (billOutStorePickDtlExt.gettTransferMode() != null) {
				billOutStorePickDtlExtResDto.settTransferModeName(
						ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_OUT_STORE_TRANSFER_MODE,
								Integer.toString(billOutStorePickDtlExt.gettTransferMode())));
			}
			if (billOutStorePickDtlExt.gettReasonType() != null) {
				billOutStorePickDtlExtResDto.settReasonName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.OUT_REASON_TYPE, Integer.toString(billOutStorePickDtlExt.gettReasonType())));
			}
			if (billOutStorePickDtlExt.gettCurrencyType() != null) {
				billOutStorePickDtlExtResDto
						.settCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
								Integer.toString(billOutStorePickDtlExt.gettCurrencyType())));
			}

			billOutStorePickDtlExtResDto
					.settProjectName(cacheService.showProjectNameById(billOutStorePickDtlExt.gettProjectId()));
			billOutStorePickDtlExtResDto.settWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billOutStorePickDtlExt.gettWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billOutStorePickDtlExtResDto.settReceiveWarehouseName(cacheService.showSubjectNameByIdAndKey(
					billOutStorePickDtlExt.gettReceiveWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billOutStorePickDtlExtResDto.settCustomerName(cacheService
					.showSubjectNameByIdAndKey(billOutStorePickDtlExt.gettCustomerId(), CacheKeyConsts.CUSTOMER));

			BaseAddress baseAddress = cacheService.getAddressById(billOutStorePickDtlExt.gettCustomerAddressId());
			if (null != baseAddress) {
				billOutStorePickDtlExtResDto.settCustomerAddress(baseAddress.getShowValue());
			}
		}
		return billOutStorePickDtlExtResDto;
	}

	private List<BillOutStorePickDtlResDto> convertToResDto(List<BillOutStorePickDtl> billOutStorePickDtlList) {
		List<BillOutStorePickDtlResDto> pickDtlResDtoList = new ArrayList<BillOutStorePickDtlResDto>(5);
		if (!CollectionUtils.isEmpty(billOutStorePickDtlList)) {
			for (BillOutStorePickDtl billOutStorePickDtl : billOutStorePickDtlList) {
				BillOutStorePickDtlResDto billOutStorePickDtlResDto = convertToPickResDto(billOutStorePickDtl);
				pickDtlResDtoList.add(billOutStorePickDtlResDto);
			}
		} else {
			return null;
		}
		return pickDtlResDtoList;
	}

	private BillOutStorePickDtlResDto convertToPickResDto(BillOutStorePickDtl billOutStorePickDtl) {
		BillOutStorePickDtlResDto billOutStorePickDtlResDto = new BillOutStorePickDtlResDto();
		if (null != billOutStorePickDtl) {
			BeanUtils.copyProperties(billOutStorePickDtl, billOutStorePickDtlResDto);
			BaseGoods baseGoods = cacheService.getGoodsById(billOutStorePickDtlResDto.getGoodsId());
			if (null != baseGoods) {
				billOutStorePickDtlResDto.setGoodsName(baseGoods.getName());
				billOutStorePickDtlResDto.setGoodsNumber(baseGoods.getNumber());
				billOutStorePickDtlResDto.setGoodsType(baseGoods.getType());
				billOutStorePickDtlResDto.setGoodsUnit(baseGoods.getUnit());
				billOutStorePickDtlResDto.setGoodsBarCode(baseGoods.getBarCode());
			}
			if (billOutStorePickDtl.getGoodsStatus() != null) {
				billOutStorePickDtlResDto
						.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
								Integer.toString(billOutStorePickDtl.getGoodsStatus())));
			}
		}
		return billOutStorePickDtlResDto;
	}

	public boolean isOverBillOutStorePickDtlMaxLine(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		billOutStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billOutStorePickDtlDao.queryCountByBillOutStoreCon(billOutStoreSearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("出库单单据拣货明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillOutStorePickDtlExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_out_store_pick_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_14);
			asyncExcelService.addAsyncExcel(billOutStoreSearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillOutStorePickDtlExport(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillOutStorePickDtlExtResDto> billOutStorePickDtlExtResDtoList = queryAllBillOutStorePickDtlExtList(
				billOutStoreSearchReqDto);
		model.put("billOutStorePickDtlList", billOutStorePickDtlExtResDtoList);
		return model;
	}
}
