package com.scfs.web.controller.interf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.interf.dto.PMSSupplierBindReqDto;
import com.scfs.domain.interf.dto.PMSSupplierBindResDto;
import com.scfs.domain.interf.entity.PMSSupplierBind;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.interf.PMSSupplierBindService;

@RestController
public class PMSSupplierBindController {
	private final static Logger LOGGER = LoggerFactory.getLogger(PMSSupplierBindController.class);

	@Autowired
	private PMSSupplierBindService pMSSupplierBindService;

	@RequestMapping(value = BusUrlConsts.QUERY_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PMSSupplierBindResDto> queryByCondition(PMSSupplierBindReqDto pMSSupplierBindReqDto) {
		PageResult<PMSSupplierBindResDto> pageResult = new PageResult<PMSSupplierBindResDto>();
		try {
			pageResult = pMSSupplierBindService.queryInfoByCon(pMSSupplierBindReqDto);
		} catch (BaseException e) {
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("查询PMS信息异常[{}]", JSONObject.toJSON(pMSSupplierBindReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}

	@RequestMapping(value = BusUrlConsts.EDIT_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public Result<PMSSupplierBind> edit(Integer id) {
		Result<PMSSupplierBind> baseResult = new Result<PMSSupplierBind>();
		try {
			PMSSupplierBind pMSSupplierBind = pMSSupplierBindService.edit(id);
			baseResult.setItems(pMSSupplierBind);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("编辑PMS信息异常[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("编辑异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.DETAIL_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public Result<PMSSupplierBindResDto> detail(Integer id) {
		Result<PMSSupplierBindResDto> baseResult = new Result<PMSSupplierBindResDto>();
		try {
			PMSSupplierBindResDto pMSSupplierBindResDto = pMSSupplierBindService.detail(id);
			baseResult.setItems(pMSSupplierBindResDto);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("查询PMS信息异常[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("查询异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.DELETE_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult delete(Integer id) {
		BaseResult baseResult = new BaseResult();
		try {
			pMSSupplierBindService.delete(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("删除PMS信息异常[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("删除异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.UPDATE_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult update(PMSSupplierBind pMSSupplierBind) {
		BaseResult baseResult = new BaseResult();
		try {
			pMSSupplierBindService.update(pMSSupplierBind);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("更新PMS信息异常[{}]", JSONObject.toJSON(pMSSupplierBind), e);
			baseResult.setMsg("更新异常，请稍后重试");
		}
		return baseResult;
	}

	/**
	 * 修改
	 * 
	 * @param pMSSupplierBind
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.UPDATE_PMSS_SUPPLIER_BIND_PRO, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updatePro(PMSSupplierBind pMSSupplierBind) {
		BaseResult baseResult = new BaseResult();
		try {
			pMSSupplierBindService.updatePro(pMSSupplierBind);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("更新PMS信息异常[{}]", JSONObject.toJSON(pMSSupplierBind), e);
			baseResult.setMsg("更新异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.ADD_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult add(PMSSupplierBind pMSSupplierBind) {
		BaseResult baseResult = new BaseResult();
		try {
			pMSSupplierBindService.add(pMSSupplierBind);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("新增PMS信息异常[{}]", JSONObject.toJSON(pMSSupplierBind), e);
			baseResult.setMsg("新增异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.SUBMIT_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult submit(Integer id) {
		BaseResult baseResult = new BaseResult();
		try {
			pMSSupplierBindService.submit(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("提交PMS信息异常[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("提交异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.UNBIND_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unbind(Integer id) {
		BaseResult baseResult = new BaseResult();
		try {
			pMSSupplierBindService.unbind(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("解绑PMS信息异常[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("解绑异常，请稍后重试");
		}
		return baseResult;
	}

	@RequestMapping(value = BusUrlConsts.BIND_PMSS_SUPPLIER_BIND, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult bind(Integer id) {
		BaseResult baseResult = new BaseResult();
		try {
			pMSSupplierBindService.bind(id);
		} catch (BaseException e) {
			baseResult.setMsg(e.getMsg());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error("绑定PMS信息异常[{}]", JSONObject.toJSON(id), e);
			baseResult.setMsg("绑定异常，请稍后重试");
		}
		return baseResult;
	}
}
