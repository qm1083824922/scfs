package com.scfs.web.controller.fi;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BusUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.fi.dto.req.FundPoolReqDto;
import com.scfs.domain.fi.dto.resp.FundPoolResDto;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.common.RefreshReceiptPoolService;
import com.scfs.service.fi.ReceiptFundPoolService;
import com.scfs.service.fi.ReceiptPoolAssestService;
import com.scfs.service.fi.ReceiptPoolService;

/**
 * <pre>
 * 
 *  File: PayFundPoolController.java
 *  Description: 融资池
 *  TODO
 *  Date,					Who,				
 *  2017年06月08日			Administrator
 *
 * </pre>
 */
@Controller
public class fundPoolController {

	private final static Logger LOGGER = LoggerFactory.getLogger(fundPoolController.class);

	@Autowired
	private ReceiptPoolService receiptPoolService;
	@Autowired
	private ReceiptFundPoolService receiptFundPoolService;
	@Autowired
	private ReceiptPoolAssestService receiptPoolAssestService;
	@Autowired
	private RefreshReceiptPoolService refreshReceiptPoolService;

	/**
	 * 查询融资池列表信息
	 * 
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.QUERY_FUND_POOL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FundPoolResDto> queryReceiptResultByCon(FundPoolReqDto poolReqDto) {
		PageResult<FundPoolResDto> pageResult = new PageResult<FundPoolResDto>();
		try {
			pageResult = receiptPoolService.queryRecPoolResultByCon(poolReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询融资池列表失败[{}]", JSONObject.toJSON(poolReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询融资池列表失败[{}]: {}", JSONObject.toJSON(poolReqDto), e);
			pageResult.setMsg("查询融资池列表失败,请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 融资池基本信息的浏览
	 * 
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_FUND_POOL, method = RequestMethod.POST)
	@ResponseBody
	public Result<FundPoolResDto> queryReceiptPoolResultById(FundPoolReqDto poolReqDto) {
		Result<FundPoolResDto> result = new Result<FundPoolResDto>();
		try {
			result = receiptPoolService.queryReceiptPoolResultByid(poolReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询融资池明细数据失败[{}]", JSONObject.toJSON(poolReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询融资池数据明细异常[{}]", JSONObject.toJSON(poolReqDto), e);
			result.setSuccess(false);
		}
		return result;
	}

	/**
	 * 融资池明细查询
	 * 
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_FUND_POOL_DTL, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FundPoolResDto> queryFundPoolDtlResultByCon(FundPoolReqDto poolReqDto) {
		PageResult<FundPoolResDto> pageResult = new PageResult<FundPoolResDto>();
		try {
			pageResult = receiptPoolService.queryFundPoolDtlResultByCon(poolReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询融资池明细失败[{}]", JSONObject.toJSON(poolReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询融资池明细异常[{}]", JSONObject.toJSON(poolReqDto), e);
			pageResult.setSuccess(false);
		}
		return pageResult;
	}

	/**
	 * 根据融资池ID查询融资池明细
	 * 
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_FUND_POOL_FUND, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FundPoolResDto> queryReceiptFundDtlResulByCon(FundPoolReqDto poolReqDto) {
		PageResult<FundPoolResDto> pageResult = new PageResult<FundPoolResDto>();
		try {
			pageResult = receiptFundPoolService.queryPoolFundResultByCon(poolReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询融资池融资明细失败[{}]", JSONObject.toJSON(poolReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询融资池融资明细失败[{}]: {}", JSONObject.toJSON(poolReqDto), e);
			pageResult.setMsg("查询融资池融资明细失败,请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 根据融资池ID查询资产信息列表
	 * 
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.DETAIL_FUND_POOL_ASSEST, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<FundPoolResDto> queryFundPoolAssestResultByCon(FundPoolReqDto poolReqDto) {
		PageResult<FundPoolResDto> pageResult = new PageResult<FundPoolResDto>();
		try {
			pageResult = receiptPoolAssestService.queryFundPoolAssestResultByCon(poolReqDto);
		} catch (BaseException e) {
			LOGGER.error("查询融资池资产明细失败[{}]", JSONObject.toJSON(poolReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询融资池资产明细失败[{}]: {}", JSONObject.toJSON(poolReqDto), e);
			pageResult.setMsg("查询融资池资产明细失败,请稍后重试");
		}
		return pageResult;
	}

	/**
	 * 刷新融资池数据
	 * 
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.REFRESH_RECEIPT_POOL, method = RequestMethod.GET)
	@ResponseBody
	public BaseResult refreshPoolAssetDtl() {
		BaseResult result = new BaseResult();
		try {
			refreshReceiptPoolService.refreshReceiptPool();
		} catch (BaseException e) {
			LOGGER.error("刷新融资池数据异常", e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("刷新融资池数据异常", e);
			result.setMsg("系统异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 融资池明细数据的导出
	 * 
	 * @param model
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUND_POOL_DTL, method = RequestMethod.GET)
	public String exportReceiptPoolDtlExcel(ModelMap model, FundPoolReqDto poolReqDto) {
		List<FundPoolResDto> fundPoolResDtos = receiptPoolService.queryReceiptPoolDtltResultsByEx(poolReqDto);
		model.addAttribute("receiptPoolDtlList", fundPoolResDtos);
		return "export/pool/receiptPoolDtl_list";
	}

	/**
	 * 融资池明细数据导出
	 * 
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUND_POOL_DTL_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportReceiptPoolDtlCount(FundPoolReqDto poolReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = receiptPoolService.isOverasyncMaxLine(poolReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", poolReqDto, e);
		}
		return result;
	}

	/**
	 * 融资池资产明细数据的导出
	 * 
	 * @param model
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUND_POOL_ASSEST, method = RequestMethod.GET)
	public String exportReceiptPoolAssestExcel(ModelMap model, FundPoolReqDto poolReqDto) {
		List<FundPoolResDto> fundPoolResDtos = receiptPoolAssestService.queryReceiptPoolFundResultsByEx(poolReqDto);
		model.addAttribute("receiptPoolAssestList", fundPoolResDtos);
		return "export/pool/receiptPoolAssest_list";
	}

	/**
	 * 融资池资产明细数据导出
	 * 
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUND_POOL_ASSEST_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportReceiptPoolAssestCount(FundPoolReqDto poolReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = receiptPoolAssestService.isOverasyncMaxLine(poolReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", poolReqDto, e);
		}
		return result;
	}

	/**
	 * 融资池资金明细数据的导出
	 * 
	 * @param model
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUND_POOL_FUND, method = RequestMethod.GET)
	public String exportReceiptPoolFundExcel(ModelMap model, FundPoolReqDto poolReqDto) {
		List<FundPoolResDto> fundPoolResDtos = receiptFundPoolService.queryReceiptPoolFundResultsByEx(poolReqDto);
		model.addAttribute("receiptPoolFundList", fundPoolResDtos);
		return "export/pool/receiptPoolFund_list";
	}

	/**
	 * 融资池资金明细数据导出
	 * 
	 * @param poolReqDto
	 * @return
	 */
	@RequestMapping(value = BusUrlConsts.EXPORT_FUND_POOL_FUND_COUNT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult exportReceiptPoolFundCount(FundPoolReqDto poolReqDto) {
		BaseResult result = new BaseResult();
		try {
			boolean isOver = receiptFundPoolService.isOverasyncMaxLine(poolReqDto);
			if (isOver) {
				result.setMsg("导出excel条数过多，已经在后台导出，请在exel下载中查看！");
			}
		} catch (Exception e) {
			result.setMsg("系统异常，请稍后重试！");
			LOGGER.error("导出异常[{}]", poolReqDto, e);
		}
		return result;
	}
}
