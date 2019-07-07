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
import com.scfs.domain.audit.model.MatterManageAuditInfo;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.MatterManageAuditService;

/**
 * <pre>
 * 
 *  File: MatterManageAuditController.java
 *  Description:事项管理审核
 *  TODO
 *  Date,					Who,				
 *  2017年08月08日				Administrator
 *
 * </pre>
 */
@RestController
public class MatterManageAuditController {
	private final static Logger LOGGER = LoggerFactory.getLogger(MatterManageAuditController.class);

	@Autowired
	private MatterManageAuditService matterManageAuditService;

	/** 查询信息 **/
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public Result<MatterManageAuditInfo> queryMatterManageAuditInfo(Integer matterId) {
		Result<MatterManageAuditInfo> result = new Result<MatterManageAuditInfo>();
		try {
			result = matterManageAuditService.queryMatterManageAuditInfo(matterId);
		} catch (BaseException e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(matterId), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(matterId), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 业务审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_BUSI_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passBusiAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.passBusiAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理业务审核审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理业务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理业务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 商务审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_BIZ_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passBizAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.passBizAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理商务审核审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理商务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理商务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 部门主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_DEPT_MANAGE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passDeptManageAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.passDeptManageAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理部门主管审核审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理部门主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理部门主管审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 法务审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_JUSTICE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passJusticeAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.passJusticeAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理法务审核审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理法务审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理法务审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 财务主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_FINANCE_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passFinanceAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.passFinanceAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理财务主管审核审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理财务主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理财务主管审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 风控主管审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_RISK_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passRiskAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.passRiskAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理风控主管审核审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理风控主管审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理风控主管审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 总经理审核通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_BOSS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult passBossAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.passBossAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理总经理审核审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理总经理审核异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理总经理审核异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核不通过
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_UNPASS_AUDIT, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult unPassAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.unPassAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理审核不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理审核不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核转交
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_DELIVER_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult deliverAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.deliverAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理转交不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理转交不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 审核加签
	 * 
	 * @param poAuditReqDto
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_SIGH_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult sighAudit(ProjectItemReqDto poAuditReqDto) {
		BaseResult result = new BaseResult();
		try {
			matterManageAuditService.sighAudit(poAuditReqDto);
		} catch (BaseException e) {
			LOGGER.error("事项管理加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("事项管理加签不通过异常：【{}】,{}", JSONObject.toJSON(poAuditReqDto), e);
			result.setMsg("事项管理加签不通过异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 查询审核信息
	 * 
	 * @param projectItem
	 * @return
	 */
	@RequestMapping(value = AuditUrlConsts.MATTER_MANAGE_AUDITFLOW_AUDIT_QUERY, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditFlowsResDto> queryMatterManageAuditFlowsResults(ProjectItemReqDto poAuditReqDto) {
		PageResult<AuditFlowsResDto> pageResult = new PageResult<AuditFlowsResDto>();
		try {
			pageResult = matterManageAuditService.queryAuditFlows(poAuditReqDto.getProjectItemId());
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
