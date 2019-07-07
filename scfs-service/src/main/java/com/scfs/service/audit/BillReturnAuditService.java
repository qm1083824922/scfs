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
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.domain.audit.dto.req.BillDeliveryAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.BillDeliveryAuditInfo;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillDeliveryDtlSearchReqDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryDtlResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryFileResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.sale.BillDeliveryDtlService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * Created by Administrator on 2016年11月2日.
 */
@Service
public class BillReturnAuditService extends AuditService {
	@Autowired
	private BillDeliveryService billDeliveryService;
	@Autowired
	private BillDeliveryDtlService billDeliveryDtlService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private BillDeliveryDao billDeliveryDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private AuditFlowService auditFlowService;

	@Override
	public void batchPassAudit(Audit audit) {
		BillDeliveryAuditReqDto billDeliveryAuditReqDto = new BillDeliveryAuditReqDto();
		billDeliveryAuditReqDto.setAuditId(audit.getId());
		billDeliveryAuditReqDto.setBillDeliveryId(audit.getPoId());
		try {
			if (audit.getState() == BaseConsts.INT_25) {
				passFinanceAudit(billDeliveryAuditReqDto);
			} else if (audit.getState() == BaseConsts.INT_30) {
				passFinance2Audit(billDeliveryAuditReqDto);
			}
		} catch (BaseException e) {
			throw new BaseException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		BillDeliveryAuditReqDto billDeliveryAuditReqDto = new BillDeliveryAuditReqDto();
		billDeliveryAuditReqDto.setAuditId(audit.getId());
		billDeliveryAuditReqDto.setBillDeliveryId(audit.getPoId());
		try {
			unPassAudit(billDeliveryAuditReqDto);
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
			state = BaseConsts.FOUR;
		}
		return state;
	}

	public void sendMessage(AuditNode auditNode, BillDeliveryAuditReqDto billDeliveryAuditReqDto, Audit audit,
			Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailMsg(newId); // 发送邮件
			sendRTXMsg(newId); // 发送RTX
			sendWechatMsg(newId, "您有新的销售退货单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点

		}
	}

	/**
	 * 财务专员审核通过
	 */
	public void passFinanceAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) throws Exception {
		Audit audit = auditDao.queryAuditById(billDeliveryAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_25 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(billDeliveryAuditReqDto, audit);
		audit.setAuditState(BaseConsts.ONE); // 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_25, audit.getPoType(),
					audit.getProjectId());

			// 业务逻辑处理
			updateBillStatus(audit, billDeliveryAuditReqDto, getState(nextAuditNode), true); // 业务单据状态:待财务主管审核

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, billDeliveryAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务主管审核通过
	 *
	 * @throws Exception
	 */
	public void passFinance2Audit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) throws Exception {
		Audit audit = auditDao.queryAuditById(billDeliveryAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_30 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(billDeliveryAuditReqDto, audit);
		audit.setAuditState(BaseConsts.ONE); // 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_30, audit.getPoType(),
					audit.getProjectId());

			// 业务逻辑处理
			updateBillStatus(audit, billDeliveryAuditReqDto, getState(nextAuditNode), true); // 业务单据状态:待发货

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, billDeliveryAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 审核不通过
	 *
	 * @throws Exception
	 */
	public void unPassAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) throws Exception {
		Audit audit = auditDao.queryAuditById(billDeliveryAuditReqDto.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(billDeliveryAuditReqDto, audit);
		audit.setAuditState(BaseConsts.TWO);// 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态,终止流程
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			updateBillStatus(audit, billDeliveryAuditReqDto, BaseConsts.ONE, false); // 业务单据状态：待提交
		}
	}

