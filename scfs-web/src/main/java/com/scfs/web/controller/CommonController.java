package com.scfs.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.base.entity.BaseGoods;
import com.scfs.domain.base.model.Department;
import com.scfs.domain.base.model.UserPermission;
import com.scfs.domain.common.dto.req.RollbackOrderReqDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.CommonService;
import com.scfs.service.schedule.CacheRefreshJob;
import com.scfs.service.support.CacheService;

@Controller
public class CommonController extends BaseController {

	private final static Logger LOGGER = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private CacheService cacheService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CacheRefreshJob cacheRefreshJob;

	/**
	 * 获取所有下拉列表集合
	 *
	 * @param key
	 * @param pId
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYCOMMONSELECTED, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CodeValue> queryAllSelectedByKey(@RequestParam("key") String key, String pId) {
		PageResult<CodeValue> result = new PageResult<CodeValue>();
		try {
			List<CodeValue> codeValueList = commonService.queryAllSelectedByKey(key, pId);
			result.setItems(codeValueList);
		} catch (Exception e) {
			LOGGER.error("获取下key:[{}],pId:[{}]拉列表异常：", key, pId, e);
		}
		return result;
	}

	/**
	 * 当前用户是否拥有此权限URL
	 *
	 * @param url
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.PREMCOMMONSELECTED, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult isOwnPermUrl(String url) {
		BaseResult result = new BaseResult();
		try {
			boolean isAllow = commonService.isOwnPermUrl(url);
			result.setSuccess(isAllow);
		} catch (Exception e) {
			LOGGER.error("判断是否拥有URL权限【{}】异常：", url, e);
		}
		return result;
	}

	/**
	 * 适配autocomplete插件
	 *
	 * @param projectId
	 * @param term
	 *            搜索关键字
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUEYRCOMMONGOODS, method = RequestMethod.GET)
	@ResponseBody
	public PageResult<BaseGoods> queryProGoods(Integer projectId, String term) {
		PageResult<BaseGoods> result = new PageResult<BaseGoods>();
		try {
			List<BaseGoods> goodsList = commonService.queryProGoods(projectId, term);
			result.setItems(goodsList);
		} catch (Exception e) {
			LOGGER.error("查询项目下的商品异常项目ID：【{}】，商品关键字：【{}】异常：", projectId, term, e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DETAILCOMMONGOODS, method = RequestMethod.POST)
	@ResponseBody
	public Result<BaseGoods> queryGoodsByNo(Integer id) {
		Result<BaseGoods> result = new Result<BaseGoods>();
		try {
			BaseGoods goods = cacheService.getGoodsById(id);
			result.setItems(goods);
		} catch (Exception e) {
			LOGGER.error("获取商品信息错误[{}],[{}]", id, e);
			result.setMsg("获取商品信息异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.QUEYRCOMMONMENU, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<UserPermission> queryMenuPermissions() {
		PageResult<UserPermission> result = new PageResult<UserPermission>();
		List<UserPermission> userPermissions = cacheService.queryMenuPermissions();
		result.setItems(userPermissions);
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.REFRESHCOMMONCACHE, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult refreshRedisData() {
		BaseResult result = new BaseResult();
		try {
			cacheRefreshJob.refreshCache();
		} catch (Exception e) {
			LOGGER.error("刷新缓存异常：", e);
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.UPDATEPRINTNUM, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePrintNum(Integer id, Integer billType) {
		BaseResult result = new BaseResult();
		try {
			commonService.updatePrintNum(id, billType);
		} catch (BaseException e) {
			LOGGER.error("更新打印次数异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新打印次数异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.BATCHUPDATEPRINTNUM, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult batchUpdatePrintNum(@RequestParam("ids") String ids, Integer billType) {
		BaseResult result = new BaseResult();
		try {
			commonService.batchUpdatePrintNum(ids, billType);
		} catch (BaseException e) {
			LOGGER.error("更新打印次数异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("更新打印次数异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DEPARTMENT_TREE, method = RequestMethod.GET)
	@ResponseBody
	public Result<Department> getAllDepartment() {
		Result<Department> result = new Result<Department>();
		try {
			Department department = commonService.getAllDepartment();
			result.setItems(department);
		} catch (Exception e) {
			LOGGER.error("获取所有部门异常", e);
			result.setMsg("获取所有部门异常，请稍后重试!");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.DEPARTMENT_USER_TREE, method = RequestMethod.GET)
	@ResponseBody
	public Result<Department> getUserDepartments() {
		Result<Department> result = new Result<Department>();
		try {
			Department department = commonService.getUserDepartments();
			result.setItems(department);
		} catch (Exception e) {
			LOGGER.error("获取用户部门树异常", e);
			result.setMsg("获取用户部门树异常，请稍后重试!");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.COMMON_ROLLBACK, method = RequestMethod.POST)
	public BaseResult rollbackOrder(RollbackOrderReqDto rollbackOrderReqDto) {
		BaseResult result = new BaseResult();
		try {
			commonService.rollbackOrder(rollbackOrderReqDto);
		} catch (Exception e) {
			LOGGER.error("驳回单据异常", e);
			result.setMsg("驳回单据异常，请稍后重试!");
		}
		return result;
	}

}
