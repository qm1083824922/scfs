package com.scfs.service.po;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.consts.SysParamConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.api.pms.PmsOutPoRelDao;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.fi.ReceiptOutStoreRelDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.BillOutStoreDtlDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.api.pms.entity.PmsOutPoRel;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.req.ReceiptOutRelReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.fi.entity.ReceiptOutStoreRel;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.BillOutStoreDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.entity.PayAdvanceRelation;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.pay.entity.PayPoRelation;
import com.scfs.domain.pay.entity.PmsStoreOut;
import com.scfs.domain.po.dto.req.AutoProcessPoDto;
import com.scfs.domain.po.dto.req.PoLineReqDto;
import com.scfs.domain.po.dto.req.PoReturnListReqDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoFileAttachRespDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.entity.PurchaseReturnDtl;
import com.scfs.domain.po.excel.PurchaseOrderDtlExtResDto;
import com.scfs.domain.po.excel.PurchaseOrderExcel;
import com.scfs.domain.po.excel.PurchaseOrderLineExcel;
import com.scfs.domain.po.excel.PurchaseOrderTitleExcel;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectItemFileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillOutStoreDetailSearchReqDto;
import com.scfs.service.audit.PoReturnAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.base.goods.DistributionGoodsService;
import com.scfs.service.base.subject.BaseSubjectService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.common.SysParamService;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.interf.PMSSupplierBindService;
import com.scfs.service.logistics.BillInStoreService;
import com.scfs.service.logistics.BillOutStorePickDtlService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.logistics.StlService;
import com.scfs.service.pay.PayAdvanceRelationService;
import com.scfs.service.pay.PayPoRelationService;
import com.scfs.service.pay.PayService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016/10/14.
 */
@Service
public class PurchaseOrderService {

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private PayAdvanceRelationService payAdvanceRelationService;
	@Autowired
	private PayService payService;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private PayPoRelationService payPoRelationService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private BillInStoreService billInStoreService;
	@Autowired
	private BankReceiptService bankReceiptService;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private BillOutStoreDtlDao billOutStoreDtlDao;
	@Autowired
	private BillOutStorePickDtlService billOutStorePickDtlService;
	@Autowired
	private StlService stlService;
	@Autowired
	private StlDao stlDao;
	@Autowired
	private PoReturnAuditService poReturnAuditService;
	@Autowired
	private PMSSupplierBindService pmsSupplierBindService;
	@Autowired
	private DistributionGoodsService distributionGoodsService;
	@Autowired
	private BaseAccountDao accountDao;
	@Autowired
	private ReceiptOutStoreRelDao receiptOutStoreRelDao;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private PmsOutPoRelDao pmsOutPoRelDao;
	@Autowired
	private AuditFlowService auditFlowService;
	@Autowired
	private BaseSubjectService baseSubjectService;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private BillDeliveryService billDeliveryService;
	@Autowired
	private BillOutStoreService billOutStoreService;

