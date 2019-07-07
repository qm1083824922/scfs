package com.scfs.service.audit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.audit.AuditDao;
import com.scfs.domain.audit.dto.req.AuditReqDto;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.MergePayOrderAuditInfo;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.pay.dto.req.MergePayOrderRelSearchReqDto;
import com.scfs.domain.pay.dto.req.PayAdvanceRelationReqDto;
import com.scfs.domain.pay.dto.req.PayFeeRelationReqDto;
import com.scfs.domain.pay.dto.req.PayPoRelationReqDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderRelResDto;
import com.scfs.domain.pay.dto.resq.MergePayOrderResDto;
import com.scfs.domain.pay.dto.resq.PayAdvanceRelationResDto;
import com.scfs.domain.pay.dto.resq.PayFeeRelationResDto;
import com.scfs.domain.pay.dto.resq.PayOrderFileResDto;
import com.scfs.domain.pay.dto.resq.PayPoRelationResDto;
import com.scfs.domain.pay.entity.MergePayOrder;
import com.scfs.domain.project.entity.ProjectPool;
import com.scfs.domain.result.PageResult;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.pay.MergePayOrderRelService;
import com.scfs.service.pay.MergePayOrderService;
import com.scfs.service.pay.PayAdvanceRelationService;
import com.scfs.service.pay.PayFeeRelationService;
import com.scfs.service.pay.PayPoRelationService;
import com.scfs.service.pay.PayService;
import com.scfs.service.pay.UploadPayService;
import com.scfs.service.project.ProjectPoolService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.beust.jcommander.internal.Maps;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: MergePayAuditService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月2日				Administrator
 *
 * </pre>
 */

@Service
public class MergePayAuditService extends AuditService {
	@Autowired
	private PayService payService; // 付款
	@Autowired
	private PayPoRelationService payPoRelationService; // 付款订单
	@Autowired
	private PayFeeRelationService payFeeRelationService;// 付款费用
	@Autowired
	private PayAdvanceRelationService payAdvanceRelationService;// 预收单信息
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private MergePayOrderService mergePayOrderService;
	@Autowired
	private MergePayOrderRelService mergePayOrderRelService;
	@Autowired
	private ProjectPoolService projectPoolService;
	@Autowired
	private UploadPayService uploadPayService;
	@Autowired
	private AuditFlowService auditFlowService;

	@Override
	public void batchPassAudit(Audit audit) {
		ProjectItemReqDto projectItemReqDto = new ProjectItemReqDto();
		projectItemReqDto.setAuditId(audit.getId());
		if (audit.getState() == BaseConsts.INT_11) {
			passLawAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_80) {
			passDeptManageAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_30) {
			passFinance2Audit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_25) {
			passFinanceAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_90) {
			passBossAudit(projectItemReqDto);
		} else if (audit.getState() == BaseConsts.INT_40) {
			passRiskAudit(projectItemReqDto);
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
			state = BaseConsts.SIX;
		}
		return state;
	}

	public void sendMessage(AuditNode auditNode, ProjectItemReqDto poAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailMsg(newId); // 发送邮件
			sendRTXMsg(newId); // 发送RTX
			sendWechatMsg(newId, "您有新的合并付款单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点

		}
	}

