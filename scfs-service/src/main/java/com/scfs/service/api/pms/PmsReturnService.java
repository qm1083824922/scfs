package com.scfs.service.api.pms;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.base.entity.BaseAddressDao;
import com.scfs.dao.base.entity.DistributionGoodsDao;
import com.scfs.dao.interf.PMSSupplierBindDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.dao.project.ProjectGoodsDao;
import com.scfs.dao.project.ProjectSubjectDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsReturnHttpResDto;
import com.scfs.domain.api.pms.entity.PmsReturn;
import com.scfs.domain.api.pms.entity.PmsReturnDtl;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseAddress;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.project.dto.req.ProjectGoodsSearchReqDto;
import com.scfs.domain.project.dto.req.ProjectSubjectSearchReqDto;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.audit.DistributionReturnAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2017年6月22日.
 */
@Service
public class PmsReturnService {
	private final static Logger LOGGER = LoggerFactory.getLogger(PmsReturnService.class);

	@Autowired
	private InvokeConfig invokeConfig;
	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private PMSSupplierBindDao pmsSupplierBindDao;
	@Autowired
	private ProjectGoodsDao projectGoodsDao;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ProjectSubjectDao projectSubjectDao;
	@Autowired
	private DistributionGoodsDao distributionGoodsDao;
	@Autowired
	private BaseAddressDao baseAddressDao;
	@Autowired
	private DistributionReturnAuditService distributionReturnAuditService;
	@Autowired
	private AuditFlowService auditFlowService;

