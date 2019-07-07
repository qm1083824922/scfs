package com.scfs.web.controller.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.project.dto.req.ProjectSubjectSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectSubjectCResDto;
import com.scfs.domain.project.dto.resp.ProjectSubjectVResDto;
import com.scfs.domain.project.dto.resp.ProjectSubjectWResDto;
import com.scfs.domain.project.dto.resp.SubjectCResDto;
import com.scfs.domain.project.dto.resp.SubjectVResDto;
import com.scfs.domain.project.dto.resp.SubjectWResDto;
import com.scfs.domain.project.entity.ProjectSubject;
import com.scfs.domain.result.PageResult;
import com.scfs.service.project.ProjectSubjectService;

@RestController
public class ProjectSubjectController {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectSubjectController.class);

	@Autowired
	private ProjectSubjectService projectSubjectService;

	/**
	 * 查询项目关联的供应商信息
	 * 
	 * @param projectReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_SUBJECTV, method = RequestMethod.POST)
	public PageResult<ProjectSubjectVResDto> queryProjectSubjectVByProjectId(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<ProjectSubjectVResDto> pageResult = new PageResult<ProjectSubjectVResDto>();
		try {
			projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_SUPPLIER);
			pageResult = projectSubjectService.queryProjectSubjectVByProjectId(projectReqDto);

		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 删除项目关联供应商信息
	 * 
	 * @param projectSubject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PROJECT_SUBJECTV, method = RequestMethod.POST)
	public BaseResult deleteProjectSubjectVById(ProjectSubject projectSubject) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.deleteProjectSubjectById(projectSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(projectSubject), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 批量删除项目关联供应商信息
	 * 
	 * @param ids
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEALL_PROJECT_SUBJECTV, method = RequestMethod.POST)
	public BaseResult deleteProjectSubjectVByIds(ProjectSubjectSearchReqDto dto) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.deleteProjectSubjectByIds(dto.getIds());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(dto), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 查询项目未分配供应商信息
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_SUBJECTV_NOTASSIGNED, method = RequestMethod.POST)
	public PageResult<SubjectVResDto> querySubjectVToProjectByCon(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<SubjectVResDto> pageResult = new PageResult<SubjectVResDto>();
		try {
			projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_SUPPLIER);
			pageResult = projectSubjectService.querySubjectVToProjectByCon(projectReqDto);

		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 批量分配供应商到项目
	 * 
	 * @param subjectId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVIDALL_PROJECT_SUBJECTV, method = RequestMethod.POST)
	public BaseResult createProjectSubjectV(ProjectSubjectSearchReqDto projectReqDto) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.createProjectSubject(projectReqDto.getIds(), projectReqDto.getProjectId(),
					BaseConsts.SUBJECT_TYPE_SUPPLIER);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配信息异常[{}]", JSONObject.toJSON(projectReqDto), e);
			br.setSuccess(false);
			br.setMsg("分配失败，请重试");
		}
		return br;
	}

	/**
	 * 分配供应商到项目
	 * 
	 * @param subjectId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PROJECT_SUBJECTV, method = RequestMethod.POST)
	public BaseResult createProjectSubjectV(ProjectSubject projectSubject) {
		BaseResult br = new BaseResult();
		try {
			projectSubject.setSubjectType(BaseConsts.SUBJECT_TYPE_SUPPLIER);
			projectSubjectService.createProjectSubject(projectSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配信息异常[{}]", JSONObject.toJSON(projectSubject), e);
			br.setSuccess(false);
			br.setMsg("分配失败，请重试");
		}
		return br;
	}

	/**
	 * 查询项目关联的客戶信息
	 * 
	 * @param projectReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_SUBJECTC, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectSubjectCResDto> queryProjectSubjectCByProjectId(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<ProjectSubjectCResDto> pageResult = new PageResult<ProjectSubjectCResDto>();
		try {
			projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_CUSTOMER);
			pageResult = projectSubjectService.queryProjectSubjectCByProjectId(projectReqDto);

		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 删除项目关联客戶信息
	 * 
	 * @param projectSubject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PROJECT_SUBJECTC, method = RequestMethod.POST)
	public BaseResult deleteProjectSubjectCById(ProjectSubject projectSubject) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.deleteProjectSubjectById(projectSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(projectSubject), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 批量删除项目关联客戶信息
	 * 
	 * @param ids
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEALL_PROJECT_SUBJECTC, method = RequestMethod.POST)
	public BaseResult deleteProjectSubjectCByIds(ProjectSubjectSearchReqDto dto) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.deleteProjectSubjectByIds(dto.getIds());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(dto), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 查询项目未分配客戶信息
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_SUBJECTC_NOTASSIGNED, method = RequestMethod.POST)
	public PageResult<SubjectCResDto> querySubjectCToProjectByCon(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<SubjectCResDto> pageResult = new PageResult<SubjectCResDto>();
		try {
			projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_CUSTOMER);
			pageResult = projectSubjectService.querySubjectCToProjectByCon(projectReqDto);

		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 批量分配客戶到项目
	 * 
	 * @param subjectId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVIDALL_PROJECT_SUBJECTC, method = RequestMethod.POST)
	public BaseResult createProjectSubjectC(ProjectSubjectSearchReqDto projectReqDto) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.createProjectSubject(projectReqDto.getIds(), projectReqDto.getProjectId(),
					BaseConsts.SUBJECT_TYPE_CUSTOMER);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配信息异常[{}]", JSONObject.toJSON(projectReqDto), e);
			br.setSuccess(false);
			br.setMsg("分配失败，请重试");
		}
		return br;
	}

	/**
	 * 分配客戶到项目
	 * 
	 * @param subjectId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PROJECT_SUBJECTC, method = RequestMethod.POST)
	public BaseResult createProjectSubjectC(ProjectSubject projectSubject) {
		BaseResult br = new BaseResult();
		try {
			projectSubject.setSubjectType(BaseConsts.SUBJECT_TYPE_CUSTOMER);
			projectSubjectService.createProjectSubject(projectSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配信息异常[{}]", JSONObject.toJSON(projectSubject), e);
			br.setSuccess(false);
			br.setMsg("分配失败，请重试");
		}
		return br;
	}

	/**
	 * 查询项目关联的仓库信息
	 * 
	 * @param projectReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_SUBJECTW, method = RequestMethod.POST)
	public PageResult<ProjectSubjectWResDto> queryProjectSubjectWByProjectId(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<ProjectSubjectWResDto> pageResult = new PageResult<ProjectSubjectWResDto>();
		try {
			projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_WAREHOUSE);
			pageResult = projectSubjectService.queryProjectSubjectWByProjectId(projectReqDto);

		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 删除项目关联仓库信息
	 * 
	 * @param projectSubject
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_PROJECT_SUBJECTW, method = RequestMethod.POST)
	public BaseResult deleteProjectSubjectWById(ProjectSubject projectSubject) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.deleteProjectSubjectById(projectSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(projectSubject), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 批量删除项目关联仓库信息
	 * 
	 * @param ids
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEALL_PROJECT_SUBJECTW, method = RequestMethod.POST)
	public BaseResult deleteProjectSubjectWByIds(ProjectSubjectSearchReqDto dto) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.deleteProjectSubjectByIds(dto.getIds());
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(dto), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 查询项目未分配仓库信息
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_PROJECT_SUBJECTW_NOTASSIGNED, method = RequestMethod.POST)
	public PageResult<SubjectWResDto> querySubjectWToProjectByCon(ProjectSubjectSearchReqDto projectReqDto) {
		PageResult<SubjectWResDto> pageResult = new PageResult<SubjectWResDto>();
		try {
			projectReqDto.setSubjectType(BaseConsts.SUBJECT_TYPE_WAREHOUSE);
			pageResult = projectSubjectService.querySubjectWToProjectByCon(projectReqDto);

		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}

		return pageResult;
	}

	/**
	 * 批量分配仓库到项目
	 * 
	 * @param subjectId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVIDALL_PROJECT_SUBJECTW, method = RequestMethod.POST)
	public BaseResult createProjectSubjectW(ProjectSubjectSearchReqDto projectReqDto) {
		BaseResult br = new BaseResult();
		try {
			projectSubjectService.createProjectSubject(projectReqDto.getIds(), projectReqDto.getProjectId(),
					BaseConsts.SUBJECT_TYPE_WAREHOUSE);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配信息异常[{}]", JSONObject.toJSON(projectReqDto), e);
			br.setSuccess(false);
			br.setMsg("分配失败，请重试");
		}
		return br;
	}

	/**
	 * 分配仓库到项目
	 * 
	 * @param subjectId
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DIVID_PROJECT_SUBJECTW, method = RequestMethod.POST)
	public BaseResult createProjectSubjectW(ProjectSubject projectSubject) {
		BaseResult br = new BaseResult();
		try {
			projectSubject.setSubjectType(BaseConsts.SUBJECT_TYPE_WAREHOUSE);
			projectSubjectService.createProjectSubject(projectSubject);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("分配信息异常[{}]", JSONObject.toJSON(projectSubject), e);
			br.setSuccess(false);
			br.setMsg("分配失败，请重试");
		}
		return br;
	}
}
