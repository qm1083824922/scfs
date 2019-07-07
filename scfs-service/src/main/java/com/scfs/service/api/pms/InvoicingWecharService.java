package com.scfs.service.api.pms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.ListUtil;
import com.scfs.dao.api.pms.InvoicingWecharDao;
import com.scfs.dao.base.entity.BaseGoodsDao;
import com.scfs.dao.base.entity.BaseUserSubjectDao;
import com.scfs.domain.api.pms.dto.res.InvoicingWecharResDto;
import com.scfs.domain.api.pms.model.InvoicingWechar;
import com.scfs.domain.base.dto.req.BaseUserSubjectReqDto;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.entity.BaseUserSubject;
import com.scfs.domain.pay.dto.req.InvoicingWecharReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.wechat.entity.WechatUser;
import com.scfs.service.base.user.UserWechatService;
import com.scfs.service.common.MsgContentService;

/**
 * <pre>
 * 	进销存销售相关业务
 *  File: InvoicingWecharService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年10月27日			Administrator
 *
 * </pre>
 */
@Service
public class InvoicingWecharService {
	@Autowired
	private InvoicingWecharDao invoicingWecharDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private UserWechatService userWechatService;
	@Autowired
	private BaseUserSubjectDao baseUserSubjectDao;
	@Autowired
	private BaseGoodsDao baseGoodsDao;
	@Value("${domain}")
	private String domain;

	/**
	 * 获取信息列表
	 * 
	 * @param supplierId
	 * @return
	 */
	public PageResult<InvoicingWecharResDto> querySupplierStoreOutBySupplierId(InvoicingWecharReqDto reqDto) {
		PageResult<InvoicingWecharResDto> result = new PageResult<InvoicingWecharResDto>();
		List<InvoicingWecharResDto> resDtoList = convertToResDtos(invoicingWecharDao.queryResultsByCon(reqDto));
		result.setItems(resDtoList);
		return result;
	}

