package com.scfs.service.audit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
import com.scfs.dao.fi.BankReceiptDao;
import com.scfs.dao.sale.BillDeliveryDao;
import com.scfs.dao.sale.BillDeliveryDtlDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.dto.req.BillDeliveryAuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.audit.model.BillDeliveryAuditInfo;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.base.entity.BaseUser;
import com.scfs.domain.common.MailTemplateOne;
import com.scfs.domain.fi.dto.req.BankReceiptSearchReqDto;
import com.scfs.domain.fi.dto.resp.BankReceiptResDto;
import com.scfs.domain.fi.entity.BankReceipt;
import com.scfs.domain.logistics.dto.req.BankReceiptAdvanceMentReqDto;
import com.scfs.domain.logistics.dto.req.VerificationAdvanceReqDto;
import com.scfs.domain.logistics.entity.BillOutStore;
import com.scfs.domain.logistics.entity.VerificationAdvance;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.domain.sale.dto.req.BillDeliveryDtlSearchReqDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryDtlResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryFileResDto;
import com.scfs.domain.sale.dto.resp.BillDeliveryResDto;
import com.scfs.domain.sale.entity.BillDelivery;
import com.scfs.domain.sale.entity.BillDeliveryDtl;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.MsgContentService;
import com.scfs.service.fi.BankReceiptService;
import com.scfs.service.logistics.BillOutStoreService;
import com.scfs.service.logistics.VerificationAdvanceService;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.sale.BillDeliveryDtlService;
import com.scfs.service.sale.BillDeliveryService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * Created by Administrator on 2016年11月2日.
 */
@Service
public class BillDeliveryAuditService extends AuditService {
	@Autowired
	private BillDeliveryService billDeliveryService;
	@Autowired
	private BillDeliveryDtlService billDeliveryDtlService;
	@Autowired
	private BillOutStoreService billOutStoreService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BankReceiptService bankReceiptService;
	@Autowired
	private AuditDao auditDao;
	@Autowired
	private BillDeliveryDao billDeliveryDao;
	@Autowired
	private BillDeliveryDtlDao billDeliveryDtlDao;
	@Autowired
	private BankReceiptDao bankReceiptDao;
	@Autowired
	private VerificationAdvanceService verificationAdvanceService;
	@Autowired
	private MsgContentService msgContentService;
	@Autowired
	private ProjectItemService projectItemService;
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
			sendWechatMsg(newId, "您有新的销售单审核【" + audit.getPoNo() + "】");
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