	/**
	 * 加签
	 */
	public void sighAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		if (billDeliveryAuditReqDto.getAuditId() == null || billDeliveryAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(billDeliveryAuditReqDto.getAuditId(), billDeliveryAuditReqDto.getPauditorId());
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的销售退货单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		if (billDeliveryAuditReqDto.getAuditId() == null || billDeliveryAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(billDeliveryAuditReqDto.getAuditId(),
				billDeliveryAuditReqDto.getPauditorId());
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的销售退货单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 提交之后开始走流程
	 *
	 * @param billDelivery
	 */
	public void startAudit(BillDelivery billDelivery, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoId(billDelivery.getId());
		audit.setPoDate(billDelivery.getRequiredSendDate());
		audit.setPoNo(billDelivery.getBillNo());
		audit.setProjectId(billDelivery.getProjectId());
		audit.setCustomerId(billDelivery.getCustomerId());
		audit.setAmount(billDelivery.getRequiredSendAmount());
		audit.setCustomerId(billDelivery.getCustomerId());
		audit.setPoType(BaseConsts.INT_17); // 17-表示销售退货单
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setCurrencyId(billDelivery.getCurrencyType());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		sendWechatMsg(newId, "您有新的销售退货单审核【" + audit.getPoNo() + "】");
	}

	/**
	 * 查询销售退货单信息
	 *
	 * @param billDelivery
	 * @return
	 * @throws Exception
	 */
	public Result<BillDeliveryAuditInfo> queryBillDeliveryResultAuditInfo(BillDelivery billDelivery) throws Exception {
		Result<BillDeliveryAuditInfo> result = new Result<BillDeliveryAuditInfo>();
		Result<BillDeliveryResDto> billDeliveryResDtoResult = new Result<BillDeliveryResDto>();
		BillDelivery billDeliveryRes = billDeliveryDao.queryEntityById(billDelivery);
		BillDeliveryResDto billDeliveryResDto = billDeliveryService.convertToResDto(billDeliveryRes);

		BillDeliveryDtlSearchReqDto billDeliveryDtlSearchReqDto = new BillDeliveryDtlSearchReqDto();
		billDeliveryDtlSearchReqDto.setBillDeliveryId(billDelivery.getId());
		PageResult<BillDeliveryDtlResDto> billDeliveryDtlResDtoResult = new PageResult<BillDeliveryDtlResDto>();
		billDeliveryDtlResDtoResult = billDeliveryDtlService.queryAllBillDeliveryDtlsByBillDeliveryId(
				billDeliveryDtlSearchReqDto, false, billDelivery.getReturnTime());

		billDeliveryResDtoResult.setItems(billDeliveryResDto);

		List<BillDeliveryFileResDto> billDeliveryFileList = billDeliveryService.queryFileList(billDelivery.getId());
		BillDeliveryAuditInfo billDeliveryAuditInfo = new BillDeliveryAuditInfo();
		billDeliveryAuditInfo.setBillDeliveryResDto(billDeliveryResDtoResult.getItems());
		billDeliveryAuditInfo.setBillDeliveryDtlResDtoList(billDeliveryDtlResDtoResult.getItems());
		billDeliveryAuditInfo.setBillDeliveryFileList(billDeliveryFileList);
		result.setItems(billDeliveryAuditInfo);
		return result;
	}

	/**
	 * 查询审核记录
	 *
	 * @param billDeliveryId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer billDeliveryId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.INT_17;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(billDeliveryId, poType, auditflows);
		return result;
	}

	private void updateBillStatus(Audit audit, BillDeliveryAuditReqDto billDeliveryAuditReqDto, Integer status,
			boolean passAudit) throws Exception {
		Integer billDeliveryId = billDeliveryAuditReqDto.getBillDeliveryId();
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryId);
		billDelivery.setStatus(status);
		billDeliveryService.updateBillDelivery(billDelivery);

		if (status.equals(BaseConsts.FOUR)) { // 最终审核通过，生成退货入库单
			BillDelivery billDelivery2 = billDeliveryService.queryEntityById(billDeliveryId);
			billDeliveryService.addBillInStore(billDelivery2);
		}
		Audit audit2 = new Audit();
		audit2.setId(audit.getId());
		audit2.setAmount(billDeliveryService.queryRequiredSendAmountById(billDeliveryId));
		super.updateAudit(audit2); // 更新审核金额
	}

	private Audit convertToAudit(BillDeliveryAuditReqDto billDeliveryAuditReqDto, Audit audit) {
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(billDeliveryAuditReqDto.getSuggestion());
		return audit;
	}

	private void sendMailMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);
		BaseUser fromUser = ServiceSupport.getUser();
		BaseUser toUser = cacheService.getUserByid(newAudit.getAuditorId());

		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(newAudit.getPoId());
		BillDeliveryResDto billDeliveryDto = billDeliveryService.detailBillDeliveryById(billDelivery).getItems();
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
		mail1.setColumnOne("销售退货单编号");
		mail1.setColumnTwo(billDeliveryDto.getBillNo());
		templateOnes.add(mail1);

		MailTemplateOne mail4 = new MailTemplateOne();
		mail4.setColumnOne("项目");
		mail4.setColumnTwo(billDeliveryDto.getProjectName());
		templateOnes.add(mail4);

		MailTemplateOne mail6 = new MailTemplateOne();
		mail6.setColumnOne("客户");
		mail6.setColumnTwo(billDeliveryDto.getCustomerName());
		templateOnes.add(mail6);

		MailTemplateOne mail5 = new MailTemplateOne();
		mail5.setColumnOne("销售退货金额");
		mail5.setColumnTwo(DecimalUtil.toAmountString(billDeliveryDto.getRequiredSendAmount())
				+ billDeliveryDto.getCurrencyTypeName());
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
		String content = msgContentService.convertMailOneContent(auditTypeStr + "销售退货单" + typeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【销售退货单】需要审核", content);
	}

	private void sendRTXMsg(int auditId) {
		Audit newAudit = auditDao.queryAuditById(auditId);
		BaseUser fromUser = ServiceSupport.getUser();
		BaseUser toUser = cacheService.getUserByid(newAudit.getAuditorId());

		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(newAudit.getPoId());
		BillDeliveryResDto billDeliveryDto = billDeliveryService.detailBillDeliveryById(billDelivery).getItems();
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

		content = content + "销售退货单编号:" + billDeliveryDto.getBillNo() + "\n";
		content = content + "项目:" + billDeliveryDto.getProjectName() + "\n";
		content = content + "客户:" + billDeliveryDto.getCustomerName() + "\n";
		content = content + "销售退货金额:" + DecimalUtil.toAmountString(billDeliveryDto.getRequiredSendAmount())
				+ billDeliveryDto.getCurrencyTypeName() + "\n";
		if (!auditTypeStr.equals("")) {
			content = content + auditTypeStr + "信息:" + "单据由" + fromUser.getChineseName() + "于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
					+ toUser.getChineseName() + "审核\n";
		}
		content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
		content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

		sendWarnRtx(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【销售退货单】需要审核", content);
	}

}
