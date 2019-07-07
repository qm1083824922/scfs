package com.scfs.service.api.pms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.MD5Util;
import com.scfs.dao.api.pms.PmsReturnPoRelDao;
import com.scfs.dao.interf.PMSSupplierBindDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.dao.po.PurchaseOrderTitleDao;
import com.scfs.dao.project.ProjectGoodsDao;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.api.pms.dto.req.PmsHttpReqDto;
import com.scfs.domain.api.pms.dto.res.PmsPurchaseResDto;
import com.scfs.domain.api.pms.dto.res.PmsSyncPurchaseResDto;
import com.scfs.domain.api.pms.dto.res.PmsSyncPurchaseSendResDto;
import com.scfs.domain.api.pms.entity.PmsReturnPoRel;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.entity.PmsPurchase;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.project.dto.req.ProjectGoodsSearchReqDto;
import com.scfs.domain.project.entity.ProjectGoods;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.rpc.util.InvokeConfig;
import com.scfs.service.common.CommonService;
import com.scfs.service.common.InvokeLogService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.ServiceSupport;

/**
 * PMS退货订单完成接口
 * 
 * <pre>
 * 
 *  File: PmsSyncReturnPurchseSendService.java
 *  Description: 
 *  TODO
 *  Date,					Who,				
 *  2017年06月23日			Administrator
 *
 * </pre>
 */
