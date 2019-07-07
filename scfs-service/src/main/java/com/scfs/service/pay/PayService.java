package com.scfs.service.pay;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.consts.SysParamConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.fee.FeeDao;
import com.scfs.dao.fee.FeeSpecDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.interf.PmsPayOrderTitleDao;
import com.scfs.dao.logistics.BillInStoreDao;
import com.scfs.dao.logistics.BillInStoreDtlDao;
import com.scfs.dao.logistics.BillInStoreTallyDtlDao;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.pay.PayFeeRelationDao;
import com.scfs.dao.pay.PayOrderDao;
import com.scfs.dao.pay.PayPoRelationDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.api.pms.entity.PmsPayOrderTitle;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseAccount;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fee.dto.req.FeeSpecSearchReqDto;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeSpec;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.dto.resp.VoucherLineModelResDto;
import com.scfs.domain.fi.entity.AccountBook;
import com.scfs.domain.fi.entity.RecDetail;
import com.scfs.domain.fi.entity.RecLine;
import com.scfs.domain.fi.entity.Receive;
import com.scfs.domain.interf.dto.PmsPoTitleSearchReqDto;
import com.scfs.domain.logistics.entity.BillInStore;
import com.scfs.domain.logistics.entity.BillInStoreDtl;
import com.scfs.domain.logistics.entity.BillInStoreTallyDtl;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.pay.dto.req.PayAdvanceRelationReqDto;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.dto.req.PayOrderBatchConfirmReq;
import com.scfs.domain.pay.dto.req.PayOrderSearchReqDto;
import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.dto.resq.PayAdvanceRelationResDto;
import com.scfs.domain.pay.dto.resq.PayOrderBatchConfirmResp;
import com.scfs.domain.pay.dto.resq.PayOrderFileResDto;
import com.scfs.domain.pay.dto.resq.PayOrderResDto;
import com.scfs.domain.pay.dto.resq.PayPoRelationResDto;
import com.scfs.domain.pay.entity.PayFeeRelationModel;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.pay.entity.PayPoRelation;
import com.scfs.domain.pay.entity.PayPoRelationModel;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectPoolFund;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.PayAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.base.exchangeRate.BaseExchangeRateService;
import com.scfs.service.bookkeeping.FeeKeepingService;
import com.scfs.service.bookkeeping.PayOrderKeepingService;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.common.SysParamService;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.fi.ReceiptFundPoolService;
import com.scfs.service.fi.ReceiveService;
import com.scfs.service.fi.VoucherLineService;
import com.scfs.service.interf.PmsPayOrderTitleService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ExcelService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 *
 *  File: PayService.java
 *  Description:
 *  TODO
 *  Date,                   Who,
 *  2016年11月08日         Administrator
 *
 * </pre>
 */
@Service
public class PayService {

	@Autowired
	private PayOrderDao payOrderDao;
	@Autowired
	private PayFeeRelationDao payFeeRelationDao;
	@Autowired
	private FeeDao feeDao;
	@Autowired
	private PayPoRelationDao payPoRelationDao;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private PayPoRelationService payPoRelationService;
	@Autowired
	private PayFeeRelationService payFeeRelationService;
	@Autowired
	private PayAuditService payAuditService;
	@Autowired
	private PayAdvanceRelationService payAdvanceRelationService;// 预收单
	@Autowired
	private PayOrderKeepingService payOrderKeepingService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private ProjectPoolService projectPoolService;
	@Autowired
	private BillInStoreDao billInStoreDao;
	@Autowired
	private BillInStoreDtlDao billInStoreDtlDao;
	@Autowired
	private BillInStoreTallyDtlDao billInStoreTallyDtlDao;
	@Autowired
	private StlDao stlDao;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private PmsPayOrderTitleService pmsPayOrderTitleService;
	@Autowired
	private PmsPayOrderTitleDao pmsPayOrderTitleDao;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private BaseExchangeRateService baseExchangeRateService;
	@Autowired
	private AsyncExcelService asyncExcelService;
	@Autowired
	private FeeServiceImpl feeServiceImpl;
	@Autowired
	private FeeKeepingService feeKeepingService;
	@Autowired
	private FeeSpecDao feeSpecDao;
	@Autowired
	private MergePayOrderService mergePayOrderService;
	@Autowired
	private VoucherLineService voucherLineService;
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private PayRefundRelationService payRefundRelationService;
	@Autowired
	private ReceiptFundPoolService receiptFundPoolService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private AccountPoolFundService accountPoolFundService;// 资金池明细
	@Autowired
	private AuditFlowService auditFlowService;
	@Autowired
	private BankReceiptService bankReceiptService;