			// 修改销售单、出库单的状态、插入核销预收
			billDeliveryAuditReqDto = saveVerificationAdvance(billDeliveryAuditReqDto);
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
			// 修改销售单、出库单的状态、回退核销预收
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(billDeliveryAuditReqDto.getBillDeliveryId());
			billDelivery = billDeliveryDao.queryEntityById(billDelivery);
			ProjectItem projectItem = cacheService.getProjectItemByPid(billDelivery.getProjectId());
			if (null == projectItem) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目没有条款!");
			}
			Integer settleType = projectItem.getSettleType();
			if (null == settleType) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目条款未设置客户结算方式!");
			}
			if (settleType.equals(BaseConsts.TWO)) { // 2-款到放货
				billDeliveryService.reCalcPrice(billDeliveryAuditReqDto.getBillDeliveryId(), null);
				verificationAdvanceService.rollBackReceiptAdvance(billDeliveryAuditReqDto.getBillDeliveryId());
			}
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
		sendWechatMsg(newId, "您有加签的销售单审核【" + newAudit.getPoNo() + "】");
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
		sendWechatMsg(newId, "您有转交的销售单审核【" + newAudit.getPoNo() + "】");
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
		audit.setPoType(BaseConsts.TWO); // 2表示销售单
		audit.setProposerId(ServiceSupport.getUser().getId());
		audit.setProposer(ServiceSupport.getUser().getChineseName());
		audit.setProposerAt(new Date());
		audit.setCurrencyId(billDelivery.getCurrencyType());
		super.createSubmitAudit(audit);// 提交节点

		audit.setState(startAuditNode.getAuditNodeState());
		int newId = super.createStartNode2Audit(startAuditNode, audit); // 开始审核流程
		sendMailMsg(newId); // 发送邮件
		sendRTXMsg(newId); // 发送RTX
		sendWechatMsg(newId, "您有新的销售单审核【" + audit.getPoNo() + "】");
	}

	/**
	 * 查询销售单信息
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

		ProjectItem projectItem = cacheService.getProjectItemByPid(billDeliveryRes.getProjectId());
		if (null == projectItem) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目没有设置条款!");
		}
		billDeliveryResDto.setSettleType(projectItem.getSettleType());

		boolean reCalcPrice = false;
		Integer isChangePrice = billDeliveryRes.getIsChangePrice(); // 是否改价
		PageResult<BillDeliveryDtlResDto> billDeliveryDtlResDtoResult = new PageResult<BillDeliveryDtlResDto>();
		if (null != projectItem.getSettleType() && projectItem.getSettleType() == BaseConsts.ONE) { // 1-赊销，需重算提价金额
			billDeliveryDtlResDtoResult = billDeliveryDtlService.queryAllBillDeliveryDtlsByBillDeliveryId(
					billDeliveryDtlSearchReqDto, reCalcPrice, billDelivery.getReturnTime());
			BillDeliveryResDto billDeliveryResDto2 = queryServiceAmount(billDeliveryRes); // 服务金额
			billDeliveryResDto.setServiceAmount(DecimalUtil.formatScale2(billDeliveryResDto2.getServiceAmount()));
		} else {
			if (isChangePrice.equals(BaseConsts.ZERO) && projectItem.getIsFundAccount().equals(BaseConsts.ONE)
					&& null != billDelivery.getReturnTime()) { // 0-销售单未改价，且1-资金占用
				reCalcPrice = true;
				billDeliveryRes.setReturnTime(billDelivery.getReturnTime());
			}
			billDeliveryDtlResDtoResult = billDeliveryDtlService.queryAllBillDeliveryDtlsByBillDeliveryId(
					billDeliveryDtlSearchReqDto, reCalcPrice, billDelivery.getReturnTime());
			// 获取服务金额
			BillDeliveryResDto billDeliveryResDto2 = queryServiceAmount(billDeliveryRes); // 服务金额
			billDeliveryResDto.setServiceAmount(DecimalUtil.formatScale2(billDeliveryResDto2.getServiceAmount()));
			if (null != billDelivery.getReturnTime() && isChangePrice.equals(BaseConsts.ZERO)) { // 0-销售单未改价
				billDeliveryResDto
						.setRequiredSendAmount(DecimalUtil.formatScale2(billDeliveryResDto2.getRequiredSendAmount()));
			}
		}
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
	 * 查询水单信息
	 *
	 * @param billDeliveryAuditReqDto
	 * @return
	 * @throws Exception
	 */
	public PageResult<BankReceiptResDto> queryFiBankReceipt(BillDeliveryAuditReqDto billDeliveryAuditReqDto)
			throws Exception {
		PageResult<BankReceiptResDto> result = new PageResult<BankReceiptResDto>();
		Audit audit = auditDao.queryAuditById(billDeliveryAuditReqDto.getAuditId());
		if (audit.getState() == BaseConsts.INT_30 || audit.getAuditState() != BaseConsts.ZERO) {
			List<BankReceiptResDto> bankReceiptResDtoList = Lists.newArrayList();
			List<BankReceipt> bankReceiptList = verificationAdvanceService
					.queryBankReceiptByBillDeliveryId(billDeliveryAuditReqDto.getId());
			if (!CollectionUtils.isEmpty(bankReceiptList)) {
				for (BankReceipt bankReceipt : bankReceiptList) {
					BankReceiptResDto bankReceiptResDto = bankReceiptService.convertToBankReceiptResDto(bankReceipt,
							true);
					if (null != bankReceiptResDto) {
						bankReceiptResDto.setAvailableAmount(bankReceiptResDto.getVerificationAdvanceAmount());
						bankReceiptResDto.setChecked(true);
						bankReceiptResDto.setDisabled(true);
						bankReceiptResDtoList.add(bankReceiptResDto);
					}
				}
			}
			result.setItems(bankReceiptResDtoList);
		} else {
			String currReceivedDate = billDeliveryAuditReqDto.getReceiptDate();
			BillDelivery billDelivery = new BillDelivery();
			billDelivery.setId(billDeliveryAuditReqDto.getId());
			billDelivery = billDeliveryDao.queryEntityById(billDelivery);
			if (null != billDelivery && StringUtils.isNotBlank(currReceivedDate)) {
				BaseProject baseProject = cacheService.getProjectById(billDelivery.getProjectId());
				ProjectItem projectItem = cacheService.getProjectItemByPid(billDelivery.getProjectId());
				if (null != baseProject && null != projectItem) {
					Integer settleType = projectItem.getSettleType();
					if (null == settleType) {
						throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目条款未设置客户结算方式!");
					}
					if (settleType == BaseConsts.TWO) { // 2-款到放货
						BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
						BankReceiptSearchReqDto bankReceiptSearchReqDto = new BankReceiptSearchReqDto();
						bankReceiptSearchReqDto.setBusiUnit(busiUnit.getId());
						bankReceiptSearchReqDto.setProjectId(billDelivery.getProjectId());
						bankReceiptSearchReqDto.setCustId(billDelivery.getCustomerId());
						bankReceiptSearchReqDto.setCurrencyType(billDelivery.getCurrencyType());
						List<BankReceipt> bankReceiptList = bankReceiptDao
								.queryResultsByCon4BillDelivery(bankReceiptSearchReqDto);
						List<BankReceiptResDto> bankReceiptResDtoList = Lists.newArrayList();
						if (!CollectionUtils.isEmpty(bankReceiptList)) {
							for (BankReceipt bankReceipt : bankReceiptList) {
								BankReceiptResDto bankReceiptResDto = bankReceiptService
										.convertToBankReceiptResDto(bankReceipt, true);
								if (null != bankReceiptResDto) {
									Date parentReceiptDate = bankReceiptResDto.getParentReceiptDate();
									if (bankReceiptResDto.getReceiptType().equals(BaseConsts.ONE)
											|| bankReceiptResDto.getReceiptType().equals(BaseConsts.THREE)) {
										String parentReceiptDateStr = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
												parentReceiptDate);
										if (!currReceivedDate.equals(parentReceiptDateStr)) {
											bankReceiptResDto.setDisabled(true);
										}
									}
									bankReceiptResDto.setAvailableAmount(bankReceiptResDto.getRemainAmount());
									bankReceiptResDtoList.add(bankReceiptResDto);
								}
							}
						}
						// 自动赋值选择金额
						filterBankReceipt(currReceivedDate, billDelivery, bankReceiptResDtoList, baseProject);
						// 结果集排序
						sortBankReceipt(bankReceiptResDtoList);
						result.setItems(bankReceiptResDtoList);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 根据水单日期查询水单预收货款的数据
	 * 
	 * @param billDeliveryAuditReqDto
	 * @return
	 */
	public PageResult<BankReceiptResDto> queryFiBankReceiptAdvance(BillDeliveryAuditReqDto billDeliveryAuditReqDto) {
		PageResult<BankReceiptResDto> result = new PageResult<BankReceiptResDto>();
		String currReceivedDate = billDeliveryAuditReqDto.getReceiptDate();
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryAuditReqDto.getId());
		billDelivery = billDeliveryDao.queryEntityById(billDelivery);
		if (null != billDelivery && StringUtils.isNotBlank(currReceivedDate)) {
			BaseProject baseProject = cacheService.getProjectById(billDelivery.getProjectId());
			ProjectItem projectItem = cacheService.getProjectItemByPid(billDelivery.getProjectId());
			if (null != baseProject && null != projectItem) {
				Integer settleType = projectItem.getSettleType();
				if (null == settleType) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目条款未设置客户结算方式!");
				}
				BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
				BankReceiptSearchReqDto bankReceiptSearchReqDto = new BankReceiptSearchReqDto();
				bankReceiptSearchReqDto.setBusiUnit(busiUnit.getId());
				bankReceiptSearchReqDto.setProjectId(billDelivery.getProjectId());
				bankReceiptSearchReqDto.setCustId(billDelivery.getCustomerId());
				bankReceiptSearchReqDto.setCurrencyType(billDelivery.getCurrencyType());
				bankReceiptSearchReqDto.setStartReceiptDate(currReceivedDate);// 开始日期
				List<BankReceiptResDto> bankReceiptResDtoList = Lists.newArrayList();
				List<BankReceipt> bankReceiptList = bankReceiptDao
						.queryBankReceptTypeThreeAllResult(bankReceiptSearchReqDto);
				if (!CollectionUtils.isEmpty(bankReceiptList)) {
					for (BankReceipt bankReceipt : bankReceiptList) {
						BankReceiptResDto bankReceiptResDto = bankReceiptService.convertToBankReceiptResDto(bankReceipt,
								true);
						bankReceiptResDto.setAvailableAmount(bankReceiptResDto.getRemainAmount());
						bankReceiptResDtoList.add(bankReceiptResDto);
					}
				}
				result.setItems(bankReceiptResDtoList);
			}
		}
		return result;
	}

	/**
	 * 查询水单信息日期列表
	 *
	 * @param billDeliveryId
	 * @return
	 */
	public PageResult<CodeValue> queryFiBankReceiptDateList(Integer billDeliveryId) {
		PageResult<CodeValue> result = new PageResult<CodeValue>();
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryId);
		billDelivery = billDeliveryDao.queryEntityById(billDelivery);
		List<CodeValue> codeValueList = new ArrayList<CodeValue>();
		Set<String> receiptDateSet = new HashSet<String>();
		String currReceiptDate = null;
		if (null != billDelivery) {
			Integer isChangePrice = billDelivery.getIsChangePrice(); // 是否改价
			Date requiredSendDate = billDelivery.getRequiredSendDate();
			String requiredSendDateStr = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, requiredSendDate);

			BaseProject baseProject = cacheService.getProjectById(billDelivery.getProjectId());
			ProjectItem projectItem = cacheService.getProjectItemByPid(billDelivery.getProjectId());
			if (null != baseProject) {
				BaseSubject busiUnit = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
				BankReceiptSearchReqDto bankReceiptSearchReqDto = new BankReceiptSearchReqDto();
				bankReceiptSearchReqDto.setBusiUnit(busiUnit.getId());
				bankReceiptSearchReqDto.setProjectId(billDelivery.getProjectId());
				bankReceiptSearchReqDto.setCustId(billDelivery.getCustomerId());
				bankReceiptSearchReqDto.setCurrencyType(billDelivery.getCurrencyType());
				List<BankReceipt> bankReceiptList = bankReceiptDao
						.queryResultsByCon4BillDelivery(bankReceiptSearchReqDto);
				if (!CollectionUtils.isEmpty(bankReceiptList)) {
					for (BankReceipt bankReceipt : bankReceiptList) {
						BankReceiptResDto bankReceiptResDto = bankReceiptService.convertToBankReceiptResDto(bankReceipt,
								true);
						if (null != bankReceiptResDto) {
							if (bankReceiptResDto.getReceiptType().equals(BaseConsts.ONE)
									|| bankReceiptResDto.getReceiptType().equals(BaseConsts.THREE)) { // 过滤预收定金日期
								String parentReceiptDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
										bankReceiptResDto.getParentReceiptDate());
								if (projectItem.getIsFundAccount().equals(BaseConsts.TWO)) { // 2-价差(由贸易改成价差)
									if (requiredSendDateStr.equals(parentReceiptDate)) {
										currReceiptDate = requiredSendDateStr;
									} else {
										receiptDateSet.add(parentReceiptDate);
									}
								} else if (projectItem.getIsFundAccount().equals(BaseConsts.ONE)) { // 1-资金占用(由服务改成资金占用)
									if (isChangePrice.equals(BaseConsts.ZERO)) {// 0-销售单未改价
										if (requiredSendDateStr.equals(parentReceiptDate)) {
											currReceiptDate = requiredSendDateStr;
										} else {
											receiptDateSet.add(parentReceiptDate);
										}
									} else if (isChangePrice.equals(BaseConsts.ONE)) {// 1-改价
										if (requiredSendDateStr.equals(parentReceiptDate)) {
											receiptDateSet.add(parentReceiptDate);
										}
									}
								}
							}
						}
					}
				}
				receiptDateSet = sortSet(receiptDateSet);
			}
		}
		if (null != currReceiptDate) {
			CodeValue codeValue = new CodeValue();
			codeValue.setCode(currReceiptDate);
			codeValue.setValue(currReceiptDate);
			codeValueList.add(codeValue);
		}
		if (!CollectionUtils.isEmpty(receiptDateSet)) {
			for (String receiptDate : receiptDateSet) {
				CodeValue codeValue = new CodeValue();
				codeValue.setCode(receiptDate);
				codeValue.setValue(receiptDate);
				codeValueList.add(codeValue);
			}
		}
		if (!CollectionUtils.isEmpty(codeValueList)) {
			result.setItems(codeValueList);
		}
		return result;
	}

	public Set<String> sortSet(Set<String> set) {
		if (CollectionUtils.isEmpty(set)) {
			return null;
		}
		List<String> setList = new ArrayList<String>(set);
		Collections.sort(setList, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) { // 升序排序
				return o1.toString().compareTo(o2.toString());
			}
		});
		set = new LinkedHashSet<String>(setList);
		return set;
	}

	public void filterBankReceipt(String currReceivedDate, BillDelivery billDelivery,
			List<BankReceiptResDto> bankReceiptResDtoList, BaseProject baseProject) throws Exception {
		Date returnTime = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, currReceivedDate);
		billDelivery.setReturnTime(returnTime);
		if (!CollectionUtils.isEmpty(bankReceiptResDtoList)) {
			BillDeliveryResDto billDeliveryResDto = queryServiceAmount(billDelivery);
			BigDecimal costAmount = (null == billDelivery.getCostAmount() ? BigDecimal.ZERO
					: billDelivery.getCostAmount());
			BigDecimal payAmount = (null == billDelivery.getPayAmount() ? BigDecimal.ZERO
					: billDelivery.getPayAmount());
			BigDecimal depositAmount = DecimalUtil.subtract(costAmount, payAmount); // 成本金额-资金占用金额
			if (depositAmount.compareTo(BigDecimal.ZERO) < 0) {
				depositAmount = BigDecimal.ZERO;
			}
			BigDecimal requiredSendAmount = BigDecimal.ZERO;
			if (billDelivery.getIsChangePrice().equals(BaseConsts.ZERO)) { // 0-销售单未改价
				requiredSendAmount = billDeliveryResDto.getRequiredSendAmount();
			} else {
				requiredSendAmount = billDelivery.getRequiredSendAmount();
			}
			BigDecimal bankReceiptAmount = DecimalUtil.subtract(requiredSendAmount, depositAmount); // 总金额-预收定金

			List<BankReceiptResDto> preDepositList = Lists.newArrayList();
			Map<String, List<BankReceiptResDto>> map = new HashMap<String, List<BankReceiptResDto>>();
			for (BankReceiptResDto bankReceiptResDto : bankReceiptResDtoList) {
				String receiptDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
						bankReceiptResDto.getParentReceiptDate());
				if (currReceivedDate.equals(receiptDate)) { // 到账日期一致
					if (bankReceiptResDto.getReceiptType().equals(BaseConsts.ONE)
							|| bankReceiptResDto.getReceiptType().equals(BaseConsts.THREE)) {
						List<BankReceiptResDto> list = null;
						if (map.containsKey(receiptDate)) {
							list = map.get(receiptDate);
						} else {
							list = new ArrayList<BankReceiptResDto>();
						}
						list.add(bankReceiptResDto);
						Collections.sort(list, new Comparator<BankReceiptResDto>() {
							public int compare(BankReceiptResDto o1, BankReceiptResDto o2) { // 升序排序
								return o1.getReceiptType().compareTo(o2.getReceiptType());
							}
						});
						map.put(receiptDate, list);
					}
				}
				if (bankReceiptResDto.getReceiptType().equals(BaseConsts.TWO)) {
					preDepositList.add(bankReceiptResDto);
				}
			}
			List<Map.Entry<String, List<BankReceiptResDto>>> list = new ArrayList<Map.Entry<String, List<BankReceiptResDto>>>(
					map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, List<BankReceiptResDto>>>() {
				public int compare(Entry<String, List<BankReceiptResDto>> o1,
						Entry<String, List<BankReceiptResDto>> o2) { // 升序排序
					return o1.getKey().compareTo(o2.getKey());
				}
			});

			BigDecimal leftAmount = depositAmount;
			if (!CollectionUtils.isEmpty(preDepositList)) {
				if (depositAmount.compareTo(BigDecimal.ZERO) > 0) {
					Collections.sort(preDepositList, new Comparator<BankReceiptResDto>() {
						public int compare(BankReceiptResDto o1, BankReceiptResDto o2) { // 升序排序
							return o1.getParentReceiptDate().compareTo(o2.getParentReceiptDate());
						}
					});
					leftAmount = matchBankReceipt(depositAmount, preDepositList);
				}
			}
			if (leftAmount.compareTo(BigDecimal.ZERO) > 0) {
				bankReceiptAmount = DecimalUtil.add(bankReceiptAmount, leftAmount);
			}
			if (bankReceiptAmount.compareTo(BigDecimal.ZERO) > 0) {
				boolean isMatch = false;
				BigDecimal maxTotalAmount = BigDecimal.ZERO;
				int maxIndex = 0;
				if (!CollectionUtils.isEmpty(list)) {
					for (int i = 0; i < list.size(); i++) {
						Map.Entry<String, List<BankReceiptResDto>> entry = list.get(i);
						List<BankReceiptResDto> bankReceiptList = entry.getValue();
						BigDecimal totalAmount = BigDecimal.ZERO;
						for (BankReceiptResDto bankReceiptResDto : bankReceiptList) {
							BigDecimal remainAmount = bankReceiptResDto.getRemainAmount();
							totalAmount = DecimalUtil.add(totalAmount, remainAmount);
						}
						if (maxTotalAmount.compareTo(totalAmount) < 0) {
							maxTotalAmount = totalAmount;
							maxIndex = i;
						}
						if (totalAmount.compareTo(bankReceiptAmount) >= 0) {
							matchBankReceipt(bankReceiptAmount, bankReceiptList);
							isMatch = true;
							break;
						}
					}
					if (isMatch == false) {
						Map.Entry<String, List<BankReceiptResDto>> entry = list.get(maxIndex);
						List<BankReceiptResDto> bankReceiptResList = entry.getValue();
						matchBankReceipt(bankReceiptAmount, bankReceiptResList);
					}
				}
			}
		}
	}

	private BigDecimal matchBankReceipt(BigDecimal matchAmount, List<BankReceiptResDto> bankReceiptResDtoList) {
		for (BankReceiptResDto bankReceiptResDto : bankReceiptResDtoList) {
			BigDecimal amount = BigDecimal.ZERO;
			BigDecimal remainAmount = bankReceiptResDto.getRemainAmount();
			if (DecimalUtil.ge(remainAmount, matchAmount)) {
				amount = matchAmount;
			} else {
				amount = remainAmount;
			}
			bankReceiptResDto.setAvailableAmount(amount);
			bankReceiptResDto.setChecked(true);
			matchAmount = DecimalUtil.subtract(matchAmount, amount);
			if (DecimalUtil.eq(matchAmount, BigDecimal.ZERO)) {
				break;
			}
		}
		return matchAmount;
	}

	private void sortBankReceipt(List<BankReceiptResDto> bankReceiptResDtoList) {
		Collections.sort(bankReceiptResDtoList, new Comparator<BankReceiptResDto>() {
			public int compare(BankReceiptResDto o1, BankReceiptResDto o2) { // 升序排序
				int result = o1.getReceiptType().compareTo(o2.getReceiptType());
				if (result == 0) {
					return o1.getParentReceiptDate().compareTo(o2.getParentReceiptDate());
				} else {
					return result;
				}
			}
		});
	}

	/**
	 * 查询审核记录
	 *
	 * @param billDeliveryId
	 * @return
	 */
	public PageResult<AuditFlowsResDto> queryAuditFlows(Integer billDeliveryId) {
		PageResult<AuditFlowsResDto> result = new PageResult<AuditFlowsResDto>();
		Integer poType = BaseConsts.TWO;
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
		if (null != billDeliveryAuditReqDto.getReturnTime()) {
			billDelivery.setReturnTime(billDeliveryAuditReqDto.getReturnTime());
		}
		billDeliveryService.updateBillDelivery(billDelivery);
		BillOutStore billOutStore = billOutStoreService.queryValidBillOutStoreByBillDeliveryId(billDeliveryId);
		if (passAudit == true) { // 审核通过
			if (null != billOutStore) {
				billOutStore.setStatus(status);
			}
			if (status.equals(BaseConsts.INT_30)) {
				billDelivery = billDeliveryDao.queryEntityById(billDelivery);
				if (billDelivery.getIsChangePrice().equals(BaseConsts.ZERO)) { // 0-销售单未改价
					billOutStoreService.updateBillOutStore4BillDeliveryAudit(billOutStore);
				} else {
					billOutStoreService.updateBillOutStoreById(billOutStore);
				}
			} else {
				billOutStoreService.updateBillOutStoreById(billOutStore);
			}

		} else {
			// 作废出库单
			billOutStoreService.deleteBillOutStoreById(billOutStore, BaseConsts.THREE);
		}
		Audit audit2 = new Audit();
		audit2.setId(audit.getId());
		audit2.setAmount(billDeliveryService.queryRequiredSendAmountById(billDeliveryId));
		super.updateAudit(audit2); // 更新审核金额
	}

	private BillDeliveryAuditReqDto saveVerificationAdvance(BillDeliveryAuditReqDto billDeliveryAuditReqDto)
			throws Exception {
		String receiptDate = null;
		Date returnTime = null;
		BillDelivery billDelivery = new BillDelivery();
		billDelivery.setId(billDeliveryAuditReqDto.getBillDeliveryId());
		billDelivery = billDeliveryDao.queryEntityById(billDelivery);
		ProjectItem projectItem = cacheService.getProjectItemByPid(billDelivery.getProjectId());
		if (null == projectItem) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目没有条款!");
		}
		Integer settleType = projectItem.getSettleType();
		if (null == settleType) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目条款未设置客户结算方式!");
		}
		// 订金 货款 回款的总金额
		BigDecimal countAmount = BigDecimal.ZERO;
		BigDecimal preDepositAmount = BigDecimal.ZERO;
		if (settleType.equals(BaseConsts.TWO)) { // 2-款到放货
			/**
			 * String selectedReceiptDate =
			 * billDeliveryAuditReqDto.getReceiptDate(); if
			 * (StringUtils.isBlank(selectedReceiptDate)) { throw new
			 * BaseException(ExcMsgEnum.ERROR_GENERAL, "请选择到账日期!"); }
			 **/
			if (!CollectionUtils.isEmpty(billDeliveryAuditReqDto.getVerificationAdvanceList())) {
				List<VerificationAdvanceReqDto> verificationAdvanceReqDtoList = billDeliveryAuditReqDto
						.getVerificationAdvanceList();

				List<VerificationAdvanceReqDto> returnedList = Lists.newArrayList();
				List<VerificationAdvanceReqDto> preDepositList = Lists.newArrayList();
				List<VerificationAdvanceReqDto> preGoodsList = Lists.newArrayList();

				BigDecimal returnedAmount = BigDecimal.ZERO;
				for (VerificationAdvanceReqDto verificationAdvanceReqDto : verificationAdvanceReqDtoList) {
					if (verificationAdvanceReqDto.getReceiptType().equals(BaseConsts.ONE)) {
						returnedList.add(verificationAdvanceReqDto);
						returnedAmount = DecimalUtil.add(returnedAmount,
								verificationAdvanceReqDto.getAvailableAmount());
					}
					if (verificationAdvanceReqDto.getReceiptType().equals(BaseConsts.TWO)) {
						preDepositList.add(verificationAdvanceReqDto);
						preDepositAmount = DecimalUtil.add(preDepositAmount,
								verificationAdvanceReqDto.getAvailableAmount());
					}
					if (verificationAdvanceReqDto.getReceiptType().equals(BaseConsts.THREE)) {
						preGoodsList.add(verificationAdvanceReqDto);
						returnedAmount = DecimalUtil.add(returnedAmount,
								verificationAdvanceReqDto.getAvailableAmount());
					}
				}
				countAmount = DecimalUtil.add(returnedAmount, preDepositAmount);
				// // 通过金额控制能否通过审核（到账金额-预收定金 >= 资金占用金额）
				// if (DecimalUtil.lt(returnedAmount,
				// billDelivery.getPayAmount())) {
				// throw new BaseException(ExcMsgEnum.ERROR_GENERAL,
				// "回款金额不能小于资金占用金额!");
				// }

				if (null == receiptDate) {
					if (!CollectionUtils.isEmpty(returnedList)) {
						receiptDate = returnedList.get(0).getReceiptDate();
					}
				}
				if (null == receiptDate) {
					if (!CollectionUtils.isEmpty(preGoodsList)) {
						receiptDate = preGoodsList.get(0).getReceiptDate();
					}
				}
				if (null == receiptDate) {
					if (!CollectionUtils.isEmpty(preDepositList)) {
						receiptDate = DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, billDelivery.getReturnTime());
					}
				}
				if (null == receiptDate) {
					throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "获取回款日期失败!");
				}

				returnTime = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD, receiptDate);
				billDeliveryService.reCalcPrice(billDelivery.getId(), returnTime); // 重新计算销售单价和销售金额
				BillDelivery billDelivery2 = billDeliveryService.queryEntityById(billDelivery.getId());
				// 匹配水单
				BigDecimal requiredSendAmount = billDelivery2.getRequiredSendAmount();
				if (null != requiredSendAmount && requiredSendAmount.compareTo(BigDecimal.ZERO) > 0) {
					BigDecimal remainAmount = requiredSendAmount;
					remainAmount = subtractAmount(remainAmount, billDelivery2, preDepositList); // 预收定金
					remainAmount = subtractAmount(remainAmount, billDelivery2, returnedList); // 回款
					remainAmount = subtractAmount(remainAmount, billDelivery2, preGoodsList); // 预收货款
				}
			} else {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请勾选水单明细!");
			}
			BigDecimal ductionAmount = BigDecimal.ZERO;
			// 预收货款抵扣水单的业务
			if (!CollectionUtils.isEmpty(billDeliveryAuditReqDto.getAdvanceMentList())) {
				ductionAmount = this.createBankRecAdvanceMent(billDeliveryAuditReqDto.getAdvanceMentList(),
						billDelivery);
			}
			countAmount = DecimalUtil.add(countAmount, ductionAmount);
			if (DecimalUtil.gt(countAmount, billDelivery.getRequiredSendAmount()) || DecimalUtil
					.lt(DecimalUtil.subtract(countAmount, preDepositAmount), billDelivery.getPayAmount())) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "回款金额不能小于资金占用金额或者是大于等于总金额!");
			}
			billDeliveryAuditReqDto.setReturnTime(returnTime);
		}
		return billDeliveryAuditReqDto;
	}

	/**
	 * 新增水单预收货款的核心预收的关系
	 * 
	 * @param mentReqDtos
	 */
	private BigDecimal createBankRecAdvanceMent(List<BankReceiptAdvanceMentReqDto> mentReqDtos,
			BillDelivery billDelivery) {
		List<BankReceiptAdvanceMentReqDto> preGoodsList = Lists.newArrayList();
		BigDecimal returnedAmount = BigDecimal.ZERO;
		for (BankReceiptAdvanceMentReqDto bankReceiptAdvanceMentReqDto : mentReqDtos) {
			preGoodsList.add(bankReceiptAdvanceMentReqDto);
			returnedAmount = DecimalUtil.add(returnedAmount, bankReceiptAdvanceMentReqDto.getAvailableAmount());
		}
		BillDelivery billDelivery2 = billDeliveryService.queryEntityById(billDelivery.getId());
		// 匹配水单
		BigDecimal requiredSendAmount = billDelivery2.getRequiredSendAmount();
		if (null != requiredSendAmount && requiredSendAmount.compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal remainAmount = requiredSendAmount;
			if (!CollectionUtils.isEmpty(preGoodsList)) {
				BigDecimal amount = BigDecimal.ZERO;
				for (BankReceiptAdvanceMentReqDto bankReceiptAdvanceMentReqDto : preGoodsList) {
					if (DecimalUtil.ge(bankReceiptAdvanceMentReqDto.getAvailableAmount(), remainAmount)) {
						amount = remainAmount;
					} else {
						amount = bankReceiptAdvanceMentReqDto.getAvailableAmount();
					}
					VerificationAdvance verificationAdvance = new VerificationAdvance();
					verificationAdvance.setId(null);
					verificationAdvance.setBillDeliveryId(billDelivery.getId());
					verificationAdvance.setReceiptId(bankReceiptAdvanceMentReqDto.getReceiptId()); // 水单ID
					verificationAdvance.setAmount(amount);
					verificationAdvanceService.addVerificationAdvance(verificationAdvance);
					verificationAdvanceService.dealReceiptAdvance(billDelivery, verificationAdvance.getId());
					remainAmount = DecimalUtil.subtract(remainAmount, amount);
					if (DecimalUtil.eq(remainAmount, BigDecimal.ZERO)) {
						break;
					}
				}
			}
		}
		return returnedAmount;
	}

	/**
	 * 匹配水单
	 *
	 * @param remainAmount
	 * @param billDelivery
	 * @param verificationAdvanceReqDtoList
	 */
	private BigDecimal subtractAmount(BigDecimal remainAmount, BillDelivery billDelivery,
			List<VerificationAdvanceReqDto> verificationAdvanceReqDtoList) {
		if (remainAmount.compareTo(BigDecimal.ZERO) > 0) {
			if (!CollectionUtils.isEmpty(verificationAdvanceReqDtoList)) {
				BigDecimal amount = BigDecimal.ZERO;
				for (VerificationAdvanceReqDto verificationAdvanceReqDto : verificationAdvanceReqDtoList) {
					if (DecimalUtil.ge(verificationAdvanceReqDto.getAvailableAmount(), remainAmount)) {
						amount = remainAmount;
					} else {
						amount = verificationAdvanceReqDto.getAvailableAmount();
					}
					VerificationAdvance verificationAdvance = new VerificationAdvance();
					verificationAdvance.setId(null);
					verificationAdvance.setBillDeliveryId(billDelivery.getId());
					verificationAdvance.setReceiptId(verificationAdvanceReqDto.getReceiptId()); // 水单ID
					verificationAdvance.setAmount(amount);
					verificationAdvanceService.addVerificationAdvance(verificationAdvance);
					verificationAdvanceService.dealReceiptAdvance(billDelivery, verificationAdvance.getId());

					remainAmount = DecimalUtil.subtract(remainAmount, amount);
					if (DecimalUtil.eq(remainAmount, BigDecimal.ZERO)) {
						break;
					}
				}
			}
		}
		return remainAmount;
	}

	/**
	 * 根据水单日期获取服务费
	 *
	 * @param billDelivery
	 * @return
	 * @throws Exception
	 */
	public BillDeliveryResDto queryServiceAmount(BillDelivery billDelivery) throws Exception {
		BigDecimal requiredSendAmount = BigDecimal.ZERO; // 销售金额
		BigDecimal serviceAmount = BigDecimal.ZERO; // 服务金额
		List<BillDeliveryDtl> dillDeliveryDtlList = billDeliveryDtlDao
				.queryResultsByBillDeliveryId(billDelivery.getId());
		for (BillDeliveryDtl billDeliveryDtl : dillDeliveryDtlList) {
			BigDecimal profitPrice = projectItemService.getProfitPriceByStl(billDeliveryDtl.getStlId(),
					billDelivery.getReturnTime());
			BigDecimal profitAmount = DecimalUtil.multiply(null == profitPrice ? BigDecimal.ZERO : profitPrice,
					billDeliveryDtl.getRequiredSendNum());
			serviceAmount = DecimalUtil.add(serviceAmount, profitAmount);
			BigDecimal salePrice = projectItemService.getSalePrice(billDeliveryDtl.getStlId(),
					billDelivery.getReturnTime());
			requiredSendAmount = DecimalUtil.add(requiredSendAmount, DecimalUtil
					.multiply(null == salePrice ? BigDecimal.ZERO : salePrice, billDeliveryDtl.getRequiredSendNum()));
		}
		BillDeliveryResDto billDeliveryResDto = new BillDeliveryResDto();
		billDeliveryResDto.setServiceAmount(DecimalUtil.formatScale2(serviceAmount));
		billDeliveryResDto.setRequiredSendAmount(DecimalUtil.formatScale2(requiredSendAmount));
		return billDeliveryResDto;
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
		mail1.setColumnOne("销售单编号");
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
		mail5.setColumnOne("销售金额");
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
		String content = msgContentService.convertMailOneContent(auditTypeStr + "销售单" + typeStr, templateOnes, null);
		sendWarnMail(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【销售单】需要审核", content);
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
		case BaseConsts.ONE:
			auditTypeStr = "提交";
			break;
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

		content = content + "销售单编号:" + billDeliveryDto.getBillNo() + "\n";
		content = content + "项目:" + billDeliveryDto.getProjectName() + "\n";
		content = content + "客户:" + billDeliveryDto.getCustomerName() + "\n";
		content = content + "销售金额:" + DecimalUtil.toAmountString(billDeliveryDto.getRequiredSendAmount())
				+ billDeliveryDto.getCurrencyTypeName() + "\n";
		if (!auditTypeStr.equals("")) {
			content = content + auditTypeStr + "信息:" + "单据由" + fromUser.getChineseName() + "于"
					+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + auditTypeStr + "给"
					+ toUser.getChineseName() + "审核\n";
		}
		content = content + "日期:" + DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, new Date()) + "\n";
		content = content + "该信息为SCFS系统自动发送。如有疑问，请联系系统管理员。";

		sendWarnRtx(toUser.getId(), "SCFS系统提醒您,有新的" + auditTypeStr + "【销售单】需要审核", content);
	}

	public static void main(String[] args) throws Exception {
		Date date1 = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, "2017-01-11 23:00:34");
		Date date2 = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD_HH_MM_SS, "2017-01-10 20:00:34");

		List<Date> list = Lists.newArrayList();
		list.add(date1);
		list.add(date2);
		Collections.sort(list, new Comparator<Date>() {
			// 升序排序
			public int compare(Date o1, Date o2) {
				return o1.compareTo(o2);
			}
		});
		for (Date a : list) {
			System.out.println(a);
		}
	}

}
