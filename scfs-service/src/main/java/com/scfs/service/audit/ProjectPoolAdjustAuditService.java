package com.scfs.service.audit;

import java.util.ArrayList;
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
import com.scfs.domain.audit.dto.req.BaseAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.ProjectPoolAdjustAuditInfo;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectPoolAdjustFileDto;
import com.scfs.domain.project.dto.resp.ProjectPoolAdjustResDto;
import com.scfs.domain.project.dto.resp.ProjectPoolResDto;
import com.scfs.domain.project.entity.ProjectPoolAdjust;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.project.ProjectPoolAdjustService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: ProjectPoolAdjustAuditService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月23日				Administrator
 *
 * </pre>
 */

@Service
public class ProjectPoolAdjustAuditService extends AuditService {
	@Autowired
	private CacheService cacheService;
	@Autowired
	private ProjectPoolAdjustService projectPoolAdjustService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private ProjectPoolService projectPoolService;
	@Autowired
	private AuditFlowService auditFlowService;

	@Override
	public void batchPassAudit(Audit audit) {
		BaseAuditReqDto baseAuditReqDto = new BaseAuditReqDto();
		baseAuditReqDto.setAuditId(audit.getId());
		baseAuditReqDto.setPoId(audit.getPoId());
		if (audit.getState() == BaseConsts.INT_20) {
			passBusAudit(baseAuditReqDto);
		} else if (audit.getState() == BaseConsts.INT_30) {
			passFinance2Audit(baseAuditReqDto);
		} else if (audit.getState() == BaseConsts.INT_40) {
			passRiskAudit(baseAuditReqDto);
		} else if (audit.getState() == BaseConsts.INT_80) {
			passDeptManageAudit(baseAuditReqDto);
		}
	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		BaseAuditReqDto baseAuditReqDto = new BaseAuditReqDto();
		baseAuditReqDto.setAuditId(audit.getId());
		baseAuditReqDto.setPoId(audit.getPoId());
		unPassAudit(baseAuditReqDto);
	}

