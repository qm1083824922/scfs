package com.scfs.service.logistics;

import static com.scfs.service.support.ServiceSupport.getUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.api.pms.PmsPayDao;
import com.scfs.dao.base.entity.BaseAddressDao;
import com.scfs.dao.base.entity.BaseSubjectDao;
import com.scfs.dao.base.entity.BaseUserSubjectDao;
import com.scfs.dao.fi.ReceiptOutStoreRelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.interf.PmsPayOrderTitleDao;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.dao.logistics.BillInStoreTallyDtlDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.BillOutStorePickDtlDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.api.pms.entity.PmsPay;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.dto.req.ReceiptOutRelReqDto;
import com.scfs.domain.fi.dto.req.VoucherLineSearchReqDto;
import com.scfs.domain.fi.dto.resp.ReceiptOutRelResDto;
import com.scfs.domain.fi.dto.resp.VoucherLineModelResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.RecDetail;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.ReceiptOutStoreRel;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreFileResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreResDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.logistics.entity.BillOutStoreSum;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectPoolAsset;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillOutStoreDetailSearchReqDto;
import com.scfs.domain.sale.dto.resp.BillOutStoreDetailResDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.service.audit.BillOutStoreAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.bookkeeping.OutStoreBookkeepingService;
import com.scfs.service.bookkeeping.OutStorePurchaseReturnService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.fi.ReceiptPoolAssestService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.fi.VoucherLineService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月17日.
 */
@Service
public class BillOutStoreService {
	private final static Logger LOGGER = LoggerFactory.getLogger(BillOutStoreService.class);

	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private BillOutStorePickDtlDao billOutStorePickDtlDao;
	@Autowired
	private StlDao stlDao;
	@Autowired
	private BillDeliveryDao billDeliveryDao;
	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	private BaseSubjectDao baseSubjectDao;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	private BillInStoreTallyDtlDao billInStoreTallyDtlDao;
	@Autowired
	private BaseAddressDao baseAddressDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private BillOutStoreAuditService billOutStoreAuditService;
	@Autowired
	private BillInStoreService billInStoreService;
	@Autowired
	private OutStoreBookkeepingService outStoreBookkeepingService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private VoucherLineService voucherLineService;
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private BillDeliveryService billDeliveryService;
	@Autowired
	private VerificationAdvanceService verificationAdvanceService;
	@Autowired
	private PmsPayOrderTitleDao pmsPayOrderTitleDao;
	@Autowired
	private BillOutStorePickDtlService billOutStorePickDtlService;
	@Autowired
	private BillOutStoreDtlService billOutStoreDtlService;
	@Autowired
	private StlService stlService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private ProjectPoolService projectPoolService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private OutStorePurchaseReturnService purchaseReturnService;
	@Autowired
	private BaseUserSubjectDao baseUserSubjectDao;
	@Autowired
	private ReceiptOutStoreRelDao receiptOutStoreRelDao;
	@Autowired
	private ReceiptPoolAssestService poolAssestService;
	@Autowired
	private AuditFlowService auditFlowService;
	@Autowired
	private PmsPayDao pmsPayDao;

	/**
	 * 查询出库单列表
	 * 
	 * @param billOutStoreSearchReqDto
	 * @return
	 */
	public PageResult<BillOutStoreResDto> queryBillOutStoreList(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		PageResult<BillOutStoreResDto> result = new PageResult<BillOutStoreResDto>();

		int offSet = PageUtil.getOffSet(billOutStoreSearchReqDto.getPage(), billOutStoreSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, billOutStoreSearchReqDto.getPer_page());

		billOutStoreSearchReqDto.setUserId(getUser().getId());
		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		List<BillOutStore> billOutStoreList = null;
		if (!CollectionUtils.isEmpty(userSubject)) {
			billOutStoreSearchReqDto.setUserSubject(userSubject);
			billOutStoreList = billOutStoreDao.queryResultsByCon(billOutStoreSearchReqDto, rowBounds);
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_OUT_STORE);
		List<BillOutStoreResDto> billOutStoreResDtoList = convertToResDto(billOutStoreList, isAllowPerm);
		result.setItems(billOutStoreResDtoList);
		String totalStr = querySumBillOutStore(billOutStoreSearchReqDto, isAllowPerm);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), billOutStoreSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(billOutStoreSearchReqDto.getPage());
		result.setPer_page(billOutStoreSearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 查询出库单列表(不分页)
	 * 
	 * @param billOutStoreSearchReqDto
	 * @return
	 */
	public List<BillOutStoreResDto> queryAllBillOutStoreList(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		if (null == billOutStoreSearchReqDto.getUserId()) {
			billOutStoreSearchReqDto.setUserId(getUser().getId());
		}
		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		List<BillOutStore> billOutStoreList = null;
		if (!CollectionUtils.isEmpty(userSubject)) {
			billOutStoreSearchReqDto.setUserSubject(userSubject);
			billOutStoreList = billOutStoreDao.queryResultsByCon(billOutStoreSearchReqDto);
		}
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_OUT_STORE,
				billOutStoreSearchReqDto.getUserId());
		List<BillOutStoreResDto> billOutStoreResDtoList = convertToResDto(billOutStoreList, isAllowPerm);

		return billOutStoreResDtoList;
	}

