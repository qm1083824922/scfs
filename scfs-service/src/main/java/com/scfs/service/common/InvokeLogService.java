package com.scfs.service.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.InvokeTypeEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.common.InvokeLogDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.tx.IgnoreTransactionalMark;
import com.scfs.domain.CodeValue;
import com.scfs.domain.common.dto.req.InvokeLogSearchReqDto;
import com.scfs.domain.common.dto.resp.InvokeLogResDto;
import com.scfs.domain.common.entity.InvokeLog;
import com.scfs.domain.result.PageResult;
import com.scfs.rpc.cms.dto.PayCmsHttpResDto;
import com.scfs.rpc.cms.service.CmsPayRpcService;
import com.scfs.rpc.pms.dto.PayPmsHttpResDto;
import com.scfs.rpc.pms.dto.PmsSyncReturnPurchaseResDto;
import com.scfs.rpc.pms.service.PayRpcService;
import com.scfs.rpc.pms.service.PurchaseRpcService;
import com.scfs.service.support.ServiceSupport;

/**
 * Created by Administrator on 2016年11月24日.
 */
@Service
public class InvokeLogService {
	private final static Logger LOGGER = LoggerFactory.getLogger(InvokeLogService.class);

	@Autowired
	private InvokeLogDao invokeLogDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private PayRpcService payRpcService;
	@Autowired
	private PurchaseRpcService purchaseRpcService;
	@Autowired
	private CmsPayRpcService cmsPayRpcService;

	/**
	 * 查询接口调用日志
	 * 
	 * @param invokeLogSearchReqDto
	 * @return
	 */
	public PageResult<InvokeLogResDto> queryInvokeLogResultsByCon(InvokeLogSearchReqDto invokeLogSearchReqDto) {
		PageResult<InvokeLogResDto> result = new PageResult<InvokeLogResDto>();

		int offSet = PageUtil.getOffSet(invokeLogSearchReqDto.getPage(), invokeLogSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, invokeLogSearchReqDto.getPer_page());
		List<InvokeLog> invokeLogList = invokeLogDao.queryResultsByCon(invokeLogSearchReqDto, rowBounds);
		List<InvokeLogResDto> invokeLogResDtoList = convertToResDto(invokeLogList);
		result.setItems(invokeLogResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), invokeLogSearchReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(invokeLogSearchReqDto.getPage());
		result.setPer_page(invokeLogSearchReqDto.getPer_page());

		return result;
	}

	private List<InvokeLogResDto> convertToResDto(List<InvokeLog> invokeLogList) {
		List<InvokeLogResDto> invokeLogResDtoList = new ArrayList<InvokeLogResDto>(5);
		if (CollectionUtils.isEmpty(invokeLogList)) {
			return invokeLogResDtoList;
		}
		for (InvokeLog invokeLog : invokeLogList) {
			InvokeLogResDto invokeLogResDto = convertToResDto(invokeLog);
			invokeLogResDto.setOpertaList(getOperList(invokeLog));
			invokeLogResDtoList.add(invokeLogResDto);
		}
		return invokeLogResDtoList;
	}

