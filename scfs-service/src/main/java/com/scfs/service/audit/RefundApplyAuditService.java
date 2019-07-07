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
import com.scfs.dao.audit.AuditDao;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.RefundApplyAuditInfo;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.export.dto.resp.RefundApplyLineResDto;
import com.scfs.domain.export.dto.resp.RefundApplyResDto;
import com.scfs.domain.export.entity.RefundApply;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.export.RefundApplyLineService;
import com.scfs.service.export.RefundApplyService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: RefundApplyAuditService.java
 *  Description: 退税审核
 *  TODO
 *  Date,					Who,				
 *  2016年12月07日			Administrator
 *
 * </pre>
 */
@Service
public class RefundApplyAuditService extends AuditService {
	@Autowired
	private RefundApplyService refundApplyService;
	@Autowired
	private RefundApplyLineService refundApplyLineService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private CacheService cacheService;
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
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE, audit.getState()); // 发送邮件
		} else { // 最后一个审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE, audit.getState()); // 发送邮件
		}
	}

	/**
	 * 获取信息
	 * 
	 * @param refundId
	 * @return
	 */
	public Result<RefundApplyAuditInfo> queryRefundApplyAuditInfo(Integer refundId) {
		Result<RefundApplyAuditInfo> result = new Result<RefundApplyAuditInfo>();
		RefundApplyResDto refundApply = refundApplyService.queryEntityResDtoById(refundId);
		List<RefundApplyLineResDto> refundList = refundApplyLineService.queryResultsByRefundId(refundId);
		RefundApplyAuditInfo auditInfo = new RefundApplyAuditInfo();
		auditInfo.setRefundApply(refundApply);
		auditInfo.setRefundList(refundList);
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
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO, BaseConsts.ZERO); // 发送邮件
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
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO, audit.getState()); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的退税单审核【" + newAudit.getPoNo() + "】");
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
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.THREE, audit.getState()); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的退税单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param refundApply
	 */
	public void startAudit(RefundApply refundApply, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setCustomerId(refundApply.getCustId());
		audit.setPoDate(refundApply.getRefundApplyDate());
		audit.setPoId(refundApply.getId());
		audit.setPoNo(refundApply.getRefundApplyNo());
		audit.setProjectId(refundApply.getProjectId());
		audit.setAmount(refundApply.getRefundApplyAmount());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoType(BaseConsts.INT_12); // 12表示退税
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(refundApply.getId(), audit.getAuditorId(), BaseConsts.ONE, startAuditNode.getAuditNodeState()); // 发送邮件
	}

	public void updateCollectState(Integer id, Integer state) {
		RefundApply refundApply = new RefundApply();
		refundApply.setId(id);
		refundApply.setState(state);
		refundApplyService.updateRefundApplyById(refundApply);
	}

	/**
	 * 获取节点信息
	 * 
	 * @param projectItemId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectItemId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.INT_12;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(projectItemId, poType, auditflows);
		return result;
	}

	private void sendMailMsg(Integer refundId, Integer auditId, Integer type, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		RefundApplyResDto refundApply = refundApplyService.queryEntityResDtoById(refundId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		String auditTypeStr = "";
		switch (type) {
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
		mail1.setColumnOne("退税单编号");
		mail1.setColumnTwo(refundApply.getRefundApplyNo());
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
		String content = msgContentService.convertMailOneContent("退税单"
				+ ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, auditType + "") + "审核" + auditTypeStr,
				templateOnes, null);
		sendWarnMail(toUser.getId(),
				"退税单" + ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, auditType + "") + "审核", content);
	}

	private void sendMailResult(Integer refundId, Integer auditId, Integer auditState, Integer state) {
		RefundApplyResDto refundApply = refundApplyService.queryEntityResDtoById(refundId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("退税单编号");
		mail1.setColumnTwo(refundApply.getRefundApplyNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核结果");
		mail2.setColumnTwo(auditState.equals(BaseConsts.ONE) ? "审核通过" : "审核不通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String typeStr = "";
		if (state != BaseConsts.ZERO) {
			typeStr = ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, state + "");
		}
		String content = msgContentService.convertMailOneContent("退税单" + typeStr + "审核结果", templateOnes, null);
		sendWarnMail(toUser.getId(), "退税单" + typeStr + "审核", content);
	}
}
