package com.scfs.web.controller.project;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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
import com.scfs.domain.project.dto.req.ProjectItemSearchReqDto;
import com.scfs.domain.project.dto.resp.ProjectItemResDto;
import com.scfs.domain.project.entity.ProjectItem;
import com.scfs.domain.project.entity.ProjectItemFileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.project.ProjectItemService;
import com.scfs.service.support.FileUploadService;
import com.scfs.service.support.ServiceSupport;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: ProjectItemController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年10月18日			cuichao
 *
 * </pre>
 */
@Controller
public class ProjectItemController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(ProjectItemController.class);

	@Autowired
	private ProjectItemService projectService;
	@Autowired
	private FileUploadService fileUploadService;

	/**
	 * 查询条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERYPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectItemResDto> queryProjectResultsByCon(ProjectItemSearchReqDto projectItem) {
		PageResult<ProjectItemResDto> pageResult = new PageResult<ProjectItemResDto>();
		try {
			pageResult = projectService.queryProjectItemResultsByCon(projectItem);

		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(projectItem), e);
			pageResult.setMsg("查询异常，请稍后重试");
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 新建条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADDPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createProjectItem(@RequestBody ProjectItem projectItem) {
		Result<Integer> result = new Result<Integer>();
		try {
			result = projectService.createProjectItem(projectItem);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("插入信息异常[{}]", JSONObject.toJSON(projectItem), e);
			result.setSuccess(false);
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
	@RequestMapping(value = BusUrlConsts.UPDATEPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateProjectItemById(@RequestBody ProjectItem projectItem) {
		BaseResult br = new BaseResult();
		try {
			projectService.updateProjectItemById(projectItem);
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
	@RequestMapping(value = BusUrlConsts.DETAILPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectItem> detailProjectItemById(ProjectItem projectItem) {
		Result<ProjectItem> result = new Result<ProjectItem>();
		try {
			ProjectItem vo = projectService.detailProjectItemById(projectItem);
			result.setItems(vo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(projectItem), e);
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
	@RequestMapping(value = BusUrlConsts.EDITPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectItem> editProjectItemById(ProjectItem projectItem) {
		Result<ProjectItem> result = new Result<ProjectItem>();
		try {
			ProjectItem vo = projectService.editProjectItemById(projectItem);
			result.setItems(vo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询项目信息异常[{}]", JSONObject.toJSON(projectItem), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMITPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitProjectItemById(ProjectItem projectItem) {
		BaseResult br = new BaseResult();
		try {
			projectService.submitProjectItemById(projectItem);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交信息异常[{}]", JSONObject.toJSON(projectItem), e);
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
	@RequestMapping(value = BusUrlConsts.DELETEPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteProjectItemById(ProjectItem projectItem) {
		BaseResult br = new BaseResult();
		try {
			projectService.deleteProjectItemById(projectItem);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(projectItem), e);
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
	@RequestMapping(value = BusUrlConsts.LOCKPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult lockProjectItemById(ProjectItem projectItem) {
		BaseResult br = new BaseResult();
		try {
			projectService.lockProjectItemById(projectItem);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("锁定信息异常[{}]", JSONObject.toJSON(projectItem), e);
			br.setSuccess(false);
			br.setMsg("锁定失败，请重试");
		}
		return br;
	}

	/**
	 * 解锁条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UNLOCKPROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unlockProjectItemById(ProjectItem projectItem) {
		BaseResult br = new BaseResult();
		try {
			projectService.unlockProjectItemById(projectItem);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("解锁信息异常[{}]", JSONObject.toJSON(projectItem), e);
			br.setSuccess(false);
			br.setMsg("解锁失败，请重试");
		}
		return br;
	}

	/**
	 * 下拉框查询项目下条款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SELECTEDROJECTITEM, method = RequestMethod.POST)
	@ResponseBody
	public Result<ProjectItem> getProjectItemByProjectId(ProjectItem projectItem) {
		Result<ProjectItem> result = new Result<ProjectItem>();
		try {
			ProjectItem vo = projectService.getProjectItemByProjectId(projectItem.getProjectId());
			result.setItems(vo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询项目条款信息异常[{}]", JSONObject.toJSON(projectItem), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 上传附件
	 * 
	 * @param MultipartFile
	 *            file, FileAttach fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOADFILEITEM, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			ServiceSupport.getValueByBizCode(BizCodeConsts.FILE_BUS_TYPE, BaseConsts.ONE + "");
			fileAttach.setBusType(1);
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
	@RequestMapping(value = BusUrlConsts.QUERYFILEITEM, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ProjectItemFileAttach> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<ProjectItemFileAttach> pageResult = new PageResult<ProjectItemFileAttach>();
		try {
			fileAttReqDto.setBusType(BaseConsts.ONE);
			pageResult = projectService.queryFileList(fileAttReqDto);

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
	@RequestMapping(value = BusUrlConsts.DELETEFILEITEM, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOADFILELIST, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOADFILEITEM, method = RequestMethod.GET)
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