	/**
	 * 定时发送微信相关业务处理
	 */
	public void realSendWechatMsg() {
		Date currDate = new Date();
		Date yesterday = DateFormatUtils.beforeDay(currDate, BaseConsts.ONE);// 获取昨日时间
		String dailyDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
				DateFormatUtils.beforeDay(currDate, BaseConsts.ONE));
		Date oneDay = DateFormatUtils.getNowDateFristDay(yesterday);// 获取1号
		String no = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, oneDay);
		InvoicingWecharReqDto reqDto = new InvoicingWecharReqDto();
		reqDto.setStartDate(no);
		reqDto.setEndDate(dailyDate);
		List<Integer> supplierIds = invoicingWecharDao.querySupplierIdByDate(reqDto);// 入库供应商
		if (CollectionUtils.isNotEmpty(supplierIds)) {
			for (Integer supplierId : supplierIds) {
				reqDto.setSupplierId(supplierId);
				List<InvoicingWechar> inList = invoicingWecharDao.queryStoreInBySkuDate(reqDto);
				if (CollectionUtils.isNotEmpty(inList)) {
					for (InvoicingWechar model : inList) {
						Integer sendSate = BaseConsts.ZERO;
						BaseGoods baseGoods = baseGoodsDao.queryGoodsByNumber(model.getSku());
						if (baseGoods != null) {
							model.setGoodsType(baseGoods.getType());
						}
						model.setSupplierId(supplierId);
						model.setSendTime(dailyDate);
						model.setSendType(BaseConsts.ZERO);// 入库
						model.setCreateAt(currDate);

						reqDto.setSku(model.getSku());
						BigDecimal salseNum = invoicingWecharDao.queryStockOutNum(reqDto);// 获取销售数量
						if (salseNum != null) {
							model.setSalseNum(salseNum);
						} else {
							model.setSalseNum(BigDecimal.ZERO);
						}
						BigDecimal dailySalseNum = invoicingWecharDao.queryStoreOutByDate(reqDto);// 获取昨日销售数量
						if (dailySalseNum != null) {
							model.setDailySalseNum(dailySalseNum);
						} else {
							model.setDailySalseNum(BigDecimal.ZERO);
						}
						BigDecimal dailyStockinNum = invoicingWecharDao.queryYesterdayStockInNum(reqDto);// 获取昨日入库数量
						if (dailyStockinNum != null) {
							model.setDailyStockinNum(dailyStockinNum);
						} else {
							model.setDailyStockinNum(BigDecimal.ZERO);
						}

						if (!model.getDailySalseNum().equals(BigDecimal.ZERO)
								|| !model.getDailyStockinNum().equals(BigDecimal.ZERO)) {
							Integer resultState = sendInvoiceWechar(supplierId, BaseConsts.ZERO, dailyDate);
							if (resultState != null) {
								sendSate = resultState;
							}
						}
						model.setSendSate(sendSate);
						invoicingWecharDao.insert(model);
					}
				}
			}
		}
		reqDto.setSupplierIds(supplierIds);
		List<Integer> outSupplierIds = invoicingWecharDao.querySupplierIdOutByDate(reqDto);// 获取出库供应商
		if (CollectionUtils.isNotEmpty(outSupplierIds)) {
			for (Integer supplierId : outSupplierIds) {
				reqDto.setSupplierId(supplierId);
				List<InvoicingWechar> outList = invoicingWecharDao.queryStoreOutBySkuDate(reqDto);
				if (CollectionUtils.isNotEmpty(outList)) {
					for (InvoicingWechar model : outList) {
						Integer sendSate = BaseConsts.ZERO;
						BaseGoods baseGoods = baseGoodsDao.queryGoodsByNumber(model.getSku());
						if (baseGoods != null) {
							model.setGoodsType(baseGoods.getType());
						}
						model.setSupplierId(supplierId);
						model.setSendTime(dailyDate);
						model.setSendType(BaseConsts.ONE);// 出口
						model.setCreateAt(currDate);

						reqDto.setSku(model.getSku());
						BigDecimal stockinNum = invoicingWecharDao.queryStockInNum(reqDto);// 入库数量
						if (stockinNum != null) {
							model.setStockinNum(stockinNum);
						} else {
							model.setStockinNum(BigDecimal.ZERO);
						}

						BigDecimal dailySalseNum = invoicingWecharDao.queryStoreOutByDate(reqDto);// 获取昨日销售数量
						if (dailySalseNum != null) {
							model.setDailySalseNum(dailySalseNum);
						} else {
							model.setDailySalseNum(BigDecimal.ZERO);
						}
						BigDecimal dailyStockinNum = invoicingWecharDao.queryYesterdayStockInNum(reqDto);// 获取昨日入库数量
						if (dailyStockinNum != null) {
							model.setDailyStockinNum(dailyStockinNum);
						} else {
							model.setDailyStockinNum(BigDecimal.ZERO);
						}

						if (!model.getDailySalseNum().equals(BigDecimal.ZERO)
								|| !model.getDailyStockinNum().equals(BigDecimal.ZERO)) {
							Integer resultState = sendInvoiceWechar(supplierId, BaseConsts.ONE, dailyDate);
							if (resultState != null) {
								sendSate = resultState;
							}
						}
						model.setSendSate(sendSate);
						invoicingWecharDao.insert(model);
					}
				}
			}
		}
	};

	public Integer sendInvoiceWechar(Integer supplierId, Integer type, String sendTime) {
		Integer result = null;
		BaseUserSubjectReqDto baseReq = new BaseUserSubjectReqDto();// 用户下是否有仓库
		baseReq.setSubjectId(supplierId);
		List<BaseUserSubject> userSubject = baseUserSubjectDao.queryUserSubjectByCon(baseReq);
		if (CollectionUtils.isNotEmpty(userSubject)) {
			for (BaseUserSubject baseUserSubject : userSubject) {
				List<WechatUser> wechatUsers = userWechatService.queryBindWechatsByUserId(baseUserSubject.getUserId());
				if (CollectionUtils.isNotEmpty(wechatUsers)) {
					StringBuilder acc = new StringBuilder();
					for (WechatUser wechatUser : wechatUsers) {
						acc.append(wechatUser.getOpenid()).append(",");
					}
					Map<String, String> map = Maps.newHashMap();
					map.put("template_id", "UHt3WWjHBpfEtoEK2UJja23Uv_vCdoMtkr59p_MGqG0");
					map.put("first", "你好！你昨日的进销存信息请查看：");
					String url = domain + "wechat/html/service/show_store_out.html?supplierId=" + supplierId + "&type="
							+ type + "&sendTime=" + sendTime;
					map.put("url", url);
					map.put("keyword1", "进销存信息");
					map.put("keyword2", "正常");
					map.put("keyword3", "推送昨日进销存信息");
					map.put("remark", "点击跳转进销存信息页面查看。");
					String content = JSONObject.toJSONString(map);
					msgContentService.addMsgContent(acc.toString(), null, content, BaseConsts.FOUR);
					result = BaseConsts.ONE;
				}
			}
		}
		return result;
	}

	public List<InvoicingWecharResDto> convertToResDtos(List<InvoicingWechar> result) {
		List<InvoicingWecharResDto> resDtos = new ArrayList<InvoicingWecharResDto>();
		if (ListUtil.isEmpty(result)) {
			return resDtos;
		}
		for (InvoicingWechar model : result) {
			InvoicingWecharResDto resDto = convertResDto(model);
			resDtos.add(resDto);
		}
		return resDtos;
	}

	public InvoicingWecharResDto convertResDto(InvoicingWechar model) {
		InvoicingWecharResDto result = new InvoicingWecharResDto();
		result.setSupplierId(model.getSupplierId());
		result.setSku(model.getSku());
		result.setSendTime(model.getSendTime());
		result.setSalseNum(model.getSalseNum());
		result.setStockinNum(model.getStockinNum());
		result.setRemainSendNum(model.getRemainSendNum());
		result.setDailySalseNum(model.getDailySalseNum());
		result.setDailyStockinNum(model.getDailyStockinNum());
		result.setSendType(model.getSendType());
		result.setGoodsType(model.getGoodsType());
		return result;
	}
}
