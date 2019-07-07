package com.scfs.service.project;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;
import com.scfs.common.consts.*;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.DecimalUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.common.entity.AsyncExcel;
import com.scfs.domain.project.dto.req.ProjectSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectModelResDto;
import com.scfs.domain.project.dto.resp.ProjectResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.service.common.AsyncExcelService;
import com.scfs.service.common.SequenceService;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

	@Autowired
	private BaseProjectDao baseProjectDao;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private SequenceService sequenceService;
	@Autowired
	private AsyncExcelService asyncExcelService;

	public PageResult<ProjectResDto> queryProjectResultsByCon(ProjectSearchReqDto projectReqDto) {
		PageResult<ProjectResDto> result = new PageResult<ProjectResDto>();
		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());
		projectReqDto.setUserId(ServiceSupport.getUser().getId());
		boolean isOwerAllPro = ServiceSupport.isAllowPerm(UrlConsts.VIRTUAL_ALL_PROJECT);
		List<BaseProject> projectList = null;
		if (isOwerAllPro) {// 是否有所有项目权限
			projectList = baseProjectDao.queryProjectResultsByCon(projectReqDto, rowBounds);
		} else {
			projectList = baseProjectDao.queryUserProjectResultsByCon(projectReqDto, rowBounds);
		}

		List<ProjectResDto> projectResDtoList = convertToResult(projectList);
		result.setItems(projectResDtoList);

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());
		return result;
	}

	/**
	 * 获取所有数据不分页
	 * 
	 * @param projectReqDto
	 * @return
	 */
	public List<ProjectResDto> queryProjectResDtoResultsExcel(ProjectSearchReqDto projectReqDto) {
		projectReqDto.setUserId(ServiceSupport.getUser().getId());
		boolean isOwerAllPro = ServiceSupport.isAllowPerm(UrlConsts.VIRTUAL_ALL_PROJECT);
		List<BaseProject> projectList = null;
		if (isOwerAllPro) {// 是否有所有项目权限
			projectList = baseProjectDao.queryProjectResultsByCon(projectReqDto);
		} else {
			projectList = baseProjectDao.queryUserProjectResultsByCon(projectReqDto);
		}
		List<ProjectResDto> projectResDtoList = convertToResult(projectList);
		return projectResDtoList;
	}

	/**
	 * 判断是否超出导出行数
	 * 
	 * @param queryInvoiceReqDto
	 * @return
	 */
	public boolean isOverasyncMaxLine(ProjectSearchReqDto projectReqDto) {
		projectReqDto.setUserId(ServiceSupport.getUser().getId());
		boolean isOwerAllPro = ServiceSupport.isAllowPerm(UrlConsts.VIRTUAL_ALL_PROJECT);
		int count = 0;
		if (isOwerAllPro) {// 是否有所有项目权限
			count = baseProjectDao.isOverasyncMaxLine(projectReqDto);
		} else {
			count = baseProjectDao.isOverasyncMaxUserLine(projectReqDto);
		}
		//
		if (count >= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			// 后台导出
			AsyncExcel asyncExcel = new AsyncExcel();
			asyncExcel.setName("项目单据导出");
			asyncExcel.setClassName(this.getClass().getName());// 导出类名
			asyncExcel.setMethodName("asyncInvoiceApplyExport");// 导出方法
			asyncExcel.setTemplatePath("/WEB-INF/excel/export/project/project_list.xls");// 导出模板路径
			asyncExcel.setPoType(BaseConsts.INT_15);
			asyncExcelService.addAsyncExcel(projectReqDto, asyncExcel);
			return true;
		}
		return false;
	}

	public Map<String, Object> asyncInvoiceApplyExport(ProjectSearchReqDto projectReqDto) {
		Map<String, Object> model = Maps.newHashMap();
		List<ProjectResDto> projectList = queryProjectResDtoResultsExcel(projectReqDto);
		model.put("projectList", projectList);
		return model;
	}

	public Integer createProject(BaseProject baseProject) {
		BaseSubject baseSubject = cacheService.getBusiUnitById(baseProject.getBusinessUnitId());
		String industrial = String.valueOf(baseProject.getIndustrial());
		String projectNoType = baseProject.getProjectNoType();
		if (null == baseSubject || StringUtils.isBlank(baseSubject.getSubjectNo())) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请选择经营单位");
		}
		int subjectNoLength = baseSubject.getSubjectNo().length();
		if (subjectNoLength < 2) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "请选择经营单位编号长度至少2位字符");
		}
		String busiUnit = baseSubject.getSubjectNo().substring(subjectNoLength - 2, subjectNoLength);
		String projectNo = sequenceService.getProjectNo(BaseConsts.THREE, industrial, projectNoType, busiUnit);
		baseProject.setProjectNo(projectNo);
		baseProject.setStatus(BaseConsts.ONE);
		baseProject.setCreator(ServiceSupport.getUser().getChineseName());
		baseProject.setCreateAt(new Date());
		Integer id = baseProjectDao.insert(baseProject);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "创建失败");
		}
		return baseProject.getId();
	}

	public void updateProjectById(BaseProject baseProject) {
		int result = baseProjectDao.updateById(baseProject);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "更新失败");
		}
	}

	public void submitProjectById(BaseProject baseProject) {
		BaseProject vo = lockEntityById(baseProject.getId());
		if (vo.getStatus() != BaseConsts.ONE) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态错误");
		}
		BaseProject bp = new BaseProject();
		bp.setId(vo.getId());
		bp.setStatus(BaseConsts.TWO);
		updateProjectById(bp);
	}

	public void deleteProjectById(BaseProject baseProject) {
		baseProject.setDeleteAt(new Date());
		baseProject.setDeleter(ServiceSupport.getUser().getChineseName());
		baseProject.setIsDelete(BaseConsts.ONE);
		updateProjectById(baseProject);
	}

	public BaseProject lockEntityById(Integer id) {
		BaseProject vo = baseProjectDao.lockEntityById(id);
		return vo;
	}

	public void lockProjectById(BaseProject baseProject) {
		BaseProject vo = lockEntityById(baseProject.getId());
		if (vo.getStatus() != BaseConsts.TWO) {
			throw new RuntimeException();
		}
		vo.setStatus(BaseConsts.THREE);
		updateProjectById(vo);
	}

	public void unlockProjectById(BaseProject baseProject) {
		BaseProject vo = lockEntityById(baseProject.getId());
		if (vo.getStatus() != BaseConsts.THREE) {
			throw new RuntimeException();
		}
		vo.setStatus(BaseConsts.TWO);
		updateProjectById(vo);
	}

	public ProjectModelResDto detailProjectById(BaseProject baseProject) {
		BaseProject vo = baseProjectDao.queryEntityById(baseProject.getId());
		ProjectModelResDto dto = convertToModelResDto(vo);
		return dto;
	}

	public ProjectModelResDto editProjectById(BaseProject baseProject) {
		BaseProject vo = baseProjectDao.queryEntityById(baseProject.getId());
		ProjectModelResDto dto = convertToModelResDto(vo);
		return dto;
	}

	private List<ProjectResDto> convertToResult(List<BaseProject> projectList) {
		List<ProjectResDto> projectResDtoList = new ArrayList<ProjectResDto>();
		if (CollectionUtils.isEmpty(projectList)) {
			return projectResDtoList;
		}
		for (BaseProject baseProject : projectList) {
			ProjectResDto projectResDto = convertToResDto(baseProject);
			projectResDto.setOpertaList(getOperList(baseProject.getStatus()));
			projectResDtoList.add(projectResDto);
		}
		return projectResDtoList;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList, ProjectResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		opertaList.add(OperateConsts.DETAIL);
		switch (state) {
		// 状态,1表示待提交 2表示已完成 3表示已锁定
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.SUBMIT);
			opertaList.add(OperateConsts.DELETE);
			break;
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.LOCK);
			opertaList.add(OperateConsts.COPY);
			break;
		case BaseConsts.THREE:
			opertaList.add(OperateConsts.UNLOCK);
			opertaList.add(OperateConsts.COPY);
			break;
		}
		return opertaList;
	}

	private ProjectResDto convertToResDto(BaseProject baseProject) {
		ProjectResDto projectResDto = new ProjectResDto();
		projectResDto.setId(baseProject.getId());
		projectResDto.setProjectNo(baseProject.getProjectNo());
		projectResDto.setProjectName(baseProject.getProjectName());
		projectResDto.setFullName(baseProject.getFullName());
		projectResDto.setBizType(baseProject.getBizType());
		projectResDto.setBizTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE, baseProject.getBizType() + ""));
		projectResDto.setBusinessUnitName(
				cacheService.getSubjectNameByIdAndKey(baseProject.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		projectResDto.setBizManagerName(cacheService.getUserChineseNameByid(baseProject.getBizManagerId()));
		projectResDto.setBusinessManagerName(cacheService.getUserChineseNameByid(baseProject.getBusinessManagerId()));
		projectResDto.setFinanceManagerName(cacheService.getUserChineseNameByid(baseProject.getFinanceManagerId()));
		projectResDto.setRiskSpecialName(cacheService.getUserChineseNameByid(baseProject.getRiskSpecialId()));
		projectResDto.setRiskManagerName(cacheService.getUserChineseNameByid(baseProject.getRiskManagerId()));
		projectResDto.setFinanceSpecialName(cacheService.getUserChineseNameByid(baseProject.getFinanceSpecialId()));
		projectResDto.setStatus(baseProject.getStatus());
		projectResDto.setStatusName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_STATUS, baseProject.getStatus() + ""));
		projectResDto.setIndustrialName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.INDUSTRIAL, baseProject.getIndustrial() + ""));
		projectResDto.setCreator(baseProject.getCreator());
		projectResDto.setCreateAt(baseProject.getCreateAt());
		projectResDto.setTotalAmountValue((DecimalUtil.toAmountString(baseProject.getTotalAmount())));
		projectResDto.setAmmountUnitValue(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				baseProject.getAmountUnit() + ""));
		projectResDto.setDepartmentName(cacheService.getBaseDepartmentById(baseProject.getDepartmentId()).getName());
		projectResDto.setProjectNoTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_NO_TYPE, baseProject.getProjectNoType() + ""));
		projectResDto.setBizSpecialName(cacheService.getUserChineseNameByid(baseProject.getBizSpecialId()));
		projectResDto.setLawName(cacheService.getUserChineseNameByid(baseProject.getLawId()));
		projectResDto.setDepartmentManagerName(cacheService.getUserChineseNameByid(baseProject.getDepartmentManagerId()));
		projectResDto.setBossName(cacheService.getUserChineseNameByid(baseProject.getBossId()));
		return projectResDto;
	}

	private ProjectModelResDto convertToModelResDto(BaseProject baseProject) {

		ProjectModelResDto dto = new ProjectModelResDto();
		dto.setId(baseProject.getId());
		dto.setProjectNo(baseProject.getProjectNo());
		dto.setProjectNoType(baseProject.getProjectNoType());
		dto.setProjectName(baseProject.getProjectName());
		dto.setFinanceSpecialId(baseProject.getFinanceSpecialId());
		dto.setIndustrial(baseProject.getIndustrial());
		dto.setFullName(baseProject.getFullName());
		dto.setBusinessUnitId(baseProject.getBusinessUnitId());
		dto.setTotalAmount(baseProject.getTotalAmount());
		dto.setAmountUnit(baseProject.getAmountUnit());
		dto.setBizType(baseProject.getBizType());
		dto.setBizSpecialId(baseProject.getBizSpecialId());
		dto.setBizManagerId(baseProject.getBizManagerId());
		dto.setBusinessManagerId(baseProject.getBusinessManagerId());
		dto.setFinanceManagerId(baseProject.getFinanceManagerId());
		dto.setRiskSpecialId(baseProject.getRiskSpecialId());
		dto.setRiskManagerId(baseProject.getRiskManagerId());
		dto.setStatus(baseProject.getStatus());
		dto.setDepartmentId(baseProject.getDepartmentId());
		dto.setLawId(baseProject.getLawId());
		dto.setDepartmentManagerId(baseProject.getDepartmentManagerId());
		dto.setBossId(baseProject.getBossId());

		dto.setBizTypeValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_BIZTYPE, baseProject.getBizType() + ""));
		dto.setAmountUnitValue(ServiceSupport.getValueByBizCode(BizCodeConsts.DEFAULT_CURRENCY_TYPE,
				baseProject.getAmountUnit() + ""));
		dto.setBusinessUnitName(cacheService.getSubjectNoNameById(baseProject.getBusinessUnitId()));
		dto.setBizManagerName(cacheService.getUserChineseNameByid(baseProject.getBizManagerId()));
		dto.setBusinessManagerName(cacheService.getUserChineseNameByid(baseProject.getBusinessManagerId()));
		dto.setFinanceManagerName(cacheService.getUserChineseNameByid(baseProject.getFinanceManagerId()));
		dto.setRiskSpecialName(cacheService.getUserChineseNameByid(baseProject.getRiskSpecialId()));
		dto.setRiskManagerName(cacheService.getUserChineseNameByid(baseProject.getRiskManagerId()));
		dto.setFinanceSpecialName(cacheService.getUserChineseNameByid(baseProject.getFinanceSpecialId()));
		dto.setStatusValue(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_STATUS, baseProject.getStatus() + ""));
		dto.setIndustrialName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.INDUSTRIAL, baseProject.getIndustrial() + ""));
		dto.setProjectNoTypeName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_NO_TYPE, baseProject.getProjectNoType() + ""));
		dto.setBizType(baseProject.getBizType());
		dto.setDepartmentName(cacheService.getBaseDepartmentById(baseProject.getDepartmentId()).getNameNo());
		dto.setBizSpecialName(cacheService.getUserChineseNameByid(baseProject.getBizSpecialId()));
		dto.setLawName(cacheService.getUserChineseNameByid(baseProject.getLawId()));
		dto.setDepartmentManagerName(cacheService.getUserChineseNameByid(baseProject.getDepartmentManagerId()));
		dto.setBossName(cacheService.getUserChineseNameByid(baseProject.getBossId()));
		return dto;
	}

	public List<BaseProject> queryProjectByBusiUnit(Integer userId, List<String> busiUnitIdList) {
		List<BaseProject> projectList = baseProjectDao.queryProjectByBusiUnit(userId, busiUnitIdList);
		return projectList;
	}

}
