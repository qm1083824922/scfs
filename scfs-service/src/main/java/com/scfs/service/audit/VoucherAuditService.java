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
import com.scfs.domain.audit.dto.req.VoucherAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.fi.entity.Voucher;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.fi.VoucherService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: VoucherAuditService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月5日				Administrator
 *
 * </pre>
 */
@Service
public class VoucherAuditService extends AuditService {

	@Autowired
	private VoucherService voucherService;
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
		VoucherAuditReqDto voucherAuditReqDto = new VoucherAuditReqDto();
		voucherAuditReqDto.setAuditId(audit.getId());
		if (audit.getState() == BaseConsts.INT_25) {
			passFinanceAudit(voucherAuditReqDto);
		}
	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		VoucherAuditReqDto voucherAuditReqDto = new VoucherAuditReqDto();
		voucherAuditReqDto.setAuditId(audit.getId());
		unPassAudit(voucherAuditReqDto);
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

	public void sendMessage(AuditNode auditNode, VoucherAuditReqDto voucherAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点

		} else { // 最后一个审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), audit.getState());
		}
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param id
	 */
	public void startAudit(Integer id, AuditNode startAuditNode) {
		Voucher voucher = voucherService.queryEntityById(id);
		Audit audit = new Audit();
		audit.setAuditType(BaseConsts.ONE);
		audit.setAmount(voucher.getCreditAmount());
		audit.setPoType(BaseConsts.SIX);
		audit.setPoId(voucher.getId());
		audit.setPoNo(voucher.getVoucherNo());
		audit.setPoDate(voucher.getVoucherDate());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		Audit newAudit = auditDao.queryAuditById(newId);
		sendMailMsg(audit.getPoId(), newAudit.getAuditorId(), newAudit.getAuditType());
	}

	/**
	 * 加签
	 */
	public void sighAudit(VoucherAuditReqDto voucherAuditReqDto) {
		if (voucherAuditReqDto.getAuditId() == null || voucherAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(voucherAuditReqDto.getAuditId(), voucherAuditReqDto.getPauditorId());
		Audit audit = auditDao.queryAuditById(voucherAuditReqDto.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), audit.getAuditType());
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的凭证单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(VoucherAuditReqDto voucherAuditReqDto) {
		if (voucherAuditReqDto.getAuditId() == null || voucherAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(voucherAuditReqDto.getAuditId(), voucherAuditReqDto.getPauditorId());
		Audit audit = auditDao.queryAuditById(voucherAuditReqDto.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), audit.getAuditType());
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的凭证单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 财务审核通过
	 */
	public void passFinanceAudit(VoucherAuditReqDto voucherAuditReqDto) {
		Audit audit = auditDao.queryAuditById(voucherAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_25 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);// 审核状态，1表示审核通过，2表示审核不通过
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(voucherAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态

		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_25, audit.getPoType(),
					audit.getProjectId());

			Voucher oldVoucher = voucherService.queryEntityById(audit.getPoId());
			if (oldVoucher == null) {
				throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, VoucherService.class, audit.getPoId());
			}
			if (oldVoucher.getState() != BaseConsts.INT_25) {
				throw new BaseException(ExcMsgEnum.VOUCHER_AUDIT_STATE_ERROR);
			}
			// 业务逻辑处理
			Voucher voucher = new Voucher();
			voucher.setId(audit.getPoId());
			voucher.setState(getState(nextAuditNode));
			voucherService.updateVoucherById(voucher);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, voucherAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 审核不通过
	 */
	public void unPassAudit(VoucherAuditReqDto voucherAuditReqDto) {
		Audit audit = auditDao.queryAuditById(voucherAuditReqDto.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.TWO);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(voucherAuditReqDto.getSuggestion());
		super.updateAudit(audit);// 更新审核状态,终止流程

		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			// 业务逻辑处理
			Voucher oldVoucher = voucherService.queryEntityById(audit.getPoId());
			if (oldVoucher == null) {
				throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, VoucherService.class, audit.getPoId());
			}
			if (oldVoucher.getState() != BaseConsts.TWO) {
				throw new BaseException(ExcMsgEnum.VOUCHER_AUDIT_STATE_ERROR);
			}
			Voucher voucher = new Voucher();
			voucher.setId(audit.getPoId());
			voucher.setState(BaseConsts.ONE);
			voucherService.updateVoucherById(voucher);
			sendMailResult(audit.getPoId(), audit.getAuditorId(), audit.getState());
		}
	}

	public PageResult<AuditFlowsResDto> queryAuditFlows(VoucherAuditReqDto voucherAuditReqDto) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.SIX;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(voucherAuditReqDto.getVoucherId(), poType, auditflows);
		return result;
	}

	private void sendMailMsg(Integer voucherId, Integer auditorId, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		BaseUser toUser = cacheService.getUserByid(auditorId);
		Voucher voucher = voucherService.queryEntityById(voucherId);
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
		mail1.setColumnOne("凭证单编号");
		mail1.setColumnTwo(voucher.getVoucherNo());
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
		String content = msgContentService.convertMailOneContent("凭证单财务审核" + auditTypeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "凭证单财务审核", content);
	}

	private void sendMailResult(Integer voucherId, Integer auditId, Integer auditState) {
		Voucher voucher = voucherService.queryEntityById(voucherId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("凭证单编号");
		mail1.setColumnTwo(voucher.getVoucherNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核结果");
		mail2.setColumnTwo(auditState.equals(BaseConsts.ONE) ? "审核通过" : "审核不通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("凭证单财务审核结果", templateOnes, null);
		sendWarnMail(toUser.getId(), "凭证单财务审核", content);
	}
}
