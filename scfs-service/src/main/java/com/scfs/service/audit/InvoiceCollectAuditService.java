package com.scfs.service.audit;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.dao.audit.AuditDao;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.InvoiceCollectAuditInfo;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectFeeResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectPoResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceCollectResDto;
import com.scfs.domain.invoice.entity.InvoiceCollect;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.bookkeeping.InvoiceCollectBookkeepingService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.invoice.InvoiceCollectFeeService;
import com.scfs.service.invoice.InvoiceCollectPoService;
import com.scfs.service.invoice.InvoiceCollectService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: InvoiceCollectAuditService.java
 *  Description: 收票审核
 *  TODO
 *  Date,					Who,				
 *  2016年12月1日			Administrator
 *
 * </pre>
 */
@Service
public class InvoiceCollectAuditService extends AuditService {
	@Autowired
	private InvoiceCollectService invoiceCollectService;// 收票
	@Autowired
	private InvoiceCollectFeeService invoiceCollectFeeService;// 收票费用
	@Autowired
	private InvoiceCollectPoService invoiceCollectPoService;// 收票采购
	@Autowired
	private CacheService cacheService;
	@Autowired
	private InvoiceCollectBookkeepingService invoiceCollectBookkeepingService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private AuditFlowService auditFlowService;

	@Override
	public void batchPassAudit(Audit audit) {
		ProjectItemReqDto projectItemReqDto = new ProjectItemReqDto();
		projectItemReqDto.setAuditId(audit.getId());
		if (audit.getState() == BaseConsts.INT_25) {
			passFinanceAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_30) {
			passFinance2Audit(projectItemReqDto);
		}
	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		ProjectItemReqDto projectItemReqDto = new ProjectItemReqDto();
		projectItemReqDto.setAuditId(audit.getId());
		unPassAudit(projectItemReqDto);
	}

	public Integer getState(AuditNode auditNode) {
		Integer state = null;
		if (null != auditNode) { // 中间审核节点
			state = auditNode.getAuditNodeState();
		} else { // 最后一个审核节点
			state = BaseConsts.THREE;
		}
		return state;
	}

	public void sendMessage(AuditNode auditNode, ProjectItemReqDto poAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "收票申请审核单据", "收票申请财务专员审核通过");
			sendWechatMsg(newId, "您有新的收票单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "收票申请审核单据", "收票申请财务主管审核通过");
		}
	}

	/**
	 * 获取信息
	 * 
	 * @param payId
	 * @return
	 */
	public Result<InvoiceCollectAuditInfo> queryCollectAuditInfo(Integer collectId) {
		Result<InvoiceCollectAuditInfo> result = new Result<InvoiceCollectAuditInfo>();
		InvoiceCollectResDto invoiceCollect = invoiceCollectService.queryInvoiceCollectById(collectId);
		List<InvoiceCollectFeeResDto> invoiceCollectFeeList = invoiceCollectFeeService
				.queryInvoiceCollectFeeByCollectId(collectId);
		List<InvoiceCollectPoResDto> invoiceCollectPoList = invoiceCollectPoService
				.queryInvoiceCollectPoByCollectId(collectId);
		List<InvoiceCollectFileResDto> invoiceCollectFileList = invoiceCollectService.queryFileList(collectId);
		InvoiceCollectAuditInfo auditInfo = new InvoiceCollectAuditInfo();
		auditInfo.setInvoiceCollect(invoiceCollect);
		auditInfo.setInvoiceCollectFeeList(invoiceCollectFeeList);
		auditInfo.setInvoiceCollectPoList(invoiceCollectPoList);
		auditInfo.setInvoiceCollectFileList(invoiceCollectFileList);
		result.setItems(auditInfo);
		return result;
	}

	/**
	 * 财务专员审核通过
	 */
	public void passFinanceAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_25 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_25, audit.getPoType(),
					audit.getProjectId());

			// 业务逻辑处理
			updateCollectState(audit.getPoId(), getState(nextAuditNode)); // 待认证

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务主管审核通过
	 */
	public void passFinance2Audit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_30 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_30, audit.getPoType(),
					audit.getProjectId());

			// 业务逻辑处理
			updateCollectState(audit.getPoId(), getState(nextAuditNode)); // 待认证
			invoiceCollectBookkeepingService.collectBookkeeping(audit.getPoId());

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 审核不通过
	 */
	public void unPassAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.TWO);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态,终止流程
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			updateCollectState(audit.getPoId(), BaseConsts.ONE); // 业务单据状态：待提交
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "收票申请审核单据", "收票申请审核不通过");
		}
	}

	/**
	 * 加签
	 */
	public void sighAudit(ProjectItemReqDto poAuditReqDto) {
		if (poAuditReqDto.getAuditId() == null || poAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(poAuditReqDto.getAuditId(), poAuditReqDto.getPauditorId());
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的收票单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(ProjectItemReqDto poAuditReqDto) {
		if (poAuditReqDto.getAuditId() == null || poAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(poAuditReqDto.getAuditId(), poAuditReqDto.getPauditorId());
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.THREE); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的收票单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param purchaseOrderTitle
	 */
	public void startAudit(InvoiceCollect collect, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setSupplierId(collect.getSupplierId());
		audit.setPoDate(collect.getInvoiceDate());
		audit.setPoId(collect.getId());
		audit.setPoNo(collect.getApplyNo());
		audit.setProjectId(collect.getProjectId());
		audit.setBusinessUnitId(collect.getBusinessUnit());
		audit.setAmount(collect.getInvoiceAmount());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoType(BaseConsts.INT_11); // 11表示收票
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(collect.getId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
		sendWechatMsg(newId, "您有新的收票申请单审核【" + audit.getPoNo() + "】");
	}

	public void updateCollectState(Integer id, Integer state) {
		InvoiceCollect invoiceCollect = new InvoiceCollect();
		invoiceCollect.setId(id);
		invoiceCollect.setState(state);
		invoiceCollectService.updateInvoiceCollectById(invoiceCollect);
	}

	/**
	 * 获取节点信息
	 * 
	 * @param projectItemId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectItemId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.INT_11;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(projectItemId, poType, auditflows);
		return result;
	}

	private void sendMailMsg(Integer collectId, Integer auditId, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		InvoiceCollectResDto invoiceCollect = invoiceCollectService.queryInvoiceCollectById(collectId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		String auditTypeStr = "";
		switch (auditType) {
		case BaseConsts.ONE:
			auditTypeStr = "转交";
			break;
		case BaseConsts.TWO:
			auditTypeStr = "加签";
			break;
		case BaseConsts.THREE:
			auditTypeStr = "转交";
			break;
		default:
			auditTypeStr = "转交";
			break;
		}
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("收票单编号");
		mail1.setColumnTwo(invoiceCollect.getApplyNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne(auditTypeStr + "信息");
		mail2.setColumnTwo("单据由" + fromUser.getChineseName() + "于"
				+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
				+ toUser.getChineseName() + "审核");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne(auditTypeStr + "人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("收票单财务审核" + auditTypeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "收票单财务审核", content);
	}

	private void sendMailResult(Integer collectId, Integer auditId, Integer auditState) {
		InvoiceCollectResDto invoiceCollect = invoiceCollectService.queryInvoiceCollectById(collectId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("收票单编号");
		mail1.setColumnTwo(invoiceCollect.getApplyNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核结果");
		mail2.setColumnTwo(auditState.equals(BaseConsts.ONE) ? "审核通过" : "审核不通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("收票单财务审核结果", templateOnes, null);
		sendWarnMail(toUser.getId(), "收票单财务审核", content);
	}
}
