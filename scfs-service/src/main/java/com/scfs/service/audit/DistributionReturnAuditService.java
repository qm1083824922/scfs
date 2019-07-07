package com.scfs.service.audit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.dao.audit.AuditDao;
import com.scfs.dao.po.PurchaseOrderLineDao;
import com.scfs.domain.audit.dto.req.PoAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.PoAuditInfo;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderLine;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.interf.PmsSyncReturnPurchsePassService;
import com.scfs.service.po.DistributionReturnService;
import com.scfs.service.po.PurchaseOrderService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * Created by Administrator on 2017年6月23日.
 */
@Service
public class DistributionReturnAuditService extends AuditService {
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private DistributionReturnService distributionReturnService;
	@Autowired
	private PmsSyncReturnPurchsePassService pmsSyncReturnPurchsePassService;
	@Autowired
	private PurchaseOrderLineDao purchaseOrderLineDao;
	@Autowired
	private ProjectItemService projectItemService;
	@Autowired
	private PurchaseOrderService purchaseOrderService;
	@Autowired
	private AuditFlowService auditFlowService;

	@Override
	public void batchPassAudit(Audit audit) {
		PoAuditReqDto poAuditReqDto = new PoAuditReqDto();
		poAuditReqDto.setAuditId(audit.getId());
		poAuditReqDto.setPoId(audit.getPoId());
		try {
			if (audit.getState() == BaseConsts.TEN) {
				passBizAudit(poAuditReqDto);
			} else if (audit.getState() == BaseConsts.INT_25) {
				passFinanceAudit(poAuditReqDto);
			} else if (audit.getState() == BaseConsts.INT_30) {
				passFinance2Audit(poAuditReqDto);
			}
		} catch (BaseException e) {
			throw new BaseException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		PoAuditReqDto poAuditReqDto = new PoAuditReqDto();
		poAuditReqDto.setAuditId(audit.getId());
		poAuditReqDto.setPoId(audit.getPoId());
		try {
			unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			throw new BaseException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Integer getState(AuditNode auditNode) {
		Integer state = null;
		if (null != auditNode) { // 中间审核节点
			state = auditNode.getAuditNodeState();
		} else { // 最后一个审核节点
			state = BaseConsts.FIVE;
		}
		return state;
	}

	public void sendMessage(AuditNode auditNode, PoAuditReqDto poAuditReqDto, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailMsg(newId); // 发送邮件
			sendRTXMsg(newId); // 发送RTX
			sendWechatMsg(newId, "您有新的铺货退货单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点

		}
	}

	/**
	 * 商务专员审核通过
	 */
	public void passBizAudit(PoAuditReqDto poAuditReqDto) throws Exception {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.TEN || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(poAuditReqDto, audit);
		audit.setAuditState(BaseConsts.ONE); // 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.TEN, audit.getPoType(),
					audit.getProjectId());

			PurchaseOrderTitle purchaseOrderTitle = distributionReturnService.queryEntityById(poAuditReqDto.getPoId());
			PurchaseOrderTitle updatePurchaseOrderTitle = new PurchaseOrderTitle();
			updatePurchaseOrderTitle.setId(poAuditReqDto.getPoId());
			updatePurchaseOrderTitle.setState(getState(nextAuditNode));
			updatePurchaseOrderTitle.setOrderTime(audit.getAuditorPassAt());
			distributionReturnService.updatePurchaseOrderTitle(updatePurchaseOrderTitle);
			calcServiceAmount(purchaseOrderTitle, audit.getAuditorPassAt());

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务专员审核通过
	 */
	public void passFinanceAudit(PoAuditReqDto poAuditReqDto) throws Exception {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_25 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(poAuditReqDto, audit);
		audit.setAuditState(BaseConsts.ONE); // 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_25, audit.getPoType(),
					audit.getProjectId());

			updatePoStateById(poAuditReqDto.getPoId(), getState(nextAuditNode));

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务主管审核通过
	 *
	 * @throws Exception
	 */
	public void passFinance2Audit(PoAuditReqDto poAuditReqDto) throws Exception {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_30 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(poAuditReqDto, audit);
		audit.setAuditState(BaseConsts.ONE); // 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_30, audit.getPoType(),
					audit.getProjectId());

			updatePoStateById(poAuditReqDto.getPoId(), getState(nextAuditNode));
			PurchaseOrderTitle purchaseOrderTitle = distributionReturnService.queryEntityById(poAuditReqDto.getPoId());
			pmsSyncReturnPurchsePassService.passPurchase(audit, purchaseOrderTitle);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, poAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 审核不通过
	 *
	 * @throws Exception
	 */
	public void unPassAudit(PoAuditReqDto poAuditReqDto) throws Exception {
		Audit audit = auditDao.queryAuditById(poAuditReqDto.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(poAuditReqDto, audit);
		audit.setAuditState(BaseConsts.TWO);// 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态,终止流程
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			updatePoStateById(poAuditReqDto.getPoId(), BaseConsts.SEVEN); // 审核不通过
			PurchaseOrderTitle purchaseOrderTitle = distributionReturnService.queryEntityById(poAuditReqDto.getPoId());
			pmsSyncReturnPurchsePassService.unPassPurchase(audit, purchaseOrderTitle);
		}
	}

	/**
	 * 加签
	 */
	public void sighAudit(PoAuditReqDto poAuditReqDto) {
		if (poAuditReqDto.getAuditId() == null || poAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(poAuditReqDto.getAuditId(), poAuditReqDto.getPauditorId());
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的铺货退货单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(PoAuditReqDto poAuditReqDto) {
		if (poAuditReqDto.getAuditId() == null || poAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(poAuditReqDto.getAuditId(), poAuditReqDto.getPauditorId());
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的铺货退货单审核【" + newAudit.getPoNo() + "】");
	}

	private Audit convertToAudit(PoAuditReqDto poAuditReqDto, Audit audit) {
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(poAuditReqDto.getSuggestion());
		return audit;
	}

	/**
	 * 获取审核信息
	 * 
	 * @param poId
	 * @return
	 */
	public Result<PoAuditInfo> queryPoAuditInfoResultByPoId(Integer poId) {
		Result<PoAuditInfo> result = new Result<PoAuditInfo>();
		Result<PoTitleRespDto> poTitle = distributionReturnService.queryPurchaseOrderTitleById(poId);
		PoTitleRespDto poTitleRespDto = poTitle.getItems();
		PoAuditInfo poAuditInfo = new PoAuditInfo();
		poAuditInfo.setPoTitleRespDto(poTitleRespDto);
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setId(poId);
		PageResult<PoLineModel> poLineList = distributionReturnService.queryPoLinesByPoTitleId(poTitleReqDto);
		poAuditInfo.setPoLineDetailList(poLineList.getItems());
		result.setItems(poAuditInfo);
		return result;
	}

	/**
	 * 获取审核信息(商务审核)
	 * 
	 * @param poId
	 * @return
	 */
	public Result<PoAuditInfo> queryPoAuditInfoResultByPoId4BizAudit(Integer poId) {
		Result<PoAuditInfo> result = new Result<PoAuditInfo>();
		Result<PoTitleRespDto> poTitle = distributionReturnService.queryPurchaseOrderTitleById4BizAudit(poId);
		PoTitleRespDto poTitleRespDto = poTitle.getItems();
		PoAuditInfo poAuditInfo = new PoAuditInfo();
		poAuditInfo.setPoTitleRespDto(poTitleRespDto);
		PoTitleReqDto poTitleReqDto = new PoTitleReqDto();
		poTitleReqDto.setId(poId);
		PageResult<PoLineModel> poLineList = distributionReturnService.queryPoLinesByPoTitleId4BizAudit(poTitleReqDto);
		BigDecimal totalRefundAmount = BigDecimal.ZERO;
		BigDecimal totalOccupyServiceAmount = BigDecimal.ZERO;
		if (!CollectionUtils.isEmpty(poLineList.getItems())) {
			for (PoLineModel poLineModel : poLineList.getItems()) {
				totalRefundAmount = DecimalUtil.add(totalRefundAmount, poLineModel.getGoodsAmount());
				totalOccupyServiceAmount = DecimalUtil.add(totalOccupyServiceAmount,
						poLineModel.getOccupyServiceAmount());
			}
		}
		poTitleRespDto.setOrderTime(new Date());
		poTitleRespDto.setTotalRefundAmount(DecimalUtil.add(totalRefundAmount, totalOccupyServiceAmount));
		poTitleRespDto.setTotalOccupyServiceAmount(totalOccupyServiceAmount);
		poAuditInfo.setPoLineDetailList(poLineList.getItems());
		result.setItems(poAuditInfo);
		return result;
	}

	private void updatePoStateById(Integer poId, int state) {
		PurchaseOrderTitle purchaseOrderTitle = new PurchaseOrderTitle();
		purchaseOrderTitle.setId(poId);
		purchaseOrderTitle.setState(state);// 更新采购单状态
		distributionReturnService.updatePurchaseOrderTitle(purchaseOrderTitle);
	}

	private void calcServiceAmount(PurchaseOrderTitle purchaseOrderTitle, Date auditTime) {
		List<PoLineModel> poLineModelList = purchaseOrderLineDao.queryPoLineListByPoId(purchaseOrderTitle.getId());
		ProjectItem projectItem = projectItemService.getProjectItem(purchaseOrderTitle.getProjectId());
		for (PoLineModel poLineModel : poLineModelList) {
			long occupyDays = projectItemService.getOccupyDays(purchaseOrderTitle.getProjectId(),
					poLineModel.getPayTime(), auditTime);
			BigDecimal profitPrice = projectItemService.getProfitPrice(purchaseOrderTitle.getProjectId(),
					poLineModel.getPayPrice(), poLineModel.getPayTime(), auditTime);
			PurchaseOrderLine purchaseOrderLine = new PurchaseOrderLine();
			purchaseOrderLine.setId(poLineModel.getId());
			purchaseOrderLine.setOccupyDay((int) occupyDays);
			purchaseOrderLine.setOccupyServiceAmount(
					DecimalUtil.formatScale2(DecimalUtil.multiply(profitPrice, poLineModel.getGoodsNum())));
			purchaseOrderLine.setRefundAmount(
					DecimalUtil.add(poLineModel.getGoodsAmount(), purchaseOrderLine.getOccupyServiceAmount()));
			purchaseOrderLine.setFundMonthRate(projectItem.getFundMonthRate());
			purchaseOrderLineDao.updatePurchaseOrderLineById(purchaseOrderLine);
		}
		purchaseOrderService.updatePoTotalNum(purchaseOrderTitle.getId());
	}

	/**
	 * 提交之后开始走流程
	 *
	 * @param billDelivery
	 */
	public void startAudit(PurchaseOrderTitle purchaseOrderTitle, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoId(purchaseOrderTitle.getId());
		audit.setPoDate(purchaseOrderTitle.getOrderTime());
		audit.setPoNo(purchaseOrderTitle.getOrderNo());
		audit.setProjectId(purchaseOrderTitle.getProjectId());
		audit.setCustomerId(purchaseOrderTitle.getCustomerId());
		audit.setAmount(purchaseOrderTitle.getOrderTotalAmount());
		audit.setPoType(BaseConsts.INT_22); // 22表示铺货退货单
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setCurrencyId(purchaseOrderTitle.getCurrencyId());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		sendWechatMsg(newId, "您有新的铺货退货单审核【" + audit.getPoNo() + "】");
	}

	private void sendMailMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);
		BaseUser fromUser = ServiceSupport.getUser();
		BaseUser toUser = cacheService.getUserByid(newAudit.getAuditorId());

		PoTitleRespDto poTitleRespDto = distributionReturnService.queryPurchaseOrderTitleById(newAudit.getPoId())
				.getItems();
		String auditTypeStr = "";
		switch (newAudit.getAuditType()) {
		case BaseConsts.TWO:
			auditTypeStr = "转交";
			break;
		case BaseConsts.THREE:
			auditTypeStr = "加签";
			break;
		default:
			auditTypeStr = "";
			break;
		}
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("铺货退货编号");
		mail1.setColumnTwo(poTitleRespDto.getOrderNo());
		templateOnes.add(mail1);

		MailTemplateOne mail4 = new MailTemplateOne();
		mail4.setColumnOne("项目");
		mail4.setColumnTwo(poTitleRespDto.getProjectName());
		templateOnes.add(mail4);

		MailTemplateOne mail6 = new MailTemplateOne();
		mail6.setColumnOne("客户");
		mail6.setColumnTwo(poTitleRespDto.getCustomerName());
		templateOnes.add(mail6);

		MailTemplateOne mail5 = new MailTemplateOne();
		mail5.setColumnOne("销售金额");
		mail5.setColumnTwo(DecimalUtil.toAmountString(poTitleRespDto.getOrderTotalAmount())
				+ poTitleRespDto.getCurrencyTypeName());
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
		String content = msgContentService.convertMailOneContent(auditTypeStr + "铺货退货单" + typeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【铺货退货单】需要审核", content);
	}

	private void sendRTXMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);
		BaseUser fromUser = ServiceSupport.getUser();
		BaseUser toUser = cacheService.getUserByid(newAudit.getAuditorId());

		PoTitleRespDto poTitleRespDto = distributionReturnService.queryPurchaseOrderTitleById(newAudit.getPoId())
				.getItems();
		String auditTypeStr = "";
		switch (newAudit.getAuditType()) {
		case BaseConsts.TWO:
			auditTypeStr = "转交";
			break;
		case BaseConsts.THREE:
			auditTypeStr = "加签";
			break;
		default:
			auditTypeStr = "";
			break;
		}

		String content = "";

		content = content + "铺货退货单编号:" + poTitleRespDto.getOrderNo() + "\n";
		content = content + "项目:" + poTitleRespDto.getProjectName() + "\n";
		content = content + "客户:" + poTitleRespDto.getCustomerName() + "\n";
		content = content + "销售金额:" + DecimalUtil.toAmountString(poTitleRespDto.getOrderTotalAmount())
				+ poTitleRespDto.getCurrencyTypeName() + "\n";
		if (!auditTypeStr.equals("")) {
			content = content + auditTypeStr + "信息:" + "单据由" + fromUser.getChineseName() + "于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
					+ toUser.getChineseName() + "审核\n";
		}
		content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
		content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

		sendWarnRtx(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【铺货退货单】需要审核", content);
	}

	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer projectItemId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.INT_22;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(projectItemId, poType, auditflows);
		return result;
	}
}
