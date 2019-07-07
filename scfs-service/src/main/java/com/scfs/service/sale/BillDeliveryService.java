package com.scfs.service.sale;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.base.entity.BaseAddressDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.BillOutStorePickDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillDeliveryDtlSearchReqDto;
import com.scfs.domain.sale.dto.req.BillDeliveryReqDto;
import com.scfs.domain.sale.dto.req.BillDeliverySearchReqDto;
import com.scfs.domain.sale.dto.req.BillOutStoreDetailSearchReqDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryDtlResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryFileResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;
import com.scfs.domain.sale.dto.resp.BillOutStoreDetailResDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.domain.sale.entity.BillDeliveryDtlExcel;
import com.scfs.domain.sale.entity.BillDeliveryDtlSum;
import com.scfs.domain.sale.entity.BillDeliveryExcel;
import com.scfs.domain.sale.entity.BillDeliverySum;
import com.scfs.service.audit.BillDeliveryAuditService;
import com.scfs.service.audit.BillReturnAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.logistics.BillInStoreTallyDtlService;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.logistics.StlService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年10月27日.
 */
@Service
public class BillDeliveryService {
	@Value("${billDelivery.import.xmlConfig}")
	private String billDeliveryXmlConfig;
	@Autowired
	private BillDeliveryDao billDeliveryDao;
	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private BaseAddressDao baseAddressDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private BillOutStorePickDtlService billOutStorePickDtlService;
	@Autowired
	private BillDeliveryAuditService billDeliveryAuditService;
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private BillDeliveryDtlService billDeliveryDtlService;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private StlService stlService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	private BillInStoreTallyDtlService billInStoreTallyDtlService;
	@Autowired
	private BillReturnAuditService billReturnAuditService;
	@Autowired
	private BaseAccountDao accountDao;
	@Autowired
	private AuditFlowService auditFlowService;

	/**
	 * 查询销售单
	 *
	 * @param billDeliverySearchReqDto
	 * @return
	 */
	public PageResult<BillDeliveryResDto> queryBillDeliveryResultsByCon(
			BillDeliverySearchReqDto billDeliverySearchReqDto) {
		PageResult<BillDeliveryResDto> result = new PageResult<BillDeliveryResDto>();

		int offSet = PageUtil.getOffSet(billDeliverySearchReqDto.getPage(), billDeliverySearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, billDeliverySearchReqDto.getPer_page());

		billDeliverySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<BillDelivery> billDeliveryList = billDeliveryDao.queryResultsByCon(billDeliverySearchReqDto, rowBounds);
		List<BillDeliveryResDto> billDeliveryResDtoList = convertToResDto(billDeliveryList);
		result.setItems(billDeliveryResDtoList);
		String totalStr = querySumBillDelivery(billDeliverySearchReqDto);
		if (StringUtils.isNotBlank(totalStr)) {
			result.setTotalStr(totalStr);
		}
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), billDeliverySearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(billDeliverySearchReqDto.getPage());
		result.setPer_page(billDeliverySearchReqDto.getPer_page());

