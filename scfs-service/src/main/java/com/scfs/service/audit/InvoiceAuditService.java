package com.scfs.service.audit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.dao.audit.AuditDao;
import com.scfs.dao.base.entity.BaseInvoiceDao;
import com.scfs.dao.invoice.InvoiceApplyDao;
import com.scfs.dao.invoice.InvoiceDtlInfoDao;
import com.scfs.dao.invoice.InvoiceFeeDao;
import com.scfs.dao.invoice.InvoiceInfoDao;
import com.scfs.dao.invoice.InvoiceSaleDao;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.InvoiceAuditInfo;
import com.scfs.domain.base.entity.BaseInvoice;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.invoice.entity.InvoiceApplyManager;
import com.scfs.domain.invoice.entity.InvoiceAuditModel;
import com.scfs.domain.invoice.entity.InvoiceDtlInfo;
import com.scfs.domain.invoice.entity.InvoiceFeeManager;
import com.scfs.domain.invoice.entity.InvoiceInfo;
import com.scfs.domain.invoice.entity.InvoiceInfoDtl;
import com.scfs.domain.invoice.entity.InvoiceSaleManager;
import com.scfs.domain.project.entity.ProjectItemFileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 *
 *  File: InvoiceAuditService.java
 *  Description:
 *  TODO
 *  Date,					Who,
 *  2016年11月5日				Administrator
 *
 * </pre>
 */
@Service
public class InvoiceAuditService extends AuditService {
	@Autowired
	private BaseInvoiceDao baseInvoiceDao;
	@Autowired
	private InvoiceApplyDao invoiceApplyDao;
	@Autowired
	private InvoiceSaleDao invoiceSaleDao;
	@Autowired
	private InvoiceFeeDao invoiceFeeDao;
	@Autowired
	private InvoiceInfoDao invoiceInfoDao;
	@Autowired
	private InvoiceDtlInfoDao invoiceDtlInfoDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private AuditFlowService auditFlowService;