	/**
	 * 商务审核
	 */
	public void passRiskBusinessAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.TEN || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit); // 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			Map<String, Object> paramMap = Maps.newHashMap();
			paramMap.put("mergePayOrderId", audit.getPoId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.TEN, audit.getPoType(),
					audit.getProjectId(), paramMap);
			// 业务逻辑处理
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), this.getState(nextAuditNode));

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 法务主管审核通过
	 */
	public void passLawAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_11 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit); // 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			Map<String, Object> paramMap = Maps.newHashMap();
			paramMap.put("mergePayOrderId", audit.getPoId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_11, audit.getPoType(),
					audit.getProjectId(), paramMap);
			// 业务逻辑处理
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), this.getState(nextAuditNode));

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
		super.updateAudit(audit); // 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			Map<String, Object> paramMap = Maps.newHashMap();
			paramMap.put("mergePayOrderId", audit.getPoId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_40, audit.getPoType(),
					audit.getProjectId(), paramMap);
			// 业务逻辑处理
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), this.getState(nextAuditNode));

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 风控专员审核
	 * 
	 * @param poAuditReqDto
	 */
	public void passRiskSpecialAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_35 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.ONE);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		super.updateAudit(audit); // 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			Map<String, Object> paramMap = Maps.newHashMap();
			paramMap.put("mergePayOrderId", audit.getPoId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_35, audit.getPoType(),
					audit.getProjectId(), paramMap);
			// 业务逻辑处理
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), this.getState(nextAuditNode));

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务审核通过
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
		super.updateAudit(audit); // 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			Map<String, Object> paramMap = Maps.newHashMap();
			paramMap.put("mergePayOrderId", audit.getPoId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_25, audit.getPoType(),
					audit.getProjectId(), paramMap);
			// 业务逻辑处理
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), this.getState(nextAuditNode));

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
			Map<String, Object> paramMap = Maps.newHashMap();
			paramMap.put("mergePayOrderId", audit.getPoId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_30, audit.getPoType(),
					audit.getProjectId(), paramMap);
			// 业务逻辑处理
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), this.getState(nextAuditNode));

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
			Map<String, Object> paramMap = Maps.newHashMap();
			paramMap.put("mergePayOrderId", audit.getPoId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_80, audit.getPoType(),
					audit.getProjectId(), paramMap);
			// 业务逻辑处理
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), this.getState(nextAuditNode));

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
			super.closeSighAudit(audit.getId());
			Map<String, Object> paramMap = Maps.newHashMap();
			paramMap.put("mergePayOrderId", audit.getPoId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_90, audit.getPoType(),
					audit.getProjectId(), paramMap);
			// 业务逻辑处理
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), this.getState(nextAuditNode));
			//不上传至第三方财务系统
			//uploadPayService.uploadPayOrder(mergePayOrderService.queryEntityById(audit.getPoId()));

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
			mergePayOrderService.updateMergePayOrderState(audit.getPoId(), BaseConsts.ZERO); // 业务单据状态：待提交
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
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的合并付款单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(ProjectItemReqDto poAuditReqDto) {
		if (poAuditReqDto.getAuditId() == null || poAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(poAuditReqDto.getAuditId(), poAuditReqDto.getPauditorId());
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的合并付款单审核【" + newAudit.getPoNo() + "】");

	}

	/**
	 * 提交之后开始走流程
	 * 
	 *
	 * @param payOrder
	 */
	public void startAudit(MergePayOrder mergePayOrder, AuditNode startAuditNode, Integer state) {
		Audit audit = new Audit();
		audit.setSupplierId(mergePayOrder.getPayee());
		audit.setPoDate(mergePayOrder.getRequestPayTime());
		audit.setPoId(mergePayOrder.getId());
		audit.setPoNo(mergePayOrder.getMergePayNo());
		audit.setProjectId(mergePayOrder.getProjectId());
		audit.setBusinessUnitId(mergePayOrder.getBusiUnit());
		audit.setAmount(mergePayOrder.getPayAmount());
		audit.setCustomerId(mergePayOrder.getPayee());
		audit.setProposerId(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser() == null ? null : ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoType(BaseConsts.INT_14); // 14表示合并付款
		audit.setCurrencyId(mergePayOrder.getCurrencyType());

		if (state.equals(BaseConsts.INT_99)) {
			super.createSubmitAudit(audit);// 提交节点
			audit.setId(null);
			audit.setAuditType(BaseConsts.ONE);
			audit.setSuggestion(null);
			audit.setAuditorPass(null);
			audit.setAuditorPassId(null);
			audit.setAuditorPassAt(null);
			audit.setPauditId(null);
			audit.setPauditor(null);
			audit.setPauditorId(null);
			audit.setState(mergePayOrder.getState());
			audit.setAuditState(BaseConsts.ZERO);
			audit.setAuditor(null);
			audit.setAuditorId(null);
			auditDao.insert(audit);// 添加供应商确认审核节点
		} else {
			if (state.equals(BaseConsts.THREE)) {
				AuditReqDto auditReqDto = new AuditReqDto();
				auditReqDto.setPoId(mergePayOrder.getId());
				auditReqDto.setPoType(BaseConsts.INT_14);
				List<Audit> auditList = auditDao.queryAuditFlows(auditReqDto);
				for (Audit supAudit : auditList) {
					if (supAudit.getState().equals(BaseConsts.THREE)
							&& supAudit.getAuditState().equals(BaseConsts.ZERO)) {
						audit.setId(supAudit.getId());
						audit.setAuditState(BaseConsts.ONE);
						audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
						audit.setAuditorPassId(ServiceSupport.getUser().getId());
						audit.setAuditorPassAt(new Date());
						audit.setAuditor(ServiceSupport.getUser().getChineseName());
						audit.setAuditorId(ServiceSupport.getUser().getId());
						audit.setSuggestion("已确认");
						super.updateAudit(audit); // 更新审核状态
					}
				}
			} else {
				super.createSubmitAudit(audit);// 提交节点
			}
			audit.setState(startAuditNode.getAuditNodeState());
			int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
			sendMailMsg(newId);
			sendRTXMsg(newId);
			sendWechatMsg(newId, "您有新的合并付款单审核【" + audit.getPoNo() + "】");
		}
	}

	/**
	 * 获取信息
	 * 
	 * @param payId
	 * @return
	 */
	public MergePayOrderAuditInfo queryPayAuditInfo(Integer mergePayId) {
		MergePayOrderAuditInfo result = new MergePayOrderAuditInfo();
		MergePayOrderResDto mergePayOrderResDto = mergePayOrderService.detailPayOrderById(mergePayId);
		MergePayOrderRelSearchReqDto req = new MergePayOrderRelSearchReqDto();
		req.setMergePayId(mergePayId);
		List<MergePayOrderRelResDto> mergePayOrderRelResDtos = mergePayOrderRelService.queryListByMergeId(req);

		ProjectPool pp = projectPoolService.queryProjectPoolByProjectId(mergePayOrderResDto.getProjectId());
		mergePayOrderResDto.setProjectTotalAmount(pp.getProjectAmount());
		mergePayOrderResDto.setProjectBalanceAmount(pp.getRemainFundAmount());
		mergePayOrderResDto.setProjectAmountUnit(pp.getCurrencyType());
		mergePayOrderResDto.setProjectAmountUnitTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, pp.getCurrencyType() + ""));
		if (pp != null) {
			mergePayOrderResDto.setPayProjectBalanceAmount(
					DecimalUtil.subtract(pp.getRemainFundAmount(), mergePayOrderResDto.getPayAdvanceAmount()));
		}

		List<Integer> payIds = new ArrayList<Integer>();
		for (MergePayOrderRelResDto mergePayOrderRelResDto : mergePayOrderRelResDtos) {
			payIds.add(mergePayOrderRelResDto.getPayId());
		}
		Integer poType = BaseConsts.INT_14;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, mergePayOrderResDto.getProjectId());
		PageResult<AuditFlowsResDto> auditFlows = queryAuditFlowsByCon(mergePayId, poType, auditflows);

		PayPoRelationReqDto payPoRelationReqDto = new PayPoRelationReqDto();
		payPoRelationReqDto.setPayIds(payIds);
		List<PayPoRelationResDto> payPoRelationResDto = payPoRelationService
				.queryPayPoRelationAuditByCon(payPoRelationReqDto);

		PayFeeRelationReqDto payFeeRelationReqDto = new PayFeeRelationReqDto();
		payFeeRelationReqDto.setPayIds(payIds);
		List<PayFeeRelationResDto> payFeeRelationResDto = payFeeRelationService
				.queryPayFeeRelationAuditByCon(payFeeRelationReqDto);

		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusIds(payIds);
		List<PayOrderFileResDto> payOrderFileList = payService.queryFileListAll(fileAttReqDto);

		PayAdvanceRelationReqDto payAdvanceRelationReq = new PayAdvanceRelationReqDto();
		payAdvanceRelationReq.setPayIds(payIds);
		List<PayAdvanceRelationResDto> payAdvanceRelation = payAdvanceRelationService
				.queryPayAdvanRelationByPayId(payAdvanceRelationReq);

		result.setPayPoRelationResDto(payPoRelationResDto);
		result.setPayFeeRelationResDto(payFeeRelationResDto);
		result.setPayOrderFileList(payOrderFileList);
		result.setPayAdvanceRelation(payAdvanceRelation);

		result.setMergePayOrderResDto(mergePayOrderResDto);
		result.setMergePayOrderRelResDtos(mergePayOrderRelResDtos);
		result.setAuditFlowsResDtos(auditFlows.getItems());
		return result;
	}

	/**
	 * 获取节点信息
	 * 
	 * @param meragePayId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer meragePayId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		MergePayOrder mergePayOrder = new MergePayOrder();
		mergePayOrder.setId(meragePayId);
		mergePayOrder = mergePayOrderService.queryEntityById(meragePayId);
		Integer poType = BaseConsts.INT_14;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, mergePayOrder.getProjectId());
		result = queryAuditFlowsByCon(meragePayId, poType, auditflows);
		return result;
	}

	private void sendMailMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);

		BaseUser fromUser = ServiceSupport.getUser();
		MergePayOrderResDto mergePayOrder = mergePayOrderService.detailPayOrderById(newAudit.getPoId());
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
		mail1.setColumnOne("合并付款单编号");
		mail1.setColumnTwo(mergePayOrder.getMergePayNo());
		templateOnes.add(mail1);

		MailTemplateOne mail4 = new MailTemplateOne();
		mail4.setColumnOne("项目");
		mail4.setColumnTwo(mergePayOrder.getProjectName());
		templateOnes.add(mail4);

		MailTemplateOne mail5 = new MailTemplateOne();
		mail5.setColumnOne("付款金额");
		mail5.setColumnTwo(
				DecimalUtil.toAmountString(mergePayOrder.getPayAmount()) + mergePayOrder.getCurrencyTypeName());
		templateOnes.add(mail5);

		if (!auditTypeStr.equals("")) {
			MailTemplateOne mail2 = new MailTemplateOne();
			mail2.setColumnOne(auditTypeStr + "信息");
			mail2.setColumnTwo("单据由" + fromUser.getChineseName() + "于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
					+ toUser.getChineseName() + "审核");
			templateOnes.add(mail2);
		}

		String typeStr = ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, newAudit.getState() + "");
		String content = msgContentService.convertMailOneContent(auditTypeStr + "合并付款单" + typeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【合并付款单】需要审核", content);
	}

	private void sendRTXMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);
		BaseUser fromUser = ServiceSupport.getUser();
		MergePayOrderResDto mergePayOrder = mergePayOrderService.detailPayOrderById(newAudit.getPoId());
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

		content = content + "合并付款单编号:" + mergePayOrder.getMergePayNo() + "\n";
		content = content + "项目:" + mergePayOrder.getProjectName() + "\n";
		content = content + "合并付款金额:" + DecimalUtil.toAmountString(mergePayOrder.getPayAmount())
				+ mergePayOrder.getCurrencyTypeName() + "\n";
		if (!auditTypeStr.equals("")) {
			content = content + auditTypeStr + "信息:" + "单据由" + fromUser.getChineseName() + "于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
					+ toUser.getChineseName() + "审核\n";
		}
		content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
		content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

		sendWarnRtx(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【合并付款单】需要审核", content);
	}

}
