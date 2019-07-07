package com.scfs.web.controller.base;

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
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.MatterManageReqDto;
import com.scfs.domain.base.dto.resp.MatterManageFileResDto;
import com.scfs.domain.base.dto.resp.MatterManageResDto;
import com.scfs.domain.base.dto.resp.MatterServiceResDto;
import com.scfs.domain.base.entity.MatterManage;
import com.scfs.domain.base.entity.MatterService;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.customer.MatterManageService;
import com.scfs.service.base.customer.MatterServiceService;
import com.scfs.service.support.FileUploadService;

/**
 * <pre>
 *  事项管理
 *  File: MatterManageController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年07月28日				Administrator
 *
 * </pre>
 */
@Controller
public class MatterManageController {
	@Autowired
	private MatterManageService matterManageService;
	@Autowired
	private MatterServiceService matterServiceService;
	@Autowired
	private FileUploadService fileUploadService;

	private final static Logger LOGGER = LoggerFactory.getLogger(MatterManageController.class);

	/**
	 * 新建
	 * 
	 * @param MatterManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createMatterManage(MatterManage matterManage) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = matterManageService.insertMatterManage(matterManage);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建事项管理异常[{}]", JSONObject.toJSON(matterManage), e);
			br.setSuccess(false);
			br.setMsg("新建客户维护失败，请重试");
		}
		return br;
	}

	/**
	 * 修改信息
	 * 
	 * @param MatterManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateMatterManage(MatterManage matterManage) {
		BaseResult result = new BaseResult();
		try {
			result = matterManageService.updateMatterManage(matterManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改事项管理失败[{}]", JSONObject.toJSON(matterManage), e);
			result.setMsg("修改事项管理异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑事项管理
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<MatterManageResDto> editMatterManage(MatterManage matterManage) {
		Result<MatterManageResDto> result = new Result<MatterManageResDto>();
		try {
			result = matterManageService.queryMatterManageById(matterManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑事项管理失败[{}]", JSONObject.toJSON(matterManage), e);
			result.setMsg("编辑事项管理异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 浏览事项管理
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<MatterManageResDto> detailMatterManage(MatterManage matterManage) {
		Result<MatterManageResDto> result = new Result<MatterManageResDto>();
		try {
			result = matterManageService.queryMatterManageById(matterManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览事项管理失败[{}]", JSONObject.toJSON(matterManage), e);
			result.setMsg("浏览事项管理异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除信息
	 * 
	 * @param senderManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteMatterManage(MatterManageReqDto reqDto) {
		BaseResult result = new BaseResult();
		try {
			result = matterManageService.deleteMatterManage(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除事项管理失败[{}]", JSONObject.toJSON(reqDto), e);
			result.setMsg("删除事项管理异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交信息
	 * 
	 * @param matterManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitMatterManage(MatterManage matterManage) {
		BaseResult result = new BaseResult();
		try {
			result = matterManageService.submitMatterManage(matterManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交事项管理失败[{}]", JSONObject.toJSON(matterManage), e);
			result.setMsg("提交事项管理异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<MatterManageResDto> queryMatterManage(MatterManageReqDto reqDto) {
		PageResult<MatterManageResDto> result = new PageResult<MatterManageResDto>();
		try {
			result = matterManageService.queryMatterManageResultsByCon(reqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询事项管理信息异常[{}]", JSONObject.toJSON(reqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑服务要求
	 * 
	 * @param matterService
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_MATTER_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public Result<MatterServiceResDto> editMatterService(MatterService matterService) {
		Result<MatterServiceResDto> result = new Result<MatterServiceResDto>();
		try {
			result = matterServiceService.queryMatterServiceById(matterService);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑服务要求失败[{}]", JSONObject.toJSON(matterService), e);
			result.setMsg("编辑服务要求异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加或修改服务要求
	 * 
	 * @param MatterManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_MATTER_SERVICE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult saveOrUpdateMatterService(MatterService matterService) {
		BaseResult result = new BaseResult();
		try {
			result = matterServiceService.saveOrUpdateMatterService(matterService);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("处理服务要求失败[{}]", JSONObject.toJSON(matterService), e);
			result.setMsg("处理服务要求异常，请稍后重试");
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
	@RequestMapping(value = BusUrlConsts.UPLOAD_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_MATTER_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<MatterManageFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<MatterManageFileResDto> pageResult = new PageResult<MatterManageFileResDto>();
		try {
			pageResult = matterManageService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_MATTER_MANAGE, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_MATTER_MANAGE, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_MATTER_MANAGE, method = RequestMethod.GET)
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
