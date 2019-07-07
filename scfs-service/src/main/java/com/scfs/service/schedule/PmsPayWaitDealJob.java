package com.scfs.service.schedule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.api.pms.PmsPayDao;
import com.scfs.domain.api.pms.entity.PmsPay;
import com.scfs.service.interf.PmsPayWaitService;

/**
 * <pre>
 *  pms同步请款待付款或驳回定时任务处理 
 *  File: PmsSyncWaitPayService.java
 *  Description:
 *  TODO
 *  Date,                   Who,                
 *  2017年05月05日                                    Administrator
 *
 * </pre>
 */
public class PmsPayWaitDealJob {
	@Autowired
	PmsPayWaitService pmsPayWaitService;

	@Autowired
	private PmsPayDao pmsPayDao;

	private final static Logger LOGGER = LoggerFactory.getLogger(PmsPayWaitDealJob.class);

	public void execute() {
		try {
			long time = BaseConsts.ZERO;
			List<PmsPay> list = pmsPayDao.queryPmsPayRebutDao();// 优先处理驳回
			if (!CollectionUtils.isEmpty(list)) {
				long startTime = System.currentTimeMillis();
				LOGGER.info("[pms入库单自动处理]开始时间：" + new Date());
				for (PmsPay pmsPay : list) {
					Integer id = pmsPay.getId();
					try {
						// 删除采购定单、入库单、销售提货、出库单、入库凭证、出库凭证
						pmsPayWaitService.dealPmsPayRebut(id);
					} catch (BaseException e) {
						pmsPayWaitService.dealFail(id, e.getMsg());
					} catch (Exception e) {
						LOGGER.error("处理pms请款单驳回处理失败[{}]: {}", JSONObject.toJSON(pmsPay), e);
						pmsPayWaitService.dealFail(id, "系统异常，请稍后再试");
					}
				}
				long endTime = System.currentTimeMillis();
				time = endTime - startTime + time;
			}
			List<PmsPay> waitList = pmsPayDao.queryPmsPayWaitDao();// 获取所有未处理待付款信息
			if (!CollectionUtils.isEmpty(waitList)) {
				long startTime = System.currentTimeMillis();
				LOGGER.info("[pms入库单自动处理]开始时间：" + new Date());
				for (PmsPay pmsPay : waitList) {
					Integer id = pmsPay.getId();
					try {
						// 通过流水号判断QK有已完成数据并且有驳回数据进行处理
						PmsPay pmsPayPam = new PmsPay();
						pmsPay.setDeduction_money(BigDecimal.ZERO); // pms传过来的抵扣金额赋值为0，目前业务不使用该金额
						pmsPayPam.setPay_sn(pmsPay.getPay_sn());
						List<PmsPay> result = pmsPayDao.queryPmsPayWaitGroupDao(pmsPayPam);// 获取QK所有待付款已处理信息
						boolean isRebut = true;
						if (result != null) {
							for (PmsPay pms : result) {
								PmsPay pmsPayNumber = new PmsPay();
								pmsPayNumber.setUnique_number(pms.getUnique_number());
								pmsPayNumber.setStatus(BaseConsts.ONE);
								PmsPay pmsRebut = pmsPayDao.queryEntityByParam(pmsPayNumber);// 通过流水号获取驳回数据
								if (pmsRebut != null) {
									if (pmsRebut.getDealFlag().equals(BaseConsts.THREE)) {
										isRebut = true;
									} else {
										isRebut = false;
									}
								} else {
									isRebut = false;
								}
							}
						}

						if (isRebut) {
							pmsPayWaitService.dealPmsPayWait(pmsPay, true);
						}
					} catch (BaseException e) {
						pmsPayWaitService.dealFail(id, e.getMsg());
					} catch (Exception e) {
						LOGGER.error("处理pms请款单待付款处理失败[{}]: {}", JSONObject.toJSON(pmsPay), e);
						pmsPayWaitService.dealFail(id, "系统异常，请稍后再试");
					}
				}
				long endTime = System.currentTimeMillis();
				time = endTime - startTime + time;
			}
			LOGGER.info("[pms请款单待付款驳回自动处理]结束时间：" + new Date() + "，共执行" + (time) / 1000 + "秒");
		} catch (BaseException e) {
			LOGGER.error("请款单待付款驳回同步定时任务定时任务失败：", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("请款单待付款驳回同步定时任务定时任务失败：", e);
		}
	}

}
