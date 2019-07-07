package com.scfs.service.audit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.dao.audit.AuditDao;
import com.scfs.domain.audit.dto.req.PoAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.model.PoAuditInfo;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.pay.entity.PayOrder;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.pay.PayService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.support.ServiceSupport;

/**
 * TODO 已废弃 采购单审核(新功能未做更新)
 */
@Service
public class PoAuditService extends AuditService {

	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private PayService payService;
	@Autowired
	private AuditDao auditDao;

	private static List<Integer> auditflows = new ArrayList<Integer>();
	static {
		// 顺序不能乱
		auditflows.add(BaseConsts.ZERO);
		auditflows.add(BaseConsts.INT_25);
	}

	@Override
	public void batchPassAudit(Audit audit) {
		PoAuditReqDto poAuditReqDto = new PoAuditReqDto();
		poAuditReqDto.setAuditId(audit.getId());
		if (audit.getState() == BaseConsts.INT_25) {
			passFinanceAudit(poAuditReqDto);
		}

	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		PoAuditReqDto poAuditReqDto = new PoAuditReqDto();
		poAuditReqDto.setAuditId(audit.getId());
		unPassAudit(poAuditReqDto);
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param purchaseOrderTitle
	 */
	public void startAudit(PurchaseOrderTitle purchaseOrderTitle) {
		Audit audit = new Audit();
		audit.setPoId(purchaseOrderTitle.getId());
		audit.setProjectId(purchaseOrderTitle.getProjectId());
		audit.setBusinessUnitId(purchaseOrderTitle.getBusinessUnitId());
		audit.setAmount(purchaseOrderTitle.getOrderTotalAmount());
		audit.setCustomerId(purchaseOrderTitle.getCustomerId());
		audit.setSupplierId(purchaseOrderTitle.getSupplierId());
		audit.setPoNo(purchaseOrderTitle.getOrderNo());
		audit.setPoDate(purchaseOrderTitle.getOrderTime());
		audit.setPoType(BaseConsts.ONE);
		audit.setState(BaseConsts.INT_20);
		audit.setProposer(ServiceSupport.getUser().getChineseName());// 申请人是不会变
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposerAt(purchaseOrderTitle.getCreateAt());
		audit.setAuditType(BaseConsts.ONE);
		audit.setCurrencyId(purchaseOrderTitle.getCurrencyId());
		super.createSubmitAudit(audit);// 提交节点
		super.createBusiAudit(audit);// 开始业务审核流程
		sendMail(audit, audit.getCreator(), audit.getAuditor());// 发送邮件提醒
		sendRtx(audit, audit.getCreator(), audit.getAuditor());// 发送RTX提醒
	}

	/**
	 * 业务审核通过
	 */
	public void passBusAudit(PoAuditReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.TWO || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);// 审核状态，1表示审核通过，2表示审核不通过
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		// 业务逻辑处理
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			updatePoStateById(audit.getPoId(), BaseConsts.THREE, BaseConsts.TWO);
			int newId = super.createFinanceAudit(audit);// 新增财务审核，下一节点
			sendWechatMsg(newId, "您有新的采购单审核【" + audit.getPoNo() + "】");
		}
		sendMail(audit, audit.getProposer(), audit.getAuditor());// 发送邮件提醒
		sendRtx(audit, audit.getProposer(), audit.getAuditor());// 发送RTX提醒

	}

	/**
	 * 财务审核通过
	 */
	public void passFinanceAudit(PoAuditReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.THREE || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);// 审核状态，1表示审核通过，2表示审核不通过
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		// 业务逻辑处理
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			updatePoStateById(audit.getPoId(), BaseConsts.FOUR, BaseConsts.THREE);
			int newId = super.createRiskAudit(audit);// 新增风控审核，下一节点
			sendWechatMsg(newId, "您有新的采购单审核【" + audit.getPoNo() + "】");
		}
		sendMail(audit, audit.getProposer(), audit.getAuditor());// 发送邮件提醒
		sendRtx(audit, audit.getProposer(), audit.getAuditor());// 发送RTX提醒

	}

	/**
	 * 风控审核通过
	 */
	public void passRiskAudit(PoAuditReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.FOUR || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);// 审核状态，1表示审核通过，2表示审核不通过
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		// 业务逻辑处理
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			updatePoStateById(audit.getPoId(), BaseConsts.FIVE, BaseConsts.FOUR);// 已完成状态
			sendWarnMail(audit.getProposerId(), "采购单审核通过", "SCFS系统，你提交的采购单审核已经通过");
		}

		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne line = new MailTemplateOne();
		line.setColumnOne("单据编号: ");
		line.setColumnOne(audit.getPoNo());
		templateOnes.add(line);
		MailTemplateOne line2 = new MailTemplateOne();
		line2.setColumnOne("转交信息: ");
		line2.setColumnOne("单据由" + audit.getAuditor() + "审核通过");
		templateOnes.add(line2);
		String content = msgContentService.convertMailOneContent("采购单单据转交提醒", templateOnes, null);
		sendWarnMail(audit.getAuditorId(), "采购单业务审核单据", content);
		sendWarnRtx(audit.getAuditorId(), "采购单业务审核单据", "单据由" + audit.getAuditor() + "审核通过");
	}

	/**
	 * 审核不通过
	 */
	public void unPassAudit(PoAuditReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.TWO);// 审核状态，1表示审核通过，2表示审核不通过
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态,终止流程
		// 业务逻辑处理
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			updatePoStateById(audit.getPoId(), BaseConsts.ONE, BaseConsts.ZERO);// 返回编辑状态
			sendWarnMail(audit.getProposerId(), "采购单审核不通过", "SCFS系统，你提交的采购单审核不通过");
		}

		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne line = new MailTemplateOne();
		line.setColumnOne("单据编号: ");
		line.setColumnOne(audit.getPoNo());
		templateOnes.add(line);
		MailTemplateOne line2 = new MailTemplateOne();
		line2.setColumnOne("转交信息: ");
		line2.setColumnOne("单据由" + audit.getAuditor() + "审核不通过");
		templateOnes.add(line2);
		String content = msgContentService.convertMailOneContent("采购单单据转交提醒", templateOnes, null);
		sendWarnMail(audit.getAuditorId(), "采购单业务审核单据", content);
		sendWarnRtx(audit.getAuditorId(), "采购单业务审核单据", "单据由" + audit.getAuditor() + "审核不通过");
	}

	/**
	 * 加签
	 */
	public void sighAudit(PoAuditReqDto poAuditReqDto) {
		if (poAuditReqDto.getAuditId() == null || poAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(poAuditReqDto.getAuditId(), poAuditReqDto.getPauditorId());
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的销售单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(PoAuditReqDto poAuditReqDto) {
		if (poAuditReqDto.getAuditId() == null || poAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(poAuditReqDto.getAuditId(), poAuditReqDto.getPauditorId());
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的销售单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 获取审核信息
	 * 
	 * @param poId
	 * @return
	 */
	public Result<PoAuditInfo> queryPoAuditInfoResultByPoId(Integer poId) {
		Result<PoAuditInfo> result = new Result<PoAuditInfo>();
		Result<PoTitleRespDto> poTitle = purchaseOrderService.queryPurchaseOrderTitleById(poId);
		PoTitleRespDto poTitleRespDto = poTitle.getItems();
		PoAuditInfo poAuditInfo = new PoAuditInfo();
		poAuditInfo.setPoTitleRespDto(poTitleRespDto);
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setId(poId);
		PageResult<PoLineModel> poLineList = purchaseOrderService.queryPoLinesByPoTitleId(poTitleReqDto);
		poAuditInfo.setPoLineDetailList(poLineList.getItems());
		result.setItems(poAuditInfo);
		return result;
	}

	private void updatePoStateById(Integer poId, int state, int payState) {
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		purchaseOrderTitle.setState(state);// 更新采购单状态
		purchaseOrderTitle.setId(poId);
		PurchaseOrderTitle po = purchaseOrderService.queryAndLockById(poId);

		if (po.getIsRequestPay() == BaseConsts.ONE) {// 如果是付款，则更新付款单
			Integer payId = payService.getPayIdByPoId(poId);
			// 审核不通过，删除掉
			if (payState == BaseConsts.ZERO) {
				purchaseOrderTitle.setPayAmount(BigDecimal.ZERO);
				payService.deletePayInfoByPayId(payId);
				// if(po.getPerRecAmount() != null &&
				// DecimalUtil.gt(po.getPerRecAmount(),BigDecimal.ZERO)){
				// purchaseOrderService.deleteAdvanceInfo(payId);
				// }
			} else {// 采购单完成状态
				PayOrder payOrder = new PayOrder();
				payOrder.setId(payId);
				if (state == BaseConsts.FIVE && po.getPayWay() == BaseConsts.TWO) {// 采购单的付款方式为承兑汇票，付款单为代开立
					payOrder.setState(BaseConsts.FIVE);
				} else {
					payOrder.setState(payState);
				}
				payService.updatePayOrderById(payOrder);
			}
		}
		purchaseOrderService.updatePurchaseOrderTitle(purchaseOrderTitle);

	}

	/**
	 * 发送RTX消息提醒
	 * 
	 * @param audit
	 */
	private void sendRtx(Audit audit, String commitUser, String auditorUser) {
		sendWarnRtx(audit.getAuditorId(), "采购单业务审核单据", "单据由" + commitUser + "转交给" + auditorUser + "审核");
	}

	/**
	 * 发送邮件提醒
	 * 
	 * @param audit
	 */
	private void sendMail(Audit audit, String commitUser, String auditorUser) {
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne line = new MailTemplateOne();
		line.setColumnOne("单据编号: ");
		line.setColumnOne(audit.getPoNo());
		templateOnes.add(line);
		MailTemplateOne line2 = new MailTemplateOne();
		line2.setColumnOne("转交信息: ");
		line2.setColumnOne("单据由" + commitUser + "转交给" + auditorUser + "审核");
		templateOnes.add(line2);
		String content = msgContentService.convertMailOneContent("采购单单据转交提醒", templateOnes, null);
		sendWarnMail(audit.getAuditorId(), "采购单业务审核单据", content);
	}

	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectItemId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		result = queryAuditFlowsByCon(projectItemId, BaseConsts.ONE, auditflows);
		return result;
	}
}
