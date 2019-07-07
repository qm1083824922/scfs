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
import com.scfs.dao.audit.AuditDao;
import com.scfs.dao.logistics.BillOutStoreDao;
import com.scfs.domain.audit.dto.req.BillOutStoreAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.BillOutStoreAuditInfo;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.logistics.dto.req.BillOutStoreDtlSearchReqDto;
import com.scfs.domain.logistics.dto.req.BillOutStoreSearchReqDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreDtlResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreFileResDto;
import com.scfs.domain.logistics.dto.resp.BillOutStoreResDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.logistics.BillOutStoreDtlService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * Created by Administrator on 2016年11月2日.
 */
@Service
public class BillOutStoreAuditService extends AuditService {
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private BillOutStoreDtlService billOutStoreDtlService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private BillOutStoreDao billOutStoreDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private AuditFlowService auditFlowService;

	@Override
	public void batchPassAudit(Audit audit) {
		BillOutStoreAuditReqDto billOutStoreAuditReqDto = new BillOutStoreAuditReqDto();
		billOutStoreAuditReqDto.setAuditId(audit.getId());
		billOutStoreAuditReqDto.setBillOutStoreId(audit.getPoId());
		try {
			if (audit.getState() == BaseConsts.INT_25) {
				passFinanceAudit(billOutStoreAuditReqDto);
			} else if (audit.getState() == BaseConsts.INT_30) {
				passFinance2Audit(billOutStoreAuditReqDto);
			}
		} catch (BaseException e) {
			throw new BaseException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void batchUnPassAudit(Audit audit) {
		BillOutStoreAuditReqDto billOutStoreAuditReqDto = new BillOutStoreAuditReqDto();
		billOutStoreAuditReqDto.setAuditId(audit.getId());
		billOutStoreAuditReqDto.setBillOutStoreId(audit.getPoId());
		try {
			unPassAudit(billOutStoreAuditReqDto);
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

	public void sendMessage(AuditNode auditNode, BillOutStoreAuditReqDto billOutStoreAuditReqDto, Audit audit,
			Integer newId) {
		if (null != auditNode) { // 中间审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE, audit.getState()); // 发送邮件
			sendWechatMsg(newId, "您有新的出库单审核【" + audit.getPoNo() + "】");
		} else { // 最后一个审核节点
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.ONE, audit.getState()); // 发送邮件
		}
	}

	/**
	 * 财务专员审核通过
	 */
	public void passFinanceAudit(BillOutStoreAuditReqDto billOutStoreAuditReqDto) {
		Audit audit = auditDao.queryAuditById(billOutStoreAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_25 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(billOutStoreAuditReqDto, audit);
		audit.setAuditState(BaseConsts.ONE);// 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_25, audit.getPoType(),
					audit.getProjectId());

			// 修改出库单的状态
			updateBillStatus(billOutStoreAuditReqDto.getBillOutStoreId(), getState(nextAuditNode)); // 业务单据状态：待财务主管审核

			Integer newId = super.createNextNode2Audit(nextAuditNode, audit);// 新增下一节点
			sendMessage(nextAuditNode, billOutStoreAuditReqDto, audit, newId); // 发送消息
		}
	}

	/**
	 * 财务主管审核通过
	 */
	public void passFinance2Audit(BillOutStoreAuditReqDto billOutStoreAuditReqDto) {
		Audit audit = auditDao.queryAuditById(billOutStoreAuditReqDto.getAuditId());
		if (audit.getState() != BaseConsts.INT_30 || audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(billOutStoreAuditReqDto, audit);
		audit.setAuditState(BaseConsts.ONE);// 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			AuditNode nextAuditNode = auditFlowService.getNextAuditNode(BaseConsts.INT_30, audit.getPoType(),
					audit.getProjectId());

			// 修改出库单的状态
			updateBillStatus(billOutStoreAuditReqDto.getBillOutStoreId(), getState(nextAuditNode)); // 业务单据状态：待发货
			sendMessage(nextAuditNode, billOutStoreAuditReqDto, audit, null); // 发送消息
		}
	}

	/**
	 * 审核不通过
	 */
	public void unPassAudit(BillOutStoreAuditReqDto billOutStoreAuditReqDto) {
		Audit audit = auditDao.queryAuditById(billOutStoreAuditReqDto.getAuditId());
		if (audit.getAuditState() != BaseConsts.ZERO) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，审核状态错误!");
		}
		audit = convertToAudit(billOutStoreAuditReqDto, audit);
		audit.setAuditState(BaseConsts.TWO);// 审核状态，1表示审核通过，2表示审核不通过
		super.updateAudit(audit);// 更新审核状态,终止流程
		if (audit.getAuditType() == BaseConsts.ONE || audit.getAuditType() == BaseConsts.TWO) {
			super.closeSighAudit(audit.getId());
			// 修改出库单的状态
			updateBillStatus(billOutStoreAuditReqDto.getBillOutStoreId(), BaseConsts.ONE); // 业务单据状态：待提交
			sendMailResult(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO, BaseConsts.ZERO); // 发送邮件
		}
	}

