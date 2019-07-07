package com.scfs.web.controller.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.DistributionGoodsReqDto;
import com.scfs.domain.base.dto.resp.DistributionGoodsResDto;
import com.scfs.domain.base.entity.DistributionGoods;
import com.scfs.domain.common.dto.req.FileAttachSearchReqDto;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.po.dto.resp.PoFileAttachRespDto;
import com.scfs.domain.report.entity.PerformanceReport;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.goods.DistributionGoodsService;
import com.scfs.service.support.FileUploadService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 *  铺货商品
 *  File: DistributeGoodsController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月02日				Administrator
 *
 * </pre>
 */
@Controller
public class DistributeGoodsController extends BaseController {

	@Autowired
	private DistributionGoodsService distributionGoodsService;
	@Autowired
	private FileUploadService fileUploadService;

	private final static Logger LOGGER = LoggerFactory.getLogger(DistributeGoodsController.class);

	/**
	 * 查询铺货商品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERY_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<DistributionGoodsResDto> queryDistributionGoods(DistributionGoodsReqDto distributionGoodsReqDto) {
		PageResult<DistributionGoodsResDto> result = new PageResult<DistributionGoodsResDto>();
		try {
			result = distributionGoodsService.queryDistributionGoodsList(distributionGoodsReqDto);
		} catch (Exception e) {
			LOGGER.error("查询铺货商品信息异常[{}]", JSONObject.toJSON(distributionGoodsReqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADD_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult addDistributionGoods(@RequestBody DistributionGoodsReqDto distributionGoodsReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsService.addDistributionBaseGoods(distributionGoodsReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增铺货商品异常：[{}]", JSONObject.toJSON(distributionGoodsReqDto), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DELETE_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	BaseResult deleteDistributionGoods(DistributionGoods distributionGoods) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsService.delete(distributionGoods);
		} catch (Exception e) {
			LOGGER.error("删除铺货商品信息异常[{}]", JSONObject.toJSON(distributionGoods), e);
			result.setMsg("删除铺货商品异常，请稍后重试！");
		}
		return result;
	}

	/**
	 * 浏览铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DETAIL_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public Result<DistributionGoodsResDto> queryDistributionGoodsById(DistributionGoods distributionGoods) {
		Result<DistributionGoodsResDto> result = new Result<DistributionGoodsResDto>();
		try {
			result = distributionGoodsService.queryDistributionBaseGoodsById(distributionGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.EDIT_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public Result<DistributionGoodsResDto> editDistributionGoodsById(DistributionGoods distributionGoods) {
		Result<DistributionGoodsResDto> result = new Result<DistributionGoodsResDto>();
		try {
			result = distributionGoodsService.queryDistributionBaseGoodsById(distributionGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.SUBMIT_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitDistributionGoods(DistributionGoods distributionGoods) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsService.submit(distributionGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交铺货商品异常：[{}]", JSONObject.toJSON(result), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;

	}

	/**
	 * 更新铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.UPDATE_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateDistributionGoods(DistributionGoods distributionGoods) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsService.updateDistributionGoods(distributionGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改铺货商品异常：[{}]", JSONObject.toJSON(result), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 锁定铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.LOCK_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult lockDistributionGoodsById(DistributionGoods distributionGoods) {
		BaseResult br = new BaseResult();
		try {
			distributionGoodsService.lock(distributionGoods);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("锁定信息异常[{}]", JSONObject.toJSON(distributionGoods), e);
			br.setSuccess(false);
			br.setMsg("锁定失败，请重试");
		}
		return br;
	}

	/**
	 * 解锁铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.UNLOCK_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unlockDistributionGoods(DistributionGoods distributionGoods) {
		BaseResult br = new BaseResult();
		try {
			distributionGoodsService.unlock(distributionGoods);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("解锁信息异常[{}]", JSONObject.toJSON(distributionGoods), e);
			br.setSuccess(false);
			br.setMsg("解锁失败，请重试");
		}
		return br;
	}

	/**
	 * 下载excel模板
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DOWNLOAD_DISTRIBUTE_GOODS_TEMPLATE, method = RequestMethod.GET)
	public String downloadDistributionGoodsTemplate() {
		return "template/baseinfo/goods/distribute_template";
	}

	/**
	 * 导入excel
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.IMPORT_DISTRIBUTE_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importDistributionGoodsExcel(MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsService.importExcel(file);
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
	 * @param request
	 * @param fileAttach
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.UPLOAD_DISTRIBUTE_GOODS_FILE, method = RequestMethod.POST)
	@ResponseBody
	public Result<String> upLoadFileByCon(MultipartHttpServletRequest request, FileAttach fileAttach) {
		Result<String> result = new Result<String>();
		try {
			List<MultipartFile> fileList = request.getFiles("file");
			fileAttach.setBusType(BaseConsts.INT_35);
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
	@RequestMapping(value = BaseUrlConsts.QUERY_DISTRIBUTE_GOODS_FILE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoFileAttachRespDto> queryFileAttachByCon(FileAttachSearchReqDto fileAttReqDto) {
		PageResult<PoFileAttachRespDto> result = new PageResult<PoFileAttachRespDto>();
		try {
			// 业务类型
			fileAttReqDto.setBusType(BaseConsts.INT_35);
			result = distributionGoodsService.queryFileList(fileAttReqDto);
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
	@RequestMapping(value = BaseUrlConsts.DELETE_DISTRIBUTE_GOODS_FILE, method = RequestMethod.POST)
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
	@RequestMapping(value = BaseUrlConsts.DOWNLOAD_DISTRIBUTE_GOODS_FILE_LIST, method = RequestMethod.GET)
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
	@RequestMapping(value = BaseUrlConsts.DOWNLOAD_DISTRIBUTE_GOODS_FILE, method = RequestMethod.GET)
	public void downLoadFileById(Integer fileId, HttpServletResponse response) {
		try {
			fileUploadService.viewFileById(fileId, response);
		} catch (BaseException e) {
			LOGGER.error("下载附件异常[{}]", e.getMsg());
		} catch (Exception e) {
			LOGGER.error("下载附件异常[{}]", JSONObject.toJSON(fileId), e);
		}
	}

	/**
	 * 浏览铺货商品
	 * 
	 * @param distributionGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERY_DISTRIBUTE_GOODS_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<DistributionGoodsResDto> queryBaseGoodsListById(DistributionGoodsReqDto distributionGoodsReqDto) {
		PageResult<DistributionGoodsResDto> result = new PageResult<DistributionGoodsResDto>();
		try {
			result = distributionGoodsService.getBaseGoodsList(distributionGoodsReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 铺货商品Excel导出
	 * 
	 * @param model
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.EXCEL_DISTRIBUTE_GOODS, method = RequestMethod.GET)
	public String exportDistributeGoodsReport(ModelMap model, DistributionGoodsReqDto reqDto) {
		List<DistributionGoodsResDto> result = distributionGoodsService.queryDistributionGoodsListAll(reqDto);
		if (!CollectionUtils.isEmpty(result) && result.size() <= BaseConsts.EXPORT_EXCEL_ROWS_MAX) {
			model.addAttribute("distributionGoodsList", result);
		} else {
			model.addAttribute("distributionGoodsList", new ArrayList<PerformanceReport>());
		}
		return "export/goods/gistribution_goods_list";
	}

}
