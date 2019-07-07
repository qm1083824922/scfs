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
import com.scfs.domain.audit.model.MatterManageAuditInfo;
import com.scfs.domain.base.dto.resp.MatterManageFileResDto;
import com.scfs.domain.base.dto.resp.MatterManageResDto;
import com.scfs.domain.base.dto.resp.MatterServiceResDto;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.MatterManage;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.base.customer.MatterManageService;
import com.scfs.service.base.customer.MatterServiceService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 	
 *  File: MatterManageAuditService.java
 *  Description:事项管理票审核业务
 *  TODO
 *  Date,					Who,				
 *  2017年08月05日				Administrator
 *
 * </pre>
 */
@Service
public class MatterManageAuditService extends AuditService {
	@Autowired
	private MatterManageService matterManageService;
	@Autowired
	private MatterServiceService matterServiceService;
	@Autowired
	private CacheService cacheService;
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
		if (audit.getState() == BaseConsts.INT_20) {// 业务审核
			passBusiAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.TEN) {// 商务审核
			passBizAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_80) {// 部门主管审核
			passDeptManageAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_11) {// 法务审核
			passJusticeAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_30) {// 财务主管审核
			passFinanceAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_40) {// 分控主管审核
			passRiskAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_90) {// 总经理审核
			passBossAudit(projectItemReqDto);
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
			state = BaseConsts.TWO;
		}
		return state;
	}

	public void sendMessage(AuditNode auditNode, ProjectItemReqDto poAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			sendWechatMsg(newId, "您有新的事项管理单审核【" + audit.getPoNo() + "】");
			sendWarnRtx(audit.getAuditorId(), "事项管理申请审核单据", "事项管理商务审核通过");
		} else { // 最后一个审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "事项管理申请审核单据", "事项管理申请总经理审核通过");
		}
	}

	/**
	 * 获取信息
	 * 
	 * @param payId
	 * @return
	 */
	public Result<MatterManageAuditInfo> queryMatterManageAuditInfo(Integer matterManageId) {
		Result<MatterManageAuditInfo> result = new Result<MatterManageAuditInfo>();
		MatterManageResDto matterManage = matterManageService.queryMatterManageById(matterManageId); // 基础信息
		MatterServiceResDto matterService = matterServiceService.queryMatterServiceById(matterManageId);// 服务要求
		List<MatterManageFileResDto> matterManageFileList = matterManageService.queryFileList(matterManageId);// 附件
		MatterManageAuditInfo auditInfo = new MatterManageAuditInfo();
		auditInfo.setMatterManage(matterManage);
		auditInfo.setMatterService(matterService);
		auditInfo.setMatterManageFileList(matterManageFileList);
		result.setItems(auditInfo);
		return result;
	}

	/**
	 * 业务审核通过
	 */
	public void passBusiAudit(ProjectItemReqDto poAuditReqDto) {
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
			// 业务逻辑处理
			updateMatterManageState(audit.getPoId(), this.getState(nextAuditNode)); // 待认证

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 商务审核通过
	 */
	public void passBizAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.TEN || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.TEN, audit.getPoType(),
					audit.getProjectId());
			// 业务逻辑处理
			updateMatterManageState(audit.getPoId(), this.getState(nextAuditNode));

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
			// 业务逻辑处理
			updateMatterManageState(audit.getPoId(), this.getState(nextAuditNode));

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 法务审核通过
	 */
	public void passJusticeAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_11 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_11, audit.getPoType(),
					audit.getProjectId());
			// 业务逻辑处理
			updateMatterManageState(audit.getPoId(), this.getState(nextAuditNode));

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务主管审核通过
	 */
	public void passFinanceAudit(ProjectItemReqDto poAuditReqDto) {
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
			updateMatterManageState(audit.getPoId(), this.getState(nextAuditNode));

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 风控审核通过
	 * 
	 * @param poAuditReqDto
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
			// 业务逻辑处理
			updateMatterManageState(audit.getPoId(), this.getState(nextAuditNode));

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 总经理审核通过
	 */
	public void passBossAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_90 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId()); // 业务逻辑处理
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_90, audit.getPoType(),
					audit.getProjectId());
			updateMatterManageState(audit.getPoId(), this.getState(nextAuditNode)); // 待认证

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
			updateMatterManageState(audit.getPoId(), BaseConsts.ZERO); // 业务单据状态：待提交
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "事项管理申请审核单据", "事项管理申请审核不通过");
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
		sendWechatMsg(newId, "您有加签的事项管理单审核【" + newAudit.getPoNo() + "】");
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
		sendWechatMsg(newId, "您有转交的事项管理单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param purchaseOrderTitle
	 */
	public void startAudit(MatterManage matterManage, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setPoDate(matterManage.getCreateAt());
		audit.setProjectId(matterManage.getProjectId());
		audit.setPoId(matterManage.getId());
		audit.setPoNo(matterManage.getMatterNo());
		audit.setProjectId(matterManage.getProjectId());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoType(BaseConsts.INT_27); // 27-表示事项管理
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程

		sendMailMsg(matterManage.getId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
		sendWechatMsg(newId, "您有新的事项管理单审核【" + audit.getPoNo() + "】");
	}

	public void updateMatterManageState(Integer id, Integer state) {
		MatterManage matterManage = new MatterManage();
		matterManage.setId(id);
		matterManage.setState(state);
		matterManageService.updateMatterManage(matterManage);
	}

	/**
	 * 获取节点信息
	 * 
	 * @param projectItemId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectItemId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.INT_27;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(projectItemId, poType, auditflows);
		return result;
	}

	private void sendMailMsg(Integer matterId, Integer auditId, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		MatterManageResDto matterManageResDto = matterManageService.queryMatterManageById(matterId);
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
		mail1.setColumnOne("事项管理单编号");
		mail1.setColumnTwo(matterManageResDto.getMatterNo());
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
		String content = msgContentService.convertMailOneContent("事项管理单财务审核" + auditTypeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "事项管理单财务审核", content);
	}

	private void sendMailResult(Integer matterId, Integer auditId, Integer auditState) {
		MatterManageResDto matterManageResDto = matterManageService.queryMatterManageById(matterId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("事项管理编号");
		mail1.setColumnTwo(matterManageResDto.getMatterNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核结果");
		mail2.setColumnTwo(auditState.equals(BaseConsts.ONE) ? "审核通过" : "审核不通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("事项管理单财务审核结果", templateOnes, null);
		sendWarnMail(toUser.getId(), "事项管理单财务审核", content);
	}

}
