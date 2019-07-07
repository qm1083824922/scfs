package com.scfs.service.interf;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.dao.api.pms.PmsPayDao;
import com.scfs.dao.api.pms.PmsPayDtlDao;
import com.scfs.dao.api.pms.PmsPayPoRelDao;
import com.scfs.dao.base.entity.BaseAccountDao;
import com.scfs.dao.fi.RecLineDao;
import com.scfs.dao.fi.ReceiveDao;
import com.scfs.dao.interf.PMSSupplierBindDao;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.dao.project.ProjectGoodsDao;
import com.scfs.dao.project.ProjectPoolAssetDao;
import com.scfs.dao.project.ProjectSubjectDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.CodeValue;
import com.scfs.domain.api.pms.entity.PmsPay;
import com.scfs.domain.api.pms.entity.PmsPayDtl;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.api.pms.entity.PmsPayPoRel;
import com.scfs.domain.api.pms.entity.PmsPaySum;
import com.scfs.domain.api.pms.model.PmsPayModel;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.subject.dto.req.QueryAccountReqDto;
import com.scfs.domain.base.subject.dto.req.QuerySubjectReqDto;
import com.scfs.domain.fi.dto.req.RecLineSearchReqDto;
import com.scfs.domain.fi.dto.req.VoucherSearchReqDto;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.dto.PmsStoreResDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.logistics.dto.req.BillInStoreSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.project.dto.req.ProjectGoodsSearchReqDto;
import com.scfs.domain.project.dto.req.ProjectSubjectSearchReqDto;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.project.entity.ProjectPoolAsset;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.service.base.subject.BaseSubjectService;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.logistics.BillInStoreService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.pay.PayService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 *  pms同步请款待付款处理相关
 *  File: PmsPayWaitService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年05月06日			Administrator
 *
 * </pre>
 */
