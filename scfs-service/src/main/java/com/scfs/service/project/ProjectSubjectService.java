package com.scfs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.project.ProjectSubjectDao;
import com.scfs.domain.base.entity.BaseSubject;
import com.scfs.domain.project.dto.req.ProjectSubjectSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectSubjectCResDto;
import com.scfs.domain.project.dto.resp.ProjectSubjectVResDto;
import com.scfs.domain.project.dto.resp.ProjectSubjectWResDto;
import com.scfs.domain.project.dto.resp.SubjectCResDto;
import com.scfs.domain.project.dto.resp.SubjectVResDto;
import com.scfs.domain.project.dto.resp.SubjectWResDto;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.domain.result.PageResult;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;

@Service
public class ProjectSubjectService {

	@Autowired
	private ProjectSubjectDao projectSubjectDao;

	@Autowired
	private CacheService cacheService;

	public ProjectSubject loadAndLockEntityById(int id) {
		ProjectSubject obj = projectSubjectDao.loadAndLockEntityById(id);
		if (obj == null) {
			throw new BaseException(ExcMsgEnum.ENTITY_NOT_EXSIT, projectSubjectDao.getClass(), id);
		}
		return obj;
	}

	public Integer createProjectSubject(Integer subjectId, Integer projectId, Integer subjectType) {
		ProjectSubject projectSubject = new ProjectSubject();
		projectSubject.setSubjectId(subjectId);
		projectSubject.setProjectId(projectId);
		projectSubject.setSubjectType(subjectType);
		Integer id = createProjectSubject(projectSubject);
		return id;
	}

	public void createProjectSubject(List<Integer> subjectIds, Integer projectId, Integer subjectType) {
		for (Integer subjectId : subjectIds) {
			ProjectSubject projectSubject = new ProjectSubject();
			projectSubject.setSubjectId(subjectId);
			projectSubject.setProjectId(projectId);
			projectSubject.setSubjectType(subjectType);
			createProjectSubject(projectSubject);
		}
	}

