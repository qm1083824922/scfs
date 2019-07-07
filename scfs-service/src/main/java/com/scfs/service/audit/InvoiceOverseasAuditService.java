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
import com.scfs.domain.audit.model.InvoiceOverseasAuditInfo;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasFeeReqDto;
import com.scfs.domain.invoice.dto.req.InvoiceOverseasPoReqDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFeeResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasFileResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasPoResDto;
import com.scfs.domain.invoice.dto.resp.InvoiceOverseasResDto;
import com.scfs.domain.invoice.entity.InvoiceOverseas;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.bookkeeping.InvoiceCollectBookkeepingService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.invoice.InvoiceOverseasFeeService;
import com.scfs.service.invoice.InvoiceOverseasPoService;
import com.scfs.service.invoice.InvoiceOverseasService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 	
 *  File: InvoiceOverseasAuditService.java
 *  Description:境外发票审核业务
 *  TODO
 *  Date,					Who,				
 *  2017年03月29日				Administrator
 *
 * </pre>
 */
@Service
public class InvoiceOverseasAuditService extends AuditService {
	@Autowired
	private InvoiceOverseasService overseasService;
	@Autowired
	private InvoiceOverseasPoService overseasPoService;
	@Autowired
	private InvoiceOverseasFeeService overseasFeeService;
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
			state = BaseConsts.TWO;
		}
		return state;
	}

	public void sendMessage(AuditNode auditNode, ProjectItemReqDto poAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			sendWechatMsg(newId, "您有新的INCOICE开票单审核【" + audit.getPoNo() + "】");
			sendWarnRtx(audit.getAuditorId(), "INCOICE开票申请审核单据", "INCOICE开票申请财务专员审核通过");
		} else { // 最后一个审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "INCOICE开票申请审核单据", "INCOICE开票申请财务主管审核通过");
		}
	}

	/**
	 * 获取信息
	 * 
	 * @param payId
	 * @return
	 */
	public Result<InvoiceOverseasAuditInfo> queryOverseasAuditInfo(Integer overseasId) {
		Result<InvoiceOverseasAuditInfo> result = new Result<InvoiceOverseasAuditInfo>();
		InvoiceOverseasResDto invoiceOverseas = overseasService.queryInvoiceCollectById(overseasId);
		InvoiceOverseasFeeReqDto feeReqDto = new InvoiceOverseasFeeReqDto();
		feeReqDto.setOverseasId(overseasId);
		List<InvoiceOverseasFeeResDto> invoiceOverseasFeeList = overseasFeeService
				.queryInvoiceCollectFeeResults(feeReqDto);
		InvoiceOverseasPoReqDto poReqDto = new InvoiceOverseasPoReqDto();
		poReqDto.setOverseasId(overseasId);
		List<InvoiceOverseasPoResDto> invoiceOverseasPoList = overseasPoService.queryInvoiceCollectPoResults(poReqDto);
		List<InvoiceOverseasFileResDto> invoiceOverseasFileList = overseasService.queryFileList(overseasId);
		InvoiceOverseasAuditInfo auditInfo = new InvoiceOverseasAuditInfo();
		auditInfo.setInvoiceOverseas(invoiceOverseas);
		auditInfo.setInvoiceOverseasFeeList(invoiceOverseasFeeList);
		auditInfo.setInvoiceOverseasFileList(invoiceOverseasFileList);
		auditInfo.setInvoiceOverseasPoList(invoiceOverseasPoList);
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
			updateOverseasState(audit.getPoId(), getState(nextAuditNode)); // 待认证

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
			updateOverseasState(audit.getPoId(), getState(nextAuditNode)); // 待认证
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
			updateOverseasState(audit.getPoId(), BaseConsts.ONE); // 业务单据状态：待提交
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "INCOICE开票申请审核单据", "INCOICE开票申请审核不通过");
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
		sendWechatMsg(newId, "您有加签的INCOICE开票单审核【" + newAudit.getPoNo() + "】");
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
		sendWechatMsg(newId, "您有转交的INCOICE开票单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param purchaseOrderTitle
	 */
	public void startAudit(InvoiceOverseas overseas, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setPoDate(overseas.getCreateAt());
		audit.setPoId(overseas.getId());
		audit.setPoNo(overseas.getApplyNo());
		audit.setProjectId(overseas.getProjectId());
		audit.setBusinessUnitId(overseas.getBusinessUnit());
		audit.setCustomerId(overseas.getCustomerId());
		audit.setAmount(overseas.getInvoiceAmount());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoType(BaseConsts.INT_19); // 19-表示境外开票
		audit.setCurrencyId(overseas.getCurrnecyType());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(overseas.getId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
		sendWechatMsg(newId, "您有新的INCOICE开票单审核【" + audit.getPoNo() + "】");
	}

	public void updateOverseasState(Integer id, Integer state) {
		InvoiceOverseas invoiceOverseas = new InvoiceOverseas();
		invoiceOverseas.setId(id);
		invoiceOverseas.setState(state);
		overseasService.updateInvoiceOverseasById(invoiceOverseas);
	}

	/**
	 * 获取节点信息
	 * 
	 * @param projectItemId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectItemId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.INT_19;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(projectItemId, poType, auditflows);
		return result;
	}

	private void sendMailMsg(Integer overseasId, Integer auditId, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		InvoiceOverseasResDto invoiceOverseas = overseasService.queryInvoiceCollectById(overseasId);
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
		mail1.setColumnOne("境外开票单编号");
		mail1.setColumnTwo(invoiceOverseas.getApplyNo());
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
		String content = msgContentService.convertMailOneContent("INCOICE开票单财务审核" + auditTypeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "INCOICE开票单财务审核", content);
	}

	private void sendMailResult(Integer overseasId, Integer auditId, Integer auditState) {
		InvoiceOverseasResDto invoiceOverseas = overseasService.queryInvoiceCollectById(overseasId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("INCOICE开票编号");
		mail1.setColumnTwo(invoiceOverseas.getApplyNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核结果");
		mail2.setColumnTwo(auditState.equals(BaseConsts.ONE) ? "审核通过" : "审核不通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("INCOICE开票单财务审核结果", templateOnes, null);
		sendWarnMail(toUser.getId(), "INCOICE开票单财务审核", content);
	}

}