@Service
public class PmsPayWaitService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayWaitService.class);

	@Autowired
	private PmsPayDao pmsPayDao;

	@Autowired
	private PmsPayDtlDao pmsPayDtlDao;

	@Autowired
	private PMSSupplierBindDao pmsSupplierBindDao;

	@Autowired
	private ProjectGoodsDao projectGoodsDao;

	@Autowired
	private PurchaseOrderService purchaseOrderService;// 采购单

	@Autowired
	private BillInStoreService billInStoreService;// 入库单

	@Autowired
	private BillInStoreDao billInStoreDao;// 入库

	@Autowired
	private BillOutStoreDao billOutStoreDao;// 出库

	@Autowired
	private BillDeliveryService billDeliveryService;

	@Autowired
	private BillOutStoreService billOutStoreService; // 出库单

	@Autowired
	private BillDeliveryDao billDeliveryDao;// 销售单

	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;// 销售单明细

	@Autowired
	private ReceiveDao receiveDao; // 应收

	@Autowired
	private RecLineDao recLineDao;// 应收明细

	@Autowired
	private BaseAccountDao baseAccountDao;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private StlDao stlDao;

	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao; // 代购订单明细

	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;// 代购订单头

	@Autowired
	private ProjectPoolAssetDao projectPoolAssetDao;// 资产明细
	@Autowired
	private ProjectPoolService projectPoolService;// 资金池
	@Autowired
	private ProjectItemService projectItemService; // 条款
	@Autowired
	private VoucherService voucherService;// 凭证
	@Autowired
	private ProjectSubjectDao projectSubjectDao;// 仓库
	@Autowired
	private PmsPayPoRelDao payPoRelDao;
	@Autowired
	private PayService payService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private BaseSubjectService baseSubjectService;
	@Autowired
	private PMSSupplierBindService pmsSupplierBindService;

	/**
	 * 驳回业务处理
	 * 
	 * @param id
	 */
	public void dealPmsPayRebut(Integer id) {
		PmsPay pmsPay = pmsPayDao.queryEntityById(id);
		PmsPay pmsPayResult = new PmsPay();
		pmsPayResult.setUnique_number(pmsPay.getUnique_number());
		List<PmsPay> pmsPayList = pmsPayDao.queryPmsPayGroupDao(pmsPayResult);// 处理
		for (PmsPay model : pmsPayList) {
			if (model.getStatus().equals(BaseConsts.ZERO) && model.getDealFlag().equals(BaseConsts.THREE)) {// 驳回相关处理
				rejectPmsPay(pmsPay);
				dealSuccess(id);
			}
		}
	}

	/**
	 * 待付款业务
	 * 
	 * @param id
	 */
	public void dealPmsPayWait(PmsPay pmsPay, boolean isSchedule) {
		Integer id = pmsPay.getId();
		// 根据邓老板确认，先去取明细结算对象并判断是否相同，在根据结算对象去PMS供应商
		PmsPayDtl pmsDtlList = new PmsPayDtl();
		pmsDtlList.setPmsPayId(id);
		if (isSchedule) {
			pmsDtlList.setDealFlag(BaseConsts.ONE);
		} else {
			pmsDtlList.setDealFlag(BaseConsts.TWO);
		}
		List<PmsPayDtl> payDtlList = pmsPayDtlDao.queryPmsPayDtl(pmsDtlList);
		String accountSn = null;
		if (CollectionUtils.isNotEmpty(payDtlList)) {
			for (PmsPayDtl pmsPayDtl : payDtlList) {
				if (StringUtils.isEmpty(accountSn)) {
					accountSn = pmsPayDtl.getAccount_sn();
				} else {
					if (!accountSn.equals(pmsPayDtl.getAccount_sn())) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
								"PMS请款单明细结算类型【" + accountSn + ">>>" + pmsPayDtl.getAccount_sn() + "】两者不一致");
					}
				}
			}
		}
		// 根据结算对象查经营单位
		QuerySubjectReqDto querySubjectReqDto = new QuerySubjectReqDto();
		querySubjectReqDto.setPmsSupplierCode(accountSn);
		querySubjectReqDto.setSubjectType(BaseConsts.ONE);
		List<BaseSubject> custs = baseSubjectService.querySubTypeAndPmsSupplier(querySubjectReqDto);
		if (CollectionUtils.isEmpty(custs)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS请款结算对象供应商编码【" + accountSn + "】不存在");
		}
		if (custs.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "PMS请款结算对象供应商编码【" + accountSn + "】不唯一");
		}
		// 通过pms供应商查询对应的项目与供应商
		PMSSupplierBindReqDto pMSSupplierBindReqDto = new PMSSupplierBindReqDto();
		pMSSupplierBindReqDto.setPmsSupplierNo(pmsPay.getProvider_sn());
		pMSSupplierBindReqDto.setBusinessUnit(custs.get(BaseConsts.ZERO).getId());
		PMSSupplierBind pmsSupplierBind = pmsSupplierBindService.queryPmsBySuppNoAndBui(pMSSupplierBindReqDto);
		Integer projectId = pmsSupplierBind.getProjectId();
		String supplierNo = pmsSupplierBind.getSupplierNo();
		BaseSubject supplier = cacheService.getSupplierByPidAndNo(projectId, supplierNo);
		if (supplier == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目" + projectId + "下供应商编号" + supplierNo + "不存在");
		}
		QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
		queryAccountReqDto.setId(supplier.getId());
		List<BaseAccount> baseAccounts = baseAccountDao.queryAccountBySubjectId(queryAccountReqDto);
		if (CollectionUtils.isEmpty(baseAccounts)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商[" + supplierNo + "]没有可用的账户");
		}
		ProjectSubjectSearchReqDto projectReqDto = new ProjectSubjectSearchReqDto();
		projectReqDto.setProjectId(projectId);
		projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_WAREHOUSE);
		projectReqDto.setStatus(BaseConsts.ONE);
		List<ProjectSubject> projectSubjectList = projectSubjectDao.queryResultsByCon(projectReqDto);
		if (CollectionUtils.isEmpty(projectSubjectList)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目" + projectId + "下无仓库信息");
		}

		Integer currencyType = PmsPayOrderTitle.convertCurrencyType(pmsPay.getCurrency_type());

		List<CodeValue> codeValue = commonService.getAllOwnCv("PROJECT_CUSTOMER", projectId.toString());

		PmsPayDtl result = new PmsPayDtl();
		result.setPmsPayId(id);
		List<PmsPayDtl> pmsPayDtlList = pmsPayDtlDao.queryPmsPayDtl(result);
		if (!CollectionUtils.isEmpty(pmsPayDtlList)) {
			BigDecimal payAmount = BigDecimal.ZERO;// 付款金额

			PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
			purchaseOrderTitle.setProjectId(projectId);
			purchaseOrderTitle.setSupplierId(supplier.getId());
			purchaseOrderTitle.setWarehouseId(projectSubjectList.get(BaseConsts.ZERO).getSubjectId()); // 请款仓
			purchaseOrderTitle.setAppendNo(pmsPay.getPay_sn());
			purchaseOrderTitle.setCurrencyId(currencyType);
			purchaseOrderTitle.setOrderTime(pmsPay.getPay_create_time());
			purchaseOrderTitle.setIsRequestPay(BaseConsts.TWO); // 不需要付款
			purchaseOrderTitle.setCustomerId(Integer.parseInt(codeValue.get(BaseConsts.ZERO).getCode()));
			purchaseOrderTitle.setOrderType(BaseConsts.ZERO); // 采购

			Integer poId = purchaseOrderService.addPurchaseOrderTitle(purchaseOrderTitle); // 添加采购订单头
			purchaseOrderTitle.setId(poId);
			for (PmsPayDtl item : pmsPayDtlList) {
				ProjectGoodsSearchReqDto projectGoodsSearchReqDto = new ProjectGoodsSearchReqDto();
				projectGoodsSearchReqDto.setProjectId(projectId);
				projectGoodsSearchReqDto.setNumber(item.getSku());
				List<ProjectGoods> projectGoods = projectGoodsDao.queryResultGoodsBySku(projectGoodsSearchReqDto);// 查询对应的商品信息
				if (CollectionUtils.isEmpty(projectGoods)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
							"商品SKU为[" + item.getSku() + "],项目ID为【" + projectId + "】未找到商品信息");
				}
				PoTitleReqDto poTitleReqDto = new PoTitleReqDto();// 获取sku和对应采购单号获取铺货信息
				List<PurchaseOrderLine> pulineList = new ArrayList<PurchaseOrderLine>();
				// 商品ID
				Integer goodId = null;
				BigDecimal pledge = BigDecimal.ZERO;
				for (ProjectGoods goods : projectGoods) {
					goodId = goods.getGoodsId();
					pledge = goods.getPledge();
					poTitleReqDto.setGoodsId(goods.getGoodsId());
					poTitleReqDto.setAppendNo(item.getPurchase_sn());
					poTitleReqDto.setOrderType(BaseConsts.TWO);
					poTitleReqDto.setProjectId(projectId);
					poTitleReqDto.setIsPayAll(BaseConsts.ONE); // 1-已付款
					List<PurchaseOrderLine> lines = purchaseOrderLineDao.queryLineBySkuNumber(poTitleReqDto);
					if (CollectionUtils.isNotEmpty(lines)) {
						for (PurchaseOrderLine purchaseOrderLine : lines) {
							pulineList.add(purchaseOrderLine);
						}
					}
				}
				if (CollectionUtils.isNotEmpty(pulineList)) {
					BigDecimal sumNum = BigDecimal.ZERO;
					for (PurchaseOrderLine purchaseOrderLine : pulineList) {
						sumNum = DecimalUtil.add(sumNum, purchaseOrderLine.getSendNum());
					}
					if (DecimalUtil.gt(item.getPay_quantity(), sumNum)) {// 处理数量相关业务
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货待付款数量【" + item.getPay_quantity()
								+ "】大于匹配的铺货订单【" + item.getPurchase_sn() + "】可用发货数量【" + sumNum + "】");
					} else {
						BigDecimal amount = BigDecimal.ZERO;
						for (PurchaseOrderLine purchaseLine : pulineList) {
							amount = DecimalUtil.add(amount, purchaseLine.getSendNum());
							BigDecimal num = BigDecimal.ZERO;
							if (DecimalUtil.ge(amount, item.getPay_quantity())) {// 执行数量总数
								num = DecimalUtil.subtract(item.getPay_quantity(),
										DecimalUtil.subtract(amount, purchaseLine.getSendNum()));
							} else {
								num = purchaseLine.getSendNum();
							}
							PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
							purchaseOrderLine.setGoodsNum(num);
							purchaseOrderLine.setGoodsId(goodId);
							purchaseOrderLine.setPoId(poId);
							purchaseOrderLine.setGoodsPrice(item.getDeal_price());
							purchaseOrderLine.setDiscountAmount(BigDecimal.ZERO);
							purchaseOrderLine.setCostPrice(item.getDeal_price());
							purchaseOrderLine.setPoPrice(item.getDeal_price());
							purchaseOrderLine.setRequiredSendPrice(item.getDeal_price());
							purchaseOrderLine.setBatchNum(item.getPurchase_sn());// 采购单明细批次新增采购单号

							BigDecimal profitPrice = projectItemService.getProfitPrice(projectId,
									purchaseLine.getPayPrice(), purchaseLine.getPayTime(), pmsPay.getPay_create_time());
							purchaseOrderLine.setDiscountPrice(
									DecimalUtil.subtract(purchaseOrderLine.getGoodsPrice(), profitPrice));
							purchaseOrderLine.setDiscountAmount(DecimalUtil.formatScale2(DecimalUtil.subtract(
									DecimalUtil.multiply(purchaseOrderLine.getGoodsPrice(),
											purchaseOrderLine.getGoodsNum()),
									DecimalUtil.multiply(purchaseOrderLine.getDiscountPrice(),
											purchaseOrderLine.getGoodsNum()))));

							purchaseOrderLine.setPayTime(purchaseLine.getPayTime());
							purchaseOrderLine.setPayPrice(purchaseLine.getPayPrice());
							purchaseOrderLine.setPayRate(
									purchaseLine.getPayRate() == null ? BigDecimal.ZERO : purchaseLine.getPayRate());
							purchaseOrderLine.setPayRealCurrency(purchaseLine.getPayRealCurrency());
							purchaseOrderLine.setDistributeId(purchaseLine.getId());
							purchaseOrderLine.setPledgeProportion(pledge);
							BigDecimal payPrice = BigDecimal.ZERO;// 付款单价
							BigDecimal payNum = BigDecimal.ZERO;// 数量
							if (purchaseLine.getPayPrice() != null) {
								payPrice = purchaseOrderLine.getPayPrice();
							}
							if (purchaseLine.getGoodsNum() != null) {
								payNum = purchaseOrderLine.getGoodsNum();
							}
							BigDecimal paidAmount = DecimalUtil.formatScale2(DecimalUtil.multiply(payPrice, payNum));// 已付款金额
							purchaseOrderLine.setPaidAmount(paidAmount);

							Integer pyPoLineId = purchaseOrderService.addPoLine(poId, purchaseOrderLine);// 添加采购单行信息
							PurchaseOrderLine upPurchaseLine = new PurchaseOrderLine();
							upPurchaseLine.setId(purchaseLine.getId());
							upPurchaseLine
									.setWaitDistributeNum(DecimalUtil.add(purchaseLine.getWaitDistributeNum(), num));
							purchaseOrderLineDao.updatePurchaseOrderLineById(upPurchaseLine);
							// 新增pms请款单和采购单的关系
							PmsPayPoRel pmsPayPoRel = new PmsPayPoRel();
							pmsPayPoRel.setPmsPayDtlId(item.getId());
							pmsPayPoRel.setPoId(purchaseLine.getPoId());
							pmsPayPoRel.setPoLineId(purchaseLine.getId());
							pmsPayPoRel.setPayQuantity(num);
							pmsPayPoRel.setPyPoLineId(pyPoLineId);
							payPoRelDao.insert(pmsPayPoRel);
							if (DecimalUtil.ge(amount, item.getPay_quantity())) {
								break;
							}
							payAmount = DecimalUtil.add(payAmount, paidAmount);
						}
					}
				} else {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "查询不到匹配的铺货订单");
				}
			}
			// 修改付款金额
			PurchaseOrderTitle upOrderTitle = new PurchaseOrderTitle();
			upOrderTitle.setId(poId);
			upOrderTitle.setPayAmount(payAmount);
			purchaseOrderService.updatePurchaseOrderTitle(upOrderTitle);
			realWait(purchaseOrderTitle, pmsPay);
			dealSuccess(id);
		}

	}

	private PMSSupplierBind getPmsSupplierBind(PmsPay pmsPay) {
		PMSSupplierBindReqDto reqDto = new PMSSupplierBindReqDto();
		reqDto.setPmsSupplierNo(pmsPay.getProvider_sn());
		reqDto.setStatus(BaseConsts.TWO);
		reqDto.setBizType(BaseConsts.SIX);// 待付款 融通质押
		List<PMSSupplierBind> pmsSupplierBinds = pmsSupplierBindDao.queryByProjectStatus(reqDto);
		if (CollectionUtils.isEmpty(pmsSupplierBinds)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "pms供应商绑定数据有误，请核查");
		}
		return pmsSupplierBinds.get(0);
	}

	/**
	 * 驳回业务
	 * 
	 * @param id
	 */
	public void rejectPmsPay(PmsPay model) {
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setAppendNo(model.getPay_sn());
		// poTitleReqDto.setWarehouseId(BaseConsts.INT_42); // 请款仓
		PurchaseOrderTitle title = purchaseOrderTitleDao.queryEntityByParam(poTitleReqDto);// 获取采购单头

		// 驳回相关处理
		realReject(model.getPay_sn());

		// 回写待铺货数量
		List<PoLineModel> lineList = purchaseOrderLineDao.queryPoLineListByPoId(title.getId());
		for (PoLineModel poLine : lineList) {
			Integer distriteId = poLine.getDistributeId();// 铺货id
			BigDecimal num = poLine.getGoodsNum();
			PurchaseOrderLine resultLine = purchaseOrderLineDao.queryPurchaseOrderLineById(distriteId);
			PurchaseOrderLine upLine = new PurchaseOrderLine();
			upLine.setId(distriteId);
			upLine.setWaitDistributeNum(DecimalUtil.subtract(resultLine.getWaitDistributeNum(), num));
			purchaseOrderLineDao.updatePurchaseOrderLineById(upLine);
		}
		// 删除采购单
		purchaseOrderTitleDao.deleteById(title.getId());
	}

	/**
	 * 待付款，入库单，出库单，库存操作
	 * 
	 * @param purchaseOrderTitle
	 * @param pmsPay
	 */
	public void realWait(PurchaseOrderTitle purchaseOrderTitle, PmsPay pmsPay) {
		purchaseOrderService.submitPurchaseOrderTitle(purchaseOrderTitle); // 自动提交，创建相关
		createPayOrderByPo(pmsPay);// 尾款生成付款单
		String billInStoreNo = purchaseOrderService.receivePurchaseOrderTitle(purchaseOrderTitle);// 自动收货,生成入库单
		BillInStoreSearchReqDto billInStoreSearchReqDto = new BillInStoreSearchReqDto();
		billInStoreSearchReqDto.setBillNo(billInStoreNo);
		billInStoreSearchReqDto.setProjectId(purchaseOrderTitle.getProjectId());
		billInStoreSearchReqDto.setSupplierId(purchaseOrderTitle.getSupplierId());
		List<BillInStore> billInStores = billInStoreDao.queryResultsByCon(billInStoreSearchReqDto);
		if (CollectionUtils.isEmpty(billInStores)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "生成入库单失败");
		}
		billInStoreService.submitBillInStore(billInStores.get(BaseConsts.ZERO), pmsPay.getPay_create_time()); // 提交入库单

		List<Stl> stls = stlDao.queryResultsByPoId(purchaseOrderTitle.getId());
		if (CollectionUtils.isEmpty(stls)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "生成库存失败");
		}
		for (Stl stl : stls) {
			stl.setRequiredSendNum(stl.getInStoreNum());
			PurchaseOrderLine line = purchaseOrderService.queryPoLineEntityById(stl.getPoDtlId());
			stl.setRequiredSendPrice(line.getRequiredSendPrice());
		}
		Integer billDeliveryId = billDeliveryService.autoDelivery(purchaseOrderTitle, stls,
				pmsPay.getPay_create_time()); // 生成销售单，待发货
		BillOutStore billOutStore = billOutStoreService.queryValidBillOutStoreByBillDeliveryId(billDeliveryId);
		billOutStore.setDeliverTime(pmsPay.getPay_create_time());
		billOutStoreService.sendBillOutStore(billOutStore); // 出库单送货
	}

	/**
	 * 驳回相,入库单，库存，出库，资金回写
	 * 
	 * @param paySn
	 *            附属编号
	 */
	public void realReject(String paySn) {
		// 获取入库信息
		BillInStoreSearchReqDto billInStoreSearchReqDto = new BillInStoreSearchReqDto();
		billInStoreSearchReqDto.setAffiliateNo(paySn);
		List<BillInStore> billInStore = billInStoreDao.queryEntityByParam(billInStoreSearchReqDto);
		Integer billInStoreId = billInStore.get(BaseConsts.ZERO).getId();
		// 删除库存
		Stl stl = new Stl();
		stl.setBillInStoreId(billInStoreId);
		stl.setInStoreNum(BigDecimal.ZERO); // 入库单数量
		stl.setStoreNum(BigDecimal.ZERO); // 库单数量
		stl.setLockNum(BigDecimal.ZERO); // 出库锁定数量
		stl.setSaleLockNum(BigDecimal.ZERO); // 销售锁定数量
		stlDao.updateByBillInStoreId(stl);
		// 入库资金回写
		ProjectPoolAsset projectPoolAsset = projectPoolAssetDao
				.queryResultByBillNo(billInStore.get(BaseConsts.ZERO).getBillNo());
		projectPoolAssetDao.deleteById(projectPoolAsset.getId());
		projectPoolService.updateProjectPoolInfo(billInStore.get(BaseConsts.ZERO).getProjectId());
		// 撤销入库单凭证
		cancelVoucherByBillInStore(billInStoreId);
		// 删除入库单
		billInStoreDao.deleteById(billInStoreId);

		// 删除销售单
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setAffiliateNo(paySn);
		BillDelivery updateDeliver = billDeliveryDao.queryEntityByParam(billDelivery);
		// 删除销售单明细
		List<BillDeliveryDtl> dtlList = billDeliveryDtlDao.queryResultsByBillDeliveryId(updateDeliver.getId());// 获取销售单明细信息
		if (!CollectionUtils.isEmpty(dtlList)) {
			for (BillDeliveryDtl billDeliveryDtl : dtlList) {
				billDeliveryDtlDao.deleteById(billDeliveryDtl.getId());
			}
		}
		billDeliveryDao.deleteById(updateDeliver);// 删除

		// 删除出库
		BillOutStore billOutStore = new BillOutStore();
		billOutStore.setAffiliateNo(paySn);
		List<BillOutStore> outStore = billOutStoreDao.queryResultsByAffiliateNo(billOutStore);
		Integer billOutStoreId = outStore.get(BaseConsts.ZERO).getId();
		// 出库资金回写
		ProjectPoolAsset projectPoolAssetOut = projectPoolAssetDao
				.queryResultByBillNo(outStore.get(BaseConsts.ZERO).getBillNo());
		projectPoolAssetDao.deleteById(projectPoolAssetOut.getId());
		projectPoolService.updateProjectPoolInfo(outStore.get(BaseConsts.ZERO).getProjectId());
		// 撤销出库单凭证
		cancelVoucherByBillOutStore(billOutStoreId);
		// 删除出库单
		billOutStoreDao.deleteById(billOutStoreId);

		// 删除应收和应收明细
		RecLineSearchReqDto req = new RecLineSearchReqDto();
		req.setOutStoreId(billOutStoreId);
		List<RecLine> recLineList = recLineDao.queryResultsRecLineByCon(req);
		if (!CollectionUtils.isEmpty(recLineList)) {
			for (RecLine recLine : recLineList) {
				Receive receive = receiveDao.queryEntityById(recLine.getRecId());
				if (receive != null) {
					receiveDao.deleteById(receive.getId());
				}
				recLineDao.deleteById(recLine.getId());
			}
		}
	}

	private void cancelVoucherByBillInStore(Integer billInStoreId) {
		BillInStore billInStore = billInStoreDao.queryAndLockEntityById(billInStoreId);
		VoucherSearchReqDto voucherSearchReqDto = new VoucherSearchReqDto();
		voucherSearchReqDto.setBillType(BaseConsts.TWO);
		voucherSearchReqDto.setBillNo(billInStore.getBillNo());
		List<Voucher> vouchers = voucherService.queryListByCon(voucherSearchReqDto);
		if (!CollectionUtils.isEmpty(vouchers)) {
			Voucher voucher = vouchers.get(0);
			voucherService.deleteOverVoucherById(voucher.getId());
		}
	}

	private void cancelVoucherByBillOutStore(Integer billOutStoreId) {
		BillOutStore billOutStore = billOutStoreDao.queryAndLockEntityById(billOutStoreId);
		VoucherSearchReqDto voucherSearchReqDto = new VoucherSearchReqDto();
		voucherSearchReqDto.setBillType(BaseConsts.THREE);
		voucherSearchReqDto.setBillNo(billOutStore.getBillNo());
		List<Voucher> vouchers = voucherService.queryListByCon(voucherSearchReqDto);
		if (!CollectionUtils.isEmpty(vouchers)) {
			Voucher voucher = vouchers.get(0);
			voucherService.deleteOverVoucherById(voucher.getId());
		}
	}

	/**
	 * 失败处理
	 * 
	 * @param id
	 * @param errorMsg
	 */
	public void dealFail(Integer id, String errorMsg) {
		PmsPay pmsPay = pmsPayDao.queryEntityById(id);
		pmsPay.setDealFlag(BaseConsts.TWO);
		pmsPay.setDealMsg(commonService.getMsg(errorMsg));
		pmsPayDao.updateById(pmsPay);
		sendSystemAlarm(id, BaseConsts.ZERO);
	}

	/**
	 * 成功处理
	 * 
	 * @param id
	 */
	public void dealSuccess(Integer id) {
		PmsPay pmsPay = pmsPayDao.queryEntityById(id);
		pmsPay.setDealFlag(BaseConsts.THREE);
		pmsPay.setDealMsg("pms请款单待付款处理成功！");
		pmsPayDao.updateById(pmsPay);
		// 推送消息
		sendSystemAlarm(id, BaseConsts.ONE);
	}

	/**
	 * 封装当前的PMS请款单的明细数据
	 * 
	 * @param storeIns
	 * @return
	 */
	public List<PmsStoreResDto> converPmsPayResDtos(List<PmsPayModel> payModels) {
		List<PmsStoreResDto> dtos = new ArrayList<PmsStoreResDto>();
		if (ListUtil.isEmpty(payModels)) {
			return dtos;
		} else {
			for (PmsPayModel payModel : payModels) {
				PmsStoreResDto dto = new PmsStoreResDto();
				if (null != payModel) {
					BeanUtils.copyProperties(payModel, dto);
					dto.setPay_sn(payModel.getPaySn());// 请款单号
					dto.setPmsSeriesId(payModel.getPmsSeriesId());// 流水表id
					dto.setProvider_sn(payModel.getProviderSn());// 供应商编号
					dto.setCurrency_type(payModel.getCurrencyType());// 币种
					// 通过value查询code
					String code = ServiceSupport.getCodeByBizValue(BizCodeConsts.DEFAULT_CURRENCY_TYPE_EN,
							payModel.getCurrencyType());
					dto.setCurrencyName(
							ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, code + ""));
					dto.setDeduction_money(payModel.getDeductionMoney());// 入库价格
					dto.setMsg(payModel.getMsg());// 描述
					dto.setPurchase_sn(payModel.getPurchaseSn());// 采购单号
					dto.setSku(payModel.getSku());
					dto.setPay_quantity(payModel.getPayQuantity());// 数量
					dto.setDeal_price(payModel.getDealPrice());// 价格
					dto.setStatusName(
							ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_PAY_STATE, payModel.getStatus() + ""));
					dto.setDealFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_SERIES_STATE,
							payModel.getDealFlag() + ""));
					dtos.add(dto);
				}
			}
		}
		return dtos;
	}

	private void createPayOrderByPo(PmsPay pmsPay) {
		List<PurchaseOrderTitle> poList = purchaseOrderService.queryFinishedPoByAppendNo(pmsPay.getPay_sn());
		if (!CollectionUtils.isEmpty(poList)) {
			if (poList.size() > 1) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "处理异常: 该请款单对应多个SCFS采购单");
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "处理异常: 该请款单没有对应SCFS采购单");
		}

		PurchaseOrderTitle po = poList.get(BaseConsts.ZERO);
		Integer payId = purchaseOrderService.addPayOrderByPo(po, BaseConsts.TWO);// 生成付款单

		QueryAccountReqDto queryAccountReqDto = new QueryAccountReqDto();
		queryAccountReqDto.setId(po.getSupplierId());
		List<BaseAccount> baseAccounts = baseAccountDao.queryAccountBySubjectId(queryAccountReqDto);
		if (CollectionUtils.isEmpty(baseAccounts)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "供应商[" + po.getSupplierId() + "]没有可用的账户");
		}
		PayOrder poUpd = new PayOrder();
		poUpd.setId(payId);
		poUpd.setPayWay(BaseConsts.ONE); // 付款方式为转账
		poUpd.setPayAccountId(baseAccounts.get(BaseConsts.ZERO).getId());
		poUpd.setAttachedNumbe(pmsPay.getPay_sn());
		poUpd.setRequestPayTime(DateFormatUtils.afterDay(pmsPay.getPay_create_time(), BaseConsts.SEVEN));
		poUpd.setInnerPayDate(null);
		poUpd.setRemark(
				"PMS请款单确认日期：" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, pmsPay.getPay_create_time()));
		payService.updatePayOrderById(poUpd);
	}

	/**
	 * 发送系统消息
	 * 
	 * @param pmsPayId
	 * @param resultType
	 *            处理结果 0-失败 1-成功
	 */
	@IgnoreTransactionalMark
	private void sendSystemAlarm(int pmsPayId, Integer resultType) {
		try {
			PmsPay pmsPay = pmsPayDao.queryEntityById(pmsPayId);
			PmsPaySum pmsPaySum = pmsPayDtlDao.queryPmsPaySum(pmsPayId);
			PMSSupplierBind pmsSupplierBind = getPmsSupplierBind(pmsPay);

			String stateName = ServiceSupport.getValueByBizCode(BizCodeConsts.PMS_PAY_STATE, pmsPay.getStatus() + "");
			BaseUser toUser = ServiceSupport.getOfficalUser(pmsSupplierBind.getProjectId()); // 商务主管
			String content = "";
			content = content + "单据编号:" + pmsPay.getPay_sn() + "\n";
			content = content + "项目:" + cacheService.getProjectNameById(pmsSupplierBind.getProjectId()) + "\n";
			content = content + "付款金额:"
					+ DecimalUtil.toAmountString(null == pmsPaySum ? BigDecimal.ZERO : pmsPaySum.getTotalDealAmount())
					+ pmsPay.getCurrency_type() + "\n";
			content = content + "信息:" + "单据由系统管理员于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "处理,请款单状态【" + stateName
					+ "】\n";
			content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
			content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

			String title = "SCFS系统提醒您,有新的【PMS融通铺货请款单】需要处理";
			if (resultType.equals(BaseConsts.ONE)) {
				msgContentService.addMsgContentByUserId(toUser.getId(), title, content, BaseConsts.ONE);
			} else {
				msgContentService.addMsgContentByUserId(toUser.getId(), title, content, BaseConsts.ONE);
				msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, content, BaseConsts.ONE);
			}
		} catch (Exception e) {
			LOGGER.error("PMS铺货请款单(待付款)接口发送系统消息失败：", e);
		}
	}
}