	public Integer createProjectSubject(ProjectSubject projectSubject) {
		projectSubject.setStatus(BaseConsts.ONE);
		projectSubject.setCreator(ServiceSupport.getUser().getChineseName());
		projectSubject.setCreateAt(new Date());
		projectSubject.setIsDelete(BaseConsts.ZERO);
		Integer id = projectSubjectDao.insert(projectSubject);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "分配失败:" + JSONObject.toJSON(projectSubject));
		}
		return id;
	}

	public void deleteProjectSubjectById(ProjectSubject projectSubject) {
		ProjectSubject vo = loadAndLockEntityById(projectSubject.getId());
		if (vo.getStatus().compareTo(BaseConsts.ONE) != 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "状态错误,作废失败");
		}
		vo.setStatus(BaseConsts.TWO);
		vo.setIsDelete(BaseConsts.ONE);
		vo.setDeleter(ServiceSupport.getUser().getChineseName());
		vo.setDeleteAt(new Date());
		int result = projectSubjectDao.updateById(vo);
		if (result <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "作废失败:" + JSONObject.toJSON(vo));
		}
	}

	public void deleteProjectSubjectByIds(List<Integer> ids) {
		for (Integer id : ids) {
			ProjectSubject projectSubject = new ProjectSubject();
			projectSubject.setId(id);
			deleteProjectSubjectById(projectSubject);
		}
	}

	public PageResult<ProjectSubjectVResDto> queryProjectSubjectVByProjectId(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<ProjectSubjectVResDto> result = new PageResult<ProjectSubjectVResDto>();
		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());
		List<ProjectSubject> projectSubjectList = projectSubjectDao.queryResultsByCon(projectReqDto, rowBounds);
		result.setItems(convertVToResult(projectSubjectList));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());
		return result;
	}

	private List<ProjectSubjectVResDto> convertVToResult(List<ProjectSubject> projectSubjectList) {
		List<ProjectSubjectVResDto> projectSubjectResDtoList = new ArrayList<ProjectSubjectVResDto>();
		if (CollectionUtils.isEmpty(projectSubjectList)) {
			return projectSubjectResDtoList;
		}
		for (ProjectSubject projectSubject : projectSubjectList) {
			ProjectSubjectVResDto projectSubjectResDto = convertVToResDto(projectSubject);
			projectSubjectResDtoList.add(projectSubjectResDto);
		}
		return projectSubjectResDtoList;
	}

	private ProjectSubjectVResDto convertVToResDto(ProjectSubject projectSubject) {
		ProjectSubjectVResDto projectSubjectResDto = new ProjectSubjectVResDto();
		projectSubjectResDto.setId(projectSubject.getId());
		projectSubjectResDto.setSubjectType(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_SUBJECT_TYPE,
				Integer.toString(projectSubject.getSubjectType())));
		projectSubjectResDto.setSubjectName(cacheService.getSubjectNoNameById(projectSubject.getSubjectId()));
		projectSubjectResDto.setCreator(projectSubject.getCreator());
		projectSubjectResDto.setCreateAt(projectSubject.getCreateAt());
		projectSubjectResDto.setDeleter(projectSubject.getDeleter());
		projectSubjectResDto.setDeleteAt(projectSubject.getDeleteAt());
		projectSubjectResDto.setStatus(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_SUBJECT_STATUS,
				projectSubject.getStatus() + ""));
		return projectSubjectResDto;
	}

	public PageResult<ProjectSubjectCResDto> queryProjectSubjectCByProjectId(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<ProjectSubjectCResDto> result = new PageResult<ProjectSubjectCResDto>();
		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());
		List<ProjectSubject> projectSubjectList = projectSubjectDao.queryResultsByCon(projectReqDto, rowBounds);
		result.setItems(convertCToResult(projectSubjectList));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());
		return result;
	}

	private List<ProjectSubjectCResDto> convertCToResult(List<ProjectSubject> projectSubjectList) {
		List<ProjectSubjectCResDto> projectSubjectResDtoList = new ArrayList<ProjectSubjectCResDto>();
		if (CollectionUtils.isEmpty(projectSubjectList)) {
			return projectSubjectResDtoList;
		}
		for (ProjectSubject projectSubject : projectSubjectList) {
			ProjectSubjectCResDto projectSubjectResDto = convertCToResDto(projectSubject);
			projectSubjectResDtoList.add(projectSubjectResDto);
		}
		return projectSubjectResDtoList;
	}

	private ProjectSubjectCResDto convertCToResDto(ProjectSubject projectSubject) {
		ProjectSubjectCResDto projectSubjectResDto = new ProjectSubjectCResDto();
		projectSubjectResDto.setId(projectSubject.getId());
		projectSubjectResDto.setSubjectType(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_SUBJECT_TYPE,
				Integer.toString(projectSubject.getSubjectType())));
		projectSubjectResDto.setSubjectName(cacheService.getSubjectNoNameById(projectSubject.getSubjectId()));
		projectSubjectResDto.setCreator(projectSubject.getCreator());
		projectSubjectResDto.setCreateAt(projectSubject.getCreateAt());
		projectSubjectResDto.setDeleter(projectSubject.getDeleter());
		projectSubjectResDto.setDeleteAt(projectSubject.getDeleteAt());
		projectSubjectResDto.setStatus(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_SUBJECT_STATUS,
				projectSubject.getStatus() + ""));
		return projectSubjectResDto;
	}

	public PageResult<ProjectSubjectWResDto> queryProjectSubjectWByProjectId(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<ProjectSubjectWResDto> result = new PageResult<ProjectSubjectWResDto>();
		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());
		List<ProjectSubject> projectSubjectList = projectSubjectDao.queryResultsByCon(projectReqDto, rowBounds);
		result.setItems(convertWToResult(projectSubjectList));
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());
		return result;
	}

	private List<ProjectSubjectWResDto> convertWToResult(List<ProjectSubject> projectSubjectList) {
		List<ProjectSubjectWResDto> projectSubjectResDtoList = new ArrayList<ProjectSubjectWResDto>();
		if (CollectionUtils.isEmpty(projectSubjectList)) {
			return projectSubjectResDtoList;
		}
		for (ProjectSubject projectSubject : projectSubjectList) {
			ProjectSubjectWResDto projectSubjectResDto = convertWToResDto(projectSubject);
			projectSubjectResDtoList.add(projectSubjectResDto);
		}
		return projectSubjectResDtoList;
	}

	private ProjectSubjectWResDto convertWToResDto(ProjectSubject projectSubject) {
		ProjectSubjectWResDto projectSubjectResDto = new ProjectSubjectWResDto();
		projectSubjectResDto.setId(projectSubject.getId());
		projectSubjectResDto.setSubjectType(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_SUBJECT_TYPE,
				Integer.toString(projectSubject.getSubjectType())));
		projectSubjectResDto.setSubjectName(cacheService.getSubjectNoNameById(projectSubject.getSubjectId()));
		projectSubjectResDto.setCreator(projectSubject.getCreator());
		projectSubjectResDto.setCreateAt(projectSubject.getCreateAt());
		projectSubjectResDto.setDeleter(projectSubject.getDeleter());
		projectSubjectResDto.setDeleteAt(projectSubject.getDeleteAt());
		projectSubjectResDto.setStatus(ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_SUBJECT_STATUS,
				projectSubject.getStatus() + ""));
		return projectSubjectResDto;
	}

	public PageResult<SubjectVResDto> querySubjectVToProjectByCon(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<SubjectVResDto> result = new PageResult<SubjectVResDto>();
		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());
		List<BaseSubject> baseSubjectList = projectSubjectDao.querySubjectToProjectByCon(projectReqDto, rowBounds);
		List<SubjectVResDto> baseProjectResDtoList = convertVToSubjectResult(baseSubjectList);
		result.setItems(baseProjectResDtoList);

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());
		return result;
	}

	private List<SubjectVResDto> convertVToSubjectResult(List<BaseSubject> baseSubjectList) {
		List<SubjectVResDto> subjectResDtoList = new ArrayList<SubjectVResDto>();
		if (CollectionUtils.isEmpty(baseSubjectList)) {
			return subjectResDtoList;
		}
		for (BaseSubject baseSubject : baseSubjectList) {
			SubjectVResDto subjectResDto = new SubjectVResDto();
			BaseSubject t = cacheService.getBaseSubjectById(baseSubject.getId());
			subjectResDto.setId(t.getId());
			subjectResDto.setSubjectName(t.getNoName());
			subjectResDto.setSubjectType(t.getSubjectType().toString());
			subjectResDtoList.add(subjectResDto);
		}
		return subjectResDtoList;
	}

	public PageResult<SubjectCResDto> querySubjectCToProjectByCon(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<SubjectCResDto> result = new PageResult<SubjectCResDto>();
		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());
		List<BaseSubject> baseSubjectList = projectSubjectDao.querySubjectToProjectByCon(projectReqDto, rowBounds);
		List<SubjectCResDto> baseProjectResDtoList = convertCToSubjectResult(baseSubjectList);
		result.setItems(baseProjectResDtoList);

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());
		return result;
	}

	private List<SubjectCResDto> convertCToSubjectResult(List<BaseSubject> baseSubjectList) {
		List<SubjectCResDto> subjectResDtoList = new ArrayList<SubjectCResDto>();
		if (CollectionUtils.isEmpty(baseSubjectList)) {
			return subjectResDtoList;
		}
		for (BaseSubject baseSubject : baseSubjectList) {
			SubjectCResDto subjectResDto = new SubjectCResDto();
			BaseSubject t = cacheService.getBaseSubjectById(baseSubject.getId());
			subjectResDto.setId(t.getId());
			subjectResDto.setSubjectName(t.getNoName());
			subjectResDto.setSubjectType(t.getSubjectType().toString());
			subjectResDtoList.add(subjectResDto);
		}
		return subjectResDtoList;
	}

	public PageResult<SubjectWResDto> querySubjectWToProjectByCon(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<SubjectWResDto> result = new PageResult<SubjectWResDto>();
		int offSet = PageUtil.getOffSet(projectReqDto.getPage(), projectReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectReqDto.getPer_page());
		List<BaseSubject> baseSubjectList = projectSubjectDao.querySubjectToProjectByCon(projectReqDto, rowBounds);
		List<SubjectWResDto> baseProjectResDtoList = convertWToSubjectResult(baseSubjectList);
		result.setItems(baseProjectResDtoList);

		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(projectReqDto.getPage());
		result.setPer_page(projectReqDto.getPer_page());
		return result;
	}

	private List<SubjectWResDto> convertWToSubjectResult(List<BaseSubject> baseSubjectList) {
		List<SubjectWResDto> subjectResDtoList = new ArrayList<SubjectWResDto>();
		if (CollectionUtils.isEmpty(baseSubjectList)) {
			return subjectResDtoList;
		}
		for (BaseSubject baseSubject : baseSubjectList) {
			SubjectWResDto subjectResDto = new SubjectWResDto();
			BaseSubject t = cacheService.getBaseSubjectById(baseSubject.getId());
			subjectResDto.setId(t.getId());
			subjectResDto.setSubjectName(t.getNoName());
			subjectResDto.setSubjectType(t.getSubjectType().toString());
			subjectResDtoList.add(subjectResDto);
		}
		return subjectResDtoList;
	}

}
