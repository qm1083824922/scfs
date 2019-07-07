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
import com.scfs.domain.audit.dto.req.FeeAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.fee.entity.Fee;
import com.scfs.domain.fee.entity.FeeQueryModel;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.bookkeeping.FeeKeepingService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.fee.impl.FeeServiceImpl;
import com.scfs.service.finance.CopeManageService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 *
 *  File: FeeAuditService.java
 *  Description:
 *  TODO
 *  Date,					Who,
 *  2016年11月5日				Administrator
 *
 * </pre>
 */

@Service
public class FeeAuditService extends AuditService {
	@Autowired
	FeeServiceImpl feeService;
	@Autowired
	FeeKeepingService feeKeepingService;
	@Autowired
	AuditDao auditDao;
	@Autowired
	CacheService cacheService;
	@Autowired
	MsgContentService msgContentService;
	@Autowired
	AuditFlowService auditFlowService;
	@Autowired
	CopeManageService copeManageService;// 应付管理

	@Override
	public void batchPassAudit(Audit audit) {
		FeeAuditReqDto feeAuditReqDto = new FeeAuditReqDto();
		feeAuditReqDto.setAuditId(audit.getId());
		feeAuditReqDto.setFeeId(audit.getPoId());
		if (audit.getState() == BaseConsts.INT_25) {
			passFinanceAudit(feeAuditReqDto);
		} else if (audit.getState() == BaseConsts.INT_30) {
			passFinance2Audit(feeAuditReqDto);
		}
	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		FeeAuditReqDto feeAuditReqDto = new FeeAuditReqDto();
		feeAuditReqDto.setAuditId(audit.getId());
		feeAuditReqDto.setFeeId(audit.getPoId());
		unPassAudit(feeAuditReqDto);
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

	public void sendMessage(AuditNode auditNode, FeeAuditReqDto feeAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailResult(feeAuditReqDto.getFeeId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "费用申请审核单据", "费用申请审核通过");
			sendWechatMsg(newId, "您有新的费用单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点
			sendMailResult(feeAuditReqDto.getFeeId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "费用申请审核单据", "费用申请审核通过");
		}
	}

	/**
	 * 提交之后开始走流程
	 */
	public void startAudit(Integer id, AuditNode startAuditNode) {
		FeeQueryModel fee = feeService.queryEntityById(id).getItems();
		Audit audit = new Audit();
		audit.setAuditType(BaseConsts.ONE);
		switch (fee.getFeeType()) {
		case BaseConsts.ONE: // 应收
			audit.setCustomerId(fee.getCustPayer());
			audit.setAmount(fee.getRecAmount());
			audit.setPoType(BaseConsts.FOUR);
			audit.setPoDate(fee.getRecDate());
			break;
		case BaseConsts.TWO: // 应付
			audit.setCustomerId(fee.getCustReceiver());
			audit.setAmount(fee.getPayAmount());
			audit.setPoType(BaseConsts.EIGHT);
			audit.setPoDate(fee.getPayDate());
			break;
		case BaseConsts.THREE: // 应收应付
			audit.setCustomerId(fee.getCustPayer());
			audit.setAmount(fee.getRecAmount());
			audit.setPoType(BaseConsts.NINE);
			audit.setPoDate(fee.getRecDate());
			break;
		case BaseConsts.FOUR: // 应收抵扣费用
			audit.setCustomerId(fee.getCustPayer());
			audit.setAmount(fee.getRecAmount());
			audit.setPoType(BaseConsts.INT_24);
			audit.setPoDate(fee.getRecDate());
			break;
		case BaseConsts.FIVE: // 应付抵扣费用
			audit.setCustomerId(fee.getCustPayer());
			audit.setAmount(fee.getPayAmount());
			audit.setPoType(BaseConsts.INT_25);
			audit.setPoDate(fee.getPayDate());
			break;
		}
		audit.setProjectId(fee.getProjectId());
		audit.setPoNo(fee.getFeeNo());
		audit.setPoId(fee.getId());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setCurrencyId(fee.getCurrencyType());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程

		sendMailMsg(id, audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
		sendWechatMsg(newId, "您有新的费用单审核【" + audit.getPoNo() + "】");
	}

	/**
	 * 加签
	 */
	public void sighAudit(FeeAuditReqDto feeAuditReqDto) {
		if (feeAuditReqDto.getAuditId() == null || feeAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(feeAuditReqDto.getAuditId(), feeAuditReqDto.getPauditorId());
		Audit audit = auditDao.queryAuditById(feeAuditReqDto.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.THREE); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的费用单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(FeeAuditReqDto feeAuditReqDto) {
		if (feeAuditReqDto.getAuditId() == null || feeAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(feeAuditReqDto.getAuditId(), feeAuditReqDto.getPauditorId());
		Audit audit = auditDao.queryAuditById(feeAuditReqDto.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的销售单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 财务专员审核通过
	 */
	public void passFinanceAudit(FeeAuditReqDto feeAuditReqDto) {
		Audit audit = auditDao.queryAuditById(feeAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_25 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);// 审核状态，1表示审核通过，2表示审核不通过
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(feeAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_25, audit.getPoType(),
					audit.getProjectId());

			// 1.更新费用状态为已完成
			feeService.queryEntityById(feeAuditReqDto.getFeeId()).getItems();
			Fee fee = new Fee();
			fee.setId(feeAuditReqDto.getFeeId());
			fee.setState(this.getState(nextAuditNode));
			fee.setAuditAt(new Date());
			fee.setAuditor(ServiceSupport.getUser().getChineseName());
			fee.setAuditorId(ServiceSupport.getUser().getId());
			if (feeAuditReqDto.getBookDate() != null) {
				fee.setBookDate(feeAuditReqDto.getBookDate());
			} else {
				fee.setBookDate(new Date());
			}
			feeService.updateFeeById(fee);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, feeAuditReqDto, audit, newId); // 发送消息
		}

	}

	/**
	 * 财务主管审核通过
	 */
	public void passFinance2Audit(FeeAuditReqDto feeAuditReqDto) {
		Audit audit = auditDao.queryAuditById(feeAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_30 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);// 审核状态，1表示审核通过，2表示审核不通过
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(feeAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_30, audit.getPoType(),
					audit.getProjectId());

			// 1.更新费用状态为已完成
			feeService.queryEntityById(feeAuditReqDto.getFeeId()).getItems();
			Fee fee = new Fee();
			fee.setId(feeAuditReqDto.getFeeId());
			fee.setState(this.getState(nextAuditNode));
			fee.setAuditAt(new Date());
			fee.setAuditor(ServiceSupport.getUser().getChineseName());
			fee.setAuditorId(ServiceSupport.getUser().getId());
			feeService.updateFeeById(fee);
			// 生成凭证
			feeKeepingService.feeBookkeeping(feeAuditReqDto.getFeeId());
			// 添加应付管理
			copeManageService.saveCopeManage(feeAuditReqDto.getFeeId());

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, feeAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 审核不通过
	 */
	public void unPassAudit(FeeAuditReqDto feeAuditReqDto) {
		Audit audit = auditDao.queryAuditById(feeAuditReqDto.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.TWO);// 审核状态，1表示审核通过，2表示审核不通过
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(feeAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态,终止流程
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());

			feeService.queryEntityById(feeAuditReqDto.getFeeId()).getItems();
			Fee fee = new Fee();
			fee.setId(feeAuditReqDto.getFeeId());
			fee.setState(BaseConsts.ONE);
			feeService.updateFeeById(fee);
			sendMailResult(feeAuditReqDto.getFeeId(), audit.getAuditorId(), BaseConsts.TWO);
			sendWarnRtx(audit.getAuditorId(), "费用申请审核单据", "费用申请审核不通过");
		}
	}

	public PageResult<AuditFlowsResDto> queryAuditFlows(FeeAuditReqDto feeAuditReqDto) {
		FeeQueryModel fee = feeService.queryEntityById(feeAuditReqDto.getFeeId()).getItems();
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = null;
		switch (fee.getFeeType()) {
		case BaseConsts.ONE:
			poType = BaseConsts.FOUR;
			break;
		case BaseConsts.TWO:
			poType = BaseConsts.EIGHT;
			break;
		case BaseConsts.THREE:
			poType = BaseConsts.NINE;
			break;
		case BaseConsts.FOUR:
			poType = BaseConsts.INT_24;
			break;
		case BaseConsts.FIVE:
			poType = BaseConsts.INT_25;
			break;
		default:
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "费用类型有误，请核查");
		}
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = super.queryAuditFlowsByCon(feeAuditReqDto.getFeeId(), poType, auditflows);
		return result;
	}

	private void sendMailMsg(Integer feeId, Integer auditId, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		FeeQueryModel fee = feeService.queryEntityById(feeId).getItems();
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
		mail1.setColumnOne("费用单编号");
		mail1.setColumnTwo(fee.getFeeNo());
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
		String content = msgContentService.convertMailOneContent("费用单财务审核" + auditTypeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "费用单财务审核", content);
	}

	private void sendMailResult(Integer feeId, Integer auditId, Integer auditState) {
		FeeQueryModel fee = feeService.queryEntityById(feeId).getItems();
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("费用单编号");
		mail1.setColumnTwo(fee.getFeeNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核结果");
		mail2.setColumnTwo(auditState.equals(BaseConsts.ONE) ? "审核通过" : "审核不通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("费用单财务审核结果", templateOnes, null);
		sendWarnMail(toUser.getId(), "费用单财务审核", content);
	}
}
