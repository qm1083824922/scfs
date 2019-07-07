package com.scfs.service.project;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.consts.SeqConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DateFormatUtils;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.project.ProjectPoolAdjustDao;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.entity.AuditNode;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.pay.dto.resq.PayOrderFileResDto;
import com.scfs.domain.project.dto.req.ProjectPoolAdjustSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectItemResDto;
import com.scfs.domain.project.dto.resp.ProjectPoolAdjustFileDto;
import com.scfs.domain.project.dto.resp.ProjectPoolAdjustResDto;
import com.scfs.domain.project.entity.ProjectPool;
import com.scfs.domain.project.entity.ProjectPoolAdjust;
import com.scfs.domain.result.PageResult;
import com.scfs.service.audit.ProjectPoolAdjustAuditService;
import com.scfs.service.base.auditFlow.AuditFlowService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 * 
 *  File: ProjectPoolAdjustService.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月22日				Administrator
 *
 * </pre>
 */

@Service
public class ProjectPoolAdjustService {
	private final static Logger Logger = LoggerFactory.getLogger(ProjectPoolAdjustService.class);
	@Autowired
	CacheService cacheService;
	@Autowired
	ProjectPoolAdjustDao projectPoolAdjustDao;
	@Autowired
	ProjectPoolService projectPoolService;
	@Autowired
	SequenceService sequenceService;
	@Autowired
	FileUploadService fileUploadService;
	@Autowired
	ProjectPoolAdjustAuditService projectPoolAdjustAuditService;
	@Autowired
	AuditFlowService auditFlowService;

