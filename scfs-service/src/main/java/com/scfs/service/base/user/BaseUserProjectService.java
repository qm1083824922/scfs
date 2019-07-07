package com.scfs.service.base.user;

import com.beust.jcommander.internal.Lists;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.CacheKeyConsts;
import com.scfs.common.consts.ExcMsgEnum;
import com.scfs.common.consts.OperateConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.common.utils.PageUtil;
import com.scfs.dao.base.entity.BaseProjectDao;
import com.scfs.dao.base.entity.BaseUserProjectDao;
import com.scfs.dao.interceptor.CountHelper;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.dto.resp.BaseProjectResDto;
import com.scfs.domain.base.dto.resp.BaseUserProjectResDto;
import com.scfs.domain.base.entity.BaseProject;
import com.scfs.domain.base.entity.BaseUserProject;
import com.scfs.domain.project.dto.req.UserProjectReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.support.CacheService;
import com.scfs.service.support.ServiceSupport;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BaseUserProjectService {

	@Autowired
	private BaseUserProjectDao baseUserProjectDao;
	@Autowired
	private BaseProjectDao baseProjectDao;

	@Autowired
	private CacheService cacheServiceImpl;

	public PageResult<BaseUserProjectResDto> queryBaseUserProjectAssignedToUser(UserProjectReqDto baseReqDto) {
		PageResult<BaseUserProjectResDto> result = new PageResult<BaseUserProjectResDto>();
		int offSet = PageUtil.getOffSet(baseReqDto.getPage(), baseReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseReqDto.getPer_page());
		List<BaseUserProject> baseUserProjectList = baseProjectDao
				.queryUserProjectAssignedToUser(baseReqDto.getUserId(), rowBounds);
		List<BaseUserProjectResDto> baseUserProjectResDto = convertToResult(baseUserProjectList);
		result.setItems(baseUserProjectResDto);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseReqDto.getPage());
		result.setPer_page(baseReqDto.getPer_page());
		return result;
	}

	private List<BaseUserProjectResDto> convertToResult(List<BaseUserProject> baseUserProjectList) {
		List<BaseUserProjectResDto> baseUserProjectResDtoList = new ArrayList<BaseUserProjectResDto>();
		if (CollectionUtils.isEmpty(baseUserProjectList)) {
			return baseUserProjectResDtoList;
		}
		for (BaseUserProject baseUserProject : baseUserProjectList) {
			BaseUserProjectResDto baseUserProjectResDto = convertToResDto(baseUserProject);
			baseUserProjectResDto.setOpertaList(getOperList(baseUserProject.getState()));
			baseUserProjectResDtoList.add(baseUserProjectResDto);
		}
		return baseUserProjectResDtoList;
	}

	private List<CodeValue> getOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseUserProjectResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		switch (state) {
		// 状态,1表示可用
		case BaseConsts.ONE:
			opertaList.add(OperateConsts.INVALID);
			break;
		}
		return opertaList;
	}

	private BaseUserProjectResDto convertToResDto(BaseUserProject baseUserProject) {
		BaseUserProjectResDto baseUserProjectResDto = new BaseUserProjectResDto();
		baseUserProjectResDto.setId(baseUserProject.getId());
		baseUserProjectResDto.setAssigner(baseUserProject.getAssigner());
		baseUserProjectResDto.setAssignAt(baseUserProject.getAssignAt());
		baseUserProjectResDto.setDeleter(baseUserProject.getDeleter());
		baseUserProjectResDto.setDeleteAt(baseUserProject.getDeleteAt());
		baseUserProjectResDto.setState(
				ServiceSupport.getValueByBizCode(BizCodeConsts.USER_PROJECT_STATUS, baseUserProject.getState() + ""));
		baseUserProjectResDto.setBusinessUnit(cacheServiceImpl
				.getSubjectNameByIdAndKey(baseUserProject.getBusinessUnitId(), CacheKeyConsts.BUSI_UNIT));
		baseUserProjectResDto.setProjectName(cacheServiceImpl.showProjectNameById(baseUserProject.getProjectId()));
		// baseUserProjectResDto.setStatus(
		// ServiceSupport.getValueByBizCode(BizCodeConsts.USER_PROJECT_STATUS,
		// cacheServiceImpl.getProjectById(baseUserProject.getProjectId()).getStatus()+""));

		return baseUserProjectResDto;
	}

	public PageResult<BaseProjectResDto> queryProjectNotAssignedToUser(UserProjectReqDto baseReqDto) {
		PageResult<BaseProjectResDto> result = new PageResult<BaseProjectResDto>();
		int offSet = PageUtil.getOffSet(baseReqDto.getPage(), baseReqDto.getPer_page());
		RowBounds rowBounds = new RowBounds(offSet, baseReqDto.getPer_page());
		if (baseReqDto.getSighId() != null) {
			baseReqDto.setUserId(baseReqDto.getSighId());
		}
		List<BaseProject> baseProjectList = baseProjectDao.queryProjectNotAssignedToUser(baseReqDto, rowBounds);
		List<BaseProjectResDto> baseProjectResDtoList = convertToProjectResult(baseProjectList);
		result.setItems(baseProjectResDtoList);
		int totalPage = PageUtil.getTotalPage(CountHelper.getTotalRow(), baseReqDto.getPer_page());
		result.setLast_page(totalPage);
		result.setTotal(CountHelper.getTotalRow());
		result.setCurrent_page(baseReqDto.getPage());
		result.setPer_page(baseReqDto.getPer_page());
		return result;
	}

	private List<BaseProjectResDto> convertToProjectResult(List<BaseProject> baseProjectList) {
		List<BaseProjectResDto> baseProjectResDtoList = new ArrayList<BaseProjectResDto>();
		if (CollectionUtils.isEmpty(baseProjectList)) {
			return baseProjectResDtoList;
		}
		for (BaseProject baseProject : baseProjectList) {
			BaseProjectResDto baseProjectResDto = new BaseProjectResDto();
			baseProjectResDto.setId(baseProject.getId());
			baseProjectResDto.setBusinessUnit(cacheServiceImpl.getSubjectNameByIdAndKey(baseProject.getBusinessUnitId(),
					CacheKeyConsts.BUSI_UNIT));
			baseProjectResDto.setProjectName(cacheServiceImpl.showProjectNameById(baseProject.getId()));
			baseProjectResDto.setOpertaList(getProjectOperList(baseProject.getStatus()));
			baseProjectResDtoList.add(baseProjectResDto);
		}
		return baseProjectResDtoList;
	}

	private List<CodeValue> getProjectOperList(Integer state) {
		// 1.根据状态得到操作列表
		List<String> operNameList = getProjectOperListByState(state);
		// 2.将上述的操作列表通过权限过滤
		List<CodeValue> oprResult = ServiceSupport.getOperListByPermission(operNameList,
				BaseProjectResDto.Operate.operMap);
		return oprResult;
	}

	private List<String> getProjectOperListByState(Integer state) {
		if (state == null) {
			return null;
		}
		List<String> opertaList = Lists.newArrayList(5);
		switch (state) {
		// 状态,1表示可用
		case BaseConsts.TWO:
			opertaList.add(OperateConsts.DIVIDE);
			break;
		}
		return opertaList;
	}

	public Result<String> deleteAllBaseUserProject(BaseReqDto baseReqDto) {
		Result<String> result = new Result<String>();
		List<BaseUserProject> userProjectList = new ArrayList<BaseUserProject>();
		for (int i = 0; i < baseReqDto.getIds().size(); i++) {
			BaseUserProject baseUserProject = baseUserProjectDao.queryUserProjectById(baseReqDto.getIds().get(i));
			if (baseUserProject.getIsDelete() != null && baseUserProject.getIsDelete() == 1) {
				throw new BaseException(ExcMsgEnum.USERSPROJECT_EXISTINVALID_EXCEPTION);
			}
			BaseUserProject userProject = new BaseUserProject();
			userProject.setId(baseReqDto.getIds().get(i));
			userProject.setDeleter(ServiceSupport.getUser().getChineseName());
			userProject.setDeleteAt(new Date());
			userProject.setState(BaseConsts.TWO);
			userProject.setIsDelete(BaseConsts.ONE);
			userProjectList.add(userProject);
		}
		baseUserProjectDao.batchUpdateUserProject(userProjectList);

		return result;
	}

	public Result<BaseUserProject> addBaseUserProject(BaseReqDto baseReqDto) {
		Result<BaseUserProject> result = new Result<BaseUserProject>();
		for (int i = 0; i < baseReqDto.getIds().size(); i++) {
			BaseUserProject baseUserProject = new BaseUserProject();
			baseUserProject.setUserId(baseReqDto.getUserId());
			baseUserProject.setProjectId(baseReqDto.getIds().get(i));
			List<BaseUserProject> con = baseUserProjectDao.queryUserProjectByCon(baseUserProject);
			if (con.isEmpty()) {
				baseUserProject.setCreator(ServiceSupport.getUser().getChineseName());
				baseUserProject.setCreateAt(new Date());
				baseUserProject.setAssigner(ServiceSupport.getUser().getChineseName());
				baseUserProject.setAssignAt(new Date());
				baseUserProject.setState(BaseConsts.ONE);
				baseUserProjectDao.insertUserProject(baseUserProject);
			} else {
				throw new BaseException(ExcMsgEnum.USERSPROJECT_DIVIDE_EXCEPTION);
			}
		}
		return result;
	}

	public Result<BaseUserProject> addUserToProject(BaseReqDto baseReqDto) {
		Result<BaseUserProject> result = new Result<BaseUserProject>();
		for (int i = 0; i < baseReqDto.getIds().size(); i++) {
			BaseUserProject baseUserProject = new BaseUserProject();
			baseUserProject.setUserId(baseReqDto.getIds().get(i));
			baseUserProject.setProjectId(baseReqDto.getUserId());
			List<BaseUserProject> con = baseUserProjectDao.queryUserProjectByCon(baseUserProject);
			if (con.isEmpty()) {
				baseUserProject.setCreator(ServiceSupport.getUser().getChineseName());
				baseUserProject.setCreateAt(new Date());
				baseUserProject.setAssigner(ServiceSupport.getUser().getChineseName());
				baseUserProject.setAssignAt(new Date());
				baseUserProject.setState(BaseConsts.ONE);
				baseUserProjectDao.insertUserProject(baseUserProject);
			} else {
				throw new BaseException(ExcMsgEnum.USERSPROJECT_DIVIDE_EXCEPTION);
			}
		}
		return result;
	}

	public Result<String> deleteAllUserToProject(BaseReqDto baseReqDto) {
		Result<String> result = new Result<String>();
		List<BaseUserProject> userProjectList = new ArrayList<BaseUserProject>();
		for (int i = 0; i < baseReqDto.getIds().size(); i++) {
			BaseUserProject userProject = new BaseUserProject();
			userProject.setUserId(baseReqDto.getIds().get(i));
			userProject.setProjectId(baseReqDto.getUserId());
			List<BaseUserProject> baseUserProject = baseUserProjectDao.queryUserProjectByCon(userProject);
			if (baseUserProject.get(0).getIsDelete() != null && baseUserProject.get(0).getIsDelete() == 1) {
				throw new BaseException(ExcMsgEnum.USERSPROJECT_EXISTINVALID_EXCEPTION);
			}
			userProject.setId(baseUserProject.get(0).getId());
			userProject.setDeleter(ServiceSupport.getUser().getChineseName());
			userProject.setDeleteAt(new Date());
			userProject.setState(BaseConsts.TWO);
			userProject.setIsDelete(BaseConsts.ONE);
			userProjectList.add(userProject);
		}
		baseUserProjectDao.batchUpdateUserProject(userProjectList);

		return result;
	}
}
