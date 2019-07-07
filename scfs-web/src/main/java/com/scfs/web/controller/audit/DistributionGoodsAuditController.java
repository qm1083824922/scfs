package com.scfs.web.controller.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.AuditUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseResult;
import com.scfs.domain.audit.dto.req.ProjectItemReqDto;
import com.scfs.domain.audit.dto.resp.AuditFlowsResDto;
import com.scfs.domain.audit.model.DistributionGoodsAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.DistributionGoodsAuditService;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 *  铺货商品审核信息
 *  File: DistributionGoodsAuditController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2017年05月03日				Administrator
 *
 * </pre>
 */
@RestController
public class DistributionGoodsAuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(DistributionGoodsAuditController.class);

	@Autowired
	DistributionGoodsAuditService distributionGoodsAuditService;

	/** 查询信息 **/
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_INFO_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<DistributionGoodsAuditInfo> queryOverseasAuditInfo(Integer goodsId) {
		Result<DistributionGoodsAuditInfo> result = new Result<DistributionGoodsAuditInfo>();
		try {
			result = distributionGoodsAuditService.queryOverseasAuditInfo(goodsId);
		} catch (BaseException e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(goodsId), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(goodsId), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 事业部审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_CAREER_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passCareerAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsAuditService.passCareerAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("铺货商品事业部审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("铺货商品事业部审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("铺货商品事业部审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 采购审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_PURCHASE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passPurchaseAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsAuditService.passPurchaseAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("铺货商品采购审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("铺货商品采购审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("铺货商品采购审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 供应链小组审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_GROUP_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passSupplyChainGroupAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsAuditService.passSupplyChainGroupAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("铺货商品供应链小组审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("铺货商品供应链小组审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("铺货商品供应链小组审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 供应链服务部审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_SERVICE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passSupplyChainServiceAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsAuditService.passSupplyChainServiceAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("铺货商品供应链服务部审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("铺货商品供应链服务部审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("铺货商品供应链服务部审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 风控审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_RISK_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passRiskAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsAuditService.passRiskAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("铺货商品风控审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("铺货商品风控审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("铺货商品单风控审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsAuditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("铺货商品审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("铺货商品审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("铺货商品审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsAuditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("铺货商品转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("铺货商品转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("铺货商品转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			distributionGoodsAuditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("铺货商品转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("铺货商品加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("铺货商品加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询付款
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.DISTRIBUTE_GOODS_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryProjectItemAuditFlowsResults(ProjectItemReqDto poAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = distributionGoodsAuditService.queryAuditFlows(poAuditReqDto.getProjectItemId());
		} catch (BaseException e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(poAuditReqDto), e);
			pageResult.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询异常[{}]", JSONObject.toJSON(poAuditReqDto), e);
			pageResult.setMsg("查询异常，请稍后重试");
		}
		return pageResult;
	}
}