@Service
public class PmsSyncReturnPurchseSendService {

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsSyncReturnPurchseSendService.class);
	@Autowired
	private InvokeLogService invokeLogService;
	@Autowired
	private InvokeConfig invokeConfig;
	@Autowired
	private PMSSupplierBindDao pmsSupplierBindDao;
	@Autowired
	private ProjectGoodsDao projectGoodsDao;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private PmsReturnService pmsReturnService;
	@Autowired
	private PurchaseOrderTitleDao purchaseOrderTitleDao;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private PmsReturnPoRelDao pmsReturnPoRelDao;

	/**
	 * pms 退货订单完成发送接口 1 ：log 日志记录
	 * 
	 * @param req
	 * @return
	 */
	@IgnoreTransactionalMark
	public PmsPurchaseResDto doPmsPurchase(PmsHttpReqDto req) {
		PmsPurchaseResDto resDto = new PmsPurchaseResDto();
		// 获取传入的数据
		String sign = req.getKey();
		String data = req.getData();
		InvokeLog invokeLog = new InvokeLog();
		// 记录当前的退货订单发送接口的日志信息
		this.createPurchaseLogByCon(data, invokeLog);
		try {
			List<PmsPurchase> purchases = JSON.parseArray(data, PmsPurchase.class);
			String newSign = MD5Util.getMD5String(invokeConfig.SCFS_KEY + data);
			if (!invokeConfig.profile.equals(BaseConsts.PROFILE_DEV)) { // 开发环境不校验key
				if (!sign.equals(newSign)) {
					invokeLog.setReturnMsg(JSON.toJSONString("请求非法: 签名校验出错"));
					invokeLogService.invokeError(invokeLog);
					resDto.setFlag(BaseConsts.FLAG_NO);// 初始化加载数据有误，直接返回错误
					resDto.setMsg("请求非法: 签名校验出错");
					return resDto;
				}
			}
			try {
				// 数据校验之前进行退货单号，采购单号 ，SKU 数据的合并
				Map<String, BigDecimal> map = groupByPurchase(purchases);
				// 校验当前传入的数据
				List<PmsSyncPurchaseSendResDto> purchaseSendResDtos = checkPurchase(purchases, map);
				// 生成铺货退货类型的数据
				resDto = createPurchase(resDto, purchaseSendResDtos, purchases);
			} catch (BaseException e) {
				LOGGER.error("[pms]pms 退货订单完成发送接口失败[{}]: {}", JSONObject.toJSON(req), e);
				invokeLog.setReturnMsg(JSON.toJSONString(resDto));
				invokeLogService.invokeError(invokeLog);
				return resDto;
			}
			invokeLog.setReturnMsg(JSON.toJSONString(resDto));
			invokeLogService.invokeSuccess(invokeLog);
		} catch (Exception e) {
			LOGGER.error("[pms]pms 退货订单完成发送接口失败[{}]: {}", JSONObject.toJSON(req), e);
			resDto.setMsg(commonService.getMsg(e.getMessage()));
			invokeLog.setReturnMsg(JSON.toJSONString(resDto));
			invokeLogService.invokeException(invokeLog, e);
			resDto.setFlag(BaseConsts.FLAG_NO);// 初始化加载数据有误，直接返回错误
			resDto.setMsg("退货订单完成发送接口数据接收失败");
		}
		return resDto;
	}

	/**
	 * 校验当前传入的数据
	 * 
	 * @param purchase
	 * @return
	 */
	private List<PmsSyncPurchaseSendResDto> checkPurchase(List<PmsPurchase> purchases, Map<String, BigDecimal> map) {
		List<PmsSyncPurchaseResDto> purchaseResDtos = new ArrayList<PmsSyncPurchaseResDto>();
		List<PmsSyncPurchaseSendResDto> purchaseSendResDtos = new ArrayList<PmsSyncPurchaseSendResDto>();
		if (!CollectionUtils.isEmpty(purchases)) {
			for (PmsPurchase pmsPurchase : purchases) {
				PmsSyncPurchaseResDto purchaseResDto = new PmsSyncPurchaseResDto();
				StringBuffer buffer = new StringBuffer();
				/*********** 针对传入的数据进行校验 *****************/
				// 初始数据的校验是否数据为空
				if (StringUtils.isEmpty(pmsPurchase.getRefund_order_id())) {
					buffer.append("退货订单完成发送接口退货ID【refund_order_id】为空;");
				}
				if (StringUtils.isEmpty(pmsPurchase.getRefund_order_sn())) {
					buffer.append("退货订单完成发送接口退货单号【refund_order_sn】为空;");
				}
				if (StringUtils.isEmpty(pmsPurchase.getRefund_order_sn())) {
					buffer.append("退货订单完成发送接口采购订单号【purchase_sn】为空;");
				}
				if (StringUtils.isEmpty(pmsPurchase.getProvider_sn())) {
					buffer.append("退货订单完成发送接口供应商【provider_sn】为空;");
				}
				if (StringUtils.isEmpty(pmsPurchase.getSku())) {
					buffer.append("退货订单完成发送接口商品SKU【sku】为空;");
				}
				if (StringUtils.isEmpty(pmsPurchase.getRefund_order_id())) {
					buffer.append("退货订单完成发送接口退货ID【refund_order_id】为空;");
				}
				if (StringUtils.isEmpty(pmsPurchase.getDivision_code())) {
					buffer.append("退货订单完成发送接口事业部【division_code】为空;");
				}
				/*********** 针对相同退货单号的数据校验 *****************/
				// 针对基本校验的数据进行封装到MAP 数据
				Map<String, String> maps = new HashMap<String, String>();
				String purchaseKey = pmsPurchase.getRefund_order_sn();
				if (buffer.length() > 0) {// 长度大于0 数据有误
					if (maps.containsKey(purchaseKey)) { // 包含当前的退货单号
						buffer.append("退货订单完成发送接口退货单号【" + purchaseKey + "】为整体数据有误;");
					} else {
						maps.put(purchaseKey, "N");
					}
				} else {
					if (maps.containsKey(purchaseKey)) {
						buffer.append("退货订单完成发送接口退货单号【" + purchaseKey + "】为整体数据有误;");
					}
				}
				// 供应商项目校验
				PMSSupplierBind pmsSupplierBind = pmsSupplierBindDao
						.queryEntityBySupplierNo(pmsPurchase.getProvider_sn());
				if (null != pmsSupplierBind) {
					if (null == pmsPurchase.getRefund_quantity()
							|| DecimalUtil.eq(pmsPurchase.getRefund_quantity(), BigDecimal.ZERO)) {
						buffer.append("退货订单完成发送接口实际数量【refund_quantity】为空或者为0");
					}
					ProjectGoodsSearchReqDto projectGoodsSearchReqDto = new ProjectGoodsSearchReqDto();
					projectGoodsSearchReqDto.setProjectId(pmsSupplierBind.getProjectId());
					projectGoodsSearchReqDto.setNumber(pmsPurchase.getSku());
					// 查询对应的商品信息
					ProjectGoods projectGoods = projectGoodsDao.queryByProjectIdAndGoodsNo(projectGoodsSearchReqDto);
					if (null == projectGoods || null == projectGoods.getGoodsId()) {
						buffer.append("退货订单完成发送接口该供应商【" + pmsPurchase.getProvider_sn() + "】下无商品【 "
								+ pmsPurchase.getSku() + "】");
					} else {
						/********** 针对实际退货数量的校验 ****************/
						// 校验当前采购单数据是否存在并且统计总的剩余量
						PoTitleReqDto reqDto = new PoTitleReqDto();
						reqDto.setAppendNo(pmsPurchase.getPurchase_sn());// 采购号对附属编号
						reqDto.setGoodsId(projectGoods.getGoodsId());// 商品ID
						reqDto.setOrderType(BaseConsts.TWO);// 铺货类型的商品
						PoLineModel lineModel = purchaseOrderLineDao.queryLineByGoodsIdAndAppendNo(reqDto);
						if (null != lineModel) {
							BigDecimal sendAmount = null == lineModel.getRemainSendNum() ? BigDecimal.ZERO
									: lineModel.getRemainSendNum();
							String amountKey = pmsPurchase.getRefund_order_sn() + pmsPurchase.getPurchase_sn()
									+ pmsPurchase.getSku(); // 封装的KEY
							if (map.containsKey(amountKey)) {
								BigDecimal refund_quantity = map.get(amountKey);
								if (DecimalUtil.lt(sendAmount, refund_quantity)) {
									buffer.append("退货订单完成发送接口退货单号+采购单号+商品SKU【" + amountKey + "】的数量为【" + refund_quantity
											+ "】大于采购余量【" + sendAmount);
								}
							} else {
								buffer.append("退货订单完成发送接口退货单号+采购单号+商品SKU【" + amountKey + "】整体数据有误");
							}
						} else {
							buffer.append("退货订单完成发送接口退货单号为【" + pmsPurchase.getRefund_order_sn() + "】SKU为【"
									+ pmsPurchase.getSku() + "】数据为空");
						}
					}
				} else {
					buffer.append("退货订单完成发送接口该供应商【" + pmsPurchase.getProvider_sn() + "】无质押项目");
				}
				if (buffer.length() > 0) {
					String msg = buffer.toString();
					purchaseResDto.setFlag(BaseConsts.FLAG_NO);// 数据失败
					purchaseResDto.setMsg(msg);
				} else {
					purchaseResDto.setFlag(BaseConsts.FLAG_YES);// 数据成功
				}
				purchaseResDto.setRefund_order_id(pmsPurchase.getRefund_order_id());// 退货ID
				purchaseResDto.setRefund_order_sn(pmsPurchase.getRefund_order_sn());
				purchaseResDtos.add(purchaseResDto);
			}

			if (!CollectionUtils.isEmpty(purchaseResDtos)) {
				for (PmsSyncPurchaseResDto pmsSyncPurchaseResDto : purchaseResDtos) {
					if (pmsSyncPurchaseResDto.getFlag().equals(BaseConsts.FLAG_NO)) {
						for (PmsSyncPurchaseResDto resDto : purchaseResDtos) {
							if (pmsSyncPurchaseResDto.getRefund_order_sn().equals(resDto.getRefund_order_sn())) {
								resDto.setFlag(BaseConsts.FLAG_NO);
								resDto.setMsg(pmsSyncPurchaseResDto.getMsg());
								resDto.setRefund_order_id(resDto.getRefund_order_id());
								resDto.setRefund_order_sn(resDto.getRefund_order_sn());
							}
						}
					}
				}
				for (PmsSyncPurchaseResDto pmsSyncPurchaseResDto : purchaseResDtos) {
					PmsSyncPurchaseSendResDto sendResDto = new PmsSyncPurchaseSendResDto();
					sendResDto.setFlag(pmsSyncPurchaseResDto.getFlag());
					sendResDto.setMsg(pmsSyncPurchaseResDto.getMsg());
					sendResDto.setRefund_order_id(pmsSyncPurchaseResDto.getRefund_order_id());
					purchaseSendResDtos.add(sendResDto);
				}
			}
		}
		return purchaseSendResDtos;
	}

	/**
	 * 创建当前检验成功的pms数据
	 * 
	 * @param purchaseSendResDtos
	 * @param purchases
	 * @return
	 */
	public List<PmsPurchase> createPmsPurchaseSuccessByCon(List<PmsSyncPurchaseSendResDto> purchaseSendResDtos,
			List<PmsPurchase> purchases) {
		List<PmsPurchase> pmsPurchases = new ArrayList<PmsPurchase>();
		for (PmsSyncPurchaseSendResDto sendResDto : purchaseSendResDtos) {
			for (PmsPurchase pmsPurchase : purchases) {
				if (sendResDto.getFlag().equals(BaseConsts.FLAG_YES)
						&& (sendResDto.getRefund_order_id().equals(pmsPurchase.getRefund_order_id()))) {
					pmsPurchases.add(pmsPurchase);
				}
			}
		}
		return pmsPurchases;
	}

	/***
	 * 封装当前以退货单号为Key的数据
	 * 
	 * @param pmsPurchases
	 * @return
	 */
	public List<PmsPurchase> createPmsPurchaseByOrderSn(List<PmsPurchase> pmsPurchases) {
		// 按照退货单号进行分组并且进行数据增加操作
		Map<String, PmsPurchase> map = new HashMap<String, PmsPurchase>();
		for (PmsPurchase pmsPurch : pmsPurchases) {
			// 以退货单号做KEY
			String orderKey = pmsPurch.getRefund_order_sn();
			if (map.containsKey(orderKey)) {
				// 合并当前实际退货数量
				PmsPurchase purchase = map.get(orderKey);
				purchase.setRefund_quantity(
						DecimalUtil.add(purchase.getRefund_quantity(), pmsPurch.getRefund_quantity()));
				map.put(orderKey, purchase);
			} else {
				map.put(orderKey, pmsPurch);
			}
		}
		List<PmsPurchase> purchases2 = Lists.newArrayList();
		if (map.size() != pmsPurchases.size()) { // 合并后的数据不等于合并之前的数据，则去合并后的数据
			for (Entry<String, PmsPurchase> entry : map.entrySet()) {
				PmsPurchase pus = entry.getValue();
				purchases2.add(pus);
			}
		} else {
			purchases2 = pmsPurchases;
		}
		return purchases2;
	}

	/**
	 * 生成铺货退货的采购单数据
	 * 
	 * @param purchaseResDto
	 * @param purchaseSendResDtos
	 * @param purchases
	 * @param invokeLog
	 * @return
	 */
	private PmsPurchaseResDto createPurchase(PmsPurchaseResDto purchaseResDto,
			List<PmsSyncPurchaseSendResDto> purchaseSendResDtos, List<PmsPurchase> purchases) {
		if (!CollectionUtils.isEmpty(purchases)) {
			// 封装当前满足pms校验成功的数据
			List<PmsPurchase> pmsPurchases = this.createPmsPurchaseSuccessByCon(purchaseSendResDtos, purchases);
			List<PmsPurchase> purchases2 = createPmsPurchaseByOrderSn(pmsPurchases);
			// 循环以退货单号合并后的数据信息
			for (PmsPurchase pmsPurchase : purchases2) {
				try {
					// 根据当前退货单号查询铺货退货的审核时间
					PurchaseOrderTitle orderTitle = purchaseOrderTitleDao
							.queryDistribeReturnPoAppend(pmsPurchase.getRefund_order_sn());
					PMSSupplierBind pmsSupplierBind = new PMSSupplierBind();
					if (orderTitle == null) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
								"铺货退货类型且退货单号为【" + pmsPurchase.getRefund_order_sn() + "】为空");
					}
					// 币种的定义
					Integer currencyId = null;
					BigDecimal countRefundQuantity = BigDecimal.ZERO;// 同退货单号对应的整体实际退货数量
					BigDecimal countRefundAmount = BigDecimal.ZERO;// 同退货单号对应的整体实际退货金额
					BigDecimal countPriceAmount = BigDecimal.ZERO;// 同退货单号对应的整体不加服务费的退货金额
					List<PurchaseOrderLine> listLine = Lists.newArrayList();
					List<PurchaseOrderLine> listModel = Lists.newArrayList();
					List<PmsReturnPoRel> poRelsList = Lists.newArrayList();
					// 循环满足条件的数据并且为分组的
					for (PmsPurchase purchase : pmsPurchases) {
						if (pmsPurchase.getRefund_order_sn().equals(purchase.getRefund_order_sn())) {
							// 供应商项目校验 根据当前未合并的供应商编码查询项目信息
							pmsSupplierBind = pmsSupplierBindDao.queryEntityBySupplierNo(purchase.getProvider_sn());

							// 根据pms 传入的SKU 查询SCFS 项目的商品信息
							ProjectGoodsSearchReqDto projectGoodsSearchReqDto = new ProjectGoodsSearchReqDto();
							projectGoodsSearchReqDto.setProjectId(pmsSupplierBind.getProjectId());
							projectGoodsSearchReqDto.setNumber(purchase.getSku());
							ProjectGoods projectGoods = projectGoodsDao
									.queryByProjectIdAndGoodsNo(projectGoodsSearchReqDto);
							// 根据传的PMS采购单号和商品sku查询SCFS铺货头信息
							PoTitleReqDto reqDto = new PoTitleReqDto();
							reqDto.setAppendNo(purchase.getPurchase_sn());// 采购单号对附属编号
							reqDto.setGoodsId(projectGoods.getGoodsId());// 商品ID
							reqDto.setOrderType(BaseConsts.TWO);// 铺货类型的商品
							List<PurchaseOrderLine> lines = purchaseOrderLineDao
									.queryDistribePoLineAppendNoAndGoodID(reqDto);
							// 实际退货数量
							BigDecimal refundQuantity = purchase.getRefund_quantity();
							BigDecimal sendNum = BigDecimal.ZERO;
							sendNum = refundQuantity; // 初始的实际发货余量
							BigDecimal countRaminNum = BigDecimal.ZERO;// 明细发货总量
							// 当该采购单号和SKU对应的SCFS 项目的铺货数据不为空
							// 铺货头信息的退货数量和金额
							BigDecimal titleSendAmout = BigDecimal.ZERO;
							BigDecimal titleSendNum = BigDecimal.ZERO;
							if (!CollectionUtils.isEmpty(lines)) {
								PurchaseOrderTitle purchaseOrderTitle = purchaseOrderTitleDao
										.queryEntityById(lines.get(BaseConsts.ZERO).getPoId());
								// 采购单明细不为空
								for (PurchaseOrderLine lineModel : lines) {
									// 保存当前代销订单和结算单的关系
									PmsReturnPoRel pmsReturnPoRel = new PmsReturnPoRel();
									pmsReturnPoRel.setPoId(purchaseOrderTitle.getId());
									pmsReturnPoRel.setPoLineId(lineModel.getId());
									// 获取当前明细的可发货数量
									BigDecimal remainSendNum = null == lineModel.getRemainSendNum() ? BigDecimal.ZERO
											: lineModel.getRemainSendNum();
									// 记录明细发货的总量
									countRaminNum = DecimalUtil.add(countRaminNum, remainSendNum);
									// 判断当前发货余量是不是大于0
									if (DecimalUtil.gt(sendNum, BigDecimal.ZERO)) {
										// 实际扣除数量
										BigDecimal matchNum = BigDecimal.ZERO;
										currencyId = purchaseOrderTitle.getCurrencyId();
										// 判断当前明细的实际发货数量大于等于可发货数量
										// 则将留到下一次扣除
										if (DecimalUtil.ge(sendNum, remainSendNum)) {
											sendNum = DecimalUtil.subtract(sendNum, remainSendNum);
											matchNum = remainSendNum;
										} else {
											matchNum = sendNum;
											sendNum = BigDecimal.ZERO;
										}
										// 计算当前实际退货金额和服务费
										ProjectItem projectItem = projectItemService
												.getProjectItem(purchaseOrderTitle.getProjectId());
										long occupyDays = projectItemService.getOccupyDays(
												purchaseOrderTitle.getProjectId(), lineModel.getPayTime(),
												orderTitle.getOrderTime());
										BigDecimal profitPrice = projectItemService.getProfitPrice(
												purchaseOrderTitle.getProjectId(), lineModel.getPayPrice(),
												lineModel.getPayTime(), orderTitle.getOrderTime());
										BigDecimal occupyAmount = DecimalUtil.add(
												DecimalUtil.multiply(profitPrice, matchNum),
												DecimalUtil.multiply(lineModel.getPayPrice(), matchNum));
										BigDecimal priceAmount = DecimalUtil.multiply(lineModel.getPayPrice(),
												matchNum);
										titleSendNum = DecimalUtil.add(titleSendNum, matchNum);
										titleSendAmout = DecimalUtil.add(titleSendAmout, occupyAmount);
										// 计算该明细退款的总费用
										countRefundAmount = DecimalUtil.add(countRefundAmount, occupyAmount);
										countRefundQuantity = DecimalUtil.add(matchNum, countRefundQuantity);
										countPriceAmount = DecimalUtil.add(countPriceAmount, priceAmount);
										// 采购单铺货结算单明细数据
										PurchaseOrderLine orderLine = this.createPurchaseOrderLine(lineModel, matchNum,
												purchase.getPurchase_sn(), projectItem, occupyDays, profitPrice);
										pmsReturnPoRel.setReturnNumber(matchNum);
										listLine.add(orderLine);
										poRelsList.add(pmsReturnPoRel);
										// 修改当前SCFS 铺货明细的数据
										lineModel = this.updatePolineModel(lineModel, matchNum, profitPrice);
										listModel.add(lineModel);
									}
								}
							}
							if (DecimalUtil.gt(refundQuantity, countRaminNum)) {
								throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
										"铺货商品【" + purchase.getSku() + "】可退货数量不足");
							}
						}
					}

					// 铺货结算单的头信息的封装
					PurchaseOrderTitle title = pmsReturnService.createPurchaseOrderTitle(pmsSupplierBind,
							pmsPurchase.getRefund_order_sn(), pmsPurchase.getProvider_sn(), orderTitle.getPerdictTime(),
							BaseConsts.FOUR, orderTitle.getOrderTime());// 生成采购订单头信息
					title.setOrderTotalNum(DecimalUtil.multiply(new BigDecimal("-1"), countRefundQuantity));// 退货数量
					title.setOrderTotalAmount(DecimalUtil.multiply(new BigDecimal("-1"), countPriceAmount));// 退货金额不
																											// 包含服务费
					title.setTotalRefundAmount(DecimalUtil.multiply(new BigDecimal("-1"), countRefundAmount));
					title.setState(BaseConsts.FIVE);// 完成状态
					title.setOrderTime(orderTitle.getOrderTime());// 审核日期
					// 更新数据
					updateTitleAndLine(currencyId, title, listLine, listModel, poRelsList);
				} catch (Exception e) {
					LOGGER.error("[pms]pms 退货订单完成发送接口失败[{}]: {}", JSONObject.toJSON(purchaseSendResDtos), e);
					// 新增失败 回写传入数据的状态
					String refund_order_sn = pmsPurchase.getRefund_order_sn();
					for (PmsPurchase purchase : pmsPurchases) {
						if (refund_order_sn.equals(purchase.getRefund_order_sn())) {
							for (PmsSyncPurchaseSendResDto purchaseSendResDto : purchaseSendResDtos) {
								if (purchase.getRefund_order_id().equals(purchaseSendResDto.getRefund_order_id())) {
									purchaseSendResDto.setFlag(BaseConsts.FLAG_NO);
									purchaseSendResDto.setMsg("数据插入失败" + e.getMessage());
								}
							}
						}
					}
				}
			}
		} else {
			purchaseResDto.setMsg("数据传入为空");
		}
		purchaseResDto.setFlag(BaseConsts.FLAG_YES);
		purchaseResDto.setData(purchaseSendResDtos);
		return purchaseResDto;
	}

	/**
	 * 更新明细和头信息的数据
	 * 
	 * @param currencyId
	 * @param title
	 * @param listLine
	 * @param list
	 * @param listModel
	 */
	public void updateTitleAndLine(Integer currencyId, PurchaseOrderTitle title, List<PurchaseOrderLine> listLine,
			List<PurchaseOrderLine> listModel, List<PmsReturnPoRel> poRelsList) {
		// 生成结算单头信息
		title.setCurrencyId(currencyId);
		purchaseOrderTitleDao.insert(title);
		// 生成结算单明细信息
		for (int i = 0; i < listLine.size(); i++) {
			PurchaseOrderLine orderLine = listLine.get(i);
			orderLine.setPoId(title.getId());
			purchaseOrderLineDao.insert(orderLine);
			PmsReturnPoRel pmsReturnPoRel = poRelsList.get(i);
			pmsReturnPoRel.setRtPoId(title.getId());
			pmsReturnPoRel.setRtPoLineId(orderLine.getId());
			pmsReturnPoRelDao.insert(pmsReturnPoRel);
		}
		// for (PurchaseOrderLine orderLine : listLine) {
		// orderLine.setPoId(title.getId());
		// purchaseOrderLineDao.insert(orderLine);
		// }
		// 修改铺货单明细信息
		for (PurchaseOrderLine poLineModel2 : listModel) {
			purchaseOrderLineDao.updatePurchaseOrderLineById(poLineModel2);
		}
	}

	/**
	 * 生成铺货退货单明细
	 * 
	 * @param poLineModel
	 * @param matchNum
	 * @param profitPrice
	 * @param occupyDays
	 * @param projectItem
	 * @param profitPrice
	 * @param occupyDays
	 * @param projectItem
	 * @return
	 */
	public PurchaseOrderLine createPurchaseOrderLine(PurchaseOrderLine poLineModel, BigDecimal matchNum,
			String purchase_sn, ProjectItem projectItem, long occupyDays, BigDecimal profitPrice) {
		PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
		purchaseOrderLine.setGoodsId(poLineModel.getGoodsId());
		purchaseOrderLine.setGoodsNum(DecimalUtil.multiply(new BigDecimal("-1"), matchNum));
		purchaseOrderLine.setGoodsPrice(poLineModel.getPayPrice());
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
		purchaseOrderLine.setBatchNum(purchase_sn); // 批次采购单号
		purchaseOrderLine.setPledgeProportion(poLineModel.getPledgeProportion());
		purchaseOrderLine.setIsDelete(BaseConsts.ZERO);
		purchaseOrderLine.setDistributeId(poLineModel.getId());
		purchaseOrderLine.setCreator(ServiceSupport.getUser().getChineseName());
		purchaseOrderLine.setCreatorId(ServiceSupport.getUser().getId());
		purchaseOrderLine.setCreateAt(new Date());
		purchaseOrderLine.setOccupyDay((int) occupyDays);
		purchaseOrderLine.setOccupyServiceAmount(
				DecimalUtil.multiply(new BigDecimal("-1"), DecimalUtil.multiply(profitPrice, matchNum)));
		purchaseOrderLine.setAmount(DecimalUtil.multiply(purchaseOrderLine.getGoodsNum(), poLineModel.getPayPrice()));
		purchaseOrderLine.setRefundAmount(DecimalUtil.formatScale2(DecimalUtil
				.multiply(DecimalUtil.add(poLineModel.getPayPrice(), profitPrice), purchaseOrderLine.getGoodsNum())));
		purchaseOrderLine.setFundMonthRate(projectItem.getFundMonthRate());
		return purchaseOrderLine;
	}

	/**
	 * 修改当前明细信息
	 * 
	 * @param poLineModel
	 * @param matchNum
	 * @param profitPrice
	 * @return
	 */
	public PurchaseOrderLine updatePolineModel(PurchaseOrderLine poLineModel, BigDecimal matchNum,
			BigDecimal profitPrice) {
		BigDecimal returnNum = poLineModel.getReturnNum() == null ? BigDecimal.ZERO : poLineModel.getReturnNum();
		BigDecimal returnAmount = poLineModel.getReturnAmount() == null ? BigDecimal.ZERO
				: poLineModel.getReturnAmount();
		poLineModel.setReturnNum(DecimalUtil.add(returnNum, matchNum));
		poLineModel.setReturnAmount(
				DecimalUtil.add(returnAmount, DecimalUtil.multiply(matchNum, poLineModel.getPayPrice())));
		poLineModel.setRemainSendNum(DecimalUtil.subtract(
				poLineModel.getRemainSendNum() == null ? BigDecimal.ZERO : poLineModel.getRemainSendNum(), matchNum));
		poLineModel.setRefundAmount(DecimalUtil.formatScale2(DecimalUtil.add(
				poLineModel.getRefundAmount() == null ? BigDecimal.ZERO : poLineModel.getRemainSendNum(),
				DecimalUtil.multiply(matchNum, DecimalUtil.add(poLineModel.getPayPrice(), profitPrice)))));
		return poLineModel;
	}

	/**
	 * 修改当前铺货头信息的数据
	 * 
	 * @param orderTitle
	 * @param titleSendAmout
	 * @param titleSendNum
	 * @return
	 */
	public PurchaseOrderTitle updateOrderTitle(PurchaseOrderTitle orderTitle, BigDecimal titleSendNum,
			BigDecimal titleSendAmout) {
		orderTitle.setOrderTotalNum(DecimalUtil.subtract(orderTitle.getOrderTotalNum(), titleSendNum));
		orderTitle.setOrderTotalAmount(DecimalUtil.subtract(orderTitle.getOrderTotalAmount(), titleSendAmout));
		orderTitle.setTotalRefundAmount(DecimalUtil.add(orderTitle.getTotalRefundAmount(), titleSendAmout));
		return orderTitle;

	}

	/**
	 * 将初始数据按照 采购单号 退货单号 进行数量的分组
	 * 
	 * @param purchases
	 */
	private Map<String, BigDecimal> groupByPurchase(List<PmsPurchase> purchases) {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		for (PmsPurchase pmsPurchase : purchases) {
			String key = pmsPurchase.getRefund_order_sn() + pmsPurchase.getPurchase_sn() + pmsPurchase.getSku(); // 封装的KEY
			if (map.containsKey(key)) {
				BigDecimal refund_quantity = map.get(key);
				refund_quantity = DecimalUtil.add(refund_quantity, pmsPurchase.getRefund_quantity());
				map.put(key, refund_quantity);
			} else {
				map.put(key, pmsPurchase.getRefund_quantity());
			}
		}
		return map;
	}

	/**
	 * 记录当前退货订单完成发送接口的日志信息
	 * 
	 * @param data
	 * @param invokeLog
	 */
	public void createPurchaseLogByCon(String data, InvokeLog invokeLog) {
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setContent(data);// 传输内容
		invokeLog.setCreateAt(new Date());// 创建时间
		invokeLog.setConsumer(BaseConsts.THREE);// 调用方
		invokeLog.setProvider(BaseConsts.ONE);// 提供方
		invokeLog.setInvokeType(InvokeTypeEnum.PMS_PURCHASE_ORDER_SEND.getType()); // pms
																					// 退货订单完成发送接口
		invokeLog.setInvokeMode(BaseConsts.TWO);
		invokeLogService.add(invokeLog);
	}
}
