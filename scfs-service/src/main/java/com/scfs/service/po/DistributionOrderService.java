package com.scfs.service.po;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.po.dto.req.DistributeAccountReqDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderLineTaxGroupSum;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.excel.PurchaseOrderDtlExtResDto;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class DistributionOrderService {
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;

	public PageResult<PoTitleRespDto> queryPoTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		PageResult<PoTitleRespDto> result = new PageResult<PoTitleRespDto>();
		int offSet = PageUtil.getOffSet(poTitleReqDto.getPage(), poTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poTitleReqDto.getPer_page());
		List<PoTitleRespDto> poRespDto = new LinkedList<PoTitleRespDto>();
		List<PurchaseOrderTitle> PoTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto, rowBounds);
		// 添加操作
		poRespDto = convertToResult(PoTitles, poTitleReqDto);

		if (poTitleReqDto.getNeedSum() != null && poTitleReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<PurchaseOrderTitle> orderOrder = purchaseOrderTitleDao.sumPoTitle(poTitleReqDto);
			String totalStr = "合计：";
			if (CollectionUtils.isNotEmpty(orderOrder)) {
				for (PurchaseOrderTitle puOrder : orderOrder) {
					totalStr = totalStr + "订单数量:&nbsp;"
							+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(puOrder.getOrderTotalNum()))
							+ "&nbsp;&nbsp;订单金额:&nbsp;"
							+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(puOrder.getOrderTotalAmount()))
							+ "&nbsp;" + BaseConsts.CURRENCY_UNIT_MAP.get(puOrder.getCurrencyId())
							+ "&nbsp;&nbsp;收货数量:&nbsp;"
							+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(puOrder.getOrderTotalNum()))
							+ "&nbsp;&nbsp;可发货数量:&nbsp;"
							+ DecimalUtil.toAmountString(DecimalUtil.formatScale2(puOrder.getTotalRemainSendNum()))
							+ "&nbsp;&nbsp;";
				}
				result.setTotalStr(totalStr);
			}

		}

		result.setItems(poRespDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poTitleReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(poTitleReqDto.getPage());
		result.setPer_page(poTitleReqDto.getPer_page());
		return result;
	}

	private PoTitleRespDto purchaseOrderTitleConvertToRes(PurchaseOrderTitle purchaseOrderTitle) {
		if (purchaseOrderTitle == null) {
			return null;
		}
		PoTitleRespDto poRespDto = new PoTitleRespDto();

		poRespDto.setAppendNo(purchaseOrderTitle.getAppendNo());
		poRespDto.setOrderNo(purchaseOrderTitle.getOrderNo());
		poRespDto.setId(purchaseOrderTitle.getId());
		poRespDto.setIsRequestPay(purchaseOrderTitle.getIsRequestPay());
		poRespDto.setIsRequestPayName(ServiceSupport.getValueByBizCode(BizCodeConsts.PO_IS_REQUEST_PAY,
				purchaseOrderTitle.getIsRequestPay() + ""));
		poRespDto.setRemark(purchaseOrderTitle.getRemark());
		poRespDto.setcBankWater(purchaseOrderTitle.getcBankWater());
		poRespDto.setBusinessUnitId(purchaseOrderTitle.getBusinessUnitId());
		poRespDto.setProjectId(purchaseOrderTitle.getProjectId());
		poRespDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		poRespDto.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		// 项目
		poRespDto.setProjectId(purchaseOrderTitle.getProjectId());
		poRespDto.setProjectName(cacheService.showProjectNameById(purchaseOrderTitle.getProjectId()));
		poRespDto
				.setBusinessUnitNameValue(cacheService.getBusiUnitById(poRespDto.getBusinessUnitId()).getChineseName());
		poRespDto
				.setBusinessUnitAddress(cacheService.getBusiUnitById(poRespDto.getBusinessUnitId()).getOfficeAddress());
		poRespDto.setSystemTime(new Date());
		// 经营单位
		poRespDto.setBusinessUnitName(cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getBusinessUnitId(),
				CacheKeyConsts.BUSI_UNIT));
		poRespDto.setBusinessUnitId(purchaseOrderTitle.getBusinessUnitId());
		// 供应商
		poRespDto.setSupplierName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getSupplierId(), CacheKeyConsts.SUPPLIER));
		poRespDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		// 仓库
		poRespDto.setWarehouseName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		poRespDto.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		// 仓库地址
		poRespDto.setTitleDiscountAmount(purchaseOrderTitle.getTotalDiscountAmount());
		poRespDto.setSignStandard(purchaseOrderTitle.getSignStandard());
		poRespDto.setCertificateId(purchaseOrderTitle.getCertificateId());
		poRespDto.setCertificateName(purchaseOrderTitle.getCertificateName());
		poRespDto.setOfficialSeal(purchaseOrderTitle.getOfficialSeal());
		poRespDto.setSupplierAddressId(purchaseOrderTitle.getSupplierAddressId());
		poRespDto.setSupplierAddressName(
				cacheService.getAddressById(purchaseOrderTitle.getSupplierAddressId()).getAddressDetail());
		poRespDto.setSignStandardName(purchaseOrderTitle.getSignStandard() == null ? null
				: ServiceSupport.getValueByBizCode(BizCodeConsts.SIGN_STANDARD,
						purchaseOrderTitle.getSignStandard() + ""));
		poRespDto.setTransferMode(purchaseOrderTitle.getTransferMode());
		poRespDto.setTransferModeName(purchaseOrderTitle.getTransferMode() == null ? null
				: ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_OUT_STORE_TRANSFER_MODE,
						purchaseOrderTitle.getTransferMode() + ""));
		BaseAddress address = cacheService.getAddressById(purchaseOrderTitle.getWareAddrId());
		if (address != null) {
			poRespDto.setWareAddrName(address.getShowValue());
			poRespDto.setWareAddrId(address.getId());
		}

		// 客户
		poRespDto.setCustomerName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getCustomerId(), CacheKeyConsts.CUSTOMER));
		poRespDto.setCustomerId(purchaseOrderTitle.getCustomerId());
		poRespDto.setAccountId(purchaseOrderTitle.getAccountId());
		BaseAccount account = cacheService.getAccountById(purchaseOrderTitle.getAccountId());
		if (account != null) {
			poRespDto.setAccountNo(account.getAccountNo());
			poRespDto.setBankName(account.getBankName());
		}
		poRespDto.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				purchaseOrderTitle.getCurrencyId() + ""));
		poRespDto.setCurrencyId(purchaseOrderTitle.getCurrencyId());
		poRespDto.setPayWayName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_WAY, purchaseOrderTitle.getPayWay() + ""));
		poRespDto.setPayWayId(purchaseOrderTitle.getPayWay());
		poRespDto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PO_STS, purchaseOrderTitle.getState() + ""));
		poRespDto.setPerRecAmount(purchaseOrderTitle.getPerRecAmount());
		poRespDto.setStateId(purchaseOrderTitle.getState());
		poRespDto.setOrderTime(purchaseOrderTitle.getOrderTime());
		poRespDto.setPerdictTime(purchaseOrderTitle.getPerdictTime());
		poRespDto.setRequestPayTime(purchaseOrderTitle.getRequestPayTime());
		poRespDto.setOpenType(purchaseOrderTitle.getOpenType());
		poRespDto.setOrderTotalNum(purchaseOrderTitle.getOrderTotalNum());
		poRespDto.setOrderTotalAmount(purchaseOrderTitle.getOrderTotalAmount());
		poRespDto.setArrivalAmount(purchaseOrderTitle.getArrivalAmount());
		poRespDto.setArrivalNum(purchaseOrderTitle.getArrivalNum());
		poRespDto.setPayAmount(purchaseOrderTitle.getPayAmount());
		poRespDto.setInvoiceTotalNum(purchaseOrderTitle.getInvoiceTotalNum());
		poRespDto.setInvoiceTotalAmount(purchaseOrderTitle.getInvoiceTotalAmount());
		poRespDto.setTotalRemainSendNum(purchaseOrderTitle.getTotalRemainSendNum());
		poRespDto.setCreateAt(purchaseOrderTitle.getCreateAt());
		poRespDto.setCreateUser(purchaseOrderTitle.getCreator());
		return poRespDto;
	}

	private List<PoTitleRespDto> convertToResult(List<PurchaseOrderTitle> PoTitles, PoTitleReqDto poTitleReqDto) {
		List<PoTitleRespDto> poTitleList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(PoTitles)) {
			return poTitleList;
		}
		for (PurchaseOrderTitle purchaseOrderTitle : PoTitles) {
			PoTitleRespDto poRespDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
			// 操作集合
			List<CodeValue> operList = getOperList(purchaseOrderTitle.getState(), purchaseOrderTitle.getId());
			poRespDto.setOpertaList(operList);
			poTitleList.add(poRespDto);
		}
		return poTitleList;
	}

	private List<CodeValue> getOperList(Integer state, Integer id) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state, id);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PoTitleRespDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state, Integer id) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DETAIL);
		return opertaList;
	}

	public Integer addPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle1) {
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		// 经营单位 根据项目ID获取经营单位ID
		if (purchaseOrderTitle.getProjectId() != null) {
			BaseProject baseProject = cacheService.getProjectById(purchaseOrderTitle.getProjectId());// 项目
			if (baseProject != null) {
				purchaseOrderTitle.setBusinessUnitId(baseProject.getBusinessUnitId());
			}
		}
		return 0;
	}

	public Result<PoTitleRespDto> queryPurchaseOrderTitleById(Integer id) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(id);
		PoTitleRespDto respDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
		result.setItems(respDto);
		return result;
	}

	public PageResult<PoLineModel> queryPoLinesByPoTitleId(PoTitleReqDto purchaseOrderTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		int offSet = PageUtil.getOffSet(purchaseOrderTitleReqDto.getPage(), purchaseOrderTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, purchaseOrderTitleReqDto.getPer_page());
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByPoId(purchaseOrderTitleReqDto.getId(),
				rowBounds);
		processPoLineModels(poLineList);
		if (CollectionUtils.isNotEmpty(poLineList)) {
			result.setTotalStr(
					"合计：订单总金额：" + DecimalUtil.toAmountString(poLineList.get(0).getOrderTotalAmount()) + "  &nbsp;"
							+ ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
									poLineList.get(0).getCurrencyId().toString())
							+ "  &nbsp;" + "&nbsp;订单总数量："
							+ DecimalUtil.toQuantityString(poLineList.get(0).getOrderTotalNum()) + "&nbsp;订单折扣总金额："
							+ DecimalUtil.toQuantityString(poLineList.get(0).getTotalDiscountAmount()) + "  &nbsp;"
							+ ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
									poLineList.get(0).getCurrencyId().toString()));
		}
		result.setItems(poLineList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), purchaseOrderTitleReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(purchaseOrderTitleReqDto.getPage());
		result.setPer_page(purchaseOrderTitleReqDto.getPer_page());
		return result;
	}

	private void processPoLineModels(List<PoLineModel> poLineList) {
		if (CollectionUtils.isNotEmpty(poLineList)) {
			for (PoLineModel poLine : poLineList) {
				poLine.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						poLine.getCurrencyId() + ""));
				DistributionGoods goods = cacheService.getDistributionGoodsById(poLine.getGoodsId());
				poLine.setGoodsName(goods.getName());
				poLine.setGoodsNo(goods.getNumber());
				poLine.setGoodsBarCode(goods.getBarCode());
				poLine.setGoodsType(goods.getType());
				poLine.setSpecification(goods.getSpecification());
				poLine.setGoodsAmount(poLine.getGoodsAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getGoodsAmount()));
				poLine.setArrivalAmount(poLine.getArrivalAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getArrivalAmount()));
				poLine.setDiscountAmount(poLine.getDiscountAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getDiscountAmount()));
				poLine.setPaidAmount(poLine.getPaidAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getPaidAmount()));
				if (null != poLine.getGoodsStatus()) {
					poLine.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
							poLine.getGoodsStatus() + ""));
				}
			}
		}
	}

	/**
	 * 仅月底冲销使用
	 */
	public List<PurchaseOrderTitle> queryDistributeOrderGroupByProjectId(
			DistributeAccountReqDto distributeAccountReqDto) {
		return purchaseOrderTitleDao.queryDistributeOrderGroupByProjectId(distributeAccountReqDto);
	}

	/**
	 * 仅月底冲销使用
	 */
	public List<PurchaseOrderLineTaxGroupSum> queryTaxGroupSumByProjectId(
			DistributeAccountReqDto distributeAccountReqDto) {
		return purchaseOrderTitleDao.queryTaxGroupSumByProjectId(distributeAccountReqDto);
	}

	/**
	 * 仅月底冲销使用
	 */
	public BigDecimal queryUnDistributeAmountByProjectId(DistributeAccountReqDto distributeAccountReqDto) {
		return purchaseOrderTitleDao.queryUnDistributeAmountByProjectId(distributeAccountReqDto);

	}

	/**
	 * 统计订单头信息
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	public boolean isOverDistriOrderMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		poTitleReqDto.setOrderType(BaseConsts.TWO);
		int count = purchaseOrderTitleDao.countPurchaseOrderTitle(poTitleReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("铺货单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncDisOrderExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/dis_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_39);
			asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncDisOrderExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<PoTitleRespDto> poTitles = purchaseOrderService.queryAllPoTitlesResultsByCon(poTitleReqDto);
		model.put("disOrderList", poTitles);
		return model;
	}

	/**
	 * 获取导出明细信息
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	public List<PoLineModel> queryDistriOrderLine(PoTitleReqDto poTitleReqDto) {
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByCon(poTitleReqDto);
		processPoLineModels(poLineList);
		return poLineList;
	}

	/**
	 * 统计明细信息
	 * 
	 * @param poTitleReqDto
	 * @return
	 */
	public boolean isOverasyncDistriOrderDtlByTitleIdMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		poTitleReqDto.setOrderType(BaseConsts.TWO);
		int count = 0;
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByCon(poTitleReqDto);
		processPoLineModels(poLineList);
		if (CollectionUtils.isNotEmpty(poLineList)) {
			for (int i = 0; i < poLineList.size(); i++) {
				int c = purchaseOrderLineDao.countPoLineListByPoId(poLineList.get(i).getId());
				count = count + c;
			}
			if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
				// 后台导出
				AsyncExcel asyncExcel = new AsyncExcel();
				asyncExcel.setName("铺货单单据明细导出");
				asyncExcel.setClassName(this.getClass().getName());// 导出类名
				asyncExcel.setMethodName("asyncDisOrderDtlExport");// 导出方法
				asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/dis_order_dtl_list.xls");// 导出模板路径
				asyncExcel.setPoType(BaseConsts.INT_40);
				asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
				return true;
			}
		}
		return false;
	}

	public Map<String, Object> asyncDisOrderDtlExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<PurchaseOrderDtlExtResDto> PoTitles = purchaseOrderService.queryAllDtlResultsByCon(poTitleReqDto);
		model.put("disOrderDtlList", PoTitles);
		return model;
	}
	
}
