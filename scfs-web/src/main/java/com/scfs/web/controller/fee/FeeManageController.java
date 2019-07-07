package com.scfs.web.controller.fee;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.fee.dto.req.FeeManageSearchReqDto;
import com.scfs.domain.fee.dto.resp.FeeManageResDto;
import com.scfs.domain.fee.entity.FeeManage;
import com.scfs.domain.invoice.dto.resp.FeeManageFileResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.fee.FeeManageService;
import com.scfs.service.support.FileUploadService;

/**
 * <pre>
 * 	
 *  File: FeeManageController.java
 *  Description:费用管理业务
 *  TODO
 *  Date,					Who,				
 *  2017年04月13日				Administrator
 *
 * </pre>
 */
@Controller
public class FeeManageController {
	private final static Logger LOGGER = LoggerFactory.getLogger(FeeManageController.class);

	@Autowired
	private FeeManageService feeManageService;

	@Autowired
	private FileUploadService fileUploadService;

	/**
	 * 新建费用管理信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.ADD_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> createFeeManage(FeeManage feeManage) {
		Result<Integer> br = new Result<Integer>();
		try {
			int id = feeManageService.createFeeManage(feeManage);
			br.setItems(id);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新建费用管理信息异常[{}]", JSONObject.toJSON(feeManage), e);
			br.setSuccess(false);
			br.setMsg("新建费用管理失败，请重试");
		}
		return br;
	}

	/***
	 * 修改费用管理信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateFeeManage(FeeManage feeManage) {
		BaseResult result = new BaseResult();
		try {
			result = feeManageService.updateFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改费用管理开票失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("修改费用管理开票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 编辑费用管理信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EDIT_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeManageResDto> editFeeManage(FeeManage feeManage) {
		Result<FeeManageResDto> result = new Result<FeeManageResDto>();
		try {
			result = feeManageService.editFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("编辑境外开票失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("编辑境外开票异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 浏览费用管理信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public Result<FeeManageResDto> detailFeeManage(FeeManage feeManage) {
		Result<FeeManageResDto> result = new Result<FeeManageResDto>();
		try {
			result = feeManageService.editFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("浏览费用管理失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("浏览费用管理异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 删除费用管理信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteFeeManage(FeeManage feeManage) {
		BaseResult result = new BaseResult();
		try {
			result = feeManageService.deleteFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除费用管理失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("删除费用管理异常，请稍后重试");
		}
		return result;
	}

	/***
	 * 提交费用管理信息
	 * 
	 * @param feeManage
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitFeeManage(FeeManage feeManage) {
		BaseResult result = new BaseResult();
		try {
			result = feeManageService.sumitFeeManageById(feeManage);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交费用管理失败[{}]", JSONObject.toJSON(feeManage), e);
			result.setMsg("提交费用管理异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 批量提交费用管理信息
	 * 
	 * @param feeManageReq
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.SUBMIT_FEE_MANAGE_ALL, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitFeeManageAll(FeeManageSearchReqDto feeManageReq) {
		BaseResult result = new BaseResult();
		try {
			result = feeManageService.sumitFeeManageByIds(feeManageReq);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量提交费用管理失败[{}]", JSONObject.toJSON(feeManageReq), e);
			result.setMsg("批量提交费用管理异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 费用管理列表信息
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeManageResDto> queryFeeManageResultsByCon(FeeManageSearchReqDto searchreqDto) {
		PageResult<FeeManageResDto> pageResult = new PageResult<FeeManageResDto>();
		try {
			pageResult = feeManageService.queryFeeManagesResultsByCon(searchreqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询费用管理失败[{}]", JSONObject.toJSON(searchreqDto), e);
			pageResult.setMsg("查询费用管理异常，请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 导出人工费用信息excel
	 * 
	 * @param model
	 * @param searchreqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FEE_MANAGE, method = RequestMethod.GET)
	public String exportFeeManageExcel(ModelMap model, FeeManageSearchReqDto searchreqDto) {
		searchreqDto.setFeeType(BaseConsts.FOUR);
		List<FeeManageResDto> feeManageList = feeManageService.queryFeeManageResultsByEx(searchreqDto).getItems();
		model.addAttribute("feeManageList", feeManageList);
		return "export/fee/feeManage_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_FEE_MANAGE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportFeeManageByCount(FeeManageSearchReqDto searchreqDto) {
		BaseResult result = new BaseResult();
		try {
			searchreqDto.setFeeType(BaseConsts.FOUR);
			boolean isOver = feeManageService.isFeeManageMaxLine(searchreqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", searchreqDto, e);
		}
		return result;
	}

	/**
	 * 下载费用管理导入excel模板
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FEE_FILE_MANAGE, method = RequestMethod.GET)
	public String downloadFeeManageTemplate() {
		return "template/fee/feeManage_template";
	}

	/**
	 * 导入费用管理excel
	 *
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.IMPORT_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importFeeManageExcel(MultipartFile file) {
		BaseResult result = new BaseResult();
		try {
			feeManageService.importFeeManageExcel(file, BaseConsts.FOUR);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
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
	@RequestMapping(value = BusUrlConsts.UPLOAD_FEE_MANAGE, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.QUERY_FILE_FEE_MANAGE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FeeManageFileResDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<FeeManageFileResDto> pageResult = new PageResult<FeeManageFileResDto>();
		try {
			pageResult = feeManageService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BusUrlConsts.DELETE_FILE_FEE_MANAGE, method = RequestMethod.POST)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_LIST_FEE_MANAGE, method = RequestMethod.GET)
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
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_FEE_MANAGE, method = RequestMethod.GET)
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