	public Integer createProjectPoolAdjust(ProjectPoolAdjust projectPoolAdjust) {
		projectPoolAdjust.setCreateAt(new Date());
		projectPoolAdjust.setCreator(ServiceSupport.getUser().getChineseName());
		projectPoolAdjust.setCreateId(ServiceSupport.getUser().getId());
		ProjectPool projectPool = projectPoolService.queryProjectPoolByProjectId(projectPoolAdjust.getProjectId());
		if (projectPool == null) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "项目" + projectPoolAdjust.getProjectId() + "资金池有误");
		}
		projectPoolAdjust.setState(BaseConsts.ONE);
		projectPoolAdjust.setCurrencyType(projectPool.getCurrencyType());
		projectPoolAdjust.setAdjustNo(sequenceService.getNumDateByBusName(BaseConsts.PRE_PROJECT_POOL_ADJUST_NO,
				SeqConsts.S_PROJECT_POOL_ADJUST_NO, BaseConsts.INT_13));
		projectPoolAdjustDao.insert(projectPoolAdjust);
		return projectPoolAdjust.getId();
	}

	public void updateProjectPoolAdjustById(ProjectPoolAdjust projectPoolAdjust) {
		projectPoolAdjustDao.updateById(projectPoolAdjust);
	}

	public ProjectPoolAdjust queryEntityById(Integer id) {
		return projectPoolAdjustDao.queryEntityById(id);
	}

	public ProjectPoolAdjustResDto detailProjectPoolAdjustById(Integer id) {
		return convertToResDto(projectPoolAdjustDao.queryEntityById(id));
	}

	public void submitProjectPoolAdjustById(Integer id) {
		ProjectPoolAdjust projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		if (projectPoolAdjust == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, ProjectPoolAdjust.class, id);
		}
		if (BaseConsts.ONE != projectPoolAdjust.getState()) {
			throw new BaseException(ExcMsgEnum.SUBMIT_ERROR);
		}
		AuditNode startAuditNode = auditFlowService.getStartAuditNode(BaseConsts.INT_16, null);
		if (null == startAuditNode) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请设置审核流");
		}
		ProjectPoolAdjust ppaUpd = new ProjectPoolAdjust();
		ppaUpd.setId(id);
		ppaUpd.setState(startAuditNode.getAuditNodeState());
		projectPoolAdjustDao.updateById(ppaUpd);
		projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		projectPoolAdjustAuditService.startAudit(projectPoolAdjust, startAuditNode);// 提交审核
	}

	public void lockProjectPoolAdjustById(Integer id) {
		ProjectPoolAdjust projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		if (projectPoolAdjust == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, ProjectPoolAdjust.class, id);
		}
		updateLockProjectPool(projectPoolAdjust);
	}

	public void deleteProjectPoolAdjustById(Integer id) {

		ProjectPoolAdjust projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		if (projectPoolAdjust == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, ProjectPoolAdjust.class, id);
		}
		if (BaseConsts.ONE != projectPoolAdjust.getState()) {
			throw new BaseException(ExcMsgEnum.DELETE_ERROR);
		}
		projectPoolAdjustDao.deleteById(id);
	}

	public Integer updateState(ProjectPoolAdjust projectPoolAdjust, AuditNode auditNode) {
		Integer state = null;
		if (null != auditNode) { // 中间审核节点
			state = auditNode.getAuditNodeState();
			ProjectPoolAdjust projectPoolAdjustUpd = new ProjectPoolAdjust();
			projectPoolAdjustUpd.setId(projectPoolAdjust.getId());
			projectPoolAdjustUpd.setState(state);
			projectPoolAdjustDao.updateById(projectPoolAdjustUpd);
		} else { // 最后一个审核节点
			Date startDate = projectPoolAdjust.getStartValidDate();
			Date endDate = projectPoolAdjust.getEndValidDate();
			Date currDate = DateFormatUtils.getCurrentDate();
			try {
				currDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD,
						DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, currDate));
			} catch (ParseException e) {
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "获取当前系统时间失败");
			}
			if (DateFormatUtils.diffDateTime(endDate, currDate) < 0) { // 当前日期大于结束日期
				throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "申请已过期，无法通过审核");
			}

			if (DateFormatUtils.diffDateTime(startDate, currDate) <= 0
					&& DateFormatUtils.diffDateTime(endDate, currDate) >= 0) { // 开始日期小于等于当前日期且结束日期大于当前日期
				ProjectPoolAdjustSearchReqDto projectPoolAdjustSearchReqDto = new ProjectPoolAdjustSearchReqDto();
				projectPoolAdjustSearchReqDto.setProjectId(projectPoolAdjust.getProjectId());
				projectPoolAdjustSearchReqDto.setState(BaseConsts.THREE); // 查询项目下已完成的条款
				List<ProjectPoolAdjust> projectPoolAdjustList = projectPoolAdjustDao
						.queryResultsByCon(projectPoolAdjustSearchReqDto);
				if (CollectionUtils.isNotEmpty(projectPoolAdjustList)) {
					for (ProjectPoolAdjust pItem : projectPoolAdjustList) {
						updateLockProjectPool(pItem);
					}
				}
				updateOverProjectPool(projectPoolAdjust);
			} else if (DateFormatUtils.diffDateTime(startDate, currDate) > 0) { // 开始日期大于当前日期
				ProjectPoolAdjust ppaUpd = new ProjectPoolAdjust();
				ppaUpd.setId(projectPoolAdjust.getId());
				ppaUpd.setState(BaseConsts.TWO); // 2-待完成
				projectPoolAdjustDao.updateById(ppaUpd);
			}
		}
		return state;
	}

	public void updateStatePassBusAudit(Integer id, AuditNode auditNode) {
		ProjectPoolAdjust projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		if (projectPoolAdjust == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, ProjectPoolAdjust.class, id);
		}
		Date endDate = projectPoolAdjust.getEndValidDate();
		Date currDate = DateFormatUtils.getCurrentDate();
		try {
			currDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD,
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, currDate));
		} catch (ParseException e) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "获取当前系统时间失败");
		}
		if (DateFormatUtils.diffDateTime(endDate, currDate) < 0) { // 当前日期大于结束日期
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "申请已过期，无法通过审核");
		}
		updateState(projectPoolAdjust, auditNode);
	}

	public void updateStatePassFinance2Audit(Integer id, AuditNode auditNode) {
		ProjectPoolAdjust projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		if (projectPoolAdjust == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, ProjectPoolAdjust.class, id);
		}
		Date endDate = projectPoolAdjust.getEndValidDate();
		Date currDate = DateFormatUtils.getCurrentDate();
		try {
			currDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD,
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, currDate));
		} catch (ParseException e) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "获取当前系统时间失败");
		}
		if (DateFormatUtils.diffDateTime(endDate, currDate) < 0) { // 当前日期大于结束日期
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "申请已过期，无法通过审核");
		}
		updateState(projectPoolAdjust, auditNode);
	}

	public void updateStatePassRiskAudit(Integer id, AuditNode auditNode) {
		ProjectPoolAdjust projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		if (projectPoolAdjust == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, ProjectPoolAdjust.class, id);
		}
		Date endDate = projectPoolAdjust.getEndValidDate();
		Date currDate = DateFormatUtils.getCurrentDate();
		try {
			currDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD,
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, currDate));
		} catch (ParseException e) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "获取当前系统时间失败");
		}
		if (DateFormatUtils.diffDateTime(endDate, currDate) < 0) { // 当前日期大于结束日期
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "申请已过期，无法通过审核");
		}
		updateState(projectPoolAdjust, auditNode);
	}

	public void updateStatePassDeptManageAudit(Integer id, AuditNode auditNode) {
		ProjectPoolAdjust projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		if (projectPoolAdjust == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, ProjectPoolAdjust.class, id);
		}
		updateState(projectPoolAdjust, auditNode);
	}

	public void updateStateUnPassAudit(Integer id) {
		ProjectPoolAdjust projectPoolAdjust = projectPoolAdjustDao.queryEntityById(id);
		if (projectPoolAdjust == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, ProjectPoolAdjust.class, id);
		}
		ProjectPoolAdjust projectPoolAdjustUpd = new ProjectPoolAdjust();
		projectPoolAdjustUpd.setId(id);
		projectPoolAdjustUpd.setState(BaseConsts.ONE);
		projectPoolAdjustDao.updateById(projectPoolAdjustUpd);
	}

	public void updateScheduleJob() {
		Date currDate = DateFormatUtils.getCurrentDate();
		try {
			currDate = DateFormatUtils.parse(DateFormatUtils.YYYY_MM_DD,
					DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, currDate));
		} catch (ParseException e) {
			Logger.error("获取当前系统时间失败", e);
			currDate = null;
		}
		ProjectPoolAdjustSearchReqDto req = new ProjectPoolAdjustSearchReqDto();
		req.setSearchType(BaseConsts.ONE); // 待完成和已完成
		List<ProjectPoolAdjust> projectPoolAdjusts = projectPoolAdjustDao.queryResultsByCon(req);
		for (ProjectPoolAdjust projectPoolAdjust : projectPoolAdjusts) {
			if (projectPoolAdjust.getState() == BaseConsts.TWO
					&& DateFormatUtils.diffDateTime(projectPoolAdjust.getStartValidDate(), currDate) == 0
					&& DateFormatUtils.diffDateTime(projectPoolAdjust.getEndValidDate(), currDate) > 0) { // 待完成且开始日期为当前日期

				ProjectPoolAdjustSearchReqDto projectPoolAdjustSearchReqDto = new ProjectPoolAdjustSearchReqDto();
				projectPoolAdjustSearchReqDto.setProjectId(projectPoolAdjust.getProjectId());
				projectPoolAdjustSearchReqDto.setState(BaseConsts.THREE); // 查询项目下已完成的条款
				List<ProjectPoolAdjust> projectItemList = projectPoolAdjustDao
						.queryResultsByCon(projectPoolAdjustSearchReqDto);
				if (CollectionUtils.isNotEmpty(projectItemList)) {
					for (ProjectPoolAdjust pItem : projectItemList) {
						updateLockProjectPool(pItem);
					}
				}
				updateOverProjectPool(projectPoolAdjust);
			} else if (projectPoolAdjust.getState().equals(BaseConsts.THREE) && DateFormatUtils
					.diffDateTime(projectPoolAdjust.getEndValidDate(), DateFormatUtils.beforeDay(currDate, 1)) == 0) {
				updateLockProjectPool(projectPoolAdjust);
			}
		}
	}

	private void updateOverProjectPool(ProjectPoolAdjust projectPoolAdjust) {

		ProjectPool projectPool = projectPoolService.queryProjectPoolByProjectId(projectPoolAdjust.getProjectId());
		ProjectPool uProjectPool = new ProjectPool();
		uProjectPool.setId(projectPool.getId());
		uProjectPool.setProjectAmount(
				DecimalUtil.formatScale2(projectPool.getProjectAmount().add(projectPoolAdjust.getAdjustAmount())));
		uProjectPool.setRemainFundAmount(
				DecimalUtil.formatScale2(projectPool.getRemainFundAmount().add(projectPoolAdjust.getAdjustAmount())));
		projectPoolService.updateProjectPool(uProjectPool);

		ProjectPoolAdjust uProjectPoolAdjust = new ProjectPoolAdjust();
		uProjectPoolAdjust.setId(projectPoolAdjust.getId());
		uProjectPoolAdjust.setState(BaseConsts.THREE); // 6-已完成
		projectPoolAdjustDao.updateById(uProjectPoolAdjust);
	}

	private void updateLockProjectPool(ProjectPoolAdjust projectPoolAdjust) {

		ProjectPool projectPool = projectPoolService.queryProjectPoolByProjectId(projectPoolAdjust.getProjectId());
		ProjectPool uProjectPool = new ProjectPool();
		uProjectPool.setId(projectPool.getId());
		uProjectPool.setProjectAmount(
				DecimalUtil.formatScale2(projectPool.getProjectAmount().subtract(projectPoolAdjust.getAdjustAmount())));
		uProjectPool.setRemainFundAmount(DecimalUtil
				.formatScale2(projectPool.getRemainFundAmount().subtract(projectPoolAdjust.getAdjustAmount())));
		projectPoolService.updateProjectPool(uProjectPool);

		ProjectPoolAdjust uProjectPoolAdjust = new ProjectPoolAdjust();
		uProjectPoolAdjust.setId(projectPoolAdjust.getId());
		uProjectPoolAdjust.setState(BaseConsts.FOUR); // 6-已锁定
		projectPoolAdjustDao.updateById(uProjectPoolAdjust);
	}

	public PageResult<ProjectPoolAdjustResDto> queryResultsByCon(ProjectPoolAdjustSearchReqDto reqDto) {
		PageResult<ProjectPoolAdjustResDto> pageResult = new PageResult<ProjectPoolAdjustResDto>();
		reqDto.setUserId(ServiceSupport.getUser().getId());
		int offSet = PageUtil.getOffSet(reqDto.getPage(), reqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, reqDto.getPer_page());
		List<ProjectPoolAdjustResDto> projectPoolAdjustResDtos = convertToResDtos(
				projectPoolAdjustDao.queryResultsByCon(reqDto, rowBounds));
		pageResult.setItems(projectPoolAdjustResDtos);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), reqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(reqDto.getPage());
		pageResult.setPer_page(reqDto.getPer_page());
		return pageResult;
	}

	private ProjectPoolAdjustResDto convertToResDto(ProjectPoolAdjust projectPoolAdjust) {
		if (projectPoolAdjust == null) {
			return null;
		}
		ProjectPoolAdjustResDto projectPoolAdjustResDto = new ProjectPoolAdjustResDto();
		BeanUtils.copyProperties(projectPoolAdjust, projectPoolAdjustResDto);
		projectPoolAdjustResDto.setCurrencyTypeName(ServiceSupport
				.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE, projectPoolAdjust.getCurrencyType() + ""));
		projectPoolAdjustResDto.setProjectName(cacheService.getProjectNameById(projectPoolAdjust.getProjectId()));
		projectPoolAdjustResDto.setStateName(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_POOL_ADJUST_STATE,
				projectPoolAdjust.getState() + ""));
		projectPoolAdjustResDto.setOpertaList(getOperList(projectPoolAdjust.getState()));
		projectPoolAdjustResDto.setRemainRate(DecimalUtil.toPercentString(DecimalUtil
				.divide(projectPoolAdjustResDto.getRemainFundAmount(), projectPoolAdjustResDto.getProjectAmount())));
		projectPoolAdjustResDto.setOpertaList(getOperList(projectPoolAdjust.getState()));
		projectPoolAdjustResDto.setValidDateString(DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD,
				projectPoolAdjustResDto.getStartValidDate()) + "~"
				+ DateFormatUtils.format(DateFormatUtils.YYYY_MM_DD, projectPoolAdjustResDto.getEndValidDate()));
		return projectPoolAdjustResDto;
	}

	private List<ProjectPoolAdjustResDto> convertToResDtos(List<ProjectPoolAdjust> projectPoolAdjusts) {
		List<ProjectPoolAdjustResDto> projectPoolAdjustResDtos = new ArrayList<ProjectPoolAdjustResDto>();
		if (CollectionUtils.isEmpty(projectPoolAdjusts)) {
			return projectPoolAdjustResDtos;
		}
		for (ProjectPoolAdjust projectPoolAdjust : projectPoolAdjusts) {
			ProjectPoolAdjustResDto projectPoolAdjustResDto = convertToResDto(projectPoolAdjust);
			projectPoolAdjustResDtos.add(projectPoolAdjustResDto);
		}
		return projectPoolAdjustResDtos;
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
		case BaseConsts.TWO: // 2-待完成
			break;
		case BaseConsts.THREE: // 3-已完成
			opertaList.add(OperateConsts.LOCK);
			break;
		case BaseConsts.FOUR: // 4-已锁定
			break;
		}

		return opertaList;
	}

	/**
	 * 获取文件操作列表
	 * 
	 * @param state
	 * @return
	 */
	public PageResult<ProjectPoolAdjustFileDto> queryFileList(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<ProjectPoolAdjustFileDto> pageResult = new PageResult<ProjectPoolAdjustFileDto>();
		fileAttReqDto.setBusType(BaseConsts.INT_23);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<ProjectPoolAdjustFileDto> list = convertToFileResDto(fielAttach);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), fileAttReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(fileAttReqDto.getPage());
		pageResult.setPer_page(fileAttReqDto.getPer_page());
		pageResult.setItems(list);
		return pageResult;
	}

	public List<ProjectPoolAdjustFileDto> queryFileListAll(FileAttachSearchReqDto fileAttReqDto) {
		fileAttReqDto.setBusType(BaseConsts.INT_23);
		List<FileAttach> fielAttach = fileUploadService.queryFileAttList(fileAttReqDto);
		List<ProjectPoolAdjustFileDto> list = convertToFileResDto(fielAttach);
		return list;
	}

	private List<ProjectPoolAdjustFileDto> convertToFileResDto(List<FileAttach> fileAttach) {
		List<ProjectPoolAdjustFileDto> list = new LinkedList<ProjectPoolAdjustFileDto>();
		if (ListUtil.isEmpty(fileAttach)) {
			return list;
		}
		for (FileAttach model : fileAttach) {
			ProjectPoolAdjustFileDto result = new ProjectPoolAdjustFileDto();
			result.setId(model.getId());
			result.setBusId(model.getBusId());
			result.setBusTypeName(
					ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, model.getBusType() + ""));
			result.setName(model.getName());
			result.setType(model.getType());
			result.setCreateAt(model.getCreateAt());
			result.setCreator(model.getCreator());
			List<CodeValue> operList = getOperList();
			result.setOpertaList(operList);
			list.add(result);
		}
		return list;
	}

	private List<CodeValue> getOperList() {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState();
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				PayOrderFileResDto.Operate.operMap);
		return oprResult;
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

}