	public void sendMessage(AuditNode auditNode, BaseAuditReqDto baseAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailMsg(newId);// 发邮件
			sendRTXMsg(newId);// 发RTX
			sendWechatMsg(newId, "您有新的临时额度申请单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点

		}
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param payOrder
	 */
	public void startAudit(ProjectPoolAdjust projectPoolAdjust, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setPoDate(projectPoolAdjust.getCreateAt());
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoId(projectPoolAdjust.getId());
		audit.setPoNo(projectPoolAdjust.getAdjustNo());
		audit.setProjectId(projectPoolAdjust.getProjectId());
		BaseProject baseProject = cacheService.getProjectById(projectPoolAdjust.getProjectId());
		audit.setBusinessUnitId(baseProject.getBusinessUnitId());
		audit.setAmount(projectPoolAdjust.getAdjustAmount());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposerAt(new Date());
		audit.setPoType(BaseConsts.INT_16); // 16表示临时额度申请单
		audit.setCurrencyId(projectPoolAdjust.getCurrencyType());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(newId);// 发邮件
		sendRTXMsg(newId);// 发RTX
		sendWechatMsg(newId, "您有新的临时额度申请单审核【" + audit.getPoNo() + "】");
	}

	/**
	 * 业务审核通过
	 */
	public void passBusAudit(BaseAuditReqDto baseAuditReqDto) {
		Audit audit = auditDao.queryAuditById(baseAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_20 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(baseAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_20, audit.getPoType(),
					audit.getProjectId());

			projectPoolAdjustService.updateStatePassBusAudit(baseAuditReqDto.getPoId(), nextAuditNode);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, baseAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务主管审核通过
	 */
	public void passFinance2Audit(BaseAuditReqDto baseAuditReqDto) {
		Audit audit = auditDao.queryAuditById(baseAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_30 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(baseAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_30, audit.getPoType(),
					audit.getProjectId());

			projectPoolAdjustService.updateStatePassFinance2Audit(baseAuditReqDto.getPoId(), nextAuditNode);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, baseAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 风控主管审核通过
	 */
	public void passRiskAudit(BaseAuditReqDto baseAuditReqDto) {
		Audit audit = auditDao.queryAuditById(baseAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_40 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(baseAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_40, audit.getPoType(),
					audit.getProjectId());

			projectPoolAdjustService.updateStatePassRiskAudit(baseAuditReqDto.getPoId(), nextAuditNode);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, baseAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 部门主管审核通过
	 */
	public void passDeptManageAudit(BaseAuditReqDto baseAuditReqDto) {
		Audit audit = auditDao.queryAuditById(baseAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_80 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(baseAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_80, audit.getPoType(),
					audit.getProjectId());

			projectPoolAdjustService.updateStatePassDeptManageAudit(baseAuditReqDto.getPoId(), nextAuditNode);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, baseAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 审核不通过
	 */
	public void unPassAudit(BaseAuditReqDto baseAuditReqDto) {
		Audit audit = auditDao.queryAuditById(baseAuditReqDto.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.TWO);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(baseAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态,终止流程
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			projectPoolAdjustService.updateStateUnPassAudit(baseAuditReqDto.getPoId());
		}
	}

	/**
	 * 加签
	 */
	public void sighAudit(BaseAuditReqDto baseAuditReqDto) {
		if (baseAuditReqDto.getAuditId() == null || baseAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(baseAuditReqDto.getAuditId(), baseAuditReqDto.getPauditorId());
		sendMailMsg(newId);// 发邮件
		sendRTXMsg(newId);// 发RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的临时额度申请单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(BaseAuditReqDto baseAuditReqDto) {
		if (baseAuditReqDto.getAuditId() == null || baseAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(baseAuditReqDto.getAuditId(), baseAuditReqDto.getPauditorId());
		sendMailMsg(newId);// 发邮件
		sendRTXMsg(newId);// 发RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的临时额度申请单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 获取信息
	 * 
	 * @param projectItemId
	 * @return
	 */
	public Result<ProjectPoolAdjustAuditInfo> queryProjectPoolAdjustAuditInfo(Integer poId) {
		Result<ProjectPoolAdjustAuditInfo> result = new Result<ProjectPoolAdjustAuditInfo>();
		ProjectPoolAdjustAuditInfo auditInfo = new ProjectPoolAdjustAuditInfo();
		List<ProjectPoolResDto> projectPoolResDtos = new ArrayList<ProjectPoolResDto>();
		ProjectPoolAdjustResDto itemDto = projectPoolAdjustService.detailProjectPoolAdjustById(poId);
		try {
			ProjectPoolResDto projectPool = projectPoolService.detailProjectItemByProjectId(itemDto.getProjectId());
			projectPoolResDtos.add(projectPool);
		} catch (Exception e) {
			result.setMsg("融资池查询出错");
		}
		FileAttachSearchReqDto fileAttachSearchReqDto = new FileAttachSearchReqDto();
		fileAttachSearchReqDto.setBusId(poId);
		fileAttachSearchReqDto.setBusType(BaseConsts.INT_23);
		List<ProjectPoolAdjustFileDto> projectPoolAdjustFileDtos = projectPoolAdjustService
				.queryFileListAll(fileAttachSearchReqDto);

		auditInfo.setProjectPoolAdjustResDto(itemDto);
		auditInfo.setProjectPoolResDtos(projectPoolResDtos);
		auditInfo.setProjectPoolAdjustFileDtos(projectPoolAdjustFileDtos);
		result.setItems(auditInfo);
		return result;
	}

	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectPoolAdjustId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.INT_16;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(projectPoolAdjustId, poType, auditflows);
		return result;
	}

	private void sendMailMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);

		BaseUser fromUser = ServiceSupport.getUser();
		ProjectPoolAdjustResDto itemDto = projectPoolAdjustService.detailProjectPoolAdjustById(newAudit.getPoId());
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
		mail1.setColumnOne("申请编号");
		mail1.setColumnTwo(itemDto.getAdjustNo());
		templateOnes.add(mail1);

		MailTemplateOne mail4 = new MailTemplateOne();
		mail4.setColumnOne("项目");
		mail4.setColumnTwo(itemDto.getProjectName());
		templateOnes.add(mail4);

		MailTemplateOne mail5 = new MailTemplateOne();
		mail5.setColumnOne("临时额度");
		mail5.setColumnTwo(DecimalUtil.toAmountString(itemDto.getAdjustAmount()) + itemDto.getCurrencyTypeName());
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
		sendWarnMail(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【临时额度申请】需要审核", content);
	}

	private void sendRTXMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);

		BaseUser fromUser = ServiceSupport.getUser();
		ProjectPoolAdjustResDto itemDto = projectPoolAdjustService.detailProjectPoolAdjustById(newAudit.getPoId());
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

		content = content + "临时额度申请编号:" + itemDto.getAdjustNo() + "\n";
		content = content + "项目:" + itemDto.getProjectName() + "\n";
		content = content + "临时额度:" + DecimalUtil.toAmountString(itemDto.getAdjustAmount())
				+ itemDto.getCurrencyTypeName() + "\n";
		if (!auditTypeStr.equals("")) {
			content = content + auditTypeStr + "信息:" + "单据由" + fromUser.getChineseName() + "于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
					+ toUser.getChineseName() + "审核\n";
		}
		content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
		content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

		sendWarnRtx(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【临时额度申请】需要审核", content);
	}

}
