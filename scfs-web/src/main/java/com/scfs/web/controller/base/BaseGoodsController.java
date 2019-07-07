package com.scfs.web.controller.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.base.dto.req.BaseGoodsReqDto;
import com.scfs.domain.base.dto.resp.BaseGoodsResDto;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.common.entity.FileAttach;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.base.goods.BaseGoodsService;
import com.scfs.web.controller.BaseController;

/**
 * 
 * @author Administrator
 *
 */
@Controller
public class BaseGoodsController extends BaseController {

	@Autowired
	private BaseGoodsService baseGoodsService;

	private final static Logger LOGGER = LoggerFactory.getLogger(BaseGoodsController.class);

	/**
	 * 查询商品列表
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYGOODS, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<BaseGoodsResDto> queryGoods(BaseGoodsReqDto baseGoodsReqDto) {
		PageResult<BaseGoodsResDto> result = new PageResult<BaseGoodsResDto>();
		try {
			result = baseGoodsService.getBaseGoodsList(baseGoodsReqDto);
		} catch (Exception e) {
			LOGGER.error("查询商品信息异常[{}]", JSONObject.toJSON(baseGoodsReqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 添加商品
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.ADDGOODS, method = RequestMethod.POST)
	@ResponseBody
	public Result<Integer> addGoods(BaseGoods baseGoods) {
		Result<Integer> result = new Result<Integer>();
		try {
			Integer id = baseGoodsService.addBaseGoods(baseGoods);
			result.setItems(id);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("新增采购单异常：[{}]", JSONObject.toJSON(baseGoods), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 删除商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DELETEGOODS, method = RequestMethod.POST)
	@ResponseBody
	BaseResult deleteGoods(BaseGoods baseGoods) {
		BaseResult result = new BaseResult();
		try {
			baseGoodsService.delete(baseGoods);
		} catch (Exception e) {
			LOGGER.error("删除权限信息异常[{}]", JSONObject.toJSON(baseGoods), e);
			result.setMsg("删除权限异常，请稍后重试！");
		}
		return result;
	}

	/**
	 * 浏览商品
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.DETAILGOODS, method = RequestMethod.POST)
	@ResponseBody
	public Object queryGoodsById(BaseGoods baseGoods) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		try {
			result = baseGoodsService.queryBaseGoodsById(baseGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 编辑商品
	 * 
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.EDITGOODS, method = RequestMethod.POST)
	@ResponseBody
	public Result<BaseGoods> editGoodsById(BaseGoods baseGoods) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		try {
			result = baseGoodsService.queryBaseGoodsById(baseGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 提交商品
	 * 
	 * @param baseGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.SUBMITGOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submitGoods(BaseGoods baseGoods) {
		BaseResult result = new BaseResult();
		try {
			baseGoodsService.submit(baseGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("提交商品异常：[{}]", JSONObject.toJSON(result), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;

	}

	/**
	 * 更新商品信息
	 * 
	 * @param baseGoods
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.UPDATEGOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateGoods(BaseGoods baseGoods) {
		BaseResult result = new BaseResult();
		try {
			baseGoodsService.updateGoods(baseGoods);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("修改商品异常：[{}]", JSONObject.toJSON(result), e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/** 锁定商品 **/
	@RequestMapping(value = BaseUrlConsts.LOCKGOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult lockGoodsById(BaseGoods baseGoods) {
		BaseResult br = new BaseResult();
		try {
			baseGoodsService.lock(baseGoods);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("锁定信息异常[{}]", JSONObject.toJSON(baseGoods), e);
			br.setSuccess(false);
			br.setMsg("锁定失败，请重试");
		}
		return br;
	}

	/** 解锁商品 **/
	@RequestMapping(value = BaseUrlConsts.UNLOCKGOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unlockGoods(BaseGoods baseGoods) {
		BaseResult br = new BaseResult();
		try {
			baseGoodsService.unlock(baseGoods);
		} catch (BaseException e) {
			br.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("解锁信息异常[{}]", JSONObject.toJSON(baseGoods), e);
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
	@RequestMapping(value = BaseUrlConsts.DOWNLOAD_GOODS_TEMPLATE, method = RequestMethod.GET)
	public String downloadGoodsTemplate() {
		return "template/baseinfo/goods/goods_template";
	}

	/**
	 * 导入excel
	 * 
	 * @param importFile
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.IMPORT_GOODS, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult importGoodsExcel(MultipartFile file, FileAttach fileAttach) {
		BaseResult result = new BaseResult();
		try {
			baseGoodsService.importExcel(file);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			result.setMsg("导入异常，请稍后再试！");
			LOGGER.error("导入异常，请稍后再试！", e);
		}
		return result;
	}

}
