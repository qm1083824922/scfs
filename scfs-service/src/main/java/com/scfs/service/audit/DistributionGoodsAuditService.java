package com.scfs.service.audit;

import java.math.BigDecimal;
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
import com.scfs.domain.audit.model.DistributionGoodsAuditInfo;
import com.scfs.domain.base.dto.resp.DistributionGoodsResDto;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.po.dto.resp.PoFileAttachRespDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.base.goods.DistributionGoodsService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 	
 *  File: DistributionGoodsAuditService.java
 *  Description:铺货商品审核业务
 *  TODO
 *  Date,					Who,				
 *  2017年05月03日				Administrator
 *
 * </pre>
 */
@Service
public class DistributionGoodsAuditService extends AuditService {
	@Autowired
	private DistributionGoodsService distributionGoodsService;
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
		if (audit.getState() == BaseConsts.INT_14) {
			passCareerAudit(projectItemReqDto); // 事业部审核
		} else if (audit.getState() == BaseConsts.INT_15) {
			passPurchaseAudit(projectItemReqDto);// 采购审核
		} else if (audit.getState() == BaseConsts.INT_16) {
			passSupplyChainGroupAudit(projectItemReqDto);// 供应链小组审核
		} else if (audit.getState() == BaseConsts.INT_17) {
			passSupplyChainServiceAudit(projectItemReqDto);// 供应链服务部审核
		} else if (audit.getState() == BaseConsts.INT_18) {
			passRiskAudit(projectItemReqDto);// 商品风控审核
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
			sendWechatMsg(newId, "您有新的铺货商品审核【" + audit.getPoNo() + "】");
			sendRTXMsg(newId, false, false);
		} else { // 最后一个审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
			// DistributionGoodsResDto resDto =
			// distributionGoodsService.queryDistributionById(audit.getPoId());//发给创建人
			// sendRTXMsg(resDto.getCareerId(), true, false);
		}
	}

	/**
	 * 获取信息
	 * 
	 * @param payId
	 * @return
	 */
	public Result<DistributionGoodsAuditInfo> queryOverseasAuditInfo(Integer goodsId) {
		Result<DistributionGoodsAuditInfo> result = new Result<DistributionGoodsAuditInfo>();
		DistributionGoodsResDto distribution = distributionGoodsService.queryDistributionById(goodsId);
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusId(goodsId);
		List<PoFileAttachRespDto> poFileAttachList = distributionGoodsService.queryAllFileList(fileAttReqDto);
		DistributionGoodsAuditInfo model = new DistributionGoodsAuditInfo();
		model.setDistributionGoods(distribution);
		model.setPoFileAttachList(poFileAttachList);
		result.setItems(model);
		return result;
	}

	/**
	 * 事业部审核通过
	 * 
	 * @param poAuditReqDto
	 */
	public void passCareerAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_14 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_14, audit.getPoType(),
					audit.getProjectId());
			// 业务逻辑处理
			updateDistributionGoodsState(audit.getPoId(), getState(nextAuditNode)); // 待采购审核

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 采购审核审核通过
	 * 
	 * @param poAuditReqDto
	 */
	public void passPurchaseAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_15 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_15, audit.getPoType(),
					audit.getProjectId());
			// 业务逻辑处理
			updateDistributionGoodsState(audit.getPoId(), getState(nextAuditNode)); // 待供应链小组审核

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 供应链小组审核通过
	 * 
	 * @param poAuditReqDto
	 */
	public void passSupplyChainGroupAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_16 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_16, audit.getPoType(),
					audit.getProjectId());
			// 业务逻辑处理
			updateDistributionGoodsState(audit.getPoId(), getState(nextAuditNode)); // 待供应链服务部审核

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 供应链服务部审核通过
	 * 
	 * @param poAuditReqDto
	 */
	public void passSupplyChainServiceAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_17 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_17, audit.getPoType(),
					audit.getProjectId());
			// 业务逻辑处理
			updateDistributionGoodsState(audit.getPoId(), getState(nextAuditNode)); // 待分控审核

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 商品风控审核通过
	 * 
	 * @param poAuditReqDto
	 */
	public void passRiskAudit(ProjectItemReqDto poAuditReqDto) {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_18 || audit.getAuditState() != BaseConsts.ZERO) {
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
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_18, audit.getPoType(),
					audit.getProjectId());
			updateDistributionGoodsState(audit.getPoId(), getState(nextAuditNode)); // 已完成

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
			updateDistributionGoodsState(audit.getPoId(), BaseConsts.ONE); // 业务单据状态：待提交
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO); // 发送邮件
			sendRTXMsg(audit.getId(), true, true);
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
		sendWechatMsg(newId, "您有加签的铺货商品单审核【" + newAudit.getPoNo() + "】");
		sendRTXMsg(newId, false, false);
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
		sendWechatMsg(newId, "您有转交的铺货商品单审核【" + newAudit.getPoNo() + "】");
		sendRTXMsg(newId, false, false);
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param purchaseOrderTitle
	 */
	public void startAudit(DistributionGoods baseGoods, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setPoDate(baseGoods.getCreateAt());
		audit.setPoId(baseGoods.getId());
		audit.setPoNo(baseGoods.getNumber());
		audit.setSupplierId(baseGoods.getSupplierId());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setAmount(new BigDecimal((BaseConsts.ZERO)));
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoType(BaseConsts.INT_21); // 21表示铺货商品
		audit.setState(BaseConsts.INT_14); // 状态
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendWechatMsg(newId, "您有新的铺货商品单审核【" + baseGoods.getNumber() + "】");
		sendRTXMsg(newId, false, false);
	}

	public void updateDistributionGoodsState(Integer id, Integer state) {
		if (state.equals(BaseConsts.TWO)) {
			distributionGoodsService.isOver(id);// 锁住以往
		}
		DistributionGoods distributionGoods = new DistributionGoods();
		distributionGoods.setId(id);
		distributionGoods.setStatus(state);
		distributionGoodsService.updateDistributionGoods(distributionGoods);
	}

	/**
	 * 获取节点信息
	 * 
	 * @param projectItemId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectItemId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.INT_21;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(projectItemId, poType, auditflows);
		return result;
	}

	private void sendMailMsg(Integer goodsId, Integer auditId, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		DistributionGoodsResDto distribution = distributionGoodsService.queryDistributionById(goodsId);
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
		mail1.setColumnOne("铺货商品单编号");
		mail1.setColumnTwo(distribution.getNumber());
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
		String content = msgContentService.convertMailOneContent("铺货商品单财务审核" + auditTypeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "铺货商品单财务审核", content);
	}

	private void sendMailResult(Integer goodsId, Integer auditId, Integer auditState) {
		DistributionGoodsResDto distribution = distributionGoodsService.queryDistributionById(goodsId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("铺货商品单编号");
		mail1.setColumnTwo(distribution.getNumber());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核结果");
		mail2.setColumnTwo(auditState.equals(BaseConsts.ONE) ? "审核通过" : "审核不通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("铺货商品单财务审核结果", templateOnes, null);
		sendWarnMail(toUser.getId(), "铺货商品单财务审核", content);
	}

	private void sendRTXMsg(int auditId, boolean isOver, boolean isRefuse) {
		Audit newAudit = auditDao.queryAuditById(auditId);
		BaseUser fromUser = ServiceSupport.getUser();
		DistributionGoodsResDto resDto = distributionGoodsService.queryDistributionById(newAudit.getPoId());
		BaseUser toUser = cacheService.getUserByid(newAudit.getAuditorId());
		String auditTypeStr = "";
		String content = "";
		String title = "";
		if (isOver) {// 是否是最后节点
			content = content + "SCFS系统提醒您,【铺货商品】审核结果\n";
			if (isRefuse) {
				content = content + "审核不通过！\n";
			}
			title = auditTypeStr + "【铺货商品】审核结果";
		} else {
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
			content = content + "SCFS系统提醒您,有新的" + auditTypeStr + "【铺货商品】需要审核\n";
			content = content + "商品编号:" + resDto.getNumber() + "\n";
			content = content + "质押比例:" + resDto.getPledge() + "\n";
			content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
			if (!auditTypeStr.equals("")) {
				content = content + auditTypeStr + "信息:" + "单据由" + fromUser.getChineseName() + "于"
						+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
						+ toUser.getChineseName() + "审核\n";
			}
			title = auditTypeStr + "【铺货商品】需要审核";
		}
		content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

		sendWarnRtx(toUser.getId(), "SCFS系统提醒您,有新的" + title, content);
	}
}