		return result;
	}

	/**
	 * 查询销售单(不分页)
	 *
	 * @param billDeliverySearchReqDto
	 * @return
	 */
	public List<BillDeliveryResDto> queryAllBillDeliveryResultsByCon(
			BillDeliverySearchReqDto billDeliverySearchReqDto) {
		if (null == billDeliverySearchReqDto.getUserId()) {
			billDeliverySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<BillDelivery> billDeliveryList = billDeliveryDao.queryResultsByCon(billDeliverySearchReqDto);
		List<BillDeliveryResDto> billDeliveryResDtoList = convertToResDto(billDeliveryList);

		return billDeliveryResDtoList;
	}

	private String querySumBillDelivery(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		String totalStr = "";
		if (billDeliverySearchReqDto.getNeedSum() != null && billDeliverySearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<BillDeliverySum> billDeliverySumList = billDeliveryDao.querySumBillDelivery(billDeliverySearchReqDto);
			totalStr = getTotalStr(billDeliverySearchReqDto, billDeliverySumList);
		}
		return totalStr;
	}

	private String getTotalStr(BillDeliverySearchReqDto billDeliverySearchReqDto,
			List<BillDeliverySum> billDeliverySumList) {
		String totalStr = "";
		if (!CollectionUtils.isEmpty(billDeliverySumList)) {
			BigDecimal totalRequiredSendNum = BigDecimal.ZERO;
			BigDecimal totalRequiredSendAmount = BigDecimal.ZERO;
			String totalRequiredSendAmountStr = "";

			for (BillDeliverySum billDeliverySum : billDeliverySumList) {
				totalRequiredSendNum = DecimalUtil.add(totalRequiredSendNum,
						null == billDeliverySum.getTotalRequiredSendNum() ? BigDecimal.ZERO
								: billDeliverySum.getTotalRequiredSendNum());
				BigDecimal cnyTotalRequiredSendAmount = BigDecimal.ZERO;
				if (null != billDeliverySum.getCurrencyType()) {
					cnyTotalRequiredSendAmount = ServiceSupport.amountNewToRMB(
							null == billDeliverySum.getTotalRequiredSendAmount() ? BigDecimal.ZERO
									: billDeliverySum.getTotalRequiredSendAmount(),
							billDeliverySum.getCurrencyType(), null);
				}
				totalRequiredSendAmount = DecimalUtil.add(totalRequiredSendAmount,
						null == cnyTotalRequiredSendAmount ? BigDecimal.ZERO : cnyTotalRequiredSendAmount);
			}
			totalRequiredSendAmountStr = DecimalUtil.toAmountString(totalRequiredSendAmount);
			if (billDeliverySearchReqDto.getNeedSum().equals(BaseConsts.ONE)) {
				totalStr = "销售数量：" + DecimalUtil.toQuantityString(totalRequiredSendNum) + "；销售金额："
						+ (StringUtils.isBlank(totalRequiredSendAmountStr) == true ? "0.00"
								: totalRequiredSendAmountStr)
						+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
			} else if (billDeliverySearchReqDto.getNeedSum().equals(BaseConsts.TWO)) {
				totalStr = "退货数量：" + DecimalUtil.toQuantityString(totalRequiredSendNum) + "；退货金额："
						+ (StringUtils.isBlank(totalRequiredSendAmountStr) == true ? "0.00"
								: totalRequiredSendAmountStr)
						+ BaseConsts.STRING_BLANK_SPACE + BaseConsts.CURRENCY_UNIT_MAP.get(BaseConsts.ONE);
			}

		}
		return totalStr;
	}

	private List<BillDeliveryResDto> convertToResDto(List<BillDelivery> billDeliveryList) {
		List<BillDeliveryResDto> billDeliveryResDtoList = new ArrayList<BillDeliveryResDto>(5);
		if (CollectionUtils.isEmpty(billDeliveryList)) {
			return billDeliveryResDtoList;
		}
		for (BillDelivery billDelivery : billDeliveryList) {
			BillDeliveryResDto billDeliveryResDto = convertToResDto(billDelivery);
			billDeliveryResDto.setOpertaList(getOperList(billDelivery));
			billDeliveryResDtoList.add(billDeliveryResDto);
		}
		return billDeliveryResDtoList;
	}

	public BillDeliveryResDto convertToResDto(BillDelivery billDelivery) {
		BillDeliveryResDto billDeliveryResDto = new BillDeliveryResDto();
		if (null != billDelivery) {
			BeanUtils.copyProperties(billDelivery, billDeliveryResDto);
			billDeliveryResDto
					.setReceiveProjectName(cacheService.getProjectNameById(billDelivery.getReceiveProjectId()));
			BaseSubject reciveWareHouse = cacheService.getWarehouseById(billDelivery.getReceiveWarehouseId());
			if (reciveWareHouse != null) {
				billDeliveryResDto.setReceiveWarehouseName(reciveWareHouse.getNoName());
			}
			BaseSubject supplier = cacheService.getSupplierById(billDelivery.getReceiveSupplierId());
			if (supplier != null) {
				billDeliveryResDto.setReceiveSupplierName(supplier.getNoName());
			}
			if (billDelivery.getStatus() != null) {
				if (billDelivery.getBillType().equals(BaseConsts.ONE)
						|| billDelivery.getBillType() == BaseConsts.THREE) { // 1-外部销售
																				// 3内部销售
					billDeliveryResDto.setStatusName(ServiceSupport.getValueByBizCode(
							BizCodeConsts.BILL_DELIVERY_STATUS, Integer.toString(billDelivery.getStatus())));
				} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
					billDeliveryResDto.setStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_RETURN_STATUS,
							Integer.toString(billDelivery.getStatus())));
				}
			}
			if (billDelivery.getBillType() != null) {
				billDeliveryResDto.setBillTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_DELIVERY_TYPE_SELECT, Integer.toString(billDelivery.getBillType())));
			}
			if (billDelivery.getTransferMode() != null) {
				billDeliveryResDto.setTransferModeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.BILL_OUT_STORE_TRANSFER_MODE, Integer.toString(billDelivery.getTransferMode())));
			}
			if (billDelivery.getCurrencyType() != null) {
				billDeliveryResDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.DEFAULT_CURRENCY_TYPE, Integer.toString(billDelivery.getCurrencyType())));
				billDeliveryResDto.setCurrencyTypeEnName(ServiceSupport.getValueByBizCode(
						BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN, billDelivery.getCurrencyType().toString()));
			}
			if (billDelivery.getSignStandard() != null) {
				billDeliveryResDto.setSignStandardName(ServiceSupport.getValueByBizCode(BizCodeConsts.SIGN_STANDARD,
						Integer.toString(billDelivery.getSignStandard())));
			}

			billDeliveryResDto.setProjectName(cacheService.showProjectNameById(billDeliveryResDto.getProjectId()));
			billDeliveryResDto.setWarehouseName(cacheService
					.showSubjectNameByIdAndKey(billDeliveryResDto.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
			billDeliveryResDto.setCustomerName(cacheService
					.showSubjectNameByIdAndKey(billDeliveryResDto.getCustomerId(), CacheKeyConsts.CUSTOMER));
			List<BaseAddress> baseAddressList = baseAddressDao
					.queryResultsyBySubjectId(billDeliveryResDto.getWarehouseId());
			if (!CollectionUtils.isEmpty(baseAddressList)) {
				BaseAddress baseAddress = baseAddressList.get(0);
				baseAddress = cacheService.getAddressById(baseAddress.getId());
				if (null != baseAddress) {
					billDeliveryResDto.setSendWarehouseAddressName(baseAddress.getShowValue());
					billDeliveryResDto.setSendCityName(baseAddress.getCityName());
				}
			}
			BaseAddress customerAddress = cacheService.getAddressById(billDeliveryResDto.getCustomerAddressId());
			if (null != customerAddress) {
				billDeliveryResDto.setCustomerAddress(customerAddress.getShowValue());
				billDeliveryResDto.setCityName(customerAddress.getCityName());
				billDeliveryResDto.setContactPerson(customerAddress.getContactPerson());
				billDeliveryResDto.setMobilePhone(customerAddress.getMobilePhone());
				billDeliveryResDto.setTelephone(customerAddress.getTelephone());
			}
			BaseProject baseProject = cacheService.getProjectById(billDeliveryResDto.getProjectId());
			BaseSubject busiUnit = null;
			if (null != baseProject) {
				busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
				if (null != busiUnit) {
					billDeliveryResDto.setBusinessUnitName(busiUnit.getChineseName());
					billDeliveryResDto.setBusinessUnitEnglishName(busiUnit.getEnglishName());
					billDeliveryResDto.setBusinessUnitAddress(busiUnit.getOfficeAddress());
					billDeliveryResDto.setBusinessUnitPhone(busiUnit.getRegPhone());
				}
			}
			BigDecimal requiredSendAmount = (null == billDeliveryResDto.getRequiredSendAmount() ? BigDecimal.ZERO
					: billDeliveryResDto.getRequiredSendAmount());
			billDeliveryResDto.setRequiredSendAmountStr(DecimalUtil.toAmountString(requiredSendAmount));
			BigDecimal requiredSendNum = (null == billDeliveryResDto.getRequiredSendNum() ? BigDecimal.ZERO
					: billDeliveryResDto.getRequiredSendNum());
			billDeliveryResDto.setRequiredSendNumStr(DecimalUtil.toAmountString(requiredSendNum));

			BaseSubject wareProject = cacheService.getBaseSubjectById(billDelivery.getWarehouseId());// 仓库
			billDeliveryResDto.setWarehouseAddress(wareProject.getOfficeAddress());
			BaseSubject customerProject = cacheService.getBaseSubjectById(billDelivery.getCustomerId());// 客户信息
			billDeliveryResDto.setCustomerChineseName(customerProject.getChineseName());
			billDeliveryResDto.setCustomerEnglishName(customerProject.getEnglishName());
			billDeliveryResDto.setCustomerAfficeName(customerProject.getOfficeAddress());
			billDeliveryResDto.setCustomerRegPhone(customerProject.getRegPhone());
			List<BillDeliveryDtl> billDeliveryDtls = billDeliveryDtlDao
					.queryResultsByBillDeliveryId(billDelivery.getId());// 获取商品信息
			String goodUnit = "";
			if (!CollectionUtils.isEmpty(billDeliveryDtls)) {
				for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtls) {
					if (null != billDeliveryDtl.getGoodsId()) {
						BaseGoods baseGoods = cacheService.getGoodsById(billDeliveryDtl.getGoodsId());
						// 获取单位
						goodUnit = baseGoods.getUnit();
					}
				}
			}
			billDeliveryResDto.setGoodUnit(goodUnit);

			if (null != busiUnit) {
				QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
				queryAccountReqDto.setSubjectId(busiUnit.getId());
				queryAccountReqDto.setState(BaseConsts.ONE);
				// queryAccountReqDto.setCurrencyType(billDelivery.getCurrencyType());
				List<BaseAccount> baseAccount = accountDao.queryFicBySubjectId(queryAccountReqDto);// 获取客户帐户信息
				if (baseAccount != null && baseAccount.size() > BaseConsts.ZERO) {
					billDeliveryResDto.setBankName(baseAccount.get(BaseConsts.ZERO).getBankName());
					billDeliveryResDto.setSubjectName(baseAccount.get(BaseConsts.ZERO).getAccountor());
					billDeliveryResDto.setBankAddress(baseAccount.get(BaseConsts.ZERO).getBankAddress());
					billDeliveryResDto.setAccountNo(baseAccount.get(BaseConsts.ZERO).getAccountNo());
					billDeliveryResDto.setBankCode(baseAccount.get(BaseConsts.ZERO).getBankCode());
					billDeliveryResDto.setPhoneNumber(baseAccount.get(BaseConsts.ZERO).getPhoneNumber());
					billDeliveryResDto.setDefaultCurrency(ServiceSupport.getValueByBizCode(
							BizCodeConsts.DEFAULT_CURRENCY_TYPE, billDelivery.getCurrencyType() + ""));
				}
			}
		}
		return billDeliveryResDto;
	}

	private List<CodeValue> getOperList(BillDelivery billDelivery) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(billDelivery);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BillDeliveryResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(BillDelivery billDelivery) {
		if (billDelivery.getStatus() == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		switch (billDelivery.getStatus()) {
		// 状态, 1-待提交 2-待业务审核 3-待财务审核 4-待发货(待收货) 5-已发货(已收货)
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.INT_30:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.FOUR:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.FIVE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}
		return opertaList;
	}

	/**
	 * 新增销售单头
	 *
	 * @param billDelivery
	 * @return
	 */
	public BillDelivery addBillDelivery(BillDelivery billDelivery) {
		billDelivery.setBillNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_BILL_DELIVERY,
				SeqConsts.BILL_DELIVERY_NO, BaseConsts.INT_13));
		billDelivery.setRequiredSendNum(BigDecimal.ZERO);
		billDelivery.setRequiredSendAmount(BigDecimal.ZERO);
		billDelivery.setCostAmount(BigDecimal.ZERO);
		billDelivery.setPoAmount(BigDecimal.ZERO);
		billDelivery.setStatus(BaseConsts.ONE); // 待提交
		billDelivery.setCreator(ServiceSupport.getUser().getChineseName());
		billDelivery.setCreatorId(ServiceSupport.getUser().getId());
		billDelivery.setReturnTime(billDelivery.getRequiredSendDate()); // 回款日期
		billDelivery.setIsChangePrice(BaseConsts.ZERO); // 默认 0-未改价
		int result = billDeliveryDao.insert(billDelivery);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_DELIVERY_ADD_ERROR);
		}
		return billDelivery;
	}

	/**
	 * 更新销售单头
	 *
	 * @param billDelivery
	 * @return
	 */
	public void updateBillDelivery(BillDelivery billDelivery) {
		billDeliveryDao.updateById(billDelivery);
	}

	/**
	 * 查询销售单销售金额
	 *
	 * @param billDeliveryId
	 * @return
	 */
	public BigDecimal queryRequiredSendAmountById(Integer billDeliveryId) {
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryId);
		billDelivery = billDeliveryDao.queryEntityById(billDelivery);
		if (null != billDelivery) {
			return null == billDelivery.getRequiredSendAmount() ? BigDecimal.ZERO
					: billDelivery.getRequiredSendAmount();
		} else {
			return BigDecimal.ZERO;
		}
	}

	/**
	 * 更新销售单头
	 *
	 * @param billDelivery
	 * @return
	 * @throws ParseException
	 */
	public void updateBillDeliveryById(BillDelivery billDelivery) throws Exception {
		BillDelivery billDeliveryRes = billDeliveryDao.queryAndLockEntityById(billDelivery.getId());
		if (billDeliveryRes.getStatus().equals(BaseConsts.ONE)
				&& billDeliveryRes.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			if (null != billDelivery.getSignStandard()) {
				if (billDelivery.getSignStandard().equals(BaseConsts.ZERO)) { // 0-身份证
					billDelivery.setOfficialSeal("");
				} else if (billDelivery.getSignStandard().equals(BaseConsts.ONE)) { // 1-公章
					billDelivery.setCertificateId("");
					billDelivery.setCertificateName("");
				}
			}

			billDelivery.setCustomerAddressId(
					null == billDelivery.getCustomerAddressId() ? -1 : billDelivery.getCustomerAddressId());
			billDelivery.setReturnTime(billDelivery.getRequiredSendDate()); // 回款日期

			billDeliveryDao.updateById(billDelivery);
			reCalcPrice(billDelivery.getId(), billDelivery.getReturnTime());
		} else {
			throw new BaseException(ExcMsgEnum.BILL_DELIVERY_UPDATE_ERROR);
		}
	}

	/**
	 * 查询销售单详情
	 *
	 * @param billDelivery
	 * @return
	 */
	public Result<BillDeliveryResDto> detailBillDeliveryById(BillDelivery billDelivery) {
		Result<BillDeliveryResDto> result = new Result<BillDeliveryResDto>();
		BillDelivery billDeliveryRes = billDeliveryDao.queryEntityById(billDelivery);
		BillDeliveryResDto billDeliveryResDto = convertToResDto(billDeliveryRes);
		result.setItems(billDeliveryResDto);
		return result;
	}

	/**
	 * 查询销售退货详情(打印)
	 *
	 * @param billDelivery
	 * @return
	 */
	public Result<BillDeliveryResDto> printDetailBillReturn(BillDelivery billDelivery) {
		Result<BillDeliveryResDto> result = new Result<BillDeliveryResDto>();
		BillDelivery billDeliveryRes = billDeliveryDao.queryEntityById(billDelivery);
		BillDeliveryResDto billDeliveryResDto = convertToResDto(billDeliveryRes);
		// 查询项目条款
		ProjectItem projectItem = projectItemService.getProjectItem(billDeliveryResDto.getProjectId()); // 条款
		billDeliveryResDto.setFundAccountPeriod(
				null == projectItem.getFundAccountPeriod() ? BaseConsts.ZERO : projectItem.getFundAccountPeriod());// 合作账期

		result.setItems(billDeliveryResDto);
		return result;
	}

	/**
	 * 批量删除销售单
	 *
	 * @param ids
	 * @return
	 */
	public void deleteBillDeliveryByIds(List<Integer> ids) {
		for (Integer id : ids) {
			BillDelivery billDelivery = billDeliveryDao.queryAndLockEntityById(id);
			if (billDelivery.getStatus().equals(BaseConsts.ONE) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
				billDelivery.setDeleterId(ServiceSupport.getUser().getId());
				billDelivery.setDeleter(ServiceSupport.getUser().getChineseName());
				billDelivery.setDeleteAt(new Date());
				billDelivery.setIsDelete(BaseConsts.ONE); // 已删除
				billDeliveryDao.updateById(billDelivery);

				if (billDelivery.getBillType().equals(BaseConsts.ONE)
						|| billDelivery.getBillType() == BaseConsts.THREE) { // 1-销售
					releaseStlSaleNum(id);
				} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
					releaseReturnNum(id);
				}
			} else {
				throw new BaseException(ExcMsgEnum.BILL_DELIVERY_DELETE_ERROR);
			}
		}
	}

	/**
	 * 还原库存锁定的销售数量
	 *
	 * @param billDeliveryId
	 */
	public void releaseStlSaleNum(Integer billDeliveryId) {
		List<BillDeliveryDtl> billDeliveryDtls = billDeliveryDtlDao.queryResultsByBillDeliveryId(billDeliveryId);
		if (!CollectionUtils.isEmpty(billDeliveryDtls)) {
			for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtls) {
				if (null != billDeliveryDtl.getStlId()) {
					// 释放库存的销售数量
					stlService.releaseStlSaleNum(billDeliveryDtl.getStlId(), billDeliveryDtl.getRequiredSendNum());
				}
			}
		}
	}

	/**
	 * 还原出库单拣货明细的退货数量
	 *
	 * @param billDeliveryId
	 */
	private void releaseReturnNum(Integer billDeliveryId) {
		List<BillDeliveryDtl> billDeliveryDtls = billDeliveryDtlDao.queryResultsByBillDeliveryId(billDeliveryId);
		if (!CollectionUtils.isEmpty(billDeliveryDtls)) {
			for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtls) {
				if (null != billDeliveryDtl.getStlId()) {
					billDeliveryDtlService.updateReturnNum(billDeliveryDtl, billDeliveryDtl, null, BaseConsts.THREE);
				}
			}
		}
	}

	public Integer autoDelivery(PurchaseOrderTitle poTitle, List<Stl> stls, Date sendDate) {
		// 1.生成销售单
		BillDelivery billDelivery = convertToDeliveryByPo(poTitle, sendDate);

		BaseProject baseProject = cacheService.getProjectById(billDelivery.getProjectId());// 获取商务信息
		billDelivery.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
				: cacheService.getUserByid(baseProject.getBusinessManagerId()).getChineseName());
		billDelivery.setCreatorId(baseProject.getBusinessManagerId());

		billDeliveryDao.insert(billDelivery);
		// 2.生成销售单明细
		if (!CollectionUtils.isEmpty(stls)) {
			BillDeliveryReqDto billDeliveryReqDto = new BillDeliveryReqDto();
			billDeliveryReqDto.setId(billDelivery.getId());
			billDeliveryReqDto.setStlList(stls);
			billDeliveryDtlService.addBillDeliveryDtlsByStl(billDeliveryReqDto);
		}
		// 3.提交销售单，生成出库单
		submitBillDelivery(billDelivery);
		// 4.跳过审核，更新销售单状态为待发货
		BillDelivery bdUpd = new BillDelivery();
		bdUpd.setId(billDelivery.getId());
		bdUpd.setStatus(BaseConsts.FOUR);
		billDeliveryDao.updateById(bdUpd);
		// 5.跳过审核，更新出库单状态为待发货
		BillOutStore billOutStore = billOutStoreService.queryValidBillOutStoreByBillDeliveryId(billDelivery.getId());
		BillOutStore bosUpd = new BillOutStore();
		bosUpd.setId(billOutStore.getId());
		bosUpd.setStatus(BaseConsts.FOUR);
		billOutStoreService.updateBillOutStoreById(bosUpd);
		return billDelivery.getId();
	}

	private BillDelivery convertToDeliveryByPo(PurchaseOrderTitle poTitle, Date sendDate) {
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setAffiliateNo(poTitle.getAppendNo());
		billDelivery.setBillType(BaseConsts.ONE);// 销售销售
		billDelivery.setProjectId(poTitle.getProjectId());
		billDelivery.setWarehouseId(poTitle.getWarehouseId());
		billDelivery.setCustomerId(poTitle.getCustomerId());
		billDelivery.setStatus(BaseConsts.ONE);// 待发货
		billDelivery.setBillNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_BILL_DELIVERY,
				SeqConsts.BILL_DELIVERY_NO, BaseConsts.INT_13));
		billDelivery.setRequiredSendDate(sendDate == null ? new Date() : sendDate); // 应发日期
		billDelivery.setRequiredSendNum(BigDecimal.ZERO);
		billDelivery.setRequiredSendAmount(BigDecimal.ZERO);
		billDelivery.setCostAmount(BigDecimal.ZERO);
		billDelivery.setPoAmount(BigDecimal.ZERO);
		billDelivery.setCurrencyType(poTitle.getCurrencyId());
		billDelivery.setCreateAt(new Date());
		billDelivery.setCreator(ServiceSupport.getUser().getChineseName());
		billDelivery.setCreatorId(ServiceSupport.getUser().getId());
		billDelivery.setIsDelete(BaseConsts.ZERO);
		billDelivery.setReturnTime(billDelivery.getRequiredSendDate()); // 回款日期
		billDelivery.setFlyOrderFlag(poTitle.getFlyOrderFlag());
		return billDelivery;
	}

	/**
	 * 提交销售单处理，不包含审核 TODO.
	 *
	 * @param billDelivery
	 */
	public AuditNode submitBillDelivery(BillDelivery billDelivery) {
		AuditNode startAuditNode = null;
		billDelivery = billDeliveryDao.queryAndLockEntityById(billDelivery.getId());
		if (billDelivery.getStatus().equals(BaseConsts.ONE) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 1-待提交
			ProjectItem projectItem = projectItemService.getProjectItem(billDelivery.getProjectId());
			if (projectItem == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单项目无条款，不能提交！");
			}
			// 校验明细数量
			validateSubmit(billDelivery);

			if (billDelivery.getBillType().equals(BaseConsts.ONE) || billDelivery.getBillType() == BaseConsts.THREE) { // 1-销售
				/**
				 * 根据销售单自动生成出库单、出库明细、拣货明细， 同时校验库存，校验不通过，提交失败 策略：1、库存先进先出(自动)
				 * 2、指定库存记录(销售界面操作)
				 */
				addBillOutStore(billDelivery);
			} else if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
				/**
				 * 销售退货生成入库单(改成审核时生成)
				 */
				// addBillInStore(billDelivery);
			}
			if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
				startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_17, billDelivery.getProjectId());
			} else {
				startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.TWO, billDelivery.getProjectId());
			}
			if (null == startAuditNode) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
			}
			billDelivery.setSubmitter(ServiceSupport.getUser().getChineseName());
			billDelivery.setSubmitterId(ServiceSupport.getUser().getId());
			billDelivery.setSubmitTime(new Date());
			billDelivery.setStatus(startAuditNode.getAuditNodeState()); // 待财务专员审核
			int result = billDeliveryDao.updateById(billDelivery);
			if (result <= 0) {
				throw new BaseException(ExcMsgEnum.BILL_DELIVERY_UPDATE_ERROR);
			}
		} else {
			throw new BaseException(ExcMsgEnum.BILL_DELIVERY_SUBMIT_STATUS_ERROR);
		}
		return startAuditNode;
	}

	/**
	 * 提交销售单
	 *
	 * @param billDelivery
	 * @return
	 */
	public void submitBillDeliveryById(BillDelivery billDelivery) {
		AuditNode startAuditNode = submitBillDelivery(billDelivery);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}

		billDelivery = billDeliveryDao.queryAndLockEntityById(billDelivery.getId());
		// 开始业务审核
		if (billDelivery.getBillType().equals(BaseConsts.TWO)) { // 2-退货
			billReturnAuditService.startAudit(billDelivery, startAuditNode);
			return;
		}
		billDeliveryAuditService.startAudit(billDelivery, startAuditNode);
	}

	/**
	 * 驳回销售单
	 *
	 * @param billDelivery
	 * @return
	 */
	public void rejectbillDeliveryById(BillDelivery billDelivery) {
		billDelivery = billDeliveryDao.queryAndLockEntityById(billDelivery.getId());
		if (billDelivery.getStatus().equals(BaseConsts.FOUR) && billDelivery.getIsDelete().equals(BaseConsts.ZERO)) { // 4-待发货
			billDelivery.setStatus(BaseConsts.ONE);
			billDeliveryDao.updateById(billDelivery);

			BillOutStore billOutStore = billOutStoreService
					.queryValidBillOutStoreByBillDeliveryId(billDelivery.getId());
			// 作废出库单
			billOutStoreService.deleteBillOutStoreById(billOutStore, BaseConsts.THREE);
		} else {
			throw new BaseException(ExcMsgEnum.BILL_DELIVERY_REJECT_STATUS_ERROR);
		}
	}

	/**
	 * 更新打印次数
	 */
	public void updatePrintNum(Integer id) {
		BillDelivery billDelivery = billDeliveryDao.queryAndLockEntityById(id);
		if (null != billDelivery) {
			billDelivery.setPrintNum(billDelivery.getPrintNum() + 1);
			billDeliveryDao.updateById(billDelivery);
		}
	}

	/**
	 * 新增出库单，当销售单提交后，系统自动生成
	 *
	 * @param billDelivery
	 * @return
	 * @throws Exception
	 */
	private void addBillOutStore(BillDelivery billDelivery) {
		BillOutStore billOutStore = createBillOutStoreEntity(billDelivery);
		createBillOutStoreDtlEntity(billOutStore);
	}

	/**
	 * 新增入库单，当销售单审核通过后，系统自动生成
	 *
	 * @param billDelivery
	 * @return
	 * @throws Exception
	 */
	public void addBillInStore(BillDelivery billDelivery) {
		BillInStore billInStore = createBillInStoreEntity(billDelivery);
		createBillInStoreDtlEntity(billInStore);
	}

	/**
	 * 创建出库单头
	 *
	 * @param billDelivery
	 * @return
	 */
	private BillOutStore createBillOutStoreEntity(BillDelivery billDelivery) {
		BillOutStore billOutStore = new BillOutStore();
		billOutStore.setBillNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_BILL_OUT_STORE,
				SeqConsts.BILL_OUT_STORE_NO, BaseConsts.INT_13));
		billOutStore.setBillType(billDelivery.getBillType());
		if (billDelivery.getBillType() == BaseConsts.THREE) {// 内部销售，对应出库单类型为6
			billOutStore.setBillType(BaseConsts.SIX);
		}
		billOutStore.setBillDeliveryId(billDelivery.getId());
		billOutStore.setAffiliateNo(billDelivery.getAffiliateNo());
		billOutStore.setProjectId(billDelivery.getProjectId());
		billOutStore.setWarehouseId(billDelivery.getWarehouseId());
		billOutStore.setCustomerId(billDelivery.getCustomerId());
		billOutStore.setCustomerAddressId(billDelivery.getCustomerAddressId());
		billOutStore.setTransferMode(billDelivery.getTransferMode());
		billOutStore.setCostAmount(billDelivery.getCostAmount());
		billOutStore.setPoAmount(billDelivery.getPoAmount());
		billOutStore.setSendAmount(billDelivery.getRequiredSendAmount());
		billOutStore.setSendNum(billDelivery.getRequiredSendNum());
		billOutStore.setRequiredSendDate(billDelivery.getRequiredSendDate());
		billOutStore.setCurrencyType(billDelivery.getCurrencyType());
		billOutStore.setPickupNum(BigDecimal.ZERO);
		billOutStore.setPickupAmount(BigDecimal.ZERO);
		billOutStore.setStatus(BaseConsts.INT_25); // 待财务审核
		billOutStore.setRemark(billDelivery.getRemark());
		BaseProject baseProject = cacheService.getProjectById(billDelivery.getProjectId());// 获取商务信息
		billOutStore.setCreator(ServiceSupport.getUser() == null ? BaseConsts.SYSTEM_ROLE_NAME
				: cacheService.getUserByid(baseProject.getBusinessManagerId()).getChineseName());
		billOutStore.setCreatorId(baseProject.getBusinessManagerId());

		billOutStore.setSignStandard(billDelivery.getSignStandard());
		billOutStore.setCertificateId(billDelivery.getCertificateId());
		billOutStore.setCertificateName(billDelivery.getCertificateName());
		billOutStore.setOfficialSeal(billDelivery.getOfficialSeal());
		billOutStore.setPayAmount(billDelivery.getPayAmount());
		billOutStore.setFlyOrderFlag(billDelivery.getFlyOrderFlag());
		int result = billOutStoreDao.insert(billOutStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_OUT_STORE_ADD_ERROR);
		}
		return billOutStore;
	}

	/**
	 * 创建出库单明细
	 *
	 * @param billOutStore
	 * @return
	 */
	private void createBillOutStoreDtlEntity(BillOutStore billOutStore) {
		List<BillDeliveryDtl> billDeliveryDtls = billDeliveryDtlDao
				.queryResultsByBillDeliveryId(billOutStore.getBillDeliveryId());
		for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtls) {
			BillOutStoreDtl billOutStoreDtl = new BillOutStoreDtl();
			billOutStoreDtl.setBillOutStoreId(billOutStore.getId());
			billOutStoreDtl.setBillDeliveryDtlId(billDeliveryDtl.getId());
			billOutStoreDtl.setGoodsId(billDeliveryDtl.getGoodsId());
			billOutStoreDtl.setSendNum(billDeliveryDtl.getRequiredSendNum());
			billOutStoreDtl.setSendPrice(billDeliveryDtl.getRequiredSendPrice());
			billOutStoreDtl.setPickupNum(BigDecimal.ZERO);
			billOutStoreDtl.setCostAmount(BigDecimal.ZERO);
			billOutStoreDtl.setPoAmount(BigDecimal.ZERO);
			billOutStoreDtl.setStlId(billDeliveryDtl.getStlId());
			billOutStoreDtl.setBatchNo(billDeliveryDtl.getBatchNo());
			billOutStoreDtl.setGoodsStatus(billDeliveryDtl.getGoodsStatus());
			billOutStoreDtl.setAssignStlFlag(billDeliveryDtl.getAssignStlFlag());

			billOutStoreDtl.setCreator(billOutStore.getCreator());
			billOutStoreDtl.setCreatorId(billOutStore.getCreatorId());

			billOutStoreDtl.setPayPrice(billDeliveryDtl.getPayPrice());
			billOutStoreDtl.setPayRate(billDeliveryDtl.getPayRate());
			billOutStoreDtl.setPayTime(billDeliveryDtl.getPayTime());
			billOutStoreDtl.setPayRealCurrency(billDeliveryDtl.getPayRealCurrency());
			billOutStoreDtlDao.insert(billOutStoreDtl);

			BillOutStorePickDtl billOutStorePickDtl = new BillOutStorePickDtl();
			billOutStorePickDtl.setBillOutStoreId(billOutStoreDtl.getBillOutStoreId());
			billOutStorePickDtl.setBillOutStoreDtlId(billOutStoreDtl.getId());
			billOutStorePickDtl.setSendPrice(billDeliveryDtl.getRequiredSendPrice());
			billOutStorePickDtl.setGoodsId(billDeliveryDtl.getGoodsId());
			billOutStorePickDtl.setCreator(ServiceSupport.getUser().getChineseName());
			billOutStorePickDtl.setCreatorId(ServiceSupport.getUser().getId());
			billOutStorePickDtl.setPayPrice(billDeliveryDtl.getPayPrice());
			billOutStorePickDtl.setPayRate(billDeliveryDtl.getPayRate());
			billOutStorePickDtl.setPayRealCurrency(billDeliveryDtl.getPayRealCurrency());
			billOutStorePickDtl.setPayTime(billDeliveryDtl.getPayTime());
			// 匹配库存
			boolean isPickupFlag = billOutStorePickDtlService.matchStl(billOutStore, billOutStoreDtl);
			if (isPickupFlag == false) { // 拣货失败
				throw new BaseException(ExcMsgEnum.STL_NOT_ENOUGH);
			}
			// 更新出库单明细
			billOutStorePickDtlService.updateBillOutStoreDtlPickInfo(billOutStoreDtl.getId());
		}
		// 更新出库单头
		billOutStorePickDtlService.updateBillOutStorePickInfo(billOutStore.getId());
	}

	/**
	 * 创建入库单头
	 *
	 * @param billDelivery
	 * @return
	 */
	private BillInStore createBillInStoreEntity(BillDelivery billDelivery) {
		BillInStore billInStore = new BillInStore();
		billInStore.setBillNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_BILL_IN_STORE,
				SeqConsts.BILL_IN_STORE_NO, BaseConsts.INT_13));
		billInStore.setBillType(BaseConsts.THREE); // 3-销售退货
		billInStore.setAffiliateNo(billDelivery.getAffiliateNo());
		billInStore.setProjectId(billDelivery.getProjectId());
		billInStore.setWarehouseId(billDelivery.getWarehouseId());
		billInStore.setCustomerId(billDelivery.getCustomerId());
		billInStore.setSupplierId(billDelivery.getCustomerId()); // 默认客户为供应商
		billInStore.setCurrencyType(billDelivery.getCurrencyType());
		billInStore.setReceiveDate(billDelivery.getRequiredSendDate());
		billInStore.setReceiveNum(billDelivery.getRequiredSendNum().abs());
		billInStore.setReceiveAmount(billDelivery.getRequiredSendAmount().abs());
		billInStore.setTallyNum(BigDecimal.ZERO);
		billInStore.setTallyAmount(BigDecimal.ZERO);
		billInStore.setStatus(BaseConsts.ONE); // 待收货
		billInStore.setBillDeliveryId(billDelivery.getId());
		billInStore.setCreator(ServiceSupport.getUser().getChineseName());
		billInStore.setCreatorId(ServiceSupport.getUser().getId());
		billInStore.setPayAmount(billDelivery.getPayAmount());
		billInStore.setRemark(billDelivery.getRemark());
		int result = billInStoreDao.insert(billInStore);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.BILL_IN_STORE_ADD_ERROR);
		}
		return billInStore;
	}

	/**
	 * 创建入库单明细
	 *
	 * @param billInStore
	 */
	private void createBillInStoreDtlEntity(BillInStore billInStore) {
		List<BillDeliveryDtl> billDeliveryDtls = billDeliveryDtlDao
				.queryResultsByBillDeliveryId(billInStore.getBillDeliveryId());
		for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtls) {
			BillInStoreDtl billInStoreDtl = new BillInStoreDtl();
			billInStoreDtl.setBillInStoreId(billInStore.getId());
			billInStoreDtl.setGoodsId(billDeliveryDtl.getGoodsId());
			billInStoreDtl.setReceiveNum(billDeliveryDtl.getRequiredSendNum().abs());
			billInStoreDtl.setReceivePrice(billDeliveryDtl.getRequiredSendPrice());
			billInStoreDtl.setTallyNum(BigDecimal.ZERO);
			billInStoreDtl.setTallyAmount(BigDecimal.ZERO);
			billInStoreDtl.setPoPrice(billDeliveryDtl.getPoPrice());
			billInStoreDtl.setCostPrice(billDeliveryDtl.getCostPrice());
			billInStoreDtl.setBatchNo(billDeliveryDtl.getBatchNo());
			billInStoreDtl.setReceiveDate(billInStore.getReceiveDate());
			billInStoreDtl.setCurrencyType(billInStore.getCurrencyType());
			billInStoreDtl.setCustomerId(billInStore.getCustomerId());
			billInStoreDtl.setSupplierId(billInStore.getSupplierId());
			billInStoreDtl.setRemark(billDeliveryDtl.getRemark());
			billInStoreDtl.setCreator(ServiceSupport.getUser().getChineseName());
			billInStoreDtl.setCreatorId(ServiceSupport.getUser().getId());
			billInStoreDtl.setPayPrice(billDeliveryDtl.getPayPrice());
			billInStoreDtl.setPayTime(billDeliveryDtl.getPayTime());
			billInStoreDtl
					.setPayRate(billDeliveryDtl.getPayRate() == null ? BigDecimal.ZERO : billDeliveryDtl.getPayRate());
			billInStoreDtl.setPayRealCurrency(billDeliveryDtl.getPayRealCurrency());
			billInStoreDtl.setBillDeliveryId(billDeliveryDtl.getBillDeliveryId());
			billInStoreDtl.setBillDeliveryDtlId(billDeliveryDtl.getId());
			billInStoreDtlDao.insert(billInStoreDtl);
		}
		// 自动生成理货明细
		billInStoreTallyDtlService.autoTally(billInStore);
	}

	private void validateSubmit(BillDelivery billDelivery) {
		if (null != billDelivery) {
			BillDeliveryDtl billDeliveryDtlReq = new BillDeliveryDtl();
			billDeliveryDtlReq.setBillDeliveryId(billDelivery.getId());
			int dtlsCount = billDeliveryDtlDao.queryCountByBillDeliveryId(billDeliveryDtlReq);
			if (dtlsCount <= 0) {
				throw new BaseException(ExcMsgEnum.BILL_DELIVERY_DTL_NOT_ADD);
			}

			BillDeliveryDtlSum billDeliveryDtlSum = billDeliveryDtlDao.querySumByBillDeliveryId(billDeliveryDtlReq);
			if (null != billDeliveryDtlSum) {
				if (DecimalUtil.ne(billDelivery.getRequiredSendNum(), billDeliveryDtlSum.getRequiredSendNum())) {
					throw new BaseException(ExcMsgEnum.BILL_DELIVERY_DTL_NUM_NOT_EQUAL);
				}
			}
		}
	}

	/**
	 * 获取文件操作列表
	 *
	 * @param fileAttReqDto
	 * @return
	 */
	public PageResult<BillDeliveryFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<BillDeliveryFileResDto> pageResult = new PageResult<BillDeliveryFileResDto>();
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<BillDeliveryFileResDto> list = convertToResDtoByFileAttach(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<BillDeliveryFileResDto> queryFileList(Integer billDeliveryId) {
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(billDeliveryId);
		fileAttReqDto.setBusType(BaseConsts.FIVE);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<BillDeliveryFileResDto> list = convertToResDtoByFileAttach(fielAttach);
		return list;
	}

	private List<BillDeliveryFileResDto> convertToResDtoByFileAttach(List<FileAttach> fileAttach) {
		List<BillDeliveryFileResDto> list = new LinkedList<BillDeliveryFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			BillDeliveryFileResDto result = new BillDeliveryFileResDto();
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
				BillDeliveryFileResDto.Operate.operMap);
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

	/**
	 * 导入销售单Excel
	 *
	 * @param importFile
	 */
	public void importBillDeliveryExcel(MultipartFile importFile, Integer billType) {
		List<BillDeliveryExcel> billDeliveryExcelList = Lists.newArrayList();
		Map<String, List<BillDeliveryExcel>> beans = Maps.newHashMap();
		beans.put("billDeliveryExcelList", billDeliveryExcelList);
		ExcelService.resolverExcel(importFile, "/excel/sale/billDelivery/billDelivery.xml", beans);
		// 业务逻辑处理
		billDeliveryExcelList = (List<BillDeliveryExcel>) beans.get("billDeliveryExcelList");
		if (!CollectionUtils.isEmpty(billDeliveryExcelList)) {
			if (billDeliveryExcelList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			Map<BillDeliveryExcel, List<BillDeliveryDtlExcel>> map = Maps.newHashMap();
			for (BillDeliveryExcel billDeliveryExcel : billDeliveryExcelList) {
				String affiliateNo = (null == billDeliveryExcel.getAffiliateNo() ? ""
						: billDeliveryExcel.getAffiliateNo().trim());
				if (StringUtils.isNotBlank(affiliateNo)) {
					if (affiliateNo.length() > 50) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售附属编号不能超过30个字符");
					}
				} else {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售附属编号不能为空");
				}
				billDeliveryExcel.setAffiliateNo(affiliateNo);

				String projectNo = (null == billDeliveryExcel.getProjectNo() ? ""
						: billDeliveryExcel.getProjectNo().trim());
				if (StringUtils.isBlank(projectNo)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目不能为空");
				}
				BaseProject baseProject = cacheService.getProjectByPno(projectNo);
				if (null == baseProject) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目【" + projectNo + "】不存在");
				}
				billDeliveryExcel.setProjectNo(projectNo);
				billDeliveryExcel.setProjectId(baseProject.getId());

				String warehouseNo = (null == billDeliveryExcel.getWarehouseNo() ? ""
						: billDeliveryExcel.getWarehouseNo().trim());
				if (StringUtils.isBlank(warehouseNo)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "仓库不能为空");
				}
				BaseSubject baseSubject = cacheService.getWarehouseByPidAndNo(baseProject.getId(), warehouseNo);
				if (null == baseSubject) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"项目【" + projectNo + "】下仓库【" + warehouseNo + "】不存在");
				}
				billDeliveryExcel.setWarehouseNo(warehouseNo);
				billDeliveryExcel.setWarehouseId(baseSubject.getId());

				String customerNo = (null == billDeliveryExcel.getCustomerNo() ? ""
						: billDeliveryExcel.getCustomerNo().trim());
				if (StringUtils.isBlank(customerNo)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "客户不能为空");
				}
				BaseSubject baseSubject2 = cacheService.getCustomerByPidAndNo(baseProject.getId(), customerNo);
				if (null == baseSubject2) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"项目【" + projectNo + "】下客户【" + customerNo + "】不存在");
				}
				billDeliveryExcel.setCustomerNo(customerNo);
				billDeliveryExcel.setCustomerId(baseSubject2.getId());

				String transferModeName = (null == billDeliveryExcel.getTransferModeName() ? ""
						: billDeliveryExcel.getTransferModeName().trim());
				if (StringUtils.isBlank(transferModeName)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "运输方式不能为空");
				}
				billDeliveryExcel.setTransferModeName(transferModeName);
				String transferMode = ServiceSupport.getCodeByBizValue(BizCodeConsts.BILL_OUT_STORE_TRANSFER_MODE,
						transferModeName);
				if (StringUtils.isBlank(transferMode)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "运输方式【" + transferModeName + "】不存在");
				}
				billDeliveryExcel.setTransferMode(Integer.parseInt(transferMode));

				List<CodeValue> codeValueList = commonService.queryAllSelectedByKey(CacheKeyConsts.SUBJECT_ADDRESS,
						billDeliveryExcel.getCustomerId() + "");
				if (Integer.parseInt(transferMode) != BaseConsts.ONE) { // 1-自提
					if (CollectionUtils.isEmpty(codeValueList)) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "客户【" + customerNo + "】收货地址不存在");
					}
				}
				if (!CollectionUtils.isEmpty(codeValueList)) {
					billDeliveryExcel.setCustomerAddressId(Integer.parseInt(codeValueList.get(0).getCode()));
				}

				Date requiredSendDate = billDeliveryExcel.getRequiredSendDate();
				if (null == requiredSendDate) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售日期不能为空");
				}
				String currencyTypeName = (null == billDeliveryExcel.getCurrencyTypeName() ? ""
						: billDeliveryExcel.getCurrencyTypeName().trim());
				if (StringUtils.isBlank(currencyTypeName)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不能为空");
				}
				billDeliveryExcel.setCurrencyTypeName(currencyTypeName);
				String currencyType = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						currencyTypeName);
				if (StringUtils.isBlank(currencyType)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种【" + currencyTypeName + "】不存在");
				}
				billDeliveryExcel.setCurrencyType(Integer.parseInt(currencyType));

				String orderNo = (null == billDeliveryExcel.getOrderNo() ? "" : billDeliveryExcel.getOrderNo().trim());
				if (StringUtils.isBlank(orderNo)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单编号不能为空");
				}
				String goodsNo = (null == billDeliveryExcel.getGoodsNo() ? "" : billDeliveryExcel.getGoodsNo().trim());
				if (StringUtils.isBlank(goodsNo)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品编号不能为空");
				}
				String requiredSendNum = billDeliveryExcel.getRequiredSendNum();
				if (StringUtils.isBlank(requiredSendNum)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售数量不能为空");
				}
				BigDecimal sendNum = BigDecimal.ZERO;
				try {
					sendNum = new BigDecimal(requiredSendNum);
				} catch (Exception e) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售数量格式错误");
				}
				if (sendNum.compareTo(BigDecimal.ZERO) <= 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售数量必须大于零");
				}
				if (StringUtils.isNotBlank(billDeliveryExcel.getRequiredSendPrice())) {
					try {
						new BigDecimal(billDeliveryExcel.getRequiredSendPrice());
					} catch (Exception e) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售价格式错误");
					}
				}
				BaseGoods baseGoods = cacheService.getGoodsByPidAndNo(billDeliveryExcel.getProjectId(),
						billDeliveryExcel.getGoodsNo());
				if (null == baseGoods) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目【" + projectNo + "】下商品【" + goodsNo + "】不存在");
				}

				BillDeliveryDtlExcel billDeliveryDtlExcel = new BillDeliveryDtlExcel();
				billDeliveryDtlExcel.setOrderNo(orderNo);
				billDeliveryDtlExcel.setGoodsId(baseGoods.getId());
				billDeliveryDtlExcel.setGoodsNo(goodsNo);
				billDeliveryDtlExcel.setRequiredSendNum(requiredSendNum);
				billDeliveryDtlExcel.setRequiredSendPrice(billDeliveryExcel.getRequiredSendPrice());
				billDeliveryDtlExcel.setBatchNo(billDeliveryExcel.getBatchNo());
				if (map.containsKey(billDeliveryExcel)) {
					List<BillDeliveryDtlExcel> billDeliveryDtlExcelList = map.get(billDeliveryExcel);
					billDeliveryDtlExcelList.add(billDeliveryDtlExcel);
					map.put(billDeliveryExcel, billDeliveryDtlExcelList);
				} else {
					List<BillDeliveryDtlExcel> billDeliveryDtlExcelList = new ArrayList<BillDeliveryDtlExcel>();
					billDeliveryDtlExcelList.add(billDeliveryDtlExcel);
					map.put(billDeliveryExcel, billDeliveryDtlExcelList);
				}
			}
			Iterator<Entry<BillDeliveryExcel, List<BillDeliveryDtlExcel>>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<BillDeliveryExcel, List<BillDeliveryDtlExcel>> entry = iterator.next();
				BillDeliveryExcel billDeliveryExcel = entry.getKey();
				List<BillDeliveryDtlExcel> billDeliveryDtlExcelList = entry.getValue();
				ProjectItem projectItem = projectItemService
						.getProjectItemByProjectId(billDeliveryExcel.getProjectId());
				if (null == projectItem) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"项目【" + billDeliveryExcel.getProjectNo() + "】条款不存在");
				}
				BillDelivery billDelivery = new BillDelivery();
				billDelivery.setAffiliateNo(billDeliveryExcel.getAffiliateNo());
				billDelivery.setProjectId(billDeliveryExcel.getProjectId());
				billDelivery.setWarehouseId(billDeliveryExcel.getWarehouseId());
				billDelivery.setCustomerId(billDeliveryExcel.getCustomerId());
				billDelivery.setTransferMode(billDeliveryExcel.getTransferMode());
				billDelivery.setRequiredSendDate(billDeliveryExcel.getRequiredSendDate());
				billDelivery.setCurrencyType(billDeliveryExcel.getCurrencyType());
				billDelivery.setRemark(billDeliveryExcel.getRemark());
				billDelivery.setCustomerAddressId(billDeliveryExcel.getCustomerAddressId());
				billDelivery.setSignStandard(projectItem.getSignStandard());
				billDelivery.setCertificateId(projectItem.getCertificateId());
				billDelivery.setCertificateName(projectItem.getCertificateName());
				billDelivery.setOfficialSeal(projectItem.getOfficialSeal());
				billDelivery.setStatus(BaseConsts.ONE);
				billDelivery.setBillType(billType);
				billDelivery.setReturnTime(billDeliveryExcel.getRequiredSendDate());
				addBillDelivery(billDelivery);
				billDeliveryDtlService.addBillDeliveryDtlsByImport(billDelivery, billDeliveryDtlExcelList);
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入销售单不能为空");
		}
	}

	public BillDelivery queryEntityById(Integer id) {
		return billDeliveryDao.queryAndLockEntityById(id);
	}

	/**
	 * 重新计算销售单价和销售金额
	 *
	 * @param billDeliveryId
	 * @param returnTime
	 */
	public void reCalcPrice(Integer billDeliveryId, Date returnTime) {
		BillDelivery billDelivery = queryEntityById(billDeliveryId);
		ProjectItem projectItem = cacheService.getProjectItemByPid(billDelivery.getProjectId());

		if (billDelivery.getIsChangePrice().equals(BaseConsts.ZERO)) { // 0-未改价
			if (projectItem.getIsFundAccount().equals(BaseConsts.ONE)) { // 1-资金占用
				if (null == returnTime) {
					returnTime = billDelivery.getRequiredSendDate();
				}
				List<BillDeliveryDtl> billDeliveryDtlList = billDeliveryDtlDao
						.queryResultsByBillDeliveryId(billDelivery.getId());
				for (BillDeliveryDtl billDeliveryDtl : billDeliveryDtlList) {
					if (null != billDeliveryDtl.getStlId()) {
						Stl stl = stlService.queryEntityById(billDeliveryDtl.getStlId());
						if (null != stl) {
							BigDecimal salePrice = projectItemService.getSalePrice(billDeliveryDtl.getStlId(),
									returnTime);
							billDeliveryDtl.setRequiredSendPrice(null == salePrice ? BigDecimal.ZERO : salePrice);
							billDeliveryDtl.setSaleGuidePrice(salePrice);
							BigDecimal profitPrice = projectItemService.getProfitPriceByStl(billDeliveryDtl.getStlId(),
									returnTime);
							billDeliveryDtl.setProfitPrice(profitPrice);
						}
						billDeliveryDtlDao.updateById(billDeliveryDtl);
					}
				}
				billDeliveryDtlService.updateBillDeliveryInfo(billDelivery.getId(), returnTime);
			}
		}
	}

	public BillDelivery queryEntityByBillNo(String billNo) {
		return billDeliveryDao.queryEntityByBillNo(billNo);
	}

	/**
	 * 查询可退货明细列表
	 *
	 * @param billOutStoreDetailSearchReqDto
	 * @return
	 */
	public PageResult<BillOutStoreDetailResDto> queryBillOutStoreDetailResultsByCon(
			BillOutStoreDetailSearchReqDto billOutStoreDetailSearchReqDto) {
		PageResult<BillOutStoreDetailResDto> result = new PageResult<BillOutStoreDetailResDto>();
		int offSet = PageUtil.getOffSet(billOutStoreDetailSearchReqDto.getPage(),
				billOutStoreDetailSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, billOutStoreDetailSearchReqDto.getPer_page());

		billOutStoreDetailSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		Integer billDeliveryId = billOutStoreDetailSearchReqDto.getBillDeliveryId();
		if (null != billDeliveryId) {
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(billDeliveryId);
			billDelivery = billDeliveryDao.queryEntityById(billDelivery);
			if (null != billDelivery) {
				billOutStoreDetailSearchReqDto.setProjectId(billDelivery.getProjectId());
				billOutStoreDetailSearchReqDto.setWarehouseId(billDelivery.getWarehouseId());
				billOutStoreDetailSearchReqDto.setCustomerId(billDelivery.getCustomerId());
				billOutStoreDetailSearchReqDto.setCurrencyType(billDelivery.getCurrencyType());
			}
		}
		List<BillOutStoreDetailResDto> billOutStoreDetailResDtoList = billOutStoreDao
				.queryBillOutStoreDetailResultsByCon(billOutStoreDetailSearchReqDto, rowBounds);
		List<BillOutStoreDetailResDto> resultList = convertToBillOutStoreDetailResDto(billOutStoreDetailResDtoList);
		result.setItems(resultList);

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), billOutStoreDetailSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(billOutStoreDetailSearchReqDto.getPage());
		result.setPer_page(billOutStoreDetailSearchReqDto.getPer_page());
		return result;
	}

	private List<BillOutStoreDetailResDto> convertToBillOutStoreDetailResDto(
			List<BillOutStoreDetailResDto> billOutStoreDetailResDtoList) {
		List<BillOutStoreDetailResDto> resultList = new ArrayList<BillOutStoreDetailResDto>(5);
		if (CollectionUtils.isEmpty(billOutStoreDetailResDtoList)) {
			return resultList;
		}
		for (BillOutStoreDetailResDto billOutStoreDetailResDto : billOutStoreDetailResDtoList) {
			BillOutStoreDetailResDto result = convertToBillOutStoreDetailResDto(billOutStoreDetailResDto);
			resultList.add(result);
		}
		return resultList;
	}

	public BillOutStoreDetailResDto convertToBillOutStoreDetailResDto(
			BillOutStoreDetailResDto billOutStoreDetailResDto) {
		BillOutStoreDetailResDto result = new BillOutStoreDetailResDto();
		BeanUtils.copyProperties(billOutStoreDetailResDto, result);
		if (result.getCurrencyType() != null) {
			result.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					Integer.toString(result.getCurrencyType())));
		}
		if (result.getGoodsStatus() != null) {
			result.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
					Integer.toString(result.getGoodsStatus())));
		}
		result.setProjectName(cacheService.showProjectNameById(result.getProjectId()));
		result.setWarehouseName(
				cacheService.showSubjectNameByIdAndKey(result.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		result.setCustomerName(cacheService.showSubjectNameByIdAndKey(result.getCustomerId(), CacheKeyConsts.CUSTOMER));
		if (result.getGoodsId() != null) {
			BaseGoods baseGoods = cacheService.getGoodsById(result.getGoodsId());
			if (null != baseGoods) {
				result.setGoodsName(baseGoods.getName());
				result.setGoodsNumber(baseGoods.getNumber());
				result.setGoodsType(baseGoods.getType());
				result.setGoodsUnit(baseGoods.getUnit());
				result.setGoodsBarCode(baseGoods.getBarCode());
			}
		}
		result.setAvailableReturnNum(DecimalUtil.subtract(result.getPickupNum(), result.getReturnNum()));
		return result;
	}

	public boolean isOverBillDeliveryMaxLine(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		billDeliverySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billDeliveryDao.queryCountByCon(billDeliverySearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("销售单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillDeliveryExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_delivery_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.FIVE);
			asyncExcelService.addAsyncExcel(billDeliverySearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillDeliveryExport(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillDeliveryResDto> billDeliveryResDtoList = queryAllBillDeliveryResultsByCon(billDeliverySearchReqDto);
		model.put("billDeliveryList", billDeliveryResDtoList);
		return model;
	}

	public boolean isOverBillReturnMaxLine(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		billDeliverySearchReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = billDeliveryDao.queryCountByCon(billDeliverySearchReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("销售退货单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncBillReturnExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/logistics/bill_return_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_24);
			asyncExcelService.addAsyncExcel(billDeliverySearchReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncBillReturnExport(BillDeliverySearchReqDto billDeliverySearchReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<BillDeliveryResDto> billDeliveryResDtoList = queryAllBillDeliveryResultsByCon(billDeliverySearchReqDto);
		model.put("billDeliveryList", billDeliveryResDtoList);
		return model;
	}

	public List<BillDelivery> queryFinishedBillDeliveryByAffiliateNo(String affiliateNo) {
		return billDeliveryDao.queryFinishedBillDeliveryByAffiliateNo(affiliateNo);
	}

	/**
	 * 查询销售单详情
	 *
	 * @param billDelivery
	 * @return
	 */
	public Result<BillDeliveryResDto> detailBillDeliveryResultById(BillDelivery billDelivery) {
		Result<BillDeliveryResDto> result = new Result<BillDeliveryResDto>();
		BillDelivery billDeliveryRes = billDeliveryDao.queryEntityById(billDelivery);
		BillDeliveryResDto billDeliveryResDto = convertToBillDeliverResDto(billDeliveryRes);
		result.setItems(billDeliveryResDto);
		return result;
	}

	/**
	 * 销售合同打印 币种的取值区别
	 * 
	 * @param billDelivery
	 * @return
	 */
	public BillDeliveryResDto convertToBillDeliverResDto(BillDelivery billDelivery) {
		BillDeliveryResDto billDeliveryResDto = this.convertToResDto(billDelivery);
		// 根据业务需求查询实际付款币种
		// 新规则，无需校验实际支付币种
		/**
		 * Integer currnecyType = null; BillDeliveryDtlSearchReqDto
		 * billDeliveryDtlSearchReqDto = new BillDeliveryDtlSearchReqDto();
		 * billDeliveryDtlSearchReqDto.setBillDeliveryId(billDelivery.getId());
		 * PageResult<BillDeliveryDtlResDto> pageResult = billDeliveryDtlService
		 * .queryAllBillDeliveryDtlsByBillDeliveryId(billDeliveryDtlSearchReqDto
		 * ); List<BillDeliveryDtlResDto> list = pageResult.getItems();//
		 * 获取销售单明细的库存ID if (!CollectionUtils.isEmpty(list)) { BigDecimal
		 * payRate=BigDecimal.ZERO; Integer currnecyTypes = null; for
		 * (BillDeliveryDtlResDto dtlResDto : list) {
		 * if(DecimalUtil.eq(payRate,dtlResDto.getPayRate())){ throw new
		 * BaseException(ExcMsgEnum.ERROR_GENERAL, "销售明细实际支付币种不一致"); } Integer
		 * currnecy = dtlResDto.getPayRealCurrency(); if (currnecyTypes == null)
		 * { currnecyTypes = currnecy; } else { if
		 * (!currnecyTypes.equals(currnecy) ) { throw new
		 * BaseException(ExcMsgEnum.ERROR_GENERAL, "销售明细实际支付币种不一致"); } } if
		 * (currnecyType == null) { currnecyType = currnecyTypes; } else { if
		 * (!currnecyType.equals(currnecyTypes) ) { throw new
		 * BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单明细币种不一致"); } } } } if
		 * (currnecyType == null) { throw new
		 * BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单明细币种不一致"); }else{
		 * billDeliveryResDto.setCurrencyTypeName(ServiceSupport.
		 * getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
		 * currnecyType.toString()));
		 * billDeliveryResDto.setCurrencyTypeEnName(ServiceSupport.
		 * getValueByBizCode( BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
		 * currnecyType.toString())); }
		 **/
		ProjectItem projectItem = cacheService.getProjectItemByPid(billDelivery.getProjectId());
		if (projectItem == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单条款为空");
		}
		billDeliveryResDto.setSettleName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_ITEM_SETTLE_TYPE,
				projectItem.getSettleType() + ""));
		return billDeliveryResDto;
	}

	/**
	 * 合同单打印列表集合数据(合并打印)
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public Result<BillDeliveryResDto> queryBillDeliveryResultById(BillDelivery billDelivery) throws Exception {
		Result<BillDeliveryResDto> result = new Result<BillDeliveryResDto>();
		BillDelivery billDeliverys = new BillDelivery();
		List<Integer> ids = billDelivery.getIds();
		billDeliverys = checkBillDelivery(ids);
		BillDeliveryResDto respDto = billDeliveryConvertToRes(billDeliverys);
		Integer currnecyTypes = billDeliveryCurrnecy(billDeliverys);
		respDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN, currnecyTypes.toString()));
		result.setItems(respDto);
		return result;
	}

	/**
	 * 校验当前销售单关联付款单的实际币种是否一致
	 * 
	 * @param purchaseOrderTitle
	 * @param respDto
	 */
	private Integer billDeliveryCurrnecy(BillDelivery billDeliverys) {

		BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto = new BillDeliveryDtlSearchReqDto();
		billDeliveryDtlSearchReqDto.setBillDeliveryId(billDeliverys.getId());
		PageResult<BillDeliveryDtlResDto> pageResult = billDeliveryDtlService
				.queryAllBillDeliveryDtlsByBillDeliveryId(billDeliveryDtlSearchReqDto);
		List<BillDeliveryDtlResDto> list = pageResult.getItems();// 获取销售单明细的库存ID
		if (CollectionUtils.isEmpty(list)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单明细为空");
		}
		Integer currnecyTypes = null;
		if (!CollectionUtils.isEmpty(list)) {
			BigDecimal payRate = BigDecimal.ZERO;

			for (BillDeliveryDtlResDto dtlResDto : list) {
				if (DecimalUtil.eq(payRate, dtlResDto.getPayRate())) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单明细实际支付币种不一致");
				}
				Integer currnecy = dtlResDto.getPayRealCurrency();
				if (currnecyTypes == null) {
					currnecyTypes = currnecy;
				} else {
					if (!currnecyTypes.equals(currnecy)) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单明细实际支付币种不一致");
					}
				}
			}
		}
		if (currnecyTypes == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单明细实际支付币种为空");
		}

		return currnecyTypes;
	}

	/**
	 * 校验当前批量打印 销售单头信息的数据
	 * 
	 * @param billDelivery
	 * @throws Exception
	 */
	private BillDelivery checkBillDelivery(List<Integer> ids) throws Exception {
		BillDelivery billDelivery = new BillDelivery();
		if (!CollectionUtils.isEmpty(ids)) {
			Integer project = null; // 项目
			Integer warehouse = null;// 供应商
			Integer currency = null;// 币种
			for (Integer id : ids) {
				billDelivery = billDeliveryDao.queryBillDeliveryById(id);
				if (billDelivery == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单头信息为空");
				}
				if (billDelivery.getStatus() != BaseConsts.FIVE) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单头信息状态有误");
				}
				if (billDelivery.getProjectId() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单头信息项目为空");
				} else {
					project = project == null ? billDelivery.getProjectId() : project;
					if (!project.equals(billDelivery.getProjectId())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单头信息项目不匹配");
					}
				}
				if (billDelivery.getWarehouseId() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单头信息客户为空");
				} else {
					warehouse = warehouse == null ? billDelivery.getWarehouseId() : warehouse;
					if (!warehouse.equals(billDelivery.getWarehouseId())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单头信息客户不匹配");
					}
				}
				if (billDelivery.getCurrencyType() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单头信息币种为空");
				} else {
					currency = currency == null ? billDelivery.getCurrencyType() : currency;
					if (!currency.equals(billDelivery.getCurrencyType())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单头信息币种不匹配");
					}
				}
				billDeliveryCurrnecy(billDelivery);
			}
		}
		return billDelivery;
	}

	// 查询合同打印需要的显示字段
	public BillDeliveryResDto billDeliveryConvertToRes(BillDelivery billDelivery) {
		if (billDelivery == null) {
			return null;
		}
		BillDeliveryResDto poRespDto = new BillDeliveryResDto();
		poRespDto.setId(billDelivery.getId());
		poRespDto.setBillType(billDelivery.getBillType());// 销售类型
		poRespDto.setAffiliateNo(billDelivery.getAffiliateNo());// 销售附属编号
		poRespDto.setWarehouseId(billDelivery.getWarehouseId());// 仓库
		poRespDto.setCustomerId(billDelivery.getCustomerId());// 客户ID
		poRespDto.setWarehouseId(billDelivery.getWarehouseId());
		// 项目
		poRespDto.setProjectId(billDelivery.getProjectId());
		BaseProject baseProject = cacheService.getProjectById(billDelivery.getProjectId());
		BaseSubject busiUnit = null;
		if (null != baseProject) {
			busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
			if (null != busiUnit) {
				poRespDto.setBusinessUnitName(busiUnit.getChineseName());
				poRespDto.setBusinessUnitEnglishName(busiUnit.getEnglishName());
				poRespDto.setBusinessUnitAddress(busiUnit.getOfficeAddress());
				poRespDto.setBusinessUnitPhone(busiUnit.getRegPhone());
			}
		}
		BaseSubject customerProject = cacheService.getBaseSubjectById(billDelivery.getCustomerId());// 客户信息
		if (customerProject != null) {
			poRespDto.setCustomerChineseName(customerProject.getChineseName());
			poRespDto.setCustomerAfficeName(customerProject.getOfficeAddress());
		}
		if (billDelivery.getCurrencyType() != null) {
			poRespDto.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					Integer.toString(billDelivery.getCurrencyType())));
			poRespDto.setCurrencyTypeEnName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
					billDelivery.getCurrencyType().toString()));
		}
		poRespDto.setProjectName(cacheService.showProjectNameById(billDelivery.getProjectId()));
		poRespDto.setDeliveryDate(billDelivery.getDeliveryDate());
		ProjectItem projectItem = cacheService.getProjectItemByPid(billDelivery.getProjectId());
		if (projectItem == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "销售单条款为空");
		}
		poRespDto.setSettleName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_ITEM_SETTLE_TYPE,
				projectItem.getSettleType() + ""));
		if (billDelivery.getTransferMode() != null) {
			poRespDto.setTransferModeName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_OUT_STORE_TRANSFER_MODE,
					Integer.toString(billDelivery.getTransferMode())));
		}
		poRespDto.setFundAccountPeriod(
				null == projectItem.getFundAccountPeriod() ? BaseConsts.ZERO : projectItem.getFundAccountPeriod());// 合作账期

		BaseAddress customerAddress = cacheService.getAddressById(poRespDto.getCustomerAddressId());
		if (customerAddress != null) {
			poRespDto.setCustomerAddress(customerAddress.getShowValue());
			poRespDto.setCityName(customerAddress.getCityName());
			poRespDto.setContactPerson(customerAddress.getContactPerson());
			poRespDto.setMobilePhone(customerAddress.getMobilePhone());
			poRespDto.setTelephone(customerAddress.getTelephone());
		}
		if (busiUnit.getId() != null) {
			QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
			queryAccountReqDto.setSubjectId(busiUnit.getId());
			queryAccountReqDto.setState(BaseConsts.ONE);
			List<BaseAccount> baseAccount = accountDao.queryFicBySubjectId(queryAccountReqDto);// 获取客户帐户信息
			if (!CollectionUtils.isEmpty(baseAccount)) {
				poRespDto.setBankName(baseAccount.get(BaseConsts.ZERO).getBankName());
				poRespDto.setSubjectName(baseAccount.get(BaseConsts.ZERO).getAccountor());
				poRespDto.setBankAddress(baseAccount.get(BaseConsts.ZERO).getBankAddress());
				poRespDto.setAccountNo(baseAccount.get(BaseConsts.ZERO).getAccountNo());
				poRespDto.setBankCode(baseAccount.get(BaseConsts.ZERO).getBankCode());
				poRespDto.setPhoneNumber(baseAccount.get(BaseConsts.ZERO).getPhoneNumber());
				poRespDto.setDefaultCurrency(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						billDelivery.getCurrencyType() + ""));
			}
		}
		poRespDto.setRequiredSendDate(billDelivery.getRequiredSendDate());

		return poRespDto;
	}

}
