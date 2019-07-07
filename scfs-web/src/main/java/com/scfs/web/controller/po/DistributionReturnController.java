package com.scfs.web.controller.po;

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
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoFileAttachRespDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.po.entity.PurchaseOrderTitle;
import com.scfs.domain.po.model.PoLineModel;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.po.DistributionReturnService;
import com.scfs.service.support.FileUploadService;

@Controller
public class DistributionReturnController {
	private final static Logger LOGGER = LoggerFactory.getLogger(DistributionReturnController.class);
	@Autowired
	private DistributionReturnService distributionReturnService;
	@Autowired
	private FileUploadService fileUploadService;

	@RequestMapping(value = BusUrlConsts.QUERY_DISTRIBUTION_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoTitleRespDto> queryPoTitlesResultsByCon(PoTitleReqDto poTitleReqDto) {
		PageResult<PoTitleRespDto> result = new PageResult<PoTitleRespDto>();
		try {
			poTitleReqDto.setOrderType(3);
			result = distributionReturnService.queryPoTitlesResultsByCon(poTitleReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询铺货退货单信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg(e.getMsg());
			return result;
		} catch (Exception e) {
			LOGGER.error("查询信息异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_DISTRIBUTION_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public Result<PoTitleRespDto> detailPurchaseOrderTitle(PurchaseOrderTitle purchaseOrderTitle) {
		Result<PoTitleRespDto> result = new Result<PoTitleRespDto>();
		try {
			return distributionReturnService.queryPurchaseOrderTitleById(purchaseOrderTitle.getId());
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("浏览铺货退货单详细信息异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("浏览铺货退货单详细信息异常[{}]", purchaseOrderTitle, e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_DISTRIBUTION_RETURN_LINE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoLineModel> queryPoLinesByPoTitleId(PoTitleReqDto poTitleReqDto) {
		PageResult<PoLineModel> result = new PageResult<PoLineModel>();
		try {
			return distributionReturnService.queryDistributionLinesByPoTitleId(poTitleReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("查询铺货退货单列表异常,入参：[{}],{},{}", JSONObject.toJSON(poTitleReqDto), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("查询铺货退货单列表异常：[{}]", JSONObject.toJSON(poTitleReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_DISTRIBUTION_RETURN_TITLE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitPurchaseReturnLine(PurchaseOrderTitle purchaseOrderTitle) {
		BaseResult result = new BaseResult();
		try {
			purchaseOrderTitle.setOrderType(3);
			distributionReturnService.submitPurchaseOrderTitle(purchaseOrderTitle);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
			LOGGER.error("提交铺货退货单异常,入参：[{}],{},{}", JSONObject.toJSON(purchaseOrderTitle), e.getMsg(), e);
		} catch (Exception e) {
			LOGGER.error("提交铺货退货单异常：[{}]", JSONObject.toJSON(purchaseOrderTitle), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_DISTRIBUTION_RETURN_TITLE, method = RequestMethod.GET)
	public String exportDistributionReturnTitle(ModelMap model, PoTitleReqDto poTitleReqDto) {
		List<PoTitleRespDto> result = distributionReturnService
				.queryAllDistributionReturnTitlesResultsByCon(poTitleReqDto);
		model.addAttribute("distributionReturnList", result);
		return "export/po/distribution_return_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_DISTRIBUTION_RETURN_LINE, method = RequestMethod.GET)
	public String exportDistributionReturnLine(ModelMap model, PoTitleReqDto poTitleReqDto) {
		List<PoLineModel> result = distributionReturnService.queryAllDistributionReturnLineResultsByCon(poTitleReqDto);
		model.addAttribute("distributionReturnLineList", result);
		return "export/po/distribution_return_dtl_list";
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_DISTRIBUTION_RETURN_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportDistributionReturnCount(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = distributionReturnService.isOverDistributionReturnMaxLine(poTitleReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
		}
		return result;
	}

	@RequestMapping(value = BusUrlConsts.EXPORT_DISTRIBUTION_RETURN_LINE_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportDistributionReturnLineCount(PoTitleReqDto poTitleReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = distributionReturnService.isOverasyncDistributionReturnDtlByTitleIdMaxLine(poTitleReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", JSONObject.toJSON(poTitleReqDto), e);
		}
		return result;
	}

	/**
	 * 上传附件
	 * 
	 * @param request
	 * @param fileAttach
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPLOAD_DISTRIBUTION_RETURN_FILE, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			fileAttach.setBusType(BaseConsts.INT_31);
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
	@RequestMapping(value = BusUrlConsts.QUERY_DISTRIBUTION_RETURN_FILE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoFileAttachRespDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<PoFileAttachRespDto> result = new PageResult<PoFileAttachRespDto>();
		try {
			// 业务类型
			fileAttReqDto.setBusType(BaseConsts.INT_31);
			result = distributionReturnService.queryFileList(fileAttReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除附件
	 *
	 * @param fileId
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DELETE_DISTRIBUTION_RETURN_FILE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deleteFileById(Integer fileId) {
		BaseResult br = new BaseResult();
		try {
			fileUploadService.deleteFileById(fileId);
		} catch (BaseException e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(fileId), e);
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("删除信息异常[{}]", JSONObject.toJSON(fileId), e);
			br.setSuccess(false);
		}
		return br;
	}

	/**
	 * 批量下载附件
	 *
	 * @param fileAttReqDto
	 * @param response
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_DISTRIBUTION_RETURN_FILE_LIST, method = RequestMethod.GET)
	public void downLoadFileListById(FileAttachSearchReqDto fileAttReqDto, HttpServletResponse response) {
		try {
			fileUploadService.downLoadFileList(fileAttReqDto, response);
		} catch (BaseException e) {
			LOGGER.error("下载附件异常[{}]", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileAttReqDto), e);
		}
	}

	/**
	 * 下载附件
	 *
	 * @param fileId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DOWNLOAD_DISTRIBUTION_RETURN_FILE, method = RequestMethod.GET)
	public void downLoadFileById(Integer fileId, HttpServletResponse response) {
		try {
			fileUploadService.viewFileById(fileId, response);
		} catch (BaseException e) {
			LOGGER.error("下载附件异常[{}]", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileId), e);
		}
	}
}
