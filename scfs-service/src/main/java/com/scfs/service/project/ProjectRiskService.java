package com.scfs.service.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.ListUtil;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.dao.project.ProjectRiskDao;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.project.dto.req.ProjectRiskSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectRiskResDto;
import com.scfs.domain.project.entity.ProjectRisk;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import com.google.common.collect.Lists;

/**
 * <pre>
 *
 *  File: PayService.java
 *  Description:项目事件
 *  TODO
 *  Date,					Who,
 *  2016年12月22日			Administrator
 *
 * </pre>
 */
@Service
public class ProjectRiskService {
	@Autowired
	private ProjectRiskDao projectRiskDao;

	@Autowired
	private CacheService cacheService;

	/**
	 * 添加数据
	 * 
	 * @param projectRisk
	 * @return
	 */
	public int createProjectRisk(ProjectRisk projectRisk) {
		Date date = new Date();
		projectRisk.setCreateAt(date);
		projectRisk.setCreator(ServiceSupport.getUser().getChineseName());
		projectRisk.setStatus(BaseConsts.ONE);
		projectRisk.setIsDelete(BaseConsts.ZERO);
		int id = projectRiskDao.insert(projectRisk);
		if (id <= 0) {
			throw new BaseException(ExcMsgEnum.ERROR_GENERAL, "添加失败:" + JSONObject.toJSON(projectRisk));
		}
		return projectRisk.getId();
	}

	/**
	 * 更新信息
	 * 
	 * @param projectRisk
	 * @return
	 */
	public BaseResult updateProjectRiskById(ProjectRisk projectRisk) {
		BaseResult baseResult = new BaseResult();
		projectRiskDao.queryEntityById(projectRisk.getId());
		projectRisk.setUpdateAt(new Date());
		int result = projectRiskDao.updateById(projectRisk);
		if (result == BaseConsts.ZERO) {
			baseResult.setSuccess(false);
			baseResult.setMsg("更新失败，请重试");
		}
		return baseResult;
	}

	/**
	 * 编辑
	 * 
	 * @param projectRisk
	 * @return
	 */
	public Result<ProjectRiskResDto> editProjectRiskById(ProjectRisk projectRisk) {
		Result<ProjectRiskResDto> result = new Result<ProjectRiskResDto>();
		ProjectRiskResDto projectRiskResDto = convertToProjectRiskResDto(
				projectRiskDao.queryEntityById(projectRisk.getId()));
		result.setItems(projectRiskResDto);
		return result;
	}

	/**
	 * 提交
	 * 
	 * @param projectRisk
	 * @return
	 */
	public BaseResult submitProjectRisk(ProjectRisk projectRisk) {
		BaseResult baseResult = new BaseResult();
		projectRiskDao.queryEntityById(projectRisk.getId());
		projectRisk.setStatus(BaseConsts.TWO);
		projectRiskDao.updateById(projectRisk);
		return baseResult;
	}

	/**
	 * 删除
	 * 
	 * @param projectRisk
	 * @return
	 */
	public BaseResult deleteProjectRisk(ProjectRisk projectRisk) {
		BaseResult baseResult = new BaseResult();
		projectRisk.setDeleter(ServiceSupport.getUser().getChineseName());
		projectRiskDao.queryEntityById(projectRisk.getId());
		projectRisk.setDeleteAt(new Date());
		projectRisk.setIsDelete(BaseConsts.ONE);
		projectRiskDao.updateById(projectRisk);
		return baseResult;
	}

	/**
	 * 列表
	 * 
	 * @param payOrderSearchReqDto
	 * @return
	 */
	public PageResult<ProjectRiskResDto> queryPayOrderResultsByCon(ProjectRiskSearchReqDto projectRiskSearchReqDto) {
		PageResult<ProjectRiskResDto> pageResult = new PageResult<ProjectRiskResDto>();
		int offSet = PageUtil.getOffSet(projectRiskSearchReqDto.getPage(), projectRiskSearchReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, projectRiskSearchReqDto.getPer_page());
		projectRiskSearchReqDto.setUserId(ServiceSupport.getUser().getId());
		List<ProjectRiskResDto> projectRiskRes = convertToProjectRiskResDtos(
				projectRiskDao.queryResultsByCon(projectRiskSearchReqDto, rowBounds));
		pageResult.setItems(projectRiskRes);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), projectRiskSearchReqDto.getPer_page());
		pageResult.setLast_page(totalPage);
		pageResult.setTotal(CountHelper.getTotalRow());
		pageResult.setCurrent_page(projectRiskSearchReqDto.getPage());
		pageResult.setPer_page(projectRiskSearchReqDto.getPer_page());
		return pageResult;
	}

	public List<ProjectRiskResDto> convertToProjectRiskResDtos(List<ProjectRisk> result) {
		List<ProjectRiskResDto> projectRiskResDtos = new ArrayList<ProjectRiskResDto>();
		if (ListUtil.isEmpty(result)) {
			return projectRiskResDtos;
		}
		for (ProjectRisk projectRisk : result) {
			ProjectRiskResDto projectRiskResDto = convertToProjectRiskResDto(projectRisk);
			List<CodeValue> operList = getOperList(projectRisk.getStatus());
			projectRiskResDto.setOpertaList(operList);
			projectRiskResDtos.add(projectRiskResDto);
		}
		return projectRiskResDtos;
	}

	public ProjectRiskResDto convertToProjectRiskResDto(ProjectRisk model) {
		ProjectRiskResDto result = new ProjectRiskResDto();
		result.setId(model.getId());
		result.setProjectId(model.getProjectId());
		result.setProjectName(cacheService.showProjectNameById(model.getProjectId()));
		result.setRisktype(model.getRisktype());
		result.setTitle(model.getTitle());
		result.setRemarks(model.getRemarks());
		result.setStatus(model.getStatus());
		result.setStatusName(
				ServiceSupport.getValueByBizCode(BizCodeConsts.PROJECT_RISK_STATUS, model.getStatus() + ""));
		result.setCreator(model.getCreator());
		result.setCreateAt(model.getCreateAt());
		return result;
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
				ProjectRiskResDto.Operate.operMap);
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
		switch (state) {
		// 状态 1 待提交 2 已完成
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.DELETE);
			opertaList.add(OperateConsts.EDIT);
			opertaList.add(OperateConsts.DETAIL);
			opertaList.add(OperateConsts.SUBMIT);
			break;
		default:
			opertaList.add(OperateConsts.DETAIL);
			break;
		}
		return opertaList;
	}
}
