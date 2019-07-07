package com.scfs.service.project;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.logistics.StlDao;
import com.scfs.dao.project.ProjectItemDao;
import com.scfs.dao.project.ProjectItemSegmentDao;
import com.scfs.dao.project.ProjectSubjectDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.base.entity.AuditFlow;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.logistics.entity.Stl;
import com.scfs.domain.project.dto.req.ProjectItemSearchReqDto;
import com.scfs.domain.project.dto.req.ProjectItemSegmentReqDto;
import com.scfs.domain.project.dto.req.ProjectSubjectSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectItemResDto;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectItemAuditModel;
import com.scfs.domain.project.entity.ProjectItemFileAttach;
import com.scfs.domain.project.entity.ProjectItemSegment;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.ProjectItemAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: ProjectItemService.java
 *  Description:
 *  Date,					Who,				
 *  2016年10月18日			Administrator
 *
 * </pre>
 */
@Service
public class ProjectItemService {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectItemService.class);

	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private ProjectItemDao projectItemDao;
	@Autowired
	private ProjectItemAuditService projectItemAuditService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private BaseProjectDao baseProjectDao;
	@Autowired
	private ProjectPoolService projectPoolService;
	@Autowired
	private FileUploadService fileUploadService;
	@Autowired
	private ProjectSubjectDao projectSubjectDao;
	@Autowired
	private StlDao stlDao;
	@Autowired
	private ProjectItemSegmentDao projectItemSegmentDao;
	@Autowired
	private AuditFlowService auditFlowService;

	public Result<Integer> createProjectItem(ProjectItem projectItem) {
		Result<Integer> result = new Result<Integer>();
		String orderNo = sequenceService.getNumDateByBusName(BaseConsts.PRE_PROJITEM_NO, SeqConsts.PROJECTITEM_NO,
				BaseConsts.INT_13);
		projectItem.setItemNo(orderNo);
		projectItem.setStatus(BaseConsts.ONE);
		projectItem.setCreator(ServiceSupport.getUser().getChineseName());
		projectItem.setCreateAt(new Date());
		BaseProject project = baseProjectDao.queryEntityById(projectItem.getProjectId());
		projectItem.setBusinessUnitId(project.getBusinessUnitId());
		projectItem.setAmountCurrency(project.getAmountUnit());
		if (null == projectItem.getOperateFeeRate()) {
			projectItem.setOperateFeeRate(BigDecimal.ZERO);
		}
		Integer count = projectItemDao.insert(projectItem);
		if (projectItem.getIsFundAccount().equals(BaseConsts.ONE) && null != projectItem.getPaypalCalcType()
				&& projectItem.getPaypalCalcType().equals(BaseConsts.TWO)) { // 2-分段
			if (!CollectionUtils.isEmpty(projectItem.getProjectItemSegmentList())) {
				Integer fundAccountPeriod = 0;
				for (ProjectItemSegment projectItemSegment : projectItem.getProjectItemSegmentList()) {
					projectItemSegment.setProjectItemId(projectItem.getId());
					projectItemSegment.setCreatorId(ServiceSupport.getUser().getId());
					projectItemSegment.setCreator(ServiceSupport.getUser().getChineseName());
					projectItemSegmentDao.insert(projectItemSegment);
					if (projectItemSegment.getSegmentDay() > fundAccountPeriod) {
						fundAccountPeriod = projectItemSegment.getSegmentDay();
					}
				}
				projectItem.setFundAccountPeriod(fundAccountPeriod);
				projectItemDao.updateById(projectItem);
			}
		}
		if (count <= 0) {
			throw new BaseException(ExcMsgEnum.PROJECT_ITEM_EXCEPTION);
		} else {
			result.setItems(projectItem.getId());
		}
		return result;
	}

	public void updateProjectItemById(ProjectItem projectItem) {
		if (projectItem.getIsAgencyExport() == 2) {
			projectItem.setAgencyExportRate(BigDecimal.ZERO);
		}
		if (projectItem.getIsagencyimport() == 2) {
			projectItem.setAgencyimportrate(BigDecimal.ZERO);
		}
		if (projectItem.getAccountRateType() == 1) {
			projectItem.setAccountRate(BigDecimal.ZERO);
		}
		if (projectItem.getIsOperateAccount() == 1) {
			projectItem.setOperateFeeRate(BigDecimal.ZERO);
		}
		if (projectItem.getFixedpoints() != null && projectItem.getFixedpoints() == 2) {
			projectItem.setSpreadfixedpoints(BigDecimal.ZERO);
		}
		int result = projectItemDao.updateById(projectItem);
		if (projectItem.getIsFundAccount().equals(BaseConsts.ONE) && null != projectItem.getPaypalCalcType()
				&& projectItem.getPaypalCalcType().equals(BaseConsts.TWO)) { // 2-分段
			if (!CollectionUtils.isEmpty(projectItem.getProjectItemSegmentList())) {
				List<Integer> ids = Lists.newArrayList();
				Integer fundAccountPeriod = 0;
				for (ProjectItemSegment projectItemSegment : projectItem.getProjectItemSegmentList()) {
					if (projectItemSegment.getId() == null) {
						projectItemSegment.setProjectItemId(projectItem.getId());
						projectItemSegment.setCreatorId(ServiceSupport.getUser().getId());
						projectItemSegment.setCreator(ServiceSupport.getUser().getChineseName());
						projectItemSegmentDao.insert(projectItemSegment);
						ids.add(projectItemSegment.getId());
					} else {
						projectItemSegmentDao.updateById(projectItemSegment);
						ids.add(projectItemSegment.getId());
					}
					if (projectItemSegment.getSegmentDay() > fundAccountPeriod) {
						fundAccountPeriod = projectItemSegment.getSegmentDay();
					}
				}
				projectItem.setFundAccountPeriod(fundAccountPeriod);
				projectItemDao.updateById(projectItem);
				if (!CollectionUtils.isEmpty(ids)) {
					ProjectItemSegmentReqDto projectItemSegmentReqDto = new ProjectItemSegmentReqDto();
					projectItemSegmentReqDto.setProjectItemId(projectItem.getId());
					projectItemSegmentReqDto.setIds(ids);
					List<ProjectItemSegment> deleteItems = projectItemSegmentDao
							.queryNeedToDeleteResults(projectItemSegmentReqDto);
					if (!CollectionUtils.isEmpty(deleteItems)) {
						for (ProjectItemSegment projectItemSegment : deleteItems) {
							projectItemSegment.setIsDelete(BaseConsts.ONE);
							projectItemSegment.setDeleter(ServiceSupport.getUser().getChineseName());
							projectItemSegment.setDeleteAt(new Date());
							projectItemSegmentDao.updateById(projectItemSegment);
						}
					}
				}
			}
		} else {
			ProjectItemSegmentReqDto projectItemSegmentReqDto = new ProjectItemSegmentReqDto();
			projectItemSegmentReqDto.setProjectItemId(projectItem.getId());
			projectItemSegmentReqDto.setIsDelete(BaseConsts.ZERO);
			List<ProjectItemSegment> projectItemSegmentList = projectItemSegmentDao
					.queryResultsByCon(projectItemSegmentReqDto);
			if (!CollectionUtils.isEmpty(projectItemSegmentList)) {
				for (ProjectItemSegment projectItemSegment : projectItemSegmentList) {
					projectItemSegment.setIsDelete(BaseConsts.ONE);
					projectItemSegment.setDeleter(ServiceSupport.getUser().getChineseName());
					projectItemSegment.setDeleteAt(new Date());
					projectItemSegmentDao.updateById(projectItemSegment);
				}
			}
		}
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PROJECT_ITEM_UPDATE_EXCEPTION);
		}
	}

	public void updateStatusById(ProjectItem projectItem) {

		int result = projectItemDao.updateStatusById(projectItem);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PROJECT_ITEM_UPDATE_EXCEPTION);
		}
	}

	public void deleteProjectItemById(ProjectItem projectItem) {
		ProjectItem vo = projectItemDao.queryEntityById(projectItem.getId());
		vo.setDeleteAt(new Date());
		vo.setIsDelete(BaseConsts.ONE);
		int result = projectItemDao.updateById(vo);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PROJECT_ITEM_DELETE_EXCEPTION);
		}
	}

	public BaseResult submitProjectItemById(ProjectItem projectItem) {
		BaseResult result = new BaseResult();
		ProjectItem vo = projectItemDao.queryEntityById(projectItem.getId());
		if (vo.getStatus() == BaseConsts.TWO) {
			result.setMsg("该条款已提交，不能再次提交！");
			result.setSuccess(false);
		} else {
			AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.SEVEN, null);
			if (null == startAuditNode) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
			}
			projectItem.setStatus(startAuditNode.getAuditNodeState()); // 待业务审核
			int count = projectItemDao.updateStatusById(projectItem);
			projectItemAuditService.startAudit(vo, startAuditNode);
			if (count <= 0) {
				throw new BaseException(ExcMsgEnum.PROJECT_ITEM_SUBMIT_EXCEPTION);
			}
		}
		return result;
	}

	public ProjectItem detailProjectItemById(ProjectItem projectItem) {
		ProjectItem vo = projectItemDao.queryEntityById(projectItem.getId());
		vo.setProjectName(cacheService.getProjectNameById(vo.getProjectId()));
		vo.setBusinessUnitName(cacheService.getSubjectNameByIdAndKey(vo.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		vo.setIsFundAccountName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.SETTLE_TYPE, vo.getIsFundAccount() + ""));
		vo.setIsOperateAccountValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CALCULATE_OPE_COST, vo.getIsOperateAccount() + ""));
		vo.setProjectchecktypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_TYPE, vo.getProjectchecktype() + ""));
		vo.setClientchecktypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_TYPE, vo.getClientchecktype() + ""));
		vo.setSupplierchecktypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_TYPE, vo.getSupplierchecktype() + ""));
		vo.setReceiveCurrencyValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, vo.getReceiveCurrency() + ""));
		vo.setPayCurrencyValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, vo.getPayCurrency() + ""));
		vo.setAmountCurrencyValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, vo.getAmountCurrency() + ""));
		vo.setPaypalAmountCurrencyValue(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				vo.getPaypalAmountCurrency() + ""));
		vo.setIsagencyimportValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getIsagencyimport() + ""));
		vo.setIsAgencyExportValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getIsAgencyExport() + ""));
		vo.setAccountRateTypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.SETTLE_RATE_TYPE, vo.getAccountRateType() + ""));
		vo.setFixedpointsValue(ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getFixedpoints() + ""));
		vo.setAccountMethodValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_METHOD, vo.getAccountMethod() + ""));
		vo.setSignStandardValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.SIGN_STANDARD, vo.getSignStandard() + ""));
		vo.setTotalAmountValue(DecimalUtil.toAmountString(vo.getTotalAmount()));
		vo.setDayRulesValue(ServiceSupport.getValueByBizCode(BizCodeConsts.DAY_RULES, vo.getDayRules() + ""));
		vo.setSettleTypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_ITEM_SETTLE_TYPE, vo.getSettleType() + ""));
		if (vo.getBankId() != null)
			vo.setBankName(ServiceSupport.getValueByBizCode(BizCodeConsts.BANK_NAME, vo.getBankId() + ""));
		if (vo.getCustomerId() != null)
			vo.setCustomerName(cacheService.getSubjectNameByIdAndKey(vo.getCustomerId(), CacheKeyConsts.PROJECT_CS));
		if (null != vo.getPaypalCalcType()) {
			vo.setPaypalCalcTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAYPAL_CALC_TYPE, vo.getPaypalCalcType() + ""));
		}
		if (null != vo.getPaypalCalcType() && vo.getPaypalCalcType().equals(BaseConsts.TWO)) { // 2-分段
			ProjectItemSegmentReqDto projectItemSegmentReqDto = new ProjectItemSegmentReqDto();
			projectItemSegmentReqDto.setProjectItemId(vo.getId());
			projectItemSegmentReqDto.setIsDelete(BaseConsts.ZERO);
			List<ProjectItemSegment> projectItemSegmentList = projectItemSegmentDao
					.queryResultsByCon(projectItemSegmentReqDto);
			vo.setProjectItemSegmentList(projectItemSegmentList);
		}
		if (null != vo.getClientCheckWeek()) {
			vo.setClientCheckWeekName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_WEEK, vo.getClientCheckWeek() + ""));
		}
		if (null != vo.getSupplierCheckWeek()) {
			vo.setSupplierCheckWeekName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_WEEK, vo.getSupplierCheckWeek() + ""));
		}
		if (null != vo.getProjectCheckWeek()) {
			vo.setProjectCheckWeekName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_WEEK, vo.getProjectCheckWeek() + ""));
		}
		if (null != vo.getPayAuditType()) {
			AuditFlow auditFlow = auditFlowService.queryAuditFlowByNo(vo.getPayAuditType());
			if (null != auditFlow) {
				vo.setPayAuditTypeName(auditFlow.getAuditFlowName());
			}
		}
		return vo;
	}

	public ProjectItemAuditModel detailProjectById(ProjectItem projectItem) {
		ProjectItemAuditModel projectItemAuditModel = new ProjectItemAuditModel();
		StringBuilder supplierName = new StringBuilder(), customerName = new StringBuilder();
		ProjectItem vo = projectItemDao.queryEntityById(projectItem.getId());
		ProjectSubjectSearchReqDto projectSubjectSearchReqDto = new ProjectSubjectSearchReqDto();
		projectSubjectSearchReqDto.setProjectId(vo.getProjectId());
		projectSubjectSearchReqDto.setSubjectType(BaseConsts.FOUR);
		projectSubjectSearchReqDto.setStatus(BaseConsts.ONE);
		List<ProjectSubject> projectSubjectList = projectSubjectDao.queryResultsByCon(projectSubjectSearchReqDto);
		for (int i = 0; i < projectSubjectList.size(); i++) {
			String name = cacheService.getSupplierById(projectSubjectList.get(i).getSubjectId()).getChineseName();
			supplierName.append(name);
			if (i < projectSubjectList.size() - 1) {
				supplierName.append(",");
			}
		}
		projectSubjectSearchReqDto.setSubjectType(BaseConsts.EIGHT);
		vo.setProjectName(cacheService.getProjectNameById(vo.getProjectId()));
		List<ProjectSubject> projectList = projectSubjectDao.queryResultsByCon(projectSubjectSearchReqDto);
		for (int i = 0; i < projectList.size(); i++) {
			String name = cacheService.getCustomerById(projectList.get(i).getSubjectId()).getChineseName();
			customerName.append(name);
			if (i < projectList.size() - 1) {
				customerName.append(",");
			}
		}
		vo.setSupplierName(supplierName.toString());
		vo.setCustomerName(customerName.toString());
		vo.setBusinessUnitName(cacheService.getSubjectNameByIdAndKey(vo.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		vo.setIsFundAccountName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.SETTLE_TYPE, vo.getIsFundAccount() + ""));
		vo.setIsOperateAccountValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CALCULATE_OPE_COST, vo.getIsOperateAccount() + ""));
		vo.setProjectchecktypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_TYPE, vo.getProjectchecktype() + ""));
		vo.setClientchecktypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_TYPE, vo.getClientchecktype() + ""));
		vo.setSupplierchecktypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.CHECK_TYPE, vo.getSupplierchecktype() + ""));
		vo.setReceiveCurrencyValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, vo.getReceiveCurrency() + ""));
		vo.setPayCurrencyValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, vo.getPayCurrency() + ""));
		vo.setAmountCurrencyValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, vo.getAmountCurrency() + ""));
		vo.setPaypalAmountCurrencyValue(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				vo.getPaypalAmountCurrency() + ""));
		vo.setIsagencyimportValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getIsagencyimport() + ""));
		vo.setIsAgencyExportValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getIsAgencyExport() + ""));
		vo.setAccountRateTypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.SETTLE_RATE_TYPE, vo.getAccountRateType() + ""));
		vo.setFixedpointsValue(ServiceSupport.getValueByBizCode(BizCodeConsts.PROMPT_STATUS, vo.getFixedpoints() + ""));
		vo.setAccountMethodValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.ACCOUNT_METHOD, vo.getAccountMethod() + ""));
		vo.setTotalAmountValue(DecimalUtil.toAmountString(vo.getTotalAmount()));
		vo.setSignStandardValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.SIGN_STANDARD, vo.getSignStandard() + ""));
		vo.setSingleAmountValue(DecimalUtil.toAmountString(vo.getSingleAmount()));
		vo.setDayRulesValue(ServiceSupport.getValueByBizCode(BizCodeConsts.DAY_RULES, vo.getDayRules() + ""));
		vo.setSettleTypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_ITEM_SETTLE_TYPE, vo.getSettleType() + ""));
		if (vo.getBankId() != null) {
			vo.setBankName(ServiceSupport.getValueByBizCode(BizCodeConsts.BANK_NAME, vo.getBankId() + ""));
		}
		if (vo.getCustomerId() != null) {
			vo.setPrimaryCustomerName(
					cacheService.getSubjectNameByIdAndKey(vo.getCustomerId(), CacheKeyConsts.PROJECT_CS));
		}
		if (null != vo.getPaypalCalcType()) {
			vo.setPaypalCalcTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.PAYPAL_CALC_TYPE, vo.getPaypalCalcType() + ""));
		}
		if (null != vo.getPaypalCalcType() && vo.getPaypalCalcType().equals(BaseConsts.TWO)) { // 2-分段
			ProjectItemSegmentReqDto projectItemSegmentReqDto = new ProjectItemSegmentReqDto();
			projectItemSegmentReqDto.setProjectItemId(vo.getId());
			projectItemSegmentReqDto.setIsDelete(BaseConsts.ZERO);
			List<ProjectItemSegment> projectItemSegmentList = projectItemSegmentDao
					.queryResultsByCon(projectItemSegmentReqDto);
			vo.setProjectItemSegmentList(projectItemSegmentList);
		}

		projectItemAuditModel.setProjectItem(vo);
		FileAttachSearchReqDto fileAttReqDto = new FileAttachSearchReqDto();
		fileAttReqDto.setBusType(BaseConsts.ONE);
		fileAttReqDto.setBusId(vo.getId());
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<ProjectItemFileAttach> list = convertToResDto(fielAttach);
		projectItemAuditModel.setProjectItemFileAttachList(list);
		return projectItemAuditModel;
	}

	public ProjectItem editProjectItemById(ProjectItem projectItem) {
		ProjectItem vo = projectItemDao.queryEntityById(projectItem.getId());
		if (null != vo.getPaypalCalcType() && vo.getPaypalCalcType().equals(BaseConsts.TWO)) { // 2-分段
			ProjectItemSegmentReqDto projectItemSegmentReqDto = new ProjectItemSegmentReqDto();
			projectItemSegmentReqDto.setProjectItemId(vo.getId());
			projectItemSegmentReqDto.setIsDelete(BaseConsts.ZERO);
			List<ProjectItemSegment> projectItemSegmentList = projectItemSegmentDao
					.queryResultsByCon(projectItemSegmentReqDto);
			vo.setProjectItemSegmentList(projectItemSegmentList);
		}
		vo.setProjectName(cacheService.getProjectNameById(vo.getProjectId()));
		return vo;
	}

	public void lockProjectItemById(ProjectItem projectItem) {
		projectItem.setStatus(BaseConsts.SEVEN); // 7-锁定
		int result = projectItemDao.updateStatusById(projectItem);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PROJECT_ITEM_LOCK_EXCEPTION);
		}
	}

	public void unlockProjectItemById(ProjectItem projectItem) {
		projectItem.setStatus(BaseConsts.TWO);
		int result = projectItemDao.updateStatusById(projectItem);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.PROJECT_ITEM_UNLOCK_EXCEPTION);
		}
	}

	public PageResult<ProjectItemResDto> queryProjectItemResultsByCon(ProjectItemSearchReqDto queryProjectItemReqDto) {
		PageResult<ProjectItemResDto> pageResult = new PageResult<ProjectItemResDto>();
		int offSet = PageUtil.getOffSet(queryProjectItemReqDto.getPage(), queryProjectItemReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, queryProjectItemReqDto.getPer_page());
		queryProjectItemReqDto.setUserId(ServiceSupport.getUser().getId());
		List<ProjectItem> result = projectItemDao.queryProjectItemResultsByCon(queryProjectItemReqDto, rowBounds);
		List<ProjectItemResDto> vos = new ArrayList<ProjectItemResDto>();
		if (queryProjectItemReqDto.getNeedSum() != null && queryProjectItemReqDto.getNeedSum() == BaseConsts.ONE) {// 是否需要合计，1表示是，2表示否
			List<ProjectItem> proSumList = projectItemDao.sumPoTitle(queryProjectItemReqDto);
			if (CollectionUtils.isNotEmpty(proSumList)) {
				BigDecimal amountRmbTotal = BigDecimal.ZERO;
				for (ProjectItem pro : proSumList) {
					BigDecimal rmbAmount = ServiceSupport.amountNewToRMB(pro.getTotalAmount(), pro.getAmountCurrency(),
							null);
					amountRmbTotal = DecimalUtil.add(amountRmbTotal, rmbAmount);
				}
				pageResult.setTotalAmount(amountRmbTotal);
			}
		}
		if (CollectionUtils.isNotEmpty(result)) {
			for (ProjectItem rs : result) {
				ProjectItemResDto recFeeQueryResDto = convertToFeeQueryResDto(rs);
				List<CodeValue> operList = getOperList(rs.getStatus());
				recFeeQueryResDto.setOpertaList(operList);
				vos.add(recFeeQueryResDto);
			}
		}
		pageResult.setItems(vos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), queryProjectItemReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(queryProjectItemReqDto.getPage());
		pageResult.setPer_page(queryProjectItemReqDto.getPer_page());

		return pageResult;
	}

	private ProjectItemResDto convertToFeeQueryResDto(ProjectItem vo) {
		ProjectItemResDto dto = new ProjectItemResDto();
		BaseProject project = cacheService.getProjectById(vo.getProjectId());
		dto.setId(vo.getId());
		dto.setItemNo(vo.getItemNo());

		dto.setBusinessUnitName(
				cacheService.getSubjectNameByIdAndKey(vo.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		dto.setProjectName(cacheService.getProjectNameById(vo.getProjectId()));
		dto.setDateStr(DateFormatUtils.format(vo.getStartDate(), DateFormatUtils.YYYY_MM_DD) + "到"
				+ DateFormatUtils.format(vo.getEndDate(), DateFormatUtils.YYYY_MM_DD));
		dto.setBizType(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE, project.getBizType() + ""));
		dto.setIsFundAccount(ServiceSupport.getValueByBizCode(BizCodeConsts.SETTLE_TYPE, vo.getIsFundAccount() + ""));
		dto.setAmount(DecimalUtil.toAmountString(vo.getTotalAmount())
				+ ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, vo.getAmountCurrency() + ""));
		dto.setStatus(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECTITEM_STATE, vo.getStatus() + ""));
		dto.setCreateAt(vo.getCreateAt());
		dto.setCreator(vo.getCreator());
		return dto;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState(Integer state) {
		List<String> opertaList = Lists.newArrayList();
		if (state == null) {
			return opertaList;
		}
		opertaList.add(OperateConsts.DETAIL);
		switch (state) {
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.INT_20:
			break;
		case BaseConsts.INT_30:
			break;
		case BaseConsts.INT_40:
			break;
		case BaseConsts.INT_80:
			break;
		case BaseConsts.FIVE: // 5-待完成
			break;
		case BaseConsts.SIX: // 6-已完成
			opertaList.add(OperateConsts.LOCK);
			opertaList.add(OperateConsts.COPY);
			break;
		case BaseConsts.SEVEN: // 7-已锁定
			opertaList.add(OperateConsts.COPY);
			break;
		}

		return opertaList;
	}

	/**
	 * 根据状态得到操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<String> getOperListByState() {
		List<String> opertaList = Lists.newArrayList();
		opertaList.add(OperateConsts.DOWNLOAD);
		opertaList.add(OperateConsts.DELETE);
		return opertaList;
	}

	/**
	 * 获取操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				ProjectItemResDto.Operate.operMap);
		return oprResult;
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param state
	 * @return
	 */
	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				ProjectItemFileAttach.Operate.operMap);
		return oprResult;
	}

	public PageResult<ProjectItemFileAttach> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<ProjectItemFileAttach> pageResult = new PageResult<ProjectItemFileAttach>();
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<ProjectItemFileAttach> list = convertToResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
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
			List<CodeValue> operList = getOperList();
			projectItemFileAttach.setOpertaList(operList);
			list.add(projectItemFileAttach);
		}
		return list;
	}

	/**
	 * 业务操作中,取条款信息
	 * 
	 * @param projectId
	 *            项目ID
	 * @return
	 */
	public ProjectItem getProjectItem(Integer projectId) {
		if (projectId == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,项目信息错误");
		}
		ProjectItemSearchReqDto reqDto = new ProjectItemSearchReqDto();
		reqDto.setProjectId(projectId);
		reqDto.setStatus(BaseConsts.SIX); // 6-已完成
		List<ProjectItem> ls = projectItemDao.queryProjectItemByProjectId(reqDto);

		if (ls.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,项目条款不唯一");
		}

		if (ls.size() == 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败,项目无条款");
		}

		return ls.get(0);
	}

	/**
	 * 下拉框选择项目获取条框
	 * 
	 * @param projectId
	 * @return
	 */
	public ProjectItem getProjectItemByProjectId(Integer projectId) {
		if (projectId == null) {
			return null;
		}
		ProjectItemSearchReqDto reqDto = new ProjectItemSearchReqDto();
		reqDto.setProjectId(projectId);
		reqDto.setStatus(BaseConsts.SIX); // 6-已完成
		List<ProjectItem> ls = projectItemDao.queryProjectItemByProjectId(reqDto);

		if (ls.size() > 1) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目条款不唯一");
		}
		if (ls.size() == 0) {
			return null;
		}
		return ls.get(0);
	}

	public Integer updateState(AuditNode auditNode, ProjectItem projectItem) {
		Integer state = null;
		if (null != auditNode) { // 中间审核节点
			state = auditNode.getAuditNodeState();
			ProjectItem projectItemReq = new ProjectItem();
			projectItemReq.setId(projectItem.getId());
			projectItemReq.setStatus(state);
			projectItemDao.updateStatusById(projectItemReq);
		} else { // 最后一个审核节点
			ProjectItemSearchReqDto projectReqDto = new ProjectItemSearchReqDto();
			projectReqDto.setProjectId(projectItem.getProjectId());
			projectReqDto.setStatus(BaseConsts.SIX);
			List<ProjectItem> pro = projectItemDao.queryProjectItemByProjectId(projectReqDto);
			if (CollectionUtils.isNotEmpty(pro)) {
				ProjectItem projectItemUpdate = new ProjectItem();
				projectItemUpdate.setId(pro.get(0).getId());
				projectItemUpdate.setStatus(BaseConsts.SEVEN);
				projectItemDao.updateStatusById(projectItemUpdate);
			}

			Date startDate = projectItem.getStartDate();
			Date currDate = DateFormatUtils.getCurrentDate();
			try {
				currDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD,
						DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, currDate));
			} catch (ParseException e) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "获取当前系统时间失败");
			}

			long diffTime = DateFormatUtils.diffDateTime(startDate, currDate);
			if (diffTime <= 0) { // 开始日期小于等于当前日期
				ProjectItemSearchReqDto projectItemSearchReqDto = new ProjectItemSearchReqDto();
				projectItemSearchReqDto.setProjectId(projectItem.getProjectId());
				projectItemSearchReqDto.setStatus(BaseConsts.SIX); // 查询项目下已完成的条款
				List<ProjectItem> projectItemList = projectItemDao
						.queryProjectItemResultsByCon(projectItemSearchReqDto);
				if (CollectionUtils.isNotEmpty(projectItemList)) {
					for (ProjectItem pItem : projectItemList) {
						ProjectItem projectItemReq = new ProjectItem();
						projectItemReq.setId(pItem.getId());
						projectItemReq.setStatus(BaseConsts.SEVEN); // 7-已锁定
						projectItemDao.updateStatusById(projectItemReq);
					}
				}
				ProjectItem projectItemReq = new ProjectItem();
				projectItemReq.setId(projectItem.getId());
				projectItemReq.setStatus(BaseConsts.SIX); // 6-已完成
				projectItemDao.updateStatusById(projectItemReq);
				// 更改融资池项目额度
				projectPoolService.updateProjectPoolInfo(projectItem.getProjectId(), true);
			} else if (diffTime > 0) { // 开始日期大于当前日期
				ProjectItem projectItemReq = new ProjectItem();
				projectItemReq.setId(projectItem.getId());
				projectItemReq.setStatus(BaseConsts.FIVE); // 5-待完成
				projectItemDao.updateStatusById(projectItemReq);
			}
		}
		return state;
	}

	public void updateProjectItemState4BusAudit(Integer projectItemId, AuditNode auditNode) {
		ProjectItem projectItem = projectItemDao.queryEntityById(projectItemId);
		if (null == projectItem) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款不存在");
		}
		if (projectItem.getStatus().equals(BaseConsts.INT_20)) {
			updateState(auditNode, projectItem);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款当前状态无法审核通过");
		}
	}

	public void updateProjectItemState4FinanceAudit(Integer projectItemId, AuditNode auditNode) {
		ProjectItem projectItem = projectItemDao.queryEntityById(projectItemId);
		if (null == projectItem) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款不存在");
		}
		if (projectItem.getStatus().equals(BaseConsts.INT_30)) {
			updateState(auditNode, projectItem);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款当前状态无法审核通过");
		}
	}

	public void updateProjectItemState4UnPassAudit(Integer projectItemId) {
		ProjectItem projectItem = projectItemDao.queryEntityById(projectItemId);
		if (null == projectItem) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款不存在");
		}
		projectItem.setId(projectItem.getId());
		projectItem.setStatus(BaseConsts.ONE); // 业务单据状态：待提交
		projectItemDao.updateStatusById(projectItem);
	}

	public void updateProjectItemState4RiskAudit(Integer projectItemId, AuditNode auditNode) {
		ProjectItem projectItem = projectItemDao.queryEntityById(projectItemId);
		if (null == projectItem) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款不存在");
		}
		if (projectItem.getStatus().equals(BaseConsts.INT_40)) {
			updateState(auditNode, projectItem);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款当前状态无法审核通过");
		}
	}

	public void updateProjectItemState4DeptManageAudit(Integer projectItemId, AuditNode auditNode) {
		ProjectItem projectItem = projectItemDao.queryEntityById(projectItemId);
		if (null == projectItem) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款不存在");
		}
		if (projectItem.getStatus().equals(BaseConsts.INT_80)) {
			updateState(auditNode, projectItem);
		} else {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "操作失败，项目条款当前状态无法审核通过");
		}
	}

	public void updateStatusBySchedule() {
		Date currDate = DateFormatUtils.getCurrentDate();
		try {
			currDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD,
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, currDate));
		} catch (ParseException e) {
			LOGGER.error("获取当前系统时间失败", e);
			currDate = null;
		}
		if (null != currDate) {
			ProjectItemSearchReqDto projectItemSearchReqDto = new ProjectItemSearchReqDto();
			projectItemSearchReqDto.setScheduleFlag(BaseConsts.ONE); // 查询项目下带待完成和已完成的条款
			List<ProjectItem> projectItemList = projectItemDao.queryProjectItemResultsByCon(projectItemSearchReqDto);
			if (CollectionUtils.isNotEmpty(projectItemList)) {
				for (ProjectItem projectItem : projectItemList) {
					if (projectItem.getStatus().equals(BaseConsts.FIVE)
							&& DateFormatUtils.diffDateTime(projectItem.getStartDate(), currDate) == 0) { // 待完成且开始日期为当前日期
						projectItem.setStatus(BaseConsts.SIX); // 6-已完成
						projectItemDao.updateStatusById(projectItem);
						// 更改融资池项目额度
						projectPoolService.updateProjectPoolInfo(projectItem.getProjectId(), true);
					} else if (projectItem.getStatus().equals(BaseConsts.SIX) && DateFormatUtils
							.diffDateTime(projectItem.getEndDate(), DateFormatUtils.beforeDay(currDate, 1)) == 0) {
						projectItem.setStatus(BaseConsts.SEVEN); // 7-已锁定
						projectItemDao.updateStatusById(projectItem);
					}
				}
			}
		}
	}

	/**
	 * 计算销售指导价
	 * 
	 * @param stlId
	 *            库存id
	 * @param date
	 *            销售日期
	 * @return
	 */
	public BigDecimal getSalePrice(Integer stlId, Date date) {
		Stl stl = stlDao.queryEntityById(stlId);
		if (stl == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, StlDao.class, stlId);
		}

		ProjectItem projectItem = getProjectItem(stl.getProjectId());
		BigDecimal profit = BigDecimal.ZERO;
		BigDecimal ftcPrice = BigDecimal.ZERO;
		Integer isFundAccount = projectItem.getIsFundAccount();

		if (isFundAccount.equals(BaseConsts.TWO)) { // 2-价差
			if (projectItem.getSpreadfixedpoints() != null) {
				ftcPrice = stl.getCostPrice().multiply(DecimalUtil.ONE.add(projectItem.getSpreadfixedpoints()))
						.setScale(8, BigDecimal.ROUND_HALF_UP);
			} else {
				ftcPrice = stl.getCostPrice();
			}
		} else if (isFundAccount.equals(BaseConsts.ONE)) { // 1-资金占用
			profit = getProfitPriceByStl(stlId, date);
			ftcPrice = DecimalUtil.add(stl.getCostPrice(), profit);
		} else {
			ftcPrice = stl.getCostPrice();
		}

		return ftcPrice;
	}

	public long getProfitDays(ProjectItem projectItem, Date payTime, Date outDate) {
		if (null == payTime) {
			return 0L;
		}
		long day = 0L;
		try {
			day = DateFormatUtils.diffDate(outDate, payTime);

			if (projectItem.getDayRules() != null && projectItem.getDayRules().compareTo(BaseConsts.TWO) == 0) {
				day = day + 1;
			}
		} catch (ParseException e) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "参数有误");
		}

		return day;
	}

	/**
	 * 获取资金占用天数，当存在最低消费天数，且天数差值小于最低消费天数时，取最低消费天数
	 * 
	 * @return
	 */
	public long getOccupyDays(Integer projectId, Date payTime, Date outDate) {
		ProjectItem projectItem = getProjectItem(projectId);
		return getOccupyDays(projectItem, payTime, outDate);
	}

	public long getOccupyDays(ProjectItem projectItem, Date payTime, Date outDate) {
		BaseProject baseProject = cacheService.getProjectById(projectItem.getProjectId());
		long days = getProfitDays(projectItem, payTime, outDate);
		long occupyDays = 0L;
		if (projectItem.getIsFundAccount().equals(BaseConsts.ONE) && null != projectItem.getPaypalCalcType()
				&& projectItem.getPaypalCalcType().equals(BaseConsts.TWO)) {
			occupyDays = days;
		} else {
			if (StringUtils.isEmpty(projectItem.getMinSaleDay())) {
				return days;
			}
			// 有最低消费天数
			if (days <= projectItem.getMinSaleDay()) {
				days = projectItem.getMinSaleDay();
			}
			occupyDays = days;
		}
		if (baseProject.getBizType().equals(BaseConsts.SIX)) { // 6-融通质押
			occupyDays = days + (long) (projectItem.getPayCycle() == null ? 0 : projectItem.getPayCycle());
		}
		return occupyDays;
	}

	/**
	 * @param projectId
	 *            项目ID
	 * @param payPrice
	 *            资金占用价格
	 * @param payTime
	 *            付款日期
	 * @param outDate
	 *            销售日期
	 * @return
	 */
	public BigDecimal getProfitPrice(Integer projectId, BigDecimal payPrice, Date payTime, Date outDate) {
		ProjectItem projectItem = getProjectItem(projectId);

		BigDecimal profit = BigDecimal.ZERO;
		if (projectItem.getIsFundAccount().equals(BaseConsts.TWO)) { // 2-价差
			return profit;
		}
		if (outDate == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "参数有误，请核查!");
		}
		if (projectItem.getIsFundAccount().equals(BaseConsts.ONE) && null != projectItem.getPaypalCalcType()
				&& projectItem.getPaypalCalcType().equals(BaseConsts.ONE)) {
			if (null == projectItem.getFundAccountPeriod()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "条款资金使用帐期不能为空!");
			}

			if (null == projectItem.getFundMonthRate()) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "条款资金日服务费率不能为空!");
			}
		}
		if (null == payPrice) {
			return BigDecimal.ZERO;
		}
		long day = getOccupyDays(projectItem, payTime, outDate); // 计算占用天数
		if (day < 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal fundMonthRate = getFundMonthRate(projectItem, day);
		if (projectItem.getIsFundAccount().equals(BaseConsts.ONE) && null != projectItem.getPaypalCalcType()
				&& projectItem.getPaypalCalcType().equals(BaseConsts.TWO)) {
			if (StringUtils.isEmpty(projectItem.getDayPenalRate())) { // 无违约费率
				profit = payPrice.multiply(fundMonthRate).setScale(8, BigDecimal.ROUND_HALF_UP);
			} else { // 有违约费率
				if (day <= projectItem.getFundAccountPeriod()) {
					profit = payPrice.multiply(fundMonthRate).setScale(8, BigDecimal.ROUND_HALF_UP);
				} else {
					long expireDay = day - (long) projectItem.getFundAccountPeriod();
					profit = DecimalUtil.add(payPrice.multiply(fundMonthRate),
							payPrice.multiply(DecimalUtil.format(expireDay)).multiply(projectItem.getDayPenalRate()));
				}
			}
		} else {
			if (StringUtils.isEmpty(projectItem.getDayPenalRate())) { // 无违约费率
				profit = payPrice.multiply(DecimalUtil.format(day)).multiply(fundMonthRate).setScale(8,
						BigDecimal.ROUND_HALF_UP);
			} else { // 有违约费率
				if (day <= projectItem.getFundAccountPeriod()) {
					profit = payPrice.multiply(DecimalUtil.format(day)).multiply(fundMonthRate).setScale(8,
							BigDecimal.ROUND_HALF_UP);
				} else {
					long expireDay = day - (long) projectItem.getFundAccountPeriod();
					profit = DecimalUtil.add(
							payPrice.multiply(DecimalUtil.format(projectItem.getFundAccountPeriod()))
									.multiply(fundMonthRate),
							payPrice.multiply(DecimalUtil.format(expireDay)).multiply(projectItem.getDayPenalRate()));
				}
			}
		}
		return profit;
	}

	public BigDecimal getFundMonthRate(ProjectItem projectItem, long day) {
		BigDecimal fundMonthRate = BigDecimal.ZERO;
		if (projectItem.getIsFundAccount().equals(BaseConsts.ONE) && null != projectItem.getPaypalCalcType()
				&& projectItem.getPaypalCalcType().equals(BaseConsts.TWO)) {
			if (day > projectItem.getFundAccountPeriod()) {
				ProjectItemSegmentReqDto projectItemSegmentReqDto = new ProjectItemSegmentReqDto();
				projectItemSegmentReqDto.setProjectItemId(projectItem.getId());
				ProjectItemSegment projectItemSegment = projectItemSegmentDao
						.queryMaxSegmentBySegmentDay(projectItemSegmentReqDto);
				fundMonthRate = projectItemSegment.getSegmentFundMonthRate();
			} else {
				ProjectItemSegmentReqDto projectItemSegmentReqDto = new ProjectItemSegmentReqDto();
				projectItemSegmentReqDto.setProjectItemId(projectItem.getId());
				projectItemSegmentReqDto.setSegmentDay(day);
				ProjectItemSegment projectItemSegment = projectItemSegmentDao
						.querySegmentBySegmentDay(projectItemSegmentReqDto);
				fundMonthRate = projectItemSegment.getSegmentFundMonthRate();
			}
		} else {
			fundMonthRate = projectItem.getFundMonthRate();
		}
		return fundMonthRate;
	}

	/**
	 * 计算利润价格
	 * 
	 * @param stlId
	 *            库存id
	 * @param date
	 *            销售日期
	 * @return
	 */
	public BigDecimal getProfitPriceByStl(Integer stlId, Date date) {
		Stl stl = stlDao.queryEntityById(stlId);
		if (stl == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, StlDao.class, stlId);
		}
		if (date == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "参数有误，请核查!");
		}
		return getProfitPrice(stl.getProjectId(), stl.getPayPrice(), stl.getPayTime(), date);
	}
}