	private InvokeLogResDto convertToResDto(InvokeLog invokeLog) {
		InvokeLogResDto invokeLogResDto = new InvokeLogResDto();
		if (null != invokeLog) {
			BeanUtils.copyProperties(invokeLog, invokeLogResDto);
			if (invokeLogResDto.getProvider() != null) {
				invokeLogResDto.setProviderName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_SYSTEM,
						Integer.toString(invokeLogResDto.getProvider())));
			}
			if (invokeLogResDto.getConsumer() != null) {
				invokeLogResDto.setConsumerName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_SYSTEM,
						Integer.toString(invokeLogResDto.getConsumer())));
			}
			if (invokeLogResDto.getInvokeType() != null) {
				invokeLogResDto.setInvokeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_TYPE,
						Integer.toString(invokeLogResDto.getInvokeType())));
			}
			if (invokeLogResDto.getModuleType() != null) {
				invokeLogResDto.setModuleTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_MODULE_TYPE,
						Integer.toString(invokeLogResDto.getModuleType())));
			}
			if (invokeLogResDto.getBillType() != null) {
				invokeLogResDto.setBillTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_BILL_TYPE,
						Integer.toString(invokeLogResDto.getBillType())));
			}
			if (invokeLogResDto.getIsSuccess() != null) {
				invokeLogResDto.setIsSuccessName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_SUCCESS_FLAG,
						Integer.toString(invokeLogResDto.getIsSuccess())));
			}
			if (invokeLogResDto.getDealFlag() != null) {
				invokeLogResDto.setDealFlagName(ServiceSupport.getValueByBizCode(BizCodeConsts.DEAL_FLAG,
						Integer.toString(invokeLogResDto.getDealFlag())));
			}
		}
		return invokeLogResDto;
	}

	private List<CodeValue> getOperList(InvokeLog invokeLog) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(invokeLog);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				InvokeLogResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(InvokeLog invokeLog) {
		List<String> opertaList = Lists.newArrayList(5);
		if (null != invokeLog) {
			if (invokeLog.getProvider() == null || invokeLog.getConsumer() == null || invokeLog.getIsSuccess() == null
					|| invokeLog.getDealFlag() == null) {
				return null;
			}
			Integer isSuccess = invokeLog.getIsSuccess(); // 调用成功状态 0-未调用 1-调用失败
															// 2-调用成功
			if (invokeLog.getConsumer().equals(BaseConsts.ONE)
					&& (isSuccess.equals(BaseConsts.ZERO) || invokeLog.getIsSuccess().equals(BaseConsts.ONE))
					&& invokeLog.getTryAgainFlag().equals(BaseConsts.ONE)) { // 调用方scfs，且未调用或调用失败，且可重新调用
				opertaList.add(OperateConsts.REINVOKE);
			}
		}
		return opertaList;
	}

	/**
	 * 重新调用接口
	 * 
	 * @param invokeLogSearchReqDto
	 */
	public void reInvoke(InvokeLogSearchReqDto invokeLogSearchReqDto, boolean isJob) {
		InvokeLog invokeLog = invokeLogDao.queryAndLockEntityById(invokeLogSearchReqDto.getId());
		if (invokeLog.getConsumer().equals(BaseConsts.ONE)
				&& (invokeLog.getIsSuccess().equals(BaseConsts.ZERO) || invokeLog.getIsSuccess().equals(BaseConsts.ONE))
				&& invokeLog.getTryAgainFlag().equals(BaseConsts.ONE)) { // 调用方scfs，且未调用或调用失败，且可重新调用
			invokeLog.setTryNum(invokeLog.getTryNum() + 1);
			if (invokeLog.getInvokeType().equals(InvokeTypeEnum.PMS_PAY_ORDER_UPLOAD.getType())) {
				invokePmsPay(invokeLog, isJob);
			}
			if (invokeLog.getInvokeType().equals(InvokeTypeEnum.PMS_PURCHASE_ORDER_CONFIRM.getType())) {// 退货订单确认回传接口调用
				invokePurchase(invokeLog, isJob);
			}
			if (invokeLog.getInvokeType().equals(InvokeTypeEnum.CMS_PAY_ORDER.getType())) { // cms上传付款信息接口
				invokeCmsPay(invokeLog, isJob);
			}
		}
	}

	/**
	 * PMS上传付款信息接口
	 * 
	 * @param invokeLog
	 */
	public void invokePmsPay(InvokeLog invokeLog, boolean isJob) {
		try {
			if (invokeLog != null) {
				PayPmsHttpResDto payPmsHttpResDto = payRpcService.invokePay(invokeLog.getContent());
				invokeLog.setReturnMsg(payPmsHttpResDto.getMsg());
				if (payPmsHttpResDto.getFlag().equals(BaseConsts.FLAG_NO)) { // 返回结果N，调用失败
					invokeLog.setTryAgainFlag(BaseConsts.ONE);
					invokeError(invokeLog);
				} else {
					invokeSuccess(invokeLog);
				}
			}
		} catch (Exception e) {
			LOGGER.error("调用PMS上传付款信息接口：", e);
			invokeLog.setTryAgainFlag(BaseConsts.ONE);
			invokeException(invokeLog, e);
		}
	}

	/**
	 * 退货订单确认回传接口
	 * 
	 * @param invokeLog
	 * @param isJob
	 */
	public void invokePurchase(InvokeLog invokeLog, boolean isJob) {
		try {
			if (invokeLog != null) {
				PmsSyncReturnPurchaseResDto purchaseResDto = purchaseRpcService.invokePurchase(invokeLog.getContent());
				invokeLog.setReturnMsg(purchaseResDto.getMsg());
				if (purchaseResDto.getFlag().equals(BaseConsts.FLAG_NO)) { // 返回结果N，调用失败
					invokeLog.setTryAgainFlag(BaseConsts.ONE);
					invokeError(invokeLog);
				} else {
					invokeSuccess(invokeLog);
				}
			}
		} catch (Exception e) {
			LOGGER.error("调用退货订单确认回传接口：", e);
			invokeLog.setTryAgainFlag(BaseConsts.ONE);
			invokeException(invokeLog, e);
		}
	}

	/**
	 * CMS上传付款信息接口
	 * 
	 * @param invokeLog
	 */
	public void invokeCmsPay(InvokeLog invokeLog, boolean isJob) {
		try {
			if (invokeLog != null) {
				PayCmsHttpResDto payCmsHttpResDto = cmsPayRpcService.invokePay(invokeLog.getContent());
				if (null != payCmsHttpResDto) {
					invokeLog.setReturnMsg(payCmsHttpResDto.getMsg());
				}
				if (null == payCmsHttpResDto || payCmsHttpResDto.getAck().equals(BaseConsts.FLAG_NO)) { // 返回结果N，调用失败
					invokeLog.setTryAgainFlag(BaseConsts.ONE);
					invokeError(invokeLog);
				} else {
					invokeSuccess(invokeLog);
				}
			}
		} catch (Exception e) {
			LOGGER.error("调用CMS上传付款信息接口：", e);
			invokeLog.setTryAgainFlag(BaseConsts.ONE);
			invokeException(invokeLog, e);
		}
	}

	/**
	 * TODO 重新处理接口(废弃)
	 * 
	 * @param invokeLogSearchReqDto
	 */
	public void reDeal(InvokeLogSearchReqDto invokeLogSearchReqDto) {
		InvokeLog invokeLog = invokeLogDao.queryAndLockEntityById(invokeLogSearchReqDto.getId());
		String data = invokeLog.getContent();
		if (StringUtils.isBlank(data)) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "JSON数据不能为空");
		}
		if (invokeLog.getProvider().equals(BaseConsts.ONE) && invokeLog.getIsSuccess().equals(BaseConsts.ONE)
				&& !invokeLog.getDealFlag().equals(BaseConsts.TWO)
				&& invokeLog.getDealAgainFlag().equals(BaseConsts.ONE)) { // 提供者scfs，且调用接口成功，且未处理或处理失败，且可重新处理
			invokeLog.setDealNum(invokeLog.getDealNum() + 1);
		}
	}

	/**
	 * 调度任务处理
	 */
	public void excThirdParty() {
		List<InvokeLog> invokeLogList = invokeLogDao.queryExcResults();
		if (!CollectionUtils.isEmpty(invokeLogList)) {
			if (invokeLogList.size() > 100) {
				invokeLogList = invokeLogList.subList(0, 100);
			}
			for (InvokeLog invokeLog : invokeLogList) {
				InvokeLogSearchReqDto invokeLogSearchReqDto = new InvokeLogSearchReqDto();
				invokeLogSearchReqDto.setId(invokeLog.getId());
				reInvoke(invokeLogSearchReqDto, true);
			}
		}
	}

	/**
	 * 新增接口调用日志
	 * 
	 * @param invokeLog
	 * @return
	 */
	public InvokeLog add(InvokeLog invokeLog) {
		invokeLogDao.insert(invokeLog);
		return invokeLog;
	}

	/**
	 * 更新接口调用日志
	 * 
	 * @param invokeLog
	 * @return
	 */
	public InvokeLog update(InvokeLog invokeLog) {
		invokeLogDao.updateById(invokeLog);
		return invokeLog;
	}

	/**
	 * 接口调用失败
	 * 
	 * @param invokeLog
	 * @return
	 */
	public InvokeLog invokeError(InvokeLog invokeLog) {
		invokeLog.setContent(null);
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setReturnMsg(getMsg(invokeLog.getReturnMsg()));
		invokeLogDao.updateById(invokeLog);
		sendSystemAlarm(invokeLog);
		return invokeLog;
	}

	/**
	 * 接口调用成功
	 * 
	 * @param invokeLog
	 * @return
	 */
	public InvokeLog invokeSuccess(InvokeLog invokeLog) {
		invokeLog.setContent(null);
		invokeLog.setIsSuccess(BaseConsts.TWO);
		invokeLog.setTryAgainFlag(BaseConsts.ZERO);
		invokeLogDao.updateById(invokeLog);
		return invokeLog;
	}

	/**
	 * 接口异常
	 * 
	 * @param invokeLog
	 * @param e
	 *            异常
	 * @return
	 */
	public InvokeLog invokeException(InvokeLog invokeLog, Exception e) {
		invokeLog.setContent(null);
		invokeLog.setIsSuccess(BaseConsts.ONE);
		invokeLog.setExceptionMsg(getExceptionMsg(e));
		invokeLogDao.updateById(invokeLog);
		sendSystemAlarm(invokeLog);
		return invokeLog;
	}

	/**
	 * 处理失败
	 * 
	 * @param invokeLog
	 * @return
	 */
	public InvokeLog dealError(InvokeLog invokeLog) {
		invokeLog.setContent(null);
		invokeLog.setDealFlag(BaseConsts.ONE);
		invokeLog.setDealMsg(getMsg(invokeLog.getDealMsg()));
		invokeLogDao.updateById(invokeLog);
		sendSystemAlarm(invokeLog);
		return invokeLog;
	}

	/**
	 * 处理成功
	 * 
	 * @param invokeLog
	 * @return
	 */
	public InvokeLog dealSuccess(InvokeLog invokeLog) {
		invokeLog.setContent(null);
		invokeLog.setDealFlag(BaseConsts.TWO);
		invokeLogDao.updateById(invokeLog);
		return invokeLog;
	}

	/**
	 * 发送系统报警(RTX和邮件)
	 * 
	 * @param invokeLog
	 */
	@IgnoreTransactionalMark
	public void sendSystemAlarm(InvokeLog invokeLog) {
		if (null != invokeLog) {
			String id = invokeLog.getId().toString();
			String providerName = ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_SYSTEM,
					Integer.toString(invokeLog.getProvider()));
			String consumerName = ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_SYSTEM,
					Integer.toString(invokeLog.getConsumer()));
			String invokeTypeName = ServiceSupport.getValueByBizCode(BizCodeConsts.INVOKE_TYPE,
					Integer.toString(invokeLog.getInvokeType()));

			String title = MessageFormatter.arrayFormat("[ID={}]{}异常", new String[] { id, invokeTypeName })
					.getMessage();
			String msg = "未知错误";
			if (invokeLog.getIsSuccess().equals(BaseConsts.ONE)
					&& (null == invokeLog.getTryNum() || invokeLog.getTryNum() == 0)) { // 调用接口失败，且调用重试次数为0
				title = MessageFormatter.arrayFormat("[ID={}]{}调用{}的{}失败",
						new String[] { id, consumerName, providerName, invokeTypeName }).getMessage();
				msg = invokeLog.getReturnMsg();
				if (StringUtils.isBlank(msg)) {
					msg = invokeLog.getExceptionMsg();
				}
			}
			msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, msg, BaseConsts.ONE);
			msgContentService.addMsgContentByRoleName(BaseConsts.SYSTEM_ROLE_NAME, title, msg, BaseConsts.TWO);
		}
	}

	private String getExceptionMsg(Exception e) {
		String exceptionMsg = e.getMessage();
		if (StringUtils.isNotBlank(exceptionMsg)) {
			exceptionMsg = exceptionMsg.substring(0, exceptionMsg.length() > 500 ? 500 : exceptionMsg.length());
		}
		return exceptionMsg;
	}

	public String getMsg(String msg) {
		if (StringUtils.isNotBlank(msg)) {
			msg = msg.substring(0, msg.length() > 255 ? 255 : msg.length());
		}
		return msg;
	}
}