	/**
	 * 加签
	 */
	public void sighAudit(BillOutStoreAuditReqDto billOutStoreAuditReqDto) {
		if (billOutStoreAuditReqDto.getAuditId() == null || billOutStoreAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatSighAudit(billOutStoreAuditReqDto.getAuditId(), billOutStoreAuditReqDto.getPauditorId());
		Audit audit = auditDao.queryAuditById(billOutStoreAuditReqDto.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.TWO, audit.getState()); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有加签的出库单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 转交
	 */
	public void deliverAudit(BillOutStoreAuditReqDto billOutStoreAuditReqDto) {
		if (billOutStoreAuditReqDto.getAuditId() == null || billOutStoreAuditReqDto.getPauditorId() == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，参数错误!");
		}
		int newId = super.creatDeliverAudit(billOutStoreAuditReqDto.getAuditId(),
				billOutStoreAuditReqDto.getPauditorId());
		Audit audit = auditDao.queryAuditById(billOutStoreAuditReqDto.getAuditId());
		sendMailMsg(audit.getPoId(), audit.getAuditorId(), BaseConsts.THREE, audit.getState()); // 发送邮件
		Audit newAudit = auditDao.queryAuditById(newId);
		sendWechatMsg(newId, "您有转交的出库单审核【" + newAudit.getPoNo() + "】");
	}

	/**
	 * 提交之后开始走流程
	 * 
	 * @param billOutStore
	 */
	public void startAudit(BillOutStore billOutStore, AuditNode startAuditNode) {
		Audit audit = new Audit();
		audit.setAuditType(BaseConsts.ONE);
		audit.setPoId(billOutStore.getId());
		audit.setPoDate(billOutStore.getRequiredSendDate());
		audit.setPoNo(billOutStore.getBillNo());
		audit.setProjectId(billOutStore.getProjectId());
		audit.setCustomerId(billOutStore.getCustomerId());
		audit.setAmount(billOutStore.getSendAmount());
		audit.setCustomerId(billOutStore.getCustomerId());
		audit.setPoType(BaseConsts.THREE); // 3表示出库单
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setCurrencyId(billOutStore.getCurrencyType());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程

		sendMailMsg(billOutStore.getId(), audit.getAuditorId(), BaseConsts.ONE, startAuditNode.getAuditNodeState()); // 发送邮件
		sendWechatMsg(newId, "您有新的出库单审核【" + audit.getPoNo() + "】");
	}

	/**
	 * 查询出库单信息
	 * 
	 * @param billOutStoreId
	 * @return
	 */
	public Result<BillOutStoreAuditInfo> queryBillOutStoreResultAuditInfo(Integer billOutStoreId) {
		Result<BillOutStoreAuditInfo> result = new Result<BillOutStoreAuditInfo>();
		BillOutStoreSearchReqDto billOutStoreReqDto = new BillOutStoreSearchReqDto();
		billOutStoreReqDto.setId(billOutStoreId);
		Result<BillOutStoreResDto> billOutStoreResDtoResult = new Result<BillOutStoreResDto>();
		BillOutStore billOutStoreRes = billOutStoreDao.queryById(billOutStoreReqDto);
		BillOutStoreResDto billOutStoreResDto = billOutStoreService.convertToResDto(billOutStoreRes, false, 0);
		billOutStoreResDtoResult.setItems(billOutStoreResDto);

		BillOutStoreDtlSearchReqDto billOutStoreDtlSearchReqDto = new BillOutStoreDtlSearchReqDto();
		billOutStoreDtlSearchReqDto.setBillOutStoreId(billOutStoreId);
		PageResult<BillOutStoreDtlResDto> BillOutStoreDtlResDtoResult = billOutStoreDtlService
				.queryAllBillOutStoreDtlsByBillOutStoreId(billOutStoreDtlSearchReqDto);

		List<BillOutStoreFileResDto> billOutStoreFileList = billOutStoreService.queryFileList(billOutStoreId);
		BillOutStoreAuditInfo billOutStoreAuditInfo = new BillOutStoreAuditInfo();
		billOutStoreAuditInfo.setBillOutStoreResDto(billOutStoreResDtoResult.getItems());
		billOutStoreAuditInfo.setBillOutStoreDtlResDtoList(BillOutStoreDtlResDtoResult.getItems());
		billOutStoreAuditInfo.setBillOutStoreFileList(billOutStoreFileList);
		result.setItems(billOutStoreAuditInfo);
		return result;
	}

	/**
	 * 查询审核记录
	 * 
	 * @param billOutStoreId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer billOutStoreId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.THREE;
		List<Integer> auditflows = auditFlowService.getAuditFlows(poType, null);
		result = queryAuditFlowsByCon(billOutStoreId, BaseConsts.THREE, auditflows);
		return result;
	}

	private void updateBillStatus(Integer billOutStoreId, Integer status) {
		BillOutStore billOutStore = new BillOutStore();
		billOutStore.setId(billOutStoreId);
		billOutStore.setStatus(status);
		billOutStoreService.updateBillOutStoreById(billOutStore);
	}

	private Audit convertToAudit(BillOutStoreAuditReqDto billOutStoreAuditReqDto, Audit audit) {
		audit.setAuditorPass(ServiceSupport.getUser().getChineseName());
		audit.setAuditorPassId(ServiceSupport.getUser().getId());
		audit.setAuditorPassAt(new Date());
		audit.setSuggestion(billOutStoreAuditReqDto.getSuggestion());
		return audit;
	}

	private void sendMailMsg(Integer billOutStoreId, Integer auditId, Integer type, Integer auditType) {
		BaseUser fromUser = ServiceSupport.getUser();
		BillOutStoreSearchReqDto billOutStoreReqDto = new BillOutStoreSearchReqDto();
		billOutStoreReqDto.setId(billOutStoreId);
		BillOutStore billOutStoreRes = billOutStoreDao.queryById(billOutStoreReqDto);
		BaseUser toUser = cacheService.getUserByid(auditId);
		String auditTypeStr = "";
		switch (type) {
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
		mail1.setColumnOne("出库单编号");
		mail1.setColumnTwo(billOutStoreRes.getBillNo());
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
		String content = msgContentService.convertMailOneContent("出库单"
				+ ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, auditType + "") + "审核" + auditTypeStr,
				templateOnes, null);
		sendWarnMail(toUser.getId(),
				"出库单" + ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, auditType + "") + "审核", content);
	}

	private void sendMailResult(Integer billOutStoreId, Integer auditId, Integer auditState, Integer state) {
		BillOutStoreSearchReqDto billOutStoreReqDto = new BillOutStoreSearchReqDto();
		billOutStoreReqDto.setId(billOutStoreId);
		BillOutStore billOutStoreRes = billOutStoreDao.queryById(billOutStoreReqDto);
		BaseUser toUser = cacheService.getUserByid(auditId);
		List<MailTemplateOne> templateOnes = Lists.newArrayList();
		MailTemplateOne mail1 = new MailTemplateOne();
		mail1.setColumnOne("出库单编号");
		mail1.setColumnTwo(billOutStoreRes.getBillNo());
		MailTemplateOne mail2 = new MailTemplateOne();
		mail2.setColumnOne("审核结果");
		mail2.setColumnTwo(auditState.equals(BaseConsts.ONE) ? "审核通过" : "审核不通过");
		MailTemplateOne mail3 = new MailTemplateOne();
		mail3.setColumnOne("审核人");
		mail3.setColumnTwo(toUser.getChineseName());
		templateOnes.add(mail1);
		templateOnes.add(mail2);
		templateOnes.add(mail3);
		String typeStr = "";
		if (state != BaseConsts.ZERO) {
			typeStr = ServiceSupport.getValueByBizCode(BizCodeConsts.AUDIT_STATE, state + "");
		}
		String content = msgContentService.convertMailOneContent("出库单" + typeStr + "审核结果", templateOnes, null);
		sendWarnMail(toUser.getId(), "出库单" + typeStr + "审核", content);
	}
}