	@IgnoreTransactionalMark
	public PmsReturnHttpResDto doPmsReturn(PmsHttpReqDto req) {
		PmsReturnHttpResDto res = new PmsReturnHttpResDto();
		res.setFlag(BaseConsts.FLAG_YES);
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);
		invokeLog.setCreateAt(new Date());
		invokeLog.setConsumer(BaseConsts.THREE);
		invokeLog.setProvider(BaseConsts.ONE);
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PAY_RETURN.getType());
		invokeLog.setInvokeMode(BaseConsts.TWO);
		invokeLogService.add(invokeLog);
		if (StringUtils.isBlank(sign) || StringUtils.isBlank(data)) {
			res.setFlag(BaseConsts.FLAG_NO);
			res.setFlag("请求参数不正确，key且data不能为空");
			return res;
		}

		PmsReturn pmsReturn = null;
		try {
			pmsReturn = JSON.parseObject(data, PmsReturn.class);
		} catch (Exception e) {
			LOGGER.error("[pms]pms退货单申请接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
			res.setMsg("[pms]pms退货单申请接口请求失败");
			invokeLog.setReturnMsg(JSON.toJSONString(res));
			invokeLogService.invokeException(invokeLog, e);
		}
		if (null != pmsReturn) {
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					res.setMsg("请求非法: 签名校验出错");
					res.setRefund_order_sn(pmsReturn.getRefund_order_sn());
					invokeLog.setReturnMsg(JSON.toJSONString(res));
					invokeLogService.invokeError(invokeLog);
					return res;
				}
			}
			try {
				checkData(pmsReturn); // 接口前置检查

				res.setRefund_order_sn(pmsReturn.getRefund_order_sn());
				invokeLog.setBillNo(pmsReturn.getRefund_order_sn());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeSuccess(invokeLog);
				dealPmsReturn(pmsReturn);
			} catch (BaseException e) {
				LOGGER.error("[pms]pms退货单申请接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
				res.setMsg(commonService.getMsg(e.getMsg()));
				res.setRefund_order_sn(pmsReturn.getRefund_order_sn());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
			} catch (Exception e) {
				LOGGER.error("[pms]pms退货单申请接口请求失败[{}]: {}", JSONObject.toJSON(req), e);
				res.setMsg(commonService.getMsg(e.getMessage()));
				res.setRefund_order_sn(pmsReturn.getRefund_order_sn());
				invokeLog.setReturnMsg(JSON.toJSONString(res));
				invokeLogService.invokeError(invokeLog);
			}
		}
		return res;
	}

	private void checkData(PmsReturn pmsReturn) {
		if (pmsReturn == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数格式有误");
		}
		if (StringUtils.isEmpty(pmsReturn.getProvider_sn())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 供应商编号不能为空");
		}
		PMSSupplierBind pmsSupplierBind = pmsSupplierBindDao.queryEntityBySupplierNo(pmsReturn.getProvider_sn());
		if (null == pmsSupplierBind) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "该供应商【" + pmsReturn.getProvider_sn() + "】无质押项目");
		}
		if (StringUtils.isEmpty(pmsReturn.getRefund_order_sn())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 退货单号不能为空");
		}
		if (null == pmsReturn.getSubmit_time()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 退货日期不能为空");
		}
		if (null == pmsReturn.getDivision_code()) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 事业部不能为空");
		}
		if (CollectionUtils.isEmpty(pmsReturn.getDetails())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 退货单明细不能为空");
		}
		for (PmsReturnDtl pmsReturnDtl : pmsReturn.getDetails()) {
			if (null == pmsReturnDtl.getSku()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请求参数有误: 商品SKU不能为空");
			}
			if (null == pmsReturnDtl.getRefund_quantity()
					|| DecimalUtil.eq(pmsReturnDtl.getRefund_quantity(), BigDecimal.ZERO)) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"请求参数有误: 商品SKU【" + pmsReturnDtl.getSku() + "】退货数量不能为空或0");
			}
			ProjectGoodsSearchReqDto projectGoodsSearchReqDto = new ProjectGoodsSearchReqDto();
			projectGoodsSearchReqDto.setProjectId(pmsSupplierBind.getProjectId());
			projectGoodsSearchReqDto.setNumber(pmsReturnDtl.getSku());
			// 查询对应的商品信息
			ProjectGoods projectGoods = projectGoodsDao.queryByProjectIdAndGoodsNo(projectGoodsSearchReqDto);
			if (null == projectGoods || null == projectGoods.getGoodsId()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
						"请求参数有误: 该供应商【" + pmsReturn.getProvider_sn() + "】下无商品【 " + pmsReturnDtl.getSku() + "】");
			}
		}
	}

	public void dealPmsReturn(PmsReturn pmsReturn) {
		// 通过pms供应商查询对应的项目与供应商
		PMSSupplierBind pmsSupplierBind = pmsSupplierBindDao.queryEntityBySupplierNo(pmsReturn.getProvider_sn());
		PurchaseOrderTitle purchaseOrderTitle = createPurchaseOrderTitle(pmsSupplierBind,
				pmsReturn.getRefund_order_sn(), pmsReturn.getProvider_sn(), pmsReturn.getSubmit_time(),
				BaseConsts.THREE, null);
		Integer currencyId = null;
		List<PmsReturnDtl> pmsReturnList = pmsReturn.getDetails();
		List<PurchaseOrderLine> purchaseOrderLineList = Lists.newArrayList();
		BigDecimal totalNum = BigDecimal.ZERO;
		Map<String, PmsReturnDtl> map = new HashMap<String, PmsReturnDtl>();
		List<PmsReturnDtl> meragePmsReturnList = Lists.newArrayList();
		for (PmsReturnDtl pmsReturnDtl : pmsReturnList) { // 合并sku
			String sku = pmsReturnDtl.getSku();
			if (map.containsKey(sku)) {
				PmsReturnDtl dtl = map.get(sku);
				dtl.setRefund_quantity(DecimalUtil.add(dtl.getRefund_quantity(), pmsReturnDtl.getRefund_quantity()));
				map.put(sku, dtl);
			} else {
				map.put(sku, pmsReturnDtl);
			}
			totalNum = DecimalUtil.add(totalNum, pmsReturnDtl.getRefund_quantity());
		}
		if (map.size() != pmsReturnList.size()) { // sku合并后的条数跟合并前不一样，则取合并后的数据
			for (Entry<String, PmsReturnDtl> entry : map.entrySet()) {
				PmsReturnDtl dtl = entry.getValue();
				meragePmsReturnList.add(dtl);
			}
		} else {
			meragePmsReturnList = pmsReturnList;
		}
		for (PmsReturnDtl pmsReturnDtl : meragePmsReturnList) {
			String sku = pmsReturnDtl.getSku();
			BigDecimal refund_quantity = pmsReturnDtl.getRefund_quantity();
			DistributionGoods distributionGoods = distributionGoodsDao.queryDistributionGoodsByNumber(sku);
			PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
			poTitleReqDto.setGoodsId(distributionGoods.getId());
			poTitleReqDto.setSupplierId(pmsSupplierBind.getSupplierId());
			poTitleReqDto.setProjectId(pmsSupplierBind.getProjectId());
			List<PoLineModel> poLineModelList = purchaseOrderLineDao
					.queryDistributionLinesByGoodsIdAndSupplierId(poTitleReqDto);
			if (!CollectionUtils.isEmpty(poLineModelList)) {
				BigDecimal totalMatchNum = BigDecimal.ZERO;
				BigDecimal remainNum = refund_quantity;
				for (PoLineModel poLineModel : poLineModelList) {
					currencyId = poLineModel.getCurrencyId();
					BigDecimal matchNum = BigDecimal.ZERO;
					if (DecimalUtil.ge(remainNum, poLineModel.getRemainSendNum())) {
						matchNum = poLineModel.getRemainSendNum();
					} else {
						matchNum = remainNum;
					}
					PurchaseOrderLine purchaseOrderLine = createPurchaseOrderLine(poLineModel, matchNum);
					purchaseOrderLineList.add(purchaseOrderLine);
					totalMatchNum = DecimalUtil.add(totalMatchNum, matchNum);
					remainNum = DecimalUtil.subtract(remainNum, matchNum); // 剩余未匹配数量
					if (DecimalUtil.eq(remainNum, BigDecimal.ZERO)) {
						break;
					}
				}
				if (!DecimalUtil.eq(totalMatchNum, refund_quantity)) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货商品【" + sku + "】可退货数量不足");
				}
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货商品【" + sku + "】可退货数量不足");
			}
		}
		if (!CollectionUtils.isEmpty(purchaseOrderLineList)) {
			AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_22, null);
			if (null == startAuditNode) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
			}
			purchaseOrderTitle.setCurrencyId(currencyId);
			purchaseOrderTitle.setState(startAuditNode.getAuditNodeState()); // 商务审核

			// 创建一条铺货订单信息
			purchaseOrderTitleDao.insert(purchaseOrderTitle);
			BigDecimal orderTotalNum = BigDecimal.ZERO;
			BigDecimal orderTotalAmount = BigDecimal.ZERO;
			BigDecimal totalDiscountAmount = BigDecimal.ZERO;
			for (PurchaseOrderLine purchaseOrderLine : purchaseOrderLineList) {
				purchaseOrderLine.setPoId(purchaseOrderTitle.getId());
				purchaseOrderLineDao.insert(purchaseOrderLine);
				orderTotalNum = DecimalUtil.add(orderTotalNum, purchaseOrderLine.getGoodsNum());
				orderTotalAmount = DecimalUtil.add(orderTotalAmount, purchaseOrderLine.getAmount());
				totalDiscountAmount = DecimalUtil.add(totalDiscountAmount, purchaseOrderLine.getDiscountAmount());
			}
			purchaseOrderTitle.setOrderTotalNum(orderTotalNum);
			purchaseOrderTitle.setOrderTotalAmount(orderTotalAmount);
			purchaseOrderTitle.setTotalDiscountAmount(totalDiscountAmount);
			purchaseOrderTitleDao.updatePurchaseOrderTitleById(purchaseOrderTitle);

			// 开始审核
			distributionReturnAuditService.startAudit(purchaseOrderTitle, startAuditNode);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "铺货商品可退货数量不足");
		}
	}

	/**
	 * 生成铺货退货单头
	 * 
	 * @param pmsSupplierBind
	 * @param refund_order_sn
	 * @param provider_sn
	 * @param return_time
	 * @return
	 */
	public PurchaseOrderTitle createPurchaseOrderTitle(PMSSupplierBind pmsSupplierBind, String refund_order_sn,
			String provider_sn, Date return_time, Integer orderType, Date orderTime) {
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		// 订单编号
		String orderNo = sequenceService.getNumDateByBusName(BaseConsts.PO_NO_PRE, SeqConsts.PO_NO, BaseConsts.INT_13);
		// 通过项目查询经营单位
		BaseProject baseProject = cacheService.getProjectById(pmsSupplierBind.getProjectId());
		// 通过项目查询类型为监管的仓库
		purchaseOrderTitle.setWarehouseId(BaseConsts.INT_79);
		List<BaseAddress> baseAddresses = baseAddressDao.queryResultsyBySubjectId(purchaseOrderTitle.getWarehouseId());
		if (!CollectionUtils.isEmpty(baseAddresses)) {
			purchaseOrderTitle.setWareAddrId(baseAddresses.get(0).getId());
		}
		// 通过项目查询客户
		ProjectSubjectSearchReqDto projectReqDto = new ProjectSubjectSearchReqDto();
		projectReqDto.setProjectId(pmsSupplierBind.getProjectId());
		projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_CUSTOMER);
		List<ProjectSubject> projectSubject = projectSubjectDao.queryResultsByCon(projectReqDto);
		purchaseOrderTitle.setOrderNo(orderNo);
		purchaseOrderTitle.setOrderType(orderType);
		purchaseOrderTitle.setAppendNo(refund_order_sn);
		purchaseOrderTitle.setCreateAt(new Date());
		purchaseOrderTitle.setCreator(ServiceSupport.getUser().getChineseName());
		purchaseOrderTitle.setCreatorId(ServiceSupport.getUser().getId());
		purchaseOrderTitle.setIsDelete(BaseConsts.ZERO);
		purchaseOrderTitle.setProjectId(pmsSupplierBind.getProjectId());
		purchaseOrderTitle.setSupplierId(pmsSupplierBind.getSupplierId());
		purchaseOrderTitle.setBusinessUnitId(baseProject.getBusinessUnitId());
		purchaseOrderTitle.setCustomerId(projectSubject.get(0).getId());
		purchaseOrderTitle.setOrderTime(orderTime);
		purchaseOrderTitle.setPerdictTime(return_time);
		purchaseOrderTitle.setInvoiceTotalNum(BigDecimal.ZERO);
		purchaseOrderTitle.setInvoiceTotalAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setPerRecAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setArrivalNum(BigDecimal.ZERO);
		purchaseOrderTitle.setArrivalAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setPayAmount(BigDecimal.ZERO);
		purchaseOrderTitle.setDuctionMoney(BigDecimal.ZERO);
		purchaseOrderTitle.setTransferMode(BaseConsts.ONE);
		return purchaseOrderTitle;
	}

	/**
	 * 生成铺货退货单明细
	 * 
	 * @param poLineModel
	 * @param matchNum
	 * @return
	 */
	public PurchaseOrderLine createPurchaseOrderLine(PoLineModel poLineModel, BigDecimal matchNum) {
		PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
		purchaseOrderLine.setGoodsId(poLineModel.getGoodsId());
		purchaseOrderLine.setGoodsNum(DecimalUtil.multiply(new BigDecimal("-1"), matchNum));
		purchaseOrderLine.setGoodsPrice(poLineModel.getPayPrice());
		purchaseOrderLine
				.setAmount(DecimalUtil.multiply(purchaseOrderLine.getGoodsNum(), purchaseOrderLine.getGoodsPrice()));
		purchaseOrderLine.setDiscountPrice(poLineModel.getPayPrice());
		purchaseOrderLine.setCostPrice(poLineModel.getCostPrice());
		purchaseOrderLine.setPoPrice(poLineModel.getPoPrice());
		purchaseOrderLine.setDiscountAmount(BigDecimal.ZERO);
		purchaseOrderLine.setStorageNum(BigDecimal.ZERO);
		purchaseOrderLine.setInvoiceNum(BigDecimal.ZERO);
		purchaseOrderLine.setInvoiceAmount(BigDecimal.ZERO);
		purchaseOrderLine.setPaidAmount(BigDecimal.ZERO);
		purchaseOrderLine.setPayPrice(poLineModel.getPayPrice());
		purchaseOrderLine.setPayTime(poLineModel.getPayTime());
		purchaseOrderLine.setRequiredSendPrice(BigDecimal.ZERO);
		purchaseOrderLine.setOriginGoodsPrice(poLineModel.getGoodsPrice());
		purchaseOrderLine.setSendNum(BigDecimal.ZERO);
		purchaseOrderLine.setSendAmount(BigDecimal.ZERO);
		purchaseOrderLine.setReturnNum(BigDecimal.ZERO);
		purchaseOrderLine.setReturnAmount(BigDecimal.ZERO);
		purchaseOrderLine.setRemainSendNum(BigDecimal.ZERO);
		purchaseOrderLine.setDistributeNum(BigDecimal.ZERO);
		purchaseOrderLine.setDeductionMoney(BigDecimal.ZERO);
		purchaseOrderLine.setBatchNum(poLineModel.getBatchNum());
		purchaseOrderLine.setPledgeProportion(poLineModel.getPledgeProportion());
		purchaseOrderLine.setOccupyDay(0);
		purchaseOrderLine.setIsDelete(BaseConsts.ZERO);
		purchaseOrderLine.setDistributeId(poLineModel.getId());
		purchaseOrderLine.setCreator(ServiceSupport.getUser().getChineseName());
		purchaseOrderLine.setCreatorId(ServiceSupport.getUser().getId());
		purchaseOrderLine.setCreateAt(new Date());
		return purchaseOrderLine;
	}

}
