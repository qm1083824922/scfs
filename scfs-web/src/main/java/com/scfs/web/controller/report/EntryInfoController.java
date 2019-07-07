package com.scfs.web.controller.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.fi.dto.req.ReceiveSearchReqDto;
import com.scfs.domain.fi.dto.resp.ReceiveResDto;
import com.scfs.domain.logistics.dto.req.StlSearchReqDto;
import com.scfs.domain.logistics.dto.resp.StlResDto;
import com.scfs.domain.po.dto.req.PoTitleReqDto;
import com.scfs.domain.po.dto.resp.PoTitleRespDto;
import com.scfs.domain.report.req.CapitalTurnoverReqDto;
import com.scfs.domain.report.resp.CapitalTurnoverResDto;
import com.scfs.domain.report.resp.EntryInfoResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.report.CapitalTurnoverService;
import com.scfs.service.report.EntryInfoService;

/**
 * <pre>
 * 
 *  File: EntryInfoController.java
 *  Description: 首页相关数据统计
 *  TODO
 *  Date,					Who,				
 *  2017年06月28日				Administrator
 *
 * </pre>
 */
@Controller
public class EntryInfoController {
	private final static Logger LOGGER = LoggerFactory.getLogger(EntryInfoController.class);

	@Autowired
	private EntryInfoService entryInfoService;
	@Autowired
	private CapitalTurnoverService capitalTurnoverService;

	/**
	 * 获取业务指标信息
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_ENTRY_INFO, method = RequestMethod.POST)
	@ResponseBody
	public Result<EntryInfoResDto> queryEntriInfo() {
		Result<EntryInfoResDto> result = new Result<EntryInfoResDto>();
		try {
			result = entryInfoService.queryEntriInfoDetail();
		} catch (BaseException e) {
			LOGGER.error("获取业务指标信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取业务指标信息失败[{}]", null, e);
			result.setMsg("获取业务指标信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取资金周转率明细信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_CAPITAL_TURNOVER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<CapitalTurnoverResDto> queryCapitalTurnover(CapitalTurnoverReqDto reqDto) {
		PageResult<CapitalTurnoverResDto> result = new PageResult<CapitalTurnoverResDto>();
		try {
			result = capitalTurnoverService.queryCapitalTurnoverResultsByCon(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取资金周转率明细信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取资金周转率明细信息失败[{}]", null, e);
			result.setMsg("获取资金周转率明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 待到货PO单信息
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_POWAIT_ORDER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<PoTitleRespDto> queryPoWaitOrderResult(PoTitleReqDto reqDto) {
		PageResult<PoTitleRespDto> result = new PageResult<PoTitleRespDto>();
		try {
			result = entryInfoService.queryPoWaitOrderResult(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取待到货PO单明细信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取待到货PO单明细信息失败[{}]", null, e);
			result.setMsg("获取待到货PO单明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 在仓库存
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_INSENATE_STL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlResDto> queryInSenateStlResult(StlSearchReqDto reqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		try {
			result = entryInfoService.queryInSenateStlResult(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取在参库存明细信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取在参库存明细信息失败[{}]", null, e);
			result.setMsg("获取在参库存明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 平均库龄
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_AVG_STLAGE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlResDto> queryAvgStlAgeResult(StlSearchReqDto reqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		try {
			result = entryInfoService.queryAvgStlAgeResult(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取平均库龄明细信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取平均库龄明细信息失败[{}]", null, e);
			result.setMsg("获取平均库龄明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 超期库存
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_OVER_STLAGE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlResDto> queryOverStlAgeResult(StlSearchReqDto reqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		try {
			result = entryInfoService.queryStlByOverAge(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取平均库龄明细信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取平均库龄明细信息失败[{}]", null, e);
			result.setMsg("获取平均库龄明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 超期应收
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_OVER_RECEIVE, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<ReceiveResDto> queryOverDayReceive(ReceiveSearchReqDto reqDto) {
		PageResult<ReceiveResDto> result = new PageResult<ReceiveResDto>();
		try {
			result = entryInfoService.queryOverDayReceive(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取超期应收明细信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取超期应收明细信息失败[{}]", null, e);
			result.setMsg("获取超期应收明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 动销滞销风险
	 * 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_RISK_STL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<StlResDto> queryRiskStlResult(StlSearchReqDto reqDto) {
		PageResult<StlResDto> result = new PageResult<StlResDto>();
		try {
			result = entryInfoService.queryRiskStlResult(reqDto);
		} catch (BaseException e) {
			LOGGER.error("获取动销滞销风险明细信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取动销滞销风险明细信息失败[{}]", null, e);
			result.setMsg("获取动销滞销风险明细信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取资金资产信息信息
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_ENTRY_POOL, method = RequestMethod.POST)
	@ResponseBody
	public Result<EntryInfoResDto> queryFundPool(String businName) {
		Result<EntryInfoResDto> result = new Result<EntryInfoResDto>();
		try {
			result = entryInfoService.queryFundPool(businName);
		} catch (BaseException e) {
			LOGGER.error("获取资金资产信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取资金资产信息失败[{}]", null, e);
			result.setMsg("获取资金资产信息异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 获取比例
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_ENTRY_POOL_LIST, method = RequestMethod.POST)
	@ResponseBody
	public Result<EntryInfoResDto> queryFundPoolList() {
		Result<EntryInfoResDto> result = new Result<EntryInfoResDto>();
		try {
			result = entryInfoService.queryFundPoolList();
		} catch (BaseException e) {
			LOGGER.error("获取资金池信息失败[{}]", null, e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("获取资金池信息失败[{}]", null, e);
			result.setMsg("获取资金池信息异常，请稍后重试");
		}
		return result;
	}
}