	@Override
	public void batchPassAudit(Audit audit) {
		InvoiceAuditInfo invoiceAuditInfo = new InvoiceAuditInfo();
		invoiceAuditInfo.setAuditId(audit.getId());
		if (audit.getState() == BaseConsts.INT_25) {
			passFinanceAudit(invoiceAuditInfo);
		} else if (audit.getState() == BaseConsts.INT_30) {
			passFinance2Audit(invoiceAuditInfo);
		}

	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		InvoiceAuditInfo invoiceAuditInfo = new InvoiceAuditInfo();
		invoiceAuditInfo.setAuditId(audit.getId());
		unPassAudit(invoiceAuditInfo);
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

	public void sendMessage(AuditNode auditNode, InvoiceAuditInfo invoiceAuditInfo, Audit audit, Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailMsgPass(audit.getPoId(), audit.getAuditorId()); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "开票申请审核单据", "开票申请财务专员审核通过");
			sendWechatMsg(newId, "您有新的开票申请单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点
			sendMailMsgPass(audit.getPoId(), audit.getAuditorId()); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "开票申请审核单据", "开票申请财务主管审核通过");
		}
	}

	/**
	 * 提交之后开始走流程
	 *
	 * @param id
	 */
	public void startAudit(Integer id, AuditNode startAuditNode) {
		InvoiceApplyManager vo = invoiceApplyDao.queryEntityById(id);
		Audit audit = new Audit();
		audit.setProjectId(vo.getProjectId());
		audit.setCustomerId(vo.getCustomerId());
		audit.setBusinessUnitId(vo.getBusinessUnitId());
		audit.setPoNo(vo.getApplyNo());
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoType(BaseConsts.TEN);
		audit.setPoId(vo.getId());
		if (vo.getBillType() == 1) {
			List<InvoiceSaleManager> list = invoiceSaleDao.selectByInvoiceId(id);
			BigDecimal sum = BigDecimal.ZERO;
			for (int i = 0; i < list.size(); i++) {
				sum = sum.add(list.get(i).getProvideInvoiceAmount());
			}
			audit.setAmount(sum);
		} else if (vo.getBillType() == 2) {
			List<InvoiceFeeManager> list = invoiceFeeDao.selectByInvoiceId(id);
			BigDecimal sum = BigDecimal.ZERO;
			for (int i = 0; i < list.size(); i++) {
				sum = sum.add(list.get(i).getProvideInvoiceAmount());
			}
			audit.setAmount(sum);
		}
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposerAt(new Date());
		audit.setCurrencyId(vo.getCurrencyType());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(id, audit.getAuditorId(), BaseConsts.ONE); // 发送邮件
		sendWechatMsg(newId, "您有新的开票申请单审核【" + audit.getPoNo() + "】");
	}

	private void sendMailMsg(Integer invoiceApplyId, Integer auditId, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		InvoiceApplyManager invoiceApplyManager = invoiceApplyDao.queryEntityById(invoiceApplyId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		String auditTypeStr = "";
		switch (auditType) {
		case BaseConsts.ONE:
			auditTypeStr = "提交";
			break;
		case BaseConsts.TWO:
			auditTypeStr = "加签";
			break;
		case BaseConsts.THREE:
			auditTypeStr = "转交";
			break;
		default:
			auditTypeStr = "";
			break;
		}
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("开票申请编号");
		mail1.setColumnTwo(invoiceApplyManager.getApplyNo());
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
		String content = msgContentService.convertMailOneContent("开票申请单财务专员审核" + auditTypeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "开票申请单财务专员审核", content);
	}

	/**
	 * 财务专员审核通过
	 */
	public void passFinanceAudit(InvoiceAuditInfo invoiceAuditInfo) {
		Audit audit = auditDao.queryAuditById(invoiceAuditInfo.getAuditId());
		if (audit.getState() != BaseConsts.INT_25 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		// 业务逻辑处理
		audit.setAuditState(BaseConsts.ONE);
		audit.setSuggestion(invoiceAuditInfo.getSuggestion());
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_25, audit.getPoType(),
					audit.getProjectId());

			InvoiceApplyManager invoice = new InvoiceApplyManager();
			invoice.setId(audit.getPoId());
			invoice.setStatus(getState(nextAuditNode));
			invoiceApplyDao.updateStatus(invoice);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, invoiceAuditInfo, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务主管审核通过
	 */
	public void passFinance2Audit(InvoiceAuditInfo invoiceAuditInfo) {
		Audit audit = auditDao.queryAuditById(invoiceAuditInfo.getAuditId());
		if (audit.getState() != BaseConsts.INT_30 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		// 业务逻辑处理
		audit.setAuditState(BaseConsts.ONE);
		audit.setSuggestion(invoiceAuditInfo.getSuggestion());
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_30, audit.getPoType(),
					audit.getProjectId());

			InvoiceApplyManager invoice = new InvoiceApplyManager();
			invoice.setId(audit.getPoId());
			invoice.setStatus(getState(nextAuditNode));
			invoiceApplyDao.updateStatus(invoice);

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, invoiceAuditInfo, audit, newId); // 发送消息
		}
	}

	private void sendMailMsgPass(Integer invoiceApplyId, Integer auditId) {
		InvoiceApplyManager invoiceApplyManager = invoiceApplyDao.queryEntityById(invoiceApplyId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("开票申请编号");
		mail1.setColumnTwo(invoiceApplyManager.getApplyNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核通过信息");
		mail2.setColumnTwo("单据由" + toUser.getChineseName() + "审核通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("开票申请单财务专员审核", templateOnes, "");
		sendWarnMail(toUser.getId(), "开票申请单财务专员审核", content);
	}

	/**
	 * 审核不通过
	 */
	public void unPassAudit(InvoiceAuditInfo invoiceAuditInfo) {
		Audit audit = auditDao.queryAuditById(invoiceAuditInfo.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit.setAuditState(BaseConsts.TWO);
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(invoiceAuditInfo.getSuggestion());
		super.updateAudit(audit);// 更新审核状态,终止流程
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			InvoiceApplyManager vo = new InvoiceApplyManager();
			vo.setId(invoiceAuditInfo.getInvoiceApplyId());
			vo.setStatus(BaseConsts.TWO);
			invoiceApplyDao.updateStatus(vo);
			sendMailMsgUnPass(invoiceAuditInfo.getInvoiceApplyId(), audit.getAuditorId()); // 发送邮件
			sendWarnRtx(audit.getAuditorId(), "开票申请审核单据", "开票申请审核不通过");
		}
	}

	private void sendMailMsgUnPass(Integer invoiceApplyId, Integer auditId) {
		InvoiceApplyManager invoiceApplyManager = invoiceApplyDao.queryEntityById(invoiceApplyId);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("开票申请编号");
		mail1.setColumnTwo(invoiceApplyManager.getApplyNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核驳回信息");
		mail2.setColumnTwo("单据由" + toUser.getChineseName() + "审核驳回");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("驳回人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String content = msgContentService.convertMailOneContent("开票申请单财务专员审核", templateOnes, "");
		sendWarnMail(toUser.getId(), "开票申请单财务专员审核", content);
	}

	public Result<InvoiceAuditModel> queryData(Integer poId) {
		Result<InvoiceAuditModel> result = new Result<InvoiceAuditModel>();
		List<InvoiceInfoDtl> InvoiceInfoDtlList = new LinkedList<InvoiceInfoDtl>();
		InvoiceAuditModel invoiceAuditModel = new InvoiceAuditModel();
		InvoiceApplyManager invoiceApplyManager = new InvoiceApplyManager();
		invoiceApplyManager.setId(poId);
		invoiceApplyManager = invoiceApplyDao.queryEntityById(poId);
		invoiceApplyManager = convertInvoiceManagerRes(invoiceApplyManager);
		if (invoiceApplyManager.getBillType() == 1) {
			List<InvoiceSaleManager> invoiceSaleManagerList = invoiceSaleDao.selectByInvoiceId(poId);
			BigDecimal sum = BigDecimal.ZERO;
			for (int i = 0; i < invoiceSaleManagerList.size(); i++) {
				InvoiceSaleManager invoice = invoiceSaleManagerList.get(i);
				invoiceSaleManagerList.get(i).setNumber(cacheService.getGoodsById(invoice.getGoodsId()).getNumber());
				invoiceSaleManagerList.get(i).setName(cacheService.getGoodsById(invoice.getGoodsId()).getName());
				sum = sum.add(invoice.getProvideInvoiceAmount());
			}
			invoiceApplyManager.setApplyAmount(sum);
			invoiceAuditModel.setInvoiceSaleManagerList(invoiceSaleManagerList);
		} else if (invoiceApplyManager.getBillType() == 2) {
			List<InvoiceFeeManager> invoiceFeeManagerList = invoiceFeeDao.selectByInvoiceId(poId);
			BigDecimal sum = BigDecimal.ZERO;
			for (int i = 0; i < invoiceFeeManagerList.size(); i++) {
				invoiceFeeManagerList.get(i).setFeeTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.FEE_TYPE,
						invoiceFeeManagerList.get(i).getFeeType() + ""));
				sum = sum.add(invoiceFeeManagerList.get(i).getProvideInvoiceAmount());
			}
			invoiceApplyManager.setApplyAmount(sum);
			invoiceAuditModel.setInvoiceFeeManagerList(invoiceFeeManagerList);
		}
		List<InvoiceInfo> invoiceInfo = invoiceInfoDao.selectByApplyId(poId);
		for (int i = 0; i < invoiceInfo.size(); i++) {
			BaseInvoice invoice = baseInvoiceDao.queryInvoiceById(invoiceApplyManager.getBaseInvoiceId());
			if (invoiceApplyManager.getBaseInvoiceId() != null) {
				invoiceInfo.get(i).setTaxPay(invoice.getTaxPayer());
				invoiceInfo.get(i).setPhoneNumber(invoice.getPhoneNumber());
				invoiceInfo.get(i).setAccountNo(invoice.getAccountNo());
				invoiceInfo.get(i).setBankName(invoice.getBankName());
				invoiceInfo.get(i).setAddress(invoice.getAddress());
				invoiceInfo.get(i).setStatusName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_STATUS,
						invoiceInfo.get(i).getStatus() + ""));
			}
			invoiceInfo.get(i).setInvoiceInAmount(
					invoiceInfo.get(i).getInRateAmount().subtract(invoiceInfo.get(i).getDiscountInRateAmount()));
			invoiceInfo.get(i).setInvoiceExAmount(
					invoiceInfo.get(i).getExRateAmount().subtract(invoiceInfo.get(i).getDiscountExRateAmount()));
			invoiceInfo.get(i).setInvoiceRateAmount(
					invoiceInfo.get(i).getRateAmount().subtract(invoiceInfo.get(i).getDiscountRateAmount()));
			invoiceInfo.get(i).setCustomerName(
					cacheService.getCustomerById(invoiceApplyManager.getCustomerId()).getChineseName());
			InvoiceInfoDtl invoiceInfoDtl = new InvoiceInfoDtl();
			invoiceInfoDtl.setInvoiceInfoList(invoiceInfo.get(i));
			List<InvoiceDtlInfo> invoiceDtlInfoList = invoiceDtlInfoDao.selectByApplyId(invoiceInfo.get(i).getId());
			invoiceInfoDtl.setId(invoiceApplyManager.getId());
			invoiceInfoDtl.setInvoiceDtlInfoList(invoiceDtlInfoList);
			InvoiceInfoDtlList.add(invoiceInfoDtl);

		}
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusType(BaseConsts.SIX);
		fileAttReqDto.setBusId(invoiceApplyManager.getId());
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<ProjectItemFileAttach> list = convertToResDto(fielAttach);
		invoiceAuditModel.setProjectItemFileAttachList(list);
		invoiceAuditModel.setInvoiceApplyManager(invoiceApplyManager);
		invoiceAuditModel.setInvoiceInfoDtl(InvoiceInfoDtlList);
		result.setItems(invoiceAuditModel);
		return result;
	}

	private InvoiceApplyManager convertInvoiceManagerRes(InvoiceApplyManager invoiceApplyManager) {
		invoiceApplyManager.setProjectName(cacheService.getProjectNameById(invoiceApplyManager.getProjectId()));
		invoiceApplyManager.setCustomerName(
				cacheService.getSubjectNameByIdAndKey(invoiceApplyManager.getCustomerId(), CacheKeyConsts.BCS));
		invoiceApplyManager.setInvoiceTypeName(ServiceSupport.getValueByBizCode(BizCodeConsts.INVOICE_TYPE,
				invoiceApplyManager.getInvoiceType() + ""));
		invoiceApplyManager.setBillTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DOCUMENT_TYPE, invoiceApplyManager.getBillType() + ""));
		invoiceApplyManager.setFeeTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.BILL_DETAIL, invoiceApplyManager.getFeeType() + ""));
		invoiceApplyManager.setBusinessUnitName(cacheService
				.getSubjectNameByIdAndKey(invoiceApplyManager.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		if (invoiceApplyManager.getInvoiceType() == 3) {
			invoiceApplyManager.setIsElecInvoiceValue(ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS,
					invoiceApplyManager.getIsElecInvoice() + ""));
		}
		return invoiceApplyManager;
	}

	/**
	 * 转交
	 */
	public void deliverAudit(InvoiceAuditInfo invoiceAuditInfo) {
		if (invoiceAuditInfo.getAuditId() == null || invoiceAuditInfo.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(invoiceAuditInfo.getAuditId(), invoiceAuditInfo.getPauditorId());
		Audit audit = auditDao.queryAuditById(invoiceAuditInfo.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.THREE); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的开票申请单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 加签
	 */
	public void sighAudit(InvoiceAuditInfo invoiceAuditInfo) {
		if (invoiceAuditInfo.getAuditId() == null || invoiceAuditInfo.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(invoiceAuditInfo.getAuditId(), invoiceAuditInfo.getPauditorId());
		Audit audit = auditDao.queryAuditById(invoiceAuditInfo.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的开票申请单审核【" + newAudit.getPoNo() + "】");
	}

	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer invoiceApplyId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.TEN;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(invoiceApplyId, poType, auditflows);
		return result;
	}

	private List<ProjectItemFileAttach> convertToResDto(List<FileAttach> fileAttach) {
		List<ProjectItemFileAttach> list = new LinkedList<ProjectItemFileAttach>();
		for (int i = 0; i < fileAttach.size(); i++) {
			ProjectItemFileAttach projectItemFileAttach = new ProjectItemFileAttach();
			projectItemFileAttach.setId(fileAttach.get(i).getId());
			projectItemFileAttach.setBusId(fileAttach.get(i).getBusId());
			projectItemFileAttach.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, fileAttach.get(i).getBusType() + ""));
			projectItemFileAttach.setName(fileAttach.get(i).getName());
			projectItemFileAttach.setType(fileAttach.get(i).getType());
			projectItemFileAttach.setCreateAt(fileAttach.get(i).getCreateAt());
			projectItemFileAttach.setCreator(fileAttach.get(i).getCreator());
			list.add(projectItemFileAttach);
		}
		return list;
	}
}