	/**
	 * 查询有效出库单
	 * 
	 * @param billDeliveryId
	 * @return
	 */
	public BillOutStore queryValidBillOutStoreByBillDeliveryId(Integer billDeliveryId) {
		BillOutStore billOutStore = new BillOutStore();
		billOutStore.setBillDeliveryId(billDeliveryId);
		billOutStore.setIsDelete(BaseConsts.ZERO);
		List<BillOutStore> list = billOutStoreDao.queryByBillDeliveryId(billOutStore);
		if (CollectionUtils.isEmpty(list)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "对应销售出库单不存在，请联系管理员！");
		}
		if (list.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "存在多条对应销售出库单，请联系管理员！");
		}
		return list.get(0);
	}

	/**
	 * 查询有效出库单
	 * 
	 * @param poReturnId
	 * @return
	 */
	public BillOutStore queryValidBillOutStoreByPoReturnId(Integer poReturnId) {
		BillOutStore billOutStore = new BillOutStore();
		billOutStore.setPoReturnId(poReturnId);
		billOutStore.setIsDelete(BaseConsts.ZERO);
		List<BillOutStore> list = billOutStoreDao.queryByPoReturnId(billOutStore);
		if (CollectionUtils.isEmpty(list)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "对应退货出库单不存在，请联系管理员！");
		}
		if (list.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "存在多条对应退货出库单，请联系管理员！");
		}
		return list.get(0);
	}

	/**
	 * 查询合计
	 * 
	 * @param billOutStoreSearchReqDto
	 * @param isAllowPerm
	 * @return
	 */
	private String querySumBillOutStore(BillOutStoreSearchReqDto billOutStoreSearchReqDto, boolean isAllowPerm) {
		String totalStr = "";
		if (billOutStoreSearchReqDto.getNeedSum() != null && billOutStoreSearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<BillOutStoreSum> billOutStoreSumList = billOutStoreDao.querySumBillOutStore(billOutStoreSearchReqDto);
			if (!CollectionUtils.isEmpty(billOutStoreSumList)) {
				BigDecimal totalSendNum = BigDecimal.ZERO;
				BigDecimal totalPickupNum = BigDecimal.ZERO;
				BigDecimal totalSendAmount = BigDecimal.ZERO;
				BigDecimal totalPickupAmount = BigDecimal.ZERO;
				String totalSendAmountStr = "";
				String totalPickupAmountStr = "";

				for (BillOutStoreSum billOutStoreSum : billOutStoreSumList) {
					totalSendNum = DecimalUtil.add(totalSendNum, null == billOutStoreSum.getTotalSendNum()
							? BigDecimal.ZERO : billOutStoreSum.getTotalSendNum());
					totalPickupNum = DecimalUtil.add(totalPickupNum, null == billOutStoreSum.getTotalPickupNum()
							? BigDecimal.ZERO : billOutStoreSum.getTotalPickupNum());
					BigDecimal cnyTotalSendAmount = BigDecimal.ZERO;
					if (null != billOutStoreSum.getCurrencyType()) {
						cnyTotalSendAmount = ServiceSupport.amountNewToRMB(
								null == billOutStoreSum.getTotalSendAmount() ? BigDecimal.ZERO
										: billOutStoreSum.getTotalSendAmount(),
								billOutStoreSum.getCurrencyType(), null);
					}
					totalSendAmount = DecimalUtil.add(totalSendAmount,
							null == cnyTotalSendAmount ? BigDecimal.ZERO : cnyTotalSendAmount);
					BigDecimal cnyTotalPickupAmount = BigDecimal.ZERO;
					if (null != billOutStoreSum.getCurrencyType()) {
						cnyTotalPickupAmount = ServiceSupport.amountNewToRMB(
								null == billOutStoreSum.getTotalPickupAmount() ? BigDecimal.ZERO
										: billOutStoreSum.getTotalPickupAmount(),
								billOutStoreSum.getCurrencyType(), null);
					}
					totalPickupAmount = DecimalUtil.add(totalPickupAmount,
							null == cnyTotalPickupAmount ? BigDecimal.ZERO : cnyTotalPickupAmount);
				}
				if (isAllowPerm == true) {
					totalSendAmountStr = BaseConsts.NO_PERMISSION_HIT;
					totalPickupAmountStr = BaseConsts.NO_PERMISSION_HIT;
				} else {
					totalSendAmountStr = DecimalUtil.toAmountString(totalSendAmount);
					totalPickupAmountStr = DecimalUtil.toAmountString(totalPickupAmount);
				}
				totalStr = "发货数量：" + DecimalUtil.toQuantityString(totalSendNum) + "；拣货数量："
						+ DecimalUtil.toQuantityString(totalPickupNum) + "；发货金额：" + totalSendAmountStr
						+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE) + "；拣货金额："
						+ totalPickupAmountStr + BaseConsts.STRING_BLANK_SPACE
						+ BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
			}
		}
		return totalStr;
	}

	private List<BillOutStoreResDto> convertToResDto(List<BillOutStore> billOutStoreList, boolean isAllowPerm) {
		List<BillOutStoreResDto> billOutStoreResDtoList = new ArrayList<BillOutStoreResDto>(5);
		if (CollectionUtils.isEmpty(billOutStoreList)) {
			return billOutStoreResDtoList;
		}
		for (BillOutStore billOutStore : billOutStoreList) {
			BillOutStoreResDto billOutStoreResDto = convertToResDto(billOutStore, isAllowPerm, 0);
			billOutStoreResDto.setOpertaList(
					getOperList(billOutStore.getStatus(), billOutStore.getBillType(), billOutStore.getWarehouseId()));
			billOutStoreResDtoList.add(billOutStoreResDto);
		}
		return billOutStoreResDtoList;
	}

	public BillOutStoreResDto convertToResDto(BillOutStore billOutStore, boolean isAllowPerm, int type) {
		BillOutStoreResDto billOutStoreResDto = new BillOutStoreResDto();
		if (null != billOutStore) {
			BeanUtils.copyProperties(billOutStore, billOutStoreResDto);
			if (billOutStore.getStatus() != null) {
				billOutStoreResDto.setStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_OUT_STORE_STATUS,
						Integer.toString(billOutStore.getStatus())));
			}
			if (billOutStore.getBillType() != null) {
				billOutStoreResDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_OUT_STORE_TYPE,
						Integer.toString(billOutStore.getBillType())));
			}
			if (billOutStore.getTransferMode() != null) {
				billOutStoreResDto.setTransferModeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_OUT_STORE_TRANSFER_MODE, Integer.toString(billOutStore.getTransferMode())));
			}
			if (billOutStore.getReasonType() != null) {
				billOutStoreResDto.setReasonName(ServiceSupport.getValueByBizCode(BizCodeConsts.OUT_REASON_TYPE,
						Integer.toString(billOutStore.getReasonType())));
			}
			if (billOutStore.getCurrencyType() != null) {
				billOutStoreResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.DEFAULT_CURRENCY_TYPE, Integer.toString(billOutStore.getCurrencyType())));
			}
			if (billOutStore.getSignStandard() != null) {
				billOutStoreResDto.setSignStandardName(ServiceSupport.getValueByBizCode(BizCodeConsts.SIGN_STANDARD,
						Integer.toString(billOutStore.getSignStandard())));
			}

			billOutStoreResDto.setProjectName(cacheService.showProjectNameById(billOutStoreResDto.getProjectId()));
			billOutStoreResDto.setWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billOutStoreResDto.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billOutStoreResDto.setReceiveWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billOutStoreResDto.getReceiveWarehouseId(), CacheKeyConsts.WAREHOUSE));
			if (null != billOutStoreResDto.getCustomerId() && type == 1) {
				billOutStoreResDto.setCustomerName(
						cacheService.getCustomerById(billOutStoreResDto.getCustomerId()).getChineseName());
			} else {
				billOutStoreResDto.setCustomerName(cacheService
						.showSubjectNameByIdAndKey(billOutStoreResDto.getCustomerId(), CacheKeyConsts.CUSTOMER));
			}
			List<BaseAddress> baseAddressList = baseAddressDao
					.queryResultsyBySubjectId(billOutStoreResDto.getWarehouseId());
			if (!CollectionUtils.isEmpty(baseAddressList)) {
				BaseAddress baseAddress = baseAddressList.get(0);
				baseAddress = cacheService.getAddressById(baseAddress.getId());
				if (null != baseAddress) {
					billOutStoreResDto.setSendWarehouseAddressName(baseAddress.getShowValue());
					billOutStoreResDto.setSendCityName(baseAddress.getCityName());
				}
			}
			BaseAddress customerAddress = cacheService.getAddressById(billOutStoreResDto.getCustomerAddressId());
			if (null != customerAddress) {
				billOutStoreResDto.setCustomerAddress(customerAddress.getShowValue());
				billOutStoreResDto.setCityName(customerAddress.getCityName());
				billOutStoreResDto.setContactPerson(customerAddress.getContactPerson());
				billOutStoreResDto.setMobilePhone(customerAddress.getMobilePhone());
				billOutStoreResDto.setTelephone(customerAddress.getTelephone());
			}
			BaseProject baseProject = cacheService.getProjectById(billOutStoreResDto.getProjectId());
			if (null != baseProject) {
				BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
				if (null != busiUnit) {
					billOutStoreResDto.setBusinessUnitName(busiUnit.getChineseName());
					billOutStoreResDto.setBusinessId(baseProject.getBusinessUnitId());
					billOutStoreResDto.setBusinessUnitAddress(busiUnit.getOfficeAddress());
					billOutStoreResDto.setBusinessUnitPhone(busiUnit.getRegPhone());
				}
			}

			BigDecimal sendAmount = (null == billOutStoreResDto.getSendAmount() ? BigDecimal.ZERO
					: billOutStoreResDto.getSendAmount());
			BigDecimal pickupAmount = (null == billOutStoreResDto.getPickupAmount() ? BigDecimal.ZERO
					: billOutStoreResDto.getPickupAmount());
			billOutStoreResDto.setSendAmountStr(DecimalUtil.toAmountString(sendAmount));
			billOutStoreResDto.setPickupAmountStr(DecimalUtil.toAmountString(pickupAmount));
			if (isAllowPerm) { // 不显示金额权限
				billOutStoreResDto.setCurrencyTypeName(BaseConsts.NO_PERMISSION_HIT);
				billOutStoreResDto.setSendAmountStr(BaseConsts.NO_PERMISSION_HIT);
				billOutStoreResDto.setPickupAmountStr(BaseConsts.NO_PERMISSION_HIT);
			}
			if (null != billOutStoreResDto.getBillDeliveryId()) {
				BillDelivery billDelivery = billDeliveryDao
						.queryBillDeliveryById(billOutStoreResDto.getBillDeliveryId());
				if (null != billDelivery) {
					billOutStoreResDto.setBillDeliveryNo(billDelivery.getBillNo());
				}
			}
			if (null != billOutStoreResDto.getPoReturnId()) {
				PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao
						.queryEntityById(billOutStoreResDto.getPoReturnId());
				if (null != purchaseOrderTitle) {
					billOutStoreResDto.setPoReturnNo(purchaseOrderTitle.getOrderNo());
				}
			}
		}
		return billOutStoreResDto;
	}

	private List<CodeValue> getOperList(Integer state, Integer billType, Integer warehouseId) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state, billType, warehouseId);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BillOutStoreResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state, Integer billType, Integer warehouseId) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);

		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setUserId(ServiceSupport.getUser().getId());
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);

		switch (state) {
		// 状态, 1-待提交 25-待财务专员审核 30-待财务主管审核 4-待发货 5-已发货
		case BaseConsts.ONE:
			if (billType.equals(BaseConsts.ONE) || billType.equals(BaseConsts.FIVE)) { // 1-销售出库、5-采购退货
				opertaList.add(OperateConsts.DETAIL);
			} else { // 2-调拨、借货、还货
				opertaList.add(OperateConsts.DETAIL);
				opertaList.add(OperateConsts.EDIT);
				opertaList.add(OperateConsts.SUBMIT);
				opertaList.add(OperateConsts.DELETE);
			}
			break;
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT_PICK);
			break;
		case BaseConsts.INT_30:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT_PICK);
			break;
		case BaseConsts.FOUR:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.EDIT);
			if (!CollectionUtils.isEmpty(userSubject)) {
				for (BaseUserSubject baseUserSubject : userSubject) {
					if (baseUserSubject.getSubjectId().equals(warehouseId)
							&& baseUserSubject.getOperater().equals(BaseConsts.ONE)) {
						opertaList.add(OperateConsts.SEND);
					}
				}
			}
			opertaList.add(OperateConsts.REJECT);
			opertaList.add(OperateConsts.PRINT);
			opertaList.add(OperateConsts.PRINT_PICK);
			break;
		case BaseConsts.FIVE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			opertaList.add(OperateConsts.PRINT_PICK);
			break;
		}
		return opertaList;
	}

	/**
	 * 根据ID查询出库单头详情
	 * 
	 * @param billOutStoreSearchReqDto
	 * @return
	 */
	public Result<BillOutStoreResDto> queryBillOutStoreById(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		Result<BillOutStoreResDto> result = new Result<BillOutStoreResDto>();
		BillOutStore billOutStoreRes = billOutStoreDao.queryById(billOutStoreSearchReqDto);
		boolean isAllowPerm = ServiceSupport.isAllowPerm(BusUrlConsts.NOT_SHOW_AMOUNT_BILL_OUT_STORE);
		BillOutStoreResDto billOutStoreResDto = convertToResDto(billOutStoreRes, isAllowPerm, 1);
		result.setItems(billOutStoreResDto);
		return result;
	}

	/**
	 * 新增出库单头(调拨)
	 * 
	 * @param billOutStore
	 * @return
	 */
	public BillOutStore addBillOutStore(BillOutStore billOutStore) {
		billOutStore.setBillNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_BILL_OUT_STORE,
				SeqConsts.BILL_OUT_STORE_NO, BaseConsts.INT_13));
		billOutStore.setCostAmount(BigDecimal.ZERO);
		billOutStore.setPoAmount(BigDecimal.ZERO);
		billOutStore.setSendAmount(BigDecimal.ZERO);
		billOutStore.setSendNum(BigDecimal.ZERO);
		billOutStore.setPickupAmount(BigDecimal.ZERO);
		billOutStore.setPickupNum(BigDecimal.ZERO);
		billOutStore.setExchangeRate(BigDecimal.ZERO);
		billOutStore.setStatus(BaseConsts.ONE); // 待提交
		billOutStore.setCreator(getUser().getChineseName());
		billOutStore.setCreatorId(getUser().getId());
		billOutStore.setPayAmount(BigDecimal.ZERO);
		int result = billOutStoreDao.insert(billOutStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_ADD_ERROR);
		}
		return billOutStore;
	}

	/**
	 * 更新出库单头(调拨)
	 * 
	 * @param billOutStore
	 * @return
	 */
	public void updateBillOutStore(BillOutStore billOutStore) {
		BillOutStore billOutStoreRes = billOutStoreDao.queryAndLockEntityById(billOutStore.getId());
		if (billOutStoreRes.getStatus().equals(BaseConsts.ONE)
				&& billOutStoreRes.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			billOutStore.setCustomerAddressId(
					null == billOutStore.getCustomerAddressId() ? -1 : billOutStore.getCustomerAddressId());
			if (null != billOutStore.getSignStandard()) {
				if (billOutStore.getSignStandard().equals(BaseConsts.ZERO)) { // 0-身份证
					billOutStore.setOfficialSeal("");
				} else if (billOutStore.getSignStandard().equals(BaseConsts.ONE)) { // 1-公章
					billOutStore.setCertificateId("");
					billOutStore.setCertificateName("");
				}
			}
			int result = billOutStoreDao.updateById(billOutStore);
			if (result <= 0) {
				throw new RuntimeException();
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_UPDATE_ERROR);
		}
	}

	public void updateBillOutStoreById(BillOutStore billOutStore) {
		int result = billOutStoreDao.updateById(billOutStore);
		if (result <= 0) {
			throw new RuntimeException();
		}
	}

	/**
	 * 销售单审核时更新对应出库单的发货单价
	 * 
	 * @param billOutStore
	 */
	public void updateBillOutStore4BillDeliveryAudit(BillOutStore billOutStore) {
		BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto = new BillOutStoreDtlSearchReqDto();
		billOutStoreDtlSearchReqDto.setBillOutStoreId(billOutStore.getId());
		List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreDtlDao.selectList(billOutStoreDtlSearchReqDto);
		if (!CollectionUtils.isEmpty(billOutStoreDtlList)) {
			for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtlList) {
				Integer billDeliveryDtlId = billOutStoreDtl.getBillDeliveryDtlId();
				BillDeliveryDtl billDeliveryDtl = billDeliveryDtlDao.queryEntityById(billDeliveryDtlId);
				billOutStoreDtl.setSendPrice(billDeliveryDtl.getRequiredSendPrice());
				billOutStoreDtl.setSendAmount(
						DecimalUtil.multiply(billOutStoreDtl.getSendNum(), billOutStoreDtl.getSendPrice()));
				billOutStoreDtlDao.updateById(billOutStoreDtl);
				List<BillOutStorePickDtl> billOutStorePickDtlList = billOutStorePickDtlDao
						.queryResultsByBillOutStoreDtlId(billOutStoreDtl.getId());
				if (!CollectionUtils.isEmpty(billOutStorePickDtlList)) {
					for (BillOutStorePickDtl billOutStorePickDtl : billOutStorePickDtlList) {
						billOutStorePickDtl.setSendPrice(billDeliveryDtl.getRequiredSendPrice());
						billOutStorePickDtlDao.updateById(billOutStorePickDtl);
					}
				}
				// 更新出库单明细的拣货数量、拣货金额、成本金额、订单金额
				billOutStorePickDtlService.updateBillOutStoreDtlPickInfo(billOutStoreDtl.getId());
			}
		}
		// 更新出库单发货数量、发货金额、拣货数量、拣货金额
		billOutStoreDtlService.updateBillOutStoreInfo(billOutStore);
	}

	/**
	 * 提交出库单(调拨才会有提交)
	 * 
	 * @param billOutStore
	 * @return
	 */
	public void submitBillOutStore(BillOutStore billOutStore) {
		billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStore.getId());
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.THREE, billOutStore.getProjectId());
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		if (billOutStore.getStatus().equals(BaseConsts.ONE) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
			validateSubmit(billOutStore);
			billOutStore.setStatus(startAuditNode.getAuditNodeState()); // 待财务专员审核
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_SUBMIT_ERROR);
		}

		// 开始业务审核
		billOutStoreAuditService.startAudit(billOutStore, startAuditNode);

		// 最后更新出库单
		int result = billOutStoreDao.updateById(billOutStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_UPDATE_ERROR);
		}
	}

	/**
	 * 批量送货
	 * 
	 * @param ids
	 */
	public void batchSendBillOutStore(List<Integer> ids) {
		if (!CollectionUtils.isEmpty(ids)) {
			for (Integer id : ids) {
				BillOutStore billOutStore = new BillOutStore();
				billOutStore.setId(id);
				sendBillOutStore(billOutStore);
			}
		}
	}

	/**
	 * 出库单送货
	 * 
	 * @param billOutStore
	 * @return
	 */
	public void sendBillOutStore(BillOutStore billOutStore) {
		// 检查是否存在出库明细和拣货明细、明细数量是否等于拣货数量
		Date deliverTime = billOutStore.getDeliverTime() == null ? new Date() : billOutStore.getDeliverTime();
		billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStore.getId());
		validateSubmit(billOutStore);
		if (billOutStore.getStatus().equals(BaseConsts.FOUR) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待发货且未删除
			billOutStore.setSendDate(deliverTime); // 送货日期
			billOutStore.setDeliverId(getUser() == null ? BaseConsts.SYSTEM_ROLE_ID : getUser().getId());
			billOutStore.setDeliverer(getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME : getUser().getChineseName());
			billOutStore.setDeliverTime(deliverTime);
			billOutStore.setStatus(BaseConsts.FIVE); // 已发货
			billOutStore.setSystemDeliverTime(new Date());
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_SEND_ERROR);
		}

		if (billOutStore.getBillType().equals(BaseConsts.ONE) || billOutStore.getBillType().equals(BaseConsts.SIX)) { // 类型：销售出库,或者内部销售
			// 回写销售单状态
			updateBillDeliveryInfo(billOutStore, BaseConsts.FIVE);

			if (billOutStore.getBillType().equals(BaseConsts.SIX)) {
				addBillInStoreByOutStore(billOutStore);
			}

		} else if (billOutStore.getBillType().equals(BaseConsts.FIVE)) { // 类型：5-采购退货出库
			// 回写采购退货单状态
			updatePoReturnInfo(billOutStore, BaseConsts.FIVE);
		}
		// 释放库存锁定数量(根据拣货明细)
		releaseAndSubtractStl(billOutStore, BaseConsts.FOUR);
		// 仓库调拨业务，自动生成在途仓和接收仓的入库单
		if (billOutStore.getBillType().equals(BaseConsts.TWO) || billOutStore.getBillType().equals(BaseConsts.THREE)
				|| billOutStore.getBillType().equals(BaseConsts.FOUR)) { // 调拨出库、借还货
			allotGoods(billOutStore);
		}

		// 最后更新出库单
		int result = billOutStoreDao.updateById(billOutStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_UPDATE_ERROR);
		}
		// 销售和采购退货的记账
		if (billOutStore.getBillType() == BaseConsts.ONE || billOutStore.getBillType() == BaseConsts.FIVE) {
			accountBillOutStore(billOutStore);
		}

	}

	public void accountBillOutStore(BillOutStore billOutStore) {
		Integer voucherId = null;
		if (billOutStore.getBillType() == BaseConsts.ONE) {// 1.销售出库 记账
			voucherId = outStoreBookkeepingService.outStoreBookkeeping(billOutStore.getId());
		} else if (billOutStore.getBillType() == BaseConsts.FIVE) {// 采购退货的记账
			voucherId = purchaseReturnService.OutStorePurchaseReturn(billOutStore.getId());
		}
		// 2.生成应收
		RecDetail recDetail = convertToRecDetail(billOutStore, voucherId);
		if (!CollectionUtils.isEmpty(recDetail.getRecLines())) { // 应收金额大于0
			Integer recId = receiveService.createRecDetail(recDetail);
			// 3.水单核销
			verificationAdvanceService.verificationReceipt(recId, billOutStore.getBillNo(),
					billOutStore.getBillDeliveryId());
		}
		// 4.融资池记账
		createProjectPool(billOutStore);

		// 销售类型的进行资金池入池
		if (billOutStore.getBillType() == BaseConsts.ONE) {
			FundPoolReqDto poolReqDto = new FundPoolReqDto();
			poolReqDto.setId(billOutStore.getId());
			poolAssestService.createPoolAssestOut(poolReqDto);
		}
	}

	public void createProjectPool(BillOutStore billOutStore) {
		ProjectPoolAsset ppf = new ProjectPoolAsset();
		ppf.setType(BaseConsts.TWO);
		ppf.setBillNo(billOutStore.getBillNo());
		ppf.setBillSource(BaseConsts.TWO);
		ppf.setProjectId(billOutStore.getProjectId());
		ppf.setCustomerId(billOutStore.getCustomerId());
		ppf.setBusinessDate(billOutStore.getDeliverTime());
		ppf.setBillAmount(billOutStore.getPayAmount());
		// ppf.setBillAmount(billOutStore.getCostAmount());
		ppf.setBillCurrencyType(billOutStore.getCurrencyType());
		projectPoolService.addProjectPoolAsset(ppf, BaseConsts.TWO); // 2-出库单
		projectPoolService.updateProjectPoolInfo(ppf.getProjectId());
	}

	private void addBillInStoreByOutStore(BillOutStore billOutStore) {
		BillDelivery billDelivery = billDeliveryDao.queryBillDeliveryById(billOutStore.getBillDeliveryId());
		// 新增采购单头
		PurchaseOrderTitle poTitle = new PurchaseOrderTitle();
		poTitle.setAppendNo(billDelivery.getAffiliateNo());
		poTitle.setProjectId(billDelivery.getReceiveProjectId());
		poTitle.setSupplierId(billDelivery.getReceiveSupplierId());
		poTitle.setWarehouseId(billDelivery.getReceiveWarehouseId());
		List<BaseAddress> baseAddresses = baseAddressDao.queryResultsyBySubjectId(billDelivery.getReceiveWarehouseId());
		if (org.apache.commons.collections.CollectionUtils.isNotEmpty(baseAddresses)) {
			poTitle.setWareAddrId(baseAddresses.get(0).getId());
		}
		poTitle.setCurrencyId(billDelivery.getCurrencyType());
		Date d = new Date();
		poTitle.setOrderTime(d);
		poTitle.setPerdictTime(d);
		poTitle.setIsRequestPay(BaseConsts.TWO);
		poTitle.setState(BaseConsts.FIVE);
		poTitle.setOrderType(BaseConsts.ZERO);
		Integer poTitleId = purchaseOrderService.addPurchaseOrderTitle(poTitle);

		BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto = new BillOutStoreDtlSearchReqDto();
		billOutStoreDtlSearchReqDto.setBillOutStoreId(billOutStore.getId());
		List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreDtlDao.selectList(billOutStoreDtlSearchReqDto);
		for (BillOutStoreDtl billOutStoreDtl : billOutStoreDtlList) {
			// 新增采购单明细
			PurchaseOrderLine poLine = new PurchaseOrderLine();
			poLine.setPoId(poTitleId);
			poLine.setGoodsId(billOutStoreDtl.getGoodsId());
			poLine.setGoodsNum(billOutStoreDtl.getSendNum());
			poLine.setDiscountAmount(BigDecimal.ZERO);
			poLine.setGoodsPrice(billOutStoreDtl.getSendPrice());
			// poLine.setCostPrice(billOutStoreDtl.getco());
			poLine.setBatchNum(billOutStoreDtl.getBatchNo());
			purchaseOrderService.addPoLine(poTitleId, poLine);
		}
		purchaseOrderService.submitPurchaseOrderTitle(poTitle);
		BillInStore billInStore = billInStoreService.autoReceive(poTitle);
		billInStoreService.submitBillInStore(billInStore, d);
	}

	private RecDetail convertToRecDetail(BillOutStore billOutStore, Integer voucherId, Date expireDate) {
		RecDetail recDetail = new RecDetail();
		Receive receive = new Receive();
		List<RecLine> recLines = new ArrayList<RecLine>();
		receive.setProjectId(billOutStore.getProjectId());
		receive.setCustId(billOutStore.getCustomerId());
		receive.setExpireDate(expireDate);
		LOGGER.info("pms请款单预计内部打款日期:" + expireDate);
		BaseProject project = cacheService.getProjectById(billOutStore.getProjectId());
		if (project == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目id" + billOutStore.getProjectId() + "不存在");
		}
		BaseSubject busiUnit = cacheService.getBusiUnitById(project.getBusinessUnitId());
		if (busiUnit == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "经营单位id" + project.getBusinessUnitId() + "不存在");
		}
		// Voucher voucher = voucherDao.queryEntityById(voucherId);
		receive.setBusiUnit(busiUnit.getId());
		receive.setCurrencyType(billOutStore.getCurrencyType());
		receive.setCheckDate(billOutStore.getSendDate() == null ? new Date() : billOutStore.getSendDate());
		// receive.setBillType(voucher.getBillType());
		// receive.setBillNo(voucher.getBillNo());

		VoucherLineSearchReqDto voucherLineSearchReqDto = new VoucherLineSearchReqDto();
		voucherLineSearchReqDto.setVoucherId(voucherId);
		List<VoucherLineModelResDto> voucherLineModelResDtos = voucherLineService
				.queryLineCheckResultsByCon(voucherLineSearchReqDto);
		if (!CollectionUtils.isEmpty(voucherLineModelResDtos)) {
			for (VoucherLineModelResDto item : voucherLineModelResDtos) {
				RecLine recLine = new RecLine();
				recLine.setAmountCheck(item.getAmount());
				recLine.setVoucherLineId(item.getId());
				recLines.add(recLine);
			}
		}
		recDetail.setReceive(receive);
		recDetail.setRecLines(recLines);
		return recDetail;
	}

	private RecDetail convertToRecDetail(BillOutStore billOutStore, Integer voucherId) {
		Date expireDate = null;
		if (!StringUtils.isEmpty(billOutStore.getAffiliateNo())) {
			PmsPoTitleSearchReqDto req = new PmsPoTitleSearchReqDto();
			req.setPayNo(billOutStore.getAffiliateNo());
			List<PmsPayOrderTitle> pmsPayOrderTitles = pmsPayOrderTitleDao.queryResultsByCon(req);
			if (!CollectionUtils.isEmpty(pmsPayOrderTitles)) { // pms请款单(应收保理)
				expireDate = pmsPayOrderTitles.get(0).getInnerPayDate();
			}

			PmsPay pmsPay = new PmsPay();
			pmsPay.setPay_sn(billOutStore.getAffiliateNo());
			pmsPay.setStatus(BaseConsts.ZERO);
			pmsPay.setFlag(BaseConsts.ZERO);
			List<PmsPay> pmsPayList = pmsPayDao.queryPmsPayByParam(pmsPay);
			if (!CollectionUtils.isEmpty(pmsPayList)) { // pms请款单(融通质押)
				Date payCreateTime = pmsPayList.get(0).getPay_create_time();
				if (null != payCreateTime) {
					ProjectItem projectItem = cacheService.getProjectItemByPid(billOutStore.getProjectId());
					expireDate = DateFormatUtils.afterDay(payCreateTime,
							null == projectItem.getPayCycle() ? 0 : projectItem.getPayCycle()); // 延长到期时间
				}
			}
		}
		return convertToRecDetail(billOutStore, voucherId, expireDate);
	}

	/**
	 * 批量删除出库单
	 * 
	 * @param ids
	 */
	public void deleteBillOutStoreByIds(List<Integer> ids) {
		for (Integer id : ids) {
			BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(id);

			if (billOutStore.getStatus().equals(BaseConsts.ONE) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待提交且未删除
				deleteBillOutStoreById(billOutStore);
			} else {
				throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_DELETE_ERROR);
			}
		}
	}

	/**
	 * 删除出库单
	 * 
	 * @param billOutStore
	 */
	public void deleteBillOutStoreById(BillOutStore billOutStore) {
		deleteBillOutStoreById(billOutStore, BaseConsts.ONE);
	}

	/**
	 * 删除出库单
	 * 
	 * @param billOutStore
	 * @param operateFlag
	 *            1-删除 2-提交 3-驳回 4-送货
	 */
	public void deleteBillOutStoreById(BillOutStore billOutStore, Integer operateFlag) {
		billOutStore.setDeleterId(getUser().getId());
		billOutStore.setDeleter(getUser().getChineseName());
		billOutStore.setDeleteAt(new Date());
		billOutStore.setIsDelete(BaseConsts.ONE);

		// 释放库存锁定数量(根据拣货明细)
		releaseAndSubtractStl(billOutStore, operateFlag);

		int result = billOutStoreDao.updateById(billOutStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_UPDATE_ERROR);
		}
	}

	/**
	 * 回写销售单状态(销售出库)
	 * 
	 * @param billOutStore
	 */
	public void updateBillDeliveryInfo(BillOutStore billOutStore, Integer status) {
		if (null != billOutStore.getBillDeliveryId()) {
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(billOutStore.getBillDeliveryId());
			billDelivery.setStatus(status);
			billDeliveryDao.updateById(billDelivery);
		}
	}

	/**
	 * 回写采购退货单状态(采购退货出库)
	 * 
	 * @param billOutStore
	 */
	public void updatePoReturnInfo(BillOutStore billOutStore, Integer status) {
		if (null != billOutStore.getPoReturnId()) {
			PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
			purchaseOrderTitle.setId(billOutStore.getPoReturnId());
			purchaseOrderTitle.setState(status);
			purchaseOrderService.updatePurchaseOrderTitle(purchaseOrderTitle);
		}
	}

	/**
	 * 驳回出库单
	 * 
	 * @param billOutStore
	 */
	public void rejectBillOutStore(BillOutStore billOutStore) {
		// 作废出库单
		billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStore.getId());

		if (billOutStore.getStatus().equals(BaseConsts.FOUR) && billOutStore.getIsDelete().equals(BaseConsts.ZERO)) { // 待发货且未删除
			if (billOutStore.getBillType().equals(BaseConsts.ONE)
					|| billOutStore.getBillType().equals(BaseConsts.FIVE)) { // 1-销售出库、5-采购退货
				billOutStore.setDeleterId(getUser().getId());
				billOutStore.setDeleter(getUser().getChineseName());
				billOutStore.setDeleteAt(new Date());
				billOutStore.setIsDelete(BaseConsts.ONE);
				if (billOutStore.getBillType().equals(BaseConsts.ONE)) {
					// 更新销售单状态
					updateBillDeliveryInfo(billOutStore, BaseConsts.ONE);
					// 回退核销预收
					billDeliveryService.reCalcPrice(billOutStore.getBillDeliveryId(), null);
					verificationAdvanceService.rollBackReceiptAdvance(billOutStore.getBillDeliveryId());
				} else if (billOutStore.getBillType().equals(BaseConsts.FIVE)) {
					// 更新采购退货单状态
					updatePoReturnInfo(billOutStore, BaseConsts.ONE);
				}
				// 释放库存锁定数量(根据拣货明细)
				releaseAndSubtractStl(billOutStore, BaseConsts.THREE);
			} else { // 调拨出库、借还货
				billOutStore.setStatus(BaseConsts.ONE);
			}
			// 最后更新出库单
			int result = billOutStoreDao.updateById(billOutStore);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_UPDATE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_REJECT_ERROR);
		}
	}

	/**
	 * 更新打印次数
	 */
	public void updatePrintNum(Integer id) {
		BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(id);
		if (null != billOutStore) {
			billOutStore.setPrintNum(billOutStore.getPrintNum() + 1);
			billOutStoreDao.updateById(billOutStore);
		}
	}

	/**
	 * 释放库存锁定数量，同时扣减库存
	 * 
	 * @param billOutStore
	 * @param operateFlag
	 *            1-删除 2-提交 3-驳回 4-送货
	 */
	private void releaseAndSubtractStl(BillOutStore billOutStore, Integer operateFlag) {
		List<BillOutStorePickDtl> billOutStorePickDtlList = billOutStorePickDtlDao
				.queryResultsByBillOutStoreId(billOutStore.getId());
		if (!CollectionUtils.isEmpty(billOutStorePickDtlList)) {
			for (BillOutStorePickDtl pickDtl : billOutStorePickDtlList) {
				Integer stlId = pickDtl.getStlId();
				if (null != stlId) {
					stlService.releaseAndSubtractStl(stlId, pickDtl.getPickupNum(), billOutStore, operateFlag);
				}
			}
		}
	}

	/**
	 * 检查是否存在出库明细和拣货明细、明细数量是否等于拣货数量
	 * 
	 * @param billOutStore
	 * @return
	 */
	private void validateSubmit(BillOutStore billOutStore) {
		BillOutStoreSearchReqDto billOutStoreReqDto = new BillOutStoreSearchReqDto();
		billOutStoreReqDto.setId(billOutStore.getId());
		BillOutStore billOutStoreRes = billOutStoreDao.queryById(billOutStoreReqDto);

		int dtlsCount = billOutStoreDao.queryDtlsCount(billOutStore);
		if (dtlsCount <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_DTL_NOT_ADD);
		}
		int pickDtlsCount = billOutStoreDao.queryPickDtlsCount(billOutStore);
		if (pickDtlsCount <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_TALLY_DTL_NOT_ADD);
		}

		BigDecimal sendNum = (null == billOutStoreRes.getSendNum() ? BigDecimal.ZERO : billOutStoreRes.getSendNum());
		BigDecimal pickupNum = (null == billOutStoreRes.getPickupNum() ? BigDecimal.ZERO
				: billOutStoreRes.getPickupNum());
		if (DecimalUtil.ne(sendNum, pickupNum)) {
			throw new BaseException(ExcMsgEnum.SEND_NUM_NOT_EQUAL_PICK_NUM);
		}
		BillOutStore outStore = billOutStoreDao.queryDtlsTotalInfo(billOutStore);
		BillOutStore pickOutStore = billOutStoreDao.queryPickDtlsTotalInfo(billOutStore);
		if (null != outStore) {
			if (DecimalUtil.ne(sendNum, outStore.getSendNum())) {
				throw new BaseException(ExcMsgEnum.TOTAL_SEND_NUM_NOT_EQUAL);
			}
		}
		if (null != outStore && null != pickOutStore) {
			if (DecimalUtil.ne(pickupNum, outStore.getPickupNum())
					|| DecimalUtil.ne(pickupNum, pickOutStore.getPickupNum())) {
				throw new BaseException(ExcMsgEnum.TOTAL_PICK_NUM_NOT_EQUAL);
			}
		}
	}

	/**
	 * 仓库调拨货物业务
	 * 
	 * @param billOutStore
	 */
	private void allotGoods(BillOutStore billOutStore) {
		billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStore.getId());
		if (null == billOutStore) {
			throw new RuntimeException();
		}
		BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto = new BillOutStoreDtlSearchReqDto();
		billOutStoreDtlSearchReqDto.setBillOutStoreId(billOutStore.getId());
		List<BillOutStoreDtl> billOutStoreDtlList = billOutStoreDtlDao.selectList(billOutStoreDtlSearchReqDto);
		if (CollectionUtils.isEmpty(billOutStoreDtlList)) {
			throw new RuntimeException();
		}
		List<BillOutStorePickDtl> billOutStorePickDtlList = billOutStorePickDtlDao
				.queryResultsByBillOutStoreId(billOutStore.getId());
		if (CollectionUtils.isEmpty(billOutStorePickDtlList)) {
			throw new RuntimeException();
		}

		Integer receiveWarehouseId = billOutStore.getReceiveWarehouseId();
		if (null != receiveWarehouseId) { // 存在接收仓库
			BaseSubject baseSubject = cacheService.getWarehouseById(receiveWarehouseId);
			if (null != baseSubject) {
				// 生成在途仓调拨入库单
				if (!billOutStore.getBillType().equals(BaseConsts.THREE)) { // 2-调拨
																			// 4-还货，生成在途调拨入库单，借货不生成
					createBillInStore(billOutStore, billOutStorePickDtlList, true);
				}
				// 生成接收仓的调拨入库单
				createBillInStore(billOutStore, billOutStorePickDtlList, false);
			} else {
				throw new BaseException(ExcMsgEnum.RECEIVE_WAREHOUSE_NOT_EXIST);
			}
		} else {
			throw new BaseException(ExcMsgEnum.RECEIVE_WAREHOUSE_NOT_EXIST);
		}
	}

	/**
	 * 获取在途仓
	 * 
	 * @return
	 */
	public BaseSubject getOnwayWarehouse() {
		BaseSubject onwayWarehouse = null;
		QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
		querySubjectReqDto.setId(1);
		querySubjectReqDto.setSubjectType(BaseConsts.TWO);
		List<BaseSubject> warehouseList = baseSubjectDao.querySubjectByCond(querySubjectReqDto); // 查询在途仓，id=1为在途仓
		if (!CollectionUtils.isEmpty(warehouseList)) {
			onwayWarehouse = warehouseList.get(0);
		} else {
			throw new BaseException(ExcMsgEnum.ONWAY_WAREHOUSE_NOT_EXIST);
		}
		return onwayWarehouse;
	}

	/**
	 * 生成调拨入库单
	 * 
	 * @param billOutStore
	 *            出库单
	 * @param billOutStorePickDtlList
	 *            出库单拣货明细
	 * @param isOnwayWarehouse
	 *            是否在途仓调拨入库单
	 * @return
	 */
	private BillInStore createBillInStore(BillOutStore billOutStore, List<BillOutStorePickDtl> billOutStorePickDtlList,
			boolean isOnwayWarehouse) {
		Date currTime = new Date();
		BillInStore billInStore = new BillInStore();
		billInStore.setBillNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_BILL_IN_STORE,
				SeqConsts.BILL_IN_STORE_NO, BaseConsts.INT_13));
		billInStore.setBillType(BaseConsts.TWO); // 单据类型：调拨入库
		billInStore.setAffiliateNo(billOutStore.getAffiliateNo() + "-T");
		billInStore.setProjectId(billOutStore.getProjectId());
		billInStore.setCustomerId(billOutStore.getCustomerId());
		billInStore.setSupplierId(billOutStore.getWarehouseId()); // 供应商为仓库
		billInStore.setReceiveDate(currTime); // 当前日期
		billInStore.setReceiveNum(billOutStore.getSendNum());
		billInStore.setReceiveAmount(billOutStore.getCostAmount()); // 取成本金额
		billInStore.setTallyNum(billOutStore.getSendNum());
		billInStore.setTallyAmount(billOutStore.getCostAmount()); // 取成本金额
		billInStore.setBillOutStoreId(billOutStore.getId());
		billInStore.setCurrencyType(billOutStore.getCurrencyType());
		billInStore.setExchangeRate(billOutStore.getExchangeRate());
		billInStore.setCreator(getUser().getChineseName());
		billInStore.setCreatorId(getUser().getId());
		billInStore.setRemark(billOutStore.getRemark());
		billInStore.setPayAmount(billOutStore.getPayAmount());
		if (isOnwayWarehouse == true) {
			BaseSubject onwayWarehouse = getOnwayWarehouse();
			billInStore.setWarehouseId(onwayWarehouse.getId()); // 仓库
			billInStore.setStatus(BaseConsts.TWO); // 已收货
			billInStore.setAcceptorId(getUser().getId());
			billInStore.setAcceptor(getUser().getChineseName());
			billInStore.setAcceptTime(currTime);
		} else {
			billInStore.setWarehouseId(billOutStore.getReceiveWarehouseId()); // 仓库
			if (billOutStore.getBillType().equals(BaseConsts.THREE)) { // 3-借货
				billInStore.setStatus(BaseConsts.TWO); // 已收货
				billInStore.setAcceptorId(getUser().getId());
				billInStore.setAcceptor(getUser().getChineseName());
				billInStore.setAcceptTime(currTime);
			} else { // 2-调拨 4-还货
				billInStore.setStatus(BaseConsts.ONE); // 待收货
			}
		}
		billInStoreDao.insert(billInStore);

		for (BillOutStorePickDtl billOutStorePickDtl : billOutStorePickDtlList) {
			BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
			billInStoreDtl.setBillInStoreId(billInStore.getId());
			billInStoreDtl.setBillOutStoreId(billOutStorePickDtl.getBillOutStoreId());
			billInStoreDtl.setBillOutStoreDtlId(billOutStorePickDtl.getBillOutStoreDtlId());
			billInStoreDtl.setPoId(billOutStorePickDtl.getPoId());
			billInStoreDtl.setPoDtlId(billOutStorePickDtl.getPoDtlId());
			billInStoreDtl.setGoodsId(billOutStorePickDtl.getGoodsId());
			billInStoreDtl.setReceiveNum(billOutStorePickDtl.getPickupNum());
			billInStoreDtl.setReceivePrice(billOutStorePickDtl.getCostPrice()); // 取成本金额
			billInStoreDtl.setTallyNum(billOutStorePickDtl.getPickupNum());
			billInStoreDtl.setPoPrice(billOutStorePickDtl.getPoPrice());
			billInStoreDtl.setPayRate(billOutStorePickDtl.getPayRate());
			billInStoreDtl.setCostPrice(billOutStorePickDtl.getCostPrice());
			Integer stlId = billOutStorePickDtl.getStlId();
			if (null != stlId) {
				Stl stl = stlDao.queryEntityById(stlId);
				if (null != stl) {
					billInStoreDtl.setBillInStoreDtlId(stl.getBillInStoreDtlId()); // 关联入库单明细ID
				}
			}
			billInStoreDtl.setBatchNo(billOutStorePickDtl.getBatchNo());
			billInStoreDtl.setRemark(billOutStorePickDtl.getRemark());
			billInStoreDtl.setCreator(getUser().getChineseName());
			billInStoreDtl.setCreatorId(getUser().getId());
			billInStoreDtl.setAcceptTime(billInStore.getAcceptTime());
			billInStoreDtl.setCurrencyType(billOutStorePickDtl.getCurrencyType());
			billInStoreDtl.setReceiveDate(billOutStorePickDtl.getReceiveDate()); // 最初收货日期
			billInStoreDtl.setOriginAcceptTime(billOutStorePickDtl.getOriginAcceptTime()); // 最初入库时间
			billInStoreDtl.setCustomerId(billOutStorePickDtl.getCustomerId());
			billInStoreDtl.setSupplierId(billOutStorePickDtl.getSupplierId());
			billInStoreDtl.setPayPrice(billOutStorePickDtl.getPayPrice());
			billInStoreDtl.setPayTime(billOutStorePickDtl.getPayTime());
			billInStoreDtl.setPayRate(
					billOutStorePickDtl.getPayRate() == null ? BigDecimal.ZERO : billOutStorePickDtl.getPayRate());
			billInStoreDtl.setPayRealCurrency(billOutStorePickDtl.getPayRealCurrency());
			billInStoreDtlDao.insert(billInStoreDtl);

			BillInStoreTallyDtl billInStoreTallyDtl = new BillInStoreTallyDtl();
			billInStoreTallyDtl.setBillInStoreId(billInStore.getId());
			billInStoreTallyDtl.setBillInStoreDtlId(billInStoreDtl.getId());
			billInStoreTallyDtl.setBillOutStoreId(billOutStorePickDtl.getBillOutStoreId());
			billInStoreTallyDtl.setBillOutStoreDtlId(billOutStorePickDtl.getBillOutStoreDtlId());
			billInStoreTallyDtl.setPoId(billOutStorePickDtl.getPoId());
			billInStoreTallyDtl.setPoDtlId(billOutStorePickDtl.getPoDtlId());
			billInStoreTallyDtl.setGoodsId(billOutStorePickDtl.getGoodsId());
			billInStoreTallyDtl.setTallyNum(billOutStorePickDtl.getPickupNum());
			billInStoreTallyDtl.setReceivePrice(billInStoreDtl.getReceivePrice());
			billInStoreTallyDtl.setPoPrice(billOutStorePickDtl.getPoPrice());
			billInStoreTallyDtl.setCostPrice(billOutStorePickDtl.getCostPrice());
			billInStoreTallyDtl.setBatchNo(billOutStorePickDtl.getBatchNo());
			billInStoreTallyDtl.setGoodsStatus(billOutStorePickDtl.getGoodsStatus());
			billInStoreTallyDtl.setRemark(billOutStorePickDtl.getRemark());
			billInStoreTallyDtl.setCreator(getUser().getChineseName());
			billInStoreTallyDtl.setCreatorId(getUser().getId());
			billInStoreTallyDtl.setAcceptTime(billInStore.getAcceptTime());
			billInStoreTallyDtl.setCurrencyType(billOutStorePickDtl.getCurrencyType());
			billInStoreTallyDtl.setReceiveDate(billOutStorePickDtl.getReceiveDate()); // 最初收货日期
			billInStoreTallyDtl.setOriginAcceptTime(billOutStorePickDtl.getOriginAcceptTime()); // 最初入库时间
			billInStoreTallyDtl.setCustomerId(billOutStorePickDtl.getCustomerId());
			billInStoreTallyDtl.setSupplierId(billOutStorePickDtl.getSupplierId());
			billInStoreTallyDtl.setPayPrice(billOutStorePickDtl.getPayPrice());
			billInStoreTallyDtl.setPayTime(billOutStorePickDtl.getPayTime());
			billInStoreTallyDtl.setPayRate(
					billOutStorePickDtl.getPayRate() == null ? BigDecimal.ZERO : billOutStorePickDtl.getPayRate());
			billInStoreTallyDtl.setPayRealCurrency(billOutStorePickDtl.getPayRealCurrency());
			billInStoreTallyDtlDao.insert(billInStoreTallyDtl);

			if (isOnwayWarehouse == true
					|| (isOnwayWarehouse == false && billOutStore.getBillType().equals(BaseConsts.THREE))) { // 在途仓或者借货虚拟仓入库
				// 增加库存
				Stl stl = billInStoreService.createStl(billInStore, billInStoreTallyDtl);
				stlDao.insert(stl);
			}
		}
		return billInStore;
	}

	/**
	 * 按出库拣货明细的退货数量合计总退货数量
	 */
	public void updateReturnNum(Integer id) {
		billOutStoreDao.updateReturnNum(id);
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param fileAttReqDto
	 * @return
	 */
	public PageResult<BillOutStoreFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<BillOutStoreFileResDto> pageResult = new PageResult<BillOutStoreFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.FOUR);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<BillOutStoreFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<BillOutStoreFileResDto> queryFileList(Integer billOutStoreId) {
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(billOutStoreId);
		fileAttReqDto.setBusType(BaseConsts.FOUR);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<BillOutStoreFileResDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<BillOutStoreFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<BillOutStoreFileResDto> list = new LinkedList<BillOutStoreFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			BillOutStoreFileResDto result = new BillOutStoreFileResDto();
			result.setId(model.getId());
			result.setBusId(model.getBusId());
			result.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, model.getBusType() + ""));
			result.setName(model.getName());
			result.setType(model.getType());
			result.setCreateAt(model.getCreateAt());
			result.setCreator(model.getCreator());
			List<CodeValue> operList = getOperList();
			result.setOpertaList(operList);
			list.add(result);
		}
		return list;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BillOutStoreFileResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @return
	 */
	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DOWNLOAD);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}

	public boolean isOverBillOutStoreMaxLine(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		billOutStoreSearchReqDto.setUserId(getUser().getId());
		int count = billOutStoreDao.queryCountByCon(billOutStoreSearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("出库单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillOutStoreExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_out_store_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.FOUR);
			asyncExcelService.addAsyncExcel(billOutStoreSearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillOutStoreExport(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillOutStoreResDto> billOutStoreResDtoList = queryAllBillOutStoreList(billOutStoreSearchReqDto);
		model.put("billOutStoreList", billOutStoreResDtoList);
		return model;
	}

	/**
	 * 供应商退款类型 获取出库单的数据
	 * 
	 * @param req
	 * @return
	 */
	public PageResult<ReceiptOutRelResDto> queryOutStoreResutByCon(BillOutStoreDetailSearchReqDto dto) {
		dto.setUserId(ServiceSupport.getUser().getId());
		PageResult<ReceiptOutRelResDto> pageResult = new PageResult<ReceiptOutRelResDto>();
		int offSet = PageUtil.getOffSet(dto.getPage(), dto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, dto.getPer_page());
		// 查询当前出库单的数据
		List<BillOutStoreDetailResDto> outStores = billOutStoreDao.queryOutStoreResultsByCon(dto, rowBounds);
		List<ReceiptOutRelResDto> receiveResDtos = new ArrayList<ReceiptOutRelResDto>();
		for (BillOutStoreDetailResDto billOutStoreDetailResDto : outStores) {
			ReceiptOutRelResDto receiveResDto = convertToOutStore(billOutStoreDetailResDto);
			receiveResDtos.add(receiveResDto);
		}
		/*** 新增业务 查询铺货结算单 退款金额减去已付金额不为0 的数据 ***/
		List<PoLineModel> poLineModelList = purchaseOrderService.queryPoTitleResultByOrderType(dto);
		for (PoLineModel poLineModel : poLineModelList) {
			ReceiptOutRelResDto poModelDto = convertToPoLineModel(poLineModel);
			receiveResDtos.add(poModelDto);
		}
		pageResult.setItems(receiveResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), dto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(dto.getPage());
		pageResult.setPer_page(dto.getPer_page());
		return pageResult;
	}

	/**
	 * 封装当前水单出库单的数据
	 * 
	 * @param billOutStoreDetailResDto
	 * @return
	 */
	public ReceiptOutRelResDto convertToOutStore(BillOutStoreDetailResDto billOutStoreDetailResDto) {
		ReceiptOutRelResDto resDto = new ReceiptOutRelResDto();
		resDto.setId(billOutStoreDetailResDto.getId());
		resDto.setCreateAt(billOutStoreDetailResDto.getSendDate());// 发货日期
		resDto.setBusiUnit(
				cacheService.getSubjectNcByIdAndKey(billOutStoreDetailResDto.getBusiUnit(), CacheKeyConsts.BUSI_UNIT)); // 经营单位
		resDto.setCustName(
				cacheService.getSubjectNcByIdAndKey(billOutStoreDetailResDto.getCustomerId(), CacheKeyConsts.CUSTOMER));// 客户名称
		resDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				billOutStoreDetailResDto.getCurrencyType() + ""));// 币种
		resDto.setReceivedAmount(billOutStoreDetailResDto.getReceivedAmount());// 应收金额
		resDto.setProjectName(cacheService.showProjectNameById(billOutStoreDetailResDto.getProjectId()));
		resDto.setWriteOffAmount(DecimalUtil.subtract(billOutStoreDetailResDto.getSendAmount(),
				billOutStoreDetailResDto.getReceivedAmount()));
		resDto.setBillType(BaseConsts.THREE);// 出库单
		resDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_TYPE, (BaseConsts.THREE) + ""));
		return resDto;
	}

	/**
	 * 根据水单的id去回写出库单的回款金额
	 * 
	 * @param bankReceipt
	 */
	public BigDecimal updateOutBillRecAmountByCon(BankReceipt bankReceipt) {
		// 根据水单ID查询水单核销出库单关联表
		ReceiptOutRelReqDto relReqDto = new ReceiptOutRelReqDto();
		BigDecimal countWriteAmount = BigDecimal.ZERO;// 出库单核销总金额
		relReqDto.setReceiptId(bankReceipt.getId());
		List<ReceiptOutStoreRel> outStoreRels = receiptOutStoreRelDao.queryResultsByCon(relReqDto);
		if (outStoreRels != null && outStoreRels.size() > 0) {
			for (ReceiptOutStoreRel receiptOutStoreRel : outStoreRels) {
				if (receiptOutStoreRel.getBillType() == BaseConsts.THREE) {
					BillOutStore billOutStore = billOutStoreDao.queryEntityById(receiptOutStoreRel.getBillOutId());// 获取出库单数据
					if (billOutStore != null) {
						if (DecimalUtil.gt(billOutStore.getReceivedAmount(), billOutStore.getSendAmount())) {// 如果回款金额大于并等于发货金额
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单核销出库单回款金额大于发货金额");
						}
						countWriteAmount = DecimalUtil.add(countWriteAmount,
								null == billOutStore.getSendAmount() ? BigDecimal.ZERO : billOutStore.getSendAmount());
					}
				}
			}
		}
		return countWriteAmount;
	}

	/**
	 * 封装当前水单结算单的数据
	 * 
	 * @param billOutStoreDetailResDto
	 * @return
	 */
	public ReceiptOutRelResDto convertToPoLineModel(PoLineModel poLineModel) {
		ReceiptOutRelResDto resDto = new ReceiptOutRelResDto();
		resDto.setId(poLineModel.getId());
		resDto.setCreateAt(poLineModel.getOrderTime());// 创建时间
		resDto.setBusiUnit(
				cacheService.getSubjectNcByIdAndKey(poLineModel.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT)); // 经营单位
		resDto.setCustName(cacheService.getSubjectNcByIdAndKey(poLineModel.getCustomerId(), CacheKeyConsts.CUSTOMER));// 客户名称
		resDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				poLineModel.getCurrencyId() + ""));// 币种
		resDto.setReceivedAmount(DecimalUtil.multiply(new BigDecimal("-1"), poLineModel.getPaidAmount()));// 退款金额
		resDto.setProjectName(cacheService.showProjectNameById(poLineModel.getProjectId()));
		resDto.setWriteOffAmount(DecimalUtil.multiply(new BigDecimal("-1"),
				DecimalUtil.subtract(poLineModel.getRefundAmount(), poLineModel.getPaidAmount())));// 已付款金额
		resDto.setBillType(BaseConsts.INT_33);// 采购单
		resDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, (BaseConsts.INT_33) + ""));
		return resDto;
	}

	/**
	 * 查询出库单数据 打印
	 * 
	 * @param billOutStoreSearchReqDto
	 * @return
	 */
	public Result<BillOutStoreResDto> queryBillOutPrint(BillOutStoreSearchReqDto billOutStoreSearchReqDto) {
		Result<BillOutStoreResDto> result = new Result<BillOutStoreResDto>();
		List<Integer> ids = billOutStoreSearchReqDto.getIds();
		BillOutStoreResDto billOutStoreResDto = new BillOutStoreResDto();
		if (!CollectionUtils.isEmpty(ids)) {
			BillOutStore billOutStore = billOutStoreDao.queryEntityById(ids.get(BaseConsts.ZERO));
			if (billOutStore != null) {

				billOutStoreResDto.setId(billOutStore.getId());
				if (billOutStore.getCustomerId() != null) {
					BaseSubject baseSubject = cacheService.getCustomerById(billOutStore.getCustomerId());
					if (baseSubject != null) {
						billOutStoreResDto.setCustomerName(baseSubject.getChineseName());
						billOutStoreResDto.setCustomerPhone(baseSubject.getRegPhone());
					}
				}
				BaseProject baseProject = cacheService.getProjectById(billOutStore.getProjectId());
				if (null != baseProject) {
					BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
					if (null != busiUnit) {
						billOutStoreResDto.setBusinessChineseName(busiUnit.getChineseName());
						billOutStoreResDto.setBusinessUnitPhone(busiUnit.getRegPhone());
					}
				}

			}
		}
		result.setItems(billOutStoreResDto);
		return result;
	}

}
