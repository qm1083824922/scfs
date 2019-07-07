package com.scfs.service.audit;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.audit.AuditDao;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: ProjectItemAuditService.java
 *  Description: 条款
 *  TODO
 *  Date,					Who,				
 *  2016年11月18日			Administrator
 *
 * </pre>
 */
@Service
public class ProjectItemAuditService extends AuditService {

	@Autowired
	private CacheService cacheService;
	@Autowired
	private ProjectItemService projectItemService;
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
		projectItemReqDto.setProjectItemId(audit.getPoId());
		if (audit.getState() == BaseConsts.INT_20) {
			passBusAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_30) {
			passFinance2Audit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_40) {
			passRiskAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_80) {
			passDeptManageAudit(projectItemReqDto);
		}
	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		ProjectItemReqDto projectItemReqDto = new ProjectItemReqDto();
		projectItemReqDto.setAuditId(audit.getId());
		projectItemReqDto.setProjectItemId(audit.getPoId());
		unPassAudit(projectItemReqDto);
	}

	public void sendMessage(AuditNode auditNode, ProjectItemReqDto poAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailMsg(newId);// 发邮件
			sendRTXMsg(newId);// 发RTX
			sendWechatMsg(newId, "您有新的条款单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点

		}
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param payOrder
	 */
	public void startAudit(ProjectItem payOrder, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setPoDate(payOrder.getCreateAt());
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoId(payOrder.getId());
		audit.setPoNo(payOrder.getItemNo());
		audit.setProjectId(payOrder.getProjectId());
		audit.setBusinessUnitId(payOrder.getBusinessUnitId());
		audit.setAmount(payOrder.getTotalAmount());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposerAt(new Date());
		audit.setPoType(BaseConsts.SEVEN); // 7表示条款
		audit.setCurrencyId(payOrder.getAmountCurrency());// 额度总额单位
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(newId);// 发邮件
		sendRTXMsg(newId);// 发RTX
		sendWechatMsg(newId, "您有新的条款单审核【" + audit.getPoNo() + "】");
	}

	/**
	 * 业务审核通过
	 */
	public void passBusAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_20 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_20, audit.getPoType(),
					audit.getProjectId());

			projectItemService.updateProjectItemState4BusAudit(poAuditReqDto.getProjectItemId(), nextAuditNode);

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

			projectItemService.updateProjectItemState4FinanceAudit(poAuditReqDto.getProjectItemId(), nextAuditNode);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 风控主管审核通过
	 */
	public void passRiskAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_40 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_40, audit.getPoType(),
					audit.getProjectId());

			projectItemService.updateProjectItemState4RiskAudit(poAuditReqDto.getProjectItemId(), nextAuditNode);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 部门主管审核通过
	 */
	public void passDeptManageAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_80 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_80, audit.getPoType(),
					audit.getProjectId());

			projectItemService.updateProjectItemState4DeptManageAudit(poAuditReqDto.getProjectItemId(), nextAuditNode);

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
			projectItemService.updateProjectItemState4UnPassAudit(poAuditReqDto.getProjectItemId());
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
		sendMailMsg(newId);// 发邮件
		sendRTXMsg(newId);// 发RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的条款单审核【" + newAudit.getPoNo() + "】");

	}

	/**
	 * 转交
	 */
	public void deliverAudit(ProjectItemReqDto poAuditReqDto) {
		if (poAuditReqDto.getAuditId() == null || poAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(poAuditReqDto.getAuditId(), poAuditReqDto.getPauditorId());
		sendMailMsg(newId);// 发邮件
		sendRTXMsg(newId);// 发RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的条款单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 获取信息
	 * 
	 * @param projectItemId
	 * @return
	 */
	public Result<ProjectItem> queryProjectItemAuditInfo(Integer projectItemId) {
		Result<ProjectItem> result = new Result<ProjectItem>();
		ProjectItem projectItem = new ProjectItem();
		projectItem.setId(projectItemId);
		ProjectItem payDto = projectItemService.detailProjectItemById(projectItem);

		PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
		payFeeRelationReqDto.setPayId(projectItemId);
		result.setItems(payDto);
		return result;
	}

	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectItemId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.SEVEN;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(projectItemId, poType, auditflows);
		return result;
	}

	private void sendMailMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);

		BaseUser fromUser = ServiceSupport.getUser();
		ProjectItem projectItem = new ProjectItem();
		projectItem.setId(newAudit.getPoId());
		ProjectItem itemDto = projectItemService.detailProjectItemById(projectItem);
		BaseUser toUser = cacheService.getUserByid(newAudit.getAuditorId());

		String auditTypeStr = "";
		switch (newAudit.getAuditType()) {
		case BaseConsts.TWO:
			auditTypeStr = "【转交】";
			break;
		case BaseConsts.THREE:
			auditTypeStr = "【加签】";
			break;
		default:
			auditTypeStr = "";
			break;
		}

		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("条款编号");
		mail1.setColumnTwo(itemDto.getItemNo());
		templateOnes.add(mail1);

		MailTemplateOne mail4 = new MailTemplateOne();
		mail4.setColumnOne("项目");
		mail4.setColumnTwo(itemDto.getProjectName());
		templateOnes.add(mail4);

		MailTemplateOne mail5 = new MailTemplateOne();
		mail5.setColumnOne("条款额度");
		mail5.setColumnTwo(DecimalUtil.toAmountString(itemDto.getTotalAmount()) + itemDto.getAmountCurrencyValue());
		templateOnes.add(mail5);

		if (!auditTypeStr.equals("")) {
			MailTemplateOne mail6 = new MailTemplateOne();
			mail6.setColumnOne(auditTypeStr + "信息");
			mail6.setColumnTwo("单据由" + fromUser.getChineseName() + "于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
					+ toUser.getChineseName() + "审核");
			templateOnes.add(mail6);
		}

		String typeStr = ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, newAudit.getState() + "");
		String content = msgContentService.convertMailOneContent(auditTypeStr + "条款" + typeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【条款】需要审核", content);
	}

	private void sendRTXMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);

		BaseUser fromUser = ServiceSupport.getUser();
		ProjectItem projectItem = new ProjectItem();
		projectItem.setId(newAudit.getPoId());
		ProjectItem itemDto = projectItemService.detailProjectItemById(projectItem);
		BaseUser toUser = cacheService.getUserByid(newAudit.getAuditorId());

		String auditTypeStr = "";
		switch (newAudit.getAuditType()) {
		case BaseConsts.TWO:
			auditTypeStr = "【转交】";
			break;
		case BaseConsts.THREE:
			auditTypeStr = "【加签】";
			break;
		default:
			auditTypeStr = "";
			break;
		}

		String content = "";

		content = content + "条款编号:" + itemDto.getItemNo() + "\n";
		content = content + "项目:" + itemDto.getProjectName() + "\n";
		content = content + "条款额度:" + DecimalUtil.toAmountString(itemDto.getTotalAmount())
				+ itemDto.getAmountCurrencyValue() + "\n";
		if (!auditTypeStr.equals("")) {
			content = content + auditTypeStr + "信息:" + "单据由" + fromUser.getChineseName() + "于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
					+ toUser.getChineseName() + "审核\n";
		}
		content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
		content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

		sendWarnRtx(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【条款】需要审核", content);
	}

}