	public PageResult<PoTitleRespDto> queryPoTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoTitleRespDto> result = new PageResult<PoTitleRespDto>();
		int offSet = PageUtil.getOffSet(poTitleReqDto.getPage(), poTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poTitleReqDto.getPer_page());
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PoTitleRespDto> poRespDto = new LinkedList<PoTitleRespDto>();
		List<PurchaseOrderTitle> poTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto, rowBounds);
		if (poTitleReqDto.getOrderType() == 0) {
			if (poTitleReqDto.getNeedSum() != null && poTitleReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
				List<PurchaseOrderTitle> poSumList = purchaseOrderTitleDao.sumPoTitle(poTitleReqDto);
				if (CollectionUtils.isNotEmpty(poSumList)) {
					BigDecimal numTotal = BigDecimal.ZERO;
					BigDecimal amountRmbTotal = BigDecimal.ZERO;
					BigDecimal titleRmbTotal = BigDecimal.ZERO;
					for (PurchaseOrderTitle purchaseOrderTitle : poSumList) {
						BigDecimal rmbAmount = ServiceSupport.amountNewToRMB(purchaseOrderTitle.getOrderTotalAmount(),
								purchaseOrderTitle.getCurrencyId(), null);
						if (purchaseOrderTitle.getTotalDiscountAmount() != null) {
							BigDecimal disRmbTotal = ServiceSupport.amountNewToRMB(
									purchaseOrderTitle.getTotalDiscountAmount(), purchaseOrderTitle.getCurrencyId(),
									null);
							titleRmbTotal = DecimalUtil.add(titleRmbTotal,
									disRmbTotal == null ? BigDecimal.ZERO : disRmbTotal);
						}
						amountRmbTotal = DecimalUtil.add(amountRmbTotal, rmbAmount);
						numTotal = DecimalUtil.add(numTotal, purchaseOrderTitle.getOrderTotalNum());
					}
					result.setTotalStr("合计：订单总金额：" + DecimalUtil.toAmountString(amountRmbTotal) + " CNY &nbsp;"
							+ "&nbsp;订单总数量：" + DecimalUtil.toQuantityString(numTotal) + "&nbsp;" + "&nbsp;订单折扣总金额："
							+ DecimalUtil.toQuantityString(titleRmbTotal) + " CNY");
					result.setTotalAmount(amountRmbTotal);
					result.setExRateTotalAmount(titleRmbTotal);
				}
			}
			// 添加操作
			poRespDto = convertToResult(poTitles, poTitleReqDto);
		} else if (poTitleReqDto.getOrderType() == 1) {
			// 添加操作
			poRespDto = convertReturnToResult(poTitles, poTitleReqDto);
			if (poTitleReqDto.getNeedSum() != null && poTitleReqDto.getNeedSum() == BaseConsts.ONE) {
				List<PurchaseOrderTitle> poSumList = purchaseOrderTitleDao.sumPoTitle(poTitleReqDto);
				if (CollectionUtils.isNotEmpty(poSumList)) {
					BigDecimal numTotal = BigDecimal.ZERO;
					BigDecimal amountRmbTotal = BigDecimal.ZERO;
					for (PurchaseOrderTitle purchaseOrderTitle : poSumList) {
						BigDecimal rmbAmount = ServiceSupport.amountNewToRMB(purchaseOrderTitle.getOrderTotalAmount(),
								purchaseOrderTitle.getCurrencyId(), null);
						amountRmbTotal = DecimalUtil.add(amountRmbTotal,
								rmbAmount == null ? BigDecimal.ZERO : rmbAmount);
						numTotal = DecimalUtil.add(numTotal, purchaseOrderTitle.getOrderTotalNum() == null
								? BigDecimal.ZERO : purchaseOrderTitle.getOrderTotalNum());
					}
					result.setTotalStr("合计：总退货金额：" + DecimalUtil.toAmountString(amountRmbTotal) + " CNY &nbsp;"
							+ "&nbsp;总退货数量：" + DecimalUtil.toQuantityString(numTotal));
				}
			}
		} else if (poTitleReqDto.getOrderType() == 3) {
			// 添加操作
			poRespDto = convertDistributionReturnToResult(poTitles, poTitleReqDto);
			if (poTitleReqDto.getNeedSum() != null && poTitleReqDto.getNeedSum() == BaseConsts.ONE) {
				List<PurchaseOrderTitle> poSumList = purchaseOrderTitleDao.sumPoTitle(poTitleReqDto);
				if (CollectionUtils.isNotEmpty(poSumList)) {
					BigDecimal numTotal = BigDecimal.ZERO;
					BigDecimal amountRmbTotal = BigDecimal.ZERO;
					BigDecimal refundAmountRmbTotal = BigDecimal.ZERO;
					for (PurchaseOrderTitle purchaseOrderTitle : poSumList) {
						BigDecimal rmbAmount = ServiceSupport.amountNewToRMB(purchaseOrderTitle.getOrderTotalAmount(),
								purchaseOrderTitle.getCurrencyId(), null);
						BigDecimal rmbRefundAmount = ServiceSupport.amountNewToRMB(
								purchaseOrderTitle.getTotalRefundAmount(), purchaseOrderTitle.getCurrencyId(), null);
						amountRmbTotal = DecimalUtil.add(amountRmbTotal,
								rmbAmount == null ? BigDecimal.ZERO : rmbAmount);
						numTotal = DecimalUtil.add(numTotal, purchaseOrderTitle.getOrderTotalNum() == null
								? BigDecimal.ZERO : purchaseOrderTitle.getOrderTotalNum());
						refundAmountRmbTotal = DecimalUtil.add(refundAmountRmbTotal,
								rmbRefundAmount == null ? BigDecimal.ZERO : rmbRefundAmount);
					}
					result.setTotalStr("合计：总退货金额：" + DecimalUtil.toAmountString(amountRmbTotal) + " CNY &nbsp;"
							+ "&nbsp;总预退款金额：" + DecimalUtil.toAmountString(refundAmountRmbTotal) + " CNY &nbsp;"
							+ "&nbsp;总退货数量：" + DecimalUtil.toQuantityString(numTotal));
				}
			}
		} else if (poTitleReqDto.getOrderType() == BaseConsts.FOUR) {// 铺货结算单
			// 添加操作
			poRespDto = convertDistributionTotalResult(poTitles, poTitleReqDto);
			if (poTitleReqDto.getNeedSum() != null && poTitleReqDto.getNeedSum() == BaseConsts.ONE) {
				List<PurchaseOrderTitle> poSumList = purchaseOrderTitleDao.sumPoTitle(poTitleReqDto);
				if (CollectionUtils.isNotEmpty(poSumList)) {
					BigDecimal numTotal = BigDecimal.ZERO;
					BigDecimal amountRmbTotal = BigDecimal.ZERO;
					for (PurchaseOrderTitle purchaseOrderTitle : poSumList) {
						BigDecimal rmbAmount = ServiceSupport.amountNewToRMB(purchaseOrderTitle.getOrderTotalAmount(),
								purchaseOrderTitle.getCurrencyId(), null);
						amountRmbTotal = DecimalUtil.add(amountRmbTotal,
								rmbAmount == null ? BigDecimal.ZERO : rmbAmount);
						numTotal = DecimalUtil.add(numTotal, purchaseOrderTitle.getOrderTotalNum() == null
								? BigDecimal.ZERO : purchaseOrderTitle.getOrderTotalNum());
					}
					result.setTotalStr("合计：总退货金额：" + DecimalUtil.toAmountString(amountRmbTotal) + " CNY &nbsp;"
							+ "&nbsp;总退货数量：" + DecimalUtil.toQuantityString(numTotal));
				}
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

	public boolean isOverPoOrderMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = purchaseOrderTitleDao.countPurchaseOrderTitle(poTitleReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("采购单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncPoOrderExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/po_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.TWO);
			asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public boolean isOverPoReturnMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		poTitleReqDto.setOrderType(1);
		int count = purchaseOrderTitleDao.countPurchaseOrderTitle(poTitleReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("采购退货单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncPoReturnExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/po_return_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_27);
			asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public boolean isOverasyncPoReturnDtlByTitleIdMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(1);
		int count = purchaseOrderLineDao.countPoLineListByCon(poTitleReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("采购退货单单据明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncPoReturnLineExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/po_return_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_28);
			asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncPoReturnExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		poTitleReqDto.setOrderType(1);
		List<PoTitleRespDto> PoTitles = convertReturnToResult(
				purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto), poTitleReqDto);
		model.put("poReturnList", PoTitles);
		return model;
	}

	public Map<String, Object> asyncPoReturnLineExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		poTitleReqDto.setOrderType(1);
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PurchaseReturnDtl> poLines = purchaseOrderLineDao.queryBillInStoreListDivide(poTitleReqDto);
		for (PurchaseReturnDtl poLine : poLines) {
			poLine.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					poLine.getCurrencyType() + ""));
		}
		model.put("poReturnLineList", poLines);
		return model;
	}

	public Map<String, Object> asyncPoOrderExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<PoTitleRespDto> poTitles = queryAllPoTitlesResultsByCon(poTitleReqDto);
		model.put("poList", poTitles);
		return model;
	}

	public List<PoTitleRespDto> queryAllPoTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		if (poTitleReqDto.getUserId() == null) {
			poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<PurchaseOrderTitle> PoTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto);
		// 添加操作
		List<PoTitleRespDto> poRespDto = convertToResult(PoTitles, poTitleReqDto);
		return poRespDto;

	}

	/**
	 * 获取编号分组分页数据
	 * 
	 * @param reqDto
	 * @param rowBounds
	 * @return
	 */
	public List<PoTitleRespDto> queryPuOrderTitleListGroupByNo(PoTitleReqDto reqDto, RowBounds rowBounds) {
		List<PurchaseOrderTitle> PoTitles = purchaseOrderTitleDao.queryPuOrderTitleListGroupByNo(reqDto, rowBounds);
		// 添加操作
		List<PoTitleRespDto> poRespDto = convertToResult(PoTitles, reqDto);
		return poRespDto;
	}

	/**
	 * 获取编号分组所有有数据
	 * 
	 * @param reqDto
	 * @return
	 */
	public List<PoTitleRespDto> queryPuOrderTitleListGroupByNo(PoTitleReqDto reqDto) {
		List<PurchaseOrderTitle> poTitles = purchaseOrderTitleDao.queryPuOrderTitleListGroupByNo(reqDto);
		// 添加操作
		List<PoTitleRespDto> poRespDto = convertToResult(poTitles, reqDto);
		return poRespDto;
	}

	/**
	 * 获取总PO待付款金额
	 * 
	 * @param reqDto
	 * @return
	 */
	public BigDecimal querySumPoBlance(PoTitleReqDto reqDto) {
		BigDecimal poBlance = purchaseOrderTitleDao.queryPoBlance(reqDto);
		if (poBlance == null) {
			poBlance = BigDecimal.ZERO;
		}
		return poBlance;
	}

	public List<PurchaseReturnDtl> queryAllPoReturnLineResultsByCon(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PurchaseReturnDtl> poTitles = purchaseOrderLineDao.queryBillInStoreListDivide(poTitleReqDto);
		for (PurchaseReturnDtl poTitle : poTitles) {
			poTitle.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					poTitle.getCurrencyType() + ""));
			// 项目
			poTitle.setProjectName(cacheService.showProjectNameById(poTitle.getProjectId()));
			// 供应商
			poTitle.setSupplierName(
					cacheService.showSubjectNameByIdAndKey(poTitle.getSupplierId(), CacheKeyConsts.SUPPLIER));
			// 仓库
			poTitle.setWarehouseName(
					cacheService.showSubjectNameByIdAndKey(poTitle.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		}
		return poTitles;

	}

	public List<PoTitleRespDto> queryAllPoReturnTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		if (poTitleReqDto.getUserId() == null) {
			poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<PurchaseOrderTitle> PoTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto);
		// 添加操作
		List<PoTitleRespDto> poRespDto = convertReturnToResult(PoTitles, poTitleReqDto);
		return poRespDto;

	}

	public boolean isOverasyncPoDtlByTitleIdMaxLine(Integer poTitleId) {
		int count = purchaseOrderLineDao.countPoLineListByPoId(poTitleId);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("采购单单据明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncPoDtlByTitleIdExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/po_order_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.TWO);
			asyncExcelService.addAsyncExcel(poTitleId, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncPoDtlByTitleIdExport(Integer poTitleId) {
		Map<String, Object> model = Maps.newHashMap();
		List<PurchaseOrderDtlExtResDto> PoTitles = queryPoDtlByTitleId(poTitleId);
		model.put("poOrderDtlList", PoTitles);
		return model;
	}

	public List<PurchaseOrderDtlExtResDto> queryPoDtlByTitleId(Integer poTitleId) {
		List<PurchaseOrderDtlExtResDto> result = new LinkedList<PurchaseOrderDtlExtResDto>();
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(poTitleId);
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByPoId(poTitleId);
		PurchaseOrderDtlExtResDto purchaseOrderDtlExtResDto = new PurchaseOrderDtlExtResDto();
		purchaseOrderDtlExtResDto = purConvertToRes(purchaseOrderTitle, purchaseOrderDtlExtResDto);
		if (CollectionUtils.isNotEmpty(poLineList)) {
			for (PoLineModel poLine : poLineList) {
				poLine.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						poLine.getCurrencyId() + ""));
				BaseGoods goods = cacheService.getGoodsById(poLine.getGoodsId());
				purchaseOrderDtlExtResDto.setGoodsName(goods.getName());
				purchaseOrderDtlExtResDto.setGoodsNo(goods.getNumber());
				purchaseOrderDtlExtResDto.setGoodsBarCode(goods.getBarCode());
				purchaseOrderDtlExtResDto.setGoodsType(goods.getType());
				purchaseOrderDtlExtResDto.setArrivalAmount(poLine.getArrivalAmount());
				purchaseOrderDtlExtResDto.setArrivalNum(poLine.getArrivalNum());
				purchaseOrderDtlExtResDto.setOrderTotalAmount(poLine.getOrderTotalAmount());
				purchaseOrderDtlExtResDto.setGoodsPrice(poLine.getGoodsPrice());
				purchaseOrderDtlExtResDto.setGoodsAmount(poLine.getGoodsAmount());
				result.add(purchaseOrderDtlExtResDto);
			}
		}
		return result;
	}

	public boolean isOverPoOrderDtlMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		int count = 0;
		List<PurchaseOrderTitle> PoTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto);
		if (CollectionUtils.isNotEmpty(PoTitles)) {
			for (int i = 0; i < PoTitles.size(); i++) {
				int c = purchaseOrderLineDao.countPoLineListByPoId(PoTitles.get(i).getId());
				count = count + c;
			}
			if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
				// 后台导出
				AsyncExcel asyncExcel = new AsyncExcel();
				asyncExcel.setName("采购单单据明细导出");
				asyncExcel.setClassName(this.getClass().getName());// 导出类名
				asyncExcel.setMethodName("asyncPoOrderDtlExport");// 导出方法
				asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/po_order_dtl_list.xls");// 导出模板路径
				asyncExcel.setPoType(BaseConsts.TWO);
				asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
				return true;
			}
		}
		return false;
	}

	public Map<String, Object> asyncPoOrderDtlExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<PurchaseOrderDtlExtResDto> PoTitles = queryAllDtlResultsByCon(poTitleReqDto);
		model.put("poOrderDtlList", PoTitles);
		return model;
	}

	public List<PurchaseOrderDtlExtResDto> queryAllDtlResultsByCon(PoTitleReqDto poTitleReqDto) {
		List<PurchaseOrderDtlExtResDto> result = new LinkedList<PurchaseOrderDtlExtResDto>();
		if (poTitleReqDto.getUserId() == null) {
			poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<PurchaseOrderTitle> PoTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto);
		for (int i = 0; i < PoTitles.size(); i++) {
			List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByPoId(PoTitles.get(i).getId());
			if (CollectionUtils.isNotEmpty(poLineList)) {
				for (PoLineModel poLine : poLineList) {
					PurchaseOrderDtlExtResDto purchaseOrderDtlExtResDto = new PurchaseOrderDtlExtResDto();
					purchaseOrderDtlExtResDto = purConvertToRes(PoTitles.get(i), purchaseOrderDtlExtResDto);
					poLine.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
							poLine.getCurrencyId() + ""));
					BaseGoods goods = cacheService.getGoodsById(poLine.getGoodsId());
					purchaseOrderDtlExtResDto.setGoodsName(goods.getName());
					purchaseOrderDtlExtResDto.setGoodsNo(goods.getNumber());
					purchaseOrderDtlExtResDto.setGoodsBarCode(goods.getBarCode());
					purchaseOrderDtlExtResDto.setGoodsType(goods.getType());
					purchaseOrderDtlExtResDto.setArrivalAmount(poLine.getArrivalAmount());
					purchaseOrderDtlExtResDto.setArrivalNum(poLine.getArrivalNum());
					purchaseOrderDtlExtResDto.setOrderTotalAmount(poLine.getOrderTotalAmount());
					purchaseOrderDtlExtResDto.setGoodsPrice(poLine.getGoodsPrice());
					purchaseOrderDtlExtResDto.setGoodsAmount(poLine.getGoodsAmount());
					result.add(purchaseOrderDtlExtResDto);
				}
			}
		}
		return result;
	}

	public Result<PoTitleRespDto> queryPurchaseOrderTitleById(Integer id) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(id);
		PoTitleRespDto respDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
		result.setItems(respDto);
		return result;
	}

	private List<PoTitleRespDto> convertToResult(List<PurchaseOrderTitle> PoTitles, PoTitleReqDto poTitleReqDto) {
		List<PoTitleRespDto> poTitleList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(PoTitles)) {
			return poTitleList;
		}
		for (PurchaseOrderTitle purchaseOrderTitle : PoTitles) {
			PoTitleRespDto poRespDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
			// 操作集合
			List<CodeValue> operList = getOperList(purchaseOrderTitle.getState(), purchaseOrderTitle);
			poRespDto.setOpertaList(operList);
			poTitleList.add(poRespDto);
		}
		return poTitleList;
	}

	private List<PoTitleRespDto> convertReturnToResult(List<PurchaseOrderTitle> PoTitles, PoTitleReqDto poTitleReqDto) {
		List<PoTitleRespDto> poTitleList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(PoTitles)) {
			return poTitleList;
		}
		for (PurchaseOrderTitle purchaseOrderTitle : PoTitles) {
			PoTitleRespDto poRespDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
			poRespDto.setStateName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PO_RETURN_STS, purchaseOrderTitle.getState() + ""));
			// 操作集合
			List<CodeValue> operList = getOperReturnList(purchaseOrderTitle.getState(), purchaseOrderTitle.getId());
			poRespDto.setOpertaList(operList);
			poTitleList.add(poRespDto);
		}
		return poTitleList;
	}

	private List<CodeValue> getOperReturnList(Integer state, Integer id) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperReturnListByState(state, id);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PoTitleRespDto.OperateReturn.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperReturnListByState(Integer state, Integer id) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		// 状态过滤
		switch (state) {
		// 状态,1表示待提交 操作浏览、编辑、提交、删除。
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		// 状态,1表示待提交 操作浏览、编辑、提交、删除。
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.INT_30:
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

	private List<CodeValue> getOperList(Integer state, PurchaseOrderTitle purchaseOrderTitle) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state, purchaseOrderTitle);
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
	private List<String> getOperListByState(Integer state, PurchaseOrderTitle purchaseOrderTitle) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		// 状态过滤
		switch (state) {
		// 状态,1表示待提交 操作浏览、编辑、提交、删除。
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		// 状态,1表示待提交 操作浏览、编辑、提交、删除。
		case BaseConsts.INT_20:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.INT_35:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		case BaseConsts.FIVE:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			if (isCanReceive(purchaseOrderTitle.getId())) {
				opertaList.add(OperateConsts.RECEIVE);
				if (!Integer.valueOf(BaseConsts.ONE).equals(purchaseOrderTitle.getFlyOrderFlag())) {	//未操作过飞单
					opertaList.add(OperateConsts.FLY_ORDER);
				}
			}
			break;
		}
		return opertaList;
	}

	private boolean isCanReceive(Integer id) {
		int count = purchaseOrderLineDao.queryReceiveCountByPoId(id);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	private List<PoTitleRespDto> convertDistributionReturnToResult(List<PurchaseOrderTitle> PoTitles,
			PoTitleReqDto poTitleReqDto) {
		List<PoTitleRespDto> poTitleList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(PoTitles)) {
			return poTitleList;
		}
		for (PurchaseOrderTitle purchaseOrderTitle : PoTitles) {
			PoTitleRespDto poRespDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
			poRespDto.setStateName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PO_DISTRI_STS, purchaseOrderTitle.getState() + ""));
			// 操作集合
			List<CodeValue> operList = getOperDistributionReturnList(purchaseOrderTitle.getState(),
					purchaseOrderTitle.getId());
			poRespDto.setOpertaList(operList);
			poTitleList.add(poRespDto);
		}
		return poTitleList;
	}

	private List<CodeValue> getOperDistributionReturnList(Integer state, Integer id) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperDistributionReturnListByState(state, id);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PoTitleRespDto.OperateReturn.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperDistributionReturnListByState(Integer state, Integer id) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		// 状态过滤
		switch (state) {
		// 状态,1表示待提交 操作浏览、编辑、提交、删除。
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.TEN:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.INT_30:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.FIVE:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.SEVEN:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}

	/**
	 * 结算单数据查询
	 * 
	 * @param PoTitles
	 * @param poTitleReqDto
	 * @return
	 */
	private List<PoTitleRespDto> convertDistributionTotalResult(List<PurchaseOrderTitle> PoTitles,
			PoTitleReqDto poTitleReqDto) {
		List<PoTitleRespDto> poTitleList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(PoTitles)) {
			return poTitleList;
		}
		for (PurchaseOrderTitle purchaseOrderTitle : PoTitles) {
			PoTitleRespDto poRespDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
			poRespDto.setStateName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PO_DISTRI_STS, purchaseOrderTitle.getState() + ""));
			// 操作集合
			List<CodeValue> operList = getOperDistributionTotalList(purchaseOrderTitle.getState(),
					purchaseOrderTitle.getId());
			poRespDto.setOpertaList(operList);
			poTitleList.add(poRespDto);
		}
		return poTitleList;
	}

	private List<CodeValue> getOperDistributionTotalList(Integer state, Integer id) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperDistributionTotalListByState(state, id);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PoTitleRespDto.OperateReturn.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表 结算单
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperDistributionTotalListByState(Integer state, Integer id) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList();
		// 状态过滤
		switch (state) {
		// 状态,1表示待提交 操作浏览、编辑、提交、删除。
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.TEN:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.INT_25:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.INT_30:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.FIVE:
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.SEVEN:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}

	public Integer addPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		// 经营单位 根据项目ID获取经营单位ID
		if (purchaseOrderTitle.getProjectId() != null) {
			BaseProject baseProject = cacheService.getProjectById(purchaseOrderTitle.getProjectId());// 项目
			if (baseProject != null) {
				purchaseOrderTitle.setBusinessUnitId(baseProject.getBusinessUnitId());
			}
			ProjectItem projectItem = projectItemService.getProjectItem(purchaseOrderTitle.getProjectId());
			if (null == projectItem) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "当前项目无有效条款");
			}
		}
		purchaseOrderTitle.setState(BaseConsts.ONE);// 待提交
		// 订单编号
		String orderNo = sequenceService.getNumDateByBusName(BaseConsts.PO_NO_PRE, SeqConsts.PO_NO, BaseConsts.INT_13);
		purchaseOrderTitle.setOrderTotalNum(BigDecimal.ZERO);
		purchaseOrderTitle.setOrderTotalAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setPayAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setInvoiceTotalAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setInvoiceTotalNum(BigDecimal.ZERO);
		purchaseOrderTitle.setArrivalNum(BigDecimal.ZERO);
		purchaseOrderTitle.setArrivalAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setOrderNo(orderNo);
		purchaseOrderTitle.setCreateAt(new Date());
		purchaseOrderTitle.setIsDelete(BaseConsts.ZERO);
		purchaseOrderTitle
				.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		purchaseOrderTitle.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		int count = purchaseOrderTitleDao.insert(purchaseOrderTitle);
		if (count < 1) {
			throw new BaseException(ExcMsgEnum.PO_ADD_EXCEPTION);
		}
		return purchaseOrderTitle.getId();
	}

	/**
	 * 删除采购单
	 *
	 * @param purchaseOrderTitle
	 * @return
	 */
	public int deletePurchaseOrderTitle(PoTitleReqDto poTitleReqDto) {
		PurchaseOrderTitle oldPo = queryAndLockById(poTitleReqDto.getId());
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		if (oldPo.getState() != BaseConsts.ONE) {// 校验采购单状态，已经提交，不能删除
			throw new BaseException(ExcMsgEnum.PO_NOT_DELETE);
		}
		oldPo.setIsDelete(BaseConsts.ONE);
		if (oldPo.getOrderType() == BaseConsts.ONE) {
			// 删除所有明细
			List<PurchaseReturnDtl> poTitles = purchaseOrderLineDao.queryBillInStoreListDivide(poTitleReqDto);
			PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
			purchaseOrderLine.setIsDelete(BaseConsts.ONE);
			purchaseOrderLine.setPoId(poTitleReqDto.getId());
			purchaseOrderLineDao.deleteAllPurchaseLineByPoId(purchaseOrderLine);
			oldPo.setOrderTotalNum(BigDecimal.ZERO);
			oldPo.setOrderTotalAmount(BigDecimal.ZERO);
			for (PurchaseReturnDtl poTitle : poTitles) {
				stlService.releaseStlSaleNum(poTitle.getStlId(), poTitle.getReturnNum().abs());
			}
		}
		return updatePurchaseOrderTitle(oldPo);
	}

	/**
	 * 更新po 先锁定后更新
	 *
	 * @param purchaseOrderTitle
	 * @return
	 */
	public int updatePurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		if (purchaseOrderTitle != null) {
			int num = purchaseOrderTitleDao.updatePurchaseOrderTitleById(purchaseOrderTitle);
			if (num != 1) {
				throw new BaseException(ExcMsgEnum.PO_UPDATE_EXCEPTION, purchaseOrderTitle.getId());
			}
			return num;
		} else {
			throw new BaseException(ExcMsgEnum.ENTITY_UPDATE_NOT_EXSIT);
		}
	}

	public void submitPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		PurchaseOrderTitle oldPo = queryAndLockById(purchaseOrderTitle.getId());
		if (oldPo.getOrderType() == BaseConsts.ZERO) {
			ProjectItem projectItem = projectItemService.getProjectItem(oldPo.getProjectId());
			if (projectItem == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购订单项目无条款，不能提交！");
			}
			if (oldPo.getState() != BaseConsts.ONE) {// 校验采购单状态，不能重复提交
				throw new BaseException(ExcMsgEnum.PO_NOT_SUBMIT);
			}

			List<PoLineModel> poLineModels = purchaseOrderLineDao.queryPoLineListByPoId(oldPo.getId());
			if (CollectionUtils.isEmpty(poLineModels)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购明细为空，不能提交！");
			}
			oldPo.setState(BaseConsts.FIVE);
			// 提交po采购单 创建付款单,有预付款的情况
			BaseProject baseProject = cacheService.getProjectById(oldPo.getProjectId());
			if (baseProject.getBizType().equals(BaseConsts.SEVEN)) {// 质押项目,不提交付款
				oldPo.setPayAmount(oldPo.getOrderTotalAmount());
				if (oldPo.getPayWay() == null) {
					oldPo.setPayWay(BaseConsts.ONE);// 付款方式TT
				}
				if (oldPo.getAccountId() == null) {
					List<CodeValue> codeList = commonService.getAllOwnCv("SUBJECT_ACCOUNT", oldPo.getSupplierId() + "");
					oldPo.setAccountId(Integer.parseInt(codeList.get(BaseConsts.ZERO).getCode()));// 获取供应商下付款账户
				}
				if (oldPo.getRequestPayTime() == null) {
					oldPo.setRequestPayTime(oldPo.getPerdictTime());// 获取预计到货时间
				}
				validAndUpdate(oldPo);// 如果是要求付款，创建付款单 付款与订单明细关系 付款与水单关系 更新水单
			} else {
				if (oldPo.getIsRequestPay() == BaseConsts.ONE) {
					oldPo.setPayAmount(oldPo.getOrderTotalAmount());
					validAndUpdate(oldPo);// 如果是要求付款，创建付款单 付款与订单明细关系 付款与水单关系
											// 更新水单
				}
			}
			updatePurchaseOrderTitle(oldPo);
		} else if (oldPo.getOrderType() == BaseConsts.ONE) {
			if (oldPo.getState() != BaseConsts.ONE) {// 校验采购单状态，不能重复提交
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购退货单已经提交，不能提交！");
			}
			List<PoLineModel> poLineModels = purchaseOrderLineDao.queryPoLineListByPoId(oldPo.getId());
			if (CollectionUtils.isEmpty(poLineModels)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购退货单明细为空，不能提交！");
			}
			AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_18, null);
			if (null == startAuditNode) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
			}
			oldPo.setState(startAuditNode.getAuditNodeState());
			updatePurchaseOrderTitle(oldPo);
			createBillOutStore(oldPo);
			poReturnAuditService.startAudit(oldPo, startAuditNode);
		} else if (oldPo.getOrderType() == BaseConsts.THREE) {

		}
	}

	private void createBillOutStore(PurchaseOrderTitle oldPo) {
		// TODO Auto-generated method stub
		BillOutStore billOutStore = createBillOutStoreEntity(oldPo);
		createBillOutStoreDtlEntity(billOutStore);

	}

	/**
	 * 创建出库单头
	 * 
	 * @param billDelivery
	 * @return
	 */
	private BillOutStore createBillOutStoreEntity(PurchaseOrderTitle purchaseOrderTitle) {
		BillOutStore billOutStore = new BillOutStore();
		BigDecimal returnNum = purchaseOrderLineDao.queryReturnNumByPoId(purchaseOrderTitle.getId());
		BigDecimal returnAmount = purchaseOrderLineDao.queryReturnAmountByPoId(purchaseOrderTitle.getId());
		billOutStore.setBillNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_BILL_OUT_STORE,
				SeqConsts.BILL_OUT_STORE_NO, BaseConsts.INT_13));
		billOutStore.setBillType(BaseConsts.FIVE);
		billOutStore.setAffiliateNo(purchaseOrderTitle.getAppendNo());
		billOutStore.setProjectId(purchaseOrderTitle.getProjectId());
		billOutStore.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		billOutStore.setCustomerId(purchaseOrderTitle.getSupplierId());
		billOutStore.setCustomerAddressId(purchaseOrderTitle.getSupplierAddressId());
		billOutStore.setTransferMode(purchaseOrderTitle.getTransferMode());
		billOutStore.setCostAmount(returnAmount.abs());
		billOutStore.setPoAmount(purchaseOrderTitle.getOrderTotalAmount());
		billOutStore.setSendAmount(returnAmount.abs());
		billOutStore.setSendNum(returnNum.abs());
		billOutStore.setRequiredSendDate(purchaseOrderTitle.getOrderTime());
		billOutStore.setCurrencyType(purchaseOrderTitle.getCurrencyId());
		billOutStore.setPickupNum(BigDecimal.ZERO);
		billOutStore.setPickupAmount(BigDecimal.ZERO);
		billOutStore.setStatus(BaseConsts.INT_25); // 待财务审核
		billOutStore.setRemark(purchaseOrderTitle.getRemark());
		billOutStore.setCreator(ServiceSupport.getUser().getChineseName());
		billOutStore.setCreatorId(ServiceSupport.getUser().getId());
		billOutStore.setPoReturnId(purchaseOrderTitle.getId());
		billOutStore.setSignStandard(purchaseOrderTitle.getSignStandard());
		billOutStore.setCertificateId(purchaseOrderTitle.getCertificateId());
		billOutStore.setCertificateName(purchaseOrderTitle.getCertificateName());
		billOutStore.setOfficialSeal(purchaseOrderTitle.getOfficialSeal());
		billOutStore.setPayAmount(BigDecimal.ZERO);
		billOutStore.setReturnNum(BigDecimal.ZERO);
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
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		poTitleReqDto.setId(billOutStore.getPoReturnId());
		List<PurchaseReturnDtl> poTitles = purchaseOrderLineDao.queryBillInStoreListDivide(poTitleReqDto);
		for (PurchaseReturnDtl purchaseReturnDtl : poTitles) {
			BillOutStoreDtl billOutStoreDtl = new BillOutStoreDtl();
			billOutStoreDtl.setBillOutStoreId(billOutStore.getId());
			billOutStoreDtl.setGoodsId(purchaseReturnDtl.getGoodsId());
			billOutStoreDtl.setSendNum(purchaseReturnDtl.getReturnNum().abs());
			billOutStoreDtl.setSendPrice(purchaseReturnDtl.getReturnPrice());
			billOutStoreDtl.setPickupNum(BigDecimal.ZERO);
			billOutStoreDtl.setCostAmount(
					DecimalUtil.multiply(purchaseReturnDtl.getCostPrice(), purchaseReturnDtl.getReturnNum().abs()));
			billOutStoreDtl.setPoAmount(
					DecimalUtil.multiply(purchaseReturnDtl.getPoPrice(), purchaseReturnDtl.getReturnNum().abs()));
			billOutStoreDtl.setStlId(purchaseReturnDtl.getStlId());
			billOutStoreDtl.setBatchNo(purchaseReturnDtl.getBatchNo());
			billOutStoreDtl.setGoodsStatus(purchaseReturnDtl.getGoodsStatus());
			billOutStoreDtl.setAssignStlFlag(BaseConsts.ONE);
			billOutStoreDtl.setCreator(ServiceSupport.getUser().getChineseName());
			billOutStoreDtl.setCreatorId(ServiceSupport.getUser().getId());
			billOutStoreDtl.setPoReturnId(purchaseReturnDtl.getPoId());
			billOutStoreDtl.setPoReturnDtlId(purchaseReturnDtl.getId());
			billOutStoreDtl.setPayRate(purchaseReturnDtl.getPayRate());
			billOutStoreDtl.setPayRealCurrency(purchaseReturnDtl.getPayRealCurrency());
			billOutStoreDtl.setPayTime(purchaseReturnDtl.getPayTime());
			billOutStoreDtl.setPayPrice(purchaseReturnDtl.getPayPrice());
			billOutStoreDtlDao.insert(billOutStoreDtl);
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

	public Integer addPayOrderByPo(PurchaseOrderTitle oldPo) {
		return addPayOrderByPo(oldPo, 0);
	}

	/**
	 * 
	 * @param oldPo
	 *            采购单
	 * @param payWayType
	 *            付款支付类型 0-全部 1-预付 2-尾款
	 * @return
	 */
	public Integer addPayOrderByPo(PurchaseOrderTitle oldPo, Integer payWayType) {
		PayOrder payOrder = createPayOrder(oldPo);
		payOrder.setPayWayType(payWayType);
		int payId = payService.createPayOrder(payOrder).getId();

		BaseProject baseProject = cacheService.getProjectById(payOrder.getProjectId());
		PayPoRelationReqDto record = new PayPoRelationReqDto();
		record.setPayId(payId);
		List<PayPoRelation> relList = new ArrayList<PayPoRelation>();
		List<PoLineModel> poLineModels = purchaseOrderLineDao.queryPoLineListByPoId(oldPo.getId());
		for (PoLineModel item : poLineModels) {
			PayPoRelation paypo = new PayPoRelation();
			paypo.setPoId(oldPo.getId());
			paypo.setPoLineId(item.getId());
			BigDecimal deductionMoney = item.getDeductionMoney();
			if (payWayType.equals(BaseConsts.TWO)) { // 2-尾款
				BigDecimal payAmount = calcPayAmount(item, payWayType, baseProject.getBizType());
				BigDecimal prePayAmount = DecimalUtil.multiply(item.getPayPrice(), item.getGoodsNum());
				paypo.setPayAmount(DecimalUtil.formatScale2(payAmount));
				paypo.setPrePayAmount(DecimalUtil.formatScale2(prePayAmount)); // 由于预付金额后面需参与计算，不能四舍五入
			} else {
				BigDecimal payAmount = calcPayAmount(item, payWayType, baseProject.getBizType());
				paypo.setPayAmount(DecimalUtil.formatScale2(payAmount));
				paypo.setPrePayAmount(BigDecimal.ZERO);
			}
			paypo.setDiscountAmount(item.getDiscountAmount());
			paypo.setDuctionMoney(deductionMoney);// 采购订单的抵扣金额
			relList.add(paypo);
		}
		record.setRelList(relList);
		payPoRelationService.createPayPoRelation(record, baseProject);

		if (oldPo.getPerRecAmount() != null && DecimalUtil.gt(oldPo.getPerRecAmount(), BigDecimal.ZERO)) {
			BankReceiptSearchReqDto bankReceiptReqDto = new BankReceiptSearchReqDto();
			bankReceiptReqDto.setProjectId(oldPo.getProjectId());
			bankReceiptReqDto.setCustId(oldPo.getCustomerId());
			bankReceiptReqDto.setCurrencyType(oldPo.getCurrencyId());
			bankReceiptReqDto.setState(BaseConsts.THREE);
			List<BankReceiptResDto> bankReceiptResDtos = bankReceiptService
					.queryRootListByConPayAdvanceRel(bankReceiptReqDto);
			BigDecimal perSumAmout = BigDecimal.ZERO;
			List<PayAdvanceRelation> payAdvanceRelations = Lists.newArrayList();
			List<BankReceipt> bankReceipts = new ArrayList<BankReceipt>();
			if (CollectionUtils.isNotEmpty(bankReceiptResDtos)) {
				BigDecimal perRecAmount = oldPo.getPerRecAmount();
				// 项目和客户对应多个预付款，所有预付金额总和大于订单预付款金额，匹配预付款，则成功，否则失败
				for (BankReceiptResDto bankReceiptResDto : bankReceiptResDtos) {
					bankReceiptService.queryEntityById(bankReceiptResDto.getId());
					if (DecimalUtil.eq(bankReceiptResDto.getPayableAmount(), DecimalUtil.ZERO)) {
						continue;
					}
					// 剩余的预付款
					perSumAmout = DecimalUtil.add(perSumAmout, bankReceiptResDto.getPayableAmount());// 所有预付款总额
					PayAdvanceRelation payAdvanceRelation = new PayAdvanceRelation();// 插入水单与付款关系
					payAdvanceRelation.setPayId(payId);
					payAdvanceRelation.setAdvanceId(bankReceiptResDto.getId());
					BankReceipt bankReceipt = new BankReceipt();
					bankReceipt.setId(bankReceiptResDto.getId());
					if (DecimalUtil.le(perRecAmount, bankReceiptResDto.getPayableAmount())) {// 预付金大于订单预付金，匹配上，则直接更新，退出
						payAdvanceRelation.setPayAmount(perRecAmount);
						payAdvanceRelations.add(payAdvanceRelation);

						bankReceipt.setPaidAmount(DecimalUtil.add(bankReceiptResDto.getPaidAmount(), perRecAmount));
						bankReceipts.add(bankReceipt);
						break;
					} else {
						perRecAmount = perRecAmount.subtract(bankReceiptResDto.getPayableAmount());// 剩余的
						payAdvanceRelation.setPayAmount(bankReceiptResDto.getPayableAmount());
						payAdvanceRelations.add(payAdvanceRelation);

						bankReceipt.setPaidAmount(DecimalUtil.add(bankReceiptResDto.getPaidAmount(),
								bankReceiptResDto.getPayableAmount()));
						bankReceipts.add(bankReceipt);
					}
				}
			}
			boolean isAmoutOk = DecimalUtil.le(oldPo.getPerRecAmount(), perSumAmout);
			if (!isAmoutOk) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "预收定金不足，不能提交");
			} else {
				for (PayAdvanceRelation payAdvanceRelation : payAdvanceRelations) {
					payAdvanceRelation.setCreateAt(new Date());
					if (StringUtils.isEmpty(payAdvanceRelation.getCreator())) {
						payAdvanceRelation.setCreator(ServiceSupport.getUser().getChineseName());
					}
					if (StringUtils.isEmpty(payAdvanceRelation.getCreatorId())) {
						payAdvanceRelation.setCreatorId(ServiceSupport.getUser().getId());
					}
					payAdvanceRelationService.addPayAdvanceRelation(payAdvanceRelation);
				}
				for (BankReceipt bankReceipt : bankReceipts) {
					bankReceiptService.updateBankReceiptById(bankReceipt);
				}
			}
		}
		return payId;
	}

	/**
	 * 是否质押项目
	 * 
	 * @param bizType
	 *            项目业务类型
	 * @return
	 */
	public boolean isPledgeProject(int bizType) {
		List<String> paramValueList = sysParamService.queryParamValueListByParamKey(SysParamConsts.PLEDGE_PROJECT_TYPE);
		for (String paramValue : paramValueList) {
			if (Integer.parseInt(paramValue) == bizType) {
				return true;
			}
		}
		return false;
	}

	public BigDecimal calcPayAmount(PoLineModel item, Integer payWayType, Integer bizType) {
		return calcPayAmount(item, payWayType, bizType, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
	}

	/**
	 * 计算付款金额
	 * 
	 * @param item
	 *            采购明细
	 * @param payWayType
	 *            付款支付类型
	 * @param bizType
	 *            项目业务类型
	 * @param poPayAmount
	 *            付款采购关系明细的付款金额(添加抵扣费用时使用)，没有则传0
	 * @param totalPayAmount
	 *            总的付款金额(添加抵扣费用时使用)，没有则传0
	 * @param deductionFeeAmount
	 *            抵扣费用金额(添加抵扣费用时使用)，没有则传0
	 * @return 付款金额
	 */
	public BigDecimal calcPayAmount(PoLineModel item, Integer payWayType, Integer bizType, BigDecimal poPayAmount,
			BigDecimal totalPayAmount, BigDecimal deductionFeeAmount) {
		BigDecimal deductionMoney = item.getDeductionMoney();
		BigDecimal payAmount = BigDecimal.ZERO;
		if (payWayType.equals(BaseConsts.TWO)) { // 2-尾款
			payAmount = BigDecimal.ZERO;
			BigDecimal prePayAmount = DecimalUtil
					.formatScale2(DecimalUtil.multiply(item.getPayPrice(), item.getGoodsNum()));
			BigDecimal deductionAmount = BigDecimal.ZERO;
			/**
			 * if (isPledgeProject(bizType)) { //是否质押项目 if
			 * (!DecimalUtil.eq(item.getPledgeProportion(), BigDecimal.ZERO) &&
			 * !DecimalUtil.eq(item.getPayPrice(), BigDecimal.ZERO)) {
			 * //预付款的抵扣费用金额 deductionAmount =
			 * DecimalUtil.subtract(item.getGoodsAmount(),
			 * DecimalUtil.divide(DecimalUtil.multiply(item.getPayPrice(),
			 * item.getGoodsNum()), item.getPledgeProportion())); } } else {
			 * deductionAmount = deductionMoney; //采购订单的抵扣金额 }
			 **/
			deductionAmount = deductionMoney; // 采购订单的抵扣金额
			/**
			 * 付款金额=订单金额-折扣金额-预付金额(付款单价*数量)-计算出的抵扣金额(非质押项目取采购订单的抵扣金额；质押项目：订单金额-(
			 * (付款单价*数量)/质押比例))-抵扣费用金额
			 **/
			// 付款金额=订单金额-折扣金额-预付金额(付款单价*数量)-采购订单的抵扣金额
			payAmount = item.getGoodsAmount().subtract(item.getDiscountAmount()).subtract(prePayAmount)
					.subtract(deductionAmount);
		} else {
			// 付款金额=采购订单金额-折扣金额-采购订单的抵扣金额
			payAmount = item.getGoodsAmount().subtract(item.getDiscountAmount()).subtract(deductionMoney);
			if (payWayType.equals(BaseConsts.ONE) && isPledgeProject(bizType)) { // 1-预付款+是否质押项目
				// 质押项目的付款金额需乘以质押比例
				if (DecimalUtil.ne(poPayAmount, BigDecimal.ZERO) && DecimalUtil.ne(totalPayAmount, BigDecimal.ZERO)
						&& DecimalUtil.ne(deductionFeeAmount, BigDecimal.ZERO)) {
					// 付款金额=(采购单的付款金额-已有付款明细的付款金额/已有付款单的付款总金额*抵扣费用金额)*质押比例
					payAmount = DecimalUtil.multiply(payAmount.subtract(
							DecimalUtil.multiply(DecimalUtil.divide(poPayAmount, totalPayAmount), deductionFeeAmount)),
							item.getPledgeProportion());
				} else {
					payAmount = DecimalUtil.multiply(payAmount, item.getPledgeProportion());
				}
			}
		}
		return payAmount;
	}

	private void validAndUpdate(PurchaseOrderTitle oldPo) {
		BaseProject baseProject = cacheService.getProjectById(oldPo.getProjectId());
		Integer bizType = BaseConsts.ZERO;
		if (baseProject.getBizType().equals(BaseConsts.SEVEN)) {// 质押项目,不提交付款
			bizType = BaseConsts.ONE;
			addPayOrderByPo(oldPo, bizType);
		} else {
			Integer payId = addPayOrderByPo(oldPo, bizType);
			PayOrder entity = new PayOrder();
			entity.setId(payId);
			payService.submitPayOrderById(entity, false);
		}
	}

	private PayOrder createPayOrder(PurchaseOrderTitle po) {
		PayOrder payOrder = new PayOrder();
		payOrder.setPayType(BaseConsts.ONE);
		payOrder.setState(BaseConsts.ZERO);
		payOrder.setProjectId(po.getProjectId());
		payOrder.setBusiUnit(po.getBusinessUnitId());
		payOrder.setPayer(po.getBusinessUnitId());
		payOrder.setPayWay(po.getPayWay());
		payOrder.setPayee(po.getSupplierId());
		payOrder.setPayAccountId(po.getAccountId());
		payOrder.setRequestPayTime(po.getRequestPayTime());
		payOrder.setCurrnecyType(po.getCurrencyId());
		payOrder.setOpenType(po.getOpenType());
		payOrder.setPayAmount(BigDecimal.ZERO); // 在添加明细时更新该字段
		payOrder.setPoBlance(po.getOrderTotalAmount());
		payOrder.setRemark(po.getRemark());
		payOrder.setAdvanceAmount(po.getPerRecAmount());
		payOrder.setCreator(po.getCreator());
		payOrder.setCreatorId(po.getCreatorId());
		payOrder.setAttachedNumbe(po.getAppendNo());
		return payOrder;
	}

	public PurchaseOrderTitle queryAndLockById(Integer poId) {
		PurchaseOrderTitle po = purchaseOrderTitleDao.queryAndLockById(poId);
		if (po == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, purchaseOrderTitleDao.getClass(), poId);
		}
		return po;
	}

	/**
	 * 根据采购单ID更新采购单总额和数量*
	 *
	 * @param poId
	 */
	public void updatePoTotalNum(Integer poId) {
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderLineDao.queryTotalByPoId(poId);
		if (purchaseOrderTitle == null) {// 明细全部删除后，更新订单金额和数量为0
			purchaseOrderTitle = new PurchaseOrderTitle();
			purchaseOrderTitle.setOrderTotalAmount(BigDecimal.ZERO);
			purchaseOrderTitle.setOrderTotalNum(BigDecimal.ZERO);
			purchaseOrderTitle.setTotalDiscountAmount(BigDecimal.ZERO);
		} else {
			if (purchaseOrderTitle.getOrderTotalAmount() == null) {
				purchaseOrderTitle.setOrderTotalAmount(BigDecimal.ZERO);
			}
			if (purchaseOrderTitle.getOrderTotalNum() == null) {
				purchaseOrderTitle.setOrderTotalNum(BigDecimal.ZERO);
			}
			if (purchaseOrderTitle.getTotalDiscountAmount() == null) {
				purchaseOrderTitle.setTotalDiscountAmount(BigDecimal.ZERO);
			}
		}
		purchaseOrderTitle.setId(poId);
		int num = purchaseOrderTitleDao.updatePurchaseOrderTitleById(purchaseOrderTitle);
		if (num != 1) {
			throw new BaseException(ExcMsgEnum.ENTITY_UPDATE_NOT_EXSIT, purchaseOrderTitle.getId());
		}
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

	public PageResult<PoLineModel> queryDistributionLinesByPoTitleId(PoTitleReqDto purchaseOrderTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		int offSet = PageUtil.getOffSet(purchaseOrderTitleReqDto.getPage(), purchaseOrderTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, purchaseOrderTitleReqDto.getPer_page());
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByPoId(purchaseOrderTitleReqDto.getId(),
				rowBounds);
		processPoLineModels(poLineList);
		if (CollectionUtils.isNotEmpty(poLineList)) {
			result.setTotalStr(
					"合计：退货总金额：" + DecimalUtil.toAmountString(poLineList.get(0).getOrderTotalAmount()) + "  &nbsp;"
							+ ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
									poLineList.get(0).getCurrencyId().toString())
							+ "  &nbsp;" + "&nbsp;预退款总金额："
							+ DecimalUtil.toAmountString(poLineList.get(0).getRefundAmount()) + "  &nbsp;"
							+ ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
									poLineList.get(0).getCurrencyId().toString())
							+ "  &nbsp;" + "&nbsp;退货总数量："
							+ DecimalUtil.toQuantityString(poLineList.get(0).getOrderTotalNum()) + "  &nbsp;"
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

	/**
	 * 结算单明细的查询
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public PageResult<PoLineModel> queryDistributionTotalLinesByPoTitleId(PoTitleReqDto purchaseOrderTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		int offSet = PageUtil.getOffSet(purchaseOrderTitleReqDto.getPage(), purchaseOrderTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, purchaseOrderTitleReqDto.getPer_page());
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByPoId(purchaseOrderTitleReqDto.getId(),
				rowBounds);
		processPoLineModels(poLineList);
		if (CollectionUtils.isNotEmpty(poLineList)) {
			result.setTotalStr(
					"合计：结算总金额：" + DecimalUtil.toAmountString(poLineList.get(0).getOrderTotalAmount()) + "  &nbsp;"
							+ ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
									poLineList.get(0).getCurrencyId().toString())
							+ "  &nbsp;" + "&nbsp;结算总数量："
							+ DecimalUtil.toQuantityString(poLineList.get(0).getOrderTotalNum()) + "  &nbsp;"
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

	/**
	 * 获取采购信息
	 *
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public PageResult<PoLineModel> queryPoLinesByPoReqDto(PoTitleReqDto purchaseOrderTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		int offSet = PageUtil.getOffSet(purchaseOrderTitleReqDto.getPage(), purchaseOrderTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, purchaseOrderTitleReqDto.getPer_page());
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByCon(purchaseOrderTitleReqDto, rowBounds);
		processPoLineModels(poLineList);
		result.setItems(poLineList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), purchaseOrderTitleReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(purchaseOrderTitleReqDto.getPage());
		result.setPer_page(purchaseOrderTitleReqDto.getPer_page());
		return result;
	}

	/**
	 * 添加采购单行信息
	 *
	 * @param poLineReqDto
	 */
	public void addPoLines(PoLineReqDto poLineReqDto) {
		List<PurchaseOrderLine> purchaseOrderLineList = poLineReqDto.getPoLines();
		if (CollectionUtils.isNotEmpty(purchaseOrderLineList)) {
			Integer poId = poLineReqDto.getId();
			PurchaseOrderTitle purchaseOrderTitle = queryAndLockById(poId);
			if (purchaseOrderTitle.getState() != BaseConsts.ONE) {// 校验采购单状态，已经提交
				throw new BaseException(ExcMsgEnum.PO_NOT_ADD_LINE);
			}
			int i = 1;
			for (PurchaseOrderLine poLine : purchaseOrderLineList) {
				BaseGoods baseGoods = cacheService.getGoodsById(poLine.getGoodsId());
				if (poLine.getGoodsId() == null || baseGoods == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品编号:" + poLine.getGoodsNo() + "不存在，不能添加");
				}

				poLine.setPoId(poId);
				poLine.setAmount(DecimalUtil.formatScale2(poLine.getGoodsNum().multiply(poLine.getGoodsPrice())));// 数量*单价
				if (poLine.getCostPrice() == null) {
					poLine.setCostPrice(poLine.getGoodsPrice());
				}
				if (null == poLine.getDiscountAmount()) {
					poLine.setDiscountAmount(DecimalUtil.ZERO);
				}
				if (poLine.getAmount().compareTo(poLine.getDiscountAmount()) < 0) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "第" + i + "行的折扣金额大于订单金额！");
				}
				poLine.setDiscountPrice(DecimalUtil.subtract(poLine.getGoodsPrice(),
						DecimalUtil.format(DecimalUtil.divide(poLine.getDiscountAmount(), poLine.getGoodsNum()))));
				poLine.setInvoiceAmount(BigDecimal.ZERO);
				poLine.setInvoiceNum(BigDecimal.ZERO);
				poLine.setCreateAt(new Date());
				poLine.setCreator(ServiceSupport.getUser().getChineseName());
				poLine.setCreatorId(ServiceSupport.getUser().getId());
				poLine.setIsDelete(BaseConsts.ZERO);
				poLine.setPledgeProportion(baseGoods.getPledgeProportion());
				purchaseOrderLineDao.insert(poLine);
				i++;
			}
			updatePoTotalNum(poId);
		}
	}

	/**
	 * 添加采购单单行信息
	 */
	public Integer addPoLine(Integer poId, PurchaseOrderLine poLine) {
		PurchaseOrderTitle purchaseOrderTitle = queryAndLockById(poId);
		poLine.setPoId(poId);
		poLine.setAmount(DecimalUtil.formatScale2(poLine.getGoodsNum().multiply(poLine.getGoodsPrice())));// 数量*单价
		if (purchaseOrderTitle.getState() != BaseConsts.ONE) {// 校验采购单状态，已经提交
			throw new BaseException(ExcMsgEnum.PO_NOT_ADD_LINE);
		}
		if (poLine.getGoodsId() == null || cacheService.getGoodsById(poLine.getGoodsId()) == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品编号:" + poLine.getGoodsNo() + "不存在，不能添加");
		}
		if (poLine.getAmount().compareTo(poLine.getDiscountAmount()) < 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "折扣金额大于订单金额！");
		}
		if (poLine.getCostPrice() == null) {
			poLine.setCostPrice(poLine.getGoodsPrice());
		}
		if (null != poLine.getDeductionMoney() && DecimalUtil.gt(poLine.getDeductionMoney(), BigDecimal.ZERO)) {
			if (null == poLine.getDiscountPrice() || DecimalUtil.eq(poLine.getDiscountPrice(), BigDecimal.ZERO)) {
				poLine.setDiscountPrice(
						DecimalUtil
								.subtract(
										DecimalUtil.subtract(poLine.getGoodsPrice(),
												DecimalUtil.format(DecimalUtil.divide(poLine.getDeductionMoney(),
														poLine.getGoodsNum()))),
										DecimalUtil.divide(poLine.getDiscountAmount(), poLine.getGoodsNum())));
			}
		} else {
			if (null == poLine.getDiscountPrice() || DecimalUtil.eq(poLine.getDiscountPrice(), BigDecimal.ZERO)) {
				poLine.setDiscountPrice(DecimalUtil.subtract(poLine.getGoodsPrice(),
						DecimalUtil.format(DecimalUtil.divide(poLine.getDiscountAmount(), poLine.getGoodsNum()))));
			}
		}
		poLine.setInvoiceAmount(BigDecimal.ZERO);
		poLine.setInvoiceNum(BigDecimal.ZERO);
		poLine.setCreateAt(new Date());
		poLine.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		poLine.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		poLine.setIsDelete(BaseConsts.ZERO);
		purchaseOrderLineDao.insert(poLine);
		updatePoTotalNum(poId);
		return poLine.getId();
	}

	/**
	 * 删除采购单行信息
	 *
	 * @param poLineReqDto
	 */
	public void deletePoLinesById(PoLineReqDto poLineReqDto) {

		if (CollectionUtils.isNotEmpty(poLineReqDto.getIds())) {
			Integer poId = poLineReqDto.getId();
			PurchaseOrderTitle purchaseOrderTitle = queryAndLockById(poId);
			if (purchaseOrderTitle.getState() != BaseConsts.ONE) {// 校验采购单状态，已经提交
				throw new BaseException(ExcMsgEnum.PO_NOT_DELETE_LINE);
			}
			for (Integer id : poLineReqDto.getIds()) {
				PurchaseOrderLine poLine = new PurchaseOrderLine();
				poLine.setId(id);
				poLine.setIsDelete(BaseConsts.ONE);
				poLine.setDeleteAt(new Date());
				poLine.setDeleter(ServiceSupport.getUser().getChineseName());
				poLine.setDeleterId(ServiceSupport.getUser().getId());
				purchaseOrderLineDao.updatePurchaseOrderLineById(poLine);
			}
			updatePoTotalNum(poId);
		}
	}

	/**
	 * 删除采购退货单行信息
	 *
	 * @param poLineReqDto
	 */
	public void deletePoReturnLinesById(PoLineReqDto poLineReqDto) {
		if (CollectionUtils.isNotEmpty(poLineReqDto.getIds())) {
			Integer poId = poLineReqDto.getId();
			PurchaseOrderTitle purchaseOrderTitle = queryAndLockById(poId);
			if (purchaseOrderTitle.getState() != BaseConsts.ONE) {// 校验采购单状态，已经提交
				throw new BaseException(ExcMsgEnum.PO_NOT_DELETE_LINE);
			}
			for (Integer id : poLineReqDto.getIds()) {
				PurchaseOrderLine poLine = new PurchaseOrderLine();
				PurchaseReturnDtl purchaseReturnDtl = purchaseOrderLineDao.queryAndLockReturnById(id);
				poLine.setId(id);
				poLine.setIsDelete(BaseConsts.ONE);
				poLine.setDeleteAt(new Date());
				poLine.setDeleter(ServiceSupport.getUser().getChineseName());
				poLine.setDeleterId(ServiceSupport.getUser().getId());
				purchaseOrderLineDao.updatePurchaseOrderLineById(poLine);
				// 释放库存
				stlService.releaseStlSaleNum(purchaseReturnDtl.getStlId(), purchaseReturnDtl.getReturnNum().abs());
			}
			updatePoTotalNum(poId);
		}
	}

	public void processPoLineModels(List<PoLineModel> poLineList) {
		if (CollectionUtils.isNotEmpty(poLineList)) {
			BigDecimal countGoodsAmount = BigDecimal.ZERO;
			for (PoLineModel poLine : poLineList) {
				poLine.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
						poLine.getCurrencyId() + ""));
				PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(poLine.getPoId());
				poLine.setMark(purchaseOrderTitle.getRemark());// 备注
				BaseGoods goods = cacheService.getGoodsById(poLine.getGoodsId());
				poLine.setGoodsNo(goods.getNumber());// 商品编号
				poLine.setGoodsName(goods.getName());
				poLine.setDiscountPrice(poLine.getDiscountPrice());// 商品折扣单价
				poLine.setBarSpecificationName(goods.getName() + goods.getSpecification());
				poLine.setGoodsBarCode(goods.getBarCode());
				poLine.setGoodsType(goods.getType());
				poLine.setSpecification(goods.getSpecification());
				poLine.setGoodsAmount(poLine.getGoodsAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getGoodsAmount()));
				poLine.setArrivalAmount(poLine.getArrivalAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getArrivalAmount()));
				poLine.setDiscountAmount(poLine.getDiscountAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getDiscountAmount()));
				// 用于发票打印的显示
				poLine.setRateDisAmount(
						DecimalUtil.multiply(DecimalUtil.divide(poLine.getDiscountAmount(), poLine.getGoodsNum()),
								poLine.getPayRate() == null ? BigDecimal.ONE : poLine.getPayRate()));
				poLine.setPaidAmount(poLine.getPaidAmount() == null ? DecimalUtil.ZERO
						: DecimalUtil.formatScale2(poLine.getPaidAmount()));
				poLine.setVolume(goods.getVolume());// 体积
				poLine.setGrossWeight(goods.getGrossWeight());// 毛重
				poLine.setNetWeight(goods.getNetWeight());// 净重
				poLine.setDeductionMoney(poLine.getDeductionMoney());// 抵扣后金额
				poLine.setGoodsPrice(null == poLine.getGoodsPrice() ? BigDecimal.ZERO : poLine.getGoodsPrice());// 商品单价
				poLine.setCountGoodsAmount(
						DecimalUtil.formatScale2(DecimalUtil.add(countGoodsAmount, poLine.getGoodsAmount())));
				countGoodsAmount = DecimalUtil.add(countGoodsAmount, poLine.getGoodsAmount());
				poLine.setAfterDiscountAmount(DecimalUtil.formatScale2(
						poLine.getAfterDiscountAmount() == null ? BigDecimal.ZERO : poLine.getAfterDiscountAmount()));// 折扣后金额
				if (null != poLine.getGoodsStatus()) {
					poLine.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
							poLine.getGoodsStatus() + ""));
				}
				// 单据日期
				poLine.setOrderTime(purchaseOrderTitle.getOrderTime());

				BigDecimal deductionPrice = BigDecimal.ZERO;
				if (poLine.getDeductionMoney() != null && poLine.getOrderTotalAmount() != null
						&& poLine.getGoodsNum() != null) {
					if (poLine.getGoodsNum() != BigDecimal.ZERO) {
						deductionPrice = DecimalUtil.divide(
								DecimalUtil.subtract(poLine.getOrderTotalAmount(), poLine.getDeductionMoney()),
								poLine.getGoodsNum());
					}
				}
				poLine.setDeductionPrice(deductionPrice);
				poLine.setAfterDeductionMoney(DecimalUtil.subtract(
						null == poLine.getOrderTotalAmount() ? BigDecimal.ZERO : poLine.getOrderTotalAmount(),
						null == poLine.getDeductionMoney() ? BigDecimal.ZERO : poLine.getDeductionMoney()));
			}
		}
	}

	public List<PoLineModel> editPoLinesByIds(List<Integer> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			List<PoLineModel> poLineModels = purchaseOrderLineDao.queryPoLinesByIds(ids);
			processPoLineModels(poLineModels);
			return poLineModels;
		} else {
			return null;
		}
	}

	/**
	 * 更新采购单行信息
	 *
	 * @param purchaseOrderLine
	 */
	public void updatePoLinesById(PurchaseOrderLine purchaseOrderLine) {
		PurchaseOrderTitle purchaseOrderTitle = queryAndLockById(purchaseOrderLine.getPoId());
		/**
		 * if (purchaseOrderTitle.getState() != BaseConsts.ONE) {// 校验采购单状态，已经提交
		 * throw new BaseException(ExcMsgEnum.PO_NOT_UPDATE_LINE); }
		 **/
		if (purchaseOrderLine.getGoodsNum() != null && purchaseOrderLine.getGoodsPrice() != null) {
			purchaseOrderLine.setAmount(purchaseOrderLine.getGoodsNum().multiply(purchaseOrderLine.getGoodsPrice()));
		}
		if (purchaseOrderLine.getAmount() != null && purchaseOrderLine.getDiscountAmount() != null) {
			if (purchaseOrderLine.getAmount().compareTo(purchaseOrderLine.getDiscountAmount()) < 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "折扣金额大于订单金额！");
			}
			purchaseOrderLine.setDiscountPrice(
					DecimalUtil.subtract(purchaseOrderLine.getGoodsPrice(), DecimalUtil.format(DecimalUtil
							.divide(purchaseOrderLine.getDiscountAmount(), purchaseOrderLine.getGoodsNum()))));
		}
		updatePoLineById(purchaseOrderLine);
		updatePoTotalNum(purchaseOrderTitle.getId());// 更新订单金额和数量
	}

	/**
	 * 更新采购单行信息
	 *
	 * @param purchaseOrderLine
	 */
	private void updatePoLineById(PurchaseOrderLine purchaseOrderLine) {
		int num = purchaseOrderLineDao.updatePurchaseOrderLineById(purchaseOrderLine);
		if (num != 1) {
			throw new BaseException(ExcMsgEnum.ENTITY_UPDATE_NOT_EXSIT, purchaseOrderLine.getId());
		}
	}

	public PurchaseOrderLine queryPoLineEntityById(Integer id) {
		return purchaseOrderLineDao.queryPurchaseOrderLineById(id);
	}

	/**
	 * 导入Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void importExcel(MultipartFile importFile) {
		List<PurchaseOrderExcel> poList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("poList", poList);
		ExcelService.resolverExcel(importFile, "/excel/po/po.xml", beans);
		poList = (List<PurchaseOrderExcel>) beans.get("poList");
		// 业务逻辑处理poList
		if (CollectionUtils.isNotEmpty(poList)) {
			if (poList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			savePurchaseOrders(poList);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入采购单为空");
		}
	}

	/**
	 * 导入订单明细Excel 业务逻辑处理
	 *
	 * @param importFile
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void importPoLineExcel(MultipartFile importFile, Integer poId) {
		List<PurchaseOrderLineExcel> poLineList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("poLineList", poLineList);
		ExcelService.resolverExcel(importFile, "/excel/po/poLine.xml", beans);
		poLineList = (List<PurchaseOrderLineExcel>) beans.get("poLineList");
		// 业务逻辑处理poList
		if (CollectionUtils.isNotEmpty(poLineList)) {
			if (poLineList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			savePurchaseOrderLines(poLineList, poId);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入采购单明细为空");
		}
	}

	private void savePurchaseOrderLines(List<PurchaseOrderLineExcel> poLineExcelList, Integer poId) {
		PurchaseOrderTitle purchaseOrderTitle = queryAndLockById(poId);
		List<PurchaseOrderLine> purchaseOrderLines = convertToLines(purchaseOrderTitle.getProjectId(), poLineExcelList);
		PoLineReqDto poLineReqDto = new PoLineReqDto();
		poLineReqDto.setId(poId);
		poLineReqDto.setPoLines(purchaseOrderLines);
		addPoLines(poLineReqDto);
	}

	private void savePurchaseOrders(List<PurchaseOrderExcel> poList) {
		Map<PurchaseOrderTitleExcel, List<PurchaseOrderLineExcel>> poMap = new HashMap<PurchaseOrderTitleExcel, List<PurchaseOrderLineExcel>>();
		for (PurchaseOrderExcel poItem : poList) {
			PurchaseOrderTitleExcel purchaseOrderTitleExcel = convertToTitleExcel(poItem);
			PurchaseOrderLineExcel purchaseOrderLineExcel = convertToLineExcel(poItem);
			List<PurchaseOrderLineExcel> polExcels = new ArrayList<PurchaseOrderLineExcel>();
			System.out.println(purchaseOrderTitleExcel.toString());
			if (poMap.containsKey(purchaseOrderTitleExcel)) {
				polExcels = poMap.get(purchaseOrderTitleExcel);
			}
			polExcels.add(purchaseOrderLineExcel);
			poMap.put(purchaseOrderTitleExcel, polExcels);
		}
		for (PurchaseOrderTitleExcel key : poMap.keySet()) {
			PurchaseOrderTitle poTitle = convertToTitle(key);
			Integer poId = addPurchaseOrderTitle(poTitle);
			List<PurchaseOrderLineExcel> pOrderLineExcels = poMap.get(key);
			if (CollectionUtils.isEmpty(pOrderLineExcels)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单附属编号" + poTitle.getAppendNo() + "订单明细为空，导入失败");
			}
			List<PurchaseOrderLine> purchaseOrderLines = convertToLines(key.getProjectId(), pOrderLineExcels);
			PoLineReqDto poLineReqDto = new PoLineReqDto();
			poLineReqDto.setId(poId);
			poLineReqDto.setPoLines(purchaseOrderLines);
			addPoLines(poLineReqDto);
		}
	}

	private PurchaseOrderTitleExcel convertToTitleExcel(PurchaseOrderExcel purchaseOrderExcel) {
		PurchaseOrderTitleExcel purchaseOrderTitleExcel = new PurchaseOrderTitleExcel();
		if (StringUtils.isEmpty(purchaseOrderExcel.getAppendNo())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单附属编号不能为空");
		}
		if (StringUtils.isEmpty(purchaseOrderExcel.getCurrencyName())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种不能为空");
		}
		if (StringUtils.isEmpty(purchaseOrderExcel.getProjectNo())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目编号不能为空");
		}
		if (StringUtils.isEmpty(purchaseOrderExcel.getSupplierNo())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商编号不能为空");
		}
		if (StringUtils.isEmpty(purchaseOrderExcel.getWarehouseNo())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "仓库编号不能为空");
		}
		if (StringUtils.isEmpty(purchaseOrderExcel.getOrderTime())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单日期不能为空");
		}
		if (StringUtils.isEmpty(purchaseOrderExcel.getPerdictTime())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "预计到货不能为空");
		}
		purchaseOrderTitleExcel.setAppendNo(purchaseOrderExcel.getAppendNo());
		String currencyId = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				purchaseOrderExcel.getCurrencyName());
		if (StringUtils.isEmpty(currencyId)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "币种有误");
		}
		purchaseOrderTitleExcel.setCurrencyId(Integer.parseInt(currencyId));
		BaseProject baseProject = cacheService.getProjectByPno(purchaseOrderExcel.getProjectNo());
		if (baseProject == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目编号" + purchaseOrderExcel.getProjectNo() + "不存在");
		}
		purchaseOrderTitleExcel.setProjectId(baseProject.getId());
		BaseSubject supplier = cacheService.getSupplierByPidAndNo(baseProject.getId(),
				purchaseOrderExcel.getSupplierNo());
		if (supplier == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"项目" + purchaseOrderExcel.getProjectNo() + "下供应商编号" + purchaseOrderExcel.getSupplierNo() + "不存在");
		}
		purchaseOrderTitleExcel.setSupplierId(supplier.getId());
		BaseSubject warehouse = cacheService.getWarehouseByPidAndNo(baseProject.getId(),
				purchaseOrderExcel.getWarehouseNo());
		if (warehouse == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"项目" + purchaseOrderExcel.getProjectNo() + "下仓库编号" + purchaseOrderExcel.getWarehouseNo() + "不存在");
		}
		if (!StringUtils.isEmpty(purchaseOrderExcel.getCustomerNo())) {
			BaseSubject cust = cacheService.getCustomerByPidAndNo(baseProject.getId(),
					purchaseOrderExcel.getCustomerNo());
			if (cust == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目" + purchaseOrderExcel.getProjectNo() + "下客户编号"
						+ purchaseOrderExcel.getCustomerNo() + "不存在");
			}
			purchaseOrderTitleExcel.setCustomerId(cust.getId());
		}
		purchaseOrderTitleExcel.setWarehouseId(warehouse.getId());
		purchaseOrderTitleExcel.setOrderTime(purchaseOrderExcel.getOrderTime());
		purchaseOrderTitleExcel.setPerdictTime(purchaseOrderExcel.getPerdictTime());
		purchaseOrderTitleExcel.setRemark(purchaseOrderExcel.getRemark());
		return purchaseOrderTitleExcel;
	}

	private PurchaseOrderLineExcel convertToLineExcel(PurchaseOrderExcel purchaseOrderExcel) {
		if (StringUtils.isEmpty(purchaseOrderExcel.getGoodsNo())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品编码不能为空");
		}
		if (StringUtils.isEmpty(purchaseOrderExcel.getGoodsNum())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单数量不能为空");
		}
		if (StringUtils.isEmpty(purchaseOrderExcel.getGoodsPrice())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单单价不能为空");
		}
		PurchaseOrderLineExcel purchaseOrderLineExcel = new PurchaseOrderLineExcel();
		purchaseOrderLineExcel.setBatchNum(purchaseOrderExcel.getBatchNum());
		purchaseOrderLineExcel.setGoodsNo(purchaseOrderExcel.getGoodsNo());
		purchaseOrderLineExcel.setGoodsNum(purchaseOrderExcel.getGoodsNum());
		purchaseOrderLineExcel.setGoodsPrice(purchaseOrderExcel.getGoodsPrice());
		purchaseOrderLineExcel.setDiscountAmount(purchaseOrderExcel.getDiscountAmount());
		return purchaseOrderLineExcel;
	}

	private PurchaseOrderTitle convertToTitle(PurchaseOrderTitleExcel purchaseOrderTitleExcel) {
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		BeanUtils.copyProperties(purchaseOrderTitleExcel, purchaseOrderTitle);
		purchaseOrderTitle.setIsRequestPay(BaseConsts.TWO); // 不需要付款
		List<CodeValue> wareAddrs = commonService.queryAllSelectedByKey(CacheKeyConsts.SUBJECT_ADDRESS,
				purchaseOrderTitle.getWarehouseId() + "");
		if (CollectionUtils.isEmpty(wareAddrs)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
					"根据仓库id[" + purchaseOrderTitle.getWarehouseId() + "]找不到仓库地址");
		}
		purchaseOrderTitle.setWareAddrId(Integer.parseInt(wareAddrs.get(0).getCode()));
		return purchaseOrderTitle;
	}

	private List<PurchaseOrderLine> convertToLines(Integer projectId,
			List<PurchaseOrderLineExcel> purchaseOrderLineExcels) {
		List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<PurchaseOrderLine>();
		for (PurchaseOrderLineExcel purchaseOrderLineExcel : purchaseOrderLineExcels) {
			PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
			BeanUtils.copyProperties(purchaseOrderLineExcel, purchaseOrderLine);
			BaseGoods baseGoods = cacheService.getGoodsByPidAndNo(projectId, purchaseOrderLineExcel.getGoodsNo());
			if (baseGoods == null) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "商品出错");
			}
			// 根据projectId和goodsNo查找商品
			purchaseOrderLine.setGoodsId(baseGoods.getId());
			purchaseOrderLine.setGoodsPrice(new BigDecimal(purchaseOrderLineExcel.getGoodsPrice()));
			purchaseOrderLine.setGoodsNum(new BigDecimal(purchaseOrderLineExcel.getGoodsNum()));
			purchaseOrderLine.setDiscountAmount(new BigDecimal(purchaseOrderLineExcel.getDiscountAmount()));
			purchaseOrderLines.add(purchaseOrderLine);
		}
		return purchaseOrderLines;
	}

	public PoTitleRespDto purchaseOrderTitleConvertToRes(PurchaseOrderTitle purchaseOrderTitle) {
		if (purchaseOrderTitle == null) {
			return null;
		}
		PoTitleRespDto poRespDto = new PoTitleRespDto();
		poRespDto.setAppendNo(purchaseOrderTitle.getAppendNo());
		poRespDto.setOrderNo(purchaseOrderTitle.getOrderNo());// 订单编号
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
		poRespDto.setStartDate(cacheService.getProjectItemByPid(purchaseOrderTitle.getProjectId()).getStartDate());
		BaseProject project = cacheService.getProjectById(purchaseOrderTitle.getProjectId());
		if (project != null) {
			poRespDto.setBizType(project.getBizType());
		}
		poRespDto
				.setBusinessUnitNameValue(cacheService.getBusiUnitById(poRespDto.getBusinessUnitId()).getChineseName());
		poRespDto
				.setBusinessUnitAddress(cacheService.getBusiUnitById(poRespDto.getBusinessUnitId()).getOfficeAddress());
		poRespDto.setSystemTime(new Date());
		// 经营单位
		poRespDto.setBusinessUnitName(cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getBusinessUnitId(),
				CacheKeyConsts.BUSI_UNIT));
		BaseSubject subject = cacheService.getSubjectById(purchaseOrderTitle.getBusinessUnitId(),
				CacheKeyConsts.BUSI_UNIT);
		poRespDto.setBusinessEnglishName(subject.getEnglishName());// 经营单位英文名称
		poRespDto.setBusinessChineseName(subject.getChineseName());// 经营单位中文名称
		// 经营单位注册号码
		poRespDto.setRegNo(subject.getRegNo());// 经营单位中文名称

		poRespDto.setBusinessUnitId(purchaseOrderTitle.getBusinessUnitId());
		poRespDto.setPhone(subject.getRegPhone()); // 电话号码
		// 供应商
		poRespDto.setSupplierName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getSupplierId(), CacheKeyConsts.SUPPLIER));
		BaseSubject baseSubject = cacheService.getSubjectById(purchaseOrderTitle.getSupplierId(),
				CacheKeyConsts.SUPPLIER);
		poRespDto.setSupplierEnglishName(baseSubject.getEnglishName());
		poRespDto.setSupplierChineseName(baseSubject.getChineseName());// 供应商中文名称
		poRespDto.setSupplierAddress(baseSubject.getOfficeAddress());// 供应商地址
		poRespDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
		queryAccountReqDto.setId(purchaseOrderTitle.getSupplierId());
		List<BaseAccount> baseAccountList = accountDao.queryAccountBySubjectId(queryAccountReqDto);// 获取客户帐户信息
		if (CollectionUtils.isNotEmpty(baseAccountList)) {
			poRespDto.setBankNamePay(baseAccountList.get(BaseConsts.ZERO).getBankName());
			poRespDto.setAccountName(baseAccountList.get(BaseConsts.ZERO).getAccountor());
			poRespDto.setAccountNoPay(baseAccountList.get(BaseConsts.ZERO).getAccountNo());
			poRespDto.setBankCode(baseAccountList.get(BaseConsts.ZERO).getBankCode());
		}
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
		BaseAccount account = cacheService.getAccountById(purchaseOrderTitle.getAccountId());
		if (account != null) {
			poRespDto.setAccountNo(account.getAccountNo());
			poRespDto.setBankName(account.getBankName());
		}
		poRespDto.setCurrency(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
				purchaseOrderTitle.getCurrencyId() + ""));
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
		poRespDto.setOrderTime(purchaseOrderTitle.getOrderTime());// 订单日期
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
		poRespDto.setCreateAt(purchaseOrderTitle.getCreateAt());
		poRespDto.setCreateUser(purchaseOrderTitle.getCreator());
		poRespDto.setDuctionMoney(purchaseOrderTitle.getDuctionMoney());
		poRespDto.setTotalRefundAmount(purchaseOrderTitle.getTotalRefundAmount());
		poRespDto.setTotalOccupyServiceAmount(DecimalUtil.subtract(
				null == purchaseOrderTitle.getTotalRefundAmount() ? BigDecimal.ZERO
						: purchaseOrderTitle.getTotalRefundAmount(),
				null == purchaseOrderTitle.getOrderTotalAmount() ? BigDecimal.ZERO
						: purchaseOrderTitle.getOrderTotalAmount()));
		poRespDto.setCnyRate(purchaseOrderTitle.getCnyRate());
		poRespDto.setPoAmount(purchaseOrderTitle.getPoAmount());
		poRespDto.setTotalRemainSendNum(purchaseOrderTitle.getTotalRemainSendNum());
		poRespDto.setFlyOrderFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.YES_NO,
				purchaseOrderTitle.getFlyOrderFlag()+ ""));
		return poRespDto;
	}

	/**
	 * 查询附件列表
	 */
	public PageResult<PoFileAttachRespDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<PoFileAttachRespDto> pageResult = new PageResult<PoFileAttachRespDto>();
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<PoFileAttachRespDto> list = convertToResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	private List<PoFileAttachRespDto> convertToResDto(List<FileAttach> fileAttach) {
		List<PoFileAttachRespDto> list = new LinkedList<PoFileAttachRespDto>();
		for (int i = 0; i < fileAttach.size(); i++) {
			PoFileAttachRespDto poFileAttachRespDto = new PoFileAttachRespDto();
			poFileAttachRespDto.setId(fileAttach.get(i).getId());
			poFileAttachRespDto.setBusId(fileAttach.get(i).getBusId());
			poFileAttachRespDto.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, fileAttach.get(i).getBusType() + ""));
			poFileAttachRespDto.setName(fileAttach.get(i).getName());
			poFileAttachRespDto.setType(fileAttach.get(i).getType());
			poFileAttachRespDto.setCreateAt(fileAttach.get(i).getCreateAt());
			poFileAttachRespDto.setCreator(fileAttach.get(i).getCreator());
			List<CodeValue> operList = getFileOperList();
			poFileAttachRespDto.setOpertaList(operList);
			list.add(poFileAttachRespDto);
		}
		return list;
	}

	/**
	 * 获取文件操作列表
	 *
	 * @return
	 */
	private List<CodeValue> getFileOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getFileOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				ProjectItemFileAttach.Operate.operMap);
		return oprResult;
	}

	/**
	 * 获取文件操作列表
	 *
	 * @return
	 */
	private List<String> getFileOperListByState() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DOWNLOAD);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}

	/**
	 * 采购单收货
	 *
	 * @param purchaseOrderTitle
	 */
	public String receivePurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		PurchaseOrderTitle po = queryAndLockById(purchaseOrderTitle.getId());
		if (!po.getState().equals(BaseConsts.FIVE)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "已完成采购订单才能收货！");
		}
		BillInStore billInStore = billInStoreService.autoReceive(po);
		return billInStore.getBillNo();
	}
	
	/**
	 * 采购单飞单
	 *
	 * @param purchaseOrderTitle
	 */
	public String flyOrderPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		PurchaseOrderTitle po = queryAndLockById(purchaseOrderTitle.getId());
		if (po.getState() != BaseConsts.FIVE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "已完成采购订单才能飞单！");
		}
		if (po.getFlyOrderFlag() == BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购订单已飞单，无法再操作！");
		}
		//检查采购单是否已全部付款
		boolean isFinished = payService.isFinishedPay(po);
		if (isFinished == false) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购订单未完成付款");
		}
		PurchaseOrderTitle updatePurchaseOrderTitle = new PurchaseOrderTitle();
		updatePurchaseOrderTitle.setId(po.getId());
		updatePurchaseOrderTitle.setFlyOrderFlag(BaseConsts.ONE);	//标识飞单
		purchaseOrderTitleDao.updatePurchaseOrderTitleById(updatePurchaseOrderTitle);
		//自动入库、销售、发货
		//TODO 收货日期、发货日期取采购订单的订单日期
		AutoProcessPoDto autoProcessPoDto = this.autoProcessPo(po, po.getOrderTime(), BaseConsts.ONE);
		return autoProcessPoDto.getBillInStoreNo();
	}
	
	public AutoProcessPoDto autoProcessPo(PurchaseOrderTitle purchaseOrderTitle, Date businessDate) {
		return this.autoProcessPo(purchaseOrderTitle, businessDate, BaseConsts.ZERO);
	}
	
	/**
	 * 自动入库、销售、发货
	 */
	public AutoProcessPoDto autoProcessPo(PurchaseOrderTitle purchaseOrderTitle, Date businessDate, Integer flyOrderFlag) {
		AutoProcessPoDto autoProcessPoDto = new AutoProcessPoDto();
		purchaseOrderTitle.setFlyOrderFlag(flyOrderFlag);
		String billInStoreNo = this.receivePurchaseOrderTitle(purchaseOrderTitle);// 自动收货
		BillInStoreSearchReqDto billInStoreSearchReqDto = new BillInStoreSearchReqDto();
		billInStoreSearchReqDto.setBillNo(billInStoreNo);
		billInStoreSearchReqDto.setProjectId(purchaseOrderTitle.getProjectId());
		billInStoreSearchReqDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		List<BillInStore> billInStores = billInStoreDao.queryResultsByCon(billInStoreSearchReqDto);
		if (CollectionUtils.isEmpty(billInStores)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "生成入库单失败");
		}
		billInStoreService.submitBillInStore(billInStores.get(0), businessDate); // 提交入库单

		List<Stl> stls = stlDao.queryResultsByPoId(purchaseOrderTitle.getId());
		if (CollectionUtils.isEmpty(stls)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "生成库存失败");
		}
		for (Stl stl : stls) {
			stl.setRequiredSendNum(stl.getInStoreNum());
			PoLineModel poLineModel = purchaseOrderLineDao.queryPoLineByPoLineId(stl.getPoDtlId());
			stl.setRequiredSendPrice(DecimalUtil.subtract(poLineModel.getRequiredSendPrice(), DecimalUtil.divide(
					null == poLineModel.getDeductionMoney() ? BigDecimal.ZERO : poLineModel.getDeductionMoney(),
					poLineModel.getGoodsNum())));
		}
		Integer billDeliveryId = billDeliveryService.autoDelivery(purchaseOrderTitle, stls, businessDate); // 生成销售单，待发货
		BillOutStore billOutStore = billOutStoreService.queryValidBillOutStoreByBillDeliveryId(billDeliveryId);
		billOutStore.setDeliverTime(businessDate);
		billOutStoreService.sendBillOutStore(billOutStore); // 出库单送货
		autoProcessPoDto.setBillInStoreNo(billInStoreNo);
		return autoProcessPoDto;
	}

	
	private PurchaseOrderDtlExtResDto purConvertToRes(PurchaseOrderTitle purchaseOrderTitle,
			PurchaseOrderDtlExtResDto purchaseOrderDtlExtResDto) {
		if (purchaseOrderTitle == null) {
			return null;
		}
		purchaseOrderDtlExtResDto.setAppendNo(purchaseOrderTitle.getAppendNo());
		purchaseOrderDtlExtResDto.setOrderNo(purchaseOrderTitle.getOrderNo());
		purchaseOrderDtlExtResDto.setId(purchaseOrderTitle.getId());
		purchaseOrderDtlExtResDto.setcBankWater(purchaseOrderTitle.getcBankWater());
		purchaseOrderDtlExtResDto.setBusinessUnitId(purchaseOrderTitle.getBusinessUnitId());
		purchaseOrderDtlExtResDto.setProjectId(purchaseOrderTitle.getProjectId());
		purchaseOrderDtlExtResDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		purchaseOrderDtlExtResDto.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		// 项目
		purchaseOrderDtlExtResDto.setProjectId(purchaseOrderTitle.getProjectId());
		purchaseOrderDtlExtResDto.setProjectName(cacheService.showProjectNameById(purchaseOrderTitle.getProjectId()));
		// 经营单位
		purchaseOrderDtlExtResDto.setBusinessUnitName(cacheService
				.showSubjectNameByIdAndKey(purchaseOrderTitle.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		purchaseOrderDtlExtResDto.setBusinessUnitId(purchaseOrderTitle.getBusinessUnitId());
		// 供应商
		purchaseOrderDtlExtResDto.setSupplierName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getSupplierId(), CacheKeyConsts.SUPPLIER));
		purchaseOrderDtlExtResDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		// 仓库
		purchaseOrderDtlExtResDto.setWarehouseName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
		purchaseOrderDtlExtResDto.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		// 仓库地址
		BaseAddress address = cacheService.getAddressById(purchaseOrderTitle.getWareAddrId());
		if (address != null) {
			purchaseOrderDtlExtResDto.setWareAddrName(address.getShowValue());
			purchaseOrderDtlExtResDto.setWareAddrId(address.getId());
		}

		// 客户
		purchaseOrderDtlExtResDto.setCustomerName(
				cacheService.showSubjectNameByIdAndKey(purchaseOrderTitle.getCustomerId(), CacheKeyConsts.CUSTOMER));
		purchaseOrderDtlExtResDto.setCustomerId(purchaseOrderTitle.getCustomerId());
		purchaseOrderDtlExtResDto.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				purchaseOrderTitle.getCurrencyId() + ""));
		purchaseOrderDtlExtResDto.setCurrencyId(purchaseOrderTitle.getCurrencyId());
		purchaseOrderDtlExtResDto.setStateName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PO_STS, purchaseOrderTitle.getState() + ""));
		purchaseOrderDtlExtResDto.setStateId(purchaseOrderTitle.getState());
		purchaseOrderDtlExtResDto.setOrderTime(purchaseOrderTitle.getOrderTime());
		purchaseOrderDtlExtResDto.setPerdictTime(purchaseOrderTitle.getPerdictTime());
		purchaseOrderDtlExtResDto.setRequestPayTime(purchaseOrderTitle.getRequestPayTime());
		purchaseOrderDtlExtResDto.setOrderTotalNum(purchaseOrderTitle.getOrderTotalNum());
		purchaseOrderDtlExtResDto.setOrderTotalAmount(purchaseOrderTitle.getOrderTotalAmount());
		purchaseOrderDtlExtResDto.setArrivalAmount(purchaseOrderTitle.getArrivalAmount());
		purchaseOrderDtlExtResDto.setArrivalNum(purchaseOrderTitle.getArrivalNum());
		purchaseOrderDtlExtResDto.setInvoiceTotalNum(purchaseOrderTitle.getInvoiceTotalNum());
		purchaseOrderDtlExtResDto.setInvoiceTotalAmount(purchaseOrderTitle.getInvoiceTotalAmount());
		return purchaseOrderDtlExtResDto;
	}

	public void updatePrintNum(Integer id) {
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(id);
		PurchaseOrderTitle purchaseOrder = new PurchaseOrderTitle();
		purchaseOrder.setId(id);
		purchaseOrder.setPrintNum(purchaseOrderTitle.getPrintNum() + 1);
		purchaseOrderTitleDao.updatePrintNum(purchaseOrder);
	}

	public PageResult<PurchaseReturnDtl> queryBillInStoreLineByPoTitle(PoTitleReqDto poTitleReqDto) {
		PageResult<PurchaseReturnDtl> result = new PageResult<PurchaseReturnDtl>();
		int offSet = PageUtil.getOffSet(poTitleReqDto.getPage(), poTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poTitleReqDto.getPer_page());
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PurchaseReturnDtl> purchaseReturnDtls = purchaseOrderLineDao.queryBillInStoreListDivide(poTitleReqDto,
				rowBounds);
		for (PurchaseReturnDtl purchaseReturnDtl : purchaseReturnDtls) {
			if (purchaseReturnDtl.getGoodsStatus() != null) {
				purchaseReturnDtl
						.setGoodsStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_IN_STORE_GOODS_STATUS,
								Integer.toString(purchaseReturnDtl.getGoodsStatus())));
			}
			purchaseReturnDtl.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					purchaseReturnDtl.getCurrencyType() + ""));
		}
		result.setItems(purchaseReturnDtls);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poTitleReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(poTitleReqDto.getPage());
		result.setPer_page(poTitleReqDto.getPer_page());
		return result;
	}

	public PageResult<PurchaseReturnDtl> queryBillInStoreUndivideByPoTitle(PoTitleReqDto poTitleReqDto) {
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(poTitleReqDto.getId());
		poTitleReqDto.setProjectId(purchaseOrderTitle.getProjectId());
		poTitleReqDto.setCustomerId(purchaseOrderTitle.getCustomerId());
		poTitleReqDto.setWarehouseId(purchaseOrderTitle.getWarehouseId());
		poTitleReqDto.setCurrencyId(purchaseOrderTitle.getCurrencyId());
		poTitleReqDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		PageResult<PurchaseReturnDtl> result = new PageResult<PurchaseReturnDtl>();
		int offSet = PageUtil.getOffSet(poTitleReqDto.getPage(), poTitleReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, poTitleReqDto.getPer_page());
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PurchaseReturnDtl> poTitles = purchaseOrderTitleDao.queryBillInStoreListUndivide(poTitleReqDto, rowBounds);
		for (PurchaseReturnDtl poTitle : poTitles) {
			poTitle.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					poTitle.getCurrencyType() + ""));
			poTitle.setReturnNum(poTitle.getTallyNum());
			poTitle.setReturnPrice(poTitle.getReceivePrice());
		}
		result.setItems(poTitles);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), poTitleReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(poTitleReqDto.getPage());
		result.setPer_page(poTitleReqDto.getPer_page());
		return result;
	}

	/**
	 * 
	 * @param purchaseReturnDtl
	 * @return
	 */
	public void addPoReturnDtlInfo(PoReturnListReqDto poReturnListReqDto) {
		// TODO Auto-generated method stub
		List<PurchaseReturnDtl> purchaseReturnDtlList = poReturnListReqDto.getPurchaseReturnDtl();
		for (PurchaseReturnDtl purchaseReturnDtl : purchaseReturnDtlList) {
			purchaseReturnDtl.setReturnNum(BigDecimal.ZERO.subtract(purchaseReturnDtl.getReturnNum()));
			Stl stl = stlDao.queryAndLockEntityById(purchaseReturnDtl.getId());
			PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
			purchaseOrderLine.setPoId(poReturnListReqDto.getPoId());
			purchaseOrderLine.setGoodsId(stl.getGoodsId());
			purchaseOrderLine.setStlId(stl.getId());
			purchaseOrderLine.setGoodsNum(purchaseReturnDtl.getReturnNum());
			purchaseOrderLine.setGoodsPrice(purchaseReturnDtl.getReturnPrice());
			purchaseOrderLine.setBatchNum(stl.getBatchNo());
			purchaseOrderLine.setPoPrice(stl.getPoPrice());
			purchaseOrderLine.setGoodsStatus(stl.getGoodsStatus());
			purchaseOrderLine.setCostPrice(stl.getCostPrice());
			purchaseOrderLine.setDiscountPrice(stl.getCostPrice());
			purchaseOrderLine.setOriginGoodsPrice(stl.getCostPrice());
			purchaseOrderLine.setAmount(
					DecimalUtil.multiply(purchaseReturnDtl.getReturnNum(), purchaseReturnDtl.getReturnPrice()));
			purchaseOrderLine
					.setDiscountAmount(DecimalUtil.multiply(stl.getCostPrice(), purchaseReturnDtl.getReturnNum()));
			purchaseOrderLine.setInvoiceNum(BigDecimal.ZERO);
			purchaseOrderLine.setInvoiceAmount(BigDecimal.ZERO);
			purchaseOrderLine.setCreateAt(new Date());
			purchaseOrderLine.setCreator(ServiceSupport.getUser().getChineseName());
			purchaseOrderLine.setCreatorId(ServiceSupport.getUser().getId());
			purchaseOrderLine.setIsDelete(BaseConsts.ZERO);
			purchaseOrderLine.setPayTime(stl.getPayTime());
			purchaseOrderLine.setPayPrice(stl.getPayPrice());
			purchaseOrderLine.setPayRate(stl.getPayRate());
			purchaseOrderLine.setPayRealCurrency(stl.getPayRealCurrency());
			purchaseOrderLineDao.insert(purchaseOrderLine);
			stlService.lockStlSaleNum(stl.getId(), purchaseReturnDtl.getReturnNum().abs());
		}
		updatePoTotalNum(poReturnListReqDto.getPoId());
	}

	public Result<PurchaseReturnDtl> queryPurchaseReturnById(Integer id) {
		Result<PurchaseReturnDtl> result = new Result<PurchaseReturnDtl>();
		PurchaseReturnDtl purchaseReturnDtl = purchaseOrderLineDao.queryAndLockReturnById(id);
		if (purchaseReturnDtl.getGoodsStatus() != null) {
			purchaseReturnDtl.setGoodsStatusName(ServiceSupport.getValueByBizCode(
					BizCodeConsts.BILL_IN_STORE_GOODS_STATUS, Integer.toString(purchaseReturnDtl.getGoodsStatus())));
		}
		purchaseReturnDtl.setCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				purchaseReturnDtl.getCurrencyType() + ""));
		result.setItems(purchaseReturnDtl);
		return result;
	}

	/** * 更新采购退货单详细信息 */
	public int updatePurchaseReturnLine(PurchaseReturnDtl purchaseReturnDtl) {
		if (purchaseReturnDtl == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_UPDATE_NOT_EXSIT);
		}
		PurchaseReturnDtl purchaseReturnDtlOld = purchaseOrderLineDao.queryAndLockReturnById(purchaseReturnDtl.getId());
		int num = purchaseOrderLineDao.updatePurchaseReturnLine(purchaseReturnDtl);
		BigDecimal diffNum = purchaseReturnDtlOld.getReturnNum().subtract(purchaseReturnDtl.getReturnNum());
		if (purchaseReturnDtl.getReturnNum().abs().compareTo(purchaseReturnDtl.getTallyNum()) > 0) {
			throw new BaseException(ExcMsgEnum.PO_RETURN_UPDATE_NUM_EXCEPTION, purchaseReturnDtl.getId());
		}
		if (num != 1) {
			throw new BaseException(ExcMsgEnum.PO_RETURN_UPDATE_EXCEPTION, purchaseReturnDtl.getId());
		}
		updatePoTotalNum(purchaseReturnDtlOld.getPoId());
		/** 锁定库存数量 */
		stlService.lockStlSaleNum(purchaseReturnDtlOld.getStlId(), diffNum);
		return num;
	}

	/**
	 * 根据附属编号，币种，类型和商品ID查询采购明细信息
	 * 
	 * @param storeOut
	 * @return
	 */
	public void updatePurchaseOrderLineByCon(PmsStoreOut storeOut) {
		if (storeOut == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货采购单明细查询数据为空");
		}
		// 1.根据供应商编码查询供应商的ID 根据结算对象查经营单位
		QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
		querySubjectReqDto.setPmsSupplierCode(storeOut.getAccount_sn());
		querySubjectReqDto.setSubjectType(BaseConsts.ONE);
		List<BaseSubject> custs = baseSubjectService.querySubTypeAndPmsSupplier(querySubjectReqDto);
		if (CollectionUtils.isEmpty(custs)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS出库结算对象供应商编码【" + storeOut.getAccount_sn() + "】不存在");
		}
		if (custs.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS出库结算对象供应商编码【" + storeOut.getAccount_sn() + "】不唯一");
		}
		// 2.通过pms供应商查询对应的项目与供应商
		PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
		pMSSupplierBindReqDto.setPmsSupplierNo(storeOut.getProvider_sn());
		pMSSupplierBindReqDto.setBusinessUnit(custs.get(BaseConsts.ZERO).getId());
		PMSSupplierBind pmsSupplierBind = pmsSupplierBindService.queryPmsBySuppNoAndBui(pMSSupplierBindReqDto);
		if (pmsSupplierBind == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS出库消息发送,PMS供应商编号【" + storeOut.getProvider_sn()
					+ "】,结算单编码【" + storeOut.getAccount_sn() + "】查询PMS供应商绑定数据为空");
		}
		// 3.根据供应商和SKU(编号)查询铺货商品表(得到铺货商品ID)
		List<DistributionGoods> distributionGoods = distributionGoodsService.queryDistributionGoodByNumber(storeOut,
				pmsSupplierBind);
		if (CollectionUtils.isEmpty(distributionGoods)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货采购单明细查询铺货商品信息为空");
		}
		// 4.根据铺货商品的ID查询采购单明细
		List<PurchaseOrderLine> lines = this.queryPurchaseOrderLineByCon(storeOut, distributionGoods);
		// 5.对采购单明细进行具体的业务操作
		this.purchaseOrderLineBusinessByCon(lines, storeOut);
	}

	/**
	 * 根据商品id，币种附属编号查询采购明细
	 * 
	 * @param storeOut
	 * @param distributionGoods
	 * @return
	 */
	public List<PurchaseOrderLine> queryPurchaseOrderLineByCon(PmsStoreOut storeOut,
			List<DistributionGoods> distributionGoods) {
		List<PurchaseOrderLine> lines = new ArrayList<PurchaseOrderLine>();
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setAppendNo(storeOut.getPurchase_sn());// 采购单号对应的附属编号
		// 通过value查询code
		String code = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
				storeOut.getCurrency_type());
		poTitleReqDto.setCurrencyId(Integer.valueOf(code));// 币种
		poTitleReqDto.setOrderType(BaseConsts.TWO);// 订单类型 2 铺货
		for (DistributionGoods distributionGoods2 : distributionGoods) {
			poTitleReqDto.setGoodsId(distributionGoods2.getId());// 商品的ID
			List<PurchaseOrderLine> orderLines = purchaseOrderLineDao.queryPurchaseOrderLineByCon(poTitleReqDto);
			if (CollectionUtils.isNotEmpty(orderLines)) {
				for (PurchaseOrderLine purchaseOrderLine : orderLines) {
					lines.add(purchaseOrderLine);
				}
			}
		}
		return lines;
	}

	/**
	 * 针对采购单明细进行具体的业务操作
	 * 
	 * @param lines
	 */
	private void purchaseOrderLineBusinessByCon(List<PurchaseOrderLine> lines, PmsStoreOut storeOut) {
		if (!CollectionUtils.isEmpty(lines)) {// 长度大于0
			BigDecimal wmsOutStockin = storeOut.getWms_out_stockin();
			PmsOutPoRel outPoRel = new PmsOutPoRel();
			outPoRel.setIsDelete(BaseConsts.ZERO);
			outPoRel.setPmsOutId(storeOut.getId());
			for (PurchaseOrderLine purchaseOrderLine : lines) {// 根据先进先出的原则进行级别性的数量操作
				BigDecimal outNumber = BigDecimal.ZERO;
				// 判断采购单明细的数量是否正确
				BigDecimal decimal = DecimalUtil.add(
						purchaseOrderLine.getSendNum() == null ? BigDecimal.ZERO : purchaseOrderLine.getSendNum(),
						purchaseOrderLine.getReturnNum() == null ? BigDecimal.ZERO : purchaseOrderLine.getReturnNum());
				if (!DecimalUtil.eq(DecimalUtil.subtract(purchaseOrderLine.getGoodsNum(), decimal),
						purchaseOrderLine.getRemainSendNum())) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单明细铺货可发货数量有误");
				}
				if (DecimalUtil.gt(wmsOutStockin, BigDecimal.ZERO)
						&& DecimalUtil.gt(purchaseOrderLine.getRemainSendNum(), BigDecimal.ZERO)) {
					// 根据业务需求,对铺货可发货数量出现的三种情况做相应的业务操作
					if (DecimalUtil.ge(purchaseOrderLine.getRemainSendNum(), wmsOutStockin)) {// 可发货数量大于销售数量
						outNumber = wmsOutStockin;
						purchaseOrderLine.setSendNum(DecimalUtil.add(purchaseOrderLine.getSendNum(), wmsOutStockin));// 发货数量的改变
						purchaseOrderLine.setRemainSendNum(
								DecimalUtil.subtract(purchaseOrderLine.getRemainSendNum(), wmsOutStockin));// 可发货数量
						purchaseOrderLine.setSendAmount(DecimalUtil.add(purchaseOrderLine.getSendAmount(),
								DecimalUtil.multiply(storeOut.getPurchase_price(), wmsOutStockin)));
						purchaseOrderLineDao.updatePurchaseOrderLineById(purchaseOrderLine);
						wmsOutStockin = BigDecimal.ZERO;
						// 获取当前需要组装的采购单id和数量
						Integer poId = purchaseOrderLine.getPoId();
						outPoRel.setPoId(poId);
						outPoRel.setOutNumber(outNumber);
						outPoRel.setPoLineId(purchaseOrderLine.getId());
						pmsOutPoRelDao.insertPmsOutPoRel(outPoRel);
						break;
					} else {
						BigDecimal sendNum = purchaseOrderLine.getRemainSendNum() == null ? BigDecimal.ZERO
								: purchaseOrderLine.getRemainSendNum();
						purchaseOrderLine.setSendNum(
								DecimalUtil.add(purchaseOrderLine.getSendNum(), purchaseOrderLine.getRemainSendNum()));// 发货数量的改变
						purchaseOrderLine.setRemainSendNum(DecimalUtil.subtract(purchaseOrderLine.getRemainSendNum(),
								purchaseOrderLine.getRemainSendNum()));// 可发货数量
						purchaseOrderLine.setSendAmount(DecimalUtil.add(purchaseOrderLine.getSendAmount(),
								DecimalUtil.multiply(storeOut.getPurchase_price(), sendNum)));
						purchaseOrderLineDao.updatePurchaseOrderLineById(purchaseOrderLine);
						outNumber = sendNum;
						wmsOutStockin = DecimalUtil.subtract(wmsOutStockin, sendNum);// 剩余的销售数量
						// 获取当前需要组装的采购单id和数量
						Integer poId = purchaseOrderLine.getPoId();
						outPoRel.setPoId(poId);
						outPoRel.setOutNumber(outNumber);
						outPoRel.setPoLineId(purchaseOrderLine.getId());
						pmsOutPoRelDao.insertPmsOutPoRel(outPoRel);
					}
				}
			}
			if (!DecimalUtil.le(wmsOutStockin, BigDecimal.ZERO)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS出库销售数量大于采购单明细总体数量");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "根据商品SKU查询代销订单为空");
		}
	}

	public List<PurchaseOrderTitle> queryFinishedPoByAppendNo(String affiliateNo) {
		return purchaseOrderTitleDao.queryFinishedPoByAppendNo(affiliateNo);
	}

	public BigDecimal queryRecPayAmount(Integer poId) {
		return purchaseOrderLineDao.queryRecPayAmount(poId);
	}

	public Result<PoTitleRespDto> queryPurchaseOrderTitle(Integer id) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(id);
		PoTitleRespDto respDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
		Integer currnecyTypes = checkPoTitleCurrnecy(purchaseOrderTitle);
		respDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN, currnecyTypes.toString()));
		result.setItems(respDto);
		return result;
	}

	/**
	 * 校验当前采购单关联付款单的实际币种是否一致
	 * 
	 * @param purchaseOrderTitle
	 * @param respDto
	 */
	private Integer checkPoTitleCurrnecy(PurchaseOrderTitle purchaseOrderTitle) {
		// 根据采购单头获取采购明细
		List<PoLineModel> lineModels = purchaseOrderLineDao.queryPoLineListByPoId(purchaseOrderTitle.getId());
		if (CollectionUtils.isEmpty(lineModels)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单明细为空");
		}
		BigDecimal payRate = BigDecimal.ZERO;
		Integer currnecyTypes = null;
		for (PoLineModel lineModel : lineModels) {
			if (DecimalUtil.eq(payRate, lineModel.getPayRate())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单明细汇率为零");
			}
			Integer currnecy = lineModel.getPayRealCurrency();
			if (currnecyTypes == null) {
				currnecyTypes = currnecy;
			} else {
				if (!currnecyTypes.equals(currnecy)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单明细实际支付币种不一致");
				}
			}
		}
		if (currnecyTypes == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单明细实际支付币种为空");
		}
		return currnecyTypes;

	}

	public List<PoTitleRespDto> queryAllDistributionReturnTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.THREE);
		if (poTitleReqDto.getUserId() == null) {
			poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<PurchaseOrderTitle> PoTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto);
		// 添加操作
		List<PoTitleRespDto> poRespDto = convertReturnToResult(PoTitles, poTitleReqDto);
		return poRespDto;
	}

	public List<PoTitleRespDto> queryAllDistributionSettleTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.FOUR);
		if (poTitleReqDto.getUserId() == null) {
			poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		}
		List<PurchaseOrderTitle> PoTitles = purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto);
		// 添加操作
		List<PoTitleRespDto> poRespDto = convertReturnToResult(PoTitles, poTitleReqDto);
		return poRespDto;
	}

	public List<PoLineModel> queryAllDistributionReturnLineResultsByCon(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.THREE);
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PoLineModel> poLines = purchaseOrderLineDao.queryPoLineListByCon(poTitleReqDto);
		for (PoLineModel poLineModel : poLines) {
			poLineModel.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					poLineModel.getCurrencyId() + ""));
			// 项目
			poLineModel.setProjectName(cacheService.showProjectNameById(poLineModel.getProjectId()));
			// 供应商
			poLineModel.setSupplierName(
					cacheService.showSubjectNameByIdAndKey(poLineModel.getSupplierId(), CacheKeyConsts.SUPPLIER));
			// 仓库
			poLineModel.setWarehouseName(
					cacheService.showSubjectNameByIdAndKey(poLineModel.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
			// 客户
			poLineModel.setCustomerName(
					cacheService.showSubjectNameByIdAndKey(poLineModel.getCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		return poLines;
	}

	public List<PoLineModel> queryAllDistributionSettleLineResultsByCon(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.FOUR);
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PoLineModel> poLines = purchaseOrderLineDao.queryPoLineListByCon(poTitleReqDto);
		for (PoLineModel poLineModel : poLines) {
			poLineModel.setCurrencyName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					poLineModel.getCurrencyId() + ""));
			// 项目
			poLineModel.setProjectName(cacheService.showProjectNameById(poLineModel.getProjectId()));
			// 供应商
			poLineModel.setSupplierName(
					cacheService.showSubjectNameByIdAndKey(poLineModel.getSupplierId(), CacheKeyConsts.SUPPLIER));
			// 仓库
			poLineModel.setWarehouseName(
					cacheService.showSubjectNameByIdAndKey(poLineModel.getWarehouseId(), CacheKeyConsts.WAREHOUSE));
			// 客户
			poLineModel.setCustomerName(
					cacheService.showSubjectNameByIdAndKey(poLineModel.getCustomerId(), CacheKeyConsts.CUSTOMER));
		}
		return poLines;
	}

	public boolean isOverDistributionReturnMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		poTitleReqDto.setOrderType(BaseConsts.THREE);
		int count = purchaseOrderTitleDao.countPurchaseOrderTitle(poTitleReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("铺货退货单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncDistributionReturnExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/distribution_return_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_31);
			asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public boolean isOverasyncDistributionReturnDtlByTitleIdMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.THREE);
		int count = purchaseOrderLineDao.countPoLineListByCon(poTitleReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("铺货退货单单据明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncDistributionReturnLineExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/distribution_return_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_32);
			asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncDistributionReturnExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		poTitleReqDto.setOrderType(BaseConsts.THREE);
		List<PoTitleRespDto> PoTitles = convertReturnToResult(
				purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto), poTitleReqDto);
		model.put("distributionReturnList", PoTitles);
		return model;
	}

	public Map<String, Object> asyncDistributionReturnLineExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		poTitleReqDto.setOrderType(BaseConsts.THREE);
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PoLineModel> poLines = purchaseOrderLineDao.queryPoLineListByCon(poTitleReqDto);
		for (PoLineModel poLine : poLines) {
			poLine.setCurrencyName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, poLine.getCurrencyId() + ""));
		}
		model.put("distributionReturnLineList", poLines);
		return model;
	}

	public boolean isOverDistributionSettleMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		poTitleReqDto.setOrderType(BaseConsts.FOUR);
		int count = purchaseOrderTitleDao.countPurchaseOrderTitle(poTitleReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("铺货结算单单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncDistributionSettleExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/distribution_settle_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_33);
			asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public boolean isOverasyncDistributionSettleDtlByTitleIdMaxLine(PoTitleReqDto poTitleReqDto) {
		poTitleReqDto.setOrderType(BaseConsts.FOUR);
		int count = purchaseOrderLineDao.countPoLineListByCon(poTitleReqDto);
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("铺货结算单单据明细导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncDistributionSettleLineExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/po/distribution_settle_dtl_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_34);
			asyncExcelService.addAsyncExcel(poTitleReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncDistributionSettleExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		poTitleReqDto.setOrderType(BaseConsts.FOUR);
		List<PoTitleRespDto> PoTitles = convertReturnToResult(
				purchaseOrderTitleDao.queryPurchaseOrderTitleList(poTitleReqDto), poTitleReqDto);
		model.put("distributionSettleList", PoTitles);
		return model;
	}

	public Map<String, Object> asyncDistributionSettleLineExport(PoTitleReqDto poTitleReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		poTitleReqDto.setOrderType(BaseConsts.FOUR);
		poTitleReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PoLineModel> poLines = purchaseOrderLineDao.queryPoLineListByCon(poTitleReqDto);
		for (PoLineModel poLine : poLines) {
			poLine.setCurrencyName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, poLine.getCurrencyId() + ""));
		}
		model.put("distributionSettleLineList", poLines);
		return model;
	}

	/**
	 * 新增业务 水单 供应商退款类型 核销 查询结算单 退款金额减去
	 * 
	 * @param dto
	 * @return
	 */
	public List<PoLineModel> queryPoTitleResultByOrderType(BillOutStoreDetailSearchReqDto dto) {
		// 查询铺货结算单的查询数据
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setProjectId(dto.getProjectId());// 项目Id
		poTitleReqDto.setSupplierId(dto.getSupplierId());
		poTitleReqDto.setCurrencyId(dto.getCurrencyType());// 币种
		poTitleReqDto.setOrderType(BaseConsts.FOUR);// 计算单数据类型
		List<PoLineModel> lineModels = purchaseOrderLineDao.queryPoTitleResultByOrderType(poTitleReqDto);
		return lineModels;
	}

	/**
	 * 采购单打印数据的查询
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public PageResult<PoLineModel> queryPoLinesByPoPrintTitleId(PoTitleReqDto purchaseOrderTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		Integer id = purchaseOrderTitleReqDto.getId();
		List<PoLineModel> poLineList = queryPoTitlePrintByCon(id);
		result.setItems(poLineList);
		return result;
	}

	/**
	 * 采购单打印页面数据查询的封装
	 * 
	 * @param id
	 * @param rowBounds
	 * @return
	 */
	private List<PoLineModel> queryPoTitlePrintByCon(Integer id) {
		List<PoLineModel> poLineList = purchaseOrderLineDao.queryPoLineListByPoId(id);
		processPoLineModels(poLineList);
		if (!CollectionUtils.isEmpty(poLineList)) {
			BigDecimal totalPriceCountAmout = BigDecimal.ZERO;
			BigDecimal sumDiscountAmount = BigDecimal.ZERO;
			BigDecimal sumRateDisAmount = BigDecimal.ZERO;
			for (PoLineModel poLine : poLineList) {
				BigDecimal payRate = poLine.getPayRate() == null ? BigDecimal.ZERO : poLine.getPayRate();
				poLine.setPriceCountAmout(DecimalUtil
						.formatScale2(DecimalUtil.multiply(
								DecimalUtil.multiply(null == poLine.getDiscountPrice() ? DecimalUtil.ZERO
										: poLine.getDiscountPrice(), payRate),
								null == poLine.getGoodsNum() ? DecimalUtil.ZERO : poLine.getGoodsNum())));
				poLine.setDiscountAmount(poLine.getDiscountAmount());
				totalPriceCountAmout = DecimalUtil.add(totalPriceCountAmout, poLine.getPriceCountAmout());
				sumDiscountAmount = DecimalUtil.add(sumDiscountAmount, poLine.getDiscountAmount());
				sumRateDisAmount = DecimalUtil.add(sumRateDisAmount, poLine.getRateDisAmount());
				poLine.setTotalPriceCountAmout(totalPriceCountAmout);
				poLine.setSumDiscountAmount(sumDiscountAmount);
				poLine.setSumRateDisAmount(sumRateDisAmount);
				BigDecimal deductionPrice = BigDecimal.ZERO;
				if (poLine.getDeductionMoney() != null && poLine.getOrderTotalAmount() != null
						&& poLine.getGoodsNum() != null) {
					if (poLine.getGoodsNum() != BigDecimal.ZERO) {
						deductionPrice = DecimalUtil.divide(
								DecimalUtil.subtract(poLine.getGoodsAmount(), poLine.getDeductionMoney()),
								poLine.getGoodsNum());
					}
				}
				poLine.setDeductionPrice(deductionPrice);
				poLine.setAfterDeductionMoney(DecimalUtil.subtract(
						null == poLine.getGoodsAmount() ? BigDecimal.ZERO : poLine.getGoodsAmount(),
						null == poLine.getDeductionMoney() ? BigDecimal.ZERO : poLine.getDeductionMoney()));
			}
		}
		return poLineList;
	}

	/**
	 * 水单核完 根据水单ID核销铺货结算单明星的退款金额
	 * 
	 * @param bankReceipt
	 * @return
	 */
	public BigDecimal updatePoLineRefundAmount(BankReceipt bankReceipt) {
		// 根据水单ID查询水单和铺货结算单关联表 tb_fi_receipt_out_rel
		ReceiptOutRelReqDto relReqDto = new ReceiptOutRelReqDto();
		BigDecimal countRefundAmount = BigDecimal.ZERO;// 铺货结算单退款总金额
		relReqDto.setReceiptId(bankReceipt.getId());
		List<ReceiptOutStoreRel> outStoreRels = receiptOutStoreRelDao.queryResultsByCon(relReqDto);
		if (!CollectionUtils.isEmpty(outStoreRels)) {
			for (ReceiptOutStoreRel receiptOutStoreRel : outStoreRels) {
				if (receiptOutStoreRel.getBillType() == BaseConsts.INT_33) {
					PurchaseOrderLine purchaseOrderLine = purchaseOrderLineDao
							.queryPurchaseOrderLineById(receiptOutStoreRel.getBillOutId());
					if (purchaseOrderLine != null) {
						if (DecimalUtil.eq(purchaseOrderLine.getRefundAmount(), BigDecimal.ZERO)) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "水单核销铺货结算单退货金额为0");
						}
						countRefundAmount = DecimalUtil.add(countRefundAmount, purchaseOrderLine.getRefundAmount());// 退货金额
					}
				}
			}
		}
		return DecimalUtil.multiply(countRefundAmount, new BigDecimal("-1"));
	}

	/**
	 * 查询批量打印的采购单头信息数据
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public Result<PoTitleRespDto> queryPoSearchTitleList(PoTitleReqDto purchaseOrderTitleReqDto) throws Exception {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		List<Integer> ids = purchaseOrderTitleReqDto.getIds();
		purchaseOrderTitle = checkPoSearchTitle(ids);
		PoTitleRespDto respDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
		Integer currnecyTypes = checkPoTitleCurrnecy(purchaseOrderTitle);
		respDto.setStartDate(cacheService.getProjectItemByPid(purchaseOrderTitle.getProjectId()).getStartDate());
		BaseProject project = cacheService.getProjectById(purchaseOrderTitle.getProjectId());
		respDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN, currnecyTypes.toString()));
		respDto.setBizType(project.getBizType());
		result.setItems(respDto);
		return result;
	}

	/**
	 * 校验当前批量打印 采购单头信息的数据
	 * 
	 * @param purchaseOrderTitle
	 * @throws Exception
	 */
	private PurchaseOrderTitle checkPoSearchTitle(List<Integer> ids) throws Exception {
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		if (!CollectionUtils.isEmpty(ids)) {
			Integer project = null; // 项目
			Integer supplier = null;// 供应商
			Integer currency = null;// 币种
			Date orderTime = null; // 订单日期
			for (Integer id : ids) {
				purchaseOrderTitle = purchaseOrderTitleDao.queryAndLockById(id);
				if (purchaseOrderTitle == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息为空");
				}
				if (purchaseOrderTitle.getState() != BaseConsts.FIVE) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息状态有误");
				}
				if (purchaseOrderTitle.getProjectId() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息项目为空");
				} else {
					project = project == null ? purchaseOrderTitle.getProjectId() : project;
					if (!project.equals(purchaseOrderTitle.getProjectId())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息项目不匹配");
					}
				}
				if (purchaseOrderTitle.getSupplierId() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息供应商为空");
				} else {
					supplier = supplier == null ? purchaseOrderTitle.getSupplierId() : supplier;
					if (!supplier.equals(purchaseOrderTitle.getSupplierId())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息供应商不匹配");
					}
				}
				if (purchaseOrderTitle.getCurrencyId() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息币种为空");
				} else {
					currency = currency == null ? purchaseOrderTitle.getCurrencyId() : currency;
					if (!currency.equals(purchaseOrderTitle.getCurrencyId())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息币种不匹配");
					}
				}
				if (purchaseOrderTitle.getOrderTime() == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息订单时间为空");
				} else {
					Date time = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD,
							DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, purchaseOrderTitle.getOrderTime()));
					orderTime = orderTime == null ? time : orderTime;
					if (time.getTime() != orderTime.getTime()) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "采购单头信息订单时间不匹配");
					}
				}
				checkPoTitleCurrnecy(purchaseOrderTitle);
			}
		}
		return purchaseOrderTitle;
	}

	/**
	 * 查询采购单明细列表集合数据
	 * 
	 */
	public PageResult<PoLineModel> queryPoLinesListByPoPrintTitleId(PoTitleReqDto purchaseOrderTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		List<PoLineModel> lineModels = new ArrayList<PoLineModel>();
		List<Integer> ids = purchaseOrderTitleReqDto.getIds();
		if (!CollectionUtils.isEmpty(ids)) {
			BigDecimal countGoodsAmount = BigDecimal.ZERO;
			BigDecimal totalPriceCountAmout = BigDecimal.ZERO;
			BigDecimal sumRateDisAmount = BigDecimal.ZERO;
			BigDecimal sumDiscountAmount = BigDecimal.ZERO;
			for (Integer id : ids) {
				List<PoLineModel> poLineList = queryPoTitlePrintByCon(id);
				for (PoLineModel poLineModel : poLineList) {
					countGoodsAmount = DecimalUtil.add(countGoodsAmount, poLineModel.getGoodsAmount());
					totalPriceCountAmout = DecimalUtil.add(totalPriceCountAmout, poLineModel.getPriceCountAmout());
					sumDiscountAmount = DecimalUtil.add(sumDiscountAmount, poLineModel.getDiscountAmount());
					sumRateDisAmount = DecimalUtil.add(sumRateDisAmount, poLineModel.getRateDisAmount());
					poLineModel.setCountGoodsAmount(countGoodsAmount);
					poLineModel.setTotalPriceCountAmout(totalPriceCountAmout);
					poLineModel.setSumRateDisAmount(sumDiscountAmount);
					poLineModel.setSumDiscountAmount(sumDiscountAmount);
					lineModels.add(poLineModel);
				}
			}
		}
		result.setItems(lineModels);
		return result;
	}

	/**
	 * 对账单打印列表集合数据
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public Result<PoTitleRespDto> queryPoSearchBalOfAccount(PoTitleReqDto purchaseOrderTitleReqDto) throws Exception {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		List<Integer> ids = purchaseOrderTitleReqDto.getIds();
		purchaseOrderTitle = checkPoSearchTitle(ids);
		PoTitleRespDto respDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
		Integer currnecyTypes = checkPoTitleCurrnecy(purchaseOrderTitle);
		respDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN, currnecyTypes.toString()));
		result.setItems(respDto);
		return result;
	}

	/**
	 * 送货单打印列表集合数据
	 * 
	 * @param purchaseOrderTitleReqDto
	 * @return
	 */
	public Result<PoTitleRespDto> queryPoSearchDelGoods(PoTitleReqDto purchaseOrderTitleReqDto) throws Exception {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		List<Integer> ids = purchaseOrderTitleReqDto.getIds();
		purchaseOrderTitle = checkPoSearchTitle(ids);
		PoTitleRespDto respDto = purchaseOrderTitleConvertToRes(purchaseOrderTitle);
		Integer currnecyTypes = checkPoTitleCurrnecy(purchaseOrderTitle);
		respDto.setCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN, currnecyTypes.toString()));
		result.setItems(respDto);
		return result;
	}
	
	public int countPoLineListByPoId(Integer poId) {
		return purchaseOrderLineDao.countPoLineListByPoId(poId);
	}
	
}