	/**
	 * 新建付款信息
	 *
	 * @param payOrder
	 */
	public PayOrder createPayOrder(PayOrder payOrder) {
		// 经营单位 根据项目ID获取经营单位ID
		if (payOrder.getPayType() == BaseConsts.THREE) {
			payOrder.setBusiUnit(payOrder.getPayer());
		} else {
			if (payOrder.getProjectId() != null) {
				BaseProject baseProject = cacheService.getProjectById(payOrder.getProjectId());// 项目
				if (baseProject != null) {
					payOrder.setBusiUnit(baseProject.getBusinessUnitId());
				}
			}
		}
		if (payOrder.getState() == null) {
			payOrder.setState(BaseConsts.ZERO);
		}
		if (payOrder.getPayWayType() == null) {
			payOrder.setPayWayType(BaseConsts.ZERO);
		}
		Date date = new Date();
		payOrder.setCreateAt(date);
		payOrder.setCreator(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		payOrder.setCreatorId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		payOrder.setPayNo(sequenceService.getNumDateByBusName(BaseConsts.PAY_ORDER_NO, SeqConsts.S_PAYORDER_NO,
				BaseConsts.INT_13));
		payOrder.setBlance(payOrder.getPayAmount()); // 该字段已废弃
		payOrder.setPoBlance(BigDecimal.ZERO);
		payOrder.setFeeBlance(BigDecimal.ZERO);
		if (payOrder.getAdvanceAmount() == null) {
			payOrder.setAdvanceAmount(BigDecimal.ZERO);
		}
		int id = payOrderDao.insert(payOrder);

		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(payOrder));
		}
		return payOrder;
	}

	public BaseResult updateReject(ProjectItemReqDto reqDto) {
		BaseResult baseResult = new BaseResult();
		payAuditService.unPassAudit(reqDto);
		return baseResult;
	}

	/**
	 * 更新付款信息
	 *
	 * @param payOrder
	 * @return
	 */
	public BaseResult updatePayOrderById(PayOrder payOrder) {
		BaseResult baseResult = new BaseResult();

		// 经营单位 根据项目ID获取经营单位ID
		if (payOrder.getProjectId() != null) {
			BaseProject baseProject = cacheService.getProjectById(payOrder.getProjectId());// 项目
			if (baseProject != null) {
				payOrder.setBusiUnit(baseProject.getBusinessUnitId());
			}
		}
		int result = payOrderDao.updateById(payOrder);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新付款失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 编辑付款信息
	 *
	 * @param payOrder
	 * @return
	 */
	public Result<PayOrderResDto> editPayOrderById(PayOrder payOrder) {
		Result<PayOrderResDto> result = new Result<PayOrderResDto>();
		PayOrder payOrderResult = payOrderDao.queryEntityById(payOrder.getId());
		PayOrderResDto data = convertToPayOrderResDto(payOrderResult);
		result.setItems(data);
		return result;
	}

	/**
	 * 浏览付款信息
	 *
	 * @param payOrder
	 * @return
	 */
	public Result<PayOrderResDto> detailPayOrderById(PayOrder payOrder) {
		Result<PayOrderResDto> result = new Result<PayOrderResDto>();
		PayOrder payResult = payOrderDao.queryEntityById(payOrder.getId());
		PayOrderResDto data = convertToPayOrderResDto(payResult);
		result.setItems(data);
		return result;
	}

	/**
	 * 已完成,付款确认/开立
	 * 
	 * @param payOrder
	 * @return
	 */
	public BaseResult submitPayOver(PayOrder payOrder) {
		return submitPayOver(payOrder, BaseConsts.ZERO);
	}

	/**
	 * 已完成,付款确认/开立
	 *
	 * @param payOrder
	 * @param paySource 0-scfs确认 1-cms确认
	 * @return
	 */
	public BaseResult submitPayOver(PayOrder payOrder, Integer paySource) {
		BaseResult result = new BaseResult();
		PayOrder vo = payOrderDao.queryEntityById(payOrder.getId());// 锁住表
		if (vo.getState() != BaseConsts.FOUR && vo.getState() != BaseConsts.FIVE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,付款单状态不正确!");
		}
		Integer state = vo.getState();
		vo.setState(BaseConsts.SIX);
		if (paySource.equals(BaseConsts.ZERO)) {
			vo.setConfirmor(ServiceSupport.getUser().getChineseName());
			vo.setConfirmorId(ServiceSupport.getUser().getId());
		}
		vo.setPaymentAccount(payOrder.getPaymentAccount());
		vo.setBankCharge(payOrder.getBankCharge());
		vo.setConfirmorAt(payOrder.getConfirmorAt());
		vo.setMemoTime(payOrder.getConfirmorAt());// 水单日期
		vo.setRealCurrencyType(payOrder.getRealCurrencyType());
		vo.setPayRate(payOrder.getPayRate());
		vo.setUnionOverIdentifier(payOrder.getUnionOverIdentifier());
		vo.setRealPayAmount(payOrder.getRealPayAmount());
		vo.setCmsPayer(payOrder.getCmsPayer());
		payOrderDao.updateById(vo);
		//核销付款单
		writeOffPayOrder(vo, payOrder, state);
		return result;
	}

	/**
	 * 核销付款单
	 */
	private void writeOffPayOrder(PayOrder vo, PayOrder payOrder, Integer state) {
		if (vo.getNoneOrderFlag() == BaseConsts.ZERO 
				|| (state == BaseConsts.SIX && vo.getNoneOrderFlag() == BaseConsts.ONE)) {	//有订单付款，或者已完成且无订单付款
			BigDecimal checkAmount = DecimalUtil.add(null == payOrder.getPoBlance() ? BigDecimal.ZERO : payOrder.getPoBlance(), 
					DecimalUtil.add(null == payOrder.getFeeBlance() ? BigDecimal.ZERO : payOrder.getFeeBlance(), 
							null == payOrder.getDeductionFeeAmount() ? BigDecimal.ZERO : payOrder.getDeductionFeeAmount()));
			vo.setCheckAmount(checkAmount);
			
			updateFundUsed(vo);
			if (vo.getPayType() == BaseConsts.ONE && !vo.getPayWayType().equals(BaseConsts.TWO)) { // 非尾款，付款类型为货款
				/**
				 * 1.更新订单行付款时间(pay_time) 2.更新订单行付款单价(pay_price)
				 */
				// 实际付款金额 =付款金额(已经减掉抵扣费用金额)-预收金额
				BigDecimal realPayAmount = DecimalUtil.subtract(vo.getPayAmount(), vo.getAdvanceAmount());

				if (realPayAmount != null) {
					List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<PurchaseOrderLine>();
					BigDecimal rate = BigDecimal.ZERO;
					if (DecimalUtil.ne(vo.getPayAmount(), BigDecimal.ZERO)) {
						rate = realPayAmount.divide(vo.getPayAmount(), 8, BigDecimal.ROUND_HALF_UP);
					}
					PayPoRelationReqDto payPoRelationReqDto = new PayPoRelationReqDto();
					payPoRelationReqDto.setPayId(vo.getId());
					List<PayPoRelationModel> payPoRelationModels = payPoRelationService
							.queryPayPoRelationByCon(payPoRelationReqDto);
					for (int index = 0; index < payPoRelationModels.size(); index++) {
						PayPoRelationModel item = payPoRelationModels.get(index);
						if (item.getWriteOffFlag() == BaseConsts.ZERO) {	//未核销
							PurchaseOrderLine poLineEntity = purchaseOrderService.queryPoLineEntityById(item.getPoLineId());
							PurchaseOrderLine poLineUpd = new PurchaseOrderLine();
							if (poLineEntity == null) {
								throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PurchaseOrderService.class,
										item.getPoLineId());
							}
							poLineUpd.setId(poLineEntity.getId());
							poLineUpd.setPoId(poLineEntity.getPoId());
							poLineUpd.setPayTime(payOrder.getConfirmorAt());
							BigDecimal tmp_price = poLineEntity.getDiscountPrice().multiply(rate).setScale(8,
									BigDecimal.ROUND_HALF_UP);
							BaseGoods pro = cacheService.getGoodsById(poLineEntity.getGoodsId());
							if (pro.getGoodType() == 1) {
								poLineUpd.setPayPrice(DecimalUtil.multiply(tmp_price, poLineEntity.getPledgeProportion()));
							} else {
								poLineUpd.setPayPrice(tmp_price);
							}
							poLineUpd.setPayRate(payOrder.getPayRate() == null ? BigDecimal.ZERO : payOrder.getPayRate());
							poLineUpd.setPayRealCurrency(payOrder.getRealCurrencyType());
							purchaseOrderService.updatePoLinesById(poLineUpd);
							purchaseOrderLines.add(poLineUpd);
						}
						if (state == BaseConsts.SIX && vo.getNoneOrderFlag() == BaseConsts.ONE) {	//已完成且无订单付款
							item.setWriteOffFlag(BaseConsts.ONE);	//已核销
							payPoRelationDao.updateById(item);
						}
					}
					// 更新入库单相关付款价格付款时间
					updateBillPayPrice(purchaseOrderLines);
				}
				this.createProjectPool(vo, BigDecimal.ZERO);
			} else if (vo.getPayType() == BaseConsts.TWO) {
				PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
				payFeeRelationReqDto.setPayId(vo.getId());
				List<PayFeeRelationModel> ll = payFeeRelationService.queryPayFeeRelatioByCon(payFeeRelationReqDto);
				BigDecimal mm = DecimalUtil.ZERO;
				for (int i = 0; i < ll.size(); i++) {
					PayFeeRelationModel item = ll.get(i);
					// 费用为资金占用类型入池
					if (item.getPayFeeType() == BaseConsts.ONE) {
						mm = DecimalUtil.add(mm, item.getPayAmount());
					}
					if (state == BaseConsts.SIX && vo.getNoneOrderFlag() == BaseConsts.ONE) {	//已完成且无订单付款
						item.setWriteOffFlag(BaseConsts.ONE);	//已核销
						payFeeRelationDao.updateById(item);
					}
				}
				mm = DecimalUtil.formatScale2(mm);
				if (mm.compareTo(DecimalUtil.ZERO) != 0) {
					this.createProjectPool(vo, mm);
				}
			} else if (vo.getPayType() == BaseConsts.SIX) { // 付退款的核销业务操作
				payRefundRelationService.refundWriteByCon(vo);
			}
		}
		if (vo.getNoneOrderFlag() == BaseConsts.ZERO 
				|| !(state == BaseConsts.SIX && vo.getNoneOrderFlag() == BaseConsts.ONE)) {	//有订单付款，或者非已完成且无订单付款
			payOrderKeepingService.payBookkeeping(payOrder.getId());
			// 付借款类型的付款单不查询条款信息
			ProjectItem projectItem = new ProjectItem();
			if (!vo.getPayType().equals(BaseConsts.THREE)) {
				projectItem = projectItemService.getProjectItem(vo.getProjectId());
			}
			// 预付款单的时候生成预付款类型水单
			if (vo.getPayType().equals(BaseConsts.FIVE)) {
				bankReceiptService.createBankReceiptByPay(payOrder.getId());
				this.createProjectPool(vo, vo.getRealPayAmount());
			}
			if (!StringUtils.isEmpty(projectItem.getIsOperateAccount())
					&& projectItem.getIsOperateAccount().equals(BaseConsts.TWO) && projectItem.getOperateFeeRate() != null
					&& DecimalUtil.gt(projectItem.getOperateFeeRate(), DecimalUtil.ZERO)) {
				Fee fee = convertToFee(payOrder, projectItem);
				if (fee != null) {
					feeServiceImpl.addFee(fee);

					Fee feeUpd = new Fee();
					feeUpd.setId(fee.getId());
					feeUpd.setState(BaseConsts.THREE);
					feeUpd.setAuditAt(payOrder.getConfirmorAt());
					feeUpd.setAuditor(BaseConsts.SYSTEM_ROLE_NAME);
					feeUpd.setAuditorId(BaseConsts.SYSTEM_ROLE_ID);
					feeUpd.setBookDate(payOrder.getConfirmorAt());
					feeServiceImpl.updateFeeById(feeUpd);

					// 生成凭证
					Integer lineId = feeKeepingService.feeBookkeeping(fee.getId());

					if (lineId != null) {// 自动对账
						BaseProject project = cacheService.getProjectById(fee.getProjectId());
						if (project == null) {
							throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class, fee.getProjectId());
						}
						BaseSubject busiUnit = cacheService.getBusiUnitById(project.getBusinessUnitId());
						if (busiUnit == null) {
							throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, CacheService.class,
									project.getBusinessUnitId());
						}

						VoucherLineModelResDto lineDto = voucherLineService.queryEntityDtoById(lineId);

						RecDetail recDetail = new RecDetail();
						Receive receive = new Receive();
						receive.setProjectId(fee.getProjectId());
						receive.setCustId(projectItem.getCustomerId());
						receive.setBusiUnit(busiUnit.getId());
						receive.setCurrencyType(fee.getCurrencyType());
						receive.setCheckDate(payOrder.getConfirmorAt());// 对账日期
						receive.setExpireDate(DateFormatUtils.afterDay(payOrder.getConfirmorAt(),
								projectItem.getFundAccountPeriod() == null ? BaseConsts.ZERO
										: projectItem.getFundAccountPeriod()));// 应收到期日期=确认日期+合作账期
						List<RecLine> recLines = new ArrayList<RecLine>();

						RecLine model = new RecLine();
						model.setAmountCheck(lineDto.getAmountUnChecked());// 对账金额
						model.setVoucherLineId(lineDto.getId());// 对账id
						recLines.add(model);

						recDetail.setReceive(receive);
						recDetail.setRecLines(recLines);
						receiveService.createRecDetail(recDetail);
					}
				}
			}

			PmsPoTitleSearchReqDto req = new PmsPoTitleSearchReqDto();
			if (!StringUtils.isEmpty(vo.getAttachedNumbe())) {
				req.setPayNo(vo.getAttachedNumbe());
				List<PmsPayOrderTitle> pmsPayOrderTitles = pmsPayOrderTitleDao.queryResultsByPayNo(req);
				if (!CollectionUtils.isEmpty(pmsPayOrderTitles)) { // pms请款单
					pmsPayOrderTitleService.confirmPmsPayOrder(pmsPayOrderTitles.get(0).getId(), vo.getId());
				}
			}
			// 除去尾款的入池
			if (!vo.getPayWayType().equals(BaseConsts.TWO)) {
				// 付款订单类型做资金池入池
				if (vo.getPayType() == BaseConsts.ONE || vo.getPayType() == BaseConsts.TWO
						|| vo.getPayType() == BaseConsts.THREE) {
					FundPoolReqDto fundPoolReqDto = new FundPoolReqDto();
					fundPoolReqDto.setId(payOrder.getId());
					receiptFundPoolService.createFundPoolPayByCon(fundPoolReqDto);
				}
			}

			if (state.equals(BaseConsts.FOUR)) {// 待确认进行资金池操作
				if (vo.getPayType() == BaseConsts.ONE || vo.getPayType() == BaseConsts.TWO) {
					accountPoolFundService.dealPayOrder(payOrderDao.queryEntityById(payOrder.getId()));
				}
			}
		}
		if (vo.getNoneOrderFlag() == BaseConsts.ZERO 
				|| (state == BaseConsts.SIX && vo.getNoneOrderFlag() == BaseConsts.ONE)) {	//有订单付款，或者已完成且无订单付款
			//更改核销状态-已核销
			PayOrder updatePayOrder = new PayOrder();
			updatePayOrder.setId(vo.getId());
			updatePayOrder.setWriteOffFlag(BaseConsts.ONE);
			updatePayOrder.setCheckAmount(vo.getCheckAmount());
			payOrderDao.updateById(updatePayOrder);
		}
	}
	
	/**
	 * 提取 付款确认/开立 的公共属性和方法
	 * 
	 * @param ppf
	 * @return
	 */
	public void createProjectPool(PayOrder payOrder, BigDecimal billAmount) {
		ProjectPoolFund ppf = new ProjectPoolFund();
		if (payOrder.getPayType() == BaseConsts.ONE) { // 货款
			ppf.setBillAmount(DecimalUtil.subtract(payOrder.getPayAmount(), payOrder.getAdvanceAmount()));
		} else if (payOrder.getPayType() == BaseConsts.TWO) { // 费用-资金占用
			ppf.setBillAmount(billAmount);
		} else if (payOrder.getPayType() == BaseConsts.FIVE) { // 预付款 -实际付款金额
			ppf.setBillAmount(billAmount);
		}
		ppf.setType(BaseConsts.ONE);
		ppf.setBillNo(payOrder.getPayNo());
		ppf.setBillSource(BaseConsts.ONE);
		ppf.setProjectId(payOrder.getProjectId());
		ppf.setSupplierId(payOrder.getPayee());
		ppf.setBusinessDate(payOrder.getConfirmorAt());
		ppf.setBillCurrencyType(payOrder.getRealCurrencyType());
		ppf.setBillProjectExchangeRate(payOrder.getPayRate());
		if (payOrder.getPayType() == BaseConsts.ONE) {
			projectPoolService.addProjectPoolFund(ppf, BaseConsts.ONE); // 1-付货款
		} else if (payOrder.getPayType() == BaseConsts.TWO) {
			projectPoolService.addProjectPoolFund(ppf, BaseConsts.TWO); // 2-付费用
		} else if (payOrder.getPayType() == BaseConsts.FIVE) {
			projectPoolService.addProjectPoolFund(ppf, BaseConsts.FIVE); // 5-预付款
		}
		projectPoolService.updateProjectPoolInfo(ppf.getProjectId());
	}

	private Fee convertToFee(PayOrder payOrder, ProjectItem projectItem) {
		payOrder = queryEntityById(payOrder.getId());
		Fee fee = new Fee();
		if (payOrder.getPayType().equals(BaseConsts.TWO)) {
			List<PayFeeRelationModel> payFeeRelationModels = payFeeRelationService
					.queryGroupFeeByPayOrderId(payOrder.getId());
			if (CollectionUtils.isEmpty(payFeeRelationModels)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单【" + payOrder.getPayNo() + "】下没有费用单");
			}
			BigDecimal sumAmount = BigDecimal.ZERO;
			for (PayFeeRelationModel payFeeRelationModel : payFeeRelationModels) {
				if (payFeeRelationModel.getFeeType().equals(BaseConsts.THREE)) {
					sumAmount = DecimalUtil.formatScale2(DecimalUtil.add(sumAmount, payOrder.getPayRate()
							.multiply(payFeeRelationModel.getPayAmount().multiply(projectItem.getOperateFeeRate()))));
				}
			}
			if (DecimalUtil.eq(sumAmount, DecimalUtil.ZERO)) {
				return null;
			}
			fee.setRecAmount(sumAmount);
		} else {
			fee.setRecAmount(DecimalUtil
					.formatScale2(DecimalUtil.multiply(payOrder.getRealPayAmount(), projectItem.getOperateFeeRate())));
		}
		fee.setFeeType(BaseConsts.ONE);
		fee.setPayId(payOrder.getId());
		fee.setCurrencyType(payOrder.getRealCurrencyType());
		fee.setProjectId(payOrder.getProjectId());
		FeeSpecSearchReqDto feeSpecSearchReqDto = new FeeSpecSearchReqDto();
		feeSpecSearchReqDto.setFeeSpecNo(BaseConsts.DEFAULT_REC_FEE_SPEC_NO);
		feeSpecSearchReqDto.setFeeType(BaseConsts.ONE);
		List<FeeSpec> feeSpecs = feeSpecDao.queryAllFeeSpec(feeSpecSearchReqDto);
		if (CollectionUtils.isEmpty(feeSpecs)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "应收费用科目【" + BaseConsts.DEFAULT_REC_FEE_SPEC_NO + "】不存在");
		}
		fee.setRecFeeSpec(feeSpecs.get(0).getId());
		fee.setRecType(BaseConsts.ONE);// 转账
		fee.setRecDate(payOrder.getConfirmorAt());
		fee.setCustPayer(projectItem.getCustomerId());

		AccountBook accountBook = cacheService.getAccountBookByBusiUnitAndState(payOrder.getBusiUnit());
		if (accountBook == null || accountBook.getId() == null) {
			throw new BaseException(ExcMsgEnum.ACCOUNT_BOOK_INVALID, payOrder.getBusiUnit());
		}
		if (accountBook.getIsHome().equals(BaseConsts.ONE)) {
			fee.setProvideInvoiceType(BaseConsts.TWO);
			fee.setProvideInvoiceTaxRate(new BigDecimal(BaseConsts.DEFAULT_PROVIDE_INVOICE_TAX_RATE));
		} else {
			fee.setProvideInvoiceType(BaseConsts.ONE);
		}
		fee.setRecNote("付款编号" + payOrder.getPayNo() + "/付款金额" + payOrder.getRealPayAmount() + ServiceSupport
				.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, payOrder.getRealCurrencyType() + ""));
		return fee;
	}

	// 批量确认付款订单
	public void batchConfirmPayOver(PayOrderBatchConfirmReq payOrderBatchConfirmReq) {
		List<Integer> ids = payOrderBatchConfirmReq.getIds();
		if (CollectionUtils.isNotEmpty(ids)) {
			String unionOverIdentifier = sequenceService.getNumIncByBusName("", SeqConsts.S_UNION_OVER_IDENTIFIER,
					BaseConsts.SIX);
			BigDecimal realPayAmount = payOrderBatchConfirmReq.getRealPayAmount();
			BigDecimal payAmountSum = payOrderDao.querySumByIds(ids);
			BigDecimal payRate8Bit = DecimalUtil.divide(realPayAmount, payAmountSum);
			for (int index = 0; index < ids.size(); index++) {
				PayOrder entity = queryEntityById(ids.get(index));
				PayOrder payOrder = new PayOrder();
				payOrder.setId(entity.getId());
				try {
					payOrder.setConfirmorAt(DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD_HH_MM_SS,
							payOrderBatchConfirmReq.getConfirmorAt()));
					payOrder.setMemoTime(payOrder.getConfirmorAt());
				} catch (ParseException e) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "确认日期有误");
				}
				payOrder.setPaymentAccount(payOrderBatchConfirmReq.getPaymentAccount());
				payOrder.setRealCurrencyType(payOrderBatchConfirmReq.getRealCurrencyType());
				payOrder.setPayRate(payOrderBatchConfirmReq.getPayRate());
				payOrder.setUnionOverIdentifier(unionOverIdentifier);
				if (index == 0) {
					payOrder.setBankCharge(payOrderBatchConfirmReq.getBankCharge());
				} else {
					payOrder.setBankCharge(BigDecimal.ZERO);
				}
				if ((index + 1) == ids.size()) {
					payOrder.setRealPayAmount(realPayAmount);
				} else {
					BigDecimal tempAmount = DecimalUtil.formatScale2(payRate8Bit.multiply(entity.getPayAmount()));
					realPayAmount = realPayAmount.subtract(tempAmount);
					payOrder.setRealPayAmount(tempAmount);
				}
				submitPayOver(payOrder);
			}
		}
	}

	private void updateFundUsed(PayOrder payOrder) {
		PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
		payFeeRelationReqDto.setPayId(payOrder.getId());
		payFeeRelationReqDto.setPayFeeType(BaseConsts.ONE);
		List<PayFeeRelationModel> list = payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto);
		if (CollectionUtils.isNotEmpty(list)) {
			BigDecimal plage = getPlage(list);
			for (PayFeeRelationModel payFeeRelationModel : list) {
				Fee fee = feeDao.queryEntityById(payFeeRelationModel.getFeeId());
				if (DecimalUtil.ge(payFeeRelationModel.getPayAmount(), BigDecimal.ZERO)) {
					Fee newFee = new Fee();
					newFee.setId(payFeeRelationModel.getFeeId());
					newFee.setFundUsed(DecimalUtil.add(fee.getFundUsed() == null ? BigDecimal.ZERO : fee.getFundUsed(),
							payFeeRelationModel.getPayAmount()
									.subtract(DecimalUtil.multiply(payFeeRelationModel.getPayAmount(), plage))));
					newFee.setFundUsed(newFee.getFundUsed().setScale(BaseConsts.TWO, BigDecimal.ROUND_HALF_UP));
					feeDao.updateById(newFee);
				}
			}
		}
	}

	private BigDecimal getPlage(List<PayFeeRelationModel> list) {
		BigDecimal minusNumber = new BigDecimal(BaseConsts.ZERO);
		BigDecimal positiveNumber = new BigDecimal(BaseConsts.ZERO);
		for (PayFeeRelationModel fee : list) {
			if (DecimalUtil.ge(fee.getPayAmount(), BigDecimal.ZERO))
				positiveNumber = DecimalUtil.add(fee.getPayAmount(), positiveNumber);
			else
				minusNumber = DecimalUtil.add(fee.getPayAmount(), minusNumber);
		}
		BigDecimal plage = DecimalUtil.divide(minusNumber, positiveNumber)
				.setScale(BaseConsts.EIGHT, BigDecimal.ROUND_HALF_UP).abs();
		return plage;
	}

	public BigDecimal queryFundUsedByPayIdAndFee(Integer payId, Fee fee) {
		BigDecimal fundUsed = BigDecimal.ZERO;
		PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
		payFeeRelationReqDto.setPayId(payId);
		payFeeRelationReqDto.setPayFeeType(BaseConsts.ONE);
		List<PayFeeRelationModel> list = payFeeRelationDao.queryResultsByCon(payFeeRelationReqDto);
		if (CollectionUtils.isNotEmpty(list)) {
			BigDecimal plage = getPlage(list);
			for (PayFeeRelationModel payFeeRelationModel : list) {
				if (DecimalUtil.ge(payFeeRelationModel.getPayAmount(), BigDecimal.ZERO)
						&& fee.getId().equals(payFeeRelationModel.getFeeId())) {
					PayOrder payOrder = payOrderDao.queryEntityById(payId);
					BigDecimal payRate = null == payOrder.getPayRate() ? BigDecimal.ZERO : payOrder.getPayRate();
					fundUsed = DecimalUtil.multiply(payFeeRelationModel.getPayAmount()
							.subtract(DecimalUtil.multiply(payFeeRelationModel.getPayAmount(), plage)), payRate);
				}
			}
		}
		return fundUsed;
	}

	private void updateBillPayPrice(List<PurchaseOrderLine> purchaseOrderLines) {
		if (!CollectionUtils.isEmpty(purchaseOrderLines)) {
			for (PurchaseOrderLine item : purchaseOrderLines) {
				List<BillInStoreDtl> billInStoreDtls = billInStoreDtlDao.queryResultsByPoLineId(item.getId());
				if (!CollectionUtils.isEmpty(billInStoreDtls)) {
					Map<Integer, BigDecimal> payMap = new HashMap<Integer, BigDecimal>();
					// 如果存在入库单明细，且入库单明细的pay_time,pay_price为空，回写采购订单明细的pay_time,pay_price
					for (BillInStoreDtl billInStoreDtl : billInStoreDtls) {
						BillInStoreDtl entity = billInStoreDtlDao.queryEntityById(billInStoreDtl.getId()); // 加锁
						if (entity == null) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, BillInStoreDtlDao.class,
									billInStoreDtl.getId());
						}
						Integer billInStoreId = entity.getBillInStoreId();
						BillInStoreDtl billInStoreDtlUpd = new BillInStoreDtl();
						billInStoreDtlUpd.setId(entity.getId());
						billInStoreDtlUpd.setPayPrice(item.getPayPrice());
						billInStoreDtlUpd.setPayTime(item.getPayTime());
						billInStoreDtlUpd.setPayRate(item.getPayRate() == null ? BigDecimal.ZERO : item.getPayRate());
						billInStoreDtlUpd.setPayRealCurrency(item.getPayRealCurrency());
						billInStoreDtlDao.updateById(billInStoreDtlUpd);
						BigDecimal amount = DecimalUtil.multiply(item.getPayPrice(), entity.getReceiveNum());
						if (StringUtils.isEmpty(payMap.get(billInStoreId))) {
							payMap.put(billInStoreId, amount);
						} else {
							payMap.put(billInStoreId, DecimalUtil.add(amount, payMap.get(billInStoreId)));
						}
					}

					List<BillInStoreTallyDtl> billInStoreTallyDtls = billInStoreTallyDtlDao
							.queryResultsByPoLineId(item.getId());
					if (!CollectionUtils.isEmpty(billInStoreTallyDtls)) {
						for (BillInStoreTallyDtl billInStoreTallyDtl : billInStoreTallyDtls) {
							BillInStoreTallyDtl entity = billInStoreTallyDtlDao
									.queryEntityById(billInStoreTallyDtl.getId());
							if (entity == null) {
								throw new BaseException(ExcMsgEnum.ERROR_GENERAL, BillInStoreTallyDtlDao.class,
										billInStoreTallyDtl.getId());
							}
							BillInStoreTallyDtl billInStoreTallyDtlUpd = new BillInStoreTallyDtl();
							billInStoreTallyDtlUpd.setId(entity.getId());
							billInStoreTallyDtlUpd.setPayPrice(item.getPayPrice());
							billInStoreTallyDtlUpd.setPayTime(item.getPayTime());
							billInStoreTallyDtlUpd
									.setPayRate(item.getPayRate() == null ? BigDecimal.ZERO : item.getPayRate());
							billInStoreTallyDtlUpd.setPayRealCurrency(item.getPayRealCurrency());
							billInStoreTallyDtlDao.updateById(billInStoreTallyDtlUpd);
						}
					}

					List<Stl> stls = stlDao.queryResultsByPoLineId(item.getId());
					if (!CollectionUtils.isEmpty(stls)) {
						for (Stl stl : stls) {
							Stl entity = stlDao.queryEntityById(stl.getId());
							if (entity == null) {
								throw new BaseException(ExcMsgEnum.ERROR_GENERAL, StlDao.class, stl.getId());
							}
							Stl stlUpd = new Stl();
							stlUpd.setId(entity.getId());
							stlUpd.setPayPrice(item.getPayPrice());
							stlUpd.setPayTime(item.getPayTime());
							stlUpd.setPayRate(item.getPayRate() == null ? BigDecimal.ZERO : item.getPayRate());
							stlUpd.setPayRealCurrency(item.getPayRealCurrency());
							stlDao.updateById(stlUpd);
						}
					}
					for (Integer key : payMap.keySet()) {
						BillInStore billInStoreUpd = new BillInStore();
						BillInStore billInStore = billInStoreDao.queryAndLockEntityById(key);
						billInStoreUpd.setId(key);
						billInStoreUpd.setPayAmount(DecimalUtil.add(billInStore.getPayAmount(), payMap.get(key)));
						billInStoreDao.updateById(billInStoreUpd);
					}
				}
			}
		}
	}

	public PayOrder queryEntityById(Integer id) {
		return payOrderDao.queryEntityById(id);
	}

	public PayOrder queryEntityByPayNo(String payNo) {
		return payOrderDao.queryEntityByPayNo(payNo);
	}

	/**
	 * 提交付款信息
	 *
	 * @param payOrder
	 * @param isWechat true-微信 false-PC
	 * @return
	 */
	public void submitPayOrderById(PayOrder payOrder, boolean isWechat) {
		int payId = payOrder.getId();
		PayOrder payResult = payOrderDao.queryEntityById(payId);
		List<PayPoRelation> paypoList = payPoRelationDao.queryPayPoListByPayId(payId);
		if (payResult.getPayType().equals(BaseConsts.ONE) && DecimalUtil.eq(payResult.getPayAmount(), DecimalUtil.ZERO)
				&& CollectionUtils.isNotEmpty(paypoList)) {// 付款金额为零，且有采购单明细,付款确认时间取当前时间，且回写采购单相关
			payResult.setConfirmorAt(new Date());
			payResult.setConfirmor(ServiceSupport.getUser().getChineseName());
			payResult.setConfirmorId(ServiceSupport.getUser().getId());
			payOrderDao.updateById(payResult);

			List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<PurchaseOrderLine>();
			PayPoRelationReqDto payPoRelationReqDto = new PayPoRelationReqDto();
			payPoRelationReqDto.setPayId(payId);
			List<PayPoRelationModel> payPoRelationModels = payPoRelationService
					.queryPayPoRelationByCon(payPoRelationReqDto);
			for (int index = 0; index < payPoRelationModels.size(); index++) {
				PayPoRelationModel item = payPoRelationModels.get(index);
				PurchaseOrderLine poLineEntity = purchaseOrderService.queryPoLineEntityById(item.getPoLineId());
				PurchaseOrderLine poLineUpd = new PurchaseOrderLine();
				if (poLineEntity == null) {
					throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PurchaseOrderService.class,
							item.getPoLineId());
				}
				poLineUpd.setId(poLineEntity.getId());
				poLineUpd.setPoId(poLineEntity.getPoId());
				poLineUpd.setPayTime(payResult.getConfirmorAt());
				poLineUpd.setPayRate(new BigDecimal(BaseConsts.ONE));
				poLineUpd.setPayRealCurrency(payResult.getCurrnecyType());
				purchaseOrderService.updatePoLinesById(poLineUpd);
				purchaseOrderLines.add(poLineUpd);
			}
			updateBillPayPrice(purchaseOrderLines);
			PayOrder poUpd = new PayOrder();
			poUpd.setState(BaseConsts.SIX);
			poUpd.setId(payId);
			poUpd.setPayRate(new BigDecimal(BaseConsts.ONE));
			poUpd.setRealCurrencyType(payResult.getCurrnecyType());
			poUpd.setRealPayAmount(payOrder.getPayAmount());
			payOrderDao.updateById(poUpd);
		} else {
			Integer state = payResult.getState();
			checkSubmit(payResult);
			if (state == BaseConsts.SIX && payResult.getNoneOrderFlag() == BaseConsts.ONE) {	//已完成且无订单付款
				//付款单核销
				writeOffPayOrder(payResult, payResult, state);
			} else {
				PayOrder poUpd = new PayOrder();
				BaseProject baseProject = cacheService.getProjectById(payResult.getProjectId());
				Map<String, Object> paramMap = Maps.newHashMap();
				paramMap.put("payOrderId", payId);
				AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.FIVE, baseProject.getId(), paramMap);
				if (null == startAuditNode) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
				}
				if (payResult.getPayWayType().equals(BaseConsts.ONE) && baseProject.getBizType().equals(BaseConsts.SIX)
						&& state != BaseConsts.THREE) { // 预付款付款单,融通铺货项目,并且不是供应商确认提交
					poUpd.setState(BaseConsts.THREE);// 供应商确认
					poUpd.setId(payId);
					payOrderDao.updateById(poUpd);
					payAuditService.sendWechatMsgByProject(payResult);// 发送微信消息
					payResult = payOrderDao.queryEntityById(payOrder.getId());
					payAuditService.startAudit(payResult, startAuditNode, BaseConsts.INT_99);// 提交审核
				} else {
					poUpd.setState(startAuditNode.getAuditNodeState());
					poUpd.setId(payId);
					payOrderDao.updateById(poUpd);
					payResult = payOrderDao.queryEntityById(payOrder.getId());
					payAuditService.startAudit(payResult, startAuditNode, state);// 提交审核
				}
			}
		}
	}

	public boolean isNeedRiskAudit(int bizType) {
		List<String> paramValueList = sysParamService.queryParamValueListByParamKey(SysParamConsts.RISK_BIZ_TYPE);
		for (String paramValue : paramValueList) {
			if (Integer.parseInt(paramValue) == bizType) {
				return true;
			}
		}
		return false;
	}

	public void checkSubmit(PayOrder payOrder) {
		int state = payOrder.getState();
		if (state != BaseConsts.ZERO && state != BaseConsts.THREE && state != BaseConsts.SIX) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误，不能提交");
		} else if (state == BaseConsts.SIX && payOrder.getNoneOrderFlag() == BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单状态错误，不能提交");
		}
		BigDecimal payAmount = (null == payOrder.getPayAmount() ? BigDecimal.ZERO : payOrder.getPayAmount());
		BigDecimal deductionFeeAmount = (null == payOrder.getDeductionFeeAmount() ? BigDecimal.ZERO
				: payOrder.getDeductionFeeAmount());
		int type = payOrder.getPayType();
		
		BigDecimal sumPayAmount = payAdvanceRelationService.sumPayAmount(payOrder.getId());// 获取预收单总额
		if (DecimalUtil.lt(payAmount, sumPayAmount)) { // 预收总额是否大于付款单金额
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款金额与预收单总额不匹配");
		}
		if (DecimalUtil.le(payAmount, DecimalUtil.ZERO) && DecimalUtil.eq(deductionFeeAmount, BigDecimal.ZERO)) { // 不存在抵扣
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款金额必须大于0，不能提交");
		} else if (DecimalUtil.lt(payAmount, DecimalUtil.ZERO)
				&& !DecimalUtil.eq(deductionFeeAmount, BigDecimal.ZERO)) { // 存在抵扣
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款金额必须大于等于0，不能提交");
		}

		if (type == BaseConsts.ONE) {// 订单货款
			if (state == BaseConsts.SIX && payOrder.getNoneOrderFlag() == BaseConsts.ONE) {	//无单据付款
				BigDecimal orderPayAmount = DecimalUtil.add(payOrder.getPoBlance(), payOrder.getDeductionFeeAmount());
				if (DecimalUtil.le(orderPayAmount, BigDecimal.ZERO)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "订单总额必须大于0");
				}
				if (DecimalUtil.lt(payOrder.getPayAmount(), orderPayAmount)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款金额小于订单总额");
				}
			}
		} else if (type == BaseConsts.TWO) {// 费用
			if (state == BaseConsts.SIX && payOrder.getNoneOrderFlag() == BaseConsts.ONE) {	//无单据付款
				BigDecimal feePayAmount = DecimalUtil.add(payOrder.getFeeBlance(), payOrder.getDeductionFeeAmount());
				if (DecimalUtil.le(feePayAmount, BigDecimal.ZERO)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用总额必须大于0");
				}
				if (DecimalUtil.lt(payOrder.getPayAmount(), feePayAmount)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款金额小于费用总额"); 
				}
			}
		} else if (type == BaseConsts.SIX) {// 付退款
			payRefundRelationService.checkSubmitRefund(payOrder);
		}
	}

	/**
	 * 删除
	 *
	 * @param payOrder
	 * @return
	 */
	public BaseResult deletePayOrderById(PayOrder payOrder) {
		BaseResult baseResult = new BaseResult();
		int payId = payOrder.getId();
		payOrder = payOrderDao.queryEntityById(payId);// 锁住表
		deleteRel(payId);
		PmsPoTitleSearchReqDto req = new PmsPoTitleSearchReqDto();
		if (!StringUtils.isEmpty(payOrder.getAttachedNumbe())) {
			req.setPayNo(payOrder.getAttachedNumbe());
			List<PmsPayOrderTitle> pmsPayOrderTitles = pmsPayOrderTitleDao.queryResultsByPayNo(req);
			if (!CollectionUtils.isEmpty(pmsPayOrderTitles)) { // pms请款单
				pmsPayOrderTitleService.deletePmsPurchaseOrder(pmsPayOrderTitles.get(0), payOrder);
				pmsPayOrderTitleService.rejectPmsPayOrder(pmsPayOrderTitles.get(0).getId(), payOrder.getId());
			}
		}

		payOrder.setIsDelete(BaseConsts.ONE);
		payOrderDao.updateById(payOrder);
		return baseResult;
	}

	/**
	 * 删除关联关系
	 *
	 * @param payId
	 *            付款id
	 */
	public void deleteRel(int payId) {
		// 获取所有订单关联数据并删除
		PayPoRelationReqDto payPoRelationSearchReqDto = new PayPoRelationReqDto();
		payPoRelationSearchReqDto.setPayId(payId);
		List<PayPoRelationModel> poReList = payPoRelationService.queryPayPoRelationByCon(payPoRelationSearchReqDto);
		if (poReList != null && poReList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (PayPoRelationModel payPoRelationModel : poReList) {
				ids.add(payPoRelationModel.getId());
			}
			PayPoRelationReqDto payPoRelation = new PayPoRelationReqDto();
			payPoRelation.setIds(ids);
			payPoRelationService.deletePayPoRelation(payPoRelation);
		}
		// 删除所有费用关联数据
		PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
		payFeeRelationReqDto.setPayId(payId);
		List<PayFeeRelationModel> feeRelList = payFeeRelationService.queryPayFeeRelatioByCon(payFeeRelationReqDto);
		if (feeRelList != null && feeRelList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (PayFeeRelationModel feeRelationModel : feeRelList) {
				ids.add(feeRelationModel.getId());
			}
			PayFeeRelationReqDto payFeeRelation = new PayFeeRelationReqDto();
			payFeeRelation.setIds(ids);
			payFeeRelationService.deletePayFeeRelation(payFeeRelation);
		}
		// 删除所有预收单相关
		PayAdvanceRelationReqDto payAdvanceRelatio = new PayAdvanceRelationReqDto();
		payAdvanceRelatio.setPayId(payId);
		List<PayAdvanceRelationResDto> advanceRelList = payAdvanceRelationService
				.queryPayAdvanByPayId(payAdvanceRelatio);
		if (advanceRelList != null && advanceRelList.size() > BaseConsts.ZERO) {
			List<Integer> ids = new ArrayList<Integer>();
			for (PayAdvanceRelationResDto payAdvanceRelationResDto : advanceRelList) {
				ids.add(payAdvanceRelationResDto.getId());
			}
			PayAdvanceRelationReqDto payAdvanRelation = new PayAdvanceRelationReqDto();
			payAdvanRelation.setIds(ids);
			payAdvanRelation.setPayId(payId);
			payAdvanceRelationService.deletePayAdvanRelation(payAdvanRelation);
		}
	}

	/**
	 * 待确认
	 *
	 * @param payOrderSearchReqDto
	 * @return
	 */
	public PageResult<PayOrderResDto> queryPayOrderResults(PayOrderSearchReqDto payOrderSearchReqDto) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		if (!StringUtils.isEmpty(payOrderSearchReqDto.getPayNo())) {
			payOrderSearchReqDto.setPayNoList(Arrays.asList((payOrderSearchReqDto.getPayNo().split(","))));
		}
		int offSet = PageUtil.getOffSet(payOrderSearchReqDto.getPage(), payOrderSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payOrderSearchReqDto.getPer_page());
		payOrderSearchReqDto.setState(BaseConsts.FOUR);
		// payOrderSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PayOrderResDto> payOrderResDto = convertToPayOrderResDtos(
				payOrderDao.queryResultsByCon(payOrderSearchReqDto, rowBounds));
		if (payOrderSearchReqDto.getNeedSum() != null && payOrderSearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<PayOrder> sumResDto = payOrderDao.sumPayOrder(payOrderSearchReqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal payAmountSum = BigDecimal.ZERO;
				for (PayOrder payOrder : sumResDto) {
					if (payOrder != null) {
						payAmountSum = DecimalUtil.add(payAmountSum,
								ServiceSupport.amountNewToRMB(
										payOrder.getPayAmount() == null ? DecimalUtil.ZERO : payOrder.getPayAmount(),
										payOrder.getCurrnecyType(), new Date()));
					}
				}
				String totalStr = "付款金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum))
						+ " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(payOrderResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payOrderSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payOrderSearchReqDto.getPage());
		pageResult.setPer_page(payOrderSearchReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 待开立
	 *
	 * @param payOrderSearchReqDto
	 * @return
	 */
	public PageResult<PayOrderResDto> queryPayOrderOpen(PayOrderSearchReqDto payOrderSearchReqDto) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		if (!StringUtils.isEmpty(payOrderSearchReqDto.getPayNo())) {
			payOrderSearchReqDto.setPayNoList(Arrays.asList((payOrderSearchReqDto.getPayNo().split(","))));
		}
		int offSet = PageUtil.getOffSet(payOrderSearchReqDto.getPage(), payOrderSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payOrderSearchReqDto.getPer_page());
		payOrderSearchReqDto.setState(BaseConsts.FIVE);
		// payOrderSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PayOrderResDto> payOrderResDto = convertToPayOrderResDtos(
				payOrderDao.queryResultsByCon(payOrderSearchReqDto, rowBounds));
		if (payOrderSearchReqDto.getNeedSum() != null && payOrderSearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<PayOrder> sumResDto = payOrderDao.sumPayOrder(payOrderSearchReqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal payAmountSum = BigDecimal.ZERO;
				for (PayOrder payOrder : sumResDto) {
					if (payOrder != null) {
						payAmountSum = DecimalUtil.add(payAmountSum,
								ServiceSupport.amountNewToRMB(
										payOrder.getPayAmount() == null ? DecimalUtil.ZERO : payOrder.getPayAmount(),
										payOrder.getCurrnecyType(), new Date()));
					}
				}
				String totalStr = "付款金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum))
						+ " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(payOrderResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payOrderSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payOrderSearchReqDto.getPage());
		pageResult.setPer_page(payOrderSearchReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 查询付款信息信息列表
	 *
	 * @param payOrderSearchReqDto
	 * @return
	 */
	public PageResult<PayOrderResDto> queryPayOrderResultsByCon(PayOrderSearchReqDto payOrderSearchReqDto) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		if (!StringUtils.isEmpty(payOrderSearchReqDto.getPayNo())) {
			payOrderSearchReqDto.setPayNoList(Arrays.asList((payOrderSearchReqDto.getPayNo().split(","))));
		}
		int offSet = PageUtil.getOffSet(payOrderSearchReqDto.getPage(), payOrderSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payOrderSearchReqDto.getPer_page());
		// payOrderSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.OVER_PAY_ORDER)
				|| !ServiceSupport.isAllowPerm(BusUrlConsts.OVER_PAY_ORDER_OPEN)) {// 判断用户是否拥有操作付款确认及承兑开立权限
			payOrderSearchReqDto.setUserId(ServiceSupport.getUser().getId());
			List<Integer> subjectList = new ArrayList<Integer>();
			subjectList.add(BaseConsts.ONE);
			subjectList.add(BaseConsts.FIVE);
			subjectList.add(BaseConsts.NINE);
			subjectList.add(BaseConsts.INT_13);
			payOrderSearchReqDto.setSubjectList(subjectList);
		}
		List<PayOrderResDto> payOrderResDto = new ArrayList<PayOrderResDto>();
		String source = payOrderSearchReqDto.getWechatSource();// 微信访问标识字段
		if (source != null && source.equalsIgnoreCase("1")) {
			if (payOrderSearchReqDto.getState() == null) {
				payOrderSearchReqDto.setState(BaseConsts.ZERO);// 微信端访问查询待提交状态的付款数据
			}
			payOrderResDto = wechatConvertToPayOrderResDtos(
					payOrderDao.queryResultsByCon(payOrderSearchReqDto, rowBounds));
		} else {
			payOrderResDto = convertToPayOrderResDtos(payOrderDao.queryResultsByCon(payOrderSearchReqDto, rowBounds));
		}
		if (payOrderSearchReqDto.getNeedSum() != null && payOrderSearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<PayOrder> sumResDto = payOrderDao.sumPayOrder(payOrderSearchReqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal payAmountSum = BigDecimal.ZERO;
				for (PayOrder payOrder : sumResDto) {
					if (payOrder != null) {
						payAmountSum = DecimalUtil.add(payAmountSum,
								ServiceSupport.amountNewToRMB(
										payOrder.getPayAmount() == null ? DecimalUtil.ZERO : payOrder.getPayAmount(),
										payOrder.getCurrnecyType(), new Date()));
					}
				}
				String totalStr = "付款金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum))
						+ " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(payOrderResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payOrderSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payOrderSearchReqDto.getPage());
		pageResult.setPer_page(payOrderSearchReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 获取所有数据不分页
	 * 
	 * @param payOrderSearchReqDto
	 * @return
	 */
	public List<PayOrderResDto> queryPayOrderResultsExcel(PayOrderSearchReqDto payOrderSearchReqDto) {
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.OVER_PAY_ORDER)
				|| !ServiceSupport.isAllowPerm(BusUrlConsts.OVER_PAY_ORDER_OPEN)) {// 判断用户是否拥有操作付款确认及承兑开立权限
			if (payOrderSearchReqDto.getUserId() == null) {
				payOrderSearchReqDto.setUserId(ServiceSupport.getUser().getId());
				List<Integer> subjectList = new ArrayList<Integer>();
				subjectList.add(BaseConsts.ONE);
				subjectList.add(BaseConsts.FIVE);
				subjectList.add(BaseConsts.NINE);
				subjectList.add(BaseConsts.INT_13);
				payOrderSearchReqDto.setSubjectList(subjectList);
			}
		}
		if (!StringUtils.isEmpty(payOrderSearchReqDto.getPayNo())) {
			payOrderSearchReqDto.setPayNoList(Arrays.asList((payOrderSearchReqDto.getPayNo().split(","))));
		}
		return convertToPayOrderResDtos(payOrderDao.queryResultsByCon(payOrderSearchReqDto));
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param queryInvoiceReqDto
	 * @return
	 */
	public boolean isOverasyncMaxLine(PayOrderSearchReqDto searchreqDto) {
		if (!ServiceSupport.isAllowPerm(BusUrlConsts.OVER_PAY_ORDER)
				|| !ServiceSupport.isAllowPerm(BusUrlConsts.OVER_PAY_ORDER_OPEN)) {// 判断用户是否拥有操作付款确认及承兑开立权限
			if (searchreqDto.getUserId() == null) {
				searchreqDto.setUserId(ServiceSupport.getUser().getId());
			}
		}
		int count = payOrderDao.isOverasyncMaxLine(searchreqDto);
		//
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("付款单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/pay/pay_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.EIGHT);
			asyncExcelService.addAsyncExcel(searchreqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceApplyExport(PayOrderSearchReqDto searchreqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<PayOrderResDto> payList = queryPayOrderResultsExcel(searchreqDto);
		model.put("payList", payList);
		return model;
	}

	public List<PayOrderResDto> convertToPayOrderResDtos(List<PayOrder> result) {
		List<PayOrderResDto> payOrderQueryResDtos = new ArrayList<PayOrderResDto>();
		if (ListUtil.isEmpty(result)) {
			return payOrderQueryResDtos;
		}
		for (PayOrder payOrder : result) {
			PayOrderResDto payOrderResDto = convertToPayOrderResDto(payOrder);
			List<CodeValue> operList = getOperList(payOrder);
			payOrderResDto.setOpertaList(operList);
			payOrderQueryResDtos.add(payOrderResDto);
		}
		return payOrderQueryResDtos;
	}

	// 微信请求的数据封装
	public List<PayOrderResDto> wechatConvertToPayOrderResDtos(List<PayOrder> result) {
		List<PayOrderResDto> payOrderQueryResDtos = new ArrayList<PayOrderResDto>();
		if (ListUtil.isEmpty(result)) {
			return payOrderQueryResDtos;
		}
		for (PayOrder payOrder : result) {
			PayOrderResDto payOrderResDto = convertToPayOrderResDto(payOrder);
			List<CodeValue> operList = getWechatOperList(payOrder);
			payOrderResDto.setOpertaList(operList);
			payOrderQueryResDtos.add(payOrderResDto);
		}
		return payOrderQueryResDtos;
	}

	/**
	 * 根据采购单ID获取付款ID
	 *
	 * @param poId
	 * @return
	 */
	public Integer getPayIdByPoId(Integer poId) {
		return payOrderDao.queryPayIdByPoId(poId);
	}

	/**
	 * 根据付款ID删除付款与订单关系
	 *
	 * @param payId
	 */
	public void deletePayInfoByPayId(Integer payId) {
		PayOrder payOrder = new PayOrder();
		payOrder.setId(payId);
		payOrder.setIsDelete(BaseConsts.ONE);
		int result = payOrderDao.updateById(payOrder);
		if (result != 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "删除付款信息异常");
		}
		List<PayPoRelation> payPoRelations = payPoRelationDao.queryPayPoListByPayId(payId);
		if (CollectionUtils.isNotEmpty(payPoRelations)) {
			for (PayPoRelation payPoRelation : payPoRelations) {
				payPoRelation.setIsDelete(BaseConsts.ONE);
				result = payPoRelationDao.updateById(payPoRelation);
				if (result != 1) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "删除付款信息异常");
				}
			}
		}
	}

	public PayOrderResDto convertToPayOrderResDto(PayOrder payOrder) {
		PayOrderResDto payOrderResDto = new PayOrderResDto();
		if (null != payOrder) {
			BeanUtils.copyProperties(payOrder, payOrderResDto);
			try {
				if (DateFormatUtils.diffDate(payOrder.getMemoTime(), BaseConsts.DEFAULT_DATE) == BaseConsts.ZERO) {
					payOrderResDto.setMemoTime(null);
				}
			} catch (Exception e) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单水单日期格式转换有误");
			}
			payOrderResDto.setId(payOrder.getId());
			// 经营单位
			payOrderResDto.setBusiUnit(
					cacheService.showSubjectNameByIdAndKey(payOrder.getBusiUnit(), CacheKeyConsts.BUSI_UNIT));
			payOrderResDto.setBusiUnitId(payOrder.getBusiUnit());
			// 项目
			payOrderResDto.setProjectName(cacheService.showProjectNameById(payOrder.getProjectId()));
			payOrderResDto.setSystemTime(new Date());
			BaseProject baseProject = cacheService.getProjectById(payOrder.getProjectId());
			if (null != baseProject) {
				BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
				if (null != busiUnit) {
					payOrderResDto.setBusinessUnitNameValue(busiUnit.getChineseName());
					payOrderResDto.setBusinessUnitAddress(busiUnit.getOfficeAddress());
					payOrderResDto.setBizTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE,
							baseProject.getBizType() + ""));
				}
			}
			// 付款人
			payOrderResDto.setPayerName(
					cacheService.showSubjectNameByIdAndKey(payOrder.getPayer(), CacheKeyConsts.BUSI_UNIT));
			// 付款类型
			payOrderResDto.setPayTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_TYPE, payOrder.getPayType() + ""));
			payOrderResDto.setPayType(payOrder.getPayType());
			// 付款方式
			payOrderResDto.setPayWay(payOrder.getPayWay());
			payOrderResDto.setPayWayName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_WAY, payOrder.getPayWay() + ""));
			if (payOrderResDto.getPayAmount() != null
					&& !DecimalUtil.eq(DecimalUtil.ZERO, payOrderResDto.getPayAmount())) {
				if (payOrder.getPayWayType().equals(BaseConsts.TWO)) {// 付尾款时占用为0
					payOrderResDto.setPayAdvanceAmount(BigDecimal.ZERO);
					payOrderResDto.setPayAdvanceAmountRate(BigDecimal.ZERO);
					payOrderResDto.setPayAdvanceAmountRateName(BigDecimal.ZERO + "%");
				} else {
					payOrderResDto.setPayAdvanceAmount(
							DecimalUtil.subtract(payOrder.getPayAmount(), payOrder.getAdvanceAmount()));
					payOrderResDto.setPayAdvanceAmountRate(payOrderResDto.getPayAdvanceAmount()
							.divide(payOrderResDto.getPayAmount(), 4, BigDecimal.ROUND_HALF_UP));
					payOrderResDto.setPayAdvanceAmountRateName(
							payOrderResDto.getPayAdvanceAmountRate().multiply(new BigDecimal("100")).setScale(2) + "%");
				}
				payOrderResDto.setAdvanceAmountRate(payOrderResDto.getAdvanceAmount()
						.divide(payOrderResDto.getPayAmount(), 4, BigDecimal.ROUND_HALF_UP));
				payOrderResDto.setAdvanceAmountRateName(
						payOrderResDto.getAdvanceAmountRate().multiply(new BigDecimal("100")).setScale(2) + "%");
			}
			// 收款单位
			payOrderResDto
					.setPayeeName(cacheService.getSubjectNcByIdAndKey(payOrder.getPayee(), CacheKeyConsts.CUSTOMER));
			// 收款账户id
			payOrderResDto.setState(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_ORDER_STATE, payOrder.getState() + ""));
			payOrderResDto.setStateInt(payOrder.getState());
			if (payOrder.getPayAccountId() != null) {
				BaseAccount baseAccount = cacheService.getAccountById(payOrder.getPayAccountId());
				if (baseAccount != null) {
					payOrderResDto.setBankName(baseAccount.getBankName());
					payOrderResDto.setSubjectName(baseAccount.getAccountor());
					payOrderResDto.setBankAddress(baseAccount.getBankAddress());
					payOrderResDto.setAccountNo(baseAccount.getAccountNo());
					payOrderResDto.setBankCode(baseAccount.getBankCode());
					payOrderResDto.setPhoneNumber(baseAccount.getPhoneNumber());
					payOrderResDto.setDefaultCurrency(ServiceSupport
							.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, payOrder.getCurrnecyType() + ""));
					payOrderResDto.setIban(baseAccount.getIban());
				}
			}
			if (payOrder.getPaymentAccount() != null) {
				BaseAccount baseAccount = cacheService.getAccountById(payOrder.getPaymentAccount());
				if (baseAccount != null) {
					payOrderResDto.setPaymentAccountName(baseAccount.getAccountNo());
				}
			}

			payOrderResDto.setCurrnecyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					payOrder.getCurrnecyType() + ""));
			payOrderResDto.setRealCurrencyTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
					payOrder.getRealCurrencyType() + ""));

			PayPoRelationReqDto payPoRelationReqDto = new PayPoRelationReqDto();
			payPoRelationReqDto.setPayId(payOrder.getId());
			List<PayPoRelationResDto> payPoRelationResDto = payPoRelationService
					.queryPayPoRelationAuditByCon(payPoRelationReqDto);
			BigDecimal sumSendPrice = BigDecimal.ZERO;// 计算总销售价
			if (payPoRelationResDto != null) {
				for (int i = 0; i < payPoRelationResDto.size(); i++) {
					BigDecimal sendPrice = payPoRelationResDto.get(i).getRequiredSendPrice();
					BigDecimal num = payPoRelationResDto.get(i).getGoodsNum();
					BigDecimal count = DecimalUtil.multiply(null == num ? BigDecimal.ZERO : num,
							null == sendPrice ? BigDecimal.ZERO : sendPrice);
					sumSendPrice = DecimalUtil.add(sumSendPrice, count);
				}
			}
			payOrderResDto.setSumSendPrice(sumSendPrice);
			BigDecimal sumProfit = BigDecimal.ZERO;
			if (DecimalUtil.gt(sumSendPrice, BigDecimal.ZERO)) {// 计算总利润率
				BigDecimal count = DecimalUtil.subtract(sumSendPrice, payOrder.getPayAmount());
				sumProfit = DecimalUtil.divide(count, sumSendPrice);

			}

			BigDecimal profit = BigDecimal.ZERO;
			if (DecimalUtil.gt(sumSendPrice, payOrder.getPayAmount())) {
				profit = DecimalUtil.subtract(sumSendPrice, payOrder.getPayAmount());
			}
			payOrderResDto.setProfit(profit);
			payOrderResDto.setSumProfit(DecimalUtil.toPercentString(sumProfit));
			payOrderResDto.setDiscountAmount(payOrder.getDiscountAmount());
			payOrderResDto.setInDiscountAmount(payOrder.getInDiscountAmount());
			if (DecimalUtil.gt(payOrderResDto.getInDiscountAmount(), DecimalUtil.ZERO)) {
				payOrderResDto.setDiscountRate(
						DecimalUtil.divide(payOrderResDto.getDiscountAmount(), payOrderResDto.getInDiscountAmount()));
				payOrderResDto.setDiscountRateStr(DecimalUtil.toPercentString(payOrderResDto.getDiscountRate()));
			}
			payOrderResDto.setPayWayType(payOrder.getPayWayType());
			payOrderResDto.setPayWayTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAY_WAY_TYPE, payOrder.getPayWayType() + ""));
			payOrderResDto.setNoneOrderFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.YES_NO, payOrder.getNoneOrderFlag() + ""));
			payOrderResDto.setWriteOffFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.WRITE_OFF_FLAG, payOrder.getWriteOffFlag() + ""));
			payOrderResDto.setCheckAmount(payOrder.getCheckAmount());
		}
		return payOrderResDto;
	}

	/**
	 * 获取操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(PayOrder payOrder) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(payOrder);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PayOrderResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 微信 获取操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<CodeValue> getWechatOperList(PayOrder payOrder) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getWechatOperListByState(payOrder);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PayOrderResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(PayOrder payOrder) {
		List<String> opertaList = Lists.newArrayList();
		if (payOrder.getState() == null) {
			return opertaList;
		}
		switch (payOrder.getState()) {
		// 状态 0 待提交 1待业务审核 2待财务审核 3待风控审核 4待确认 5待开立 6已完成
		case BaseConsts.ZERO:
			if (StringUtils.isEmpty(payOrder.getMergePayNo())) {
				opertaList.add(OperateConsts.DELETE);
				opertaList.add(OperateConsts.EDIT);
				opertaList.add(OperateConsts.SUBMIT);
			}
			opertaList.add(OperateConsts.DETAIL);
			break;
		case BaseConsts.SIX:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			BigDecimal leftCheckAmount = DecimalUtil.subtract(null == payOrder.getPayAmount() ? BigDecimal.ZERO : payOrder.getPayAmount(), 
					null == payOrder.getCheckAmount() ? BigDecimal.ZERO : payOrder.getCheckAmount());
			if (payOrder.getNoneOrderFlag() == BaseConsts.ONE 
					&& DecimalUtil.gt(leftCheckAmount, BigDecimal.ZERO)) {	//已完成，且无单据付款，且剩余核销金额大于0，需核销付款单
				opertaList.add(OperateConsts.CHECK);
			}
			break;
		default:
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.PRINT);
			break;
		}
		return opertaList;
	}

	/**
	 * 微信访问 根据状态得到操作列表
	 *
	 * @param state
	 * @return
	 */
	private List<String> getWechatOperListByState(PayOrder payOrder) {
		List<String> opertaList = Lists.newArrayList();
		if (payOrder.getState() == null) {
			return opertaList;
		}
		// 默认为提交和浏览
		opertaList.add(OperateConsts.DETAIL);
		opertaList.add(OperateConsts.SUBMIT);
		return opertaList;
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<PayOrderFileResDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<PayOrderFileResDto> pageResult = new PageResult<PayOrderFileResDto>();
		fileAttReqDto.setBusType(BaseConsts.EIGHT);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<PayOrderFileResDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<PayOrderFileResDto> queryFileListAll(FileAttachSearchReqDto fileAttReqDto) {
		fileAttReqDto.setBusType(BaseConsts.EIGHT);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<PayOrderFileResDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<PayOrderFileResDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<PayOrderFileResDto> list = new LinkedList<PayOrderFileResDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			PayOrderFileResDto result = new PayOrderFileResDto();
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
				PayOrderFileResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DOWNLOAD);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}

	public PayOrderResDto queryDefaultRealPayAmount(PayOrderBatchConfirmReq payOrderBatchConfirmReq) {
		Integer accountId = payOrderBatchConfirmReq.getPaymentAccount();
		List<Integer> ids = payOrderBatchConfirmReq.getIds();
		if (CollectionUtils.isEmpty(ids)) {
			return null;
		}
		PayOrderResDto payOrderResDto = new PayOrderResDto();
		BaseAccount baseAccount = cacheService.getAccountById(accountId);
		if (StringUtils.isEmpty(baseAccount)) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, BaseAccount.class, accountId);
		}
		Integer payId = ids.get(0);
		PayOrder payOrder = queryEntityById(payId);
		BigDecimal payAmountSum = payOrderDao.querySumByIds(ids);
		if (StringUtils.isEmpty(payOrder)) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, PayOrder.class, payId);
		}
		Integer accCurrencyType = baseAccount.getDefaultCurrency();
		Integer payCurrencyType = payOrder.getCurrnecyType();
		BigDecimal realPayAmount = null;
		BigDecimal payRate = null;
		/**
		 * 付款账户币种与付款单币种相同，实际付款默认值返回付款金额
		 */
		if (accCurrencyType.equals(payCurrencyType)) {
			payRate = new BigDecimal("1");
			realPayAmount = payAmountSum;
		} else {
			BaseProject baseProject = cacheService.getProjectById(payOrder.getProjectId());
			if (!payOrder.getPayType().equals(BaseConsts.THREE)) { // 付借款类型不走条款
				ProjectItem projectItem = projectItemService.getProjectItemByProjectId(payOrder.getProjectId());
				if (projectItem == null) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "付款单【" + payOrder.getPayNo() + "】关联项目下无条款");
				}
				if (projectItem.getAccountRateType().equals(BaseConsts.ONE)) { // 1：结算日汇率
																				// 2：固定汇率
					payRate = baseExchangeRateService
							.convertCurrency(String.valueOf(projectItem.getBankId()), String.valueOf(accCurrencyType),
									String.valueOf(payCurrencyType), new Date(), projectItem.getAccountMethod() + "")
							.setScale(8, BigDecimal.ROUND_HALF_UP);
					realPayAmount = DecimalUtil.formatScale2(payAmountSum.multiply(payRate));
				} else {
					if (payCurrencyType.equals(baseProject.getAmountUnit())
							&& accCurrencyType.equals(projectItem.getPaypalAmountCurrency())) {
						payRate = new BigDecimal("1").divide(projectItem.getAccountRate(), 4, BigDecimal.ROUND_HALF_UP);
						realPayAmount = payAmountSum.divide(projectItem.getAccountRate(), 2, BigDecimal.ROUND_HALF_UP);
					} else if (payCurrencyType.equals(projectItem.getPaypalAmountCurrency())
							&& accCurrencyType.equals(baseProject.getAmountUnit())) {
						payRate = projectItem.getAccountRate();
						realPayAmount = DecimalUtil.formatScale2(payAmountSum.multiply(projectItem.getAccountRate()));
					} else {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该条款不支持" + ServiceSupport
								.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, accCurrencyType + "") + "账户");
					}
				}
			} else {
				payRate = baseExchangeRateService.convertCurrency(String.valueOf(BaseConsts.TWO),
						String.valueOf(accCurrencyType), String.valueOf(payCurrencyType), new Date())
						.setScale(8, BigDecimal.ROUND_HALF_UP);
				realPayAmount = DecimalUtil.formatScale2(payAmountSum.multiply(payRate));
			}

		}
		payOrderResDto.setPayRate(payRate);
		payOrderResDto.setRealCurrencyType(accCurrencyType);
		payOrderResDto.setRealCurrencyTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, accCurrencyType + ""));
		payOrderResDto.setRealPayAmount(realPayAmount);
		return payOrderResDto;
	}

	public PayOrderBatchConfirmResp queryBatchConfirmPayOrder(PayOrderBatchConfirmReq payOrderBatchConfirmReq) {
		PayOrderBatchConfirmResp payOrderBatchConfirmResp = new PayOrderBatchConfirmResp();
		List<PayOrder> payOrders = payOrderDao.queryListByIds(payOrderBatchConfirmReq.getIds());
		List<PayOrderResDto> payOrderResDto = convertToPayOrderResDtos(payOrders);
		BigDecimal sumPayAmount = payOrderDao.querySumByIds(payOrderBatchConfirmReq.getIds());
		payOrderBatchConfirmResp.setSumPayAmount(sumPayAmount);
		payOrderBatchConfirmResp.setPayOrderResDto(payOrderResDto);
		return payOrderBatchConfirmResp;
	}

	/**
	 * 更新打印次数 +1 TODO.
	 *
	 * @param id
	 */
	public void updatePrintNum(Integer id) {
		PayOrder payOrder = queryEntityById(id);

		PayOrder payOrderUpd = new PayOrder();
		payOrderUpd.setId(id);
		payOrderUpd.setPrintNum(payOrder.getPrintNum() + 1);

		updatePayOrderById(payOrderUpd);
	}

	/**
	 * 批量更新打印次数 +1 TODO.
	 *
	 * @param id
	 */
	public void batchUpdatePrintNum(String ids) {
		List<String> list = Arrays.asList(ids.split(","));
		if (!CollectionUtils.isEmpty(list)) {
			for (String str : list) {
				Integer id = Integer.valueOf(str);
				PayOrder payOrder = queryEntityById(id);

				PayOrder payOrderUpd = new PayOrder();
				payOrderUpd.setId(id);
				payOrderUpd.setPrintNum(payOrder.getPrintNum() + 1);

				updatePayOrderById(payOrderUpd);
			}
		}
	}

	/**
	 * 批量打印预处理 TODO.
	 *
	 * @param payOrderSearchReqDto
	 */
	public String preBatchPrint(PayOrderSearchReqDto payOrderSearchReqDto) {
		List<Integer> ids = payOrderSearchReqDto.getIds();
		String unionPrintIdentifier = sequenceService.getNumIncByBusName("", SeqConsts.S_UNION_PRINT_IDENTIFIER,
				BaseConsts.SIX); // 生成统一打印标示符
		if (!CollectionUtils.isEmpty(ids)) {
			for (Integer id : ids) {
				PayOrder payOrderUpd = new PayOrder();
				payOrderUpd.setId(id);
				payOrderUpd.setUnionPrintIdentifier(unionPrintIdentifier);
				updatePayOrderById(payOrderUpd);
			}
		}
		return unionPrintIdentifier;
	}

	public List<PayOrderResDto> queryExportPayOrderResults(PayOrderSearchReqDto payOrderSearchReqDto) {
		payOrderSearchReqDto.setState(BaseConsts.FOUR);
		// payOrderSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PayOrderResDto> payOrderResDto = convertToPayOrderResDtos(
				payOrderDao.queryResultsByCon(payOrderSearchReqDto));
		for (PayOrderResDto pay : payOrderResDto) {
			pay.setRealPayAmount(null);
			pay.setBankCharge(BigDecimal.ZERO);
		}
		return payOrderResDto;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
	public void importExcel(MultipartFile importFile) {
		List<PayOrderResDto> payList = Lists.newArrayList();
		Map beans = Maps.newHashMap();
		beans.put("payEnsureList", payList);
		ExcelService.resolverExcel(importFile, "/excel/pay/payEnsure.xml", beans);
		// 业务逻辑处理
		payList = (List<PayOrderResDto>) beans.get("payEnsureList");
		if (CollectionUtils.isNotEmpty(payList)) {
			if (payList.size() > BaseConsts.IMPORT_EXCEL_ROWS_MAX) {
				throw new BaseException(ExcMsgEnum.IMPORT_EXCEL_ROWS_MAX);
			}
			List<CodeValue> goodChinaRateList = ServiceSupport.getCvListByBizCode(BizCodeConsts.GOODS_CHINA_RATE);
			int row = 1;
			StringBuilder errorCode = new StringBuilder();
			for (PayOrderResDto payOrderResDto : payList) {
				errorCode.append(validatePayEnsure(payOrderResDto, row++));
			}
			if (errorCode.length() > 0) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, errorCode);
			}
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "导入商品不能为空");
		}
	}

	private String validatePayEnsure(PayOrderResDto payOrderResDto, int row) {
		// TODO Auto-generated method stub
		StringBuilder errorCode = new StringBuilder();
		PayOrder payOrder = new PayOrder();
		String payNo = payOrderResDto.getPayNo();
		if (StringUtils.isEmpty(payNo)) {
			errorCode.append("第" + row + "行付款编号不能为空;");
		} else if (StringUtils.isEmpty(payOrderResDto.getPaymentAccountName())) {
			errorCode.append("第" + row + "行付款账户不能为空;");
		} else if (StringUtils.isEmpty(payOrderResDto.getConfirmorAt())) {
			errorCode.append("第" + row + "行确认日期不能为空;");
		} else if (StringUtils.isEmpty(payOrderResDto.getRealPayAmount())) {
			errorCode.append("第" + row + "行实际付款金额不能为空;");
		} else {
			payOrder = payOrderDao.queryEntityByPayNo(payNo);
			payOrderResDto.setPayer(payOrder.getPayer());
			String payCountNo = payOrderResDto.getPaymentAccountName();
			if (payCountNo.length() < 4) {
				errorCode.append("第" + row + "行尾号为" + payCountNo + "的账号不合法,请输入账号后四位;");
			} else {
				payCountNo = payCountNo.substring(payCountNo.length() - 4, payCountNo.length());
				payOrderResDto.setPaymentAccountName(payCountNo);
				Integer accountId = payOrderDao.queryAccounIdByCon(payOrderResDto);
				payOrder.setPaymentAccount(accountId);
				payOrder.setConfirmorAt(dateChange(payOrderResDto.getConfirmorAt()));
				payOrder.setMemoTime(dateChange(payOrderResDto.getConfirmorAt()));
				payOrder.setBankCharge(payOrderResDto.getBankCharge());
				payOrder.setRealPayAmount(payOrderResDto.getRealPayAmount());
				if (null == accountId) {
					errorCode.append("第" + row + "行尾号为" + payCountNo + "账号信息不存在;");
				} else {
					payOrder.setRealCurrencyType(cacheService.getAccountById(accountId).getDefaultCurrency());
					if (payOrder.getRealCurrencyType().equals(payOrder.getCurrnecyType())) {
						if (payOrder.getRealPayAmount().compareTo(payOrder.getPayAmount()) != 0) {
							errorCode.append("第" + row + "行付款金额与实际付款金额不相等;");
						}
					}
					if (null != payOrder.getPayAmount()) {
						payOrder.setPayRate(
								DecimalUtil.divide(payOrderResDto.getRealPayAmount(), payOrder.getPayAmount()));
						submitPayOver(payOrder);
					} else {
						errorCode.append("第" + row + "行付款金额不能为空;");
					}
				}
			}
		}
		return errorCode.toString();
	}

	private Date dateChange(Date date) {
		try {
			DateFormat df1 = DateFormat.getDateInstance();
			Date currDate = df1.parse(df1.format(date));
			currDate.setTime(currDate.getTime() + 12 * 60 * 60 * 1000);
			return currDate;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 付款确认 驳回 业务操作 业务需求：驳回待确认的付款单的信息，设计单体和合并的付款单 1.1 合并付款项需要业务删除其明细信息和状态变为待提交状态
	 * 1.2 付款单 执行更改其状态为待提交状态
	 * 
	 * @param order
	 */
	public void rejectPayOrderById(PayOrder order) {
		// 根据Id查询当前实体数据
		PayOrder payResult = payOrderDao.queryEntityById(order.getId());
		// 根据mergePayNo(付款账号)判定
		if (null != payResult) {
			if (!StringUtils.isEmpty(payResult.getMergePayNo())) {
				// 合并付款编号不为空，进行合并付款明细业务删除
				mergePayOrderService.deleteMergePayOderById(payResult);
				// 修改合并付款单的合并金额
				mergePayOrderService.updateMergeOrderAmount(payResult);
				// 清空当前付款单的合并付款单的编号
				payResult.setMergePayNo(BaseConsts.MINUSONE);
			}
			// 明细删除或者是直接驳回，都需要改动状态
			payResult.setState(BaseConsts.ZERO);// 待提交
			if (org.apache.commons.lang3.StringUtils.isNotBlank(order.getReason())) {
				payResult.setReason(order.getReason());
			}
			if (org.apache.commons.lang3.StringUtils.isNotBlank(order.getCmsRejecter())) {
				payResult.setCmsRejecter(order.getCmsRejecter());
			}
			// 修改付款单到待提交状态
			payOrderDao.updateById(payResult);
			// 驳回 增加一条审核的数据
			payAuditService.createRejectAudit(payResult);
		}
	}

	/**
	 * 批量提交审核
	 * 
	 * @param payOrder
	 */
	public void submitBatchPayOrderById(PayOrder payOrder, boolean type) {
		List<Integer> ids = payOrder.getIds();
		if (CollectionUtils.isNotEmpty(ids)) {
			for (Integer payId : ids) {
				PayOrder payResult = payOrderDao.queryEntityById(payId);
				this.submitPayOrderById(payResult, type);
			}
		}
	}

	public void refreshFundUsed(PayOrderSearchReqDto req) {
		PayOrderSearchReqDto payOrderSearchReqDto = new PayOrderSearchReqDto();
		// payOrderSearchReqDto.setState(BaseConsts.SIX);
		payOrderSearchReqDto.setPayNo(req.getPayNo());
		List<PayOrder> ds = payOrderDao.queryResultsByCon(payOrderSearchReqDto);
		for (PayOrder p : ds) {
			updateFundUsed(p);
		}
	}

	/**
	 * 已完成
	 *
	 * @param payOrderSearchReqDto
	 * @return
	 */
	public PageResult<PayOrderResDto> queryOverPayOrderResults(PayOrderSearchReqDto payOrderSearchReqDto) {
		PageResult<PayOrderResDto> pageResult = new PageResult<PayOrderResDto>();
		if (!StringUtils.isEmpty(payOrderSearchReqDto.getPayNo())) {
			payOrderSearchReqDto.setPayNoList(Arrays.asList((payOrderSearchReqDto.getPayNo().split(","))));
		}
		int offSet = PageUtil.getOffSet(payOrderSearchReqDto.getPage(), payOrderSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, payOrderSearchReqDto.getPer_page());
		payOrderSearchReqDto.setState(BaseConsts.SIX);
		// payOrderSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<PayOrderResDto> payOrderResDto = convertToPayOrderResDtos(
				payOrderDao.queryResultsByCon(payOrderSearchReqDto, rowBounds));
		if (payOrderSearchReqDto.getNeedSum() != null && payOrderSearchReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<PayOrder> sumResDto = payOrderDao.sumPayOrder(payOrderSearchReqDto);
			if (CollectionUtils.isNotEmpty(sumResDto)) {
				BigDecimal payAmountSum = BigDecimal.ZERO;
				for (PayOrder payOrder : sumResDto) {
					if (payOrder != null) {
						payAmountSum = DecimalUtil.add(payAmountSum,
								ServiceSupport.amountNewToRMB(
										payOrder.getPayAmount() == null ? DecimalUtil.ZERO : payOrder.getPayAmount(),
										payOrder.getCurrnecyType(), new Date()));
					}
				}
				String totalStr = "付款金额  : " + DecimalUtil.toAmountString(DecimalUtil.formatScale2(payAmountSum))
						+ " CNY";
				pageResult.setTotalStr(totalStr);
			}
		}
		pageResult.setItems(payOrderResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), payOrderSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(payOrderSearchReqDto.getPage());
		pageResult.setPer_page(payOrderSearchReqDto.getPer_page());
		return pageResult;
	}

	/**
	 * 修改付款水单
	 * 
	 * @param payOrder
	 * @return
	 */
	public BaseResult updateMemoPayOrderById(PayOrder payOrder) {
		BaseResult baseResult = new BaseResult();
		int result = payOrderDao.updateById(payOrder);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新付款失败，请重试");
		}
		updatePayTime(payOrder);
		return baseResult;
	}

	/**
	 * 修改采购订单明细,入库单明细,库存的pay_time=memoTime payOrder的水单日期
	 * 
	 * @param payOrder
	 */
	public void updatePayTime(PayOrder payOrder) {
		PayPoRelationReqDto payPoRelationReqDto = new PayPoRelationReqDto();
		payPoRelationReqDto.setPayId(payOrder.getId());
		List<PayPoRelationModel> payPoRelationModels = payPoRelationService
				.queryPayPoRelationByCon(payPoRelationReqDto);
		List<PurchaseOrderLine> purchaseOrderLines = new ArrayList<PurchaseOrderLine>();
		if (!CollectionUtils.isEmpty(payPoRelationModels)) {
			for (int index = 0; index < payPoRelationModels.size(); index++) {
				PayPoRelationModel item = payPoRelationModels.get(index);
				PurchaseOrderLine poLineEntity = purchaseOrderService.queryPoLineEntityById(item.getPoLineId());
				PurchaseOrderLine poLineUpd = new PurchaseOrderLine();
				poLineUpd.setId(poLineEntity.getId());
				poLineUpd.setPoId(poLineEntity.getPoId());
				poLineUpd.setPayTime(payOrder.getMemoTime());
				purchaseOrderService.updatePoLinesById(poLineUpd);
				purchaseOrderLines.add(poLineUpd);
			}
		}
		if (!CollectionUtils.isEmpty(purchaseOrderLines)) {
			for (PurchaseOrderLine item : purchaseOrderLines) {
				List<BillInStoreDtl> billInStoreDtls = billInStoreDtlDao.queryResultsByPoLineId(item.getId());
				if (!CollectionUtils.isEmpty(billInStoreDtls)) {
					// 修改采购订单明细,入库单明细,库存的pay_time
					for (BillInStoreDtl billInStoreDtl : billInStoreDtls) {
						BillInStoreDtl entity = billInStoreDtlDao.queryEntityById(billInStoreDtl.getId()); // 加锁
						if (entity == null) {
							throw new BaseException(ExcMsgEnum.ERROR_GENERAL, BillInStoreDtlDao.class,
									billInStoreDtl.getId());
						}
						BillInStoreDtl billInStoreDtlUpd = new BillInStoreDtl();
						billInStoreDtlUpd.setId(entity.getId());
						billInStoreDtlUpd.setPayTime(item.getPayTime());
						billInStoreDtlDao.updateById(billInStoreDtlUpd);
					}
					List<Stl> stls = stlDao.queryResultsByPoLineId(item.getId());
					if (!CollectionUtils.isEmpty(stls)) {
						for (Stl stl : stls) {
							Stl entity = stlDao.queryEntityById(stl.getId());
							if (entity == null) {
								throw new BaseException(ExcMsgEnum.ERROR_GENERAL, StlDao.class, stl.getId());
							}
							Stl stlUpd = new Stl();
							stlUpd.setId(entity.getId());
							stlUpd.setPayTime(item.getPayTime());
							stlDao.updateById(stlUpd);
						}
					}
				}
			}
		}
	}
	
	public boolean isFinishedPay(PurchaseOrderTitle purchaseOrderTitle) {
		boolean isFinishedPay = false;
		int poLineCount = purchaseOrderService.countPoLineListByPoId(purchaseOrderTitle.getId());
		int payPoLineCount = payPoRelationDao.queryFinishedCountPayPoByPoId(purchaseOrderTitle.getId());
		if (poLineCount == payPoLineCount) {
			isFinishedPay = true;
		}
		return isFinishedPay;
	}
}
