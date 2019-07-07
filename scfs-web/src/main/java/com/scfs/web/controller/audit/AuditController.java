package com.scfs.web.controller.audit;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.scfs.common.consts.BaseConsts;
import com.scfs.common.consts.BaseUrlConsts;
import com.scfs.common.exception.BaseException;
import com.scfs.domain.BaseReqDto;
import com.scfs.domain.BaseResult;
import com.scfs.domain.CodeValue;
import com.scfs.domain.audit.dto.req.AuditReqDto;
import com.scfs.domain.audit.dto.resp.AuditResDto;
import com.scfs.domain.audit.entity.Audit;
import com.scfs.domain.result.PageResult;
import com.scfs.domain.result.Result;
import com.scfs.service.audit.AuditService;
import com.scfs.service.common.CommonService;
import com.scfs.service.support.ServiceSupport;
import com.scfs.web.controller.BaseController;

/**
 * <pre>
 * 
 *  File: AuditController.java
 *  Description:
 *  TODO
 *  Date,					Who,				
 *  2016年11月17日				cuichao
 *
 * </pre>
 */
@RestController
public class AuditController extends BaseController {
	private final static Logger LOGGER = LoggerFactory.getLogger(AuditController.class);

	@Autowired
	AuditService auditService;
	@Autowired
	CommonService commonService;

	@RequestMapping(value = BaseUrlConsts.AUDIT_BATCH, method = RequestMethod.POST)
	@ResponseBody
	public BaseResult batchAudits(BaseReqDto baseReqDto, int checkAudit) {
		BaseResult result = new BaseResult();
		try {
			auditService.batchAudit(baseReqDto, checkAudit);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("批量审核单据信息异常[{}]", JSONObject.toJSON(baseReqDto), e);
			result.setMsg("批量审核单据信息异常，请稍后重试");
		}
		return result;
	}

	/** 查询信息 **/
	@RequestMapping(value = BaseUrlConsts.QUERYAUDITENTRY1, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditResDto> queryAudit1ByCond(AuditReqDto auditReqDto) {
		PageResult<AuditResDto> result = new PageResult<AuditResDto>();
		try {
			if (auditReqDto.getState() != null && auditReqDto.getState() == BaseConsts.THREE) {
				List<CodeValue> codeList = commonService.getAllCdByKey("USER_PROJECT");
				auditReqDto.setCodeList(codeList);
			} else {
				auditReqDto.setAuditorId(ServiceSupport.getUser().getId());
			}
			result = auditService.queryAuditResultsByCon(auditReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/** 微信端模糊查询信息 **/
	@RequestMapping(value = BaseUrlConsts.QUERY_AUDIT_ENTRY1_WECHAT, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditResDto> queryAudit1WechatByCond(AuditReqDto auditReqDto) {
		PageResult<AuditResDto> result = new PageResult<AuditResDto>();
		try {
			auditReqDto.setAuditorId(ServiceSupport.getUser().getId());
			result = auditService.queryAuditWechatResultsByCon(auditReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/** 查询信息 **/
	@RequestMapping(value = BaseUrlConsts.QUERYAUDITAUDITOR, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditResDto> queryAuditAuditorByAuditId(AuditReqDto auditReqDto) {
		PageResult<AuditResDto> result = new PageResult<AuditResDto>();
		try {
			auditReqDto.setAuditorId(ServiceSupport.getUser().getId());
			result = auditService.queryAuditResultsByCon(auditReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/** 查询信息 **/
	@RequestMapping(value = BaseUrlConsts.QUERYAUDITENTRY2, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditResDto> queryAudit2ByCond(AuditReqDto auditReqDto) {
		PageResult<AuditResDto> result = new PageResult<AuditResDto>();
		try {
			auditReqDto.setProposerId(ServiceSupport.getUser().getId());
			boolean isAllowPerm = ServiceSupport.isAllowPerm(BaseUrlConsts.QUERY_AUDIT_PROPOSER_ALL);
			if (isAllowPerm == true) {
				auditReqDto.setProposerId(null); // 可查看所有的申请单据
			}
			result = auditService.queryAuditResultsByCon(auditReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/** 查询信息 **/
	@RequestMapping(value = BaseUrlConsts.QUERYAUDITPROPOSER, method = RequestMethod.POST)
	@ResponseBody
	public PageResult<AuditResDto> queryAuditProposerByAuditId(AuditReqDto auditReqDto) {
		PageResult<AuditResDto> result = new PageResult<AuditResDto>();
		try {
			auditReqDto.setProposerId(ServiceSupport.getUser().getId());
			boolean isAllowPerm = ServiceSupport.isAllowPerm(BaseUrlConsts.QUERY_AUDIT_PROPOSER_ALL);
			if (isAllowPerm == true) {
				auditReqDto.setProposerId(null); // 可查看所有的申请单据
			}
			result = auditService.queryAuditResultsByCon(auditReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核单据信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	/**
	 * 根据审核ID查询审核记录
	 * 
	 * @param auditReqDto
	 *            id
	 * @return
	 */
	@RequestMapping(value = BaseUrlConsts.QUERYAUDITBYID, method = RequestMethod.POST)
	public Result<Audit> queryAuditById(AuditReqDto auditReqDto) {
		Result<Audit> result = new Result<Audit>();
		try {
			Audit vo = auditService.queryAuditByld(auditReqDto.getId());
			result.setItems(vo);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}

	@RequestMapping(value = BaseUrlConsts.QUERYAUDITBYNEXT, method = RequestMethod.POST)
	public Result<Audit> queryAuditNext(AuditReqDto auditReqDto) {
		Result<Audit> result = new Result<Audit>();
		try {
			auditReqDto.setAuditorId(ServiceSupport.getUser().getId());
			result = auditService.queryAuditResultsNext(auditReqDto);
		} catch (BaseException e) {
			result.setMsg(e.getMsg());
		} catch (Exception e) {
			LOGGER.error("查询审核信息异常[{}]", JSONObject.toJSON(auditReqDto), e);
			result.setSuccess(false);
			result.setMsg("查询异常，请稍后重试");
		}
		return result;
	}
}
