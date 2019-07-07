package com.scfs.web.controller.project;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BizCodeConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.project.dto.req.ProjectPoolAdjustSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectPoolAdjustFileDto;
import com.scfs.domain.project.dto.resp.ProjectPoolAdjustResDto;
import com.scfs.domain.project.entity.ProjectPoolAdjust;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.project.ProjectPoolAdjustService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;

/**
 * <pre>
 * 
 *  File: ProjectPoolAdjustController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年3月23日				Administrator
 *
 * </pre>
 */

@Controller
public class ProjectPoolAdjustController {
	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectPoolAdjustController.class);

	@Autowired
	ProjectPoolAdjustService projectPoolAdjustService;
	@Autowired
	FileUploadService fileUploadService;

	/**
	 * 查询条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectPoolAdjustResDto> queryProjectResultsByCon(ProjectPoolAdjustSearchReqDto reqDto) {
		PageResult<ProjectPoolAdjustResDto> pageResult = new PageResult<ProjectPoolAdjustResDto>();
		try {
			pageResult = projectPoolAdjustService.queryResultsByCon(reqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(reqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.ADDPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createProjectPoolAdjust(ProjectPoolAdjust projectPoolAdjust) {
		Result<Integer> result = new Result<Integer>();
		try {
			Integer id = projectPoolAdjustService.createProjectPoolAdjust(projectPoolAdjust);
			result.setItems(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(projectPoolAdjust), e);
			result.setMsg("插入失败，请重试");
		}
		return result;
	}

	/**
	 * 更新保存条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATEPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateProjectPoolAdjustById(ProjectPoolAdjust projectItem) {
		BaseResult br = new BaseResult();
		try {
			projectPoolAdjustService.updateProjectPoolAdjustById(projectItem);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新信息异常[{}]", JSONObject.toJSON(projectItem), e);
			br.setSuccess(false);
			br.setMsg("更新失败，请重试");
		}
		return br;
	}

	/**
	 * 浏览条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAILPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectPoolAdjustResDto> detailProjectPoolAdjustById(Integer id) {
		Result<ProjectPoolAdjustResDto> result = new Result<ProjectPoolAdjustResDto>();
		try {
			ProjectPoolAdjustResDto vo = projectPoolAdjustService.detailProjectPoolAdjustById(id);
			result.setItems(vo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(id), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑预览条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDITPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectPoolAdjustResDto> editProjectPoolAdjustById(Integer id) {
		Result<ProjectPoolAdjustResDto> result = new Result<ProjectPoolAdjustResDto>();
		try {
			ProjectPoolAdjustResDto vo = projectPoolAdjustService.detailProjectPoolAdjustById(id);
			result.setItems(vo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑项目信息异常[{}]", JSONObject.toJSON(id), e);
			result.setSuccess(false);
			result.setMsg("编辑异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMITPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitProjectPoolAdjustById(Integer id) {
		BaseResult br = new BaseResult();
		try {
			projectPoolAdjustService.submitProjectPoolAdjustById(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交信息异常[{}]", JSONObject.toJSON(id), e);
			br.setSuccess(false);
			br.setMsg("提交失败，请重试");
		}
		return br;
	}

	/**
	 * 删除条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteProjectPoolAdjustById(Integer id) {
		BaseResult br = new BaseResult();
		try {
			projectPoolAdjustService.deleteProjectPoolAdjustById(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(id), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 锁定条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.LOCKPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult lockProjectPoolAdjustById(Integer id) {
		BaseResult br = new BaseResult();
		try {
			projectPoolAdjustService.lockProjectPoolAdjustById(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("锁定信息异常[{}]", JSONObject.toJSON(id), e);
			br.setSuccess(false);
			br.setMsg("锁定失败，请重试");
		}
		return br;
	}

	/**
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOADPROJECTPOOLADJUST, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, BaseConsts.ONE + "");
			fileAttach.setBusType(BaseConsts.INT_23);
			fileUploadService.uploadFileList(fileList, fileAttach);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("上传文件异常[{}]", JSONObject.toJSON(request), e);
			result.setSuccess(false);
			result.setMsg("上传失败，请重试");
		}
		return result;
	}

	/**
	 * 查询附件列表
	 * 
	 * @param fileAttReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECTPOOLADJUSTFILE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectPoolAdjustFileDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<ProjectPoolAdjustFileDto> pageResult = new PageResult<ProjectPoolAdjustFileDto>();
		try {
			fileAttReqDto.setBusType(BaseConsts.INT_23);
			pageResult = projectPoolAdjustService.queryFileList(fileAttReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 删除附件
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETEPROJECTPOOLADJUSTFILE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteFileById(Integer fileId) {
		BaseResult br = new BaseResult();
		try {
			fileUploadService.deleteFileById(fileId);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(fileId), e);
			br.setSuccess(false);
			br.setMsg("删除失败，请重试");
		}
		return br;
	}

	/**
	 * 批量下载附件
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOADPROJECTPOOLADJUSTFILELIST, method = RequestMethod.GET)
	public void downLoadFileListById(FileAttachSearchReqDto fileAttReqDto, HttpServletResponse response) {
		try {
			fileUploadService.downLoadFileList(fileAttReqDto, response);
		} catch (BaseException e) {
			e.getMsg();
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
		}
	}

	/**
	 * 下载附件
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOADPROJECTPOOLADJUST, method = RequestMethod.GET)
	public void downLoadFileById(Integer fileId, HttpServletResponse response) {
		try {
			fileUploadService.viewFileById(fileId, response);
		} catch (BaseException e) {
			e.getMsg();
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileId), e);
		}
	}

}
