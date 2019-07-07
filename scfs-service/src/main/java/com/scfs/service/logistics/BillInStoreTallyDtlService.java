package com.scfs.service.logistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.dao.logistics.BillInStoreTallyDtlDao;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.logistics.dto.req.BillInStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillInStoreTallyDtlSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillInStoreTallyDtlExtResDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreDtlSum;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtlExcel;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtlExt;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtlSum;
import com.scfs.domain.po.entity.PurchaseReturnDtl;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月17日.
 */
@Service
public class BillInStoreTallyDtlService {
	@Value("${billInStoreTallyDtl.import.xmlConfig}")
	private String billInStoreTallyDtlExcelConfig;
	@Autowired
	private BillInStoreTallyDtlDao billInStoreTallyDtlDao;
	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	/**
	 * 新增理货明细
	 * 
	 * @param billInStoreDtl
	 * @return
	 */
	public void addBillInStoreTallyDtls(BillInStoreDtl billInStoreDtl) {
		BillInStore billInStore = billInStoreDao.queryAndLockEntityById(billInStoreDtl.getBillInStoreId());
		if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
			List<BillInStoreTallyDtl> billInStoreTallyDtlList = billInStoreDtl.getBillInStoreTallyDtlList();
			BigDecimal tallyNum = BigDecimal.ZERO;
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto = new BillInStoreDtlSearchReqDto();
			billInStoreDtlSearchReqDto.setId(billInStoreDtl.getId());
			BillInStoreDtl billInStoreDtlRes = billInStoreDtlDao.queryById(billInStoreDtlSearchReqDto);
			for (BillInStoreTallyDtl billInStoreTallyDtl : billInStoreTallyDtlList) {
				billInStoreTallyDtl.setBillInStoreId(billInStoreDtlRes.getBillInStoreId());
				billInStoreTallyDtl.setBillInStoreDtlId(billInStoreDtlRes.getId());
				billInStoreTallyDtl.setPoId(billInStoreDtlRes.getPoId());
				billInStoreTallyDtl.setPoDtlId(billInStoreDtlRes.getPoDtlId());
				billInStoreTallyDtl.setGoodsId(billInStoreDtlRes.getGoodsId());
				billInStoreTallyDtl.setReceivePrice(null == billInStoreDtlRes.getReceivePrice() ? BigDecimal.ZERO
						: billInStoreDtlRes.getReceivePrice());
				billInStoreTallyDtl.setPoPrice(
						null == billInStoreDtlRes.getPoPrice() ? BigDecimal.ZERO : billInStoreDtlRes.getPoPrice());
				billInStoreTallyDtl.setCostPrice(
						null == billInStoreDtlRes.getCostPrice() ? BigDecimal.ZERO : billInStoreDtlRes.getCostPrice());
				billInStoreTallyDtl.setCreator(ServiceSupport.getUser().getChineseName());
				billInStoreTallyDtl.setCreatorId(ServiceSupport.getUser().getId());
				billInStoreTallyDtl.setCurrencyType(billInStore.getCurrencyType());
				billInStoreTallyDtl.setReceiveDate(billInStoreDtlRes.getReceiveDate());
				billInStoreTallyDtl.setCustomerId(billInStoreDtlRes.getCustomerId());
				billInStoreTallyDtl.setSupplierId(billInStoreDtlRes.getSupplierId());
				billInStoreTallyDtl.setPayPrice(billInStoreDtlRes.getPayPrice());
				billInStoreTallyDtl.setPayRate(
						billInStoreDtlRes.getPayRate() == null ? BigDecimal.ZERO : billInStoreDtlRes.getPayRate());
				billInStoreTallyDtl.setPayRealCurrency(billInStoreDtlRes.getPayRealCurrency());
				billInStoreTallyDtl.setPayTime(billInStoreDtlRes.getPayTime());
				tallyNum = tallyNum.add(null == billInStoreTallyDtl.getTallyNum() ? BigDecimal.ZERO
						: billInStoreTallyDtl.getTallyNum());
			}
			BigDecimal unTallyNum = (null == billInStoreDtlRes.getUnTallyNum() ? BigDecimal.ZERO
					: billInStoreDtlRes.getUnTallyNum());
			if (tallyNum.compareTo(unTallyNum) > 0) {
				throw new BaseException(ExcMsgEnum.AVAILABLE_TALLY_NUM_NOT_ENOUGH);
			}
			for (BillInStoreTallyDtl billInStoreTallyDtl : billInStoreTallyDtlList) {
				// 新增理货明细
				billInStoreTallyDtlDao.insert(billInStoreTallyDtl);
			}
			// 更新入库单明细的理货数量和理货金额
			updateBillInStoreDtlTallyInfo(billInStoreDtlRes.getId());
			// 更新入库单头的理货数量和理货金额
			updateBillInStoreTallyInfo(billInStoreDtlRes.getBillInStoreId());
		} else {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_TALLY_ADD_STATUS_ERROR);
		}
	}

	/**
	 * 删除理货明细
	 * 
	 * @param billInStoreTallyDtl
	 * @return
	 */
	public void deleteById(BillInStoreTallyDtl billInStoreTallyDtl) {
		Integer id = billInStoreTallyDtl.getId();
		Integer billInStoreId = billInStoreTallyDtl.getBillInStoreId();
		if (null != id && null != billInStoreId) {
			BillInStore billInStore = billInStoreDao.queryAndLockEntityById(billInStoreId);
			if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
				BillInStoreTallyDtl tallyDtl = billInStoreTallyDtlDao.queryEntityById(id);
				billInStoreTallyDtlDao.deleteById(id);
				if (null != tallyDtl) {
					// 更新入库单明细的理货数量和理货金额
					updateBillInStoreDtlTallyInfo(tallyDtl.getBillInStoreDtlId());
					// 更新入库单头的理货数量和理货金额
					updateBillInStoreTallyInfo(tallyDtl.getBillInStoreId());
				}
			} else {
				throw new BaseException(ExcMsgEnum.BILL_IN_STORE_TALLY_DELETE_STATUS_ERROR);
			}
		}
	}

	/**
	 * 根据入库单明细ID删除理货明细
	 * 
	 * @param billInStoreTallyDtlSearchReqDto
	 * @return
	 */
	public void deleteByBillInStoreDtlIds(BillInStoreTallyDtlSearchReqDto billInStoreTallyDtlSearchReqDto) {
		List<Integer> ids = billInStoreTallyDtlSearchReqDto.getIds();
		Integer billInStoreId = billInStoreTallyDtlSearchReqDto.getBillInStoreId();
		if (!CollectionUtils.isEmpty(ids) && null != billInStoreId) {
			BillInStore billInStore = billInStoreDao.queryAndLockEntityById(billInStoreId);
			if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
				for (Integer id : ids) {
					BillInStoreDtl billInStoreDtl = billInStoreDtlDao.queryEntityById(id);
					billInStoreTallyDtlDao.deleteByBillInStoreDtlId(id);
					if (null != billInStoreDtl) {
						// 更新入库单明细的理货数量和理货金额
						updateBillInStoreDtlTallyInfo(id);
					}
				}
				// 更新入库单头的理货数量和理货金额
				updateBillInStoreTallyInfo(billInStoreId);
			} else {
				throw new BaseException(ExcMsgEnum.BILL_IN_STORE_TALLY_DELETE_STATUS_ERROR);
			}
		}
	}

	/**
	 * 自动理货
	 * 
	 * @param billInStore
	 * @return
	 */
	public void autoTally(BillInStore billInStore) {
		billInStore = billInStoreDao.queryAndLockEntityById(billInStore.getId());
		if (billInStore.getStatus().equals(BaseConsts.ONE) && billInStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
			BillInStoreDtlSearchReqDto billInStoreDtlSearchReqDto = new BillInStoreDtlSearchReqDto();
			billInStoreDtlSearchReqDto.setBillInStoreId(billInStore.getId());
			billInStoreDtlSearchReqDto.setAvailableFlag(BaseConsts.ONE); // 收货数量大于已理货数量的记录
			List<BillInStoreDtl> billInStoreDtlList = billInStoreDtlDao.queryResultsByCon(billInStoreDtlSearchReqDto);
			if (!CollectionUtils.isEmpty(billInStoreDtlList)) {
				for (BillInStoreDtl billInStoreDtl : billInStoreDtlList) {
					autoTallyBillInStoreDtl(billInStoreDtl);
				}
			}
			// 更新入库单头的理货数量和理货金额
			updateBillInStoreTallyInfo(billInStore.getId());
		} else {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_AUTO_TALLY_STATUS_ERROR);
		}
	}

	/**
	 * 理货单条收货明细
	 * 
	 * @param billInStoreDtl
	 */
	public void autoTallyBillInStoreDtl(BillInStoreDtl billInStoreDtl) {
		BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
		BeanUtils.copyProperties(billInStoreDtl, billInStoreTallyDtl);
		billInStoreTallyDtl.setId(null);
		billInStoreTallyDtl.setBatchNo(billInStoreDtl.getBatchNo());
		billInStoreTallyDtl.setGoodsStatus(BaseConsts.ONE);
		billInStoreTallyDtl.setBillInStoreDtlId(billInStoreDtl.getId());
		BigDecimal receiveNum = (null == billInStoreDtl.getReceiveNum() ? BigDecimal.ZERO
				: billInStoreDtl.getReceiveNum());
		BigDecimal tallyNum = (null == billInStoreDtl.getTallyNum() ? BigDecimal.ZERO : billInStoreDtl.getTallyNum());
		billInStoreTallyDtl.setTallyNum(receiveNum.subtract(tallyNum));
		billInStoreTallyDtl.setCurrencyType(billInStoreDtl.getCurrencyType());
		billInStoreTallyDtl.setReceiveDate(billInStoreDtl.getReceiveDate());
		billInStoreTallyDtl.setCustomerId(billInStoreDtl.getCustomerId());
		billInStoreTallyDtl.setSupplierId(billInStoreDtl.getSupplierId());
		billInStoreTallyDtl.setPayPrice(billInStoreDtl.getPayPrice());
		billInStoreTallyDtl.setPayTime(billInStoreDtl.getPayTime());
		billInStoreTallyDtl
				.setPayRate(billInStoreDtl.getPayRate() == null ? BigDecimal.ZERO : billInStoreDtl.getPayRate());
		billInStoreTallyDtl.setPayRealCurrency(billInStoreDtl.getPayRealCurrency());
		billInStoreTallyDtlDao.insert(billInStoreTallyDtl);
		// 更新入库单明细的理货数量和理货金额
		updateBillInStoreDtlTallyInfo(billInStoreDtl.getId());
	}

	/**
	 * 更新入库单头
	 * 
	 * @param billInStoreId
	 */
	private void updateBillInStoreTallyInfo(Integer billInStoreId) {
		if (null != billInStoreId) {
			BillInStore billInStore = new BillInStore();
			billInStore.setId(billInStoreId);
			BillInStoreDtl billInStoreDtlReq = new BillInStoreDtl();
			billInStoreDtlReq.setBillInStoreId(billInStoreId);
			BillInStoreDtlSum billInStoreDtlSum = billInStoreDtlDao.querySumByBillInStoreId(billInStoreDtlReq);

			if (null != billInStoreDtlSum) {
				billInStore.setTallyNum(
						null == billInStoreDtlSum.getTallyNum() ? BigDecimal.ZERO : billInStoreDtlSum.getTallyNum());
				billInStore.setTallyAmount(null == billInStoreDtlSum.getTallyAmount() ? BigDecimal.ZERO
						: DecimalUtil.formatScale2(billInStoreDtlSum.getTallyAmount()));
				billInStoreDao.updateById(billInStore);
			}
		}
	}

	/**
	 * 更新入库单明细
	 * 
	 * @param billInStoreDtlId
	 */
	public void updateBillInStoreDtlTallyInfo(Integer billInStoreDtlId) {
		if (null != billInStoreDtlId) {
			BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
			billInStoreDtl.setId(billInStoreDtlId);
			BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
			billInStoreTallyDtl.setBillInStoreDtlId(billInStoreDtlId);
			BillInStoreTallyDtlSum billInStoreTallyDtlSum = billInStoreTallyDtlDao
					.querySumByBillInStoreDtlId(billInStoreTallyDtl);

			if (null != billInStoreTallyDtlSum) {
				billInStoreDtl.setTallyNum(null == billInStoreTallyDtlSum.getTallyNum() ? BigDecimal.ZERO
						: billInStoreTallyDtlSum.getTallyNum());
				billInStoreDtlDao.updateById(billInStoreDtl);
			}
		}
	}

	/**
	 * 根据单据条件查询理货明细(含入库单信息)
	 * 
	 * @param billInStoreSearchReqDto
	 * @return
	 */
	public List<BillInStoreTallyDtlExtResDto> queryAllBillInStoreTallyDtlExtList(
			BillInStoreSearchReqDto billInStoreSearchReqDto) {
		if (null == billInStoreSearchReqDto.getUserId()) {
			billInStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<BillInStoreTallyDtlExt> billInStoreTallyDtlExtList = billInStoreTallyDtlDao
				.queryResultsByBillInStoreCon(billInStoreSearchReqDto);
		List<BillInStoreTallyDtlExtResDto> billInStoreTallyDtlExtResDtoList = convertToExtResDto(
				billInStoreTallyDtlExtList);

		return billInStoreTallyDtlExtResDtoList;
	}

	/**
	 * 对象转换扩展
	 * 
	 * @param billInStoreDtlExtList
	 * @return
	 */
	private List<BillInStoreTallyDtlExtResDto> convertToExtResDto(
			List<BillInStoreTallyDtlExt> billInStoreTallyDtlExtList) {
		List<BillInStoreTallyDtlExtResDto> billInStoreTallyDtlExtResDtoList = new ArrayList<BillInStoreTallyDtlExtResDto>(
				5);
		if (CollectionUtils.isEmpty(billInStoreTallyDtlExtList)) {
			return billInStoreTallyDtlExtResDtoList;
		}
		for (BillInStoreTallyDtlExt billInStoreTallyDtlExt : billInStoreTallyDtlExtList) {
			BillInStoreTallyDtlExtResDto billInStoreTallyDtlExtResDto = convertToExtResDto(billInStoreTallyDtlExt);
			billInStoreTallyDtlExtResDtoList.add(billInStoreTallyDtlExtResDto);
		}
		return billInStoreTallyDtlExtResDtoList;
	}

	private BillInStoreTallyDtlExtResDto convertToExtResDto(BillInStoreTallyDtlExt billInStoreTallyDtlExt) {
		BillInStoreTallyDtlExtResDto billInStoreTallyDtlExtResDto = new BillInStoreTallyDtlExtResDto();
		if (null != billInStoreTallyDtlExt) {
			BeanUtils.copyProperties(billInStoreTallyDtlExt, billInStoreTallyDtlExtResDto);
			if (billInStoreTallyDtlExtResDto.getGoodsId() != null) {
				BaseGoods baseGoods = cacheService.getGoodsById(billInStoreTallyDtlExtResDto.getGoodsId());
				if (null != baseGoods) {
					billInStoreTallyDtlExtResDto.setGoodsName(baseGoods.getName());
					billInStoreTallyDtlExtResDto.setGoodsNumber(baseGoods.getNumber());
					billInStoreTallyDtlExtResDto.setGoodsType(baseGoods.getType());
					billInStoreTallyDtlExtResDto.setGoodsBarCode(baseGoods.getBarCode());
				}
			}
			if (billInStoreTallyDtlExt.getGoodsStatus() != null) {
				billInStoreTallyDtlExtResDto
						.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
								Integer.toString(billInStoreTallyDtlExt.getGoodsStatus())));
			}

			// 入库单信息
			if (billInStoreTallyDtlExt.gettStatus() != null) {
				billInStoreTallyDtlExtResDto.settStatusName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_IN_STORE_STATUS, Integer.toString(billInStoreTallyDtlExt.gettStatus())));
			}
			if (billInStoreTallyDtlExt.gettBillType() != null) {
				billInStoreTallyDtlExtResDto.settBillTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_IN_STORE_TYPE, Integer.toString(billInStoreTallyDtlExt.gettBillType())));
			}
			if (billInStoreTallyDtlExt.getCurrencyType() != null) {
				billInStoreTallyDtlExtResDto
						.settCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
								Integer.toString(billInStoreTallyDtlExt.getCurrencyType())));
			}

			billInStoreTallyDtlExtResDto
					.settProjectName(cacheService.showProjectNameById(billInStoreTallyDtlExt.gettProjectId()));
			billInStoreTallyDtlExtResDto.settSupplierName(cacheService
					.showSubjectNameByIdAndKey(billInStoreTallyDtlExt.gettSupplierId(), CacheKeyConsts.SUPPLIER));
			billInStoreTallyDtlExtResDto.settWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billInStoreTallyDtlExt.gettWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billInStoreTallyDtlExtResDto.settCustomerName(cacheService
					.showSubjectNameByIdAndKey(billInStoreTallyDtlExt.gettCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		return billInStoreTallyDtlExtResDto;
	}

	/**
	 * 导入入库单理货明细Excel
	 * 
	 * @param importFile
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void importBillInStoreTallyDtlExcel(BillInStoreTallyDtlSearchReqDto billInStoreTallyDtlSearchReqDto,
			MultipartFile importFile) {
		List<BillInStoreTallyDtlExcel> billInStoreTallyDtlExcelList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("billInStoreTallyDtlExcelList", billInStoreTallyDtlExcelList);
		ExcelService.resolverExcel(importFile, "/excel/logistics/billInStore/billInStoreTallyDtl.xml", beans);
		// 业务逻辑处理
		billInStoreTallyDtlExcelList = (List<BillInStoreTallyDtlExcel>) beans.get("billInStoreTallyDtlExcelList");
		if (!CollectionUtils.isEmpty(billInStoreTallyDtlExcelList)) {
			if (billInStoreTallyDtlExcelList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			Map<Integer, List<BillInStoreTallyDtl>> map = Maps.newHashMap();
			for (BillInStoreTallyDtlExcel billInStoreTallyDtlExcel : billInStoreTallyDtlExcelList) {
				if (null == billInStoreTallyDtlExcel.getId()) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收货明细ID不能为空");
				}

				String orderNo = (null == billInStoreTallyDtlExcel.getOrderNo() ? ""
						: billInStoreTallyDtlExcel.getOrderNo().trim());
				if (StringUtils.isBlank(orderNo)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单编号不能为空");
				}
				billInStoreTallyDtlExcel.setOrderNo(orderNo);

				String appendNo = (null == billInStoreTallyDtlExcel.getAppendNo() ? ""
						: billInStoreTallyDtlExcel.getAppendNo().trim());
				if (StringUtils.isBlank(appendNo)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单附属编号不能为空");
				}
				billInStoreTallyDtlExcel.setAppendNo(appendNo);

				String goodsNumber = (null == billInStoreTallyDtlExcel.getGoodsNumber() ? ""
						: billInStoreTallyDtlExcel.getGoodsNumber().trim());
				if (StringUtils.isBlank(goodsNumber)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品编号不能为空");
				}
				billInStoreTallyDtlExcel.setGoodsNumber(goodsNumber);

				String receiveNumStr = billInStoreTallyDtlExcel.getReceiveNum();
				if (StringUtils.isBlank(receiveNumStr)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收货数量不能为空");
				}
				BigDecimal receiveNum = BigDecimal.ZERO;
				try {
					receiveNum = new BigDecimal(receiveNumStr);
				} catch (Exception e) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收货数量格式错误");
				}
				if (receiveNum.compareTo(BigDecimal.ZERO) <= 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "收货数量必须大于零");
				}

				String unTallyNumStr = billInStoreTallyDtlExcel.getUnTallyNum();
				if (StringUtils.isBlank(unTallyNumStr)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "可理货数量不能为空");
				}
				BigDecimal unTallyNum = BigDecimal.ZERO;
				try {
					unTallyNum = new BigDecimal(unTallyNumStr);
				} catch (Exception e) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "可理货数量格式错误");
				}
				if (unTallyNum.compareTo(BigDecimal.ZERO) <= 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "可理货数量必须大于零");
				}

				String tallyNumStr = billInStoreTallyDtlExcel.getTallyNum();
				if (StringUtils.isBlank(tallyNumStr)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "理货数量不能为空");
				}
				BigDecimal tallyNum = BigDecimal.ZERO;
				try {
					tallyNum = new BigDecimal(tallyNumStr);
				} catch (Exception e) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "理货数量格式错误");
				}
				if (tallyNum.compareTo(BigDecimal.ZERO) <= 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "理货数量必须大于零");
				}

				String goodsStatusName = (null == billInStoreTallyDtlExcel.getGoodsStatusName() ? ""
						: billInStoreTallyDtlExcel.getGoodsStatusName().trim());
				if (StringUtils.isBlank(goodsStatusName)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "库存状态不能为空");
				}
				billInStoreTallyDtlExcel.setGoodsStatusName(goodsStatusName);
				String goodsStatus = ServiceSupport.getCodeByBizValue(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
						goodsStatusName);
				if (StringUtils.isBlank(goodsStatus)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "库存状态【" + goodsStatusName + "】不存在");
				}

				String batchNo = (null == billInStoreTallyDtlExcel.getBatchNo() ? ""
						: billInStoreTallyDtlExcel.getBatchNo().trim());
				Integer billInStoreDtlId = billInStoreTallyDtlExcel.getId();
				BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
				billInStoreTallyDtl.setBillInStoreDtlId(billInStoreDtlId);
				billInStoreTallyDtl.setTallyNum(tallyNum);
				billInStoreTallyDtl.setGoodsStatus(Integer.parseInt(goodsStatus));
				billInStoreTallyDtl.setBatchNo(batchNo);
				if (map.containsKey(billInStoreDtlId)) {
					List<BillInStoreTallyDtl> billInStoreTallyDtlList = map.get(billInStoreDtlId);
					billInStoreTallyDtlList.add(billInStoreTallyDtl);
					map.put(billInStoreDtlId, billInStoreTallyDtlList);
				} else {
					List<BillInStoreTallyDtl> billInStoreTallyDtlList = new ArrayList<BillInStoreTallyDtl>();
					billInStoreTallyDtlList.add(billInStoreTallyDtl);
					map.put(billInStoreDtlId, billInStoreTallyDtlList);
				}
			}
			Iterator<Entry<Integer, List<BillInStoreTallyDtl>>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Integer, List<BillInStoreTallyDtl>> entry = iterator.next();
				Integer billInStoreId = entry.getKey();
				List<BillInStoreTallyDtl> billInStoreTallyDtlList = entry.getValue();
				BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
				billInStoreDtl.setId(billInStoreId);
				billInStoreDtl.setBillInStoreId(billInStoreTallyDtlSearchReqDto.getBillInStoreId());
				billInStoreDtl.setBillInStoreTallyDtlList(billInStoreTallyDtlList);
				addBillInStoreTallyDtls(billInStoreDtl);
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入理货明细不能为空");
		}
	}

	public BigDecimal querySumByBillInStoreId(Integer billInStoreId) {
		return billInStoreDtlDao.querySumAmountByBillInStoreId(billInStoreId);
	}

	public boolean isOverBillInStoreTallyDtlMaxLine(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		billInStoreSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billInStoreTallyDtlDao.queryCountByBillInStoreCon(billInStoreSearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("入库单单据理货明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillInStoreTallyDtlExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_in_store_tally_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_12);
			asyncExcelService.addAsyncExcel(billInStoreSearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillInStoreTallyDtlExport(BillInStoreSearchReqDto billInStoreSearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillInStoreTallyDtlExtResDto> billInStoreTallyDtlExtResDtoList = queryAllBillInStoreTallyDtlExtList(
				billInStoreSearchReqDto);
		model.put("billInStoreTallyDtlList", billInStoreTallyDtlExtResDtoList);
		return model;
	}

	// 更新入库单明细的退货数量
	public void updateBillInStoreTally(PurchaseReturnDtl purchaseReturnDtl) {
		BillInStoreTallyDtl billInStoreTallyDtl = billInStoreTallyDtlDao
				.queryEntityById(purchaseReturnDtl.getBillInStoreTallyDtlId());
		BigDecimal tallyNum = billInStoreTallyDtl.getTallyNum();
		BigDecimal returnNum = (billInStoreTallyDtl.getReturnNum() == null ? BigDecimal.ZERO
				: billInStoreTallyDtl.getReturnNum()).add(purchaseReturnDtl.getReturnNum());
		if (DecimalUtil.gt(returnNum, tallyNum)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "当前退货数量大于理货数量");
		}

		billInStoreTallyDtl.setId(purchaseReturnDtl.getBillInStoreTallyDtlId());
		billInStoreTallyDtl.setReturnNum(returnNum);
		billInStoreTallyDtlDao.updateById(billInStoreTallyDtl);
	}
}
